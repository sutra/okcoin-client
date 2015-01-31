package org.oxerr.okcoin.rest.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Trade extends AbstractObject {

	private static final long serialVersionUID = 2013112501L;

	private final Date date;

	private final BigDecimal price;

	private final BigDecimal amount;

	private final String tid;

	private final Type type;

	public Trade(
			@JsonProperty("date") final long date,
			@JsonProperty("price") final BigDecimal price,
			@JsonProperty("amount") final BigDecimal amount,
			@JsonProperty("tid") final String tid,
			@JsonProperty("type") final String type) {
		this.date = new Date(date * 1000);
		this.price = price;
		this.amount = amount;
		this.tid = tid;
		this.type = Type.toType(type);;
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

}