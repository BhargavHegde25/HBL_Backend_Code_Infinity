package com.temenos.dbx.product.forexservices.businessdelegate.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.constants.DBPConstants;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.temenos.dbx.product.commonsutils.CommonUtils;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.URLFinder;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.forexservices.backenddelegate.api.ForexBackendDelegate;
import com.temenos.dbx.product.forexservices.backenddelegate.impl.ForexBackendDelegateImpl;
import com.temenos.dbx.product.forexservices.businessdelegate.api.ForexBusinessDelegate;
import com.temenos.dbx.product.forexservices.dto.CurrencyDTO;

public class ForexBusinessDelegateImpl implements ForexBusinessDelegate {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	ForexBackendDelegate forexBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(ForexBackendDelegate.class);
	
	@Override
	public List<CurrencyDTO> getPopularCurrencies(String baseCurrencyCode, DataControllerRequest dcRequest) {
		
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.FOREXOROCGET;
		StringBuilder filter = new StringBuilder();
		String response = null;
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		String schema = URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest);
	    filter.append("select * from " +schema+ ".currency where code in (select quoteCurrencyCode from "+schema+".popularcurrencies where baseCurrencyCode = '" + baseCurrencyCode + "');");				
		
	    requestParameters.put("read_query", filter.toString());
		
		try {
			response = DBPServiceExecutorBuilder.builder()
					.withServiceId(serviceName)
					.withObjectId(null)
					.withOperationId(operationName)
					.withRequestParameters(requestParameters)
					.build().getResponse();
			JSONObject responseObj = new JSONObject(response);
			JSONArray jsonArray = CommonUtils.getFirstOccuringArray(responseObj);
			
			List<CurrencyDTO> popularCurrencies = JSONUtils.parseAsList(jsonArray.toString(), CurrencyDTO.class);			
			return popularCurrencies;
            
		} catch (JSONException e) {
			alert.prepareError("Failed to fetch popular currencies ", e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at getPopularCurrencies: ", e).log();
			return null;
		}		
	}	
	
	@Override
	public List<CurrencyDTO> getRecentCurrencies(String customerId, DataControllerRequest dcRequest) {
		
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = "dbxdb_forex_proc_get";
		Map<String, Object> customer = CustomerSession.getCustomerMap(dcRequest);
		String legalEntityId = (String) customer.get("legalEntityId");
		StringBuilder filter = new StringBuilder();
		String response = null;
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		String schema = URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest);
		if(StringUtils.isBlank(legalEntityId))
	    filter.append(
	    	"select " +
	    	schema + ".currency.code,"+
	    	schema + ".currency.name,"+
	    	schema + ".currency.symbol,"+
	    	schema + ".currency.createdby,"+
	    	schema + ".currency.createdts,"+
	    	schema + ".currency.modifiedby,"+
	    	schema + ".currency.lastmodifiedts,"+
	    	schema + ".currency.softdeleteflag"+
	    	" from " + schema + ".currency, " + schema + ".recentcurrencies" + 
	    	" where currency.code = recentcurrencies.quoteCurrencyCode"+
	    	" and recentcurrencies.customerId = '" + customerId + "'" +
	    	" ORDER BY recentcurrencies.createdts DESC;"
	    );
		else
			  filter.append(
				    	"select " +
				    	schema + ".currency.code,"+
				    	schema + ".currency.name,"+
				    	schema + ".currency.symbol,"+
				    	schema + ".currency.createdby,"+
				    	schema + ".currency.createdts,"+
				    	schema + ".currency.modifiedby,"+
				    	schema + ".currency.lastmodifiedts,"+
				    	schema + ".currency.softdeleteflag"+
				    	" from " + schema + ".currency, " + schema + ".recentcurrencies" + 
				    	" where currency.code = recentcurrencies.quoteCurrencyCode"+
				    	" and recentcurrencies.customerId = '" + customerId + "'" + 
				    	" and recentcurrencies.legalEntityId = '" + legalEntityId + "'" +
				    	" ORDER BY recentcurrencies.createdts DESC;"
				    );	
		
	    requestParameters.put("read_query", filter.toString());
		
		try {
			response = DBPServiceExecutorBuilder.builder()
					.withServiceId(serviceName)
					.withObjectId(null)
					.withOperationId(operationName)
					.withRequestParameters(requestParameters)
					.build().getResponse();
			JSONObject responseObj = new JSONObject(response);
			JSONArray jsonArray = CommonUtils.getFirstOccuringArray(responseObj);
			
			List<CurrencyDTO> recentCurrencies = JSONUtils.parseAsList(jsonArray.toString(), CurrencyDTO.class);			
			return recentCurrencies;
            
		} catch (JSONException e) {
			alert.prepareError("Failed to fetch recent currencies ", e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at getRecentCurrencies: ", e).log();
			return null;
		}		
	}	
	
