package com.bct.custom.resource.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.bct.custom.businessdeligate.api.BranchDetailsBusinessDelegate;
import com.bct.custom.businessdeligate.api.ManualMerchantsBusinessDelagate;
import com.bct.custom.resource.api.ManualMerchantsResource;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.constants.DBPConstants;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class ManualMerchantsResourceImpl implements ManualMerchantsResource{
	private static final Logger LOG = LogManager.getLogger(ManualMerchantsResourceImpl.class);
	@Override
	public Result manualMerchantOperations(String methodId, Object[] inputArray,
			DataControllerRequest dcRequest) throws ApplicationException {
		Result result = new Result();
		LOG.debug("BCT::ManualMerchantsResourceImpl:createBranchDetails:inputArray:"+inputArray.toString());
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		ManualMerchantsBusinessDelagate businessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(ManualMerchantsBusinessDelagate.class);
		LOG.debug("BCT::ManualMerchantsResourceImpl:methodId:"+methodId);
		try {
			if(methodId.equals("getManualMerchantsForCreate")) {
			String input = inputParams.get("category");
			LOG.debug("BCT::ManualMerchantsResourceImpl::getAvailableMerchantsAndPaymentAggregators: " + inputParams.toString());
			JSONArray response = businessDelegate.getAvailableMerchantsForCreate(input, dcRequest);
			LOG.debug("BCT::ManualMerchantsResourceImpl:getAvailableMerchantsAndPaymentAggregators:response: " + response.toString());
			Dataset ds = new Dataset();
			ds = constructDatasetFromJSONArray(response);
			ds.setId("merchants");
			result.addDataset(ds);
	}
		}catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}


public static Dataset constructDatasetFromJSONArray(JSONArray JSONArray) {
	Dataset dataset = new Dataset();
	for (int count = 0; count < JSONArray.length(); count++) {
		Record resRecord = constructRecordFromJSONObject((JSONObject) JSONArray.get(count));
		dataset.addRecord(resRecord);
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