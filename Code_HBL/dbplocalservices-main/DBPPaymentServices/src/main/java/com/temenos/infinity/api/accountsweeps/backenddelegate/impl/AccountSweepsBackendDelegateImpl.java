package com.temenos.infinity.api.accountsweeps.backenddelegate.impl;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.infinity.dbx.temenos.transactions.TransactionConstants;
import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.TokenUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.api.accountsweeps.backenddelegate.api.AccountSweepsBackendDelegate;
import com.temenos.infinity.api.accountsweeps.constants.Constants;
import com.temenos.infinity.api.accountsweeps.constants.ErrorCodeEnum;
import com.temenos.infinity.api.accountsweeps.dto.AccountSweepsDTO;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com.temenos.infinity.api.accountsweeps.utils.AccountSweepsAPIServices.DB_GETACCOUNTSWEEP;
import static com.temenos.infinity.api.srmstransactions.config.SrmsTransactionsAPIServices.SERVICEREQUESTJAVA_CREATEORDER;

/**
 * @author naveen.yerra
 */
public class AccountSweepsBackendDelegateImpl implements AccountSweepsBackendDelegate, Constants {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    public AccountSweepsDTO getSweepByAccountId(String accountId) {

        String filter = "primaryAccountNumber" +  DBPUtilitiesConstants.EQUAL + accountId +
                DBPUtilitiesConstants.OR + "secondaryAccountNumber" +  DBPUtilitiesConstants.EQUAL + accountId;
        HashMap<String, Object> inputMap = new HashMap<>();
        inputMap.put(DBPUtilitiesConstants.FILTER, filter);
        String response;
        try {
            response = DBPServiceExecutorBuilder.builder()
                    .withServiceId(DB_GETACCOUNTSWEEP.getServiceName())
                    .withOperationId(DB_GETACCOUNTSWEEP.getOperationName())
                    .withRequestParameters(inputMap)
                    .build().getResponse();
        } catch (Exception e) {
            alert.prepareError("Unable to get Account Sweep from Backend" + e).log();
            return ErrorCodeEnum.ERR_3001.setErrorMessageToDto(null);
        }


        AccountSweepsDTO sweepsDTO;
        try {
            JSONArray responseArray = new JSONObject(response).getJSONArray("accountsweeps");
            sweepsDTO = responseArray.length() !=0 ?
                    JSONUtils.parse(responseArray.getJSONObject(0).toString(), AccountSweepsDTO.class) : new AccountSweepsDTO();
        } catch (Exception e) {
            alert.prepareError("Unable to get Account Sweep from Backend" + e).log();
            return ErrorCodeEnum.ERR_3001.setErrorMessageToDto(null);
        }
        return sweepsDTO;
    }

    public AccountSweepsDTO createSweepAtBackEnd (AccountSweepsDTO sweepsDTO, DataControllerRequest request,String accountSweepBackend) {
    	
    	if("t24".equalsIgnoreCase(accountSweepBackend)) {
    		 return createSweepInT24(sweepsDTO, request,"create");
    	}else {
            // Convert JSONObject requestbody to String
            String requestBody = constructRequestPayload(sweepsDTO).replaceAll("\"", "'");

            // convert into input map
            Map<String, Object> inputMap = new HashMap<>();
            inputMap.put(REQUEST_BODY, requestBody);
            inputMap.put(TYPE, "AccountSweeps");
            inputMap.put(SUBTYPE, "SetUpSweep");
            inputMap.put(ACCOUNT_ID, sweepsDTO.getPrimaryAccountNumber());

            return createOrder(inputMap, request);
    	}
    	

    }

