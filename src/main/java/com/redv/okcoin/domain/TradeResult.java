package com.redv.okcoin.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TradeResult extends ErrorResult {

	private static final long serialVersionUID = 20140614L;

	private final long orderId;

	public TradeResult(
			@JsonProperty("result") final boolean result,
			@JsonProperty("errorCode") final int errorCode,
			@JsonProperty("order_id") final long orderId) {
		super(result, errorCode);
		this.orderId = orderId;
	}

	public long getOrderId() {
		return orderId;
	}

}
