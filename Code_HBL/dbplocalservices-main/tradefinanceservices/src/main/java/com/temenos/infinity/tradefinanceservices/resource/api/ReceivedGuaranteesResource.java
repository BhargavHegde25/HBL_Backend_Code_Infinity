/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.resource.api;

import com.dbp.core.api.Resource;
import com.dbp.core.error.DBPApplicationException;
import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.commons.dto.FilterDTO;
import com.temenos.infinity.tradefinanceservices.dto.ReceivedGuaranteesDTO;

public interface ReceivedGuaranteesResource extends Resource {
    Result createReceivedGuarantee(ReceivedGuaranteesDTO inputDTO, DataControllerRequest request) throws DBPApplicationException;

    Result getReceivedGuarantees(FilterDTO filterDto, DataControllerRequest request);

    Result getReceivedGuaranteeById(DataControllerRequest request) throws ApplicationException;

    Result updateReceivedGuarantee(ReceivedGuaranteesDTO inputDTO, DataControllerRequest request);

    Result updateReceivedGuaranteeByBank(ReceivedGuaranteesDTO inputDto, DataControllerRequest request);

    Result generateReceivedGuarantee(DataControllerRequest request);

    Result releaseLiability(DataControllerRequest request);

}
