/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;

import java.util.HashMap;

public interface CorporatePayeesResource extends Resource {
    Result getCorporatePayees(HashMap inputParams, DataControllerRequest request) throws Exception;

    Result createCorporatePayee(HashMap inputParams, DataControllerRequest request) throws Exception;

    Result editPayee(HashMap inputParams, DataControllerRequest request);


}
