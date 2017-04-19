package org.oxerr.okcoin.fix.fix44;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import quickfix.ConfigError;
import quickfix.DataDictionary;
import quickfix.FieldNotFound;
import quickfix.InvalidMessage;
import quickfix.field.ExecType;
import quickfix.field.OrdStatus;
import quickfix.fix44.ExecutionReport;

public class ExecutionReportTest {

	@Test
	public void test() throws IOException, InvalidMessage, FieldNotFound, ConfigError {
		String messageData = AccountInfoResponseTest.getMessageData("8.txt");
		ExecutionReport message = new ExecutionReport();
		DataDictionary dataDictionary = new DataDictionary("org/oxerr/okcoin/fix/FIX44.xml");
		message.fromString(messageData, dataDictionary, false);
		assertEquals(ExecType.REJECTED, message.getExecType().getValue());
		assertEquals(OrdStatus.REJECTED, message.getOrdStatus().getValue());
		assertEquals("b80091ca-52f7-43b3-ae8b-ab6ac0ba438a", message.getClOrdID().getValue());
		assertEquals("refused", message.getOrderID().getValue());
	}

}
