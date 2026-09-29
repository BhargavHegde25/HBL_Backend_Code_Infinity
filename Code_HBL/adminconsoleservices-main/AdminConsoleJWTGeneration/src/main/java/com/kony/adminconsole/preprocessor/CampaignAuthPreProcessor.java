package com.kony.adminconsole.preprocessor;

import java.util.HashMap;
import java.util.Iterator;
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.commons.utils.JSONInputValidatorUtil;
import com.kony.adminconsole.constants.TemenosConstantsC360;
import com.kony.adminconsole.jwt.auth.utils.CommonUtilsC360;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class CampaignAuthPreProcessor implements DataPreProcessor2{

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	
	@Override
	public boolean execute(HashMap params, DataControllerRequest request, DataControllerResponse response, Result result)
			throws Exception {
		Log4j2Configurator.getInstance();
		
		try {
			removeExtraSymbols(request);
			if(!validRequestInputJSON(request)) {
				result.addParam("errmsg", "Invalid input payload");
				return Boolean.FALSE;
			}
			return CommonUtilsC360.setCloudAuthenticationHeaders(request, TemenosConstantsC360.CAMPAIGN_DEPLOYMENT_PLATFORM,TemenosConstantsC360.CAMPAIGN_AUTHORIZATION_KEY ) 
					&& CommonUtilsC360.setAuthenticationHeader(request, TemenosConstantsC360.POST_LOGIN_FLOW, FabricConstants.PRODUCT_TYPE_MS);	
		}catch (Exception e) {
			alert.prepareError("Exception in validating authentication headers - CampaignAuthPreProcessor : ",e).log();
			return Boolean.FALSE;
		}
	}
	
	private void removeExtraSymbols(DataControllerRequest request) {
		Iterator<String> parameters = request.getParameterNames();
		while (parameters.hasNext()) {
			String key = parameters.next();
			if (StringUtils.isNotBlank(request.getParameter(key))) {
				String paramJson = request.getParameter(key).toString();
				request.addRequestParam_(key, paramJson);
			}
			
		}
		
	}

	public static boolean validRequestInputJSON(DataControllerRequest request) {
		Iterator<String> parameters = request.getParameterNames();
		while (parameters.hasNext()) {
			String paramJson = request.getParameter(parameters.next());
			if (StringUtils.isNotBlank(paramJson)) {
				if (!JSONInputValidatorUtil.isValidNestedJsonInput(paramJson)) {
					return false;
				}
			}
		}
		return true;
	}
}
