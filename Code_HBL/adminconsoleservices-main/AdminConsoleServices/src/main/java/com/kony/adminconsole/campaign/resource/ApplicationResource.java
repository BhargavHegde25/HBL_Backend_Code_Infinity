package com.kony.adminconsole.campaign.resource;

import com.dbp.core.api.Resource;
import com.kony.adminconsole.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;

public interface ApplicationResource extends Resource {

	String getApplicationBankReference(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws ApplicationException;
}
