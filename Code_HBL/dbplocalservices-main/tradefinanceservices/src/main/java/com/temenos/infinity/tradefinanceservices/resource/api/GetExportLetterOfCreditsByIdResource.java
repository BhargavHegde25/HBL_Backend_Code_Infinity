/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;

public interface GetExportLetterOfCreditsByIdResource extends Resource{
	public Result getExportLetterOfCreditById(Object[] inputArray, DataControllerRequest request);
}
