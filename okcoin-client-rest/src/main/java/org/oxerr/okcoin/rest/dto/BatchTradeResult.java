package org.oxerr.okcoin.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BatchTradeResult extends ErrorResult {

	private static final long serialVersionUID = 2015020501L;

	private final OrderInfo[] orderInfo;

	/**
	 * @param result true indicates order successfully placed.
	 * Return true if any one order is placed successfully.
	 * @param orderInfo order info.
	 */
	public BatchTradeResult(
		@JsonProperty("result") boolean result,
		@JsonProperty("order_info") OrderInfo[] orderInfo
	) {
		super(result);
		this.orderInfo = orderInfo;
	}

	public OrderInfo[] getOrderInfo() {
		return orderInfo;
	}

	public static class OrderInfo extends BaseObject {
		private static final long serialVersionUID = 2015020501L;

		private final Integer errorCode;

		private final long orderId;

		/**
		 * @param errorCode error code.
		 * @param orderId order ID
		 */
		public OrderInfo(
			@JsonProperty("error_code") Integer errorCode,
			@JsonProperty("order_id") long orderId) {
			this.errorCode = errorCode;
			this.orderId = orderId;
		}

		public Integer getErrorCode() {
			return errorCode;
		}

		public long getOrderId() {
			return orderId;
		}

	}

}
