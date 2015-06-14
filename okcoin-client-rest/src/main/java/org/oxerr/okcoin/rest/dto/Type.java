package org.oxerr.okcoin.rest.dto;

import java.util.Arrays;

/**
 * Order type.
 */
public enum Type {

	BUY("buy"), SELL("sell"),
	BUY_MARKET("buy_market"), SELL_MARKET("sell_market");

	public static Type of(String code) {
		return Arrays
			.stream(values())
			.filter(type -> type.code.equals(code))
			.findFirst()
			.get();
	}

	private final String code;

	Type(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	@Override
	public String toString() {
		return code;
	}

}
