package org.oxerr.okcoin.rest.dto;

import java.math.BigDecimal;
import java.time.Instant;

public class Candlestick extends BaseObject {

	private static final long serialVersionUID = 2015020401L;

	private final Instant timestamp;

	private final BigDecimal open;
	private final BigDecimal high;
	private final BigDecimal low;
	private final BigDecimal close;
	private final BigDecimal volume;

	public Candlestick(Instant timestamp,
		BigDecimal open, BigDecimal high, BigDecimal low, BigDecimal close,
		BigDecimal volume) {
		this.timestamp = timestamp;
		this.open = open;
		this.high = high;
		this.low = low;
		this.close = close;
		this.volume = volume;
	}

	public Instant getTimestamp() {
		return timestamp;
	}

	public BigDecimal getOpen() {
		return open;
	}

	public BigDecimal getHigh() {
		return high;
	}

	public BigDecimal getLow() {
		return low;
	}

	public BigDecimal getClose() {
		return close;
	}

	public BigDecimal getVolume() {
		return volume;
	}

}
