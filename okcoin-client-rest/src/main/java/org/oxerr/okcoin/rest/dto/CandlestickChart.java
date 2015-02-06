package org.oxerr.okcoin.rest.dto;

import org.oxerr.okcoin.rest.dto.deserializer.CandlestickChartDeserializer;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = CandlestickChartDeserializer.class)
public class CandlestickChart extends BaseObject {

	private static final long serialVersionUID = 2015020401L;

	private final Candlestick[] candlesticks;

	public CandlestickChart(Candlestick[] candlesticks) {
		this.candlesticks = candlesticks;
	}

	public Candlestick[] getCandlesticks() {
		return candlesticks;
	}

}
