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
import com.temenos.dbx.product.constants.TransactionStatusEnum;

public class NEABillpaymentPostprocessor implements DataPostProcessor2{
	LoggerUtil logger = new LoggerUtil(NEABillpaymentPostprocessor.class);
	@Override
	public Object execute(Result result, DataControllerRequest dcRequest, DataControllerResponse dcResponse) throws Exception {
		String httpResponseCode=result.getHttpStatusCodeParamValue();
		logger.debug("BCT::NEABillpaymentPostprocessor::result:" + ResultToJSON.convert(result));
		logger.debug("BCT::NEABillpaymentPostprocessor::httpResponseCode:" + httpResponseCode);
		JSONObject response= new JSONObject(ResultToJSON.convert(result));
		String dbpErrCode=result.getParamValueByName("dbpErrCode");
		if(httpResponseCode.equalsIgnoreCase("200") && result.getParamValueByName("success").equalsIgnoreCase("true")) {
			response.put("responseFieldMapping", generateResponseFieldMapping(response).toString());
			}
		if(StringUtils.isNotBlank(dbpErrCode)  && dbpErrCode.equalsIgnoreCase("20001")){ // Transaction Reversed
			response.put("responseFieldMapping", generateReversalResponseFieldMapping(response).toString());
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
	public JSONObject generateResponseFieldMapping(JSONObject response) {
		JSONObject responseFieldMapping= new JSONObject();
		responseFieldMapping.put("customerName", "Customer Name");
		responseFieldMapping.put("consumerId", "Consumer Id");
		responseFieldMapping.put("scno", "Service No");
		responseFieldMapping.put("office", "Office");
		responseFieldMapping.put("billAmt", "Bill Amount");
		responseFieldMapping.put("paidAmount", "Paid Amount");
		responseFieldMapping.put("paidDate", "Paid Date");
		return responseFieldMapping;
	}
	public JSONObject generateReversalResponseFieldMapping(JSONObject response) {
		JSONObject responseFieldMapping= new JSONObject();
		responseFieldMapping.put("scno", "Service No");
		responseFieldMapping.put("consumerId", "Consumer Id");
		responseFieldMapping.put("counterValue", "Office");
		responseFieldMapping.put("transactionAmount", "Bill Amount");
		return responseFieldMapping;
	}


}
