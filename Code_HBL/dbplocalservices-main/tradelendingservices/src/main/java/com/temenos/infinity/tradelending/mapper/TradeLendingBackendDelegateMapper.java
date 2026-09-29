/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2024. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradelending.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.infinity.tradelending.backenddelegate.api.DrawdownRequestBackendDelegate;
import com.temenos.infinity.tradelending.backenddelegate.api.RolloverRequestBackendDelegate;
import com.temenos.infinity.tradelending.backenddelegate.impl.DrawdownRequestBackendDelegateImpl;
import com.temenos.infinity.tradelending.backenddelegate.api.PaymentRequestBackendDelegate;
import com.temenos.infinity.tradelending.backenddelegate.api.RolloverRequestBackendDelegate;
import com.temenos.infinity.tradelending.backenddelegate.impl.PaymentRequestBackendDelegateImpl;
import com.temenos.infinity.tradelending.backenddelegate.impl.RolloverRequestBackendDelegateImpl;

/**
 * @author mrunalini.adepu
 *
 */
public class TradeLendingBackendDelegateMapper implements DBPAPIMapper<BackendDelegate> {

	@Override
	public Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> getAPIMappings() {
		Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> backendDelegateMap = new HashMap<>();
        backendDelegateMap.put(RolloverRequestBackendDelegate.class, RolloverRequestBackendDelegateImpl.class);
        backendDelegateMap.put(DrawdownRequestBackendDelegate.class, DrawdownRequestBackendDelegateImpl.class);

        backendDelegateMap.put(PaymentRequestBackendDelegate.class, PaymentRequestBackendDelegateImpl.class);

        return backendDelegateMap;
	}
	
	 @Override
	    public Class<BackendDelegate> getParameterizedAPITypeClass() {
	        return DBPAPIMapper.super.getParameterizedAPITypeClass();
	    }

}
