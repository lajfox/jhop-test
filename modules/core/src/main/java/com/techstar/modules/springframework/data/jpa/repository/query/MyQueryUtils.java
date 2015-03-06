package com.techstar.modules.springframework.data.jpa.repository.query;

import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static javax.persistence.metamodel.Attribute.PersistentAttributeType.MANY_TO_MANY;
import static javax.persistence.metamodel.Attribute.PersistentAttributeType.MANY_TO_ONE;
import static javax.persistence.metamodel.Attribute.PersistentAttributeType.ONE_TO_MANY;
import static javax.persistence.metamodel.Attribute.PersistentAttributeType.ONE_TO_ONE;
import static org.springframework.data.jpa.repository.query.QueryUtils.detectAlias;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.Bindable;
import javax.persistence.metamodel.Attribute.PersistentAttributeType;
import javax.persistence.metamodel.Bindable.BindableType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.data.mapping.PropertyPath;
import org.springframework.data.repository.query.parser.Part;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.techstar.modules.mybatis.util.SQLUtils;

public final class MyQueryUtils {

	private static final Logger logger = LoggerFactory.getLogger(MyQueryUtils.class);

	private static final Set<PersistentAttributeType> ASSOCIATION_TYPES;
	static {
		Set<PersistentAttributeType> persistentAttributeTypes = new HashSet<PersistentAttributeType>();
		persistentAttributeTypes.add(ONE_TO_ONE);
		persistentAttributeTypes.add(ONE_TO_MANY);
		persistentAttributeTypes.add(MANY_TO_ONE);
		persistentAttributeTypes.add(MANY_TO_MANY);
		ASSOCIATION_TYPES = Collections.unmodifiableSet(persistentAttributeTypes);
	}

	// 表连接，如select * from table1 a,table2 b where b.id = a.id ...
	private static final Pattern TABLE_PATTERN = Pattern.compile(
			"((\\s*[from]*\\s*[\\w]+)|(\\s*[\\(]+\\s*[\\w|\\W]+\\s*[\\)]+\\s*))((\\s+as\\s+)+|\\s+)(\\w*)\\s*[,]*\\s*",
			CASE_INSENSITIVE);

	// 表连接，如select * from table1 a left outer join table2 b on (b.id = a.id ...)
	// ...
	private static final Pattern JOIN_PATTERN = Pattern
			.compile(
					"(\\s*(inner|left|right)\\s+[outer]*\\s+join\\s+\\w+)((\\s+as\\s+)+|\\s+)(\\w*)(\\s*on\\s*[\\(]\\s*[\\w.\\s=><!]+([\\)]\\s*))",
					CASE_INSENSITIVE);

	private MyQueryUtils() {

	}

	/**
	 * 设置参数
	 * 
	 * @param query
	 */
	public static void applyParameters(final Query query, final Object... values) {
		if (ArrayUtils.isNotEmpty(values)) {
			int i = 1;
			for (Object param : values) {
				query.setParameter(i, param);
				i++;
			}
		}
	}

	public static void applyParameters(final Query query, final int index, final Object... values) {
		if (ArrayUtils.isNotEmpty(values)) {
			int i = index + 1;
			for (Object param : values) {
				query.setParameter(i, param);
				i++;
			}
		}
	}

	/**
	 * 设置参数
	 * 
	 * @param query
	 */
	public static void applyParameters(final Query query, Map<String, Object> values) {
		if (MapUtils.isNotEmpty(values)) {

			Set<Entry<String, Object>> set = values.entrySet();
			Iterator<Entry<String, Object>> iterator = set.iterator();
			Entry<String, Object> entry = null;
			while (iterator.hasNext()) {
				entry = iterator.next();
				query.setParameter(entry.getKey(), entry.getValue());
			}
		}
	}

