package com.redv.okcoin;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.redv.okcoin.domain.Depth;
import com.redv.okcoin.domain.TickerResponse;
import com.redv.okcoin.domain.Trade;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public interface OKCoin {

	@GET
	@Path("ticker.do")
	public TickerResponse getTicker(@QueryParam("symbol") String symbol);

	@GET
	@Path("depth.do")
	public Depth getDepth(@QueryParam("symbol") String symbol);

	@GET
	@Path("trades.do")
	public Trade[] getTrades(@QueryParam("symbol") String symbol);

	@GET
	@Path("trades.do")
	public Trade[] getTrades(@QueryParam("symbol") String symbol,
			@QueryParam("since") int since);

}
