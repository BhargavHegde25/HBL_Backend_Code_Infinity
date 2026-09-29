package com.kony.adminconsole.service.approvalworkflow.javaservices;

import com.kony.adminconsole.commons.utils.JSONUtils;
import com.kony.adminconsole.core.security.LoggedInUserHandler;
import com.kony.adminconsole.utilities.ACConstants;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.api.processor.IdentityHandler;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.kony.dbputilities.util.Log4j2Configurator;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class CheckUserPermission implements JavaService2 {
    @Override
    public Object invoke(String s, Object[] objects, DataControllerRequest dataControllerRequest, DataControllerResponse dataControllerResponse) throws Exception {
		Log4j2Configurator.getInstance();
        Result res = new Result();
        Map<String, Object> input = (Map<String, Object>) objects[1];
        String approvalPermissionName = input.containsKey("approvalPermissionName") ? input.get("approvalPermissionName").toString() : "";
        if(approvalPermissionName == null || approvalPermissionName.trim().equals("")) {
            return getResultWithErrorParams(res, "Bad Request: Invalid input", "-1", "400");
        }
        Set<String> permissions = LoggedInUserHandler.getLoggedInUserPermissions(dataControllerRequest);

        if(permissions.size()>0) {
            res.addParam(ACConstants.HAS_PERMISSION, String.valueOf(permissions.contains(approvalPermissionName)));
            res.addParam(ACConstants.OPSTATUS, "0");
            res.addParam(ACConstants.HTTP_STATUS_CODE, "200");
        }
        return res;
    }

    Result getResultWithErrorParams(Result res,String msg, String opstatus, String code) {
        res.addParam(ACConstants.ERRMSG, msg);
        res.addParam(ACConstants.OPSTATUS, opstatus);
        res.addParam(ACConstants.HTTP_STATUS_CODE, code);
        return res;
    }
}
