package com.hbl.javaservices;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.error.DBPApplicationException;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
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

public class BulkAutoEnrollRetailCustomerSystem2 implements JavaService2{
	long Process1SuccessCount = 0;
	long Process1FailureCount = 0;
	private final int maxLength=20;
	LoggerUtil logger = new LoggerUtil(BulkAutoEnrollRetailCustomerSystem2.class);
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest, DataControllerResponse dcResponse)
			throws Exception {
		Result result = new Result();
		if(methodID.equalsIgnoreCase("BulkAutoEnrollRetailCustomerSystem2")) {
			Process1FailureCount =0;
			Process1SuccessCount =0;
			return process1(methodID, inputArray, dcRequest, dcResponse);
		}
		return result;
	}
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
		logger.debug("BulkAutoEnrollRetailCustomerSystem2 enrollRetailCustomer procesed result:" + ResultToJSON.convert(res));
		return res;
	}
	public JSONArray preProcess1(DataControllerRequest dcRequest){
		return getLegacyCustomer1(dcRequest);
	}
	public JSONArray getLegacyCustomer1( DataControllerRequest dcRequest) {
		JSONArray legacycustomers = new JSONArray();
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = "dbxdb_legacycustomerSystem2_get";
		try {
		Map<String, Object> inputParams = new HashMap<>();
		String isEnrolled = "0";
		String filter = "isEnrolled" + DBPUtilitiesConstants.EQUAL + "'" + isEnrolled+ "'";
		inputParams.put(DBPUtilitiesConstants.FILTER, filter);
		Result response = CommonUtils.callIntegrationService(dcRequest, inputParams, dcRequest.getHeaderMap(), serviceName,operationName, false);
		if (response != null) {
			JSONObject responseObj = new JSONObject(ResultToJSON.convert(response));
			 legacycustomers = responseObj.getJSONArray("legacycustomerSystem2");
		}
		}catch (DBPApplicationException e) {
			logger.debug("Exception occured while getting the legacy customers in BulkAutoEnrollRetailCustomerSystem2"+e.getMessage());
		}catch (Exception e) {
			logger.debug("Exception occured while getting the legacy customers in BulkAutoEnrollRetailCustomerSystem2"+e.getMessage());
		}
		return legacycustomers;
	}
	public Result enrollProcess1(String methodID, JSONObject customerObj, DataControllerRequest dcRequest, DataControllerResponse dcResponse){
		logger.debug("BulkAutoEnrollRetailCustomerSystem2 enrollRetailCustomer inputMap:" + customerObj);
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
			logger.debug("HBL::BulkAutoEnrollRetailCustomerSystem2: enrollRetailCustomer: response:" + ResultToJSON.convert(res));
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
		String operationName = "dbxdb_legacycustomerSystem2_update";
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
			logger.debug("Exception occured in BulkAutoEnrollRetailCustomerSystem2"+e.getMessage());
		}
		return 0;
	}

}
