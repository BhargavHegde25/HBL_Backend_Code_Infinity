package com.temenos.dbx.eum.product.usermanagement.javaservice;

import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.eum.product.usermanagement.javaservice.RiskScoreGetOpertaion;
import com.kony.dbputilities.util.Log4j2Configurator;

public class RiskScoreGetOpertaion implements JavaService2 {
    LoggerUtil logger = new LoggerUtil(RiskScoreGetOpertaion.class);

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
        Result result = new Result();
        result.addParam("riskScore", "100", DBPUtilitiesConstants.STRING_TYPE);
        return result;
    }

}
