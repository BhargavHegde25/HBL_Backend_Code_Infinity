package com.temenos.infinity.api.arrangements.javaservice;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.TokenUtils;
import com.kony.dbx.util.CommonUtils;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import org.apache.commons.lang3.StringUtils;
import com.temenos.infinity.api.arrangements.constants.TemenosConstants;
import com.temenos.infinity.api.arrangements.utils.ArrangementsUtils;
import com.temenos.infinity.api.commons.invocation.Executor;
import com.temenos.infinity.api.arrangements.config.ArrangementsAPIServices;
import com.temenos.infinity.transact.tokenmanager.exception.CertificateNotRegisteredException;

public class closeAccountAck implements JavaService2 {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
		Log4j2Configurator.getInstance();
    	 Result res = new Result(); 
    	 Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
         String customerId = inputParams.get("customerid");
         String accountId = inputParams.get("accountNumber"); 	
         String legalEntityId = inputParams.get("legalEntityId"); 	
         String orderId = inputParams.get("orderId"); 	
         if (StringUtils.isBlank(orderId)){
        	 res.addErrMsgParam("Order id missing");
        	 return res;
         }
 		 String status = inputParams.get("status").equals("CLOSURE_REJECTED")?"ACTIVE":inputParams.get("status").equals("CLOSURE_SUCCESS")?"CLOSED":inputParams.get("status");		 		
         Boolean suspend = updateCustomerAccountsDBX(dcRequest,customerId,accountId,legalEntityId,status);
         if(suspend) {
        	status = "SUSPENDED";
         }
         String externalStatus = inputParams.get("status").equals("CLOSURE_REJECTED")?"Failed":inputParams.get("status").equals("CLOSURE_SUCCESS")?"Account Closed":inputParams.get("status");
         updateSRMSOrder(orderId,externalStatus, customerId);
         
         res.addParam("status", status);
    	 return res;
    	
    }
    
    private void updateSRMSOrder(String orderId, String externalStatus, String customerId) {
    	Map<String, Object> servinputMap = new HashMap<>();
		Map<String, Object> servHeaderMap = new HashMap<>();

		String authToken = "";
		try {
			Map<String, String> inputMap = new HashMap<>();
			inputMap.put("customerId",customerId);
			authToken = TokenUtils.getAMSAuthToken(inputMap);
		} catch (CertificateNotRegisteredException e) {
			alert.prepareError("Certificate Not Registered" + e).log();
		} catch (Exception e) {
			alert.prepareError("Exception occured during generation of authToken " + e).log();
		}
		servHeaderMap = ArrangementsUtils.generateSecurityHeadersForSRMS(authToken, servHeaderMap);
		String response;
		servinputMap.put("status", externalStatus);
		servinputMap.put("serviceReqId", orderId);
		try {
			diagnostic.prepareDebug("PUT SR Request Payload:"+servinputMap).log();
			response = Executor.invokeService(ArrangementsAPIServices.SERVICEREQUESTJSON_UPDATEORDER,
					servinputMap, servHeaderMap);
			diagnostic.prepareDebug("Service response:" + response).log();
		} catch (Exception e) {
			alert.prepareError("Failed to call SRMS Update API , to send acknowledgement").log();
		}
		
	}

	public static Boolean updateCustomerAccountsDBX(DataControllerRequest request, String customerId, String accountId, String legalEntityId, String status) throws Exception {
		HashMap<String, Object> inputParams = new HashMap<String, Object>();
		HashMap<String, Object> headerParams = new HashMap<String, Object>();
		Boolean suspend = false;
		String companyId = com.temenos.infinity.api.arrangements.utils.CommonUtils.getCompanyId(request);
		inputParams.put("$filter", "Account_id eq " + accountId +" and "+"Customer_id eq "+customerId);
		inputParams.put("accountId", accountId);
		inputParams.put("statusFlag",status );
		inputParams.put("legalEntityId", companyId);
		Result readAccounts = CommonUtils.callIntegrationService(request, inputParams, headerParams,
				TemenosConstants.SERVICE_BACKEND_CERTIFICATE, TemenosConstants.CLOSE_ACCOUNT_PROC, false);
		JSONObject resp =new JSONObject(ResultToJSON.convert(readAccounts));
		JSONArray suspendedAccounts = (JSONArray) resp.get("records");
		HashSet hs = new HashSet();
		for (int i=0;i<suspendedAccounts.length();i++) {
			JSONObject acc = (JSONObject) suspendedAccounts.get(i);
			hs.add(acc.get("suspendedaccounts"));
		}
		String strNames = String.join(",", hs);
		if (hs.contains(customerId)) suspend = true;
		return suspend;		
	}
	
	
}
