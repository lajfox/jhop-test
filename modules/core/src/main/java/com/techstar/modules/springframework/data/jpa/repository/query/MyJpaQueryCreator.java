package com.techstar.modules.springframework.data.jpa.repository.query;

import static org.springframework.data.jpa.repository.query.QueryUtils.applySorting;
import static org.springframework.data.jpa.repository.query.QueryUtils.createCountQueryFor;
import static org.springframework.data.jpa.repository.query.QueryUtils.getQueryString;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mapping.PropertyPath;
import org.springframework.data.repository.query.parser.Part;
import org.springframework.data.repository.query.parser.PartTree;
import org.springframework.data.repository.query.parser.PartTree.OrPart;
import org.springframework.util.Assert;

@Deprecated
public final class MyJpaQueryCreator<E> {

	private static final Logger log = LoggerFactory.getLogger(MyJpaQueryCreator.class);

	private final Class<E> enityClass;
	private final String propertyName;
	private final Object[] values;
	private final EntityManager em;

	private final StringBuilder querysb = new StringBuilder();

	public MyJpaQueryCreator(EntityManager em, Class<E> enityClass, String propertyName, Object... values) {
		this.em = em;
		this.enityClass = enityClass;
		this.propertyName = propertyName;
		this.values = values;

		PartTree tree = new PartTree(this.propertyName, this.enityClass);
		StringBuilder base = new StringBuilder("");
		int i = builder(tree, base);
		validate(i, this.values);

		querysb.append(getQueryString("select x from %s x", this.enityClass.getSimpleName()));
		where(querysb, base);
	}

	/**
	 * 查询记录总数Query
	 * 
	 * @param jql
	 * @param i
	 * @param values
	 * @return
	 */
	public TypedQuery<Long> getCountQuery() {

		String countJql = createCountQueryFor(querysb.toString());
		log.info("count jql:{}", countJql);

		TypedQuery<Long> query = em.createQuery(countJql, Long.class);
		applyParameters(query);

		return query;
	}

	/**
	 * 
	 * @param sort
	 * @return
	 */
	public Query getQuery(final Sort sort) {
		String jql = applySorting(querysb.toString(), sort);
		log.info("JQL:{}", jql);

		Query query = em.createQuery(jql);
		applyParameters(query);

		return query;
	}

	/**
	 * 
	 * @param pageable
	 * @return
	 */
	public Query getQuery(final Pageable pageable) {
		String jql = applySorting(querysb.toString(), pageable.getSort());
		log.info("JQL:{}", jql);

		Query query = em.createQuery(jql);
		applyParameters(query);

		applyPageable(query, pageable);

		return query;
	}

	/**
	 * 分页设置
	 * 
	 * @param query
	 * @param pageable
	 */
	private void applyPageable(final Query query, final Pageable pageable) {
		if (pageable != null) {
			query.setFirstResult(pageable.getOffset());
			query.setMaxResults(pageable.getPageSize());
		}
	}

	/**
	 * 设置参数
	 * 
	 * @param query
	 */
	private void applyParameters(final Query query) {
		if (ArrayUtils.isNotEmpty(values)) {
			for (int j = 1; j <= values.length; j++) {
				query.setParameter(j, values[j - 1]);
			}
		}
	}

	/**
	 * 增加where条件
	 * 
	 * @param sb
	 * @param base
	 */
	private void where(final StringBuilder sb, final StringBuilder base) {
		if (StringUtils.isNotEmpty(base)) {
			sb.append(" where ");
			sb.append(base);
		}
	}

	/**
	 * 验证参数
	 * 
	 * @param i
	 * @param values
	 */
	private void validate(final int i, final Object... values) {
		if (i > 1 && ArrayUtils.isNotEmpty(values) && i - 1 != values.length) {
			log.error("参数错误：{}", StringUtils.join(values, ","));
			throw new IllegalArgumentException("参数错误：" + StringUtils.join(values, ","));
		}
	}

	/**
	 * 给JQL增加and
	 * 
	 * @param criteria
	 */
	private void and(final StringBuilder criteria) {
		if (StringUtils.isNotEmpty(criteria)) {
			criteria.append(" and ");
		}
	}

	/**
	 * 根据PartTree构建JQL
	 * 
	 * @param tree
	 * @param base
	 * @return
	 */
	private int builder(final PartTree tree, final StringBuilder base) {

		Assert.notNull(tree, "参数 PartTree 不能为 null!");

		int i = 1;
		StringBuilder criteria = null;
		for (OrPart node : tree) {
			criteria = new StringBuilder("");
			for (Part part : node) {
				
				String segment = getSegment(part);

				switch (part.getType()) {
				case BETWEEN:
					and(criteria);
					criteria.append(" (x.").append(segment);
					criteria.append(" between ?").append((i++)).append(" and ?").append((i++)).append(")");
					break;
				case AFTER:					
				case GREATER_THAN:
					and(criteria);
					criteria.append(" x.").append(segment).append(">?").append((i++));
					break;
				case GREATER_THAN_EQUAL:
					and(criteria);
					criteria.append(" x.").append(segment).append(">=?").append((i++));
					break;
				case BEFORE:				
				case LESS_THAN:
					and(criteria);
					criteria.append(" x.").append(segment).append("<?").append((i++));
					break;
				case LESS_THAN_EQUAL:
					and(criteria);
					criteria.append(" x.").append(segment).append("<=?").append((i++));
					break;
				case IS_NULL:
					and(criteria);
					criteria.append(" x.").append(segment).append(" is null");
					break;
				case IS_NOT_NULL:
					and(criteria);
					criteria.append(" x.").append(segment).append(" is not null");
					break;
				case NOT_IN:
					and(criteria);
					criteria.append(" x.").append(segment).append(" not in (?").append((i++))
							.append(")");
					break;
				case IN:
					and(criteria);
					criteria.append(" x.").append(segment).append(" in (?").append((i++))
							.append(")");
					break;
				case STARTING_WITH:					
				case ENDING_WITH:					
				case CONTAINING:				
				case LIKE:
					and(criteria);
					criteria.append(" x.").append(segment).append(" like ?").append((i++));
					break;
				case NOT_LIKE:
					and(criteria);
					criteria.append(" x.").append(segment).append(" not like ?").append((i++));
					break;
				case TRUE:
					and(criteria);
					criteria.append(" x.").append(segment).append(" = 1");
					break;
				case FALSE:
					and(criteria);
					criteria.append(" x.").append(segment).append(" = 0");
					break;
				case SIMPLE_PROPERTY:
					and(criteria);
					criteria.append(" x.").append(segment).append("=?").append((i++));
					break;
				case NEGATING_SIMPLE_PROPERTY:
					and(criteria);
					criteria.append(" x.").append(segment).append("!=?").append((i++));
					break;
				default:
					throw new IllegalArgumentException("不支持的关键词: " + part.getType());
				}

			}
			base.append(criteria).append(" or ");
		}
		base.delete(base.length() - 3, base.length());

		return i;
	}

	private String getSegment(final Part part) {
		
		PropertyPath property = part.getProperty();
		String segment = property.getSegment();
		while (property.hasNext()) {
			property = property.next();
			segment += "." + property.getSegment();
		}
		return segment;
	}
}
