package com.redv.okcoin.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Test for parsing the result of /api/trade.do.
 */
public class TradeTest {

	@Test
	public void test() throws JsonParseException, JsonMappingException,
			IOException {
		ObjectMapper mapper = new ObjectMapper();
		TradeResult tradeResult = mapper.readValue(
				getClass().getResource("trade.json"), TradeResult.class);

		assertTrue(tradeResult.isResult());
		assertEquals(123456L, tradeResult.getOrderId());
	}

	@Test
	public void testError() throws JsonParseException, JsonMappingException,
			IOException {
		ObjectMapper mapper = new ObjectMapper();
		TradeResult tradeResult = mapper.readValue(
				getClass().getResource("trade-error.json"), TradeResult.class);

		assertFalse(tradeResult.isResult());
		assertEquals(10000, tradeResult.getErrorCode());
	}

}