	@Override
	public List<CurrencyDTO> getDashboardCurrencies(String methodID, String customerId, String baseCurrencyCode, DataControllerRequest dcRequest) {

		
		try {
			List<CurrencyDTO> allCurrencies = forexBackendDelegate.getCurrencyListFromBackend();
			List<CurrencyDTO> recentCurrencies = getRecentCurrencies(customerId, dcRequest);
			List<CurrencyDTO> popularCurrencies = null;
			if(recentCurrencies.size() < 5 ) {
				popularCurrencies = getPopularCurrencies(baseCurrencyCode, dcRequest);
				int count = 5 - recentCurrencies.size();
				int popularCurrenciesIndex = 0;
				while (count > 0 && popularCurrenciesIndex < popularCurrencies.size()) {
					if (!_popularCurrencyExistsInRecentCurrencies(recentCurrencies,
							popularCurrencies.get(popularCurrenciesIndex))) {
						recentCurrencies.add(popularCurrencies.get(popularCurrenciesIndex));
						count--;
					}
					popularCurrenciesIndex++;
				}					
			}				
			allCurrencies.addAll(0, recentCurrencies);			
			return allCurrencies;
            
		} catch (JSONException e) {
			alert.prepareError("Failed to fetch dashboard currencies", e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at getDashboardCurrencies: ", e).log();
			return null;
		}		
	}

	@Override
	public boolean updateRecentCurrencies(String customerId, String quoteCurrencyCode, String legalEntityId) {

		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.UPDATE_USER_RECENT_CURRENCY_PROC;
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		
		try {
			requestParameters.put("_customerId", customerId);
			requestParameters.put("_currencyCode", quoteCurrencyCode);
			requestParameters.put("_legalEntityId", legalEntityId);
			String response = DBPServiceExecutorBuilder.builder()
					.withServiceId(serviceName)
					.withObjectId(null)
					.withOperationId(operationName)
					.withRequestParameters(requestParameters)
					.build().getResponse();
			JSONObject responseObj = new JSONObject(response);
			if(responseObj == null || responseObj.has(DBPConstants.DBP_ERROR_CODE_KEY)) {
				return false;
			}
		}
		catch(Exception exp) {
			alert.prepareError("Unable to update recent currencies of the user", exp).log();
			return false;
		}
		return true;
		
	}	

	public List<CurrencyDTO> getDashboardCurrencyRates(String methodID, String customerId, String baseCurrencyCode, String market, String companyCode,
			DataControllerRequest dcRequest) {
		try {
			List<CurrencyDTO> recentCurrencies = getRecentCurrencies(customerId, dcRequest);
			List<CurrencyDTO> popularCurrencies = null;
			if (recentCurrencies.size() < 5) {
				popularCurrencies = getPopularCurrencies(baseCurrencyCode, dcRequest);
				int count = 5 - recentCurrencies.size();
				int popularCurrenciesIndex = 0;
				while (count > 0 && popularCurrenciesIndex < popularCurrencies.size()) {
					if (!_popularCurrencyExistsInRecentCurrencies(recentCurrencies,
							popularCurrencies.get(popularCurrenciesIndex))) {
						recentCurrencies.add(popularCurrencies.get(popularCurrenciesIndex));
						count--;
					}
					popularCurrenciesIndex++;
				}
			}

			StringBuilder baseCurrencyCodes = new StringBuilder();
			StringBuilder quoteCurrencyCodes = new StringBuilder();
			StringBuilder markets = new StringBuilder();

			for (int i = 0; i < recentCurrencies.size(); i++) {
				baseCurrencyCodes.append(baseCurrencyCode);
				if (i != recentCurrencies.size() - 1) {
					baseCurrencyCodes.append(".");
				}
				quoteCurrencyCodes.append(recentCurrencies.get(i).getCode());
				if (i != recentCurrencies.size() - 1) {
					quoteCurrencyCodes.append(".");
				}

				if (market != null) {
					markets.append(market);
					if (i != recentCurrencies.size() - 1) {
						markets.append("."); 
					}							
				}

			}
			int loopCount = recentCurrencies.size();
			List<CurrencyDTO> currencies = forexBackendDelegate.getRecentOrPopularCurrencyRatesFromBackend(
					 dcRequest, methodID, baseCurrencyCodes.toString(), quoteCurrencyCodes.toString(), markets.toString(), companyCode.toString(), loopCount);
			return currencies;
		} catch (JSONException e) {
			alert.prepareError("Failed to fetch dashboard currency rates", e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at getDashboardCurrencyRates: ", e).log();
			return null;
		}
	}
	
	private boolean _popularCurrencyExistsInRecentCurrencies(List<CurrencyDTO> recentCurrencies, CurrencyDTO popularCurrency) {
		for(int i = 0; i < recentCurrencies.size(); i++) {
			if(recentCurrencies.get(i).getCode().equals(popularCurrency.getCode())) {
				return true;
			}
		}
		return false;
	}

}
