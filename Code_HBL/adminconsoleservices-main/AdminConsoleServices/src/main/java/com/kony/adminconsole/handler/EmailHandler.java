package com.kony.adminconsole.handler;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONObject;

import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.dto.EmailHandlerBean;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.konylabs.middleware.controller.DataControllerRequest;

public class EmailHandler {
    @SuppressWarnings("unused")
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");

    private static final String INPUT_SUBJECT = "subject";
    private static final String INPUT_BODY = "body";
    private static final String INPUT_FIRSTNAME = "recepientFirstname";
    private static final String INPUT_LASTNAME = "recepientLastname";
    private static final String INPUT_PHONENUMBER = "phonenumber";
    private static final String INPUT_COUNTRY = "phonenumber";

    public static EmailHandlerBean initializeEmailDTO(DataControllerRequest requestInstance) {
        EmailHandlerBean emailDTOInstance = new EmailHandlerBean();
        emailDTOInstance.setSubject(requestInstance.getParameter(INPUT_SUBJECT));
        emailDTOInstance.setBody(requestInstance.getParameter(INPUT_BODY));
        emailDTOInstance.setFirstName(requestInstance.getParameter(INPUT_FIRSTNAME));
        emailDTOInstance.setLastName(requestInstance.getParameter(INPUT_LASTNAME));
        emailDTOInstance.setPhoneNumber(requestInstance.getParameter(INPUT_PHONENUMBER));
        emailDTOInstance.setCountry(requestInstance.getParameter(INPUT_COUNTRY));
        return emailDTOInstance;
    }

    public static void getKMSAuthToken(EmailHandlerBean emailDTOInstance, DataControllerRequest requestInstance) {
        JSONObject response = CommonUtilities.getStringAsJSONObject(Executor
                .invokeService(ServiceURLEnum.AUTHKMSSERVICE_AUTHENTICATE, new HashMap<>(), null, requestInstance));
        if (response != null && response.has(FabricConstants.OPSTATUS) && response.getInt(FabricConstants.OPSTATUS) == 0
                && response.has("KMSAuthToken")) {
            emailDTOInstance.setKMSClaimsToken(String.valueOf(response.getString("KMSAuthToken")));
        }
    }

    public static JSONObject sendEmailToSingleRecipient(EmailHandlerBean emailDTOInstance,
            DataControllerRequest requestInstance) {

//        if (StringUtils.isBlank(emailDTOInstance.getKMSClaimsToken())) {
//            getKMSAuthToken(emailDTOInstance, requestInstance);
//        }
//
//        Map<String, String> emailPayloadMap = new HashMap<>();
//        emailPayloadMap.put("emailId", emailDTOInstance.getRecipientEmailId());
//        emailPayloadMap.put("subject", emailDTOInstance.getSubject());
//        emailPayloadMap.put("content", CommonUtilities.encodeToBase64(emailDTOInstance.getBody()));
//        Map<String, String> headers = new HashMap<>();
//        headers.put("KMSAuthToken", emailDTOInstance.getKMSClaimsToken());
//        String sendKMSEmailResponse = Executor.invokeService(ServiceURLEnum.EMAILSERVICE_SENDEMAIL, emailPayloadMap,
//                headers, requestInstance);
    	String content = buildEmailContent(emailDTOInstance.getBody());
    	
    	Map<String, Object> inputmap = new HashMap<>();
        inputmap.put("inputparams", buildpayload(emailDTOInstance, content));
        
        String responseString = StringUtils.EMPTY;
		try {
			responseString = DBPServiceExecutorBuilder.builder().withOperationId("sendEmail")
			        .withRequestParameters(inputmap).withServiceId("notificationInvokeService").build().getResponse();
		} catch (DBPApplicationException e) {
			
			alert.prepareError("Something went wrong in sendEmal service api:  "+ e).log();
		}
        return CommonUtilities.getStringAsJSONObject(responseString);
    }

//    public static JSONObject enrolKMSUser(EmailHandlerBean emailDTOInstance, DataControllerRequest requestInstance) {
//
//        if (StringUtils.isBlank(emailDTOInstance.getKMSClaimsToken()))
//            getKMSAuthToken(emailDTOInstance, requestInstance);
//
//        if (StringUtils.isBlank(emailDTOInstance.getPhoneNumber()))
//            emailDTOInstance.setPhoneNumber("+91" + CommonUtilities.genPhoneNumber());
//        if (StringUtils.isBlank(emailDTOInstance.getCountry()))
//            emailDTOInstance.setCountry("India");
//
//        Map<String, String> enrolKMSUserJSONObject = new HashMap<>();
//        enrolKMSUserJSONObject.put("firstName", emailDTOInstance.getFirstName());
//        enrolKMSUserJSONObject.put("lastName", emailDTOInstance.getLastName());
//        enrolKMSUserJSONObject.put("email", emailDTOInstance.getRecipientEmailId());
//        enrolKMSUserJSONObject.put("mobileNumber", emailDTOInstance.getPhoneNumber());
//        enrolKMSUserJSONObject.put("country", emailDTOInstance.getCountry());
//        enrolKMSUserJSONObject.put("state", emailDTOInstance.getState());
//        enrolKMSUserJSONObject.put("smsSubscription", "true");
//        enrolKMSUserJSONObject.put("emailSubscription", "true");
//
//        Map<String, String> headers = new HashMap<>();
//        headers.put("KMSAuthToken", emailDTOInstance.getKMSClaimsToken());
//
//        String enrolKMSEmailResponse = Executor.invokeService(ServiceURLEnum.EMAILSERVICE_ENROLLUSER,
//                enrolKMSUserJSONObject, headers, requestInstance);
//        return CommonUtilities.getStringAsJSONObject(enrolKMSEmailResponse);
//    }

