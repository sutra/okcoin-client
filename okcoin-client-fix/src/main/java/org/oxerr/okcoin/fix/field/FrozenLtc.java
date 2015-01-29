package org.oxerr.okcoin.fix.field;

import java.math.BigDecimal;

import quickfix.DecimalField;

public class FrozenLtc extends DecimalField {

	private static final long serialVersionUID = 20150129L;

	public static final int FIELD = 8105;

	public FrozenLtc() {
		super(FIELD);
	}

	public FrozenLtc(BigDecimal data) {
		super(FIELD, data);
	}

	public FrozenLtc(double data) {
		super(FIELD, new BigDecimal(data));
	}

}
