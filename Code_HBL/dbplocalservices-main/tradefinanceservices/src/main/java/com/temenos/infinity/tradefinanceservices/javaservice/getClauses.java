/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.javaservice;

import com.kony.dbputilities.util.DBPUtilitiesConstants;
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

public class getClauses implements JavaService2 {
    @Override
    public Object invoke(String s, Object[] objects, DataControllerRequest dataControllerRequest, DataControllerResponse dataControllerResponse) throws Exception {
		Log4j2Configurator.getInstance();
        HashMap<String, String> hashMap = new HashMap<>();
        Map<String, Object> customer = CustomerSession.getCustomerMap(dataControllerRequest);
        StringBuilder filter = new StringBuilder("createdBy" + DBPUtilitiesConstants.EQUAL + CustomerSession.getCustomerId(customer));
        filter.append(DBPUtilitiesConstants.OR).append("createdBy" + DBPUtilitiesConstants.EQUAL + "null");
        hashMap.put(DBPUtilitiesConstants.FILTER, filter.toString());
        Result result = HelperMethods.callApi(dataControllerRequest, new HashMap(), HelperMethods.getHeaders(dataControllerRequest),
                URLConstants.GET_CLAUSE);

        return result;
    }
}
