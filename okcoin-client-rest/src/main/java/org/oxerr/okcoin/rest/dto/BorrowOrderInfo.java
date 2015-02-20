package org.oxerr.okcoin.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BorrowOrderInfo extends ErrorResult {

	private static final long serialVersionUID = 2015022001L;

	private final BorrowOrder borrowOrder;

	public BorrowOrderInfo(
		@JsonProperty("borrow_order") BorrowOrder borrowOrder,
		@JsonProperty("result") boolean result) {
		super(result);
		this.borrowOrder = borrowOrder;
	}

	public BorrowOrder getBorrowOrder() {
		return borrowOrder;
	}

}
