package org.oxerr.okcoin.rest.dto;

import java.math.BigDecimal;
import java.time.Instant;

import org.oxerr.okcoin.rest.dto.deserializer.EpochMilliDeserializer;
import org.oxerr.okcoin.rest.dto.deserializer.StatusDeserializer;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class BorrowOrder extends BaseObject {

	private static final long serialVersionUID = 2015022001L;

	private final BigDecimal amount;
	private final long borrowId;
	private final Instant borrowDate;
	private final int days;
	private final BigDecimal dealAmount;
	private final BigDecimal rate;
	private final Status status;
	private final String symbol;

	public BorrowOrder(
		@JsonProperty("amount") BigDecimal amount,
		@JsonProperty("borrow_date")
		@JsonDeserialize(using = EpochMilliDeserializer.class)
		Instant borrowDate,
		@JsonProperty("borrow_id") long borrowId,
		@JsonProperty("days") int days,
		@JsonProperty("deal_amount") BigDecimal dealAmount,
		@JsonProperty("rate") BigDecimal rate,
		@JsonProperty("status")
		@JsonDeserialize(using = StatusDeserializer.class)
		Status status,
		@JsonProperty("symbol") String symbol) {
		this.borrowId = borrowId;
		this.amount = amount;
		this.borrowDate = borrowDate;
		this.days = days;
		this.dealAmount = dealAmount;
		this.rate = rate;
		this.status = status;
		this.symbol = symbol;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public Instant getBorrowDate() {
		return borrowDate;
	}

	public long getBorrowId() {
		return borrowId;
	}

	public int getDays() {
		return days;
	}

	public BigDecimal getDealAmount() {
		return dealAmount;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public Status getStatus() {
		return status;
	}

	public String getSymbol() {
		return symbol;
	}

}
