package org.oxerr.okcoin.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderHistory extends ErrorResult {

	private static final long serialVersionUID = 2015020701L;

	private final int currentPage;
	private final int pageLength;
	private final int total;
	private final Order[] orders;

	public OrderHistory(
		@JsonProperty("result") boolean result,
		@JsonProperty("current_page") int currentPage,
		@JsonProperty("page_length") int pageLength,
		@JsonProperty("total") int total,
		@JsonProperty("orders") Order[] orders) {
		super(result);
		this.currentPage = currentPage;
		this.pageLength = pageLength;
		this.total = total;
		this.orders = orders;
	}

	/**
	 * Returns the current page number.
	 *
	 * @return the current page number.
	 */
	public int getCurrentPage() {
		return currentPage;
	}

	/**
	 * Returns number of orders per page.
	 *
	 * @return number of orders per page.
	 */
	public int getPageLength() {
		return pageLength;
	}

	public int getTotal() {
		return total;
	}

	public Order[] getOrders() {
		return orders;
	}

}
