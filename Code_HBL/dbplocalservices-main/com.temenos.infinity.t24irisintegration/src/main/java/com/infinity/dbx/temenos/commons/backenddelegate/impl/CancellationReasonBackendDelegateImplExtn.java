package com.infinity.dbx.temenos.commons.backenddelegate.impl;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.temenos.dbx.product.commons.backenddelegate.impl.CancellationReasonBackendDelegateImpl;
import com.temenos.dbx.product.commons.dto.CancellationReasonDTO;
import com.konylabs.middleware.controller.DataControllerRequest;


public class CancellationReasonBackendDelegateImplExtn extends CancellationReasonBackendDelegateImpl {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	public List<CancellationReasonDTO> fetchCancellationReasons(DataControllerRequest dcr) {
		    String BULKPAYMENT_BACKEND= EnvironmentConfigurationsHandler.getValue("BULKPAYMENT_BACKEND");
		    String serviceName;
		    if((BULKPAYMENT_BACKEND!=null) && BULKPAYMENT_BACKEND.equalsIgnoreCase("MOCK")){
		    	serviceName = "MOCKBulkPaymentAPIs";
		    }
		    else {
		    serviceName = TemenosConstants.SERVICE_T24IS_BULK_PAYMENTS;
		    }
			String operationName = TemenosConstants.OP_FETCH_CANCELLATION_REASONS;
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
				JSONArray jsonArray = TemenosUtils.getFirstOccuringArray(responseObj);
	            reasons = JSONUtils.parseAsList(jsonArray.toString(), CancellationReasonDTO.class);
			}
			catch (JSONException e) {
				alert.prepareError("Error in Fetching Cancellation Reasons", e).log();			
				return null;
			} 
			catch (Exception e) {
				alert.prepareError("Error occured in fetchCancellationReasons", e).log();			
				return null;
			}
			
			return reasons;
		}
	
	
}
  