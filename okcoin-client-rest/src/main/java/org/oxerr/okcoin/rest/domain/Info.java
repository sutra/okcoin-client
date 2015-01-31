package org.oxerr.okcoin.rest.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Info extends AbstractObject {

	private static final long serialVersionUID = 20140614L;

	private final Funds funds;

	public Info(@JsonProperty("funds") final Funds funds) {
		this.funds = funds;
	}

	public Funds getFunds() {
		return funds;
	}

}
