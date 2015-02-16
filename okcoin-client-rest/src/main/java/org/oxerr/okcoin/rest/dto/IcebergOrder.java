package org.oxerr.okcoin.rest.dto;

import java.math.BigDecimal;
import java.time.Instant;

public class IcebergOrder extends BaseObject {

	private static final long serialVersionUID = 2015021501L;

	private final long id;

	private final Instant date;

	private final Type side;

	/**
	 * Total order amount.
	 */
	private final BigDecimal tradeValue;

	/**
	 * Average order Amount.
	 */
	private final BigDecimal singleAvg;

	/**
	 * Price variance.
	 */
	private final BigDecimal depthRange;

	/**
	 * Highest/lowest price.
	 */
	private final BigDecimal protectedPrice;

	private final BigDecimal filled;

	public IcebergOrder(
			long id,
			Instant date, Type side, BigDecimal tradeValue,
			BigDecimal singleAvg, BigDecimal depthRange,
			BigDecimal protectedPrice, BigDecimal filled) {
		this.id = id;
		this.date = date;
		this.side = side;
		this.tradeValue = tradeValue;
		this.singleAvg = singleAvg;
		this.depthRange = depthRange;
		this.protectedPrice = protectedPrice;
		this.filled = filled;
	}

	public long getId() {
		return id;
	}

	public Instant getDate() {
		return date;
	}

	public Type getSide() {
		return side;
	}

	public BigDecimal getTradeValue() {
		return tradeValue;
	}

	public BigDecimal getSingleAvg() {
		return singleAvg;
	}

	public BigDecimal getDepthRange() {
		return depthRange;
	}

	public BigDecimal getProtectedPrice() {
		return protectedPrice;
	}

	public BigDecimal getFilled() {
		return filled;
	}

}
