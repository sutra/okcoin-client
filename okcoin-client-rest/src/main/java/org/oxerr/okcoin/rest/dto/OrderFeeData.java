package org.oxerr.okcoin.rest.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderFeeData extends BaseObject {

	private static final long serialVersionUID = 2015060701L;

	private final BigDecimal fee;
	private final long orderId;
	private final String type;

	public OrderFeeData(
		@JsonProperty("fee") BigDecimal fee,
		@JsonProperty("order_id") long orderId,
		@JsonProperty("type") String type) {
		this.fee = fee;
		this.orderId = orderId;
		this.type = type;
	}

	public BigDecimal getFee() {
		return fee;
	}

	public long getOrderId() {
		return orderId;
	}

	public String getType() {
		return type;
	}

}
