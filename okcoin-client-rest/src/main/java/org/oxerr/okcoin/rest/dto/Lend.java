package org.oxerr.okcoin.rest.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Lend extends BaseObject {

	private static final long serialVersionUID = 2015022001L;

	private final BigDecimal amount;
	private final String days;
	private final int num;
	private final BigDecimal rate;

	public Lend(
		@JsonProperty("amount") BigDecimal amount,
		@JsonProperty("days") String days,
		@JsonProperty("num") int num,
		@JsonProperty("rate") BigDecimal rate) {
		this.amount = amount;
		this.days = days;
		this.num = num;
		this.rate = rate;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public String getDays() {
		return days;
	}

	public int getNum() {
		return num;
	}

	public BigDecimal getRate() {
		return rate;
	}

}
