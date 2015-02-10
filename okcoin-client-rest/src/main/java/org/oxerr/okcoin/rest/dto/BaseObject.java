package org.oxerr.okcoin.rest.dto;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

abstract class BaseObject implements Serializable {

	private static final long serialVersionUID = 2013112501L;

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
