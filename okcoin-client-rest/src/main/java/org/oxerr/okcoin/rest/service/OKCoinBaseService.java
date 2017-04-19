package org.oxerr.okcoin.rest.service;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.service.BaseExchangeService;
import org.knowm.xchange.service.BaseService;

/**
 * {@link BaseService} implementation.
 */
public class OKCoinBaseService extends BaseExchangeService
		implements BaseService {

	protected OKCoinBaseService(Exchange exchange) {
		super(exchange);
	}

}
