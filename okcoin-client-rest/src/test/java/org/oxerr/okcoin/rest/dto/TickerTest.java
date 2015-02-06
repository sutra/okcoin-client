package org.oxerr.okcoin.rest.dto;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class TickerTest extends UnmarshalTest {

	@Test
	public void test() throws JsonParseException, JsonMappingException,
			IOException {
		TickerResponse tickerResponse = readValue("ticker.json",
				TickerResponse.class);
		assertEquals(Instant.ofEpochSecond(1410431279L),
				tickerResponse.getDate());
		Ticker ticker = tickerResponse.getTicker();
		assertEquals(new BigDecimal("34.15"), ticker.getHigh());
		assertEquals(new BigDecimal("32.05"), ticker.getLow());
		assertEquals(new BigDecimal("33.15"), ticker.getBuy());
		assertEquals(new BigDecimal("33.16"), ticker.getSell());
		assertEquals(new BigDecimal("33.15"), ticker.getLast());
		assertEquals(new BigDecimal("10532696.39199642"), ticker.getVol());
	}

}
