package org.oxerr.okcoin.rest.dto;

import java.math.BigDecimal;
import java.time.Instant;

import org.oxerr.okcoin.rest.dto.deserializer.EpochMilliDeserializer;
import org.oxerr.okcoin.rest.dto.deserializer.TypeDeserializer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonIgnoreProperties(value = "date")
public class Trade extends BaseObject {

	private static final long serialVersionUID = 2015020401L;

	private final Instant date;

	private final BigDecimal price;

	private final BigDecimal amount;

	private final long tid;

	private final Type type;

	public Trade(
		@JsonProperty("date_ms")
		@JsonDeserialize(using = EpochMilliDeserializer.class)
		final Instant date,
		@JsonProperty("price") final BigDecimal price,
		@JsonProperty("amount") final BigDecimal amount,
		@JsonProperty("tid") final long tid,
		@JsonProperty("type")
		@JsonDeserialize(using = TypeDeserializer.class)
		final Type type) {
		this.date = date;
		this.price = price;
		this.amount = amount;
		this.tid = tid;
		this.type = type;
	}

	/**
	 * Returns the transaction time.
	 *
	 * @return the transaction time.
	 */
	public Instant getDate() {
		return date;
	}

	/**
	 * Returns the transaction price.
	 *
	 * @return the transaction price.
	 */
	public BigDecimal getPrice() {
		return price;
	}

	/**
	 * Returns the quantity in BTC (or LTC).
	 *
	 * @return the quantity in BTC (or LTC).
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * Returns the transaction ID.
	 *
	 * @return the transaction ID.
	 */
	public long getTid() {
		return tid;
	}

	/**
	 * Returns the type.
	 *
	 * @return buy/sell.
	 */
	public Type getType() {
		return type;
	}

}