    public static JSONObject invokeSendEmailObjectService(DataControllerRequest dataControllerRequest, String authToken,
            String recipientEmailId, String cc, String subject, String emailType, JSONObject additionalContext) {

        Map<String, String> postParametersMap = new HashMap<>();
        postParametersMap.put("emailSubject", subject);
        postParametersMap.put("senderEmailId", "retailbankingdemos@kony.com");
        postParametersMap.put("recipientEmailId", recipientEmailId);
        postParametersMap.put("cc", cc);
        postParametersMap.put("emailType", emailType);
        if (additionalContext != null) {
            postParametersMap.put("AdditionalContext", additionalContext.toString());
            if (additionalContext.has("vizServerURL")) {
                postParametersMap.put("vizServerURL", additionalContext.optString("vizServerURL"));
            }
        }

        String EndpointResponse = Executor.invokeService(ServiceURLEnum.EMAILKMSJAVASERVICE_SENDEMAIL,
                postParametersMap, null, dataControllerRequest);
        return CommonUtilities.getStringAsJSONObject(EndpointResponse);

    }
    
    private static String buildEmailContent(String content) {
  	  
  	  if (StringUtils.isNotBlank(content)) {
  		  
            content = content.replaceAll("\t", "");
            content = content.replaceAll("\r", "");
            content = content.replaceAll("\n", "");
            content = content.replaceAll("\"", "'");
        }
  	  
  	  return content;
    }
    
    private static JsonObject buildpayload(EmailHandlerBean emailHandlerBean, String content) {
        
        JsonObject inputparams = new JsonObject(); 
        
        JsonObject emailservicerequest = new JsonObject();

        String subject =  emailHandlerBean.getSubject();
        JsonObject email = new JsonObject();
        JsonObject emails = new JsonObject(); 
        JsonObject recipients = new JsonObject();
        JsonArray recipient = new JsonArray();
        recipients.add("recipient", recipient);

    	JsonObject recipientto = new JsonObject();
    	recipientto.addProperty("type", "TO"); 
    	recipientto.addProperty("emailId", emailHandlerBean.getRecipientEmailId()); 
    	recipient.add(recipientto);

        email.add("recipients", recipients);
       
        email.addProperty("subject", subject);
        email.addProperty("content", content);
        email.addProperty("priority", "true");
        emails.add("email", email);
        
        emailservicerequest.add("emails", emails);
        
        inputparams.add("emailServiceRequest", emailservicerequest);
        
        return   inputparams;
       
    }
    
    
}
