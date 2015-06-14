package org.oxerr.okcoin.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountRecords extends ErrorResult {

	private static final long serialVersionUID = 2015060701L;

	private final AccountRecord[] records;
	private final String symbol;

	public AccountRecords(
		@JsonProperty("result") Boolean result,
		@JsonProperty("error_code") int errorCode,
		@JsonProperty("records") AccountRecord[] records,
		@JsonProperty("symbol") String symbol) {
		super(result, errorCode);
		this.records = records;
		this.symbol = symbol;
	}

	public AccountRecord[] getRecords() {
		return records;
	}

	public String getSymbol() {
		return symbol;
	}

}
