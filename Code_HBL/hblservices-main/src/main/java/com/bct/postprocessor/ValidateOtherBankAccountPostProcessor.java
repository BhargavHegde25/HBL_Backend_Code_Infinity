package com.bct.postprocessor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;

public class ValidateOtherBankAccountPostProcessor implements DataPostProcessor2{
	private static final Logger LOG = LogManager.getLogger(ValidateOtherBankAccountPostProcessor.class);

	@Override
	public Object execute(Result result, DataControllerRequest dcRequest, DataControllerResponse dcResponse) {
		String httpResponseCode=result.getHttpStatusCodeParamValue();
		LOG.debug("HBL::ValidateOtherBankAccountPostProcessor::result:" + ResultToJSON.convert(result));
		JSONObject response= new JSONObject(ResultToJSON.convert(result));
		String errorMessage = response.has("error_description")?response.getString("error_description"):null;
		response=response.has("validateOtherBankAccount")?response.getJSONObject("validateOtherBankAccount"):new JSONObject();
		String responseMessage = response.has("responseMessage")?response.getString("responseMessage"):null;
		String responseCode = response.has("responseCode")?response.getString("responseCode"):"";
		try {
		if(httpResponseCode.equalsIgnoreCase("401")) {
			 result.addParam(new Param("opstatus", "0"));
			 result.addParam(new Param("httpStatusCode", "200"));
			 result.addParam(new Param("dbpErrCode", "20000"));
			 result.addParam(new Param("dbpErrMsg", errorMessage));
			 response.put("responseCode", "401");
			 response.put("responseMessage", errorMessage);
		}else if(responseCode.equals("502")) {
			 result.addParam(new Param("opstatus", "0"));
			 result.addParam(new Param("httpStatusCode", "200"));
			 result.addParam(new Param("dbpErrCode", "20000"));
			 errorMessage="Sorry! Beneficiary account does not exists.";
			 result.addParam(new Param("dbpErrMsg", errorMessage));
			 response.put("responseMessage", errorMessage);
		}
		else if(responseCode.equals("523")) {
			Integer matchPercentate = response.has("matchPercentate")?response.getInt("matchPercentate"):null;
			if(matchPercentate!=null && matchPercentate<=60) {
			 errorMessage="Sorry! Beneficiary account name mismatch.";
			}else {
				 errorMessage="Some difference in beneficiary account name observed. Transaction once sent is irreversible, please reconfirm the beneficiary account number.";
			}
			 result.addParam(new Param("opstatus", "0"));
			 result.addParam(new Param("httpStatusCode", "200"));
			 result.addParam(new Param("dbpErrCode", "20000"));
			 result.addParam(new Param("dbpErrMsg", errorMessage));
			 response.put("responseMessage", errorMessage);
		}
		else if(responseCode.equalsIgnoreCase("E999")) {
			 result.addParam(new Param("opstatus", "0"));
			 result.addParam(new Param("httpStatusCode", "200"));
			 result.addParam(new Param("dbpErrCode", "20000"));
			 errorMessage="Sorry! Beneficiary Bank not reachable at the moment. Please try again later.";
			 result.addParam(new Param("dbpErrMsg", errorMessage));
			 response.put("responseMessage", errorMessage);
		}
		else if(responseCode.equals("1000")) {
			 result.addParam(new Param("opstatus", "0"));
			 result.addParam(new Param("httpStatusCode", "200"));
			 result.addParam(new Param("dbpErrCode", "20000"));
			 errorMessage="Sorry! Beneficiary Bank not reachable at the moment. Please try again later.";
			 result.addParam(new Param("dbpErrMsg", errorMessage));
			 response.put("responseMessage", errorMessage);
		}
		else if(responseCode.equals("504")) {
			 result.addParam(new Param("opstatus", "0"));
			 result.addParam(new Param("httpStatusCode", "200"));
			 result.addParam(new Param("dbpErrCode", "20000"));
			 errorMessage="Sorry! Transaction not allowed on the beneficiary Account.Please check with beneficiary bank.";
			 result.addParam(new Param("dbpErrMsg", errorMessage));
			 response.put("responseMessage", errorMessage);
		}
		else if(responseCode.equals("001")) {
			 result.addParam(new Param("opstatus", "0"));
			 result.addParam(new Param("httpStatusCode", "200"));
			 result.addParam(new Param("dbpErrCode", "20000"));
			 errorMessage="Sorry! Bank not reachable.";
			 result.addParam(new Param("dbpErrMsg", errorMessage));
			 response.put("responseMessage", errorMessage);
		}
		else if(responseCode.equals("999")) {
			Integer matchPercentate = response.has("matchPercentate")?response.getInt("matchPercentate"):null;
			if(matchPercentate!=null && matchPercentate<=60) {
			 result.addParam(new Param("opstatus", "0"));
			 result.addParam(new Param("httpStatusCode", "200"));
			 result.addParam(new Param("dbpErrCode", "20000"));
			 errorMessage="Sorry! Beneficiary account name mismatch.";
			 result.addParam(new Param("dbpErrMsg", errorMessage));
			 response.put("responseMessage", errorMessage);
			}
		}
		}
		catch (Exception e) {
			 result.addParam(new Param("opstatus", "0"));
			 result.addParam(new Param("httpStatusCode", "500"));
			 result.addParam(new Param("dbpErrCode", "20000"));
			 errorMessage="Sorry! Beneficiary Bank not reachable at the moment. Please try again later.";
			 result.addParam(new Param("dbpErrMsg", errorMessage));
			 responseMessage=e.getMessage();
			 response.put("responseMessage", errorMessage);
		}
		JSONObject newResponse = new JSONObject();
		newResponse.put("validateOtherBankAccount", response);
		result.appendJson(newResponse.toString());
		return result;
	}
}
