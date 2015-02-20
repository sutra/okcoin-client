package org.oxerr.okcoin.rest.dto;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;

import org.junit.Test;

public class UnrepaymentsInfoTest extends UnmarshalTest {

	@Test
	public void test() throws IOException {
		UnrepaymentsInfo unrepaymentsInfo = readValue("unrepayments_info.json", UnrepaymentsInfo.class);
		BorrowOrder[] unrepayments = unrepaymentsInfo.getUnrepayments();
		BorrowOrder unrepayment = unrepayments[0];
		assertEquals(new BigDecimal("0.1"), unrepayment.getAmount());
		assertEquals(1423475249000L, unrepayment.getBorrowDate().toEpochMilli());
		assertEquals(613388L, unrepayment.getBorrowId());
		assertEquals(15, unrepayment.getDays());
		assertEquals(new BigDecimal("0.1"), unrepayment.getDealAmount());
		assertEquals(new BigDecimal("0.001"), unrepayment.getRate());
		assertEquals(Status.FULLY_FILLED, unrepayment.getStatus());
		assertEquals("btc_cny", unrepayment.getSymbol());
	}

}
