package com.kony.adminconsole.postprocessor;

import javax.servlet.http.HttpServletResponse;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;

import com.kony.adminconsole.commons.utils.FabricConstants;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

/**
 * Postprocessor for the key cloak identity attributes get operation. <br>
 * This gets rid of unwanted parameters
 * 
 * @author Sri kavya Pitchika
 *
 */
public class KeyCloakIdentityEndpointPostProcessor implements DataPostProcessor2 {

    @Override
    public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
            throws Exception {
		Log4j2Configurator.getInstance();
    	// Read Inputs
        String backendErrorMessage = result.getParamValueByName(FabricConstants.BACKEND_ERROR_MESSAGE_KEY);
        String backendErrorCode = result.getParamValueByName(FabricConstants.BACKEND_ERROR_CODE_KEY);

        if (StringUtils.isNotBlank(backendErrorCode) || StringUtils.isNotBlank(backendErrorMessage)) {
            // Authentication Failure
            result.addParam(new Param(FabricConstants.HTTP_STATUS_CODE,
                    Integer.toString(HttpServletResponse.SC_UNAUTHORIZED), FabricConstants.INT));
        } else {
            // Authentication Success
            result.addParam(new Param(FabricConstants.HTTP_STATUS_CODE, Integer.toString(HttpServletResponse.SC_OK),
                    FabricConstants.INT));
            //Remove duplicate parameters from result
            if(result.hasParamByName("roles"))
           	 result.removeParamByName("roles");
            if(result.hasParamByName("preferred_username"))
           	 result.removeParamByName("preferred_username");
            if(result.hasParamByName("token_type"))
           	 result.removeParamByName("token_type");
            if(result.hasParamByName("given_name"))
           	 result.removeParamByName("given_name");
            if(result.hasParamByName("email"))
           	 result.removeParamByName("email");
            if(result.hasParamByName("family_name"))
           	 result.removeParamByName("family_name");
            if(result.hasParamByName("user_id"))
           	 result.removeParamByName("user_id");
        }
        return result;
    }

}
