package org.oxerr.okcoin.examples.rest;

import java.io.IOException;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.service.trade.TradeService;
import org.oxerr.okcoin.rest.OKCoinException;
import org.oxerr.okcoin.rest.OKCoinExchange;
import org.oxerr.okcoin.rest.dto.BorrowsInfo;
import org.oxerr.okcoin.rest.service.OKCoinTradeServiceRaw;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BorrowDemo {

	private final Logger log = LoggerFactory.getLogger(BorrowDemo.class);

	private final TradeService tradeService;
	private final OKCoinTradeServiceRaw rawTradeService;

	public BorrowDemo(Exchange exchange) {
		tradeService = exchange.getTradeService();
		rawTradeService = (OKCoinTradeServiceRaw) tradeService;
	}

	public void demoGetBorrowsInfo() throws OKCoinException, IOException {
		BorrowsInfo borrowsInfo = rawTradeService.getBorrowsInfo("btc_cny");
		log.info("borrowsInfo: {}", borrowsInfo);
	}

	public static void main(String[] args) throws OKCoinException, IOException {
		String apiKey = args[0], secretKey = args[1];

		ExchangeSpecification spec = new ExchangeSpecification(OKCoinExchange.class);
		spec.setApiKey(apiKey);
		spec.setSecretKey(secretKey);

		Exchange domesticExchange = ExchangeFactory.INSTANCE.createExchange(spec);
		BorrowDemo borrowDemo = new BorrowDemo(domesticExchange);
		borrowDemo.demoGetBorrowsInfo();
	}

}
