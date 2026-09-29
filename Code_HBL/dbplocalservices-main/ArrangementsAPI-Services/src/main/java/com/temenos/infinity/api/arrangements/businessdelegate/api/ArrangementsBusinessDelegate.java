package com.temenos.infinity.api.arrangements.businessdelegate.api;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.dbp.core.api.BusinessDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.arrangements.dto.ArrangementsDTO;
import com.temenos.infinity.api.commons.exception.ApplicationException;

/**
 * 
 * @author smugesh
 * @version 1.0 Interface for AccountsResource extends {@link BusinessDelegate}
 *
 */
public interface ArrangementsBusinessDelegate extends BusinessDelegate {

    /**
     * method to get the Arrangements return JSONObject of accounts
     * 
     * @param request
     */
	ArrayList getArrangements(ArrangementsDTO inputPayload, DataControllerRequest request, String authToken, Boolean balanceFlag)
            throws ApplicationException;
    /**
     * method to get the Arrangements return JSONObject of accounts
     * 
     * @param request
     */
    List<ArrangementsDTO> getArrangementOverview(ArrangementsDTO inputPayload, DataControllerRequest request, String authToken)
            throws ApplicationException;

    /**
     * method to get the Business User Arrangements return JSONObject of accounts
     */
    List<ArrangementsDTO> getBusinessUserArrangements(ArrangementsDTO inputPayload, DataControllerRequest request)
            throws ApplicationException;

    /**
     * method to get the Business User Arrangements return JSONObject of accounts
     */
    List<ArrangementsDTO> getBusinessUserArrangementOverview(ArrangementsDTO inputPayload,
            DataControllerRequest request) throws ApplicationException;

    List<ArrangementsDTO> getBusinessUserArrangementPreview(ArrangementsDTO inputPayloadDTO,
            DataControllerRequest request) throws ApplicationException;

    List<ArrangementsDTO> getUserDetailsFromDBX(ArrangementsDTO inputPayloadDTO, DataControllerRequest request)
            throws ApplicationException;
	
    JSONObject getAccountDetailForCombinedStatements(String accountID, String customerType, String authToken) throws ApplicationException;
    
	JSONObject getAccountDetailForCombinedStatementsfromT24(String accountID, String customerType,
			String authToken) throws ApplicationException, Exception;
    
	List<ArrangementsDTO> getArrangementBulkOverview(String ArrangementResponse,ArrangementsDTO inputPayload, DataControllerRequest request,
            String authToken) throws ApplicationException;
	
	Result getSimulatedResults(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws ApplicationException, Exception;
	
	Result createAndGetPayOffSimulatedResults(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws ApplicationException, Exception;	
	

	List<ArrangementsDTO> getLatestBalances(String accountIdListStr,String authToken) throws ApplicationException, Exception;
	
}
