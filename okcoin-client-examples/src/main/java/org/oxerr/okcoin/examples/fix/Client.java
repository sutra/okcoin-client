package org.oxerr.okcoin.examples.fix;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.oxerr.okcoin.fix.OKCoinApplication;
import org.oxerr.okcoin.fix.fix44.AccountInfoResponse;
import org.oxerr.okcoin.fix.fix44.OKCoinMessageFactory;
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

public class Client {

	private static final Logger log = LoggerFactory.getLogger(Client.class);

	private final DataDictionary dataDictionary;
	private final OKCoinApplication app;
	private final SessionID sessionId;
	private final Initiator initiator;

	public Client(String partner, String secretKey) throws IOException,
			ConfigError, InterruptedException {
		dataDictionary = new DataDictionary("FIX44.xml");

		app = new OKCoinApplication(partner, secretKey) {

			@Override
			public void onMessage(MarketDataSnapshotFullRefresh message,
					SessionID sessionID) throws FieldNotFound,
					UnsupportedMessageType, IncorrectTagValue {
				log.info("MarketDataSnapshotFullRefresh: {}, {}",
						message, message.toXML(dataDictionary));
			}

			@Override
			public void onMessage(AccountInfoResponse message,
					SessionID sessionId) throws FieldNotFound,
					UnsupportedMessageType, IncorrectTagValue {
				String[] currencies = message.getCurrency().getValue().split("/");
				String[] balances = message.getBalance().getValue().split("/");
				for (int i = 0, l = currencies.length; i < l; i++) {
					log.info("{}: {}", currencies[i], balances[i]);
				}
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
