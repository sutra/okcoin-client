package com.redv.okcoin.domain;

import java.math.BigDecimal;

public class Ticker extends AbstractObject {

	private static final long serialVersionUID = 2013122001L;

	private BigDecimal high;

	private BigDecimal low;

	private BigDecimal buy;

	private BigDecimal sell;

	private BigDecimal last;

	private BigDecimal vol;

	public Ticker() {
	}

	public Ticker(BigDecimal high, BigDecimal low, BigDecimal buy,
			BigDecimal sell, BigDecimal last, BigDecimal vol) {
		this.high = high;
		this.low = low;
		this.buy = buy;
		this.sell = sell;
		this.last = last;
		this.vol = vol;
	}

	/**
	 * @return the high
	 */
	public BigDecimal getHigh() {
		return high;
	}

	/**
	 * @return the low
	 */
	public BigDecimal getLow() {
		return low;
	}

	/**
	 * @return the buy
	 */
	public BigDecimal getBuy() {
		return buy;
	}

	/**
	 * @return the sell
	 */
	public BigDecimal getSell() {
		return sell;
	}

	/**
	 * @return the last
	 */
	public BigDecimal getLast() {
		return last;
	}

	/**
	 * @return the vol
	 */
	public BigDecimal getVol() {
		return vol;
	}

}
