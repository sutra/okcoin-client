package com.redv.okcoin;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.redv.okcoin.domain.Depth;
import com.redv.okcoin.domain.OrderResult;
import com.redv.okcoin.domain.TickerResponse;
import com.redv.okcoin.domain.Trade;
import com.redv.okcoin.domain.TradeResult;
import com.redv.okcoin.domain.UserInfo;

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

	@POST
	@Path("userinfo.do")
	public UserInfo getUserInfo(@QueryParam("partner") long partner,
			@QueryParam("sign") String sign);

	@POST
	@Path("trade.do")
	public TradeResult trade(@QueryParam("partner") long partner,
			@QueryParam("symbol") String symbol,
			@QueryParam("type") String type,
			@QueryParam("rate") String rate,
			@QueryParam("amount") String amount,
			@QueryParam("sign") String sign);

	@POST
	@Path("cancelorder.do")
	public TradeResult cancelOrder(@QueryParam("partner") long partner,
			@QueryParam("order_id") long orderId,
			@QueryParam("symbol") String symbol,
			@QueryParam("sign") String sign);

	@POST
	@Path("getorder.do")
	public OrderResult getOrder(@QueryParam("partner") long partner,
			@QueryParam("order_id") long orderId,
			@QueryParam("symbol") String symbol,
			@QueryParam("sign") String sign);

}
