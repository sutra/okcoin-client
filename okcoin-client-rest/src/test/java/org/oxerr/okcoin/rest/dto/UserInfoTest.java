package org.oxerr.okcoin.rest.dto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.math.BigDecimal;

import org.junit.Test;
import org.oxerr.okcoin.rest.OKCoinException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class UserInfoTest extends UnmarshalTest {

	@Test
	public void test() throws JsonParseException, JsonMappingException, IOException {
		UserInfo userInfo = readValue(
				"userinfo.json",
				UserInfo.class);

		assertEquals(new BigDecimal("1000"), userInfo.getInfo().getFunds().getFree().get("cny"));
		assertEquals(new BigDecimal("10"), userInfo.getInfo().getFunds().getFree().get("btc"));
		assertEquals(new BigDecimal("0"), userInfo.getInfo().getFunds().getFree().get("ltc"));

		assertEquals(new BigDecimal("1000"), userInfo.getInfo().getFunds().getFrozen().get("cny"));
		assertEquals(new BigDecimal("10"), userInfo.getInfo().getFunds().getFrozen().get("btc"));
		assertEquals(new BigDecimal("0"), userInfo.getInfo().getFunds().getFrozen().get("ltc"));
	}

	@Test
	public void testError() throws JsonParseException, JsonMappingException,
			IOException {
		try {
			readValue("userinfo-error.json", UserInfo.class);
			fail("A JsonMappingException should be thrown.");
		} catch (JsonMappingException e) {
			assertEquals(10000, ((OKCoinException) e.getCause()).getErrorCode().intValue());
		}
	}

}
