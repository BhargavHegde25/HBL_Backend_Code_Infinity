package com.kony.adminconsole.service.termandcondition.preprocessor;

import java.util.HashMap;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.adminconsole.constants.TemenosConstantsC360;
import com.kony.adminconsole.core.config.EnvironmentConfiguration;
import com.kony.adminconsole.jwt.auth.utils.CommonUtilsC360;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class TNCTokenGeneration implements DataPreProcessor2{

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean execute(HashMap params, DataControllerRequest request, DataControllerResponse response,
			Result result)
			throws Exception {
		Log4j2Configurator.getInstance();
		// TODO Auto-generated method stub
		try {

			String branchRef = EnvironmentConfiguration.BRANCH_ID_REFERENCE.getValue(request);
			if (StringUtils.isNotBlank(branchRef)) {
				request.addRequestParam_("branchRef", branchRef);
			} else {
				request.addRequestParam_("branchRef", "GB0010001");
			}
			return CommonUtilsC360.setCloudAuthenticationHeaders(request,TemenosConstantsC360.CONSENT_DEPLOYMENT_PLATFORM,TemenosConstantsC360.CONSENT_AUTHORIZATION_KEY);

		} catch (Exception e) {
			alert.prepareError("Exception in generating JWT authToken for Tnc", e).log();
			return Boolean.FALSE;
		}
	}
}
