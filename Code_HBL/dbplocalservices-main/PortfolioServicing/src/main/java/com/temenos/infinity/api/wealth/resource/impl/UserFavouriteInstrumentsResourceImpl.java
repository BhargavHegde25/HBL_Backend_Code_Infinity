package com.temenos.infinity.api.wealth.resource.impl;

import java.util.Map;
import java.util.Set;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.sessionmanager.SessionScope;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealth.businessdelegate.api.UserFavouriteInstrumentsBusinessDelegate;
import com.temenos.infinity.api.wealth.constants.WealthFeatureAction;
import com.temenos.infinity.api.wealth.resource.api.UserFavouriteInstrumentsResource;

public class UserFavouriteInstrumentsResourceImpl implements UserFavouriteInstrumentsResource {

	@Override
	public Result getFavouriteInstruments(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws ApplicationException {

		Result result = new Result();
		Map<String, Object> headers = request.getHeaderMap();
		
		Set<String> userPermissions = SessionScope.getAllPermissionsFromIdentityScope(request);
        if (!userPermissions.contains(WealthFeatureAction.WEALTH_WATCHLIST_INSTRUMENT_VIEW)) {
            ErrorCodeEnum.ERR_12001.setErrorCode(result);
            return result;
        }
        
		UserFavouriteInstrumentsBusinessDelegate userFavouritesBusinessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(UserFavouriteInstrumentsBusinessDelegate.class);
		result = userFavouritesBusinessDelegate.getFavouriteInstruments(methodID, inputArray, request, response, headers);
		return result;
	}

	@Override
	public Result updateFavouriteInstruments(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws ApplicationException {
		Result result = new Result();
		Map<String, Object> headers = request.getHeaderMap();
		
		Set<String> userPermissions = SessionScope.getAllPermissionsFromIdentityScope(request);
        if (!userPermissions.contains(WealthFeatureAction.WEALTH_WATCHLIST_INSTRUMENT_CREATE)) {
            ErrorCodeEnum.ERR_12001.setErrorCode(result);
            return result;
        }
        
		UserFavouriteInstrumentsBusinessDelegate userFavouritesBusinessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(UserFavouriteInstrumentsBusinessDelegate.class);
		result = userFavouritesBusinessDelegate.updateFavouriteInstruments(methodID, inputArray, request, response, headers);
		return result;
	}
	
	@Override
	public Result createFavouriteInstruments(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws ApplicationException {
		Result result = new Result();
		Map<String, Object> headers = request.getHeaderMap();
		
		Set<String> userPermissions = SessionScope.getAllPermissionsFromIdentityScope(request);
        if (!userPermissions.contains(WealthFeatureAction.WEALTH_WATCHLIST_INSTRUMENT_CREATE)) {
            ErrorCodeEnum.ERR_12001.setErrorCode(result);
            return result;
        }
        
		UserFavouriteInstrumentsBusinessDelegate userFavouritesBusinessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(UserFavouriteInstrumentsBusinessDelegate.class);
		result = userFavouritesBusinessDelegate.createFavouriteInstruments(methodID, inputArray, request, response, headers);
		return result;
	}
}
