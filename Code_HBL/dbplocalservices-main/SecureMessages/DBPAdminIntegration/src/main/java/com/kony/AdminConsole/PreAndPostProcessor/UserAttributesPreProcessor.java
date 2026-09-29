package com.kony.AdminConsole.PreAndPostProcessor;

import java.util.HashMap;

import com.kony.dbputilities.util.MWConstants;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class UserAttributesPreProcessor implements DataPreProcessor2 {

    @Override
    public boolean execute(@SuppressWarnings("rawtypes") HashMap inputArray, DataControllerRequest request,
            DataControllerResponse arg2,
            Result result) throws Exception {

        String username = (String) inputArray.get("username");
        result.addParam(new Param("username", username, MWConstants.STRING));
        return false;
    }

}
