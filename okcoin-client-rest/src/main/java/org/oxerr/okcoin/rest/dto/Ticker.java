package org.oxerr.okcoin.rest.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Ticker extends BaseObject {

	private static final long serialVersionUID = 2013122001L;

	private final BigDecimal high;

	private final BigDecimal low;

	private final BigDecimal buy;

	private final BigDecimal sell;

	private final BigDecimal last;

	private final BigDecimal vol;

	public Ticker(
			@JsonProperty("high") final BigDecimal high,
			@JsonProperty("low") final BigDecimal low,
			@JsonProperty("buy") final BigDecimal buy,
			@JsonProperty("sell") final BigDecimal sell,
			@JsonProperty("last") final BigDecimal last,
			@JsonProperty("vol") final BigDecimal vol) {
		this.high = high;
		this.low = low;
		this.buy = buy;
		this.sell = sell;
		this.last = last;
		this.vol = vol;
	}

	/**
	 * Returns the highest price.
	 *
	 * @return the highest price.
	 */
	public BigDecimal getHigh() {
		return high;
	}

	/**
	 * Returns the lowest price.
	 *
	 * @return the lowest price.
	 */
	public BigDecimal getLow() {
		return low;
	}

	/**
	 * Returns the best bid.
	 *
	 * @return the best bid.
	 */
	public BigDecimal getBuy() {
		return buy;
	}

	/**
	 * Returns the best ask.
	 *
	 * @return the best ask.
	 */
	public BigDecimal getSell() {
		return sell;
	}

	/**
	 * Returns the latest price.
	 *
	 * @return the latest price.
	 */
	public BigDecimal getLast() {
		return last;
	}

	/**
	 * Returns the volume.
	 *
	 * @return the volume (in the last rolling 24 hours).
	 */
	public BigDecimal getVol() {
		return vol;
	}

}
