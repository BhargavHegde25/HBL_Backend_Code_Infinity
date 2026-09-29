package com.temenos.infinity.api.savingspot.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface SavingsPotResource extends Resource {
	
	/**
	 * (INFO) Fetches all savings pot's for the given funding accoutn id of user by calling business delegate method to fetch the same
	 * @param methodID
	 * @param inputArray
	 * @param request
	 * @param response
	 * @return {@link Result}
	 * @author KH1769
	 */
	  Result getAllSavingsPot(String methodID, Object[] inputArray, DataControllerRequest request,
				DataControllerResponse response);
      
	  /**
		 * (INFO)* Fetches the list of available categories for goals by calling business delegate method to fetch the same
		 * @param methodID
		 * @param inputArray
		 * @param request
		 * @param response
		 * @return {@link Result}
		 * @author 19459
		 */
	  Result getCategories(String methodID, Object[] inputArray, DataControllerRequest request,
				DataControllerResponse response);
	  
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
		 * @param methodID
		 * @param inputArray
		 * @param request
		 * @param response
		 * @return {@link Result}
		 * @author KH2347
		 * */
		public Result closeSavingsPot(String methodID, Object[] inputArray, DataControllerRequest request,
				DataControllerResponse response);
		
		/**
		 * Calls the business delegate for funding and withdrawing amount from the savingsPot with the given Id
		 * @param methodID
		 * @param inputArray
		 * @param request
		 * @param response
		 * @return {@link Result}
		 * @author KH2347
		 */
		public Result updateSavingsPotBalance(String methodID, Object[] inputArray, DataControllerRequest request,
				DataControllerResponse response);
		
		/**
		 * Calls the business delegate for updating savingsPot with the given Id
		 * @param methodID
		 * @param inputArray
		 * @param request
		 * @param response
		 * @return {@link Result}
		 * @author KH2347
		 */
		public Result updateSavingsPot(String methodID, Object[] inputArray, DataControllerRequest request,
				DataControllerResponse response);

}
