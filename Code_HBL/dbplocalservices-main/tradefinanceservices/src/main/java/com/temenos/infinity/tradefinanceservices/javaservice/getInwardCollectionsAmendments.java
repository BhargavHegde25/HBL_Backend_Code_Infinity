/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.javaservice;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.temenos.dbx.product.commons.dto.FilterDTO;
import com.temenos.infinity.tradefinanceservices.resource.api.InwardCollectionAmendmentsResource;
import org.json.JSONObject;
import com.kony.dbputilities.util.Log4j2Configurator;

import java.util.Map;

public class getInwardCollectionsAmendments implements JavaService2 {
    private final InwardCollectionAmendmentsResource requestBusinessDelegate = DBPAPIAbstractFactoryImpl.getResource(InwardCollectionAmendmentsResource.class);

    @Override
    public Object invoke(String methodId, Object[] objects, DataControllerRequest dataControllerRequest,
                         DataControllerResponse dataControllerResponse) throws Exception {
		Log4j2Configurator.getInstance();

        Map<String, Object> inputParams = (Map<String, Object>) objects[1];
        FilterDTO filterDTO = JSONUtils.parse(new JSONObject(inputParams).toString(), FilterDTO.class);
        return requestBusinessDelegate.getInwardCollectionAmendments(filterDTO, dataControllerRequest);
    }
}
