package com.redv.okcoin.domain;

import java.math.BigDecimal;
import java.util.Map;

public class Funds extends AbstractObject {

	private static final long serialVersionUID = 20140614L;

	private Map<String, BigDecimal> free;

	private Map<String, BigDecimal> freezed;

	public Map<String, BigDecimal> getFree() {
		return free;
	}

	public void setFree(Map<String, BigDecimal> free) {
		this.free = free;
	}

	public Map<String, BigDecimal> getFreezed() {
		return freezed;
	}

	public void setFreezed(Map<String, BigDecimal> freezed) {
		this.freezed = freezed;
	}

}
