/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;

public interface GetAmendmentsLetterOfCreditsResource extends Resource {

    Result getAmendLetterOfCredits(Object[] inputArray, DataControllerRequest request);

    Result getAmendmentsById(Object[] inputArray, DataControllerRequest request);

}
