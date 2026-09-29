/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.javaservice;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.temenos.infinity.tradefinanceservices.dto.InwardCollectionsDTO;
import com.temenos.infinity.tradefinanceservices.resource.api.InwardCollectionsResource;
import org.json.JSONObject;
import com.kony.dbputilities.util.Log4j2Configurator;

import java.util.HashMap;

public class updateInwardCollections implements JavaService2 {
    @Override
    public Object invoke(String s, Object[] objects, DataControllerRequest dataControllerRequest,
                         DataControllerResponse dataControllerResponse) throws Exception {
		Log4j2Configurator.getInstance();
        InwardCollectionsResource inwardCollectionsResource = DBPAPIAbstractFactoryImpl.getResource(InwardCollectionsResource.class);
        HashMap<String, Object> inputParams = (HashMap<String, Object>) objects[1];
        InwardCollectionsDTO inwardCollectionsDTO = JSONUtils.parse(new JSONObject(inputParams).toString(), InwardCollectionsDTO.class);
        return inwardCollectionsResource.updateInwardCollection(inwardCollectionsDTO, dataControllerRequest);
    }
}
