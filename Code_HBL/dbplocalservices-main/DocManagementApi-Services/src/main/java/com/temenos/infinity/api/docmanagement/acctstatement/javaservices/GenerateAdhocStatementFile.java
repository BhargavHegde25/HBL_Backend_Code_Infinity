package com.temenos.infinity.api.docmanagement.acctstatement.javaservices;

import java.util.HashMap;
import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.google.gson.JsonObject;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.LegalEntityUtil;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.api.ServicesManagerHelper;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.konylabs.middleware.ehcache.ResultCache;
import com.temenos.dbx.product.commonsutils.CommonUtils;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.infinity.api.docmanagement.javaservices.GenerateTransactionsDetails;
import com.temenos.infinity.api.docmanagement.javaservices.SessionMap;

public class GenerateAdhocStatementFile implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	public static final int UNIQUE_ID_LENGTH = 32;

	@SuppressWarnings("unchecked")
	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Log4j2Configurator.getInstance();
		Result result = new Result();
		try {

			HashMap<String, String> inputParams = (HashMap<String, String>) inputArray[1];
			//String userId = inputParams.get("userId");
			String userId = HelperMethods.getUserIdFromSession(dcRequest);
			String fileType = inputParams.get("fileType");			
			String accountID = inputParams.get("accountID");   
			String accountName = inputParams.get("accountName");
			String transactionType = inputParams.get("transactionType");                                     
			//String offset = inputParams.get("offset");                                   
			String limit = "500";                            
			String isScheduled = inputParams.get("isScheduled");               
			String order = inputParams.get("order");
			String requestType = inputParams.get("requestType");
			String searchStartDate = inputParams.get("searchStartDate");                             
			String searchEndDate = inputParams.get("searchEndDate"); 
			String description = inputParams.get(DBPUtilitiesConstants.SEARCH_DESCRIPTION);
			String searchMinAmount = inputParams.get("searchMinAmount");
			String searchMaxAmount = inputParams.get("searchMaxAmount");
			String fromCheckNumber = inputParams.get("fromCheckNumber");
			String toCheckNumber = inputParams.get("toCheckNumber");
			String branchName = inputParams.get("branchName");
			String searchDateRange = inputParams.get("searchDateRange");
			String searchTransactionType = inputParams.get("searchTransactionType");
		
			String dateFormat = Constants.COMBINED_STATTEMENTS_DATE_FORMAT;
			
			String currencyCode = "EUR";
			//String bankName = "Eurobank";
			

			// check for empty constant value
			String id = "";
			String currentDate = HelperMethods.getCurrentDate();
			currentDate = HelperMethods.convertDateFormat(currentDate, Constants.COMBINED_STATTEMENTS_DATE_FORMAT);
			String fileName = "Adhoc_Statement_" + accountID + "_" + currentDate + "." + fileType;
			String status = Constants.STATUS_INPROGRESS;

			HashMap<String, String> getDataInputParams = new HashMap<String, String>();
			String legalEntityId = LegalEntityUtil.getLegalEntityIdFromSessionOrCache(dcRequest); 
			getDataInputParams.put(Constants.$FILTER, "userId  eq " + userId + " and accountIds eq "+accountID+" and statementType eq ADHOC and legalEntityId" + DBPUtilitiesConstants.EQUAL +legalEntityId );
			Result getStatementFileDetails = HelperMethods.callApi(dcRequest, getDataInputParams, HelperMethods.getHeaders(dcRequest), URLConstants.ACCOUNTS_STATEMENT_FILES_GET);

			Dataset accountStatementset = getStatementFileDetails.getDatasetById("accountsstatementfiles");
			
			JSONObject inputParamsJson = new JSONObject(inputParams);
			
			  ServicesManager servicesManager = dcRequest.getServicesManager();
              String customerID = (String) servicesManager.getIdentityHandler().getUserAttributes().get("customer_id");
              String key="INTERNAL_BANK_ACCOUNTS"+customerID;
              try {
                  ResultCache resultCache = ServicesManagerHelper.getServicesManager().getResultCache();
                  if (resultCache != null && StringUtils.isNotBlank(key)) {
                      String value = (String) resultCache.retrieveFromCache(key);
                      SessionMap sessionData = new SessionMap();
                      sessionData.setData(value);
                     // return sessionData;

                      Map<String, String> account = sessionData.getValue(accountID);
                      if(account!=null) {
                          accountName =  account.get("accountName").replace(" ", "");
                          currencyCode =  account.get("currencyCode");
                          branchName = account.get("branchName")==null?"":account.get("branchName").replace(" ", "");

                      }
                  }
              } catch (Exception e) {
                  alert.prepareError("Exception occured while fetching ResultCache instance from Services Manager API", e).log();
              }
            
             
             
			
			HashMap<String, String> data = new HashMap<String, String>();
			
			data.put("userId", userId);
			data.put("fileName", fileName);
			data.put("status", status);
			data.put("fileType", fileType);
			data.put("modifiedBy", userId);
			data.put("accountIds", accountID);
			data.put("fromDate", searchStartDate);
			data.put("toDate", searchEndDate);
			data.put("inputPayload", inputParamsJson.toString());
			data.put("limit", limit.toString());
			data.put("statementType", "ADHOC");
			data.put("legalEntityId", legalEntityId);
			data.put("accountName", accountName);
			data.put("currency", currencyCode);
			data.put("branch", branchName);
			
			if (accountStatementset.getAllRecords().size() > 0) {
				diagnostic.prepareInfo("File aleardy exists for user updating the existing row").log();
				id = accountStatementset.getRecord(0).getParamValueByName("id");
				data.put("id", id);
				data.put("failureMessage", "");
				data.put("lastmodifiedts", HelperMethods.getCurrentTimeStamp());
				diagnostic.prepareDebug("**************** GenerateAdhocStatementFile ACCOUNTS_STATEMENT_FILES_UPDATE data :"+data.toString()).log();
				Result updateStatementFiles = HelperMethods.callApi(dcRequest, data, HelperMethods.getHeaders(dcRequest), URLConstants.ACCOUNTS_STATEMENT_FILES_UPDATE);
				
				if (updateStatementFiles.getParamValueByName("opstatus") != null
						&& !updateStatementFiles.getParamValueByName("opstatus").toString().equalsIgnoreCase("0")) {
					
					alert.prepareError("Error while updating the combined statement with userId" + userId).log();
					result.addParam(new Param(ErrorCodeEnum.ERROR_CODE_KEY,	String.valueOf(ErrorCodeEnum.ERR_28028.getErrorCode())));
					result.addParam(new Param(ErrorCodeEnum.ERROR_MESSAGE_KEY, ErrorCodeEnum.ERR_28028.getMessage()));
					
					updateFailureStatus(id, ErrorCodeEnum.ERR_28028.getMessage(), dcRequest);
					
					diagnostic.prepareDebug("*************** GenerateAdhocStatementFile result111 "+ResultToJSON.convert(result)).log();
					
					return result;
				}
			} else {
				
				id = CommonUtils.generateUniqueID(UNIQUE_ID_LENGTH);
				data.put("id", id);
				diagnostic.prepareInfo("File doesnot exists for user so creating new row").log();
				diagnostic.prepareDebug("**************** GenerateAdhocStatementFile ACCOUNTS_STATEMENT_FILES_CREATE data :"+data.toString()).log();
				Result createStatementFiles = HelperMethods.callApi(dcRequest, data, HelperMethods.getHeaders(dcRequest), URLConstants.ACCOUNTS_STATEMENT_FILES_CREATE);

				if (createStatementFiles.getParamValueByName("opstatus") != null
						&& !createStatementFiles.getParamValueByName("opstatus").toString().equalsIgnoreCase("0")) {
					
					alert.prepareError("Error while updating the row with userId" + userId).log();
					result.addParam(new Param(ErrorCodeEnum.ERROR_CODE_KEY, String.valueOf(ErrorCodeEnum.ERR_28029.getErrorCode())));
					result.addParam(new Param(ErrorCodeEnum.ERROR_MESSAGE_KEY, ErrorCodeEnum.ERR_28029.getMessage()));
					diagnostic.prepareDebug("*************** GenerateAdhocStatementFile result222 "+ResultToJSON.convert(result)).log();
					return result;
				}
			}
			
			data.remove("inputPayload");
			data.put(DBPUtilitiesConstants.X_KONY_AUTHORIZATION, dcRequest.getHeader(DBPUtilitiesConstants.X_KONY_AUTHORIZATION));
			data.put(DBPUtilitiesConstants.X_KONY_DEVICEID, dcRequest.getHeader(DBPUtilitiesConstants.X_KONY_DEVICEID));
			data.put("accountID", accountID);
			data.put("transactionType", transactionType);
			//data.put("offset", offset);
			data.put("limit", limit);
			data.put("isScheduled", isScheduled);
			data.put("order", order);
			data.put("requestType", requestType);
			data.put("searchStartDate", searchStartDate);
			data.put("searchEndDate", searchEndDate);
			data.put("currencyCode", currencyCode);		
			data.put("accountName", accountName);

			data.put("searchDateRange", searchDateRange);
			data.put("searchTransactionType", searchTransactionType);
			
			
			if(null != description && !"".equals(description.trim()))
				data.put(DBPUtilitiesConstants.SEARCH_DESCRIPTION,description);
			if(null != searchMinAmount && !"".equals(searchMinAmount.trim()))
				data.put("searchMinAmount",searchMinAmount);
			if(null != searchMaxAmount && !"".equals(searchMaxAmount.trim()))
				data.put("searchMaxAmount",searchMaxAmount);
			if(null != fromCheckNumber && !"".equals(fromCheckNumber.trim()))
				data.put("fromCheckNumber",fromCheckNumber);
			if(null != toCheckNumber && !"".equals(toCheckNumber.trim()))
				data.put("toCheckNumber",toCheckNumber);
			
			Map<String, String> user = HelperMethods.getUserFromIdentityService(dcRequest);

			data.put("dateFormat", dateFormat);
			data.put("generatedBy", user.get("userName"));
			if (data.containsKey("failureMessage")) {
				data.remove("failureMessage");
			}
			if (data.containsKey("lastmodifiedts")) {
				data.remove("lastmodifiedts");
			}
			Map<String, String> userProfile = HelperMethods.getCustomerFromIdentityService(dcRequest);
			if (userProfile.containsKey("customerType")) {
				data.put("customerType", userProfile.get("customerType"));
			}
			String companyId = (String) dcRequest.getServicesManager().getIdentityHandler().getUserAttributes().get("companyId");
			if (StringUtils.isNotBlank(companyId)) {
				data.put("companyId", companyId);
			}
			
			GenerateTransactionsDetails generateTransactionsDetails = new GenerateTransactionsDetails();
			
			
			HashMap<String, String> pushEventData = new HashMap<String, String>();
			//pushEventData.put("eventCode", "COMBINED_STATEMENT");
			pushEventData.put("eventCode", "ADHOC_STATEMENT");
			pushEventData.put("eventData", data.toString());
			diagnostic.prepareDebug("******************** GenerateAdhocStatementFile eventData :"+data.toString()).log();
			Result pushResult = HelperMethods.callApi(dcRequest, pushEventData, HelperMethods.getHeaders(dcRequest), URLConstants.PUSH_EVENT);
			diagnostic.prepareDebug("******************** GenerateAdhocStatementFile pushResult :"+ResultToJSON.convert(pushResult)).log();
			if (pushResult.getParamValueByName("success") != null && pushResult.getParamValueByName("success").equals("false")) {
				alert.prepareError("Error while creating event").log();
				result.addParam(ErrorCodeEnum.ERROR_CODE_KEY, pushResult.getParamValueByName("dbpErrCode"));
				result.addParam(ErrorCodeEnum.ERROR_MESSAGE_KEY, pushResult.getParamValueByName("dbpErrMsg"));
				
				updateFailureStatus(id, pushResult.getParamValueByName("dbpErrMsg"), dcRequest);
				diagnostic.prepareDebug("*************** GenerateAdhocStatementFile result333 "+ResultToJSON.convert(result)).log();
				return result;
			}

		} catch (Exception exception) {
			alert.prepareError("Error occured while generating combined statement", exception).log();
			result.addParam(ErrorCodeEnum.ERROR_CODE_KEY, String.valueOf(ErrorCodeEnum.ERR_28030.getErrorCode()));
			result.addParam(ErrorCodeEnum.ERROR_MESSAGE_KEY, ErrorCodeEnum.ERR_28030.getMessage());
		}
		diagnostic.prepareDebug("*************** GenerateAdhocStatementFile result444 "+ResultToJSON.convert(result)).log();
		return result;
	}

	public static void updateFailureStatus(String fileId, String message, DataControllerRequest dcRequest) {
		diagnostic.prepareDebug("*************** updateFailureStatus **********").log();
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("id", fileId);
		data.put("status", Constants.STATUS_FAIL);
		data.put("failureMessage", message);
		try {
			HelperMethods.callApi(dcRequest, data, HelperMethods.getHeaders(dcRequest),	URLConstants.ACCOUNTS_STATEMENT_FILES_UPDATE);
		} catch (HttpCallException e) {
			alert.prepareError("Error while updating failure").log();
		}
	}

}
