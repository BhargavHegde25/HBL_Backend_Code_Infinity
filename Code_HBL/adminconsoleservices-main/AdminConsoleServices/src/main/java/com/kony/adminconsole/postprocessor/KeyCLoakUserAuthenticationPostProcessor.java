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

import net.minidev.json.JSONArray;

/**
 * Postprocessor for the User Authentication JSON Service. <br>
 * Sets the httpStatusCode and the attribute {@link FabricConstants.IS_AUTHENTICATED_KEY} to true if the authentication
 * is successful
 * 
 * @author Sri kavya Pitchika
 *
 */
public class KeyCLoakUserAuthenticationPostProcessor implements DataPostProcessor2 {

    private static final String USERNAME_PARAM = "username";
    private static final String ROLES_PARAM = "roles";

    @Override
    public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
            throws Exception {
		Log4j2Configurator.getInstance();

        // Read Inputs
        String username = result.getParamValueByName(USERNAME_PARAM);
        if(result.hasParamByName(ROLES_PARAM)){
            JSONArray roles = (JSONArray) (result.getParamByName(ROLES_PARAM).getObjectValue());
            String roleNames=StringUtils.join(roles.toArray(), ',');
            request.setAttribute(ROLES_PARAM, roleNames);
            result.addParam(ROLES_PARAM, roleNames);
        }
        String backendErrorMessage = result.getParamValueByName(FabricConstants.BACKEND_ERROR_MESSAGE_KEY);
        String backendErrorCode = result.getParamValueByName(FabricConstants.BACKEND_ERROR_CODE_KEY);

        if (StringUtils.isNotBlank(backendErrorCode) || StringUtils.isNotBlank(backendErrorMessage)
                || StringUtils.isBlank(username)) {
            // Authentication Failure
            result.addParam(new Param(FabricConstants.HTTP_STATUS_CODE,
                    Integer.toString(HttpServletResponse.SC_UNAUTHORIZED), FabricConstants.INT));
        } else {
            // Authentication Success
            request.setAttribute(FabricConstants.IS_AUTHENTICATED_KEY, String.valueOf(true));
            result.addParam(new Param(FabricConstants.HTTP_STATUS_CODE, Integer.toString(HttpServletResponse.SC_OK),
                    FabricConstants.INT));
        }

        return result;
    }
}
