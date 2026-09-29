/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.commons.dto.FilterDTO;
import com.temenos.infinity.tradefinanceservices.dto.InwardCollectionAmendmentsDTO;

public interface InwardCollectionAmendmentsResource extends Resource {

    Result createInwardCollectionAmendment(InwardCollectionAmendmentsDTO inputDTO, DataControllerRequest request);

    Result getInwardCollectionAmendments(FilterDTO filterDto, DataControllerRequest request);

    Result getInwardCollectionAmendmentById(DataControllerRequest request);

    Result updateInwardCollectionAmendment(InwardCollectionAmendmentsDTO amendmentDetails, DataControllerRequest request);
    Result updateInwardCollectionAmendmentByBank(InwardCollectionAmendmentsDTO amendmentDetails, DataControllerRequest request);

    Result generateInwardCollectionAmendment(DataControllerRequest request);
}
