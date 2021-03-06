package com.jgoetsch.eventtrader.source.parser;

import org.jsoup.nodes.Element;

import com.jgoetsch.eventtrader.Msg;
import com.jgoetsch.eventtrader.TradeSignal;
import com.jgoetsch.eventtrader.TradeType;

public class HtmlSelectorTradeFieldsMsgParser extends HtmlSelectorMsgParser {

	private final String messageSelector;
	private final String symbolSelector;
	private final String typeSelector;

	public HtmlSelectorTradeFieldsMsgParser(String selector, String messageSelector, String symbolSelector, String typeSelector) {
		super(selector);
		this.messageSelector = messageSelector;
		this.symbolSelector = symbolSelector;
		this.typeSelector = typeSelector;
	}

	@Override
	protected Msg createMsg(Element node) {
		Element message = node.select(messageSelector).first();
		Element sym = symbolSelector != null ? node.select(symbolSelector).first() : null;
		if (message != null) {
			Msg msg = super.createMsg(message);
			if (sym != null) {
				return new TradeSignal(typeSelector != null ? TradeType.valueOf(node.select(typeSelector).text()) : null, sym.text(), msg);
			}
			else {
				return msg;
			}
		}
		else {
			return null;
		}
	}

}
