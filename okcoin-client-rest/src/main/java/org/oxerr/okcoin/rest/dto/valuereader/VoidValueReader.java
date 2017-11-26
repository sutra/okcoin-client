package org.oxerr.okcoin.rest.dto.valuereader;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Nullable;

import org.apache.http.entity.ContentType;

public class VoidValueReader implements ValueReader<Void>{

	private static final VoidValueReader INSTANCE = new VoidValueReader();

	public static final VoidValueReader getInstance() {
		return INSTANCE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Void read(InputStream inputStream,
		@Nullable ContentType contentType) throws IOException {
		return null;
	}

}
