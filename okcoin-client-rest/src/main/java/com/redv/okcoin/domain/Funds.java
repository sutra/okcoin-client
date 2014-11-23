package com.redv.okcoin.domain;

import java.math.BigDecimal;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Funds extends AbstractObject {

	private static final long serialVersionUID = 20140614L;

	private final Map<String, BigDecimal> free;

	private final Map<String, BigDecimal> freezed;

	public Funds(
			@JsonProperty("free") final Map<String, BigDecimal> free,
			@JsonProperty("freezed") final Map<String, BigDecimal> freezed) {
		this.free = free;
		this.freezed = freezed;
	}

	public Map<String, BigDecimal> getFree() {
		return free;
	}

	public Map<String, BigDecimal> getFreezed() {
		return freezed;
	}

}
