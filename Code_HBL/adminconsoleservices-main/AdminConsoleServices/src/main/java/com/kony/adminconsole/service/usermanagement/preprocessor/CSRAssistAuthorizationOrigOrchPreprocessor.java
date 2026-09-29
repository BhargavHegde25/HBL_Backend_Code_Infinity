package com.kony.adminconsole.service.usermanagement.preprocessor;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.ODataQueryConstants;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class CSRAssistAuthorizationOrigOrchPreprocessor implements DataPreProcessor2 {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

	private static final String INPUT_CUSTOMERID = "customerid";
			
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		Log4j2Configurator.getInstance();
		
		boolean status = true;
		
		try {
			
			String customerId = request.getParameter(INPUT_CUSTOMERID);
			
			if(StringUtils.isNotBlank(customerId)) {
				
				String userId = getInfinityUserId(request, customerId);
				
				if(StringUtils.isNotBlank(userId)){
					
					request.addRequestParam_(INPUT_CUSTOMERID, userId);
					
				} else {
					
					ErrorCodeEnum.ERR_20554.setErrorCode(result);
					status = false;
				}
			
			} else {
				ErrorCodeEnum.ERR_20688.setErrorCode(result);
				status = false;
			}
			
		} catch(Exception exp) {
			alert.prepareError("Encountered exception while fetching Infinity User Id from Party Id "+exp).log();
			ErrorCodeEnum.ERR_20553.setErrorCode(result);
			status = false;
		}
		return status;
	}
	
	
	private String getInfinityUserId(DataControllerRequest request, String backendId) {
		
		
		String userId = StringUtils.EMPTY;
		try {
			
			Map<String, String> postParamsMap = new HashMap<>();
			postParamsMap.put(ODataQueryConstants.FILTER, "BackendId eq " + backendId +
					" and " + "BackendType eq PARTY");
			postParamsMap.put(ODataQueryConstants.SELECT, "Customer_id");
			
			String readBackendIdentifierResponse = Executor.invokeService(ServiceURLEnum.BACKENDIDENTIFIER_READ,
					postParamsMap, null, request);
			if (readBackendIdentifierResponse != null) {
				JSONObject serviceResponseJSON = CommonUtilities.getStringAsJSONObject(readBackendIdentifierResponse);

				JSONArray serviceResponseArray = serviceResponseJSON.optJSONArray("backendidentifier");
				if (serviceResponseArray.length() != 0) {
					userId = serviceResponseArray.optJSONObject(0).optString("Customer_id");
				}
			}
			
		} catch(Exception exp){
			alert.prepareError("Encountered exception while fetching Infinity User Id from DB by Party Id "+exp).log();
		}
		
		return userId;
	}
	
}
