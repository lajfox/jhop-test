package com.techstar.modules.springframework.data.jpa.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.query.parser.PartTree;

import com.techstar.modules.springframework.data.jpa.repository.query.XPredicateCreator;


/**
 * spring data jpa 查询Specification工具类
 * 
 * @author sundoctor
 * 
 */
public final class SpecificationUtils {

	/**
	 * 增加查询条件
	 * 
	 * @param domainClass
	 *            业务实体类型
	 * @param spec
	 *            原有的查询条件
	 * @param cb
	 * @param source
	 *            　jpa查询规范条件，如:id,idIn,idLike等
	 * @param value
	 *            查询条件参数值
	 * @return Specification
	 */
	public static <T> Specification<T> and(final Specification<T> spec, final Root<T> root,
			final CriteriaQuery<?> query, final CriteriaBuilder cb, final String source, final Object value) {

		if (StringUtils.isEmpty(source)) {
			return spec;
		} else {
			Predicate predicate = toPredicate(root, query, cb, source, value);
			return and(spec, root, query, cb, predicate);
		}
	}

	/**
	 * 增加查询条件
	 * 
	 * @param domainClass
	 *            业务实体类型
	 * @param spec
	 *            原有的查询条件
	 * @param cb
	 * @param sourcesMap
	 *            　新增查询条件，key为jpa查询规范条件，如:id,idIn,idLike等,value为 查询条件参数值
	 * 
	 * @return Specification
	 */
	public static <T> Specification<T> and(final Specification<T> spec, final Root<T> root,
			final CriteriaQuery<?> query, final CriteriaBuilder cb, final Map<String, Object> sourcesMap) {

		if (MapUtils.isEmpty(sourcesMap)) {
			return spec;
		} else {
			int i = 0;
			Predicate[] predicates = new Predicate[sourcesMap.size()];
			Iterator<Entry<String, Object>> iterator = sourcesMap.entrySet().iterator();
			Entry<String, Object> entry = null;
			while (iterator.hasNext()) {
				entry = iterator.next();
				predicates[i] = toPredicate(root, query, cb, entry.getKey(), entry.getValue());
				i++;
			}

			return and(spec, root, query, cb, predicates);
		}
	}

	/**
	 * 增加查询条件
	 * 
	 * @param domainClass
	 *            业务实体类型
	 * @param spec
	 *            　原有的查询条件
	 * @param cb
	 * @param predicate
	 *            　新增的查询条件
	 * @return Specification
	 */
	public static <T> Specification<T> and(final Specification<T> spec, final Root<T> root,
			final CriteriaQuery<?> query, final CriteriaBuilder cb, final Predicate predicate) {
		return and(spec, root, query, cb, new Predicate[] { predicate });
	}

	/**
	 * 增加查询条件
	 * 
	 * @param domainClass
	 *            业务实体类型
	 * @param spec
	 *            　原有的查询条件
	 * @param cb
	 * @param predicates
	 *            　新增的查询条件
	 * @return Specification
	 */
	public static <T> Specification<T> and(final Specification<T> spec, final Root<T> root,
			final CriteriaQuery<?> query, final CriteriaBuilder cb, final Predicate... predicates) {

		final Predicate p = spec.toPredicate(root, query, cb);
		if (p == null) {
			return new Specification<T>() {

				@Override
				public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					return cb.and(predicates);
				}

			};

		} else {
			return new Specification<T>() {
				@Override
				public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					return cb.and(p, cb.and(predicates));
				}

			};
		}

	}

	private static <T> Predicate toPredicate(final Root<T> root, final CriteriaQuery<?> query,
			final CriteriaBuilder cb, final String source, final Object value) {
		List<Object> params = new ArrayList<Object>();
		params.add(value);

		PartTree tree = new PartTree(source, root.getJavaType());
		XPredicateCreator creator = new XPredicateCreator(tree, params, root, cb);
		return creator.createCriteria();

	}

}
