package com.kony.adminconsole.service.featuresandactions.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface FeaturesAndActionsResource extends Resource{
	
	 /**
     *  This method gets all the existing limit groups
     *  @author KH2660
     *  @version 1.0
     *  @param methodId contains the operation id
     *  @param inputArray contains the input parameter to fetch existing limit groups
     *  @param request contains request handler
     *  @param response contains the response handler
     *  @return Result object contains all the limit group details
     */
	public Result fetchAllLimitGroups(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);

	 /**
     *  This method gets all the existing features
     *  @author KH2660
     *  @version 1.0
     *  @param methodId contains the operation id
     *  @param inputArray contains the input parameter to fetch existing features
     *  @param request contains request handler
     *  @param response contains the response handler
     *  @return Result object contains all the features
     */
	public Result fetchAllFeatures(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
	/**
     *  This method edits the existing limit group
     *  @author KH2660
     *  @version 1.0
     *  @param methodId contains the operation id
     *  @param inputArray contains the input parameter to edit existing limit group
     *  @param request contains request handler
     *  @param response contains the response handler
     *  @return Result object contains the edited details
     */
	public Result editLimitGroup(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
	/**
     *  This method edits the existing feature and actions
     *  @author KH2660
     *  @version 1.0
     *  @param methodId contains the operation id
     *  @param inputArray contains the input parameter to edit existing feature and actions
     *  @param request contains request handler
     *  @param response contains the response handler
     *  @return Result object contains the edited details
     */
	public Result editFeatureAndActions(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
	 /**
     *  This method gets all the existing feature actions
     *  @author KH2660
     *  @version 1.0
     *  @param methodId contains the operation id
     *  @param inputArray contains the input parameter to fetch existing feature actions
     *  @param request contains request handler
     *  @param response contains the response handler
     *  @return Result object contains all the feature actions
     */
	public Result fetchFeatureActions(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
	 /**
     *  This method gets all the existing monetary actions
     *  @author KH2660
     *  @version 1.0
     *  @param methodId contains the operation id
     *  @param inputArray contains the input parameter to fetch existing monetary actions
     *  @param request contains request handler
     *  @param response contains the response handler
     *  @return Result object contains all the monetary actions
     */
	public Result fetchAllMonetaryActions(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
	 /**
     *  This method gets all the existing feature actions by type
     *  @author KH2660
     *  @version 1.0
     *  @param methodId contains the operation id
     *  @param inputArray contains the input parameter to fetch existing feature actions by type
     *  @param request contains request handler
     *  @param response contains the response handler
     *  @return Result object contains all the feature actions
     */
	public Result fetchFeatureActionsByType(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
	 /**
     *  This method gets all the existing access policies
     *  @author KH2660
     *  @version 1.0
     *  @param methodId contains the operation id
     *  @param inputArray contains the input parameter to fetch access policies
     *  @param request contains request handler
     *  @param response contains the response handler
     *  @return Result object contains all the access policies
     */
	public Result fetchAccessPolicies(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	 /**
     *  This method gets all the existing action levels
     *  @author KH2660
     *  @version 1.0
     *  @param methodId contains the operation id
     *  @param inputArray contains the input parameter to fetch action levels
     *  @param request contains request handler
     *  @param response contains the response handler
     *  @return Result object contains all the action level
     */
	public Result fetchActionLevels(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
	 /**
     *  This method downloads the existing list of features
     *  @author KH2660
     *  @version 1.0
     *  @param methodId contains the operation id
     *  @param inputArray contains the input parameter to download the features
     *  @param request contains request handler
     *  @param response contains the response handler
     *  @return Result
     */
	public Result downloadFeaturesList(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
	 /**
     *  This method activates/deactivates an action
     *  @author KH2660
     *  @version 1.0
     *  @param methodId contains the operation id
     *  @param inputArray contains the input parameter activate/deactivate an action
     *  @param request contains request handler
     *  @param response contains the response handler
     *  @return Result
     */
	public Result manageActionStatus(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
	/**
     *  This method gets service fee for a featureId
     *  @author KH2691
     *  @param methodId contains the operation id
     *  @param inputArray contains the input parameter
     *  @param request contains request handler
     *  @param response contains the response handler
     *  @return Result
     */
	public Result getServiceFee(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
	/**
     *  This method gets all the existing account level actions
     *  @author KH2691
     *  @version 1.0
     *  @param methodId contains the operation id
     *  @param inputArray contains the input parameter to fetch existing account level actions
     *  @param request contains request handler
     *  @param response contains the response handler
     *  @return Result object contains all the monetary actions
     */
	public Result fetchAccountLevelActions(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
}
