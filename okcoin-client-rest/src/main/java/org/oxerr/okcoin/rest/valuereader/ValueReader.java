package org.oxerr.okcoin.rest.valuereader;

import java.io.IOException;
import java.io.InputStream;

public interface ValueReader<T> {

	T read(InputStream inputStream) throws IOException;

}