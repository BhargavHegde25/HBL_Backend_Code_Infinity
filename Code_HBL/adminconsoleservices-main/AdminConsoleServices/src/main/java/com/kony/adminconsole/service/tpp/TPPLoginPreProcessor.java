package com.kony.adminconsole.service.tpp;

import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class TPPLoginPreProcessor implements DataPreProcessor2 {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
	private static final String INPUT_TOKEN = "token";

	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		
		try {
			
			String jwt_token = request.getParameter(INPUT_TOKEN);
			if(StringUtils.isBlank(jwt_token)) {
				diagnostic.prepareDebug("Missing or Invalid Token found in request").log();
				result.addIntParam(Param.HTTP_STATUS_CODE, HttpServletResponse.SC_BAD_REQUEST);
				result.addParam(Param.ERR_MSG, "Missing or Invalid Token in request");
				result.addParam(FabricConstants.BACKEND_ERROR_MESSAGE_KEY, "Missing or Invalid Token in request");
				result.addIntParam(FabricConstants.BACKEND_ERROR_CODE_KEY, ErrorCodeEnum.ERR_20000.getErrorCode());
				return false;
			}
			
			String token = jwt_token.split("\\.")[0];
			String decodedTokenHeader = CommonUtilities.decodeFromBase64(token);
			JSONObject decodedTokenHeaderJson = CommonUtilities.getStringAsJSONObject(decodedTokenHeader);
			
			String kid = decodedTokenHeaderJson.getString("kid");
			request.addRequestParam_("kid", kid);
			request.addRequestParam_("jwt_token", jwt_token);
			
		}catch(Exception exp) {
			
			alert.prepareError("Encountered exception within TPPLoginPreProcessor: "+exp).log();
			result.addIntParam(Param.HTTP_STATUS_CODE, HttpServletResponse.SC_UNAUTHORIZED);
			result.addParam(Param.ERR_MSG, "Invalid Token in request");
			result.addParam(FabricConstants.BACKEND_ERROR_MESSAGE_KEY, "Invalid Token in request");
			result.addIntParam(FabricConstants.BACKEND_ERROR_CODE_KEY, ErrorCodeEnum.ERR_20001.getErrorCode());
			
			return false;
		}
		
		return true;
	}

}
