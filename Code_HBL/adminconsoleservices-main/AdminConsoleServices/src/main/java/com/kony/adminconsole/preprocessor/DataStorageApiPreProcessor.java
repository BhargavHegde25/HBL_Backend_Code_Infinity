package com.kony.adminconsole.preprocessor;

import com.konylabs.middleware.common.DataPreProcessor2;
import java.util.HashMap;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.adminconsole.commons.utils.AuthenticationC360;
import com.kony.adminconsole.constants.TemenosConstantsC360;
import com.kony.adminconsole.core.config.EnvironmentConfiguration;
import com.kony.adminconsole.jwt.auth.AuthConstantsC360;
import com.kony.adminconsole.jwt.auth.utils.CommonUtilsC360;
import com.kony.adminconsole.utilities.ACConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class DataStorageApiPreProcessor implements DataPreProcessor2 {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
	
@SuppressWarnings({ "unchecked", "rawtypes" })
@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response, Result result)
			throws Exception {
		Log4j2Configurator.getInstance();
		try {
			String coreInfo = (String) inputMap.get(TemenosConstantsC360.CORE_IDENTIFIER);
			if (StringUtils.isNotBlank(coreInfo)) {
				String userID = CommonUtilsC360.getBackendId(coreInfo, TemenosConstantsC360.CONSTANT_TEMPLATE_NAME);
				if (StringUtils.isNotBlank(userID)) {
					inputMap.put(TemenosConstantsC360.USER_ID, userID);
				}
			}
			request.addRequestParam_(TemenosConstantsC360.FLOW_TYPE, TemenosConstantsC360.PRE_LOGIN_FLOW);
			request.addRequestParam_(TemenosConstantsC360.ROLE_ID, AuthConstantsC360.PROP_ROLE_ODMS);
			String authToken = AuthenticationC360.getAuthToken(request);
			if (StringUtils.isBlank(authToken)) {
				alert.prepareError("Error - JWT authToken generated for OriginationDataMS is empty").log();
				return Boolean.FALSE;
			}
			request.addRequestParam_(TemenosConstantsC360.PARAM_AUTHORIZATION, authToken);
			diagnostic.prepareInfo("Auth Token generated from OriginationDataMS PreProcessor" + authToken).log();
			

			String deploymentPlatform = EnvironmentConfiguration.ODMS_DEPLOYMENT_PLATFORM.getValue(request);
			if (StringUtils.isNotBlank(deploymentPlatform)) {
				if (StringUtils.equals(deploymentPlatform, ACConstants.AWS))
					request.addRequestParam_("x-api-key",
							EnvironmentConfiguration.ODMS_AUTHORIZATION_KEY.getValue(request));
				if (StringUtils.equals(deploymentPlatform, ACConstants.AZURE))
					request.addRequestParam_("x-functions-key",
							EnvironmentConfiguration.ODMS_AUTHORIZATION_KEY.getValue(request));
			}
			
			return true;
		}catch (Exception e) {
			alert.prepareError("Exception in generating JWT authToken for OriginationDataMS",e).log();
			return Boolean.FALSE;
		}
		
	}

}
