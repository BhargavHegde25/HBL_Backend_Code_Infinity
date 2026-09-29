/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2023. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradesupplyfinance.javaservices;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.kony.dbputilities.util.Log4j2Configurator;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.temenos.infinity.tradesupplyfinance.dto.CounterPartyInvoiceDTO;
import com.temenos.infinity.tradesupplyfinance.resource.api.CounterPartyInvoiceResource;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author k.meiyazhagan
 */
public class CreateCounterPartyInvoiceOperation implements JavaService2 {
    @Override
    public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request, DataControllerResponse response) throws Exception {
        Log4j2Configurator.getInstance();
        CounterPartyInvoiceResource requestResource = DBPAPIAbstractFactoryImpl.getResource(CounterPartyInvoiceResource.class);
        Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
        CounterPartyInvoiceDTO inputDto = JSONUtils.parse(new JSONObject(inputParams).toString(), CounterPartyInvoiceDTO.class);
        return requestResource.createCounterPartyInvoice(inputDto, request);
    }
}