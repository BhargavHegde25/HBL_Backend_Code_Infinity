package com.temenos.dbx.nonproduct.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface SavingsPotResource extends Resource{
    
	/**
	 * (INFO)calls the business delegate for creating a new savingsPot for the accountId(given in input params) of the user 
	 * @param methodID
	 * @param inputArray
	 * @param request
	 * @param response
	 * @return {@link Result}
	 * @author KH2347
	 */
	public Result createSavingsPot(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
	/**
	 * (INFO)Calls the business delegate for closing the savingsPot with the given Id
	 */
	public Result closeSavingsPot(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
	/**
	 * Calls the business delegate for funding and withdrawing amount from the savingsPot with the given Id
	 */
	public Result updateSavingsPotBalance(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
	/**
	 * Calls the business delegate for updating savingsPot with the given Id
	 */
	public Result updateSavingsPot(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
}
