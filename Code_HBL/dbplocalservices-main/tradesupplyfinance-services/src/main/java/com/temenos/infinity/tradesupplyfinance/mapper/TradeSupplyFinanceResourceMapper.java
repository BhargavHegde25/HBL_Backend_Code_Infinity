/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2023. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradesupplyfinance.mapper;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;
import com.temenos.infinity.tradesupplyfinance.resource.api.*;
import com.temenos.infinity.tradesupplyfinance.resource.impl.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author k.meiyazhagan
 */
public class TradeSupplyFinanceResourceMapper implements DBPAPIMapper<Resource> {

    @Override
    public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {
        Map<Class<? extends Resource>, Class<? extends Resource>> resourcesMap = new HashMap<>();
        resourcesMap.put(DocumentsResource.class, DocumentsResourceImpl.class);
        resourcesMap.put(AnchorFundingRequestResource.class, AnchorFundingRequestResourceImpl.class);
        resourcesMap.put(AnchorInvoiceResource.class, AnchorInvoiceResourceImpl.class);
        resourcesMap.put(CounterPartyInvoiceResource.class, CounterPartyInvoiceResourceImpl.class);
        resourcesMap.put(PaymentAllocationResource.class, PaymentAllocationResourceImpl.class);

        return resourcesMap;
    }

    @Override
    public Class<Resource> getParameterizedAPITypeClass() {
        return DBPAPIMapper.super.getParameterizedAPITypeClass();
    }
}
