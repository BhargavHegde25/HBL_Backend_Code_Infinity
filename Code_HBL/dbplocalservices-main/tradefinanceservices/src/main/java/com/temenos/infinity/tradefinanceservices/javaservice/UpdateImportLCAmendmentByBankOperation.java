package com.temenos.infinity.tradefinanceservices.javaservice;

import java.util.HashMap;
import java.util.Map;

import com.kony.dbputilities.util.Log4j2Configurator;

import com.temenos.infinity.tradefinanceservices.dto.LetterOfCreditsAmendmentDTO;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.temenos.infinity.tradefinanceservices.resource.api.CreateLetterOfCreditsResource;

public class UpdateImportLCAmendmentByBankOperation implements JavaService2 {

    public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request, DataControllerResponse response) throws Exception {
        Log4j2Configurator.getInstance();
        CreateLetterOfCreditsResource requestResource = DBPAPIAbstractFactoryImpl.getResource(CreateLetterOfCreditsResource.class);
        Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
        LetterOfCreditsAmendmentDTO inputDto = JSONUtils.parse(new JSONObject(inputParams).toString(), LetterOfCreditsAmendmentDTO.class);
        return requestResource.updateImportLCAmendmentByBank(inputDto, request);
    }
}