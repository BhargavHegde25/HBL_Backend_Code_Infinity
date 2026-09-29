package com.kony.kmsinvoke.service;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.kmsinvoke.resource.api.SendPushNotificationResource;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;

public class SendPushNotificationService implements JavaService2 {

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		SendPushNotificationResource pushNotificationResource = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(ResourceFactory.class).getResource(SendPushNotificationResource.class);

		return pushNotificationResource.sendPushNotification(inputArray, request);
	}

}
