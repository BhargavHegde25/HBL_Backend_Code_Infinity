/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.businessdelegate.api;

import com.dbp.core.api.BusinessDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradefinanceservices.dto.ReceivedAmendmentsDTO;
import com.temenos.infinity.tradefinanceservices.dto.ReceivedGuaranteesDTO;
import org.json.JSONObject;

import java.util.List;

public interface ReceivedGuaranteeAmendmentsBusinessDelegate extends BusinessDelegate {
    ReceivedAmendmentsDTO createReceivedAmendment(ReceivedAmendmentsDTO inputDto, DataControllerRequest request);

    ReceivedAmendmentsDTO updateReceivedAmendment(ReceivedAmendmentsDTO amendmentDetails, DataControllerRequest request);

    List<ReceivedAmendmentsDTO> getReceivedAmendments(DataControllerRequest request);

    ReceivedAmendmentsDTO getReceivedAmendmentById(String amendmentSrmsId, DataControllerRequest request);

    byte[] generateReceivedAmendment(ReceivedAmendmentsDTO amendmentsDTO, ReceivedGuaranteesDTO receivedGuaranteeDTO, DataControllerRequest request);
}
