package com.temenos.auth.admininteg.operation;

import java.util.HashMap;
import java.util.Map;
import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.Record;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import java.util.List;

/**
 * 
 *  @author g.sailendra
 *  Java Service end point which will update the user's default legal entity in customer table
 *          
 */

public class UpdateDefaultLegalEntityOperation implements JavaService2 {
	
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance) throws Exception {
		Result result = new Result();
        Map<String, String> updateInput = new HashMap<>();
        try {
			if (requestInstance.containsKeyInRequest("defaultLegalEntity")) {
				 String  defaultLegalEntity = requestInstance.getParameter("defaultLegalEntity");
				 String customerId = requestInstance.getServicesManager().getIdentityHandler().getUserId();
					String filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + customerId
							+ DBPUtilitiesConstants.AND
							+ "Status_id" + DBPUtilitiesConstants.EQUAL + "SID_CUS_ACTIVE";
					Map<String, Object> inputParams = new HashMap<>();
					inputParams.put(DBPUtilitiesConstants.FILTER, filter);
					Result customerLegalEntity = HelperMethods.callApi(requestInstance, inputParams, HelperMethods.getHeaders(requestInstance), URLConstants.CUSTOMERLEGALENTITY_GET);
					Map < String, String > customerLegalEntitySet = legalEntityMap(customerLegalEntity);
					if (!customerLegalEntitySet.containsKey(defaultLegalEntity))
						return ErrorCodeEnum.ERR_10019.setErrorCode(new Result());
				 updateInput.put("id",requestInstance.getParameter("userId").toString());
				 updateInput.put("defaultLegalEntity", defaultLegalEntity);
				 diagnostic.prepareDebug("input data for update default entity : "+updateInput).log();
		         result = HelperMethods.callApi(requestInstance, updateInput, HelperMethods.getHeaders(requestInstance),URLConstants.CUSTOMER_UPDATE);
			}
			updateInput.clear();
	        return result;
       }
        catch(Exception e) {
			result.addErrMsgParam("error occurred while updating default entity");
        	return result;
        }
	}
	public static Map<String, String> legalEntityMap (Result customerLegalEntity) {
		Map<String, String> customerLegalEntitySet = new HashMap<>();
			List<Record> customerLegalEntityRecords = customerLegalEntity
					.getDatasetById("customerlegalentity").getAllRecords();	
			for(Record s : customerLegalEntityRecords) {
				customerLegalEntitySet.put(s.getParamValueByName("legalEntityId"),"true");
			}
		return  customerLegalEntitySet;
	}
}
