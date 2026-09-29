package com.temenos.dbx.datamigrationservices.backend.impl;

import java.io.IOException;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.datamigrationservices.backend.api.MigratePayeesBackendDelegate;
import com.temenos.dbx.datamigrationservices.javaservice.CreateBillPayPayee;
import com.temenos.dbx.product.commonsutils.CommonUtils;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;
import com.temenos.dbx.product.payeeservices.dto.P2PPayeeBackendDTO;

public class MigratePayeesBackendDelegateImpl implements MigratePayeesBackendDelegate {
	private static final Logger LOG = LogManager.getLogger(CreateBillPayPayee.class);
	@Override
	public P2PPayeeBackendDTO createPayee(P2PPayeeBackendDTO p2pPayeeBackendDTO, Map<String, Object> headerParams, DataControllerRequest dcRequest) {
		String serviceName = "DataMigration";
		String operationName = OperationName.P2P_PAYEE_BACKEND_CREATE;
	        
        Map<String, Object> requestParameters;
		try {
			requestParameters = JSONUtils.parseAsMap(new JSONObject(p2pPayeeBackendDTO).toString(), String.class, Object.class);
		} catch (IOException e) {
			LOG.error("Error occured while fetching the request params: " + e);
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
			LOG.error("Failed to create payee at payee table: " + e);
			return null;
		}
		catch (Exception e) {
			LOG.error("Caught exception at createPayeeAtBackend: " + e);
			return null;
		}
		
		return p2pPayeeBackendDTO;
	}

}
