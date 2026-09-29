package com.temenos.infinity.api.arrangements.backenddelegate.api;


import java.util.ArrayList;


import com.dbp.core.api.BackendDelegate;
import com.dbp.core.api.BusinessDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.api.arrangements.dto.ArrangementsDTO;
import com.temenos.infinity.api.commons.exception.ApplicationException;

/**
 * 
 * @author smugesh
 * @version 1.0 Interface for AccountsResource extends {@link BusinessDelegate}
 *
 */
public interface ArrangementsExperienceAPIBackendDelegate extends BackendDelegate {

    /**
     * method to get the Arrangements return JSONObject of accounts
     */
	ArrayList getArrangements(ArrangementsDTO inputPayload, DataControllerRequest request,String authToken, Boolean balanceFlag)
            throws ApplicationException;
    

}
