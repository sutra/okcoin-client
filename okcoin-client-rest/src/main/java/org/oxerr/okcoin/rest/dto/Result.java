package org.oxerr.okcoin.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Result extends BaseObject {

	private static final long serialVersionUID = 2013122001L;

	private final int errorNum;

	private final int resultCode;

	public Result(
			@JsonProperty("errorNum") final int errorNum,
			@JsonProperty("resultCode") final int resultCode) {
		this.errorNum = errorNum;
		this.resultCode = resultCode;
	}

	/**
	 * @return the errorNum
	 */
	public int getErrorNum() {
		return errorNum;
	}

	/**
	 * @return the resultCode
	 */
	public int getResultCode() {
		return resultCode;
	}

}
