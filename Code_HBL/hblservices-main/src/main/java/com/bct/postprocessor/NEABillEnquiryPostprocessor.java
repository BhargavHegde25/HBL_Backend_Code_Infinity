package com.bct.postprocessor;

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

public class NEABillEnquiryPostprocessor implements DataPostProcessor2{
	LoggerUtil logger = new LoggerUtil(NEABillEnquiryPostprocessor.class);
	@Override
	public Object execute(Result result, DataControllerRequest dcRequest, DataControllerResponse dcResponse) throws Exception {
		String httpResponseCode=result.getHttpStatusCodeParamValue();
		logger.debug("BCT::NEABillEnquiryPostprocessor::result:" + ResultToJSON.convert(result));
		logger.debug("BCT::NEABillEnquiryPostprocessor::httpResponseCode:" + httpResponseCode);
		JSONObject response= new JSONObject(ResultToJSON.convert(result));
		if(httpResponseCode.equalsIgnoreCase("200")) {
			if(response.has("TRANDETAILS")) {
				JSONArray transDetails = response.getJSONArray("TRANDETAILS");
				 
			  }
			JSONObject responseFieldMapping= new JSONObject();
			//responseFieldMapping.put("paybleamount", "Payable Amount");
			responseFieldMapping.put("duebillof", "Due Bill Of");
			responseFieldMapping.put("noofdays", "No Of Days");
			responseFieldMapping.put("consumerid", "Consumer Id");
			responseFieldMapping.put("billamt", "Bill Amount");
			responseFieldMapping.put("finerate", "Fine Rate");
			responseFieldMapping.put("office", "Office");
			responseFieldMapping.put("scno", "Service No");
			responseFieldMapping.put("billdate", "Bill Date");
			responseFieldMapping.put("rebate", "Rebate");
			responseFieldMapping.put("customername", "Customer Name");
			response.put("responseFieldMapping", responseFieldMapping.toString());
			
			 /*if(response.has("transactionDetails")) {
				 JSONArray transactionDetails = response.getJSONArray("transactionDetails");
				 String code=transactionDetails.getJSONObject(0).has("code")? transactionDetails.getJSONObject(0).getString("code"):"";
				 String message= transactionDetails.getJSONObject(0).has("code")? transactionDetails.getJSONObject(0).getString("code"):"";
					 if(code.equals("1016") && message.equalsIgnoreCase("Please input a valid mobile number to recieve Bill Payment SMS Notification")) {
						 result=postProcessResponse(transactionDetails);
					 }
				 }
				 */
			}
		     result = JSONToResult.convert(response.toString());
			 result.setParam(new Param("opstatus", "0"));
			 result.setParam(new Param("httpStatusCode", "200"));
		return result;
	}
	public Result postProcessResponse(JSONObject errorResponse) {
		logger.debug("BCT::ManualMerchantFormFieldsPostprocessor::postProcessResponse:jsonObj:" + errorResponse);
		String JsonString="{\"RETURN_CONFIRMPAID_NATIVE\":{\"CODE\":0,\"MESSAGE\":\"AmountPaidSuccessfully\",\"TRACEID\":57153127,\"PARTNERTXNID\":171396151548909,\"SYSTEMTXNID\":97322,\"SCNO\":\"061.13.057H6\",\"CUSTOMERNAME\":\"Mrs.RAJYASHRISTHAPIT\",\"DUE_BILL_OF\":\"Chaitra/2080\",\"PAYABLE_AMOUNT\":516,\"CONSUMER_ID\":104021295,\"OFF_CODE\":207,\"OFFICE\":\"PULCHOWKBRANCH\",\"BILL_DATE\":\"26-MAR-24\",\"NO_OF_DAYS\":30,\"BILL_AMT\":491,\"FINE_RATE\":5,\"REBATE\":\"P\",\"PAID_AMT\":516,\"AMOUNT_DUE_LEFT\":0,\"PAID_DATE\":\"4/24/202412:00:00AM\"}}}}";
		JSONObject staticResult= new JSONObject(JsonString) ;
		logger.debug("BCT::ManualMerchantFormFieldsPostprocessor::postProcessResponse:jsonObj:" + staticResult);
        Result result = JSONToResult.convert(staticResult.toString());
        return result;
	}

}
