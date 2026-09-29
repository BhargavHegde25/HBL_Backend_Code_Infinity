package com.temenos.infinity.api.wealth.mapper;

import java.util.HashMap;
import java.util.Map;
import com.dbp.core.api.BackendDelegate;
import com.dbp.core.api.DBPAPIMapper;

import com.temenos.infinity.api.wealth.backenddelegate.api.DailyMarketBackendDelegate;
import com.temenos.infinity.api.wealth.backenddelegate.api.FavoriteInstrumentsBackendDelegate;
import com.temenos.infinity.api.wealth.backenddelegate.api.InstrumentDetailsBackendDelegate;
import com.temenos.infinity.api.wealth.backenddelegate.api.InstrumentMinimalBackendDelegate;
import com.temenos.infinity.api.wealth.backenddelegate.api.PortfolioDetailsBackendDelegate;

import com.temenos.infinity.api.wealth.backenddelegate.api.TopMarketNewsBackendDelegate;
import com.temenos.infinity.api.wealth.backenddelegate.api.TransactionsListBackendDelegate;
import com.temenos.infinity.api.wealth.backenddelegate.api.UserFavouriteInstrumentsBackendDelegate;
import com.temenos.infinity.api.wealth.backenddelegate.api.UserPreferenceBackendDelegate;
import com.temenos.infinity.api.wealth.backenddelegate.api.WealthDashboardBackendDelegate;
import com.temenos.infinity.api.wealth.backenddelegate.impl.DailyMarketBackendDelegateImpl;
import com.temenos.infinity.api.wealth.backenddelegate.impl.FavoriteInstrumentsBackendDelegateImpl;
import com.temenos.infinity.api.wealth.backenddelegate.impl.InstrumentDetailsBackendDelegateImpl;
import com.temenos.infinity.api.wealth.backenddelegate.impl.InstrumentMinimalBackendDelegateImpl;
import com.temenos.infinity.api.wealth.backenddelegate.impl.PortfolioDetailsBackendDelegateImpl;

import com.temenos.infinity.api.wealth.backenddelegate.impl.TopMarketNewsBackendDelegateImpl;
import com.temenos.infinity.api.wealth.backenddelegate.impl.TransactionsListBackendDelegateImpl;
import com.temenos.infinity.api.wealth.backenddelegate.impl.UserFavouriteInstrumentsBackendDelegateImpl;
import com.temenos.infinity.api.wealth.backenddelegate.impl.UserPreferencesBackendDelegateImpl;
import com.temenos.infinity.api.wealth.backenddelegate.impl.WealthDashboardBackendDelegateImpl;
import com.temenos.infinity.api.wealth.backenddelegate.api.AccountActivityBackendDelegate;
import com.temenos.infinity.api.wealth.backenddelegate.api.DownloadAttachmentsPDFBackendDelegate;
import com.temenos.infinity.api.wealth.backenddelegate.api.HoldingsListBackendDelegate;
import com.temenos.infinity.api.wealth.backenddelegate.api.OrdersListBackendDelegate;
import com.temenos.infinity.api.wealth.backenddelegate.api.PortfolioPerformanceBackendDelegate;
import com.temenos.infinity.api.wealth.backenddelegate.impl.AccountActivityBackendDelegateImpl;
import com.temenos.infinity.api.wealth.backenddelegate.impl.DownloadAttachmentsPDFBackendDelegateImpl;
import com.temenos.infinity.api.wealth.backenddelegate.impl.HoldingsListBackendDelegateImpl;
import com.temenos.infinity.api.wealth.backenddelegate.impl.OrdersListBackendDelegateImpl;
import com.temenos.infinity.api.wealth.backenddelegate.impl.PortfolioPerformanceBackendDelegateImpl;

public class PortfolioWealthBackendDelegateMapper implements DBPAPIMapper<BackendDelegate> {
	@Override
	public Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> getAPIMappings() {
		 Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> map = new HashMap<>();

	        // Register your BackendDelegate Delegate Implementation classes here
	        map.put(AccountActivityBackendDelegate.class, AccountActivityBackendDelegateImpl.class);
	        map.put(DownloadAttachmentsPDFBackendDelegate.class, DownloadAttachmentsPDFBackendDelegateImpl.class);
	        map.put(HoldingsListBackendDelegate.class, HoldingsListBackendDelegateImpl.class);
	        map.put(OrdersListBackendDelegate.class, OrdersListBackendDelegateImpl.class);
	        map.put(PortfolioDetailsBackendDelegate.class, PortfolioDetailsBackendDelegateImpl.class);
	        map.put(PortfolioPerformanceBackendDelegate.class, PortfolioPerformanceBackendDelegateImpl.class);
	        map.put(TransactionsListBackendDelegate.class, TransactionsListBackendDelegateImpl.class);
	        map.put(UserPreferenceBackendDelegate.class, UserPreferencesBackendDelegateImpl.class);
	        map.put(WealthDashboardBackendDelegate.class, WealthDashboardBackendDelegateImpl.class);
	        
	        map.put(DailyMarketBackendDelegate.class, DailyMarketBackendDelegateImpl.class);
	        map.put(FavoriteInstrumentsBackendDelegate.class, FavoriteInstrumentsBackendDelegateImpl.class);
	        map.put(InstrumentDetailsBackendDelegate.class, InstrumentDetailsBackendDelegateImpl.class);
	        map.put(InstrumentMinimalBackendDelegate.class, InstrumentMinimalBackendDelegateImpl.class);
	        map.put(TopMarketNewsBackendDelegate.class, TopMarketNewsBackendDelegateImpl.class);
	        map.put(UserFavouriteInstrumentsBackendDelegate.class, UserFavouriteInstrumentsBackendDelegateImpl.class);
	        //map.put(PortfolioHealthBackendDelegate.class, PortfolioHealthBackendDelegateImpl.class);
		return map;
	}
}
