package org.oxerr.okcoin.rest.dto.valuereader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.io.IOUtils;
import org.cyberneko.html.parsers.DOMParser;
import org.oxerr.okcoin.rest.service.web.OKCoinClient;
import org.oxerr.okcoin.rest.service.web.OKCoinClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.html.HTMLDocument;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public abstract class HtmlPageReader<T> implements ValueReader<T> {

	private static final Charset CHARSET = Charset.forName(OKCoinClient.ENCODING);

	private final Logger log = LoggerFactory.getLogger(IndexHtmlPageReader.class);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T read(InputStream inputStream,
		@Nullable String mimeType, @Nullable Charset charset) throws IOException {
		HTMLDocument document;
		try {
			document = toDocument(inputStream, charset != null ? charset: CHARSET);
		} catch (SAXException e) {
			throw new IOException(e);
		}
		return read(document);
	}

	protected abstract T read(HTMLDocument document) throws OKCoinClientException;

	private HTMLDocument toDocument(InputStream inputStream, @Nonnull Charset charset)
			throws IOException, SAXException {
		final InputSource inputSource;
		if (log.isTraceEnabled()) {
			String html = IOUtils.toString(inputStream, charset);
			log.trace("Parsing HTML:\n{}", html);
			inputSource = new InputSource(new InputStreamReader(
					IOUtils.toInputStream(html, charset), charset));
		} else {
			inputSource = new InputSource(new InputStreamReader(inputStream,
					charset));
		}
		DOMParser parser = new DOMParser();
		parser.parse(inputSource);
		HTMLDocument document = (HTMLDocument) parser.getDocument();
		return document;
	}

}
