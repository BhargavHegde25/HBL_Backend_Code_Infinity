package com.kony.adminconsole.alertmanage.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.kony.adminconsole.alertmanage.businessdelegate.api.CustExternalAlertSubscriptionBD;
import com.kony.adminconsole.alertmanage.businessdelegate.impl.CustExternalAlertSubscriptionBDImpl;

public class AlertBusinessDelegateMapper implements DBPAPIMapper<BusinessDelegate> {

    @Override
    public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {
        Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();
        map.put(CustExternalAlertSubscriptionBD.class, CustExternalAlertSubscriptionBDImpl.class);        
        return map;
    }

}
