package com.redv.okcoin.domain;

public class OrderResult extends ErrorResult {

	private static final long serialVersionUID = 20140614L;

	private Order[] orders;

	public Order[] getOrders() {
		return orders;
	}

	public void setOrders(Order[] orders) {
		this.orders = orders;
	}

}
