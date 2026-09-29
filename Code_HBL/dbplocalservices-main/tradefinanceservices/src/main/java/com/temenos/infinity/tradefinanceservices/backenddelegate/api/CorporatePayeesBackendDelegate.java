/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.backenddelegate.api;

import com.dbp.core.api.BackendDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradefinanceservices.dto.CorporatePayeesDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface CorporatePayeesBackendDelegate extends BackendDelegate {
    List<CorporatePayeesDto> getCorporatePayees(Map<String, List<String>> inputCifMap, HashMap inputParams, String select, String filter, DataControllerRequest request)
            throws Exception;

    String createCorporatePayee(HashMap corporatePayeesDto, DataControllerRequest request) throws Exception;

    String editExternalPayee(HashMap inputParams, DataControllerRequest request);

    String editCorporatePayee(HashMap inputParams, DataControllerRequest request);
}
