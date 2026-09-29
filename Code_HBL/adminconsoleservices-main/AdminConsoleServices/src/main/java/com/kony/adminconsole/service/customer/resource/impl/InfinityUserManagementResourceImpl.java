package com.kony.adminconsole.service.customer.resource.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.error.DBPApplicationException;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.core.security.LoggedInUserHandler;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.exception.DBPAuthenticationException;
import com.kony.adminconsole.handler.AuditHandler;
import com.kony.adminconsole.service.customer.businessdelegate.api.InfinityUserManagementBusinessDelegate;
import com.kony.adminconsole.service.customer.resource.api.InfinityUserManagementResource;
import com.kony.adminconsole.utilities.ActivityStatusEnum;
import com.kony.adminconsole.utilities.DBPServices;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.EventEnum;
import com.kony.adminconsole.utilities.ModuleNameEnum;
import com.kony.adminconsole.utilities.PermissionName;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;


/**
 * @author Rishi.Gupta
 *
 */
public class InfinityUserManagementResourceImpl implements InfinityUserManagementResource {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
	private static final String INPUT_ID = "id";
	private static final String INPUT_USER_DETAILS = "userDetails";
	private static final String INPUT_COMPANY_LIST = "companyList";
	private static final String INPUT_ACCOUNT_LEVEL_PERMISSIONS = "accountLevelPermissions";
	private static final String INPUT_GLOBAL_LEVEL_PERMISSIONS = "globalLevelPermissions";
	private static final String INPUT_TRANSACTION_LIMITS = "transactionLimits";
	private static final String INPUT_CORE_CUSTOMER_ID = "coreCustomerId";
	private static final String INPUT_CORE_CUSTOMER_ROLE_LIST = "coreCustomerRoleIdList";
	private static final String INPUT_CONTRACT_DETAILS = "contractDetails";
    private static final String INPUT_USERID = "userId";
    private static final String INPUT_CONTRACTID = "contractId";
    private static final String INPUT_REMOVED_COMPANIES = "removedCompanies";
    private static final String INPUT_SIGNATORY_GROUP = "signatoryGroups";
    private static final String INPUT_LEGALENTITYID = "legalEntityId";
    private static final String INFINITY_ACCESS = "infinityAccess";

	InfinityUserManagementBusinessDelegate infinityUserManagementBusinessDelegate = DBPAPIAbstractFactoryImpl
			.getBusinessDelegate(InfinityUserManagementBusinessDelegate.class);

	@Override
	public Result getInfinityUser(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {

		Result result = new Result();
		try {
			
			if (StringUtils.isBlank(request.getParameter("legalEntityId"))) {
                alert.prepareError("legalEntityId cannot be empty").log();
                ErrorCodeEnum.ERR_22232.setErrorCode(result);
                return result;
            }
            String[] reqPermissions = {PermissionName.VIEW_CUSTOMER};
            if(!LoggedInUserHandler.hasAccessToLegalEntity(request,reqPermissions))
            {
                result.addParam(new Param("Status", "Infinity User Manage Service failed", FabricConstants.STRING));
                ErrorCodeEnum.ERR_22231.setErrorCode(result);
                alert.prepareError("Logged in user do not have access to this legalEntity ").log();
                return result;        
            }
			
			if (request.getParameter(INPUT_ID) == null) {
				ErrorCodeEnum.ERR_21991.setErrorCode(result);
				return result;
			} else {
				String userId = request.getParameter(INPUT_ID);
				String contractId = request.getParameter(INPUT_CONTRACTID);
				String coreCustomerId = request.getParameter(INPUT_CORE_CUSTOMER_ID);
				String legalEntityId = request.getParameter(INPUT_LEGALENTITYID);
				Map<String, Object> postParametersMap = new HashMap<>();
				postParametersMap.put("id", userId);
				postParametersMap.put("contractId", contractId);
				postParametersMap.put("coreCustomerId", coreCustomerId);
				postParametersMap.put("legalEntityId", legalEntityId);
				String dbpServicesClaimsToken = DBPServices.getDBPServicesClaimsToken(request);
				JSONObject getCustomerresponse = infinityUserManagementBusinessDelegate
						.getInfinityUser(postParametersMap, dbpServicesClaimsToken);
				if (getCustomerresponse == null || !getCustomerresponse.has(FabricConstants.OPSTATUS)
						|| getCustomerresponse.getInt(FabricConstants.OPSTATUS) != 0) {
					ErrorCodeEnum.ERR_21992.setErrorCode(result);
					result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					AuditHandler.auditAdminActivity(request, ModuleNameEnum.CUSTOMERS, EventEnum.SEARCH,
							ActivityStatusEnum.FAILED, "Get infinity user failed");
					return result;
				} else if (getCustomerresponse.has("dbpErrMsg")) {
					result = CommonUtilities.constructResultFromJSONObject(getCustomerresponse);
	            	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					return result;
				} else {
					
					result = CommonUtilities.constructResultFromJSONObject(getCustomerresponse);

					AuditHandler.auditAdminActivity(request, ModuleNameEnum.CUSTOMERS, EventEnum.SEARCH,
							ActivityStatusEnum.SUCCESSFUL, "Successfully fetched infinity user details. id= " + userId);

				}
			}
		} catch (Exception e) {
			alert.prepareError("Unexepected Error in get infinity user details", e).log();
			result.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
			ErrorCodeEnum.ERR_21993.setErrorCode(result);
			AuditHandler.auditAdminActivity(request, ModuleNameEnum.CUSTOMERS, EventEnum.SEARCH,
					ActivityStatusEnum.FAILED, "Infinity user details parse Failed");
		}
		return result;
	}

	@Override
	public Result createInfinityUser(String methodId, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse response) {

		Result result = new Result();
		try {

			if (requestInstance.getParameter(INPUT_USER_DETAILS) == null) {
				ErrorCodeEnum.ERR_21994.setErrorCode(result);
				return result;
			} else if (requestInstance.getParameter(INPUT_COMPANY_LIST) == null) {
				ErrorCodeEnum.ERR_21995.setErrorCode(result);
				return result;
			} else {
				String userDetails = requestInstance.getParameter(INPUT_USER_DETAILS);
				String companyList = requestInstance.getParameter(INPUT_COMPANY_LIST);
				String accountLevelPermissions = requestInstance.getParameter(INPUT_ACCOUNT_LEVEL_PERMISSIONS);
				String globalLevelPermissions = requestInstance.getParameter(INPUT_GLOBAL_LEVEL_PERMISSIONS);
				String transactionLimits = requestInstance.getParameter(INPUT_TRANSACTION_LIMITS);
				String contractDetails = requestInstance.getParameter(INPUT_CONTRACT_DETAILS);
				String signatoryGroups = requestInstance.getParameter(INPUT_SIGNATORY_GROUP);
				
								
				String legalEntityId = validateLegalEntityIdFromPayload(requestInstance,methodId);
				if(legalEntityId == "")	
				{
					ErrorCodeEnum.ERR_22230.setErrorCode(result);
					return result;
				}
				if(legalEntityId == null)
				{
					ErrorCodeEnum.ERR_22197.setErrorCode(result);
					return result;
				}
							
				requestInstance.addRequestParam_("legalEntityId", legalEntityId);
				String[] reqPermissions = {PermissionName.CREATE_INFINITY_USER};
				if(!LoggedInUserHandler.hasAccessToLegalEntity(requestInstance,reqPermissions))
	            {
	                result.addParam(new Param("Status", "createInfinityUser operation failed", FabricConstants.STRING));
	                ErrorCodeEnum.ERR_22231.setErrorCode(result);
	                alert.prepareError("Logged in user do not have access to this legalEntity ").log();
	                return result;        
	            }
				
				Map<String, Object> postParametersMap = new HashMap<>();
				postParametersMap.put("userDetails", stringifyForVelocityTemplate(userDetails));
				postParametersMap.put("companyList", stringifyForVelocityTemplate(companyList));
				postParametersMap.put("accountLevelPermissions", stringifyForVelocityTemplate(accountLevelPermissions));
				postParametersMap.put("globalLevelPermissions", stringifyForVelocityTemplate(globalLevelPermissions));
				postParametersMap.put("transactionLimits", stringifyForVelocityTemplate(transactionLimits));
				postParametersMap.put("contractDetails", stringifyForVelocityTemplate(contractDetails));
				postParametersMap.put("signatoryGroups", stringifyForVelocityTemplate(signatoryGroups));
				
				String dbpServicesClaimsToken = DBPServices.getDBPServicesClaimsToken(requestInstance);
				JSONObject createInfinityUserResponse = infinityUserManagementBusinessDelegate
						.createInfinityUser(postParametersMap, dbpServicesClaimsToken);
				if (createInfinityUserResponse == null || !createInfinityUserResponse.has(FabricConstants.OPSTATUS)
						|| createInfinityUserResponse.getInt(FabricConstants.OPSTATUS) != 0) {
					ErrorCodeEnum.ERR_21996.setErrorCode(result);
					result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.CUSTOMERS, EventEnum.CREATE,
							ActivityStatusEnum.FAILED, "Infinity user creation failed");
					return result;
				} else if (createInfinityUserResponse.has("dbpErrMsg")) {
					result = CommonUtilities.constructResultFromJSONObject(createInfinityUserResponse);
	            	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					return result;
				} else {
					result.addParam(new Param("status", "Success", FabricConstants.STRING));
					result.addParam(new Param("opstatus", createInfinityUserResponse.get("opstatus").toString(),
							FabricConstants.STRING));
					result.addParam(
							new Param("id", createInfinityUserResponse.getString("id"), FabricConstants.STRING));
				}
			}
		} catch (Exception e) {
			alert.prepareError("Unexpected Error in create infinity user: ", e).log();
			result.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
			ErrorCodeEnum.ERR_21996.setErrorCode(result);
		}
		return result;
	}

