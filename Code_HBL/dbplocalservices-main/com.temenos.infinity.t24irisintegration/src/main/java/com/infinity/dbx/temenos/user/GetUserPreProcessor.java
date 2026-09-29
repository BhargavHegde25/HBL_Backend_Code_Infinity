package com.infinity.dbx.temenos.user;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;

import com.infinity.dbx.temenos.TemenosBasePreProcessor;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbx.util.EnvironmentConfigurationsHandler;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class GetUserPreProcessor extends TemenosBasePreProcessor {

    @Override
    public boolean execute(HashMap params, DataControllerRequest request, DataControllerResponse response,
            Result result) throws Exception {

        super.execute(params, request, response, result);

        request.addRequestParam_(PARAM_LOGINUSERID, params.get(PARAM_LOGINUSERID) != null? params.get(PARAM_LOGINUSERID).toString() :"");
        request.addRequestParam_(UserConstants.USERNAME, params.get(UserConstants.USERNAME) != null? params.get(UserConstants.USERNAME).toString() :"");
        if (StringUtils.isNotBlank(request.getParameter("Customer_id")) && Boolean.parseBoolean(request.getParameter("isSuperAdmin"))) {
            request.addRequestParam_(USER_ID, request.getParameter("Customer_id"));
            String legalEntityId = request.getParameter("legalEntityId");
            if(StringUtils.isBlank(legalEntityId)) {
            	ErrorCodeEnum.ERR_29040.setErrorCode(result);
            	return false;
            }
            if(null==request.getHeaderMap().get("companyId")){
                request.getHeaderMap().put("companyId", legalEntityId);
            }

            request.addRequestParam_(PARAM_LOGINUSERID, request.getParameter("Customer_id"));
            request.addRequestParam_(UserConstants.USERNAME, request.getParameter("Customer_id"));
            params.put(USER_ID, request.getParameter("Customer_id"));
            params.put(PARAM_LOGINUSERID, request.getParameter("Customer_id"));
            params.put(UserConstants.USERNAME, request.getParameter("Customer_id"));
        }
        if("false".equalsIgnoreCase(request.getParameter("isLoggedInUser"))) {
        	params.put(USER_ID, request.getParameter("Customer_id"));
        }
        
        if(params.get(USER_ID) == null || StringUtils.isBlank((String)params.get(USER_ID))) {
            return false;
        }
        
        return Boolean.TRUE;
    }
}
