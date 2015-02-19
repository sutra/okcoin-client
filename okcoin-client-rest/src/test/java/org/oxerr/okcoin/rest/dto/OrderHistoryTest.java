package org.oxerr.okcoin.rest.dto;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;

import org.junit.Test;

public class OrderHistoryTest extends UnmarshalTest {

	@Test
	public void testOrderHistory() throws IOException {
		OrderHistory history = readValue("order_history.json", OrderHistory.class);
		assertEquals(1, history.getCurrentPage());
		assertEquals(1, history.getPageLength());
		assertEquals(3, history.getTotal());
		assertEquals(1, history.getOrders().length);

		Order order = history.getOrders()[0];
		assertEquals(new BigDecimal("0"), order.getAmount());
		assertEquals(new BigDecimal("0"), order.getAvgPrice());
		assertEquals(1405562100000L, order.getCreateDate().toEpochMilli());
		assertEquals(new BigDecimal("0"), order.getDealAmount());
		assertEquals(0, order.getOrderId());
		assertEquals(new BigDecimal("0"), order.getPrice());
		assertEquals(Status.FULLY_FILLED, order.getStatus());
		assertEquals("btc_cny", order.getSymbol());
		assertEquals(Type.SELL, order.getType());
	}

	@Test
	public void testOrderHistoryWithMarketOrders() throws IOException {
		OrderHistory history = readValue("order_history_with_market_orders.json", OrderHistory.class);
		assertEquals(0, history.getCurrentPage());
		assertEquals(10, history.getPageLength());
		assertEquals(4319, history.getTotal());

		// buy market
		Order order = history.getOrders()[1];
		assertEquals(new BigDecimal("0"), order.getAmount());
		assertEquals(new BigDecimal("1467.99"), order.getAvgPrice());
		assertEquals(1424315585000L, order.getCreateDate().toEpochMilli());
		assertEquals(new BigDecimal("0.01"), order.getDealAmount());
		assertEquals(342113073L, order.getOrderId());
		assertEquals(new BigDecimal("15"), order.getPrice());
		assertEquals(Status.FULLY_FILLED, order.getStatus());
		assertEquals("btc_cny", order.getSymbol());
		assertEquals(Type.BUY_MARKET, order.getType());

		// sell market
		order = history.getOrders()[2];
		assertEquals(new BigDecimal("0.01"), order.getAmount());
		assertEquals(new BigDecimal("1467.98"), order.getAvgPrice());
		assertEquals(1424315429000L, order.getCreateDate().toEpochMilli());
		assertEquals(new BigDecimal("0.01"), order.getDealAmount());
		assertEquals(342110762L, order.getOrderId());
		assertEquals(new BigDecimal("0"), order.getPrice());
		assertEquals(Status.FULLY_FILLED, order.getStatus());
		assertEquals("btc_cny", order.getSymbol());
		assertEquals(Type.SELL_MARKET, order.getType());
	}

}
