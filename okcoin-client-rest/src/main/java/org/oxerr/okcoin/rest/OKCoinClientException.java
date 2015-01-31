package org.oxerr.okcoin.rest;

import java.io.IOException;

public class OKCoinClientException extends IOException {

	private static final long serialVersionUID = 2013122001L;

	public OKCoinClientException() {
	}

	public OKCoinClientException(String message) {
		super(message);
	}

	public OKCoinClientException(String message, Throwable cause) {
		super(message, cause);
	}

}
