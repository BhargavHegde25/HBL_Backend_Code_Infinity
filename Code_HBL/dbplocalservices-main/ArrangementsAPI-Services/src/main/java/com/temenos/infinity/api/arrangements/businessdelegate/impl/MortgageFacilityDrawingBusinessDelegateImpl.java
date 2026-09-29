package com.temenos.infinity.api.arrangements.businessdelegate.impl;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.arrangements.backenddelegate.api.MortgageFacilityDetailsAPIBackendDelegate;
import com.temenos.infinity.api.arrangements.backenddelegate.api.MortgageFacilityDrawingAPIBackendDelegate;
import com.temenos.infinity.api.arrangements.businessdelegate.api.MortgageFacilityDetailsBusinessDelegate;
import com.temenos.infinity.api.arrangements.businessdelegate.api.MortgageFacilityDrawingBusinessDelegate;
import com.temenos.infinity.api.commons.exception.ApplicationException;

public class MortgageFacilityDrawingBusinessDelegateImpl implements MortgageFacilityDrawingBusinessDelegate {

	@Override
	public Result getMortageFacilityDrawing(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		MortgageFacilityDrawingAPIBackendDelegate mortageDrawingbackendDelegate = DBPAPIAbstractFactoryImpl
                .getBackendDelegate(MortgageFacilityDrawingAPIBackendDelegate.class);
        return mortageDrawingbackendDelegate.getMortageFacilityDrawingBackendDelegate(methodId, inputArray, request, response);
	}
}
