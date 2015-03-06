package com.techstar.modules.springframework.data.jpa.service;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public abstract class JpaServiceImpl<T, ID extends Serializable> extends PagingAndSortingServiceImpl<T, ID> implements JpaService<T, ID> {

	protected abstract JpaRepository<T, ID> getJpaRepository();

	@Override
	protected PagingAndSortingRepository<T, ID> getPagingAndSortingRepository() {
		return getJpaRepository();
	}

	@Override
	public List<T> findAll() {
		return getJpaRepository().findAll();
	}

	@Override
	public List<T> findAll(Sort sort) {
		return getJpaRepository().findAll(sort);
	}

	@Override
	@Transactional
	public <S extends T> List<S> save(Iterable<S> entities) {
		return getJpaRepository().save(entities);
	}

	@Override
	public void flush() {
		getJpaRepository().flush();
	}

	@Override
	@Transactional
	public T saveAndFlush(T entity) {
		return getJpaRepository().saveAndFlush(entity);
	}

	@Override
	@Transactional
	public void deleteInBatch(Iterable<T> entities) {
		getJpaRepository().deleteInBatch(entities);
	}

	@Override
	@Transactional
	public void deleteAllInBatch() {
		getJpaRepository().deleteAllInBatch();
	}
}
