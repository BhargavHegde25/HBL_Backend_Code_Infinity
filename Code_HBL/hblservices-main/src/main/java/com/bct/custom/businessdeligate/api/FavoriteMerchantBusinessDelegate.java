package com.bct.custom.businessdeligate.api;

import java.util.Map;

import org.json.JSONArray;

import com.bct.custom.dto.FavoriteMerchantDTO;
import com.dbp.core.api.BusinessDelegate;
import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;

public interface FavoriteMerchantBusinessDelegate extends BusinessDelegate {
	public JSONArray getFavoriteMerchants(String input, Map<String, Object> inputArray, DataControllerRequest dcRequest) throws ApplicationException;

	public Result createFavoriteMerchant(FavoriteMerchantDTO favoriteMerchant, Map<String, Object> inputParams,
			DataControllerRequest dcRequest) throws ApplicationException;
	
	public Result updateFavoriteMerchant(FavoriteMerchantDTO favoriteMerchant, Map<String, Object> inputParams,
			DataControllerRequest dcRequest) throws ApplicationException;
	
	public Result deleteFavoriteMerchant(Map<String, Object> inputParams,DataControllerRequest dcRequest) throws ApplicationException;
}
