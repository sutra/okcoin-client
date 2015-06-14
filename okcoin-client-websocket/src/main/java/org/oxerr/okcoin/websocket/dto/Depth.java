package org.oxerr.okcoin.websocket.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.stream.Collectors;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;

public class Depth extends org.oxerr.okcoin.rest.dto.Depth {

	private static final long serialVersionUID = 2015022801L;

	private static final BigDecimal[][] EMPTY_DEPTH = new BigDecimal[2][];

	private final Instant timestamp;

	public Depth(JsonValue jsonValue) {
		this((JsonObject) jsonValue);
	}

	public Depth(JsonObject jsonObject) {
		this(jsonObject.getJsonArray("asks"), jsonObject.getJsonArray("bids"),
			Instant.ofEpochMilli(Long.parseLong(jsonObject.getString("timestamp"))));
	}

	public Depth(JsonArray asks, JsonArray bids, Instant timestamp) {
		super(asks.stream().map(v -> {
			JsonArray a = (JsonArray) v;
			BigDecimal px = a.getJsonNumber(0).bigDecimalValue();
			BigDecimal qty = a.getJsonNumber(1).bigDecimalValue();
			return new BigDecimal[] { px, qty };
		}).collect(Collectors.toList()).toArray(EMPTY_DEPTH),

		bids.stream().map(v -> {
			JsonArray a = (JsonArray) v;
			BigDecimal px = a.getJsonNumber(0).bigDecimalValue();
			BigDecimal qty = a.getJsonNumber(1).bigDecimalValue();
			return new BigDecimal[] { px, qty };
		}).collect(Collectors.toList()).toArray(EMPTY_DEPTH));

		this.timestamp = timestamp;
	}

	public Instant getTimestamp() {
		return timestamp;
	}

}
