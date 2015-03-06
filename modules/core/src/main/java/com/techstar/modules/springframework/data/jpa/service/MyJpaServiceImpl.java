package com.techstar.modules.springframework.data.jpa.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.techstar.modules.springframework.data.jpa.repository.MyJpaRepository;

@Transactional(readOnly = true)
public abstract class MyJpaServiceImpl<T, ID extends Serializable> extends JpaServiceImpl<T, ID> implements
		MyJpaService<T, ID> {
	
	protected static final Logger logger = LoggerFactory.getLogger(MyJpaServiceImpl.class);

	protected abstract MyJpaRepository<T, ID> getMyJpaRepository();

	@Override
	protected JpaRepository<T, ID> getJpaRepository() {
		return getMyJpaRepository();
	}

	@Override
	@Transactional
	public <E> void delete(final Class<E> enityClass, final Serializable id) {
		getMyJpaRepository().delete(enityClass, id);
	}

	@Override
	@Transactional
	public <E> void delete(final Class<E> enityClass, final E entity) {
		getMyJpaRepository().delete(enityClass, entity);
	}

	@Override
	@Transactional
	public <E> void delete(final Class<E> enityClass, final Iterable<? extends E> entities) {
		getMyJpaRepository().delete(enityClass, entities);
	}

	@Override
	@Transactional
	public void delete(final Collection<ID> ids) {
		getMyJpaRepository().delete(ids);
	}

	@Override
	@Transactional
	public <E> void delete(final Class<E> enityClass, final Collection<? extends Serializable> ids) {
		getMyJpaRepository().delete(enityClass, ids);
	}

	@Override
	@Transactional
	public <E> void deleteInBatch(final Class<E> enityClass, Iterable<E> entities) {
		getMyJpaRepository().deleteInBatch(enityClass, entities);
	}

	@Override
	@Transactional
	public int deleteInBatch(final Collection<ID> ids) {
		return getMyJpaRepository().deleteInBatch(ids);
	}

	@Override
	@Transactional
	public <E> int deleteInBatch(final Class<E> enityClass, final Collection<? extends Serializable> ids) {
		return getMyJpaRepository().deleteInBatch(enityClass, ids);
	}

	@Override
	@Transactional
	public <E> void deleteAll(final Class<E> enityClass) {
		getMyJpaRepository().deleteAll(enityClass);
	}

	@Override
	@Transactional
	public <E> int deleteAllInBatch(final Class<E> enityClass) {
		return getMyJpaRepository().deleteAllInBatch(enityClass);
	}

	@Override
	public <E> long count(final Class<E> enityClass) {
		return getMyJpaRepository().count(enityClass);
	}

	@Override
	public long count(final Specification<T> spec) {
		return getMyJpaRepository().count(spec);
	}

	@Override
	public <E> long count(final Class<E> enityClass, final Specification<E> spec) {
		return getMyJpaRepository().count(enityClass, spec);
	}

	@Override
	@Transactional
	public <E> E save(final Class<E> enityClass, final E entity) {
		return getMyJpaRepository().save(enityClass, entity);
	}

	@Override
	@Transactional
	public <E> E saveAndFlush(final Class<E> enityClass, final E entity) {
		return getMyJpaRepository().saveAndFlush(enityClass, entity);
	}

	@Override
	@Transactional
	public <E> List<E> save(final Class<E> enityClass, final Iterable<E> entities) {
		return getMyJpaRepository().save(enityClass, entities);
	}

	@Override
	public <E> E findOne(final Class<E> enityClass, Serializable id) {
		return getMyJpaRepository().findOne(enityClass, id);
	}

	@Override
	public <E> boolean exists(final Class<E> enityClass, Serializable id) {
		return getMyJpaRepository().exists(enityClass, id);
	}

	@Override
	public boolean exists(final Specification<T> spec) {
		return getMyJpaRepository().exists(spec);
	}

	@Override
	public <E> boolean exists(final Class<E> enityClass, final Specification<E> spec) {
		return getMyJpaRepository().exists(enityClass, spec);
	}

	@Override
	public T findOne(final Specification<T> spec) {
		return getMyJpaRepository().findOne(spec);
	}

	@Override
	public <E> E findOne(final Class<E> enityClass, Specification<E> spec) {
		return getMyJpaRepository().findOne(enityClass, spec);
	}

	@Override
	public <E> List<E> findAll(final Class<E> enityClass) {
		return getMyJpaRepository().findAll(enityClass);
	}

	@Override
	public <E> List<E> findAll(final Class<E> enityClass, final Iterable<? extends Serializable> ids) {
		return getMyJpaRepository().findAll(enityClass, ids);
	}

	@Override
	public <E> List<E> findAll(final Class<E> enityClass, final Sort sort) {
		return getMyJpaRepository().findAll(enityClass, sort);
	}

	@Override
	public <E> Page<E> findAll(final Class<E> enityClass, final Pageable pageable) {
		return getMyJpaRepository().findAll(enityClass, pageable);
	}

	@Override
	public List<T> findAll(final Specification<T> spec) {
		return getMyJpaRepository().findAll(spec);
	}

	@Override
	public <E> List<E> findAll(final Class<E> enityClass, final Specification<E> spec) {
		return getMyJpaRepository().findAll(enityClass, spec);
	}

	@Override
	public List<T> findAll(final Specification<T> spec, final Sort sort) {
		return getMyJpaRepository().findAll(spec, sort);
	}

	@Override
	public <E> List<E> findAll(final Class<E> enityClass, final Specification<E> spec, final Sort sort) {
		return getMyJpaRepository().findAll(enityClass, spec, sort);
	}

	@Override
	public Page<T> findAll(final Specification<T> spec, final Pageable pageable) {
		return getMyJpaRepository().findAll(spec, pageable);
	}

	@Override
	public <E> Page<E> findAll(final Class<E> enityClass, final Specification<E> spec, final Pageable pageable) {
		return getMyJpaRepository().findAll(enityClass, spec, pageable);
	}

	@Override
	public Long count(final String queryName, final Object... values) {
		return getMyJpaRepository().count(queryName, values);
	}

	@Override
	public <E> Long count(final Class<E> enityClass, final String queryName, final Object... values) {
		return getMyJpaRepository().count(enityClass, queryName, values);
	}

	@Override
	public boolean exists(final String queryName, final Object... values) {
		return getMyJpaRepository().exists(queryName, values);
	}

	@Override
	public <E> boolean exists(final Class<E> enityClass, final String queryName, final Object... values) {
		return getMyJpaRepository().exists(enityClass, queryName, values);
	}

	@Override
	public T findOne(final String queryName, final Object... values) {
		return getMyJpaRepository().findOne(queryName, values);
	}

	@Override
	public <E> E findOne(Class<E> enityClass, String queryName, Object... values) {
		return getMyJpaRepository().findOne(enityClass, queryName, values);
	}

	@Override
	public List<T> findBy(final String queryName, final Object... values) {
		return getMyJpaRepository().findBy(queryName, values);
	}

	@Override
	public List<T> findBy(String queryName, Sort sort, Object... values) {
		return getMyJpaRepository().findBy(queryName, sort, values);
	}

	@Override
	public Page<T> findBy(String queryName, Pageable pageable, Object... values) {
		return getMyJpaRepository().findBy(queryName, pageable, values);
	}

	@Override
	public <E> List<E> findBy(Class<E> enityClass, String queryName, Object... values) {
		return getMyJpaRepository().findBy(enityClass, queryName, values);
	}

	@Override
	public <E> List<E> findBy(Class<E> enityClass, String queryName, Sort sort, Object... values) {
		return getMyJpaRepository().findBy(enityClass, queryName, sort, values);
	}

	@Override
	public <E> Page<E> findBy(Class<E> enityClass, String queryName, Pageable pageable, Object... values) {
		return getMyJpaRepository().findBy(enityClass, queryName, pageable, values);
	}

}
