/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2023. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.commons.dto.FilterDTO;
import com.temenos.infinity.tradefinanceservices.dto.OutwardCollectionAmendmentsDTO;

import java.io.IOException;

/**
 * @author k.meiyazhagan
 */
public interface OutwardCollectionAmendmentsResource extends Resource {
    Result createAmendment(OutwardCollectionAmendmentsDTO inputDto, DataControllerRequest request);
    Result updateAmendment(OutwardCollectionAmendmentsDTO inputDto, DataControllerRequest request) throws IOException;

    Result getAmendments(FilterDTO filterDto, DataControllerRequest request);

    Result getAmendmentById(DataControllerRequest request);

    Result updateAmendmentByBank(OutwardCollectionAmendmentsDTO inputDto, DataControllerRequest request);
}
