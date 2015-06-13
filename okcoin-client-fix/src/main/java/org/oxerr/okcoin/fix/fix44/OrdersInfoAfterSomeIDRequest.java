package org.oxerr.okcoin.fix.fix44;

import quickfix.FieldNotFound;
import quickfix.field.MsgType;
import quickfix.field.OrdStatus;
import quickfix.field.OrderID;
import quickfix.field.Symbol;
import quickfix.field.TradeRequestID;
import quickfix.fix44.Message;

/**
 * The request message to get history order information.
 */
public class OrdersInfoAfterSomeIDRequest extends Message {

	private static final long serialVersionUID = 20150608L;

	public static final String MSGTYPE = "Z2000";

	public OrdersInfoAfterSomeIDRequest() {
		getHeader().setField(new MsgType(MSGTYPE));
	}

	public OrdersInfoAfterSomeIDRequest(
			TradeRequestID tradeRequestID,
			Symbol symbol,
			OrderID orderID,
			OrdStatus ordStatus) {
		this();
		set(tradeRequestID);
		set(symbol);
		set(orderID);
		set(ordStatus);
	}

	public void set(OrderID value) {
		setField(value);
	}

	public OrderID get(OrderID value) throws FieldNotFound {
		getField(value);
		return value;
	}

	public OrderID getOrderID() throws FieldNotFound {
		OrderID value = new OrderID();
		getField(value);
		return value;
	}

	public boolean isSet(OrderID field) {
		return isSetField(field);
	}

	public boolean isSetOrderID() {
		return isSetField(OrderID.FIELD);
	}

	public void set(OrdStatus value) {
		setField(value);
	}

	public OrdStatus get(OrdStatus value) throws FieldNotFound {
		getField(value);
		return value;
	}

	public OrdStatus getOrdStatus() throws FieldNotFound {
		OrdStatus value = new OrdStatus();
		getField(value);
		return value;
	}

	public boolean isSet(OrdStatus field) {
		return isSetField(field);
	}

	public boolean isSetOrdStatus() {
		return isSetField(OrdStatus.FIELD);
	}

	public void set(Symbol value) {
		setField(value);
	}

	public Symbol get(Symbol value) throws FieldNotFound {
		getField(value);
		return value;
	}

	public Symbol getSymbol() throws FieldNotFound {
		Symbol value = new Symbol();
		getField(value);
		return value;
	}

	public boolean isSet(Symbol field) {
		return isSetField(field);
	}

	public boolean isSetSymbol() {
		return isSetField(Symbol.FIELD);
	}

	public void set(TradeRequestID value) {
		setField(value);
	}

	public TradeRequestID get(TradeRequestID value) throws FieldNotFound {
		getField(value);
		return value;
	}

	public TradeRequestID getTradeRequestID() throws FieldNotFound {
		TradeRequestID value = new TradeRequestID();
		getField(value);
		return value;
	}

	public boolean isSet(TradeRequestID field) {
		return isSetField(field);
	}

	public boolean isSetTradeRequestID() {
		return isSetField(TradeRequestID.FIELD);
	}

}
