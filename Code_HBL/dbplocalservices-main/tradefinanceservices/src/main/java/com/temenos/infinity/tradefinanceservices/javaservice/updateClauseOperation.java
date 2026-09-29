/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.javaservice;

import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.kony.dbputilities.util.Log4j2Configurator;

import java.util.HashMap;

public class updateClauseOperation implements JavaService2 {
    @Override
    public Object invoke(String s, Object[] objects, DataControllerRequest dataControllerRequest, DataControllerResponse dataControllerResponse) throws Exception {
		Log4j2Configurator.getInstance();
        HashMap inputParams = (HashMap) objects[1];
        Result result = HelperMethods.callApi(dataControllerRequest, inputParams, HelperMethods.getHeaders(dataControllerRequest),
                URLConstants.UPDATE_CLAUSE);

        return result;
    }
}
