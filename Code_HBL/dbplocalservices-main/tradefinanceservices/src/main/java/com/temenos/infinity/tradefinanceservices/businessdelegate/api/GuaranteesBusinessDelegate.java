/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.businessdelegate.api;

import com.dbp.core.api.BusinessDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradefinanceservices.dto.GuaranteesDTO;

import java.util.List;

public interface GuaranteesBusinessDelegate extends BusinessDelegate {
    GuaranteesDTO createGuarantees(GuaranteesDTO guaranteesDto, DataControllerRequest request);

    GuaranteesDTO updateGuarantees(GuaranteesDTO guaranteesDto, DataControllerRequest request);

    GuaranteesDTO getGuaranteesById(String srmsId, DataControllerRequest request);

    List<GuaranteesDTO> getGuranteesLC(GuaranteesDTO guaranteesDTO, DataControllerRequest request);
}
