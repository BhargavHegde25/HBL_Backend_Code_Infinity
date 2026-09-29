package com.temenos.infinity.api.cards.mapper;
import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.infinity.api.cards.businessdelegate.api.CardServicesBusinessDelegate;
import com.temenos.infinity.api.cards.businessdelegate.impl.CardServicesDBXBusinessDelegateImpl;



/**
 * 
 * @author KH2394
 * version 1.0
 * implements {@link DBPAPIMapper}
 */

public class CardServicesDBXBusinessDelegateMapper implements DBPAPIMapper<BusinessDelegate>{
	@Override
	public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {
		Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();

		//Add Mapping of Business Delegates interface and their implementation
		map.put(CardServicesBusinessDelegate.class, CardServicesDBXBusinessDelegateImpl.class);

		return map;
	}
}
