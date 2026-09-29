package com.temenos.dbx.product.transactionservices.resource.impl;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.fabric.extn.DBPServiceInvocationWrapper;
import com.dbp.core.util.JSONUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.commons.businessdelegate.api.AuthorizationChecksBusinessDelegate;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.constants.FeatureAction;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.BulkWireFileBusinessDelegate;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.BulkWireTransactionsBusinessDelegate;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.BulkWiresBusinessDelegate;
import com.temenos.dbx.product.transactionservices.dto.AccountsDTO;
import com.temenos.dbx.product.transactionservices.dto.BulkWireDTO;
import com.temenos.dbx.product.transactionservices.dto.BulkWireFileDTO;
import com.temenos.dbx.product.transactionservices.dto.BulkWireFileTransactionsDetailDTO;
import com.temenos.dbx.product.transactionservices.dto.BulkWireTemplateTransactionsDetailDTO;
import com.temenos.dbx.product.transactionservices.dto.WireTransactionDTO;
import com.temenos.dbx.product.transactionservices.resource.api.BulkWireTransactionsResource;

public class BulkWireTransactionsResourceImpl implements BulkWireTransactionsResource {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	 static Map<String, AccountsDTO> accounts = new HashMap<String,AccountsDTO>();
	 private static String orgId;
	 private static String userId;

	@Override
	public Result createBulkWireTransfer(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> inputDetails = (HashMap<String, Object>)inputArray[1];
		Map<String, Object> BWTransactonDetails = new HashMap<String, Object>();
		
		Map<String,Object> customer = CustomerSession.getCustomerMap(request);
		String createdBy = CustomerSession.getCustomerId(customer);
		userId = CustomerSession.getCustomerId(customer);
		Result result = new Result();

		AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);

		JsonObject convertedObject = new Gson().fromJson(inputDetails.get("bulkWireTransferString").toString(), JsonObject.class);
		
		String bulkWireType = ""; 
		if(convertedObject.get("bulkWireType") == null) {
			bulkWireType = Constants.FILE;
		}
		else {
			bulkWireType = convertedObject.get("bulkWireType").toString();
			bulkWireType = bulkWireType.substring(1, bulkWireType.length()-1);
		}	
		
		String featureAction1 = FeatureAction.DOMESTIC_WIRE_TRANSFER_CREATE;
		String featureAction2 = FeatureAction.INTERNATIONAL_WIRE_TRANSFER_CREATE;
		String featureAction3 = "";
		String featureAction4 = "";
		
        
		if(bulkWireType.equalsIgnoreCase(Constants.FILE)) {
			featureAction3 = FeatureAction.DOMESTIC_WIRE_TRANSFER_VIEW_BULK_FILES;
			featureAction4 = FeatureAction.INTERNATIONAL_WIRE_TRANSFER_VIEW_BULK_FILES;
		}else {
			featureAction3 = FeatureAction.DOMESTIC_WIRE_TRANSFER_VIEW_BULK_TEMPLATES;
			featureAction4 = FeatureAction.INTERNATIONAL_WIRE_TRANSFER_VIEW_BULK_TEMPLATES;
		}
		Boolean isDomesticViewPermitted =false;
		Boolean isInternationalViewPermitted =false;
		
		String featureActionlistDomestic = CustomerSession.getPermittedActionIds(request, Arrays.asList(featureAction3));		
		if (!StringUtils.isEmpty(featureActionlistDomestic)) {
			isDomesticViewPermitted = true;	
		}
		
		String featureActionlistInternational = CustomerSession.getPermittedActionIds(request, Arrays.asList(featureAction4));		
		if (!StringUtils.isEmpty(featureActionlistInternational)) {
			isInternationalViewPermitted = true;	
		}		   
	    
		if(!isDomesticViewPermitted && !isInternationalViewPermitted) {
			alert.prepareError("feature List is missing").log();
			return ErrorCodeEnum.ERR_12001.setErrorCode(result);
		}
	
		JsonArray resultarray = convertedObject.getAsJsonArray("BWrecords");
		Set<String> accountNum = new HashSet<String>();
		BulkWireFileBusinessDelegate BWFileBusinessDeligate = DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(BulkWireFileBusinessDelegate.class);
		List<AccountsDTO> accountsList = BWFileBusinessDeligate.getAccounts();
		setAccountsMap(accountsList);	
		for(int i=0;i<resultarray.size();i++) {
		JsonObject jsonObj1 = (JsonObject) resultarray.get(i); 
		String accnum = jsonObj1.get("fromAccountNumber").toString();
		accnum = accnum.substring(1, accnum.length()-1);
		if(!isValidAccountNumber(createdBy,accnum)) {
			return ErrorCodeEnum.ERR_14015.setErrorCode(result);
		}
		accountNum.add(accnum);
		}
		String accountNumbers = String.join(",", accountNum); 
		Boolean isDomesticCreatePermitted = authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction(createdBy, featureAction1, accountNumbers, CustomerSession.IsCombinedUser(customer));
		Boolean isInternationalCreatePermitted = authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction(createdBy, featureAction2, accountNumbers, CustomerSession.IsCombinedUser(customer));  
	    
		Boolean domesticPermissions = isDomesticCreatePermitted && isDomesticViewPermitted;
		Boolean internationalPermissions  = isInternationalCreatePermitted && isInternationalViewPermitted;
		
		if(!domesticPermissions && !internationalPermissions) {
			return ErrorCodeEnum.ERR_12001.setErrorCode(result);
		}
		
		Boolean isDomesticTransferAvailable = false;
		Boolean isIntTransferAvailable = false;

		if(convertedObject.get("totalCountOfDomesticTransactions").getAsInt() != 0) {
			isDomesticTransferAvailable = true;
		}
		if(convertedObject.get("totalCountOfInternationalTransactions").getAsInt() != 0 ) {
			isIntTransferAvailable = true;
		}
		
		if(!isDomesticTransferAvailable && isIntTransferAvailable) {
			if(!internationalPermissions) {
				return ErrorCodeEnum.ERR_12001.setErrorCode(result);
			}
		}else if(!isIntTransferAvailable && isDomesticTransferAvailable) {
			if(!domesticPermissions) {
				return ErrorCodeEnum.ERR_12001.setErrorCode(result);
			}
		}
		
		List<WireTransactionDTO> wireTransactionDTOs = null;
		BulkWireFileTransactionsDetailDTO bulkwirefileTransactionsDetailDTO = null;
		BulkWireTemplateTransactionsDetailDTO bulkwireTemplateTransactionsDetailDTO = null;
		
		
		BulkWireTransactionsBusinessDelegate bulkWireTransactionsDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(BulkWireTransactionsBusinessDelegate.class);

		BulkWiresBusinessDelegate wireTransactionDelegate = 
				DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(BulkWiresBusinessDelegate.class);
		
		
		String bulkWireId = "";
		String bulkWireName = "";
		BWTransactonDetails.put("totalCountOfTransactions", convertedObject.get("totalCountOfTransactions"));
		BWTransactonDetails.put("totalCountOfDomesticTransactions", convertedObject.get("totalCountOfDomesticTransactions"));
		BWTransactonDetails.put("totalCountOfInternationalTransactions", convertedObject.get("totalCountOfInternationalTransactions")); 
		BWTransactonDetails.put("createdBy", userId);
		BWTransactonDetails.put("modifiedby", userId);
		BWTransactonDetails.put("initiatedBy", userId);
		
