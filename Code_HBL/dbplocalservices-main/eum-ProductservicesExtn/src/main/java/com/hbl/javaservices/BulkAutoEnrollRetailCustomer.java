package com.hbl.javaservices;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.hbl.resource.impl.InfinityUserManagementResourceImplExtn;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.kony.dbx.util.CommonUtils;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.temenos.dbx.eum.product.usermanagement.resource.api.InfinityUserManagementResource;
import com.temenos.dbx.product.constants.ServiceId;
import com.temenos.dbx.product.dto.CustomerDTO;
import com.temenos.dbx.product.dto.DBXResult;

public class BulkAutoEnrollRetailCustomer implements JavaService2 {
	LoggerUtil logger = new LoggerUtil(BulkAutoEnrollRetailCustomer.class);
	long successCount = 0;
	long failureCount = 0;
	long Process1SuccessCount = 0;
	long Process1FailureCount = 0;
	long Process2SuccessCount = 0;
	long Process2FailureCount = 0;
	long Process3SuccessCount = 0;
	long Process3FailureCount = 0;
	private final int maxLength=20;
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest, DataControllerResponse dcResponse)
			throws Exception {
		 successCount = 0;
		 failureCount = 0;
		 Process1SuccessCount = 0;
		 Process1FailureCount = 0;
		 Process2SuccessCount = 0;
		 Process2FailureCount = 0;
		 Process3SuccessCount = 0;
		 Process3FailureCount = 0;
		Result result = new Result();
		if(methodID.equalsIgnoreCase("BulkAutoEnrollRetailCustomer")) {
			return process(methodID, inputArray, dcRequest, dcResponse);
		}else if(methodID.equalsIgnoreCase("BulkAutoEnrollRetailCustomer1")) {
			return process1(methodID, inputArray, dcRequest, dcResponse);
		}else if(methodID.equalsIgnoreCase("BulkAutoEnrollRetailCustomer2")) {
			return process2(methodID, inputArray, dcRequest, dcResponse);
		}else if(methodID.equalsIgnoreCase("BulkAutoEnrollRetailCustomer3")) {
			return process3(methodID, inputArray, dcRequest, dcResponse);
		}
		return result;
	}
	
	/*
	 *   BulkAutoEnrollRetailCustomer Logic
	 */
	public Result process(String methodID, Object[] inputArray, DataControllerRequest dcRequest, DataControllerResponse dcResponse){
		Result res = new Result();
		JSONArray legacyCustomers = preProcess(dcRequest);
		if(legacyCustomers.length()>0) {
		for(int i= 0 ;i<maxLength; i++) {
			JSONObject customerObj = legacyCustomers.getJSONObject(i);
			res=enrollProcess(methodID, customerObj, dcRequest, dcResponse);
		}
		}
		res.addParam(new Param("totalRecords", String.valueOf(legacyCustomers.length())));
		res.addParam(new Param("successCount", String.valueOf(successCount)));
		res.addParam(new Param("failureCount",  String.valueOf(failureCount)));
		logger.debug("BulkAutoEnrollRetailCustomer enrollRetailCustomer procesed result:" + ResultToJSON.convert(res));
		return res;
		
	}
	public JSONArray preProcess(DataControllerRequest dcRequest){
		return getLegacyCustomer(dcRequest);
	}
	public JSONArray getLegacyCustomer( DataControllerRequest dcRequest) {
		JSONArray legacycustomers = new JSONArray();
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = "dbxdb_legacycustomerV1_get";
		try {
		Map<String, Object> inputParams = new HashMap<>();
		String isEnrolled = "0";
		String filter = "isEnrolled" + DBPUtilitiesConstants.EQUAL + "'" + isEnrolled+ "'";
		inputParams.put(DBPUtilitiesConstants.FILTER, filter);
		Result response = CommonUtils.callIntegrationService(dcRequest, inputParams, dcRequest.getHeaderMap(), serviceName,operationName, false);
		if (response != null) {
			JSONObject responseObj = new JSONObject(ResultToJSON.convert(response));
			 legacycustomers = responseObj.getJSONArray("legacycustomerV1");
		}
		}catch (DBPApplicationException e) {
			logger.debug("Exception occured while getting the legacy customers in BulkAutoEnrollRetailCustomer"+e.getMessage());
		}catch (Exception e) {
			logger.debug("Exception occured while getting the legacy customers in BulkAutoEnrollRetailCustomer"+e.getMessage());
		}
		return legacycustomers;
	}
	public Result enrollProcess(String methodID, JSONObject customerObj, DataControllerRequest dcRequest, DataControllerResponse dcResponse){
		logger.debug("BulkAutoEnrollRetailCustomer enrollRetailCustomer inputMap:" + customerObj);
		String accountNumber = customerObj.optString("accountNumber");
		String accountName = customerObj.optString("accountName");
		String mobileNumber = customerObj.optString("mobileNumber");
		String email = customerObj.optString("email");
		Result res = new Result();
		if (StringUtils.isNotBlank(accountNumber) && StringUtils.isNotBlank(accountName) && StringUtils.isNotBlank(mobileNumber) && StringUtils.isNotBlank(email)) {
		try {
			Object[] inputArray= new Object[2];
			Map<String, Object> inputMap = new HashMap<String, Object>();
			inputMap.put("accountName", accountName);
			inputMap.put("accountNumber", accountNumber);
			inputMap.put("email", email);
			inputMap.put("mobileNumber", mobileNumber);
			inputMap.put("isMigrationFlow", "true");
			inputArray[1]=inputMap;
			dcRequest.addRequestParam_("legalEntityId", "NP0010001");
			InfinityUserManagementResource resource =
		            DBPAPIAbstractFactoryImpl.getResource(InfinityUserManagementResource.class);
			res  = resource.enrollRetailUserOperation("EnrollRetailUser", inputArray, dcRequest, dcResponse);
			logger.debug("HBL::BulkAutoEnrollRetailCustomer: enrollRetailCustomer: response:" + ResultToJSON.convert(res));
			}catch(ApplicationException e) {
				logger.error("Exception caught while enrollRetailCustomer ", e);
				res.addParam(new Param("dbpErrCode", e.getErrorCodeEnum().getErrorCodeAsString()));
				res.addParam(new Param("dbpErrMsg",  e.getErrorCodeEnum().getMessage()));
			}
			catch (Exception e) {
				logger.error("Exception caught while enrollRetailCustomer ", e);
				res.addParam(new Param("dbpErrCode", "500"));
				res.addParam(new Param("dbpErrMsg", e.getMessage()));
			}
		}else {
			res.addParam(new Param("dbpErrCode", ErrorCodeEnum.ERR_10801.getErrorCodeAsString()));
			res.addParam(new Param("dbpErrMsg",  ErrorCodeEnum.ERR_10801.getMessage()));
		}
		postProcess(res, customerObj, dcRequest);
		return res;
	}
	public void postProcess(Result enrollResult, JSONObject customerObj, DataControllerRequest dcRequest){
		String errmsg = null;
		String isEnrolled="2"; // byDefault Failed status
		String customerId=customerObj.optString("customerId");
		String infinityCustomerId = null;
		if(enrollResult!=null && !enrollResult.isEmpty()) {
			String dbpErrCode= enrollResult.getParamValueByName("dbpErrCode");
			String dbpErrMsg= enrollResult.getParamValueByName("dbpErrMsg");
			String isUserExists= enrollResult.getParamValueByName("isUserExists");
			String isUserEnrolled= enrollResult.getParamValueByName("isUserEnrolled");
			infinityCustomerId = enrollResult.getParamValueByName("infinityCustomerId");
			if(StringUtils.isBlank(dbpErrCode) && StringUtils.isBlank(dbpErrMsg)) {
				if(StringUtils.isNotBlank(isUserExists) && isUserExists.equalsIgnoreCase("false")) {
					errmsg="Customer Not Found";
				}
				else if(StringUtils.isNotBlank(isUserEnrolled) && isUserEnrolled.equalsIgnoreCase("true")) {
					errmsg="Customer Already Enrolled";
				}
				else if(StringUtils.isNotBlank(infinityCustomerId)){
					isEnrolled="1";
					errmsg = "SUCCESS";
				}
			}else {
				errmsg=dbpErrMsg;
			}
		}else {
			errmsg = "Something Went worng.";
		}
		int status= updateStaus(isEnrolled, customerId, infinityCustomerId, errmsg, dcRequest);
		if(status==1) {
			if(isEnrolled.equalsIgnoreCase("1") ) 
				successCount++;
			else
				failureCount++;
		}
	}
	private int updateStaus(String status, String customerId, String infinityCustomerId, String error, DataControllerRequest request) {
		HashMap<String, Object> inputParams = new HashMap<String, Object>();
		inputParams.put("customerId", customerId);
		inputParams.put("isEnrolled", status);
		inputParams.put("errorMessage", error);
		inputParams.put("infinityId", infinityCustomerId);
		String operationName = "dbxdb_legacycustomerV1_update";
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		try {
			Result result = CommonUtils.callIntegrationService(request, inputParams, request.getHeaderMap(), serviceName, operationName, false);
			String updatedRecords=result.getParamValueByName("updatedRecords");
			if (StringUtils.isNotEmpty(updatedRecords)) {
				if (Integer.parseInt(updatedRecords) > 0) 
					return 1;
				 else
					return 0;
			}
		} catch (Exception e) {
			logger.debug("Exception occured in BulkAutoEnrollRetailCustomer"+e.getMessage());
		}
		return 0;
	}
	
	/*
	 *  BulkAutoEnrollRetailCustomer1 Logic
	 */
	public Result process1(String methodID, Object[] inputArray, DataControllerRequest dcRequest, DataControllerResponse dcResponse){
		Result res = new Result();
		JSONArray legacyCustomers = preProcess1(dcRequest);
		if(legacyCustomers.length()>0) {
		for(int i= 0 ;i<maxLength; i++) {
			JSONObject customerObj = legacyCustomers.getJSONObject(i);
			res=enrollProcess1(methodID, customerObj, dcRequest, dcResponse);
		}
		}
		res.addParam(new Param("totalRecords", String.valueOf(legacyCustomers.length())));
		res.addParam(new Param("successCount", String.valueOf(Process1SuccessCount)));
		res.addParam(new Param("failureCount",  String.valueOf(Process1FailureCount)));
		logger.debug("BulkAutoEnrollRetailCustomer enrollRetailCustomer procesed result:" + ResultToJSON.convert(res));
		return res;
	}
	public JSONArray preProcess1(DataControllerRequest dcRequest){
		return getLegacyCustomer1(dcRequest);
	}
	public JSONArray getLegacyCustomer1( DataControllerRequest dcRequest) {
		JSONArray legacycustomers = new JSONArray();
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = "dbxdb_legacycustomerV1_1_get";
		try {
		Map<String, Object> inputParams = new HashMap<>();
		String isEnrolled = "0";
		String filter = "isEnrolled" + DBPUtilitiesConstants.EQUAL + "'" + isEnrolled+ "'";
		inputParams.put(DBPUtilitiesConstants.FILTER, filter);
		Result response = CommonUtils.callIntegrationService(dcRequest, inputParams, dcRequest.getHeaderMap(), serviceName,operationName, false);
		if (response != null) {
			JSONObject responseObj = new JSONObject(ResultToJSON.convert(response));
			 legacycustomers = responseObj.getJSONArray("legacycustomerV1_1");
		}
		}catch (DBPApplicationException e) {
			logger.debug("Exception occured while getting the legacy customers in BulkAutoEnrollRetailCustomer"+e.getMessage());
		}catch (Exception e) {
			logger.debug("Exception occured while getting the legacy customers in BulkAutoEnrollRetailCustomer"+e.getMessage());
		}
		return legacycustomers;
	}
	public Result enrollProcess1(String methodID, JSONObject customerObj, DataControllerRequest dcRequest, DataControllerResponse dcResponse){
		logger.debug("BulkAutoEnrollRetailCustomer enrollRetailCustomer inputMap:" + customerObj);
		String accountNumber = customerObj.optString("accountNumber");
		String accountName = customerObj.optString("accountName");
		String mobileNumber = customerObj.optString("mobileNumber");
		String email = customerObj.optString("email");
		Result res = new Result();
		if (StringUtils.isNotBlank(accountNumber) && StringUtils.isNotBlank(accountName) && StringUtils.isNotBlank(mobileNumber) && StringUtils.isNotBlank(email)) {
		try {
			Object[] inputArray= new Object[2];
			Map<String, Object> inputMap = new HashMap<String, Object>();
			inputMap.put("accountName", accountName);
			inputMap.put("accountNumber", accountNumber);
			inputMap.put("email", email);
			inputMap.put("mobileNumber", mobileNumber);
			inputMap.put("isMigrationFlow", "true");
			inputArray[1]=inputMap;
			dcRequest.addRequestParam_("legalEntityId", "NP0010001");
			InfinityUserManagementResource resource =
		            DBPAPIAbstractFactoryImpl.getResource(InfinityUserManagementResource.class);
			res  = resource.enrollRetailUserOperation("EnrollRetailUser", inputArray, dcRequest, dcResponse);
			logger.debug("HBL::BulkAutoEnrollRetailCustomer: enrollRetailCustomer: response:" + ResultToJSON.convert(res));
			}catch(ApplicationException e) {
				logger.error("Exception caught while enrollRetailCustomer ", e);
				res.addParam(new Param("dbpErrCode", e.getErrorCodeEnum().getErrorCodeAsString()));
				res.addParam(new Param("dbpErrMsg",  e.getErrorCodeEnum().getMessage()));
			}
			catch (Exception e) {
				logger.error("Exception caught while enrollRetailCustomer ", e);
				res.addParam(new Param("dbpErrCode", "500"));
				res.addParam(new Param("dbpErrMsg", e.getMessage()));
			}
		}else {
			res.addParam(new Param("dbpErrCode", ErrorCodeEnum.ERR_10801.getErrorCodeAsString()));
			res.addParam(new Param("dbpErrMsg",  ErrorCodeEnum.ERR_10801.getMessage()));
		}
		postProcess1(res, customerObj, dcRequest);
		return res;
	}
	
	public void postProcess1(Result enrollResult, JSONObject customerObj, DataControllerRequest dcRequest){
		String errmsg = null;
		String isEnrolled="2";
		String customerId=customerObj.optString("customerId");
		String infinityCustomerId = null;
		if(enrollResult!=null && !enrollResult.isEmpty()) {
			String dbpErrCode= enrollResult.getParamValueByName("dbpErrCode");
			String dbpErrMsg= enrollResult.getParamValueByName("dbpErrMsg");
			String isUserExists= enrollResult.getParamValueByName("isUserExists");
			String isUserEnrolled= enrollResult.getParamValueByName("isUserEnrolled");
			infinityCustomerId = enrollResult.getParamValueByName("infinityCustomerId");
			if(StringUtils.isBlank(dbpErrCode) && StringUtils.isBlank(dbpErrMsg)) {
				if(StringUtils.isNotBlank(isUserExists) && isUserExists.equalsIgnoreCase("false")) {
					errmsg="Customer Not Found";
				}
				else if(StringUtils.isNotBlank(isUserEnrolled) && isUserEnrolled.equalsIgnoreCase("true")) {
					errmsg="Customer Already Enrolled";
				}
				else if(StringUtils.isNotBlank(infinityCustomerId)){
					isEnrolled="1";
					errmsg = "SUCCESS";
				}
			}else {
				errmsg=dbpErrMsg;
			}
		}else {
			errmsg = "Something Went worng.";
		}
		int status= updateStaus1(isEnrolled, customerId, infinityCustomerId, errmsg, dcRequest);
		if(status==1) {
			if(isEnrolled.equalsIgnoreCase("1") ) 
				Process1SuccessCount++;
			else
				Process1FailureCount++;
		}
	}
	private int updateStaus1(String status, String customerId, String infinityCustomerId, String error, DataControllerRequest request) {
		HashMap<String, Object> inputParams = new HashMap<String, Object>();
		inputParams.put("customerId", customerId);
		inputParams.put("isEnrolled", status);
		inputParams.put("errorMessage", error);
		inputParams.put("infinityId", infinityCustomerId);
		String operationName = "dbxdb_legacycustomerV1_1_update";
		try {
			String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
			Result result = CommonUtils.callIntegrationService(request, inputParams, request.getHeaderMap(), serviceName, operationName, false);
			String updatedRecords=result.getParamValueByName("updatedRecords");
			if (StringUtils.isNotEmpty(updatedRecords)) {
				if (Integer.parseInt(updatedRecords) > 0) 
					return 1;
				 else
					return 0;
			}
		} catch (Exception e) {
			logger.debug("Exception occured in BulkAutoEnrollRetailCustomer"+e.getMessage());
		}
		return 0;
	}
	
	/*
	 * BulkAutoEnrollRetailCustomer2 Logic
	 */
	public Result process2(String methodID, Object[] inputArray, DataControllerRequest dcRequest, DataControllerResponse dcResponse){
		Result res = new Result();
		JSONArray legacyCustomers = preProcess2(dcRequest);
		if(legacyCustomers.length()>0) {
		for(int i= 0 ;i<maxLength; i++) {
			JSONObject customerObj = legacyCustomers.getJSONObject(i);
			res=enrollProcess2(methodID, customerObj, dcRequest, dcResponse);
		}
		}
		res.addParam(new Param("totalRecords", String.valueOf(legacyCustomers.length())));
		res.addParam(new Param("successCount", String.valueOf(Process2SuccessCount)));
		res.addParam(new Param("failureCount",  String.valueOf(Process2FailureCount)));
		logger.debug("BulkAutoEnrollRetailCustomer enrollRetailCustomer procesed result:" + ResultToJSON.convert(res));
		return res;
	}
	public JSONArray preProcess2(DataControllerRequest dcRequest){
		return getLegacyCustomer2(dcRequest);
	}
	public JSONArray getLegacyCustomer2( DataControllerRequest dcRequest) {
		JSONArray legacycustomers = new JSONArray();
		String operationName = "dbxdb_legacycustomerV2_get";
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		try {
		Map<String, Object> inputParams = new HashMap<>();
		String isEnrolled = "0";
		String filter = "isEnrolled" + DBPUtilitiesConstants.EQUAL + "'" + isEnrolled+ "'";
		inputParams.put(DBPUtilitiesConstants.FILTER, filter);
		Result response = CommonUtils.callIntegrationService(dcRequest, inputParams, dcRequest.getHeaderMap(), serviceName,operationName, false);
		if (response != null) {
			JSONObject responseObj = new JSONObject(ResultToJSON.convert(response));
			 legacycustomers = responseObj.getJSONArray("legacycustomerV2");
		}
		}catch (DBPApplicationException e) {
			logger.debug("Exception occured while getting the legacy customers in BulkAutoEnrollRetailCustomer"+e.getMessage());
		}catch (Exception e) {
			logger.debug("Exception occured while getting the legacy customers in BulkAutoEnrollRetailCustomer"+e.getMessage());
		}
		return legacycustomers;
	}
	public Result enrollProcess2(String methodID, JSONObject customerObj, DataControllerRequest dcRequest, DataControllerResponse dcResponse){
		logger.debug("BulkAutoEnrollRetailCustomer enrollRetailCustomer inputMap:" + customerObj);
		String accountNumber = customerObj.optString("accountNumber");
		String accountName = customerObj.optString("accountName");
		String mobileNumber = customerObj.optString("mobileNumber");
		String email = customerObj.optString("email");
		Result res = new Result();
		if (StringUtils.isNotBlank(accountNumber) && StringUtils.isNotBlank(accountName) && StringUtils.isNotBlank(mobileNumber) && StringUtils.isNotBlank(email)) {
		try {
			Object[] inputArray= new Object[2];
			Map<String, Object> inputMap = new HashMap<String, Object>();
			inputMap.put("accountName", accountName);
			inputMap.put("accountNumber", accountNumber);
			inputMap.put("email", email);
			inputMap.put("mobileNumber", mobileNumber);
			inputMap.put("isMigrationFlow", "true");
			inputArray[1]=inputMap;
			dcRequest.addRequestParam_("legalEntityId", "NP0010001");
			InfinityUserManagementResource resource =
		            DBPAPIAbstractFactoryImpl.getResource(InfinityUserManagementResource.class);
			res  = resource.enrollRetailUserOperation("EnrollRetailUser", inputArray, dcRequest, dcResponse);
			logger.debug("HBL::BulkAutoEnrollRetailCustomer: enrollRetailCustomer: response:" + ResultToJSON.convert(res));
			}catch(ApplicationException e) {
				logger.error("Exception caught while enrollRetailCustomer ", e);
				res.addParam(new Param("dbpErrCode", e.getErrorCodeEnum().getErrorCodeAsString()));
				res.addParam(new Param("dbpErrMsg",  e.getErrorCodeEnum().getMessage()));
			}
			catch (Exception e) {
				logger.error("Exception caught while enrollRetailCustomer ", e);
				res.addParam(new Param("dbpErrCode", "500"));
				res.addParam(new Param("dbpErrMsg", e.getMessage()));
			}
		}else {
			res.addParam(new Param("dbpErrCode", ErrorCodeEnum.ERR_10801.getErrorCodeAsString()));
			res.addParam(new Param("dbpErrMsg",  ErrorCodeEnum.ERR_10801.getMessage()));
		}
		postProcess2(res, customerObj, dcRequest);
		return res;
	}
	public void postProcess2(Result enrollResult, JSONObject customerObj, DataControllerRequest dcRequest){
		String errmsg = null;
		String isEnrolled="2";
		String customerId=customerObj.optString("customerId");
		String infinityCustomerId = null;
		if(enrollResult!=null && !enrollResult.isEmpty()) {
			String dbpErrCode= enrollResult.getParamValueByName("dbpErrCode");
			String dbpErrMsg= enrollResult.getParamValueByName("dbpErrMsg");
			String isUserExists= enrollResult.getParamValueByName("isUserExists");
			String isUserEnrolled= enrollResult.getParamValueByName("isUserEnrolled");
			infinityCustomerId = enrollResult.getParamValueByName("infinityCustomerId");
			if(StringUtils.isBlank(dbpErrCode) && StringUtils.isBlank(dbpErrMsg)) {
				if(StringUtils.isNotBlank(isUserExists) && isUserExists.equalsIgnoreCase("false")) {
					errmsg="Customer Not Found";
				}
				else if(StringUtils.isNotBlank(isUserEnrolled) && isUserEnrolled.equalsIgnoreCase("true")) {
					errmsg="Customer Already Enrolled";
				}
				else if(StringUtils.isNotBlank(infinityCustomerId)){
					isEnrolled="1";
					errmsg = "SUCCESS";
				}
			}else {
				errmsg=dbpErrMsg;
			}
		}else {
			errmsg = "Something Went worng.";
		}
		int status= updateStaus2(isEnrolled, customerId, infinityCustomerId, errmsg, dcRequest);
		if(status==1) {
			if(isEnrolled.equalsIgnoreCase("1") ) 
				Process2SuccessCount++;
			else
				Process2FailureCount++;
		}
	}
	private int updateStaus2(String status, String customerId, String infinityCustomerId, String error, DataControllerRequest request) {
		HashMap<String, Object> inputParams = new HashMap<String, Object>();
		inputParams.put("customerId", customerId);
		inputParams.put("isEnrolled", status);
		inputParams.put("errorMessage", error);
		inputParams.put("infinityId", infinityCustomerId);
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = "dbxdb_legacycustomerV2_update";
		try {
			Result result = CommonUtils.callIntegrationService(request, inputParams, request.getHeaderMap(), serviceName, operationName, false);
			String updatedRecords=result.getParamValueByName("updatedRecords");
			if (StringUtils.isNotEmpty(updatedRecords)) {
				if (Integer.parseInt(updatedRecords) > 0) 
					return 1;
				 else
					return 0;
			}
		} catch (Exception e) {
			logger.debug("Exception occured in BulkAutoEnrollRetailCustomer"+e.getMessage());
		}
		return 0;
	}
	/*
	 * BulkAutoEnrollRetailCustomer3 Logic
	 */
	public Result process3(String methodID, Object[] inputArray, DataControllerRequest dcRequest, DataControllerResponse dcResponse){
		Result res = new Result();
		JSONArray legacyCustomers = preProcess3(dcRequest);
		if(legacyCustomers.length()>0) {
		for(int i= 0 ;i<maxLength; i++) {
			JSONObject customerObj = legacyCustomers.getJSONObject(i);
			res=enrollProcess3(methodID, customerObj, dcRequest, dcResponse);
		}
		}
		res.addParam(new Param("totalRecords", String.valueOf(legacyCustomers.length())));
		res.addParam(new Param("successCount", String.valueOf(Process3SuccessCount)));
		res.addParam(new Param("failureCount",  String.valueOf(Process3FailureCount)));
		logger.debug("BulkAutoEnrollRetailCustomer enrollRetailCustomer procesed result:" + ResultToJSON.convert(res));
		return res;
	}
	public JSONArray preProcess3(DataControllerRequest dcRequest){
		return getLegacyCustomer3(dcRequest);
	}
	public JSONArray getLegacyCustomer3( DataControllerRequest dcRequest) {
		JSONArray legacycustomers = new JSONArray();
		String operationName = "dbxdb_legacycustomerV3_get";
		try {
		Map<String, Object> inputParams = new HashMap<>();
		String isEnrolled = "0";
		String filter = "isEnrolled" + DBPUtilitiesConstants.EQUAL + "'" + isEnrolled+ "'";
		inputParams.put(DBPUtilitiesConstants.FILTER, filter);
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		Result response = CommonUtils.callIntegrationService(dcRequest, inputParams, dcRequest.getHeaderMap(), serviceName,operationName, false);
		if (response != null) {
			JSONObject responseObj = new JSONObject(ResultToJSON.convert(response));
			 legacycustomers = responseObj.getJSONArray("legacycustomerV3");
		}
		}catch (DBPApplicationException e) {
			logger.debug("Exception occured while getting the legacy customers in BulkAutoEnrollRetailCustomer"+e.getMessage());
		}catch (Exception e) {
			logger.debug("Exception occured while getting the legacy customers in BulkAutoEnrollRetailCustomer"+e.getMessage());
		}
		return legacycustomers;
	}
	public Result enrollProcess3(String methodID, JSONObject customerObj, DataControllerRequest dcRequest, DataControllerResponse dcResponse){
		logger.debug("BulkAutoEnrollRetailCustomer enrollRetailCustomer inputMap:" + customerObj);
		String accountNumber = customerObj.optString("accountNumber");
		String accountName = customerObj.optString("accountName");
		String mobileNumber = customerObj.optString("mobileNumber");
		String email = customerObj.optString("email");
		Result res = new Result();
		if (StringUtils.isNotBlank(accountNumber) && StringUtils.isNotBlank(accountName) && StringUtils.isNotBlank(mobileNumber) && StringUtils.isNotBlank(email)) {
		try {
			Object[] inputArray= new Object[2];
			Map<String, Object> inputMap = new HashMap<String, Object>();
			inputMap.put("accountName", accountName);
			inputMap.put("accountNumber", accountNumber);
			inputMap.put("email", email);
			inputMap.put("mobileNumber", mobileNumber);
			inputMap.put("isMigrationFlow", "true");
			inputArray[1]=inputMap;
			dcRequest.addRequestParam_("legalEntityId", "NP0010001");
			InfinityUserManagementResource resource =
		            DBPAPIAbstractFactoryImpl.getResource(InfinityUserManagementResource.class);
			res  = resource.enrollRetailUserOperation("EnrollRetailUser", inputArray, dcRequest, dcResponse);
			logger.debug("HBL::BulkAutoEnrollRetailCustomer: enrollRetailCustomer: response:" + ResultToJSON.convert(res));
			}catch(ApplicationException e) {
				logger.error("Exception caught while enrollRetailCustomer ", e);
				res.addParam(new Param("dbpErrCode", e.getErrorCodeEnum().getErrorCodeAsString()));
				res.addParam(new Param("dbpErrMsg",  e.getErrorCodeEnum().getMessage()));
			}
			catch (Exception e) {
				logger.error("Exception caught while enrollRetailCustomer ", e);
				res.addParam(new Param("dbpErrCode", "500"));
				res.addParam(new Param("dbpErrMsg", e.getMessage()));
			}
		}else {
			res.addParam(new Param("dbpErrCode", ErrorCodeEnum.ERR_10801.getErrorCodeAsString()));
			res.addParam(new Param("dbpErrMsg",  ErrorCodeEnum.ERR_10801.getMessage()));
		}
		postProcess3(res, customerObj, dcRequest);
		return res;
	}
	public void postProcess3(Result enrollResult, JSONObject customerObj, DataControllerRequest dcRequest){
		String errmsg = null;
		String isEnrolled="2";
		String customerId=customerObj.optString("customerId");
		String infinityCustomerId = null;
		if(enrollResult!=null && !enrollResult.isEmpty()) {
			String dbpErrCode= enrollResult.getParamValueByName("dbpErrCode");
			String dbpErrMsg= enrollResult.getParamValueByName("dbpErrMsg");
			String isUserExists= enrollResult.getParamValueByName("isUserExists");
			String isUserEnrolled= enrollResult.getParamValueByName("isUserEnrolled");
			infinityCustomerId = enrollResult.getParamValueByName("infinityCustomerId");
			if(StringUtils.isBlank(dbpErrCode) && StringUtils.isBlank(dbpErrMsg)) {
				if(StringUtils.isNotBlank(isUserExists) && isUserExists.equalsIgnoreCase("false")) {
					errmsg="Customer Not Found";
				}
				else if(StringUtils.isNotBlank(isUserEnrolled) && isUserEnrolled.equalsIgnoreCase("true")) {
					errmsg="Customer Already Enrolled";
				}
				else if(StringUtils.isNotBlank(infinityCustomerId)){
					isEnrolled="1";
					errmsg = "SUCCESS";
				}
			}else {
				errmsg=dbpErrMsg;
			}
		}else {
			errmsg = "Something Went worng.";
		}
		int status= updateStaus3(isEnrolled, customerId, infinityCustomerId, errmsg, dcRequest);
		if(status==1) {
			if(isEnrolled.equalsIgnoreCase("1") ) 
				Process3SuccessCount++;
			else
				Process3FailureCount++;
		}
	}
	private int updateStaus3(String status, String customerId, String infinityCustomerId, String error, DataControllerRequest request) {
		HashMap<String, Object> inputParams = new HashMap<String, Object>();
		inputParams.put("customerId", customerId);
		inputParams.put("isEnrolled", status);
		inputParams.put("errorMessage", error);
		inputParams.put("infinityId", infinityCustomerId);
		String operationName = "dbxdb_legacycustomerV3_update";
		try {
			String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
			Result result = CommonUtils.callIntegrationService(request, inputParams, request.getHeaderMap(), serviceName, operationName, false);
			String updatedRecords=result.getParamValueByName("updatedRecords");
			if (StringUtils.isNotEmpty(updatedRecords)) {
				if (Integer.parseInt(updatedRecords) > 0) 
					return 1;
				 else
					return 0;
			}
		} catch (Exception e) {
			logger.debug("Exception occured in BulkAutoEnrollRetailCustomer"+e.getMessage());
		}
		return 0;
	}
	

}
