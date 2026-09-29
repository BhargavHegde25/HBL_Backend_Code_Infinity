/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;

public interface TradeFinanceDocumentsResource extends Resource {

    Result uploadDocument(DataControllerRequest request);

    Result fetchDocument(DataControllerRequest request);

    Result deleteDocument(DataControllerRequest request);

    Result uploadSwiftsAdvices(DataControllerRequest request);

    Result fetchSwiftsAdvices(DataControllerRequest request);
}
