/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.backenddelegate.api;

import com.dbp.core.api.BackendDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradefinanceservices.dto.SwiftsAndAdvisesDTO;

import java.util.List;

public interface GuaranteesAmendmentsSwiftAndAdvicesBackendDelegate extends BackendDelegate {
    SwiftsAndAdvisesDTO createSwiftsAndAdvises(SwiftsAndAdvisesDTO swiftsAndAdvises, DataControllerRequest request);

    List<SwiftsAndAdvisesDTO> getGuaranteeSwiftAdvices(DataControllerRequest request);
}
