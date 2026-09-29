package com.kony.bbmockservices;

import java.util.HashMap;

import com.konylabs.middleware.common.DataPreProcessor;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class FetchStatusFromACHVendorPreProcessor implements DataPreProcessor {

    @Override
    public boolean execute(HashMap inputs, DataControllerRequest dcr, Result output) throws Exception {
    	output.addParam(new Param("status", "Executed"));
    	output.addParam(new Param("confirmationNumber", inputs.get("confirmationNumber") == null ? "wrongInput" : inputs.get("confirmationNumber").toString()));
        return false;
    }
}
