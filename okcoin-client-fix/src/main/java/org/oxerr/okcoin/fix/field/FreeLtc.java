package org.oxerr.okcoin.fix.field;

import java.math.BigDecimal;

import quickfix.DecimalField;

public class FreeLtc extends DecimalField {

	private static final long serialVersionUID = 20150129L;

	public static final int FIELD = 8102;

	public FreeLtc() {
		super(FIELD);
	}

	public FreeLtc(BigDecimal data) {
		super(FIELD, data);
	}

	public FreeLtc(double data) {
		super(FIELD, BigDecimal.valueOf(data));
	}

}
