package com.techstar.modules.springframework.data.jpa.repository.query;

import static com.techstar.modules.commons.beanutils.ConvertUtils.convert2;
import static com.techstar.modules.commons.beanutils.ConvertUtils.convertComparable;
import static com.techstar.modules.commons.beanutils.ConvertUtils.converts;
import static com.techstar.modules.springframework.data.jpa.repository.query.MyQueryUtils.getComparablePath;
import static com.techstar.modules.springframework.data.jpa.repository.query.MyQueryUtils.getTypedPath;
import static com.techstar.modules.springframework.data.jpa.repository.query.MyQueryUtils.toExpressionRecursively;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mapping.PropertyPath;
import org.springframework.data.repository.query.parser.Part;
import org.springframework.data.repository.query.parser.Part.Type;
import org.springframework.data.repository.query.parser.PartTree;
import org.springframework.data.repository.query.parser.PartTree.OrPart;
import org.springframework.util.Assert;

public final class PredicateCreator {

	private final PartTree tree;
	private final CriteriaBuilder builder;
	private final Root<?> root;
	private final Iterator<String> params;

	public PredicateCreator(PartTree tree, String param, Root<?> root, CriteriaBuilder builder) {
		this(tree, StringUtils.isEmpty(param) ? null : new String[] { param }, root, builder);
	}

	public PredicateCreator(PartTree tree, String[] params, Root<?> root, CriteriaBuilder builder) {
		this(tree, ArrayUtils.isEmpty(params) ? null : Arrays.asList(params), root, builder);
	}

	public PredicateCreator(PartTree tree, Collection<String> params, Root<?> root, CriteriaBuilder builder) {
		this.tree = tree;
		this.params = CollectionUtils.isEmpty(params) ? null : params.iterator();
		this.builder = builder;
		this.root = root;
	}

	/**
	 * Actual query building logic. Traverses the {@link PartTree} and invokes callback methods to delegate actual criteria creation and
	 * concatenation.
	 * 
	 * @param tree
	 * @return
	 */
	public Predicate createCriteria() {

		Predicate base = null;
		for (OrPart node : tree) {

			Predicate criteria = null;

			for (Part part : node) {

				criteria = criteria == null ? create(part) : and(part, criteria);
			}

			base = base == null ? criteria : or(base, criteria);
		}

		return base;
	}

	protected Predicate create(Part part) {
		return toPredicate(part, root);
	}

	protected Predicate and(Part part, Predicate base) {

		return builder.and(base, toPredicate(part, root));
	}

	protected Predicate or(Predicate base, Predicate predicate) {

		return builder.or(base, predicate);
	}

	/**
	 * Creates a {@link Predicate} from the given {@link Part}.
	 * 
	 * @param part
	 * @param root
	 * @param iterator
	 * @return
	 */
	private Predicate toPredicate(Part part, Root<?> root) {
		return new PredicateBuilder(part, root, params).build();
	}

	/**
	 * Simple builder to contain logic to create JPA {@link Predicate}s from {@link Part}s.
	 * 
	 * @author Phil Webb
	 */
	@SuppressWarnings("unchecked")
	private class PredicateBuilder {

		private final Part part;
		private final Root<?> root;
		private final Iterator<String> params;

		/**
		 * Creates a new {@link PredicateBuilder} for the given {@link Part} and {@link Root}.
		 * 
		 * @param part
		 *            must not be {@literal null}.
		 * @param root
		 *            must not be {@literal null}.
		 * @param root
		 */
		public PredicateBuilder(Part part, Root<?> root, Iterator<String> params) {

			Assert.notNull(part);
			Assert.notNull(root);
			this.part = part;
			this.root = root;
			this.params = params;
		}

		/**
		 * Builds a JPA {@link Predicate} from the underlying {@link Part}.
		 * 
		 * @return
		 */
		public Predicate build() {

			PropertyPath property = part.getProperty();
			Expression<Object> path = toExpressionRecursively(root, property);

			switch (part.getType()) {
			case BETWEEN:
				return builder.between(getComparablePath(root, part),
						convertComparable(params.next(), path.getJavaType()),
						convertComparable(params.next(), path.getJavaType()));
			case AFTER:
			case GREATER_THAN:
				return builder.greaterThan(getComparablePath(root, part),
						convertComparable(params.next(), path.getJavaType()));
			case GREATER_THAN_EQUAL:
				return builder.greaterThanOrEqualTo(getComparablePath(root, part),
						convertComparable(params.next(), path.getJavaType()));
			case BEFORE:
			case LESS_THAN:
				return builder.lessThan(getComparablePath(root, part),
						convertComparable(params.next(), path.getJavaType()));
			case LESS_THAN_EQUAL:
				return builder.lessThanOrEqualTo(getComparablePath(root, part),
						convertComparable(params.next(), path.getJavaType()));
			case IS_NULL:
				return path.isNull();
			case IS_NOT_NULL:
				return path.isNotNull();
			case NOT_IN:
				return path.in(converts(params.next(), path.getJavaType())).not();
			case IN:
				return path.in(converts(params.next(), path.getJavaType()));
			case STARTING_WITH:
			case ENDING_WITH:
			case CONTAINING:
			case LIKE:
			case NOT_LIKE:
				Expression<String> stringPath = getTypedPath(root, part);
				Expression<String> propertyExpression = upperIfIgnoreCase(stringPath);
				Predicate like = builder.like(propertyExpression, "%" + params.next() + "%");
				return part.getType() == Type.NOT_LIKE ? like.not() : like;
			case TRUE:
				Expression<Boolean> truePath = getTypedPath(root, part);
				return builder.isTrue(truePath);
			case FALSE:
				Expression<Boolean> falsePath = getTypedPath(root, part);
				return builder.isFalse(falsePath);
			case SIMPLE_PROPERTY:
				String value = params.next();
				return value == null ? path.isNull() : builder.equal(upperIfIgnoreCase(path),
						convert2(value, path.getJavaType()));
			case NEGATING_SIMPLE_PROPERTY:
				return builder.notEqual(upperIfIgnoreCase(path), convert2(params.next(), path.getJavaType()));
			default:
				throw new IllegalArgumentException("Unsupported keyword " + part.getType());
			}

		}

		/**
		 * Applies an {@code UPPERCASE} conversion to the given {@link Expression} in case the underlying {@link Part} requires ignoring
		 * case.
		 * 
		 * @param expression
		 *            must not be {@literal null}.
		 * @return
		 */
		private <T> Expression<T> upperIfIgnoreCase(Expression<? extends T> expression) {

			switch (part.shouldIgnoreCase()) {
			case ALWAYS:
				Assert.state(canUpperCase(expression), "Unable to ignore case of " + expression.getJavaType().getName()
						+ " types, the property '" + part.getProperty().getSegment() + "' must reference a String");
				return (Expression<T>) builder.upper((Expression<String>) expression);
			case WHEN_POSSIBLE:
				if (canUpperCase(expression)) {
					return (Expression<T>) builder.upper((Expression<String>) expression);
				}
			}
			return (Expression<T>) expression;
		}

		private boolean canUpperCase(Expression<?> expression) {
			return String.class.equals(expression.getJavaType());
		}

	}

}
