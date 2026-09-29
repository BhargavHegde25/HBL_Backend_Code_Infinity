package com.temenos.dbx.product.payeeservices.backenddelegate.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.commonsutils.CommonUtils;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;
import com.temenos.dbx.product.payeeservices.backenddelegate.api.P2PPayeeBackendDelegate;
import com.temenos.dbx.product.payeeservices.dto.P2PPayeeBackendDTO;

public class P2PPayeeBackendDelegateImpl implements P2PPayeeBackendDelegate {

	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    
    @Override
	public P2PPayeeBackendDTO createPayee(P2PPayeeBackendDTO p2pPayeeBackendDTO, Map<String, Object> headerParams, DataControllerRequest dcRequest) {
		String serviceName = ServiceId.P2P_PAYEE_LINE_OF_BUSINESS_SERVICE;
		String operationName = OperationName.P2P_PAYEE_BACKEND_CREATE;
	        
        Map<String, Object> requestParameters;
		try {
			requestParameters = JSONUtils.parseAsMap(new JSONObject(p2pPayeeBackendDTO).toString(), String.class, Object.class);
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
					withRequestHeaders(headerParams).
					withDataControllerRequest(dcRequest).
					build().getResponse();
			
			JSONObject response = new JSONObject(createResponse);
			JSONArray jsonArray = CommonUtils.getFirstOccuringArray(response);
			p2pPayeeBackendDTO = JSONUtils.parse(jsonArray.getJSONObject(0).toString(), P2PPayeeBackendDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Failed to create payee at payee table: " + e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at createPayeeAtBackend: " + e).log();
			return null;
		}
		
		return p2pPayeeBackendDTO;
	}
	
	@Override
	public List<P2PPayeeBackendDTO> fetchPayees(Set<String> payeeIds, 
			Map<String, Object> headerParams, DataControllerRequest dcRequest) {
		List<P2PPayeeBackendDTO> backendPayeeDTOs = new ArrayList<P2PPayeeBackendDTO>();

		String serviceName = ServiceId.BACKENDPAYEESERVICESORCH;
		String operationName = OperationName.BACKENDP2PPAYEEGETORCH;
        
		Map<String, Object> requestParameters = new HashMap<String, Object>();
        requestParameters.put("id", String.join(",", payeeIds));
        requestParameters.put("loop_count", String.valueOf(payeeIds.size()));
        
        String payeeResponse = null;
        try {
        	payeeResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					withRequestHeaders(headerParams).
					withDataControllerRequest(dcRequest).
					build().getResponse();
        	
        	JSONObject responseObj = new JSONObject(payeeResponse);
			JSONArray records = responseObj.getJSONArray("LoopDataset");
			for(int i = 0; i < records.length(); i++) {
				List<P2PPayeeBackendDTO> payeeDTOs = new ArrayList<P2PPayeeBackendDTO>();
				responseObj = records.getJSONObject(i);
				P2PPayeeBackendDTO payee = JSONUtils.parse(responseObj.toString(), P2PPayeeBackendDTO.class);
				payeeDTOs.add(payee);
				JSONArray jsonArray = CommonUtils.getFirstOccuringArray(responseObj);
				if(jsonArray != null)
					payeeDTOs = JSONUtils.parseAsList(jsonArray.toString(), P2PPayeeBackendDTO.class);
				backendPayeeDTOs.addAll(payeeDTOs);
			}
        	
			

        }
        catch(JSONException e) {
            alert.prepareError("Failed to fetch p2p payees: " + e).log();
            return null;
        }
        catch(Exception e) {
            alert.prepareError("Caught exception at fetchPayeesFromBackend: " + e).log();
            return null;
        }
        return backendPayeeDTOs;
	}
	
