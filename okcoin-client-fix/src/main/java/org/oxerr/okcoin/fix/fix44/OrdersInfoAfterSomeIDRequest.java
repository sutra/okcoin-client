package org.oxerr.okcoin.fix.fix44;

import quickfix.FieldNotFound;
import quickfix.field.MassStatusReqID;
import quickfix.field.MsgType;
import quickfix.field.OrdStatus;
import quickfix.field.OrderID;
import quickfix.field.Symbol;
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
			MassStatusReqID massStatusReqID,
			Symbol symbol,
			OrderID orderID,
			OrdStatus ordStatus) {
		this();
		// Currently, if you set the MassStatusReqID, you will get rejected,
		// say tag 584 not defined for this message type(Z2000).
		// set(massStatusReqID);
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

	public void set(MassStatusReqID value) {
		setField(value);
	}

	public MassStatusReqID get(MassStatusReqID value) throws FieldNotFound {
		getField(value);
		return value;
	}

	public MassStatusReqID getMassStatusReqID() throws FieldNotFound {
		MassStatusReqID value = new MassStatusReqID();
		getField(value);
		return value;
	}

	public boolean isSet(MassStatusReqID field) {
		return isSetField(field);
	}

	public boolean isSetMassStatusReqID() {
		return isSetField(MassStatusReqID.FIELD);
	}

}
