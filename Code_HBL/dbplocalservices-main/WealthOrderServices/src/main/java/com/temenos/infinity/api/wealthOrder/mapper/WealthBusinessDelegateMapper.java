package com.temenos.infinity.api.wealthOrder.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.infinity.api.wealthOrder.businessdelegate.api.AddCurrencyBusinessDelegate;
import com.temenos.infinity.api.wealthOrder.businessdelegate.api.CancelOrderBusinessDelegate;
import com.temenos.infinity.api.wealthOrder.businessdelegate.api.CurrencyBusinessDelegate;
import com.temenos.infinity.api.wealthOrder.businessdelegate.api.CurrencyConvertionBusinessDelegate;
import com.temenos.infinity.api.wealthOrder.businessdelegate.api.DailyMarketBusinessDelegate;
import com.temenos.infinity.api.wealthOrder.businessdelegate.api.DocumentsBusinessDelegate;
import com.temenos.infinity.api.wealthOrder.businessdelegate.api.DownloadAttachmentsPDFBusinessDelegate;
import com.temenos.infinity.api.wealthOrder.businessdelegate.api.FavoriteInstrumentsBusinessDelegate;
import com.temenos.infinity.api.wealthOrder.businessdelegate.api.HistoricalDataBusinessDelegate;
import com.temenos.infinity.api.wealthOrder.businessdelegate.api.HoldingsListBusinessDelegate;
import com.temenos.infinity.api.wealthOrder.businessdelegate.api.InstrumentDetailsBusinessDelegate;
import com.temenos.infinity.api.wealthOrder.businessdelegate.api.InstrumentMinimalBusinessDelegate;
import com.temenos.infinity.api.wealthOrder.businessdelegate.api.MarketOrderBusinessDelegate;
import com.temenos.infinity.api.wealthOrder.businessdelegate.api.OrdersListBusinessDelegate;
import com.temenos.infinity.api.wealthOrder.businessdelegate.api.PortfolioDetailsBusinessDelegate;
import com.temenos.infinity.api.wealthOrder.businessdelegate.api.PricingDataBusinessDelegate;
import com.temenos.infinity.api.wealthOrder.businessdelegate.api.StockNewsBusinessDelegate;
import com.temenos.infinity.api.wealthOrder.businessdelegate.api.StockNewsStoryBusinessDelegate;
import com.temenos.infinity.api.wealthOrder.businessdelegate.api.StockNewsWebBusinessDelegate;
import com.temenos.infinity.api.wealthOrder.businessdelegate.api.TransactionsListBusinessDelegate;
import com.temenos.infinity.api.wealthOrder.businessdelegate.api.UserFavouriteInstrumentsBusinessDelegate;
import com.temenos.infinity.api.wealthOrder.businessdelegate.impl.AddCurrencyBusinessDelegateImpl;
import com.temenos.infinity.api.wealthOrder.businessdelegate.impl.CancelOrderBusinessDelegateImpl;
import com.temenos.infinity.api.wealthOrder.businessdelegate.impl.CurrencyBusinessDelegateImpl;
import com.temenos.infinity.api.wealthOrder.businessdelegate.impl.CurrencyConvertionBusinessDelegateImpl;
import com.temenos.infinity.api.wealthOrder.businessdelegate.impl.DailyMarketBusinessDelegateImpl;
import com.temenos.infinity.api.wealthOrder.businessdelegate.impl.DocumentsBusinessDelegateImpl;
import com.temenos.infinity.api.wealthOrder.businessdelegate.impl.DownloadAttachmentsPDFBusinessDelegateImpl;
import com.temenos.infinity.api.wealthOrder.businessdelegate.impl.FavoriteInstrumentsBusinessDelegateImpl;
import com.temenos.infinity.api.wealthOrder.businessdelegate.impl.HistoricalDataBusinessDelegateImpl;
import com.temenos.infinity.api.wealthOrder.businessdelegate.impl.HoldingsListBusinessDelegateImpl;
import com.temenos.infinity.api.wealthOrder.businessdelegate.impl.InstrumentDetailsBusinessDelegateImpl;
import com.temenos.infinity.api.wealthOrder.businessdelegate.impl.InstrumentMinimalBusinessDelegateImpl;
import com.temenos.infinity.api.wealthOrder.businessdelegate.impl.MarketOrderBusinessDelegateImpl;
import com.temenos.infinity.api.wealthOrder.businessdelegate.impl.OrdersListBusinessDelegateImpl;
import com.temenos.infinity.api.wealthOrder.businessdelegate.impl.PortfolioDetailsBusinessDelegateImpl;
import com.temenos.infinity.api.wealthOrder.businessdelegate.impl.PricingDataBusinessDelegateImpl;
import com.temenos.infinity.api.wealthOrder.businessdelegate.impl.StockNewsBusinessDelegateImpl;
import com.temenos.infinity.api.wealthOrder.businessdelegate.impl.StockNewsStoryBusinessDelegateImpl;
import com.temenos.infinity.api.wealthOrder.businessdelegate.impl.StockNewsWebBusinessDelegateImpl;
import com.temenos.infinity.api.wealthOrder.businessdelegate.impl.TransactionsListBusinessDelegateImpl;
import com.temenos.infinity.api.wealthOrder.businessdelegate.impl.UserFavouriteInstrumentsBusinessDelegateImpl;

