package com.techstar.modules.springframework.data.jpa.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

public interface MyJpaService<T, ID extends Serializable> extends JpaService<T, ID> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.techstar.spring.data.repository.MyJpaRepository#delete(java.lang.Class<E>,java.io.Serializable)
	 */
	<E> void delete(final Class<E> enityClass, final Serializable id);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.techstar.spring.data.repository.MyJpaRepository#delete(java.lang.Class<E>,E)
	 */
	<E> void delete(final Class<E> enityClass, final E entity);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.techstar.spring.data.repository.MyJpaRepository#delete(java.lang.Class<E>,java.lang.Iterable)
	 */
	<E> void delete(final Class<E> enityClass, final Iterable<? extends E> entities);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.techstar.spring.data.repository.MyJpaRepository#delete(java.util.Collection<ID>)
	 */
	void delete(final Collection<ID> ids);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.techstar.spring.data.repository.MyJpaRepository#delete(java.lang.Class<E>,java.util.Collection<? extends Serializable>)
	 */
	<E> void delete(final Class<E> enityClass, final Collection<? extends Serializable> ids);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.techstar.spring.data.repository.MyJpaRepository#deleteInBatch(java.lang.Class<E>,java.lang.Iterable)
	 */
	<E> void deleteInBatch(final Class<E> enityClass, Iterable<E> entities);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.techstar.spring.data.repository.MyJpaRepository#deleteInBatch(java.util.Collection<ID>)
	 */
	int deleteInBatch(final Collection<ID> ids);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.techstar.spring.data.repository.MyJpaRepository#deleteInBatch(java.lang.Class<E>,java.util.Collection<? extends
	 * Serializable>)
	 */
	<E> int deleteInBatch(final Class<E> enityClass, final Collection<? extends Serializable> ids);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.techstar.spring.data.repository.MyJpaRepository#deleteAll(java.lang.Class<E>)
	 */
	<E> void deleteAll(final Class<E> enityClass);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.techstar.spring.data.repository.MyJpaRepository#deleteAllInBatch(java.lang.Class<E>)
	 */
	<E> int deleteAllInBatch(final Class<E> enityClass);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.techstar.spring.data.repository.MyJpaRepository#count(java.lang.Class<E>)
	 */
	<E> long count(final Class<E> enityClass);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.data.jpa.repository.JpaSpecificationExecutor.count#count(org.springframework.data.jpa.domain.Specification<T>)
	 */
	long count(final Specification<T> spec);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.techstar.spring.data.repository.MyJpaRepository#count(java.lang.Class<E>,org.springframework.data.jpa.domain.Specification<E>)
	 */
	<E> long count(final Class<E> enityClass, final Specification<E> spec);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.techstar.spring.data.repository.MyJpaRepository#save(java.lang.Class<E>,E)
	 */
	<E> E save(final Class<E> enityClass, final E entity);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.techstar.spring.data.repository.MyJpaRepository#saveAndFlush(java.lang.Class<E>,E)
	 */
	<E> E saveAndFlush(final Class<E> enityClass, final E entity);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.techstar.spring.data.repository.MyJpaRepository#save(java.lang.Class<E>,java.lang.Iterable)
	 */
	<E> List<E> save(final Class<E> enityClass, final Iterable<E> entities);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.techstar.spring.data.repository.MyJpaRepository#findOne(java.lang.Class<E>,java.io.Serializable)
	 */
	<E> E findOne(final Class<E> enityClass, Serializable id);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.techstar.spring.data.repository.MyJpaRepository#exists(java.lang.Class<E>,java.io.Serializable)
	 */
	<E> boolean exists(final Class<E> enityClass, Serializable id);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.techstar.spring.data.repository.MyJpaRepository#exists(org.springframework.data.jpa.domain.Specification<T>)
	 */
	boolean exists(final Specification<T> spec);


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.techstar.spring.data.repository.MyJpaRepository#exists(java.lang.Class<E>,org.springframework.data.jpa.domain.Specification<E>)
	 */
	<E> boolean exists(final Class<E> enityClass, final Specification<E> spec);

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.data.jpa.repository.JpaSpecificationExecutor#findOne(org.springframework.data.jpa.domain.Specification<T>)
	 */
	T findOne(final Specification<T> spec);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.techstar.spring.data.repository.MyJpaRepository#findOne(java.lang.Class<E>,org.springframework.data.jpa.domain.Specification<E>)
	 */
	<E> E findOne(final Class<E> enityClass, Specification<E> spec);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.techstar.spring.data.repository.MyJpaRepository#findAll(java.lang.Class<E>)
	 */
	<E> List<E> findAll(final Class<E> enityClass);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.techstar.spring.data.repository.MyJpaRepository#findAll(java.lang.Class<E>,java.lang.Iterable)
	 */
	<E> List<E> findAll(final Class<E> enityClass, final Iterable<? extends Serializable> ids);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.techstar.spring.data.repository.MyJpaRepository#findAll(java.lang.Class<E>,org.springframework.data.domain.Sort)
	 */
	<E> List<E> findAll(final Class<E> enityClass, final Sort sort);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.techstar.spring.data.repository.MyJpaRepository#findAll(java.lang.Class<E>,org.springframework.data.domain.Pageable)
	 */
	<E> Page<E> findAll(final Class<E> enityClass, final Pageable pageable);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.data.jpa.repository.JpaSpecificationExecutor#findAll(org.springframework.data.jpa.domain.Specification<T>)
	 */
	List<T> findAll(final Specification<T> spec);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.techstar.spring.data.repository.MyJpaRepository#findAll(java.lang.Class<E>,org.springframework.data.jpa.domain.Specification)
	 */
	<E> List<E> findAll(final Class<E> enityClass, final Specification<E> spec);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.data.jpa.repository.JpaSpecificationExecutor#findAll(org.springframework.data.jpa.domain.Specification<T>,,org
	 * .springframework.data.domain.Sort)
	 */
	List<T> findAll(final Specification<T> spec, final Sort sort);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.techstar.spring.data.repository.MyJpaRepository#findAll(java.lang.Class<E>,org.springframework.data.jpa.domain.Specification,
	 * org.springframework.data.domain.Sort)
	 */
	<E> List<E> findAll(final Class<E> enityClass, final Specification<E> spec, final Sort sort);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.data.jpa.repository.JpaSpecificationExecutor#findAll(org.springframework.data.jpa.domain.Specification<T>,,org
	 * .springframework.data.domain.Pageable)
	 */	
	Page<T> findAll( final Specification<T> spec, final Pageable pageable);
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.techstar.spring.data.repository.MyJpaRepository#findAll(java.lang.Class<E>,org.springframework.data.jpa.domain.Specification,
	 * org.springframework.data.domain.Pageable)
	 */
	<E> Page<E> findAll(final Class<E> enityClass, final Specification<E> spec, final Pageable pageable);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.techstar.spring.data.repository.MyJpaRepository#count(java.lang.String, java.lang.Object...)
	 */
	Long count(final String queryName, final Object... values);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.techstar.spring.data.repository.MyJpaRepository#count(java.lang.Class<E>,java.lang.String, java.lang.Object...)
	 */
	<E> Long count(final Class<E> enityClass, final String queryName, final Object... values);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.techstar.spring.data.repository.MyJpaRepository#exists(java.lang.String, java.lang.Object...)
	 */
	boolean exists(final String queryName, final Object... values);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.techstar.spring.data.repository.MyJpaRepository#exists(java.lang.Class<E>,java.lang.String, java.lang.Object...)
	 */
	<E> boolean exists(final Class<E> enityClass, final String queryName, final Object... values);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.techstar.spring.data.repository.MyJpaRepository#findOne(java.lang.String, java.lang.Object...)
	 */
	T findOne(final String queryName, final Object... values);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.techstar.spring.data.repository.MyJpaRepository#findOne(java.lang.Class<E>,String, java.lang.Object...)
	 */
	<E> E findOne(final Class<E> enityClass, final String queryName, final Object... values);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.techstar.spring.data.repository.MyJpaRepository#findBy(java.lang.String, java.lang.Object...)
	 */
	List<T> findBy(final String queryName, final Object... values);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.techstar.spring.data.repository.MyJpaRepository#findBy(java.lang.String,
	 * org.springframework.data.domain.Sort,java.lang.Object...)
	 */
	List<T> findBy(final String queryName, final Sort sort, final Object... values);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.techstar.spring.data.repository.MyJpaRepository#findBy(java.lang.String,
	 * org.springframework.data.domain.Pageable,java.lang.Object...)
	 */
	Page<T> findBy(final String queryName, final Pageable pageable, final Object... values);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.techstar.spring.data.repository.MyJpaRepository#findBy(java.lang.Class<E>,java.lang.String, java.lang.Object...)
	 */
	<E> List<E> findBy(final Class<E> enityClass, final String queryName, final Object... values);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.techstar.spring.data.repository.MyJpaRepository#findBy(java.lang.Class<E>,String,org.springframework.data.domain.Sort,
	 * java.lang.Object...)
	 */
	<E> List<E> findBy(final Class<E> enityClass, final String queryName, final Sort sort, final Object... values);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.techstar.spring.data.repository.MyJpaRepository#findBy(java.lang.Class<E>,String,org.springframework.data.domain.Pageable,
	 * java.lang.Object...)
	 */
	<E> Page<E> findBy(final Class<E> enityClass, final String queryName, final Pageable pageable,
			final Object... values);



}
