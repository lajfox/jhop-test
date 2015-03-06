package com.techstar.modules.springframework.data.jpa.domain;

import com.techstar.modules.springframework.data.jpa.repository.query.QLCreator;

public interface QuerySpecification {

	<T> QLCreator<T> build(final boolean isNativeQuery, final boolean isNamedParameter, final String countQuery,
			final String queryString);
}
