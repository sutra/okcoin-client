package org.oxerr.okcoin.rest.dto;

import java.time.Instant;

import org.oxerr.okcoin.rest.dto.deserializer.EpochSecondStringDeserializer;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class TickerResponse extends BaseObject {

	private static final long serialVersionUID = 2015020201L;

	private final Instant date;
	private final Ticker ticker;

	public TickerResponse(
		@JsonProperty("date")
		@JsonDeserialize(using = EpochSecondStringDeserializer.class)
		Instant date,
		@JsonProperty("ticker") Ticker ticker) {
		this.date = date;
		this.ticker = ticker;
	}

	/**
	 * Returns server time for the ticker.
	 *
	 * @return the server time.
	 */
	public Instant getDate() {
		return date;
	}

	public Ticker getTicker() {
		return ticker;
	}

}
