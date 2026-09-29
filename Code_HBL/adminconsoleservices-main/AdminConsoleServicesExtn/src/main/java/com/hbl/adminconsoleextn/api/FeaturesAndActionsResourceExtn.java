package com.hbl.adminconsoleextn.api;

import com.kony.adminconsole.service.featuresandactions.resource.api.FeaturesAndActionsResource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface FeaturesAndActionsResourceExtn extends FeaturesAndActionsResource{

	/**
     *  This method edits the existing feature and actions
     *  @author JD115090
     *  @version 1.0
     *  @param methodId contains the operation id
     *  @param inputArray contains the input parameter to edit existing feature and actions
     *  @param request contains request handler
     *  @param response contains the response handler
     *  @return Result object contains the edited details
     */
	public Result editFeatureAndActions(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
}
