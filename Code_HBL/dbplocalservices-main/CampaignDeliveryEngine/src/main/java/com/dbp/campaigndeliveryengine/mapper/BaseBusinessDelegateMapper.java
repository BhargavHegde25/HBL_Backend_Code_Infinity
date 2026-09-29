package com.dbp.campaigndeliveryengine.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.campaigndeliveryengine.businessdelegate.api.DeliveryCampaignBusinessDelegate;
import com.dbp.campaigndeliveryengine.businessdelegate.api.ServeEventBusinessDelegate;
import com.dbp.campaigndeliveryengine.businessdelegate.impl.DeliveryCampaignBusinessDelegateImpl;
import com.dbp.campaigndeliveryengine.businessdelegate.impl.ServeEventBusinessDelegateImpl;
import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;

public class BaseBusinessDelegateMapper implements DBPAPIMapper<BusinessDelegate> {

	@Override
    public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {
        Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();
        map.put(DeliveryCampaignBusinessDelegate.class, DeliveryCampaignBusinessDelegateImpl.class);
        map.put(ServeEventBusinessDelegate.class, ServeEventBusinessDelegateImpl.class);
        return map;
    }

}
