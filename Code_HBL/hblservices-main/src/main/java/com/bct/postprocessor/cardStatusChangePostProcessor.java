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

public class cardStatusChangePostProcessor extends BasePostProcessor {
	Logger logger = LogManager.getLogger(cardStatusChangePostProcessor.class);

	@SuppressWarnings("deprecation")
	@Override
	public Result execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		try {

			logger.debug("cardStatusChangePostProcessor Result:###" + ResultToJSON.convert(result));
			
			String errmsg = result.getParamValueByName("respCode_out");
			logger.debug("errmsg :###" + errmsg);
			
			String regex = "^(?!0{2,3}$)\\d+$";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(errmsg);

			// Find and display matches
			while (matcher.find()) {
				result.addParam("errcode", result.getParamValueByName("respCode_out"));
				result.addParam("errmsg", CardConstants.CardStausChangeErrMessage(errmsg));
			}
			
		} catch (Exception e) {
			logger.error(e);
			CommonUtils.setErrMsg(result, e.toString());
		}
		return result;
	}
}
