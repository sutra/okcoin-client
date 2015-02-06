package org.oxerr.okcoin.rest.dto;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

public class WithdrawalTest extends UnmarshalTest {

	@Test
	public void testWithdrawal() throws IOException {
		Withdrawal withdrawal = readValue("withdraw.json", Withdrawal.class);
		assertEquals(301L, withdrawal.getWithdrawId());
	}

}
