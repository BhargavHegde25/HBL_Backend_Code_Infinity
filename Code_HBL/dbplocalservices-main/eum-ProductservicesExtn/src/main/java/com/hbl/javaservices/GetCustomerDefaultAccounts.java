package com.hbl.javaservices;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.constants.DBPConstants;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.logger.LoggerUtil;
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

public class GetCustomerDefaultAccounts implements JavaService2 {
	private static LoggerUtil logger = new LoggerUtil(GetCustomerDefaultAccounts.class);

	@Override
	public Object invoke(String arg0, Object[] arg1, DataControllerRequest request, DataControllerResponse arg3)
			throws Exception {
		Result result = new Result();
		String UserName = request.getParameter("userName");
		String Customer_id = getCustomerIDFromUsername(request, UserName);
		JSONArray accounts = new JSONArray();
		accounts = getUserDefaultAccounts(Customer_id);
		
		Dataset ds = new Dataset();
		ds.setId("defaultAccounts");
		ds = constructDatasetFromJSONArray(accounts);
        result.addDataset(ds);
        logger.debug("Result Accounts:"+ ds);
        
        
		result.setParam(new Param("opstatus", "0"));
		result.setParam(new Param("httpStatusCode", "200"));

		return result;
	}

	public JSONArray getUserDefaultAccounts(String customerID) {

		JSONArray accounts = new JSONArray();
		Map<String, Object> inputParams = new HashMap<String, Object>();
		inputParams.put(DBPUtilitiesConstants.FILTER, "Customer_id" + DBPUtilitiesConstants.EQUAL + customerID
				+ DBPUtilitiesConstants.AND + "FavouriteStatus" + DBPUtilitiesConstants.EQUAL + "1");

		try {
			String response = DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.DBPRBLOCALSERVICEDB)
					.withObjectId(null).withOperationId(OperationName.DB_CUSTOMERACCOUNTS_GET)
					.withRequestParameters(inputParams).build().getResponse();

			JSONObject responseJSON = new JSONObject(response);
			accounts = responseJSON.getJSONArray("customeraccounts");
			
			logger.debug("Default Accounts"+ accounts);
		} catch (Exception e) {
			logger.error("Exception caught while fetching user role", e);
		}
		return accounts;

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
				logger.debug("Else getThirdpartyAuthFlag:");
			}
			logger.debug("getThirdpartyAuthFlag id:" + customerid);
		} catch (Exception e) {
			logger.error("Error while retrieving CustomerType_id for Customer " + UserName);
		}
		return customerid;

	}

	public static Dataset constructDatasetFromJSONArray(JSONArray JSONArray) {
		Dataset dataset = new Dataset();
		for (int count = 0; count < JSONArray.length(); count++) {
			Record record = constructRecordFromJSONObject((JSONObject) JSONArray.get(count));
			dataset.addRecord(record);
		}
		return dataset;
	}

	public static Record constructRecordFromJSONObject(JSONObject JSONObject) {
		Record response = new Record();
		if (JSONObject == null || JSONObject.length() == 0) {
			return response;
		}
		Iterator<String> keys = JSONObject.keys();

		while (keys.hasNext()) {
			String key = keys.next();
			if (JSONObject.get(key) instanceof String) {
				Param param = new Param(key, JSONObject.getString(key), DBPConstants.FABRIC_STRING_CONSTANT_KEY);
				response.addParam(param);

			} else if (JSONObject.get(key) instanceof Integer) {
				Param param = new Param(key, JSONObject.get(key).toString(), DBPConstants.FABRIC_INT_CONSTANT_KEY);
				response.addParam(param);

			} else if (JSONObject.get(key) instanceof Boolean) {
				Param param = new Param(key, JSONObject.get(key).toString(), DBPConstants.FABRIC_BOOLEAN_CONSTANT_KEY);
				response.addParam(param);

			} else if (JSONObject.get(key) instanceof JSONArray) {
				Dataset dataset = constructDatasetFromJSONArray(JSONObject.getJSONArray(key));
				dataset.setId(key);
				response.addDataset(dataset);
			}
		}

		return response;
	}

}
