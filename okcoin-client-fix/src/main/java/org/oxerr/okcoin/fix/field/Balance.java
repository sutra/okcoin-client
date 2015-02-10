package org.oxerr.okcoin.fix.field;

import quickfix.StringField;

public class Balance extends StringField {

	private static final long serialVersionUID = 20141124L;

	public static final int FIELD = 8001;

	public Balance() {
		super(FIELD);
	}

	public Balance(String data) {
		super(FIELD, data);
	}

}
