package org.oxerr.okcoin.fix;

import java.math.BigDecimal;

import org.oxerr.okcoin.fix.fix44.AccountInfoRequest;
import org.oxerr.okcoin.fix.fix44.AccountInfoResponse;
import org.oxerr.okcoin.fix.fix44.OrdersInfoAfterSomeIDRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import quickfix.Application;
import quickfix.ConfigError;
import quickfix.DataDictionary;
import quickfix.DoNotSend;
import quickfix.FieldNotFound;
import quickfix.IncorrectDataFormat;
import quickfix.IncorrectTagValue;
import quickfix.Message;
import quickfix.RejectLogon;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.UnsupportedMessageType;
import quickfix.field.MsgType;
import quickfix.field.Password;
import quickfix.field.Username;
import quickfix.fix44.ExecutionReport;
import quickfix.fix44.MarketDataRequest;
import quickfix.fix44.MessageCracker;
import quickfix.fix44.NewOrderSingle;
import quickfix.fix44.OrderCancelRequest;
import quickfix.fix44.OrderMassStatusRequest;
import quickfix.fix44.TradeCaptureReportRequest;

/**
 * {@link Application} implementation.
 */
public class OKCoinApplication extends MessageCracker implements Application {

	private final Logger log = LoggerFactory.getLogger(OKCoinApplication.class);
	private final DataDictionary dataDictionary;
	private final MarketDataRequestCreator marketDataRequestCreator;
	private final TradeRequestCreator tradeRequestCreator;
	private final String apiKey;
	private final String secretKey;

	public OKCoinApplication(String apiKey, String secretKey) {
		this.apiKey = apiKey;
		this.secretKey = secretKey;
		this.marketDataRequestCreator = new MarketDataRequestCreator();
		this.tradeRequestCreator = new TradeRequestCreator(apiKey, secretKey);

		try {
			dataDictionary = new DataDictionary("org/oxerr/okcoin/fix/FIX44.xml");
		} catch (ConfigError e) {
			throw new RuntimeException(e);
		}
	}

