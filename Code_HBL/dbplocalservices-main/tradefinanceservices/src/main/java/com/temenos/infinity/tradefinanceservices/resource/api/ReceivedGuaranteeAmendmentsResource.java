/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.commons.dto.FilterDTO;
import com.temenos.infinity.tradefinanceservices.dto.ReceivedAmendmentsDTO;

public interface ReceivedGuaranteeAmendmentsResource extends Resource {
    Result createReceivedAmendment(ReceivedAmendmentsDTO inputDto, DataControllerRequest request);

    Result updateReceivedAmendment(ReceivedAmendmentsDTO inputDto, DataControllerRequest request);

    Result getReceivedAmendments(FilterDTO filterDto, DataControllerRequest request);

    Result getReceivedAmendmentById(DataControllerRequest request);

    Result generateReceivedAmendment(DataControllerRequest request);

    Result updateReceivedAmendmentByBank(ReceivedAmendmentsDTO inputDTO, DataControllerRequest request);
}
