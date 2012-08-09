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
package com.jgoetsch.tradeframework.order.processing;

import com.jgoetsch.tradeframework.Execution;
import com.jgoetsch.tradeframework.marketdata.MarketData;

public class LimitOrderProcessor extends MarketOrderProcessor {

	protected double limitPrice;

	public LimitOrderProcessor(int quantity, double limitPrice) {
		super(quantity);
		this.limitPrice = limitPrice;
	}

	@Override
	protected Execution handleProcessing(MarketData marketData) {
		if ((isBuying() && marketData.getAsk() <= getLimitPrice()) || (isSelling() && marketData.getBid() >= getLimitPrice()))
			return super.handleProcessing(marketData);
		else
			return null;
	}

	protected void setLimitPrice(double limitPrice) {
		this.limitPrice = limitPrice;
	}

	protected double getLimitPrice() {
		return limitPrice;
	}

}
