package com.temenos.infinity.api.wealthOrder.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.infinity.api.wealthOrder.backenddelegate.api.AddCurrencyBackendDelegate;
import com.temenos.infinity.api.wealthOrder.backenddelegate.api.CancelOrderBackendDelegate;
import com.temenos.infinity.api.wealthOrder.backenddelegate.api.CurrencyBackendDelegate;
import com.temenos.infinity.api.wealthOrder.backenddelegate.api.CurrencyConvertionBackendDelegate;
import com.temenos.infinity.api.wealthOrder.backenddelegate.api.DailyMarketBackendDelegate;
import com.temenos.infinity.api.wealthOrder.backenddelegate.api.DocumentsBackendDelegate;
import com.temenos.infinity.api.wealthOrder.backenddelegate.api.DownloadAttachmentsPDFBackendDelegate;
import com.temenos.infinity.api.wealthOrder.backenddelegate.api.FavoriteInstrumentsBackendDelegate;
import com.temenos.infinity.api.wealthOrder.backenddelegate.api.HistoricalDataBackendDelegate;
import com.temenos.infinity.api.wealthOrder.backenddelegate.api.HoldingsListBackendDelegate;
import com.temenos.infinity.api.wealthOrder.backenddelegate.api.InstrumentDetailsBackendDelegate;
import com.temenos.infinity.api.wealthOrder.backenddelegate.api.InstrumentMinimalBackendDelegate;
import com.temenos.infinity.api.wealthOrder.backenddelegate.api.MarketOrderBackendDelegate;
import com.temenos.infinity.api.wealthOrder.backenddelegate.api.OrdersListBackendDelegate;
import com.temenos.infinity.api.wealthOrder.backenddelegate.api.PortfolioDetailsBackendDelegate;
import com.temenos.infinity.api.wealthOrder.backenddelegate.api.PricingDataBackendDelegate;
import com.temenos.infinity.api.wealthOrder.backenddelegate.api.StockNewsBackendDelegate;
import com.temenos.infinity.api.wealthOrder.backenddelegate.api.StockNewsStoryBackendDelegate;
import com.temenos.infinity.api.wealthOrder.backenddelegate.api.StockNewsWebBackendDelegate;
import com.temenos.infinity.api.wealthOrder.backenddelegate.api.TransactionsListBackendDelegate;
import com.temenos.infinity.api.wealthOrder.backenddelegate.api.UserFavouriteInstrumentsBackendDelegate;
import com.temenos.infinity.api.wealthOrder.backenddelegate.impl.AddCurrencyBackendDelegateImpl;
import com.temenos.infinity.api.wealthOrder.backenddelegate.impl.CancelOrderBackendDelegateImpl;
import com.temenos.infinity.api.wealthOrder.backenddelegate.impl.CurrencyBackendDelegateImpl;
import com.temenos.infinity.api.wealthOrder.backenddelegate.impl.CurrencyConvertionBackendDelegateImpl;
import com.temenos.infinity.api.wealthOrder.backenddelegate.impl.DailyMarketBackendDelegateImpl;
import com.temenos.infinity.api.wealthOrder.backenddelegate.impl.DocumentsBackendDelegateImpl;
import com.temenos.infinity.api.wealthOrder.backenddelegate.impl.DownloadAttachmentsPDFBackendDelegateImpl;
import com.temenos.infinity.api.wealthOrder.backenddelegate.impl.FavoriteInstrumentsBackendDelegateImpl;
import com.temenos.infinity.api.wealthOrder.backenddelegate.impl.HistoricalDataBackendDelegateImpl;
import com.temenos.infinity.api.wealthOrder.backenddelegate.impl.HoldingsListBackendDelegateImpl;
import com.temenos.infinity.api.wealthOrder.backenddelegate.impl.InstrumentDetailsBackendDelegateImpl;
import com.temenos.infinity.api.wealthOrder.backenddelegate.impl.InstrumentMinimalBackendDelegateImpl;
import com.temenos.infinity.api.wealthOrder.backenddelegate.impl.MarketOrderBackendDelegateImpl;
import com.temenos.infinity.api.wealthOrder.backenddelegate.impl.OrdersListBackendDelegateImpl;
import com.temenos.infinity.api.wealthOrder.backenddelegate.impl.PortfolioDetailsBackendDelegateImpl;
import com.temenos.infinity.api.wealthOrder.backenddelegate.impl.PricingDataBackendDelegateImpl;
import com.temenos.infinity.api.wealthOrder.backenddelegate.impl.StockNewsBackendDelegateImpl;
import com.temenos.infinity.api.wealthOrder.backenddelegate.impl.StockNewsStoryBackendDelegateImpl;
import com.temenos.infinity.api.wealthOrder.backenddelegate.impl.StockNewsWebBackendDelegateImpl;
import com.temenos.infinity.api.wealthOrder.backenddelegate.impl.TransactionsListBackendDelegateImpl;
import com.temenos.infinity.api.wealthOrder.backenddelegate.impl.UserFavouriteInstrumentsBackendDelegateImpl;

public class WealthBackendDelegateMapper implements DBPAPIMapper<BackendDelegate> {
	@Override
	public Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> getAPIMappings() {
		 Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> map = new HashMap<>();

	        // Register your BackendDelegate Delegate Implementation classes here
	        map.put(AddCurrencyBackendDelegate.class, AddCurrencyBackendDelegateImpl.class);
	        map.put(CancelOrderBackendDelegate.class, CancelOrderBackendDelegateImpl.class);
	        map.put(CurrencyBackendDelegate.class, CurrencyBackendDelegateImpl.class);
	        map.put(DailyMarketBackendDelegate.class, DailyMarketBackendDelegateImpl.class);
	        map.put(DocumentsBackendDelegate.class, DocumentsBackendDelegateImpl.class);
	        map.put(DownloadAttachmentsPDFBackendDelegate.class, DownloadAttachmentsPDFBackendDelegateImpl.class);
	        map.put(HistoricalDataBackendDelegate.class, HistoricalDataBackendDelegateImpl.class);
	        map.put(HoldingsListBackendDelegate.class, HoldingsListBackendDelegateImpl.class);
	        map.put(InstrumentDetailsBackendDelegate.class, InstrumentDetailsBackendDelegateImpl.class);
	        map.put(InstrumentMinimalBackendDelegate.class, InstrumentMinimalBackendDelegateImpl.class);
	        map.put(MarketOrderBackendDelegate.class, MarketOrderBackendDelegateImpl.class);
	        map.put(OrdersListBackendDelegate.class, OrdersListBackendDelegateImpl.class);
	        map.put(PortfolioDetailsBackendDelegate.class, PortfolioDetailsBackendDelegateImpl.class);
	        map.put(PricingDataBackendDelegate.class, PricingDataBackendDelegateImpl.class);
	        map.put(StockNewsBackendDelegate.class, StockNewsBackendDelegateImpl.class);
	        map.put(StockNewsStoryBackendDelegate.class, StockNewsStoryBackendDelegateImpl.class);
	        map.put(StockNewsWebBackendDelegate.class, StockNewsWebBackendDelegateImpl.class);
	        map.put(TransactionsListBackendDelegate.class, TransactionsListBackendDelegateImpl.class);
	        map.put(UserFavouriteInstrumentsBackendDelegate.class, UserFavouriteInstrumentsBackendDelegateImpl.class);
	        map.put(FavoriteInstrumentsBackendDelegate.class, FavoriteInstrumentsBackendDelegateImpl.class);
	        map.put(CurrencyConvertionBackendDelegate.class, CurrencyConvertionBackendDelegateImpl.class);
		return map;
	}
}
