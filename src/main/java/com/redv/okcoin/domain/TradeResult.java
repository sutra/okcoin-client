package com.redv.okcoin.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TradeResult extends ErrorResult {

	private static final long serialVersionUID = 20140614L;

	private long orderId;

	public long getOrderId() {
		return orderId;
	}

	@JsonProperty("order_id")
	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

}
