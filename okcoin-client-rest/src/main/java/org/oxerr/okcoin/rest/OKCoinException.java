package org.oxerr.okcoin.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.xeiam.xchange.exceptions.ExchangeException;

/**
 * Exception to represent the exception from server side.
 */
public class OKCoinException extends ExchangeException {

	private static final long serialVersionUID = 20140614L;

	private final Integer errorCode;

	public OKCoinException() {
		this((Integer) null, (String) null);
	}

	public OKCoinException(@JsonProperty("error_code") Integer errorCode) {
		this(errorCode,
			errorCode != null ? Messages.getString(String.valueOf(errorCode)) : null);
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

	public OKCoinException(String message) {
		this((Integer) null, message);
	}

	public OKCoinException(String message, Throwable cause) {
		super(message, cause);
		this.errorCode = null;
	}

	/**
	 * @return the error code.
	 */
	public Integer getErrorCode() {
		return errorCode;
	}

}
