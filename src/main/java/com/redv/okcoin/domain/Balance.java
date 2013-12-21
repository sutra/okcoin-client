package com.redv.okcoin.domain;

import java.math.BigDecimal;

public class Balance extends AbstractObject {

	private static final long serialVersionUID = 2013113001L;

	private BigDecimal cny;

	private BigDecimal cnyFreez;

	private BigDecimal btc;

	private BigDecimal btcFreez;

	private BigDecimal ltc;

	private BigDecimal ltcFreez;

	/**
	 * Total in CNY.
	 */
	private BigDecimal total;

	public Balance() {
	}

	public Balance(
			BigDecimal cny,
			BigDecimal cnyFreez,
			BigDecimal btc,
			BigDecimal btcFreez,
			BigDecimal ltc,
			BigDecimal ltcFreez,
			BigDecimal total) {
		this.cny = cny;
		this.cnyFreez = cnyFreez;
		this.btc = btc;
		this.btcFreez = btcFreez;
		this.ltc = ltc;
		this.ltcFreez = ltcFreez;
		this.total = total;
	}

	/**
	 * @return the cny
	 */
	public BigDecimal getCny() {
		return cny;
	}

	/**
	 * @return the cnyFreez
	 */
	public BigDecimal getCnyFreez() {
		return cnyFreez;
	}

	/**
	 * @return the btc
	 */
	public BigDecimal getBtc() {
		return btc;
	}

	/**
	 * @return the btcFreez
	 */
	public BigDecimal getBtcFreez() {
		return btcFreez;
	}

	/**
	 * @return the ltc
	 */
	public BigDecimal getLtc() {
		return ltc;
	}

	/**
	 * @return the ltcFreez
	 */
	public BigDecimal getLtcFreez() {
		return ltcFreez;
	}

	/**
	 * @return the total
	 */
	public BigDecimal getTotal() {
		return total;
	}

}
