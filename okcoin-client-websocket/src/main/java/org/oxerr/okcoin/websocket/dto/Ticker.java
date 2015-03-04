package org.oxerr.okcoin.websocket.dto;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.Instant;

import javax.json.JsonObject;
import javax.json.JsonValue;

public class Ticker extends BaseObject {

	private static final long serialVersionUID = 2015022601L;

	private final BigDecimal buy;
	private final BigDecimal high;
	private final BigDecimal last;
	private final BigDecimal low;
	private final BigDecimal sell;
	private final Instant timestamp;
	private final BigDecimal vol;

	public Ticker(BigDecimal buy, BigDecimal high, BigDecimal last,
			BigDecimal low, BigDecimal sell, Instant timestamp, BigDecimal vol) {
		this.buy = buy;
		this.high = high;
		this.last = last;
		this.low = low;
		this.sell = sell;
		this.timestamp = timestamp;
		this.vol = vol;
	}

	public Ticker(JsonValue jsonValue) {
		this((JsonObject) jsonValue);
	}

	public Ticker(JsonObject jsonObject) {
		this.buy = new BigDecimal(jsonObject.getString("buy"));
		this.high = new BigDecimal(jsonObject.getString("high"));
		this.last = new BigDecimal(jsonObject.getString("last"));
		this.low = new BigDecimal(jsonObject.getString("low"));
		this.sell = new BigDecimal(jsonObject.getString("sell"));
		this.timestamp = Instant.ofEpochMilli(Long.parseLong(jsonObject.getString("timestamp")));
		DecimalFormat df = new DecimalFormat("#,###.");
		try {
			this.vol = BigDecimal.valueOf(df.parse(jsonObject.getString("vol")).doubleValue());
		} catch (ParseException e) {
			throw new IllegalArgumentException(e);
		}
	}

	public BigDecimal getBuy() {
		return buy;
	}

	public BigDecimal getHigh() {
		return high;
	}

	public BigDecimal getLast() {
		return last;
	}

	public BigDecimal getLow() {
		return low;
	}

	public BigDecimal getSell() {
		return sell;
	}

	public Instant getTimestamp() {
		return timestamp;
	}

	public BigDecimal getVol() {
		return vol;
	}

}
