package com.temenos.infinity.api.arrangements.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.exception.ApplicationException;

/**
 * 
 * @author smugesh
 * @version 1.0 Interface for AccountsResource extends {@link Resource}
 *
 */

public interface MortgageFacilityDetailsResource extends Resource {

    Result getMortageFacilityDetails(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception;
}
