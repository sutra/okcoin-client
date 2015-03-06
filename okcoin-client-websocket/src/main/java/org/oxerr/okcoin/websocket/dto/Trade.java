package org.oxerr.okcoin.websocket.dto;

import java.math.BigDecimal;
import java.time.Instant;

import javax.json.JsonObject;
import javax.json.JsonValue;

import org.oxerr.okcoin.rest.dto.Status;
import org.oxerr.okcoin.rest.dto.Type;

public class Trade extends BaseObject {

	private static final long serialVersionUID = 2015030601L;

	private final BigDecimal averagePrice;
	private final BigDecimal completedTradeAmount;
	private final Instant createdDate;
	private long id;
	private long orderId;
	private BigDecimal sigTradeAmount;
	private BigDecimal sigTradePrice;
	private Status status;
	private String symbol;
	private BigDecimal tradeAmount;
	private BigDecimal tradePrice;
	private Type tradeType;
	private BigDecimal tradeUnitPrice;
	private BigDecimal unTrade;

	public Trade(JsonValue jsonValue) {
		this((JsonObject) jsonValue);
	}

	public Trade(JsonObject jsonObject) {
		this.averagePrice = new BigDecimal(jsonObject.getString("averagePrice"));
		this.completedTradeAmount = new BigDecimal(jsonObject.getString("completedTradeAmount"));
		this.createdDate = Instant.ofEpochMilli(jsonObject.getJsonNumber("createdDate").longValue());
		this.id = jsonObject.getJsonNumber("id").longValue();
		this.orderId = jsonObject.getJsonNumber("orderId").longValue();
		this.sigTradeAmount = new BigDecimal(jsonObject.getString("sigTradeAmount"));
		this.sigTradePrice = new BigDecimal(jsonObject.getString("sigTradePrice"));
		this.status = Status.of(jsonObject.getInt("status"));
		this.symbol = jsonObject.getString("symbol");
		this.tradeAmount = new BigDecimal(jsonObject.getString("tradeAmount"));
		this.tradePrice = new BigDecimal(jsonObject.getString("tradePrice"));
		this.tradeType = Type.of(jsonObject.getString("tradeType"));
		this.tradeUnitPrice = new BigDecimal(jsonObject.getString("tradeUnitPrice"));
		this.unTrade = new BigDecimal(jsonObject.getString("unTrade"));
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public BigDecimal getSigTradeAmount() {
		return sigTradeAmount;
	}

	public void setSigTradeAmount(BigDecimal sigTradeAmount) {
		this.sigTradeAmount = sigTradeAmount;
	}

	public BigDecimal getSigTradePrice() {
		return sigTradePrice;
	}

	public void setSigTradePrice(BigDecimal sigTradePrice) {
		this.sigTradePrice = sigTradePrice;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public BigDecimal getTradeAmount() {
		return tradeAmount;
	}

	public void setTradeAmount(BigDecimal tradeAmount) {
		this.tradeAmount = tradeAmount;
	}

	public BigDecimal getTradePrice() {
		return tradePrice;
	}

	public void setTradePrice(BigDecimal tradePrice) {
		this.tradePrice = tradePrice;
	}

	public Type getTradeType() {
		return tradeType;
	}

	public void setTradeType(Type tradeType) {
		this.tradeType = tradeType;
	}

	public BigDecimal getTradeUnitPrice() {
		return tradeUnitPrice;
	}

	public void setTradeUnitPrice(BigDecimal tradeUnitPrice) {
		this.tradeUnitPrice = tradeUnitPrice;
	}

	public BigDecimal getUnTrade() {
		return unTrade;
	}

	public void setUnTrade(BigDecimal unTrade) {
		this.unTrade = unTrade;
	}

	public BigDecimal getAveragePrice() {
		return averagePrice;
	}

	public BigDecimal getCompletedTradeAmount() {
		return completedTradeAmount;
	}

	public Instant getCreatedDate() {
		return createdDate;
	}

}