		if(convertedObject.get("bulkWireName") != null) {
			bulkWireName = convertedObject.get("bulkWireName").toString();
			bulkWireName = bulkWireName.substring(1, bulkWireName.length()-1);
		} 	
		try{
		
		if(bulkWireType.equalsIgnoreCase(Constants.TEMPLATE)) {
			bulkWireId = convertedObject.get("bulkWireTemplateID").toString();
			bulkWireId = bulkWireId.substring(1, bulkWireId.length()-1);
				BWTransactonDetails.put("bulkWireTemplateID", bulkWireId);
				try{
					bulkwireTemplateTransactionsDetailDTO = bulkWireTransactionsDelegate.createBulkWireTemplateTransactionDetails(BWTransactonDetails);
				}catch (Exception e1) {
					return ErrorCodeEnum.ERR_14008.setErrorCode(result);
				}
				
			}
			else if(bulkWireType.equalsIgnoreCase(Constants.FILE)) {
				bulkWireId = convertedObject.get("bulkWireFileID").toString();
				bulkWireId = bulkWireId.substring(1, bulkWireId.length()-1);
				BWTransactonDetails.put("bulkWireFileID", bulkWireId);
				try{
					bulkwirefileTransactionsDetailDTO = bulkWireTransactionsDelegate.createBulkWireTransactionDetails(BWTransactonDetails);	
			    }catch (Exception e1) {
				return ErrorCodeEnum.ERR_14008.setErrorCode(result);
			}
			}
			else {
				alert.prepareError("The value for bulkWireType in the request is not appropriate").log();
				return ErrorCodeEnum.ERR_14011.setErrorCode(result);
			}
		}
		catch (Exception e){
			alert.prepareError("InAppropriate request payload"+e).log();
			return ErrorCodeEnum.ERR_14012.setErrorCode(result);
		}
		
		
		List<Map<String, Object>> inputs = getFormattedInput(resultarray);
		Map<String, Object> domesticparsedRequest = new HashMap<String, Object>();
		Map<String, Object> internationalparsedRequest = new HashMap<String, Object>();
        int  transactionID;
        if(bulkWireType.equalsIgnoreCase(Constants.FILE)) {
        	transactionID = bulkwirefileTransactionsDetailDTO.getBulkWireTransactionID();
        }else {
        	transactionID = bulkwireTemplateTransactionsDetailDTO.getBulkWireTransactionID();
        }
		if(inputs.size() >= 1){
			_parseDomesticRequestInput(inputs, domesticparsedRequest,transactionID ,bulkWireId,bulkWireName,bulkWireType);
			_parseInternationalRequestInput(inputs, internationalparsedRequest, transactionID,bulkWireId,bulkWireName,bulkWireType);
		}
		else{
			domesticparsedRequest = inputs.get(0);
			internationalparsedRequest = inputs.get(0);
		}

