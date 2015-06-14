package org.oxerr.okcoin.rest.dto;

public class IcebergOrderHistory extends BaseObject {

	private static final long serialVersionUID = 2015021901L;

	/**
	 * 1 based.
	 */
	private final int currentPage;

	private final boolean hasNextPage;

	private final IcebergOrder[] orders;

	public IcebergOrderHistory(int currentPage, boolean hasNextPage, IcebergOrder[] orders) {
		this.currentPage = currentPage;
		this.hasNextPage = hasNextPage;
		this.orders = orders;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public IcebergOrder[] getOrders() {
		return orders;
	}

	public boolean hasNextPage() {
		return hasNextPage;
	}

}
