package org.springframework.data.jpa.repository.query;

import java.util.Collections;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.ParameterAccessor;
import org.springframework.data.repository.query.Parameters;
import org.springframework.data.repository.query.ParametersParameterAccessor;

public class PagedExecution extends JpaQueryExecution {

	private final Parameters parameters;

	public PagedExecution(Parameters parameters) {
		this.parameters = parameters;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Object doExecute(AbstractJpaQuery repositoryQuery, Object[] values) {

		Query query = null;
		if (repositoryQuery instanceof MySimpleJpaQuery) {
			query = ((MySimpleJpaQuery) repositoryQuery).getCreateCountQuery(values);
		} else {
			query = repositoryQuery.createCountQuery(values);
		}
		List<Object> totals = query.getResultList();
		Long total = CollectionUtils.isEmpty(totals) ? 0 : (totals.size() == 1 ? ((Number) totals.get(0)).longValue()
				: totals.size());

		if (total == 0) {
			return null;
		} else {
			ParameterAccessor accessor = new ParametersParameterAccessor(parameters, values);
			Pageable pageable = accessor.getPageable();
			if (total > pageable.getOffset()) {

				query = repositoryQuery.createQuery(values);
				List<Object> content = pageable == null || total > pageable.getOffset() ? query.getResultList()
						: Collections.emptyList();

				return new PageImpl<Object>(content, pageable, total);
			} else {
				return null;
			}
		}

	}

}
