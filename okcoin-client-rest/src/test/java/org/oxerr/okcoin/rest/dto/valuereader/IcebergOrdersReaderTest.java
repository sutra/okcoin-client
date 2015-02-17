package org.oxerr.okcoin.rest.dto.valuereader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.oxerr.okcoin.rest.dto.IcebergOrder;
import org.oxerr.okcoin.rest.service.web.LoginRequiredException;

public class IcebergOrdersReaderTest {

	private final IcebergOrdersReader reader = new IcebergOrdersReader();

	@Test
	public void testNotLoggedIn() throws IOException {
		try (InputStream inputStream = IOUtils.toInputStream("")) {
			reader.read(inputStream);
			fail("A LoginRequiredException should be thrown.");
		} catch (LoginRequiredException e) {
			assertEquals("No HTML table found.", e.getMessage());
		}
	}

	@Test
	public void testIcebergOrders0() throws IOException {
		try (InputStream inputStream = getClass().getResourceAsStream(
				"open-iceberg-orders-0.html")) {
			IcebergOrder[] orders = reader.read(inputStream);
			assertEquals(0, orders.length);
		}
	}

	@Test
	public void testIcebergOrders1() throws IOException {
		try (InputStream inputStream = getClass().getResourceAsStream(
				"open-iceberg-orders-1.html")) {
			IcebergOrder[] orders = reader.read(inputStream);
			assertEquals(1, orders.length);
			IcebergOrder order = orders[0];
			assertEquals("2015-02-15T09:53:28Z", order.getDate().toString());
			assertEquals(new BigDecimal("100"), order.getTradeValue());
			assertEquals(new BigDecimal("1"), order.getSingleAvg());
			assertEquals(new BigDecimal("0.1"), order.getDepthRange());
			assertEquals(new BigDecimal("1"), order.getProtectedPrice());
			assertEquals(new BigDecimal("0"), order.getFilled());
			assertEquals(12732L, order.getId());
		}
	}

}
