package org.oxerr.okcoin.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BorrowResult extends ErrorResult {

	private static final long serialVersionUID = 2015022001L;

	private final long borrowId;

	public BorrowResult(
		@JsonProperty("result") boolean result,
		@JsonProperty("borrow_id") long borrowId) {
		super(result);
		this.borrowId = borrowId;
	}

	public long getBorrowId() {
		return borrowId;
	}

}
