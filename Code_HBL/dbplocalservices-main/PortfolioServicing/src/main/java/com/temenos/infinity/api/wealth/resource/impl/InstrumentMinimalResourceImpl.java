package com.temenos.infinity.api.wealth.resource.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.sessionmanager.SessionScope;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealth.businessdelegate.api.InstrumentMinimalBusinessDelegate;
import com.temenos.infinity.api.wealth.resource.api.InstrumentMinimalResource;

public class InstrumentMinimalResourceImpl implements InstrumentMinimalResource {


	@Override
	public Result getInstrumentMinimal(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();

		Map<String, Object> headers = request.getHeaderMap();
		if (headers == null) {
			headers = new HashMap<String, Object>();
		}

		Set<String> userPermissions = SessionScope.getAllPermissionsFromIdentityScope(request);
		if (!userPermissions.contains("WEALTH_PRODUCT_DETAILS_INSTRUMENT_VIEW")) {
			ErrorCodeEnum.ERR_12001.setErrorCode(result);
			return result;
		}	
		
		
		InstrumentMinimalBusinessDelegate instrumentMinimalBusinessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(InstrumentMinimalBusinessDelegate.class);
		result = instrumentMinimalBusinessDelegate.getInstrumentMinimal(methodID, inputArray, request, response,
				headers);
		return result;
	}

}
