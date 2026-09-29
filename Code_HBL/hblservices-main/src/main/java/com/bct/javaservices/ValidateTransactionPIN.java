package com.bct.javaservices;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import com.bct.utilities.Utils;

import com.bct.custom.constants.HBLURLConstants;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.infinity.dbx.temenos.accounts.AccountsConstants;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.mfa.LoginMFAUtil;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.constants.ServiceId;

public class ValidateTransactionPIN implements JavaService2 {
	private static final Logger LOG = LogManager.getLogger(ValidateTransactionPIN.class);

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Result result = new Result();
		try {
			String UserName = request.getParameter("userName");
			String customerId = getCustomerIDFromUsername(request, UserName);
			String pin = request.getParameter("pin");
			String pinFromCustomerTable = getTransactionPin(request, UserName);
			LOG.debug("Pin from pinFromCustomerTable:" + pinFromCustomerTable);
			
			String hashPin = Utils.hashPin(pin);
			LOG.debug("Hash pin in ValidateTransactionPIN:" + pinFromCustomerTable);
			
			if (hashPin.equalsIgnoreCase(pinFromCustomerTable)) {
				result.setParam(new Param("isPinValid", "true"));
				/*
				 * Update invalidattempts value to '0' if record exists in transactionPin table
				 */
				if (getPinInvalidAttempt(customerId) == "-1") {
					// Insert record in transactionpin table
					InvalidPinAttemptinDB(request, customerId, "0");
				} else {
					// reset invalidattempts to '0'
					updatePinInvalidAttempt(request, "0", UserName, customerId);
				}
			} else {
				int maxFailedAttempts = Integer.parseInt(getPinMaxFailedAttempts());
				int invalidAttemptCount = 0;
				String remaining_attempts = "0";
				String lockeUser = "";
				String logoutUser = "";
				LOG.debug("invalidAttemptCount Count ##" + getPinInvalidAttempt(customerId));

				if (getPinInvalidAttempt(customerId).equalsIgnoreCase("-1")) {
					// Insert record in transactionpin table
					LOG.debug("Inside if ##");
					InvalidPinAttemptinDB(request, customerId, "1");
					invalidAttemptCount = Integer.parseInt(getPinInvalidAttempt(customerId));
				} else {
					// reset invalidattempts to '0'
					invalidAttemptCount = Integer.parseInt(getPinInvalidAttempt(customerId));
					updatePinInvalidAttempt(request, (invalidAttemptCount + 1) + "", UserName, customerId);
					invalidAttemptCount = Integer.parseInt(getPinInvalidAttempt(customerId));
				}

				LOG.debug("maxFailedAttempts #:" + maxFailedAttempts);
				LOG.debug("invalidAttemptCount #:" + invalidAttemptCount);
				LOG.debug("failedAttempts:" + (maxFailedAttempts - invalidAttemptCount));
				lockeUser = getUserLockdetails();
				logoutUser = getUserLockOutdetails();
				Record MFAAttributes = new Record();
				MFAAttributes.setId("MFAAttributes");
				MFAAttributes.addParam(new Param("maxFailedAttemptsAllowed", maxFailedAttempts + "", "String"));
				MFAAttributes.addParam(
						new Param("remainingFailedAttempts", maxFailedAttempts - (invalidAttemptCount) + "", "String"));
				remaining_attempts = maxFailedAttempts - (invalidAttemptCount) + "";
				MFAAttributes.addParam(new Param("failedAttempts", invalidAttemptCount + "", "String"));

				if (invalidAttemptCount == maxFailedAttempts) {
					MFAAttributes.addParam(new Param("lockUser", lockeUser));
					MFAAttributes.addParam(new Param("logoutUser", logoutUser));
					if (getUserLockdetails().equalsIgnoreCase("true")) {
						LoginMFAUtil mfaUtil = new LoginMFAUtil(request);
						String lockoutTime = mfaUtil.getLockoutTime();
						LOG.debug("lockoutTime #:" + lockoutTime);
						MFAAttributes.addParam(new Param("lockoutTime", lockoutTime));
						// service call for locking the user
						if (lockeUser.equalsIgnoreCase("true")) {
							Map<String, String> input = new HashMap<>();
							input.put("id", customerId);

							if (request != null) {
								input.put("lockCount", (maxFailedAttempts + 1) + "");
								input.put("lockedOn", HelperMethods.getCurrentTimeStamp());
								HelperMethods.callApiAsync(request, input, HelperMethods.getHeaders(request),
										URLConstants.CUSTOMER_UPDATE);
							}
							// mfaUtil.shouldLockUser();
						}
					}
				}
				result.addRecord(MFAAttributes);

				if (remaining_attempts.equalsIgnoreCase("0") && logoutUser.equalsIgnoreCase("true")) {
					// reset remaining attempts to zero as logoutuser is true and remaining attempts
					// reached to zero
					updatePinInvalidAttempt(request, "0", UserName, customerId);
				}

				result.setParam(new Param("isPinValid", "false"));
			}
			result.setParam(new Param("opstatus", "0"));
			result.setParam(new Param("httpStatusCode", "200"));
		} catch (Exception e) {
			LOG.error("Exception occured in ValidateTransactionPIN:::" + e.getMessage(), e);
			ErrorCodeEnum.ERR_10021.setErrorCode(result);
			result.addParam(new Param("dbpErrMsg", e.getLocalizedMessage()));
			result.addParam(new Param("success", "false"));
		}
		return result;
	}

	private String getTransactionPin(DataControllerRequest request, String UserName) {
		String oldPin = "";
		try {
			String filter = CommonUtils.buildOdataCondition(TemenosConstants.PARAM_USERNAME, Constants.EQUAL, UserName);
			HashMap<String, Object> svcHeaders = new HashMap<String, Object>();
			HashMap<String, Object> svcParams = new HashMap<String, Object>();

			svcParams.put(Constants.PARAM_DOLLAR_FILTER, filter);
			Result result = CommonUtils.callIntegrationService(request, svcParams, svcHeaders,
					Constants.DBX_DB_SERVICE_NAME, TemenosConstants.OP_CUSTOMER_GET, false);
			Dataset customerDataset = result.getDatasetById(TemenosConstants.DS_CUSTOMER);
			if (null != customerDataset) {
				oldPin = customerDataset.getRecord(0).getParamValueByName("Pin");
			} else {
				LOG.debug("Else getTransactionPin:");
			}
			LOG.debug("getTransactionPin id:" + oldPin);
		} catch (Exception e) {
			LOG.error("Error while retrieving CustomerType_id for Customer " + UserName);
		}
		return oldPin;
	}

	private String getPinMaxFailedAttempts() {
		String maxAttempts = "0";
		JSONArray configurations = new JSONArray();

		Map<String, Object> inputParams = new HashMap<String, Object>();
		inputParams.put(DBPUtilitiesConstants.FILTER,
				"MFA_id" + DBPUtilitiesConstants.EQUAL + "TRANSACTION_PIN" + DBPUtilitiesConstants.AND + "MFAKey_id"
						+ DBPUtilitiesConstants.EQUAL + "MAX_FAILED_ATTEMPTS_ALLOWED");

		try {
			String response = DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.DBPRBLOCALSERVICEDB)
					.withObjectId(null).withOperationId("dbxdb_mfaconfigurations_get")
					.withRequestParameters(inputParams).build().getResponse();

			JSONObject responseJSON = new JSONObject(response);
			configurations = responseJSON.getJSONArray("mfaconfigurations");

			LOG.debug("configurations" + configurations);
			JSONObject conf = configurations.getJSONObject(0);
			maxAttempts = conf.getString("value");
			LOG.debug("MAX_FAILED_ATTEMPTS_ALLOWED VALUE:::" + maxAttempts);

		} catch (Exception e) {
			LOG.error("Exception caught while getPinMaxFailedAttempts", e);
		}
		return maxAttempts;
	}

	private String getUserLockdetails() {
		String maxAttempts = "0";
		JSONArray configurations = new JSONArray();

		Map<String, Object> inputParams = new HashMap<String, Object>();
		inputParams.put(DBPUtilitiesConstants.FILTER, "MFA_id" + DBPUtilitiesConstants.EQUAL + "TRANSACTION_PIN"
				+ DBPUtilitiesConstants.AND + "MFAKey_id" + DBPUtilitiesConstants.EQUAL + "LOCK_USER");

		try {
			String response = DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.DBPRBLOCALSERVICEDB)
					.withObjectId(null).withOperationId("dbxdb_mfaconfigurations_get")
					.withRequestParameters(inputParams).build().getResponse();

			JSONObject responseJSON = new JSONObject(response);
			configurations = responseJSON.getJSONArray("mfaconfigurations");

			LOG.debug("configurations" + configurations);
			JSONObject conf = configurations.getJSONObject(0);
			maxAttempts = conf.getString("value");
			LOG.debug("LOCK_USER VALUE:::" + maxAttempts);

		} catch (Exception e) {
			LOG.error("Exception caught while getUserLockdetails", e);
		}
		return maxAttempts;
	}

	private String getUserLockOutdetails() {
		String maxAttempts = "0";
		JSONArray configurations = new JSONArray();

		Map<String, Object> inputParams = new HashMap<String, Object>();
		inputParams.put(DBPUtilitiesConstants.FILTER, "MFA_id" + DBPUtilitiesConstants.EQUAL + "TRANSACTION_PIN"
				+ DBPUtilitiesConstants.AND + "MFAKey_id" + DBPUtilitiesConstants.EQUAL + "LOGOUT_USER");

		try {
			String response = DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.DBPRBLOCALSERVICEDB)
					.withObjectId(null).withOperationId("dbxdb_mfaconfigurations_get")
					.withRequestParameters(inputParams).build().getResponse();

			JSONObject responseJSON = new JSONObject(response);
			configurations = responseJSON.getJSONArray("mfaconfigurations");

			LOG.debug("configurations" + configurations);
			JSONObject conf = configurations.getJSONObject(0);
			maxAttempts = conf.getString("value");
			LOG.debug("LOGOUT_USER VALUE:::" + maxAttempts);

		} catch (Exception e) {
			LOG.error("Exception caught while getUserLockOutdetails", e);
		}
		return maxAttempts;
	}

	private String getPinInvalidAttempt(String customerid) {
		String invalidAttempts = "0";
		JSONArray transactionpin = new JSONArray();

		LOG.debug("customerid #" + customerid);
		Map<String, Object> inputParams = new HashMap<String, Object>();
		inputParams.put(DBPUtilitiesConstants.FILTER, "Customer_id" + DBPUtilitiesConstants.EQUAL + customerid);

		try {
			String response = DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.DBPRBLOCALSERVICEDB)
					.withObjectId(null).withOperationId("dbxdb_transactionpin_get").withRequestParameters(inputParams)
					.build().getResponse();

			LOG.debug("transactionpin response #" + response);

			JSONObject responseJSON = new JSONObject(response);
			transactionpin = responseJSON.getJSONArray("transactionpin");

			LOG.debug("transactionpin length" + transactionpin.length());
			int length = transactionpin.length();
			if (length == 0) {
				return "-1";
			}
			JSONObject transpin = transactionpin.getJSONObject(0);
			invalidAttempts = transpin.getString("InvalidAttempt");
			LOG.debug("InvalidAttempt VALUE:::" + invalidAttempts);

		} catch (Exception e) {
			LOG.error("Exception caught while getPinInvalidAttempt", e);
		}
		return invalidAttempts;
	}

	private String getPinId(String customerid) {
		String transactionPin_id = "0";
		JSONArray transactionpin = new JSONArray();

		Map<String, Object> inputParams = new HashMap<String, Object>();
		inputParams.put(DBPUtilitiesConstants.FILTER, "Customer_id" + DBPUtilitiesConstants.EQUAL + customerid);

		try {
			String response = DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.DBPRBLOCALSERVICEDB)
					.withObjectId(null).withOperationId("dbxdb_transactionpin_get").withRequestParameters(inputParams)
					.build().getResponse();

			JSONObject responseJSON = new JSONObject(response);
			transactionpin = responseJSON.getJSONArray("transactionpin");

			LOG.debug("transactionpinID" + transactionpin);
			JSONObject transpin = transactionpin.getJSONObject(0);
			transactionPin_id = transpin.getString("id");
			LOG.debug("getPinId VALUE:::" + transactionPin_id);

		} catch (Exception e) {
			LOG.error("Exception caught while getPinId", e);
		}
		return transactionPin_id;
	}

	private String getCustomerIDFromUsername(DataControllerRequest request, String UserName) {

		String customerid = "";
		try {

			String filter = CommonUtils.buildOdataCondition(TemenosConstants.PARAM_USERNAME, Constants.EQUAL, UserName);
			HashMap<String, Object> svcHeaders = new HashMap<String, Object>();
			HashMap<String, Object> svcParams = new HashMap<String, Object>();

			svcParams.put(Constants.PARAM_DOLLAR_FILTER, filter);
			Result result = CommonUtils.callIntegrationService(request, svcParams, svcHeaders,
					Constants.DBX_DB_SERVICE_NAME, TemenosConstants.OP_CUSTOMER_GET, false);
			Dataset customerDataset = result.getDatasetById(TemenosConstants.DS_CUSTOMER);
			if (null != customerDataset) {
				customerid = customerDataset.getRecord(0).getParamValueByName("id");
			} else {
				LOG.debug("Else getThirdpartyAuthFlag:");
			}
			LOG.debug("getThirdpartyAuthFlag id:" + customerid);
		} catch (Exception e) {
			LOG.error("Error while retrieving CustomerType_id for Customer " + UserName);
		}
		return customerid;

	}

	private int updatePinInvalidAttempt(DataControllerRequest request, String invalidattemptcount, String UserName,
			String customerid) throws Exception {
		HashMap<String, Object> inputParams = new HashMap<String, Object>();
		HashMap<String, Object> serviceHeaders = new HashMap<String, Object>();

		String transactionPin_id = getPinId(customerid);
		LOG.debug("updatePinInvalidAttempt transactionPin_id" + transactionPin_id);

		inputParams.put("InvalidAttempt", invalidattemptcount);
		inputParams.put("id", transactionPin_id);
		String serviceName = Constants.DBX_DB_SERVICE_NAME;
		String operationName = "dbxdb_transactionpin_update";
		Result result = CommonUtils.callIntegrationService(request, inputParams, serviceHeaders, serviceName,
				operationName, false);
		LOG.debug("Post updatePinInvalidAttempt");
		String errMessage = result.getParamValueByName(AccountsConstants.PARAM_ERROR_MESSAGE);
		if (StringUtils.isNotBlank(errMessage)) {
			LOG.error("Couldn't create entry in dbxDb transactionpin Table due to : " + errMessage);
			return 0;
		} else if (StringUtils.isNotEmpty(result.getParamValueByName("updatedRecords"))) {
			try {
				if (Integer.parseInt(result.getParamValueByName("updatedRecords")) > 0) {
					return 1;
				} else
					return 0;
			} catch (Exception e) {
				LOG.debug("Couldn't Parse updated records Integer from String");
				return 1;
			}
		}
		return 0;
	}

	public static boolean InvalidPinAttemptinDB(DataControllerRequest dcRequest, String customerId,
			String InvalidAttemptCount) throws HttpCallException {
		boolean isinvalidattemptEntryMade = false;
		String id = UUID.randomUUID().toString();

		LOG.debug("customerId Value #: " + customerId);
		LOG.debug("ID Value : " + id);
		LOG.debug("invalidPinAttemptinDB at the end of makePINEntry :");
		Map<String, Object> inputParams = new HashMap<>();
		inputParams.put("id", id);
		inputParams.put("Customer_id", customerId);
		inputParams.put("InvalidAttempt", InvalidAttemptCount);
		inputParams.put("createdby", customerId);

		LOG.debug("BCT::InvalidPinAttemptinDB: inputParams:" + inputParams.toString());
		try {
			String dbResponse = DBPServiceExecutorBuilder.builder()
					.withOperationId(HBLURLConstants.TRANSACTIONPIN_OPERATION).withRequestParameters(inputParams)
					.withServiceId(HBLURLConstants.TRANSACTIONPIN_SERVICE).withRequestHeaders(dcRequest.getHeaderMap())
					.build().getResponse();
			LOG.debug("BCT::InvalidPinAttemptinDB: response:" + dbResponse);
			JSONObject responseJSON = new JSONObject(dbResponse);
			if (responseJSON.has("errmsg")) {
				isinvalidattemptEntryMade = false;
			} else {
				isinvalidattemptEntryMade = true;
			}
		} catch (Exception e) {
			LOG.debug("Couldn't create InvalidPinAttemptinDB");
			return false;
		}

		return isinvalidattemptEntryMade;
	}
}
