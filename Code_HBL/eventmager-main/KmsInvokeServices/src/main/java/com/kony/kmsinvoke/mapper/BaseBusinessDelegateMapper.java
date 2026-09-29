package com.kony.kmsinvoke.mapper;

import java.util.HashMap;
import java.util.Map;
import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.kony.kmsinvoke.businessdelegate.api.LogNotificationStatusBusinessDelegate;
import com.kony.kmsinvoke.businessdelegate.api.NotificationBusinessDelegate;
import com.kony.kmsinvoke.businessdelegate.api.SendEmailBusinessDelegate;
import com.kony.kmsinvoke.businessdelegate.api.SendPushNotificationBusinessDelegate;
import com.kony.kmsinvoke.businessdelegate.api.SendSmsBusinessDelegate;
import com.kony.kmsinvoke.businessdelegate.impl.LogNotificationStatusBusinessDelegateImpl;
import com.kony.kmsinvoke.businessdelegate.impl.NotificationBusinessDelegateImpl;
import com.kony.kmsinvoke.businessdelegate.impl.SendEmailBusinessDelegateImpl;
import com.kony.kmsinvoke.businessdelegate.impl.SendPushNotificationBusinessDelegateImpl;
import com.kony.kmsinvoke.businessdelegate.impl.SendSmsBusinessDelegateImpl;


public class BaseBusinessDelegateMapper implements DBPAPIMapper<BusinessDelegate> {

	@Override
    public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {
        Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();
        
        map.put(SendEmailBusinessDelegate.class, SendEmailBusinessDelegateImpl.class);
        map.put(SendSmsBusinessDelegate.class,SendSmsBusinessDelegateImpl.class);
        map.put(SendPushNotificationBusinessDelegate.class,SendPushNotificationBusinessDelegateImpl.class);
        map.put(NotificationBusinessDelegate.class,NotificationBusinessDelegateImpl.class);
        map.put(LogNotificationStatusBusinessDelegate.class, LogNotificationStatusBusinessDelegateImpl.class);
               
        return map;
    }

}
