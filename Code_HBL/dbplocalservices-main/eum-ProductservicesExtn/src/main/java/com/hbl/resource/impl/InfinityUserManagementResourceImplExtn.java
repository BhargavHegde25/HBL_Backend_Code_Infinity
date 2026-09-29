package com.hbl.resource.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.Callable;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hbl.backenddeligate.api.CustomerAccountsBusinessDelegateExtn;
import com.hbl.resource.constants.HBLConstants;
import com.hbl.utility.DeviceInfo;
import com.infinity.dbx.dbp.jwt.auth.AuthConstants;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.customersecurityservices.createOrgEmployeeAccounts;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.sessionmanager.SessionScope;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.ErrorCodes;
import com.kony.dbputilities.util.ErrorConstants;
import com.kony.dbputilities.util.FeatureConfiguration;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.IntegrationTemplateURLFinder;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.LegalEntityUtil;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.temenos.dbx.eum.product.contract.backenddelegate.api.ContractBackendDelegate;
import com.temenos.dbx.eum.product.contract.backenddelegate.api.CoreCustomerBackendDelegate;
import com.temenos.dbx.eum.product.contract.backenddelegate.api.ServiceDefinitionBackendDelegate;
import com.temenos.dbx.eum.product.contract.businessdelegate.api.ContractBusinessDelegate;
import com.temenos.dbx.eum.product.contract.resource.api.ContractResource;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.CustomerAccountsBusinessDelegate;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.CustomerPreferenceBusinessDelegate;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.InfinityUserManagementBusinessDelegate;
import com.temenos.dbx.eum.product.usermanagement.resource.api.InfinityUserManagementResource;
import com.temenos.dbx.eum.product.usermanagement.resource.impl.InfinityUserManagementResourceImpl;
import com.temenos.dbx.product.businessdelegate.api.FeatureBusinessDelegate;
import com.temenos.dbx.product.dto.AllAccountsViewDTO;
import com.temenos.dbx.product.dto.BackendIdentifierDTO;
import com.temenos.dbx.product.dto.ContractAccountsDTO;
import com.temenos.dbx.product.dto.CustomerDTO;
import com.temenos.dbx.product.dto.CustomerLegalEntityDTO;
import com.temenos.dbx.product.dto.CustomerPreferenceDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.dto.FeatureActionLimitsDTO;
import com.temenos.dbx.product.dto.MembershipDTO;
import com.temenos.dbx.product.dto.ServiceDefinitionDTO;
import com.temenos.dbx.product.utils.DTOUtils;
import com.temenos.dbx.product.utils.InfinityConstants;
import com.temenos.dbx.product.utils.ThreadExecutor;
import com.kony.eum.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.UserAgentUtil;
import com.kony.dbputilities.util.BundleConfigurationHandler;
import com.kony.dbputilities.util.DBPDatasetConstants;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.UserManagementBusinessDelegate;

public class InfinityUserManagementResourceImplExtn extends InfinityUserManagementResourceImpl{

	LoggerUtil logger = new LoggerUtil(InfinityUserManagementResourceImplExtn.class);
	static String contractStatus="";
	String accountNumber = "";
	@Override
	public Result enrollRetailUserOperation(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws ApplicationException {
		logger.debug("HBL::NEW::InfinityUserManagementResourceImplExtn:enrollRetailUserOperation");
		Result result = new Result();
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		LegalEntityUtil.addCompanyIDToHeaders(dcRequest);
		String lastName = StringUtils.isNotBlank(inputParams.get("lastName")) ? inputParams.get("lastName")
				: dcRequest.getParameter("lastName");
		String taxId = StringUtils.isNotBlank(inputParams.get("taxId")) ? inputParams.get("taxId")
				: dcRequest.getParameter("taxId");
		String dateOfBirth = StringUtils.isNotBlank(inputParams.get("dateOfBirth")) ? inputParams.get("dateOfBirth")
				: dcRequest.getParameter("dateOfBirth");
		accountNumber = StringUtils.isNotBlank(inputParams.get("accountNumber")) ? inputParams.get("accountNumber")
				: dcRequest.getParameter("accountNumber");
		String accountName = StringUtils.isNotBlank(inputParams.get("accountName")) ? inputParams.get("accountName")
				: dcRequest.getParameter("accountName");
		String mobileNumber = StringUtils.isNotBlank(inputParams.get("mobileNumber")) ? inputParams.get("mobileNumber")
				: dcRequest.getParameter("mobileNumber");
		String email = StringUtils.isNotBlank(inputParams.get("email")) ? inputParams.get("email")
				: dcRequest.getParameter("email");
		String legalEntityId = StringUtils.isNotBlank(inputParams.get("legalEntityId")) ? inputParams.get("legalEntityId")
				: dcRequest.getParameter("legalEntityId");
		String isMigrationFlow = inputParams.get("isMigrationFlow")!=null? inputParams.get("isMigrationFlow").toString():"";
		String selfMigrationFlow = inputParams.get("selfMigrationFlow")!=null? inputParams.get("selfMigrationFlow").toString():"";
		String channelAccess = "";
		if(StringUtils.isBlank(legalEntityId)) {
			legalEntityId=EnvironmentConfigurationsHandler.getValue(DBPUtilitiesConstants.BRANCH_ID_REFERENCE);
		}
		logger.debug("HBL::NEW::InfinityUserManagementResourceImplExtn:enrollRetailUserOperation inputParams:"+inputParams.toString());
		if (StringUtils.isBlank(accountNumber) || StringUtils.isBlank(accountName) || StringUtils.isBlank(mobileNumber) || StringUtils.isBlank(email)) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10801);
		}
		dcRequest.addRequestParam_(InfinityConstants.legalEntityId, legalEntityId);

		InfinityUserManagementBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(InfinityUserManagementBusinessDelegate.class);
		InfinityUserManagementResource infinityUserManagementResource = DBPAPIAbstractFactoryImpl
				.getResource(InfinityUserManagementResource.class);
		InfinityUserManagementBusinessDelegateImplExtn businessDelegateExtn = new InfinityUserManagementBusinessDelegateImplExtn();
		Map<String, Object> payload = new HashMap<>();
		payload.put("accountNumber", accountNumber);
		payload.put("accountName", accountName);
		payload.put("mobileNumber", mobileNumber);
		payload.put("phone", mobileNumber);
		payload.put("email", email);
		payload.put("legalEntityId", legalEntityId);
		payload.put("name", lastName);
		payload.put("taxId", taxId);
		payload.put("dateOfBirth", dateOfBirth);
		logger.debug("HBL::NEW::InfinityUserManagementResourceImplExtn:enrollRetailUserOperation payload:"+payload.toString());
		try {
			DBXResult dbxresult = businessDelegateExtn.validateHBLCustomerEnrollmentDetails(payload, dcRequest.getHeaderMap());
			JsonObject coreCustomerJson = new JsonObject();
			if (dbxresult != null) {
				if (dbxresult.getResponse() == null) {
					result.addStringParam("isUserExists", "false");
					return result;
				} else {
					coreCustomerJson = (JsonObject) dbxresult.getResponse();
					result.addStringParam("isUserExists", "true");
				}
			}
			com.temenos.dbx.eum.product.contract.businessdelegate.api.ContractBusinessDelegate contractBusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(
							com.temenos.dbx.eum.product.contract.businessdelegate.api.ContractBusinessDelegate.class);
			String infinityUserId = JSONUtil.getString(coreCustomerJson, "infinityUserId");
			String isConsentProvided = JSONUtil.getString(coreCustomerJson, "isEnrollConsentProvided").equalsIgnoreCase("YES")?"true":"false";
			String isUserEnrolled = JSONUtil.getString(coreCustomerJson, "isUserEnrolled");
			channelAccess = JSONUtil.getString(coreCustomerJson, "channelAccess");
			logger.debug("HBL::NEW::InfinityUserManagementResourceImplExtn:infinityUserId:"+infinityUserId);
			logger.debug("HBL::NEW::InfinityUserManagementResourceImplExtn:isUserEnrolled:"+isUserEnrolled);
			String contractId= JSONUtil.getString(coreCustomerJson, "contractId");
			dcRequest.addRequestParam_("migratedUser", isMigrationFlow);
			
			if ("true".equalsIgnoreCase(isUserEnrolled)) {
				result.addStringParam("isUserEnrolled", "true");
				getChannelAccess(coreCustomerJson, result);
				result.addStringParam("isConsentProvided", isConsentProvided);
				result.addStringParam("contractId", contractId);
				return result;
			}
			if(isMigrationFlow.equalsIgnoreCase("true")){
				if(!isConsentProvided.equalsIgnoreCase("true") ) {
				String errorMsg="Sorry! Can't migrate. Your ebanking consent is not provided. Please contact support to activate your digital banking.";
				result.addStringParam("dbpErrMsg", errorMsg);
				result.addStringParam("dbpErrCode", ErrorCodeEnum.ERR_10290.getErrorCodeAsString());
				return result;
				}
				if(channelAccess.equalsIgnoreCase("NONE")) {
				String errorMsg="Sorry! Can't migrate. Your ebanking access is currently disabled. Please contact support to activate your digital banking.";
				result.addStringParam("dbpErrMsg", errorMsg);
				result.addStringParam("dbpErrCode", ErrorCodeEnum.ERR_10290.getErrorCodeAsString());
				return result;
				}
			}
			/** 
			 * Check the Enrollment status of Admin Approval 
			 * */
			if (!isMigrationFlow.equalsIgnoreCase("true") && "false".equalsIgnoreCase(isUserEnrolled)){
				JsonObject contractInput = new JsonObject(); 
				logger.debug("HBL::InfinityUserManagementResourceImplExtn:contractId:"+contractId);
				contractInput.addProperty("contractId", contractId);
				contractInput.addProperty("legalEntityId", legalEntityId);
				 contractStatus=getContractStatus(contractInput,dcRequest.getHeaderMap());
				logger.debug("HBL::InfinityUserManagementResourceImplExtn:contractStatus:"+contractStatus);
				if(DBPUtilitiesConstants.CONTRACT_STATUS_PENDING.equalsIgnoreCase(contractStatus)) {
				result.addStringParam("isUserEnrolled", DBPUtilitiesConstants.CUSTOMER_STATUS_PENDINGVERIFICATION);
				result.addStringParam("status", "PENDING_VERIFICATION");
				return result;
				}else if(DBPUtilitiesConstants.CONTRACT_STATUS_REJECTED.equalsIgnoreCase(contractStatus)) {
					result.addStringParam("isUserEnrolled", DBPUtilitiesConstants.CONTRACT_STATUS_REJECTED);
					result.addStringParam("status", "REQUEST_REJECTED");
					return result;
				}
				else if(DBPUtilitiesConstants.CUSTOMER_STATUS_PENDINGVERIFICATION.equalsIgnoreCase(contractStatus)) {
					getChannelAccess(coreCustomerJson, result);
					result.addStringParam("isConsentProvided", isConsentProvided);
					result.addStringParam("contractId", contractId);
					return result;
					
				}
			}
			/**
			 * Checking the channel access before Enrolling the customer
			 */
			//String channelAccess = JSONUtil.getString(coreCustomerJson, "channelAccess");
			JSONObject enrolmentObj = verifyChannelAccess(coreCustomerJson, dcRequest);
			String isUserEligibleForEnrolment=enrolmentObj!=null && enrolmentObj.has("isUserEligibleForEnrolment")?enrolmentObj.getString("isUserEligibleForEnrolment"):"";
			if(!isMigrationFlow.equalsIgnoreCase("true") && isConsentProvided.equalsIgnoreCase("true") && (isUserEligibleForEnrolment.equalsIgnoreCase("No") || StringUtils.isEmpty(isUserEligibleForEnrolment))) {
				result.addStringParam("dbpErrMsg", enrolmentObj.getString("dbpErrMsg"));
				result.addStringParam("dbpErrCode", enrolmentObj.getString("dbpErrCode"));
				return result;	
			}
			String isTransactionAllowed = JSONUtil.getString(coreCustomerJson, "isTransactionAllowed");
			logger.debug("hbl::enrollRetailUserOperation:checkEligibilityForEnrolment isConsentProvided:"+isConsentProvided);
			logger.debug("hbl::enrollRetailUserOperation:checkEligibilityForEnrolment isTransactionAllowed:"+isTransactionAllowed);
			
			/**
			 * Enrolling the user
			 */
			if (StringUtils.isBlank(infinityUserId) /*&& isUserEligibleForEnrolment.equalsIgnoreCase("Yes")*/) {
				ContractResource resource = DBPAPIAbstractFactoryImpl.getResource(ContractResource.class);
				coreCustomerJson.addProperty("isConsentProvided", isConsentProvided);
				coreCustomerJson.addProperty("legalEntityId", legalEntityId);
				dcRequest.addRequestParam_("accountNumber", accountNumber);
				logger.debug("HBL::InfinityUserManagementResourceImplExtn:legalEntityId:"+legalEntityId);
				Map<String, Object> contractPayload = createContractPayload(coreCustomerJson, dcRequest,legalEntityId);
				contractPayload.put("legalEntityId", legalEntityId);
				inputArray[1] = contractPayload;
				dcRequest.addRequestParam_("isDefaultActionsEnabled", "true");
				result = resource.createContract(methodID, inputArray, dcRequest, dcResponse);
				contractId=result.getParamValueByName("contractId");
				logger.debug("HBL::InfinityUserManagementResourceImplExtn:Contract table Updated:contractPayload:"+contractPayload);
				logger.debug("HBL::InfinityUserManagementResourceImplExtn:Contract table Updated:coreCustomerJson:"+coreCustomerJson);
				infinityUserId = createAUserAndAssignTOGivenContract(contractPayload,
						JSONUtil.getString(coreCustomerJson, "id"), dcRequest,
						JSONUtil.getString(coreCustomerJson, "partyId"));
				logger.debug("HBL::InfinityUserManagementResourceImplExtn:createAUserAndAssignTOGivenContract completed:infinityUserId:"+infinityUserId);
			}
			
			/**
			 * GenerateInfinityUserActivationCodeAndUsername
			 */
			if(isConsentProvided.equalsIgnoreCase("true") && !isMigrationFlow.equalsIgnoreCase("true")) {
			contractBusinessDelegate.updateContractStatus(contractId,DBPUtilitiesConstants.CONTRACT_STATUS_ACTIVE, legalEntityId, dcRequest.getHeaderMap());
			Map<String, String> activationMap = new HashMap<>();
			activationMap.put(InfinityConstants.userId, infinityUserId);
			activationMap.put("Phone", JSONUtil.getString(coreCustomerJson, "phone"));
			activationMap.put("Email", JSONUtil.getString(coreCustomerJson, "email"));
			inputArray[1] = activationMap;
			result=generateInfinityUserActivationCodeAndUsername(methodID,
					inputArray, dcRequest, dcResponse, coreCustomerJson, legalEntityId);
			}
			else if(isConsentProvided.equalsIgnoreCase("true") && !isMigrationFlow.equalsIgnoreCase("true") && selfMigrationFlow.equalsIgnoreCase("true")) {
				contractBusinessDelegate.updateContractStatus(contractId,DBPUtilitiesConstants.CONTRACT_STATUS_ACTIVE, legalEntityId, dcRequest.getHeaderMap());
				Map<String, String> activationMap = new HashMap<>();
				activationMap.put(InfinityConstants.userId, infinityUserId);
				activationMap.put("Phone", JSONUtil.getString(coreCustomerJson, "phone"));
				activationMap.put("Email", JSONUtil.getString(coreCustomerJson, "email"));
				inputArray[1] = activationMap;
				result=generateInfinityUserActivationCodeAndUsername(methodID,
						inputArray, dcRequest, dcResponse, coreCustomerJson, legalEntityId);
				}
			/*else {
				contractBusinessDelegate.updateContractStatus(result.getParamValueByName("contractId"),
						DBPUtilitiesConstants.CUSTOMER_STATUS_PENDINGVERIFICATION, legalEntityId, dcRequest.getHeaderMap());
			}*/
			logger.debug("hbl::enrollRetailUserOperation: Result:"+ResultToJSON.convert(result));
			getChannelAccess(coreCustomerJson, result);
			result.addStringParam("isConsentProvided", isConsentProvided);
			result.addStringParam("contractId", contractId);
			result.addStringParam("infinityCustomerId", infinityUserId);
			logger.debug("hbl::enrollRetailUserOperation:final Result:"+ResultToJSON.convert(result));
			//else {
			/**
			 * Enrollment Request is sent for Admin Approval
			 */	
			/*contractBusinessDelegate.updateContractStatus(result.getParamValueByName("contractId"),
					DBPUtilitiesConstants.CONTRACT_STATUS_PENDING, legalEntityId, dcRequest.getHeaderMap());
					result.addStringParam("status", "REQUEST_SUBMITTED");
			*/
			//}
		} catch (ApplicationException e) {
			logger.error("InfinityUserManagementResourceImpl : Exception occured while enrolling retail user"
					 ,e);
			throw new ApplicationException(e.getErrorCodeEnum());
		} catch (Exception e) {
			logger.error("InfinityUserManagementResourceImpl : Exception occured while enrolling retail user"
					, e);
			throw new ApplicationException(ErrorCodeEnum.ERR_10812);
		}
		return result;
	}
	public Result getChannelAccess(JsonObject coreCustomerJson, Result result){
		String channelAccess = JSONUtil.getString(coreCustomerJson, "channelAccess");
		if(StringUtils.isNotBlank(channelAccess)) {
			 if(channelAccess.equalsIgnoreCase("MOBILE_BANKING_ONLY")) {
				result.addStringParam("status", "1");
				result.addStringParam("channelAccess", channelAccess);
			}
			else if(channelAccess.equalsIgnoreCase("ONLINE_BANKING_ONLY")) {
				result.addStringParam("status", "2");
				result.addStringParam("channelAccess", channelAccess);
			}
			else if(channelAccess.equalsIgnoreCase("BOTH")) {
				result.addStringParam("status", "3");
				result.addStringParam("channelAccess", channelAccess);
			}
			else {
				result.addStringParam("status", "0");
				result.addStringParam("channelAccess", channelAccess);
			}
		}
		else {
			result.addStringParam("status", "0");
			result.addStringParam("channelAccess", channelAccess);
		}
		logger.debug("hbl::enrollRetailUserOperation:getChannelAccess channelAccess:"+channelAccess);
		return result;
		
	}
	public JSONObject verifyChannelAccess(JsonObject coreCustomerJson, DataControllerRequest dcRequest) {
		JSONObject enorllObject = new JSONObject();
		String errorMsg="";
		String channelAccess = JSONUtil.getString(coreCustomerJson, "channelAccess");
		//String channel=DeviceInfo.GetUserDeviceInfo(dcRequest, null).get("channel_id").toString();
		String channel="";
		try {
		UserAgentUtil ua = new UserAgentUtil(dcRequest);
		channel=ua.getChannel();
		}catch (Exception e) {
			logger.debug("Exception occured in verifyChannelAccess:"+e);
			errorMsg="Failed to get current device details";
			enorllObject.put("isUserEligibleForEnrolment", "No");
			enorllObject.put("dbpErrMsg", errorMsg);
			enorllObject.put("dbpErrCode", "30004");
			return enorllObject;
		}
		logger.debug("hbl::enrollRetailUserOperation:checkEligibilityForEnrolment channelAccess:"+channelAccess);
		logger.debug("hbl::enrollRetailUserOperation:checkEligibilityForEnrolment channel name:"+channel);
		if(channelAccess.equalsIgnoreCase("ONLINE_BANKING_ONLY")) {
			if(channel.equalsIgnoreCase("desktop")) {
				enorllObject.put("isUserEligibleForEnrolment", "Yes");
				enorllObject.put("channelAccess", channelAccess);
			}else {
				errorMsg="Sorry! Can't enrol. Your Mobile banking access is currently disabled. Please contact customer support for assistance or visit your nearest branch to enable your access.";
				enorllObject.put("isUserEligibleForEnrolment", "No");
				enorllObject.put("dbpErrMsg", errorMsg);
				enorllObject.put("dbpErrCode", "30001");
				
			}
		}
		else if(channelAccess.equalsIgnoreCase("MOBILE_BANKING_ONLY")) {
			if(channel.equalsIgnoreCase("mobile")) {
				enorllObject.put("isUserEligibleForEnrolment", "Yes");
				enorllObject.put("channelAccess", channelAccess);
			}else {
				errorMsg="Sorry! Can't enrol. Your online banking access is currently disabled. Please contact customer support for assistance or visit your nearest branch to enable your access.";
				enorllObject.put("isUserEligibleForEnrolment", "No");
				enorllObject.put("dbpErrMsg", errorMsg);
				enorllObject.put("dbpErrCode", "30002");
			}
		}
		else if(channelAccess.equalsIgnoreCase("BOTH")) {
			enorllObject.put("isUserEligibleForEnrolment", "Yes");
			enorllObject.put("channelAccess", channelAccess);
		}else {
			errorMsg="Sorry! Can't enrol. Your ebanking access is currently disabled. Please contact customer support for assistance or visit your nearest branch to enable your access.";
			enorllObject.put("isUserEligibleForEnrolment", "No");
			enorllObject.put("dbpErrMsg", errorMsg);
			enorllObject.put("dbpErrCode", "30003");
		}
		return enorllObject;
	}
	
	public Result enrollRetailUserOperation_old(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws ApplicationException {
		logger.debug("HBL::NEW::InfinityUserManagementResourceImplExtn:enrollRetailUserOperation");
		String isConsentProvided =EnvironmentConfigurationsHandler.getServerProperty("isEnrollConsentProvided");
		Result result = new Result();
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		//String companyLegalUnit = EnvironmentConfigurationsHandler.getValue(DBPUtilitiesConstants.BRANCH_ID_REFERENCE);
		LegalEntityUtil.addCompanyIDToHeaders(dcRequest);
		String lastName = StringUtils.isBlank(inputParams.get("lastName")) ? inputParams.get("lastName")
				: dcRequest.getParameter("lastName");
		String taxId = StringUtils.isBlank(inputParams.get("taxId")) ? inputParams.get("taxId")
				: dcRequest.getParameter("taxId");
		String dateOfBirth = StringUtils.isBlank(inputParams.get("dateOfBirth")) ? inputParams.get("dateOfBirth")
				: dcRequest.getParameter("dateOfBirth");
		String legalEntityId = StringUtils.isBlank(inputParams.get("legalEntityId")) ? inputParams.get("legalEntityId")
				: dcRequest.getParameter("legalEntityId");
		if(legalEntityId==null) {
			legalEntityId=EnvironmentConfigurationsHandler.getValue(DBPUtilitiesConstants.BRANCH_ID_REFERENCE);
		}
				/* This is for self enrollment */
	//	JsonElement legalEntityId = new JsonParser().parse(companyLegalUnit);
		dcRequest.addRequestParam_(InfinityConstants.legalEntityId, legalEntityId);
		
		if (StringUtils.isBlank(lastName) || StringUtils.isBlank(taxId) || StringUtils.isBlank(dateOfBirth)) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10801);
		}

		InfinityUserManagementBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(InfinityUserManagementBusinessDelegate.class);
		InfinityUserManagementResource infinityUserManagementResource = DBPAPIAbstractFactoryImpl
				.getResource(InfinityUserManagementResource.class);

		try {
			DBXResult dbxresult = businessDelegate.validateCustomerEnrollmentDetails(lastName, taxId, dateOfBirth,
					dcRequest.getHeaderMap(),legalEntityId);
			JsonObject coreCustomerJson = new JsonObject();
			if (dbxresult != null) {
				if (dbxresult.getResponse() == null) {
					result.addStringParam("isUserExists", "false");
					return result;
				} else {
					coreCustomerJson = (JsonObject) dbxresult.getResponse();
					result.addStringParam("isUserExists", "true");
				}
			}
			com.temenos.dbx.eum.product.contract.businessdelegate.api.ContractBusinessDelegate contractBusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(
							com.temenos.dbx.eum.product.contract.businessdelegate.api.ContractBusinessDelegate.class);
			String infinityUserId = JSONUtil.getString(coreCustomerJson, "infinityUserId");
			logger.debug("HBL::NEW::InfinityUserManagementResourceImplExtn:infinityUserId:"+infinityUserId);
			String isUserEnrolled = JSONUtil.getString(coreCustomerJson, "isUserEnrolled");
			logger.debug("HBL::NEW::InfinityUserManagementResourceImplExtn:isUserEnrolled:"+isUserEnrolled);
			if ("true".equalsIgnoreCase(isUserEnrolled)) {
				result.addStringParam("isUserEnrolled", "true");
				return result;
			}
			if ("false".equalsIgnoreCase(isUserEnrolled)){
				JsonObject contractInput = new JsonObject(); 
				String contractId= JSONUtil.getString(coreCustomerJson, "contractId");
				logger.debug("HBL::InfinityUserManagementResourceImplExtn:contractId:"+contractId);
				contractInput.addProperty("contractId", contractId);
				contractInput.addProperty("legalEntityId", legalEntityId);
				 contractStatus=getContractStatus(contractInput,dcRequest.getHeaderMap());
				logger.debug("HBL::InfinityUserManagementResourceImplExtn:contractStatus:"+contractStatus);
				if(DBPUtilitiesConstants.CONTRACT_STATUS_PENDING.equalsIgnoreCase(contractStatus)) {
				result.addStringParam("isUserEnrolled", DBPUtilitiesConstants.CUSTOMER_STATUS_PENDINGVERIFICATION);
				result.addStringParam("status", "PENDING_VERIFICATION");
				return result;
				}else if(DBPUtilitiesConstants.CONTRACT_STATUS_REJECTED.equalsIgnoreCase(contractStatus)) {
					result.addStringParam("isUserEnrolled", DBPUtilitiesConstants.CONTRACT_STATUS_REJECTED);
					result.addStringParam("status", "REQUEST_REJECTED");
					/*
					if(isConsentProvided.equals("false")) {
						contractBusinessDelegate.updateContractStatus(JSONUtil.getString(coreCustomerJson, "contractId"),
								DBPUtilitiesConstants.CONTRACT_STATUS_PENDING, legalEntityId, dcRequest.getHeaderMap());
						result= new Result();
						result.addStringParam("isUserExists", "true");
						result.addStringParam("status", "REQUEST_SUBMITTED");
						return result;
					}else {
						contractBusinessDelegate.updateContractStatus(JSONUtil.getString(coreCustomerJson, "contractId"),
								DBPUtilitiesConstants.CONTRACT_STATUS_ACTIVE, legalEntityId, dcRequest.getHeaderMap());
					}
					*/
				}
				//This scenario is not applicable in real time for testing purpose we are handle the request
				else if(DBPUtilitiesConstants.CONTRACT_STATUS_ACTIVE.equalsIgnoreCase(contractStatus)) {
					isConsentProvided="true";
					
				}
			}

			/**
			 * Enrolling the user
			 */
			
			logger.debug("HBL:::enrollRetailUserOperation:isConsentProvided:"+isConsentProvided);
			if (StringUtils.isBlank(infinityUserId)) {
				ContractResource resource = DBPAPIAbstractFactoryImpl.getResource(ContractResource.class);
				coreCustomerJson.addProperty("isConsentProvided", isConsentProvided);
				coreCustomerJson.addProperty("legalEntityId", legalEntityId);
				logger.debug("HBL::InfinityUserManagementResourceImplExtn:legalEntityId:"+legalEntityId);
				Map<String, Object> contractPayload = createContractPayload(coreCustomerJson, dcRequest,legalEntityId);
				contractPayload.put("legalEntityId", legalEntityId);
				inputArray[1] = contractPayload;
				dcRequest.addRequestParam_("isDefaultActionsEnabled", "true");
				result = resource.createContract(methodID, inputArray, dcRequest, dcResponse);
				if(isConsentProvided.equals("true")) {
				contractBusinessDelegate.updateContractStatus(result.getParamValueByName("contractId"),
						DBPUtilitiesConstants.CONTRACT_STATUS_ACTIVE, legalEntityId, dcRequest.getHeaderMap());
				}else {
					contractBusinessDelegate.updateContractStatus(result.getParamValueByName("contractId"),
							DBPUtilitiesConstants.CONTRACT_STATUS_PENDING, legalEntityId, dcRequest.getHeaderMap());
					result.addStringParam("status", "REQUEST_SUBMITTED");
				}
				infinityUserId = createAUserAndAssignTOGivenContract(contractPayload,
						JSONUtil.getString(coreCustomerJson, "id"), dcRequest,
						JSONUtil.getString(coreCustomerJson, "partyId"));
			}
			
			/**
			 * GenerateInfinityUserActivationCodeAndUsername
			 */
			if(isConsentProvided.equals("true")) {
			Map<String, String> activationMap = new HashMap<>();
			activationMap.put(InfinityConstants.userId, infinityUserId);
			activationMap.put("Phone", JSONUtil.getString(coreCustomerJson, "phone"));
			activationMap.put("Email", JSONUtil.getString(coreCustomerJson, "email"));
			activationMap.put("customerName", JSONUtil.getString(coreCustomerJson, "name"));
			inputArray[1] = activationMap;
			// For SCA User Enrollment modified thread call to Synchronized call..
			result=generateInfinityUserActivationCodeAndUsername(methodID,
					inputArray, dcRequest, dcResponse, coreCustomerJson, legalEntityId);
			}

		} catch (ApplicationException e) {
			logger.error("InfinityUserManagementResourceImpl : Exception occured while enrolling retail user"
					+ e.getMessage());
			throw new ApplicationException(e.getErrorCodeEnum());
		} catch (Exception e) {
			logger.error("InfinityUserManagementResourceImpl : Exception occured while enrolling retail user"
					+ e.getMessage());
			throw new ApplicationException(ErrorCodeEnum.ERR_10812);
		}
		return result;
	}
	public Result generateInfinityUserActivationCodeAndUsername(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse, JsonObject coreCustomerJson, String legalEntityId) {
		Result result = new Result();
		try {
			InfinityUserManagementResource infinityUserManagementResource = DBPAPIAbstractFactoryImpl
					.getResource(InfinityUserManagementResource.class);
			
			Result resultforactivationcode = infinityUserManagementResource.generateInfinityUserActivationCodeAndUsername(methodID,
					inputArray, dcRequest, dcResponse);
			result.addStringParam("activationCodeDeliveryStatus",resultforactivationcode.getParamValueByName("activationCodeDeliveryStatus") );
			result.addStringParam("userNameGenerationStatus",resultforactivationcode.getParamValueByName("userNameGenerationStatus"));
			result.addStringParam("userName", resultforactivationcode.getParamValueByName("usernameForSCA"));
			result.addStringParam("activationCode", resultforactivationcode.getParamValueByName("activationcodeForSCA"));
			result.addStringParam("status",resultforactivationcode.getParamValueByName("status"));
			result.addStringParam("phoneNumber", JSONUtil.getString(coreCustomerJson, "phone"));
			result.addStringParam("email", JSONUtil.getString(coreCustomerJson, "email"));

		} catch (Exception e) {
			logger.debug("Exception occured while sending the activation code and username");
		}
		result.addStringParam("isActivationCodeSent", "true");
		result.addStringParam("firstName", JSONUtil.getString(coreCustomerJson, "name"));
		String customerFullName=JSONUtil.getString(coreCustomerJson, "name");
		result.addStringParam("customerName", customerFullName); 
		if(StringUtils.isBlank(customerFullName)) {
			result.addStringParam("firstName", JSONUtil.getString(coreCustomerJson, "firstName"));
			result.addStringParam("lastName", JSONUtil.getString(coreCustomerJson, "lastName"));
		}
		result.addStringParam("isUserExists", "true");
		result.addStringParam("legalEntityId", legalEntityId);
				return result;
		
	}
	private String createAUserAndAssignTOGivenContract(Map<String, Object> contractPayload, String coreCustomerId,
			DataControllerRequest request, String partyId) throws ApplicationException {
		
		logger.debug("HBL::InfinityUserManagementResourceImplExtn:createAUserAndAssignTOGivenContract:coreCustomerId:"+coreCustomerId);
		Set<String> createdValidContractCoreCustomers = request.getAttribute("createdValidCustomers");
		Map<String, Set<ContractAccountsDTO>> createdCoreCustomerAccounts = request
				.getAttribute("createdCustomerAccounts");
		String createdServiceType = request.getAttribute("serviceType");
		String contractId = request.getAttribute("contractId");
		String authorizedSignatory = contractPayload.get("authorizedSignatory").toString();
		String authorizedSignatoryRoles = contractPayload.get("authorizedSignatoryRoles").toString();
		List<CustomerDTO> authorizedSignatoryList = DTOUtils.getDTOList(authorizedSignatory, CustomerDTO.class);
		String companyId = request.getParameter("legalEntityId");

		if (authorizedSignatoryList == null || authorizedSignatoryList.isEmpty()) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10385);
		}
		Map<String, String> userToCoreCustomerRoles = getUserToCoreCustomerRoles(authorizedSignatoryRoles);
		logger.debug("HBL::InfinityUserManagementResourceImplExtn:createAUserAndAssignTOGivenContract:authorizedSignatoryList:"+authorizedSignatoryList.get(0));
		request.addRequestParam_("coreCustomerId", coreCustomerId);
		String userId = createUser(authorizedSignatoryList.get(0), createdServiceType, request); 
		logger.debug("HBL::InfinityUserManagementResourceImplExtn:createAUserAndAssignTOGivenContract:userId:"+userId);
		Callable<Result> callable = new Callable<Result>() {
			public Result call() {
				try {
					logger.debug("HBL::InfinityUserManagementResourceImplExtn:createAUserAndAssignTOGivenContract:in Callable:userId:"+userId);
					createCustomerPreference(userId,companyId,request.getHeaderMap());
					createBackendIdentifierEntry(coreCustomerId, userId, contractId, request, companyId, partyId);
					createUserRoles(userId, contractId, userToCoreCustomerRoles, request);
					assignUserToContractCustomers(userId, contractId, createdValidContractCoreCustomers, request);
					createUserAccounts(userId, contractId, createdCoreCustomerAccounts, companyId, request);
					createUserActionLimits(userId, contractId, companyId, createdValidContractCoreCustomers,
							userToCoreCustomerRoles, request);
					logger.debug("HBL::InfinityUserManagementResourceImplExtn:callable:Completed:");
				} catch (Exception e) {
					logger.debug("HBL::Exception occured while sending the activation code and username:"+e.getMessage());
				}
				return new Result();
			}
		};
		try {
			ThreadExecutor.getExecutor().execute(callable);
		} catch (Exception e) {
			logger.error("HBL::ThreadExecutor : Exception occured while createAUserAndAssignTOGivenContract:"+e.getMessage());
		}
		return userId;

	}
	public void createCustomerPreference(String customerId,String companyId, Map<String, Object> headersMap) {
		logger.debug("HBL::InfinityUserManagementResourceImplExtn:createCustomerPreference:companyId:"+companyId);
		CustomerPreferenceDTO customerPreferenceDTO = new CustomerPreferenceDTO();
		customerPreferenceDTO.setId(HelperMethods.getNewId());
		customerPreferenceDTO.setCustomer_id(customerId);
		customerPreferenceDTO.setIsNew(true);
		customerPreferenceDTO.setCompanyLegalUnit(companyId);

		CustomerPreferenceBusinessDelegate customerPreferenceBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(CustomerPreferenceBusinessDelegate.class);

		customerPreferenceBusinessDelegate.update(customerPreferenceDTO, headersMap);

	}
	public Map<String, Object> createContractPayload(JsonObject coreCustomer, DataControllerRequest dcRequest,String legalEntityId)
			throws ApplicationException, HttpCallException {
		Map<String, Object> contractPayloadMap = new HashMap<>();
		JsonArray accountsArray = new JsonArray();
		JsonArray contractCustomersJsonArray = new JsonArray();
		JsonObject contractCustomer = new JsonObject();
		JsonArray authorizedSignatoryJsonArray = new JsonArray();
		JsonObject authorizedSignatory = new JsonObject();
		JsonArray authorizedSignatoryRolesJsonArray = new JsonArray();
		JsonObject authorizedSignatoryRole = new JsonObject();
		ServiceDefinitionBackendDelegate serviceDefinitionBusinessDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(ServiceDefinitionBackendDelegate.class);
		ServiceDefinitionDTO serviceDefinitionDTO = new ServiceDefinitionDTO();
		//Map<String, String> legalEntityWiseSerDefs = processLegalEntitydefaultServiceDefsData(dcRequest);
		//String servDefId = legalEntityWiseSerDefs.get(legalEntityId);
		String servDefId = "5801fa32-a416-45b6-af01-b22e2de93777";
		serviceDefinitionDTO.setId(servDefId);
		String res = serviceDefinitionBusinessDelegate.fetchDefaultRoleId(serviceDefinitionDTO, dcRequest.getHeaderMap());
		logger.debug("HBL:::createContractPayload:fetchDefaultRoleId:"+res);
		String accountNumber=dcRequest.getParameter("accountNumber");
		String contractName = JSONUtil.getString(coreCustomer, "name");
		if(StringUtils.isNotBlank(contractName) && StringUtils.isNotBlank(accountNumber)) {
			contractName=contractName+accountNumber;
		}
		if (StringUtils.isBlank(contractName))
			contractName = JSONUtil.getString(coreCustomer, "firstName") + " "
					+ JSONUtil.getString(coreCustomer, "lastName");

		contractPayloadMap.put("contractName", contractName);
		contractPayloadMap.put("serviceDefinitionName", "Retail Online Banking");
		contractPayloadMap.put("serviceDefinitionId", servDefId);
		contractPayloadMap.put("isDefaultActionsEnabled", "true");
		JsonArray communicationArray = new JsonArray();
		JsonObject communicationJson = new JsonObject();
		communicationJson.addProperty("email", JSONUtil.getString(coreCustomer, "email"));
		String[] phoneArray = JSONUtil.getString(coreCustomer, "phone").split("-");
		if (phoneArray.length > 1) {
			communicationJson.addProperty("phoneCountryCode", phoneArray[0]);
			communicationJson.addProperty("phoneNumber", phoneArray[1]);
		} else {
			communicationJson.addProperty("phoneNumber", JSONUtil.getString(coreCustomer, "phone"));
		}
		communicationArray.add(communicationJson);
		contractPayloadMap.put("communication", communicationArray.toString());
		JsonArray addressArray = new JsonArray();
		JsonObject addressJson = new JsonObject();
		addressJson.addProperty("country", JSONUtil.getString(coreCustomer, "country"));
		addressJson.addProperty("cityName", JSONUtil.getString(coreCustomer, "cityName"));
		addressJson.addProperty("zipCode", JSONUtil.getString(coreCustomer, "zipCode"));
		addressJson.addProperty("addressLine1", JSONUtil.getString(coreCustomer, "addressLine1"));
		addressJson.addProperty("addressLine2", JSONUtil.getString(coreCustomer, "addressLine2"));
		addressArray.add(addressJson);
		contractPayloadMap.put("address", addressArray.toString());

		CoreCustomerBackendDelegate coreCustomerBackendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(CoreCustomerBackendDelegate.class);
		DBXResult accountsResult = new DBXResult();
		MembershipDTO membershipDTO = new MembershipDTO();
		membershipDTO.setId(JSONUtil.getString(coreCustomer, "id"));
		membershipDTO.setCompanyLegalUnit(JSONUtil.getString(coreCustomer, "legalEntityId"));
		accountsResult = coreCustomerBackendDelegate.getCoreCustomerAccounts(membershipDTO, dcRequest.getHeaderMap());
		if (accountsResult != null && accountsResult.getResponse() != null) {
			@SuppressWarnings("unchecked")
			List<AllAccountsViewDTO> accountsList = (List<AllAccountsViewDTO>) accountsResult.getResponse();
			String accounts = "";
			try {
				accounts = JSONUtils.stringifyCollectionWithTypeInfo(accountsList, AllAccountsViewDTO.class);
			} catch (Exception e) {
				logger.error("Exception", e);
			}
			accountsArray = new JsonParser().parse(accounts).getAsJsonArray();
		}

		contractCustomer.addProperty("isPrimary", "true");
		contractCustomer.addProperty("isBusiness", "false");
		contractCustomer.addProperty("coreCustomerId", JSONUtil.getString(coreCustomer, "id"));
		contractCustomer.addProperty("coreCustomerName", contractName);
		contractCustomer.add("accounts", accountsArray);
		contractCustomer.add("features", getFeaturesList(dcRequest.getHeaderMap()));
		contractCustomersJsonArray.add(contractCustomer);
		contractPayloadMap.put("contractCustomers", contractCustomersJsonArray.toString());
		//authorizedSignatory.addProperty("FirstName", JSONUtil.getString(coreCustomer, "firstName"));
		authorizedSignatory.addProperty("FirstName", JSONUtil.getString(coreCustomer, "name"));
		authorizedSignatory.addProperty("LastName", JSONUtil.getString(coreCustomer, "lastName"));
		authorizedSignatory.addProperty("FullName", JSONUtil.getString(coreCustomer, "name"));
		authorizedSignatory.addProperty("DateOfBirth", JSONUtil.getString(coreCustomer, "dateOfBirth"));
		authorizedSignatory.addProperty("Ssn", JSONUtil.getString(coreCustomer, "legalId"));
		String legalDocumentName=JSONUtil.getString(coreCustomer, "legalDocumentName");
		String IDType_id = StringUtils.isNotBlank(legalDocumentName)? getIdentityType().get(legalDocumentName): null;
		authorizedSignatory.addProperty("IDType_id", IDType_id);
		String IdValue=JSONUtil.getString(coreCustomer, "legalId");
		IdValue=StringUtils.isNotBlank(IdValue)?IdValue.replace("\n", ""):IdValue;
		IdValue=StringUtils.isNotBlank(IdValue)&&IdValue.length() > 45 ? IdValue.substring(0, 45) : IdValue;
		authorizedSignatory.addProperty("IDValue", IdValue);
		//authorizedSignatory.addProperty("IDValue", JSONUtil.getString(coreCustomer, "legalId"));
		authorizedSignatory.addProperty("IDIssueDate", JSONUtil.getString(coreCustomer, "lagalIssueDate"));
		authorizedSignatory.addProperty("IDExpiryDate", JSONUtil.getString(coreCustomer, "legalExpiredDate"));
		/**
		 * Assigning channelAccess and branchCode and countryCode 
		 */
		authorizedSignatory.addProperty("ApplicantChannel", JSONUtil.getString(coreCustomer, "channelAccess"));
		authorizedSignatory.addProperty("BranchCode", JSONUtil.getString(coreCustomer, "branchCode"));
		authorizedSignatory.addProperty("CountryCode", JSONUtil.getString(coreCustomer, "nationalityId"));
		authorizedSignatoryJsonArray.add(authorizedSignatory);
		contractPayloadMap.put("authorizedSignatory", authorizedSignatoryJsonArray.toString());
		authorizedSignatoryRole.addProperty("coreCustomerId", JSONUtil.getString(coreCustomer, "id"));
		
		String isTransactionAllowed = JSONUtil.getString(coreCustomer, "isTransactionAllowed");
		/**
		 * Assigning role based on isTransactionAllowed flag
		 */
		if(isTransactionAllowed.equalsIgnoreCase("YES")) {
		//authorizedSignatoryRole.addProperty("authorizedSignatoryRoleId",
		//		serviceDefinitionBusinessDelegate.fetchDefaultRoleId(serviceDefinitionDTO, dcRequest.getHeaderMap()));
			authorizedSignatoryRole.addProperty("authorizedSignatoryRoleId","DEFAULT_GROUP");
		}else {
			// Assigning VIEW_ONLY role
			authorizedSignatoryRole.addProperty("authorizedSignatoryRoleId","HBL_VIEW_ONLY");
		}

		authorizedSignatoryRolesJsonArray.add(authorizedSignatoryRole);
		contractPayloadMap.put("authorizedSignatoryRoles", authorizedSignatoryRolesJsonArray.toString());
		return contractPayloadMap;
	}
	private JsonElement getFeaturesList(Map<String, Object> headersMap) throws HttpCallException, ApplicationException {
		FeatureBusinessDelegate featureBusinessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(FeatureBusinessDelegate.class);
		DBXResult featureResponse = featureBusinessDelegate.getFeatures(headersMap);
		JsonArray features = new JsonArray();
		for (JsonElement jsonelement : ((JsonObject) featureResponse.getResponse()).get("feature").getAsJsonArray()) {
			JsonObject json = new JsonObject();
			json.addProperty("featureId", JSONUtil.getString(jsonelement.getAsJsonObject(), "id"));
			features.add(json);
		}
		logger.debug("HBL::InfinityUserManagementResourceImplExtn: getFeaturesListe:"+features.toString());
		return features;
	}
	public String getContractStatus(JsonObject backendIdentifierDTO,Map<String, Object> headersMap) throws Exception{
		String contractId=JSONUtil.getString(backendIdentifierDTO, "contractId");
		if(contractId!= null && StringUtils.isNotBlank(contractId)) {
		String legalEntityId=JSONUtil.getString(backendIdentifierDTO, "legalEntityId");
		String filter = InfinityConstants.id + DBPUtilitiesConstants.EQUAL + contractId + DBPUtilitiesConstants.AND
				+ InfinityConstants.LegalEntityId + DBPUtilitiesConstants.EQUAL + legalEntityId;
		Map<String, Object> input = new HashMap<String, Object>();
		input.put(DBPUtilitiesConstants.FILTER, filter);
		 JsonObject jsonObject = ServiceCallHelper.invokeServiceAndGetJson(input, headersMap,
				URLConstants.CONTRACT_GET);
		if (jsonObject.has(DBPDatasetConstants.DATASET_CONTRACT)) {
            JsonElement jsonElement = jsonObject.get(DBPDatasetConstants.DATASET_CONTRACT);
            logger.debug("HBL::InfinityUserManagementResourceImplExtn:getContractStatus:"+jsonElement.toString());
            if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
                JsonArray jsonArray = jsonElement.getAsJsonArray();
                jsonObject = jsonArray.get(0).getAsJsonObject();
                return jsonObject.get(InfinityConstants.statusId).getAsString();
            }
        }
		}
		
		return "";
	}
	
	@Override
	public Result generateInfinityUserActivationCodeAndUsername(String methodID, Object[] inputArray,
			DataControllerRequest dcRequest, DataControllerResponse dcResponse) throws ApplicationException {
		logger.debug("HBL::InfinityUserManagementResourceImplExtn:generateInfinityUserActivationCodeAndUsername");
		Result result = new Result();
		LegalEntityUtil.addCompanyIDToHeaders(dcRequest);
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		String contractStatus = StringUtils.isNotBlank(inputParams.get(InfinityConstants.contractStatus))
				? inputParams.get(InfinityConstants.contractStatus)
				: dcRequest.getParameter(InfinityConstants.contractStatus);
		String isUserActive=dcRequest.getParameter("isUserActive")!=null ? dcRequest.getParameter("isUserActive"):"";
		if(isUserActive.equalsIgnoreCase("SID_CUS_ACTIVE")) {
			result.addStringParam("emailDelivered", "No");
			result.addStringParam("smsDelivered", "No");
			result.addStringParam("status", String.valueOf(true));
			return result;
		}
		if (StringUtils.isNotBlank(contractStatus)
				&& !DBPUtilitiesConstants.CONTRACT_STATUS_ACTIVE.equalsIgnoreCase(contractStatus)) {
			InfinityUserManagementBusinessDelegateImplExtn infinityUserManagementBusinessDelegateExtn = new InfinityUserManagementBusinessDelegateImplExtn();
			logger.debug("HBL::InfinityUserManagementResourceImplExtn:generateInfinityUserActivationCodeAndUsername:inputParams:"+inputParams.toString());
			DBXResult result1 = infinityUserManagementBusinessDelegateExtn.SendEnrollmentRejectedEmail(inputParams, dcRequest.getHeaderMap());
			JsonObject responseObject = (JsonObject) result1.getResponse();
			boolean status = JSONUtil.hasKey(responseObject, InfinityConstants.status)
					? responseObject.get(InfinityConstants.status).getAsBoolean()
					: false;
			String emailDelivered = JSONUtil.hasKey(responseObject, "emailDelivered")
					? responseObject.get("emailDelivered").getAsString()
					: "false";
			String smsDelivered = JSONUtil.hasKey(responseObject, "smsDelivered")
					? responseObject.get("smsDelivered").getAsString()
					: "false";
			
			result.addStringParam("emailDelivered", emailDelivered);
			result.addStringParam("smsDelivered", smsDelivered);
			result.addStringParam("status", String.valueOf(status));
			return result;
		}
			
		Iterator<String> iterator = dcRequest.getParameterNames();

		while (iterator.hasNext()) {
			String key = iterator.next();
			if ((!inputParams.containsKey(key) || StringUtils.isBlank(inputParams.get(key)))
					&& StringUtils.isNotBlank(dcRequest.getParameter(key))) {
				inputParams.put(key, dcRequest.getParameter(key));
			}
		}

		String userId = StringUtils.isNotBlank(inputParams.get(InfinityConstants.userId))
				? inputParams.get(InfinityConstants.userId)
				: dcRequest.getParameter(InfinityConstants.userId);
		if (StringUtils.isBlank(userId)) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10792);
		}
		try {
			Map<String, String> bundleConfigurations = BundleConfigurationHandler
					.fetchBundleConfigurations(BundleConfigurationHandler.BUDLENAME_C360, dcRequest);
			InfinityUserManagementBusinessDelegate infinityUserManagementBusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(InfinityUserManagementBusinessDelegate.class);

			DBXResult generatedResult = infinityUserManagementBusinessDelegate
					.generateInfinityUserActivationCodeAndUsername(bundleConfigurations, inputParams,
							dcRequest.getHeaderMap());
			JsonObject responseObject = (JsonObject) generatedResult.getResponse();
			boolean status = JSONUtil.hasKey(responseObject, InfinityConstants.status)
					? responseObject.get(InfinityConstants.status).getAsBoolean()
					: false;
			String activationCode = JSONUtil.hasKey(responseObject, InfinityConstants.activationCode)
					? JSONUtil.getString(responseObject, InfinityConstants.activationCode)
					: "";
			String userIdForSCA = JSONUtil.hasKey(responseObject, InfinityConstants.userId)
					? JSONUtil.getString(responseObject, InfinityConstants.userId)
					: "";
			result.addStringParam("activationCodeDeliveryStatus", String.valueOf(status));
			result.addStringParam("userNameGenerationStatus", String.valueOf(status));
			result.addStringParam("usernameForSCA", userIdForSCA); 
			result.addStringParam("activationcodeForSCA", activationCode);
			result.addStringParam("status", String.valueOf(status));
			/**
			 * The below parameters have been added in dcRequest, that is being used while
			 * pushing the sca event
			 */

//			dcRequest.addRequestParam_("userId", userIdForSCA);
//			dcRequest.addRequestParam_("activationCode", activationCode);
//			inputParams.put("userId", userIdForSCA);
//			inputParams.put("activationCode", activationCode);
//
//			Map<String, String> map = new HashMap<>();
//			Object[] inputArray1 = new Object[3];
//			map.put("userId", userIdForSCA);
//			map.put("activationCode", activationCode);
//			inputArray1[1] = map;
//
//			PushExternalEventResource resource = DBPAPIAbstractFactoryImpl.getResource(PushExternalEventResource.class);
//			resource.pushUserIdAndActivationCode(methodID, inputArray1, dcRequest, dcResponse);
		} catch (ApplicationException e) {
			logger.error(
					"InfinityUserManagementResourceImpl : Exception occured while generating username and activation code "
							,e);
			throw new ApplicationException(e.getErrorCodeEnum());
		} catch (Exception e) {
			logger.error(
					"InfinityUserManagementResourceImpl : Exception occured while generating username and activation code "
							,e);
			throw new ApplicationException(ErrorCodeEnum.ERR_10795);
		}
		return result;
	}
	public void createUserAccounts(String userId, String contractId,
			Map<String, Set<ContractAccountsDTO>> createdCoreCustomerAccounts, String legalEntityId, DataControllerRequest request)
			throws ApplicationException {
		logger.debug("HBL::InfinityUserManagementResourceImplExtn:createUserAccounts:createdCoreCustomerAccounts:"+createdCoreCustomerAccounts.toString());
		CustomerAccountsBusinessDelegateExtn customerAccountsBD = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(CustomerAccountsBusinessDelegateExtn.class);
		 for (Entry<String, Set<ContractAccountsDTO>> entry : createdCoreCustomerAccounts.entrySet()) {
			String coreCustomerId = entry.getKey();
			Set<String> accounts = new HashSet<>();
			Set<ContractAccountsDTO> coreCustomerAccounts = entry.getValue();
			for (ContractAccountsDTO dto : coreCustomerAccounts) {
				accounts.add(dto.getAccountId());
			}
			logger.debug("HBL::InfinityUserManagementResourceImplExtn:createUserAccounts:defaultAccount:"+accountNumber);
			customerAccountsBD.createCustomerAccounts(userId, contractId, coreCustomerId, legalEntityId, accounts,
					request.getHeaderMap(),accountNumber);
		}
	}
	public void createUserAccounts_old(String userId, String contractId,
			Map<String, Set<ContractAccountsDTO>> createdCoreCustomerAccounts, String legalEntityId, DataControllerRequest request)
			throws ApplicationException {
		CustomerAccountsBusinessDelegate customerAccountsBD = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(CustomerAccountsBusinessDelegate.class);
		for (Entry<String, Set<ContractAccountsDTO>> entry : createdCoreCustomerAccounts.entrySet()) {
			String coreCustomerId = entry.getKey();
			Set<String> accounts = new HashSet<>();
			Set<ContractAccountsDTO> coreCustomerAccounts = entry.getValue();
			for (ContractAccountsDTO dto : coreCustomerAccounts) {
				accounts.add(dto.getAccountId());
			}
			customerAccountsBD.createCustomerAccounts(userId, contractId, coreCustomerId, legalEntityId, accounts,
					request.getHeaderMap());
		}
		try {
			boolean isUpdated = updatefavoriteAccounts(userId, request);
			logger.debug("hbl::updatefavoriteAccounts:isupdated:"+isUpdated);
		} catch (HttpCallException e) {
			// TODO Auto-generated catch block
			logger.debug("hbl::error occured in updatefavoriteAccounts::"+e.toString());
		}
	}
	private boolean updatefavoriteAccounts(String userId, DataControllerRequest dcRequest)
            throws HttpCallException {
		 Result result = new Result();
		 String accountId=dcRequest.getParameter("accountNumber"); 
		 boolean updated=false;
		 Map<String, String> inputParams = new HashMap<String, String>();
			        inputParams.put("Account_id", accountId);
			        inputParams.put("FavouriteStatus", "1");
			        inputParams.put("Customer_id", userId);
        if(getIdFromCustomerAccounts(inputParams, dcRequest, result)) {
        result=HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                URLConstants.CUSTOMERACCOUNTS_UPDATE);
        if(result.getParamValueByName("dbpErrCode")==null || result.getParamValueByName("dbpErrMsg")==null) {
         updated= true;
		}
        }else {
           // HelperMethods.setValidationMsgwithCode("Unable to update the favorite staus of account",ErrorCodes.ERROR_UPDATING_RECORD, result);
            updated= false;
        }

		return updated;
    }
	private boolean getIdFromCustomerAccounts( Map inputParams,
            DataControllerRequest dcRequest, Result result) throws HttpCallException {
		logger.debug("hbl::getIdFromCustomerAccounts:jsonArray:"+inputParams.toString());
        String userId = (String) inputParams.get("Customer_id");
        String accountIdToUpdate = (String) inputParams.get("Account_id");
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(accountIdToUpdate)) {
        	logger.error("hbl::invalid input params for getIdFromCustomerAccounts:inputParams:"+inputParams.toString());
            return false;
        }
        String filter = DBPUtilitiesConstants.CUSTOMER_ID + DBPUtilitiesConstants.EQUAL + userId +DBPUtilitiesConstants.AND + DBPUtilitiesConstants.ACCOUNT_ID + DBPUtilitiesConstants.EQUAL +accountIdToUpdate;
        createOrgEmployeeAccounts accountsHelper = new createOrgEmployeeAccounts();
        Result existingAccounts = accountsHelper.getExistingAccounts(filter, userId, dcRequest);
        if (!HelperMethods.hasRecords(existingAccounts)) {
            HelperMethods.setValidationMsgwithCode(ErrorConstants.INVALID_ACCOUNT_ACCESS, ErrorCodes.SECURITY_ERROR,
                    result);
            logger.error("hbl::invalid existingAccounts result for getIdFromCustomerAccounts:inputParams:"+existingAccounts.toString());
            return false;
           // HelperMethods.setValidationMsgwithCode("Unable to update the favorite staus of account",ErrorCodes.ERROR_UPDATING_RECORD, result);
           // updated= false;
        }
        List<Record> accounts = existingAccounts.getAllDatasets().get(0).getAllRecords();
        for (Record accountRecord : accounts) {
            String id = HelperMethods.getFieldValue(accountRecord, DBPUtilitiesConstants.UN_ID);
            String account_id_retrieved = HelperMethods.getFieldValue(accountRecord, DBPUtilitiesConstants.ACCOUNT_ID);
            logger.debug("hbl::getIdFromCustomerAccounts:account_id_retrieved:"+account_id_retrieved);
            if (accountIdToUpdate.equals(account_id_retrieved)) {
                inputParams.put("id", id);
                return true;
            } else {
                HelperMethods.setValidationMsgwithCode("Unable to update the favorite staus of account",
                        ErrorCodes.ERROR_UPDATING_RECORD, result);
                logger.error("hbl::invalid details for getIdFromCustomerAccounts:account_id_retrieved:"+account_id_retrieved);
                return false;
            }
        }
        return false;
		//return updated;
    }
	@Override
	public Object editInfinityUser(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws JsonMappingException, JsonProcessingException, JSONException, ApplicationException {
		Result result = new Result();

		JsonObject jsonObject = new JsonObject();

		Map<String, String> map = HelperMethods.getInputParamMap(inputArray);

		String userDetails = map.get(InfinityConstants.userDetails);
		JsonElement userDetailsElement = new JsonParser().parse(userDetails);
		JsonObject userDetailsJsonObject1 = userDetailsElement.getAsJsonObject();
		String legalEntityId1 = userDetailsJsonObject1.has(InfinityConstants.legalEntityId)
				? userDetailsJsonObject1.get(InfinityConstants.legalEntityId).getAsString()
				: null;
		request.addRequestParam_(InfinityConstants.legalEntityId, legalEntityId1);
		LegalEntityUtil.addCompanyIDToHeaders(request);
		Iterator<String> iterator = request.getParameterNames();
		while (iterator.hasNext()) {
			String key = iterator.next();
			if ((!map.containsKey(key) || StringUtils.isBlank(map.get(key)))
					&& StringUtils.isNotBlank(request.getParameter(key))) {
				map.put(key, request.getParameter(key));
			}
		}
		
		if (!map.containsKey(InfinityConstants.userDetails)
				|| StringUtils.isBlank(map.get(InfinityConstants.userDetails))) {
			ErrorCodeEnum.ERR_10056.setErrorCode(result);
			return result;
		}

		if (userDetailsElement.isJsonNull() || !userDetailsElement.isJsonObject()) {
			ErrorCodeEnum.ERR_10056.setErrorCode(result);
			return result;
		}

		Boolean isContractValidationRequired = true;
		Boolean isSuperAdmin = false;

		Map<String, String> loggedInUserInfo = HelperMethods.getCustomerFromAPIDBPIdentityService(request);
		if (HelperMethods.isAuthenticationCheckRequiredForService(loggedInUserInfo)) {
			Set<String> userPermissions = SessionScope.getAllPermissionsFromIdentityScope(request);
			if (!userPermissions.contains("USER_MANAGEMENT")) {
				ErrorCodeEnum.ERR_10051.setErrorCode(result);
				return result;
			}
		} else {
			isContractValidationRequired = false;
			isSuperAdmin = true;
		}

		JsonObject userDetailsJsonObject = userDetailsElement.getAsJsonObject();

		String signatoryGroups = map.get(InfinityConstants.signatoryGroups);
		jsonObject.addProperty(InfinityConstants.signatoryGroups, signatoryGroups);

		String id = userDetailsJsonObject.has(InfinityConstants.id)
				&& !userDetailsJsonObject.get(InfinityConstants.id).isJsonNull()
						? userDetailsJsonObject.get(InfinityConstants.id).getAsString()
						: null;
		String legalEntityId = JSONUtil.hasKey(userDetailsJsonObject, "legalEntityId")
				? JSONUtil.getString(userDetailsJsonObject, "legalEntityId")
				: null;
		String coreCustomerId = userDetailsJsonObject.has(InfinityConstants.coreCustomerId)
				&& !userDetailsJsonObject.get(InfinityConstants.coreCustomerId).isJsonNull()
						? userDetailsJsonObject.get(InfinityConstants.coreCustomerId).getAsString()
						: null;
		if (StringUtils.isBlank(id)) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10050,"Id is empty");
		}
		boolean isExistingLegalEntity = checkIsExistingLegalEntity(id, legalEntityId);
		if (isExistingLegalEntity && StringUtils.isNotBlank(coreCustomerId)) {
			throw new ApplicationException(ErrorCodeEnum.ERR_12466);
		}
		
		if (!checkIfContractCreationRequired(map, result, methodId, inputArray, request, response)) {
			return result;
		}
		Set<String> allLegalEntities = LegalEntityUtil.getAllCompanyLegalUnits();
		userDetailsJsonObject = LegalEntityUtil.addLegalEntityToPermissionsIfReq(userDetailsJsonObject, isSuperAdmin,
				allLegalEntities, request);

		jsonObject.add(InfinityConstants.userDetails, userDetailsJsonObject);

		logger.debug("Json request " + jsonObject.toString());
		logger.debug("HBL::InfinityUserManagementResourceImplExtn:editInfinityUser");

		if (validateinput(jsonObject, result, map, request, isContractValidationRequired, id, allLegalEntities)) {
			
			InfinityUserManagementBusinessDelegate infinityUserManagementBusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(InfinityUserManagementBusinessDelegate.class);
			DBXResult dbxResult = infinityUserManagementBusinessDelegate.editInfinityUser(jsonObject,
					request.getHeaderMap());
			if (dbxResult.getResponse() != null) {
				JsonObject jsonResultObject = (JsonObject) dbxResult.getResponse();
				result = JSONToResult.convert(jsonResultObject.toString());
				try {
					signatoryGroups = map.get(InfinityConstants.signatoryGroups);
					jsonObject.addProperty(InfinityConstants.signatoryGroups, signatoryGroups);
				} catch (Exception e) {
					//
				}
				updateSignatoryGroupEntry(id, jsonObject, result, request);
				logger.debug("Json response " + ResultToJSON.convert(result).toString());
				
                Map<String, Object> inputMap = HelperMethods.getInputParamObjectMap(inputArray);
                
                JSONObject root = new JSONObject(jsonObject.toString()); 
                
                String group = root.getString("signatoryGroups").replace('\'', '"');
                JSONArray arr = new JSONArray(group);
                String cif = arr.getJSONObject(0).getString("cif");
                logger.debug("CIF Value is #"+cif);

                inputMap.put("infinityAccess",request.getParameter("infinityAccess"));
                inputMap.put("customerId",cif);
                //call here transact call to update infinityaccess flag
                updateInfinityAccess(inputMap, request);
			}
		}

		return result;
	}
	
    public JsonObject updateInfinityAccess(Map<String, Object> inputParams, DataControllerRequest dcRequest) throws ApplicationException{
        boolean isSuccess=false;
        JsonObject resultObj = new JsonObject();
        try { 
        addT24Headers(dcRequest.getHeaderMap(), (String) inputParams.get("customerId"), (String) inputParams.get("legalEntityId"));
        resultObj = ServiceCallHelper.invokeServiceAndGetJson("HBL_T24ISUser", null, "updateEnrollConsent",inputParams, dcRequest.getHeaderMap());
        logger.debug("updateEnrollConent Response:"+resultObj);
        String errMsg= resultObj.get("errmsg")!=null?resultObj.get("errmsg").getAsString():"";
        if (!HelperMethods.hasError(resultObj))
        isSuccess=true; 
        else if(StringUtils.isNotBlank(errMsg)&& errMsg.equalsIgnoreCase("Record Not Changed")) {
            isSuccess=true; 
        }
        else
        isSuccess=false; 
        }catch (Exception e) {
            isSuccess=false; 
            logger.debug("Exception Occured while updating EnrollConent status:");
             resultObj.addProperty("errmsg", e.getMessage());
        }
         resultObj.addProperty("isSuccess", isSuccess);
        return resultObj;
    }

     public void addT24Headers(Map<String, Object> headersMap, String id, String legalEntityId) {
            HelperMethods.addJWTAuthHeader(headersMap, AuthConstants.PRE_LOGIN_FLOW);
            
            if(StringUtils.isNotBlank(legalEntityId)) {
                headersMap.put("companyId", legalEntityId);
            } else {
            BackendIdentifierDTO backendIdentifierDTO = new BackendIdentifierDTO();
            backendIdentifierDTO.setBackendId(id);
            backendIdentifierDTO.setBackendType(IntegrationTemplateURLFinder.getBackendURL(InfinityConstants.BackendType));
            backendIdentifierDTO = (BackendIdentifierDTO) backendIdentifierDTO.loadDTO();
                if (backendIdentifierDTO != null && StringUtils.isNotBlank(backendIdentifierDTO.getCompanyLegalUnit())) {
                    headersMap.put("companyId", backendIdentifierDTO.getCompanyLegalUnit());
                    
                } else {
                    /*This is for fall back..*/
                    headersMap.put("companyId",
                            EnvironmentConfigurationsHandler.getValue(DBPUtilitiesConstants.BRANCH_ID_REFERENCE));
                }
            }
        }

	public boolean validateinput(JsonObject jsonObject, Result result, Map<String, String> map,
			DataControllerRequest dcRequest, Boolean isContractValidationRequired, String id, Set<String> allLegalEntities) throws ApplicationException, JsonMappingException, JsonProcessingException {
		logger.debug("HBL::InfinityUserManagementResourceImplExtn:validateinput: map:"+map.toString());

		Map<String, Set<String>> customerAccountsMap = new HashMap<String, Set<String>>();

		String customerId = null;
		Boolean isSuperAdmin = true;

		if (isContractValidationRequired) {
			customerId = HelperMethods.getCustomerIdFromSession(dcRequest);
			isSuperAdmin = false;
		}

		String removedCompanies = map.get(InfinityConstants.removedCompanies);
		if (StringUtils.isNotBlank(removedCompanies)) {
			JsonElement removedCompaniesElement = new JsonParser().parse(removedCompanies);
			if (!removedCompaniesElement.isJsonNull() && removedCompaniesElement.isJsonArray()) {
				jsonObject.add(InfinityConstants.removedCompanies, removedCompaniesElement.getAsJsonArray());
			}
		}

		if (!map.containsKey(InfinityConstants.companyList)
				|| StringUtils.isBlank(map.get(InfinityConstants.companyList))) {
			return true;
		}

		String companyList = map.get(InfinityConstants.companyList);
		JsonElement companyListElement = new JsonParser().parse(companyList);
		if (companyListElement.isJsonNull() || !companyListElement.isJsonArray()) {
			ErrorCodeEnum.ERR_10050.setErrorCode(result, "Invalid CompanyList");
			return false;
		}

		Map<String, Set<String>> customerContracts = new HashMap<String, Set<String>>();
		Map<String, Set<String>> contractCIFs = new HashMap<String, Set<String>>();
		Map<String, Map<String, Set<String>>> customerAccounts = new HashMap<String, Map<String, Set<String>>>();
		Map<String, Map<String, Set<String>>> contractAccounts = new HashMap<String, Map<String, Set<String>>>();
		Map<String, Set<String>> loggedInUserPermisions = new HashMap<String, Set<String>>();
		Map<String, Map<String, Map<String, Map<String, Double>>>> loggedInUserLimits = new HashMap<String, Map<String, Map<String, Map<String, Double>>>>();
		if (isContractValidationRequired) {
			getLoggedInUserContracts(customerId, customerContracts, dcRequest.getHeaderMap());
			getAccountsForCustomer(customerId, customerAccounts, dcRequest.getHeaderMap());
			getLoggedInUserPermissions(customerId, loggedInUserPermisions, loggedInUserLimits, dcRequest);
		}

		Map<String, String> serviceDefinitions = new HashMap<String, String>();

		ContractBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(ContractBackendDelegate.class);
		ContractBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl
                .getBusinessDelegate(ContractBusinessDelegate.class);

		Map<String, FeatureActionLimitsDTO> featureActionsLimitsDTOs = new HashMap<String, FeatureActionLimitsDTO>();
		Map<String, FeatureActionLimitsDTO> contractfeatureActionsLimitsDTOs = new HashMap<String, FeatureActionLimitsDTO>();
		
		
		Map<String, Map<String, Set<String>>> productRefPermissions = new HashMap<>();
		boolean isAMSRoleFeatureEnabled = FeatureConfiguration.isAMSRoleFeatureEnabled();
		boolean isProductSpecificFeatueEnabled = FeatureConfiguration.isProductSpecificFeatueEnabled();
		
		Map<String, String> bundleConfigurations = null;
		if(isAMSRoleFeatureEnabled) {
			bundleConfigurations = BundleConfigurationHandler
	                .fetchBundleConfigurations(BundleConfigurationHandler.BUDLENAME_C360,
	                        dcRequest);
		}
		
		Set<String> productsSet = null;
		if(isProductSpecificFeatueEnabled) {
			productsSet = new HashSet<>();
		}
		
		JsonArray excludedCompaniesArray = new JsonArray();
		JsonArray companiesArray = companyListElement.getAsJsonArray();
		for (int i = 0; i < companiesArray.size(); i++) {
			JsonObject companyJsonObject = companiesArray.get(i).getAsJsonObject();
			String contractId = null;
			String cif = null;
			boolean isPrimary;
			String serviceDefinition = null;
			String userRole = null;
			boolean autoSyncAccounts = false;
			
			if (companyJsonObject.has(InfinityConstants.contractId)
					&& !companyJsonObject.get(InfinityConstants.contractId).isJsonNull()) {
				contractId = companyJsonObject.get(InfinityConstants.contractId).getAsString();
			}

			if (companyJsonObject.has(InfinityConstants.cif)
					&& !companyJsonObject.get(InfinityConstants.cif).isJsonNull()) {
				cif = companyJsonObject.get(InfinityConstants.cif).getAsString();
			}

			if (companyJsonObject.has(InfinityConstants.isPrimary)
					&& !companyJsonObject.get(InfinityConstants.isPrimary).isJsonNull()) {
				isPrimary = Boolean.parseBoolean(companyJsonObject.get(InfinityConstants.isPrimary).getAsString());
			}

			if (companyJsonObject.has(InfinityConstants.serviceDefinition)
					&& !companyJsonObject.get(InfinityConstants.serviceDefinition).isJsonNull()) {
				serviceDefinition = companyJsonObject.get(InfinityConstants.serviceDefinition).getAsString();
			}

			serviceDefinitions.put(contractId, serviceDefinition);

			if (companyJsonObject.has(InfinityConstants.roleId)
					&& !companyJsonObject.get(InfinityConstants.roleId).isJsonNull()) {
				userRole = companyJsonObject.get(InfinityConstants.roleId).getAsString();
			}
			
			try {
			companyJsonObject = LegalEntityUtil.addLegalEntityToPermissionsIfReq(companyJsonObject, isSuperAdmin, allLegalEntities, dcRequest);	
			}
			catch (Exception e) {
				ErrorCodeEnum.ERR_10050.setErrorCode(result, "Failed to validate company details");
				return false;
			}
			
			String legalEntityId = companyJsonObject.get("legalEntityId").getAsString();

			if (companyJsonObject.has(InfinityConstants.autoSyncAccounts)
					&& !companyJsonObject.get(InfinityConstants.autoSyncAccounts).isJsonNull()) {
				autoSyncAccounts = Boolean
						.parseBoolean(companyJsonObject.get(InfinityConstants.autoSyncAccounts).getAsString());
			}

			companyJsonObject.addProperty(InfinityConstants.autoSyncAccounts, autoSyncAccounts + "");

			// checking blanks for mandatory params
			if (HelperMethods.isBlank(contractId, cif, serviceDefinition, userRole)) {
				ErrorCodeEnum.ERR_10050.setErrorCode(result, "Invalid company details");
				return false;
			}

			if (isContractValidationRequired) {
				// Contract and CIF validation for Logged in user
				if (isContractValidationRequired && (!customerContracts.containsKey(contractId)
						|| !customerContracts.get(contractId).contains(cif))) {
					companiesArray.remove(i);
					i--;
					continue;
				}
			}

			getContractCIFs(contractId, contractCIFs, dcRequest.getHeaderMap());

			// Contract and CIF validation
			if (!contractCIFs.containsKey(contractId) || !contractCIFs.get(contractId).contains(cif)) {
				companiesArray.remove(i);
				i--;
				continue;
			}

			JsonElement accountsEelement = companyJsonObject.get(InfinityConstants.accounts);

			if (accountsEelement.isJsonNull() || !accountsEelement.isJsonArray()) {
				ErrorCodeEnum.ERR_10050.setErrorCode(result, "Invalid accounts");
				return false;
			}

			getAccountsForContract(contractId, contractAccounts, dcRequest.getHeaderMap());

			JsonArray accountsArray = accountsEelement.getAsJsonArray();
			Set<String> accountList = new HashSet<String>();

			Boolean isUserLevelAccountAccessDelegationEnabled = true;
			// get configuration for customer 360 or DB

			try {
				FeatureActionLimitsDTO coreCustomerFeatureActionDTO = backendDelegate.getRestrictiveFeatureActionLimits(
						serviceDefinition, "", userRole, "", "", dcRequest.getHeaderMap(), true, "",legalEntityId);
				Map<String, FeatureActionLimitsDTO> contractFeatureActionDTO = businessDelegate.getContractActions(contractId, cif,
                        dcRequest.getHeaderMap());
				featureActionsLimitsDTOs.put(cif, coreCustomerFeatureActionDTO);
				contractfeatureActionsLimitsDTOs.put(cif, contractFeatureActionDTO.get(cif));
			} catch (ApplicationException e) {
				logger.error("Exception", e);
			}

			JsonArray excludedAccountsArray = companyJsonObject.has(InfinityConstants.excludedAccounts)
					&& companyJsonObject.get(InfinityConstants.excludedAccounts).isJsonArray()
							? companyJsonObject.get(InfinityConstants.excludedAccounts).getAsJsonArray()
							: new JsonArray();
			for (int j = 0; j < accountsArray.size(); j++) {
				JsonObject accountJsonObject = accountsArray.get(j).getAsJsonObject();
				String accountID = null;
				boolean isEnabled = true;
				if (accountJsonObject.has(InfinityConstants.accountId)
						&& !accountJsonObject.get(InfinityConstants.accountId).isJsonNull()) {
					accountID = accountJsonObject.get(InfinityConstants.accountId).getAsString();
				}

				if (accountJsonObject.has(InfinityConstants.isEnabled)
						&& !accountJsonObject.get(InfinityConstants.isEnabled).isJsonNull()) {
					isEnabled = Boolean.parseBoolean(accountJsonObject.get(InfinityConstants.isEnabled).getAsString());
				}

				// Account Validation

				// accounts belong to the contract and CIF
				if (!contractAccounts.containsKey(contractId) || !contractAccounts.get(contractId).containsKey(cif)
						|| !contractAccounts.get(contractId).get(cif).contains(accountID)) {
					accountsArray.remove(j);
					j--;
					continue;
				}

				// account delegation is at user level then accounts must be accessible by
				// logged in user
				if (isContractValidationRequired && isUserLevelAccountAccessDelegationEnabled
						&& (!customerAccounts.containsKey(contractId)
								|| !customerAccounts.get(contractId).containsKey(cif)
								|| !customerAccounts.get(contractId).get(cif).contains(accountID))) {
					accountsArray.remove(j);
					j--;
					continue;
				}

				if (isEnabled) {
					accountList.add(accountID);
					if(isProductSpecificFeatueEnabled) {
						productsSet.add(JSONUtil.getString(accountJsonObject, "productId"));
					}
				} else {
					excludedAccountsArray.add(accountsArray.get(j));
					accountsArray.remove(j);
					j--;
					continue;
				}
			}
			if (accountList.size() <= 0) {
				ErrorCodeEnum.ERR_10050.setErrorCode(result, "At least one account should be present");
				return false;
			}
			customerAccountsMap.put(cif, accountList);

			companyJsonObject.add(InfinityConstants.excludedAccounts, excludedAccountsArray);

		}

		jsonObject.add(InfinityConstants.companyList, companiesArray);

		boolean isAccountLevelAllowed = true;
		boolean isLegalEntityVerified = false; // to verify whether a call to getAllCompanyLegalUnits is done or not
		// isAccountLevelAllowed = AdminUtil.isAccountlevelPermissionsAllowed(request);
		
		if(isProductSpecificFeatueEnabled) {
			productRefPermissions = backendDelegate.getProductLevelPermissions(productsSet);
		}

		JsonArray accountLevelPermissionsArray = new JsonArray();

		JsonArray excludedAccountLevelPermissionsArray = new JsonArray();
		if (!isAccountLevelAllowed) {
			map.remove(InfinityConstants.accountLevelPermissions);
		} else {
			String accountLevelPermissions = map.get(InfinityConstants.accountLevelPermissions);
			JsonElement accountLevelPermissionsElement = new JsonObject();
			try {
				accountLevelPermissionsElement = new JsonParser().parse(accountLevelPermissions);
			} catch (Exception e) {
			}
			if (!accountLevelPermissionsElement.isJsonNull() && accountLevelPermissionsElement.isJsonArray()) {

				accountLevelPermissionsArray = accountLevelPermissionsElement.getAsJsonArray();

				excludedAccountLevelPermissionsArray = new JsonArray();
				for (int i = 0; i < accountLevelPermissionsArray.size(); i++) {

					JsonObject accountLevelPermissionsJsonObject = accountLevelPermissionsArray.get(i)
							.getAsJsonObject();

					JsonObject excludedAccountLevelPermissionsJsonObject = new JsonObject();
					excludedAccountLevelPermissionsArray.add(excludedAccountLevelPermissionsJsonObject);
					String cif = accountLevelPermissionsJsonObject.get(InfinityConstants.cif).getAsString();
					excludedAccountLevelPermissionsJsonObject.add(InfinityConstants.cif,
							accountLevelPermissionsJsonObject.get(InfinityConstants.cif));
					
					try {
						if (!isLegalEntityVerified) {
							accountLevelPermissionsJsonObject = LegalEntityUtil.addLegalEntityToPermissionsIfReq(
									accountLevelPermissionsJsonObject, isSuperAdmin, allLegalEntities, dcRequest);
							isLegalEntityVerified = true;
						}
					} catch (Exception e) {
						ErrorCodeEnum.ERR_29012.setErrorCode(result, "Failed to validate account level permission details!");
						return false;
					}

					excludedAccountLevelPermissionsJsonObject.add(InfinityConstants.legalEntityId,
							accountLevelPermissionsJsonObject.get(InfinityConstants.legalEntityId));

					JsonElement accountsElement = accountLevelPermissionsJsonObject.get(InfinityConstants.accounts);

					if (!featureActionsLimitsDTOs.containsKey(cif) && !contractfeatureActionsLimitsDTOs.containsKey(cif)
							|| (isContractValidationRequired && !loggedInUserPermisions.containsKey(cif))) {
						accountLevelPermissionsArray.remove(i);
						i--;
						continue;
					}
					
					FeatureActionLimitsDTO featureActionLimitsDTO = featureActionsLimitsDTOs.get(cif);
					FeatureActionLimitsDTO contractFeatureActionDTO = contractfeatureActionsLimitsDTOs.get(cif);

					Map<String, Set<String>> accoutLevelActions = featureActionLimitsDTO.getAccountLevelPermissions();
					Map<String, Map<String, Map<String, String>>> monitoryActions = featureActionLimitsDTO
							.getMonetaryActionLimits();
					Map<String, Map<String, Map<String, String>>> monitoryActions1 = contractFeatureActionDTO
							.getCoreCustomerTransactionLimits();
                            

					if (!accountsElement.isJsonNull() && accountsElement.isJsonArray()) {

						JsonArray accounts = accountsElement.getAsJsonArray();
						JsonArray excludedAccounts = new JsonArray();

						excludedAccountLevelPermissionsJsonObject.add(InfinityConstants.accounts, excludedAccounts);

						for (int j = 0; j < accounts.size(); j++) {

							JsonObject accountJsonObject = accounts.get(j).getAsJsonObject();
							String accountId = accountJsonObject.get(InfinityConstants.accountId).getAsString();

							if (!customerAccountsMap.containsKey(cif)
									|| !customerAccountsMap.get(cif).contains(accountId)) {
								accounts.remove(j);
								j--;
								continue;
							}

							JsonObject excludedAccountJsonObject = new JsonObject();
							excludedAccounts.add(excludedAccountJsonObject);

							excludedAccountJsonObject.add(InfinityConstants.accountId,
									accountJsonObject.get(InfinityConstants.accountId));
							JsonArray featurePermissions = accountJsonObject.get(InfinityConstants.featurePermissions)
									.getAsJsonArray();
							JsonArray excludedFeaturePermissions = new JsonArray();

							excludedAccountJsonObject.add(InfinityConstants.featurePermissions,
									excludedFeaturePermissions);
							
							Map<String, Set<String>> accoutLevelActions1 = contractFeatureActionDTO
									.getAsscoiatedAccountActions().get(accountId);

							for (int k = 0; k < featurePermissions.size(); k++) {

								JsonObject featurePermissionsJsonObject = featurePermissions.get(k).getAsJsonObject();
								String featureId = featurePermissionsJsonObject.get(InfinityConstants.featureId)
										.getAsString();

//								if (!accoutLevelActions.containsKey(featureId)||
//                                        !accoutLevelActions1.containsKey(featureId)
//                                        || !monitoryActions.containsKey(featureId)|| !monitoryActions1.containsKey(featureId)) {
//									featurePermissions.remove(k);
//									k--;
//									continue;
//								}
								
								if (!(accoutLevelActions.containsKey(featureId)
                                        && accoutLevelActions1.containsKey(featureId))
                                        && !(monitoryActions.containsKey(featureId)
                                                && monitoryActions1.containsKey(featureId))) {
                                    featurePermissions.remove(k);
                                    k--;
                                    continue;
                                }

								JsonObject excludedFeaturePermissionsJsonObject = new JsonObject();

								excludedFeaturePermissions.add(excludedFeaturePermissionsJsonObject);

								excludedFeaturePermissionsJsonObject.add(InfinityConstants.featureId,
										featurePermissionsJsonObject.get(InfinityConstants.featureId));
								JsonArray permissions = featurePermissionsJsonObject.get(InfinityConstants.permissions)
										.getAsJsonArray();
								JsonArray excludedPermissions = new JsonArray();

								excludedFeaturePermissionsJsonObject.add(InfinityConstants.permissions,
										excludedPermissions);

								for (int l = 0; l < permissions.size(); l++) {

									JsonObject permissonsJsonObject = permissions.get(l).getAsJsonObject();

									String actionId = null;
									if (permissonsJsonObject.has(InfinityConstants.id)) {
										actionId = permissonsJsonObject.get(InfinityConstants.id).getAsString();
									}

									if (StringUtils.isBlank(actionId)
											&& permissonsJsonObject.has(InfinityConstants.actionId)) {
										actionId = permissonsJsonObject.get(InfinityConstants.actionId).getAsString();
									}

									Boolean isEnabled = Boolean.parseBoolean(
											permissonsJsonObject.get(InfinityConstants.isEnabled).getAsString());

									if (!isEnabled) {
										JsonObject excludedPermissionsJsonObject = new JsonObject();
										excludedPermissions.add(excludedPermissionsJsonObject);
										excludedPermissionsJsonObject.add(InfinityConstants.id,
												permissonsJsonObject.get(InfinityConstants.id));
										excludedPermissionsJsonObject.add(InfinityConstants.actionId,
												permissonsJsonObject.get(InfinityConstants.actionId));
									}
									Boolean isAccountLevel = "1".equals(
											JSONUtil.getString(featureActionLimitsDTO.getActionsInfo().get(actionId),
													InfinityConstants.isAccountLevel));

                                    
                  /* comparing feature action with feature actions frm service definition and role  */               

                                    if (((accoutLevelActions.get(featureId) == null
                                            || !accoutLevelActions.get(featureId).contains(actionId)) &&
                                            (monitoryActions.get(featureId) == null
                                                    || !monitoryActions.get(featureId).containsKey(actionId))
                                            && !isAccountLevel) || !isEnabled
                                            || (isContractValidationRequired
                                                    && !loggedInUserPermisions.get(cif).contains(actionId))) {
                                        permissions.remove(l);
                                        l--;
                                        continue;
                                    }
                  /* comparing feature action with feature actions frm contract  */     
                                    
                                    if (((accoutLevelActions1.get(featureId) == null
                                            || !accoutLevelActions1.get(featureId).contains(actionId)) &&
                                            (monitoryActions1.get(featureId) == null
                                                    || !monitoryActions1.get(featureId).containsKey(actionId))
                                            && !isAccountLevel) || !isEnabled
                                            || (isContractValidationRequired
                                                    && !loggedInUserPermisions.get(cif).contains(actionId))) {
                                        permissions.remove(l);
                                        l--;
                                        continue;
                                    }
                    /* comparing action with  actions frm product and AArole  */
                                    if(isProductSpecificFeatueEnabled) {
                                    	String product = JSONUtil.getString(accountJsonObject, "productId");
                                    	if(!productRefPermissions.containsKey(product)
                                    			|| !productRefPermissions.get(product).containsKey("featureId")
                                    			|| !productRefPermissions.get(product).get("featureId").contains(actionId)) {
                                    		permissions.remove(l);
                                    		l--;
                                    		continue;
                                    	}
                                    }
                                    
                                    if(isAMSRoleFeatureEnabled) {
                                    	String role = JSONUtil.getString(accountJsonObject, "ownerType");
                                        String rolemapping = bundleConfigurations
                                                    .get(accountLevelPermissionsJsonObject.get(InfinityConstants.legalEntityId)
                                                    		+ BundleConfigurationHandler.CUSTOMER_ROLE_MAPPING);
                                    	Map<String, Set<String>> aaRolePermissions = backendDelegate.getArrangementRolePermissions(role,
                                                       rolemapping, dcRequest.getHeaderMap());
                                    	if(!aaRolePermissions.get(role).contains(actionId)) {
                                    		permissions.remove(l);
                                    		l--;
                                    		continue;
                                    	}
                                    }
								}
							}
						}
					}
				}
				jsonObject.add(InfinityConstants.accountLevelPermissions, accountLevelPermissionsArray);
				jsonObject.add(InfinityConstants.excludedAccountLevelPermissions, excludedAccountLevelPermissionsArray);
			}
		}

		JsonArray globalLevelPermissionsArray = new JsonArray();

		JsonArray excludedGlobalLevelPermissionsArray = new JsonArray();
		String globalLevelPermissions = map.get(InfinityConstants.globalLevelPermissions);
		JsonElement globalLevelPermissionsElement = new JsonObject();
		try {
			globalLevelPermissionsElement = new JsonParser().parse(globalLevelPermissions);
		} catch (Exception e) {
		}
		isLegalEntityVerified = false;
		if (!globalLevelPermissionsElement.isJsonNull() && globalLevelPermissionsElement.isJsonArray()) {

			globalLevelPermissionsArray = globalLevelPermissionsElement.getAsJsonArray();
			excludedGlobalLevelPermissionsArray = new JsonArray();
			for (int i = 0; i < globalLevelPermissionsArray.size(); i++) {
				JsonObject globalLevelPermissionJsonObject = globalLevelPermissionsArray.get(i).getAsJsonObject();

				JsonObject excludedGlobalLevelPermissionJsonObject = new JsonObject();
				excludedGlobalLevelPermissionsArray.add(excludedGlobalLevelPermissionJsonObject);
				String cif = globalLevelPermissionJsonObject.get(InfinityConstants.cif).getAsString();
				excludedGlobalLevelPermissionJsonObject.add(InfinityConstants.cif,
						globalLevelPermissionJsonObject.get(InfinityConstants.cif));		
				
				try {
					if (!isLegalEntityVerified) {
						globalLevelPermissionJsonObject = LegalEntityUtil.addLegalEntityToPermissionsIfReq(
								globalLevelPermissionJsonObject, isSuperAdmin, allLegalEntities, dcRequest);
						isLegalEntityVerified = true;
					}
				} catch (Exception e) {
					ErrorCodeEnum.ERR_29043.setErrorCode(result, "Failed to validate global level permission details!");
					return false;
				}
				excludedGlobalLevelPermissionJsonObject.add(InfinityConstants.legalEntityId,
						globalLevelPermissionJsonObject.get(InfinityConstants.legalEntityId));

				JsonElement featuresElement = globalLevelPermissionJsonObject.get(InfinityConstants.features);

				FeatureActionLimitsDTO featureActionLimitsDTO = featureActionsLimitsDTOs.get(cif);
				FeatureActionLimitsDTO contractFeatureActionDTO = contractfeatureActionsLimitsDTOs.get(cif);

				if (!featureActionsLimitsDTOs.containsKey(cif) && !contractfeatureActionsLimitsDTOs.containsKey(cif)
						|| (isContractValidationRequired && !loggedInUserPermisions.containsKey(cif))) {
					globalLevelPermissionsArray.remove(i);
					i--;
					continue;
				}

				Map<String, Set<String>> globalLevelActions = featureActionLimitsDTO.getGlobalLevelPermissions();
				Map<String, Set<String>> globalLevelActions1 = contractFeatureActionDTO.getCoreCustomerGlobalLevelPermissions();

				if (!featuresElement.isJsonNull() && featuresElement.isJsonArray()) {

					JsonArray features = featuresElement.getAsJsonArray();

					JsonArray excludedFeatures = new JsonArray();

					excludedGlobalLevelPermissionJsonObject.add(InfinityConstants.features, excludedFeatures);
					for (int j = 0; j < features.size(); j++) {

						JsonObject featureJsonObject = features.get(j).getAsJsonObject();

						String featureId = featureJsonObject.get(InfinityConstants.featureId).getAsString();
						if (!globalLevelActions.containsKey(featureId)|| !globalLevelActions1.containsKey(featureId)) {
							features.remove(j);
							j--;
							continue;
						}
						JsonObject excludedFeatureJsonObject = new JsonObject();

						excludedFeatures.add(excludedFeatureJsonObject);

						excludedFeatureJsonObject.add(InfinityConstants.featureId,
								featureJsonObject.get(InfinityConstants.featureId));
						JsonArray permissions = featureJsonObject.get(InfinityConstants.permissions).getAsJsonArray();
						JsonArray excludedPermissions = new JsonArray();

						excludedFeatureJsonObject.add(InfinityConstants.permissions, excludedPermissions);
						for (int k = 0; k < permissions.size(); k++) {
							JsonObject permissionJsonObject = permissions.get(k).getAsJsonObject();

							String permissionId = "";

							if (permissionJsonObject.has(InfinityConstants.id)) {
								permissionId = permissionJsonObject.get(InfinityConstants.id).getAsString();
							}

							if (StringUtils.isBlank(permissionId)
									&& permissionJsonObject.has(InfinityConstants.actionId)) {
								permissionId = permissionJsonObject.get(InfinityConstants.actionId).getAsString();
							}

							if (StringUtils.isBlank(permissionId)
									&& permissionJsonObject.has(InfinityConstants.permissionType)) {
								permissionId = permissionJsonObject.get(InfinityConstants.permissionType).getAsString();
							}

							Boolean isEnabled = Boolean
									.parseBoolean(permissionJsonObject.get(InfinityConstants.isEnabled).getAsString());
							if (!isEnabled) {
								JsonObject excludedPermissionJsonObject = new JsonObject();
								excludedPermissions.add(excludedPermissionJsonObject);
								excludedPermissionJsonObject.add(InfinityConstants.id,
										permissionJsonObject.get(InfinityConstants.id));
								excludedPermissionJsonObject.add(InfinityConstants.actionId,
										permissionJsonObject.get(InfinityConstants.actionId));
								excludedPermissionJsonObject.add(InfinityConstants.permissionType,
										permissionJsonObject.get(InfinityConstants.permissionType));
							}
							if (!globalLevelActions.get(featureId).contains(permissionId)
                                    ||!globalLevelActions1.get(featureId).contains(permissionId) || !isEnabled || (isContractValidationRequired
									&& !loggedInUserPermisions.get(cif).contains(permissionId))) {
								permissions.remove(k);
								k--;
								continue;
							}
						}
					}
				}
			}

			jsonObject.add(InfinityConstants.globalLevelPermissions, globalLevelPermissionsArray);
			jsonObject.add(InfinityConstants.excludedGlobalLevelPermissions, excludedGlobalLevelPermissionsArray);
		}

		String transactionLimits = map.get(InfinityConstants.transactionLimits);
		JsonElement transactionLimitsElement = new JsonObject();
		try {
			transactionLimitsElement = new JsonParser().parse(transactionLimits);
		} catch (Exception e) {
		}
		isLegalEntityVerified = false;
		if (!transactionLimitsElement.isJsonNull() && transactionLimitsElement.isJsonArray()) {
			double user_maxtransactionlimit = 0, user_dailylimit = 0, user_weeklylimit = 0; 
			double mb_user_maxtransactionlimit = 0, mb_user_dailylimit = 0, mb_user_weeklylimit = 0; 
			JsonArray transactionLimitsArray = transactionLimitsElement.getAsJsonArray();
			for (int i = 0; i < transactionLimitsArray.size(); i++) {
				JsonObject transactionLimitsJsonObject = transactionLimitsArray.get(i).getAsJsonObject();

				String cif = transactionLimitsJsonObject.get(InfinityConstants.cif).getAsString();
				
				try {
					if (!isLegalEntityVerified) {
						transactionLimitsJsonObject = LegalEntityUtil.addLegalEntityToPermissionsIfReq(
								transactionLimitsJsonObject, isSuperAdmin, allLegalEntities, dcRequest);
						isLegalEntityVerified = true;
					}
				} catch (Exception e) {
					ErrorCodeEnum.ERR_29013.setErrorCode(result, "Failed to validate transaction limit details!");
					return false;
				}

				FeatureActionLimitsDTO featureActionLimitsDTO = featureActionsLimitsDTOs.get(cif);
				FeatureActionLimitsDTO contractFeatureActionDTO = contractfeatureActionsLimitsDTOs.get(cif);

				if (!featureActionsLimitsDTOs.containsKey(cif) ||!contractfeatureActionsLimitsDTOs.containsKey(cif)
						|| (isContractValidationRequired && !loggedInUserLimits.containsKey(cif))) {
					transactionLimitsArray.remove(i);
					i--;
					continue;
				}
				
				 JsonElement limitsElement = transactionLimitsJsonObject.get(InfinityConstants.limitGroups);

	                if (!limitsElement.isJsonNull() && limitsElement.isJsonArray()) {

	                	JsonArray limit_groups = limitsElement.getAsJsonArray();
	                	try {
	                		for (int j = 0; j < limit_groups.size(); j++) {

	                			JsonObject limitgroupsJsonObject = limit_groups.get(j).getAsJsonObject();

	                			JsonArray limits_within_limitgroup = limitgroupsJsonObject.get(InfinityConstants.limits)
	                					.getAsJsonArray();
	                			for (int c = 0 ; c < limits_within_limitgroup.size(); c++) {

	                				JsonObject arrayinsidelimits = limits_within_limitgroup.get(c).getAsJsonObject();

	                				String limitIdinsidelimits = arrayinsidelimits.get(InfinityConstants.id).getAsString();


	                				if(limitIdinsidelimits.equals(InfinityConstants.MAX_TRANSACTION_LIMIT)){

	                					user_maxtransactionlimit = Double.parseDouble(arrayinsidelimits.get(InfinityConstants.value).getAsString());
	                				}

	                				else if(limitIdinsidelimits.equals(InfinityConstants.DAILY_LIMIT)){

	                					user_dailylimit = Double.parseDouble(arrayinsidelimits.get(InfinityConstants.value).getAsString());
	                				}

	                				else if(limitIdinsidelimits.equals(InfinityConstants.WEEKLY_LIMIT)){

	                					user_weeklylimit = Double.parseDouble(arrayinsidelimits.get(InfinityConstants.value).getAsString());
	                				}
	                				

	                				//MB Limits
	                				
	                				if(limitIdinsidelimits.equals(HBLConstants.MB_MAX_TRANSACTION_LIMIT)){

	                					mb_user_maxtransactionlimit = Double.parseDouble(arrayinsidelimits.get(InfinityConstants.value).getAsString());
	                				}

	                				else if(limitIdinsidelimits.equals(HBLConstants.MB_DAILY_LIMIT)){

	                					mb_user_dailylimit = Double.parseDouble(arrayinsidelimits.get(InfinityConstants.value).getAsString());
	                				}

	                				else if(limitIdinsidelimits.equals(HBLConstants.MB_WEEKLY_LIMIT)){

	                					mb_user_weeklylimit = Double.parseDouble(arrayinsidelimits.get(InfinityConstants.value).getAsString());
	                				}

	                			}

	                			if((user_maxtransactionlimit > user_dailylimit) || (user_dailylimit > user_weeklylimit)) {

	                				throw new ApplicationException(ErrorCodeEnum.ERR_10420);
	                			}
	                			//MB limits validation
	                			if((mb_user_maxtransactionlimit > mb_user_dailylimit) || (mb_user_dailylimit > mb_user_weeklylimit)) {

	                				throw new ApplicationException(ErrorCodeEnum.ERR_10420);
	                			}
	                		}
	                	}
	                	catch (ApplicationException e) {
	                		logger.error("Exception occured invalid limits" + e.getMessage());                         	
	                    	ErrorCodeEnum.ERR_10420.setErrorCode(result);
	                        return false;
	                	}
	                }

				Map<String, Map<String, Map<String, String>>> transactionLimitsMap = featureActionLimitsDTO
						.getMonetaryActionLimits();
				Map<String, Map<String, Map<String, String>>> transactionLimitsMap1 = contractFeatureActionDTO
                        .getCoreCustomerTransactionLimits();

				JsonElement accountsElement = transactionLimitsJsonObject.get(InfinityConstants.accounts);

				if (!accountsElement.isJsonNull() && accountsElement.isJsonArray()) {

					JsonArray accounts = accountsElement.getAsJsonArray();

					for (int j = 0; j < accounts.size(); j++) {

						JsonObject accountJsonObject = accounts.get(j).getAsJsonObject();

						String accountId = accountJsonObject.get(InfinityConstants.accountId).getAsString();

						if (!customerAccountsMap.containsKey(cif) || !customerAccountsMap.get(cif).contains(accountId)
								|| (isContractValidationRequired
										&& !loggedInUserLimits.get(cif).containsKey(accountId))) {
							accounts.remove(j);
							j--;
							continue;
						}
						
						double account_maxtransactionlimit = 0, account_dailylimit = 0, account_weeklylimit = 0,
                        		account_PAmaxtransactionlimit = 0, account_PAdailylimit = 0, account_PAweeklylimit = 0,
                        		account_ADmaxtransactionlimit = 0, account_ADdailylimit = 0, account_ADweeklylimit = 0;
						
						double mb_account_maxtransactionlimit = 0, mb_account_dailylimit = 0, mb_account_weeklylimit = 0,
                        		mb_account_PAmaxtransactionlimit = 0, mb_account_PAdailylimit = 0, mb_account_PAweeklylimit = 0,
                        		mb_account_ADmaxtransactionlimit = 0, mb_account_ADdailylimit = 0, mb_account_ADweeklylimit = 0;

						JsonArray featurePermissions = accountJsonObject.get(InfinityConstants.featurePermissions)
								.getAsJsonArray();

						if (featurePermissions.size() <= 0) {
							for (int d = 0; d < companiesArray.size(); d++) {
								JsonObject companyJsonObject = companiesArray.get(d).getAsJsonObject();
								if (cif.equals(companyJsonObject.get(InfinityConstants.cif).getAsString())) {
									companiesArray.remove(d);
									d--;
								}
							}

							for (int d = 0; d < globalLevelPermissionsArray.size(); d++) {
								JsonObject companyJsonObject = globalLevelPermissionsArray.get(d).getAsJsonObject();
								if (cif.equals(companyJsonObject.get(InfinityConstants.cif).getAsString())) {
									globalLevelPermissionsArray.remove(d);
									d--;
								}
							}

							for (int d = 0; d < excludedGlobalLevelPermissionsArray.size(); d++) {
								JsonObject companyJsonObject = excludedGlobalLevelPermissionsArray.get(d)
										.getAsJsonObject();
								if (cif.equals(companyJsonObject.get(InfinityConstants.cif).getAsString())) {
									excludedGlobalLevelPermissionsArray.remove(d);
									d--;
								}
							}

							for (int d = 0; d < accountLevelPermissionsArray.size(); d++) {
								JsonObject companyJsonObject = accountLevelPermissionsArray.get(d).getAsJsonObject();
								if (cif.equals(companyJsonObject.get(InfinityConstants.cif).getAsString())) {
									accountLevelPermissionsArray.remove(d);
									d--;
								}
							}

							for (int d = 0; d < excludedAccountLevelPermissionsArray.size(); d++) {
								JsonObject companyJsonObject = excludedAccountLevelPermissionsArray.get(d)
										.getAsJsonObject();
								if (cif.equals(companyJsonObject.get(InfinityConstants.cif).getAsString())) {
									excludedAccountLevelPermissionsArray.remove(d);
									d--;
								}
							}

							transactionLimitsArray.remove(i);
							i--;
							break;
						}

						for (int k = 0; k < featurePermissions.size(); k++) {
							JsonObject featurePermissionJsonObject = featurePermissions.get(k).getAsJsonObject();

							String feaureId = featurePermissionJsonObject.get(InfinityConstants.featureId)
									.getAsString();

							String actionId = featurePermissionJsonObject.get(InfinityConstants.actionId).getAsString();

							if (!transactionLimitsMap.containsKey(feaureId)
									|| !transactionLimitsMap.get(feaureId).containsKey(actionId)|| !transactionLimitsMap1.containsKey(feaureId)
                                    || !transactionLimitsMap1.get(feaureId).containsKey(actionId)
									|| (isContractValidationRequired
											&& !loggedInUserLimits.get(cif).get(accountId).containsKey(actionId))) {
								featurePermissions.remove(k);
								k--;
								continue;
							}

							featurePermissionJsonObject.add(InfinityConstants.limitGroupId, featureActionLimitsDTO
									.getActionsInfo().get(actionId).get(InfinityConstants.limitGroupId));

							Map<String, String> limitMap = transactionLimitsMap.get(feaureId).get(actionId);

							JsonArray limits = featurePermissionJsonObject.get(InfinityConstants.limits)
									.getAsJsonArray();
							//valid action limits group
							logger.debug("HBL::InfinityUserManagementResourceImpl:current actionId "+actionId);
                            try {
                            	for (int z = 0; z < limits.size(); z++) {

                            		JsonObject accountlimit = limits.get(z).getAsJsonObject();
                            		logger.debug("HBL::InfinityUserManagementResourceImpl:current accountlimit object"+accountlimit.toString());

                            		String account_limitid = accountlimit.get(InfinityConstants.id).getAsString();

                            		if(account_limitid.equals(InfinityConstants.MAX_TRANSACTION_LIMIT)){
                            			account_maxtransactionlimit = Double.parseDouble(accountlimit.get(InfinityConstants.value).getAsString());
                            		}

                            		else if(account_limitid.equals(InfinityConstants.DAILY_LIMIT)){
                            			account_dailylimit = Double.parseDouble(accountlimit.get(InfinityConstants.value).getAsString());
                            		}

                            		else if(account_limitid.equals(InfinityConstants.WEEKLY_LIMIT)){
                            			account_weeklylimit = Double.parseDouble(accountlimit.get(InfinityConstants.value).getAsString());
                            		}

                            		else  if(account_limitid.equals(InfinityConstants.PRE_APPROVED_TRANSACTION_LIMIT)){
                            			account_PAmaxtransactionlimit = Double.parseDouble(accountlimit.get(InfinityConstants.value).getAsString());
                            		}

                            		else if(account_limitid.equals(InfinityConstants.PRE_APPROVED_DAILY_LIMIT)){
                            			account_PAdailylimit = Double.parseDouble(accountlimit.get(InfinityConstants.value).getAsString());
                            		}

                            		else if(account_limitid.equals(InfinityConstants.PRE_APPROVED_WEEKLY_LIMIT)){
                            			account_PAweeklylimit = Double.parseDouble(accountlimit.get(InfinityConstants.value).getAsString());
                            		}

                            		else  if(account_limitid.equals(InfinityConstants.AUTO_DENIED_TRANSACTION_LIMIT)){
                            			account_ADmaxtransactionlimit = Double.parseDouble(accountlimit.get(InfinityConstants.value).getAsString());
                            		}

                            		else if(account_limitid.equals(InfinityConstants.AUTO_DENIED_DAILY_LIMIT)){
                            			account_ADdailylimit = Double.parseDouble(accountlimit.get(InfinityConstants.value).getAsString());
                            		}

                            		else if(account_limitid.equals(InfinityConstants.AUTO_DENIED_WEEKLY_LIMIT)){
                            			account_ADweeklylimit = Double.parseDouble(accountlimit.get(InfinityConstants.value).getAsString());
                            		}      
                            		
                            		
                            		//MB limits
                            		
                            		
                            		else if(account_limitid.equals(HBLConstants.MB_MAX_TRANSACTION_LIMIT)){
                            			mb_account_maxtransactionlimit = Double.parseDouble(accountlimit.get(InfinityConstants.value).getAsString());
                            		}

                            		else if(account_limitid.equals(HBLConstants.MB_DAILY_LIMIT)){
                            			mb_account_dailylimit = Double.parseDouble(accountlimit.get(InfinityConstants.value).getAsString());
                            		}

                            		else if(account_limitid.equals(HBLConstants.MB_WEEKLY_LIMIT)){
                            			mb_account_weeklylimit = Double.parseDouble(accountlimit.get(InfinityConstants.value).getAsString());
                            		}

                            		else  if(account_limitid.equals(HBLConstants.PRE_APPROVED_MB_TRANSACTION_LIMIT)){
                            			mb_account_PAmaxtransactionlimit = Double.parseDouble(accountlimit.get(InfinityConstants.value).getAsString());
                            		}

                            		else if(account_limitid.equals(HBLConstants.PRE_APPROVED_MB_DAILY_LIMIT)){
                            			mb_account_PAdailylimit = Double.parseDouble(accountlimit.get(InfinityConstants.value).getAsString());
                            		}

                            		else if(account_limitid.equals(HBLConstants.PRE_APPROVED_MB_WEEKLY_LIMIT)){
                            			mb_account_PAweeklylimit = Double.parseDouble(accountlimit.get(InfinityConstants.value).getAsString());
                            		}

                            		else  if(account_limitid.equals(HBLConstants.AUTO_DENIED_MB_TRANSACTION_LIMIT)){
                            			mb_account_ADmaxtransactionlimit = Double.parseDouble(accountlimit.get(InfinityConstants.value).getAsString());
                            		}

                            		else if(account_limitid.equals(HBLConstants.AUTO_DENIED_MB_DAILY_LIMIT)){
                            			mb_account_ADdailylimit = Double.parseDouble(accountlimit.get(InfinityConstants.value).getAsString());
                            		}

                            		else if(account_limitid.equals(HBLConstants.AUTO_DENIED_MB_WEEKLY_LIMIT)){
                            			mb_account_ADweeklylimit = Double.parseDouble(accountlimit.get(InfinityConstants.value).getAsString());
                            		}
                            	}
                            	logger.debug("HBL::InfinityUserManagementResourceImpl:account_maxtransactionlimit"+account_maxtransactionlimit);
                            	logger.debug("HBL::InfinityUserManagementResourceImpl:account_dailylimit"+account_dailylimit);
                            	logger.debug("HBL::InfinityUserManagementResourceImpl:account_weeklylimit"+account_weeklylimit);
                            	logger.debug("HBL::InfinityUserManagementResourceImpl:account_ADmaxtransactionlimit"+account_ADmaxtransactionlimit);
                            	logger.debug("HBL::InfinityUserManagementResourceImpl:account_maxtransactionlimit"+account_maxtransactionlimit);
                            	logger.debug("HBL::InfinityUserManagementResourceImpl:account_ADdailylimit"+account_ADdailylimit);
                            	logger.debug("HBL::InfinityUserManagementResourceImpl:account_PAmaxtransactionlimit"+account_PAmaxtransactionlimit);
                            	logger.debug("HBL::InfinityUserManagementResourceImpl:account_PAdailylimit"+account_PAdailylimit);
                            	logger.debug("HBL::InfinityUserManagementResourceImpl:account_PAweeklylimit"+account_PAweeklylimit);
                            	logger.debug("HBL::InfinityUserManagementResourceImpl:account_ADweeklylimit"+account_ADweeklylimit);
                            	
                            	
                            	//mb limits
                            	
                            	logger.debug("HBL::InfinityUserManagementResourceImpl:mb_account_maxtransactionlimit"+mb_account_maxtransactionlimit);
                            	logger.debug("HBL::InfinityUserManagementResourceImpl:mb_account_dailylimit"+mb_account_dailylimit);
                            	logger.debug("HBL::InfinityUserManagementResourceImpl:mb_account_weeklylimit"+mb_account_weeklylimit);
                            	logger.debug("HBL::InfinityUserManagementResourceImpl:mb_account_ADmaxtransactionlimit"+mb_account_ADmaxtransactionlimit);
                            	logger.debug("HBL::InfinityUserManagementResourceImpl:mb_account_maxtransactionlimit"+mb_account_maxtransactionlimit);
                            	logger.debug("HBL::InfinityUserManagementResourceImpl:mb_account_ADdailylimit"+mb_account_ADdailylimit);
                            	logger.debug("HBL::InfinityUserManagementResourceImpl:mb_account_PAmaxtransactionlimit"+mb_account_PAmaxtransactionlimit);
                            	logger.debug("HBL::InfinityUserManagementResourceImpl:mb_account_PAdailylimit"+mb_account_PAdailylimit);
                            	logger.debug("HBL::InfinityUserManagementResourceImpl:mb_account_PAweeklylimit"+mb_account_PAweeklylimit);
                            	logger.debug("HBL::InfinityUserManagementResourceImpl:mb_account_ADweeklylimit"+mb_account_ADweeklylimit);

                            	if((account_maxtransactionlimit > account_dailylimit) || (account_dailylimit > account_weeklylimit)) {
                            		logger.debug("HBL::InfinityUserManagementResourceImpl: validation1:max TX limit > dailylimit OR dailylimit > Weekly Limit");
                            		throw new ApplicationException(ErrorCodeEnum.ERR_10420);

                            	}

                            	if((account_ADmaxtransactionlimit > account_maxtransactionlimit) || (account_ADdailylimit > account_dailylimit) || 
                            			(account_ADweeklylimit > account_weeklylimit)) {
                            		logger.debug("HBL::InfinityUserManagementResourceImpl:validation2: autoDenay max tx limit > max tx limit OR autoDenay Daily limit > Daily Limit");
                            		throw new ApplicationException(ErrorCodeEnum.ERR_10420);

                            	}

                            	if((account_PAmaxtransactionlimit > account_PAdailylimit) || (account_PAdailylimit > account_PAweeklylimit)) {  
                            		logger.debug("HBL::InfinityUserManagementResourceImpl:validation3: preApproved  max tx limit > preApproved Daily limit  OR preApproved Daily Limit > preApproved weekly limit");
                            		throw new ApplicationException(ErrorCodeEnum.ERR_10420);
                            	}

                            	if((account_ADmaxtransactionlimit > account_ADdailylimit) || (account_ADdailylimit > account_ADweeklylimit)) {
                            		logger.debug("HBL::InfinityUserManagementResourceImpl:validation4: autoDenay max tx limit > autoDenay Daily limit");
                            		throw new ApplicationException(ErrorCodeEnum.ERR_10420);

                            	}

                            	if((account_PAmaxtransactionlimit > account_ADmaxtransactionlimit)|| (account_PAdailylimit > account_ADdailylimit) ||
                            			(account_PAweeklylimit > account_ADweeklylimit)) {  
                            		logger.debug("HBL::InfinityUserManagementResourceImpl:validation3: preApproved  max tx limit > autoDenay max tx limit  OR preApproved Daily Limit > autoDenay Daily limit");
                            		throw new ApplicationException(ErrorCodeEnum.ERR_10420);

                            	}
                            	
                            	//MB limits validation
                            	
                            	if((mb_account_maxtransactionlimit > mb_account_dailylimit) || (mb_account_dailylimit > mb_account_weeklylimit)) {
                            		logger.debug("HBL::InfinityUserManagementResourceImpl: MB-validation1:max TX limit > dailylimit OR dailylimit > Weekly Limit");
                            		throw new ApplicationException(ErrorCodeEnum.ERR_10420);

                            	}

                            	if((mb_account_ADmaxtransactionlimit > mb_account_maxtransactionlimit) || (mb_account_ADdailylimit > mb_account_dailylimit) || 
                            			(account_ADweeklylimit > account_weeklylimit)) {
                            		logger.debug("HBL::InfinityUserManagementResourceImpl:MB-validation2: autoDenay max tx limit > max tx limit OR autoDenay Daily limit > Daily Limit");
                            		throw new ApplicationException(ErrorCodeEnum.ERR_10420);

                            	}

                            	if((mb_account_PAmaxtransactionlimit > mb_account_PAdailylimit) || (mb_account_PAdailylimit > mb_account_PAweeklylimit)) {  
                            		logger.debug("HBL::InfinityUserManagementResourceImpl:MB-validation3: preApproved  max tx limit > preApproved Daily limit  OR preApproved Daily Limit > preApproved weekly limit");
                            		throw new ApplicationException(ErrorCodeEnum.ERR_10420);
                            	}

                            	if((mb_account_ADmaxtransactionlimit > mb_account_ADdailylimit) || (mb_account_ADdailylimit > mb_account_ADweeklylimit)) {
                            		logger.debug("HBL::InfinityUserManagementResourceImpl:MB-validation4: autoDenay max tx limit > autoDenay Daily limit");
                            		throw new ApplicationException(ErrorCodeEnum.ERR_10420);

                            	}

                            	if((mb_account_PAmaxtransactionlimit > mb_account_ADmaxtransactionlimit)|| (mb_account_PAdailylimit > mb_account_ADdailylimit) ||
                            			(account_PAweeklylimit > account_ADweeklylimit)) {  
                            		logger.debug("HBL::InfinityUserManagementResourceImpl:MB-validation5: preApproved  max tx limit > autoDenay max tx limit  OR preApproved Daily Limit > autoDenay Daily limit");
                            		throw new ApplicationException(ErrorCodeEnum.ERR_10420);

                            	}
                            }
                            catch (ApplicationException e) {
                            	logger.error("Invalid limits" + e.getMessage());                          	
                            	ErrorCodeEnum.ERR_10420.setErrorCode(result);
                                return false;    	
                            }

							for (int l = 0; l < limits.size(); l++) {

								JsonObject limit = limits.get(l).getAsJsonObject();

								Double limit1 = new Double(0);
								Double limit2 = new Double(0);

								limit1 = Double.parseDouble(limit.get(InfinityConstants.value).getAsString());
								String limitId = limit.get(InfinityConstants.id).getAsString();

								Double limit3 = null;
								if (isContractValidationRequired) {
									limit3 = loggedInUserLimits.get(cif).get(accountId).get(actionId).get(limitId);
								}
								if (limitId.equals(InfinityConstants.PRE_APPROVED_DAILY_LIMIT)
										|| limitId.equals(InfinityConstants.AUTO_DENIED_DAILY_LIMIT)
										|| limitId.equals(InfinityConstants.DAILY_LIMIT)) {
									limit2 = Double.parseDouble((limitMap.containsKey(InfinityConstants.DAILY_LIMIT)
											&& StringUtils.isNotBlank(limitMap.get(InfinityConstants.DAILY_LIMIT)))
													? limitMap.get(InfinityConstants.DAILY_LIMIT)
													: "0.0");
								} else if (limitId.equals(InfinityConstants.PRE_APPROVED_WEEKLY_LIMIT)
										|| limitId.equals(InfinityConstants.AUTO_DENIED_WEEKLY_LIMIT)
										|| limitId.equals(InfinityConstants.WEEKLY_LIMIT)) {
									limit2 = Double.parseDouble((limitMap.containsKey(InfinityConstants.WEEKLY_LIMIT)
											&& StringUtils.isNotBlank(limitMap.get(InfinityConstants.WEEKLY_LIMIT)))
													? limitMap.get(InfinityConstants.WEEKLY_LIMIT)
													: "0.0");
								} else if (limitId.equals(InfinityConstants.PRE_APPROVED_TRANSACTION_LIMIT)
										|| limitId.equals(InfinityConstants.AUTO_DENIED_TRANSACTION_LIMIT)
										|| limitId.equals(InfinityConstants.MAX_TRANSACTION_LIMIT)) {
									limit2 = Double
											.parseDouble((limitMap.containsKey(InfinityConstants.MAX_TRANSACTION_LIMIT)
													&& StringUtils.isNotBlank(
															limitMap.get(InfinityConstants.MAX_TRANSACTION_LIMIT)))
																	? limitMap.get(
																			InfinityConstants.MAX_TRANSACTION_LIMIT)
																	: "0.0");
								}
								
								//MB Limits 
								
								else if (limitId.equals(HBLConstants.PRE_APPROVED_MB_DAILY_LIMIT)
										|| limitId.equals(HBLConstants.AUTO_DENIED_MB_DAILY_LIMIT)
										|| limitId.equals(HBLConstants.MB_DAILY_LIMIT)) {
									limit2 = Double.parseDouble((limitMap.containsKey(HBLConstants.MB_DAILY_LIMIT)
											&& StringUtils.isNotBlank(limitMap.get(HBLConstants.MB_DAILY_LIMIT)))
													? limitMap.get(HBLConstants.MB_DAILY_LIMIT)
													: "0.0");
								} else if (limitId.equals(HBLConstants.PRE_APPROVED_MB_WEEKLY_LIMIT)
										|| limitId.equals(HBLConstants.AUTO_DENIED_MB_WEEKLY_LIMIT)
										|| limitId.equals(HBLConstants.MB_WEEKLY_LIMIT)) {
									limit2 = Double.parseDouble((limitMap.containsKey(HBLConstants.MB_WEEKLY_LIMIT)
											&& StringUtils.isNotBlank(limitMap.get(HBLConstants.MB_WEEKLY_LIMIT)))
													? limitMap.get(HBLConstants.MB_WEEKLY_LIMIT)
													: "0.0");
								} else if (limitId.equals(HBLConstants.PRE_APPROVED_MB_TRANSACTION_LIMIT)
										|| limitId.equals(HBLConstants.AUTO_DENIED_MB_TRANSACTION_LIMIT)
										|| limitId.equals(HBLConstants.MB_MAX_TRANSACTION_LIMIT)) {
									limit2 = Double
											.parseDouble((limitMap.containsKey(HBLConstants.MB_MAX_TRANSACTION_LIMIT)
													&& StringUtils.isNotBlank(
															limitMap.get(HBLConstants.MB_MAX_TRANSACTION_LIMIT)))
																	? limitMap.get(
																			HBLConstants.MB_MAX_TRANSACTION_LIMIT)
																	: "0.0");
								}


								if (limit1 < limit2) {
									limit.addProperty(InfinityConstants.value,
											limit3 != null ? Math.min(limit1, limit3) + "" : limit1.toString());
								} else {
									limit.addProperty(InfinityConstants.value,
											limit3 != null ? Math.min(limit2, limit3) + "" : limit2.toString());
								}
							}
						}
					}
				}
			}

			jsonObject.add(InfinityConstants.transactionLimits, transactionLimitsArray);
		}

		return true;
	}
	private void getAccountsForContract(String contractId, Map<String, Map<String, Set<String>>> contractAccounts,
			Map<String, Object> headerMap) {

		if (!contractAccounts.containsKey(contractId)) {
			Map<String, Object> map = new HashMap<String, Object>();
			String filter = InfinityConstants.contractId + DBPUtilitiesConstants.EQUAL + contractId;

			map.put(DBPUtilitiesConstants.FILTER, filter);

			JsonObject jsonObject = ServiceCallHelper.invokeServiceAndGetJson(map, headerMap,
					URLConstants.CONTRACTACCOUNT_GET);

			if (jsonObject.has(DBPDatasetConstants.DATASET_CONTRACTACCOUNT)) {
				JsonElement jsonElement = jsonObject.get(DBPDatasetConstants.DATASET_CONTRACTACCOUNT);
				if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
					JsonArray jsonArray = jsonElement.getAsJsonArray();
					for (int i = 0; i < jsonArray.size(); i++) {
						JsonObject account = jsonArray.get(i).getAsJsonObject();

						String coreCustomerId = account.has(InfinityConstants.coreCustomerId)
								&& !account.get(InfinityConstants.coreCustomerId).isJsonNull()
										? account.get(InfinityConstants.coreCustomerId).getAsString()
										: null;

						if (!contractAccounts.containsKey(contractId)) {
							contractAccounts.put(contractId, new HashMap<String, Set<String>>());
						}

						if (!contractAccounts.get(contractId).containsKey(coreCustomerId)) {
							contractAccounts.get(contractId).put(coreCustomerId, new HashSet<String>());
						}
						contractAccounts.get(contractId).get(coreCustomerId)
								.add(account.get(InfinityConstants.accountId).getAsString());
					}
				}
			}
		}
	}
	
	private void getContractCIFs(String contractId, Map<String, Set<String>> contractCIFs,
			Map<String, Object> headerMap) {

		if (!contractCIFs.containsKey(contractId)) {
			String filter = InfinityConstants.contractId + DBPUtilitiesConstants.EQUAL + contractId;
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(DBPUtilitiesConstants.FILTER, filter);
			JsonObject jsonObject = ServiceCallHelper.invokeServiceAndGetJson(map, headerMap,
					URLConstants.CONTRACTCORECUSTOMER_GET);
			if (jsonObject.has(DBPDatasetConstants.CONTRACT_CORE_CUSTOMERS)) {
				JsonElement jsonElement = jsonObject.get(DBPDatasetConstants.CONTRACT_CORE_CUSTOMERS);
				if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
					JsonArray jsonArray = jsonElement.getAsJsonArray();
					for (int i = 0; i < jsonArray.size(); i++) {
						JsonObject contractCoreCustomer = jsonArray.get(i).getAsJsonObject();
						contractId = contractCoreCustomer.has(InfinityConstants.contractId)
								&& !contractCoreCustomer.get(InfinityConstants.contractId).isJsonNull()
										? contractCoreCustomer.get(InfinityConstants.contractId).getAsString()
										: null;
						String coreCustomerId = contractCoreCustomer.has(InfinityConstants.coreCustomerId)
								&& !contractCoreCustomer.get(InfinityConstants.coreCustomerId).isJsonNull()
										? contractCoreCustomer.get(InfinityConstants.coreCustomerId).getAsString()
										: null;
						if (!contractCIFs.containsKey(contractId)) {
							contractCIFs.put(contractId, new HashSet<String>());
						}
						contractCIFs.get(contractId).add(coreCustomerId);
					}
				}
			}
		}
	}
	
	private boolean checkIsExistingLegalEntity(String id, String legalEntityId)
	{
		if(StringUtils.isBlank(id) && StringUtils.isBlank(legalEntityId))
			return false;
		CustomerLegalEntityDTO customerLegalEntityDTO = null;
		customerLegalEntityDTO = new CustomerLegalEntityDTO();
		customerLegalEntityDTO.setCustomer_id(id);
		customerLegalEntityDTO.setLegalEntityId(legalEntityId);

		boolean isExistingLegalEntity = false;
		List<CustomerLegalEntityDTO> customerLegalEntityIds = (List<CustomerLegalEntityDTO>) customerLegalEntityDTO
				.loadDTO();
		if (customerLegalEntityIds != null && !customerLegalEntityIds.isEmpty()) {
			for (CustomerLegalEntityDTO customerLegalEntityDTOs : customerLegalEntityIds) {
				if (legalEntityId.equalsIgnoreCase(customerLegalEntityDTOs.getLegalEntityId()))
					isExistingLegalEntity = true;
			}
		}
		return isExistingLegalEntity;
	}
	
	private void getLoggedInUserContracts(String customerId, Map<String, Set<String>> customerContracts,
			Map<String, Object> headerMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(customerId)) {
			String filter = InfinityConstants.customerId + DBPUtilitiesConstants.EQUAL + customerId;
			map.put(DBPUtilitiesConstants.FILTER, filter);
			JsonObject jsonObject = ServiceCallHelper.invokeServiceAndGetJson(map, headerMap,
					URLConstants.CONTRACT_CUSTOMERS_GET);

			if (jsonObject.has(DBPDatasetConstants.DATASET_CONTRACT_CUSTOMERS)) {
				JsonElement jsonElement = jsonObject.get(DBPDatasetConstants.DATASET_CONTRACT_CUSTOMERS);
				if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
					JsonArray jsonArray = jsonElement.getAsJsonArray();
					for (int i = 0; i < jsonArray.size(); i++) {
						JsonObject customerContract = jsonArray.get(i).getAsJsonObject();
						String contractId = customerContract.has(InfinityConstants.contractId)
								&& !customerContract.get(InfinityConstants.contractId).isJsonNull()
										? customerContract.get(InfinityConstants.contractId).getAsString()
										: null;

						String coreCustomerId = customerContract.has(InfinityConstants.coreCustomerId)
								&& !customerContract.get(InfinityConstants.coreCustomerId).isJsonNull()
										? customerContract.get(InfinityConstants.coreCustomerId).getAsString()
										: null;

						if (!customerContracts.containsKey(contractId)) {
							customerContracts.put(contractId, new HashSet<String>());
						}
						if (customerContracts.containsKey(contractId)
								&& !customerContracts.get(contractId).contains(coreCustomerId)) {
							customerContracts.get(contractId).add(coreCustomerId);
						}
					}
				}
			}
		}
	}
	
	private void getAccountsForCustomer(String customerId, Map<String, Map<String, Set<String>>> customerAccounts,
			Map<String, Object> headerMap) {

		Map<String, Object> map = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(customerId)) {
			String filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + customerId;
			map.put(DBPUtilitiesConstants.FILTER, filter);

			JsonObject jsonObject = ServiceCallHelper.invokeServiceAndGetJson(map, headerMap,
					URLConstants.CUSTOMERACCOUNTS_GET);

			if (jsonObject.has(DBPDatasetConstants.CUSTOMER_ACCOUNTS_DATASET)) {
				JsonElement jsonElement = jsonObject.get(DBPDatasetConstants.CUSTOMER_ACCOUNTS_DATASET);
				if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
					JsonArray jsonArray = jsonElement.getAsJsonArray();
					for (int i = 0; i < jsonArray.size(); i++) {
						JsonObject account = jsonArray.get(i).getAsJsonObject();
						String contractId = account.has(InfinityConstants.contractId)
								&& !account.get(InfinityConstants.contractId).isJsonNull()
										? account.get(InfinityConstants.contractId).getAsString()
										: null;
						String coreCustomerId = account.has(InfinityConstants.coreCustomerId)
								&& !account.get(InfinityConstants.coreCustomerId).isJsonNull()
										? account.get(InfinityConstants.coreCustomerId).getAsString()
										: null;

						if (!customerAccounts.containsKey(contractId)) {
							customerAccounts.put(contractId, new HashMap<String, Set<String>>());
						}

						if (!customerAccounts.get(contractId).containsKey(coreCustomerId)) {
							customerAccounts.get(contractId).put(coreCustomerId, new HashSet<String>());
						}
						customerAccounts.get(contractId).get(coreCustomerId)
								.add(account.get("Account_id").getAsString());
					}
				}
			}
		}

	}
	
	private void getLoggedInUserPermissions(String customerId, Map<String, Set<String>> loggedInUserPermisions,
			Map<String, Map<String, Map<String, Map<String, Double>>>> loggedInUserLimits,
			DataControllerRequest dcRequest) {

		String filter = InfinityConstants.Customer_id + DBPUtilitiesConstants.EQUAL + customerId;
		Map<String, Object> input = new HashMap<String, Object>();
		input.put(DBPUtilitiesConstants.FILTER, filter);
		JsonObject response = ServiceCallHelper.invokeServiceAndGetJson(input, dcRequest.getHeaderMap(),
				URLConstants.CUSTOMER_ACTION_LIMITS_GET);
		if (response.has(DBPDatasetConstants.DATASET_CUSTOMERACTION)) {
			JsonElement jsonElement = response.get(DBPDatasetConstants.DATASET_CUSTOMERACTION);
			if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
				JsonArray actionsArray = jsonElement.getAsJsonArray();
				for (JsonElement element : actionsArray) {
					String coreCustomerId = element.getAsJsonObject().get(InfinityConstants.coreCustomerId)
							.getAsString();
					if (!loggedInUserPermisions.containsKey(coreCustomerId)) {
						loggedInUserPermisions.put(coreCustomerId, new HashSet<String>());
						loggedInUserLimits.put(coreCustomerId, new HashMap<String, Map<String, Map<String, Double>>>());
					}

					String account = element.getAsJsonObject().has(InfinityConstants.Account_id)
							&& !element.getAsJsonObject().get(InfinityConstants.Account_id).isJsonNull()
									? element.getAsJsonObject().get(InfinityConstants.Account_id).getAsString()
									: "";

					if (StringUtils.isNotBlank(account)
							&& !loggedInUserLimits.get(coreCustomerId).containsKey(account)) {
						loggedInUserLimits.get(coreCustomerId).put(account, new HashMap<String, Map<String, Double>>());
					}

					String actionId = element.getAsJsonObject().has(InfinityConstants.Action_id)
							&& !element.getAsJsonObject().get(InfinityConstants.Action_id).isJsonNull()
									? element.getAsJsonObject().get(InfinityConstants.Action_id).getAsString()
									: "";
					loggedInUserPermisions.get(coreCustomerId).add(actionId);

					if (StringUtils.isNotBlank(account)
							&& !loggedInUserLimits.get(coreCustomerId).get(account).containsKey(actionId)) {
						loggedInUserLimits.get(coreCustomerId).get(account).put(actionId,
								new HashMap<String, Double>());
					}
					String limitType = element.getAsJsonObject().has(InfinityConstants.LimitType_id)
							&& !element.getAsJsonObject().get(InfinityConstants.LimitType_id).isJsonNull()
									? element.getAsJsonObject().get(InfinityConstants.LimitType_id).getAsString()
									: "";

					Double value = null;
					try {
						value = element.getAsJsonObject().has(InfinityConstants.value)
								&& !element.getAsJsonObject().get(InfinityConstants.value).isJsonNull()
										? Double.parseDouble(
												element.getAsJsonObject().get(InfinityConstants.value).getAsString())
										: 0.0;
					} catch (Exception e) {
					}

					if (StringUtils.isNotBlank(limitType) && !loggedInUserLimits.get(coreCustomerId).get(account)
							.get(actionId).containsKey(limitType)) {
						loggedInUserLimits.get(coreCustomerId).get(account).get(actionId).put(limitType, value);
					}
				}
			}
		}
	}
	
	private boolean checkIfContractCreationRequired(Map<String, String> map, Result result, String methodId,
			Object[] inputArray, DataControllerRequest request, DataControllerResponse response) {
		String contractDetails = map.containsKey(InfinityConstants.contractDetails)
				&& map.get(InfinityConstants.contractDetails) != null ? map.get(InfinityConstants.contractDetails)
						: new JsonArray().toString();
		JsonElement element = new JsonParser().parse(contractDetails);
		JsonObject contractJsonObject;// = JSONUtil.parseAsJsonObject(contractDetails);

		if (element.isJsonObject()) {
			contractJsonObject = element.getAsJsonObject();

			if (!contractJsonObject.isJsonNull()) {
				if (contractJsonObject.has(InfinityConstants.contractName)
						&& !contractJsonObject.get(InfinityConstants.contractName).isJsonNull()) {
					for (Entry<String, JsonElement> entry : contractJsonObject.entrySet()) {
						map.put(entry.getKey(), entry.getValue().getAsString());
					}

					ContractResource resource = DBPAPIAbstractFactoryImpl.getResource(ContractResource.class);
					logger.error("Contract creation started....");
					try {
						result = resource.createContract(methodId, inputArray, request, response);
					} catch (ApplicationException e) {
						logger.error("createContract exception", e);
						e.getErrorCodeEnum().setErrorCode(result);
					}

					if (!result.hasParamByName(InfinityConstants.contractId)
							|| StringUtils.isBlank(result.getParamValueByName(InfinityConstants.contractId))) {
						logger.error("Contract creation failed....");
						return false;
					}
					logger.debug("Contract creation completed....");

					String contractId = result.getParamValueByName(InfinityConstants.contractId);

					String companyList = map.get(InfinityConstants.companyList);

					JsonArray companyListArray = JSONUtil.parseAsJsonArray(companyList);

					if (!companyListArray.isJsonNull()) {
						for (int i = 0; i < companyListArray.size(); i++) {
							JsonObject company = companyListArray.get(i).getAsJsonObject();
							if (!company.has(InfinityConstants.contractId)
									|| company.get(InfinityConstants.contractId).isJsonNull()
									|| StringUtils.isBlank(company.get(InfinityConstants.contractId).getAsString())) {
								company.addProperty(InfinityConstants.contractId, contractId);
							}
						}
					}
					map.put(InfinityConstants.companyList, companyListArray.toString());
					String singatoryGroups = map.get(InfinityConstants.signatoryGroups);
					JsonArray signatoryGroupsArray = JSONUtil.parseAsJsonArray(singatoryGroups);
					if (!signatoryGroupsArray.isJsonNull()) {
						for (int i = 0; i < signatoryGroupsArray.size(); i++) {
							JsonObject sigGroup = signatoryGroupsArray.get(i).getAsJsonObject();
							if (sigGroup != null && !sigGroup.isJsonNull()
									&& (!sigGroup.has(InfinityConstants.contractId)
											|| sigGroup.get(InfinityConstants.contractId).isJsonNull()
											|| StringUtils.isBlank(
													sigGroup.get(InfinityConstants.contractId).getAsString()))) {
								sigGroup.addProperty(InfinityConstants.contractId, contractId);
							}
						}
					}
					map.put(InfinityConstants.signatoryGroups, signatoryGroupsArray.toString());
				}
			}
		}
		return true;

	}
	@Override
	public String createUser(CustomerDTO customerDTO, String createdServiceType, DataControllerRequest request)
			throws ApplicationException {
		UserManagementBusinessDelegate customerBD = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(UserManagementBusinessDelegate.class);

		String id = HelperMethods.generateUniqueCustomerId(request);
		String legalEntityId = request.getParameter("legalEntityId");
		String migratedUser = request.getParameter("migratedUser");
		customerDTO.setId(id);
		//customerDTO.setUserName(HelperMethods.generateUniqueUserName(request));
		customerDTO.setUserName(generateHBLUserName(request));
		customerDTO.setIsNew(true);
		customerDTO.setStatus_id(DBPUtilitiesConstants.CUSTOMER_STATUS_NEW);
		customerDTO.setCustomerType_id(createdServiceType);
		customerDTO.setCompanyLegalUnit(legalEntityId);
		logger.debug("HBL::InfinityUserManagementResourceImplExtn:createUser:customerDTO.getDateOfBirth():"+customerDTO.getDateOfBirth());
		if(StringUtils.isBlank(customerDTO.getDateOfBirth()))
			customerDTO.setDateOfBirth(null);
		if(StringUtils.isBlank(customerDTO.getiDExpiryDate()))
			customerDTO.setiDExpiryDate(null);
		if(StringUtils.isBlank(customerDTO.getiDIssueDate()))
			customerDTO.setiDIssueDate(null);
		if(StringUtils.isBlank(customerDTO.getGender()))
		customerDTO.setGender(null);
		if (LegalEntityUtil.isSingleEntity())
			customerDTO.setDefaultLegalEntity(legalEntityId);
		customerDTO.setHomeLegalEntity(legalEntityId);
		if (StringUtils.isNotBlank(migratedUser) && migratedUser.equalsIgnoreCase("true"))
			customerDTO.setIs_BBOA(true);

		DBXResult customerResult = customerBD.update(customerDTO, request.getHeaderMap());
		String customerId = (String) customerResult.getResponse();
		if (StringUtils.isBlank(customerId)) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10386);
		}
		
		CustomerLegalEntityDTO customerLegalEntityDTO = new CustomerLegalEntityDTO();
		customerLegalEntityDTO.setId(HelperMethods.getNewId());
		customerLegalEntityDTO.setCustomer_id(customerDTO.getId());
		customerLegalEntityDTO.setNew(true);
		customerLegalEntityDTO.setStatus_id(HelperMethods.getCustomerStatus().get("NEW"));
		customerLegalEntityDTO.setLegalEntityId(legalEntityId);
		customerLegalEntityDTO.persist(DTOUtils.getParameterMap(customerLegalEntityDTO, true), request.getHeaderMap());

		return customerId;
	}
	private String generateHBLUserName(DataControllerRequest request) {
		String username=HelperMethods.generateUniqueUserName(request);
		String coreCustomerId=request.getParameter("coreCustomerId");
		logger.debug("HBL::InfinityUserManagementResourceImplExtn:createUser:coreCustomerId:"+coreCustomerId);
		if(StringUtils.isNotBlank(coreCustomerId)) {
			String charM="M";
			int coreId=Integer.valueOf(coreCustomerId);
			String format = String.format("%07d", coreId);
			coreCustomerId=charM+format;
			username=coreCustomerId;
		}
		return username;
		
	}
	 private Map<String, String> getIdentityType() {
	        Map<String, String> idTypeMap = new HashMap<String, String>();
	        idTypeMap.put("DRIVERS.LICENSE", "ID_DRIVING_LICENSE");
	        idTypeMap.put("PASSPORT", "ID_PASSPORT");
	        idTypeMap.put("MILITARY.ID", "Military ID");
	        idTypeMap.put("NATIONAL.ID", "ID State");
	        idTypeMap.put("FED.GOVT.ID", "Foreign Government ID");
	        
	        idTypeMap.put("DRIVING.LICENSE", "ID_DRIVING_LICENSE");
	        idTypeMap.put("ADHAR.CARD", "ID_AADHAR_CARD");
	        idTypeMap.put("BIRTH.CERT", "ID_BIRTH_CERTIFICATE");
	        idTypeMap.put("CITIZEN.CERT", "ID_CITIZEN_CERTIFICATE");
	        idTypeMap.put("FCY.LICENSE.VISA.EXPIRY", "ID_FCY_LICENSE/VISA_EXPIRY");
	        idTypeMap.put("INCORP.CERT", "ID_INCORPORATION_CERTIFICATE");
	        idTypeMap.put("IND.EMB.CERT", "ID_INDIAN_EMBASY_CERTIFICATE");
	        idTypeMap.put("LEI", "ID_LEGAL_ENTITY_IDENTIFIER");
	        idTypeMap.put("NATIONAL.ID", "ID_NATIONAL_ID");
	        idTypeMap.put("PAN", "ID_PAN_NO");
	        idTypeMap.put("REFUGEE.CERT", "ID_REFEGUEES_CERTIFICATE");
	        idTypeMap.put("REG.CERTIFICATE", "ID_REGISTERED_CERTIFICATE");
	        idTypeMap.put("SCHOOL.CAMP", "ID_SCHOOL_ID/CAMPUS_ID");
	        idTypeMap.put("SOCIAL.SECURITY.NO", "ID_SOCAIL_SECURITY_NO");
	        idTypeMap.put("VAT.ID", "ID_VAT_ID");
	        idTypeMap.put("VOTER.ID", "ID_VOTERS_ID");
	        return idTypeMap;

	    }



}
