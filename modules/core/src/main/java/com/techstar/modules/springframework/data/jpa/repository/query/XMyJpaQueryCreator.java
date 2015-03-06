package com.techstar.modules.springframework.data.jpa.repository.query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.query.parser.PartTree;

public final class XMyJpaQueryCreator<E> implements Specification<E> {

	private static final Logger logger = LoggerFactory.getLogger(XMyJpaQueryCreator.class);

	private final Class<E> enityClass;
	private final String propertyName;
	private final Object[] values;

	public XMyJpaQueryCreator(Class<E> enityClass, String propertyName, Object... values) {

		this.enityClass = enityClass;
		this.propertyName = propertyName;
		this.values = values;
	}

	@Override
	public Predicate toPredicate(Root<E> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		PartTree tree = new PartTree(propertyName, enityClass);
		XPredicateCreator creator = new XPredicateCreator(tree, values, root, cb);
		return creator.createCriteria();
	}

}
