package com.redv.okcoin.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorResult extends AbstractObject {

	private static final long serialVersionUID = 20140614L;

	private final boolean result;

	private final int errorCode;

	public ErrorResult(
			@JsonProperty("result") final boolean result,
			@JsonProperty("errorCode") final int errorCode) {
		this.result = result;
		this.errorCode = errorCode;
	}

	public boolean isResult() {
		return result;
	}

	public int getErrorCode() {
		return errorCode;
	}

}
