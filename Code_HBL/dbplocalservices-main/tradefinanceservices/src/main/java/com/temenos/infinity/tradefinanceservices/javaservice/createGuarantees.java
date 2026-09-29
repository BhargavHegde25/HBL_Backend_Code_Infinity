/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.javaservice;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.temenos.infinity.tradefinanceservices.resource.api.GuaranteesResource;
import com.kony.dbputilities.util.Log4j2Configurator;

import java.util.Map;

public class createGuarantees implements JavaService2 {
    @Override
    public Object invoke(String methodId, Object[] objects, DataControllerRequest dataControllerRequest,
                         DataControllerResponse dataControllerResponse) throws Exception {
		Log4j2Configurator.getInstance();
        GuaranteesResource guaranteesResource = DBPAPIAbstractFactoryImpl.getResource(GuaranteesResource.class);
        Map<String, Object> inoutParams = (Map<String, Object>) objects[1];

        String operationId = dataControllerRequest.getServicesManager().getOperationData().getOperationId();
        return guaranteesResource.createGuarantees(operationId, inoutParams, dataControllerRequest);
    }
}
