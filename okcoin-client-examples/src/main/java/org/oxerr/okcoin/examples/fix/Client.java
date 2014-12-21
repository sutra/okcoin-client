package org.oxerr.okcoin.examples.fix;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;
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
import quickfix.Group;
import quickfix.IncorrectTagValue;
import quickfix.Initiator;
import quickfix.LogFactory;
import quickfix.MessageFactory;
import quickfix.MessageStoreFactory;
import quickfix.SessionID;
import quickfix.SessionSettings;
import quickfix.SocketInitiator;
import quickfix.UnsupportedMessageType;
import quickfix.field.MDEntryPx;
import quickfix.field.MDEntrySize;
import quickfix.field.MDEntryType;
import quickfix.field.MDUpdateType;
import quickfix.field.NoMDEntries;
import quickfix.field.OrigTime;
import quickfix.field.SubscriptionRequestType;
import quickfix.fix44.MarketDataSnapshotFullRefresh;

import com.xeiam.xchange.dto.account.AccountInfo;

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
					SessionID sessionID) throws FieldNotFound,
					UnsupportedMessageType, IncorrectTagValue {
				log.info("MarketDataSnapshotFullRefresh: {}, {}",
						message, message.toXML(dataDictionary));
				Date origTime = message.getField(new OrigTime()).getValue();
				String symbol = message.getSymbol().getValue();
				String mdReqId = message.isSetMDReqID() ? message.getMDReqID().getValue() : null;

				log.info("OrigTime: {}", origTime);
				log.info("Symbol: {}", symbol);
				log.info("MDReqID: {}", mdReqId);

				for (int i = 1, l = message.getNoMDEntries().getValue(); i <= l; i++) {
					Group group = message.getGroup(i, NoMDEntries.FIELD);
					char type = group.getChar(MDEntryType.FIELD);
					BigDecimal px = group.getField(new MDEntryPx()).getValue();
					BigDecimal size = group.isSetField(MDEntrySize.FIELD) ? group.getField(new MDEntrySize()).getValue() : null;
					log.info("type: {}, px: {}, size: {}", type, px, size);
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
