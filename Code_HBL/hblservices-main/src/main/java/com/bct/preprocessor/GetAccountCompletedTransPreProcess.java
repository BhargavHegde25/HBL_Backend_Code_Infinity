package com.bct.preprocessor;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.infinity.dbx.temenos.TemenosBasePreProcessor;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;
import com.temenos.infinity.api.arrangements.utils.ArrangementsUtils;

public class GetAccountCompletedTransPreProcess extends TemenosBasePreProcessor {
	private static final Logger LOG = LogManager.getLogger(GetAccountCompletedTransPreProcess.class);
	@SuppressWarnings("unchecked")
	public boolean execute(HashMap params, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		String CustomerId = ArrangementsUtils.getUserAttributeFromIdentity(request, "customer_id");
		LOG.debug("CustomerId: "+ CustomerId);
		String defaultAccount = getUserDefaultAccounts(CustomerId);
		LOG.debug("defaultAccount: "+ defaultAccount);
		params.put("accountID",defaultAccount);
		params.put("transactionType","ALL");
		params.put("searchMinAmount","1");
		params.put("limit","5");
		
		return true;
	}
	
	public String getUserDefaultAccounts(String customerID) {

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
			
			LOG.debug("Default Accounts"+ accounts);
			JSONObject acc = accounts.getJSONObject(0);
			defaultAccId = acc.getString("Account_id");
			LOG.debug("defaultAccId:::"+defaultAccId);
			
		} catch (Exception e) {
			LOG.error("Exception caught while getUserDefaultAccounts", e);
		}
		return defaultAccId;

	}
}
