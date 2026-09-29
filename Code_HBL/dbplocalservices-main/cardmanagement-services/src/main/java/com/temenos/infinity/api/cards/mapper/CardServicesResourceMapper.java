package com.temenos.infinity.api.cards.mapper;
import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;
import com.temenos.infinity.api.cards.resource.api.CardServicesResource;
import com.temenos.infinity.api.cards.resource.impl.CardServicesResourceImpl;

public class CardServicesResourceMapper implements DBPAPIMapper<Resource> {
	@Override
	public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {
		Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();

		//Add Mapping of Business Delegates interface and their implementation
		map.put(CardServicesResource.class, CardServicesResourceImpl.class);

		return map;
	}
}
