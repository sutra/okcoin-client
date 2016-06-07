package org.oxerr.okcoin.fix.fix44;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import quickfix.FieldNotFound;
import quickfix.InvalidMessage;

public class AccountInfoResponseTest {

	@Test
	public void test() throws IOException, InvalidMessage, FieldNotFound {
		String messageData = getMessageData("Z1001.txt");
		AccountInfoResponse message = new AccountInfoResponse();
		message.fromString(messageData, null, false);
		assertEquals("1234567", message.getAccount().getValue());
		assertEquals("CNY/BTC/LTC", message.getCurrency().getValue());
		assertEquals("8.290051/1.0623/0", message.getBalance().getValue());
	}

	public static String getMessageData(String resource) throws IOException {
		String messageData = IOUtils.toString(AccountInfoResponseTest.class.getResource(resource),
				UTF_8).trim();
		messageData = StringUtils.replace(messageData, "^A", "\1");
		return messageData;
	}

}
