package org.springframework.data.jpa.repository.support;

import static com.techstar.modules.springframework.data.jpa.repository.query.MyQueryUtils.applyAndBind;
import static com.techstar.modules.springframework.data.jpa.repository.query.MyQueryUtils.getOne;
import static org.springframework.data.jpa.repository.query.QueryUtils.COUNT_QUERY_STRING;
import static org.springframework.data.jpa.repository.query.QueryUtils.DELETE_ALL_QUERY_STRING;
import static org.springframework.data.jpa.repository.query.QueryUtils.applyAndBind;
import static org.springframework.data.jpa.repository.query.QueryUtils.getQueryString;
import static org.springframework.data.jpa.repository.query.QueryUtils.toOrders;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.techstar.modules.springframework.data.jpa.repository.MyJpaRepository;
import com.techstar.modules.springframework.data.jpa.repository.query.XMyJpaQueryCreator;

@Repository
@Transactional(readOnly = true)
public class MySimpleJpaRepository<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements
		MyJpaRepository<T, ID> {

	private static final Logger logger = LoggerFactory.getLogger(MySimpleJpaRepository.class);

	private final EntityManager em;
	private final JpaEntityInformation<T, ?> entityInformation;
	private final Class<T> domainClass;

	private final PersistenceProvider provider;

	public MySimpleJpaRepository(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
		super(entityInformation, entityManager);
		this.em = entityManager;
		this.entityInformation = entityInformation;
		this.domainClass = entityInformation.getJavaType();
		this.provider = PersistenceProvider.fromEntityManager(entityManager);
	}

	public MySimpleJpaRepository(Class<T> domainClass, EntityManager em) {
		super(domainClass, em);
		this.em = em;
		this.domainClass = domainClass;
		this.entityInformation = JpaEntityInformationSupport.getMetadata(domainClass, em);
		this.provider = PersistenceProvider.fromEntityManager(em);
	}

	@Override
	public EntityManager getEntityManager() {
		return this.em;
	}

	@Override
	public Class<T> getDomainClass() {
		return domainClass;
	}

	@Override
	@Transactional
	public <E> void delete(final Class<E> enityClass, final Serializable id) {

		Assert.notNull(id, "The given id must not be null!");

		E entity = findOne(enityClass, id);

		if (entity == null) {
			throw new EmptyResultDataAccessException(String.format("No %s entity with id %s exists!",
					entityInformation.getJavaType(), id), 1);
		}

		delete(enityClass, entity);
	}

	@Override
	@Transactional
	public <E> void delete(final Class<E> enityClass, final E entity) {
		Assert.notNull(entity, "The entity must not be null!");
		em.remove(em.contains(entity) ? entity : em.merge(entity));
	}

	@Override
	@Transactional
	public <E> void delete(final Class<E> enityClass, final Iterable<? extends E> entities) {

		Assert.notNull(entities, "The given Iterable of entities not be null!");

		for (E entity : entities) {
			delete(enityClass, entity);
		}
	}

	@Override
	@Transactional
	public void delete(final Collection<ID> ids) {

		Assert.notEmpty(ids, "The given Collection of ids not be empty!");

		List<T> entities = findAll(ids);
		if (entities != null && !entities.isEmpty()) {
			delete(entities);
		}
	}

	@Override
	@Transactional
	public <E> void delete(final Class<E> enityClass, final Collection<? extends Serializable> ids) {

		Assert.notEmpty(ids, "The given Collection of ids not be empty!");

		List<E> entities = findAll(enityClass, ids);
		if (entities != null && !entities.isEmpty()) {
			delete(enityClass, entities);
		}
	}

	@Override
	@Transactional
	public <E> void deleteInBatch(final Class<E> enityClass, Iterable<E> entities) {

		Assert.notNull(entities, "The given Iterable of entities not be null!");

		if (!entities.iterator().hasNext()) {
			return;
		}

		applyAndBind(getQueryString(DELETE_ALL_QUERY_STRING, enityClass.getSimpleName()), entities, em).executeUpdate();
	}

	@Override
	@Transactional
	public int deleteInBatch(final Collection<ID> ids) {
		Assert.notEmpty(ids, "The given Collection of ids not be empty!");
		String idAttributeName = entityInformation.getIdAttribute().getName();
		return applyAndBind(idAttributeName, getQueryString(DELETE_ALL_QUERY_STRING, domainClass.getSimpleName()), ids,
				em).executeUpdate();
	}

	@Override
	@Transactional
	public <E> int deleteInBatch(final Class<E> enityClass, final Collection<? extends Serializable> ids) {
		Assert.notEmpty(ids, "The given Collection of ids not be empty!");
		JpaEntityInformation<E, ?> jpaEntityInformation = JpaMetamodelEntityInformation.getMetadata(enityClass, em);
		String idAttributeName = jpaEntityInformation.getIdAttribute().getName();
		return applyAndBind(idAttributeName, getQueryString(DELETE_ALL_QUERY_STRING, enityClass.getSimpleName()), ids,
				em).executeUpdate();
	}

	@Override
	@Transactional
	public <E> void deleteAll(final Class<E> enityClass) {

		for (E element : findAll(enityClass)) {
			delete(enityClass, element);
		}
	}

	@Override
	@Transactional
	public <E> int deleteAllInBatch(final Class<E> enityClass) {
		return em.createQuery(getDeleteAllQueryString(enityClass)).executeUpdate();
	}

	@Override
	public <E> long count(final Class<E> enityClass) {
		return em.createQuery(getCountQueryString(enityClass), Long.class).getSingleResult();
	}

	@Override
	public <E> long count(final Class<E> enityClass, final Specification<E> spec) {
		return getCountQuery(enityClass, spec).getSingleResult();
	}

	@Override
	@Transactional
	public <E> E save(final Class<E> enityClass, final E entity) {
		JpaEntityInformation<E, ?> jpaEntityInformation = JpaMetamodelEntityInformation.getMetadata(enityClass, em);
		if (jpaEntityInformation.isNew(entity)) {
			em.persist(entity);
			return entity;
		} else {
			return em.merge(entity);
		}
	}

	@Override
	@Transactional
	public <E> E saveAndFlush(final Class<E> enityClass, final E entity) {

		E result = save(enityClass, entity);
		flush();

		return result;
	}

	@Override
	@Transactional
	public <E> List<E> save(final Class<E> enityClass, final Iterable<E> entities) {

		List<E> result = new ArrayList<E>();

		if (entities == null) {
			return result;
		}

		for (E entity : entities) {
			result.add(save(enityClass, entity));
		}

		return result;
	}

	/*------------查询本实体外的其它实体---------------------*/
	@Override
	public <E> E findOne(final Class<E> enityClass, Serializable id) {
		return em.find(enityClass, id);
	}

	@Override
	public <E> E findOne(final Class<E> enityClass, Specification<E> spec) {
		List<E> list = findAll(enityClass, spec);
		return getOne(list);
	}

	@Override
	public <E, F> F findOne(final Root<E> root, final CriteriaQuery<F> query) {
		List<F> list = findAll(root, query);
		return getOne(list);
	}

	@Override
	public <E, F> F findOne(final Root<E> root, final CriteriaQuery<F> query, final Specification<E> spec) {
		List<F> list = findAll(root, query, spec);
		return getOne(list);
	}

	@Override
	public <E> boolean exists(final Class<E> enityClass, Serializable id) {

		Assert.notNull(id, "The given id must not be null!");

		JpaEntityInformation<E, ?> jpaEntityInformation = JpaMetamodelEntityInformation.getMetadata(enityClass, em);
		if (jpaEntityInformation.getIdAttribute() != null) {

			String placeholder = provider.getCountQueryPlaceholder();
			String entityName = jpaEntityInformation.getEntityName();
			Iterable<String> idAttributeNames = jpaEntityInformation.getIdAttributeNames();
			String existsQuery = QueryUtils.getExistsQueryString(entityName, placeholder, idAttributeNames);

			TypedQuery<Long> query = em.createQuery(existsQuery, Long.class);

			if (jpaEntityInformation.hasCompositeId()) {
				for (String idAttributeName : idAttributeNames) {
					query.setParameter(idAttributeName,
							jpaEntityInformation.getCompositeIdAttributeValue(id, idAttributeName));
				}
			} else {
				query.setParameter(idAttributeNames.iterator().next(), id);
			}

			return query.getSingleResult() == 1L;
		} else {
			return findOne(enityClass, id) != null;
		}
	}

	@Override
	public boolean exists(final Specification<T> spec) {
		Long count = this.count(spec);
		return count == null ? false : count > 0;
	}

	@Override
	public <E> boolean exists(final Class<E> enityClass, final Specification<E> spec) {
		Long count = this.count(enityClass, spec);
		return count == null ? false : count > 0;
	}

	@Override
	public <E> List<E> findAll(final Class<E> enityClass) {
		return getQuery(enityClass, null, (Sort) null).getResultList();
	}

	@Override
	public <E> List<E> findAll(final Class<E> enityClass, final Iterable<? extends Serializable> ids) {
		return getQuery(enityClass, new Specification<E>() {
			public Predicate toPredicate(Root<E> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				JpaEntityInformation<E, ?> jpaEntityInformation = JpaMetamodelEntityInformation.getMetadata(enityClass,
						em);
				Path<?> path = root.get(jpaEntityInformation.getIdAttribute());
				return path.in(cb.parameter(List.class, "ids"));
			}
		}, (Sort) null).setParameter("ids", ids).getResultList();
	}

	@Override
	public <E> List<E> findAll(final Class<E> enityClass, final Sort sort) {
		return getQuery(enityClass, null, sort).getResultList();
	}

	@Override
	public <E> Page<E> findAll(final Class<E> enityClass, final Pageable pageable) {
		if (null == pageable) {
			return new PageImpl<E>(findAll(enityClass));
		}

		return findAll(enityClass, null, pageable);
	}

	@Override
	public <E> List<E> findAll(final Class<E> enityClass, final Specification<E> spec) {
		return getQuery(enityClass, spec, (Sort) null).getResultList();
	}

	@Override
	public <E> List<E> findAll(final Class<E> enityClass, final Specification<E> spec, final Sort sort) {
		return getQuery(enityClass, spec, sort).getResultList();
	}

	@Override
	public <E> Page<E> findAll(final Class<E> enityClass, Specification<E> spec, Pageable pageable) {
		TypedQuery<E> query = getQuery(enityClass, spec, pageable);
		return pageable == null ? new PageImpl<E>(query.getResultList()) : readPage(enityClass, query, pageable, spec);
	}

	@Override
	public <E, F> List<F> findAll(final Root<E> root, final CriteriaQuery<F> query) {
		return getQuery(root, query, (Sort) null).getResultList();
	}

	@Override
	public <E, F> List<F> findAll(final Root<E> root, final CriteriaQuery<F> query, final Sort sort) {
		return getQuery(root, query, sort).getResultList();
	}

	@Override
	public <E, F> List<F> findAll(final Root<E> root, final CriteriaQuery<F> query, final Specification<E> spec) {
		return getQuery(root, query, spec, (Sort) null).getResultList();
	}

	@Override
	public <E, F> List<F> findAll(final Root<E> root, final CriteriaQuery<F> query, final Specification<E> spec,
			final Sort sort) {
		return getQuery(root, query, spec, sort).getResultList();
	}

	@Override
	public <E, F> Page<F> findAll(final Root<E> root, final CriteriaQuery<F> query, final Specification<E> spec,
			final Pageable pageable) {
		TypedQuery<F> typedQuery = getQuery(root, query, spec, pageable);
		return pageable == null ? new PageImpl<F>(typedQuery.getResultList()) : readPage(root, typedQuery, pageable,
				spec);
	}

	/*---------------------------------*/

	@Override
	public Long count(final String queryName, final Object... values) {
		return count(domainClass, queryName, values);
	}

	public <E> Long count(final Class<E> enityClass, final String queryName, final Object... values) {
		XMyJpaQueryCreator<E> creator = new XMyJpaQueryCreator<E>(enityClass, queryName, values);
		return this.count(enityClass, creator);
	}

	public boolean exists(final String queryName, final Object... values) {
		return exists(domainClass, queryName, values);
	}

	public <E> boolean exists(final Class<E> enityClass, final String queryName, final Object... values) {
		return count(enityClass, queryName, values) > 0;
	}

	@Override
	public T findOne(final String queryName, final Object... values) {
		return findOne(domainClass, queryName, values);
	}

	@Override
	public <E> E findOne(final Class<E> enityClass, final String queryName, final Object... values) {
		return getOne(findBy(enityClass, queryName, values));
	}

	@Override
	public List<T> findBy(final String queryName, final Object... values) {
		return findBy(domainClass, queryName, values);
	}

	@Override
	public List<T> findBy(final String queryName, final Sort sort, final Object... values) {
		return findBy(domainClass, queryName, sort, values);
	}

	@Override
	public Page<T> findBy(final String queryName, final Pageable pageable, final Object... values) {
		return findBy(domainClass, queryName, pageable, values);
	}

	@Override
	public <E> List<E> findBy(final Class<E> enityClass, final String queryName, final Object... values) {
		return findBy(enityClass, queryName, (Sort) null, values);
	}

	@Override
	public <E> List<E> findBy(final Class<E> enityClass, final String queryName, final Sort sort,
			final Object... values) {

		Assert.hasText(queryName, "参数 queryName 不能为空!");

		XMyJpaQueryCreator<E> creator = new XMyJpaQueryCreator<E>(enityClass, queryName, values);
		return this.findAll(enityClass, creator, sort);
	}

	@Override
	public <E> Page<E> findBy(final Class<E> enityClass, final String queryName, final Pageable pageable,
			final Object... values) {

		Assert.hasText(queryName, "参数 queryName 不能为空!");
		Assert.notNull(pageable, "参数 pageable 不能为 null!");

		
		XMyJpaQueryCreator<E> creator = new XMyJpaQueryCreator<E>(enityClass, queryName, values);
		return this.findAll(enityClass, creator, pageable);
	}

	@SuppressWarnings("unchecked")
	private <E, F> Page<F> readPage(final Root<E> root, final TypedQuery<F> query, final Pageable pageable,
			final Specification<E> spec) {

		query.setFirstResult(pageable.getOffset());
		query.setMaxResults(pageable.getPageSize());

		Long total = getCountQuery((Class<E>) root.getJavaType(), spec).getSingleResult();

		List<F> content = total <= pageable.getOffset() ? Collections.<F> emptyList() : query.getResultList();
		return new PageImpl<F>(content, pageable, total.longValue());
	}

	private <E> Page<E> readPage(final Class<E> enityClass, final TypedQuery<E> query, final Pageable pageable,
			final Specification<E> spec) {
		query.setFirstResult(pageable.getOffset());
		query.setMaxResults(pageable.getPageSize());
		Long total = getCountQuery(enityClass, spec).getSingleResult();
		List<E> content = total <= pageable.getOffset() ? Collections.<E> emptyList() : query.getResultList();
		return new PageImpl<E>(content, pageable, total.longValue());
	}

	private <E> TypedQuery<E> getQuery(final Class<E> enityClass, final Specification<E> spec, final Pageable pageable) {
		Sort sort = pageable != null ? pageable.getSort() : null;
		return getQuery(enityClass, spec, sort);
	}

	private <E, F> TypedQuery<F> getQuery(final Root<E> root, CriteriaQuery<F> query, final Specification<E> spec,
			final Pageable pageable) {
		Sort sort = pageable != null ? pageable.getSort() : null;
		return getQuery(root, query, spec, sort);
	}

	private <E> TypedQuery<E> getQuery(final Class<E> enityClass, final Specification<E> spec, final Sort sort) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<E> query = builder.createQuery(enityClass);
		Root<E> root = applySpecificationToCriteria(enityClass, spec, query);
		query.select(root);
		if (sort != null)
			query.orderBy(toOrders(sort, root, builder));
		return em.createQuery(query);
	}

	@SuppressWarnings("unchecked")
	private <E, F> TypedQuery<F> getQuery(final Root<E> root, CriteriaQuery<F> query, final Specification<E> spec,
			final Sort sort) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		applySpecificationToCriteria(root, spec, query);

		if (query.getSelection() == null && root.getJavaType().isAssignableFrom(query.getResultType())) {
			query.select((Root<F>) root);
		}

		if (sort != null)
			query.orderBy(toOrders(sort, root, builder));
		return em.createQuery(query);
	}

	private <E, F> TypedQuery<F> getQuery(final Root<E> root, CriteriaQuery<F> query, final Sort sort) {

		CriteriaBuilder builder = em.getCriteriaBuilder();
		if (sort != null)
			query.orderBy(toOrders(sort, root, builder));
		return em.createQuery(query);
	}

	private <E> TypedQuery<Long> getCountQuery(final Class<E> enityClass, final Specification<E> spec) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Long> query = builder.createQuery(Long.class);
		Root<E> root = applySpecificationToCriteria(enityClass, spec, query);
		query.select(builder.count(root));
		return em.createQuery(query);
	}

	@Deprecated
	private <E> TypedQuery<Long> getCountQuery(final Root<E> root, final Specification<E> spec) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Long> query = builder.createQuery(Long.class);
		applySpecificationToCriteria(root, spec, query);
		query.select(builder.count(root));
		return em.createQuery(query);
	}

	private <E> Root<E> applySpecificationToCriteria(final Class<E> enityClass, final Specification<E> spec,
			final CriteriaQuery<?> query) {
		Assert.notNull(query);
		Root<E> root = query.from(enityClass);
		if (spec == null)
			return root;
		CriteriaBuilder builder = em.getCriteriaBuilder();
		Predicate predicate = spec.toPredicate(root, query, builder);
		if (predicate != null)
			query.where(predicate);
		return root;
	}

	private <E> void applySpecificationToCriteria(final Root<E> root, final Specification<E> spec,
			final CriteriaQuery<?> query) {
		Assert.notNull(query);

		if (spec != null) {
			CriteriaBuilder builder = em.getCriteriaBuilder();
			Predicate predicate = spec.toPredicate(root, query, builder);
			if (predicate != null)
				query.where(predicate);

		}
	}

	private <E> String getDeleteAllQueryString(final Class<E> enityClass) {
		return getQueryString(DELETE_ALL_QUERY_STRING, enityClass.getSimpleName());
	}

	private <E> String getCountQueryString(final Class<E> enityClass) {
		String countQuery = String.format(COUNT_QUERY_STRING, provider.getCountQueryPlaceholder(), "%s");
		return getQueryString(countQuery, enityClass.getSimpleName());
	}

}
