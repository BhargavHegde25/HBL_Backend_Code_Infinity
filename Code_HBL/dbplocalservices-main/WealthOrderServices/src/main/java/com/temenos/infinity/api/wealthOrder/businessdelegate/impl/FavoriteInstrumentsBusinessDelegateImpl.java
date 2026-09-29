package com.temenos.infinity.api.wealthOrder.businessdelegate.impl;

import java.util.Map;


import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthOrder.backenddelegate.api.FavoriteInstrumentsBackendDelegate;
import com.temenos.infinity.api.wealthOrder.businessdelegate.api.FavoriteInstrumentsBusinessDelegate;

public class FavoriteInstrumentsBusinessDelegateImpl implements FavoriteInstrumentsBusinessDelegate{



	@Override
	public Result getFavoriteInstruments(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap) {
		// TODO Auto-generated method stub
		Result result = new Result();
		FavoriteInstrumentsBackendDelegate favoriteInstrumentsBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(FavoriteInstrumentsBackendDelegate.class);
		result = favoriteInstrumentsBackendDelegate.getFavoriteInstruments(methodID, inputArray, request, response,headersMap);
		return result;
	}

}
