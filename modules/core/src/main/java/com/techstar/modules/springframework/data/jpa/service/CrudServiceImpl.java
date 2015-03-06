package com.techstar.modules.springframework.data.jpa.service;

import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public abstract class CrudServiceImpl<T, ID extends Serializable> implements CrudService<T, ID> {

	protected abstract CrudRepository<T, ID> getCrudRepository();

	@Override
	@Transactional
	public <S extends T> S save(S entity) {
		return getCrudRepository().save(entity);
	}

	@Override
	@Transactional
	public <S extends T> Iterable<S> save(Iterable<S> entities) {
		return getCrudRepository().save(entities);
	}

	@Override	
	public T findOne(ID id) {
		return this.getCrudRepository().findOne(id);
	}

	@Override
	public boolean exists(ID id) {
		return this.getCrudRepository().exists(id);
	}

	@Override
	public Iterable<T> findAll() {
		return this.getCrudRepository().findAll();
	}

	@Override
	public Iterable<T> findAll(Iterable<ID> ids) {
		return this.getCrudRepository().findAll(ids);
	}

	@Override	
	public long count() {
		return this.getCrudRepository().count();
	}

	@Override
	@Transactional
	public void delete(ID id) {
		this.getCrudRepository().delete(id);
	}

	@Override
	@Transactional
	public void delete(T entity) {
		this.getCrudRepository().delete(entity);
	}

	@Override
	@Transactional
	public void delete(Iterable<? extends T> entities) {
		this.getCrudRepository().delete(entities);
	}

	@Override
	@Transactional
	public void deleteAll() {
		this.getCrudRepository().deleteAll();
	}
}