	@Override
	public Result getAssociatedCustomers(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse dcResponse) throws ApplicationException {

		Result result = new Result();
		Map<String, Object> postParametersMap = new HashMap<>();
		// postParametersMap.put("customerId", customerId);
		try {
			String dbpServicesClaimsToken = DBPServices.getDBPServicesClaimsToken(request);
			JSONObject getCustomerresponse = infinityUserManagementBusinessDelegate
					.getAssociatedCustomers(postParametersMap, dbpServicesClaimsToken);
		} catch (DBPAuthenticationException e1) {
			// TODO Auto-generated catch block
			alert.prepareError("Error occurred: ", e1).log();
		} catch (DBPApplicationException e) {
			// TODO Auto-generated catch block
			alert.prepareError("Error occurred: ", e).log();
		}

		return result;

	}

	@Override
	public Result editInfinityUser(String methodId, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse response) {
		Result result = new Result();
		try {
			if (requestInstance.getParameter(INPUT_USER_DETAILS) == null) {
				ErrorCodeEnum.ERR_21994.setErrorCode(result);
				return result;
			} else if (requestInstance.getParameter(INPUT_COMPANY_LIST) == null) {
				ErrorCodeEnum.ERR_21995.setErrorCode(result);
				return result;
			} else {
				String userDetails = requestInstance.getParameter(INPUT_USER_DETAILS);
				String companyList = requestInstance.getParameter(INPUT_COMPANY_LIST);
				String accountLevelPermissions = requestInstance.getParameter(INPUT_ACCOUNT_LEVEL_PERMISSIONS);
				String globalLevelPermissions = requestInstance.getParameter(INPUT_GLOBAL_LEVEL_PERMISSIONS);
				String transactionLimits = requestInstance.getParameter(INPUT_TRANSACTION_LIMITS);
				String removedCompanies = requestInstance.getParameter(INPUT_REMOVED_COMPANIES);
				String signatoryGroups = requestInstance.getParameter(INPUT_SIGNATORY_GROUP);
				String contractDetails = requestInstance.getParameter(INPUT_CONTRACT_DETAILS);
				String infinityAccess = requestInstance.getParameter(INFINITY_ACCESS);
				
				diagnostic.debug("infinityAccess Value in editInfinityUser #"+ infinityAccess);
				
				String  legalEntityId =  validateLegalEntityIdFromPayload(requestInstance,methodId);
				
								
				if(legalEntityId == "") {
					ErrorCodeEnum.ERR_22230.setErrorCode(result);
					return result;
				}
				if(legalEntityId == null)
				{
					ErrorCodeEnum.ERR_22197.setErrorCode(result);
					return result;
				}
				
				requestInstance.addRequestParam_("legalEntityId", legalEntityId);
				String[] reqPermissions = {PermissionName.EDIT_INFINTIY_USER};
				if(!LoggedInUserHandler.hasAccessToLegalEntity(requestInstance,reqPermissions))
	            {
	                result.addParam(new Param("Status", "editInfinityUser operation failed", FabricConstants.STRING));
	                ErrorCodeEnum.ERR_22231.setErrorCode(result);
	                alert.prepareError("Logged in user do not have access to this legalEntity ").log();
	                return result;        
	            }
							
				Map<String, Object> postParametersMap = new HashMap<>();
				postParametersMap.put("userDetails", stringifyForVelocityTemplate(userDetails));
				postParametersMap.put("companyList", stringifyForVelocityTemplate(companyList));
				postParametersMap.put("accountLevelPermissions", stringifyForVelocityTemplate(accountLevelPermissions));
				postParametersMap.put("globalLevelPermissions", stringifyForVelocityTemplate(globalLevelPermissions));
				postParametersMap.put("transactionLimits", stringifyForVelocityTemplate(transactionLimits));
				postParametersMap.put("removedCompanies", stringifyForVelocityTemplate(removedCompanies));
				postParametersMap.put("signatoryGroups", stringifyForVelocityTemplate(signatoryGroups));
				postParametersMap.put("contractDetails", stringifyForVelocityTemplate(contractDetails));
				postParametersMap.put("infinityAccess", stringifyForVelocityTemplate(infinityAccess));
				
				diagnostic.debug("postParametersMap Value in editInfinityUser #"+ postParametersMap);
				
				String dbpServicesClaimsToken = DBPServices.getDBPServicesClaimsToken(requestInstance);
				JSONObject editInfinityUserResponse = infinityUserManagementBusinessDelegate
						.editInfinityUser(postParametersMap, dbpServicesClaimsToken);

				if (editInfinityUserResponse == null || !editInfinityUserResponse.has(FabricConstants.OPSTATUS)
						|| editInfinityUserResponse.getInt(FabricConstants.OPSTATUS) != 0) {
					ErrorCodeEnum.ERR_21997.setErrorCode(result);
					result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.CUSTOMERS, EventEnum.UPDATE,
							ActivityStatusEnum.FAILED, "Infinity user edit failed");
					return result;
				} else if (editInfinityUserResponse.has("dbpErrMsg")) {
					result = CommonUtilities.constructResultFromJSONObject(editInfinityUserResponse);
	            	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					return result;
				} else {

					result = CommonUtilities.constructResultFromJSONObject(editInfinityUserResponse);
				}
			}
		} catch (Exception e) {
			alert.prepareError("Unexpected Error in edit infinity user: ", e).log();
			result.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
			ErrorCodeEnum.ERR_21997.setErrorCode(result);

		}

