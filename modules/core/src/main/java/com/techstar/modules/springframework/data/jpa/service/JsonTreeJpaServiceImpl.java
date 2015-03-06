package com.techstar.modules.springframework.data.jpa.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import com.techstar.modules.springframework.data.jpa.domain.JsonTree;
import com.techstar.modules.utils.JsonTreeUtils;

@SuppressWarnings({ "rawtypes", "unchecked" })
@Transactional(readOnly = true)
public abstract class JsonTreeJpaServiceImpl<T extends JsonTree, ID extends Serializable> extends
	MyJpaServiceImpl<T, ID> implements JsonTreeJpaService<T, ID> {

    @Override
    @Transactional(readOnly = false)
    public void deletex(final JsonTree entity) {

	JsonTree parent = null;
	if (entity.hasChildren()) {
	    for (JsonTree children : (Set<JsonTree>) entity.getChildrens()) {
		if (entity.hasParent()) {
		    parent = (JsonTree) entity.getParent();
		    children.setParent(parent);
		    children.setLevel(parent.getLevelx() + 1);
		} else {
		    children.setParent(null);
		    children.setLevel(0);
		}
	    }
	}

	delete(JsonTree.class, entity);

	if (entity.hasParent()) {
	    if (parent == null) {
		parent = (JsonTree) entity.getParent();
	    }
	    boolean exists = this.exists("parent", parent);
	    parent.setLeaf(!exists);
	}
    }

    @Override
    @Transactional(readOnly = false)
    public void savex(final JsonTree entity) {
	entity.setLeaf(!entity.hasChildren());

	if (entity.hasParent()) {
	    JsonTree parent = (JsonTree) entity.getParent();
	    parent.setLeaf(false);
	    entity.setLevel(parent.getLevelx() + 1);
	    
	    //修改子结点的level
	    if (entity.hasChildren()) {
		for (JsonTree children : (Set<JsonTree>) entity.getChildrens()) {
		    children.setLevel(entity.getLevelx() + 1);
		}
	    }
	} else {
	    entity.setLevel(0);
	    //修改子结点的level
	    if (entity.hasChildren()) {
		for (JsonTree children : (Set<JsonTree>) entity.getChildrens()) {
		    children.setLevel(entity.getLevelx() + 1);
		}
	    }
	}

	save(JsonTree.class, entity);
    }

    @Override
    @Transactional(readOnly = false)
    public void savex(final Collection<T> entities) {
	for (JsonTree entity : entities) {
	    savex(entity);
	}
    }

    @Override
    public List<T> findAll(final Specification<T> spec, final boolean build) {
	return findAll(spec, null, build);
    }

    @Override
    public List<T> findAll(final Specification<T> spec, final Sort sort, final boolean build) {
	List<T> list = this.findAll(spec, sort);
	if (build && CollectionUtils.isNotEmpty(list)) {
	    list = (List<T>) JsonTreeUtils.build(list);
	}
	return list;
    }
}
