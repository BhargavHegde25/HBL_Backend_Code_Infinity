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

public class KUKLBillpaymentPostprocessor implements DataPostProcessor2 {
	LoggerUtil logger = new LoggerUtil(KUKLBillpaymentPostprocessor.class);
	@Override
	public Object execute(Result result, DataControllerRequest dcRequest, DataControllerResponse arg2) throws Exception {
		String httpResponseCode=result.getHttpStatusCodeParamValue();
		logger.debug("BCT::KUKLBillpaymentPostprocessor::result:" + ResultToJSON.convert(result));
		logger.debug("BCT::KUKLBillpaymentPostprocessor::httpResponseCode:" + httpResponseCode);
		JSONObject response= new JSONObject(ResultToJSON.convert(result));
		String dbpErrCode=result.getParamValueByName("dbpErrCode");
		if(httpResponseCode.equalsIgnoreCase("200") && result.getParamValueByName("success").equalsIgnoreCase("true")) {
			response.put("responseFieldMapping", generateResponseFieldMapping(response).toString());
			}
		if(StringUtils.isNotBlank(dbpErrCode)  && dbpErrCode.equalsIgnoreCase("20001")){ // Transaction Reversed
			response.put("responseFieldMapping", generateResponseFieldMapping(response).toString());
		}
			 result = JSONToResult.convert(response.toString());
			 result.setParam(new Param("opstatus", "0"));
			 result.setParam(new Param("httpStatusCode", "200"));
		return result;
	}
	public JSONObject generateResponseFieldMapping(JSONObject response) {
		JSONObject responseFieldMapping= new JSONObject();
		JSONArray transDetails= new JSONArray();
		responseFieldMapping.put("result", "Payment Status");
		responseFieldMapping.put("amount", "Amount");
		responseFieldMapping.put("txnReferenceNo", "External Reference Id");
		responseFieldMapping.put("recNo", "Rec No");
		responseFieldMapping.put("customerNo", "Customer No");
		responseFieldMapping.put("recdate", "Date");
		responseFieldMapping.put("connectionNo", "Connection No");
		return responseFieldMapping;
	}

	

}
