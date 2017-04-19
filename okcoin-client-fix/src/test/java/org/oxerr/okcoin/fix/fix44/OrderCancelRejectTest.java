package org.oxerr.okcoin.fix.fix44;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import quickfix.ConfigError;
import quickfix.DataDictionary;
import quickfix.FieldNotFound;
import quickfix.InvalidMessage;
import quickfix.field.OrdStatus;
import quickfix.fix44.OrderCancelReject;

public class OrderCancelRejectTest {

	@Test
	public void test() throws IOException, ConfigError, InvalidMessage, FieldNotFound {
		String messageData = AccountInfoResponseTest.getMessageData("9.txt");
		OrderCancelReject message = new OrderCancelReject();
		DataDictionary dataDictionary = new DataDictionary("org/oxerr/okcoin/fix/FIX44.xml");
		message.fromString(messageData, dataDictionary, false);
		assertEquals("3777572314", message.getOrderID().getValue());
		assertEquals(OrdStatus.REJECTED, message.getOrdStatus().getValue());
		assertEquals("3777572314", message.getOrigClOrdID().getValue());
	}

}
