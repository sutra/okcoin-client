package org.oxerr.okcoin.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.xeiam.xchange.exceptions.ExchangeException;

/**
 * Exception to represent the exception from server side.
 */
public class OKCoinException extends ExchangeException {

	private static final long serialVersionUID = 20140614L;

	private final Integer errorCode;

	public OKCoinException(@JsonProperty("error_code") Integer errorCode) {
		this(errorCode,
			errorCode != null ? Messages.getString(String.valueOf(errorCode)) : null);
	}

	public OKCoinException() {
		this(null, null);
	}

	/**
	 * Constructor.
	 *
	 * @param errorCode the error code.
	 * @param message the exception message.
	 */
	public OKCoinException(Integer errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

	/**
	 * @return the error code.
	 */
	public Integer getErrorCode() {
		return errorCode;
	}

}
