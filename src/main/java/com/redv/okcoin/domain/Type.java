package com.redv.okcoin.domain;

public enum Type {

	BUY("buy"), SELL("sell");

	public static Type toType(String typeString) {
		for (Type type : Type.values()) {
			if (type.type.equals(typeString)) {
				return type;
			}
		}

		throw new IllegalArgumentException("Unexpected type: " + typeString);
	}

	private String type;

	Type(String type) {
		this.type = type;
	}

}
