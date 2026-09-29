package com.temenos.dbx.forexservices.dbservices;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.temenos.dbx.product.commons.businessdelegate.api.ApplicationBusinessDelegate;
import com.temenos.dbx.product.commonsutils.CommonUtils;
import com.temenos.dbx.product.constants.ServiceId;
import com.temenos.dbx.product.constants.TransactionStatusEnum;
import com.kony.dbputilities.dbutil.QueryFormer;
import com.kony.dbputilities.dbutil.SqlQueryEnum;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.URLFinder;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.bulkpaymentservices.dto.BulkPaymentRecordDTO;


public class ForexCurrencyDBServices {

	ApplicationBusinessDelegate application = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApplicationBusinessDelegate.class);
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	public static final String SCHEMA_NAME = EnvironmentConfigurationsHandler.getValue("DBX_SCHEMA_NAME");
	public static final String DB_BULKPAYMENT_FILERECORD_CREATE = SCHEMA_NAME + "_bulkpaymentrecordmock_create";
	public static final String DB_BULKPAYMENT_FILERECORD_GET = SCHEMA_NAME + "_bulkpaymentrecordmock_get";
	public static final String DB_BULKPAYMENT_FILERECORD_DELETE = SCHEMA_NAME + "_bulkpaymentrecordmock_delete";
	public static final String DB_BULKPAYMENT_FILERECORD_UPDATE = SCHEMA_NAME + "_bulkpaymentrecordmock_update";


	public Result fetchBaseCurrency(String countryCode, DataControllerRequest dcRequest) {
		Result result = new Result();
		
		try {
			JSONObject jsonResponse = new JSONObject();
			jsonResponse.put("symbol", "$");
			jsonResponse.put("code", "USD");
			jsonResponse.put("name", "US Dollar");
			result = JSONToResult.convert(jsonResponse.toString());
			
			return result;
            
		} catch (JSONException e) {
			alert.prepareError("Failed to fetch base currency from backend", e).log();
			return ErrorCodeEnum.ERR_27009.setErrorCode(result);
		} catch (Exception e) {
			alert.prepareError("Caught exception at fetchBaseCurrency: ", e).log();
			return ErrorCodeEnum.ERR_27009.setErrorCode(result);
		}
	}
	
	public Result fetchRates(String baseCurrencyCode, String quoteCurrencyCode, String marketId, DataControllerRequest dcRequest) {
		
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = "dbxdb_forex_proc_get";
		Result result = new Result();
		StringBuilder filter = new StringBuilder();
		char[] marketIdArray = marketId.toCharArray();
		if(Character.isDigit(marketIdArray[0]))
			marketId = "";
		String response = null;
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		String jdbcUrl=QueryFormer.getDBType(dcRequest);
	    //filter.append(SqlQueryEnum.valueOf(jdbcUrl+"_GetBaseCurrency").getQuery().replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)).replace("?2", countryCode));				
		String schema = URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest);
	    if(baseCurrencyCode != null && quoteCurrencyCode != null) {
	    	/*filter.append("select cur.code, cur.name, cur.symbol, cmr.marketId, cmr.buyRate, cmr.sellRate from " +
	    			schema + ".currencymarketrates cmr join " + 
	    			schema + ".currency cur on (cmr.quoteCurrencyCode = cur.code) where baseCurrencyCode = '" +
	    			baseCurrencyCode + "' AND quoteCurrencyCode = '" +
	    			quoteCurrencyCode + "'");*/
	    	filter.append(SqlQueryEnum.valueOf(jdbcUrl + "_GetBaseCurrency_CONDITION").getQuery().replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)).replace("?2", baseCurrencyCode).replace("?3",quoteCurrencyCode ));
	    	
	    }
	    if(marketId != null && !marketId.equals("")) {
	    	//filter.append(" AND marketId = '" + marketId + "'");
	    filter.append("("+SqlQueryEnum.valueOf(jdbcUrl + "_GetBaseCurrency_MARKETID").getQuery()+")= '" + marketId + "'");
	    }
	    filter.append(";");
	    requestParameters.put("read_query", filter.toString());
		
		try {
			response = DBPServiceExecutorBuilder.builder()
					.withServiceId(serviceName)
					.withObjectId(null)
					.withOperationId(operationName)
					.withRequestParameters(requestParameters)
					.build().getResponse();
			JSONObject jsonRsponse = new JSONObject(response);
			
			
			//result = JSONToResult.convert(jsonRsponse.toString());
			result = _processFetchRatesResponse(jsonRsponse);
			return result;
            
		} catch (JSONException e) {
			alert.prepareError("Failed to fetch base currency rates from backend", e).log();
			return ErrorCodeEnum.ERR_27010.setErrorCode(result);
		} catch (Exception e) {
			alert.prepareError("Caught exception at fetchRates: ", e).log();
			return ErrorCodeEnum.ERR_27010.setErrorCode(result);
		}
	}
	
	private Result _processFetchRatesResponse(JSONObject jsonRsponse) {
		Result result = new Result();
		JSONObject backendResponse = new JSONObject();
		JSONArray jsonArray = CommonUtils.getFirstOccuringArray(jsonRsponse);
		
		for(int i = 0; i < jsonArray.length(); i++) {
			JSONObject record = (JSONObject)jsonArray.get(i);
			if(i == 0 ) {
				backendResponse.put("symbol", record.getString("symbol"));
				backendResponse.put("code", record.getString("code"));
				backendResponse.put("name", record.getString("name"));
				backendResponse.put("markets", new JSONArray());
			}
			JSONObject market = new JSONObject();
			market.put("market", record.getString("marketId"));
			market.put("buyRate", record.getString("buyRate"));
			market.put("sellRate", record.getString("sellRate"));
			
			((JSONArray)backendResponse.get("markets")).put(market);
		}
		result = JSONToResult.convert(backendResponse.toString());
		return result;
	}

	public Result fetchCurrencyList(DataControllerRequest dcRequest) {
		
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = "dbxdb_forex_proc_get";
		Result result = new Result();
		StringBuilder filter = new StringBuilder();
		String response = null;
		String jdbcUrl=QueryFormer.getDBType(dcRequest);
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		//String schema = URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest);
	   // filter.append("select * from " + schema + ".currency");				
		filter.append(SqlQueryEnum.valueOf(jdbcUrl + "_CURRENCY").getQuery().replace("?1",  URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)));
	    requestParameters.put("read_query", filter.toString());
		
		try {
			response = DBPServiceExecutorBuilder.builder()
					.withServiceId(serviceName)
					.withObjectId(null)
					.withOperationId(operationName)
					.withRequestParameters(requestParameters)
					.build().getResponse();
			JSONObject jsonRsponse = new JSONObject(response);
			result = JSONToResult.convert(jsonRsponse.toString());
			
			return result;
            
		} catch (JSONException e) {
			alert.prepareError("Failed to fetch currency list from backend", e).log();
			return ErrorCodeEnum.ERR_27011.setErrorCode(result);
		} catch (Exception e) {
			alert.prepareError("Caught exception at fetchCurrencyList: ", e).log();
			return ErrorCodeEnum.ERR_27011.setErrorCode(result);
		}
	}	
}
