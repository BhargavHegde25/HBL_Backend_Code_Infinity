/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2024. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradelending.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;
import com.temenos.infinity.tradelending.resource.api.DrawdownRequestResource;
import com.temenos.infinity.tradelending.resource.api.RolloverRequestResource;
import com.temenos.infinity.tradelending.resource.impl.DrawdownRequestResourceImpl;
import com.temenos.infinity.tradelending.resource.api.PaymentRequestResource;
import com.temenos.infinity.tradelending.resource.api.RolloverRequestResource;
import com.temenos.infinity.tradelending.resource.impl.PaymentRequestResourceImpl;
import com.temenos.infinity.tradelending.resource.impl.RolloverRequestResourceImpl;

/**
 * @author mrunalini.adepu
 *
 */
public class TradeLendingResourceMapper implements DBPAPIMapper<Resource> {

	@Override
	public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {
		Map<Class<? extends Resource>, Class<? extends Resource>> resourcesMap = new HashMap<>();
        resourcesMap.put(RolloverRequestResource.class, RolloverRequestResourceImpl.class);
        resourcesMap.put(DrawdownRequestResource.class, DrawdownRequestResourceImpl.class);
        
        resourcesMap.put(PaymentRequestResource.class, PaymentRequestResourceImpl.class);

        return resourcesMap;
	}

	@Override
    public Class<Resource> getParameterizedAPITypeClass() {
        return DBPAPIMapper.super.getParameterizedAPITypeClass();
    }
}
