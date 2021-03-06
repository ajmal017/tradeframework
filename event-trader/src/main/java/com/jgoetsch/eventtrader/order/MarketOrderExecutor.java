/*
 * Copyright (c) 2012 Jeremy Goetsch
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jgoetsch.eventtrader.order;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jgoetsch.eventtrader.TradeSignal;
import com.jgoetsch.eventtrader.TradeType;
import com.jgoetsch.eventtrader.order.size.OrderSize;
import com.jgoetsch.eventtrader.processor.ContextCacheUtil;
import com.jgoetsch.eventtrader.processor.Processor;
import com.jgoetsch.tradeframework.Contract;
import com.jgoetsch.tradeframework.InvalidContractException;
import com.jgoetsch.tradeframework.Order;
import com.jgoetsch.tradeframework.data.DataUnavailableException;
import com.jgoetsch.tradeframework.marketdata.MarketData;
import com.jgoetsch.tradeframework.marketdata.MarketDataSource;
import com.jgoetsch.tradeframework.order.OrderException;
import com.jgoetsch.tradeframework.order.TradingService;

public abstract class MarketOrderExecutor implements Processor<TradeSignal> {

	private Logger log = LoggerFactory.getLogger(getClass());
	private MarketDataSource marketDataSource;
	private TradingService tradingService;
	private OrderSize orderSize;
	private Contract contract;
	private int roundOrderQty = 1;
	private String account;

	public final void process(TradeSignal trade, Map<Object, Object> context) throws OrderException, IOException
	{
		Order order = new Order();
		order.setAccount(account);
		try {
			MarketData marketData = ContextCacheUtil.getMarketData(marketDataSource, trade.getContract(), context);
			if (marketData == null || marketData.getBid() <= 0 || marketData.getAsk() <= 0) {
				log.warn("Market data not available for contract " + trade.getContract());
				return;
			}
			log.debug("Market data for " + trade.getContract() + ": " + marketData);
			
			int numShares = (orderSize.getValue(trade, getIntendedPrice(trade, marketData), context) / roundOrderQty) * roundOrderQty;
			if (trade.getType() == null)
				trade.setType(numShares > 0 ? TradeType.BUY : TradeType.SELL);
			order.setQuantity(trade.getType().isSell() ? -Math.abs(numShares) : Math.abs(numShares));
			prepareOrder(order, trade, marketData);
		} catch (DataUnavailableException e) {
			log.warn("Could not processs order because: " + e.getMessage());
			return;
		} catch (InvalidContractException e) {
			log.warn(e.getMessage());
			return;
		}

		if (getTradingService() != null) {
			Contract contract = getContract() == null ? trade.getContract() : getContract();
			log.info("Placing order to " + order + " " + contract);
			try {
				getTradingService().placeOrder(contract, order);
			}
			catch (InvalidContractException e) {
				log.warn(e.getMessage());
			}
		}
		else
			log.info("No trading service connected, order to " + order + " not placed");
	}

	protected void prepareOrder(Order order, TradeSignal trade, MarketData marketData) throws OrderException, DataUnavailableException {
		order.setType(Order.TYPE_MARKET);
	}

	/**
	 * Override to return the price at which the trade is expected to execute,
	 * which is used to calculate the order size.
	 * 
	 * @param trade
	 * @param marketData
	 * @return
	 */
	protected double getIntendedPrice(TradeSignal trade, MarketData marketData) throws DataUnavailableException {
		if (marketData.getLast() == 0)
			throw new DataUnavailableException("Last trade price not available");
		else
			return marketData.getLast();
	}

	public void setTradingService(TradingService tradingService) {
		this.tradingService = tradingService;
	}

	public TradingService getTradingService() {
		return tradingService;
	}

	public void setOrderSize(OrderSize orderSize) {
		this.orderSize = orderSize;
	}

	public OrderSize getOrderSize() {
		return orderSize;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}

	public Contract getContract() {
		return contract;
	}

	public void setRoundOrderQty(int roundOrderQty) {
		this.roundOrderQty = roundOrderQty;
	}

	public int getRoundOrderQty() {
		return roundOrderQty;
	}

	public void setMarketDataSource(MarketDataSource marketDataSource) {
		this.marketDataSource = marketDataSource;
	}

	public MarketDataSource getMarketDataSource() {
		return marketDataSource;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getAccount() {
		return account;
	}

}
