package com.redv.okcoin.valuereader;

import java.io.IOException;
import java.io.InputStream;

public class VoidValueReader implements ValueReader<Void>{

	private static final VoidValueReader INSTANCE = new VoidValueReader();

	public static final VoidValueReader getInstance() {
		return INSTANCE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Void read(InputStream inputStream) throws IOException {
		return null;
	}

}
