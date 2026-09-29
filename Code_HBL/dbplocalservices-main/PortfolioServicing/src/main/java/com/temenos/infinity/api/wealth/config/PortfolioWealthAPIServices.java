package com.temenos.infinity.api.wealth.config;

import com.temenos.infinity.api.commons.config.InfinityServices;

/**
 * 
 * {@link Enum} maintaining the list of Fabric services & operations of the
 * Arrangements Experience API
 * 
 * @author 19459
 *
 */
public enum PortfolioWealthAPIServices implements InfinityServices {

	WEALTH_GETINSTRUMENTTOTAL("WealthPortfolioT24Services", "getInstrumentTotal"),
	WEALTH_GETCASHACCOUNTS("WealthPortfolioT24Services", "getCashAccounts"),
	WEALTH_GETASSETALLOCATION("WealthPortfolioT24Services", "getAssetAllocation"),
	WEALTH_GETPORTFOLIOHOLDINGS("WealthPortfolioT24Services", "getPortfolioHoldings"),

	WEALTH_GETTRANSACTIONDETAILS("WealthPortfolioT24Services", "getTransactionDetails"),

	WEALTH_GETPORTFOLIOLIST("WealthPortfolioT24Services", "getPortfolioList"),

	WEALTH_GETORDERSDETAILS("WealthPortfolioT24Services", "getOrdersDetails"),
	WEALTH_GETACCOUNTACTIVITY("WealthPortfolioT24Services", "getAccountActivity"),
	WEALTH_GETACCOUNTACTIVITYOPERATIONS("WealthPortfolioServices", "getAccountActivityOperations"),
	WEALTH_GENERATEPDF("WealthPortfolioT24Services", "generatePDF"),
	WEALTH_GETPORTFOLIOPERFORMANCE("WealthPortfolioT24Services", "getPortfolioPerformance"),

	WEALTHSERVICESORCHESTRATION("WealthPortfolioOrch", "getStockNewsWeb"),
	SERVICE_BACKEND_CERTIFICATE("dbpRbLocalServicesdb", "dbxdb_backendcertificate_get"),
	WEALTH_GETREPORTDOWNLOADTYPES("WealthPortfolioServices", "getReportAndDownloadTypes"),

	WEALTH_GETPORTFOLIODETAILS("WealthPortfolioT24Services", "getPortfolioDetails"),

	
	WEALTH_GETMARKETDATA("WealthPortfolioRefServices", "getDailyMarket"),
	WEALTH_GETINSTRUMENTREFEDETAILS("WealthPortfolioRefServices", "getInstruments"),
	WEALTH_GETREFINITIVTOKEN("WealthPortfolioRefServices", "getToken"),
	WEALTH_GETASSETLIST("WealthPortfolioT24Services", "getAssetList"),
	WEALTH_GETREFINITIVTOPMARKETNEWS("WealthPortfolioRefServices", "getTopMarketNews"),
	WEALTH_GETDASHBOARDRECENTACTIVITY("WealthPortfolioT24Services", "getDashboardRecentActivity"),
	WEALTH_GETDASHBOARDGRAPHDATA("WealthPortfolioT24Services", "getDashboardGraphData"),
	WEALTH_GETFAVORITEINSTRUMENTS("WealthPortfolioRefServices", "getFavoriteInstruments"),
	GET_FAVOURITE_INSTRUMENT("WealthPortfolioOrch", "getFavoriteInstruments"),
	WEALTH_GETUSERFAVOURITES("WealthServices", "getUserFavouriteInstruments"),
	WEALTH_GETPORTFOLIOHEALTH("WealthPortfolioServices","getPortfolioHealth"),
	WEALTH_GETALLOCATION("WealthPortfolioT24Services","getAllocation"),
	WEALTH_GETPORTFOLIOHEALTHALLOCATION("WealthPortfolioServices","getAllocationHC"),
	WEALTH_GETPORTFOLIOHEALTHINVESTMENTCONSTRAINTS("WealthPortfolioServices","getInvestmentConstraintsHC"),
	WEALTH_GETPORTFOLIOHEALTHRECOMMENDEDINSTRUMENTS("WealthPortfolioServices","getRecommendedInstrumentsHC"),
	WEALTH_GETPORTFOLIOHEALTHRISKANALYSISHC("WealthPortfolioServices","getRiskAnalysisHC");
	

	private String serviceName, operationName;

	/**
	 * @param serviceName
	 * @param operationName
	 */
	private PortfolioWealthAPIServices(String serviceName, String operationName) {
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
