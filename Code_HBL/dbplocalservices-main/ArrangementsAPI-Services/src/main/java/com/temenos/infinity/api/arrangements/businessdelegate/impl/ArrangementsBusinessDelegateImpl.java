package com.temenos.infinity.api.arrangements.businessdelegate.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.arrangements.backenddelegate.api.ArrangementOverviewBackendDelegate;
import com.temenos.infinity.api.arrangements.backenddelegate.api.ArrangementsExperienceAPIBackendDelegate;
import com.temenos.infinity.api.arrangements.backenddelegate.api.BusinessUserBackendDelegate;
import com.temenos.infinity.api.arrangements.businessdelegate.api.ArrangementsBusinessDelegate;
import com.temenos.infinity.api.arrangements.config.ArrangementsAPIServices;
import com.temenos.infinity.api.arrangements.constants.ErrorCodeEnum;
import com.temenos.infinity.api.arrangements.dto.ArrangementsDTO;
import com.temenos.infinity.api.arrangements.utils.ArrangementsUtils;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.infinity.api.commons.invocation.Executor;

/**
 * 
 * @author smugesh
 * @version 1.0 Extends the {@link ArrangementsBusinessDelegate}
 */
public class ArrangementsBusinessDelegateImpl implements ArrangementsBusinessDelegate {

    /**
     * method to get the Accounts
     * 
     * @return List<ArrangementsDTO> of Arrangement Accounts
     */
    ArrangementsDTO acdto = null;

    public ArrayList getArrangements(ArrangementsDTO inputPayload, DataControllerRequest request, String authToken, Boolean balanceFlag)
            throws ApplicationException {

        ArrangementsExperienceAPIBackendDelegate backendDelegate =
                DBPAPIAbstractFactoryImpl.getBackendDelegate(ArrangementsExperienceAPIBackendDelegate.class);

        ArrayList resp = backendDelegate.getArrangements(inputPayload, request, authToken, balanceFlag);
        return resp;
    }
    
    public List<ArrangementsDTO> getArrangementOverview(ArrangementsDTO inputPayload, DataControllerRequest request,String authToken)
            throws ApplicationException {
        // Initialize Variables
        List<ArrangementsDTO> arrangementsDTO = new ArrayList<>();

        ArrangementOverviewBackendDelegate backendDelegate =
                DBPAPIAbstractFactoryImpl.getBackendDelegate(ArrangementOverviewBackendDelegate.class);

        arrangementsDTO = backendDelegate.getArrangementOverview(inputPayload, request, authToken);
        return arrangementsDTO;
    }

    public List<ArrangementsDTO> getBusinessUserArrangements(ArrangementsDTO inputPayload,
            DataControllerRequest request) throws ApplicationException {
        // Initialize Variables
        List<ArrangementsDTO> arrangementsDTO = new ArrayList<>();

        BusinessUserBackendDelegate backendDelegate =
                DBPAPIAbstractFactoryImpl.getBackendDelegate(BusinessUserBackendDelegate.class);

        arrangementsDTO = backendDelegate.getBusinessUserArrangements(inputPayload, request);
        return arrangementsDTO;
    }

    public List<ArrangementsDTO> getBusinessUserArrangementOverview(ArrangementsDTO inputPayload,
            DataControllerRequest request) throws ApplicationException {
        // Initialize Variables
        List<ArrangementsDTO> arrangementsDTO = new ArrayList<>();

        BusinessUserBackendDelegate backendDelegateOverview =
                DBPAPIAbstractFactoryImpl.getBackendDelegate(BusinessUserBackendDelegate.class);

        arrangementsDTO = backendDelegateOverview.getBusinessUserArrangementOverview(inputPayload, request);
        return arrangementsDTO;
    }

    @Override
    public List<ArrangementsDTO> getBusinessUserArrangementPreview(ArrangementsDTO inputPayloadDTO,
            DataControllerRequest request) throws ApplicationException {
        List<ArrangementsDTO> arrangementsDTO = new ArrayList<>();

        BusinessUserBackendDelegate backendDelegateOverview =
                DBPAPIAbstractFactoryImpl.getBackendDelegate(BusinessUserBackendDelegate.class);

        arrangementsDTO = backendDelegateOverview.getBusinessUserArrangementPreview(inputPayloadDTO, request);
        return arrangementsDTO;
    }

