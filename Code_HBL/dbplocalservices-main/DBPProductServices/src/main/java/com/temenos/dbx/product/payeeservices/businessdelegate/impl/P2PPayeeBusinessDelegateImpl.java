package com.temenos.dbx.product.payeeservices.businessdelegate.impl;

import java.io.IOException;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.temenos.dbx.product.commonsutils.CommonUtils;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;
import com.temenos.dbx.product.payeeservices.businessdelegate.api.P2PPayeeBusinessDelegate;
import com.temenos.dbx.product.payeeservices.dto.P2PPayeeDTO;

public class P2PPayeeBusinessDelegateImpl implements P2PPayeeBusinessDelegate {

	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    
	@Override
	public P2PPayeeDTO createPayeeAtDBX(P2PPayeeDTO p2pPayeeDTO) {
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
        String operationName = OperationName.DB_P2PPAYEE_CREATE;
	        
        Map<String, Object> requestParameters;
		try {
			requestParameters = JSONUtils.parseAsMap(new JSONObject(p2pPayeeDTO).toString(), String.class, Object.class);
		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the request params: " + e).log();
			return null;
		}
			
		String createResponse = null;
		try {
			createResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			
			JSONObject response = new JSONObject(createResponse);
			JSONArray responseArray = CommonUtils.getFirstOccuringArray(response);
			p2pPayeeDTO = JSONUtils.parse(responseArray.getJSONObject(0).toString(), P2PPayeeDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Failed to create payee at p2ppayee table: " + e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at createPayeeAtDBX: " + e).log();
			return null;
		}
		
		return p2pPayeeDTO;
	}
	
	@Override
	public List<P2PPayeeDTO> fetchPayeesFromDBX(Set<String> associatedCifs, String legalEntityId) {
		
		final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
		final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
		
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_P2PPAYEE_GET;
	
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		 
        String filter = "";
        for (String cif : associatedCifs) {
			if(filter.isEmpty()) 
				filter = "cif" + DBPUtilitiesConstants.EQUAL + cif;
			else
				filter = filter +  DBPUtilitiesConstants.OR +"cif" + DBPUtilitiesConstants.EQUAL + cif;
		}
        if (StringUtils.isNotBlank(legalEntityId)) {
            filter = DBPUtilitiesConstants.OPEN_BRACE + filter + DBPUtilitiesConstants.CLOSE_BRACE + DBPUtilitiesConstants.AND + "legalEntityId" + DBPUtilitiesConstants.EQUAL + legalEntityId;
        }
		requestParameters.put(DBPUtilitiesConstants.FILTER, filter);
		
		String payeeResponse = null;
		List<P2PPayeeDTO> p2pPayeeDTOs = null;
		
		try {
			payeeResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			JSONObject responseObj = new JSONObject(payeeResponse);
			JSONArray jsonArray = CommonUtils.getFirstOccuringArray(responseObj);
			p2pPayeeDTOs = JSONUtils.parseAsList(jsonArray.toString(), P2PPayeeDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Failed to fetch pay person payee Ids: " + e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at fetch pay person payee Ids: " + e).log();
			return null;
		}
		return p2pPayeeDTOs;
	}
	
