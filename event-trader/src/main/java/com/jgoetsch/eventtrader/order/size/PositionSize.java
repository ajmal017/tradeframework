/*
Copyright (c) 2012, Jeremy Goetsch
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that
the following conditions are met:

    Redistributions of source code must retain the above copyright notice, this list of conditions and
    the following disclaimer.
    Redistributions in binary form must reproduce the above copyright notice, this list of conditions and
    the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package com.jgoetsch.eventtrader.order.size;

import java.util.Map;

import com.jgoetsch.eventtrader.TradeSignal;
import com.jgoetsch.eventtrader.processor.ContextCacheUtil;
import com.jgoetsch.tradeframework.account.AccountData;
import com.jgoetsch.tradeframework.account.AccountDataSource;
import com.jgoetsch.tradeframework.account.Position;

/**
 * OrderSize that returns your currently held position size for the
 * contract in question.
 * 
 * @author jgoetsch
 *
 */
public class PositionSize extends MultipliedOrderSize {

	private AccountDataSource accountDataSource;

	@Override
	protected int getBaseValue(TradeSignal trade, double price, Map<Object, Object> context) {
		AccountData accountData = ContextCacheUtil.getAccountData(accountDataSource, context);

		Position pos = accountData.getPositions().get(trade.getContract());
		return pos != null ? pos.getQuantity() : 0;
	}

	public void setAccountDataSource(AccountDataSource accountDataSource) {
		this.accountDataSource = accountDataSource;
	}

	public AccountDataSource getAccountDataSource() {
		return accountDataSource;
	}

}
