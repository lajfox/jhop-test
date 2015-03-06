package com.techstar.modules.springframework.data.jpa.domain;

import com.techstar.modules.mybatis.domain.MyRowBounds;
import com.techstar.modules.springframework.data.jpa.repository.support.QueryType;

public class QLSpecification extends MyRowBounds {

	private QueryType type=QueryType.SQL;

	public QLSpecification() {
		super();
	}

	public QueryType getType() {
		return type;
	}

	public void setType(QueryType type) {
		this.type = type;
	}

}