    @Override
    public List<AccountSweepsDTO> getAllSweepsFromBackend(Set<String> accounts) {
        StringBuilder filter = new StringBuilder();
        AtomicInteger i = new AtomicInteger(accounts.size());
        accounts.forEach(accountId -> filter.append("primaryAccountNumber" + DBPUtilitiesConstants.EQUAL)
                        .append(accountId).append(i.decrementAndGet() != 0 ? DBPUtilitiesConstants.OR : ""));

        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put(DBPUtilitiesConstants.FILTER, filter.toString());

        String response;
        try {
            response = DBPServiceExecutorBuilder.builder()
                    .withServiceId(DB_GETACCOUNTSWEEP.getServiceName())
                    .withOperationId(DB_GETACCOUNTSWEEP.getOperationName())
                    .withRequestParameters(inputMap)
                    .build().getResponse();
        } catch (Exception e) {
            alert.prepareError("Unable to get Account Sweep from Backend" + e).log();
            return null;
        }

        List<AccountSweepsDTO> sweepsList;
        try {
            JSONArray responseArray = new JSONObject(response).getJSONArray("accountsweeps");
            sweepsList = responseArray.length() !=0 ?
                    JSONUtils.parseAsList(responseArray.toString(), AccountSweepsDTO.class) : new ArrayList<>();
        } catch (IOException e) {
            alert.prepareError("Unable to get Account Sweep from Backend" + e).log();
            return null;
        }
        return sweepsList;
    }
    
    @Override
    public String getAllSweepsFromT24(Set<String> accounts,DataControllerRequest request) {
        StringBuilder accountIds = new StringBuilder();
        AtomicInteger i = new AtomicInteger(accounts.size());
        accounts.forEach(accountId -> accountIds.append(accountId).append(" "));

        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("accountIdsList", accountIds.toString().trim());
       
       String  authToken = TokenUtils.getT24AuthToken(request);

	
		request.addRequestParam_(TemenosConstants.PARAM_AUTHORIZATION, authToken);
		 Map<String, Object> headerMap = new HashMap<>();
		headerMap.put(TemenosConstants.PARAM_AUTHORIZATION, authToken);

        String response;
        try {
            response = DBPServiceExecutorBuilder.builder()
                    .withServiceId("T24SweepServices")
                    .withOperationId("getSweeps")
                    .withRequestParameters(inputMap).withRequestHeaders(headerMap)
                    .withDataControllerRequest(request).build().getResponse();
        } catch (Exception e) {
            alert.prepareError("Unable to get Account Sweep from Backend" + e).log();
            return null;
        }

		/*
		 * List<AccountSweepsDTO> sweepsList; try { JSONArray responseArray = new
		 * JSONObject(response).getJSONArray("accountsweeps"); sweepsList =
		 * responseArray.length() !=0 ? JSONUtils.parseAsList(responseArray.toString(),
		 * AccountSweepsDTO.class) : new ArrayList<>(); } catch (IOException e) {
		 * alert.prepareError("Unable to get Account Sweep from Backend" + e).log();
		 * return null; }
		 */
        return response;
    }

    public AccountSweepsDTO editSweep(AccountSweepsDTO sweepsDTO, DataControllerRequest request,String accountSweepBackend) {
    	
    	
    	if("t24".equalsIgnoreCase(accountSweepBackend)) {
      		 return createSweepInT24(sweepsDTO, request,"edit");
      	}else {
            // Convert JSONObject requestbody to String
            String requestBody = constructRequestPayload(sweepsDTO).replaceAll("\"", "'");

            // convert into input map
            Map<String, Object> inputMap = new HashMap<>();
            inputMap.put(REQUEST_BODY, requestBody);
            inputMap.put(TYPE, "AccountSweeps");
            inputMap.put(SUBTYPE, "ModifySweep");
            inputMap.put(ACCOUNT_ID, sweepsDTO.getPrimaryAccountNumber());

            return createOrder(inputMap, request);
      	}
    	
    	

    }

    @Override
    public AccountSweepsDTO deleteSweepAtBackEnd(AccountSweepsDTO accountSweepsDTO, DataControllerRequest request,String accountSweepBackend) {
    	
    	if("t24".equalsIgnoreCase(accountSweepBackend)) {
     		 return createSweepInT24(accountSweepsDTO, request,"delete");
     	}else {
            // Convert JSONObject requestbody to String
            String requestBody = constructRequestPayload(accountSweepsDTO).replaceAll("\"", "'");

            // convert into input map
            Map<String, Object> inputMap = new HashMap<>();
            inputMap.put(REQUEST_BODY, requestBody);
            inputMap.put(TYPE, "AccountSweeps");
            inputMap.put(SUBTYPE, "StopSweep");
            inputMap.put(ACCOUNT_ID, accountSweepsDTO.getPrimaryAccountNumber());
            return createOrder(inputMap, request);
     	}

    }

