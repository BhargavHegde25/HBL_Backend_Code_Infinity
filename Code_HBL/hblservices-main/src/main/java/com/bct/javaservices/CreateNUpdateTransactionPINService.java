package com.bct.javaservices;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.infinity.dbx.temenos.accounts.AccountsConstants;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.bct.utilities.Utils;

public class CreateNUpdateTransactionPINService implements JavaService2 {
	private static final Logger LOG = LogManager.getLogger(CreateNUpdateTransactionPINService.class);

	@SuppressWarnings("deprecation")
	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Result result = new Result();
		try {
			String UserName = request.getParameter("userName");
			String Pin = request.getParameter("Pin");
			String OldPin = request.getParameter("OldPin");
			String FlowType = request.getParameter("FlowType");
			LOG.debug("OldPin from request:" + OldPin);
			LOG.debug("FlowType from request:" + FlowType);
			try {
				if (FlowType.equalsIgnoreCase("CREATE")) {
					// Create flow, so old pin validation not required
					String customerId = getCustomerIDFromUsername(request, UserName);
					LOG.debug("Customer ID:" + customerId);
					// Integer flagUpdate = updateTransactionPin(request, Pin, UserName,
					// customerId);
					//if (checkPinFiveEntries(request, Pin, UserName, customerId).equalsIgnoreCase("true")) {
						Integer flagUpdate = updateTransactionPin(request, Pin, UserName, customerId);
						if (flagUpdate == 1) {
							result.setParam(new Param("isTransactionPinSetupSuccess", "true"));
							result.setParam(new Param("pinHistoryEntrySuccess", "true"));
						} else {
							result.setParam(new Param("isTransactionPinSetupSuccess", "false"));
							result.setParam(new Param("pinHistoryEntrySuccess", "true"));
						}
				//	} else {
				//		result.setParam(new Param("pinHistoryEntrySuccess", "false"));
				//	}

					result.setParam(new Param("opstatus", "0"));
					result.setParam(new Param("httpStatusCode", "200"));

				} else {
					// Edit flow, so old pin validation is required
					
					//compare both new and old pin for same condition
					if (Pin.equalsIgnoreCase(OldPin)) {
						result.setParam(new Param("ErrMsg",
								"The old PIN and the new PIN cannot be the same"));
						result.setParam(new Param("opstatus", "0"));
						result.setParam(new Param("httpStatusCode", "200"));
						return result;
					}
					
					String oldPinFromCustomerTable = getTransactionPin(request, UserName);
					LOG.debug("OldPin from oldPinFromCustomerTable:" + oldPinFromCustomerTable);
					
					String hashOldPin = Utils.hashPin(OldPin);
					LOG.debug("hashOldPin: ##"+ hashOldPin);
					
					
					if (hashOldPin.equalsIgnoreCase(oldPinFromCustomerTable)) {
						String customerId = getCustomerIDFromUsername(request, UserName);
						LOG.debug("Customer ID:" + customerId);
//					Integer flagUpdate = updateTransactionPin(request, Pin, UserName, customerId);
						//if (checkPinFiveEntries(request, Pin, UserName, customerId).equalsIgnoreCase("true")) {
							Integer flagUpdate = updateTransactionPin(request, Pin, UserName, customerId);
							if (flagUpdate == 1) {
								result.setParam(new Param("isTransactionPinSetupSuccess", "true"));
								result.setParam(new Param("pinHistoryEntrySuccess", "true"));
							} else {
								result.setParam(new Param("isTransactionPinSetupSuccess", "false"));
								result.setParam(new Param("pinHistoryEntrySuccess", "true"));
							}
						//} else {
						//	result.setParam(new Param("pinHistoryEntrySuccess", "false"));
						//}

						result.setParam(new Param("opstatus", "0"));
						result.setParam(new Param("httpStatusCode", "200"));

					} else {
						LOG.debug("In else oldPinFromCustomerTable");
						result.setParam(new Param("ErrMsg",
								"Current Transaction Pin not matching with the records. Please try entering a valid"));
						result.setParam(new Param("opstatus", "0"));
						result.setParam(new Param("httpStatusCode", "200"));
					}
				}
			} catch (Exception e) {
				result.setParam(new Param("ErrMsg", "Transaction Pin Update failed"));
				result.setParam(new Param("opstatus", "0"));
				result.setParam(new Param("httpStatusCode", "200"));
				LOG.debug("CreateNUpdateTransactionPINService Failed: " + e);
			}
		} catch (Exception e) {
			LOG.error("Exception occured in CreateNUpdateTransactionPINService:::" + e.getMessage(), e);
			ErrorCodeEnum.ERR_10021.setErrorCode(result);
			result.addParam(new Param("dbpErrMsg", e.getLocalizedMessage()));
			result.addParam(new Param("success", "false"));
		}
		return result;
	}

	private int updateTransactionPin(DataControllerRequest request, String Pin, String UserName, String customerid)
			throws Exception {
		HashMap<String, Object> inputParams = new HashMap<String, Object>();
		HashMap<String, Object> serviceHeaders = new HashMap<String, Object>();

		/**
		 * Pin value not null then Transaction pin is set against the customer Pin value
		 * null then Transaction Pin is not set against the customer
		 */
		String hashPin = Utils.hashPin(Pin);
		LOG.debug("hashPin: ##"+ hashPin);
		inputParams.put("Pin", hashPin);
		inputParams.put("UserName", UserName);
		inputParams.put("id", customerid);
		String serviceName = TemenosConstants.SERVICE_BACKEND_CERTIFICATE;
		String operationName = "dbxdb_customer_update";
		Result result = CommonUtils.callIntegrationService(request, inputParams, serviceHeaders, serviceName,
				operationName, false);
		LOG.debug("Post updateTransactionPin");
		String errMessage = result.getParamValueByName(AccountsConstants.PARAM_ERROR_MESSAGE);
		if (StringUtils.isNotBlank(errMessage)) {
			LOG.error("Couldn't create entry in dbxDb accounts Table due to : " + errMessage);
			return 0;
		} else if (StringUtils.isNotEmpty(result.getParamValueByName("updatedRecords"))) {
			try {
				if (Integer.parseInt(result.getParamValueByName("updatedRecords")) > 0)
					return 1;
				else
					return 0;
			} catch (Exception e) {
				LOG.debug("Couldn't Parse updated records Integer from String");
				return 1;
			}
		}
		return 0;

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
				LOG.debug("Else getCustomerIDFromUsername:");
			}
			LOG.debug("getCustomerIDFromUsername id:" + customerid);
		} catch (Exception e) {
			LOG.error("Error while retrieving CustomerType_id for Customer " + UserName);
		}
		return customerid;
	}

	private String checkPinFiveEntries(DataControllerRequest request, String Pin, String UserName, String customerid)
			throws Exception {

		HashMap<String, Object> headerParams = new HashMap<String, Object>();
		HashMap<String, Object> inputParams = new HashMap<String, Object>();
		String hashPin = Utils.hashPin(Pin);
		LOG.debug("hashPin: ##"+ hashPin);
		inputParams.put("pin", hashPin);
		inputParams.put("userName", UserName);

		Result result = CommonUtils.callIntegrationService(request, inputParams, headerParams, "TransactionPIN",
				"pinHistoryEntry", false);
		LOG.debug("Result***" + ResultToJSON.convert(result));
		LOG.debug("Result of pinHistoryEntrySuccess***" + result.getParamValueByName("pinHistoryEntrySuccess"));

		return result.getParamValueByName("pinHistoryEntrySuccess");
	}
}
