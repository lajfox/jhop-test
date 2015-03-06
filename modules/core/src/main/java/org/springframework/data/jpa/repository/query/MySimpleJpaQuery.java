package org.springframework.data.jpa.repository.query;

import static com.techstar.modules.mybatis.util.SQLUtils.getAfterFormInsertPoint;
import static com.techstar.modules.mybatis.util.SQLUtils.hasGroupByORDistinct;
import static com.techstar.modules.mybatis.util.SQLUtils.hasOrderBy;
import static com.techstar.modules.mybatis.util.SQLUtils.removeOrders;
import static com.techstar.modules.springframework.data.jpa.repository.query.MyQueryUtils.applyParameters;
import static com.techstar.modules.springframework.data.jpa.repository.query.MyQueryUtils.applySorting;
import static com.techstar.modules.springframework.data.jpa.repository.query.MyQueryUtils.createCountQueryFor;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.hibernate.ejb.HibernateQuery;
import org.hibernate.transform.ResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.query.JpaQueryExecution.CollectionExecution;
import org.springframework.data.jpa.repository.query.JpaQueryExecution.ModifyingExecution;
import org.springframework.data.jpa.repository.query.JpaQueryExecution.SingleEntityExecution;
import org.springframework.data.repository.query.Parameter;
import org.springframework.data.repository.query.ParameterAccessor;
import org.springframework.data.repository.query.Parameters;
import org.springframework.data.repository.query.ParametersParameterAccessor;
import org.springframework.data.repository.query.RepositoryQuery;
import org.springframework.util.StringUtils;

import com.techstar.modules.hibernate.transform.MyAliasToEntityMapResultTransformer;
import com.techstar.modules.springframework.data.jpa.domain.QuerySpecification;
import com.techstar.modules.springframework.data.jpa.repository.query.QLCreator;

/**
 * {@link RepositoryQuery} implementation that inspects a
 * {@link org.springframework.data.repository.query.QueryMethod} for the
 * existanve of an {@link org.springframework.data.jpa.repository.Query}
 * annotation and creates a JPA {@link Query} from it.
 * 
 * @author ZengWenfeng
 */
final class MySimpleJpaQuery extends AbstractJpaQuery {

	private static final Logger LOG = LoggerFactory.getLogger(MySimpleJpaQuery.class);

	private static final Pattern JQPLPATTERN = Pattern.compile(
			"\\s*select\\s+new\\s+[map|list]+\\s*\\(\\s*[\\w|\\W|\\s|\\S]+\\s*\\)\\s*from", Pattern.CASE_INSENSITIVE);

	private final String queryString;
	private final String countQuery;
	private final String alias;

	private final JpaQueryMethod method;

	/**
	 * Creates a new {@link SimpleJpaQuery} that encapsulates a simple query
	 * string.
	 */
	MySimpleJpaQuery(JpaQueryMethod method, EntityManager em, String queryString) {

		super(method, em);
		this.method = method;
		this.queryString = queryString;
		this.alias = QueryUtils.detectAlias(queryString);
		this.countQuery = method.getCountQuery() == null ? queryString : method.getCountQuery();
	}

