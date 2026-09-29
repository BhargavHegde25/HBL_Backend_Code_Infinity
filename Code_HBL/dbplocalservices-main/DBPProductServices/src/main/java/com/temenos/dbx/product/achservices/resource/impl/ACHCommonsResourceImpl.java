package com.temenos.dbx.product.achservices.resource.impl;

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
import com.temenos.dbx.product.achservices.businessdelegate.api.ACHCommonsBusinessDelegate;
import com.temenos.dbx.product.achservices.dto.ACHTaxSubTypeDTO;
import com.temenos.dbx.product.achservices.dto.ACHTaxTypeDTO;
import com.temenos.dbx.product.achservices.dto.ACHTransactionTypesDTO;
import com.temenos.dbx.product.achservices.dto.BBTemplateRequestTypeDTO;
import com.temenos.dbx.product.achservices.resource.api.ACHCommonsResource;
import com.temenos.dbx.product.commons.businessdelegate.api.AuthorizationChecksBusinessDelegate;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.dbx.product.constants.ACHConstants;
import com.temenos.dbx.product.constants.FeatureAction;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;

public class ACHCommonsResourceImpl implements ACHCommonsResource {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Result fetchTemplateRequestType(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = null;
		ACHCommonsBusinessDelegate achServicesBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(ACHCommonsBusinessDelegate.class);

		@SuppressWarnings("unchecked")
		HashMap<String, Object> requestParams = (HashMap<String, Object>) inputArray[1];

		String transactionType_id = requestParams.get(ACHConstants.Transaction_Type_ID).toString();
		List<BBTemplateRequestTypeDTO> bbtemplateRequestTypeDTO = achServicesBusinessDelegate
				.fetchTemplateRequestTypes(transactionType_id);
		if (bbtemplateRequestTypeDTO == null) {
			alert.prepareError("Exception occured as BBTemplateRequestTypeDTO is NULL").log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}
		JSONArray requestTypeJSONArr = new JSONArray(bbtemplateRequestTypeDTO);
		JSONObject requestTypeList = new JSONObject();
		requestTypeList.put(ACHConstants.TEMPLATE_REQUEST_TYPE, requestTypeJSONArr);
		try {
			result = JSONToResult.convert(requestTypeList.toString());
		} catch (Exception e) {
			alert.prepareError("Exception occured while converting bbtemplateRequestTypeDTO to RESULT object", e).log();
			ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}

		return result;
	}
	
	@Override
	public Result fetchBBTransactionTypes(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) {
		
			Result result = new Result();
			
			ACHCommonsBusinessDelegate bbTransactionTypesBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(ACHCommonsBusinessDelegate.class);
			
			AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
	                .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);
			
			try {
				
		        List<ACHTransactionTypesDTO> bbTransactionTypesDTO = bbTransactionTypesBusinessDelegate.fetchBBTransactionTypes();	
		        
		        if ( bbTransactionTypesDTO == null ) {
		        	return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		        }
		        
		        Map<String, Object> customer = CustomerSession.getCustomerMap(request);
				String createdby = CustomerSession.getCustomerId(customer);

		        JSONArray transactionTypes = new JSONArray(bbTransactionTypesDTO);
		        List<String> requiredActionIds = null;
		        String features = null;
		        JSONArray authorizedTransactiontypes = new JSONArray();
		        for(int i=0; i<transactionTypes.length(); i++) {
		        	JSONObject transactionTypeObj= transactionTypes.getJSONObject(i);
		        	if("Payment".equals(transactionTypeObj.getString("transactionTypeName"))) {
		        		requiredActionIds = Arrays.asList(FeatureAction.ACH_PAYMENT_CREATE, FeatureAction.ACH_PAYMENT_CREATE_TEMPLATE);
		        		features = CustomerSession.getPermittedActionIds(request, requiredActionIds);
		        		if(features != null && !features.isEmpty()) {
		        			authorizedTransactiontypes.put(transactionTypeObj);
		        		}
		        	}
		        	else if("Collection".equals(transactionTypeObj.getString("transactionTypeName"))) {
		        		 requiredActionIds = Arrays.asList(FeatureAction.ACH_COLLECTION_CREATE, FeatureAction.ACH_COLLECTION_CREATE_TEMPLATE);
		        		 features = CustomerSession.getPermittedActionIds(request, requiredActionIds);
		        		 if(features != null && !features.isEmpty()) {
		        			authorizedTransactiontypes.put(transactionTypeObj);
		        		 }
		        	}
		        }
		        
		        JSONObject transactionTypesObj = new JSONObject();
		        transactionTypesObj.put(ACHConstants.TRANSACTION_TYPES, authorizedTransactiontypes);
		        result = JSONToResult.convert(transactionTypesObj.toString());
		        
	        } catch (Exception e) {
	            alert.prepareError("Exception occured while fetching transactionTypes", e).log();
	            ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
	        }
	
	        return result;
	}
	
	@Override
	public Result fetchACHTaxSubType(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		
		Result result = new Result();
		
		ACHCommonsBusinessDelegate achCommonsBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(ACHCommonsBusinessDelegate.class);
		
		@SuppressWarnings("unchecked")
		HashMap<String, Object> requestParams = (HashMap<String, Object>) inputArray[1];
		
		String taxType = requestParams.get("taxType").toString();
		try {
			
			List<ACHTaxSubTypeDTO> bbTaxSubTypes  = achCommonsBusinessDelegate.fetchACHTaxSubType(taxType);
			
			if(bbTaxSubTypes == null) {
				return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
			}
			JSONArray taxSubType = new JSONArray(bbTaxSubTypes);
			JSONObject taxSubTypeObj = new JSONObject();
			taxSubTypeObj.put(ACHConstants.TAX_SUB_TYPE, taxSubType);
			result = JSONToResult.convert(taxSubTypeObj.toString());
			
		}
		catch(Exception e) {
			alert.prepareError("Exception while fetching tax sub types",e).log();
			ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}
		
		return result;
	}

	@Override
	public Result fetchACHTaxType(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		
		Result result = new Result();
		
		ACHCommonsBusinessDelegate achTaxTypeBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(ACHCommonsBusinessDelegate.class);
		try {
				List<ACHTaxTypeDTO> taxTypeList = achTaxTypeBusinessDelegate.fetchTaxType();
				if( taxTypeList == null) {
					return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
				}
				JSONArray taxType = new JSONArray(taxTypeList);
		        JSONObject taxTypeObj = new JSONObject();
		        
		        taxTypeObj.put(ACHConstants.TAX_TYPE, taxType);
		        result = JSONToResult.convert(taxTypeObj.toString());
		        
		} catch (Exception e) {
	        alert.prepareError("Exception occured while fetching taxTypes", e).log();
	        ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}

  
		return result;
	}

}
