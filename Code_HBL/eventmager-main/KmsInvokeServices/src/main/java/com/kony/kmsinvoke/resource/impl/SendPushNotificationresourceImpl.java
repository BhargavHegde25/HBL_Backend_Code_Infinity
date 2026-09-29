package com.kony.kmsinvoke.resource.impl;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.google.gson.JsonParser;
import com.kony.kmsinvoke.businessdelegate.api.SendPushNotificationBusinessDelegate;
import com.kony.kmsinvoke.resource.api.SendPushNotificationResource;
import com.kony.kmsinvoke.util.HelperMethods;
import com.kony.kmsinvoke.util.KmsInvokeConstants;
import com.kony.kmsinvoke.util.KmsInvokeEnum;
import com.kony.kmsinvoke.util.StaticDataHolder;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;

public class SendPushNotificationresourceImpl implements SendPushNotificationResource {

	@Override
	public Result sendPushNotification(Object[] inputArray, DataControllerRequest request) {
		if (StaticDataHolder.getSchemaname() == null) {
			try {
				StaticDataHolder.setSchemaname(HelperMethods.getConfigProperty(KmsInvokeConstants.DBPARAM));
			} catch (Exception e) {
				StaticDataHolder.setSchemaname(KmsInvokeConstants.DEFAULTDB);
			}
		}
		String inputparams = request.getParameter(KmsInvokeConstants.INPUTPARAMS);

		if (!HelperMethods.isValidInput(inputparams))
			return HelperMethods.returnResult(false, KmsInvokeEnum.ERROR_INVALID);

		SendPushNotificationBusinessDelegate pushNotificationBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(SendPushNotificationBusinessDelegate.class);
		return JSONToResult.convert(
				pushNotificationBusinessDelegate.sendPushNotification(new JsonParser().parse(inputparams).getAsJsonObject()).toString());
	}

}
