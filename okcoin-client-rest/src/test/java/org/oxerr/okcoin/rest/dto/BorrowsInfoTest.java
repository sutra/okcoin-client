package org.oxerr.okcoin.rest.dto;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;

import org.junit.Test;

public class BorrowsInfoTest extends UnmarshalTest {

	@Test
	public void test() throws IOException {
		BorrowsInfo borrowsInfo = readValue("borrows_info.json", BorrowsInfo.class);
		assertEquals(new BigDecimal("0"), borrowsInfo.getBorrowBtc());
		assertEquals(new BigDecimal("11.06"), borrowsInfo.getBorrowLtc());
		assertEquals(new BigDecimal("0"), borrowsInfo.getBorrowCny());
		assertEquals(new BigDecimal("0.03"), borrowsInfo.getCanBorrow());
		assertEquals(new BigDecimal("0"), borrowsInfo.getInterestBtc());
		assertEquals(new BigDecimal("0.0011"), borrowsInfo.getInterestLtc());
		assertEquals(new BigDecimal("0"), borrowsInfo.getInterestCny());
		assertEquals(new BigDecimal("0"), borrowsInfo.getTodayInterestBtc());
		assertEquals(new BigDecimal("0.0011"), borrowsInfo.getTodayInterestLtc());
		assertEquals(new BigDecimal("0"), borrowsInfo.getTodayInterestCny());
	}

}
