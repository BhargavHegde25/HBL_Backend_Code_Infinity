package com.infinity.dbx.temenos;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.infinity.dbx.temenos.auth.Authentication;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.kony.dbputilities.util.TokenUtils;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class TemenosPreLoginBasePreProcessor implements DataPreProcessor2, TemenosConstants {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings("unchecked")
	@Override
	public boolean execute(HashMap params, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		request.addRequestParam_(TemenosConstants.FLOW_TYPE, TemenosConstants.PRE_LOGIN_FLOW);
		String authToken = TokenUtils.getT24AuthToken(request);
		/*if (StringUtils.isBlank(authToken)) {
			return false;
		}*/
		request.addRequestParam_(TemenosConstants.PARAM_AUTHORIZATION, authToken);
		alert.prepareError("user id " + params.get(USER_ID) + " Token:" + request.getParameter(PARAM_AUTHORIZATION)).log();
		return Boolean.TRUE;
	}

}