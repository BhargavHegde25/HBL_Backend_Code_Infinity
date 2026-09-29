package com.kony.adminconsole.campaign.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.kony.adminconsole.campaign.businessdelegate.api.DataContextBusinessDelegate;
import com.kony.adminconsole.campaign.businessdelegate.api.DataStorageBusinessDelegate;
import com.kony.adminconsole.campaign.businessdelegate.api.UpdateUsersForSegmentsBusinessDelegate;
import com.kony.adminconsole.campaign.businessdelegate.impl.DataContextBusinessDelegateImpl;
import com.kony.adminconsole.campaign.businessdelegate.impl.DataStorageBusinessDelegateImpl;
import com.kony.adminconsole.campaign.businessdelegate.impl.UpdateUsersForSegmentBusinessDelegateImpl;

public class BaseBusinessDelegateMapper implements DBPAPIMapper<BusinessDelegate> {

    @Override
    public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {
        Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();
        map.put(UpdateUsersForSegmentsBusinessDelegate.class, UpdateUsersForSegmentBusinessDelegateImpl.class);
        map.put(DataContextBusinessDelegate.class, DataContextBusinessDelegateImpl.class);
        map.put(DataStorageBusinessDelegate.class, DataStorageBusinessDelegateImpl.class);
        return map;
    }

}
