package org.oxerr.okcoin.fix.field;

import java.math.BigDecimal;

import quickfix.DecimalField;

/**
 * Frozen Quote Currency Balance(CNY in China domestic site, USD in international site).
 */
public class FrozenQtCcy extends DecimalField {

	private static final long serialVersionUID = 20150129L;

	public static final int FIELD = 8106;

	public FrozenQtCcy() {
		super(FIELD);
	}

	public FrozenQtCcy(BigDecimal data) {
		super(FIELD, data);
	}

	public FrozenQtCcy(double data) {
		super(FIELD, new BigDecimal(data));
	}

}
