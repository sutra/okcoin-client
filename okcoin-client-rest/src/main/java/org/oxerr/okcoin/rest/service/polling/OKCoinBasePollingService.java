package org.oxerr.okcoin.rest.service.polling;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.service.polling.BasePollingExchangeService;
import org.knowm.xchange.service.polling.BasePollingService;

/**
 * {@link BasePollingService} implementation.
 */
public class OKCoinBasePollingService extends BasePollingExchangeService
		implements BasePollingService {

	protected OKCoinBasePollingService(Exchange exchange) {
		super(exchange);
	}

}
