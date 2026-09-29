package com.bct.javaservices;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.infinity.dbx.temenos.accounts.AccountsConstants;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.Param;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;

public class ResetCustomerDefaultAccount implements JavaService2 {
	private static final Logger LOG = LogManager.getLogger(ResetCustomerDefaultAccount.class);

	@SuppressWarnings("deprecation")
	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		String userName = request.getParameter("userName");
		String newDefaultAcc = request.getParameter("defaultACC");
		String customerId = getCustomerIDFromUsername(request, userName);
		String currentDefaultAcc = getCustomerDefaultAcc(customerId);
		//String currentDefaultAcc = "105538";
		LOG.debug("userName:" + userName);
		LOG.debug("customerId:" + customerId);
		LOG.debug("newDefaultAcc**:" + newDefaultAcc);
		LOG.debug("currentDefaultAcc**:" + currentDefaultAcc);
		String idOfExistAcc = getIdFromCustomerIdNew(customerId, currentDefaultAcc);
		String idOfNewAcc = getIdFromCustomerIdNew(customerId, newDefaultAcc);
		LOG.debug("idOfExistAcc***:" + idOfExistAcc);
		LOG.debug("idOfNewAcc***:" + idOfNewAcc);
		Result result = new Result();
		try {
			Integer oldAccUpdateStatus = updateExistCustomerDefaultAcc(request, currentDefaultAcc, customerId,idOfExistAcc);
			if (oldAccUpdateStatus == 1) {
				Integer newAccUpdateStatus = updateNewCustomerDefaultAcc(request, newDefaultAcc, customerId,idOfNewAcc);
				if (newAccUpdateStatus == 1) {
					result.setParam(new Param("isDefaultAccNewUpdated", "true"));
					result.setParam(new Param("isDefaultAccOldUpdated", "true"));
				} else {
					result.setParam(new Param("isDefaultAccNewUpdated", "false"));
					result.setParam(new Param("isDefaultAccOldUpdated", "true"));
				}
			} else {
				result.setParam(new Param("isDefaultAccNewUpdated", "false"));
				result.setParam(new Param("isDefaultAccOldUpdated", "false"));
			}
			result.setParam(new Param("opstatus", "0"));
			result.setParam(new Param("httpStatusCode", "200"));
		} catch (Exception e) {
			result.setParam(new Param("ErrMsg", "defualtAccUpdateFailed"));
			result.setParam(new Param("isDefaultAccNewUpdated", "false"));
			result.setParam(new Param("isDefaultAccOldUpdated", "false"));
			result.setParam(new Param("opstatus", "0"));
			result.setParam(new Param("httpStatusCode", "200"));
			LOG.debug("defualtAccUpdate Failed: " + e);
		}
		return result;
	}

	public String getCustomerDefaultAcc(String customerID) {
		LOG.debug("customerID **:"+ customerID);
		JSONArray accounts = new JSONArray();
		String defaultAccId = "";
		
		Map<String, Object> inputParams = new HashMap<String, Object>();
		inputParams.put(DBPUtilitiesConstants.FILTER, "Customer_id" + DBPUtilitiesConstants.EQUAL + customerID
				+ DBPUtilitiesConstants.AND + "FavouriteStatus" + DBPUtilitiesConstants.EQUAL + "1");

		try {
			String response = DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.DBPRBLOCALSERVICEDB)
					.withObjectId(null).withOperationId(OperationName.DB_CUSTOMERACCOUNTS_GET)
					.withRequestParameters(inputParams).build().getResponse();

			JSONObject responseJSON = new JSONObject(response);
			accounts = responseJSON.getJSONArray("customeraccounts");

			LOG.debug("Default Accounts" + accounts);
			JSONObject acc = accounts.getJSONObject(0);
			defaultAccId = acc.getString("Account_id");
			LOG.debug("defaultAccId:::" + defaultAccId);

		} catch (Exception e) {
			LOG.error("Exception caught while getCustomerDefaultAcc", e);
		}
		return defaultAccId;
	}

	private int updateExistCustomerDefaultAcc(DataControllerRequest request, String existAccountId, String customerid, String id)
			throws Exception {
		HashMap<String, Object> inputParams = new HashMap<String, Object>();
		HashMap<String, Object> serviceHeaders = new HashMap<String, Object>();

		/**
		 * FavouriteStatus Flag - 0 reverting default account of a customer
		 * 
		 */
		inputParams.put("FavouriteStatus", "0");
		inputParams.put("Account_id", existAccountId);
		inputParams.put("id", id);
		String serviceName = TemenosConstants.SERVICE_BACKEND_CERTIFICATE;
		String operationName = "dbxdb_customeraccounts_update";
		Result result = CommonUtils.callIntegrationService(request, inputParams, serviceHeaders, serviceName,
				operationName, false);
		LOG.debug("Post updateExistCustomerDefaultAcc");
		String errMessage = result.getParamValueByName(AccountsConstants.PARAM_ERROR_MESSAGE);
		if (StringUtils.isNotBlank(errMessage)) {
			LOG.error("Couldn't create entry in dbxDb customeraccounts Table due to : " + errMessage);
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

	private int updateNewCustomerDefaultAcc(DataControllerRequest request, String newAccountId, String customerid, String id)
			throws Exception {
		HashMap<String, Object> inputParams = new HashMap<String, Object>();
		HashMap<String, Object> serviceHeaders = new HashMap<String, Object>();

		/**
		 * FavouriteStatus Flag - 1 making new account Id as default account of a
		 * customer
		 * 
		 */
		inputParams.put("FavouriteStatus", "1");
		inputParams.put("Account_id", newAccountId);
		inputParams.put("id", id);
		String serviceName = TemenosConstants.SERVICE_BACKEND_CERTIFICATE;
		String operationName = "dbxdb_customeraccounts_update";
		Result result = CommonUtils.callIntegrationService(request, inputParams, serviceHeaders, serviceName,
				operationName, false);
		LOG.debug("Post updateNewCustomerDefaultAcc");
		String errMessage = result.getParamValueByName(AccountsConstants.PARAM_ERROR_MESSAGE);
		if (StringUtils.isNotBlank(errMessage)) {
			LOG.error("Couldn't create entry in dbxDb customeraccounts Table due to : " + errMessage);
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
	
	private String getIdFromCustomerId(DataControllerRequest request, String customerId) {
		String id = "";
		try {
			String filter = CommonUtils.buildOdataCondition("Customer_id", Constants.EQUAL, customerId);
			HashMap<String, Object> svcHeaders = new HashMap<String, Object>();
			HashMap<String, Object> svcParams = new HashMap<String, Object>();

			svcParams.put(Constants.PARAM_DOLLAR_FILTER, filter);
			Result result = CommonUtils.callIntegrationService(request, svcParams, svcHeaders,
					Constants.DBX_DB_SERVICE_NAME, OperationName.DB_CUSTOMERACCOUNTS_GET, false);
			Dataset customerDataset = result.getDatasetById("customeraccounts");
			if (null != customerDataset) {
				id = customerDataset.getRecord(0).getParamValueByName("id");
			} else {
				LOG.debug("Else getIdFromCustomerId:");
			}
			LOG.debug("getIdFromCustomerId id:" + id);
		} catch (Exception e) {
			LOG.error("Error while retrieving CustomerType_id for Customer " + customerId);
		}
		return id;
	}
	
	private String getIdFromCustomerIdNew(String customerID, String accountId) {
		//select id from dbxdb.customeraccounts where Customer_Id = 0826942649 AND Account_id = 123129;
		
		LOG.debug("customerID **:"+ customerID);
		LOG.debug("accountId **:"+ accountId);
		JSONArray records = new JSONArray();
		String idValue = "";
		Map<String, Object> inputParams = new HashMap<String, Object>();
		inputParams.put(DBPUtilitiesConstants.FILTER, "Customer_id" + DBPUtilitiesConstants.EQUAL + customerID
				+ DBPUtilitiesConstants.AND + "Account_id" + DBPUtilitiesConstants.EQUAL + accountId);

		try {
			String response = DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.DBPRBLOCALSERVICEDB)
					.withObjectId(null).withOperationId(OperationName.DB_CUSTOMERACCOUNTS_GET)
					.withRequestParameters(inputParams).build().getResponse();
			
			LOG.debug("Id Val:" + response);
			JSONObject responseJSON = new JSONObject(response);
			records = responseJSON.getJSONArray("customeraccounts");

			LOG.debug("response" + records);
			JSONObject acc = records.getJSONObject(0);
			idValue = acc.getString("id");
			LOG.debug("id:::" + idValue);

		} catch (Exception e) {
			LOG.error("Exception caught while getIdFromCustomerIdNew", e);
		}
		return idValue;
	}
	
}
