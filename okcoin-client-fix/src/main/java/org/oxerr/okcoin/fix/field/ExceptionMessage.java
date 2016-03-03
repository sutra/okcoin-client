package org.oxerr.okcoin.fix.field;

import quickfix.StringField;

public class ExceptionMessage extends StringField {

	private static final long serialVersionUID = 20160303L;

	public static final int FIELD = 8600;

	public ExceptionMessage() {
		super(FIELD);
	}

	public ExceptionMessage(String data) {
		super(FIELD, data);
	}

}
