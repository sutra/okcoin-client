package org.oxerr.okcoin.rest.dto.valuereader;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

public class PlainTextReader implements ValueReader<String> {

	private static final PlainTextReader INSTANCE = new PlainTextReader();

	public static PlainTextReader getInstance() {
		return INSTANCE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String read(InputStream inputStream) throws IOException {
		return IOUtils.toString(inputStream);
	}

}