		return result;
	}

	@Override
	public Result getAllEligibleRelationalCustomers(String methodId, Object[] inputArray,
			DataControllerRequest requestInstance, DataControllerResponse response) {

		Result result = new Result();
		String coreCustomerId = StringUtils.EMPTY;
		String legalEntityId = requestInstance.getParameter(INPUT_LEGALENTITYID);

		try {
			
			   if (StringUtils.isBlank(requestInstance.getParameter("legalEntityId"))) {
	                alert.prepareError("legalEntityId cannot be empty").log();
	                ErrorCodeEnum.ERR_22232.setErrorCode(result);
	                return result;
	            }
	            String[] reqPermissions = {PermissionName.VIEW_CUSTOMER};
	            if(!LoggedInUserHandler.hasAccessToLegalEntity(requestInstance,reqPermissions))
	            {
	                result.addParam(new Param("Status", "Get All Eligible Relational Customers Operation failed", FabricConstants.STRING));
	                ErrorCodeEnum.ERR_22231.setErrorCode(result);
	                alert.prepareError("Logged in user do not have access to this legalEntity ").log();
	                return result;        
	            }

			if (StringUtils.isBlank(requestInstance.getParameter(INPUT_CORE_CUSTOMER_ID))) {
				ErrorCodeEnum.ERR_22026.setErrorCode(result);
				return result;
			}
			
			if(StringUtils.isBlank(legalEntityId)) {
				ErrorCodeEnum.ERR_22230.setErrorCode(result);
				return result;
			}
			String dbpServicesClaimsToken = DBPServices.getDBPServicesClaimsToken(requestInstance);
			coreCustomerId = requestInstance.getParameter(INPUT_CORE_CUSTOMER_ID);
			Map<String, Object> postParametersMap = new HashMap<>();
			postParametersMap.put("coreCustomerId", coreCustomerId);
			postParametersMap.put("legalEntityId", legalEntityId);
			JSONObject serviceResponse = infinityUserManagementBusinessDelegate
					.getAllEligibleRelationalCustomers(postParametersMap, dbpServicesClaimsToken);
			if (serviceResponse == null || !serviceResponse.has(FabricConstants.OPSTATUS)
					|| serviceResponse.getInt(FabricConstants.OPSTATUS) != 0) {
				ErrorCodeEnum.ERR_22025.setErrorCode(result);
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.CONTRACTS, EventEnum.UPDATE,
						ActivityStatusEnum.FAILED,
						"Failed to fetch AllEligibleRelationalCustomers for coreCustomerId : " + coreCustomerId);
				return result;
			} else if (serviceResponse.has("dbpErrMsg")) {
				result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return result;
			} else {

				result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
			}
		} catch (Exception e) {
			alert.prepareError("Unexpected Error in getAllEligibleRelationalCustomers", e).log();
			result.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
			ErrorCodeEnum.ERR_22025.setErrorCode(result);
			AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.CONTRACTS, EventEnum.SEARCH,
					ActivityStatusEnum.FAILED,
					"Failed to fetch AllEligibleRelationalCustomers for coreCustomerId : " + coreCustomerId);
		}

		return result;
	}

	@Override
	public Result getCoreCustomerRoleFeatureActionLimits(String methodId, Object[] inputArray,
			DataControllerRequest requestInstance, DataControllerResponse response) {

		Result result = new Result();
		String coreCustomerRoleIdList = StringUtils.EMPTY;
		String legalEntityId = requestInstance.getParameter(INPUT_LEGALENTITYID);

		try {

			if (StringUtils.isBlank(requestInstance.getParameter(INPUT_CORE_CUSTOMER_ROLE_LIST))) {
				ErrorCodeEnum.ERR_22028.setErrorCode(result);
				return result;
			}
			if (StringUtils.isBlank(requestInstance.getParameter(INPUT_LEGALENTITYID))) {
				ErrorCodeEnum.ERR_22230.setErrorCode(result);
				return result;
			}
			String[] reqPermissions = {PermissionName.VIEW_CUSTOMER};
			if(!LoggedInUserHandler.hasAccessToLegalEntity(requestInstance,reqPermissions))
			{
				result.addParam(new Param("Status", "Get core customer role feature action limits operation failed", FabricConstants.STRING));
				ErrorCodeEnum.ERR_22231.setErrorCode(result);
				alert.prepareError("Logged in user do not have access to this legalEntity ").log();
				return result;
				
			}
			
			String dbpServicesClaimsToken = DBPServices.getDBPServicesClaimsToken(requestInstance);
			Map<String, Object> postParametersMap = new HashMap<>();
			coreCustomerRoleIdList = requestInstance.getParameter(INPUT_CORE_CUSTOMER_ROLE_LIST).toString();
			coreCustomerRoleIdList = "\"" + coreCustomerRoleIdList.replace("\"", "\\\"") + "\"";
			postParametersMap.put("coreCustomerRoleIdList", coreCustomerRoleIdList);
			postParametersMap.put("legalEntityId", legalEntityId);
			JSONObject serviceResponse = infinityUserManagementBusinessDelegate
					.getCoreCustomerRoleFeatureActionLimits(postParametersMap, dbpServicesClaimsToken);
			if (serviceResponse == null || !serviceResponse.has(FabricConstants.OPSTATUS)
					|| serviceResponse.getInt(FabricConstants.OPSTATUS) != 0) {
				ErrorCodeEnum.ERR_22027.setErrorCode(result);
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.CONTRACTS, EventEnum.UPDATE,
						ActivityStatusEnum.FAILED,
						"Failed to fetch CoreCustomerRoleFeatureActionLimits for coreCustomerRoleIdList : "
								+ coreCustomerRoleIdList);
				return result;
			} else if (serviceResponse.has("dbpErrMsg")) {
				result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return result;
			} else {

				result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
			}
		} catch (Exception e) {
			alert.prepareError("Unexpected Error in getCoreCustomerRoleFeatureActionLimits", e).log();
			result.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
			ErrorCodeEnum.ERR_22027.setErrorCode(result);
			AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.CONTRACTS, EventEnum.SEARCH,
					ActivityStatusEnum.FAILED,
					"Failed to fetch CoreCustomerRoleFeatureActionLimits for coreCustomerRoleIdList : "
							+ coreCustomerRoleIdList);
		}

		return result;
	}
	
	@Override
	public Result getCoreCustomerProductRolesFeatureActionLimits(String methodId, Object[] inputArray,
			DataControllerRequest requestInstance, DataControllerResponse response) {

		Result result = new Result();
		String coreCustomerRoleIdList = StringUtils.EMPTY;
		String legalEntityId = requestInstance.getParameter(INPUT_LEGALENTITYID);

		try {

			if (StringUtils.isBlank(requestInstance.getParameter(INPUT_CORE_CUSTOMER_ROLE_LIST))) {
				ErrorCodeEnum.ERR_22028.setErrorCode(result);
				return result;
			}
			if (StringUtils.isBlank(requestInstance.getParameter(INPUT_LEGALENTITYID))) {
				ErrorCodeEnum.ERR_22230.setErrorCode(result);
				return result;
			}
			String dbpServicesClaimsToken = DBPServices.getDBPServicesClaimsToken(requestInstance);
			Map<String, Object> postParametersMap = new HashMap<>();
			coreCustomerRoleIdList = requestInstance.getParameter(INPUT_CORE_CUSTOMER_ROLE_LIST).toString();
			coreCustomerRoleIdList = "\"" + coreCustomerRoleIdList.replace("\"", "\\\"") + "\"";
			postParametersMap.put("coreCustomerRoleIdList", coreCustomerRoleIdList);
			postParametersMap.put("legalEntityId", legalEntityId);
			JSONObject serviceResponse = infinityUserManagementBusinessDelegate
					.getCoreCustomerProductRolesFeatureActionLimits(postParametersMap, dbpServicesClaimsToken);
			if (serviceResponse == null || !serviceResponse.has(FabricConstants.OPSTATUS)
					|| serviceResponse.getInt(FabricConstants.OPSTATUS) != 0) {
				ErrorCodeEnum.ERR_22027.setErrorCode(result);
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.CONTRACTS, EventEnum.UPDATE,
						ActivityStatusEnum.FAILED,
						"Failed to fetch CoreCustomerProductRolesFeatureActionLimits for coreCustomerRoleIdList : "
								+ coreCustomerRoleIdList);
				return result;
			} else if (serviceResponse.has("dbpErrMsg")) {
				result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return result;
			} else {

				result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
			}
		} catch (Exception e) {
			alert.prepareError("Unexpected Error in getCoreCustomerProductRolesFeatureActionLimits", e).log();
			result.addParam(new Param("FailureReason", "Unexpected error", FabricConstants.STRING));
			ErrorCodeEnum.ERR_22027.setErrorCode(result);
			AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.CONTRACTS, EventEnum.SEARCH,
					ActivityStatusEnum.FAILED,
					"Failed to fetch CoreCustomerProductRolesFeatureActionLimits for coreCustomerRoleIdList : "
							+ coreCustomerRoleIdList);
		}

		return result;
	}
	
	@Override
	public Result getRelativeCoreCustomerContractDetails(String methodId, Object[] inputArray,
			DataControllerRequest requestInstance, DataControllerResponse response) {

		Result result = new Result();
		String coreCustomerId = StringUtils.EMPTY;
		String legalEntityId = StringUtils.EMPTY;

		try {

			if (StringUtils.isBlank(requestInstance.getParameter(INPUT_CORE_CUSTOMER_ID))) {
				ErrorCodeEnum.ERR_22029.setErrorCode(result);
				return result;
			}
			if (StringUtils.isBlank(requestInstance.getParameter(INPUT_LEGALENTITYID))) {
				ErrorCodeEnum.ERR_22230.setErrorCode(result);
				return result;
			}
			String dbpServicesClaimsToken = DBPServices.getDBPServicesClaimsToken(requestInstance);
			coreCustomerId = requestInstance.getParameter(INPUT_CORE_CUSTOMER_ID);
			legalEntityId = requestInstance.getParameter(INPUT_LEGALENTITYID);
			Map<String, Object> postParametersMap = new HashMap<>();
			postParametersMap.put("coreCustomerId", coreCustomerId);
			postParametersMap.put("legalEntityId",legalEntityId);
			JSONObject serviceResponse = infinityUserManagementBusinessDelegate
					.getRelativeCoreCustomerContractDetails(postParametersMap, dbpServicesClaimsToken);
			if (serviceResponse == null || !serviceResponse.has(FabricConstants.OPSTATUS)
					|| serviceResponse.getInt(FabricConstants.OPSTATUS) != 0) {
				ErrorCodeEnum.ERR_22029.setErrorCode(result);
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.CONTRACTS, EventEnum.UPDATE,
						ActivityStatusEnum.FAILED,
						"Failed to fetch RelativeCoreCustomerContractDetails for coreCustomerId : " + coreCustomerId);
				return result;
			} else if (serviceResponse.has("dbpErrMsg")) {
				result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return result;
			} else {

				result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
			}
		} catch (Exception e) {
			alert.prepareError("Unexpected Error in getRelativeCoreCustomerContractDetails", e).log();
			result.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
			ErrorCodeEnum.ERR_22029.setErrorCode(result);
			AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.CONTRACTS, EventEnum.SEARCH,
					ActivityStatusEnum.FAILED,
					"Failed to fetch RelativeCoreCustomerContractDetails for coreCustomerId : " + coreCustomerId);
		}

		return result;

	}

	@Override
	public Result getCoreCustomerContractDetails(String methodId, Object[] inputArray,
			DataControllerRequest requestInstance, DataControllerResponse response) {

		Result result = new Result();
		String coreCustomerId = StringUtils.EMPTY;
		String legalEntityId = requestInstance.getParameter(INPUT_LEGALENTITYID);

		try {

			if (StringUtils.isBlank(requestInstance.getParameter(INPUT_CORE_CUSTOMER_ID))) {
				ErrorCodeEnum.ERR_22026.setErrorCode(result);
				return result;
			}
			if(StringUtils.isBlank(legalEntityId)) {
				ErrorCodeEnum.ERR_22230.setErrorCode(result);
				return result;
			}
			
			String[] reqPermissions = {PermissionName.VIEW_CUSTOMER};
			if(!LoggedInUserHandler.hasAccessToLegalEntity(requestInstance,reqPermissions))
			{
				result.addParam(new Param("Status", "Get corecustomer contract details failed", FabricConstants.STRING));
				ErrorCodeEnum.ERR_22231.setErrorCode(result);
				alert.prepareError("Logged in user do not have access to this legalEntity ").log();
				return result;
				
			}


			String dbpServicesClaimsToken = DBPServices.getDBPServicesClaimsToken(requestInstance);
			coreCustomerId = requestInstance.getParameter(INPUT_CORE_CUSTOMER_ID);
			Map<String, Object> postParametersMap = new HashMap<>();
			postParametersMap.put("coreCustomerId", coreCustomerId);
			postParametersMap.put("legalEntityId", legalEntityId);
			JSONObject serviceResponse = infinityUserManagementBusinessDelegate
					.getCoreCustomerContractDetails(postParametersMap, dbpServicesClaimsToken);
			if (serviceResponse == null || !serviceResponse.has(FabricConstants.OPSTATUS)
					|| serviceResponse.getInt(FabricConstants.OPSTATUS) != 0) {
				ErrorCodeEnum.ERR_22030.setErrorCode(result);
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.CONTRACTS, EventEnum.UPDATE,
						ActivityStatusEnum.FAILED,
						"Failed to fetch CoreCustomerContractDetails for coreCustomerId : " + coreCustomerId);
				return result;
			} else if (serviceResponse.has("dbpErrMsg")) {
//				result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
//            	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
//				return result;
				Dataset recordsDS = new Dataset();
                recordsDS.setId("contracts");
                result.addDataset(recordsDS);
                return result;
			} else {

				result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
			}
		} catch (Exception e) {
			alert.prepareError("Unexpected Error in getCoreCustomerContractDetails", e).log();
			result.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
			ErrorCodeEnum.ERR_22030.setErrorCode(result);
			AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.CONTRACTS, EventEnum.SEARCH,
					ActivityStatusEnum.FAILED,
					"Failed to fetch CoreCustomerContractDetails for coreCustomerId : " + coreCustomerId);
		}

		return result;

	}

	@Override
	public Result getInfinityUserContractDetails(String methodId, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse response) {
		
		Result result = new Result();
        String userId = StringUtils.EMPTY;
        String legalEntityId = StringUtils.EMPTY;
        
        try {
        	
        	if (StringUtils.isBlank(requestInstance.getParameter(INPUT_USERID))) {
	            ErrorCodeEnum.ERR_22081.setErrorCode(result);
	            return result;
	        }
            String dbpServicesClaimsToken = DBPServices.getDBPServicesClaimsToken(requestInstance);
            userId = requestInstance.getParameter(INPUT_USERID);
            legalEntityId = requestInstance.getParameter(INPUT_LEGALENTITYID);
            if (StringUtils.isBlank(legalEntityId)) {
	            ErrorCodeEnum.ERR_22232.setErrorCode(result);
	            return result;
	        }
            String[] reqPermissions = {PermissionName.VIEW_CUSTOMER};
			if(!LoggedInUserHandler.hasAccessToLegalEntity(requestInstance,reqPermissions))
            {
                result.addParam(new Param("Status", "Get infinity user contract details operation failed", FabricConstants.STRING));
                ErrorCodeEnum.ERR_22231.setErrorCode(result);
                alert.prepareError("Logged in user do not have access to this legalEntity ").log();
                return result;
                
            }
        	
            Map<String, Object> postParametersMap = new HashMap<>();
            postParametersMap.put(INPUT_USERID, userId);
            postParametersMap.put(INPUT_LEGALENTITYID, legalEntityId);
            JSONObject serviceResponse =
                    infinityUserManagementBusinessDelegate.getInfinityUserContractDetails(postParametersMap,
                            dbpServicesClaimsToken);
            if (serviceResponse == null || !serviceResponse.has(FabricConstants.OPSTATUS)
	                || serviceResponse.getInt(FabricConstants.OPSTATUS) != 0) {
	            ErrorCodeEnum.ERR_22077.setErrorCode(result);
	            result.addParam(new Param("status", "Failure", FabricConstants.STRING));
	            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.CONTRACTS, EventEnum.SEARCH,
	                    ActivityStatusEnum.FAILED, "Failed to fetch infinity user contract details for userId : "+userId);
	            return result;
	        } else if (serviceResponse.has("dbpErrMsg")) {
	        	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return result;
	        } else {
	        	
	        	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
	        	AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.CONTRACTS, EventEnum.SEARCH,
                        ActivityStatusEnum.SUCCESSFUL,
                        "Succefully fetched Infinity User Contract Details for userId :" + userId);
	        }
        } catch (Exception e) {
        	alert.prepareError("Unexpected Error in getInfinityUserContractDetails", e).log();
            result.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
            ErrorCodeEnum.ERR_22077.setErrorCode(result);
            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.CONTRACTS, EventEnum.SEARCH,
                    ActivityStatusEnum.FAILED, "Failed to fetch infinity user contract details for userId : "+userId);
        }

        return result;
	}

	@Override
	public Result getInfinityUserAccounts(String methodId, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse response) {
		
		Result result = new Result();
        String userId = StringUtils.EMPTY;
        String legalEntityId = StringUtils.EMPTY;
        
        try {
        	
        	if (StringUtils.isBlank(requestInstance.getParameter(INPUT_USERID))) {
	            ErrorCodeEnum.ERR_22081.setErrorCode(result);
	            return result;
	        }
        	if (StringUtils.isBlank(requestInstance.getParameter(INPUT_LEGALENTITYID))) {
	            ErrorCodeEnum.ERR_22232.setErrorCode(result);
	            return result;
	        }
        	String[] reqPermissions = {PermissionName.VIEW_CUSTOMER};
			if(!LoggedInUserHandler.hasAccessToLegalEntity(requestInstance,reqPermissions))
            {
                result.addParam(new Param("Status", "Get infinity user accounts failed", FabricConstants.STRING));
                ErrorCodeEnum.ERR_22231.setErrorCode(result);
                alert.prepareError("Logged in user do not have access to this legalEntity ").log();
                return result;        
            }
            String dbpServicesClaimsToken = DBPServices.getDBPServicesClaimsToken(requestInstance);
            userId = requestInstance.getParameter(INPUT_USERID);
            legalEntityId= requestInstance.getParameter(INPUT_LEGALENTITYID);
            Map<String, Object> postParametersMap = new HashMap<>();
            postParametersMap.put(INPUT_USERID, userId);
            postParametersMap.put(INPUT_LEGALENTITYID, legalEntityId);
            JSONObject serviceResponse =
                    infinityUserManagementBusinessDelegate.getInfinityUserAccounts(postParametersMap,
                            dbpServicesClaimsToken);
            if (serviceResponse == null || !serviceResponse.has(FabricConstants.OPSTATUS)
	                || serviceResponse.getInt(FabricConstants.OPSTATUS) != 0) {
	            ErrorCodeEnum.ERR_22078.setErrorCode(result);
	            result.addParam(new Param("status", "Failure", FabricConstants.STRING));
	            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.CUSTOMERS, EventEnum.SEARCH,
	                    ActivityStatusEnum.FAILED, "Failed to fetch infinity user Accounts for userId : "+userId);
	            return result;
	        } else if (serviceResponse.has("dbpErrMsg")) {
	        	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return result;
	        } else {
	        	
	        	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
	        	AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.CUSTOMERS, EventEnum.SEARCH,
                        ActivityStatusEnum.SUCCESSFUL,
                        "Succefully fetched Infinity User Accounts for userId :" + userId);
	        }
        } catch (Exception e) {
        	alert.prepareError("Unexpected Error in getInfinityUserAccounts", e).log();
            result.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
            ErrorCodeEnum.ERR_22078.setErrorCode(result);
            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.CUSTOMERS, EventEnum.SEARCH,
                    ActivityStatusEnum.FAILED, "Failed to fetch infinity user Accounts for userId : "+userId);
        }

        return result;
        
	}

	@Override
	public Result getInfinityUserFeatureActions(String methodId, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse response) {
		

		Result result = new Result();
        String userId = StringUtils.EMPTY;
        String coreCustomerId = StringUtils.EMPTY;
        String contractId = StringUtils.EMPTY;
        String legalEntityId = StringUtils.EMPTY;
        
        try {
        	
        	if (StringUtils.isBlank(requestInstance.getParameter(INPUT_USERID))) {
	            ErrorCodeEnum.ERR_22081.setErrorCode(result);
	            return result;
	        }
        	if (StringUtils.isBlank(requestInstance.getParameter(INPUT_LEGALENTITYID))) {
	            ErrorCodeEnum.ERR_22232.setErrorCode(result);
	            return result;
	        }
            String dbpServicesClaimsToken = DBPServices.getDBPServicesClaimsToken(requestInstance);
            userId = requestInstance.getParameter(INPUT_USERID);
            coreCustomerId = requestInstance.getParameter(INPUT_CORE_CUSTOMER_ID);
            contractId = requestInstance.getParameter(INPUT_CONTRACTID);
            legalEntityId = requestInstance.getParameter(INPUT_LEGALENTITYID);
            Map<String, Object> postParametersMap = new HashMap<>();
            postParametersMap.put(INPUT_USERID, userId);
            postParametersMap.put(INPUT_CONTRACTID, contractId);
            postParametersMap.put(INPUT_CORE_CUSTOMER_ID, coreCustomerId);
            postParametersMap.put(INPUT_LEGALENTITYID, legalEntityId);
            JSONObject serviceResponse =
                    infinityUserManagementBusinessDelegate.getInfinityUserFeatureActions(postParametersMap,
                            dbpServicesClaimsToken);
            if (serviceResponse == null || !serviceResponse.has(FabricConstants.OPSTATUS)
	                || serviceResponse.getInt(FabricConstants.OPSTATUS) != 0) {
	            ErrorCodeEnum.ERR_22079.setErrorCode(result);
	            result.addParam(new Param("status", "Failure", FabricConstants.STRING));
	            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.CUSTOMERS, EventEnum.SEARCH,
	                    ActivityStatusEnum.FAILED, "Failed to fetch infinity user features and actions for userId : "+userId);
	            return result;
	        } else if (serviceResponse.has("dbpErrMsg")) {
	        	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return result;
	        } else {
	        	
	        	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
	        	AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.CUSTOMERS, EventEnum.SEARCH,
                        ActivityStatusEnum.SUCCESSFUL,
                        "Succefully fetched Infinity User features and actions for userId :" + userId);
	        }
        } catch (Exception e) {
        	alert.prepareError("Unexpected Error in getInfinityUserFeatureActions", e).log();
            result.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
            ErrorCodeEnum.ERR_22079.setErrorCode(result);
            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.CUSTOMERS, EventEnum.SEARCH,
                    ActivityStatusEnum.FAILED, "Failed to fetch infinity user features and actions for userId : "+userId);
        }

        return result;
        
	}

	@Override
	public Result getInfinityUserLimits(String methodId, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse response) {
		
		Result result = new Result();
        String userId = StringUtils.EMPTY;
        String contractId = StringUtils.EMPTY;
        String coreCustomerId = StringUtils.EMPTY;
        String legalEntityId = StringUtils.EMPTY;
        
        try {
        	
        	if (StringUtils.isBlank(requestInstance.getParameter(INPUT_USERID))) {
	            ErrorCodeEnum.ERR_22081.setErrorCode(result);
	            return result;
	        }
        	if (StringUtils.isBlank(requestInstance.getParameter(INPUT_LEGALENTITYID))) {
	            ErrorCodeEnum.ERR_22232.setErrorCode(result);
	            return result;
	        }
            String dbpServicesClaimsToken = DBPServices.getDBPServicesClaimsToken(requestInstance);
            userId = requestInstance.getParameter(INPUT_USERID);
            contractId = requestInstance.getParameter(INPUT_CONTRACTID);
            coreCustomerId = requestInstance.getParameter(INPUT_CORE_CUSTOMER_ID);
            legalEntityId = requestInstance.getParameter(INPUT_LEGALENTITYID);
            Map<String, Object> postParametersMap = new HashMap<>();
            postParametersMap.put(INPUT_USERID, userId);
            postParametersMap.put("contractId", contractId);
            postParametersMap.put("coreCustomerId", coreCustomerId);
            postParametersMap.put("legalEntityId", legalEntityId);
            JSONObject serviceResponse =
                    infinityUserManagementBusinessDelegate.getInfinityUserLimits(postParametersMap,
                            dbpServicesClaimsToken);
            if (serviceResponse == null || !serviceResponse.has(FabricConstants.OPSTATUS)
	                || serviceResponse.getInt(FabricConstants.OPSTATUS) != 0) {
	            ErrorCodeEnum.ERR_22080.setErrorCode(result);
	            result.addParam(new Param("status", "Failure", FabricConstants.STRING));
	            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.CUSTOMERS, EventEnum.SEARCH,
	                    ActivityStatusEnum.FAILED, "Failed to fetch infinity user limits for userId : "+userId);
	            return result;
	        } else if (serviceResponse.has("dbpErrMsg")) {
	        	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return result;
	        } else {
	        	
	        	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
	        	AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.CUSTOMERS, EventEnum.SEARCH,
                        ActivityStatusEnum.SUCCESSFUL,
                        "Succefully fetched Infinity User limits for userId :" + userId);
	        }
        } catch (Exception e) {
        	alert.prepareError("Unexpected Error in getInfinityUserLimits", e).log();
            result.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
            ErrorCodeEnum.ERR_22080.setErrorCode(result);
            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.CUSTOMERS, EventEnum.SEARCH,
                    ActivityStatusEnum.FAILED, "Failed to fetch infinity user limits for userId : "+userId);
        }

        return result;
        
	}
	
	@Override

	public Result getInfinityUserServicedefsRoles(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response)
	{
		Result result = new Result();
		try {
			if (request.getParameter(INPUT_ID) == null || StringUtils.isBlank(request.getParameter(INPUT_LEGALENTITYID))) {
				
				ErrorCodeEnum.ERR_21991.setErrorCode(result);
				return result;
			} else {
				
				String[] reqPermissions = {PermissionName.VIEW_CUSTOMER};
				if(!LoggedInUserHandler.hasAccessToLegalEntity(request,reqPermissions))
	            {
	                result.addParam(new Param("Status", "Get Infinity User Service Defs Roles failed", FabricConstants.STRING));
	                ErrorCodeEnum.ERR_22231.setErrorCode(result);
	                alert.prepareError("Logged in user do not have access to this legalEntity ").log();
	                return result;
	                
	            }
				String userId = request.getParameter(INPUT_ID);
				String legalEntityId = request.getParameter(INPUT_LEGALENTITYID);
				Map<String, Object> postParametersMap = new HashMap<>();
				postParametersMap.put("id", userId);
				postParametersMap.put("legalEntityId", legalEntityId);
				

				String dbpServicesClaimsToken = DBPServices.getDBPServicesClaimsToken(request);
				JSONObject getCustomerresponse = infinityUserManagementBusinessDelegate
						.getInfinityUserServicedefsRoles(postParametersMap, dbpServicesClaimsToken);
				if (getCustomerresponse == null || !getCustomerresponse.has(FabricConstants.OPSTATUS)
						|| getCustomerresponse.getInt(FabricConstants.OPSTATUS) != 0) {
					ErrorCodeEnum.ERR_22227.setErrorCode(result);
					result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					AuditHandler.auditAdminActivity(request, ModuleNameEnum.CUSTOMERS, EventEnum.SEARCH,
							ActivityStatusEnum.FAILED, "Get infinity user failed");
					return result;
				} else if (getCustomerresponse.has("dbpErrMsg")) {
					result = CommonUtilities.constructResultFromJSONObject(getCustomerresponse);
	            	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					return result;
				} else {
					
					result = CommonUtilities.constructResultFromJSONObject(getCustomerresponse);
					AuditHandler.auditAdminActivity(request, ModuleNameEnum.CUSTOMERS, EventEnum.SEARCH,
							ActivityStatusEnum.SUCCESSFUL, "Successfully fetched infinity user details. id= " + userId);
				}
			}

		} catch (Exception e) {
			alert.prepareError("Unexepected Error in get infinity user details", e).log();
			result.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
			ErrorCodeEnum.ERR_21993.setErrorCode(result);
			AuditHandler.auditAdminActivity(request, ModuleNameEnum.CUSTOMERS, EventEnum.SEARCH,
					ActivityStatusEnum.FAILED, "Infinity user details parse Failed");
		}
		return result;
	}

	@Override

	public Result getInfinityUserAccountsForCorecustomer(String methodId, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse response) {

		Result result = new Result();
		String userId = StringUtils.EMPTY;
        String coreCustomerId = StringUtils.EMPTY;
        String legalEntityId = StringUtils.EMPTY;
        try {
        	if (StringUtils.isBlank(requestInstance.getParameter(INPUT_USERID))) {
	            ErrorCodeEnum.ERR_22081.setErrorCode(result);
	            return result;
	        }
        	if (StringUtils.isBlank(requestInstance.getParameter(INPUT_CORE_CUSTOMER_ID))) {
	            ErrorCodeEnum.ERR_22038.setErrorCode(result);
	            return result;
	        }
        	if (StringUtils.isBlank(requestInstance.getParameter(INPUT_LEGALENTITYID))) {
	            ErrorCodeEnum.ERR_22230.setErrorCode(result);
	            return result;
	        }
            String dbpServicesClaimsToken = DBPServices.getDBPServicesClaimsToken(requestInstance);
            userId = requestInstance.getParameter(INPUT_USERID);
            coreCustomerId = requestInstance.getParameter(INPUT_CORE_CUSTOMER_ID);   
            legalEntityId = requestInstance.getParameter(INPUT_LEGALENTITYID);
            
            Map<String, Object> postParametersMap = new HashMap<>();
            postParametersMap.put(INPUT_USERID, userId);
            postParametersMap.put(INPUT_CORE_CUSTOMER_ID, coreCustomerId);
            postParametersMap.put(INPUT_LEGALENTITYID, legalEntityId);

            JSONObject serviceResponse =
                    infinityUserManagementBusinessDelegate.getInfinityUserAccountsForCorecustomer(postParametersMap,
                            dbpServicesClaimsToken);
            if (serviceResponse == null || !serviceResponse.has(FabricConstants.OPSTATUS)
	                || serviceResponse.getInt(FabricConstants.OPSTATUS) != 0) {
	            ErrorCodeEnum.ERR_22078.setErrorCode(result);
	            result.addParam(new Param("status", "Failure", FabricConstants.STRING));
	            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.CUSTOMERS, EventEnum.SEARCH,
	                    ActivityStatusEnum.FAILED, "Failed to fetch infinity user Accounts for core customers : "+coreCustomerId);
	            return result;
	        } else if (serviceResponse.has("dbpErrMsg")) {
	        	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return result;
	        } else {
	        	
	        	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
	        	AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.CUSTOMERS, EventEnum.SEARCH,
                        ActivityStatusEnum.SUCCESSFUL,
                        "Succefully fetched Infinity User Accounts for core customers :" + coreCustomerId);
	        }

        } catch (Exception e) {
        	alert.prepareError("Unexpected Error in getInfinityUserAccounts", e).log();
            result.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
            ErrorCodeEnum.ERR_22078.setErrorCode(result);
            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.CUSTOMERS, EventEnum.SEARCH,
                    ActivityStatusEnum.FAILED, "Failed to fetch infinity user Accounts for core customers : "+coreCustomerId);
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
	
	private String validateLegalEntityIdFromPayload(DataControllerRequest requestInstance, String methodId) {
		
		
		String userDetails = requestInstance.getParameter(INPUT_USER_DETAILS);
		String companyList = requestInstance.getParameter(INPUT_COMPANY_LIST);
		String accountLevelPermissions = requestInstance.getParameter(INPUT_ACCOUNT_LEVEL_PERMISSIONS);
		String globalLevelPermissions = requestInstance.getParameter(INPUT_GLOBAL_LEVEL_PERMISSIONS);
		String transactionLimits = requestInstance.getParameter(INPUT_TRANSACTION_LIMITS);
		String contractDetails = requestInstance.getParameter(INPUT_CONTRACT_DETAILS);
		String signatoryGroups = requestInstance.getParameter(INPUT_SIGNATORY_GROUP);
		String removedCompanies = requestInstance.getParameter(INPUT_REMOVED_COMPANIES);
		
		Set<String> legalEntitySet = new HashSet<String>();
		
		
		JsonElement userDetailsElement = new JsonParser().parse(userDetails);
		JsonObject userDetailsJsonObject =userDetailsElement.getAsJsonObject();
		if(userDetailsJsonObject.has("legalEntityId") && userDetailsJsonObject.get("legalEntityId").isJsonNull()) {
			return "";
		}
		String userDetailslegalEntityId = userDetailsJsonObject.has("legalEntityId") ? userDetailsJsonObject.get("legalEntityId").getAsString():null;
		if(StringUtils.isBlank(userDetailslegalEntityId)) {
			return "";
		}
		legalEntitySet.add(userDetailslegalEntityId);
		
		
		
		JsonArray accountLevelPermissionsJsonArray = new JsonParser().parse(accountLevelPermissions).getAsJsonArray();
		JsonObject accountLevelJsonObject = accountLevelPermissionsJsonArray.size() >0 ? accountLevelPermissionsJsonArray.get(0).getAsJsonObject():null;
		if(accountLevelJsonObject!=null) {
			if(accountLevelJsonObject.has("legalEntityId") && accountLevelJsonObject.get("legalEntityId").isJsonNull()) {
				return "";
			}
			String accountLevelPermissionsLegalEntityId = accountLevelJsonObject.has("legalEntityId") ? accountLevelJsonObject.get("legalEntityId").getAsString():null;
			if(StringUtils.isBlank(accountLevelPermissionsLegalEntityId)) {
				return "";
			}
			legalEntitySet.add(accountLevelPermissionsLegalEntityId);
		}
		
		
		JsonArray companyListJsonArray = new JsonParser().parse(companyList).getAsJsonArray();
		JsonObject companyListJsonObject = companyListJsonArray.size() >0? companyListJsonArray.get(0).getAsJsonObject():null;
		if(companyListJsonObject!=null) {
			if(companyListJsonObject.has("legalEntityId") && companyListJsonObject.get("legalEntityId").isJsonNull()) {
				return "";
			}
			String companyListLegalEntityId =companyListJsonObject.has("legalEntityId") ? companyListJsonObject.get("legalEntityId").getAsString():null;
			if(StringUtils.isBlank(companyListLegalEntityId))
			{
				return "";
			}
			legalEntitySet.add(companyListLegalEntityId);
		}
		
		
		JsonArray globalLevelPermissionsJsonArray = new JsonParser().parse(globalLevelPermissions).getAsJsonArray();
		JsonObject globalLevelPermissionsJsonObject = globalLevelPermissionsJsonArray.size() >0 ? globalLevelPermissionsJsonArray.get(0).getAsJsonObject():null;
		if(globalLevelPermissionsJsonObject!=null) {
			if(globalLevelPermissionsJsonObject.has("legalEntityId") && globalLevelPermissionsJsonObject.get("legalEntityId").isJsonNull()) {
				return "";
			}
			String globalLevelPermissionsLegalEntityId = globalLevelPermissionsJsonObject.has("legalEntityId") ? globalLevelPermissionsJsonObject.get("legalEntityId").getAsString():null;
			if(StringUtils.isBlank(globalLevelPermissionsLegalEntityId)) {
				return "";
			}
			legalEntitySet.add(globalLevelPermissionsLegalEntityId);
		}
		
		
		
		JsonArray signatoryGroupsJsonArray = new JsonParser().parse(signatoryGroups).getAsJsonArray();
		JsonObject signatoryGroupsJsonObject = signatoryGroupsJsonArray.size() > 0 ? signatoryGroupsJsonArray.get(0).getAsJsonObject():null;
		if(signatoryGroupsJsonObject!=null) {
			if(signatoryGroupsJsonObject.has("legalEntityId") && signatoryGroupsJsonObject.get("legalEntityId").isJsonNull()) {
				return "";
			}
			String signatoryGroupslegalEntityId =signatoryGroupsJsonObject.has("legalEntityId") ? signatoryGroupsJsonObject.get("legalEntityId").getAsString():null;
			if(StringUtils.isBlank(signatoryGroupslegalEntityId))
			{
				return "";
			}
			legalEntitySet.add(signatoryGroupslegalEntityId);
		}
		
		
		JsonArray transactionLimitsJsonArray = new JsonParser().parse(transactionLimits).getAsJsonArray();
		JsonObject transactionLimitsJsonObject =transactionLimitsJsonArray.size()>0 ? transactionLimitsJsonArray.get(0).getAsJsonObject():null;
		if(transactionLimitsJsonObject!=null) {
			if(transactionLimitsJsonObject.has("legalEntityId") && transactionLimitsJsonObject.get("legalEntityId").isJsonNull()) {
				return "";
			}
			String transactionLimitsLegalEntityId = transactionLimitsJsonObject.has("legalEntityId") ? transactionLimitsJsonObject.get("legalEntityId").getAsString():null;
			if(StringUtils.isBlank(transactionLimitsLegalEntityId))
			{
				return "";
			}
			legalEntitySet.add(transactionLimitsLegalEntityId);
		}
		
		
		if(methodId.equals("editInfinityUser")) {
			if(StringUtils.isNotBlank(removedCompanies)) {
			JsonArray removedCompaniesJsonArray = new JsonParser().parse(removedCompanies).getAsJsonArray();
			JsonObject removedCompaniesJsonObject = removedCompaniesJsonArray.size() > 0? removedCompaniesJsonArray.get(0).getAsJsonObject():null;
			/*if(removedCompaniesJsonObject!=null) {
				if(removedCompaniesJsonObject.has("legalEntityId") && removedCompaniesJsonObject.get("legalEntityId").isJsonNull()) {
					return "";
				}
				String removedCompanieslegalEntityId = removedCompaniesJsonObject.has("legalEntityId") ?removedCompaniesJsonObject.get("legalEntityId").getAsString():null;
				if(StringUtils.isBlank(removedCompanieslegalEntityId))
				{
					return "";
				}
				legalEntitySet.add(removedCompanieslegalEntityId);
			}*/
			}
		}
		else if (methodId.equals("createInfinityUser")) {

			if (StringUtils.isNotBlank(contractDetails)) {
				JsonElement contractDetailsElement = new JsonParser().parse(contractDetails);
				JsonObject contractDetailsJsonObject = contractDetailsElement.getAsJsonObject();
				if (contractDetailsJsonObject.has("legalEntityId")
						&& contractDetailsJsonObject.get("legalEntityId").isJsonNull()) {
					return "";
				}
				String contractDetailslegalEntityId = contractDetailsJsonObject.has("legalEntityId")
						? contractDetailsJsonObject.get("legalEntityId").getAsString()
						: null;
				if (StringUtils.isBlank(contractDetailslegalEntityId)) {
					return "";
				}
				legalEntitySet.add(contractDetailslegalEntityId);
			}
		}
		
		if(legalEntitySet.size()==1)
		{
			return userDetailslegalEntityId;	
		}
		else {
			return null;
		}
		
		
		
				
		
	}

}