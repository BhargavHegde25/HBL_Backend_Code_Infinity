package com.temenos.dbx.eum.sca.preprocessor;

import java.util.HashMap;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.eum.sca.SCAConstants;

public class ResendActivationCodePreProcessor implements DataPreProcessor2{

	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest dcRequest, DataControllerResponse dcResponse, Result result)
			throws Exception {
		Log4j2Configurator.getInstance();
		boolean isSCAEnabled = Boolean.parseBoolean(EnvironmentConfigurationsHandler.getServerProperty(SCAConstants.ENV_IS_SCA_ENABLED));
		if(isSCAEnabled)
			inputMap.put("sendEmail", Boolean.FALSE.toString());
		return true;
	}

}
