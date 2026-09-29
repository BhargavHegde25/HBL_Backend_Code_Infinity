package com.kony.adminconsole.service.sfs;


import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.core.config.EnvironmentConfiguration;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class SFSpotlightKCUserLoginPreProcessor2 implements DataPreProcessor2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
	
	private static final String SERVICE_ID = "SFSpotlightKCUserinfo";
	private static final String OPERATION_ID = "getToken";
	private static final String PARAM_REFRESH_TOKEN = "refresh_token";
	
	
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		try (CloseableHttpClient httpclient = HttpClients.createDefault())
        {
            String refresh_token = request.getParameter("refresh_token");
            
            Map<String, Object> inputmap = new HashMap<>();
            inputmap.put("grant_type", PARAM_REFRESH_TOKEN);
            inputmap.put("client_id", EnvironmentConfiguration.SFS_KEYCLOAK_SERVICE_CLIENT_ID.getValue(request));
            inputmap.put("client_secret", EnvironmentConfiguration.SFS_KEYCLOAK_SERVICE_CLIENT_SECRET.getValue(request));
            inputmap.put("refresh_token", refresh_token);
            inputmap.put("redirect_uri", EnvironmentConfiguration.KEYCLOAK_SERVICE_REDIRECT_URI.getValue(request));
            
            JSONObject responseObj = getAccessTokenfromRefreshToken(inputmap);

            if(null != responseObj && responseObj.has("error") && StringUtils.isNotBlank(responseObj.getString("error"))) {
            	
            	result.addIntParam(Param.HTTP_STATUS_CODE, HttpServletResponse.SC_UNAUTHORIZED);
    			result.addParam(Param.ERR_MSG, responseObj.getString("error"));
    			result.addParam(FabricConstants.BACKEND_ERROR_MESSAGE_KEY, responseObj.getString("error_description"));
    			result.addIntParam(FabricConstants.BACKEND_ERROR_CODE_KEY, ErrorCodeEnum.ERR_20633.getErrorCode());
    			
    			return false;
            }
            String access_token = responseObj.getString("access_token");
            request.addRequestParam_("access_token", access_token);

            String response_refresh_token = responseObj.getString("refresh_token");
            if (response_refresh_token != null && response_refresh_token.length() > 0){
            	
            	request.addRequestParam_("refresh_token", response_refresh_token);
            }

            String authorization = "Bearer " + access_token;

            request.addRequestParam_("Authorization", authorization);

        }
        catch (Exception exp){
        	
        	alert.prepareError("Encountered exception within SFSpotlightKCUserLoginPreProcessor2: "+exp).log();
        	
        	result.addIntParam(Param.HTTP_STATUS_CODE, HttpServletResponse.SC_UNAUTHORIZED);
			result.addParam(Param.ERR_MSG, exp.getMessage());
			result.addParam(FabricConstants.BACKEND_ERROR_MESSAGE_KEY, exp.getMessage());
			result.addIntParam(FabricConstants.BACKEND_ERROR_CODE_KEY, ErrorCodeEnum.ERR_20001.getErrorCode());
            
        	return false;
        }

        return true;
    }
	
	private JSONObject  getAccessTokenfromRefreshToken(Map<String, Object> inputmap) throws DBPApplicationException {

		
		String serviceResponse = DBPServiceExecutorBuilder.builder().withOperationId(OPERATION_ID)
        .withRequestParameters(inputmap).withServiceId(SERVICE_ID).withPassThroughOutput(true).build().getResponse();
		
        return CommonUtilities.getStringAsJSONObject(serviceResponse);
	}
	
}
