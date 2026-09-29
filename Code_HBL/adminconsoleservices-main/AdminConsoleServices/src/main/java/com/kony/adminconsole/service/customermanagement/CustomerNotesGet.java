package com.kony.adminconsole.service.customermanagement;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.commons.utils.ODataQueryConstants;
import com.kony.adminconsole.handler.ApplicationParametersHandler;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

/**
 * CustomerNotesGet service is retrieve notes
 * 
 * @author Sri Kavya Pitchika (KH2573)
 * 
 */
public class CustomerNotesGet implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance) throws Exception {

		try {
			Result processedResult = new Result();
			String customerID = requestInstance.getParameter("Customer_id");
			if (StringUtils.isBlank(customerID)) {
				ErrorCodeEnum.ERR_20565.setErrorCode(processedResult);
				processedResult.addParam(new Param("status", "failure", FabricConstants.STRING));
				return processedResult;
			}
			String isKeyCloakEnabled = ApplicationParametersHandler.fetchIsKeyCloakEnabled(requestInstance);
			ServiceURLEnum constant_value = null;
			if (isKeyCloakEnabled.equals("true")) {
				constant_value = ServiceURLEnum.CUSTOMERNOTESFETCH_VIEW;
			} else {
				constant_value = ServiceURLEnum.CUSTOMERNOTES_SYSTEMUSERS;
			}

			// Construct Filter Query
	        String filterQuery="(Customer_id eq '" + customerID + "')";
	        // Construct Input map
	        Map<String, String> inputMap = new HashMap<>();
	        inputMap.put(ODataQueryConstants.FILTER, filterQuery);
	        inputMap.put(ODataQueryConstants.ORDER_BY, "createdts asc");
			String readNotesResponse = Executor.invokeService(constant_value, inputMap, null, requestInstance);
	        JSONObject readNotesResponseJSON = CommonUtilities.getStringAsJSONObject(readNotesResponse);
	        
            if (readNotesResponseJSON.getInt(FabricConstants.OPSTATUS)!= 0) {
            	ErrorCodeEnum.ERR_20001.setErrorCode(processedResult);
				processedResult.addParam(new Param("status", "failure", FabricConstants.STRING));
				return processedResult;
            }
			if(isKeyCloakEnabled.equals("true")) {
				//Fetch keycloak users
				String readSystemUserResponse = Executor.invokeService(ServiceURLEnum.KEYCLOAK_USERS,
						new HashMap<String, String>(), null, requestInstance);
	            JSONObject readSystemUserResponseJSON = CommonUtilities.getStringAsJSONObject(readSystemUserResponse);
	            int opStatusCode = readSystemUserResponseJSON.getInt(FabricConstants.OPSTATUS);
	            if (opStatusCode == 0) {
	                JSONArray systemuserJSONArray = readSystemUserResponseJSON.getJSONArray("internalusers_view");
	                Map <String, JSONObject> map = new HashMap<String, JSONObject>();
	                for (int indexVar = 0; indexVar < systemuserJSONArray.length(); indexVar++) {
	                	JSONObject currRecordJSONObject = systemuserJSONArray.getJSONObject(indexVar);
	                	map.put(currRecordJSONObject.getString("User_id"),currRecordJSONObject);
	                }
	                JSONArray readNotesResponseArray= readNotesResponseJSON.getJSONArray("customernotesfetch_view");
	                JSONArray notesArray=new JSONArray();
	                for (int indexVar = 0; indexVar < readNotesResponseArray.length(); indexVar++) {
	                	String userId=readNotesResponseArray.getJSONObject(indexVar).getString("InternalUser_id");
	                	JSONObject userJson=map.get(userId);
	                	if(userJson!=null) {
	                	JSONObject currentnoteJson = readNotesResponseArray.getJSONObject(indexVar);
	                	currentnoteJson.put("InternalUser_FirstName", userJson.getString("FirstName"));
	                	currentnoteJson.put("InternalUser_Username", userJson.getString("Username"));
	                	currentnoteJson.put("InternalUser_MiddleName", ""); //Not applicable for keycloak
	                	currentnoteJson.put("InternalUser_LastName", userJson.getString("LastName"));
	                	currentnoteJson.put("InternalUser_Email", userJson.getString("Email"));
	                	notesArray.put(currentnoteJson);
	                	}	                		
	                }
	                readNotesResponseJSON.put("customernotes_view", notesArray);
	                readNotesResponseJSON.remove("customernotesfetch_view");
	            }
			}
			processedResult = CommonUtilities.getResultObjectFromJSONObject(readNotesResponseJSON);	
			return processedResult;
		} catch (Exception e) {
			Result errorResult = new Result();
			diagnostic.prepareDebug("Runtime Exception.Exception Trace:", e).log();
			ErrorCodeEnum.ERR_20001.setErrorCode(errorResult);
			return errorResult;
		}

	}

}