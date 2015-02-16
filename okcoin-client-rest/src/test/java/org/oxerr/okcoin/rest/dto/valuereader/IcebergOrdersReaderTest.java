package org.oxerr.okcoin.rest.dto.valuereader;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;

import org.junit.Test;
import org.oxerr.okcoin.rest.dto.IcebergOrder;

public class IcebergOrdersReaderTest {

	private final IcebergOrdersReader reader = new IcebergOrdersReader();

	@Test
	public void test() throws IOException {
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
