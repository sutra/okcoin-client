package com.redv.okcoin.domain;

import java.math.BigDecimal;
import java.util.Date;

public class Trade extends AbstractObject {

	private static final long serialVersionUID = 2013112501L;

	private Date date;

	private BigDecimal price;

	private BigDecimal amount;

	private String tid;

	private Type type;

	public Trade() {
	}

	public Trade(Date date, BigDecimal price, BigDecimal amount, String tid,
			Type type) {
		this.date = date;
		this.price = price;
		this.amount = amount;
		this.tid = tid;
		this.type = type;
	}

	public Date getDate() {
		return date;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public String getTid() {
		return tid;
	}

	public Type getType() {
		return type;
	}

	public void setDate(long date) {
		this.date = new Date(date * 1000);
	}

	public void setType(String type) {
		this.type = Type.toType(type);
	}

}