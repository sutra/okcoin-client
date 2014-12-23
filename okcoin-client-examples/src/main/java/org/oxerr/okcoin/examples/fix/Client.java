package org.oxerr.okcoin.examples.fix;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.oxerr.okcoin.fix.OKCoinApplication;
import org.oxerr.okcoin.fix.fix44.OKCoinMessageFactory;
import org.oxerr.okcoin.xchange.service.fix.OKCoinXChangeApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import quickfix.ConfigError;
import quickfix.DataDictionary;
import quickfix.FieldNotFound;
import quickfix.FileLogFactory;
import quickfix.FileStoreFactory;
import quickfix.IncorrectTagValue;
import quickfix.Initiator;
import quickfix.LogFactory;
import quickfix.MessageFactory;
import quickfix.MessageStoreFactory;
import quickfix.SessionID;
import quickfix.SessionSettings;
import quickfix.SocketInitiator;
import quickfix.UnsupportedMessageType;
import quickfix.field.MDUpdateType;
import quickfix.field.SubscriptionRequestType;
import quickfix.fix44.MarketDataSnapshotFullRefresh;

import com.xeiam.xchange.dto.account.AccountInfo;
import com.xeiam.xchange.dto.marketdata.OrderBook;
import com.xeiam.xchange.dto.marketdata.Trade;
import com.xeiam.xchange.dto.trade.LimitOrder;

public class Client {

	private static final Logger log = LoggerFactory.getLogger(Client.class);

	private final DataDictionary dataDictionary;
	private final OKCoinApplication app;
	private final SessionID sessionId;
	private final Initiator initiator;

	public Client(String partner, String secretKey) throws IOException,
			ConfigError, InterruptedException {
		dataDictionary = new DataDictionary("FIX44.xml");

		app = new OKCoinXChangeApplication(partner, secretKey) {

			@Override
			public void onMessage(MarketDataSnapshotFullRefresh message,
					SessionID sessionId) throws FieldNotFound,
					UnsupportedMessageType, IncorrectTagValue {
				log.info("MarketDataSnapshotFullRefresh: {}, {}",
						message, message.toXML(dataDictionary));

				super.onMessage(message, sessionId);
			}

			@Override
			public void onOrderBook(OrderBook orderBook, SessionID sessionId) {
				// bids should be sorted by limit price descending
				LimitOrder preOrder = null;
				for (LimitOrder order : orderBook.getBids()) {
					log.info("Bid: {}, {}", order.getLimitPrice(), order.getTradableAmount());

					if (preOrder != null && preOrder.compareTo(order) >= 0) {
						log.error("bids should be sorted by limit price descending");
					}
					preOrder = order;
				}

				// asks should be sorted by limit price ascending
				preOrder = null;
				for (LimitOrder order : orderBook.getAsks()) {
					log.info("Ask: {}, {}", order.getLimitPrice(), order.getTradableAmount());

					if (preOrder != null && preOrder.compareTo(order) >= 0) {
						log.error("asks should be sorted by limit price ascending");
					}
					preOrder = order;
				}

				LimitOrder ask = orderBook.getAsks().get(0);
				LimitOrder bid = orderBook.getBids().get(0);
				log.info("lowest  ask: {}, {}", ask.getLimitPrice(), ask.getTradableAmount());
				log.info("highest bid: {}, {}", bid.getLimitPrice(), bid.getTradableAmount());
			}

			@Override
			public void onTrades(List<Trade> trades, SessionID sessionId) {
				for (Trade trade : trades) {
					log.info("{}", trade);
				}
			}

			@Override
			public void onAccountInfo(AccountInfo accountInfo,
					SessionID sessionId) {
				log.info("AccountInfo: {}", accountInfo);
			}

		};

		SessionSettings settings;
		try (InputStream inputStream = getClass().getResourceAsStream("client.cfg")) {
			settings = new SessionSettings(inputStream);
		}

		MessageStoreFactory storeFactory = new FileStoreFactory(settings);
		LogFactory logFactory = new FileLogFactory(settings);
		MessageFactory messageFactory = new OKCoinMessageFactory();
		initiator = new SocketInitiator(app, storeFactory, settings,
				logFactory, messageFactory);
		initiator.start();

		while (!initiator.isLoggedOn()) {
			log.info("Waiting for logged on...");
			TimeUnit.SECONDS.sleep(1);
		}

		sessionId = initiator.getSessions().get(0);
	}

	public void demo() {
		String mdReqId = UUID.randomUUID().toString();
		String symbol = "BTC/CNY";
		char subscriptionRequestType = SubscriptionRequestType.SNAPSHOT;
		int marketDepth = 0;
		int mdUpdateType = MDUpdateType.FULL_REFRESH;
		app.requestOrderBook(mdReqId, symbol, subscriptionRequestType,
				marketDepth, mdUpdateType, sessionId);

		mdReqId = UUID.randomUUID().toString();
		app.requestLiveTrades(mdReqId, symbol, sessionId);

		mdReqId = UUID.randomUUID().toString();
		app.request24HTicker(mdReqId, symbol, sessionId);

		String accReqId = UUID.randomUUID().toString();
		app.requestAccountInfo(accReqId, sessionId);
	}

	public static void main(String[] args) throws IOException, ConfigError,
			InterruptedException {
		String partner = args[0], secretKey = args[1];
		Client client = new Client(partner, secretKey);
		client.demo();

		log.info("Waiting a moment and exiting.");
		TimeUnit.SECONDS.sleep(30);
		client.initiator.stop();
	}

}
