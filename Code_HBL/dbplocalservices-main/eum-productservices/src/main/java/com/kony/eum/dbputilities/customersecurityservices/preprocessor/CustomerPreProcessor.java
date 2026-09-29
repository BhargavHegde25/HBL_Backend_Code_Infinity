package com.kony.eum.dbputilities.customersecurityservices.preprocessor;

import java.util.HashMap;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class CustomerPreProcessor implements DataPreProcessor2 {

	@SuppressWarnings("rawtypes")
	@Override
	public boolean execute(HashMap inputParams, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		Log4j2Configurator.getInstance();

		String idmConfig = (String) inputParams.get(DBPUtilitiesConstants.IDM_IDENTIFIER);
		if (StringUtils.isBlank(idmConfig)) {
			idmConfig = request.getParameter(DBPUtilitiesConstants.IDM_IDENTIFIER);
		}

		if (StringUtils.isNotBlank(idmConfig) && !idmConfig.equals(DBPUtilitiesConstants.DBX)) {
			ErrorCodeEnum.ERR_10161.setErrorCode(result);
			return false;
		}

		return true;
	}

}