		try {
			List<WireTransactionDTO> bulkWireInternationalTransferResult = _createBulkWireInternationalTransfer(internationalparsedRequest, request);
			List<WireTransactionDTO> bulkWireDomesticTransferResult = _createBulkWireDomesticTransfer(domesticparsedRequest, request);
			String bulkWireExecutionID = "";
			if(bulkWireType.equalsIgnoreCase(Constants.FILE)) {
				bulkWireExecutionID = Integer.toString(bulkwirefileTransactionsDetailDTO.getBulkWireTransactionID());
				wireTransactionDTOs = (List<WireTransactionDTO>)wireTransactionDelegate.fetchTransactionsByWireExecutionId(bulkWireExecutionID,"", "","","",1,1);
			}
			else {
				bulkWireExecutionID = Integer.toString(bulkwireTemplateTransactionsDetailDTO.getBulkWireTransactionID());
				wireTransactionDTOs = (List<WireTransactionDTO>)wireTransactionDelegate.fetchTransactionsByWireTemplateExecutionId(bulkWireExecutionID,"", "","","",1,1);
			}
			if(wireTransactionDTOs == null) {
					return ErrorCodeEnum.ERR_14008.setErrorCode(result);
				}
				JSONArray rulesJSONArr = new JSONArray(wireTransactionDTOs);
				JSONArray lineItemsDomesticJSONArr = new JSONArray();
				JSONArray lineItemsInternationalJSONArr = new JSONArray();
				int jsonObj;
				for(jsonObj = 0;jsonObj< rulesJSONArr.length();jsonObj++)
				{
					if(rulesJSONArr.getJSONObject(jsonObj).getString(Constants.WIREACCOUNTTYPE).equalsIgnoreCase(Constants.DOMESTIC_RECORDS))
					{
						lineItemsDomesticJSONArr.put(rulesJSONArr.getJSONObject(jsonObj));
					}
					else{
						lineItemsInternationalJSONArr.put(rulesJSONArr.getJSONObject(jsonObj));
						
					}
				}	
			
				JSONObject responseObj = new JSONObject();
				responseObj.put(Constants.DOMESTIC_RECORDS, lineItemsDomesticJSONArr);
				responseObj.put(Constants.INTERNATIONAL_RECORDS, lineItemsInternationalJSONArr);
				
					Integer totalCount = rulesJSONArr.length();
					Integer successCount = 0;
					Integer failedCount = 0;
					Integer pendingCount = 0;
				    
					for(jsonObj = 0;jsonObj< rulesJSONArr.length();jsonObj++)
					{
						if(rulesJSONArr.getJSONObject(jsonObj).getString(Constants.STATUS).equalsIgnoreCase(Constants.SUCCESS))
						{
							successCount++;
							continue;
						}
						else if(rulesJSONArr.getJSONObject(jsonObj).getString(Constants.STATUS).equalsIgnoreCase(Constants.FAILED)||rulesJSONArr.getJSONObject(jsonObj).getString(Constants.STATUS).equalsIgnoreCase(Constants.DENIED))
						{	
							failedCount++;
							continue;
						}
						else if(rulesJSONArr.getJSONObject(jsonObj).getString(Constants.STATUS).equalsIgnoreCase(Constants.PENDING))
						{	
							pendingCount++;
							continue;
						}
					}
					responseObj.put(Constants.TOTALCOUNT, totalCount);
					responseObj.put(Constants.SUCCESSCOUNT , successCount);
					responseObj.put(Constants.FAILEDCOUNT, failedCount);
					responseObj.put(Constants.PENDINGCOUNT, pendingCount);
				JSONObject finalRespObj = new JSONObject();
				finalRespObj.put(Constants.BULKWIREEXECUTIONDETAILS, responseObj);
				result = JSONToResult.convert(finalRespObj.toString());
				
				BulkWireFileDTO bulkwirefileDTO = null;
				BulkWireDTO bulkwiretemplateDTO = null;
				Timestamp timestamp = new Timestamp(System.currentTimeMillis());
				BulkWiresBusinessDelegate bulkWiresDelegate = DBPAPIAbstractFactoryImpl.getInstance()
					    .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(BulkWiresBusinessDelegate.class);
					
				if(bulkWireType.equalsIgnoreCase(Constants.TEMPLATE))
				{
					Map<String, Object> BWTemplateDetails = new HashMap<String, Object>();
					BWTemplateDetails.put("bulkWireTemplateID", bulkWireId);
					BWTemplateDetails.put("lastExecutedOn", timestamp);
					try {           
						bulkwiretemplateDTO = bulkWiresDelegate.updateBulkWireTemplate(BWTemplateDetails);
					}catch (Exception e) {
						alert.prepareError("Caught exception at createBulkBillPay method: " , e).log();
					}
				}
				else {
					Map<String, Object> BWFileDetails = new HashMap<String, Object>();
					BWFileDetails.put("bulkWireFileID", bulkWireId);
					BWFileDetails.put("lastExecutedOn", timestamp);
					try {           
						bulkwirefileDTO = bulkWireTransactionsDelegate.updateBulkWireFiles(BWFileDetails);
					}catch (Exception e) {
						alert.prepareError("Caught exception at createBulkBillPay method: " , e).log();
					}
				}	
			/* bulkWireInternationalTransferResult.addAll(bulkWireDomesticTransferResult);
			JSONArray bulkwiretransferJSONArr = new JSONArray(bulkWireInternationalTransferResult);    
			JSONObject responseObj = new JSONObject();
			responseObj.put("transaction", bulkwiretransferJSONArr);
			result = JSONToResult.convert(responseObj.toString());*/ 
		}catch (Exception e) {
			alert.prepareError("Caught exception at createBulkWireTransfer method: " , e).log();
		}
		return result;
	}


	private void _parseDomesticRequestInput(List<Map<String, Object>> records, Map<String, Object> domesticparsedRequest, int bulkWireTransactionID,String bulkWireId, String bulkWireName,String bulkWireType) {
		StringBuffer amount = new StringBuffer();
		StringBuffer payeeAccountNumber = new StringBuffer();
		StringBuffer fromAccountNumber = new StringBuffer();
		StringBuffer payeeAddressLine1 = new StringBuffer();
		StringBuffer payeeName = new StringBuffer();
		StringBuffer payeeNickName = new StringBuffer();
		StringBuffer payeeCurrency = new StringBuffer();
		StringBuffer transactionCurrency = new StringBuffer();
		StringBuffer scheduledDate = new StringBuffer();
		StringBuffer transactionsNotes = new StringBuffer();
		StringBuffer swiftCode = new StringBuffer();
		StringBuffer routingNumber = new StringBuffer();
		StringBuffer wireAccountType = new StringBuffer();
		StringBuffer country = new StringBuffer();
		StringBuffer bankName = new StringBuffer();
		StringBuffer internationalRoutingCode = new StringBuffer();
		StringBuffer zipCode = new StringBuffer();
		StringBuffer cityName = new StringBuffer();
		StringBuffer payeeType = new StringBuffer();
		StringBuffer transactionType = new StringBuffer();
		StringBuffer state = new StringBuffer();
		StringBuffer bankAddressLine1 = new StringBuffer();
		StringBuffer bankAddressLine2 = new StringBuffer();
		StringBuffer bankCity = new StringBuffer();
		StringBuffer bankState = new StringBuffer();
		StringBuffer bankZip = new StringBuffer();
		StringBuffer payeeAddressLine2 = new StringBuffer();
		StringBuffer wireFileExecution_id = new StringBuffer();	
		StringBuffer initiationId = new StringBuffer();
		StringBuffer wireTemplateExecution_id = new StringBuffer();	
		StringBuffer wireId = new StringBuffer();	
		StringBuffer wireName = new StringBuffer();	
		
		
		int id =0;

		for (Map<String, Object> input : records) {
			if(input.get("wireAccountType").equals("Domestic")) {
				amount = amount.append(input.get("amount")+ ",");
				if(bulkWireType.equals(Constants.TEMPLATE))
				wireTemplateExecution_id.append(bulkWireTransactionID).append(",");
				else 
				wireFileExecution_id.append(bulkWireTransactionID).append(",");
				if(!bulkWireId.isEmpty() && !bulkWireName.isEmpty()) {
					wireId.append(bulkWireId).append(",");
					wireName.append(bulkWireName).append(",");
				}
				fromAccountNumber = fromAccountNumber.append(input.get("fromAccountNumber")+ ",");
				scheduledDate = scheduledDate.append(input.get("scheduledDate")+ ",");
				transactionsNotes = transactionsNotes.append(input.get("transactionsNotes")+ ",");
				transactionType = transactionType.append(input.get("transactionType")+ ",");   
				payeeAccountNumber = payeeAccountNumber.append(input.get("payeeAccountNumber")+ ",");
				payeeAddressLine1 = payeeAddressLine1.append(input.get("payeeAddressLine1")+ ",");
				payeeName = payeeName.append(input.get("payeeName")+ ",");
				payeeNickName = payeeNickName.append(input.get("payeeNickName")+ ",");			
				payeeCurrency = payeeCurrency.append(input.get("payeeCurrency")+ ",");
				transactionCurrency = transactionCurrency.append(input.get("transactionCurrency")+ ",");
				swiftCode = swiftCode.append(input.get("swiftCode")+ ",");
				routingNumber = routingNumber.append(input.get("routingNumber")+ ",");
				wireAccountType = wireAccountType.append(input.get("wireAccountType")+ ",");
				country = country.append(input.get("country")+ ",");
				bankName = bankName.append(input.get("bankName")+ ",");   
				internationalRoutingCode = internationalRoutingCode.append(input.get("internationalRoutingCode")+ ",");
				zipCode = zipCode.append(input.get("zipCode")+ ",");
				cityName = cityName.append(input.get("cityName")+ ",");
				state = state.append(input.get("state")+ ",");			
				bankAddressLine1 = bankAddressLine1.append(input.get("bankAddressLine1")+ ",");
				bankAddressLine2 = bankAddressLine2.append(input.get("bankAddressLine2")+ ",");   
				bankCity = bankCity.append(input.get("bankCity")+ ",");
				bankState = bankState.append(input.get("bankState")+ ",");
				bankZip = bankZip.append(input.get("bankZip")+ ",");
				payeeAddressLine2 = payeeAddressLine2.append(input.get("payeeAddressLine2")+ ",");
				payeeType = payeeType.append(input.get("payeeType")+ ",");
				initiationId = initiationId.append(id+ ",");
				id++;
			}
		}
		if(bulkWireType.equals(Constants.TEMPLATE))
			domesticparsedRequest.put("wireTemplateExecution_id",wireTemplateExecution_id.toString());
		else 
			domesticparsedRequest.put("wireFileExecution_id",wireFileExecution_id.toString());
		if(!bulkWireId.isEmpty() && !bulkWireName.isEmpty()) {
			domesticparsedRequest.put("bulkWireId",wireId.toString());
			domesticparsedRequest.put("bulkWireName",wireName.toString());
		}
		domesticparsedRequest.put("amount",amount.toString());
		domesticparsedRequest.put("fromAccountNumber",fromAccountNumber.toString());
		domesticparsedRequest.put("scheduledDate",scheduledDate.toString());
		domesticparsedRequest.put("transactionsNotes",transactionsNotes.toString());
		domesticparsedRequest.put("transactionType",transactionType.toString());
		domesticparsedRequest.put("payeeAccountNumber",payeeAccountNumber.toString());
		domesticparsedRequest.put("payeeAddressLine1",payeeAddressLine1.toString());
		domesticparsedRequest.put("payeeName",payeeName.toString());
		domesticparsedRequest.put("payeeNickName",payeeNickName.toString());
		domesticparsedRequest.put("payeeCurrency",payeeCurrency.toString());
		domesticparsedRequest.put("transactionCurrency",transactionCurrency.toString());
		domesticparsedRequest.put("swiftCode",swiftCode.toString());
		domesticparsedRequest.put("routingNumber",routingNumber.toString());
		domesticparsedRequest.put("wireAccountType",wireAccountType.toString());
		domesticparsedRequest.put("country",country.toString());
		domesticparsedRequest.put("bankName",bankName.toString());
		domesticparsedRequest.put("internationalRoutingCode",internationalRoutingCode.toString());
		domesticparsedRequest.put("zipCode",zipCode.toString());
		domesticparsedRequest.put("cityName",cityName.toString());
		domesticparsedRequest.put("state",state.toString());
		domesticparsedRequest.put("bankAddressLine1",bankAddressLine1.toString());
		domesticparsedRequest.put("bankAddressLine2",bankAddressLine2.toString());
		domesticparsedRequest.put("bankCity",bankCity.toString());
		domesticparsedRequest.put("bankState",bankState.toString());
		domesticparsedRequest.put("bankZip",bankZip.toString());
		domesticparsedRequest.put("payeeAddressLine2",payeeAddressLine2.toString());
		domesticparsedRequest.put("payeeType",payeeType.toString());
		domesticparsedRequest.put("initiationId", initiationId.toString());
		domesticparsedRequest.put("loop_count", Integer.toString(id));
	}

	private void _parseInternationalRequestInput(List<Map<String, Object>> records, Map<String, Object> internationalparsedRequest, int bulkWireTransactionID,String bulkWireId, String bulkWireName,String bulkWireType) {
		StringBuffer amount = new StringBuffer();
		StringBuffer payeeAccountNumber = new StringBuffer();
		StringBuffer fromAccountNumber = new StringBuffer();
		StringBuffer payeeAddressLine1 = new StringBuffer();
		StringBuffer payeeName = new StringBuffer();
		StringBuffer payeeNickName = new StringBuffer();
		StringBuffer payeeCurrency = new StringBuffer();
		StringBuffer transactionCurrency = new StringBuffer();
		StringBuffer scheduledDate = new StringBuffer();
		StringBuffer transactionsNotes = new StringBuffer();
		StringBuffer swiftCode = new StringBuffer();
		StringBuffer routingNumber = new StringBuffer();
		StringBuffer wireAccountType = new StringBuffer();
		StringBuffer country = new StringBuffer();
		StringBuffer bankName = new StringBuffer();
		StringBuffer internationalRoutingCode = new StringBuffer();
		StringBuffer zipCode = new StringBuffer();
		StringBuffer cityName = new StringBuffer();
		StringBuffer payeeType = new StringBuffer();
		StringBuffer transactionType = new StringBuffer();
		StringBuffer state = new StringBuffer();
		StringBuffer bankAddressLine1 = new StringBuffer();
		StringBuffer bankAddressLine2 = new StringBuffer();
		StringBuffer bankCity = new StringBuffer();
		StringBuffer bankState = new StringBuffer();
		StringBuffer bankZip = new StringBuffer();
		StringBuffer payeeAddressLine2 = new StringBuffer();
		StringBuffer wireFileExecution_id = new StringBuffer();	
		StringBuffer initiationId = new StringBuffer();
		StringBuffer wireTemplateExecution_id = new StringBuffer();	
		StringBuffer wireId = new StringBuffer();	
		StringBuffer wireName = new StringBuffer();	
		
		int id =0;

		for (Map<String, Object> input : records) {
			if(input.get("wireAccountType").equals("International")) {
				amount = amount.append(input.get("amount")+ ",");
				if(bulkWireType.equals(Constants.TEMPLATE))
					wireTemplateExecution_id.append(bulkWireTransactionID).append(",");
				else 
					wireFileExecution_id.append(bulkWireTransactionID).append(",");
				if(!bulkWireId.isEmpty() && !bulkWireName.isEmpty()) {
						wireId.append(bulkWireId).append(",");
						wireName.append(bulkWireName).append(",");
				}
				fromAccountNumber = fromAccountNumber.append(input.get("fromAccountNumber")+ ",");
				scheduledDate = scheduledDate.append(input.get("scheduledDate")+ ",");
				transactionsNotes = transactionsNotes.append(input.get("transactionsNotes")+ ",");
				transactionType = transactionType.append(input.get("transactionType")+ ",");   
				payeeAccountNumber = payeeAccountNumber.append(input.get("payeeAccountNumber")+ ",");
				payeeAddressLine1 = payeeAddressLine1.append(input.get("payeeAddressLine1")+ ",");
				payeeName = payeeName.append(input.get("payeeName")+ ",");
				payeeNickName = payeeNickName.append(input.get("payeeNickName")+ ",");			
				payeeCurrency = payeeCurrency.append(input.get("payeeCurrency")+ ",");
				transactionCurrency = transactionCurrency.append(input.get("transactionCurrency")+ ",");
				swiftCode = swiftCode.append(input.get("swiftCode")+ ",");
				routingNumber = routingNumber.append(input.get("routingNumber")+ ",");
				wireAccountType = wireAccountType.append(input.get("wireAccountType")+ ",");
				country = country.append(input.get("country")+ ",");
				bankName = bankName.append(input.get("bankName")+ ",");   
				internationalRoutingCode = internationalRoutingCode.append(input.get("internationalRoutingCode")+ ",");
				zipCode = zipCode.append(input.get("zipCode")+ ",");
				cityName = cityName.append(input.get("cityName")+ ",");
				state = state.append(input.get("state")+ ",");			
				bankAddressLine1 = bankAddressLine1.append(input.get("bankAddressLine1")+ ",");
				bankAddressLine2 = bankAddressLine2.append(input.get("bankAddressLine2")+ ",");   
				bankCity = bankCity.append(input.get("bankCity")+ ",");
				bankState = bankState.append(input.get("bankState")+ ",");
				bankZip = bankZip.append(input.get("bankZip")+ ",");
				payeeAddressLine2 = payeeAddressLine2.append(input.get("payeeAddressLine2")+ ",");
				payeeType = payeeType.append(input.get("payeeType")+ ",");
				initiationId = initiationId.append(id+ ",");
				id++;
			}
		}
		
		if(bulkWireType.equals(Constants.TEMPLATE))
			internationalparsedRequest.put("wireTemplateExecution_id",wireTemplateExecution_id.toString());
		else 
			internationalparsedRequest.put("wireFileExecution_id",wireFileExecution_id.toString());
		if(!bulkWireId.isEmpty() && !bulkWireName.isEmpty()) {
			internationalparsedRequest.put("bulkWireId",wireId.toString());
			internationalparsedRequest.put("bulkWireName",wireName.toString());
		}
		internationalparsedRequest.put("amount",amount.toString());
		internationalparsedRequest.put("fromAccountNumber",fromAccountNumber.toString());
		internationalparsedRequest.put("scheduledDate",scheduledDate.toString());
		internationalparsedRequest.put("transactionsNotes",transactionsNotes.toString());
		internationalparsedRequest.put("transactionType",transactionType.toString());
		internationalparsedRequest.put("payeeAccountNumber",payeeAccountNumber.toString());
		internationalparsedRequest.put("payeeAddressLine1",payeeAddressLine1.toString());
		internationalparsedRequest.put("payeeName",payeeName.toString());
		internationalparsedRequest.put("payeeNickName",payeeNickName.toString());
		internationalparsedRequest.put("payeeCurrency",payeeCurrency.toString());
		internationalparsedRequest.put("transactionCurrency",transactionCurrency.toString());
		internationalparsedRequest.put("swiftCode",swiftCode.toString());
		internationalparsedRequest.put("routingNumber",routingNumber.toString());
		internationalparsedRequest.put("wireAccountType",wireAccountType.toString());
		internationalparsedRequest.put("country",country.toString());
		internationalparsedRequest.put("bankName",bankName.toString());
		internationalparsedRequest.put("internationalRoutingCode",internationalRoutingCode.toString());
		internationalparsedRequest.put("zipCode",zipCode.toString());
		internationalparsedRequest.put("cityName",cityName.toString());
		internationalparsedRequest.put("state",state.toString());
		internationalparsedRequest.put("bankAddressLine1",bankAddressLine1.toString());
		internationalparsedRequest.put("bankAddressLine2",bankAddressLine2.toString());
		internationalparsedRequest.put("bankCity",bankCity.toString());
		internationalparsedRequest.put("bankState",bankState.toString());
		internationalparsedRequest.put("bankZip",bankZip.toString());
		internationalparsedRequest.put("payeeAddressLine2",payeeAddressLine2.toString());
		internationalparsedRequest.put("payeeType",payeeType.toString());
		internationalparsedRequest.put("initiationId", initiationId.toString());
		internationalparsedRequest.put("loop_count", Integer.toString(id));
	}

	private List<Map<String, Object>> getFormattedInput(JsonArray jArray) {
		List<Map<String, Object>> inputs = new ArrayList<>();
		Type type = new TypeToken<Map<String, Object>>() {
		}.getType();
		Gson gson = new Gson();
		if (jArray.isJsonArray()) {
			for (int i = 0; i < jArray.size(); i++) {
				@SuppressWarnings("unchecked")
				Map<String, Object> temp = (Map<String, Object>) gson.fromJson(jArray.get(i), type);
				inputs.add(temp);
			}
		}
		return inputs;
	}
    
	//Added this method to pass DCRequest. Will be moved to BusinessDelegate after suitable architecture changes
	private List<WireTransactionDTO> _createBulkWireInternationalTransfer(Map<String, Object> requestParameters, DataControllerRequest dataControllerRequest) {

		List<WireTransactionDTO> wireTransferDTO;

		String serviceName  = ServiceId.BULK_WIRE_TRANSFER_SERVICE;
		String operationName = OperationName.CREATE_BULK_WIRE_INTERNAIONAL_TRANSFER;

		String bulkWireTransferResponse = null;
		try {
			diagnostic.prepareDebug("In Business Delegate Method").log();
			//Checking x-forwarded-for string in DCRequest for remote address 
			Map<String,Object> headers = dataControllerRequest.getHeaderMap();
			String clientIp = dataControllerRequest.getHeader(DBPUtilitiesConstants.CLIENT_IP);
			if (StringUtils.isBlank(clientIp)) {
				try {
					clientIp = HelperMethods.getDecyptedClientIpAddressFromCache(dataControllerRequest);
				} catch (Exception e) {
				}
			} else {
				clientIp = HelperMethods.decryptClientIp(clientIp);
			}
			if (!headers.containsKey("x-forwarded-for")) {
				headers.put("x-forwarded-for", clientIp);
			}

			
//			bulkWireTransferResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
//					.withOperationId(operationName).withRequestParameters(requestParameters).build().getResponse();
			bulkWireTransferResponse = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, null, operationName, requestParameters, headers, dataControllerRequest);
			JSONObject bulkwireTransferJSON = new JSONObject(bulkWireTransferResponse);
			JSONArray records = bulkwireTransferJSON.getJSONArray("LoopDataset");
			wireTransferDTO = JSONUtils.parseAsList(records.toString(), WireTransactionDTO.class);
		}
		catch(JSONException jsonExp) {
			alert.prepareError("JSONExcpetion occured while creating the bulkwiretransfer: ",jsonExp).log();
			return null;
		}
		catch(Exception exp) {
			alert.prepareError("Excpetion occured while creating the bulkwiretransfer: ",exp).log();
			return null;
		}
		return wireTransferDTO;
	}
    
	//Added this method to pass DCRequest. Will be moved to BusinessDelegate after suitable architecture changes
	private List<WireTransactionDTO> _createBulkWireDomesticTransfer(Map<String, Object> requestParameters, DataControllerRequest dataControllerRequest) {

		List<WireTransactionDTO> wireTransferDTO;

		String serviceName  = ServiceId.BULK_WIRE_TRANSFER_SERVICE;
		String operationName = OperationName.CREATE_BULK_WIRE_DOMESTIC_TRANSFER;

		String bulkWireTransferResponse1 = null;
		try {
			diagnostic.prepareDebug("In Business Delegate Method").log();
			//Checking x-forwarded-for string in DCRequest for remote address 
			Map<String,Object> headers = dataControllerRequest.getHeaderMap();
			String clientIp = dataControllerRequest.getHeader(DBPUtilitiesConstants.CLIENT_IP);
			if (StringUtils.isBlank(clientIp)) {
				try {
					clientIp = HelperMethods.getDecyptedClientIpAddressFromCache(dataControllerRequest);
				} catch (Exception e) {
				}
			} else {
				clientIp = HelperMethods.decryptClientIp(clientIp);
			}
			if (!headers.containsKey("x-forwarded-for")) {
				headers.put("x-forwarded-for", clientIp);
			}
//			bulkWireTransferResponse1 = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
//					.withOperationId(operationName).withRequestParameters(requestParameters).build().getResponse();
			bulkWireTransferResponse1 = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, null, operationName, requestParameters, headers, dataControllerRequest);
			JSONObject bulkwireTransferJSON = new JSONObject(bulkWireTransferResponse1);
			JSONArray records = bulkwireTransferJSON.getJSONArray("LoopDataset");
			wireTransferDTO = JSONUtils.parseAsList(records.toString(), WireTransactionDTO.class);
		}
		catch(JSONException jsonExp) {
			alert.prepareError("JSONExcpetion occured while creating the bulkwiretransfer: ",jsonExp).log();
			return null;
		}
		catch(Exception exp) {
			alert.prepareError("Excpetion occured while creating the bulkwiretransfer: ",exp).log();
			return null;
		}
		return wireTransferDTO;
	}
	
	public Result getBulkWireFileTransactionsDetail(String methodID, Object[] inputArray, DataControllerRequest request, 
			DataControllerResponse response) {
		@SuppressWarnings("unchecked")
		Map<String, Object> inputParams =  (HashMap<String, Object>)inputArray[1];
		Result result = new Result();

		Map < String, Object > customer = CustomerSession.getCustomerMap(request);

        String bulkWirefileID = "";
		
        if(inputParams.get(Constants.BULKWIREFILE_ID) !=null && inputParams.get(Constants.BULKWIREFILE_ID) != "" ) {
			bulkWirefileID = inputParams.get(Constants.BULKWIREFILE_ID).toString();
		}else {
			alert.prepareError("BuklWireFileId  is missing in the payload which is mandatory to fetch the file details").log();
			return ErrorCodeEnum.ERR_14001.setErrorCode(result);
		}
        
        String user_id = CustomerSession.getCustomerId(customer);
        String featureAction1 = FeatureAction.DOMESTIC_WIRE_TRANSFER_VIEW_BULK_FILES;
        String featureAction2 = FeatureAction.INTERNATIONAL_WIRE_TRANSFER_VIEW_BULK_FILES;
        
        AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);

        Boolean isDomesticTemplatePermitted = authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction(user_id, featureAction1, null, CustomerSession.IsCombinedUser(customer));
        Boolean isInternationalTemplatePermitted = authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction(user_id, featureAction2, null, CustomerSession.IsCombinedUser(customer));
        
        BulkWireFileBusinessDelegate bulkWireFileDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(BulkWireFileBusinessDelegate.class);
        
        if(!isDomesticTemplatePermitted && !isInternationalTemplatePermitted) {
        	return ErrorCodeEnum.ERR_12001.setErrorCode(result);
        }else {
        	JSONObject delegateResponse = bulkWireFileDelegate.getBWFileDomesticInternationalCount(bulkWirefileID);
        	if(isDomesticTemplatePermitted && !isInternationalTemplatePermitted) {
        		if(Integer.parseInt(delegateResponse.getString(Constants.NUMDOMESTICTRANSACTIONS)) == 0)
        			return ErrorCodeEnum.ERR_12001.setErrorCode(result);	
        	}else if(!isDomesticTemplatePermitted && isInternationalTemplatePermitted) {
        		if(Integer.parseInt(delegateResponse.getString(Constants.NUMINTERNATIONALTRANSACTIONS)) == 0)
        			return ErrorCodeEnum.ERR_12001.setErrorCode(result);
        	}
        }
        
       
      if(!bulkWireFileDelegate.isFileAccessibleByUser(bulkWirefileID, customer))
      {
    	alert.prepareError("User does not have enough permissions to access the details of the file with given id"+bulkWirefileID).log();
		return ErrorCodeEnum.ERR_14002.setErrorCode(result);
      }
		List<BulkWireFileTransactionsDetailDTO> bulkWireFileTransactionDetailsDTO = null;

		BulkWireTransactionsBusinessDelegate bulkWireTransactionDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(BulkWireTransactionsBusinessDelegate.class);
		String sortBy = "";
		String sortOrder = "";
		String searchString = "";
		Object pageOffset = null;
		Object pageSize = null;
		
		if(inputParams.get(Constants.SORTBYPARAM) !=null){
			sortBy = inputParams.get(Constants.SORTBYPARAM).toString();
		}
		if(inputParams.get(Constants.SORTORDER)!=null) {
			sortOrder = inputParams.get(Constants.SORTORDER).toString();
		}
		if(inputParams.get(Constants.SEARCHSTRING)!=null) {
			searchString = inputParams.get(Constants.SEARCHSTRING).toString();
		}
		if(inputParams.get(Constants.PAGEOFFSET)!= null) {
			pageOffset = inputParams.get(Constants.PAGEOFFSET);
		}
		if(inputParams.get(Constants.PAGESIZE)!= null) {
			pageSize = inputParams.get(Constants.PAGESIZE);
		}
		try {
			bulkWireFileTransactionDetailsDTO = bulkWireTransactionDelegate.getBulkWireFileTransactionsDetail(bulkWirefileID,sortBy,sortOrder,pageOffset,pageSize,searchString);	
		}catch(Exception e) {
			alert.prepareError("Error while fetching bulkWireFileTransactionDetailsDTO from BulkWireTransactionsBusinessDelegate : " + e).log();
			return null;	
		}
		try {
			if (bulkWireFileTransactionDetailsDTO == null) {
				return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
			}
			JSONArray respJsonArray = new JSONArray(bulkWireFileTransactionDetailsDTO);
			JSONObject respJson = new JSONObject();
			respJson.put(Constants.BULKWIREFILETRANSACTDETAILS_TABLE, respJsonArray);
			result = JSONToResult.convert(respJson.toString());
		} catch (Exception e) {
			alert.prepareError("Error while converting response bulkWireFilesDTO to result : " + e).log();
			return null;
		}
		return result;
	}
		
	
	public static boolean isValidAccountNumber(String createdBy,String accountNumber) {
		if(accounts.containsKey(accountNumber)) {				
			AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);
				return authorizationChecksBusinessDelegate.isOneOfMyAccounts(createdBy,accountNumber);
		}
		return false;
	}
	
	private void setAccountsMap(List<AccountsDTO> accountsList) {
		try {
			int accountsCount = accountsList.size();
			for(int i = 0; i < accountsCount; i++) {
				accounts.put(accountsList.get(i).getAccountId(),accountsList.get(i));
			}
		}
		catch(Error e) {
			alert.prepareError("Error while getting accounts list").log();
		}
	}
	
	public Result getTransactionsByBulkWireTemplateExecutionId(String methodID, Object[] inputArray, DataControllerRequest request, 
			DataControllerResponse response) {
	
		Result result = new Result();
		List<WireTransactionDTO> wireTransactionDTOs = null;
		
		@SuppressWarnings("unchecked")
		HashMap<String, Object> requestParams = (HashMap<String, Object>)inputArray[1];
		
		// Initialize the required BusinessDelegate class to perform the operation
		BulkWiresBusinessDelegate wireTransactionDelegate = 
					DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
					.getBusinessDelegate(BulkWiresBusinessDelegate.class);
			
			Object wireTemplateExecutionIdVal = requestParams.get(Constants.BULKWIRETEMPLATEEXECUTION_ID);
			String wireTemplateExecutionId = "";
			String sortByParam = "";
			String sortOrder = "";
			String searchString = "";
			String statusFilter = "";
			if( wireTemplateExecutionIdVal !=null && !wireTemplateExecutionIdVal.toString().isEmpty() && !wireTemplateExecutionIdVal.toString().equalsIgnoreCase("0")) {
				wireTemplateExecutionId = requestParams.get(Constants.BULKWIRETEMPLATEEXECUTION_ID).toString();
			}else {
				alert.prepareError("wireTemplateExecutionId  is missing in the payload which is mandatory to fetch the Execution details of the bulk wire Template").log();
				return ErrorCodeEnum.ERR_14014.setErrorCode(result);
			}
			if (requestParams.get(Constants.SORTBYPARAM) != null)
				sortByParam = requestParams.get(Constants.SORTBYPARAM).toString();
			if (requestParams.get(Constants.SORTORDER) != null)
				sortOrder = requestParams.get(Constants.SORTORDER).toString();
			if (requestParams.get(Constants.SEARCHSTRING) != null && !requestParams.get(Constants.SEARCHSTRING).toString().isEmpty())
				searchString = requestParams.get(Constants.SEARCHSTRING).toString();
			if (requestParams.get(Constants.STATUSFILTER) != null && !requestParams.get(Constants.STATUSFILTER).toString().isEmpty())
				statusFilter = requestParams.get(Constants.STATUSFILTER).toString();

			Map<String, Object> customer = CustomerSession.getCustomerMap(request);
			String user_id = CustomerSession.getCustomerId(customer);
			
			String featureAction1 = FeatureAction.DOMESTIC_WIRE_TRANSFER_VIEW;
	        String featureAction2 = FeatureAction.INTERNATIONAL_WIRE_TRANSFER_VIEW;

	        AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl
	               .getInstance().getFactoryInstance(BusinessDelegateFactory.class)
	               .getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);
	        
	        int isDomesticPermitted = 0, isInternationalPermitted = 0;
	        
	        String featureActionlistDomestic = CustomerSession.getPermittedActionIds(request, Arrays.asList(featureAction1));		
			if (!StringUtils.isEmpty(featureActionlistDomestic)) {
				isDomesticPermitted = 1;	
			}
			
			String featureActionlistInternational = CustomerSession.getPermittedActionIds(request, Arrays.asList(featureAction2));		
			if (!StringUtils.isEmpty(featureActionlistInternational)) {
				isInternationalPermitted = 1;	
			}			
			
			if (isDomesticPermitted == 0 && isInternationalPermitted == 0) {
				return ErrorCodeEnum.ERR_12001.setErrorCode(result);
			}
			Object delegateResponse;
			try {
			delegateResponse = wireTransactionDelegate.fetchTransactionsByWireTemplateExecutionId(wireTemplateExecutionId, sortByParam, sortOrder, searchString, statusFilter, isDomesticPermitted, isInternationalPermitted);
			}catch(Exception e) {
				alert.prepareError("Error while fetching transactions of a template execution  from Execution Id : " + e).log();
				return ErrorCodeEnum.ERR_14008.setErrorCode(result);	
			}
			if(delegateResponse == null) {
				return ErrorCodeEnum.ERR_12000.setErrorCode(result);
			}
			else if(delegateResponse instanceof String) {
				return ErrorCodeEnum.ERR_12001.setErrorCode(result);
			}
			else if(delegateResponse instanceof List<?>) {
				wireTransactionDTOs = (List<WireTransactionDTO>)delegateResponse;
			}
			
			try {
			JSONArray rulesJSONArr = new JSONArray(wireTransactionDTOs);
			JSONArray lineItemsDomesticJSONArr = new JSONArray();
			JSONArray lineItemsInternationalJSONArr = new JSONArray();
			int jsonObj;

			for(jsonObj = 0;jsonObj< rulesJSONArr.length();jsonObj++)
			{
				if(rulesJSONArr.getJSONObject(jsonObj).getString(Constants.WIREACCOUNTTYPE).equalsIgnoreCase(Constants.DOMESTIC_RECORDS))
				{
					lineItemsDomesticJSONArr.put(rulesJSONArr.getJSONObject(jsonObj));
				}
				else{
					lineItemsInternationalJSONArr.put(rulesJSONArr.getJSONObject(jsonObj));
					
				}
			}	
			JSONObject responseObj = new JSONObject();
			responseObj.put(Constants.DOMESTIC_RECORDS, lineItemsDomesticJSONArr);
			responseObj.put(Constants.INTERNATIONAL_RECORDS, lineItemsInternationalJSONArr);
			if(statusFilter == "" && searchString == "")
			{
				Integer totalCount = rulesJSONArr.length();
				Integer successCount = 0;
				Integer failedCount = 0;
				Integer pendingCount = 0;
			    
				for(jsonObj = 0;jsonObj< rulesJSONArr.length();jsonObj++)
				{
					if(rulesJSONArr.getJSONObject(jsonObj).getString(Constants.STATUS).equalsIgnoreCase(Constants.SUCCESS))
					{
						successCount++;
						continue;
					}
					else if(rulesJSONArr.getJSONObject(jsonObj).getString(Constants.STATUS).equalsIgnoreCase(Constants.FAILED)||rulesJSONArr.getJSONObject(jsonObj).getString(Constants.STATUS).equalsIgnoreCase(Constants.DENIED))
					{	
						failedCount++;
						continue;
					}
					else if(rulesJSONArr.getJSONObject(jsonObj).getString(Constants.STATUS).equalsIgnoreCase(Constants.PENDING))
					{	
						pendingCount++;
						continue;
					}
				}
				responseObj.put(Constants.TOTALCOUNT, totalCount);
				responseObj.put(Constants.SUCCESSCOUNT , successCount);
				responseObj.put(Constants.FAILEDCOUNT, failedCount);
				responseObj.put(Constants.PENDINGCOUNT, pendingCount);
			}
		    	
			JSONObject finalRespObj = new JSONObject();
			finalRespObj.put(Constants.BULKWIRETEMPLATEEXECUTIONDETAILS, responseObj);
			result = JSONToResult.convert(finalRespObj.toString());
		}
		catch(Exception exp) {
			alert.prepareError("Exception occured while invoking wiretransaction business delegate",exp).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(result);
		}
		return result;
	
	}
	
	public Result getTransactionsByBulkWireFileExecutionId(String methodID, Object[] inputArray, DataControllerRequest request, 
			DataControllerResponse response) {
	
		Result result = new Result();
		List<WireTransactionDTO> wireTransactionDTOs = null;
		
		@SuppressWarnings("unchecked")
		HashMap<String, Object> requestParams = (HashMap<String, Object>)inputArray[1];
		
			// Initialize the required BusinessDelegate class to perform the operation
		BulkWiresBusinessDelegate wireTransactionDelegate = 
					DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
					.getBusinessDelegate(BulkWiresBusinessDelegate.class);
			Object wireFileExecutionIdVal = requestParams.get(Constants.BULKWIREFILEEXECUTION_ID);
			String wireFileExecutionId = "";
			String sortByParam = "";
			String sortOrder = "";
			String searchString = "";
			String statusFilter = "";
			if( wireFileExecutionIdVal != null &&  !wireFileExecutionIdVal.toString().isEmpty() && !wireFileExecutionIdVal.toString().equalsIgnoreCase("0")) {
				wireFileExecutionId = requestParams.get(Constants.BULKWIREFILEEXECUTION_ID).toString();
			}else {
				alert.prepareError("WireFileExecutionId  is missing in the payload which is mandatory to fetch the Execution details of the bulk wire file").log();
				return ErrorCodeEnum.ERR_14007.setErrorCode(result);
			}
			if (requestParams.get(Constants.SORTBYPARAM) != null)
				sortByParam = requestParams.get(Constants.SORTBYPARAM).toString();
			if (requestParams.get(Constants.SORTORDER) != null)
				sortOrder = requestParams.get(Constants.SORTORDER).toString();
			if (requestParams.get(Constants.SEARCHSTRING) != null && !requestParams.get(Constants.SEARCHSTRING).toString().isEmpty())
				searchString = requestParams.get(Constants.SEARCHSTRING).toString();
			if (requestParams.get(Constants.STATUSFILTER) != null && !requestParams.get(Constants.STATUSFILTER).toString().isEmpty())
				statusFilter = requestParams.get(Constants.STATUSFILTER).toString();

			Map<String, Object> customer = CustomerSession.getCustomerMap(request);
			String user_id = CustomerSession.getCustomerId(customer);
			
			String featureAction1 = FeatureAction.DOMESTIC_WIRE_TRANSFER_VIEW;
	        String featureAction2 = FeatureAction.INTERNATIONAL_WIRE_TRANSFER_VIEW;

	        AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl
	               .getInstance().getFactoryInstance(BusinessDelegateFactory.class)
	               .getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);
	        
	        int isDomesticPermitted = 0, isInternationalPermitted = 0;
	        
	        String featureActionlistDomestic = CustomerSession.getPermittedActionIds(request, Arrays.asList(featureAction1));		
			if (!StringUtils.isEmpty(featureActionlistDomestic)) {
				isDomesticPermitted = 1;	
			}
			
			String featureActionlistInternational = CustomerSession.getPermittedActionIds(request, Arrays.asList(featureAction2));		
			if (!StringUtils.isEmpty(featureActionlistInternational)) {
				isInternationalPermitted = 1;	
			}			
			
			if (isDomesticPermitted == 0 && isInternationalPermitted == 0) {
				return ErrorCodeEnum.ERR_12001.setErrorCode(result);
			}
	        Object delegateResponse;
			try {
				
			delegateResponse = wireTransactionDelegate.fetchTransactionsByWireExecutionId(wireFileExecutionId,sortByParam, sortOrder,searchString,statusFilter, isDomesticPermitted, isInternationalPermitted);
			
			if(delegateResponse == null) {
				return ErrorCodeEnum.ERR_12000.setErrorCode(result);
			}
			else if(delegateResponse instanceof String) {
				return ErrorCodeEnum.ERR_12001.setErrorCode(result);
			}
			else if(delegateResponse instanceof List<?>) {
				wireTransactionDTOs = (List<WireTransactionDTO>)delegateResponse;
			}
			JSONArray rulesJSONArr = new JSONArray(wireTransactionDTOs);
			JSONArray lineItemsDomesticJSONArr = new JSONArray();
			JSONArray lineItemsInternationalJSONArr = new JSONArray();
			int jsonObj;

			for(jsonObj = 0;jsonObj< rulesJSONArr.length();jsonObj++)
			{
				if(rulesJSONArr.getJSONObject(jsonObj).getString(Constants.WIREACCOUNTTYPE).equalsIgnoreCase(Constants.DOMESTIC_RECORDS))
				{
					lineItemsDomesticJSONArr.put(rulesJSONArr.getJSONObject(jsonObj));
				}
				else{
					lineItemsInternationalJSONArr.put(rulesJSONArr.getJSONObject(jsonObj));
					
				}
			}	
			JSONObject responseObj = new JSONObject();
			responseObj.put(Constants.DOMESTIC_RECORDS, lineItemsDomesticJSONArr);
			responseObj.put(Constants.INTERNATIONAL_RECORDS, lineItemsInternationalJSONArr);
			if(statusFilter == "" && searchString == "")
			{
				Integer totalCount = rulesJSONArr.length();
				Integer successCount = 0;
				Integer failedCount = 0;
				Integer pendingCount = 0;
			    
				for(jsonObj = 0;jsonObj< rulesJSONArr.length();jsonObj++)
				{
					if(rulesJSONArr.getJSONObject(jsonObj).getString(Constants.STATUS).equalsIgnoreCase(Constants.SUCCESS))
					{
						successCount++;
						continue;
					}
					else if(rulesJSONArr.getJSONObject(jsonObj).getString(Constants.STATUS).equalsIgnoreCase(Constants.FAILED)||rulesJSONArr.getJSONObject(jsonObj).getString(Constants.STATUS).equalsIgnoreCase(Constants.DENIED))
					{	
						failedCount++;
						continue;
					}
					else if(rulesJSONArr.getJSONObject(jsonObj).getString(Constants.STATUS).equalsIgnoreCase(Constants.PENDING))
					{	
						pendingCount++;
						continue;
					}
				}
				responseObj.put(Constants.TOTALCOUNT, totalCount);
				responseObj.put(Constants.SUCCESSCOUNT , successCount);
				responseObj.put(Constants.FAILEDCOUNT, failedCount);
				responseObj.put(Constants.PENDINGCOUNT, pendingCount);
			}
		    	
			JSONObject finalRespObj = new JSONObject();
			finalRespObj.put(Constants.BULKWIREFILEEXECUTIONDETAILS, responseObj);
			result = JSONToResult.convert(finalRespObj.toString());
		}
		catch(Exception exp) {
			alert.prepareError("Exception occured while invoking wiretransaction business delegate",exp).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(result);
		}
		return result;
	
	}


	@Override
	public Result getBulkWireTemplateTransactionDetail(String methodID, Object[] inputArray,
			DataControllerRequest request, DataControllerResponse response) {
		@SuppressWarnings("unchecked")
		Map<String, Object> inputParams =  (HashMap<String, Object>)inputArray[1];
		Result result = new Result();

		Map < String, Object > customer = CustomerSession.getCustomerMap(request);

        String bulkWireTemplateID = "";
        
        if(inputParams.get(Constants.BULKWIRETEMPLATEID) !=null && inputParams.get(Constants.BULKWIRETEMPLATEID) != "" ) {
        	bulkWireTemplateID = inputParams.get(Constants.BULKWIRETEMPLATEID).toString();
		}else {
			alert.prepareError("BulkWireTemplateId  is missing in the payload which is mandatory to fetch the template details").log();
			return ErrorCodeEnum.ERR_14009.setErrorCode(result);
		}
        String user_id = CustomerSession.getCustomerId(customer);
        String featureAction1 = FeatureAction.DOMESTIC_WIRE_TRANSFER_VIEW_BULK_TEMPLATES;
        String featureAction2 = FeatureAction.INTERNATIONAL_WIRE_TRANSFER_VIEW_BULK_TEMPLATES;
        
        AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);
        
        Boolean isDomesticTemplatePermitted = false;
        Boolean isInternationalTemplatePermitted = false;
        
        String featureActionlistDomesticTemplate = CustomerSession.getPermittedActionIds(request, Arrays.asList(featureAction1));		
		if (!StringUtils.isEmpty(featureActionlistDomesticTemplate)) {
			isDomesticTemplatePermitted = true;	
		}
		
		String featureActionlistInternationalTemplate = CustomerSession.getPermittedActionIds(request, Arrays.asList(featureAction2));		
		if (!StringUtils.isEmpty(featureActionlistInternationalTemplate)) {
			isInternationalTemplatePermitted = true;	
		}
       
        BulkWiresBusinessDelegate bulkWireDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(BulkWiresBusinessDelegate.class);

        if(!isDomesticTemplatePermitted && !isInternationalTemplatePermitted) {
        	return ErrorCodeEnum.ERR_12001.setErrorCode(result);
        }else {
        	JSONObject delegateResponse = bulkWireDelegate.getBWTemplateDomesticInternationalCount(bulkWireTemplateID,"");
        	if(isDomesticTemplatePermitted && !isInternationalTemplatePermitted) {
        		if(Integer.parseInt(delegateResponse.getString(Constants.NUMDOMESTICTRANSACTIONS)) == 0)
        			return ErrorCodeEnum.ERR_12001.setErrorCode(result);	
        	}else if(!isDomesticTemplatePermitted && isInternationalTemplatePermitted) {
        		if(Integer.parseInt(delegateResponse.getString(Constants.NUMINTERNATIONALTRANSACTIONS)) == 0)
        			return ErrorCodeEnum.ERR_12001.setErrorCode(result);
        	}
        }
      
      if(!bulkWireDelegate.isTemplateAccessibleByUser(bulkWireTemplateID, customer))
      {
    	alert.prepareError("User does not have enough permissions to access the details of the template with given id"+bulkWireTemplateID).log();
		return ErrorCodeEnum.ERR_14010.setErrorCode(result);
      }
		List<BulkWireTemplateTransactionsDetailDTO> bulkWireTemplateTransactionDetailsDTO = null;

		BulkWireTransactionsBusinessDelegate bulkWireTransactionDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(BulkWireTransactionsBusinessDelegate.class);
		String sortBy = "";
		String sortOrder = "";
		String searchString = "";
		Object pageOffset = null;
		Object pageSize = null;
		
		if(inputParams.get(Constants.SORTBYPARAM) !=null){
			sortBy = inputParams.get(Constants.SORTBYPARAM).toString();
		}
		if(inputParams.get(Constants.SORTORDER)!=null) {
			sortOrder = inputParams.get(Constants.SORTORDER).toString();
		}
		if(inputParams.get(Constants.SEARCHSTRING)!=null) {
			searchString = inputParams.get(Constants.SEARCHSTRING).toString();
		}
		if(inputParams.get(Constants.PAGEOFFSET)!= null) {
			pageOffset = inputParams.get(Constants.PAGEOFFSET);
		}
		if(inputParams.get(Constants.PAGESIZE)!= null) {
			pageSize = inputParams.get(Constants.PAGESIZE);
		}
		try {
			bulkWireTemplateTransactionDetailsDTO = bulkWireTransactionDelegate.getBulkWireTemplateTransactionDetail(bulkWireTemplateID,sortBy,sortOrder,pageOffset,pageSize,searchString);	
		}catch(Exception e) {
			alert.prepareError("Error while fetching bulkWireTemplateTransactionDetailsDTO from BulkWireTransactionsBusinessDelegate : " + e).log();
			return ErrorCodeEnum.ERR_14008.setErrorCode(result);	
		}
		try {
			JSONArray respJsonArray = new JSONArray(bulkWireTemplateTransactionDetailsDTO);
			JSONObject respJson = new JSONObject();
			respJson.put(Constants.BULKWIRETEMPLATETRANSACTDETAILS_TABLE, respJsonArray);
			result = JSONToResult.convert(respJson.toString());
		} catch (Exception e) {
			alert.prepareError("Error while converting response bulkWireDTO to result : " + e).log();
			return null;
		}
		return result;
	}
	
}
