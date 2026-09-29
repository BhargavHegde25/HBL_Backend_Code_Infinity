package com.temenos.dbx.product.transactionservices.resource.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.infinity.dbx.dbp.jwt.auth.utils.CommonUtils;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.temenos.dbx.product.commons.dto.FilterDTO;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.constants.FeatureAction;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.GeneralTransactionsBusinessDelegate;
import com.temenos.dbx.product.transactionservices.dto.GeneralTransactionDTO;
import com.temenos.dbx.product.transactionservices.resource.api.GeneralTransactionsResource;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.JSONUtil;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.convertions.ResultToJSON;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;

public class GeneralTransactionsResourceImpl implements GeneralTransactionsResource{

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	
	@Override
	public Result fetchAllGeneralTransactions(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		
		Result result = null;
		GeneralTransactionsBusinessDelegate generalTransactionBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(GeneralTransactionsBusinessDelegate.class);
		
		try {
			
			Map<String, Object> customer = CustomerSession.getCustomerMap(request);
			String userId = CustomerSession.getCustomerId(customer);
			
			List<String> requiredActionIds = Arrays.asList(
					FeatureAction.BILL_PAY_VIEW_PAYMENTS,
					FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_VIEW,
					FeatureAction.INTRA_BANK_FUND_TRANSFER_VIEW,
					FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_VIEW,
					FeatureAction.P2P_VIEW,
					FeatureAction.DOMESTIC_WIRE_TRANSFER_VIEW,
					FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_VIEW,
					FeatureAction.INTERNATIONAL_WIRE_TRANSFER_VIEW
					);
			
			String featureActionId = CustomerSession.getPermittedActionIds(request, requiredActionIds);
			
			if(featureActionId == null) {
	     		return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
			}
	
			@SuppressWarnings("unchecked")
			Map<String, Object> filterParamsMap = (HashMap<String, Object>) inputArray[1];
			filterParamsMap.put(Constants._FEATURE_ACTION_LIST, featureActionId);
			
			JSONObject requestObj = new JSONObject(filterParamsMap);
			FilterDTO params = JSONUtils.parse(requestObj.toString(), FilterDTO.class);
			
			List<GeneralTransactionDTO> transactions = generalTransactionBusinessDelegate.fetchGeneralTransactions(userId, "", "", params);
			
			if(transactions != null) {
				String listResponse = JSONUtils.stringifyCollectionWithTypeInfo(transactions, GeneralTransactionDTO.class);
				JSONArray resArray = new JSONArray(listResponse);
				JSONObject resultObj = new JSONObject();
				resultObj.put(Constants.RECORDS, resArray);
				result = JSONToResult.convert(resultObj.toString());
			}
			else
				return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}
		catch(Exception exp) {
			alert.prepareError("Error occurred while defining resources for fetch all general transactions", exp).log();
			return null;
		}
		return result;
	}
	
	@Override
	public Result fetchGeneralTransactionById(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		
		Result result = null;
		GeneralTransactionsBusinessDelegate generalTransactionBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(GeneralTransactionsBusinessDelegate.class);
		
		try {
			
			@SuppressWarnings("unchecked")
			Map<String, Object> filterParamsMap = (HashMap<String, Object>) inputArray[1];
			String transactionId = filterParamsMap.get("transactionId").toString();
			String featureActionId = filterParamsMap.get("featureActionId").toString();
			
			if(transactionId == null || "".equals(transactionId)) {
				alert.prepareError("transactionId is missing").log();
				return ErrorCodeEnum.ERR_12026.setErrorCode(new Result());
			}
			
			if(featureActionId == null || "".equals(featureActionId)) {
				alert.prepareError("featureActionId is missing").log();
				return ErrorCodeEnum.ERR_12040.setErrorCode(new Result());
			}
			
			Map<String, Object> customer = CustomerSession.getCustomerMap(request);
			String userId = CustomerSession.getCustomerId(customer);
			
			List<String> requiredActionIds = Arrays.asList(
					FeatureAction.BILL_PAY_VIEW_PAYMENTS,
					FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_VIEW,
					FeatureAction.INTRA_BANK_FUND_TRANSFER_VIEW,
					FeatureAction.TRANSFER_BETWEEN_OWN_ACCOUNT_VIEW,
					FeatureAction.P2P_VIEW,
					FeatureAction.DOMESTIC_WIRE_TRANSFER_VIEW,
					FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_VIEW,
					FeatureAction.INTERNATIONAL_WIRE_TRANSFER_VIEW
					);
			
			String permittedActionId = CustomerSession.getPermittedActionIds(request, requiredActionIds);
			
			if(permittedActionId == null) {
	     		return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
			}
	
			filterParamsMap.put(Constants._FEATURE_ACTION_LIST, permittedActionId);
			
			JSONObject requestObj = new JSONObject(filterParamsMap);
			FilterDTO params = JSONUtils.parse(requestObj.toString(), FilterDTO.class);
			
			//To Fetch the transaction entry based on given transactionId/confirmationNumber
			List<GeneralTransactionDTO> transactionDTO = generalTransactionBusinessDelegate.fetchTransactionById(transactionId, featureActionId, request);
			if(transactionDTO == null || transactionDTO.isEmpty()) {
				alert.prepareError("Record Doesn't Exist").log();
	            return ErrorCodeEnum.ERR_12606.setErrorCode(new Result());
			}
			
			transactionId = transactionDTO.get(0).getTransactionId();
			
			//To fetch transaction details from proc
			List<GeneralTransactionDTO> transactions = generalTransactionBusinessDelegate.fetchGeneralTransactions(userId, transactionId, featureActionId, params);
			
			if(transactions != null) {
				String listResponse = JSONUtils.stringifyCollectionWithTypeInfo(transactions, GeneralTransactionDTO.class);
				JSONArray resArray = new JSONArray(listResponse);
				JSONObject resultObj = new JSONObject();
				resultObj.put(Constants.RECORDS, resArray);
				result = JSONToResult.convert(resultObj.toString());
			}
			else
				return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}
		catch(Exception exp) {
			alert.prepareError("Error occurred while fetching general transaction by id", exp).log();
			return null;
		}
		return result;
	}
	
