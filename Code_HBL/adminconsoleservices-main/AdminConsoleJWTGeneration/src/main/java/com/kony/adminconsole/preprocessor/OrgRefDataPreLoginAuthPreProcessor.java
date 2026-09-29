package com.kony.adminconsole.preprocessor;

import java.util.HashMap;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang.StringEscapeUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.constants.TemenosConstantsC360;
import com.kony.adminconsole.jwt.auth.AuthConstantsC360;
import com.kony.adminconsole.jwt.auth.utils.CommonUtilsC360;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class OrgRefDataPreLoginAuthPreProcessor  implements DataPreProcessor2{

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	
	@Override
	public boolean execute(HashMap params, DataControllerRequest request, DataControllerResponse response, Result result)
			throws Exception {
		Log4j2Configurator.getInstance();
		try {
			
			return CommonUtilsC360.setCloudAuthenticationHeaders(request, TemenosConstantsC360.ORDMS_DEPLOYMENT_PLATFORM,TemenosConstantsC360.ORDMS_AUTHORIZATION_KEY) 
					&& CommonUtilsC360.setAuthenticationHeader(request, TemenosConstantsC360.PRE_LOGIN_FLOW, FabricConstants.PRODUCT_TYPE_MS);	
		}catch (Exception e) {
			alert.prepareError("Exception in validating authentication headers - organisationalRefDataPreProcessor : ",e).log();
			return Boolean.FALSE;
		}
	}
	
}

