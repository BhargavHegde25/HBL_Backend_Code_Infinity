package com.dbp.batchprocessengine.resource.impl;

import java.util.List;
import java.util.Set;

import com.dbp.batchprocessengine.businessdelegate.api.DataUpdateJobBusinessDelegate;
import com.dbp.batchprocessengine.businessdelegate.impl.DataUpdateJobBusinessDelegateImpl;
import com.dbp.batchprocessengine.resource.api.DataUpdateJobResource;
import com.dbp.batchprocessengine.utils.BatchProcessEngineConstants;
import com.dbp.batchprocessengine.utils.BatchProcessEngineHelperMethods;
import com.dbp.batchprocessengine.utils.BatchProcessingEnum;
import com.google.gson.JsonObject;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;

public class DataUpdateJobResourceImpl implements DataUpdateJobResource {

	@Override
	public Result generateEventsFromCoreData(DataControllerRequest request) {
		DataUpdateJobBusinessDelegate dataupdatejobBusinessDelegate = new DataUpdateJobBusinessDelegateImpl();

		String typeofservice = request.getParameter("servicetype");
		if (typeofservice == null || !isValidString(typeofservice))
			return BatchProcessEngineHelperMethods.returnResult(false, BatchProcessingEnum.ERROR_INVALID);
		List<String> eventtypes = dataupdatejobBusinessDelegate.getEventTypes(typeofservice);
		if (eventtypes.isEmpty())
			return BatchProcessEngineHelperMethods.returnResult(true, BatchProcessingEnum.ERROR_OTHER);
		Set<String> subscribers = dataupdatejobBusinessDelegate.getSubscribers(eventtypes);
		if (subscribers.isEmpty())
			return BatchProcessEngineHelperMethods.returnResult(true, BatchProcessingEnum.ERROR_OTHER);
		JsonObject status = dataupdatejobBusinessDelegate.callCoreService(subscribers, typeofservice);
		if (status.has(BatchProcessEngineConstants.DBPERRMSG)) {
			BatchProcessingEnum.ERROR_OTHER.setErrMsg(status.get(BatchProcessEngineConstants.DBPERRMSG).toString());
			return BatchProcessEngineHelperMethods.returnResult(false, BatchProcessingEnum.ERROR_OTHER);
		}
		return BatchProcessEngineHelperMethods.returnResult(true, BatchProcessingEnum.ERROR_OTHER);
	}

	private static boolean isValidString(String text) {
		if (text == null)
			return true;
		return text.matches("[a-zA-Z0-9_]*");

	}
}
