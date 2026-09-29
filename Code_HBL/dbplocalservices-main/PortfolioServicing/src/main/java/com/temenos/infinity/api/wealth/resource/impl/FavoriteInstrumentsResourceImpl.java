package com.temenos.infinity.api.wealth.resource.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.sessionmanager.SessionScope;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealth.businessdelegate.api.FavoriteInstrumentsBusinessDelegate;
import com.temenos.infinity.api.wealth.constants.WealthFeatureAction;
import com.temenos.infinity.api.wealth.resource.api.FavoriteInstrumentsResource;

public class FavoriteInstrumentsResourceImpl implements FavoriteInstrumentsResource {

	@Override
	public Result getFavoriteInstrumentsResource(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();

		Set<String> userPermissions = SessionScope.getAllPermissionsFromIdentityScope(request);
		if (!userPermissions.contains(WealthFeatureAction.WEALTH_WATCHLIST_INSTRUMENT_VIEW)) {
			return result;
		}

		Map<String, Object> headers = request.getHeaderMap();
		if (headers == null) {
			headers = new HashMap<String, Object>();
		}

		FavoriteInstrumentsBusinessDelegate instrumentDetailsBusinessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(FavoriteInstrumentsBusinessDelegate.class);
		result = instrumentDetailsBusinessDelegate.getFavoriteInstruments(methodID, inputArray, request, response,
				headers);
		return result;
	}

}
