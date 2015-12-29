package org.oxerr.okcoin.rest.dto;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Funds extends BaseObject {

	private static final long serialVersionUID = 20140614L;

	private final Map<String, BigDecimal> asset;
	private final Map<String, BigDecimal> borrow;
	private final Map<String, BigDecimal> free;
	private final Map<String, BigDecimal> frozen;
	private final Map<String, BigDecimal> unionFund;

	public Funds(
			@JsonProperty("asset") final Map<String, BigDecimal> asset,
			@JsonProperty("borrow") final Map<String, BigDecimal> borrow,
			@JsonProperty("free") final Map<String, BigDecimal> free,
			@JsonProperty("freezed") final Map<String, BigDecimal> frozen,
			@JsonProperty("union_fund") final Map<String, BigDecimal> unionFund) {
		this.asset     = nonNull(asset);
		this.borrow    = nonNull(borrow);
		this.free      = nonNull(free);
		this.frozen    = nonNull(frozen);
		this.unionFund = nonNull(unionFund);
	}

	private static Map<String, BigDecimal> nonNull(final Map<String, BigDecimal> map) {
		return map == null ? Collections.emptyMap() : map;
	}

	/**
	 * Returns the account funds, including net fund and total fund.
	 *
	 * @return the account funds, including net fund and total fund.
	 */
	public Map<String, BigDecimal> getAsset() {
		return asset;
	}

	/**
	 * Returns the account borrowing information (only returned when this
	 * field is not null).
	 *
	 * @return the account borrowing information.
	 */
	public Map<String, BigDecimal> getBorrow() {
		return borrow;
	}

	/**
	 * Returns the available fund.
	 *
	 * @return the available fund.
	 */
	public Map<String, BigDecimal> getFree() {
		return free;
	}

	/**
	 * Returns the frozen funds.
	 *
	 * @return the frozen funds.
	 */
	public Map<String, BigDecimal> getFrozen() {
		return frozen;
	}

	/**
	 * Returns the fund management information (only returned when this
	 * field is not null).
	 *
	 * @return the fund management information.
	 */
	public Map<String, BigDecimal> getUnionFund() {
		return unionFund;
	}

}
