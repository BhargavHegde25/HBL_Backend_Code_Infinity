/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2024. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradelending.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.infinity.tradelending.businessdelegate.api.DrawdownRequestBusinessDelegate;
import com.temenos.infinity.tradelending.businessdelegate.api.RolloverRequestBusinessDelegate;
import com.temenos.infinity.tradelending.businessdelegate.impl.DrawdownRequestBusinessDelegateImpl;
import com.temenos.infinity.tradelending.businessdelegate.api.PaymentRequestBusinessDelegate;
import com.temenos.infinity.tradelending.businessdelegate.impl.PaymentRequestBusinessDelegateImpl;
import com.temenos.infinity.tradelending.businessdelegate.impl.RolloverRequestBusinessDelegateImpl;

/**
 * @author mrunalini.adepu
 *
 */
public class TradeLendingBusinessDelegateMapper implements DBPAPIMapper<BusinessDelegate> {

	@Override
	public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {
		Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> businessDelegateMap = new HashMap<>();
        businessDelegateMap.put(RolloverRequestBusinessDelegate.class, RolloverRequestBusinessDelegateImpl.class);
        businessDelegateMap.put(DrawdownRequestBusinessDelegate.class, DrawdownRequestBusinessDelegateImpl.class);


        businessDelegateMap.put(PaymentRequestBusinessDelegate.class, PaymentRequestBusinessDelegateImpl.class);
        return businessDelegateMap;
	}

	@Override
    public Class<BusinessDelegate> getParameterizedAPITypeClass() {
        return DBPAPIMapper.super.getParameterizedAPITypeClass();
    }
}
