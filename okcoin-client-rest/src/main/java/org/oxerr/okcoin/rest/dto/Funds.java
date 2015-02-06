package org.oxerr.okcoin.rest.dto;

import java.math.BigDecimal;
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
		this.asset = asset;
		this.borrow = borrow;
		this.free = free;
		this.frozen = frozen;
		this.unionFund = unionFund;
	}

	public Map<String, BigDecimal> getAsset() {
		return asset;
	}

	public Map<String, BigDecimal> getBorrow() {
		return borrow;
	}

	public Map<String, BigDecimal> getFree() {
		return free;
	}

	public Map<String, BigDecimal> getFrozen() {
		return frozen;
	}

	public Map<String, BigDecimal> getUnionFund() {
		return unionFund;
	}

}
