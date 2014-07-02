package com.redv.okcoin.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderResult extends ErrorResult {

	private static final long serialVersionUID = 20140614L;

	private final Order[] orders;

	public OrderResult(
			@JsonProperty("result") final boolean result,
			@JsonProperty("errorCode") final int errorCode,
			@JsonProperty("orders") final Order[] orders) {
		super(result, errorCode);
		this.orders = orders;
	}

	public Order[] getOrders() {
		return orders;
	}

}
