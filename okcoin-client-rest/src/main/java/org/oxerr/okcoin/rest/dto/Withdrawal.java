package org.oxerr.okcoin.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Withdrawal extends ErrorResult {

	private static final long serialVersionUID = 2015020701L;

	private final long withdrawId;

	public Withdrawal(
		@JsonProperty("result") boolean result,
		@JsonProperty("withdraw_id") long withdrawId) {
		super(result);
		this.withdrawId = withdrawId;
	}

	public long getWithdrawId() {
		return withdrawId;
	}

}
