package com.temenos.infinity.api.wealth.mapper;

import java.util.HashMap;
import java.util.Map;
import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;

import com.temenos.infinity.api.wealth.businessdelegate.api.DailyMarketBusinessDelegate;
import com.temenos.infinity.api.wealth.businessdelegate.api.FavoriteInstrumentsBusinessDelegate;
import com.temenos.infinity.api.wealth.businessdelegate.api.InstrumentDetailsBusinessDelegate;
import com.temenos.infinity.api.wealth.businessdelegate.api.InstrumentMinimalBusinessDelegate;
import com.temenos.infinity.api.wealth.businessdelegate.api.TopMarketNewsBusinessDelegate;
import com.temenos.infinity.api.wealth.businessdelegate.api.TransactionsListBusinessDelegate;
import com.temenos.infinity.api.wealth.businessdelegate.api.UserFavouriteInstrumentsBusinessDelegate;
import com.temenos.infinity.api.wealth.businessdelegate.api.UserPreferenceBusinessDelegate;
import com.temenos.infinity.api.wealth.businessdelegate.api.WealthDashboardBusinessDelegate;
import com.temenos.infinity.api.wealth.businessdelegate.impl.DailyMarketBusinessDelegateImpl;
import com.temenos.infinity.api.wealth.businessdelegate.impl.FavoriteInstrumentsBusinessDelegateImpl;
import com.temenos.infinity.api.wealth.businessdelegate.impl.InstrumentDetailsBusinessDelegateImpl;
import com.temenos.infinity.api.wealth.businessdelegate.impl.InstrumentMinimalBusinessDelegateImpl;
import com.temenos.infinity.api.wealth.businessdelegate.impl.TopMarketNewsBusinessDelegateImpl;
import com.temenos.infinity.api.wealth.businessdelegate.impl.TransactionsListBusinessDelegateImpl;
import com.temenos.infinity.api.wealth.businessdelegate.impl.UserFavouriteInstrumentsBusinessDelegateImpl;
import com.temenos.infinity.api.wealth.businessdelegate.impl.UserPreferenceBusinessDelegateImpl;
import com.temenos.infinity.api.wealth.businessdelegate.impl.WealthDashboardBusinessDelegateImpl;
import com.temenos.infinity.api.wealth.businessdelegate.api.AccountActivityBusinessDelegate;
import com.temenos.infinity.api.wealth.businessdelegate.api.DownloadAttachmentsPDFBusinessDelegate;
import com.temenos.infinity.api.wealth.businessdelegate.api.HoldingsListBusinessDelegate;
import com.temenos.infinity.api.wealth.businessdelegate.api.OrdersListBusinessDelegate;
import com.temenos.infinity.api.wealth.businessdelegate.api.PortfolioDetailsBusinessDelegate;

import com.temenos.infinity.api.wealth.businessdelegate.api.PortfolioPerformanceBusinessDelegate;
import com.temenos.infinity.api.wealth.businessdelegate.impl.AccountActivityBusinessDelegateImpl;
import com.temenos.infinity.api.wealth.businessdelegate.impl.DownloadAttachmentsPDFBusinessDelegateImpl;
import com.temenos.infinity.api.wealth.businessdelegate.impl.HoldingsListBusinessDelegateImpl;
import com.temenos.infinity.api.wealth.businessdelegate.impl.OrdersListBusinessDelegateImpl;
import com.temenos.infinity.api.wealth.businessdelegate.impl.PortfolioDetailsBusinessDelegateImpl;

import com.temenos.infinity.api.wealth.businessdelegate.impl.PortfolioPerformanceBusinessDelegateImpl;

public class PortfolioWealthBusinessDelegateMapper implements DBPAPIMapper<BusinessDelegate> {
	 @Override
	    public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {
	        Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();

	        // Add Mapping of Business Delegates interface and their implementation
	        map.put(AccountActivityBusinessDelegate.class, AccountActivityBusinessDelegateImpl.class);
	        map.put(DownloadAttachmentsPDFBusinessDelegate.class, DownloadAttachmentsPDFBusinessDelegateImpl.class);
	        map.put(HoldingsListBusinessDelegate.class, HoldingsListBusinessDelegateImpl.class);
	        map.put(OrdersListBusinessDelegate.class, OrdersListBusinessDelegateImpl.class);
	        map.put(PortfolioDetailsBusinessDelegate.class, PortfolioDetailsBusinessDelegateImpl.class);
	        map.put(PortfolioPerformanceBusinessDelegate.class, PortfolioPerformanceBusinessDelegateImpl.class);
	        map.put(TransactionsListBusinessDelegate.class, TransactionsListBusinessDelegateImpl.class);
	        map.put(UserPreferenceBusinessDelegate.class, UserPreferenceBusinessDelegateImpl.class);
	        map.put(WealthDashboardBusinessDelegate.class, WealthDashboardBusinessDelegateImpl.class);
	        
	        map.put(DailyMarketBusinessDelegate.class, DailyMarketBusinessDelegateImpl.class);
	        map.put(FavoriteInstrumentsBusinessDelegate.class, FavoriteInstrumentsBusinessDelegateImpl.class);
	        map.put(InstrumentDetailsBusinessDelegate.class, InstrumentDetailsBusinessDelegateImpl.class);
	        map.put(InstrumentMinimalBusinessDelegate.class, InstrumentMinimalBusinessDelegateImpl.class);
	        map.put(TopMarketNewsBusinessDelegate.class, TopMarketNewsBusinessDelegateImpl.class);
	        map.put(UserFavouriteInstrumentsBusinessDelegate.class, UserFavouriteInstrumentsBusinessDelegateImpl.class);
	      //  map.put(PortfolioHealthBusinessDelegate.class,PortfolioHealthBusinessDelegateImpl.class );
	        
	        return map;
	    }
}
