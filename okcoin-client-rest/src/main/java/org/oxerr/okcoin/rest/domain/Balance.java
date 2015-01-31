package org.oxerr.okcoin.rest.domain;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Balance extends AbstractObject {

	private static final long serialVersionUID = 2013113001L;

	private final BigDecimal cny;

	private final BigDecimal cnyFreez;

	private final BigDecimal btc;

	private final BigDecimal btcFreez;

	private final BigDecimal ltc;

	private final BigDecimal ltcFreez;

	/**
	 * Total in CNY.
	 */
	private final BigDecimal total;

	public Balance(
			@JsonProperty("cny") final BigDecimal cny,
			@JsonProperty("cnyFreez") final BigDecimal cnyFreez,
			@JsonProperty("btc") final BigDecimal btc,
			@JsonProperty("btcFreez") final BigDecimal btcFreez,
			@JsonProperty("ltc") final BigDecimal ltc,
			@JsonProperty("ltcFreez") final BigDecimal ltcFreez,
			@JsonProperty("total") final BigDecimal total) {
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
