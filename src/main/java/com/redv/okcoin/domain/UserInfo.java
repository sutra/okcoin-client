package com.redv.okcoin.domain;

public class UserInfo extends ErrorResult {

	private static final long serialVersionUID = 20140614L;

	private Info info;

	public Info getInfo() {
		return info;
	}

	public void setInfo(Info info) {
		this.info = info;
	}

}
