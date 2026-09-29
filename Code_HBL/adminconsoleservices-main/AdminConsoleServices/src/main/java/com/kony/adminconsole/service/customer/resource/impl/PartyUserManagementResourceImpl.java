package com.kony.adminconsole.service.customer.resource.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.handler.AuditHandler;
import com.kony.adminconsole.service.customer.businessdelegate.api.PartyUserManagementBusinessDelegate;
import com.kony.adminconsole.service.customer.resource.api.PartyUserManagementResource;
import com.kony.adminconsole.utilities.ActivityStatusEnum;
import com.kony.adminconsole.utilities.DBPServices;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.EventEnum;
import com.kony.adminconsole.utilities.ModuleNameEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class PartyUserManagementResourceImpl implements PartyUserManagementResource {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
	
	private static final String INPUT_PARTY_ID = "partyId";
	private static final String INPUT_PARTY_TYPE = "partyType";
	private static final String INPUT_FIRST_NAME = "firstName";
	private static final String INPUT_LAST_NAME = "lastName";
	private static final String INPUT_MIDDLE_NAME = "middleName";
	private static final String INPUT_NICK_NAME = "nickName";
	private static final String INPUT_SUFFIX = "suffix";
	private static final String INPUT_GENDER = "gender";
	private static final String INPUT_DEFAULT_LANGAUGE = "defaultLanguage";
    private static final String INPUT_DEPENDENTS = "noOfDependents";
    private static final String INPUT_ALIAS = "alias";
    private static final String INPUT_PHONE_ADDRESS = "phoneAddress";
    private static final String INPUT_PARTY_STATUS = "partyStatus";
    private static final String INPUT_ELECTRONIC_ADDRESS = "electronicAddress";
    private static final String INPUT_CONTACT_ADDRESS = "contactAddress";
	private static final String LEGAL_ENTITY_ID = "legalEntityId";
    
    private static final String INPUT_IDENTIFICATION_NUMBER = "identifierNumber";
    private static final String INPUT_IDENTIFICATION_TYPE = "identifierType";
    private static final String INPUT_ALTERNATE_IDENTIFICATION_NUMBER = "alternateIdentifierNumber";
    private static final String INPUT_ALTERNATE_IDENTIFICATION_TYPE = "alternateIdentifierType";
    private static final String INPUT_DOB = "dateOfBirth";
    private static final String INPUT_CONTACT_NUMBER = "contactNumber";
    private static final String INPUT_ENTITY_NAME = "entityName";
    private static final String INPUT_EMAIL_ID = "emailId";
    private static final String INPUT_RECORD_COUNT = "recordCount";
    
    
    PartyUserManagementBusinessDelegate partyUserManagementBusinessDelegate = DBPAPIAbstractFactoryImpl
			.getBusinessDelegate(PartyUserManagementBusinessDelegate.class);
	
	@Override
	public Result createPartyUser(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws ApplicationException {
		
		Result result = new Result();
		StringBuilder builder = new StringBuilder();
		try {
			
			String partyType = StringUtils.EMPTY;
			String firstName = StringUtils.EMPTY;
			String lastName = StringUtils.EMPTY;
			String gender = StringUtils.EMPTY;
			String defaultLanguage = StringUtils.EMPTY;
			String noOfDependents = StringUtils.EMPTY;
			String middleName = StringUtils.EMPTY;
			String nickName = StringUtils.EMPTY;
			String suffix = StringUtils.EMPTY;
			String alias = StringUtils.EMPTY;
			String phoneAddress = StringUtils.EMPTY;
			String partyStatus = StringUtils.EMPTY;
			String electronicAddress = StringUtils.EMPTY;
			String contactAddress = StringUtils.EMPTY;
			String legalEntityId = StringUtils.EMPTY;
			
			if(StringUtils.isNotBlank(request.getParameter(INPUT_PARTY_TYPE))) {
				partyType = request.getParameter(INPUT_PARTY_TYPE);
				builder.append("partyType: " +partyType +",");
			}
			if(StringUtils.isNotBlank(request.getParameter(INPUT_FIRST_NAME))) {
				firstName = request.getParameter(INPUT_FIRST_NAME);
				builder.append("firstName: " +firstName +",");
			}
			if(StringUtils.isNotBlank(request.getParameter(INPUT_LAST_NAME))) {
				lastName = request.getParameter(INPUT_LAST_NAME);
				builder.append("lastName: " +lastName +",");
			}
			if(StringUtils.isNotBlank(request.getParameter(INPUT_GENDER))) {
				gender = request.getParameter(INPUT_GENDER);
				builder.append("gender: " +gender +",");
			}
			if(StringUtils.isNotBlank(request.getParameter(INPUT_DEFAULT_LANGAUGE))) {
				defaultLanguage = request.getParameter(INPUT_DEFAULT_LANGAUGE);
				builder.append("defaultLanguage: " +defaultLanguage +",");
			}
			if(StringUtils.isNotBlank(request.getParameter(INPUT_DEPENDENTS))) {
				noOfDependents = request.getParameter(INPUT_DEPENDENTS);
				builder.append("noOfDependents: " +noOfDependents +",");
			}
			if(StringUtils.isNotBlank(request.getParameter(INPUT_MIDDLE_NAME))) {
				middleName = request.getParameter(INPUT_MIDDLE_NAME);
				builder.append("middleName: " +middleName +",");
			}
			if(StringUtils.isNotBlank(request.getParameter(INPUT_NICK_NAME))) {
				nickName = request.getParameter(INPUT_NICK_NAME);
				builder.append("nickName: " +nickName +",");
			}
			if(StringUtils.isNotBlank(request.getParameter(INPUT_SUFFIX))) {
				suffix = request.getParameter(INPUT_SUFFIX);
				builder.append("suffix: " +suffix +",");
			}
			if(StringUtils.isNotBlank(request.getParameter(INPUT_ALIAS))) {
				alias = request.getParameter(INPUT_ALIAS);
				builder.append("alias: " +alias +",");
			}
			if(StringUtils.isNotBlank(request.getParameter(INPUT_PHONE_ADDRESS))) {
				phoneAddress = request.getParameter(INPUT_PHONE_ADDRESS);
			}
			if(StringUtils.isNotBlank(request.getParameter(INPUT_PARTY_STATUS))) {
				partyStatus = request.getParameter(INPUT_PARTY_STATUS);
			}
			if(StringUtils.isNotBlank(request.getParameter(INPUT_ELECTRONIC_ADDRESS))) {
				electronicAddress = request.getParameter(INPUT_ELECTRONIC_ADDRESS);
			}
			if(StringUtils.isNotBlank(request.getParameter(INPUT_CONTACT_ADDRESS))) {
				contactAddress = request.getParameter(INPUT_CONTACT_ADDRESS);
			}
			if(StringUtils.isNotBlank(request.getParameter(LEGAL_ENTITY_ID))) {
				legalEntityId = request.getParameter(LEGAL_ENTITY_ID);
			}
			
			Map<String, Object> postParametersMap = new HashMap<>();
			postParametersMap.put("partyType", partyType);
			postParametersMap.put("firstName", firstName);
			postParametersMap.put("lastName", lastName);
			postParametersMap.put("gender", gender);
			postParametersMap.put("defaultLanguage", defaultLanguage);
			postParametersMap.put("noOfDependents", noOfDependents);
			postParametersMap.put("middleName", middleName);
			postParametersMap.put("nickName", nickName);
			postParametersMap.put("suffix", suffix);
			postParametersMap.put("alias", alias);
			postParametersMap.put("partyStatus", partyStatus);
			postParametersMap.put(LEGAL_ENTITY_ID, legalEntityId);
			postParametersMap.put("phoneAddress", stringifyForVelocityTemplate(phoneAddress));
			postParametersMap.put("electronicAddress", stringifyForVelocityTemplate(electronicAddress));
			postParametersMap.put("contactAddress", stringifyForVelocityTemplate(contactAddress));
			
			String dbpServicesClaimsToken = DBPServices.getDBPServicesClaimsToken(request);
			
			JSONObject serviceResponse = partyUserManagementBusinessDelegate.createPartyUser(
										 postParametersMap, dbpServicesClaimsToken);
			
            if (serviceResponse == null || !serviceResponse.has(FabricConstants.OPSTATUS)
	                || serviceResponse.getInt(FabricConstants.OPSTATUS) != 0) {
	            ErrorCodeEnum.ERR_20559.setErrorCode(result);
	            result.addParam(new Param("status", "Failure", FabricConstants.STRING));
	            AuditHandler.auditAdminActivity(request, ModuleNameEnum.CUSTOMERS, EventEnum.CREATE,
	                    ActivityStatusEnum.FAILED, "Failed to create customer in  party, Customer provided details : "+builder.toString());
	            return result;
	        } else if (serviceResponse.has("dbpErrMsg")) {
	        	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return result;
	        } else {
	        	
	        	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
	        	AuditHandler.auditAdminActivity(request, ModuleNameEnum.CUSTOMERS, EventEnum.CREATE,ActivityStatusEnum.SUCCESSFUL,
                        "Succefully created customer in  party, Customer provided details :" + builder.toString());
	        }
			
		} catch (Exception e) {
			alert.prepareError("Unexpected Error in createPartyUser", e).log();
			result.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
			ErrorCodeEnum.ERR_20630.setErrorCode(result);
			AuditHandler.auditAdminActivity(request, ModuleNameEnum.CUSTOMERS, EventEnum.CREATE,ActivityStatusEnum.FAILED,
					"Failed to create  customer in party : Customer provided details :" + builder.toString());
		}

		return result;
	}

	@Override
	public Result updatePartyUser(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		
		Result result = new Result();
		String partyId = StringUtils.EMPTY;
		
		try {
			
			partyId = request.getParameter(INPUT_PARTY_ID);
			if (StringUtils.isBlank(partyId)) {
				ErrorCodeEnum.ERR_20558.setErrorCode(result);
				return result;
			}
			
			String partyType = StringUtils.EMPTY;
			String firstName = StringUtils.EMPTY;
			String lastName = StringUtils.EMPTY;
			String gender = StringUtils.EMPTY;
			String defaultLanguage = StringUtils.EMPTY;
			String noOfDependents = StringUtils.EMPTY;
			String middleName = StringUtils.EMPTY;
			String nickName = StringUtils.EMPTY;
			String suffix = StringUtils.EMPTY;
			String alias = StringUtils.EMPTY;
			String phoneAddress = StringUtils.EMPTY;
			String partyStatus = StringUtils.EMPTY;
			String electronicAddress = StringUtils.EMPTY;
			String contactAddress = StringUtils.EMPTY;
			String legalEntityId = StringUtils.EMPTY;
			
			if(StringUtils.isNotBlank(request.getParameter(INPUT_PARTY_TYPE))) {
				partyType = request.getParameter(INPUT_PARTY_TYPE);
			}
			if(StringUtils.isNotBlank(request.getParameter(INPUT_FIRST_NAME))) {
				firstName = request.getParameter(INPUT_FIRST_NAME);
			}
			if(StringUtils.isNotBlank(request.getParameter(INPUT_LAST_NAME))) {
				lastName = request.getParameter(INPUT_LAST_NAME);
			}
			if(StringUtils.isNotBlank(request.getParameter(INPUT_GENDER))) {
				gender = request.getParameter(INPUT_GENDER);
			}
			if(StringUtils.isNotBlank(request.getParameter(INPUT_DEFAULT_LANGAUGE))) {
				defaultLanguage = request.getParameter(INPUT_DEFAULT_LANGAUGE);
			}
			if(StringUtils.isNotBlank(request.getParameter(INPUT_DEPENDENTS))) {
				noOfDependents = request.getParameter(INPUT_DEPENDENTS);
			}
			if(StringUtils.isNotBlank(request.getParameter(INPUT_MIDDLE_NAME))) {
				middleName = request.getParameter(INPUT_MIDDLE_NAME);
			}
			if(StringUtils.isNotBlank(request.getParameter(INPUT_NICK_NAME))) {
				nickName = request.getParameter(INPUT_NICK_NAME);
			}
			if(StringUtils.isNotBlank(request.getParameter(INPUT_SUFFIX))) {
				suffix = request.getParameter(INPUT_SUFFIX);
			}
			if(StringUtils.isNotBlank(request.getParameter(INPUT_ALIAS))) {
				alias = request.getParameter(INPUT_ALIAS);
			}
			if(StringUtils.isNotBlank(request.getParameter(INPUT_PHONE_ADDRESS))) {
				phoneAddress = request.getParameter(INPUT_PHONE_ADDRESS);
			}
			if(StringUtils.isNotBlank(request.getParameter(INPUT_PARTY_STATUS))) {
				partyStatus = request.getParameter(INPUT_PARTY_STATUS);
			}
			if(StringUtils.isNotBlank(request.getParameter(INPUT_ELECTRONIC_ADDRESS))) {
				electronicAddress = request.getParameter(INPUT_ELECTRONIC_ADDRESS);
			}
			if(StringUtils.isNotBlank(request.getParameter(INPUT_CONTACT_ADDRESS))) {
				contactAddress = request.getParameter(INPUT_CONTACT_ADDRESS);
			}
			if(StringUtils.isNotBlank(request.getParameter(LEGAL_ENTITY_ID))) {
				legalEntityId = request.getParameter(LEGAL_ENTITY_ID);
			}
			
			Map<String, Object> postParametersMap = new HashMap<>();
			postParametersMap.put("partyId", partyId);
			postParametersMap.put("partyType", partyType);
			postParametersMap.put("firstName", firstName);
			postParametersMap.put("lastName", lastName);
			postParametersMap.put("gender", gender);
			postParametersMap.put("defaultLanguage", defaultLanguage);
			postParametersMap.put("noOfDependents", noOfDependents);
			postParametersMap.put("middleName", middleName);
			postParametersMap.put("nickName", nickName);
			postParametersMap.put("suffix", suffix);
			postParametersMap.put("alias", alias);
			postParametersMap.put("partyStatus", partyStatus);
			postParametersMap.put(LEGAL_ENTITY_ID, legalEntityId);
			postParametersMap.put("phoneAddress", stringifyForVelocityTemplate(phoneAddress));
			postParametersMap.put("electronicAddress", stringifyForVelocityTemplate(electronicAddress));
			postParametersMap.put("contactAddress", stringifyForVelocityTemplate(contactAddress));
			
			String dbpServicesClaimsToken = DBPServices.getDBPServicesClaimsToken(request);
			
			JSONObject serviceResponse = partyUserManagementBusinessDelegate.updatePartyUser(
										 postParametersMap, dbpServicesClaimsToken);
			
            if (serviceResponse == null || !serviceResponse.has(FabricConstants.OPSTATUS)
	                || serviceResponse.getInt(FabricConstants.OPSTATUS) != 0) {
	            ErrorCodeEnum.ERR_20560.setErrorCode(result);
	            result.addParam(new Param("status", "Failure", FabricConstants.STRING));
	            AuditHandler.auditAdminActivity(request, ModuleNameEnum.CUSTOMERS, EventEnum.UPDATE,
	                    ActivityStatusEnum.FAILED, "Failed to update customer in  party, partyId : "+partyId);
	            return result;
	        } else if (serviceResponse.has("dbpErrMsg")) {
	        	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return result;
	        } else {
	        	
	        	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
	        	AuditHandler.auditAdminActivity(request, ModuleNameEnum.CUSTOMERS, EventEnum.UPDATE,ActivityStatusEnum.SUCCESSFUL,
                        "Succefully updated customer in  party, partyId :" + partyId);
	        }
			
		} catch (Exception e) {
			alert.prepareError("Unexpected Error in updatePartyUser", e).log();
			result.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
			ErrorCodeEnum.ERR_20631.setErrorCode(result);
			AuditHandler.auditAdminActivity(request, ModuleNameEnum.CUSTOMERS, EventEnum.UPDATE,ActivityStatusEnum.FAILED,
					"Failed to Update Party Users for partyId : " + partyId);
		}

		return result;
	}
	
	@Override
	public Result searchPartyUser(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		
		Result result = new Result();
		
		StringBuilder builder = new StringBuilder();
		
		try {
			
			String partyId = StringUtils.EMPTY;
			String identifierNumber = StringUtils.EMPTY;
			String identifierType = StringUtils.EMPTY;
			String alternateIdentifierNumber = StringUtils.EMPTY;
			String alternateIdentifierType = StringUtils.EMPTY;
			String lastName = StringUtils.EMPTY;
			String dateOfBirth = StringUtils.EMPTY;
			String contactNumber = StringUtils.EMPTY;
			String entityName = StringUtils.EMPTY;
			String emailId = StringUtils.EMPTY;
			String recordCount = StringUtils.EMPTY;
			String legalEntityId = StringUtils.EMPTY;
			
			if(StringUtils.isNotBlank(request.getParameter(INPUT_PARTY_ID))) {
				partyId = request.getParameter(INPUT_PARTY_ID);
				builder.append("partyId: "+partyId+",");
			}
			if(StringUtils.isNotBlank(request.getParameter(INPUT_IDENTIFICATION_NUMBER))) {
				identifierNumber = request.getParameter(INPUT_IDENTIFICATION_NUMBER);
				builder.append("identifierNumber: "+identifierNumber);
			}
			if(StringUtils.isNotBlank(request.getParameter(INPUT_LAST_NAME))) {
				lastName = request.getParameter(INPUT_LAST_NAME);
				builder.append("lastName: "+lastName +",");
			}
			if(StringUtils.isNotBlank(request.getParameter(INPUT_IDENTIFICATION_TYPE))) {
				identifierType = request.getParameter(INPUT_IDENTIFICATION_TYPE);
				builder.append("identifierType: "+identifierType + ",");
			}
			if(StringUtils.isNotBlank(request.getParameter(INPUT_ALTERNATE_IDENTIFICATION_NUMBER))) {
				alternateIdentifierNumber = request.getParameter(INPUT_ALTERNATE_IDENTIFICATION_NUMBER);
				builder.append("alternateIdentifierNumber "+alternateIdentifierNumber +",");
			}
			if(StringUtils.isNotBlank(request.getParameter(INPUT_ALTERNATE_IDENTIFICATION_TYPE))) {
				alternateIdentifierType = request.getParameter(INPUT_ALTERNATE_IDENTIFICATION_TYPE);
				builder.append("alternateIdentifierType "+alternateIdentifierType +",");
			}
			if(StringUtils.isNotBlank(request.getParameter(INPUT_DOB))) {
				dateOfBirth = request.getParameter(INPUT_DOB);
				builder.append("dateOfBirth "+dateOfBirth +",");
			}
			if(StringUtils.isNotBlank(request.getParameter(INPUT_CONTACT_NUMBER))) {
				contactNumber = request.getParameter(INPUT_CONTACT_NUMBER);
				builder.append("contactNumber "+contactNumber +",");
			}
			if(StringUtils.isNotBlank(request.getParameter(INPUT_ENTITY_NAME))) {
				entityName = request.getParameter(INPUT_ENTITY_NAME);
				builder.append("entityName "+entityName +",");
			}
			if(StringUtils.isNotBlank(request.getParameter(INPUT_EMAIL_ID))) {
				emailId = request.getParameter(INPUT_EMAIL_ID);
				builder.append("emailId "+emailId +",");
			}
			if(StringUtils.isNotBlank(request.getParameter(INPUT_RECORD_COUNT))) {
				recordCount = request.getParameter(INPUT_RECORD_COUNT);
				builder.append("recordCount "+recordCount +",");
			}
			if(StringUtils.isNotBlank(request.getParameter(LEGAL_ENTITY_ID))) {
				legalEntityId = request.getParameter(LEGAL_ENTITY_ID);
			}
			
			
			Map<String, Object> postParametersMap = new HashMap<>();
			postParametersMap.put("partyId", partyId);
			postParametersMap.put("identifierNumber", identifierNumber);
			postParametersMap.put("identifierType", identifierType);
			postParametersMap.put("alternateIdentifierNumber", alternateIdentifierNumber);
			postParametersMap.put("alternateIdentifierType", alternateIdentifierType);
			postParametersMap.put("lastName", lastName);
			postParametersMap.put("dateOfBirth", dateOfBirth);
			postParametersMap.put("contactNumber", contactNumber);
			postParametersMap.put("entityName", entityName);
			postParametersMap.put("emailId", emailId);
			postParametersMap.put("recordCount", recordCount);
			postParametersMap.put(LEGAL_ENTITY_ID, legalEntityId);
			if(builder.toString().isEmpty()) {
				result.addParam(new Param("TotalResultsFound", "0", FabricConstants.INT));
				Dataset recordsDS = new Dataset();
				recordsDS.setId("parties");
				result.addDataset(recordsDS);
				return result;
			}
			
			String dbpServicesClaimsToken = DBPServices.getDBPServicesClaimsToken(request);
			
			JSONObject serviceResponse = partyUserManagementBusinessDelegate.searchPartyUser(
										 postParametersMap, dbpServicesClaimsToken);
			
            if (serviceResponse == null || !serviceResponse.has(FabricConstants.OPSTATUS)
	                || serviceResponse.getInt(FabricConstants.OPSTATUS) != 0) {
	            ErrorCodeEnum.ERR_20629.setErrorCode(result);
	            result.addParam(new Param("status", "Failure", FabricConstants.STRING));
	            AuditHandler.auditAdminActivity(request, ModuleNameEnum.CUSTOMERS, EventEnum.SEARCH,
	                    ActivityStatusEnum.FAILED, "Failed to search customer in  party, search Params : "+builder.toString());
	            return result;
	        } else if (serviceResponse.has("dbpErrMsg")) {
	        	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return result;
	        } else {
	        	
	        	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
	        	AuditHandler.auditAdminActivity(request, ModuleNameEnum.CUSTOMERS, EventEnum.SEARCH,ActivityStatusEnum.SUCCESSFUL,
                        "Succefully fetched customer from  party, search Params : "+builder.toString());
	        }
			
		} catch (Exception e) {
			alert.prepareError("Unexpected Error in searchPartyUser", e).log();
			result.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
			ErrorCodeEnum.ERR_20632.setErrorCode(result);
			AuditHandler.auditAdminActivity(request, ModuleNameEnum.CUSTOMERS, EventEnum.SEARCH,ActivityStatusEnum.FAILED,
					"Failed to search Party Users from party, search Params : "+builder.toString());
		}

		return result;
	}

	private  String stringifyForVelocityTemplate(String str) {
		if (StringUtils.isBlank(str)) {

			return "\"\"";
		}else if(str.contains("\\")) {
			str= str.replace("\\","\\\\" );
		}
		return "\"" + str.replace("\"", "\\\"") + "\"";
	}
	

}
