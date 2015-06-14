package org.oxerr.okcoin.rest.dto;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

public class BorrowResultTest extends UnmarshalTest {

	@Test
	public void testBorrowMoney() throws IOException {
		BorrowResult borrowOrderResult = readValue("borrow_money.json", BorrowResult.class);
		assertEquals(312L, borrowOrderResult.getBorrowId());
	}

	@Test
	public void testCancelBorrow() throws IOException {
		BorrowResult borrowOrderResult = readValue("cancel_borrow.json", BorrowResult.class);
		assertEquals(312L, borrowOrderResult.getBorrowId());
	}

	@Test
	public void testRepayment() throws IOException {
		BorrowResult borrowOrderResult = readValue("repayment.json", BorrowResult.class);
		assertEquals(613388L, borrowOrderResult.getBorrowId());
	}

}
