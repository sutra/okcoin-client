package org.oxerr.okcoin.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LendDepth extends BaseObject {

	private static final long serialVersionUID = 2015022001L;

	private final Lend[] lendDepth;

	public LendDepth(@JsonProperty("lend_depth") Lend[] lendDepth) {
		this.lendDepth = lendDepth;
	}

	public Lend[] getLendDepth() {
		return lendDepth;
	}

}
