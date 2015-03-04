package org.oxerr.okcoin.websocket.dto;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

abstract class BaseObject implements Serializable {

	private static final long serialVersionUID = 2015022601L;

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
