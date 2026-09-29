/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.javaservice;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.temenos.infinity.tradefinanceservices.dto.SwiftsAndAdvisesDTO;
import com.temenos.infinity.tradefinanceservices.resource.api.LetterOfCreditSwiftsAndAdvicesResource;
import com.kony.dbputilities.util.Log4j2Configurator;

import java.util.HashMap;
import java.util.Map;

public class CreateLetterOfCreditsSwiftAndAdvices implements JavaService2 {

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
                         DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
        LetterOfCreditSwiftsAndAdvicesResource swiftAndAdvicesCreateOrder = DBPAPIAbstractFactoryImpl.getResource(LetterOfCreditSwiftsAndAdvicesResource.class);
        Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
        SwiftsAndAdvisesDTO inputDto = JSONUtils.parse(new org.json.JSONObject(inputParams).toString(), SwiftsAndAdvisesDTO.class);
        return swiftAndAdvicesCreateOrder.createLetterOfCreditSwiftAndAdvices(inputDto, request);
    }
}
