package com.temenos.infinity.api.wealthOrder.backenddelegate.api;

import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface UserFavouriteInstrumentsBackendDelegate extends BackendDelegate {

	/**
	 * 
	 * @param methodID
	 * @param inputArray
	 * @param request
	 * @param response
	 * @param headersMap
	 * @return
	 * @throws ApplicationException
	 */
	Result getFavouriteInstruments(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap) throws ApplicationException;
	
	/**
	 * 
	 * @param methodID
	 * @param inputArray
	 * @param request
	 * @param response
	 * @param headersMap
	 * @return
	 * @throws ApplicationException
	 */
	Result updateFavouriteInstruments(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap) throws ApplicationException;
	
	/**
	 * 
	 * @param methodID
	 * @param inputArray
	 * @param request
	 * @param response
	 * @param headersMap
	 * @return
	 * @throws ApplicationException
	 */
	Result createFavouriteInstruments(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap) throws ApplicationException;
}
