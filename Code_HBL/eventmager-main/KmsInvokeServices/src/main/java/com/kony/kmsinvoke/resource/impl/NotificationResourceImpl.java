package com.kony.kmsinvoke.resource.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.kony.kmsinvoke.businessdelegate.api.NotificationBusinessDelegate;
import com.kony.kmsinvoke.resource.api.NotificationResource;
import com.kony.kmsinvoke.util.HelperMethods;
import com.kony.kmsinvoke.util.KmsInvokeConstants;
import com.kony.kmsinvoke.util.KmsInvokeEnum;
import com.kony.kmsinvoke.util.StaticDataHolder;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;

public class NotificationResourceImpl implements NotificationResource {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");

	@SuppressWarnings("unchecked")
	@Override
	public Result sendNotification(Object[] inputarray, DataControllerRequest request) {
		if (StaticDataHolder.getSchemaname() == null) {
			try {
				StaticDataHolder.setSchemaname(HelperMethods.getConfigProperty(KmsInvokeConstants.DBPARAM));
			} catch (Exception e) {
				StaticDataHolder.setSchemaname(KmsInvokeConstants.DEFAULTDB);
				alert.prepareError(e.toString()).log();
			}
		}
		Map<String, Object> inputmap = new HashMap<>();
		try {
			inputmap = JSONUtils.parse(request.getParameter(KmsInvokeConstants.INPUTPARAMS), Map.class);
		} catch (IOException e) {
			alert.prepareError(e.toString()).log();
			return HelperMethods.returnResult(false, KmsInvokeEnum.ERROR_INVALID);
		}
		NotificationBusinessDelegate notificationBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(NotificationBusinessDelegate.class);
		return JSONToResult.convert(notificationBusinessDelegate.sendNotification(inputmap).toString());

	}

}
