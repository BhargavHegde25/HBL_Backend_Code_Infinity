package com.temenos.infinity.api.wealth.resource.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.sessionmanager.SessionScope;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealth.businessdelegate.api.InstrumentDetailsBusinessDelegate;
import com.temenos.infinity.api.wealth.resource.api.InstrumentDetailsResource;

public class InstrumentDetailsResourceImpl implements InstrumentDetailsResource {


	@Override
	public Result getInstrumentDetails(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();

		/*String refenitivTokenref = EnvironmentConfigurationsHandler.getValue(TemenosConstants.WEALTH_REFINITV_TOKEN,
				request);*/
		Map<String, Object> headers = request.getHeaderMap();
		if (headers == null) {
			headers = new HashMap<String, Object>();
		}

		Set<String> userPermissions = SessionScope.getAllPermissionsFromIdentityScope(request);
		if (!userPermissions.contains("WEALTH_PRODUCT_DETAILS_INSTRUMENT_VIEW")) {
		//	ErrorCodeEnum.ERR_12001.setErrorCode(result);
			return result;
		}	
		
		
		InstrumentDetailsBusinessDelegate instrumentDetailsBusinessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(InstrumentDetailsBusinessDelegate.class);
		result = instrumentDetailsBusinessDelegate.getInstrumentDetails(methodID, inputArray, request, response,
				headers);
		return result;
	}

}
