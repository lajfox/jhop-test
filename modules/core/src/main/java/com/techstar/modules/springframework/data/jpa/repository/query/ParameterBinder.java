package com.techstar.modules.springframework.data.jpa.repository.query;

import javax.persistence.Query;

import org.hibernate.transform.ResultTransformer;
import org.springframework.data.repository.query.Parameter;
import org.springframework.data.repository.query.Parameters;

import com.techstar.modules.springframework.data.jpa.domain.QuerySpecification;

public class ParameterBinder extends org.springframework.data.jpa.repository.query.ParameterBinder {

	private final Parameters parameters;
	private final Object[] values;

	public ParameterBinder(Parameters parameters, Object[] values) {
		super(parameters, values);
		this.parameters = parameters;
		this.values = values;
	}

	@Override
	public <T extends Query> T bind(T query) {

		int methodParameterPosition = 0;
		int queryParameterPosition = 1;

		for (Parameter parameter : parameters) {

			if (parameter.isBindable() && !QuerySpecification.class.isAssignableFrom(parameter.getType())
					&& !ResultTransformer.class.isAssignableFrom(parameter.getType())) {

				Object value = values[methodParameterPosition];
				bind(query, parameter, value, queryParameterPosition++);
			}

			methodParameterPosition++;
		}

		return query;
	}

}
