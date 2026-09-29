package com.kony.dbputilities.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.google.gson.JsonObject;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.mfa.MFAConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

public class SMSInvokeHelper {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	public static final String SCHEMA_NAME = EnvironmentConfigurationsHandler.getValue("DBX_SCHEMA_NAME");

	public String constructSMSBody() {
		String body = "";

		return body;
	}

	public static Result invokeSMSService(DataControllerRequest request) {
		Result result = new Result();
		try {
			String phonenumber = "8124442352";
			String body = "Dear Customer transaction of NPR 1000 for your Policy F13447 has been initiated successfully.";

			HashMap<String, Object> headerParams = new HashMap<String, Object>();
			HashMap<String, Object> inputParams = new HashMap<String, Object>();
			inputParams.put("contact", phonenumber);
			inputParams.put("body", body);
			String accounts = DBPServiceExecutorBuilder.builder().withServiceId("sendHBLSMS")
					.withOperationId("sendHBLSMS").withRequestParameters(inputParams)
					.withRequestHeaders(headerParams).withDataControllerRequest(request).build().getResponse();
			result = JSONToResult.convert(accounts);
			return result;

		} catch (Exception e) {
			alert.prepareError("Error occured while sending SMS.", e).log();
		}
		return result;
	}
	public static Result invokeSMSService(HashMap<String, Object> inputParams, HashMap<String, Object> headerParams) {
		Result result = new Result();
		try {
			String phoneNumber=inputParams.get("phone")!=null?inputParams.get("phone").toString():"";
			if(StringUtils.isNotBlank(phoneNumber)) {
		        String[] array = phoneNumber.split("-");
		        phoneNumber = array.length > 1 && StringUtils.isNotBlank(array[1]) ? array[1] : phoneNumber;
		     }
            String content = "";
            if (inputParams.get(MFAConstants.SMS_TEXT) != null) {
                content = inputParams.get(MFAConstants.SMS_TEXT).toString();
            } else if (inputParams.get("messageType") != null) {
            	Result template = getTemplateFromDB(inputParams, headerParams);
            	String inputContext = inputParams.get("content")!=null? inputParams.get("content").toString():"";
                content = constructSMSContent(HelperMethods.getFieldValue(template, "TemplateText"), inputContext);
            }
            inputParams.put("body", content);
            inputParams.put("contact", phoneNumber);
            
			String response = DBPServiceExecutorBuilder.builder().withServiceId("sendHBLSMS")
					.withOperationId("sendHBLSMS").withRequestParameters(inputParams)
					.withRequestHeaders(headerParams).build().getResponse();
			alert.prepareError("HBL send SMS result:", response).log();
			result = JSONToResult.convert(response);
			return result;

		} catch (Exception e) {
			alert.prepareError("Error occured while sending SMS.", e.getMessage()).log();
		}
		return result;
	}
	 private static Result getTemplateFromDB(HashMap<String, Object> inputParams, HashMap<String, Object> headerParams) throws ApplicationException {
	        String templateType = null;
	        Result result = new Result();
	        templateType = inputParams.get("messageType").toString();
	        String filter = "TemplateName" + DBPUtilitiesConstants.EQUAL + templateType;
	        Map<String, Object> inputParams1 = new HashMap<String, Object>();
	        inputParams1.put(DBPUtilitiesConstants.FILTER, filter);
			String operationName=  SCHEMA_NAME+"_emailtemplates_get";
				try {
					String response = DBPServiceExecutorBuilder.builder().withServiceId("dbpRbLocalServicesdb")
							.withObjectId(null).withOperationId(operationName)
							.withRequestParameters(inputParams1).build().getResponse();
					alert.prepareError("HBL send SMS template result:" +response).log();
					result = JSONToResult.convert(response);
				} catch (DBPApplicationException e) {
					throw new ApplicationException(e.getDBPError());
				}
				return result;
	    }
	 public static String constructSMSContent(String content, String inputContext) {

	        if (StringUtils.isNotBlank(inputContext)) {
	            String[] strings = inputContext.split(";");
	            for (String str : strings) {
	                String key = str.split(":")[0];
	                String value = str.substring(key.length() + 1);
	                if (value.contains("$")) {
	                    List<Character> charList = new ArrayList<>();
	                    int index = 0;
	                    while (index < value.length()) {
	                        if (value.charAt(index) == '$') {
	                            charList.add('\\');
	                            charList.add(value.charAt(index));
	                            index++;
	                            continue;
	                        }
	                        charList.add(value.charAt(index));
	                        index++;
	                    }
	                    StringBuilder sb = new StringBuilder();
	                    for (Character ch : charList) {
	                        sb.append(ch);
	                    }
	                    value = sb.toString();
	                }
	                content = content.replaceAll("%" + key + "%", value);
	            }
	        }

	        return content;
	    }

}