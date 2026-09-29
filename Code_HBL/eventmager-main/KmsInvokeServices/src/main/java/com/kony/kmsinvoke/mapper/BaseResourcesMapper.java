package com.kony.kmsinvoke.mapper;

import java.util.HashMap;
import java.util.Map;


import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;
import com.kony.kmsinvoke.resource.api.NotificationResource;
import com.kony.kmsinvoke.resource.api.SendEmailResource;
import com.kony.kmsinvoke.resource.api.SendPushNotificationResource;
import com.kony.kmsinvoke.resource.api.SendSmsResource;
import com.kony.kmsinvoke.resource.impl.NotificationResourceImpl;
import com.kony.kmsinvoke.resource.impl.SendEmailResourceImpl;
import com.kony.kmsinvoke.resource.impl.SendPushNotificationresourceImpl;
import com.kony.kmsinvoke.resource.impl.SendSmsResourceImpl;



public class BaseResourcesMapper implements DBPAPIMapper<Resource> {

	@Override
	public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {
		Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();
		
		map.put(SendEmailResource.class,SendEmailResourceImpl.class);
		map.put(SendSmsResource.class, SendSmsResourceImpl.class);
		map.put(SendPushNotificationResource.class, SendPushNotificationresourceImpl.class);
		map.put(NotificationResource.class,NotificationResourceImpl.class);

		return map;
	}
}