    private String constructRequestPayload(AccountSweepsDTO inputDTO) {
        String requestBody = null;
        try {
            requestBody = new ObjectMapper().writeValueAsString(inputDTO).replaceAll("\"", "'");
        } catch (JsonProcessingException e) {
            alert.prepareError("Error occurred while constructing input payload").log();
        }
        return requestBody;
    }

    private AccountSweepsDTO createOrder(Map<String, Object> inputMap, DataControllerRequest request) {
        AccountSweepsDTO responseDto = new AccountSweepsDTO();
        HashMap<String, Object> headerMap = new HashMap<>();
        headerMap.put(X_KONY_AUTHORIZATION, request.getHeader(X_KONY_AUTHORIZATION));
        headerMap.put(X_KONY_REPORTING_PARAMS, request.getHeader(X_KONY_REPORTING_PARAMS));

        // Making a call to order request API
        String accountsSweepResponse;
        JSONObject Response = new JSONObject();
        try {
            accountsSweepResponse = DBPServiceExecutorBuilder.builder()
                    .withServiceId(SERVICEREQUESTJAVA_CREATEORDER.getServiceName())
                    .withOperationId(SERVICEREQUESTJAVA_CREATEORDER.getOperationName())
                    .withRequestParameters(inputMap).withRequestHeaders(headerMap).withDataControllerRequest(request)
                    .build().getResponse();
        } catch (Exception e) {
            alert.prepareError("Unable to create request " + e).log();
            return ErrorCodeEnum.ERR_3007.setErrorMessageToDto(responseDto);
        }
        if (StringUtils.isNotBlank(accountsSweepResponse)) {
            Response = new JSONObject(accountsSweepResponse);
            diagnostic.prepareDebug("Response " + accountsSweepResponse).log();
        }
        if (Response.has(ORDER_ID)){
            responseDto.setServiceRequestId(Response.getString(ORDER_ID));
            responseDto.setMessage(Response.getString("message"));
        }
        if (Response.has(DBP_ERR_MSG) && StringUtils.isNotBlank(Response.getString(DBP_ERR_MSG))) {
            alert.prepareError("Unable to create request at backend" + Response.getString(DBP_ERR_MSG)).log();
            return ErrorCodeEnum.ERR_3007.setErrorMessageToDto(responseDto);
        }
        return responseDto;
    }
    
