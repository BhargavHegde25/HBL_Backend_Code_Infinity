/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.backenddelegate.api;

import com.dbp.core.api.BackendDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradefinanceservices.dto.ClauseDTO;
import com.temenos.infinity.tradefinanceservices.dto.GuaranteesDTO;

import java.util.List;
import java.util.Map;

public interface GuaranteesBackendDelegate extends BackendDelegate {
	GuaranteesDTO createGuarantees(GuaranteesDTO guaranteesDto, DataControllerRequest request);

	GuaranteesDTO updateGuarantees(GuaranteesDTO guaranteesDto, DataControllerRequest request);

	GuaranteesDTO getGuaranteesById(String srmsId, DataControllerRequest request);

    List<GuaranteesDTO> getGuranteesLC(GuaranteesDTO guaranteesDTO, DataControllerRequest request);

	List<ClauseDTO> createClauses(Map<String, Object> requestParameters, DataControllerRequest dataControllerRequest);
}
