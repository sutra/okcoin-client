package org.oxerr.okcoin.rest.dto;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

public class BatchTradeResultTest extends UnmarshalTest {

	@Test
	public void test() throws IOException {
		BatchTradeResult result = readValue("batch_trade.json", BatchTradeResult.class);
		assertEquals(41724206L, result.getOrderInfo()[0].getOrderId());

		assertEquals(Integer.valueOf(10011), result.getOrderInfo()[1].getErrorCode());
		assertEquals(-1, result.getOrderInfo()[1].getOrderId());

		assertEquals(Integer.valueOf(10014), result.getOrderInfo()[2].getErrorCode());
		assertEquals(-1, result.getOrderInfo()[2].getOrderId());
	}

}
