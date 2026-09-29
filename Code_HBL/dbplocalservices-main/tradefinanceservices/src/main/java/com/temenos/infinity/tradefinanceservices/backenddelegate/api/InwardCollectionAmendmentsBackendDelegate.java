/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.backenddelegate.api;

import com.dbp.core.api.BackendDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradefinanceservices.dto.InwardCollectionAmendmentsDTO;

import java.util.List;

public interface InwardCollectionAmendmentsBackendDelegate extends BackendDelegate {

    InwardCollectionAmendmentsDTO createInwardCollectionAmendment(InwardCollectionAmendmentsDTO inputDTO, DataControllerRequest request);

    List<InwardCollectionAmendmentsDTO> getInwardCollectionAmendments(DataControllerRequest request);

    InwardCollectionAmendmentsDTO getInwardCollectionAmendmentById(String amendmentSrmsId, DataControllerRequest request);

    InwardCollectionAmendmentsDTO updateInwardCollectionAmendment(InwardCollectionAmendmentsDTO amendmentDetails, DataControllerRequest request);
    
}
