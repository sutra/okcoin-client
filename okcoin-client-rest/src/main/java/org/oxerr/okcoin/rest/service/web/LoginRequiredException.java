package org.oxerr.okcoin.rest.service.web;

public class LoginRequiredException extends OKCoinClientException {

	private static final long serialVersionUID = 2013122101L;

	public LoginRequiredException() {
	}

	public LoginRequiredException(String message) {
		super(message);
	}

	public LoginRequiredException(String message, Throwable cause) {
		super(message, cause);
	}

}
