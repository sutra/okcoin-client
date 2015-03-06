package org.oxerr.okcoin.websocket.dto;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonValue;

public class Funds extends org.oxerr.okcoin.rest.dto.Funds {

	private static final long serialVersionUID = 2015030701L;

	public Funds(JsonValue jsonValue) {
		this((JsonObject) jsonValue);
	}

	public Funds(JsonObject jsonObject) {
		this(
			jsonObject.getJsonObject("asset"),
			jsonObject.getJsonObject("borrow"),
			jsonObject.getJsonObject("free"),
			jsonObject.getJsonObject("freezed"),
			jsonObject.getJsonObject("union_fund")
		);
	}

	public Funds(JsonObject assetJsonObject, JsonObject borrowJsonObject,
			JsonObject freeJsonObject, JsonObject frozenJsonObject,
			JsonObject unionFundJsonObject) {
		super(
			toMap(assetJsonObject),
			toMap(borrowJsonObject),
			toMap(freeJsonObject),
			toMap(frozenJsonObject),
			toMap(unionFundJsonObject)
		);
	}

	private static Map<String, BigDecimal> toMap(JsonObject jsonObject) {
		return jsonObject == null ? Collections.emptyMap() : jsonObject
			.entrySet()
			.stream()
			.collect(
				Collectors.toMap(
					e -> e.getKey(),
					e -> new BigDecimal(((JsonString) e.getValue()).getString())
				)
			);
	}

}
