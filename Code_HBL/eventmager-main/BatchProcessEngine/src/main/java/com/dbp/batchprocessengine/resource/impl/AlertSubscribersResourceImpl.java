package com.dbp.batchprocessengine.resource.impl;


import com.dbp.batchprocessengine.businessdelegate.api.AlertSubscribersBusinessDelegate;
import com.dbp.batchprocessengine.businessdelegate.impl.AlertSubscribersBusinessDelegateImpl;
import com.dbp.batchprocessengine.resource.api.AlertSubscribersResource;
import com.dbp.core.util.JSONUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class AlertSubscribersResourceImpl implements AlertSubscribersResource {

	@Override
	public Result getSubscribers(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result res = new Result();
		String alertTypes = request.getParameter("alertTypes");
		String corecustomerids = request.getParameter("coreCustomerIds");

		AlertSubscribersBusinessDelegate subscribersBusinessDelegate = new AlertSubscribersBusinessDelegateImpl();
		JsonArray alertsubtypes = subscribersBusinessDelegate.getSubscribers(alertTypes, corecustomerids);
		res.addParam(new Param("AlertSubTypes", alertsubtypes.toString(), "String"));
		JsonObject js = new JsonObject();
		js.add("AlertSubTypes", alertsubtypes);
		return JSONToResult.convert(js.toString());

	}

}
