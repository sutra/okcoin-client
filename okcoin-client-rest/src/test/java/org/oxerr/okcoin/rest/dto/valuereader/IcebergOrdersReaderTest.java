package org.oxerr.okcoin.rest.dto.valuereader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.oxerr.okcoin.rest.dto.IcebergOrder;
import org.oxerr.okcoin.rest.dto.IcebergOrderHistory;
import org.oxerr.okcoin.rest.dto.Status;
import org.oxerr.okcoin.rest.dto.Type;
import org.oxerr.okcoin.rest.service.web.LoginRequiredException;
import org.oxerr.okcoin.rest.service.web.OKCoinClient;

public class IcebergOrdersReaderTest {

	private final IcebergOrdersReader reader = new IcebergOrdersReader();

	@Test
	public void testNotLoggedIn() throws IOException {
		try (InputStream inputStream = IOUtils.toInputStream("", OKCoinClient.ENCODING)) {
			reader.read(inputStream, null, StandardCharsets.UTF_8);
			fail("A LoginRequiredException should be thrown.");
		} catch (LoginRequiredException e) {
			assertEquals("No HTML table found.", e.getMessage());
		}
	}

	@Test
	public void testIcebergOrders0() throws IOException {
		try (InputStream inputStream = getClass().getResourceAsStream(
				"open-iceberg-orders-0.html")) {
			IcebergOrder[] orders = reader.read(inputStream, null, StandardCharsets.UTF_8).getOrders();
			assertEquals(0, orders.length);
		}
	}

	@Test
	public void testIcebergOrders1() throws IOException {
		try (InputStream inputStream = getClass().getResourceAsStream(
				"open-iceberg-orders-1.html")) {
			IcebergOrder[] orders = reader.read(inputStream, null, StandardCharsets.UTF_8).getOrders();
			assertEquals(1, orders.length);
			IcebergOrder order = orders[0];
			assertEquals("2015-02-15T09:53:28Z", order.getDate().toString());
			assertEquals(Type.BUY, order.getSide());
			assertEquals(new BigDecimal("100"), order.getTradeValue());
			assertEquals(new BigDecimal("1"), order.getSingleAvg());
			assertEquals(new BigDecimal("0.1"), order.getDepthRange());
			assertEquals(new BigDecimal("1"), order.getProtectedPrice());
			assertEquals(new BigDecimal("0"), order.getFilled());
			assertEquals(12732L, order.getId());
		}
	}

	@Test
	public void testIcebergOrderHistory() throws IOException {
		try (InputStream inputStream = getClass().getResourceAsStream("order-history-iceberg-orders.html")) {
			IcebergOrderHistory history = reader.read(inputStream, null, StandardCharsets.UTF_8);
			IcebergOrder[] orders = history.getOrders();
			assertEquals(10, orders.length);
			assertEquals("2015-02-17T14:23:25Z", orders[0].getDate().toString());
			assertEquals(Type.BUY, orders[0].getSide());
			assertEquals(new BigDecimal("100"), orders[0].getTradeValue());
			assertEquals(new BigDecimal("1"), orders[0].getSingleAvg());
			assertEquals(new BigDecimal("0.1"), orders[0].getDepthRange());
			assertEquals(new BigDecimal("1"), orders[0].getProtectedPrice());
			assertEquals(new BigDecimal("0"), orders[0].getFilled());
			assertEquals(Status.CANCELLED, orders[0].getStatus());
			assertEquals(12836L, orders[0].getId());

			assertEquals(1, history.getCurrentPage());
			assertTrue(history.hasNextPage());
		}
	}

}
