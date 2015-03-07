package org.oxerr.okcoin.rest.dto;

import java.util.Arrays;

/**
 * Order status.
 */
public enum Status {

	CANCELLED(-1), UNFILLED(0), PARTIALLY_FILLED(1), FULLY_FILLED(2),
	CANCEL_REQUEST_IN_PROCESS(4);

	public static Status of(int code) {
		return Arrays
			.stream(Status.values())
			.filter(status -> status.code == code)
			.findFirst()
			.get();
	}

	private int code;

	Status(int code) {
		this.code = code;
	}

}
