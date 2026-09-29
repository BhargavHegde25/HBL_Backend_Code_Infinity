package com.bct.javaservices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;

public class CrossBorderConsent implements JavaService2 {
	private static final Logger LOG = LogManager.getLogger(CrossBorderConsent.class);

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Result result = new Result();
		try {
			String username = request.getParameter("username");
			String accountId = request.getParameter("accountNumber");
			String type = request.getParameter("type");
			String status = request.getParameter("status");
			LOG.debug("username ##" + username);
			String customerId = getCustomerIDFromUsername(request, username);
			String defaultAcc = getCustomerDefaultAcc(customerId);
			LOG.debug("accountId ##" + accountId);
			LOG.debug("Default Account ##" + defaultAcc);
			LOG.debug("type ##" + type);
			LOG.debug("status ##" + status);
			LOG.debug("customerId ##" + customerId);
			// String customerId = HelperMethods.getCustomerIdFromSession(request);
			if (type.equalsIgnoreCase("fetch")) {
				/** get DB call to fetch consent data from customer accounts table **/
				Dataset ds = getCustomerConsents(customerId);
				result.addParam("defaultAccount", defaultAcc);
				result.addDataset(ds);
			} else {
				/**
				 * update customer accounts table to update consent for requested account ID
				 **/
				Integer updateStatus = updateCustoemrAccConsent(request, customerId, accountId, status);
				if (updateStatus == 1)
					result.addParam("consentupdated", "true");
				else
					result.addParam("consentupdated", "false");
			}
			result.addOpstatusParam(0);
			result.addHttpStatusCodeParam(200);
		} catch (Exception e) {
			LOG.error("Exception occured in CrossBorderConsent:::" + e.getMessage(), e);
			ErrorCodeEnum.ERR_10021.setErrorCode(result);
			result.addParam(new Param("dbpErrMsg", e.getLocalizedMessage()));
			result.addParam(new Param("success", "false"));
		}
		return result;
	}

	private Dataset getCustomerConsents(String customerId) {
		LOG.debug("customerID ###" + customerId);
		Dataset ds = new Dataset("CONSENTS");
		JSONArray accounts = new JSONArray();
		List<Record> accountFinals = new ArrayList<Record>();
		String status = "";
		Map<String, Object> inputParams = new HashMap<String, Object>();
		inputParams.put(DBPUtilitiesConstants.FILTER, "Customer_id" + DBPUtilitiesConstants.EQUAL + customerId);
		try {
			String response = DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.DBPRBLOCALSERVICEDB)
					.withObjectId(null).withOperationId(OperationName.DB_CUSTOMERACCOUNTS_GET)
					.withRequestParameters(inputParams).build().getResponse();
			JSONObject responseJSON = new JSONObject(response);
			accounts = responseJSON.getJSONArray("customeraccounts");
			for (int i = 0; i < accounts.length(); i++) {
				Record record = new Record();
				LOG.debug("accounts records ###" + i + "###" + accounts.getJSONObject(i));
				if (accounts.getJSONObject(i).getString("accountType").equalsIgnoreCase("Savings")
						|| accounts.getJSONObject(i).getString("accountType").equalsIgnoreCase("Checking")) {

					if (accounts.getJSONObject(i).has("email") && !accounts.getJSONObject(i).getString("email").equalsIgnoreCase("")) {
						status = (accounts.getJSONObject(i).getString("email").equalsIgnoreCase("YES")) ? "APPROVED"
								: "DECLINED";
						record.addParam("ConsentStatus", status);
					} else {
						record.addParam("ConsentStatus", "PENDING");
					}
					record.addParam("AccountId", accounts.getJSONObject(i).getString("Account_id"));
					accountFinals.add(record);
				}
			}
			ds.addAllRecords(accountFinals);
			LOG.debug("accountFinals ###" + accountFinals.toString());

		} catch (Exception e) {
			LOG.error("Exception caught while getCustomerConsents", e);
		}
		return ds;
	}

	public String getCustomerDefaultAcc(String customerID) {
		LOG.debug("customerID ##" + customerID);
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

	private String getCustomerIDFromUsername(DataControllerRequest request, String UserName) {
		String customerid = "";
		try {
			LOG.debug("UserName ##"+ UserName);
			String filter = CommonUtils.buildOdataCondition(TemenosConstants.PARAM_USERNAME, Constants.EQUAL, UserName);
			HashMap<String, Object> svcHeaders = new HashMap<String, Object>();
			HashMap<String, Object> svcParams = new HashMap<String, Object>();
			LOG.debug("filter ##"+ filter);
			svcParams.put(Constants.PARAM_DOLLAR_FILTER, filter);
			Result result = CommonUtils.callIntegrationService(request, svcParams, svcHeaders,
					Constants.DBX_DB_SERVICE_NAME, TemenosConstants.OP_CUSTOMER_GET, false);
			Dataset customerDataset = result.getDatasetById(TemenosConstants.DS_CUSTOMER);
			if (null != customerDataset) {
				customerid = customerDataset.getRecord(0).getParamValueByName("id");
			} else {
				LOG.debug("Else getCustomerIDFromUsername in Consent:");
			}
			LOG.debug("getCustomerIDFromUsername in Consent id:" + customerid);
		} catch (Exception e) {
			LOG.error("Error while retrieving CustomerType_id for Customer " + UserName);
		}
		return customerid;
	}

	private int updateCustoemrAccConsent(DataControllerRequest request, String customerId, String Acc, String status)
			throws Exception {
		HashMap<String, Object> inputParams = new HashMap<String, Object>();
		HashMap<String, Object> serviceHeaders = new HashMap<String, Object>();
		String id = getIdOfCustomer(request, customerId, Acc);

		/**
		 * email field of customerAccounts table is using to maintain CrossborderConsent
		 * status
		 * 
		 */
		// inputParams.put("Account_id", Acc);
		inputParams.put("id", id);
		inputParams.put("email", status);
		// inputParams.put("Customer_id", customerId);
		String serviceName = TemenosConstants.SERVICE_BACKEND_CERTIFICATE;
		String operationName = "dbxdb_customeraccounts_update";
		Result result = CommonUtils.callIntegrationService(request, inputParams, serviceHeaders, serviceName,
				operationName, false);
		LOG.debug("Post updateCustoemrAccConsent");
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

	private String getIdOfCustomer(DataControllerRequest request, String customerId, String accId) {
		String id = "";
		try {
			LOG.debug("customerId **" + customerId);

			HashMap<String, Object> inputParams = new HashMap<String, Object>();
			inputParams.put("$filter", "Customer_id eq " + customerId + " and Account_id eq " + accId);
			request.addRequestParam_("$filter", "Customer_id eq " + customerId + " and Account_id eq " + accId);
			HashMap<String, Object> svcHeaders = new HashMap<String, Object>();

			Result result = CommonUtils.callIntegrationService(request, inputParams, svcHeaders,
					Constants.DBX_DB_SERVICE_NAME, OperationName.DB_CUSTOMERACCOUNTS_GET, false);
			Dataset customerDataset = result.getDatasetById("customeraccounts");
			if (null != customerDataset) {

				id = customerDataset.getRecord(0).getParamValueByName("id");
			} else {
				LOG.debug("Else getContractId:");
			}

			LOG.debug("getContractId :" + id);

		} catch (Exception e) {
			LOG.error("Error while retrieving Contract Id for Customer " + id);
		}
		return id;
	}

}
