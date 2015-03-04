package org.oxerr.okcoin.websocket.dto;

import java.time.Instant;

import javax.json.JsonArray;
import javax.json.JsonValue;

public class Candlestick extends org.oxerr.okcoin.rest.dto.Candlestick {

	private static final long serialVersionUID = 2015030501L;

	public Candlestick(JsonValue jsonValue) {
		this((JsonArray) jsonValue);
	}

	public Candlestick(JsonArray jsonArray) {
		super(
			Instant.ofEpochMilli(jsonArray.getJsonNumber(0).longValue()),
			jsonArray.getJsonNumber(1).bigDecimalValue(),
			jsonArray.getJsonNumber(2).bigDecimalValue(),
			jsonArray.getJsonNumber(3).bigDecimalValue(),
			jsonArray.getJsonNumber(4).bigDecimalValue(),
			jsonArray.getJsonNumber(5).bigDecimalValue()
		);
	}

}
