package com.temenos.dbx.product.commons.backenddelegate.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.temenos.dbx.product.achservices.dto.ACHFileDTO;
import com.temenos.dbx.product.commons.backenddelegate.api.CancellationReasonBackendDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.CustomerBusinessDelegate;
import com.temenos.dbx.product.commons.dto.CancellationReasonDTO;
import com.temenos.dbx.product.commonsutils.CommonUtils;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;
import com.konylabs.middleware.controller.DataControllerRequest;

public class CancellationReasonBackendDelegateImpl implements CancellationReasonBackendDelegate {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	
	@Override
	
	public List<CancellationReasonDTO> fetchCancellationReasons(DataControllerRequest dcr) {
		
		String serviceName = ServiceId.DBPNPBULKPAYMENTSERVICES;
		String operationName = OperationName.CANCELLATION_REASONS_GET_OPERATION;
		String response;
		List<CancellationReasonDTO> reasons;
		
		try {
			response = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(null).
					withRequestHeaders(dcr.getHeaderMap()).
					withDataControllerRequest(dcr).
					build().getResponse();
			JSONObject responseObj = new JSONObject(response);
            JSONArray jsonArray = CommonUtils.getFirstOccuringArray(responseObj);
            reasons = JSONUtils.parseAsList(jsonArray.toString(), CancellationReasonDTO.class);

		}
		catch (JSONException e) {
			alert.prepareError("Error in Downloading Bulk Payment Cancellation Reasons", e).log();			
			return null;
		} 
		catch (Exception e) {
			alert.prepareError("Error occured in fetchCancellationReasons", e).log();			
			return null;
		}
		
		return reasons;
	}

}
