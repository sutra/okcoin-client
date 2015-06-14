package org.oxerr.okcoin.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderFee extends ErrorResult {

	private static final long serialVersionUID = 2015060701L;

	private final OrderFeeData data;

	public OrderFee(
		@JsonProperty("result") boolean result,
		@JsonProperty("data") OrderFeeData data) {
		super(result);
		this.data = data;
	}

	public OrderFeeData getData() {
		return data;
	}

}
