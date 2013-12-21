package com.redv.okcoin.domain;

public class TickerResponse extends AbstractObject {

	private static final long serialVersionUID = 2013122001L;

	private Ticker ticker;

	public TickerResponse() {
	}

	public TickerResponse(Ticker ticker) {
		this.ticker = ticker;
	}

	public Ticker getTicker() {
		return ticker;
	}

}
