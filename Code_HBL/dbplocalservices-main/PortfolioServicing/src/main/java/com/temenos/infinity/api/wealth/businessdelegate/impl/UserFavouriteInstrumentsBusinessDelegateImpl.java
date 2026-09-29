package com.temenos.infinity.api.wealth.businessdelegate.impl;

import java.util.Map;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealth.backenddelegate.api.UserFavouriteInstrumentsBackendDelegate;
import com.temenos.infinity.api.wealth.businessdelegate.api.UserFavouriteInstrumentsBusinessDelegate;

public class UserFavouriteInstrumentsBusinessDelegateImpl implements UserFavouriteInstrumentsBusinessDelegate {

	@Override
	public Result getFavouriteInstruments(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap) throws ApplicationException {
		Result result = new Result();
		UserFavouriteInstrumentsBackendDelegate userFavouritesBackendDelegate =DBPAPIAbstractFactoryImpl.
				getBackendDelegate(UserFavouriteInstrumentsBackendDelegate.class);
				result = userFavouritesBackendDelegate.getFavouriteInstruments(methodID, inputArray, request, response, headersMap);
		return result;
	}

	@Override
	public Result updateFavouriteInstruments(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap) throws ApplicationException {
		Result result = new Result();
		UserFavouriteInstrumentsBackendDelegate userFavouritesBackendDelegate =DBPAPIAbstractFactoryImpl.
				getBackendDelegate(UserFavouriteInstrumentsBackendDelegate.class);
				result = userFavouritesBackendDelegate.updateFavouriteInstruments(methodID, inputArray, request, response, headersMap);
		return result;
	}
	
	@Override
	public Result createFavouriteInstruments(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap) throws ApplicationException {
		Result result = new Result();
		UserFavouriteInstrumentsBackendDelegate userFavouritesBackendDelegate =DBPAPIAbstractFactoryImpl.
				getBackendDelegate(UserFavouriteInstrumentsBackendDelegate.class);
				result = userFavouritesBackendDelegate.createFavouriteInstruments(methodID, inputArray, request, response, headersMap);
		return result;
	}

}
