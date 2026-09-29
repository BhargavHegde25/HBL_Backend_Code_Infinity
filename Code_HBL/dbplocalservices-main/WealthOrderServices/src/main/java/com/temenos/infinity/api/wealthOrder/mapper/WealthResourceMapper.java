package com.temenos.infinity.api.wealthOrder.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;

import com.temenos.infinity.api.wealthOrder.resource.api.AddCurrencyResource;
import com.temenos.infinity.api.wealthOrder.resource.api.CancelOrderResource;
import com.temenos.infinity.api.wealthOrder.resource.api.CurrencyConvertionResource;
import com.temenos.infinity.api.wealthOrder.resource.api.CurrencyResource;
import com.temenos.infinity.api.wealthOrder.resource.api.DailyMarketResource;
import com.temenos.infinity.api.wealthOrder.resource.api.DocumentsResource;
import com.temenos.infinity.api.wealthOrder.resource.api.DownloadAttachmentsPDFResource;
import com.temenos.infinity.api.wealthOrder.resource.api.FavoriteInstrumentsResource;
import com.temenos.infinity.api.wealthOrder.resource.api.HistoricalDataResource;
import com.temenos.infinity.api.wealthOrder.resource.api.HoldingsListResource;
import com.temenos.infinity.api.wealthOrder.resource.api.InstrumentDetailsResource;
import com.temenos.infinity.api.wealthOrder.resource.api.InstrumentMinimalResource;
import com.temenos.infinity.api.wealthOrder.resource.api.MarketOrderResource;
import com.temenos.infinity.api.wealthOrder.resource.api.OrdersListResource;
import com.temenos.infinity.api.wealthOrder.resource.api.PortfolioDetailsResource;
import com.temenos.infinity.api.wealthOrder.resource.api.PricingDataResource;
import com.temenos.infinity.api.wealthOrder.resource.api.StockNewsResource;
import com.temenos.infinity.api.wealthOrder.resource.api.StockNewsStoryResource;
import com.temenos.infinity.api.wealthOrder.resource.api.StockNewsWebResource;
import com.temenos.infinity.api.wealthOrder.resource.api.TransactionsListResource;
import com.temenos.infinity.api.wealthOrder.resource.api.UserFavouriteInstrumentsResource;
import com.temenos.infinity.api.wealthOrder.resource.impl.AddCurrencyResourceImpl;
import com.temenos.infinity.api.wealthOrder.resource.impl.CancelOrderResourceImpl;
import com.temenos.infinity.api.wealthOrder.resource.impl.CurrencyConvertionResourceImpl;
import com.temenos.infinity.api.wealthOrder.resource.impl.CurrencyResourceImpl;
import com.temenos.infinity.api.wealthOrder.resource.impl.DocumentsResourceImpl;
import com.temenos.infinity.api.wealthOrder.resource.impl.DownloadAttachmentsPDFResourceImpl;
import com.temenos.infinity.api.wealthOrder.resource.impl.FavoriteInstrumentsResourceImpl;
import com.temenos.infinity.api.wealthOrder.resource.impl.HistoricalDataResourceImpl;
import com.temenos.infinity.api.wealthOrder.resource.impl.HoldingsListResourceImpl;
import com.temenos.infinity.api.wealthOrder.resource.impl.InstrumentDetailsResourceImpl;
import com.temenos.infinity.api.wealthOrder.resource.impl.InstrumentMinimalResourceImpl;
import com.temenos.infinity.api.wealthOrder.resource.impl.MarketOrderResourceImpl;
import com.temenos.infinity.api.wealthOrder.resource.impl.OrdersListResourceImpl;
import com.temenos.infinity.api.wealthOrder.resource.impl.PortfolioDetailsResourceImpl;
import com.temenos.infinity.api.wealthOrder.resource.impl.PricingDataResourceImpl;
import com.temenos.infinity.api.wealthOrder.resource.impl.StockNewsResourceImpl;
import com.temenos.infinity.api.wealthOrder.resource.impl.StockNewsStoryResourceImpl;
import com.temenos.infinity.api.wealthOrder.resource.impl.StockNewsWebResourceImpl;
import com.temenos.infinity.api.wealthOrder.resource.impl.TransactionsListResourceImpl;
import com.temenos.infinity.api.wealthOrder.resource.impl.UserFavouriteInstrumentsResourceImpl;
import com.temenos.infinity.api.wealthOrder.resource.impl.DailyMarketResourceImpl;

public class WealthResourceMapper implements DBPAPIMapper<Resource> {
	@Override
	public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {
		Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();

		// Add Mapping of Business Delegates interface and their implementation
		map.put(AddCurrencyResource.class, AddCurrencyResourceImpl.class);
		map.put(CancelOrderResource.class, CancelOrderResourceImpl.class);
		map.put(CurrencyResource.class, CurrencyResourceImpl.class);
		map.put(DailyMarketResource.class, DailyMarketResourceImpl.class);
		map.put(DocumentsResource.class, DocumentsResourceImpl.class);
		map.put(DownloadAttachmentsPDFResource.class, DownloadAttachmentsPDFResourceImpl.class);
		map.put(HistoricalDataResource.class, HistoricalDataResourceImpl.class);
		map.put(HoldingsListResource.class, HoldingsListResourceImpl.class);
		map.put(InstrumentDetailsResource.class, InstrumentDetailsResourceImpl.class);
		map.put(InstrumentMinimalResource.class, InstrumentMinimalResourceImpl.class);
		map.put(MarketOrderResource.class, MarketOrderResourceImpl.class);
		map.put(OrdersListResource.class, OrdersListResourceImpl.class);
		map.put(PortfolioDetailsResource.class, PortfolioDetailsResourceImpl.class);
		map.put(PricingDataResource.class, PricingDataResourceImpl.class);
		map.put(StockNewsResource.class, StockNewsResourceImpl.class);
		map.put(StockNewsStoryResource.class, StockNewsStoryResourceImpl.class);
		map.put(StockNewsWebResource.class, StockNewsWebResourceImpl.class);
		map.put(TransactionsListResource.class, TransactionsListResourceImpl.class);
		map.put(UserFavouriteInstrumentsResource.class, UserFavouriteInstrumentsResourceImpl.class);
		map.put(FavoriteInstrumentsResource.class, FavoriteInstrumentsResourceImpl.class);
		map.put(CurrencyConvertionResource.class, CurrencyConvertionResourceImpl.class);
		return map;
	}
}
