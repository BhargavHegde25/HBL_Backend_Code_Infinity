package com.bct.postprocessor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bct.custom.constants.CardConstants;
import com.kony.dbx.BasePostProcessor;
import com.kony.dbx.util.CommonUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.convertions.ResultToJSON;
import com.konylabs.middleware.dataobject.Result;

public class cardChangePINPostProcessor extends BasePostProcessor {
	Logger logger = LogManager.getLogger(cardChangePINPostProcessor.class);

	@SuppressWarnings("deprecation")
	@Override
	public Result execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		try {

			logger.debug("cardChangePINPostProcessor Result:###" + ResultToJSON.convert(result));
			
			String ErrMsg = result.getParamValueByName("p_err_code");
			logger.debug("ErrMsg :###" + ErrMsg);
			
			String regex = "^(?!0{2,3}$)\\d+$";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(ErrMsg);

			// Find and display matches
			while (matcher.find()) {
				result.addParam("errcode", result.getParamValueByName("p_err_code"));
				result.addParam("errmsg", CardConstants.changePINerrMessage(ErrMsg));
			}
			
		} catch (Exception e) {
			logger.error(e);
			CommonUtils.setErrMsg(result, e.toString());
		}
		return result;
	}
}
