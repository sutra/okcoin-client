package org.oxerr.okcoin.rest.dto;

import org.oxerr.okcoin.rest.OKCoinException;

abstract class ErrorResult extends BaseObject {

	private static final long serialVersionUID = 20140614L;

	public ErrorResult(final Boolean result) {
		if (result != null && !result) {
			throw new OKCoinException();
		}
	}

	public ErrorResult(final Boolean result, final int errorCode) {
		if (result != null && !result) {
			throw new OKCoinException(errorCode);
		}
	}

}
