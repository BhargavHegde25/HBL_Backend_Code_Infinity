package com.temenos.infinity.api.wealth.mapper;

import java.util.HashMap;
import java.util.Map;
import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;

import com.temenos.infinity.api.wealth.resource.api.DailyMarketResource;
import com.temenos.infinity.api.wealth.resource.api.FavoriteInstrumentsResource;
import com.temenos.infinity.api.wealth.resource.api.InstrumentDetailsResource;
import com.temenos.infinity.api.wealth.resource.api.InstrumentMinimalResource;
import com.temenos.infinity.api.wealth.resource.api.TopMarketNewsResource;
import com.temenos.infinity.api.wealth.resource.api.TransactionsListResource;
import com.temenos.infinity.api.wealth.resource.api.UserFavouriteInstrumentsResource;
import com.temenos.infinity.api.wealth.resource.api.UserPreferenceResource;
import com.temenos.infinity.api.wealth.resource.api.WealthDashboardResource;
import com.temenos.infinity.api.wealth.resource.impl.DailyMarketResourceImpl;
import com.temenos.infinity.api.wealth.resource.impl.FavoriteInstrumentsResourceImpl;
import com.temenos.infinity.api.wealth.resource.impl.InstrumentDetailsResourceImpl;
import com.temenos.infinity.api.wealth.resource.impl.InstrumentMinimalResourceImpl;
import com.temenos.infinity.api.wealth.resource.impl.TopMarketNewsResourceImpl;
import com.temenos.infinity.api.wealth.resource.impl.TransactionsListResourceImpl;
import com.temenos.infinity.api.wealth.resource.impl.UserFavouriteInstrumentsResourceImpl;
import com.temenos.infinity.api.wealth.resource.impl.UserPreferenceResourceImpl;
import com.temenos.infinity.api.wealth.resource.impl.WealthDashboardResourceImpl;
import com.temenos.infinity.api.wealth.resource.api.AccountActivityResource;
import com.temenos.infinity.api.wealth.resource.api.DownloadAttachmentsPDFResource;
import com.temenos.infinity.api.wealth.resource.api.HoldingsListResource;
import com.temenos.infinity.api.wealth.resource.api.OrdersListResource;
import com.temenos.infinity.api.wealth.resource.api.PortfolioDetailsResource;

import com.temenos.infinity.api.wealth.resource.api.PortfolioPerformanceResource;
import com.temenos.infinity.api.wealth.resource.impl.AccountActivityResourceImpl;
import com.temenos.infinity.api.wealth.resource.impl.DownloadAttachmentsPDFResourceImpl;
import com.temenos.infinity.api.wealth.resource.impl.HoldingsListResourceImpl;
import com.temenos.infinity.api.wealth.resource.impl.OrdersListResourceImpl;
import com.temenos.infinity.api.wealth.resource.impl.PortfolioDetailsResourceImpl;

import com.temenos.infinity.api.wealth.resource.impl.PortfolioPerformanceResourceImpl;


public class PortfolioWealthResourceMapper implements DBPAPIMapper<Resource> {
	@Override
	public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {
		Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();

		// Add Mapping of Business Delegates interface and their implementation
		map.put(AccountActivityResource.class, AccountActivityResourceImpl.class);
		map.put(DownloadAttachmentsPDFResource.class, DownloadAttachmentsPDFResourceImpl.class);
		map.put(HoldingsListResource.class, HoldingsListResourceImpl.class);
		map.put(OrdersListResource.class, OrdersListResourceImpl.class);
		map.put(PortfolioDetailsResource.class, PortfolioDetailsResourceImpl.class);
		map.put(PortfolioPerformanceResource.class, PortfolioPerformanceResourceImpl.class);
		map.put(TransactionsListResource.class, TransactionsListResourceImpl.class);
		map.put(UserPreferenceResource.class, UserPreferenceResourceImpl.class);
		map.put(WealthDashboardResource.class, WealthDashboardResourceImpl.class);
		
		map.put(DailyMarketResource.class, DailyMarketResourceImpl.class);
		map.put(FavoriteInstrumentsResource.class, FavoriteInstrumentsResourceImpl.class);
		map.put(InstrumentDetailsResource.class, InstrumentDetailsResourceImpl.class);
		map.put(InstrumentMinimalResource.class, InstrumentMinimalResourceImpl.class);
		map.put(TopMarketNewsResource.class, TopMarketNewsResourceImpl.class);
		map.put(UserFavouriteInstrumentsResource.class, UserFavouriteInstrumentsResourceImpl.class);
		//map.put(PortfolioHealthResource.class, PortfolioHealthResourceImpl.class);
		
		return map;
	}
}
