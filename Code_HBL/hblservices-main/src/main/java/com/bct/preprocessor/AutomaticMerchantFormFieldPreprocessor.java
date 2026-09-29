package com.bct.preprocessor;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.util.logger.LoggerUtil;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class AutomaticMerchantFormFieldPreprocessor implements DataPreProcessor2 {
	public static LoggerUtil logger = new LoggerUtil(AutomaticMerchantFormFieldPreprocessor.class);
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest dcRequest, DataControllerResponse dcResponse, Result result)
			throws Exception {
		String merchantType=inputMap.get("merchantType")!=null?inputMap.get("merchantType").toString():"";
		logger.debug("AutomaticMerchantFormFieldPreprocessor:merchantType:"+merchantType);
		if(StringUtils.isNotBlank(merchantType) && merchantType.equalsIgnoreCase("Automatic"))
		return true;
		else {
			result.addOpstatusParam(0);
			result.addHttpStatusCodeParam(200);
		return false;
		}
	}

}
