package org.oxerr.okcoin.examples.fix;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.oxerr.okcoin.fix.fix44.OKCoinMessageFactory;
import org.oxerr.okcoin.xchange.service.fix.OKCoinXChangeApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import quickfix.ConfigError;
import quickfix.FileLogFactory;
import quickfix.FileStoreFactory;
import quickfix.Initiator;
import quickfix.LogFactory;
import quickfix.MessageFactory;
import quickfix.MessageStoreFactory;
import quickfix.RuntimeError;
import quickfix.SessionID;
import quickfix.SessionSettings;
import quickfix.SocketInitiator;

import com.xeiam.xchange.dto.marketdata.OrderBook;

public class DepthDemo {

	private final Logger log = LoggerFactory.getLogger(DepthDemo.class);
	private final OKCoinXChangeApplication app;
	private final Initiator initiator;

	public DepthDemo(String apiKey, String secretKey) throws IOException,
			ConfigError {
		app = new OKCoinXChangeApplication(apiKey, secretKey) {

			private final NumberFormat qFmt = new DecimalFormat("0.0000");
			private final NumberFormat pFmt = new DecimalFormat("0.00");

			@Override
			public void onLogon(SessionID sessionId) {
				String symbol = "BTC/CNY";
				app.subscribeOrderBook(symbol, sessionId);
			}

			@Override
			public void onOrderBook(OrderBook orderBook, SessionID sessionId) {
				log.info("order book time: {}, bid size: {}, ask size: {}. best bid: {}@{}, best ask: {}@{}.",
					orderBook.getTimeStamp().toInstant(),
					orderBook.getBids().size(),
					orderBook.getAsks().size(),
					qFmt.format(orderBook.getBids().get(0).getTradableAmount()),
					pFmt.format(orderBook.getBids().get(0).getLimitPrice()),
					qFmt.format(orderBook.getAsks().get(0).getTradableAmount()),
					pFmt.format(orderBook.getAsks().get(0).getLimitPrice())
				);
			}

		};
		SessionSettings settings;
		try (InputStream inputStream = getClass().getResourceAsStream(
				"client.cfg")) {
			settings = new SessionSettings(inputStream);
		}

		MessageStoreFactory storeFactory = new FileStoreFactory(settings);
		LogFactory logFactory = new FileLogFactory(settings);
		MessageFactory messageFactory = new OKCoinMessageFactory();
		initiator = new SocketInitiator(app, storeFactory, settings,
				logFactory, messageFactory);
	}

	public static void main(String[] args) throws IOException, RuntimeError,
			ConfigError {
		String apiKey = args[0], secretKey = args[1];

		DepthDemo depthDemo = new DepthDemo(apiKey, secretKey);
		depthDemo.initiator.block();
	}

}
