package org.oxerr.okcoin.rest.dto;

import java.math.BigDecimal;
import java.time.Instant;

import org.oxerr.okcoin.rest.dto.deserializer.EpochMilliDeserializer;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class AccountRecord extends BaseObject {

	private static final long serialVersionUID = 2015060701L;

	private final String addr;
	private final String account;
	private final BigDecimal amount;
	private final String bank;
	private final String benificiaryAddr;
	private final BigDecimal transactionValue;
	private final BigDecimal fee;
	private final Instant date;

	public AccountRecord(
		@JsonProperty("addr") String addr,
		@JsonProperty("account") String account,
		@JsonProperty("amount") BigDecimal amount,
		@JsonProperty("bank") String bank,
		@JsonProperty("benificiary_addr") String benificiaryAddr,
		@JsonProperty("transaction_value") BigDecimal transactionValue,
		@JsonProperty("fee") BigDecimal fee,
		@JsonProperty("date")
		@JsonDeserialize(using = EpochMilliDeserializer.class)
		Instant date) {
		this.addr = addr;
		this.account = account;
		this.amount = amount;
		this.bank = bank;
		this.benificiaryAddr = benificiaryAddr;
		this.transactionValue = transactionValue;
		this.fee = fee;
		this.date = date;
	}

	public String getAddr() {
		return addr;
	}

	public String getAccount() {
		return account;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public String getBank() {
		return bank;
	}

	public String getBenificiaryAddr() {
		return benificiaryAddr;
	}

	public BigDecimal getTransactionValue() {
		return transactionValue;
	}

	public BigDecimal getFee() {
		return fee;
	}

	public Instant getDate() {
		return date;
	}

}
