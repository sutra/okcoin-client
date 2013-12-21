package com.redv.okcoin.domain;


public class Result extends AbstractObject {

	private static final long serialVersionUID = 2013122001L;

	private int errorNum;

	private int resultCode;

	/**
	 * @return the errorNum
	 */
	public int getErrorNum() {
		return errorNum;
	}

	/**
	 * @param errorNum the errorNum to set
	 */
	public void setErrorNum(int errorNum) {
		this.errorNum = errorNum;
	}

	/**
	 * @return the resultCode
	 */
	public int getResultCode() {
		return resultCode;
	}

	/**
	 * @param resultCode the resultCode to set
	 */
	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}

}