	public DataDictionary getDataDictionary() {
		return dataDictionary;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCreate(SessionID sessionId) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onLogon(SessionID sessionId) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onLogout(SessionID sessionId) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void toAdmin(Message message, SessionID sessionId) {
		String msgType;
		try {
			msgType = message.getHeader().getString(MsgType.FIELD);
		} catch (FieldNotFound e) {
			throw new RuntimeException(e.getMessage(), e);
		}

		if (MsgType.LOGON.equals(msgType) || MsgType.HEARTBEAT.equals(msgType)) {
			message.setField(new Username(apiKey));
			message.setField(new Password(secretKey));
		}

		if (log.isTraceEnabled()) {
			log.trace("toAdmin: {}", message);
			log.trace("toAdmin: {}", message.toXML(dataDictionary));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fromAdmin(Message message, SessionID sessionId)
			throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue,
			RejectLogon {
		if (log.isTraceEnabled()) {
			log.trace("fromAdmin: {}", message);
			log.trace("fromAdmin: {}", message.toXML(dataDictionary));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void toApp(Message message, SessionID sessionId) throws DoNotSend {
		if (log.isTraceEnabled()) {
			log.trace("toApp: {}", message);
			log.trace("toApp: {}", message.toXML(dataDictionary));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fromApp(Message message, SessionID sessionId)
			throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue,
			UnsupportedMessageType {
		if (log.isTraceEnabled()) {
			log.trace("fromApp: {}", message);
			log.trace("fromApp: {}", message.toXML(dataDictionary));
		}
		crack(message, sessionId);
	}

	@Override
	public void crack(quickfix.Message message, SessionID sessionId)
			throws UnsupportedMessageType, FieldNotFound, IncorrectTagValue {
		if (message instanceof AccountInfoResponse) {
			onMessage((AccountInfoResponse) message, sessionId);
		} else {
			super.crack(message, sessionId);
		}
	}

	@Override
	public void onMessage(ExecutionReport message, SessionID sessionId)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
	}

	public void onMessage(AccountInfoResponse message, SessionID sessionId)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
	}

	public void sendMessage(final Message message, final SessionID sessionId) {
		log.trace("sending message: {}", message);
		Session.lookupSession(sessionId).send(message);
	}

	public void requestMarketData(
			String mdReqId,
			String symbol,
			char subscriptionRequestType,
			int marketDepth,
			int mdUpdateType,
			char[] mdEntryTypes,
			SessionID sessionId) {
		MarketDataRequest message = marketDataRequestCreator.createMarketDataRequest(
				mdReqId, symbol, subscriptionRequestType, marketDepth,
				mdUpdateType, mdEntryTypes);
		sendMessage(message, sessionId);
	}

	/**
	 *
	 * @param mdReqId Unique ID assigned to this request.
	 * @param symbol Symbol, BTC/CNY or LTC/CNY.
	 * @param subscriptionRequestType 0 = Snapshot, 1 = Snapshot + Subscribe,
	 * 2 = Unsubscribe.
	 * @param marketDepth Applicable only to order book snapshot requests.
	 * Should be ignored otherwise.
	 * 0 = Full Book
	 * @param mdUpdateType 0 = Full Refresh, 1 = Incremental Refresh.
	 * @param sessionId FIX session ID.
	 */
	public void requestOrderBook(
			String mdReqId,
			String symbol,
			char subscriptionRequestType,
			int marketDepth,
			int mdUpdateType,
			SessionID sessionId) {
		MarketDataRequest message = marketDataRequestCreator.createOrderBookRequest(
				mdReqId,
				symbol,
				subscriptionRequestType,
				marketDepth,
				mdUpdateType);
		sendMessage(message, sessionId);
	}

	public void requestLiveTrades(
			String mdReqId,
			String symbol,
			SessionID sessionId) {
		MarketDataRequest message = marketDataRequestCreator.createLiveTradesRequest(
				mdReqId,
				symbol);
		sendMessage(message, sessionId);
	}

	public void request24HTicker(
			String mdReqId,
			String symbol,
			SessionID sessionId
			) {
		MarketDataRequest message = marketDataRequestCreator.create24HTickerRequest(
				mdReqId, symbol);
		sendMessage(message, sessionId);
	}

	public void placeOrder(
			String clOrdId,
			char side,
			char ordType,
			BigDecimal orderQty,
			BigDecimal price,
			String symbol,
			SessionID sessionId) {
		NewOrderSingle message = tradeRequestCreator.createNewOrderSingle(
				clOrdId, side, ordType, orderQty, price, symbol);
		sendMessage(message, sessionId);
	}

	public void cancelOrder(
			String clOrdId,
			String origClOrdId,
			char side,
			String symbol,
			SessionID sessionId) {
		OrderCancelRequest message = tradeRequestCreator.createOrderCancelRequest(
				clOrdId, origClOrdId, side, symbol);
		sendMessage(message, sessionId);
	}

	/**
	 * Request order status.
	 *
	 * @param massStatusReqId Client-assigned unique ID of this request.(or ORDERID)
	 * @param massStatusReqType Specifies the scope of the mass status request.
	 * 1 = status of a specified order(Tag584 is ORDERID)
	 * 7 = Status for all orders
	 * @param sessionId the FIX session ID.
	 */
	public void requestOrderMassStatus(
			String massStatusReqId,
			int massStatusReqType,
			SessionID sessionId) {
		OrderMassStatusRequest message = tradeRequestCreator.createOrderMassStatusRequest(
				massStatusReqId, massStatusReqType);
		sendMessage(message, sessionId);
	}

	public void requestTradeCaptureReportRequest(
			String tradeRequestId,
			String symbol,
			SessionID sessionId) {
		TradeCaptureReportRequest message = tradeRequestCreator
				.createTradeCaptureReportRequest(tradeRequestId, symbol);
		sendMessage(message, sessionId);
	}

	public void requestAccountInfo(String accReqId, SessionID sessionId) {
		AccountInfoRequest message = tradeRequestCreator.createAccountInfoRequest(
				accReqId);
		sendMessage(message, sessionId);
	}

	/**
	 * Request history order information which order ID is after the specified
	 * {@code orderId}.
	 *
	 * @param tradeRequestId Client-assigned unique ID of this request.
	 * @param symbol Symbol. BTC/CNY or LTC/CNY.
	 * @param orderId Order ID. Return 10 records after this id.
	 * @param ordStatus Order status. 0 = Not filled 1 = Fully filled.
	 * @param sessionId the FIX session ID.
	 */
	public void requestOrdersInfoAfterSomeID(
			String tradeRequestId,
			String symbol,
			long orderId,
			char ordStatus,
			SessionID sessionId) {
		OrdersInfoAfterSomeIDRequest message = tradeRequestCreator.createOrdersInfoAfterSomeIDRequest(
				tradeRequestId, symbol, orderId, ordStatus);
		sendMessage(message, sessionId);
	}

}