public class WealthBusinessDelegateMapper implements DBPAPIMapper<BusinessDelegate> {
	@Override
	public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {
		Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();

		// Add Mapping of Business Delegates interface and their implementation
		map.put(AddCurrencyBusinessDelegate.class, AddCurrencyBusinessDelegateImpl.class);
		map.put(CancelOrderBusinessDelegate.class, CancelOrderBusinessDelegateImpl.class);
		map.put(CurrencyBusinessDelegate.class, CurrencyBusinessDelegateImpl.class);
		map.put(DailyMarketBusinessDelegate.class, DailyMarketBusinessDelegateImpl.class);
		map.put(DocumentsBusinessDelegate.class, DocumentsBusinessDelegateImpl.class);
		map.put(DownloadAttachmentsPDFBusinessDelegate.class, DownloadAttachmentsPDFBusinessDelegateImpl.class);
		map.put(HistoricalDataBusinessDelegate.class, HistoricalDataBusinessDelegateImpl.class);
		map.put(HoldingsListBusinessDelegate.class, HoldingsListBusinessDelegateImpl.class);
		map.put(InstrumentDetailsBusinessDelegate.class, InstrumentDetailsBusinessDelegateImpl.class);
		map.put(InstrumentMinimalBusinessDelegate.class, InstrumentMinimalBusinessDelegateImpl.class);
		map.put(MarketOrderBusinessDelegate.class, MarketOrderBusinessDelegateImpl.class);
		map.put(OrdersListBusinessDelegate.class, OrdersListBusinessDelegateImpl.class);
		map.put(PortfolioDetailsBusinessDelegate.class, PortfolioDetailsBusinessDelegateImpl.class);
		map.put(PricingDataBusinessDelegate.class, PricingDataBusinessDelegateImpl.class);
		map.put(StockNewsBusinessDelegate.class, StockNewsBusinessDelegateImpl.class);
		map.put(StockNewsStoryBusinessDelegate.class, StockNewsStoryBusinessDelegateImpl.class);
		map.put(StockNewsWebBusinessDelegate.class, StockNewsWebBusinessDelegateImpl.class);
		map.put(TransactionsListBusinessDelegate.class, TransactionsListBusinessDelegateImpl.class);
		map.put(UserFavouriteInstrumentsBusinessDelegate.class, UserFavouriteInstrumentsBusinessDelegateImpl.class);
		map.put(FavoriteInstrumentsBusinessDelegate.class, FavoriteInstrumentsBusinessDelegateImpl.class);
		map.put(CurrencyConvertionBusinessDelegate.class, CurrencyConvertionBusinessDelegateImpl.class);
		return map;
	}
}
