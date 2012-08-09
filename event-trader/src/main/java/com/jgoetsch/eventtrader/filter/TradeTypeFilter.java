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
package com.jgoetsch.eventtrader.filter;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.jgoetsch.eventtrader.TradeSignal;

/**
 * Filters TradeSignal messages to the given types (any of BUY, SELL, SHORT, or COVER).
 * 
 * @author jgoetsch
 *
 */
public class TradeTypeFilter extends FilterProcessor<TradeSignal> {

	private Set<String> tradeTypes;

	@Override
	protected boolean handleProcessing(TradeSignal trade, Map<Object,Object> context) {
		return tradeTypes.contains(trade.getType().toUpperCase());
	}

	/**
	 * Sets the trade types allowed to pass through to the underlying
	 * processors.
	 * 
	 * @param tradeTypes
	 *            Collection of strings representing trade types to allow.
	 *            Contents can be any of "BUY", "SELL", "SHORT", and "COVER".
	 */
	public void setTradeTypes(Collection<String> tradeTypes) {
		this.tradeTypes = new HashSet<String>(tradeTypes.size());
		for (String type : tradeTypes)
			this.getTradeTypes().add(type.trim().toUpperCase());
	}

	/**
	 * Gets the trade types allowed to pass through to the underlying
	 * processors.
	 * 
	 * @return Collection of strings representing trade types to allow. Contents
	 *         can be any of "BUY", "SELL", "SHORT", and "COVER".
	 */
	public Collection<String> getTradeTypes() {
		return tradeTypes;
	}

}