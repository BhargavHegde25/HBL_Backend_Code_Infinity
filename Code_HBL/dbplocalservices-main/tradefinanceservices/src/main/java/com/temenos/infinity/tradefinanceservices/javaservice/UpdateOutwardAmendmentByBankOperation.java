/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2023. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.javaservice;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.temenos.infinity.tradefinanceservices.dto.OutwardCollectionAmendmentsDTO;
import com.temenos.infinity.tradefinanceservices.resource.api.OutwardCollectionAmendmentsResource;
import org.json.JSONObject;
import com.kony.dbputilities.util.Log4j2Configurator;

import java.util.HashMap;
import java.util.Map;

/**
 * @author k.meiyazhagan
 */
public class UpdateOutwardAmendmentByBankOperation implements JavaService2 {
    @Override
    public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request, DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
        OutwardCollectionAmendmentsResource requestResource = DBPAPIAbstractFactoryImpl.getResource(OutwardCollectionAmendmentsResource.class);
        Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
        OutwardCollectionAmendmentsDTO inputDto = JSONUtils.parse(new JSONObject(inputParams).toString(), OutwardCollectionAmendmentsDTO.class);
        return requestResource.updateAmendmentByBank(inputDto, request);
    }
}
