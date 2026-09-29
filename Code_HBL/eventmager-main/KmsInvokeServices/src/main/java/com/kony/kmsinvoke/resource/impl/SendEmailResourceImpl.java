package com.kony.kmsinvoke.resource.impl;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.kmsinvoke.businessdelegate.api.SendEmailBusinessDelegate;
import com.kony.kmsinvoke.resource.api.SendEmailResource;
import com.kony.kmsinvoke.util.HelperMethods;
import com.kony.kmsinvoke.util.KmsInvokeConstants;
import com.kony.kmsinvoke.util.KmsInvokeEnum;
import com.kony.kmsinvoke.util.StaticDataHolder;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;

public class SendEmailResourceImpl implements SendEmailResource {
	public Result sendEmail(Object[] inputArray, DataControllerRequest request) {
		if (StaticDataHolder.getSchemaname() == null) {
			try {
				StaticDataHolder.setSchemaname(HelperMethods.getConfigProperty(KmsInvokeConstants.DBPARAM));
			} catch (Exception e) {
				StaticDataHolder.setSchemaname(KmsInvokeConstants.DEFAULTDB);
			}
		}
		String inputparams = request.getParameter(KmsInvokeConstants.INPUTPARAMS);
		String logparams = request.getParameter(KmsInvokeConstants.LOGPARAMS);

		if (!HelperMethods.isValidInput(inputparams))
			return HelperMethods.returnResult(false, KmsInvokeEnum.ERROR_INVALID);
		JsonObject inputParamsObj = new JsonParser().parse(inputparams).getAsJsonObject();
		SendEmailBusinessDelegate emailBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(SendEmailBusinessDelegate.class);
		JsonObject kmsEmailResponse = emailBusinessDelegate.sendEmail(inputParamsObj);
		if (!HelperMethods.isValidInput(logparams)) {
			return JSONToResult.convert(kmsEmailResponse.toString());
		}

		JsonObject logparamsObject = new JsonParser().parse(logparams).getAsJsonObject();
		if (logparamsObject.get(KmsInvokeConstants.ISALERTSENGINE) != null
				&& logparamsObject.get(KmsInvokeConstants.ISALERTSENGINE).getAsBoolean()) {
			return JSONToResult.convert(kmsEmailResponse.toString());
		}
		HelperMethods.insertToAlertHistory(inputParamsObj, logparamsObject, kmsEmailResponse,
				KmsInvokeConstants.CH_EMAIL);
		return JSONToResult.convert(kmsEmailResponse.toString());
	}

}
