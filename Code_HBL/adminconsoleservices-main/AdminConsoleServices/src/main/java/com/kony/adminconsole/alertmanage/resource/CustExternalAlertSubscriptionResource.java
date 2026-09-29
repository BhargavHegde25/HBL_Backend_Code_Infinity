package com.kony.adminconsole.alertmanage.resource;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface CustExternalAlertSubscriptionResource extends Resource {

	Result registerExtnlSubcription(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);

}
