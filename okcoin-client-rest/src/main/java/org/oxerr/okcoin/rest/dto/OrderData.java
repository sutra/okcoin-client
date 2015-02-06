package org.oxerr.okcoin.rest.dto;

import java.math.BigDecimal;

public class OrderData extends BaseObject {

	private static final long serialVersionUID = 2015020701L;

	private final BigDecimal price;
	private final BigDecimal amount;
	private final Type type;

	public OrderData(BigDecimal price, BigDecimal amount, Type type) {
		this.price = price;
		this.amount = amount;
		this.type = type;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public Type getType() {
		return type;
	}

}
