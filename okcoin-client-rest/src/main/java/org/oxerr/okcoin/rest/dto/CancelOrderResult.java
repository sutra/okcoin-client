package org.oxerr.okcoin.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CancelOrderResult extends ErrorResult {

	private static final long serialVersionUID = 2015020601L;

	private final Long orderId;
	private final String success;
	private final String error;

	public CancelOrderResult(
		@JsonProperty("result") Boolean result,
		@JsonProperty("order_id") Long orderId,
		@JsonProperty("success") String success,
		@JsonProperty("error") String error) {
		super(result == null ? true : result.booleanValue());
		this.orderId = orderId;
		this.success = success;
		this.error = error;
	}

	/**
	 * Returns the order ID (applicable to single order).
	 *
	 * @return the order ID.
	 */
	public Long getOrderId() {
		return orderId;
	}

	/**
	 * Returns success order IDs.
	 *
	 * @return the order IDs which has been successfully cancelled.
	 */
	public String getSuccess() {
		return success;
	}

	/**
	 * Returns failed order IDs.
	 *
	 * @return the order IDs which cancel failed.
	 */
	public String getError() {
		return error;
	}

}