    private AccountSweepsDTO createSweepInT24(AccountSweepsDTO sweepDTO, DataControllerRequest request,String flow) {
        AccountSweepsDTO responseDto = new AccountSweepsDTO();
        HashMap<String, Object> headerMap = new HashMap<>();
        headerMap.put(X_KONY_AUTHORIZATION, request.getHeader(X_KONY_AUTHORIZATION));
        headerMap.put(X_KONY_REPORTING_PARAMS, request.getHeader(X_KONY_REPORTING_PARAMS));

        
        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("primaryAccountNumber", sweepDTO.getPrimaryAccountNumber());
        
        String isValidate =  request.getParameter("isValidate");
        if(isValidate==null || "".equals(isValidate)) {
        	isValidate = "false";
        }
        
        inputMap.put("isValidate", isValidate);
        inputMap.put("secondaryAccountNumber", sweepDTO.getSecondaryAccountNumber());
		  if(sweepDTO.getBelowSweepAmount()==null) {
			  sweepDTO.setBelowSweepAmount("");;
		  } 
		  if(sweepDTO.getAboveSweepAmount()==null) {
			  sweepDTO.setAboveSweepAmount("");
		  }
        inputMap.put("belowSweepAmount", sweepDTO.getBelowSweepAmount());
        inputMap.put("aboveSweepAmount", sweepDTO.getAboveSweepAmount());
        //end date
        String formattedEndDate ="";
        if(!"".equalsIgnoreCase(sweepDTO.getEndDate()) && !"End Manually".equalsIgnoreCase(sweepDTO.getEndDate())) {
        	SimpleDateFormat endDateFormat = new SimpleDateFormat("yyyyMMdd");
        	SimpleDateFormat infinityFormat = new SimpleDateFormat("dd/MM/yyyy");
        	try {
				 formattedEndDate = endDateFormat.format(infinityFormat.parse(sweepDTO.getEndDate()));
				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				alert.prepareError(e.getMessage()).log();
			}
        }
        inputMap.put("endDate", formattedEndDate);
        
        //frequency
        String formattedFrequency="";
        String dateComponents[]=sweepDTO.getStartDate().split("/");
        String formattedDate = dateComponents[2]+dateComponents[1]+dateComponents[0];
        switch(sweepDTO.getFrequency()) {
        case "Daily":
        			formattedFrequency=formattedDate+"BSNSS";
        			break;
        case "Weekly":
					formattedFrequency=formattedDate+"WEEK1";
					break;
        case "Monthly":
        			formattedFrequency=formattedDate+"M01"+dateComponents[0];
        			break;
        case "Every 6 Months":
					formattedFrequency=formattedDate+"M06"+dateComponents[0];
					break;
        }
        
        
        inputMap.put("frequency", formattedFrequency);
        
        
        JSONObject bundleConfig=null;
		try {
			bundleConfig = TemenosUtils.getBundleConfigurations(TransactionConstants.DBP_BUNDLE,
			        "SWEEP_TYPE_MAPPING", request);
		} catch (Exception e) {
		}
        JSONObject sweepRuleMapping = null;
        String sweepRuleMappingStr = "";
        if (bundleConfig != null) {
            JSONArray configurations = bundleConfig.optJSONArray("configurations");
            if (configurations != null && configurations.length() > 0) {
                JSONObject sweepMappingObject = configurations.optJSONObject(0);
                if (sweepMappingObject.has(TransactionConstants.DBP_CONFIG_TABLE_VALUE)) {
                	sweepRuleMappingStr = sweepMappingObject.getString(TransactionConstants.DBP_CONFIG_TABLE_VALUE);
                	if(sweepRuleMappingStr!=null && !"".equals(sweepRuleMappingStr)) {
                		sweepRuleMapping = new  JSONObject(sweepRuleMappingStr);
                	}
                			
                }
            }
        }
        
        String aboveSweepKey = "SURP";
        String belowSweepKey = "MAIN";
        String bothSweepKey = "TWOWAY";
        if(sweepRuleMapping!=null) {
        	if(sweepRuleMapping.has("Below")) {
        		belowSweepKey = sweepRuleMapping.getString("Below");
        	}
        	if(sweepRuleMapping.has("Above")) {
        		aboveSweepKey = sweepRuleMapping.getString("Above");
        	}
        	if(sweepRuleMapping.has("Both")) {
        		bothSweepKey = sweepRuleMapping.getString("Both");
        	}
        	
        }
        
      //rule
        String rule="";
        String transactRule="";
        if(!"".equals(sweepDTO.getAboveSweepAmount()) && !"".equals(sweepDTO.getBelowSweepAmount())) {//Both
        	transactRule=bothSweepKey;
        	rule="TWOWAY";
        }else if(!"".equals(sweepDTO.getAboveSweepAmount())) {//Above
        	transactRule=aboveSweepKey;
        	rule="SURP";
        }else { //Below
        	transactRule=belowSweepKey;
        	rule="MAIN";
        }
        inputMap.put("rule", rule);
        inputMap.put("transactRule", transactRule);
        // Making a call to order request API
        String accountsSweepResponse;
        JSONObject Response = new JSONObject();
        String operationName = "createSweep";
        String successMessage = "Account Sweep created successfully";
        switch(flow.toLowerCase()) {
        case "create" : 
        	operationName = "createSweep";successMessage = "Account Sweep created successfully";break;
        case "edit" : 
        	operationName = "updateSweep";successMessage = "Account Sweep updated successfully";break;
        case "delete" : 
        	operationName = "updateSweep";successMessage = "Account Sweep cancelled successfully";break;
        }
        String  authToken = TokenUtils.getT24AuthToken(request);

    	
		request.addRequestParam_(TemenosConstants.PARAM_AUTHORIZATION, authToken);
		
		headerMap.put(TemenosConstants.PARAM_AUTHORIZATION, authToken);
        try {
            accountsSweepResponse = DBPServiceExecutorBuilder.builder()
                    .withServiceId("T24SweepServices")
                    .withOperationId(operationName)
                    .withRequestParameters(inputMap).withRequestHeaders(headerMap).withDataControllerRequest(request)
                    .build().getResponse();
        } catch (Exception e) {
            alert.prepareError("Unable to create request " + e).log();
            return ErrorCodeEnum.ERR_3007.setErrorMessageToDto(responseDto);
        }
        if (StringUtils.isNotBlank(accountsSweepResponse)) {
            Response = new JSONObject(accountsSweepResponse);
            diagnostic.prepareDebug("Response " + accountsSweepResponse).log();
        }
		/*
		 * if (Response.has(ORDER_ID)){
		 * responseDto.setServiceRequestId(Response.getString(ORDER_ID));
		 * responseDto.setMessage(Response.getString("message")); }
		 */
        if (Response.has("status") && Response.getString("status").equalsIgnoreCase("failed")) {
        	StringBuffer errorMessage = new StringBuffer();
        	StringBuffer errorCodes = new StringBuffer();
        	if(Response.has("override")) {
        		JSONObject override = Response.getJSONObject("override");
        		if(override.has("overrideDetails")) {
        			JSONArray overrideDetails = override.getJSONArray("overrideDetails");
        			for(int i=0;i<overrideDetails.length();i++) {
        				JSONObject overrideObj = overrideDetails.getJSONObject(i);
	        			if(overrideObj.has("description")) {
	        				
	        				String description = overrideObj.getString("description");
	        				if(description!=null && description.contains("}")) {
	        					String descriptionArray[] = description.split("}");
	        					description = descriptionArray[0];
	        							
	        				}
	        				errorMessage.append(description).append("\n");
	        			}
	        			if(overrideObj.has("code")) {
	        				errorCodes.append(overrideObj.getString("code")).append(",");
	        			}
	        			
        			}
        		}
        	}
        	
        	if(Response.has("error")) {
        		JSONObject error = Response.getJSONObject("error");
        		if(error.has("errorDetails")) {
        			JSONArray errorDetails = error.getJSONArray("errorDetails");
        			for(int i=0;i<errorDetails.length();i++) {
        				JSONObject errorObj = errorDetails.getJSONObject(i);
        				if(errorObj.has("code")) {
        					errorCodes.append(errorObj.getString("code")).append(",");
	        			}
	        			if(errorObj.has("message")) {
	        				String message = errorObj.getString("message");
	        				if(message!=null && message.contains("}")) {
	        					String messageArray[] = message.split("}");
	        					message = messageArray[0];
	        							
	        				}
	        				errorMessage.append(message).append("\n");
	        			}
        			}
        		}
        	}
        	String errorMessageStr = errorMessage.toString();
        	if(!"".equals(errorMessageStr) || !"".equals(errorCodes.toString())) {
        		if(errorMessageStr.endsWith("\n")) {
        			errorMessageStr = errorMessageStr.substring(0, errorMessageStr.length()-1);
        		}
				responseDto.setErrorCode(errorCodes.toString());
				responseDto.setErrorMessage(errorMessageStr);
				responseDto.setDbpErrCode(errorCodes.toString());
				responseDto.setDbpErrMsg(errorMessageStr);
				responseDto.setMessage(errorMessageStr);
				 alert.prepareError("Unable to create request at backend"+errorMessage.toString()).log();
        	}else {
                alert.prepareError("Unable to create request at backend" ).log();
                responseDto = ErrorCodeEnum.ERR_3007.setErrorMessageToDto(responseDto);   
				responseDto.setDbpErrCode(errorCodes.toString());
				responseDto.setDbpErrMsg("Unable to create request at backend");
				responseDto.setMessage("Unable to create request at backend");
        	}
        	
        }else {
            if("true".equals(isValidate)) {
            	responseDto.setMessage("valid");
            }else {
            	
            	
            	responseDto.setMessage(successMessage);
            	responseDto.setServiceRequestId(responseDto.getPrimaryAccountNumber());

            }
        }
        return responseDto;
    }
}