	public static <E> E getOne(final List<E> list) {
		if (CollectionUtils.isEmpty(list)) {
			return null;
		}

		if (list.size() > 1) {
			logger.error("结果集记录不唯一，实际大小为：{}", list.size());
			throw new IllegalArgumentException("结果集记录不唯一，实际大小为：" + list.size());
		}

		return list.get(0);
	}

	/**
	 * 分页设置
	 * 
	 * @param query
	 * @param pageable
	 */
	public static void applyPageable(final Query query, final Pageable pageable) {
		if (pageable != null) {
			query.setFirstResult(pageable.getOffset());
			query.setMaxResults(pageable.getPageSize());
		}
	}

	public static <E> Query applyAndBind(final String idAttributeName, String queryString,
			final Iterable<? extends Serializable> ids, EntityManager entityManager) {

		Assert.notNull(queryString);
		Assert.notNull(ids);
		Assert.notNull(entityManager);

		Iterator<? extends Serializable> iterator = ids.iterator();

		if (!iterator.hasNext()) {
			return entityManager.createQuery(queryString);
		}

		String alias = detectAlias(queryString);
		StringBuilder builder = new StringBuilder(queryString);
		builder.append(" where ");

		// JpaEntityInformation<E, ?> jpaEntityInformation =
		// JpaMetamodelEntityInformation.getMetadata(enityClass, entityManager);
		// String idAttributeName =
		// jpaEntityInformation.getIdAttribute().getName();
		int i = 0;
		while (iterator.hasNext()) {

			iterator.next();

			builder.append(String.format(" %s = ?%d", alias + "." + idAttributeName, ++i));

			if (iterator.hasNext()) {
				builder.append(" or");
			}
		}

		Query query = entityManager.createQuery(builder.toString());

		iterator = ids.iterator();
		i = 0;
		while (iterator.hasNext()) {
			query.setParameter(++i, iterator.next());
		}

		return query;
	}

	/**
	 * Creates a count projected query from the given orginal query.
	 * 
	 * @param originalQuery
	 *            must not be {@literal null} or empty
	 * @return
	 */
	public static String createCountQueryFor(String originalQuery) {

		Assert.hasText(originalQuery);

		// 如果SELECT 中包含 DISTINCT 只能在外层包含COUNT
		if (SQLUtils.hasGroupByORDistinct(originalQuery)) {
			return new StringBuilder(originalQuery.length() + 50).append("select count(1) from as coun (")
					.append(originalQuery).append(") t").toString();
		} else {
			return QueryUtils.createCountQueryFor(originalQuery);
		}
	}

	/**
	 * 增加查询排序
	 * 
	 * @param query
	 * @param sort
	 * @param alias
	 * @return
	 * @see {@link QueryUtils#applySorting(String, Sort, String)}
	 */
	public static String applySorting(String query, Sort sort, String alias) {

		Assert.hasText(query);

		if (null == sort || !sort.iterator().hasNext()) {
			return query;
		}

		StringBuilder builder = new StringBuilder(query);

		if (!query.contains("order by")) {
			builder.append(" order by ");
		} else {
			builder.append(", ");
		}

		Set<String> joinAliases = getJoinAliases(query);
		for (Order order : sort) {
			builder.append(getOrderClause(joinAliases, alias, order));
		}

		builder.delete(builder.length() - 2, builder.length());

		return builder.toString();
	}

	/**
	 * 
	 * @param alias
	 *            the alias for the root entity.
	 * @param order
	 *            the order object to build the clause for.
	 * @return
	 * @see {@link QueryUtils#getOrderClause(String,Order)}
	 */
	private static String getOrderClause(Set<String> joinAliases, String alias, Order order) {

		String property = order.getProperty();
		boolean qualifyReference = !property.contains("("); // ( indicates a
															// function
		if (StringUtils.hasText(alias)) {
			for (String joinAlias : joinAliases) {
				if (property.startsWith(joinAlias + ".")) {
					qualifyReference = false;
					break;
				}
			}

			if (property.startsWith(alias + ".")) {
				qualifyReference = false;
			}
		}else{
			qualifyReference = false;
		}

		return String.format("%s%s %s, ", qualifyReference ? alias + "." : "", property, toJpaDirection(order));
	}

