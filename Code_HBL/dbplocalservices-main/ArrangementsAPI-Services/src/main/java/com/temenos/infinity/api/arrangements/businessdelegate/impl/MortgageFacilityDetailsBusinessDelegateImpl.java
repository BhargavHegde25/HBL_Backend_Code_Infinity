package com.temenos.infinity.api.arrangements.businessdelegate.impl;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.arrangements.backenddelegate.api.MortgageFacilityDetailsAPIBackendDelegate;
import com.temenos.infinity.api.arrangements.businessdelegate.api.MortgageFacilityDetailsBusinessDelegate;
import com.temenos.infinity.api.commons.exception.ApplicationException;

public class MortgageFacilityDetailsBusinessDelegateImpl implements MortgageFacilityDetailsBusinessDelegate {

	@Override
	public Result getMortageFacilityDetails(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		MortgageFacilityDetailsAPIBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl
                .getBackendDelegate(MortgageFacilityDetailsAPIBackendDelegate.class);
        return backendDelegate.getMortageFacilityDetailBackendDelegate(methodId, inputArray, request, response);
	}
}
