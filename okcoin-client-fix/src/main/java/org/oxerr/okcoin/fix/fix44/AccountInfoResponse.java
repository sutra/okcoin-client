package org.oxerr.okcoin.fix.fix44;

import org.oxerr.okcoin.fix.field.AccReqID;
import org.oxerr.okcoin.fix.field.Balance;

import quickfix.FieldNotFound;
import quickfix.field.Account;
import quickfix.field.Currency;
import quickfix.field.MsgType;
import quickfix.fix44.Message;

/**
 * The response message of account info.
 */
public class AccountInfoResponse extends Message {

	private static final long serialVersionUID = 20141123L;

	public static final String MSGTYPE = "Z1001";

	public AccountInfoResponse() {
		getHeader().setField(new MsgType(MSGTYPE));
	}

	public void set(Account value) {
		setField(value);
	}

	public Account get(Account value) throws FieldNotFound {
		getField(value);
		return value;
	}

	public Account getAccount() throws FieldNotFound {
		Account value = new Account();
		getField(value);
		return value;
	}

	public boolean isSet(Account field) {
		return isSetField(field);
	}

	public boolean isSetAccount() {
		return isSetField(Account.FIELD);
	}

	public void set(AccReqID value) {
		setField(value);
	}

	public AccReqID get(AccReqID value) throws FieldNotFound {
		getField(value);
		return value;
	}

	public AccReqID getAccReqID() throws FieldNotFound {
		AccReqID value = new AccReqID();
		getField(value);
		return value;
	}

	public boolean isSet(AccReqID field) {
		return isSetField(field);
	}

	public boolean isSetAccReqID() {
		return isSetField(AccReqID.FIELD);
	}

	public void set(Currency value) {
		setField(value);
	}

	public Currency get(Currency value) throws FieldNotFound {
		getField(value);
		return value;
	}

	public Currency getCurrency() throws FieldNotFound {
		Currency value = new Currency();
		getField(value);
		return value;
	}

	public boolean isSet(Currency field) {
		return isSetField(field);
	}

	public boolean isSetCurrency() {
		return isSetField(Currency.FIELD);
	}

	public void set(Balance value) {
		setField(value);
	}

	public Balance get(Balance value) throws FieldNotFound {
		getField(value);
		return value;
	}

	public Balance getBalance() throws FieldNotFound {
		Balance value = new Balance();
		getField(value);
		return value;
	}

	public boolean isSet(Balance field) {
		return isSetField(field);
	}

	public boolean isSetBalance() {
		return isSetField(Balance.FIELD);
	}

}
