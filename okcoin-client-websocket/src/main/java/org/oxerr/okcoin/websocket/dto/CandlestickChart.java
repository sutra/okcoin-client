package org.oxerr.okcoin.websocket.dto;

import static java.util.stream.Collectors.toList;

import javax.json.JsonArray;
import javax.json.JsonValue;
import javax.json.JsonValue.ValueType;

public class CandlestickChart extends
		org.oxerr.okcoin.rest.dto.CandlestickChart {

	private static final long serialVersionUID = 2015030501L;

	private static final Candlestick[] EMPTY_CANDLESTICK_ARRAY = new Candlestick[0];

	public CandlestickChart(JsonValue jsonValue) {
		this((JsonArray) jsonValue);
	}

	public CandlestickChart(JsonArray jsonArray) {
		super(parseCandlesticks(jsonArray));
	}

	private static Candlestick[] parseCandlesticks(JsonArray jsonArray) {
		if (jsonArray.get(0).getValueType() == ValueType.ARRAY) {
			// first push contains multiple candlesticks.

			return jsonArray.stream().map(e -> new Candlestick(e))
				.collect(toList())
				.toArray(EMPTY_CANDLESTICK_ARRAY);
		} else {
			// the follows contain only one candlestick.

			return new Candlestick[] { new Candlestick(jsonArray) };
		}
	}

}
