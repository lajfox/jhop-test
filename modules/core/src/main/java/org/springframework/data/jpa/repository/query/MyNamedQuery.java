package org.springframework.data.jpa.repository.query;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.hibernate.ejb.HibernateQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.query.Parameters;
import org.springframework.data.repository.query.QueryCreationException;
import org.springframework.data.repository.query.RepositoryQuery;

import com.techstar.modules.hibernate.transform.MyAliasToEntityMapResultTransformer;

/**
 * 拷贝 org.springframework.data.jpa.repository.query.NamedQuery,改写doCreateQuery，
 * 使其可以返回map
 * 
 * @see NamedQuery
 * @author ZengWenfeng
 */
final class MyNamedQuery extends AbstractJpaQuery {

	private static final String CANNOT_EXTRACT_QUERY = "Your persistence provider does not support extracting the JPQL query from a "
			+ "named query thus you can't use Pageable inside your query method. Make sure you "
			+ "have a JpaDialect configured at your EntityManagerFactoryBean as this affects "
			+ "discovering the concrete persistence provider.";

	private static final Logger LOG = LoggerFactory.getLogger(NamedQuery.class);

	private final String queryName;
	private final String countQueryName;
	private final QueryExtractor extractor;

	// add by ZengWenfeng
	private final JpaQueryMethod method;

	/**
	 * Creates a new {@link NamedQuery}.
	 */
	private MyNamedQuery(JpaQueryMethod method, EntityManager em) {

		super(method, em);

		// add by ZengWenfeng
		this.method = method;

		this.queryName = method.getNamedQueryName();
		this.countQueryName = method.getNamedCountQueryName();
		this.extractor = method.getQueryExtractor();

		Parameters parameters = method.getParameters();

		// Let's see if the referenced named query exists
		em.createNamedQuery(queryName);

		if (parameters.hasSortParameter()) {
			throw new IllegalStateException(String.format("Finder method %s is backed " + "by a NamedQuery and must "
					+ "not contain a sort parameter as we cannot modify the query! Use @Query instead!", method));
		}

		boolean weNeedToCreateCountQuery = !hasNamedQuery(em, countQueryName)
				&& method.getParameters().hasPageableParameter();
		boolean cantExtractQuery = !this.extractor.canExtractQuery();

		if (weNeedToCreateCountQuery && cantExtractQuery) {
			throw QueryCreationException.create(method, CANNOT_EXTRACT_QUERY);
		}

		if (parameters.hasPageableParameter()) {
			LOG.warn("Finder method {} is backed by a NamedQuery"
					+ " but contains a Pageable parameter! Sorting delivered "
					+ "via this Pageable will not be applied!", method);
		}
	}

	/**
	 * Returns whether the named query with the given name exists.
	 * 
	 * @param em
	 * @return
	 */
	private static boolean hasNamedQuery(EntityManager em, String queryName) {

		try {
			em.createNamedQuery(queryName);
			return true;
		} catch (IllegalArgumentException e) {
			LOG.debug("Did not find named query {}", queryName);
			return false;
		}
	}

	/**
	 * Looks up a named query for the given
	 * {@link org.springframework.data.repository.query.QueryMethod}.
	 * 
	 * @param method
	 * @return
	 */
	public static RepositoryQuery lookupFrom(JpaQueryMethod method, EntityManager em) {

		final String queryName = method.getNamedQueryName();

		LOG.debug("Looking up named query {}", queryName);

		try {
			RepositoryQuery query = new MyNamedQuery(method, em);
			LOG.debug("Found named query {}!", queryName);
			return query;
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.data.jpa.repository.query.AbstractJpaQuery#doCreateQuery
	 * (java.lang.Object[])
	 */
	@Override
	protected Query doCreateQuery(Object[] values) {

		Query query = getEntityManager().createNamedQuery(queryName);

		// add by ZengWenfeng
		if (HibernateQuery.class.isAssignableFrom(query.getClass())
				&& Map.class.isAssignableFrom(method.getReturnedObjectType())) {
			
			query.unwrap(HibernateQuery.class).getHibernateQuery()
					.setResultTransformer(MyAliasToEntityMapResultTransformer.INSTANCE);
		}

		return createBinder(values).bindAndPrepare(query);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.data.jpa.repository.query.AbstractJpaQuery#
	 * doCreateCountQuery(java.lang.Object[])
	 */
	@Override
	protected TypedQuery<Long> doCreateCountQuery(Object[] values) {

		EntityManager em = getEntityManager();
		TypedQuery<Long> countQuery = null;

		if (hasNamedQuery(em, countQueryName)) {
			countQuery = em.createNamedQuery(countQueryName, Long.class);
		} else {
			Query query = createQuery(values);
			String queryString = extractor.extractQueryString(query);
			countQuery = em.createQuery(QueryUtils.createCountQueryFor(queryString), Long.class);
		}

		return createBinder(values).bind(countQuery);
	}
}