    @Override
    public List<ArrangementsDTO> getUserDetailsFromDBX(ArrangementsDTO inputPayloadDTO, DataControllerRequest request)
            throws ApplicationException {
        List<ArrangementsDTO> arrangementsDTO = new ArrayList<>();

        BusinessUserBackendDelegate backendDelegateOverview =
                DBPAPIAbstractFactoryImpl.getBackendDelegate(BusinessUserBackendDelegate.class);

        arrangementsDTO = backendDelegateOverview.getUserDetailsFromDBX(inputPayloadDTO, request);
        return arrangementsDTO;
    }

	@Override
	public JSONObject getAccountDetailForCombinedStatements(String accountID, String customerType, String authToken)
			throws ApplicationException {
		ArrangementOverviewBackendDelegate backendDelegateOverview = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(ArrangementOverviewBackendDelegate.class);
		JSONObject accountDetails = backendDelegateOverview.getAccountDetailsForCombinedStatement(accountID, authToken);
		return accountDetails;
	}

	@Override
	public JSONObject getAccountDetailForCombinedStatementsfromT24(String accountID, String customerType,
			String authToken) throws Exception {
		if (accountID.contains("-")) {
			String[] accountStringArray = accountID.split("-");
			if (accountStringArray.length > 1) {
				accountID = accountStringArray[1];
			}
		}
		ArrangementOverviewBackendDelegate backendDelegateOverview = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(ArrangementOverviewBackendDelegate.class);
		JSONObject accountDetails = backendDelegateOverview.getAccountDetailsForCombinedStatementfromT24(accountID, authToken);
		return accountDetails;
	}
	
	@Override
	public List<ArrangementsDTO> getArrangementBulkOverview(String ArrangementResponse,ArrangementsDTO inputPayload, DataControllerRequest request,String authToken)
            throws ApplicationException {
        // Initialize Variables
        List<ArrangementsDTO> arrangementsDTO = new ArrayList<>();

        ArrangementOverviewBackendDelegate backendDelegate =
                DBPAPIAbstractFactoryImpl.getBackendDelegate(ArrangementOverviewBackendDelegate.class);

        arrangementsDTO = backendDelegate.getArrangementBulkOverview(ArrangementResponse,inputPayload, request, authToken);
        return arrangementsDTO;
    }

	@Override
	public Result getSimulatedResults(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws ApplicationException {
		Result simulatedResults = new Result();
		ArrangementOverviewBackendDelegate backendDelegate =
                DBPAPIAbstractFactoryImpl.getBackendDelegate(ArrangementOverviewBackendDelegate.class);
		simulatedResults = backendDelegate.getSimulatedResults(methodID,inputArray,request,response);
		return simulatedResults;
		
	}

	@Override
	public Result createAndGetPayOffSimulatedResults(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws ApplicationException {
		Result simulatedResults = new Result();
		ArrangementOverviewBackendDelegate backendDelegate =
                DBPAPIAbstractFactoryImpl.getBackendDelegate(ArrangementOverviewBackendDelegate.class);
		simulatedResults = backendDelegate.createAndGetPayOffSimulatedResults(methodID,inputArray,request,response);
		return simulatedResults;
		
	}

	
	@Override
	public List<ArrangementsDTO> getLatestBalances(String accountIdListStr,String authToken) throws ApplicationException, Exception {
        List<ArrangementsDTO> arrangementsDTO = new ArrayList<>();

        ArrangementOverviewBackendDelegate backendDelegate =
                DBPAPIAbstractFactoryImpl.getBackendDelegate(ArrangementOverviewBackendDelegate.class);

        arrangementsDTO = backendDelegate.getLatestBalances(accountIdListStr, authToken);
        return arrangementsDTO;
	}
	
	
	
	
	/*
	 * public HashMap<String,JSONObject> getLatestBalances(ArrangementsDTO
	 * inputPayload, DataControllerRequest request, String authToken, Boolean
	 * balanceFlag) throws ApplicationException {
	 * 
	 * ArrangementsExperienceAPIBackendDelegate backendDelegate =
	 * DBPAPIAbstractFactoryImpl.getBackendDelegate(
	 * ArrangementsExperienceAPIBackendDelegate.class);
	 * 
	 * ArrayList resp = backendDelegate.getBalanceForAccounts("","" ,authToken);
	 * return resp; }
	 */

}