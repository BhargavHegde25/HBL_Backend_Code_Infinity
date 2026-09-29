package com.temenos.infinity.api.cards.javaservice;

import java.util.HashMap;
import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.constants.DBPConstants;
import com.dbp.core.fabric.extn.DBPServiceInvocationWrapper;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.kony.AdminConsole.Utilities.AdminConsoleOperations;
//import com.temenos.infinity.api.cards.utils.HelperMethods;
import com.kony.AdminConsole.Utilities.ServiceConfig;
import com.kony.dbputilities.utils.HelperMethods;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.cards.constants.OperationName;
import com.temenos.infinity.api.cards.constants.ServiceId;


public class CreateCardRequest implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {
		Log4j2Configurator.getInstance();

        String cardId = requestInstance.getParameter("cardId");
        String CardAccountName = requestInstance.getParameter("CardAccountName");

        Map<String, Object> inputMap = com.temenos.infinity.api.cards.utils.HelperMethods.getInputMapFromInputArray(inputArray);
        String Username = (String) requestInstance.getServicesManager().getIdentityHandler().getUserAttributes().get("UserName");

        String AccountType = requestInstance.getParameter("AccountType");
        String RequestCode = requestInstance.getParameter("RequestCode");
        String RequestReason = requestInstance.getParameter("RequestReason");
        String Channel = requestInstance.getParameter("Channel");
        String Address_id = requestInstance.getParameter("Address_id");
        String communication_id = requestInstance.getParameter("communication_id");
        String AdditionalNotes = requestInstance.getParameter("AdditionalNotes");
        if(StringUtils.isBlank(CardAccountName) || StringUtils.isBlank(AccountType)) {
        	Result errorResult =new Result();
        	errorResult.addErrMsgParam("Mandatory fields are missing.");
            return errorResult;
        }
        if(!isValidInputParams(CardAccountName,AccountType,RequestCode,RequestReason,Channel,Address_id,communication_id,AdditionalNotes)) {
        	Result errorResult =new Result();
        	errorResult.addErrMsgParam("Invalid input params.");
            return errorResult;
        }
        
        String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationNameForGet = OperationName.DB_CARDS_GET;
		String filtercardnumber =  "Id"		+ " eq " + cardId ;
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("$filter", filtercardnumber);
		String updateCards = "";
		String CardAccountNumber="";
		try {
			updateCards = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, null,
					operationNameForGet, hashMap, null, "");
			JSONObject jsonRsponse = new JSONObject(updateCards);
			JSONArray countJsonArr = jsonRsponse.getJSONArray("card");
			JSONObject cardNumberdata = countJsonArr.getJSONObject(0);
			 CardAccountNumber=(String) cardNumberdata.get("cardNumber");
			
		} catch (Exception e) {
			alert.prepareError(e.toString()).log();
		}
		hashMap.clear();


        JSONObject getResponse = createCardRequest(CardAccountNumber, CardAccountName, Username, AccountType,
                RequestCode, RequestReason, Channel, Address_id, communication_id, AdditionalNotes, requestInstance);

        if (getResponse.getInt(DBPConstants.FABRIC_OPSTATUS_KEY) != 0) {
            String authToken = AdminConsoleOperations.login(requestInstance);
            ServiceConfig.setValue("Auth_Token", authToken);
            getResponse = createCardRequest(CardAccountNumber, CardAccountName, Username, AccountType, RequestCode,
                    RequestReason, Channel, Address_id, communication_id, AdditionalNotes, requestInstance);
        }
        Result processedResult = HelperMethods.constructResultFromJSONObject(getResponse);
        return processedResult;

    }

    public JSONObject createCardRequest(String CardAccountNumber, String CardAccountName, String Username,
            String AccountType, String RequestCode, String RequestReason, String Channel, String Address_id,
            String communication_id, String AdditionalNotes, DataControllerRequest dcRequest) {

        Map<String, Object> postParametersMap = new HashMap<>();
        postParametersMap.put("CardAccountNumber", CardAccountNumber);
        postParametersMap.put("CardAccountName", CardAccountName);
        postParametersMap.put("Username", Username);
        postParametersMap.put("AccountType", AccountType);
        postParametersMap.put("RequestCode", RequestCode);
        postParametersMap.put("RequestReason", RequestReason);
        postParametersMap.put("Channel", Channel);
        postParametersMap.put("Address_id", Address_id);
        postParametersMap.put("communication_id", communication_id);
        postParametersMap.put("AdditionalNotes", AdditionalNotes);

        String getResponseString = HelperMethods.invokeC360ServiceAndGetString(dcRequest, postParametersMap,
                new HashMap<>(), "createCardRequest");

        return com.temenos.infinity.api.cards.utils.HelperMethods.getStringAsJSONObject(getResponseString);
    }
    
    private boolean isValidInputParams(String cardAccountName,String accountType,String requestCode,String requestReason,String channel,String addressId,String communicationId,String additionalNotes) {
    	boolean isValid=true;
    	String validChars="^[a-zA-Z0-9\\s_-]+$";
    	if(cardAccountName!=null && !cardAccountName.matches(validChars)) {
       	 isValid = false;
        }else if(accountType!=null && !accountType.matches(validChars)) {
        	 isValid = false;
         }else if(requestCode!=null && !requestCode.matches(validChars)) {
        	 isValid = false;
         }else if(requestReason!=null && !requestReason.matches(validChars)) {
        	 isValid = false;
         }else if(channel!=null && !channel.matches(validChars)) {
        	 isValid = false;
         }else if(addressId!=null && !addressId.matches(validChars)) {
        	 isValid = false;
         }else if(communicationId!=null && !communicationId.matches(validChars)) {
        	 isValid = false;
         }
    	return isValid;
    }
    

}
