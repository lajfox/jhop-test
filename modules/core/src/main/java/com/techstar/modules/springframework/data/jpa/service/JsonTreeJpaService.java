package com.techstar.modules.springframework.data.jpa.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import com.techstar.modules.springframework.data.jpa.domain.JsonTree;

@SuppressWarnings("rawtypes")
public interface JsonTreeJpaService<T extends JsonTree, ID extends Serializable> extends MyJpaService<T, ID> {

	void deletex(final JsonTree entity);

	void savex(final JsonTree entity);
	
	void savex(final Collection<T> entities);

	List<T> findAll(final Specification<T> spec, final boolean build);

	List<T> findAll(final Specification<T> spec, final Sort sort, final boolean build);

}
