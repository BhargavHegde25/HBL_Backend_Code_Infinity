/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.backenddelegate.api;

import com.dbp.core.api.BackendDelegate;
import com.dbp.core.error.DBPApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradefinanceservices.dto.ReceivedGuaranteesDTO;

import java.util.List;

public interface ReceivedGuaranteesBackendDelegate extends BackendDelegate {
    ReceivedGuaranteesDTO createReceivedGuarantee(ReceivedGuaranteesDTO inputDTO, DataControllerRequest request) throws DBPApplicationException;

    List<ReceivedGuaranteesDTO> getReceivedGuarantees(DataControllerRequest request);

    ReceivedGuaranteesDTO getReceivedGuaranteeById(String guaranteeSrmsId, DataControllerRequest request);

    ReceivedGuaranteesDTO updateReceivedGuarantee(ReceivedGuaranteesDTO guaranteeDetails, DataControllerRequest request);

}