	/**
	 * Creates a new {@link SimpleJpaQuery} encapsulating the query annotated on
	 * the given {@link JpaQueryMethod}.
	 */
	MySimpleJpaQuery(JpaQueryMethod method, EntityManager em) {
		this(method, em, method.getAnnotatedQuery());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.data.jpa.repository.query.AbstractJpaQuery#createQuery
	 * (java.lang.Object[])
	 */
	@Override
	public Query doCreateQuery(Object[] values) {

		ParameterAccessor accessor = new ParametersParameterAccessor(method.getParameters(), values);
		String sortedQueryString = applySorting(queryString, accessor.getSort(), alias);

		EntityManager em = getEntityManager();
		Query query = null;
		QuerySpecification spec = getQuerySpecification(values);
		if (spec != null) {
			boolean isNamedParameter = this.isNamedParameter();
			if (isNamedParameter) {
				QLCreator<Map<String, Object>> creator = spec.build(method.isNativeQuery(), isNamedParameter,
						method.getCountQuery(), sortedQueryString);
				query = getQuery(em, creator.getQuery(), false);
				query = createBinder(values).bindAndPrepare(query);
				applyParameters(query, creator.getValues());
			} else {
				QLCreator<Object[]> creator = spec.build(method.isNativeQuery(), isNamedParameter,
						method.getCountQuery(), sortedQueryString);
				query = getQuery(em, creator.getQuery(), false);
				query = createBinder(values).bindAndPrepare(query);
				int idx = this.getParameterIndex();
				applyParameters(query, idx, creator.getValues());
			}

		} else {
			query = getQuery(em, sortedQueryString, false);
			query = createBinder(values).bindAndPrepare(query);
		}

		setResultTransformer(query, values);

		return query;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.data.jpa.repository.query.AbstractJpaQuery#
	 * doCreateCountQuery(java.lang.Object[])
	 */
	@Override
	protected TypedQuery<Long> doCreateCountQuery(Object[] values) {
		return createBinder(values).bind(getEntityManager().createQuery(countQuery, Long.class));
	}

	public Query getCreateCountQuery(Object[] values) {
		Query query = null;
		QuerySpecification spec = getQuerySpecification(values);
		if (spec == null) {
			query = getQuery(getEntityManager(),
					StringUtils.isEmpty(method.getCountQuery()) ? getCountQuery(countQuery) : countQuery, true);
			query = createBinder(values).bind(query);
		} else {
			boolean isNamedParameter = this.isNamedParameter();
			if (isNamedParameter) {
				QLCreator<Map<String, Object>> creator = spec.build(method.isNativeQuery(), isNamedParameter,
						method.getCountQuery(), countQuery);

				query = getQuery(
						getEntityManager(),
						StringUtils.isEmpty(method.getCountQuery()) ? getCountQuery(creator.getQuery()) : creator
								.getQuery(), true);
				query = createBinder(values).bind(query);
				applyParameters(query, creator.getValues());
			} else {
				QLCreator<Object[]> creator = spec.build(method.isNativeQuery(), isNamedParameter,
						method.getCountQuery(), countQuery);

				query = getQuery(
						getEntityManager(),
						StringUtils.isEmpty(method.getCountQuery()) ? getCountQuery(creator.getQuery()) : creator
								.getQuery(), true);
				query = createBinder(values).bind(query);
				int idx = this.getParameterIndex();
				applyParameters(query, idx, creator.getValues());
			}
		}

		return query;
	}

	@Override
	protected ParameterBinder createBinder(Object[] values) {
		return new com.techstar.modules.springframework.data.jpa.repository.query.ParameterBinder(
				method.getParameters(), values);
	}

	/**
	 * Creates a {@link RepositoryQuery} from the given
	 * {@link org.springframework.data.repository.query.QueryMethod} that is
	 * potentially annotated with
	 * {@link org.springframework.data.jpa.repository.Query}.
	 * 
	 * @param queryMethod
	 * @param em
	 * @return the {@link RepositoryQuery} derived from the annotation or
	 *         {@code null} if no annotation found.
	 */
	public static RepositoryQuery fromQueryAnnotation(JpaQueryMethod queryMethod, EntityManager em) {
		LOG.debug("Looking up query for method {}", queryMethod.getName());
		String query = queryMethod.getAnnotatedQuery();
		return query == null ? null : new MySimpleJpaQuery(queryMethod, em, query);
	}

	@Override
	public Object execute(Object[] parameters) {
		return getExecution().execute(this, parameters);
	}

	@Override
	protected JpaQueryExecution getExecution() {

		if (method.isCollectionQuery()) {
			return new CollectionExecution();
		} else if (method.isPageQuery()) {
			return new org.springframework.data.jpa.repository.query.PagedExecution(method.getParameters());
		} else if (method.isModifyingQuery()) {
			return method.getClearAutomatically() ? new ModifyingExecution(method, getEntityManager())
					: new ModifyingExecution(method, null);
		} else {
			return new SingleEntityExecution();
		}
	}

	private Query getQuery(final EntityManager em, final String queryString, final boolean isCount) {
		Query query = null;
		if (method.isNativeQuery()) {
			query = (method.isQueryForEntity() && !isCount) ? em.createNativeQuery(queryString,
					method.getReturnedObjectType()) : em.createNativeQuery(queryString);
		} else {
			query = em.createQuery(queryString);
		}
		return query;
	}

	/**
	 * 获取QuerySpecification参数
	 * 
	 * @param values
	 * @return
	 */
	private QuerySpecification getQuerySpecification(final Object[] values) {
		QuerySpecification spec = null;
		for (Object value : values) {
			if (value instanceof QuerySpecification) {
				spec = (QuerySpecification) value;
				break;
			}
		}

		return spec;
	}

	private void setResultTransformer(final Query query, final Object values[]) {
		if (query instanceof HibernateQuery) {
			ResultTransformer transformer = getResultTransformer(values);
			if (transformer != null) {
				query.unwrap(HibernateQuery.class).getHibernateQuery().setResultTransformer(transformer);
			} else {
				if (Map.class.isAssignableFrom(method.getReturnedObjectType()) && !matcher()) {
					query.unwrap(HibernateQuery.class).getHibernateQuery()
							.setResultTransformer(MyAliasToEntityMapResultTransformer.INSTANCE);
					/*
					 * ((HibernateQuery)
					 * query).getHibernateQuery().setResultTransformer(
					 * MyAliasToEntityMapResultTransformer.INSTANCE);
					 */
				}
			}
		}
	}

	/**
	 * 获取ResultTransformer参数
	 * 
	 * @param values
	 * @return
	 */
	private ResultTransformer getResultTransformer(final Object[] values) {
		ResultTransformer transformer = null;
		for (Object value : values) {
			if (value instanceof ResultTransformer) {
				transformer = (ResultTransformer) value;
				break;
			}
		}

		return transformer;
	}

	/**
	 * 参数索引
	 * 
	 * @return
	 */
	private int getParameterIndex() {
		int i = 0;
		Parameters parameters = method.getParameters();
		for (Parameter parameter : parameters) {
			if (parameter.isBindable() && !QuerySpecification.class.isAssignableFrom(parameter.getType())
					&& !ResultTransformer.class.isAssignableFrom(parameter.getType())) {
				i++;
			}
		}
		return i;
	}

	/**
	 * 命名参数
	 * 
	 * @return
	 */
	private boolean isNamedParameter() {
		Parameters parameters = method.getParameters();
		for (Parameter parameter : parameters) {
			return parameter.isNamedParameter();
		}
		return false;
	}

	private boolean matcher() {
		Matcher m = JQPLPATTERN.matcher(queryString);
		return !method.isNativeQuery() && m.find();
	}

	private String getCountQuery(String queryString) {

		// 如果select中包含order by删之
		String lineSql = queryString;
		if (hasOrderBy(lineSql)) {
			lineSql = removeOrders(lineSql);
		}

		if (method.isNativeQuery()) {
			// 如果SELECT 中包含 DISTINCT 只能在外层包含COUNT
			if (hasGroupByORDistinct(lineSql)) {
				return new StringBuffer(lineSql.length() + 20).append("select count(1) as coun from (").append(lineSql)
						.append(" ) t").toString();
			} else {
				int formIndex = getAfterFormInsertPoint(lineSql);
				return new StringBuffer(lineSql.length() + 20).append("select count(1) as coun ")
						.append(lineSql.substring(formIndex)).toString();
			}
		} else {
			return createCountQueryFor(lineSql);
		}

	}
}