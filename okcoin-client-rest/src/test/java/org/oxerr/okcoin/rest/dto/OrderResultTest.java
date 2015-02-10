package org.oxerr.okcoin.rest.dto;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class OrderResultTest extends UnmarshalTest {

	@Test
	public void testOrderResult() throws JsonParseException,
			JsonMappingException, IOException {
		OrderResult orderResult = readValue(
				"order_info.json",
				OrderResult.class);

		assertEquals(2, orderResult.getOrders().length);

		Order o = orderResult.getOrders()[0];
		assertEquals(new BigDecimal("0.1"), o.getAmount());
		assertEquals(new BigDecimal("0"), o.getAvgPrice());
		assertEquals(1418008467000L, o.getCreateDate().toEpochMilli());
		assertEquals(new BigDecimal("0"), o.getDealAmount());
		assertEquals(10000591L, o.getOrderId());
		assertEquals(new BigDecimal("500"), o.getPrice());
		assertEquals(Status.UNFILLED, o.getStatus());
		assertEquals("btc_cny", o.getSymbol());
		assertEquals(Type.SELL, o.getType());

		o = orderResult.getOrders()[1];
		assertEquals(new BigDecimal("0.2"), o.getAmount());
		assertEquals(new BigDecimal("0"), o.getAvgPrice());
		assertEquals(1417417957000L, o.getCreateDate().toEpochMilli());
		assertEquals(new BigDecimal("0"), o.getDealAmount());
		assertEquals(10000724L, o.getOrderId());
		assertEquals(new BigDecimal("0.1"), o.getPrice());
		assertEquals(Status.UNFILLED, o.getStatus());
		assertEquals("btc_cny", o.getSymbol());
		assertEquals(Type.BUY, o.getType());
	}

}
