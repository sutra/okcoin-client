package org.oxerr.okcoin.fix;

import java.math.BigDecimal;
import java.util.Date;

import org.oxerr.okcoin.fix.field.AccReqID;
import org.oxerr.okcoin.fix.fix44.AccountInfoRequest;
import org.oxerr.okcoin.fix.fix44.OrdersInfoAfterSomeIDRequest;

import quickfix.field.Account;
import quickfix.field.ClOrdID;
import quickfix.field.MassStatusReqID;
import quickfix.field.MassStatusReqType;
import quickfix.field.OrdStatus;
import quickfix.field.OrdType;
import quickfix.field.OrderID;
import quickfix.field.OrderQty;
import quickfix.field.OrigClOrdID;
import quickfix.field.Price;
import quickfix.field.Side;
import quickfix.field.Symbol;
import quickfix.field.TradeRequestID;
import quickfix.field.TradeRequestType;
import quickfix.field.TransactTime;
import quickfix.fix44.NewOrderSingle;
import quickfix.fix44.OrderCancelRequest;
import quickfix.fix44.OrderMassStatusRequest;
import quickfix.fix44.TradeCaptureReportRequest;

/**
 * Utilities for creating trade request messages.
 */
public class TradeRequestCreator {

	private final String account;

	public TradeRequestCreator(String apiKey, String secretKey) {
		this.account = String.format("%s,%s", apiKey, secretKey);
	}

	public AccountInfoRequest createAccountInfoRequest(String accReqId) {
		AccountInfoRequest message = new AccountInfoRequest();
		message.set(new AccReqID(accReqId));
		message.set(new Account(account));
		return message;
	}

	public NewOrderSingle createNewOrderSingle(
			String clOrdId,
			char side,
			char ordType,
			BigDecimal orderQty,
			BigDecimal price,
			String symbol) {
		NewOrderSingle message = new NewOrderSingle(
				new ClOrdID(clOrdId),
				new Side(side),
				new TransactTime(new Date()),
				new OrdType(ordType));
		message.set(new Account(account));
		message.set(new OrderQty(orderQty));
		message.set(new Price(price));
		message.set(new Symbol(symbol));
		return message;
	}

	public OrderCancelRequest createOrderCancelRequest(
			String clOrdId,
			String origClOrdId,
			char side,
			String symbol) {
		OrderCancelRequest message = new OrderCancelRequest(
				new OrigClOrdID(origClOrdId),
				new ClOrdID(clOrdId),
				new Side(side),
				new TransactTime(new Date()));
		message.set(new Symbol(symbol));
		return message;
	}

	public OrderMassStatusRequest createOrderMassStatusRequest(
			String massStatusReqId,
			int massStatusReqType) {
		OrderMassStatusRequest message  = new OrderMassStatusRequest(
				new MassStatusReqID(massStatusReqId),
				new MassStatusReqType(massStatusReqType));
		return message;
	}

	public TradeCaptureReportRequest createTradeCaptureReportRequest(
			String tradeRequestId, String symbol) {
		TradeCaptureReportRequest message = new TradeCaptureReportRequest(
				new TradeRequestID(tradeRequestId),
				new TradeRequestType(
						TradeRequestType.MATCHED_TRADES_MATCHING_CRITERIA_PROVIDED_ON_REQUEST));
		message.set(new Symbol(symbol));
		return message;
	}

	public OrdersInfoAfterSomeIDRequest createOrdersInfoAfterSomeIDRequest(
			String tradeRequestId, String symbol, long orderId, char ordStatus) {
		OrdersInfoAfterSomeIDRequest message = new OrdersInfoAfterSomeIDRequest(
				new TradeRequestID(tradeRequestId),
				new Symbol(symbol),
				new OrderID(String.valueOf(orderId)),
				new OrdStatus(ordStatus));
		return message;
	}

}
