/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.commons.dto.FilterDTO;
import com.temenos.infinity.tradefinanceservices.dto.InwardCollectionsDTO;

import java.io.IOException;

public interface InwardCollectionsResource extends Resource {

    Result createInwardCollection(InwardCollectionsDTO inputDTO, DataControllerRequest request);

    Result getInwardCollections(FilterDTO filterDto, DataControllerRequest request);

    Result getInwardCollectionById(DataControllerRequest request);

    Result updateInwardCollection(InwardCollectionsDTO amendmentDetails, DataControllerRequest request);

    Result generateInwardCollection(DataControllerRequest request);

    Result updateInwardCollectionByBank(InwardCollectionsDTO bankResponse, DataControllerRequest request) throws IOException;
}
