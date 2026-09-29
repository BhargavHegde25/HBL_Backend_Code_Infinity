/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.javaservice;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.temenos.infinity.tradefinanceservices.resource.api.IssuedGuaranteeClaimsResource;
import com.kony.dbputilities.util.Log4j2Configurator;

import java.util.HashMap;

public class GetIssuedGuaranteeClaimsOperation implements JavaService2 {
    @Override
    public Object invoke(String methodId, Object[] objects, DataControllerRequest dataControllerRequest, DataControllerResponse dataControllerResponse) throws Exception {
		Log4j2Configurator.getInstance();
        HashMap<String, Object> inputParams = (HashMap<String, Object>) objects[1];
        IssuedGuaranteeClaimsResource claimsResource = DBPAPIAbstractFactoryImpl.getResource(IssuedGuaranteeClaimsResource.class);
        return claimsResource.getClaims(inputParams, dataControllerRequest);
    }
}