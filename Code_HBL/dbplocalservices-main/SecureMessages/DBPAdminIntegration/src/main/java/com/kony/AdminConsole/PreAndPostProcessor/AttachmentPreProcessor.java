package com.kony.AdminConsole.PreAndPostProcessor;

import java.util.HashMap;

import com.kony.AdminConsole.Utilities.AdminConsoleOperations;
import com.kony.AdminConsole.Utilities.ServiceConfig;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class AttachmentPreProcessor implements DataPreProcessor2 {

    @SuppressWarnings("rawtypes")
    @Override
    public boolean execute(HashMap inputMap, DataControllerRequest dcRequest, DataControllerResponse dcResponse,
            Result result) throws Exception {
        String authToken = AdminConsoleOperations.login(dcRequest);
        ServiceConfig.setValue("Auth_Token", authToken);
        dcRequest.getSession().setAttribute("backendToken", authToken);
        return true;
    }

}
