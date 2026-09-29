/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.businessdelegate.api;

import com.dbp.core.api.BusinessDelegate;
import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradefinanceservices.dto.SwiftsAndAdvisesDTO;

import java.util.List;

public interface GuaranteesAmendmentsSwiftAndAdvicesBusinessDelegate extends BusinessDelegate {
    SwiftsAndAdvisesDTO createSwiftsAndAdvises(SwiftsAndAdvisesDTO SwiftsAndAdvises, DataControllerRequest request) throws ApplicationException, com.temenos.infinity.api.commons.exception.ApplicationException;

    List<SwiftsAndAdvisesDTO> getGuaranteeSwiftAdvices(DataControllerRequest request);
}
