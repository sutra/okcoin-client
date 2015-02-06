package org.oxerr.okcoin.rest.dto.deserializer;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.oxerr.okcoin.rest.dto.Candlestick;
import org.oxerr.okcoin.rest.dto.CandlestickChart;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class CandlestickChartDeserializer extends JsonDeserializer<CandlestickChart> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CandlestickChart deserialize(JsonParser p,
			DeserializationContext ctxt) throws IOException,
			JsonProcessingException {
		List<Candlestick> candlesticks = new ArrayList<>();

		while (p.nextToken() == JsonToken.START_ARRAY) {
			Instant timestamp = Instant.ofEpochMilli(p.nextLongValue(0));
			BigDecimal open = nextBigDecimal(p);
			BigDecimal high = nextBigDecimal(p);
			BigDecimal low = nextBigDecimal(p);
			BigDecimal close = nextBigDecimal(p);
			BigDecimal volume = nextBigDecimal(p);
			p.nextToken(); // END_ARRAY

			Candlestick candlestick = new Candlestick(timestamp, open, high, low, close, volume);
			candlesticks.add(candlestick);
		}

		return new CandlestickChart(candlesticks.toArray(new Candlestick[0]));
	}

	private BigDecimal nextBigDecimal(JsonParser p) throws IOException,
			JsonParseException {
		return p.nextToken().isScalarValue() ? new BigDecimal(p.getText()) : null;
	}

}