	/**
     * Edits Payee at backend table - payee table
     *
     * @param p2pPayeeBackendDTO - contains details for backend table
     * @param headerParams             - request header params
     * @param dcRequest                - contains identity handler which is used to fetch UserId at core service
     * @return {@link P2PPayeeBackendDTO}
     */
    @Override
    public P2PPayeeBackendDTO editPayee(P2PPayeeBackendDTO p2pPayeeBackendDTO, Map<String, Object> headerParams, DataControllerRequest dcRequest) {
        String serviceName = ServiceId.P2P_PAYEE_LINE_OF_BUSINESS_SERVICE;
        String operationName = OperationName.P2P_PAYEE_BACKEND_EDIT;

        Map<String, Object> requestParameters;
        try {
            requestParameters = JSONUtils.parseAsMap(new JSONObject(p2pPayeeBackendDTO).toString(), String.class, Object.class);
            
        } catch (IOException e) {
            alert.prepareError("Error occurred while fetching the request params: " + e).log();
            return null;
        }
        
        String editResponse = null;
        String paypersonId = (String) requestParameters.get("payeeId");
        if (paypersonId !=null) {
        requestParameters.put("PayPersonId", paypersonId );
        }
        
        try {
            editResponse = DBPServiceExecutorBuilder.builder().
                    withServiceId(serviceName).
                    withObjectId(null).
                    withOperationId(operationName).
                    withRequestParameters(requestParameters).
                    withRequestHeaders(headerParams).
                    withDataControllerRequest(dcRequest).
                    build().getResponse();

            JSONObject response = new JSONObject(editResponse);
            Integer records=  (Integer) response.getInt("updatedRecords");
            JSONArray jsonArray = CommonUtils.getFirstOccuringArray(response);
            p2pPayeeBackendDTO = JSONUtils.parse(jsonArray.getJSONObject(0).toString(), P2PPayeeBackendDTO.class);
            if (records!= null) {
            	p2pPayeeBackendDTO.setUpdatedRecords(records);
            }
        }
        catch (JSONException e) {
            alert.prepareError("Failed to edit payee at payee table: " + e).log();
            return null;
        }
        catch (Exception e) {
            alert.prepareError("Caught exception at editPayeeAtBackend: " + e).log();
            return null;
        }

        return p2pPayeeBackendDTO;
    }
	
    /**
     * Deletes Payee at backend table - payee table
     *
     * @param p2pPayeeBackendDTO - contains details for backend table
     * @param headerParams             - request header params
     * @param dcRequest                - contains identity handler which is used to fetch UserId at core service
     * @return {@link P2PPayeeBackendDTO}
     */
    @Override
    public P2PPayeeBackendDTO deletePayee(P2PPayeeBackendDTO p2pPayeeBackendDTO, Map<String, Object> headerParams, DataControllerRequest dcRequest) {
        String serviceName = ServiceId.P2P_PAYEE_LINE_OF_BUSINESS_SERVICE;
        String operationName = OperationName.P2P_PAYEE_BACKEND_DELETE;

        Map<String, Object> requestParameters;
        try {
            requestParameters = JSONUtils.parseAsMap(new JSONObject(p2pPayeeBackendDTO).toString(), String.class, Object.class);
        } catch (IOException e) {
            alert.prepareError("Error occurred while fetching the request params: " + e).log();
            return null;
        }

        String deleteResponse = null;
        String paypersonId = (String) requestParameters.get("payeeId");
        if (paypersonId !=null) {
        requestParameters.put("PayPersonId", paypersonId );
        }
        try {
            deleteResponse = DBPServiceExecutorBuilder.builder().
                    withServiceId(serviceName).
                    withObjectId(null).
                    withOperationId(operationName).
                    withRequestParameters(requestParameters).
                    withRequestHeaders(headerParams).
                    withDataControllerRequest(dcRequest).
                    build().getResponse();

            
            JSONObject responseObject = new JSONObject(deleteResponse);
            Integer records=  (Integer) responseObject.getInt("deletedRecords");
            p2pPayeeBackendDTO = JSONUtils.parse(responseObject.toString(), P2PPayeeBackendDTO.class);
            if (records!= null) {
            	p2pPayeeBackendDTO.setDeletedRecords(records);
            }
        }
        catch (JSONException e) {
            alert.prepareError("Failed to delete payee from payee table: " + e).log();
            return null;
        }
        catch (Exception e) {
            alert.prepareError("Caught exception at deletePayeeAtBackend: " + e).log();
            return null;
        }
        if (paypersonId !=null && p2pPayeeBackendDTO.getDbpErrMsg() == null) {
        	p2pPayeeBackendDTO.setId(paypersonId);
        }
        return p2pPayeeBackendDTO;
    }
	
}