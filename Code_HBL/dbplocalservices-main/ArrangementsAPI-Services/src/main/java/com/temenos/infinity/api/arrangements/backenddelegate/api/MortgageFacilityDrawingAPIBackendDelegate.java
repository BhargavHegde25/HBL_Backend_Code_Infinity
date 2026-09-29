package com.temenos.infinity.api.arrangements.backenddelegate.api;

import com.dbp.core.api.BackendDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.exception.ApplicationException;

public interface MortgageFacilityDrawingAPIBackendDelegate extends BackendDelegate {

	Result getMortageFacilityDrawingBackendDelegate(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception;
}
