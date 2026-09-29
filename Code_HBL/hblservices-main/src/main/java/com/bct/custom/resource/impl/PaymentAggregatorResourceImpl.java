package com.bct.custom.resource.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.bct.custom.businessdeligate.api.PaymentAggregatorBusinessDeligate;
import com.bct.custom.dto.PaymentAggregatorDTO;
import com.bct.custom.resource.api.PaymentAggregatorResource;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.constants.DBPConstants;
//import com.kony.AdminConsole.Utilities.CommonUtilities;
import com.dbp.core.util.JSONUtils;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class PaymentAggregatorResourceImpl implements PaymentAggregatorResource {
	private static final Logger LOG = LogManager.getLogger(PaymentAggregatorResourceImpl.class);
	
	public Result paymentAggregatorCRUDOperation(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws ApplicationException {
		Result result = new Result();
		PaymentAggregatorDTO dto;
		JSONArray requestArray =null;
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
		String request = inputParams.get("paymentAggregator").toString();
		LOG.debug("BCT::PaymentAggregatorResourceImpl::paymentAggregatorOperation: paymentAggregatorRequest:"
				+ request);
		if (methodID.equalsIgnoreCase("updateMerchantPaymentAggregator")) {
			try {
				requestArray = new JSONArray(request);
				request=requestArray.getJSONObject(0).toString();
				dto = JSONUtils.parse(request, PaymentAggregatorDTO.class);
				Map<String, Object> inputs = validateInputs(dto, dcRequest);
				LOG.debug("BCT::PaymentAggregatorResourceImpl::paymentAggregatorOperation: PaymentAggregatorDTO:"
						+ dto.toString() + "inputs:" + inputs.toString());
				PaymentAggregatorBusinessDeligate businessDelegate = DBPAPIAbstractFactoryImpl
						.getBusinessDelegate(PaymentAggregatorBusinessDeligate.class);
				result = businessDelegate.updatePaymentAggregator(dto, dcRequest.getHeaderMap(), inputs);
				LOG.debug("BCT::PaymentAggregatorResourceImpl::paymentAggregatorOperation: response:"
						+ result.toString());
				if (result.getParamByName("dbpErrCode") == null) {
					result.setParam(new Param("opstatus", "0"));
					result.setParam(new Param("httpStatusCode", "200"));
					result.setParam(new Param("success", "true"));
				}
			} catch (Exception e) {
				result.addParam(new Param("dbpErrCode", e.toString()));
				result.addParam(new Param("dbpErrMsg", e.getMessage()));
			}
		} else if (methodID.equalsIgnoreCase("createPaymentAggregator")) {
			try {
				requestArray = new JSONArray(request);
				request=requestArray.getJSONObject(0).toString();
				dto = JSONUtils.parse(request, PaymentAggregatorDTO.class);
				Map<String, Object> inputs = validateCreatePaymentAggInputs(dto, dcRequest);
				LOG.debug("BCT::PaymentAggregatorResourceImpl::paymentAggregatorCreateOperation: PaymentAggregatorDTO:"+ dto.toString());
				PaymentAggregatorBusinessDeligate businessDelegate = DBPAPIAbstractFactoryImpl
						.getBusinessDelegate(PaymentAggregatorBusinessDeligate.class);
				result = businessDelegate.createPaymentAggregator(dto, dcRequest.getHeaderMap(), inputs);
				LOG.debug("BCT::PaymentAggregatorResourceImpl::paymentAggregatorCreateOperation: response:"
						+ result.toString());
				if (result.getParamByName("dbpErrCode") == null) {
					result.setParam(new Param("opstatus", "0"));
					result.setParam(new Param("httpStatusCode", "200"));
					result.setParam(new Param("success", "true"));
				}
			} catch (Exception e) {
				result.addParam(new Param("dbpErrCode", e.toString()));
				result.addParam(new Param("dbpErrMsg", e.getMessage()));
			}
		}
		return result;
	}

	public Map<String, Object> validateInputs(PaymentAggregatorDTO dto, DataControllerRequest dcRequest)
			throws ApplicationException {
		if (StringUtils.isBlank(dto.getId().toString())) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10755);
		}
		Map<String, Object> inputParams = new HashMap<String, Object>();
		String loggedInUser = null;
		try {
			Map<String, String> userProfile = HelperMethods.getCustomerFromIdentityService(dcRequest);
			loggedInUser = userProfile.get("UserName");
		} catch (Exception e) {
			LOG.error("BCT::PaymentAggregatorResourceImpl::validateInputs: exception:" + e.toString());
		}
		if (dto.isActive() != null)
		inputParams.put("isActive", dto.isActive());
		inputParams.put("modifiedby", loggedInUser);
		inputParams.put("currenttime", HelperMethods.getCurrentTimeStamp());
		return inputParams;
	}

	public Map<String, Object> validateCreatePaymentAggInputs(PaymentAggregatorDTO dto, DataControllerRequest dcRequest)
			throws ApplicationException {
		String aggregatorName = dto.getName();
		if ( StringUtils.isBlank(aggregatorName) || dto.isActive()==null) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10755);
		}
		Map<String, Object> inputParams = new HashMap<String, Object>();
		String loggedInUser = null;
		try {
			Map<String, String> userProfile = HelperMethods.getCustomerFromIdentityService(dcRequest);
			loggedInUser = userProfile.get("UserName");
		} catch (Exception e) {
			LOG.error("BCT::PaymentAggregatorResourceImpl::validateInputs: exception:" + e.toString());
		}
		if (dto.isActive() != null)
		inputParams.put("isActive", dto.isActive());
		inputParams.put("modifiedby", loggedInUser);
		inputParams.put("currenttime", HelperMethods.getCurrentTimeStamp());
		return inputParams;
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
