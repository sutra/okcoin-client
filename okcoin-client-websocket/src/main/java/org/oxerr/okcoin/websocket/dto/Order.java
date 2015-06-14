package org.oxerr.okcoin.websocket.dto;

import java.time.Instant;

import javax.json.JsonObject;
import javax.json.JsonValue;

import org.oxerr.okcoin.rest.dto.Status;
import org.oxerr.okcoin.rest.dto.Type;

public class Order extends org.oxerr.okcoin.rest.dto.Order {

	private static final long serialVersionUID = 2015030701L;

	public Order(JsonValue jsonValue) {
		this((JsonObject) jsonValue);
	}

	public Order(JsonObject jsonObject) {
		super(
			jsonObject.getJsonNumber("order_id").longValue(),
			Status.of(jsonObject.getInt("status")),
			jsonObject.getString("symbol"),
			Type.of(jsonObject.getString("type")),
			jsonObject.getJsonNumber("price").bigDecimalValue(),
			jsonObject.getJsonNumber("amount").bigDecimalValue(),
			jsonObject.getJsonNumber("deal_amount").bigDecimalValue(),
			jsonObject.getJsonNumber("avg_price").bigDecimalValue(),
			Instant.ofEpochMilli(jsonObject.getJsonNumber("create_date").longValue())
		);
	}

}
