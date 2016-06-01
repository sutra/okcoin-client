package org.oxerr.okcoin.fix.fix44;

import org.oxerr.okcoin.fix.field.ExceptionMessage;

import quickfix.FieldNotFound;
import quickfix.field.MsgType;
import quickfix.field.Text;
import quickfix.fix44.Message;

/**
 * Used for OKCoin to return exceptions.
 */
public class ExceptionResponseMessage extends Message {

	private static final long serialVersionUID = 20160303L;

	public static final String MSGTYPE = "E1000";

	public ExceptionResponseMessage() {
		getHeader().setField(new MsgType(MSGTYPE));
	}

	public void set(Text value) {
		setField(value);
	}

	public Text get(Text value) throws FieldNotFound {
		getField(value);
		return value;
	}

	public Text getText() throws FieldNotFound {
		Text value = new Text();
		getField(value);
		return value;
	}

	public boolean isSet(Text field) {
		return isSetField(field);
	}

	public boolean isSetText() {
		return isSetField(Text.FIELD);
	}

	public void set(ExceptionMessage value) {
		setField(value);
	}

	public ExceptionMessage get(ExceptionMessage value) throws FieldNotFound {
		getField(value);
		return value;
	}

	public ExceptionMessage getExceptionMessage() throws FieldNotFound {
		ExceptionMessage value = new ExceptionMessage();
		getField(value);
		return value;
	}

	public boolean isSet(ExceptionMessage field) {
		return isSetField(field);
	}

	public boolean isSetExceptionMessage() {
		return isSetField(ExceptionMessage.FIELD);
	}

}
