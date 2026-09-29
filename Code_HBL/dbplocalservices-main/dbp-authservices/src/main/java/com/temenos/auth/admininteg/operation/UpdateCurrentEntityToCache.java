package com.temenos.auth.admininteg.operation;

import org.apache.commons.lang3.StringUtils;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.LegalEntityUtil;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.URLConstants;
import java.util.HashMap;
import java.util.Map;
import com.konylabs.middleware.dataobject.Record;
import com.kony.dbputilities.util.ErrorCodeEnum;
import java.util.List;
/**
 * 
 * 	@author g.sailendra
 *  Java Service end point which will store the currentLegalEntity in cache
 *          
 */

public class UpdateCurrentEntityToCache implements JavaService2 {
	

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance) throws Exception {
		Result result = new Result();
		String  currentLegalEntityId = null;
        try {
			if (requestInstance.containsKeyInRequest("currentLegalEntityId"))
				currentLegalEntityId = requestInstance.getParameter("currentLegalEntityId");
			
			if (!StringUtils.isNotBlank(currentLegalEntityId)) {
				result.addErrMsgParam("currentLegalEntityId was Null or Empty");
			    return result;
			}
			String customerId = requestInstance.getServicesManager().getIdentityHandler().getUserId();
			String filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + customerId
					+ DBPUtilitiesConstants.AND
					+ "Status_id" + DBPUtilitiesConstants.EQUAL + "SID_CUS_ACTIVE";
			Map<String, Object> inputParams = new HashMap<>();
			inputParams.put(DBPUtilitiesConstants.FILTER, filter);
			Result customerLegalEntity = HelperMethods.callApi(requestInstance, inputParams, HelperMethods.getHeaders(requestInstance), URLConstants.CUSTOMERLEGALENTITY_GET);
			Map < String, String > customerLegalEntitySet = legalEntityMap(customerLegalEntity);
			if (!customerLegalEntitySet.containsKey(currentLegalEntityId))
				return ErrorCodeEnum.ERR_10019.setErrorCode(new Result());
			LegalEntityUtil.setCurrentLegalEntityIdInCache(requestInstance, currentLegalEntityId);
			result.addStringParam("Status","SUCCESS");
			result.addStringParam("LegalEntityId", currentLegalEntityId);
			result.addOpstatusParam(0);
			result.addHttpStatusCodeParam(200);
	        return result;
			
       }
        catch(Exception e) {
			result.addErrMsgParam("error occurred while updating entity in cache");
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
