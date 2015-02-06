package org.oxerr.okcoin.rest.dto;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TypeTest {

	@Test
	public void testToString() {
		assertEquals("buy", Type.BUY.toString());
	}

}
