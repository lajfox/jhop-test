package com.techstar.modules.springframework.web;

import java.beans.PropertyEditorSupport;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import javax.persistence.metamodel.SingularAttribute;
import javax.servlet.ServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.core.MethodParameter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.query.parser.PartTree;
import org.springframework.util.Assert;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.bind.support.WebRequestDataBinder;
import org.springframework.web.context.request.NativeWebRequest;

import com.techstar.modules.commons.lang3.time.DateUtils;
import com.techstar.modules.jackson.mapper.JsonMapper;
import com.techstar.modules.springframework.data.jpa.domain.Filters;
import com.techstar.modules.springframework.data.jpa.domain.Rule;
import com.techstar.modules.springframework.data.jpa.domain.SingleSearch;
import com.techstar.modules.springframework.data.jpa.repository.query.PredicateCreator;
import com.techstar.modules.springframework.data.jpa.repository.query.XPredicateCreator;
import com.techstar.modules.springframework.web.util.WebUtils;

public class JqgridSpecificationArgumentResolver implements WebArgumentResolver {

	protected EntityManager em;

	public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
		em = entityManagerFactory.createEntityManager();
	}

	// 实体类的属性
	@SuppressWarnings({ "rawtypes" })
	private static final Map<Class<?>, List<SingularAttribute>> attributesMap = new HashMap<Class<?>, List<SingularAttribute>>();

	private static final JsonMapper BINDER = JsonMapper.nonDefaultMapper();

	private static final Map<String, String> OPMAP = new HashMap<String, String>();
	static {
		// ['eq','ne','lt','le','gt','ge','bw','bn','in','ni','ew','en','cn','nc']
		// ['equal','not equal', 'less', 'less or equal','greater','greater or
		// equal',
		// 'begins with','does not begin with','is in','is not in','ends with',
		// 'does not end with','contains','does not contain']
		OPMAP.put("eq", "");
		OPMAP.put("ne", "Not");
		OPMAP.put("lt", "LessThan");
		OPMAP.put("le", "LessThanEqual");
		OPMAP.put("gt", "GreaterThan");
		OPMAP.put("ge", "GreaterThanEqual");
		OPMAP.put("bw", "StartingWith");
		OPMAP.put("bn", "NotLike");
		OPMAP.put("in", "In");
		OPMAP.put("ni", "NotIn");
		OPMAP.put("ew", "EndingWith");
		OPMAP.put("en", "NotLike");
		OPMAP.put("cn", "Containing");
		OPMAP.put("nc", "NotLike");
		OPMAP.put("nu", "Null");
		OPMAP.put("nn", "NotNull");
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object resolveArgument(MethodParameter methodParameter, NativeWebRequest webRequest) throws Exception {

		if (methodParameter.getParameterType().equals(Specification.class)) {

			ParameterizedType type = (ParameterizedType) methodParameter.getGenericParameterType();
			Type types[] = type.getActualTypeArguments();

			Assert.notEmpty(types, "参数错误，org.springframework.data.jpa.domain.Specification没有指定泛型参数");

			// 实体类型
			Class<?> domainClass = (Class<?>) types[0];

			SingleSearch singleSearch = new SingleSearch();
			WebRequestDataBinder binder = new WebRequestDataBinder(singleSearch);
			binder.bind(webRequest);

			// 自定义扩展默认查询
			// 按Spring data jpa规范编写，sDs_为固定前缀，多个参数查询其参数格式必须为json数组，如
			// url?sDs_name=张三
			// url?sDs_name=["张三"]
			// url?sDs_teamId=1
			// url?sDs_team.id=1
			// url?sDs_createtimeBetween=["2012-1-1","2012-3-12"]
			// url?sDs_nameIn=["张三","李四","黄五"]
			// url?sDs_nameAndLoginNameLike=["张三","ad"]
			// url?sDs_nameOrLoginNameNull=["张三"]
			// url?sDs_nameOrLoginNameNull=["张三"]&sDs_status=disabled
			ServletRequest servletRequest = webRequest.getNativeRequest(ServletRequest.class);
			Map<String, Object> defSearchMap = WebUtils.getParametersStartingWith(servletRequest, "sDs_");

			// 集成工具栏搜索
			Object target = instantiateClass(domainClass, webRequest);
			List<SingularAttribute> attributes = getAttributes(domainClass);
			Map<String, Object> toolbarMap = buildSearch(servletRequest, attributes, target);

			// 高级搜索multipleSearch:true
			String filters = servletRequest.getParameter("filters");
			Filters filter = this.fromJson(filters);

			return new MySpecification(domainClass, singleSearch, defSearchMap, toolbarMap, filter, methodParameter);
		}

		return UNRESOLVED;
	}

	/**
	 * 根据实体参数构建查询条件
	 * 
	 * @param defSearchMap
	 * @param servletRequest
	 * @param attributes
	 * @param target
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	@SuppressWarnings({ "rawtypes"})
	private Map<String, Object> buildSearch(final ServletRequest servletRequest,
			final List<SingularAttribute> attributes, final Object target) throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		Class<?> type = null;
		Object value = null;
		String element = null;
		Map<String, Object> toolbarMap = new HashMap<String, Object>();

		Enumeration<String> enu = servletRequest.getParameterNames();
		while (enu.hasMoreElements()) {
			element = enu.nextElement();
			for (SingularAttribute attribute : attributes) {
				if (StringUtils.equals(attribute.getName(), element)) {
					type = attribute.getJavaType();
					value = PropertyUtils.getProperty(target, attribute.getName());
					if (value != null) {
						if (String.class.isAssignableFrom(type)) {
							toolbarMap.put(attribute.getName() + "Like", value);
						} else {
							toolbarMap.put(attribute.getName(), value);
						}
					}
				}
			}
		}

		return toolbarMap;
	}

	/**
	 * 初始化实体并绑定参数
	 * 
	 * @param domainClass
	 * @param webRequest
	 * @return
	 */
	private Object instantiateClass(final Class<?> domainClass, final NativeWebRequest webRequest) {
		Object target = BeanUtils.instantiateClass(domainClass);
		WebRequestDataBinder binder = new WebRequestDataBinder(target);
		binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
			public void setAsText(String value) {
				setValue(DateUtils.parseDate(value, false));
			}

			public String getAsText() {
				if (getValue() != null) {
					return DateFormatUtils.format((Date) getValue(), "yyyy-MM-dd HH:mm:ss");
				} else {
					return null;
				}
			}
		});
		binder.bind(webRequest);
		return target;
	}

	/**
	 * 获取实体类的SingularAttribute
	 * 
	 * @param domainClass
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	private List<SingularAttribute> getAttributes(Class<?> domainClass) {

		List<SingularAttribute> attributes = attributesMap.get(domainClass);
		if (CollectionUtils.isEmpty(attributes)) {

			attributes = new ArrayList<SingularAttribute>();

			Metamodel model = em.getMetamodel();
			EntityType<?> entity = model.entity(domainClass);
			Set set = entity.getSingularAttributes();
			if (CollectionUtils.isNotEmpty(set)) {
				Iterator iterator = set.iterator();
				while (iterator.hasNext()) {
					SingularAttribute attribute = (SingularAttribute) iterator.next();
					attributes.add(attribute);
				}
			}

			attributesMap.put(domainClass, attributes);
		}
		return attributes;

	}

	private Filters fromJson(final String filters) {
		if (StringUtils.isEmpty(filters)) {
			return null;
		} else {
			return BINDER.fromJson(filters, Filters.class);
		}
	}

	private static class MySpecification<T> implements Specification<T> {

		private final Class<?> domainClass;
		private final SingleSearch singleSearch;
		private final Filters filters;
		private final Map<String, Object> defSearchMap;
		private final Map<String, Object> toolbarMap;
		private final MethodParameter methodParameter;

		public MySpecification(Class<?> domainClass, SingleSearch singleSearch, Map<String, Object> defSearchMap,
				Map<String, Object> toolbarMap, Filters filters, MethodParameter methodParameter) {
			this.domainClass = domainClass;
			this.singleSearch = singleSearch;
			this.filters = filters;
			this.defSearchMap = defSearchMap;
			this.toolbarMap = toolbarMap;
			this.methodParameter = methodParameter;
		}

		@Override
		public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

			Predicate defaultPredicate = buildDefaultPredicate(root, cb);
			Predicate singlePredicate = buildSinglePredicate(root, cb);
			Predicate toolbarPredicate = buildToolbarPredicate(root, cb);
			Predicate annotationPredicate = buildAnnotationPredicate(methodParameter, root, cb);
			Predicate advancedPredicate = buildAdvancedPredicate(root, cb, this.filters);

			return toPredicate(cb, defaultPredicate, singlePredicate, toolbarPredicate, annotationPredicate,
					advancedPredicate);

		}

		private Predicate toPredicate(CriteriaBuilder cb, Predicate... predicates) {

			if (ArrayUtils.isEmpty(predicates)) {
				return null;
			}

			List<Predicate> ps = new ArrayList<Predicate>();
			for (Predicate p : predicates) {
				if (p != null) {
					ps.add(p);
				}
			}

			if (CollectionUtils.isEmpty(ps)) {
				return null;
			}

			if (ps.size() == 1) {
				return ps.get(0);
			}

			return cb.and(ps.toArray(new Predicate[ps.size()]));
		}

		/**
		 * 构建高级查询多条件搜索 multipleSearch:true
		 * 
		 * @param bean
		 * @param root
		 * @param cb
		 * @return
		 */
		private Predicate buildAdvancedPredicate(Root<T> root, CriteriaBuilder cb, Filters filters) {
			Predicate predicate = null;
			if (filters != null) {

				List<String> fileds = null;
				List<String> params = null;
				StringBuilder sb = null;
				List<Rule> rules = filters.getRules();
				if (CollectionUtils.isNotEmpty(rules)) {
					fileds = new ArrayList<String>(rules.size());
					params = new ArrayList<String>(rules.size());
					int i = 0;
					for (Rule rule : rules) {
						if (this.hasPredicate(rule.getOp(), rule.getData())) {
							
							sb = new StringBuilder();
							sb.append((i == 0 ? rule.getField() : StringUtils.capitalize(rule.getField())));
							sb.append(OPMAP.get(rule.getOp()));
							
							if(StringUtils.equals("ne", rule.getOp())){
								sb.append("Or");
								sb.append(StringUtils.capitalize(rule.getField()));
								sb.append(OPMAP.get("nu"));
							}
							
							fileds.add(sb.toString());
						}
						if (!StringUtils.equals(rule.getOp(), "nu") && !StringUtils.equals(rule.getOp(), "nn")
								&& StringUtils.isNotEmpty(rule.getData())) {
							params.add(rule.getData());
						}
						i++;
					}
				}

				if (CollectionUtils.isNotEmpty(fileds)) {
					PartTree tree = new PartTree(StringUtils.join(fileds,
							StringUtils.capitalize(filters.getGroupOp().toLowerCase())), domainClass);
					PredicateCreator creator = new PredicateCreator(tree, params, root, cb);
					predicate = creator.createCriteria();
				}

				List<Filters> groups = filters.getGroups();
				if (CollectionUtils.isNotEmpty(groups)) {
					for (Filters fs : groups) {
						if (StringUtils.equalsIgnoreCase(filters.getGroupOp(), "and")) {
							predicate = cb.and(buildAdvancedPredicate(root, cb, fs), predicate);
						} else {
							predicate = cb.or(buildAdvancedPredicate(root, cb, fs), predicate);
						}
					}
				}
			}

			return predicate;
		}

		/**
		 * 构建单条件搜索查询
		 * 
		 * @param bean
		 * @param root
		 * @param cb
		 * @return
		 */
		private Predicate buildSinglePredicate(Root<T> root, CriteriaBuilder cb) {
			Predicate predicate = null;
			if (singleSearch != null && StringUtils.isNotEmpty(singleSearch.getSearchField())) {
				if (hasPredicate(singleSearch.getSearchOper(), singleSearch.getSearchString())) {
					
					StringBuilder sb = new StringBuilder();
					sb.append(singleSearch.getSearchField());
					sb.append(OPMAP.get(singleSearch.getSearchOper()));

					if (StringUtils.equals("ne", singleSearch.getSearchOper())) {
						sb.append("Or").append(StringUtils.capitalize(singleSearch.getSearchField()));
						sb.append(OPMAP.get("nu"));						
					}

					PartTree tree = new PartTree(sb.toString(), domainClass);
					PredicateCreator creator = new PredicateCreator(tree, singleSearch.getSearchString(), root, cb);
					predicate = creator.createCriteria();
				}
			}

			return predicate;
		}

		private Predicate buildToolbarPredicate(Root<T> root, CriteriaBuilder cb) {

			Predicate base = null;
			if (MapUtils.isNotEmpty(toolbarMap)) {
				Entry<String, Object> entry = null;
				Set<Entry<String, Object>> set = toolbarMap.entrySet();
				Iterator<Entry<String, Object>> iterator = set.iterator();
				while (iterator.hasNext()) {
					entry = iterator.next();
					base = base == null ? create(root, cb, entry.getKey(), entry.getValue()) : and(base, root, cb,
							entry.getKey(), entry.getValue());

				}
			}

			return base;
		}

		/**
		 * 构建扩展的默认查询条件
		 * 
		 * @param bean
		 * @param root
		 * @param cb
		 * @return
		 */
		private Predicate buildDefaultPredicate(Root<T> root, CriteriaBuilder cb) {

			Predicate base = null;
			if (MapUtils.isNotEmpty(defSearchMap)) {
				Entry<String, Object> entry = null;
				Set<Entry<String, Object>> set = defSearchMap.entrySet();
				Iterator<Entry<String, Object>> iterator = set.iterator();
				while (iterator.hasNext()) {
					entry = iterator.next();
					base = base == null ? create(root, cb, entry.getKey(),
							MapUtils.getString(defSearchMap, entry.getKey())) : and(base, root, cb, entry.getKey(),
							MapUtils.getString(defSearchMap, entry.getKey()));

				}
			}

			return base;
		}

		private Predicate buildAnnotationPredicate(MethodParameter methodParameter, Root<?> root, CriteriaBuilder cb) {

			Predicate predicate = null;
			// search for SpecificationDefaults annotation
			for (Annotation annotation : methodParameter.getParameterAnnotations()) {
				if (annotation instanceof SpecificationDefaults) {

					SpecificationDefaults defaults = (SpecificationDefaults) annotation;
					if (defaults != null) {
						String[] values = defaults.values();
						PartTree tree = new PartTree(defaults.name(), domainClass);
						PredicateCreator creator = new PredicateCreator(tree, values, root, cb);
						predicate = creator.createCriteria();
					}
				}

			}

			return predicate;
		}

		private Predicate create(Root<T> root, CriteriaBuilder cb, String key, String value) {
			return toPredicate(root, cb, key, value);
		}

		private Predicate create(Root<T> root, CriteriaBuilder cb, String key, Object value) {
			return toPredicate(root, cb, key, value);
		}

		private Predicate and(Predicate base, Root<T> root, CriteriaBuilder cb, String key, String value) {
			return cb.and(base, toPredicate(root, cb, key, value));
		}

		private Predicate and(Predicate base, Root<T> root, CriteriaBuilder cb, String key, Object value) {
			return cb.and(base, toPredicate(root, cb, key, value));
		}

		private Predicate toPredicate(Root<T> root, CriteriaBuilder cb, String key, String value) {
			Predicate predicate = null;

			String values[] = null;
			if (StringUtils.isNotEmpty(value)) {
				if (value.startsWith("[") && value.endsWith("]") && !key.endsWith("In")) {
					values = BINDER.fromJson(value, String[].class);
				} else {
					values = new String[1];
					values[0] = value;
				}
			}

			// if (ArrayUtils.isNotEmpty(values)) {
			PartTree tree = new PartTree(key, domainClass);
			PredicateCreator creator = new PredicateCreator(tree, values, root, cb);
			predicate = creator.createCriteria();
			// }

			return predicate;
		}

		private Predicate toPredicate(Root<T> root, CriteriaBuilder cb, String key, Object value) {
			Predicate predicate = null;
			Object values[] = null;
			if (value != null) {
				values = new Object[1];
				values[0] = value;
			}
			if (ArrayUtils.isNotEmpty(values)) {
				PartTree tree = new PartTree(key, domainClass);
				XPredicateCreator creator = new XPredicateCreator(tree, values, root, cb);
				predicate = creator.createCriteria();
			}
			return predicate;
		}

		private boolean hasPredicate(final String op, final String value) {
			return (StringUtils.equals("nu", op) || StringUtils.equals("nn", op))
					|| (!StringUtils.equals("nu", op) && !StringUtils.equals("nn", op) && StringUtils.isNotEmpty(value));
		}

	}

}
