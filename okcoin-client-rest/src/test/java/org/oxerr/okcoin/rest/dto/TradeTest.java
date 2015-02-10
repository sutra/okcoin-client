package org.oxerr.okcoin.rest.dto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;
import org.oxerr.okcoin.rest.OKCoinException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * Test for parsing the result of /api/trade.do.
 */
public class TradeTest extends UnmarshalTest {

	@Test
	public void test() throws JsonParseException, JsonMappingException,
			IOException {
		TradeResult tradeResult = readValue("trade.json", TradeResult.class);

		assertEquals(123456L, tradeResult.getOrderId());
	}

	@Test
	public void testError() throws JsonParseException, JsonMappingException,
			IOException {
		try {
			readValue("trade-error.json", TradeResult.class);
			fail("A JsonMappingException should be thrown.");
		} catch (JsonMappingException e) {
			assertEquals(10000, ((OKCoinException) e.getCause()).getErrorCode().intValue());
		}
	}

}
