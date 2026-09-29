/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2023. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.backenddelegate.api;

import com.dbp.core.api.BackendDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradefinanceservices.dto.OutwardCollectionAmendmentsDTO;

import java.util.List;

/**
 * @author k.meiyazhagan
 */
public interface OutwardCollectionAmendmentsBackendDelegate extends BackendDelegate {
    OutwardCollectionAmendmentsDTO createAmendment(OutwardCollectionAmendmentsDTO inputDto, DataControllerRequest request);

    List<OutwardCollectionAmendmentsDTO> getAmendments(DataControllerRequest request);

    OutwardCollectionAmendmentsDTO getAmendmentById(String amendmentReference, DataControllerRequest request);

    OutwardCollectionAmendmentsDTO updateAmendment(OutwardCollectionAmendmentsDTO inputDto, DataControllerRequest request);
}
