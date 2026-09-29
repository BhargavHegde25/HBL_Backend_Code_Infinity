/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2023. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradesupplyfinance.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;

/**
 * @author k.meiyazhagan
 */
public interface DocumentsResource extends Resource {

    Result uploadDocument(DataControllerRequest request);

    Result fetchDocument(DataControllerRequest request);

    Result deleteDocument(DataControllerRequest request);
}
