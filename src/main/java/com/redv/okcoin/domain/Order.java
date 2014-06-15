package com.redv.okcoin.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Order extends AbstractObject {

	private static final long serialVersionUID = 20140614L;

	private long orderId;

	private int status;

	private String symbol;

	private String type;

	private BigDecimal rate;

	private BigDecimal amount;

	private BigDecimal dealAmount;

	private BigDecimal avgRate;

	private Date createDate;

	public long getOrderId() {
		return orderId;
	}

	@JsonProperty("orders_id")
	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getDealAmount() {
		return dealAmount;
	}

	@JsonProperty("deal_amount")
	public void setDealAmount(BigDecimal dealAmount) {
		this.dealAmount = dealAmount;
	}

	public BigDecimal getAvgRate() {
		return avgRate;
	}

	@JsonProperty("avg_rate")
	public void setAvgRate(BigDecimal avgRate) {
		this.avgRate = avgRate;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

}
