package com.redv.okcoin.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TickerResponse extends AbstractObject {

	private static final long serialVersionUID = 2013122001L;

	private final Ticker ticker;

	public TickerResponse(@JsonProperty("ticker") Ticker ticker) {
		this.ticker = ticker;
	}

	public Ticker getTicker() {
		return ticker;
	}

}
