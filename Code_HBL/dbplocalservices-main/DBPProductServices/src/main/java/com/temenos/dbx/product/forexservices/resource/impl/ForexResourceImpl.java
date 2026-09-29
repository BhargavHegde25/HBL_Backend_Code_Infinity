package com.temenos.dbx.product.forexservices.resource.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.constants.FeatureAction;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.bulkpaymentservices.backenddelegate.api.BulkPaymentRecordBackendDelegate;
import com.temenos.dbx.product.bulkpaymentservices.businessdelegate.api.BulkPaymentRecordBusinessDelegate;
import com.temenos.dbx.product.bulkpaymentservices.resource.impl.BulkPaymentRecordResourceImpl;
import com.temenos.dbx.product.commons.businessdelegate.api.AuthorizationChecksBusinessDelegate;
import com.temenos.dbx.product.forexservices.backenddelegate.api.ForexBackendDelegate;
import com.temenos.dbx.product.forexservices.businessdelegate.api.ForexBusinessDelegate;
import com.temenos.dbx.product.forexservices.dto.CurrencyDTO;
import com.temenos.dbx.product.forexservices.resource.api.ForexResource;

public class ForexResourceImpl implements ForexResource {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	ForexBackendDelegate forexBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(ForexBackendDelegate.class);
	ForexBusinessDelegate forexBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ForexBusinessDelegate.class);
	AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = (AuthorizationChecksBusinessDelegate)DBPAPIAbstractFactoryImpl.getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);
	@Override
	public Result fetchAllCurrencies(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public Result fetchBaseCurrency(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		//permission check
		Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		String customerId = CustomerSession.getCustomerId(customer);
		//permission check
        Boolean isFXRatesView = authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction(customerId, "FX_RATES_VIEW", null, CustomerSession.IsCombinedUser(customer));
        Boolean isFXRatesViewCaluculator = authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction(customerId, "FX_RATES_VIEW_CALCULATOR", null, CustomerSession.IsCombinedUser(customer));
        diagnostic.prepareDebug("isFXRatesView" + isFXRatesView).log();
        diagnostic.prepareDebug("isFXRatesView" + isFXRatesViewCaluculator).log();
        //String features = CustomerSession.getPermittedActionIds(request, requiredActionIds);
        if(!isFXRatesView && !isFXRatesViewCaluculator) {
            return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
        }
		
		Result result = null;
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
		String CountryCode = inputParams.get("CountryCode") != null ? inputParams.get("CountryCode").toString() : null;
		String market = inputParams.get("market") != null ? inputParams.get("market").toString() : null;
		String companyCode = inputParams.get("companyCode") != null ? inputParams.get("companyCode").toString() : null;
		
		CurrencyDTO baseCurrency = forexBackendDelegate.getBaseCurrencyFromBackend(request, methodID, market, companyCode,  CountryCode);
		
		if(baseCurrency == null ) {
			return ErrorCodeEnum.ERR_27001.setErrorCode(new Result());
		}
		
		try {
			JSONObject baseCurrencyObject = new JSONObject(baseCurrency);
			result = JSONToResult.convert(baseCurrencyObject.toString());
		} catch (JSONException e) {
			alert.prepareError("Error occured while fetching base currency", e).log();
			return ErrorCodeEnum.ERR_27002.setErrorCode(result);			
		}

		return result;		
	}
	
	@SuppressWarnings({ "unchecked" })
	@Override
	public Result fetchDashboardCurrencyList(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) {
		Map<String, Object> customer = CustomerSession.getCustomerMap(dcRequest);
		//permission check
		String customerId = CustomerSession.getCustomerId(customer);
		//permission check
        Boolean isFXRatesView = authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction(customerId, "FX_RATES_VIEW", null, CustomerSession.IsCombinedUser(customer));
        Boolean isFXRatesViewCaluculator = authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction(customerId, "FX_RATES_VIEW_CALCULATOR", null, CustomerSession.IsCombinedUser(customer));
        diagnostic.prepareDebug("isFXRatesView" + isFXRatesView).log();
        diagnostic.prepareDebug("isFXRatesView" + isFXRatesViewCaluculator).log();
        //String features = CustomerSession.getPermittedActionIds(request, requiredActionIds);
        if(!isFXRatesView && !isFXRatesViewCaluculator) {
            return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
        }

		Result result = null;
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
		String baseCurrencyCode = inputParams.get("baseCurrencyCode") != null ? inputParams.get("baseCurrencyCode").toString() : null;
		
		if(baseCurrencyCode == null ) {
			return ErrorCodeEnum.ERR_27013.setErrorCode(new Result());
		}


		//fetch baseCodeCurrency from backend to validate against request payload
		String companyId=String.valueOf(customer.get("companyId"));
		String countryCode=String.valueOf(customer.get("countrycode"));
		String marketCode="10 1";
		CurrencyDTO baseCurrency = forexBackendDelegate.getBaseCurrencyFromBackend(dcRequest, "fetchBaseCurrency", marketCode, companyId,  countryCode);

		if(baseCurrency == null ) {
			return ErrorCodeEnum.ERR_27001.setErrorCode(new Result());
		}
		if(!baseCurrency.getCode().equals(baseCurrencyCode))
			return ErrorCodeEnum.ERR_28002.setErrorCode(new Result());



		List<CurrencyDTO> currencyList = forexBusinessDelegate.getDashboardCurrencies(methodID, customerId, baseCurrencyCode, dcRequest);
		
		if(currencyList == null ) {
			return ErrorCodeEnum.ERR_27012.setErrorCode(new Result());
		}
		
		try {
			JSONArray resultRecords = new JSONArray(currencyList);
			JSONObject resultObject = new JSONObject();
			resultObject.put(Constants.CURRENCIES, resultRecords);
			result = JSONToResult.convert(resultObject.toString());			
		} catch (JSONException e) {
			alert.prepareError("Error occured while fetching dashboard currency list", e).log();
			return ErrorCodeEnum.ERR_27003.setErrorCode(result);			
		}

		return result;		
	}	

	@SuppressWarnings("unchecked")
	@Override
	public Result fetchCurrencyRates(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) {
		//permission check
		Map<String, Object> customer = CustomerSession.getCustomerMap(dcRequest);
		String customerId = CustomerSession.getCustomerId(customer);
		//permission check
        Boolean isFXRatesView = authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction(customerId, "FX_RATES_VIEW", null, CustomerSession.IsCombinedUser(customer));
        Boolean isFXRatesViewCaluculator = authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction(customerId, "FX_RATES_VIEW_CALCULATOR", null, CustomerSession.IsCombinedUser(customer));
        diagnostic.prepareDebug("isFXRatesView" + isFXRatesView).log();
        diagnostic.prepareDebug("isFXRatesView" + isFXRatesViewCaluculator).log();
        //String features = CustomerSession.getPermittedActionIds(request, requiredActionIds);
        if(!isFXRatesView && !isFXRatesViewCaluculator) {
            return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
        }
		
		Result result = null;
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
		String baseCurrencyCode = inputParams.get("baseCurrencyCode") != null ? inputParams.get("baseCurrencyCode").toString() : null;
		String quoteCurrencyCode = inputParams.get("quoteCurrencyCode") != null ? inputParams.get("quoteCurrencyCode").toString() : null;
		String market = inputParams.get("market") != null ? inputParams.get("market").toString() : null;				
		String companyCode = inputParams.get("companyCode") != null ? inputParams.get("companyCode").toString() : null; 

		if(baseCurrencyCode == null || quoteCurrencyCode == null) {
			return ErrorCodeEnum.ERR_27004.setErrorCode(new Result());
		}
		String legalEntityId=(String) CustomerSession.getCustomerMap(dcRequest).get("legalEntityId");
		if(companyCode!=null && !companyCode.equalsIgnoreCase(legalEntityId)) {
			alert.prepareError("Company Code does not belong to the logged in user").log();
			return ErrorCodeEnum.ERR_29064.setErrorCode(new Result());
		}

		CurrencyDTO currency = forexBackendDelegate.getCurrencyRatesFromBackend(dcRequest, methodID, baseCurrencyCode, quoteCurrencyCode, market, companyCode);

		if(currency == null) {
			return ErrorCodeEnum.ERR_27014.setErrorCode(new Result());
		}
		
		try {
			JSONObject baseCurrencyObject = new JSONObject(currency);
			result = JSONToResult.convert(baseCurrencyObject.toString());
		} catch (JSONException e) {
			alert.prepareError("Error occured while fetching currency rates", e).log();
			return ErrorCodeEnum.ERR_27005.setErrorCode(result);			
		}

		return result;	
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Result fetchDashboardCurrencyRates(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) {
		//permission check
		Map<String, Object> customer = CustomerSession.getCustomerMap(dcRequest);
		String customerId = CustomerSession.getCustomerId(customer);
		//permission check
        Boolean isFXRatesView = authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction(customerId, "FX_RATES_VIEW", null, CustomerSession.IsCombinedUser(customer));
        Boolean isFXRatesViewCaluculator = authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction(customerId, "FX_RATES_VIEW_CALCULATOR", null, CustomerSession.IsCombinedUser(customer));
        diagnostic.prepareDebug("isFXRatesView" + isFXRatesView).log();
        diagnostic.prepareDebug("isFXRatesView" + isFXRatesViewCaluculator).log();
        //String features = CustomerSession.getPermittedActionIds(request, requiredActionIds);
        if(!isFXRatesView && !isFXRatesViewCaluculator) {
            return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
        }
		
		Result result = null;
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
		
		String baseCurrencyCode = inputParams.get("baseCurrencyCode") != null ? inputParams.get("baseCurrencyCode").toString() : null;
		String market = inputParams.get("market") != null ? inputParams.get("market").toString() : null;
		String companyCode = inputParams.get("companyCode") != null ? inputParams.get("companyCode").toString() : null;
		

		if(baseCurrencyCode == null) {
			return ErrorCodeEnum.ERR_27006.setErrorCode(new Result());
		}
		if(companyCode!=null && !companyCode.equalsIgnoreCase((String) customer.get("legalEntityId"))) {
			alert.prepareError("Company Code does not belong to the logged in user").log();
			return ErrorCodeEnum.ERR_29064.setErrorCode(new Result());
		}
		
		List<CurrencyDTO> currencyList = forexBusinessDelegate.getDashboardCurrencyRates(methodID,customerId,baseCurrencyCode,market, companyCode, dcRequest);

		if(currencyList == null) {
			return ErrorCodeEnum.ERR_27015.setErrorCode(new Result());
		}		
		
		try {
			JSONArray resultRecords = new JSONArray(currencyList);
			JSONObject resultObject = new JSONObject();
			resultObject.put(Constants.CURRENCIES, resultRecords);
			result = JSONToResult.convert(resultObject.toString());	
		} catch (JSONException e) {
			alert.prepareError("Error occured while fetching dashboard currency rates", e).log();
			return ErrorCodeEnum.ERR_27007.setErrorCode(result);			
		}

		return result;	
	}	

	@SuppressWarnings("unchecked")
	@Override
	public Result fetchPopularCurrencies(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) {
		Result result = null;	
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
		String baseCurrencyCode = inputParams.get("baseCurrencyCode") != null ? inputParams.get("baseCurrencyCode").toString() : null;		
		List<CurrencyDTO> baseCurrency = forexBusinessDelegate.getPopularCurrencies(baseCurrencyCode,dcRequest);
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Result fetchRecentCurrencies(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) {
		Result result = null;	
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
		String baseCurrencyCode = inputParams.get("baseCurrencyCode") != null ? inputParams.get("baseCurrencyCode").toString() : null;		
		List<CurrencyDTO> baseCurrency = forexBusinessDelegate.getRecentCurrencies(baseCurrencyCode,dcRequest);
		return result;
	}

	@Override
	public Result updateRecentCurrencies(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		
		Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		String customerId = CustomerSession.getCustomerId(customer);
		//permission check
        Boolean isFXRatesView = authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction(customerId, "FX_RATES_VIEW", null, CustomerSession.IsCombinedUser(customer));
        Boolean isFXRatesViewCaluculator = authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction(customerId, "FX_RATES_VIEW_CALCULATOR", null, CustomerSession.IsCombinedUser(customer));
        diagnostic.prepareDebug("isFXRatesView" + isFXRatesView).log();
        diagnostic.prepareDebug("isFXRatesView" + isFXRatesViewCaluculator).log();
        //String features = CustomerSession.getPermittedActionIds(request, requiredActionIds);
        if(!isFXRatesView && !isFXRatesViewCaluculator) {
            return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
        }
		
		String legalEntityId = (String) customer.get("legalEntityId");
		
		@SuppressWarnings("unchecked")
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
		String quoteCurrencyCode = inputParams.get("quoteCurrencyCode") != null ? inputParams.get("quoteCurrencyCode").toString() : null;
		
		if(quoteCurrencyCode == null) {
			return ErrorCodeEnum.ERR_27008.setErrorCode(new Result());
		}

		String message = null;
		boolean isSuccess = forexBusinessDelegate.updateRecentCurrencies(customerId, quoteCurrencyCode, legalEntityId);
		if(isSuccess) {
			message = "Recent currencies of the user updated successfully";
		}
		else {
			message = "Failed to update recent currencies of the user";
		}
		
		Result result = new Result();
		Param successParam = new Param("message", message);
		result.addParam(successParam);
		return result;
	}

}
