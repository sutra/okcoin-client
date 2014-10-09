package com.redv.okcoin.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.math.BigDecimal;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OrderResultTest extends UnmarshalTest {

	@Test
	public void testLimitOrders() throws JsonParseException,
			JsonMappingException, IOException {
		OrderResult orderResult = readValue(
				"getorder-limit-orders.json",
				OrderResult.class);

		assertTrue(orderResult.isResult());
		assertEquals(2, orderResult.getOrders().length);

		assertEquals(15088L, orderResult.getOrders()[0].getOrderId());
		assertEquals(0, orderResult.getOrders()[0].getStatus());
		assertEquals("btc_cny", orderResult.getOrders()[0].getSymbol());
		assertEquals("sell", orderResult.getOrders()[0].getType());
		assertEquals(new BigDecimal("811"), orderResult.getOrders()[0].getRate());
		assertEquals(new BigDecimal("1.39901357"), orderResult.getOrders()[0].getAmount());
		assertEquals(new BigDecimal("1"), orderResult.getOrders()[0].getDealAmount());
		assertEquals(new BigDecimal("811"), orderResult.getOrders()[0].getAvgRate());

		assertEquals(15088L, orderResult.getOrders()[1].getOrderId());
		assertEquals(-1, orderResult.getOrders()[1].getStatus());
		assertEquals("btc_cny", orderResult.getOrders()[1].getSymbol());
		assertEquals("sell", orderResult.getOrders()[1].getType());
		assertEquals(new BigDecimal("811"), orderResult.getOrders()[1].getRate());
		assertEquals(new BigDecimal("1.39901357"), orderResult.getOrders()[1].getAmount());
		assertEquals(new BigDecimal("1"), orderResult.getOrders()[1].getDealAmount());
		assertEquals(new BigDecimal("811"), orderResult.getOrders()[1].getAvgRate());
	}

	@Test
	public void testMarketOrders() throws JsonParseException,
			JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		OrderResult orderResult = mapper.readValue(
				getClass().getResource("getorder-market-orders.json"),
				OrderResult.class);

		assertTrue(orderResult.isResult());
		assertEquals(1, orderResult.getOrders().length);

		assertEquals(new BigDecimal("0"), orderResult.getOrders()[0].getAmount());
		assertEquals(new BigDecimal("0"), orderResult.getOrders()[0].getAvgRate());
		assertEquals(1391741134000L, orderResult.getOrders()[0].getCreateDate().getTime());
		assertEquals(new BigDecimal("0"), orderResult.getOrders()[0].getDealAmount());
		assertEquals(66079L, orderResult.getOrders()[0].getOrderId());
		assertEquals(new BigDecimal("4500"), orderResult.getOrders()[0].getRate());
		assertEquals(0, orderResult.getOrders()[0].getStatus());
		assertEquals("buy_market", orderResult.getOrders()[0].getType());
	}

	@Test
	public void testError() throws JsonParseException, JsonMappingException,
			IOException {
		OrderResult orderResult = readValue(
				"getorder-error.json",
				OrderResult.class);

		assertFalse(orderResult.isResult());
		assertEquals(10000, orderResult.getErrorCode());
	}

}
