package com.techstar.modules.springframework.data.jpa.service;

import java.io.Serializable;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public abstract class PagingAndSortingServiceImpl<T, ID extends Serializable> extends CrudServiceImpl<T, ID> implements
		PagingAndSortingService<T, ID> {

	protected abstract PagingAndSortingRepository<T, ID> getPagingAndSortingRepository();

	@Override
	protected CrudRepository<T, ID> getCrudRepository() {
		return getPagingAndSortingRepository();
	}

	@Override
	public Iterable<T> findAll(Sort sort) {
		return this.getPagingAndSortingRepository().findAll(sort);
	}

	@Override
	public Page<T> findAll(Pageable pageable) {
		return this.getPagingAndSortingRepository().findAll(pageable);
	}
}
