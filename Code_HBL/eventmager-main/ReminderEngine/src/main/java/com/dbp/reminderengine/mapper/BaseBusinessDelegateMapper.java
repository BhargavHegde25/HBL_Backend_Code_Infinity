package com.dbp.reminderengine.mapper;

import java.util.HashMap;
import java.util.Map;
import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.dbp.reminderengine.businessdelegate.api.CustomerDetailsBusinessDelegate;
import com.dbp.reminderengine.businessdelegate.api.ProcessAlertsBusinessDelegate;
import com.dbp.reminderengine.businessdelegate.impl.CustomerDetailsBusinessDelegateImpl;
import com.dbp.reminderengine.businessdelegate.impl.ProcessAlertsBusinessDelegateImpl;


public class BaseBusinessDelegateMapper implements DBPAPIMapper<BusinessDelegate> {

	@Override
    public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {
        Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();
        
        map.put(CustomerDetailsBusinessDelegate.class, CustomerDetailsBusinessDelegateImpl.class);
        map.put(ProcessAlertsBusinessDelegate.class, ProcessAlertsBusinessDelegateImpl.class);
        
        return map;
    }

}
