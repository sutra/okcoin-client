package org.oxerr.okcoin.rest.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.math.BigDecimal;

import org.junit.Test;
import org.oxerr.okcoin.rest.domain.UserInfo;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class UserInfoTest extends UnmarshalTest {

	@Test
	public void test() throws JsonParseException, JsonMappingException, IOException {
		UserInfo userInfo = readValue(
				"userinfo.json",
				UserInfo.class);

		assertTrue(userInfo.isResult());

		assertEquals(new BigDecimal("1000"), userInfo.getInfo().getFunds().getFree().get("cny"));
		assertEquals(new BigDecimal("10"), userInfo.getInfo().getFunds().getFree().get("btc"));
		assertEquals(new BigDecimal("0"), userInfo.getInfo().getFunds().getFree().get("ltc"));

		assertEquals(new BigDecimal("1000"), userInfo.getInfo().getFunds().getFreezed().get("cny"));
		assertEquals(new BigDecimal("10"), userInfo.getInfo().getFunds().getFreezed().get("btc"));
		assertEquals(new BigDecimal("0"), userInfo.getInfo().getFunds().getFreezed().get("ltc"));
	}

	@Test
	public void testError() throws JsonParseException, JsonMappingException,
			IOException {
		UserInfo userInfo = readValue("userinfo-error.json", UserInfo.class);

		assertFalse(userInfo.isResult());
		assertEquals(10000, userInfo.getErrorCode());
	}

}
