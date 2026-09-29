package com.bct.postprocessor;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.dbputilities.util.logger.LoggerUtil;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;

public class ManualMerchantFormFieldsPostprocessor implements DataPostProcessor2{
	LoggerUtil logger = new LoggerUtil(ManualMerchantFormFieldsPostprocessor.class);
	@Override
	public Object execute(Result result, DataControllerRequest dcRequest, DataControllerResponse dcResponse) throws Exception {
		String httpResponseCode=result.getHttpStatusCodeParamValue();
		String success=result.getParamValueByName("success");
		logger.debug("BCT::ManualMerchantFormFieldsPostprocessor::httpResponseCode:" + httpResponseCode);
		logger.debug("BCT::ManualMerchantFormFieldsPostprocessor::success:" + success);
		if(httpResponseCode.equalsIgnoreCase("200") && success.equalsIgnoreCase("true")) {
			JSONObject response= new JSONObject(ResultToJSON.convert(result));
			logger.debug("BCT::ManualMerchantFormFieldsPostprocessor::response:" + response);
			 if(response.has("merchantFields")) {
				 JSONObject merchantFields = response.getJSONObject("merchantFields");
				 result=postProcessResponse(merchantFields);
			 }
			 String merchantCode= dcRequest.getAttribute("appCode")!=null?dcRequest.getAttribute("appCode"):"1";
			 String outageMessage=response.has("outageMessage")?response.getString("outageMessage"):"";
			 String disclimerMessage=response.has("disclimerMessage")?response.getString("disclimerMessage"):"";
			 String paymentAggregator=response.has("paymentAggregator")?response.getString("paymentAggregator"):"";
			 result.setParam(new Param("outageMessage", outageMessage));
			 result.setParam(new Param("disclimerMessage", disclimerMessage));
			 result.setParam(new Param("appCode", merchantCode));
			 result.setParam(new Param("totalProcessSeq", "1"));
			 result.setParam(new Param("responseMessage", "SUCCESS"));
			 result.setParam(new Param("opstatus", "0"));
			 result.setParam(new Param("httpStatusCode", "200"));
			 result.setParam(new Param("paymentAggregator", paymentAggregator));
		}
		return result;
	}
	public Result postProcessResponse(JSONObject jsonObj) {
		logger.debug("BCT::ManualMerchantFormFieldsPostprocessor::postProcessResponse:jsonObj:" + jsonObj);
		JSONArray merchantFields = jsonObj.getJSONArray("requiredFields");
		JSONArray responseFieldMapping = jsonObj.getJSONArray("responseFieldMapping");
		JSONArray requestFields = new JSONArray();
		JSONArray responseFields = new JSONArray();
		JSONArray dataTypeArray = new JSONArray();
		JSONObject dataTypeObj= new JSONObject();
		JSONObject responeJson = new JSONObject();
		for (int count = 0; count < merchantFields.length(); count++) {
			JSONObject obj= merchantFields.getJSONObject(count);
			String fieldType = obj.get("fieldtype").toString();
			if (fieldType.equalsIgnoreCase("textbox")) {
				obj.put("fieldtype", "TEXTFIELD");
			}
			else if (fieldType.equalsIgnoreCase("ListBox")) {
				obj.put("fieldtype", "OPTIONFIELD");
			}
			else if (fieldType.equalsIgnoreCase("Lable")) {
				obj.put("fieldtype", "READONLYFIELD");
			}
			else if (fieldType.equalsIgnoreCase("Date")) {
				obj.put("fieldtype", "DATEFIELD");
				obj.put("inputFormat", "yyyy-mm-dd");
			}
			dataTypeObj.put("minLength", "1");
			dataTypeObj.put("length", "100");
			dataTypeObj.put("type", "ANS");
			dataTypeArray.put(dataTypeObj);
			obj.put("dataType", dataTypeArray);
			requestFields.put(obj);
		}
		for (int count = 0; count < responseFieldMapping.length(); count++) {
			JSONObject obj= responseFieldMapping.getJSONObject(count);
			String fieldType = obj.get("fieldtype").toString();
			if (fieldType.equalsIgnoreCase("textbox")) {
				obj.put("fieldtype", "TEXTFIELD");
			}
			else if (fieldType.equalsIgnoreCase("ListBox")) {
				obj.put("fieldtype", "OPTIONFIELD");
			}
			else if (fieldType.equalsIgnoreCase("Lable")) {
				obj.put("fieldtype", "READONLYFIELD");
			}
			else if (fieldType.equalsIgnoreCase("Date")) {
				obj.put("fieldtype", "DATEFIELD");
				obj.put("inputFormat", "yyyy-mm-dd");
			}
			dataTypeObj.put("minLength", "1");
			dataTypeObj.put("length", "100");
			dataTypeObj.put("type", "ANS");
			dataTypeArray.put(dataTypeObj);
			obj.put("dataType", dataTypeArray);
			responseFields.put(obj);
		}
		//merchantFields.put(requestFields);
		//responseFieldMapping.put(responseFields);
		responeJson.put("requiredFields",requestFields);
		responeJson.put("responseFieldMapping",responseFields);
		JSONObject resultObj= new JSONObject();
		resultObj.put("merchantFields", responeJson);
        Result result = JSONToResult.convert(resultObj.toString());
        return result;
	}
}
