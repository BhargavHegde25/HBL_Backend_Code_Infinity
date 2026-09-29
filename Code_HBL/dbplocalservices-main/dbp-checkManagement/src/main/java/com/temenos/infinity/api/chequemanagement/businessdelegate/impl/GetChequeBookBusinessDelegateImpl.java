package com.temenos.infinity.api.chequemanagement.businessdelegate.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.kony.dbputilities.util.CommonUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.api.chequemanagement.backenddelegate.api.GetChequeBookBackendDelegate;
import com.temenos.infinity.api.chequemanagement.businessdelegate.api.GetChequeBookBusinessDelegate;
import com.temenos.infinity.api.chequemanagement.constants.Constants;
import com.temenos.infinity.api.chequemanagement.dto.BBRequestDTO;
import com.temenos.infinity.api.chequemanagement.dto.ChequeBook;
import com.temenos.infinity.api.commons.exception.ApplicationException;

/**
 * TODO: Document me!
 *
 * @author smugesh
 *
 */
public class GetChequeBookBusinessDelegateImpl implements GetChequeBookBusinessDelegate {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public List<ChequeBook> getChequeBook(ChequeBook chequeBook, DataControllerRequest request)
            throws Exception {
        
        GetChequeBookBackendDelegate orderBackendDelegate = DBPAPIAbstractFactoryImpl
                .getBackendDelegate(GetChequeBookBackendDelegate.class);
        List<ChequeBook> chequeBookOrder = orderBackendDelegate.getChequeBookOrdersFromOMS(chequeBook, request); 
        return chequeBookOrder;  
    }
    
    @Override
    public List<ChequeBook> getChequeBookFromT24(ChequeBook chequeBook, DataControllerRequest request)
            throws Exception {
        
        GetChequeBookBackendDelegate orderBackendDelegate = DBPAPIAbstractFactoryImpl
                .getBackendDelegate(GetChequeBookBackendDelegate.class);
        List<ChequeBook> chequeBookOrder = orderBackendDelegate.getChequeBookOrdersFromT24(chequeBook, request); 
        return chequeBookOrder;  
    }

	@Override
	public List<BBRequestDTO> getBBRequests(String accountId) {
         List<BBRequestDTO> statusDTO = null;
		
		String serviceName = Constants.DBPRBLOCALSERVICEDB;
        String operationName = Constants.DB_BBREQUEST_GET;
        
        Map<String, Object> requestParameters = new HashMap<String, Object>();
        String filter = "featureActionId" + " eq " + Constants.FEATURE_ACTION_ID;
        filter +=  " and " + "accountId" + " eq " + accountId;
        
        requestParameters.put("$filter", filter);

		try {
			String response = DBPServiceExecutorBuilder.builder().
	        		withServiceId(serviceName).
	        		withObjectId(null).
	        		withOperationId(operationName).
	        		withRequestParameters(requestParameters).
	        		build().getResponse();
			JSONObject jsonRsponse = new JSONObject(response);
			JSONArray jsonArray = CommonUtils.getFirstOccuringArray(jsonRsponse);
			statusDTO = JSONUtils.parseAsList(jsonArray.toString(), BBRequestDTO.class);
		}
		
		catch(JSONException jsonExp) {
			alert.prepareError("JSONExcpetion occured while approving the request",jsonExp).log();
			return null;
		}
		catch(Exception exp) {
			alert.prepareError("Excpetion occured while approving the request",exp).log();
			return null;
		}

		return statusDTO;
	}
	

}
