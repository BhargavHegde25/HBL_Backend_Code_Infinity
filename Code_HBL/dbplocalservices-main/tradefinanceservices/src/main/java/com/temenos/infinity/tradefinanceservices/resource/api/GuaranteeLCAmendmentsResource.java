/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.commons.dto.FilterDTO;
import com.temenos.infinity.tradefinanceservices.dto.GuaranteeLCAmendmentsDTO;

import java.io.IOException;
import java.util.HashMap;

public interface GuaranteeLCAmendmentsResource extends Resource {

    Result getGuaranteeLCAmendments(FilterDTO filterDTO, DataControllerRequest request);

    Result createGuaranteeLCAmendment(GuaranteeLCAmendmentsDTO guaranteeReqPayloadDTO, DataControllerRequest request);

    Result getGuaranteeLCAmendmentById(DataControllerRequest request);

    Result updateGuaranteeAmendmentByBank(HashMap inoutParams, DataControllerRequest request);

    Result updateGuaranteeAmendment(HashMap inoutParams, DataControllerRequest request) throws IOException;

    Result generatePdfGuaranteeLcAmendment(DataControllerRequest request);

}
