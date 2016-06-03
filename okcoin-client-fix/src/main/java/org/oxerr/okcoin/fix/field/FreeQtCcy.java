package org.oxerr.okcoin.fix.field;

import java.math.BigDecimal;

import quickfix.DecimalField;

/**
 * Available Quote Currency Balance(CNY in China domestic site, USD in international site).
 */
public class FreeQtCcy extends DecimalField {

	private static final long serialVersionUID = 20150129L;

	public static final int FIELD = 8103;

	public FreeQtCcy() {
		super(FIELD);
	}

	public FreeQtCcy(BigDecimal data) {
		super(FIELD, data);
	}

	public FreeQtCcy(double data) {
		super(FIELD, BigDecimal.valueOf(data));
	}

}
