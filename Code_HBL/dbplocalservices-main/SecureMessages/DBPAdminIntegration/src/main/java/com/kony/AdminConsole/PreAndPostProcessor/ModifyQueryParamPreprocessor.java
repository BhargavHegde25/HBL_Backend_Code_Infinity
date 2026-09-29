package com.kony.AdminConsole.PreAndPostProcessor;

import java.util.HashMap;

import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class ModifyQueryParamPreprocessor implements DataPreProcessor2 {

    @SuppressWarnings("unchecked")
    @Override
    public boolean execute(@SuppressWarnings("rawtypes") HashMap input, DataControllerRequest dcRequest,
            DataControllerResponse dcRespose,
            Result result) throws Exception {
        input.put("filter", input.get("$filter"));
        return true;
    }

}
