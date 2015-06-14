package org.oxerr.okcoin.websocket.dto;

import javax.json.JsonObject;
import javax.json.JsonValue;

public class TradeResult extends BaseObject {

	private static final long serialVersionUID = 2015030701L;

	private final long orderId;
	private final boolean result;

	public TradeResult(JsonValue jsonValue) {
		this((JsonObject) jsonValue);
	}

	public TradeResult(JsonObject jsonObject) {
		this.orderId = Long.parseLong(jsonObject.getString("order_id"));
		this.result = Boolean.parseBoolean(jsonObject.getString("result"));
	}

	public long getOrderId() {
		return orderId;
	}

	public boolean getResult() {
		return result;
	}

}