	/**
	 * 
	 * @param order
	 * @return
	 * @see {@link QueryUtils#toJpaDirection(Order)}
	 */
	private static String toJpaDirection(Order order) {
		return order.getDirection().name().toLowerCase(Locale.US);
	}

	/**
	 * 获取sql中的表别名
	 * 
	 * @param query
	 * @return
	 */
	public static Set<String> getJoinAliases(final String query) {

		Set<String> result = new HashSet<String>();

		int start = SQLUtils.getAfterFormInsertPoint(query);
		int end = SQLUtils.getAfterWhereInsertPoint(query);

		String sql = end <= 0 ? query.substring(start) : query.substring(start, end);

		if (StringUtils.hasText(sql)) {
			String alias = null;
			StringBuffer sb = new StringBuffer();

			// 先获取sql中的inner|left|right join ... on (...)别名
			Matcher matcher = JOIN_PATTERN.matcher(sql);
			while (matcher.find()) {
				matcher.appendReplacement(sb, "");// 删除sql中的inner|left|right
													// join ... on (...)
				alias = matcher.group(5);
				if (StringUtils.hasText(alias)) {
					result.add(alias);
				}
			}
			matcher.appendTail(sb);

			//
			matcher = TABLE_PATTERN.matcher(sb.toString());
			while (matcher.find()) {
				alias = matcher.group(6);
				if (StringUtils.hasText(alias)) {
					result.add(alias);
				}
			}
		}

		return result;
	}

	/**
	 * Returns a path to a {@link Comparable}.
	 * 
	 * @param root
	 * @param part
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Expression<? extends Comparable> getComparablePath(Root<?> root, Part part) {

		return getTypedPath(root, part);
	}

	public static <T> Expression<T> getTypedPath(Root<?> root, Part part) {
		return toExpressionRecursively(root, part.getProperty());
	}

	/**
	 * {@link QueryUtils#toExpressionRecursively(From, PropertyPath )  }
	 * 
	 * @param from
	 * @param property
	 * @return
	 */
	@SuppressWarnings("unchecked")
	static <T> Expression<T> toExpressionRecursively(From<?, ?> from, PropertyPath property) {

		Path<Object> path = from.get(property.getSegment());

		if (property.isCollection() || isEntityPath(path)) {
			Join<Object, Object> join = from.join(property.getSegment(), JoinType.LEFT);
			return (Expression<T>) (property.hasNext() ? toExpressionRecursively((From<?, ?>) join, property.next())
					: join);
		} else {
			return (Expression<T>) (property.hasNext() ? toExpressionRecursively(path, property.next()) : path);
		}
	}

	/**
	 * Returns whether the given path can be considered referring an entity.
	 * {@link QueryUtils#isEntityPath(Path) }
	 * 
	 * @param path
	 *            must not be {@literal null}.
	 * @return
	 */
	private static boolean isEntityPath(Path<?> path) {

		Bindable<?> model = path.getModel();

		if (BindableType.ENTITY_TYPE.equals(model.getBindableType())) {
			return true;
		}

		if (model instanceof Attribute) {

			Attribute<?, ?> attribute = (Attribute<?, ?>) model;

			if (attribute.isAssociation()) {
				return true;
			}

			return ASSOCIATION_TYPES.contains(attribute.getPersistentAttributeType());
		}

		return false;
	}

	/**
	 * {@link QueryUtils#toExpressionRecursively(Path, PropertyPath ) }
	 * 
	 * @param path
	 * @param property
	 * @return *
	 */
	static Expression<Object> toExpressionRecursively(Path<Object> path, PropertyPath property) {

		Path<Object> result = path.get(property.getSegment());
		return property.hasNext() ? toExpressionRecursively(result, property.next()) : result;
	}

}
