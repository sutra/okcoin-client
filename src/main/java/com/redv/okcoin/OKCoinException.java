package com.redv.okcoin;

import com.xeiam.xchange.ExchangeException;

public class OKCoinException extends ExchangeException {

	private static final long serialVersionUID = 20140614L;

	private final int errorCode;

	/**
	 * @param message
	 */
	public OKCoinException(int errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

	/**
	 * @return the errorCode
	 */
	public int getErrorCode() {
		return errorCode;
	}

}
