package com.bct.postprocessor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.constants.DBPConstants;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.konylabs.middleware.session.Session;

public class LodgeBillPayPostProcessor implements DataPostProcessor2{
	LoggerUtil logger = new LoggerUtil(LodgeBillPayPostProcessor.class);
	String responseDescription=null;
	String responseCode = null;
	String responseMessage = null;

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response1)
			throws Exception {
		logger.debug("HBL::LodgeBillPayPostProcessor:");
		logger.debug("HBL::LodgeBillPayPostProcessor:result:"+ResultToJSON.convert(result));
		String httpResponseCode=result.getHttpStatusCodeParamValue();
		JSONObject responseObj= new JSONObject();
		JSONObject cipsTransactionDetail= null;
		JSONObject cipsBatchDetail= null;
		String token=null;
		JSONObject response= new JSONObject(ResultToJSON.convert(result));
		 responseDescription = result.getParamValueByName("responseDescription");
		 responseCode = result.getParamValueByName("responseCode");
		 responseMessage = result.getParamValueByName("responseMessage");
		 if(response.has("responseResult")) {
			 responseObj=response.getJSONObject("responseResult");
		 }
		 if(response.has("cipsTransactionDetail")) {
			 cipsTransactionDetail=response.getJSONObject("cipsTransactionDetail");
		 }
		 if(response.has("cipsBatchDetail")) {
			 cipsBatchDetail=response.getJSONObject("cipsBatchDetail");
		 }
		 if(response.has("token")) {
			 token=response.getString("token");
		 }
		 logger.debug("HBL::LodgeBillPayPostProcessor:responseObj:"+responseObj.toString());
		 if(responseObj.has("responseCode")) {
				responseDescription=responseObj.getString("responseDescription");
				responseCode=responseObj.getString("responseCode");
			}
		 if(responseObj.has("responseMessage")) {
				responseMessage=responseObj.getString("responseMessage");
			}
		if(httpResponseCode.equalsIgnoreCase("200")) {
			if(responseCode.equalsIgnoreCase("000")) {
				//String lodgeBillpayRequest = request.getParameter("lodgeBillpayRequest");
				//logger.debug("HBL::LodgeBillPayPostProcessor:buildRequestParameters:lodgeBillpayRequest: "+lodgeBillpayRequest);
				HashMap inputMap = request.getAttribute("lodgeBillpayRequest");
				logger.debug("HBL::LodgeBillPayPostProcessor:buildRequestParameters:inputMap: "+inputMap);
				Session session = request.getSession();
				session.setAttribute("lodgeBillpayRequest", inputMap);
				responseObj= new JSONObject();
				responseObj.put("responseResult", response.getJSONObject("responseResult"));
				responseObj.put("cipsTransactionDetail", cipsTransactionDetail);
				responseObj.put("cipsBatchDetail", cipsBatchDetail);
				responseObj.put("token", token);
			}
		}else if(!httpResponseCode.equalsIgnoreCase("200")) {
			JSONArray fieldErrors = new JSONArray();
			if(responseObj.has("fieldErrors") && responseObj.getJSONArray("fieldErrors").length()>0) {
				fieldErrors=responseObj.getJSONArray("fieldErrors");
			}
			if(responseCode!=null) {
				responseObj.put("dbpErrCode", responseCode);
			}
			if(responseDescription!=null) {
				responseObj.put("dbpErrMsg", responseDescription);
			}
			if(responseMessage!=null) {
				responseObj.put("dbpErrMsg", responseMessage);
			}
			if(responseCode!=null && responseCode.equalsIgnoreCase("E007")) {
				/*Dataset ds = new Dataset();
				ds = constructDatasetFromJSONArray(fieldErrors);
				ds.setId("fieldErrors");
				result.addDataset(ds);
				ResultToJSON.convertDataset(ds);
				*/
				responseObj.put("fieldErrors", fieldErrors);
				
			}
		}
		logger.debug("HBL::LodgeBillPayPostProcessor:final response: "+responseObj);
			result=JSONToResult.convert(responseObj.toString());
		
		return result;
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
