/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.backenddelegate.api;

import java.util.List;

import com.dbp.core.api.BackendDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradefinanceservices.dto.GuaranteeLCAmendmentsDTO;
import org.json.JSONObject;

public interface GuaranteeLCAmendmentsBackendDelegate extends BackendDelegate {

	List<GuaranteeLCAmendmentsDTO> getGuaranteeLCAmendments(DataControllerRequest request);

	GuaranteeLCAmendmentsDTO createGuaranteeLCAmendment(GuaranteeLCAmendmentsDTO guaranteeReqPayloadDTO,
			DataControllerRequest request);

	GuaranteeLCAmendmentsDTO getGuaranteeLCAmendmentById(String amendmentSRMSId, DataControllerRequest request);

	public GuaranteeLCAmendmentsDTO updateGuaranteeAmendment(GuaranteeLCAmendmentsDTO guaranteeReqPayloadDTO,JSONObject inputObj,
											   DataControllerRequest request);

}
