package com.bct.postprocessor;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.kony.dbx.BasePostProcessor;
import com.kony.dbx.util.CommonUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.convertions.ResultToJSON;
import com.konylabs.middleware.dataobject.Result;

public class GetTransactionStatusPostProcessor extends BasePostProcessor {

	Logger logger = LogManager.getLogger(GetTransactionStatusPostProcessor.class);

	@SuppressWarnings("deprecation")
	@Override
	public Result execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		try {

			logger.debug("getCVVPostProcessor Result:###" + ResultToJSON.convert(result));
			String transactionStatus = result.getParamValueByName("transactionStatus");
			if (StringUtils.isNotBlank(transactionStatus) && !transactionStatus.equalsIgnoreCase("Complete")) {

				result.addParam("dbpErrCode", "Failed to fetch payment orders");
				result.addParam("dbpErrMsg",
						"We were unable to process your payment at this movement please try after some time. If the amount has been debited, it will be credited back to your account within 5 business days. We apologize for any inconvenience.");
			}
		} catch (Exception e) {
			logger.error(e);
			CommonUtils.setErrMsg(result, e.toString());
		}
		return result;
	}
}
