/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.businessdelegate.api;

import java.util.List;

import com.dbp.core.api.BusinessDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradefinanceservices.dto.GuaranteeLCAmendmentsDTO;
import org.json.JSONObject;

public interface GuaranteeLCAmendmentsBusinessDelegate extends BusinessDelegate {

    List<GuaranteeLCAmendmentsDTO> getGuaranteeLCAmendments(DataControllerRequest request);

    GuaranteeLCAmendmentsDTO createGuaranteeLCAmendment(GuaranteeLCAmendmentsDTO guaranteeReqPayloadDTO,
                                                        DataControllerRequest request);

    GuaranteeLCAmendmentsDTO getGuaranteeLCAmendmentById(String amendmentSRMSId, DataControllerRequest request);

    GuaranteeLCAmendmentsDTO updateGuaranteeAmendment(GuaranteeLCAmendmentsDTO guaranteeReqPayloadDTO, JSONObject inputObj,
                                                             DataControllerRequest request);

    GuaranteeLCAmendmentsDTO updateGuaranteeAmendmentByBank(GuaranteeLCAmendmentsDTO guaranteeReqPayloadDTO,
                                                                   DataControllerRequest request);

    byte[] generatePdfGuaranteeLcAmendment(JSONObject amendmentData, DataControllerRequest request);
}
