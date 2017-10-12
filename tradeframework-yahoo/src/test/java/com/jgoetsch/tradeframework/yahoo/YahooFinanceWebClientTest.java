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
package com.jgoetsch.tradeframework.yahoo;

import java.io.IOException;
import java.util.Date;

import org.junit.Ignore;

import com.jgoetsch.tradeframework.Contract;
import com.jgoetsch.tradeframework.OHLC;
import com.jgoetsch.tradeframework.data.FundamentalData;
import com.jgoetsch.tradeframework.data.HistoricalDataSource;
import com.jgoetsch.tradeframework.yahoo.YahooFinanceWebClient;

import junit.framework.Assert;
import junit.framework.TestCase;

@Ignore
public class YahooFinanceWebClientTest extends TestCase {

	YahooFinanceWebClient client;

	public void setUp() {
		client = new YahooFinanceWebClient();
	}

	public void tearDown() throws IOException {
		client.close();
	}

	protected void assertValidOHLC(OHLC ohlc) {
		Assert.assertTrue(ohlc.getOpen() >= ohlc.getLow());
		Assert.assertTrue(ohlc.getOpen() <= ohlc.getHigh());
		Assert.assertTrue(ohlc.getClose() >= ohlc.getLow());
		Assert.assertTrue(ohlc.getClose() <= ohlc.getHigh());
	}

	public void testHistoricalData() throws Exception {
		OHLC data[] = client.getHistoricalData(Contract.stock("MSFT"), new Date(), 20, HistoricalDataSource.PERIOD_1_DAY);
		Assert.assertNotNull(data);
		Assert.assertTrue(data.length > 10);
		for (OHLC ohlc : data)
			assertValidOHLC(ohlc);
		
		data = client.getHistoricalData(Contract.stock("GOOG"), new Date(), 5, HistoricalDataSource.PERIOD_1_WEEK);
		Assert.assertNotNull(data);
		Assert.assertEquals(5, data.length, 2);
		for (OHLC ohlc : data)
			assertValidOHLC(ohlc);
	}
	
	public void testFundamentalData() throws Exception {
		FundamentalData data = client.getDataSnapshot(Contract.stock("BAC"));
		Assert.assertNotNull(data);
		System.out.println(data);
	}
}
