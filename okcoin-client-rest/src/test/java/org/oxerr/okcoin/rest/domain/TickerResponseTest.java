package org.oxerr.okcoin.rest.domain;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;

import org.junit.Test;
import org.oxerr.okcoin.rest.domain.Ticker;
import org.oxerr.okcoin.rest.domain.TickerResponse;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class TickerResponseTest extends UnmarshalTest {

	@Test
	public void test() throws JsonParseException, JsonMappingException,
			IOException {
		TickerResponse tickerResponse = readValue(
				"tickerResponse.json",
				TickerResponse.class);
		Ticker ticker = tickerResponse.getTicker();
		assertEquals(new BigDecimal("809.00"), ticker.getHigh());
		assertEquals(new BigDecimal("770.01"), ticker.getLow());
		assertEquals(new BigDecimal("787.10"), ticker.getBuy());
		assertEquals(new BigDecimal("787.27"), ticker.getSell());
		assertEquals(new BigDecimal("787.00"), ticker.getLast());
		assertEquals(new BigDecimal("2625.38100000"), ticker.getVol());
	}

}
