package com.infinity.dbx.temenos.paylater;

import java.net.URLDecoder;
import java.util.Base64.Decoder;

import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.kony.dbx.BasePostProcessor;
import com.kony.dbx.util.CommonUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class CreateInstallmentPostProcessor extends BasePostProcessor {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	public Result execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		alert.prepareError(response.getResponse()).log();
		try {
			String status = result.getParamValueByName(PayLaterConstants.PARAM_STATUS);
			String override= result.getParamValueByName(PayLaterConstants.PARAM_OVERRIDE);
			if(PayLaterConstants.PARAM_VALUE_FAILED.equalsIgnoreCase(status) && !PayLaterConstants.TRANSACTION_STATUS_LIVE.equalsIgnoreCase(result.getParamValueByName(PayLaterConstants.PARAM_TRANSACTION_STATUS)) && StringUtils.isNotBlank(override)) {
				request.addRequestParam_(PayLaterConstants.PARAM_OVERRIDE,URLDecoder.decode(override, "UTF-8"));
				result = (Result) CommonUtils.callIntegrationService(request, null, null,PayLaterConstants.SERVICE_ID_PAYLATER,
						PayLaterConstants.OPER_ID_CREATE_INSTALLMENT, true);
			}
			if(PayLaterConstants.PARAM_VALUE_SUCCESS.equalsIgnoreCase(result.getParamValueByName(PayLaterConstants.PARAM_STATUS)) && !StringUtils.EMPTY.equalsIgnoreCase(result.getParamValueByName(PayLaterConstants.PARAM_ERR_MSG))) {
				result.removeParamByName(PayLaterConstants.PARAM_ERR_MSG);
			}
			return result;
		} catch (Exception e) {
			return result;
		}
		
	}
}
	