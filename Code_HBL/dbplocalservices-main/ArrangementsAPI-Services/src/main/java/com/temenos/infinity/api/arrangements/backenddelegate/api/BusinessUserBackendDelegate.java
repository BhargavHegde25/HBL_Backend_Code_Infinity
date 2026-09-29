package com.temenos.infinity.api.arrangements.backenddelegate.api;

import java.util.List;

import com.dbp.core.api.BackendDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.api.arrangements.dto.ArrangementsDTO;
import com.temenos.infinity.api.commons.exception.ApplicationException;

/**
 * TODO: Document me!
 *
 * @author smugesh
 *
 */
public interface BusinessUserBackendDelegate extends BackendDelegate {

    /**
     * method to get the business user Arrangements return JSONObject of accounts
     */
    List<ArrangementsDTO> getBusinessUserArrangements(ArrangementsDTO inputPayload, DataControllerRequest request)
            throws ApplicationException;

    /**
     * method to get the business user Arrangements return JSONObject of accounts
     */
    List<ArrangementsDTO> getBusinessUserArrangementOverview(ArrangementsDTO inputPayload,
            DataControllerRequest request) throws ApplicationException;

    List<ArrangementsDTO> getBusinessUserArrangementPreview(ArrangementsDTO inputPayloadDTO,
            DataControllerRequest request) throws ApplicationException;

    List<ArrangementsDTO> getUserDetailsFromDBX(ArrangementsDTO inputPayloadDTO, DataControllerRequest request)
            throws ApplicationException;

}
