package com.temenos.infinity.api.arrangements.backenddelegate.api;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.dbp.core.api.BackendDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.arrangements.dto.ArrangementsDTO;
import com.temenos.infinity.api.commons.exception.ApplicationException;

/**
 * TODO: Document me!
 * 
 * @author smugesh
 *
 */
public interface ArrangementOverviewBackendDelegate extends BackendDelegate {
    /**
     * method to get the Arrangements return JSONObject of accounts
     */
    List<ArrangementsDTO> getArrangementOverview(ArrangementsDTO inputPayload, DataControllerRequest request, String authToken)
            throws ApplicationException;

	JSONObject getAccountDetailsForCombinedStatement(String accountID, String authToken) throws ApplicationException;

	JSONObject getAccountDetailsForCombinedStatementfromT24(String accountID, String authToken) throws Exception;

    List<ArrangementsDTO> getArrangementBulkOverview(String ArrangementResponse,ArrangementsDTO inputPayload, DataControllerRequest request,
            String authToken) throws ApplicationException;
    

	Result getSimulatedResults(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws ApplicationException;
	
	Result createAndGetPayOffSimulatedResults(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws ApplicationException;

	List<ArrangementsDTO> getLatestBalances(String accountIdListStr, String authToken) throws ApplicationException;
}
