package org.oxerr.okcoin.rest.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BorrowsInfo extends ErrorResult {

	private static final long serialVersionUID = 2015022001L;

	private final BigDecimal borrowBtc;
	private final BigDecimal borrowLtc;
	private final BigDecimal borrowCny;
	private final BigDecimal canBorrow;
	private final BigDecimal interestBtc;
	private final BigDecimal interestLtc;
	private final BigDecimal interestCny;
	private final BigDecimal todayInterestBtc;
	private final BigDecimal todayInterestLtc;
	private final BigDecimal todayInterestCny;

	public BorrowsInfo(
		@JsonProperty("borrow_btc") BigDecimal borrowBtc,
		@JsonProperty("borrow_ltc") BigDecimal borrowLtc,
		@JsonProperty("borrow_cny") BigDecimal borrowCny,
		@JsonProperty("can_borrow") BigDecimal canBorrow,
		@JsonProperty("interest_btc") BigDecimal interestBtc,
		@JsonProperty("interest_ltc") BigDecimal interestLtc,
		@JsonProperty("interest_cny") BigDecimal interestCny,
		@JsonProperty("result") boolean result,
		@JsonProperty("today_interest_btc") BigDecimal todayInterestBtc,
		@JsonProperty("today_interest_ltc") BigDecimal todayInterestLtc,
		@JsonProperty("today_interest_cny") BigDecimal todayInterestCny) {
		super(result);
		this.borrowBtc = borrowBtc;
		this.borrowLtc = borrowLtc;
		this.borrowCny = borrowCny;
		this.canBorrow = canBorrow;
		this.interestBtc = interestBtc;
		this.interestLtc = interestLtc;
		this.interestCny = interestCny;
		this.todayInterestBtc = todayInterestBtc;
		this.todayInterestLtc = todayInterestLtc;
		this.todayInterestCny = todayInterestCny;
	}

	public BigDecimal getBorrowBtc() {
		return borrowBtc;
	}

	public BigDecimal getBorrowLtc() {
		return borrowLtc;
	}

	public BigDecimal getBorrowCny() {
		return borrowCny;
	}

	public BigDecimal getCanBorrow() {
		return canBorrow;
	}

	public BigDecimal getInterestBtc() {
		return interestBtc;
	}

	public BigDecimal getInterestLtc() {
		return interestLtc;
	}

	public BigDecimal getInterestCny() {
		return interestCny;
	}

	public BigDecimal getTodayInterestBtc() {
		return todayInterestBtc;
	}

	public BigDecimal getTodayInterestLtc() {
		return todayInterestLtc;
	}

	public BigDecimal getTodayInterestCny() {
		return todayInterestCny;
	}

}
