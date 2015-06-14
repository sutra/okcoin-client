package org.oxerr.okcoin.rest.dto;

import java.math.BigDecimal;
import java.time.Instant;

import org.oxerr.okcoin.rest.dto.deserializer.EpochMilliDeserializer;
import org.oxerr.okcoin.rest.dto.deserializer.StatusDeserializer;
import org.oxerr.okcoin.rest.dto.deserializer.TypeDeserializer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonIgnoreProperties(value = "orders_id")
public class Order extends BaseObject {

	private static final long serialVersionUID = 20140614L;

	private final long orderId;

	private final Status status;

	private final String symbol;

	private final Type type;

	/**
	 * The limit price or the amount of quoting currency amount
	 * for buy_market order.
	 *
	 * Note, price of buy_market is the quoting currency amount,
	 * price of sell_market is 0.
	 */
	private final BigDecimal price;

	/**
	 * The amount of the base symbol.
	 *
	 * Note, amount of buy_market is 0.
	 */
	private final BigDecimal amount;

	private final BigDecimal dealAmount;

	private final BigDecimal avgPrice;

	private final Instant createDate;

	public Order(
		@JsonProperty("order_id") final long orderId,
		@JsonProperty("status")
		@JsonDeserialize(using = StatusDeserializer.class)
		final Status status,
		@JsonProperty("symbol") final String symbol,
		@JsonProperty("type")
		@JsonDeserialize(using = TypeDeserializer.class)
		final Type type,
		@JsonProperty("price") final BigDecimal price,
		@JsonProperty("amount") final BigDecimal amount,
		@JsonProperty("deal_amount") final BigDecimal dealAmount,
		@JsonProperty("avg_price") final BigDecimal avgPrice,
		@JsonProperty("create_date")
		@JsonDeserialize(using = EpochMilliDeserializer.class)
		final Instant createDate) {
		this.orderId = orderId;
		this.status = status;
		this.symbol = symbol;
		this.type = type;
		this.price = price;
		this.amount = amount;
		this.dealAmount = dealAmount;
		this.avgPrice = avgPrice;
		this.createDate = createDate;
	}

	public long getOrderId() {
		return orderId;
	}

	public Status getStatus() {
		return status;
	}

	public String getSymbol() {
		return symbol;
	}

	public Type getType() {
		return type;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public BigDecimal getDealAmount() {
		return dealAmount;
	}

	public BigDecimal getAvgPrice() {
		return avgPrice;
	}

	public Instant getCreateDate() {
		return createDate;
	}

}
