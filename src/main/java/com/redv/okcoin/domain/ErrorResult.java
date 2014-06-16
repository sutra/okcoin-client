package com.redv.okcoin.domain;

public class ErrorResult extends AbstractObject {

	private static final long serialVersionUID = 20140614L;

	private boolean result;

	private int errorCode;

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

}
