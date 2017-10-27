package org.oxerr.okcoin.rest.dto.valuereader;

import static org.apache.http.HttpStatus.SC_OK;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.util.EntityUtils;

public interface ValueReader<T> {

	default T read(HttpResponse response) throws IOException {
		final StatusLine statusLine = response.getStatusLine();
		if (statusLine.getStatusCode() == SC_OK) {
			HttpEntity entity = response.getEntity();
			try (InputStream content = entity.getContent()) {
				return read(content);
			} finally {
				EntityUtils.consume(entity);
			}
		} else {
			throw new IOException(statusLine.getReasonPhrase());
		}
	}

	T read(InputStream inputStream) throws IOException;

}
