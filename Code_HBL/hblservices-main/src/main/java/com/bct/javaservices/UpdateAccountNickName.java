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

public class UpdateAccountNickName implements JavaService2 {
	private static final Logger LOG = LogManager.getLogger(UpdateAccountNickName.class);

	@SuppressWarnings("deprecation")
	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		String userName = request.getParameter("userName");
		String accountID = request.getParameter("accountID");
		String nickName = request.getParameter("nickName");
		String customerId = getCustomerIDFromUsername(request, userName);
		LOG.debug("userName:" + userName);
		LOG.debug("customerId:" + customerId);
		LOG.debug("accountID**:" + accountID);
		LOG.debug("nickName**:" + nickName);
		String id = getIdfromCustomerAccTable(customerId, accountID);
		LOG.debug("idOfExistAcc***:" + id);
		Result result = new Result();
		try {
			Integer nickNameupdateStatus = updateAccountNickName(request, accountID, nickName, id);
			if (nickNameupdateStatus == 1) {
				result.setParam(new Param("nickNameupdateStatus", "true"));
			} else {
				result.setParam(new Param("nickNameupdateStatus", "false"));
			}
			result.setParam(new Param("opstatus", "0"));
			result.setParam(new Param("httpStatusCode", "200"));
		} catch (Exception e) {
			result.setParam(new Param("ErrMsg", "nickNameupdateFailed"));
			result.setParam(new Param("nickNameupdateStatus", "false"));
			result.setParam(new Param("opstatus", "0"));
			result.setParam(new Param("httpStatusCode", "200"));
			LOG.debug("defualtAccUpdate Failed: " + e);
		}
		return result;
	}

	private int updateAccountNickName(DataControllerRequest request, String accountId, String nickName, String id)
			throws Exception {
		HashMap<String, Object> inputParams = new HashMap<String, Object>();
		HashMap<String, Object> serviceHeaders = new HashMap<String, Object>();

		inputParams.put("NickName", nickName);
		inputParams.put("Account_id", accountId);
		inputParams.put("id", id);
		String serviceName = TemenosConstants.SERVICE_BACKEND_CERTIFICATE;
		String operationName = "dbxdb_customeraccounts_update";
		Result result = CommonUtils.callIntegrationService(request, inputParams, serviceHeaders, serviceName,
				operationName, false);
		LOG.debug("Post updateAccountNickName");
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

	private String getIdfromCustomerAccTable(String customerID, String accountId) {
		// select id from dbxdb.customeraccounts where Customer_Id = 0826942649 AND
		// Account_id = 123129;

		LOG.debug("customerID **:" + customerID);
		LOG.debug("accountId **:" + accountId);
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
			LOG.error("Exception caught while getIdfromCustomerAccTable", e);
		}
		return idValue;
	}

}
