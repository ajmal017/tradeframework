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

import java.util.Date;

import com.jgoetsch.tradeframework.Execution;
import com.jgoetsch.tradeframework.marketdata.MarketData;

public class MarketOrderProcessor extends OrderProcessor {

	public MarketOrderProcessor(int quantity) {
		super(quantity);
	}

	@Override
	protected Execution handleProcessing(MarketData marketData) {
		return new Execution(getQuantityRemaining(), isBuying() ? marketData.getAsk() : marketData.getBid(), new Date(marketData.getTimestamp()));
	}

}