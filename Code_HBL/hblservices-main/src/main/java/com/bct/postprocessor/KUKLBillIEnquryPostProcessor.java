package com.bct.postprocessor;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import com.kony.dbputilities.util.logger.LoggerUtil;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;

public class KUKLBillIEnquryPostProcessor implements DataPostProcessor2{
		LoggerUtil logger = new LoggerUtil(NEABillEnquiryPostprocessor.class);
		@Override
		public Object execute(Result result, DataControllerRequest arg1, DataControllerResponse arg2) throws Exception {
			String httpResponseCode=result.getHttpStatusCodeParamValue();
			logger.debug("BCT::KUKLBillIEnquryPostProcessor::result:" + ResultToJSON.convert(result));
			logger.debug("BCT::KUKLBillIEnquryPostProcessor::httpResponseCode:" + httpResponseCode);
			JSONObject response= new JSONObject(ResultToJSON.convert(result));
			String errorMessage = response.has("errorMessage")?response.getString("errorMessage"):"";
			if(httpResponseCode.equalsIgnoreCase("200") && StringUtils.isBlank(errorMessage)) {
				JSONObject responseFieldMapping= new JSONObject();
				responseFieldMapping.put("billMonth", "Bill Month");
				responseFieldMapping.put("address", "Address");
				responseFieldMapping.put("areaNo", "Area No");
				responseFieldMapping.put("penalty", "Penalty");
				responseFieldMapping.put("name", "Name");
				responseFieldMapping.put("netamount", "Due Amount");
				responseFieldMapping.put("customerNo", "Customer No");
				responseFieldMapping.put("connectionNo", "Connection No");
				responseFieldMapping.put("unapprarrearadj", "Unapproved Arrer Adjustment");
				responseFieldMapping.put("unapprpenaltyadj", "Unapproved Penalty Adjustment");
				response.put("responseFieldMapping", responseFieldMapping.toString());
				result.appendResult(JSONToResult.convert(response.toString()));
			}else {
				result.addParam(new Param("dbpErrCode", "20000"));
				result.addParam(new Param("dbpErrMsg", errorMessage));
			}
				 result.setParam(new Param("opstatus", "0"));
				 result.setParam(new Param("httpStatusCode", "200"));
			return result;
		}

	}

