package org.oxerr.okcoin.rest.service.web;

import org.oxerr.okcoin.rest.OKCoinException;
import org.oxerr.okcoin.rest.dto.Result;

public class OKCoinClientException extends OKCoinException {

	private static final long serialVersionUID = 2013122001L;

	public OKCoinClientException() {
	}

	public OKCoinClientException(String message) {
		super(message);
	}

	public OKCoinClientException(String message, Throwable cause) {
		super(message, cause);
	}

	public OKCoinClientException(Result result) {
		super(String.format("ErrorNum: %d, ResultCode: %d.",
			result.getErrorNum(), result.getResultCode()));
	}

}
