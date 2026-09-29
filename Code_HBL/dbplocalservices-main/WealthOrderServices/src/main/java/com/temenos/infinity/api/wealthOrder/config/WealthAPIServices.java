package com.temenos.infinity.api.wealthOrder.config;

import com.temenos.infinity.api.commons.config.InfinityServices;

/**
 * 
 * {@link Enum} maintaining the list of Fabric services & operations of the Arrangements Experience API
 * 
 * @author 19459
 *
 */
public enum WealthAPIServices implements InfinityServices {
//	WEALTH_GETMARKETRATES("WealthTransactServices", "getMarketRates"),
//	WEALTH_CREATEORDER("WealthTransactServices", "createOrder"),
	WEALTH_GETINSTRUMENTTOTAL("WealthTransactServices","getInstrumentTotal"),
	WEALTH_GETCASHACCOUNTS("WealthTransactServices","getCashAccounts"),
	WEALTH_GETASSETALLOCATION("WealthTransactServices","getAssetAllocation"),
	WEALTH_GETPORTFOLIOHOLDINGS("WealthTransactServices","getPortfolioHoldings"),
//	WEALTH_GETINSTRUMENTDETAILS("WealthTransactServices", "getInstrumentDetails"),
	WEALTH_GETHISTORICALDATA("WealthRefinitivServices", "getHistoricalData"),
//	WEALTH_GETPRICINGCHARTDAILY("WealthRefinitivServices", "getPricingChartDaily"),
	WEALTH_GETTRANSACTIONDETAILS("WealthTransactServices","getTransactionDetails"), 
	WEALTH_VIEWINSTRUMENTTRANSACTIONS("WealthTransactServices","viewInstrumentTransactions"),
	WEALTH_GETCURRENTPOSITION("WealthTransactServices", "getCurrentPosition"),
	WEALTH_GETPRICINGDATA("WealthTransactServices", "getPricingData"), 
	WEALTH_GETSTOCKNEWS("WealthTransactServices", "getStockNews"),
	WEALTH_GETADDCURRENCY("WealthTransactServices", "getAddCurrency"),
	WEALTH_GETSEARCHINSTRUMENTLIST("WealthTransactServices", "getSearchInstrumentList"),
	WEALTH_GETMARKETDATA("ReutersServices", "getMarketData"),
	WEALTH_GETINSTRUMENTREFEDETAILS("WealthRefinitivServices", "getInstruments"),
//	WEALTH_GETREFINITIVSTOCKNEWS("WealthRefinitivServices", "getNews"),
	WEALTH_GETREFINITIVSTOCKNEWSSTORY("WealthRefinitivServices", "getNewsStory"),
	WEALTH_GETREFINITIVTOKEN("WealthOrderRefinitivServices", "getToken"),
	WEALTH_GETPORTFOLIOLIST("WealthTransactServices","getPortfolioList"),
	WEALTH_GETASSETLIST("WealthTransactServices","getAssetList"),
//	WEALTH_GETREFINITIVTOPMARKETNEWS("WealthRefinitivServices","getTopMarketNews"),
	WEALTH_GETDASHBOARDRECENTACTIVITY("WealthTransactServices","getDashboardRecentActivity"),
//	WEALTH_CREATEMARKETORDER("WealthTransactServices","createMarketOrder"),
    WEALTH_GETREFINITIVEDAILYMARKET("WealthOrderRefinitivServices", "getDailyMarket"),
    WEALTH_GETDASHBOARDGRAPHDATA("WealthTransactServices","getDashboardGraphData"),
	WEALTH_GETORDERSDETAILS("WealthTransactServices","getOrdersDetails"),
//	WEALTH_GETACCOUNTACTIVITY("WealthTransactServices","getAccountActivity"),
	WEALTH_GETDOCUMENTS("WealthRefinitivServices", "getDocuments"),
//	WEALTH_GETACCOUNTACTIVITYOPERATIONS("WealthServices","getAccountActivityOperations"),
	WEALTH_CANCELORDER("WealthTransactServices","cancelOrder"),
	WEALTH_GENERATEPDF("WealthTransactServices","generatePDF"),
//	WEALTH_GETPORTFOLIOPERFORMANCE("WealthTransactServices","getPortfolioPerformance"),
	WEALTH_GETFAVORITEINSTRUMENTS("WealthRefinitivServices", "getFavoriteInstruments"),
	WEALTH_GETSTOCKNEWSWEB("WealthTransactServices", "getStockNewsWeb"),
	WEALTHSERVICESORCHESTRATION("WealthServicesOrchestration","getStockNewsWeb"),
	CREATE_MARKET_ORDER("WealthServicesOrchestration","createMarketOrder"),
	SERVICE_BACKEND_CERTIFICATE("dbpRbLocalServicesdb", "dbxdb_backendcertificate_get"),
	GET_MARKET_RATES("WealthServicesOrchestration","getMarketOrder"),
	CREATE_FOREX_ORDERS("WealthServicesOrchestration","createForexOrders"),
//	GET_ADD_CURRENCY("WealthServicesOrchestration","getAddCurrency"),
	GET_FAVOURITE_INSTRUMENT("WealthServicesOrchestration","getFavoriteInstruments"),
//	WEALTH_GETREPORTDOWNLOADTYPES("WealthServices","getReportAndDownloadTypes"),
	MODIFYORDER("WealthTransactServices","modifyOrder"),
	WEALTH_GETUSERFAVOURITES("WealthServices", "getUserFavouriteInstruments"),
//	ADDACCOUNTTOPORTFOLIO("WealthTransactServices","addAccountToPortfolio"),
	WEALTH_GETPORTFOLIODETAILS("WealthTransactServices", "getPortfolioDetails"),
	CREATE_CURRENCY_CONVERTION("WealthTransactServices", "createCurrencyConvertion");

	private String serviceName, operationName;
	
	/**
     * @param serviceName
     * @param operationName
     */
    private WealthAPIServices(String serviceName, String operationName) {
        this.serviceName = serviceName;
        this.operationName = operationName;
    }

    @Override
    public String getServiceName() {
        return this.serviceName;
    }

    @Override
    public String getOperationName() {
        return this.operationName;
    }
	
}
