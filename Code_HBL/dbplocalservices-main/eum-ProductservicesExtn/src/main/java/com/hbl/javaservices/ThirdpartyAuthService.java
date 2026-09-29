package com.hbl.javaservices;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.dbp.core.constants.DBPConstants;
import com.infinity.dbx.temenos.accounts.AccountsConstants;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.BCrypt;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.kony.eum.dbputilities.customersecurityservices.PasswordHistoryManagement;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.temenos.dbx.eum.product.usermanagement.javaservice.CustomerGetByUserNameOperation;
import com.temenos.dbx.product.utils.InfinityConstants;

public class ThirdpartyAuthService implements JavaService2 {
	private static LoggerUtil logger = new LoggerUtil(ThirdpartyAuthService.class);

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		logger = new LoggerUtil(ThirdpartyAuthService.class);

		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		if (preProcess(inputParams, dcRequest, result)) {
			result = (Result) new CustomerGetByUserNameOperation().invoke(methodID, inputArray, dcRequest, dcResponse);
			if (StringUtils.isNotBlank(HelperMethods.getFieldValue(result, "CustomerType_id"))) {
				result = postProcess(inputParams, dcRequest, result, methodID, dcResponse, inputArray);
			} else {
				result.addParam(new Param("isUserAuthenticationSuccess", "" + "false", "String"));
			}
		}
		return result;
	}

	private boolean preProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result)
			throws HttpCallException {
		boolean status = true;

		logger.debug("InputParams in the beggining of preProcess : " + inputParams);

		String username = inputParams.get("userid");
		String password = inputParams.get("Password");
		logger.debug("username ****" + username);
		logger.debug("password ****" + password);

		StringBuilder sb = new StringBuilder();
		if (StringUtils.isNotBlank(password) && !"$password".equalsIgnoreCase(password)) {
			sb.append("UserName").append(DBPUtilitiesConstants.EQUAL).append("'").append(username).append("'");
		} else {
			ErrorCodeEnum.ERR_10083.setErrorCode(result);
			status = false;
		}

		inputParams.put("UserName", username);

		inputParams.put(DBPUtilitiesConstants.FILTER, sb.toString());

		logger.debug("InputParams from preProcess : " + inputParams);

		return status;
	}

	private Result postProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result,
			String methodID, DataControllerResponse dcResponse, Object[] inputArray) throws Exception {
		Result retVal = null;
		logger.debug("Input Params in the beggining of postProcess : " + inputParams);
		String password = inputParams.get("Password");
		Record user = result.getAllDatasets().get(0).getRecord(0);

		if (StringUtils.isNotBlank(password) && !"$password".equalsIgnoreCase(password)) {
			retVal = postProcessForUserName(inputParams, dcRequest, result, methodID, dcResponse, inputArray);
		} else {

		}
		return retVal;
	}

	private Result postProcessForUserName(Map<String, String> inputParams, DataControllerRequest dcRequest,
			Result result, String methodID, DataControllerResponse dcResponse, Object[] inputArray) throws Exception {
		Result retVal = new Result();
		PasswordHistoryManagement pm = new PasswordHistoryManagement(dcRequest, true);
		if (StringUtils.isNotBlank(pm.getDbpErrorCode())) {
			ErrorCodeEnum.ERR_10164.setErrorCode(retVal, pm.getDbpErrorCode(), pm.getDbpErrorMessage());
			return retVal;
		}

		String password = inputParams.get("Password");

		String prospectLogin = inputParams.get("prospect");
		String UserName = inputParams.get("UserName");

		if (StringUtils.isBlank(prospectLogin)) {
			prospectLogin = "false";
		}

		if (HelperMethods.hasRecords(result)) {

			Record user = result.getAllDatasets().get(0).getRecord(0);

			String dbPassword = HelperMethods.getFieldValue(user, "Password");
			if (Boolean.parseBoolean(prospectLogin) && dbPassword.equalsIgnoreCase(InfinityConstants.defaultPassword)) {
				ErrorCodeEnum.ERR_29032.setErrorCode(retVal);
				return retVal;
			}
			boolean isProspect = false;
			boolean isPasswordExpired = isPasswordExpired(dcRequest, user, pm, isProspect);

			if (!Boolean.parseBoolean(prospectLogin)
					&& user.getParamValueByName("CustomerType_id").equalsIgnoreCase("TYPE_ID_PROSPECT")) {
				ErrorCodeEnum.ERR_10092.setErrorCode(retVal);
			} else if (!isProspect && isPasswordExpired) {
				ErrorCodeEnum.ERR_10135.setErrorCode(retVal);
			} else if (isUserExpired(user)) {
				ErrorCodeEnum.ERR_10087.setErrorCode(retVal);
			} else if (isUserLocked(user, dcRequest, pm) && !tryAutoLockReset(user, dcRequest, pm)) {
				int x = pm.getAutoUnLockPeriod();
				ErrorCodeEnum.ERR_10088.setErrorCode(retVal,
						"Your profile is locked, it will be unlocked after " + x + " mins");
			} else if (isUserSuspended(user)) {
				ErrorCodeEnum.ERR_10089.setErrorCode(retVal);
			} else {

				if (validatePassword(dcRequest, result, password)) {

					retVal.addParam(new Param("isUserAuthenticationSuccess", "" + "true", "String"));
					Dataset customerDataset = getThirdpartyAuthFlag(dcRequest, UserName);
					String IsOlbAllowed = customerDataset.getRecord(0).getParamValueByName("IsOlbAllowed");
					String customerid = customerDataset.getRecord(0).getParamValueByName("id");
					if (IsOlbAllowed == "true") {
						//updateThirdpartyAuthCustomerTable(dcRequest, "0", UserName, customerid);
						logger.debug("Updated flag as 0");
						retVal.addParam(new Param("isThirdpartyAuthEnable", "" + "true", "String"));
					} else {
						//updateThirdpartyAuthCustomerTable(dcRequest, "1", UserName, customerid);
						logger.debug("Updated flag as 1");
						retVal.addParam(new Param("isThirdpartyAuthEnable", "" + "false", "String"));
					}

				} else {
					retVal.addParam(new Param("isUserAuthenticationSuccess", "" + "false", "String"));
				}

				if (isProspect && isPasswordExpired) {
					retVal.addParam(new Param("isProspectExpired", "true", "String"));
				}
				Param p = new Param(DBPConstants.FABRIC_HTTP_STATUS_CODE_KEY, "200", "int");
				retVal.addParam(p);
			}
		} else {
			Record record = new Record();
			record.setId(DBPUtilitiesConstants.USR_ATTR);
			retVal.addRecord(record);
			ErrorCodeEnum.ERR_10095.setErrorCode(retVal);
		}

		logger.debug("Response from postProcessForUserName : " + ResultToJSON.convert(retVal));
		return retVal;
	}

	private boolean isPasswordExpired(DataControllerRequest dcRequest, Record record, PasswordHistoryManagement pm,
			boolean isProspect) throws HttpCallException {
		String customerId = record.getParam("id").getValue();
		return pm.isPasswordExpired(dcRequest, customerId, isProspect);
	}

	private boolean isUserExpired(Record user) {

		String isEnrolled = HelperMethods.getFieldValue(user, "isEnrolled");
		String validDate = HelperMethods.getFieldValue(user, "ValidDate");
		if (("1".equals(isEnrolled) || "true".equalsIgnoreCase(isEnrolled))
				&& new Date().after(HelperMethods.getFormattedTimeStamp(validDate))) {
			logger.debug("Response from isUserExpired : " + true);
			return true;
		} else {
			logger.debug("Response from isUserExpired : " + false);
			return false;
		}

	}

	private boolean isUserLocked(Record user, DataControllerRequest dcRequest, PasswordHistoryManagement pm)
			throws HttpCallException {

		boolean isUserLocked = false;
		String lockcount = HelperMethods.getFieldValue(user, "lockCount");
		if (StringUtils.isNotBlank(lockcount)) {
			int count = Integer.parseInt(lockcount);
			isUserLocked = (count + 1) >= pm.getAccountLockoutThreshold();
		}

		logger.debug("Response from isUserLocked : " + isUserLocked);

		return isUserLocked;
	}

	private boolean isUserSuspended(Record user) throws HttpCallException {

		boolean isUserSuspended = false;

		String status_id = HelperMethods.getFieldValue(user, "Status_id");
		if (status_id.equalsIgnoreCase(InfinityConstants.SID_CUS_SUSPENDED)) {
			isUserSuspended = true;
		} else {
			isUserSuspended = false;
		}

		logger.debug("Response from isUserSuspended : " + isUserSuspended);

		return isUserSuspended;
	}

	private boolean tryAutoLockReset(Record user, DataControllerRequest dcRequest, PasswordHistoryManagement pm)
			throws HttpCallException {

		String lockedOn = HelperMethods.getFieldValue(user, "lockedOn");
		int autoUnlockPeriod = pm.getAutoUnLockPeriod();
		Date today = HelperMethods.getFormattedTimeStamp(HelperMethods.getCurrentTimeStamp());
		Calendar cal = Calendar.getInstance();
		cal.setTime(HelperMethods.getFormattedTimeStamp(lockedOn));
		cal.add(Calendar.MINUTE, autoUnlockPeriod);
		if (-1 != autoUnlockPeriod && today.after(cal.getTime())) {
			logger.debug("Response from tryAutoLockReset : " + true);
			return true;
		}
		logger.debug("Response from tryAutoLockReset : " + false);
		return false;

	}

	private Boolean validatePassword(DataControllerRequest dcRequest, Result result, String password)
			throws HttpCallException {
		String dbPassword = HelperMethods.getFieldValue(result, "Password");
		boolean isPasswordValid = false;
		try {
			isPasswordValid = BCrypt.checkpw(password, dbPassword);
		} catch (Exception e) {
		}
		logger.debug("Response from isPasswordValid  : " + isPasswordValid);
		return isPasswordValid;
	}

	private int updateThirdpartyAuthCustomerTable(DataControllerRequest request, String thirdpartyAuthFlag,
			String UserName, String customerid) throws Exception {
		HashMap<String, Object> inputParams = new HashMap<String, Object>();
		HashMap<String, Object> serviceHeaders = new HashMap<String, Object>();
		// IsOlbAllowed Flag - 1 as true indicates thirdpartyAuth Enabled
		// IsOlbAllowed Flag - 0 as false indicates thirdpartyAuth Disabled
		inputParams.put("IsOlbAllowed", thirdpartyAuthFlag);
		inputParams.put("UserName", UserName);
		inputParams.put("id", customerid);
		String serviceName = TemenosConstants.SERVICE_BACKEND_CERTIFICATE;
		String operationName = "dbxdb_customer_update";
		Result result = CommonUtils.callIntegrationService(request, inputParams, serviceHeaders, serviceName,
				operationName, false);
		logger.debug("Post updateThirdpartyAuthCustomerTable");
		String errMessage = result.getParamValueByName(AccountsConstants.PARAM_ERROR_MESSAGE);
		if (StringUtils.isNotBlank(errMessage)) {
			logger.error("Couldn't create entry in dbxDb accounts Table due to : " + errMessage);
			return 0;
		} else if (StringUtils.isNotEmpty(result.getParamValueByName("updatedRecords"))) {
			try {
				if (Integer.parseInt(result.getParamValueByName("updatedRecords")) > 0)
					return 1;
			} catch (Exception e) {
				logger.debug("Couldn't Parse updated records Integer from String");
				return 1;
			}
		}
		return 0;

	}

	private Dataset getThirdpartyAuthFlag(DataControllerRequest request, String UserName) {

		String IsOlbAllowed = "";
		String userName = "";
		String customerid = "";
		Dataset customerDataset = new Dataset();
		try {

			String filter = CommonUtils.buildOdataCondition(TemenosConstants.PARAM_USERNAME, Constants.EQUAL, UserName);
			HashMap<String, Object> svcHeaders = new HashMap<String, Object>();
			HashMap<String, Object> svcParams = new HashMap<String, Object>();

			svcParams.put(Constants.PARAM_DOLLAR_FILTER, filter);
			Result result = CommonUtils.callIntegrationService(request, svcParams, svcHeaders,
					Constants.DBX_DB_SERVICE_NAME, TemenosConstants.OP_CUSTOMER_GET, false);
			customerDataset = result.getDatasetById(TemenosConstants.DS_CUSTOMER);
			if (null != customerDataset) {

				IsOlbAllowed = customerDataset.getRecord(0).getParamValueByName("IsOlbAllowed");
				userName = customerDataset.getRecord(0).getParamValueByName(TemenosConstants.PARAM_USERNAME);
				customerid = customerDataset.getRecord(0).getParamValueByName("id");
			} else {
				logger.debug("Else getThirdpartyAuthFlag:");
			}

			logger.debug("getThirdpartyAuthFlag:" + IsOlbAllowed);
			logger.debug("getThirdpartyAuthFlag userName:" + userName);
			logger.debug("getThirdpartyAuthFlag id:" + customerid);

		} catch (Exception e) {

			logger.error("Error while retrieving CustomerType_id for Customer " + UserName);
		}
		return customerDataset;

	}

}