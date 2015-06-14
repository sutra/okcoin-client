package org.oxerr.okcoin.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UnrepaymentsInfo extends ErrorResult {

	private static final long serialVersionUID = 2015022001L;

	private final BorrowOrder[] unrepayments;

	public UnrepaymentsInfo(
		@JsonProperty("unrepayments") BorrowOrder[] unrepayments,
		@JsonProperty("result") boolean result) {
		super(result);
		this.unrepayments = unrepayments;
	}

	public BorrowOrder[] getUnrepayments() {
		return unrepayments;
	}

}
