package com.redv.okcoin.domain;

public class Info extends AbstractObject {

	private static final long serialVersionUID = 20140614L;

	private Funds funds;

	public Funds getFunds() {
		return funds;
	}

	public void setFunds(Funds funds) {
		this.funds = funds;
	}

}