	public Result getPurposeCodesById(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Result result = null;
		JSONArray combinedList = new JSONArray();
		GeneralTransactionsBusinessDelegate generalTransactionBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(GeneralTransactionsBusinessDelegate.class);
		try {
		result = generalTransactionBusinessDelegate.getPurposeCodesById(methodID, inputArray, request, response);
		String res = ResultToJSON.convert(result);
		
		JSONObject jsonObject = new JSONObject(res);

        JSONArray bodyArray = jsonObject.getJSONArray("PurposeCodes");

        for (int i = 0; i < bodyArray.length(); i++) {
            JSONObject namesObject = bodyArray.getJSONObject(i).getJSONArray("names").getJSONObject(0);
            JSONArray categoryPurposeCodeArray = namesObject.getJSONArray("categoryPurposeCode");

            StringBuilder stringBuilder = new StringBuilder();
            for (int j = 0; j < categoryPurposeCodeArray.length(); j++) {
            	if(j==categoryPurposeCodeArray.length()-1)
                stringBuilder.append(categoryPurposeCodeArray.getString(j) );
            	else
            		 stringBuilder.append(categoryPurposeCodeArray.getString(j) + " ");
            }
            combinedList.put(stringBuilder.toString());
        }
		}
		catch (Exception e) {
			alert.prepareError("Error occurred while fetching purpose codes: " + e).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}
		JSONObject jo = new JSONObject();
		jo.put("PurposeCodes", combinedList);
		return JSONToResult.convert(jo.toString());
	}
	
	@SuppressWarnings("deprecation")
	public Result getExternalCodes(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		GeneralTransactionsBusinessDelegate generalTransactionBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(GeneralTransactionsBusinessDelegate.class);
		Result transactionResult = new Result();
		JSONArray combinedList = new JSONArray();
		Result result = new Result();
		try {
			transactionResult = generalTransactionBusinessDelegate.getExternalCodes(methodID, inputArray, request,
					response);
 
			String res = ResultToJSON.convert(transactionResult);
			JSONObject jsonObject = new JSONObject(res);
			JSONArray bodyArray = jsonObject.getJSONArray("ExternalCodes");

			for (int i = 0; i < bodyArray.length(); i++) {
				JSONObject namesObject = bodyArray.getJSONObject(i).getJSONArray("names").getJSONObject(0);
				JSONArray categoryPurposeCodeArray = namesObject.getJSONArray("categoryPurposeCode");

				StringBuilder stringBuilder = new StringBuilder();
				for (int j = 0; j < categoryPurposeCodeArray.length(); j++) {
					if (j == categoryPurposeCodeArray.length() - 1)
						stringBuilder.append(categoryPurposeCodeArray.getString(j));
					else
						stringBuilder.append(categoryPurposeCodeArray.getString(j) + " ");
				}
				combinedList.put(stringBuilder.toString());
			}
			result.addStringParam("ExternalCodeDetails", combinedList.toString());
			return result;
		} catch (Exception e) {
			alert.prepareError("Exception occurred while invoking backend service : " + " " + e).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}
	}
}