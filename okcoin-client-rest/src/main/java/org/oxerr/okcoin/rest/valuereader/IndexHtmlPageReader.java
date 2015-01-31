package org.oxerr.okcoin.rest.valuereader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.cyberneko.html.parsers.DOMParser;
import org.oxerr.okcoin.rest.LoginRequiredException;
import org.oxerr.okcoin.rest.OKCoinClient;
import org.oxerr.okcoin.rest.domain.Balance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLDivElement;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLLIElement;
import org.w3c.dom.html.HTMLUListElement;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class IndexHtmlPageReader implements ValueReader<Balance>{

	private static final String ENCODING = OKCoinClient.ENCODING;

	private final Logger log = LoggerFactory.getLogger(IndexHtmlPageReader.class);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Balance read(InputStream inputStream) throws IOException {
		try {
			return parse(inputStream);
		} catch (SAXException e) {
			throw new IOException(e);
		}
	}

	public Balance parse(InputStream inputStream) throws IOException, SAXException {
		BigDecimal cny = null;
		BigDecimal cnyFreez = null;
		BigDecimal btc = null;
		BigDecimal btcFreez = null;
		BigDecimal ltc = null;
		BigDecimal ltcFreez = null;
		BigDecimal total = null;

		HTMLDocument doc = toDocument(inputStream);
		NodeList divNodeList = doc.getElementsByTagName("div");
		log.debug("divNodeList.length: {}", divNodeList.getLength());
		for (int i = 0; i < divNodeList.getLength(); i++) {
			HTMLDivElement divNode = (HTMLDivElement) divNodeList.item(i);
			String className = divNode.getClassName();
			log.debug("className: {}", className);
			if (StringUtils.equals(className, "accountinfo1")) {
				log.debug("accountinfo1 div: {}", divNode);

				NodeList divs = divNode.getElementsByTagName("div");
				HTMLDivElement nav2up1 = (HTMLDivElement) divs.item(0);
				HTMLDivElement nav2center = (HTMLDivElement) divs.item(1);

				log.debug("nav2-up1: {}", nav2up1);
				log.debug("nav2-center: {}", nav2up1);

				HTMLUListElement ul = (HTMLUListElement) nav2up1
						.getElementsByTagName("ul").item(0);
				NodeList liNodeList = ul.getElementsByTagName("li");
				for (int j = 0; j < liNodeList.getLength(); j++) {
					HTMLLIElement li = (HTMLLIElement) liNodeList.item(j);
					NodeList spanNodeList = li.getElementsByTagName("span");
					String previousText = StringUtils.EMPTY;
					for (int k = 0; k < spanNodeList.getLength(); k++) {
						HTMLElement span = (HTMLElement) spanNodeList.item(k);
						String text = span.getTextContent();
						switch (previousText) {
						case "CNY:":
							if (cny == null) {
								cny = new BigDecimal(text);
							} else {
								cnyFreez = new BigDecimal(text);
							}
							break;
						case "BTC:":
							if (btc == null) {
								btc = new BigDecimal(text);
							} else {
								btcFreez = new BigDecimal(text);
							}
							break;
						case "LTC:":
							if (ltc == null) {
								ltc = new BigDecimal(text);
							} else {
								ltcFreez = new BigDecimal(text);
							}
							break;
						default:
							break;
						}
						previousText = text;
					}
				}

				NodeList spans = nav2center.getElementsByTagName("span");
				String previousText = StringUtils.EMPTY;
				for (int j = 0; j < spans.getLength(); j++) {
					String text = spans.item(j).getTextContent();
					if (previousText.equals("CNY:")) {
						total = new BigDecimal(text);
					}
					previousText = text;
				}

				break;
			}
		}

		if (cny == null && cnyFreez == null && btc == null && btcFreez == null && ltc == null && ltcFreez == null && total == null) {
			throw new LoginRequiredException();
		}

		return new Balance(cny, cnyFreez, btc, btcFreez, ltc, ltcFreez, total);
	}


	private HTMLDocument toDocument(InputStream inputStream)
			throws IOException, SAXException {
		final InputSource inputSource;
		if (log.isDebugEnabled()) {
			String html = IOUtils.toString(inputStream, ENCODING);
			log.debug("Parsing HTML:\n{}", html);
			inputSource = new InputSource(new InputStreamReader(
					IOUtils.toInputStream(html, ENCODING), ENCODING));
		} else {
			inputSource = new InputSource(new InputStreamReader(inputStream,
					ENCODING));
		}
		DOMParser parser = new DOMParser();
		parser.parse(inputSource);
		HTMLDocument document = (HTMLDocument) parser.getDocument();
		return document;
	}

}
