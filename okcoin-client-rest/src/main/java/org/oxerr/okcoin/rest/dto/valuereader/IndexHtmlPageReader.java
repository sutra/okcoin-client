package org.oxerr.okcoin.rest.dto.valuereader;

import static java.util.Collections.emptyMap;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.oxerr.okcoin.rest.dto.Funds;
import org.oxerr.okcoin.rest.service.web.LoginRequiredException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLDivElement;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLLIElement;
import org.w3c.dom.html.HTMLUListElement;

public class IndexHtmlPageReader extends HtmlPageReader<Funds> {

	private final Logger log = LoggerFactory.getLogger(IndexHtmlPageReader.class);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Funds read(HTMLDocument doc) throws LoginRequiredException {
		BigDecimal cny = null;
		BigDecimal cnyFrozen = null;
		BigDecimal btc = null;
		BigDecimal btcFrozen = null;
		BigDecimal ltc = null;
		BigDecimal ltcFrozen = null;
		BigDecimal netAsset = null;
		BigDecimal allAsset = null;

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
								cnyFrozen = new BigDecimal(text);
							}
							break;
						case "BTC:":
							if (btc == null) {
								btc = new BigDecimal(text);
							} else {
								btcFrozen = new BigDecimal(text);
							}
							break;
						case "LTC:":
							if (ltc == null) {
								ltc = new BigDecimal(text);
							} else {
								ltcFrozen = new BigDecimal(text);
							}
							break;
						default:
							break;
						}
						previousText = text;
					}
				}

				break;
			}
		}

		Element e = doc.getElementById("netasset");
		if (e != null) {
			netAsset = new BigDecimal(e.getTextContent().replace(",", ""));
		}

		e = doc.getElementById("allasset");
		if (e != null) {
			allAsset = new BigDecimal(e.getTextContent().replace(",", ""));
		}

		if (cny == null && cnyFrozen == null && btc == null
				&& btcFrozen == null && ltc == null && ltcFrozen == null
				&& netAsset == null && allAsset == null) {
			throw new LoginRequiredException();
		}

		Map<String, BigDecimal> free = new LinkedHashMap<>();
		Map<String, BigDecimal> frozen = new LinkedHashMap<>();
		Map<String, BigDecimal> asset = new LinkedHashMap<>();
		free.put("btc", btc);
		free.put("ltc", ltc);
		free.put("cny", cny);
		frozen.put("btc", btcFrozen);
		frozen.put("ltc", ltcFrozen);
		frozen.put("cny", cnyFrozen);
		asset.put("net", netAsset);
		asset.put("total", allAsset);
		return new Funds(asset, emptyMap(), free, frozen, emptyMap());
	}

}