	/**
     * Fetches payee details from dbx table using given payeeId
     *
     * @param payeeId
     * @return {@link P2PPayeeDTO}
     */
    @Override
    public List<P2PPayeeDTO>  fetchPayeeByIdAtDBX(P2PPayeeDTO p2pPayeeDTO) {
        String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
        String operationName = OperationName.DB_P2PPAYEE_GET;

        Map<String, Object> requestParameters = new HashMap<String, Object>();
        String filter = "payeeId" + DBPUtilitiesConstants.EQUAL + p2pPayeeDTO.getPayeeId();
        
		if(p2pPayeeDTO.getContractId() != null) {
			filter += DBPUtilitiesConstants.AND + "customerId" + DBPUtilitiesConstants.EQUAL + p2pPayeeDTO.getContractId();
		}
		
		if(p2pPayeeDTO.getCif() != null) {
			filter += DBPUtilitiesConstants.AND +"cif" + DBPUtilitiesConstants.EQUAL + p2pPayeeDTO.getCif();
		}
		
		if (StringUtils.isNotBlank(p2pPayeeDTO.getLegalEntityId())) {
		filter += DBPUtilitiesConstants.AND + "legalEntityId" + DBPUtilitiesConstants.EQUAL + p2pPayeeDTO.getLegalEntityId();
		}
		
		requestParameters.put(DBPUtilitiesConstants.FILTER, filter);

        List<P2PPayeeDTO> p2pPayeeDTOs = null;
        String payeeResponse = null;
        try {
            payeeResponse = DBPServiceExecutorBuilder.builder().
                    withServiceId(serviceName).
                    withObjectId(null).
                    withOperationId(operationName).
                    withRequestParameters(requestParameters).
                    build().getResponse();
            JSONObject responseObj = new JSONObject(payeeResponse);
            JSONArray jsonArray = CommonUtils.getFirstOccuringArray(responseObj);
            p2pPayeeDTOs = JSONUtils.parseAsList(jsonArray.toString(), P2PPayeeDTO.class);
        }
        catch (JSONException e) {
            alert.prepareError("Failed to fetch p2p payee by id: " + e).log();
            return null;
        }
        catch (Exception e) {
            alert.prepareError("Caught exception at fetchPayeeByIdAtDBX: " + e).log();
            return null;
        }

        return p2pPayeeDTOs;
    }
       
	@Override
    public boolean editPayeeAtDBX(String id) {
        String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
        String operationName = OperationName.DB_P2PPAYEE_UPDATE;
        Date updatedAt = new Date(new java.util.Date().getTime());
        Map<String, Object> requestParams = new HashMap<String, Object>();
		requestParams.put("id", id);
		requestParams.put("updatedts", updatedAt);
        String editResponse = null;
        
        try {
        	 editResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParams).
					build().getResponse();

            JSONObject response = new JSONObject(editResponse);
            if(response.getInt("opstatus") == 0 && response.getInt("httpStatusCode") == 0 && response.getInt("updatedRecords") == 1) {
				return true;
			}
   
        }
        catch (JSONException e) {
            alert.prepareError("Failed to edit payee at payee table: " + e).log();
            return false;
        }
        catch (Exception e) {
            alert.prepareError("Caught exception at editPayeeAtBackend: " + e).log();
            return false;
        }

        return true;
    }
	
    @Override
	public boolean deletePayeeAtDBX(P2PPayeeDTO p2pPayeeDTO) {
		
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_P2PPAYEE_DELETE;
	
		Map<String, Object> requestParams = new HashMap<String, Object>();
		String filter = "payeeId" + DBPUtilitiesConstants.EQUAL + p2pPayeeDTO.getPayeeId();
		
		if(p2pPayeeDTO.getContractId() != null) {
			filter += DBPUtilitiesConstants.AND +"contractId" + DBPUtilitiesConstants.EQUAL + p2pPayeeDTO.getContractId();
		}
		
		if(p2pPayeeDTO.getCif() != null) {
			filter += DBPUtilitiesConstants.AND +"cif" + DBPUtilitiesConstants.EQUAL + p2pPayeeDTO.getCif();
		}
		
		requestParams.put(DBPUtilitiesConstants.FILTER, filter);
		
		try {
			String deleteResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParams).
					build().getResponse();
			JSONObject jsonRsponse = new JSONObject(deleteResponse);
			if(jsonRsponse.getInt("opstatus") == 0 && jsonRsponse.getInt("httpStatusCode") == 0 && jsonRsponse.getInt("deletedRecords") == 1) {
				return true;
			}
		}
		
		catch(JSONException jsonExp) {
			alert.prepareError("JSONExcpetion occured while deleting the p2p payee",jsonExp).log();
			return false;
		}
		catch(Exception e) {
			alert.prepareError("Excpetion occured while deleting the p2p payee",e).log();
			return false;
		}
		
		return false;	
	}

}