package com.redv.okcoin.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserInfo extends ErrorResult {

	private static final long serialVersionUID = 20140614L;

	private final Info info;

	public UserInfo(
			@JsonProperty("result") final boolean result,
			@JsonProperty("errorCode") final int errorCode,
			@JsonProperty("info") Info info) {
		super(result, errorCode);
		this.info = info;
	}

	public Info getInfo() {
		return info;
	}

}
