package com.temenos.infinity.product.bulkpaymentservices.resource.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.commons.businessdelegate.api.AccountBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.ApplicationBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.CustomerBusinessDelegate;
import com.temenos.dbx.product.commons.dto.CustomerAccountsDTO;
import com.temenos.dbx.product.commons.dto.FilterDTO;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.constants.FeatureAction;
import com.temenos.infinity.product.bulkpaymentservices.backenddelegate.api.BulkPaymentPOBackendDelegate;
import com.temenos.infinity.product.bulkpaymentservices.backenddelegate.api.BulkPaymentRecordBackendDelegate;
import com.temenos.infinity.product.bulkpaymentservices.businessdelegate.api.BulkPaymentPOBusinessDelegate;
import com.temenos.infinity.product.bulkpaymentservices.dto.BulkPaymentPODTO;
import com.temenos.infinity.product.bulkpaymentservices.dto.BulkPaymentRecordDTO;
import com.temenos.infinity.product.bulkpaymentservices.resource.api.BulkPaymentPOResource;

public class BulkPaymentPOResourceImpl implements BulkPaymentPOResource {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	BulkPaymentPOBusinessDelegate bulkPaymentPOBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(BulkPaymentPOBusinessDelegate.class);
	BulkPaymentPOBackendDelegate bulkPaymentPOBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(BulkPaymentPOBackendDelegate.class);
	BulkPaymentRecordBackendDelegate bulkPaymentRecordBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(BulkPaymentRecordBackendDelegate.class);
	CustomerBusinessDelegate customerDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(CustomerBusinessDelegate.class);
	ApplicationBusinessDelegate application = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApplicationBusinessDelegate.class);
	AccountBusinessDelegate accountBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AccountBusinessDelegate.class);

	@Override
	public Result addPaymentOrder(String methodID, Object[] inputArray, DataControllerRequest request, DataControllerResponse response) {
		
		List<String> requiredActionIds = Arrays.asList(FeatureAction.BULK_PAYMENT_REQUEST_ADD_PO);
		String features = CustomerSession.getPermittedActionIds(request, requiredActionIds);

		if (features == null) {
			return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
		}
		
		@SuppressWarnings("unchecked")
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
		Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		String createdby = CustomerSession.getCustomerId(customer);
		String featureActionId = FeatureAction.BULK_PAYMENT_REQUEST_ADD_PO;
		
		BulkPaymentPODTO validatedObj = _validateAddPODetails(inputParams);
		
		if(validatedObj.getDbpErrorCode() != null) {
			return validatedObj.getDbpErrorCode().setErrorCode(new Result());
		}
		
		String recordID = inputParams.get("recordId") != null ? inputParams.get("recordId").toString() : null;
		
		BulkPaymentRecordDTO record = bulkPaymentRecordBackendDelegate.fetchBulkPaymentRecordDetailsById(recordID, request);
		
		
		if(record == null) {
			alert.prepareError("Error occurred while fetching bulk payment record details from backend").log();
			return ErrorCodeEnum.ERR_21209.setErrorCode(new Result());
		}
		
		if(StringUtils.isNotBlank(record.getDbpErrMsg())) {
			alert.prepareError("Error occurred while fetching bulk payment record details from backend").log();
			return ErrorCodeEnum.ERR_00000.setErrorCode(new Result(), record.getDbpErrMsg());
		}
		
		BulkPaymentPODTO inputObj;
		try {
			inputParams.put("createdts",new SimpleDateFormat(Constants.TIMESTAMP_FORMAT).parse(application.getServerTimeStamp()));
			inputObj = JSONUtils.parse(new JSONObject(inputParams).toString(), BulkPaymentPODTO.class);
		} catch (IOException e) {
			alert.prepareError("Error occurred while parsing the input params: ", e).log();
			return null;
		}catch(Exception e) {
			alert.prepareError("Error occurred while parsing the input params: ", e).log();
			return null;
		}
		
		CustomerAccountsDTO account = accountBusinessDelegate.getAccountDetails(createdby, record.getFromAccount());
		String contractId = "";
		String coreCustomerId = "";
		String companyId = "";
		
		if (account != null) {
		 contractId = account.getContractId();
		 coreCustomerId = account.getCoreCustomerId();
		 companyId = account.getOrganizationId();
		}
		
		Result result = new Result();
		inputObj.setCompanyId(companyId);
		inputObj.setRoleId(customerDelegate.getUserContractCustomerRole(contractId, coreCustomerId, createdby));
		inputObj.setCreatedby(createdby);
		inputObj.setFeatureActionId(featureActionId);
		inputObj.setFromAccount(record.getFromAccount());
		inputObj.setBatchMode(record.getBatchMode());
		
		BulkPaymentPODTO outputObj = bulkPaymentPOBackendDelegate.addPaymentOrder(inputObj, request);
		
		if (outputObj == null) {
			alert.prepareError("Error occured while adding  Bulk Payment Payment Order").log();
			return ErrorCodeEnum.ERR_21209.setErrorCode(new Result());
		}
		
		if(StringUtils.isNotBlank(outputObj.getDbpErrMsg())) {
			alert.prepareError("Error occurred while adding  Bulk Payment Payment Order").log();
			return ErrorCodeEnum.ERR_00000.setErrorCodewithErrorDetails(new Result(), outputObj.getDbpErrMsg(), outputObj.geterrorDetails());
		}
		
		JSONObject resultObject = new JSONObject(outputObj);
		result = JSONToResult.convert(resultObject.toString());
		return result;
	}

	@Override
	public Result fetchPaymentOrders(String methodID, Object[] inputArray, DataControllerRequest request, DataControllerResponse response) {
		
		List<String> requiredActionIds = Arrays.asList(FeatureAction.BULK_PAYMENT_REQUEST_VIEW);
		String features = CustomerSession.getPermittedActionIds(request, requiredActionIds);

		if (features == null) {
			return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
		}
		
		@SuppressWarnings("unchecked")
		Map<String, String> inputParams = (HashMap<String, String>) inputArray[1];
		String recordId = inputParams.get("recordId") != null ? inputParams.get("recordId").toString() : null;
		
		if (recordId == null || recordId.isEmpty()) {
			alert.prepareError("Record ID is Empty.").log();
			return ErrorCodeEnum.ERR_21227.setErrorCode(new Result());
		}
		Result result = new Result();
		
		List<BulkPaymentPODTO> poList = bulkPaymentPOBackendDelegate.fetchPaymentOrders(recordId, request);
		
		if (poList == null) {
			alert.prepareError("Error occurred while fetching payment orders from backend").log();
			return ErrorCodeEnum.ERR_21210.setErrorCode(new Result());
		}
		
		if (poList.size() > 0 && StringUtils.isNotBlank(poList.get(0).getDbpErrMsg())) {
			alert.prepareError("Error occurred while fetching payment orders from backend").log();
			return ErrorCodeEnum.ERR_00000.setErrorCode(new Result(), poList.get(0).getDbpErrMsg());
		}
		
		@SuppressWarnings("unchecked")
		Map<String, Object> inputParamsMap = (HashMap<String, Object>) inputArray[1];
		FilterDTO filterDTO;
		try {
			filterDTO = JSONUtils.parse(new JSONObject(inputParamsMap).toString(), FilterDTO.class);
		} catch (IOException e) {
			alert.prepareError("Exception occurred while fetching params: ",e).log();
			return ErrorCodeEnum.ERR_21220.setErrorCode(new Result());
		}
		
		List<BulkPaymentPODTO> filteredPOs = filterDTO.filter(poList);

		try {
			JSONArray resArray = new JSONArray(filteredPOs);
			JSONObject resultObj = new JSONObject();
			resultObj.put(Constants.PAYMENTORDERS, resArray);
			result = JSONToResult.convert(resultObj.toString());
		} catch (Exception exp) {
			alert.prepareError("Error occurred while converting list to result", exp).log();
			return ErrorCodeEnum.ERR_21210.setErrorCode(new Result());
		}
		return result;
	}
	
	@Override
	public Result editPaymentOrder(String methodID, Object[] inputArray, DataControllerRequest request, DataControllerResponse response) {
		
		List<String> requiredActionIds = Arrays.asList(FeatureAction.BULK_PAYMENT_REQUEST_EDIT_PO);
		String features = CustomerSession.getPermittedActionIds(request, requiredActionIds);


		if (features == null) {
			return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
		}
		
		@SuppressWarnings("unchecked")
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
		Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		String recordId = inputParams.get("recordId") != null ? inputParams.get("recordId").toString() : null;
		String modifiedby = CustomerSession.getCustomerId(customer);
		JSONObject obj=new JSONObject(customer.get("backendIdentifiers").toString());
		String backendId=obj.getJSONArray( "CORE").getJSONObject(0).getString("BackendId");
		
		BulkPaymentPODTO validatedObj = _validateEditPODetails(inputParams);
		
		if(validatedObj.getDbpErrorCode() != null) {
			return validatedObj.getDbpErrorCode().setErrorCode(new Result());
		}
		
		BulkPaymentPODTO inputObj;
		try {
			inputParams.put("lastmodifiedts",new SimpleDateFormat(Constants.TIMESTAMP_FORMAT).parse(application.getServerTimeStamp()));
			inputObj = JSONUtils.parse(new JSONObject(inputParams).toString(), BulkPaymentPODTO.class);
		} catch (IOException e) {
			alert.prepareError("Error occurred while parsing the input params: ", e).log();
			return null;
		}catch(Exception e) {
			alert.prepareError("Error occurred while parsing the input params: ", e).log();
			return null;
		}

		//fetch Recordr to verify customer Id
		BulkPaymentRecordDTO record = bulkPaymentRecordBackendDelegate.fetchBulkPaymentRecordDetailsById(recordId, request);
		if(!backendId.equalsIgnoreCase(record.getCreatedby())){
			alert.prepareError("Logged in user is not allowed to access the record").log();
			return ErrorCodeEnum.ERR_13006.setErrorCode(new Result());
		}

		Result result = new Result();
		inputObj.setCreatedby(modifiedby);
		BulkPaymentPODTO outputObj = bulkPaymentPOBackendDelegate.updatePaymentOrder(inputObj, request);
		
		if (outputObj == null) {
			alert.prepareError("Error occured while editing  Bulk Payment Payment Order").log();
			return ErrorCodeEnum.ERR_21212.setErrorCode(new Result());
		}
		
		if (StringUtils.isNotBlank(outputObj.getDbpErrMsg())) {
			alert.prepareError("Error occured while editing  Bulk Payment Payment Order").log();
			return ErrorCodeEnum.ERR_00000.setErrorCodewithErrorDetails(new Result(), outputObj.getDbpErrMsg(), outputObj.geterrorDetails());
		}
		
		JSONObject resultObject = new JSONObject(outputObj);
		result = JSONToResult.convert(resultObject.toString());
		
		return result;
	}
	
	@Override
	public Result deletePaymentOrder(String methodID, Object[] inputArray, DataControllerRequest request, DataControllerResponse response) {
		
		List<String> requiredActionIds = Arrays.asList(FeatureAction.BULK_PAYMENT_REQUEST_REMOVE_PO);
		String features = CustomerSession.getPermittedActionIds(request, requiredActionIds);

		if (features == null) {
			return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
		}

		Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		JSONObject obj=new JSONObject(customer.get("backendIdentifiers").toString());
		String backendId=obj.getJSONArray( "CORE").getJSONObject(0).getString("BackendId");
		@SuppressWarnings("unchecked")
		Map<String, String> inputParams = (HashMap<String, String>) inputArray[1];
		
		String recordId = inputParams.get("recordId") != null ? inputParams.get("recordId").toString() : null;
		if (recordId == null || recordId.isEmpty()) {
			alert.prepareError("Record ID is Empty.").log();
			return ErrorCodeEnum.ERR_21227.setErrorCode(new Result());
		}
		
		String paymentOrderId = inputParams.get("paymentOrderId") != null ? inputParams.get("paymentOrderId").toString() : null;
		if (paymentOrderId == null || paymentOrderId.isEmpty()) {
			alert.prepareError("PaymentOrderId is Empty").log();
			return ErrorCodeEnum.ERR_21238.setErrorCode(new Result());
		}
		//fetch Record to verify customer Id
		BulkPaymentRecordDTO record = bulkPaymentRecordBackendDelegate.fetchBulkPaymentRecordDetailsById(recordId, request);
		if(!backendId.equalsIgnoreCase(record.getCreatedby())){
			alert.prepareError("Logged in user is not allowed to access the record").log();
			return ErrorCodeEnum.ERR_13006.setErrorCode(new Result());
		}
		Result result = new Result();

		BulkPaymentPODTO outputObj = bulkPaymentPOBackendDelegate.deletePaymentOrder(recordId, paymentOrderId, request);
		
		if (outputObj == null) {
			alert.prepareError("Error occured while deleting  Bulk Payment Payment Order").log();
			return ErrorCodeEnum.ERR_21211.setErrorCode(new Result());
		}
		
		if (StringUtils.isNotBlank(outputObj.getDbpErrMsg())) {
			alert.prepareError("Error occured while deleting  Bulk Payment Payment Order").log();
			return ErrorCodeEnum.ERR_00000.setErrorCodewithErrorDetails(new Result(), outputObj.getDbpErrMsg(), outputObj.geterrorDetails());
		}
		
		JSONObject resultObject = new JSONObject(outputObj);
		result = JSONToResult.convert(resultObject.toString());
		
		return result;
	}

	private BulkPaymentPODTO _validateAddPODetails(Map<String, Object> inputParams) {
		BulkPaymentPODTO poObj = new BulkPaymentPODTO();
		
		String recordID = inputParams.get("recordId") != null ? inputParams.get("recordId").toString() : null;
		if (recordID == null || recordID.isEmpty()) {
			alert.prepareError("RecordId is Empty. Error occured while Parsing Bulk Payment PO").log();			
			poObj.setDbpErrorCode(ErrorCodeEnum.ERR_21227);
			return poObj;
		}
		
		String recipientName = inputParams.get("recipientName") != null ? inputParams.get("recipientName").toString() : null;
		if (recipientName == null || recipientName.isEmpty()) {
			alert.prepareError("Recipient Name is Empty. Error occured while Parsing Bulk Payment PO").log();
			poObj.setDbpErrorCode(ErrorCodeEnum.ERR_21233);
			return poObj;
		}
		
		String accountNumber = inputParams.get("accountNumber") != null ? inputParams.get("accountNumber").toString() : null;
		if (accountNumber == null || accountNumber.isEmpty()) {
			alert.prepareError("Account Number is Empty. Error occured while Parsing Bulk Payment PO").log();
			poObj.setDbpErrorCode(ErrorCodeEnum.ERR_12002);
			return poObj;
		}
		
		String currency = inputParams.get("currency") != null ? inputParams.get("currency").toString() : null;
		if (currency == null || currency.isEmpty()) {
			alert.prepareError("Currency is Empty. Error occured while Parsing Bulk Payment PO").log();
			poObj.setDbpErrorCode(ErrorCodeEnum.ERR_21239);
			return poObj;
		}
		
		String amount = inputParams.get("amount") != null ? inputParams.get("amount").toString() : null;
		if (amount == null || amount.isEmpty()) {
			alert.prepareError("Amount is Empty. Error occured while Parsing Bulk Payment PO").log();
			poObj.setDbpErrorCode(ErrorCodeEnum.ERR_21234);
			return poObj;
		}

		String paymentReference = inputParams.get("paymentReference") != null ? inputParams.get("paymentReference").toString() : null;
		if (paymentReference == null || paymentReference.isEmpty()) {
			alert.prepareError("Payment Reference is Empty. Error occured while Parsing Bulk Payment PO").log();
			poObj.setDbpErrorCode(ErrorCodeEnum.ERR_21241);
			return poObj;
		} else if (!paymentReference.matches("^[[a-zA-Z0-9]\\s?.,':)(+-\\/]*") || paymentReference.length() > 35) {
			poObj.setDbpErrorCode(ErrorCodeEnum.ERR_21241);
			return poObj;
		}
		
		return poObj;
	}
	
	private BulkPaymentPODTO _validateEditPODetails(Map<String, Object> inputParams) {
		BulkPaymentPODTO poObj = new BulkPaymentPODTO();
		
		String recordID = inputParams.get("recordId") != null ? inputParams.get("recordId").toString() : null;
		if (recordID == null || recordID.isEmpty()) {
			alert.prepareError("RecordId is Empty. Error occured while Parsing Bulk Payment PO").log();			
			poObj.setDbpErrorCode(ErrorCodeEnum.ERR_21227);
			return poObj;
		}
		
		String paymentOrderId = inputParams.get("paymentOrderId") != null ? inputParams.get("paymentOrderId").toString() : null;
		if (paymentOrderId == null || paymentOrderId.isEmpty()) {
			alert.prepareError("PaymentOrderId is Empty. Error occured while Parsing Bulk Payment PO").log();
			poObj.setDbpErrorCode(ErrorCodeEnum.ERR_21238);
			return poObj;
		}
		
		String currency = inputParams.get("currency") != null ? inputParams.get("currency").toString() : null;
		if (currency == null || currency.isEmpty()) {
			alert.prepareError("Currency is Empty. Error occured while Parsing Bulk Payment PO").log();
			poObj.setDbpErrorCode(ErrorCodeEnum.ERR_21239);
			return poObj;
		}
		
		String amount = inputParams.get("amount") != null ? inputParams.get("amount").toString() : null;
		if (amount == null || amount.isEmpty()) {
			alert.prepareError("Amount is Empty. Error occured while Parsing Bulk Payment PO").log();
			poObj.setDbpErrorCode(ErrorCodeEnum.ERR_21234);
			return poObj;
		}

		String paymentReference = inputParams.get("paymentReference") != null ? inputParams.get("paymentReference").toString() : null;
		if (paymentReference == null || paymentReference.isEmpty()) {
			alert.prepareError("Payment Reference is Empty. Error occured while Parsing Bulk Payment PO").log();
			poObj.setDbpErrorCode(ErrorCodeEnum.ERR_21241);
			return poObj;
		} else if (!paymentReference.matches("^[[a-zA-Z0-9]\\s?.,':)(+-\\/]*") || paymentReference.length() > 35) {
			poObj.setDbpErrorCode(ErrorCodeEnum.ERR_21241);
			return poObj;
		}
		
		return poObj;
	}
	
}
