package org.oxerr.okcoin.websocket.dto;

import java.util.stream.Collectors;

import javax.json.JsonObject;
import javax.json.JsonValue;


public class OrderResult extends org.oxerr.okcoin.rest.dto.OrderResult {

	private static final long serialVersionUID = 2015030701L;

	private static final Order[] EMPTY_ORDERS = new Order[0];

	public OrderResult(JsonValue jsonValue) {
		this((JsonObject) jsonValue);
	}

	public OrderResult(JsonObject jsonObject) {
		super(
			jsonObject.getBoolean("result"),
			0,
			jsonObject
				.getJsonArray("orders")
				.stream()
				.map(v -> {
					JsonObject o = (JsonObject) v;
					return new Order(o);
				})
				.collect(Collectors.toList())
				.toArray(EMPTY_ORDERS)
		);
	}

}
