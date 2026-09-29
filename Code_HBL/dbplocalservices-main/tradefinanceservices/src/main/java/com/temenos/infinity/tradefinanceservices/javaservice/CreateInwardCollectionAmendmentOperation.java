/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.javaservice;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.temenos.infinity.tradefinanceservices.dto.InwardCollectionAmendmentsDTO;
import com.temenos.infinity.tradefinanceservices.resource.api.InwardCollectionAmendmentsResource;
import org.json.JSONObject;
import com.kony.dbputilities.util.Log4j2Configurator;

import java.util.HashMap;
import java.util.Map;

public class CreateInwardCollectionAmendmentOperation implements JavaService2 {
    @Override
    public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request, DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
        InwardCollectionAmendmentsResource requestResource = DBPAPIAbstractFactoryImpl.getResource(InwardCollectionAmendmentsResource.class);
        Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
        InwardCollectionAmendmentsDTO inputDto = JSONUtils.parse(new JSONObject(inputParams).toString(), InwardCollectionAmendmentsDTO.class);
        return requestResource.createInwardCollectionAmendment(inputDto, request);
    }
}
