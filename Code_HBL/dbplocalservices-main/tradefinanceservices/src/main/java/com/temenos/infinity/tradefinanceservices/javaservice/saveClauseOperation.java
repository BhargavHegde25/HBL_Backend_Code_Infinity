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
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.kony.dbputilities.util.Log4j2Configurator;

import java.util.HashMap;
import java.util.Map;

public class saveClauseOperation implements JavaService2 {
    @Override
    public Object invoke(String s, Object[] objects, DataControllerRequest dataControllerRequest, DataControllerResponse dataControllerResponse) throws Exception {
		Log4j2Configurator.getInstance();
        HashMap inputParams = (HashMap) objects[1];
        Map<String, Object> customer = CustomerSession.getCustomerMap(dataControllerRequest);
        inputParams.put("createdBy", CustomerSession.getCustomerId(customer));
        Result result = HelperMethods.callApi(dataControllerRequest, inputParams, HelperMethods.getHeaders(dataControllerRequest), URLConstants.SAVE_CLAUSE);

        return result;
    }
}
