/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.backenddelegate.api;

import com.dbp.core.api.BackendDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradefinanceservices.dto.ReceivedAmendmentsDTO;

import java.util.List;

public interface ReceivedGuaranteeAmendmentsBackendDelegate extends BackendDelegate {
    ReceivedAmendmentsDTO createReceivedAmendment(ReceivedAmendmentsDTO inputDto, DataControllerRequest request);

    ReceivedAmendmentsDTO updateReceivedAmendment(ReceivedAmendmentsDTO amendmentDetails, DataControllerRequest request);

    List<ReceivedAmendmentsDTO> getReceivedAmendments(DataControllerRequest request);

    ReceivedAmendmentsDTO getReceivedAmendmentById(String amendmentSrmsId, DataControllerRequest request);
}
