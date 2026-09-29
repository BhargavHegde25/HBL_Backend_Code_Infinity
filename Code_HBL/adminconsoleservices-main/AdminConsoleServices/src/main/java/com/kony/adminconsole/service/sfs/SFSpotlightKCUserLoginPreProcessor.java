package com.kony.adminconsole.service.sfs;

import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class SFSpotlightKCUserLoginPreProcessor implements DataPreProcessor2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");


	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {

		boolean status = false;

		try {

			String access_token = request.getParameter("access_token");
			if(StringUtils.isBlank(access_token)) {

				result.addIntParam(Param.HTTP_STATUS_CODE, HttpServletResponse.SC_BAD_REQUEST);
				result.addParam(Param.ERR_MSG, ErrorCodeEnum.ERR_20633.getMessage());
				result.addParam(FabricConstants.BACKEND_ERROR_MESSAGE_KEY, ErrorCodeEnum.ERR_20633.getMessage());
				result.addIntParam(FabricConstants.BACKEND_ERROR_CODE_KEY, ErrorCodeEnum.ERR_20633.getErrorCode());
				
				return status;
			}
			String authorization = "Bearer " + access_token;
			request.getHeaderMap().put("Authorization", authorization);
			request.addRequestParam_("Authorization", authorization);
			status = true;

		} catch (Exception exp) {
			diagnostic.prepareDebug("Encountered exception within SFSpotlightKCUserLoginPreProcessor: "+exp).log();
			result.addIntParam(Param.HTTP_STATUS_CODE, HttpServletResponse.SC_BAD_REQUEST);
			result.addParam(Param.ERR_MSG, exp.getMessage());
			result.addParam(FabricConstants.BACKEND_ERROR_MESSAGE_KEY, exp.getMessage());
			result.addIntParam(FabricConstants.BACKEND_ERROR_CODE_KEY, ErrorCodeEnum.ERR_20001.getErrorCode());

		} 
		
		return status;
	}

}
