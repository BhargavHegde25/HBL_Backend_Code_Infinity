package com.temenos.infinity.api.checkdep.businessdelegate.impl;

import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONObject;
import com.dbp.core.fabric.extn.DBPServiceInvocationWrapper;
import com.dbp.core.util.JSONUtils;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;
import com.temenos.infinity.api.checkdep.businessdelegate.api.RDCBusinessDelegate;
import com.temenos.infinity.api.checkdep.dto.RDCDTO;
import com.konylabs.middleware.controller.DataControllerRequest;

public class RDCBusinessDelegateImpl implements RDCBusinessDelegate{

	@Override
	public RDCDTO createRDC(RDCDTO createDTO, DataControllerRequest request) {
		
		Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
		//String serviceName = ServiceId.RDC_LINE_OF_BUSINESS_SERVICE;
		String serviceName = "CardlessCashServices";
		//String operationName = OperationName.RDC_CREATE;
		String operationName = "createTransfer";
		
		RDCDTO resultDTO = null;

		try {

			Map<String, Object> headerParams = request.getHeaderMap();
			
			JSONObject requestJSON = new JSONObject(createDTO);
			Map<String, Object> requestParams = JSONUtils.parseAsMap(requestJSON.toString(), String.class, Object.class);
			
			String createResponse = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, null,
					operationName, requestParams, headerParams, request);
			
			JSONObject responseJSON = new JSONObject(createResponse);
			resultDTO = JSONUtils.parse(responseJSON.toString(), RDCDTO.class);
		}
		catch(Exception exp) {
			alert.prepareError("Exception occured while invoking integration operation", exp).log();	
		}
		
		return resultDTO;
	}

}