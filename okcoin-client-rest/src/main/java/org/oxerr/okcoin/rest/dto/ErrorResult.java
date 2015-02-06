package org.oxerr.okcoin.rest.dto;

import org.oxerr.okcoin.rest.OKCoinException;

abstract class ErrorResult extends BaseObject {

	private static final long serialVersionUID = 20140614L;

	public ErrorResult(final boolean result) {
		if (!result) {
			throw new OKCoinException();
		}
	}

	public ErrorResult(final boolean result, final int errorCode) {
		if (!result) {
			throw new OKCoinException(errorCode);
		}
	}
}
