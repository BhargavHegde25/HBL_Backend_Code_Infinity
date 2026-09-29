package com.temenos.dbx.usermanagement.resource.impl;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.Callable;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.sessionmanager.SessionScope;
import com.kony.dbputilities.util.ConvertJsonToResult;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.LegalEntityUtil;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.temenos.dbx.eum.product.contract.businessdelegate.api.ContractBusinessDelegate;
import com.temenos.dbx.eum.product.contract.resource.api.ContractResource;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.InfinityUserManagementBusinessDelegate;
import com.temenos.dbx.eum.product.usermanagement.resource.api.InfinityUserManagementResource;
import com.temenos.dbx.eum.product.usermanagement.resource.impl.InfinityUserManagementResourceImpl;
import com.temenos.dbx.product.businessdelegate.api.ApplicationBusinessDelegate;
import com.temenos.dbx.product.dto.ApplicationDTO;

import com.temenos.dbx.product.dto.ContractAccountsDTO;
import com.temenos.dbx.product.dto.ContractCustomersDTO;
import com.temenos.dbx.product.dto.CustomerDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.utils.CustomerCreationMode;
import com.temenos.dbx.product.utils.DTOUtils;
import com.temenos.dbx.product.utils.InfinityConstants;
import com.temenos.dbx.product.utils.ThreadExecutor;

public class PartyInfinityUserManagementResourceImpl implements InfinityUserManagementResource{
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Result getAssociatedCustomers(String methodId, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws ApplicationException {
		Result result = new Result();
		Map<String, Object> inputParams = HelperMethods.getInputParamObjectMap(inputArray);

		String customerId = HelperMethods.getCustomerIdFromSession(dcRequest);
		ContractCustomersDTO contractCustomerDTO = new ContractCustomersDTO();
		if (StringUtils.isBlank(customerId)) {
			customerId = inputParams.containsKey("Customer_id") ? (String) inputParams.get("Customer_id")
					: dcRequest.getParameter("Customer_id");
		}
		
		String legalEntityId ="";
		Boolean isSuperAdmin = LegalEntityUtil.checkForSuperAdmin(dcRequest);
		
		if(isSuperAdmin)
        {
			legalEntityId = inputParams.containsKey("legalEntityId") ? (String) inputParams.get("legalEntityId")
					: dcRequest.getParameter("legalEntityId");
			if(StringUtils.isBlank(legalEntityId))
			{
				alert.prepareError("LegalEntity is mandatory!!").log();
				return ErrorCodeEnum.ERR_29040.setErrorCode(new Result());
			}				    
        }
		else
			legalEntityId = LegalEntityUtil.getLegalEntityIdFromSessionOrCache(dcRequest);

	        if(StringUtils.isBlank(legalEntityId)) {
	        	return ErrorCodeEnum.ERR_10001.setErrorCode(new Result());
	        }
	        
	    Boolean isValidLegalEntity = LegalEntityUtil.isLegalEntityValid(legalEntityId);
	    
	    if(!isValidLegalEntity) {
	    	alert.prepareError("Logged in user do not have access to this legalEntity ").log();
	    	return ErrorCodeEnum.ERR_12403.setErrorCode(new Result());    	    	
	    }
	    
		contractCustomerDTO.setCustomerId(customerId);
		contractCustomerDTO.setCompanyLegalUnit(legalEntityId);
		DBXResult response = new DBXResult();
		try {
			InfinityUserManagementBusinessDelegate infinityUserManagementBusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(InfinityUserManagementBusinessDelegate.class);
			response = infinityUserManagementBusinessDelegate.getAssociatedCustomers(contractCustomerDTO,
					dcRequest.getHeaderMap());
			if (response != null && response.getResponse() != null) {
				JsonObject jsonObject = (JsonObject) response.getResponse();
				result = ConvertJsonToResult.convert(jsonObject);
			}
		} catch (ApplicationException e) {
			alert.prepareError(
					"InfinityUserManagementResourceImpl : Exception occured while fetching the associated customers info ").log();
			throw new ApplicationException(e.getErrorCodeEnum());
		} catch (Exception e) {
			alert.prepareError(
					"InfinityUserManagementResourceImpl : Exception occured while fetching the associated customers info "
							+ e.getMessage()).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_10763);
		}
		return result;
	}


	@Override
	public Result getAllEligibleRelationalCustomers(String methodId, Object[] inputArray,
			DataControllerRequest dcRequest, DataControllerResponse response) throws ApplicationException {
		Result result = new Result();
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		String coreCustomerId = StringUtils.isNotBlank(inputParams.get(InfinityConstants.coreCustomerId))
				? inputParams.get(InfinityConstants.coreCustomerId)
				: dcRequest.getParameter(InfinityConstants.coreCustomerId);

		if (StringUtils.isBlank(coreCustomerId)) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10769);
		}
		
		String legalEntityId ="";
		Boolean isSuperAdmin = LegalEntityUtil.checkForSuperAdmin(dcRequest);
		
		if(isSuperAdmin)
			legalEntityId = LegalEntityUtil.getLegalEntityFromPayload(inputParams, dcRequest);
		else
			legalEntityId = LegalEntityUtil.getLegalEntityIdFromSessionOrCache(dcRequest);

	        if(StringUtils.isBlank(legalEntityId)) {
	        	return ErrorCodeEnum.ERR_10001.setErrorCode(new Result());
	        }
	        
	    Boolean isValidLegalEntity = LegalEntityUtil.isLegalEntityValid(legalEntityId);
	    
	    if(!isValidLegalEntity) {
	    	alert.prepareError("Logged in user do not have access to this legalEntity ").log();
	    	return ErrorCodeEnum.ERR_12403.setErrorCode(new Result());    	    	
	    }
		
		DBXResult dbxResult = new DBXResult();
		try {
			InfinityUserManagementBusinessDelegate infinityUserManagementBusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(InfinityUserManagementBusinessDelegate.class);
			dbxResult = infinityUserManagementBusinessDelegate.getAllEligibleRelationalCustomers(coreCustomerId,
					dcRequest.getHeaderMap(),legalEntityId);
			if (dbxResult.getResponse() != null) {
				result = ConvertJsonToResult.convert((JsonObject) dbxResult.getResponse());
			}
		} catch (ApplicationException e) {
			alert.prepareError("InfinityUserManagementResourceImpl : Exception occured while fetching relative customers"
					+ e.getMessage()).log();
			throw new ApplicationException(e.getErrorCodeEnum());
		} catch (Exception e) {
			alert.prepareError("InfinityUserManagementResourceImpl : Exception occured while fetching relative customers"
					+ e.getMessage()).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_10770);
		}
		return result;
	}


	@Override
	public Object createInfinityUser(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws JsonMappingException, JsonProcessingException, JSONException, ApplicationException {
		Result result = new Result();
		Result activationCodeResult = new Result();

		JsonObject jsonObject = new JsonObject();

		Map<String, String> map = HelperMethods.getInputParamMap(inputArray);

		Iterator<String> iterator = request.getParameterNames();
		
		InfinityUserManagementResourceImpl userManagementResource = new InfinityUserManagementResourceImpl(); 

		while (iterator.hasNext()) {
			String key = iterator.next();
			if ((!map.containsKey(key) || StringUtils.isBlank(map.get(key)))
					&& StringUtils.isNotBlank(request.getParameter(key))) {
				map.put(key, request.getParameter(key));
			}
		}

		Boolean isContractValidationRequired = true;
		Boolean isSuperAdmin = false;

		Map<String, String> loggedInUserInfo = HelperMethods.getCustomerFromAPIDBPIdentityService(request);
		if (HelperMethods.isAuthenticationCheckRequiredForService(loggedInUserInfo)) {
			Set<String> userPermissions = SessionScope.getAllPermissionsFromIdentityScope(request);
			if (!userPermissions.contains("USER_MANAGEMENT")) {
				ErrorCodeEnum.ERR_10051.setErrorCode(result,
						"Authorization error. logged in userDoesn't have permission");
				isContractValidationRequired = true;				
				return result;
			}
		} else {
			isContractValidationRequired = false;
			isSuperAdmin = true;
		}

		if (!map.containsKey(InfinityConstants.userDetails)
				|| StringUtils.isBlank(map.get(InfinityConstants.userDetails))) {
			ErrorCodeEnum.ERR_10056.setErrorCode(result, "Invalid user Details");
			return false;
		}

		if (!map.containsKey(InfinityConstants.companyList)
				|| StringUtils.isBlank(map.get(InfinityConstants.companyList))) {
			ErrorCodeEnum.ERR_10056.setErrorCode(result, "Invalid company List");
			return false;
		}

		String userDetails = map.get(InfinityConstants.userDetails);
		JsonElement userDetailsElement = JSONUtil.parseAsJsonObject(userDetails);
		if (userDetailsElement.isJsonNull() || !userDetailsElement.isJsonObject()) {
			ErrorCodeEnum.ERR_10056.setErrorCode(result, "Invalid user Details");
			return false;
		}

		ApplicationBusinessDelegate applicationBusinessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(ApplicationBusinessDelegate.class);
		ApplicationDTO applicationDTO = new ApplicationDTO();
		try {
			applicationDTO = applicationBusinessDelegate.getApplicationProperties(request.getHeaderMap());
		} catch (ApplicationException e) {
			alert.prepareError("Exception while fetching application property", e).log();
		}

		JsonObject userDetailsJsonObject = userDetailsElement.getAsJsonObject();
		if (StringUtils.isNotBlank(applicationDTO.getCustomerCreationMode())
				&& applicationDTO.getCustomerCreationMode().equals(CustomerCreationMode.WITH_RECORD.getValue())) {
			if (!userDetailsJsonObject.has(InfinityConstants.coreCustomerId)
					|| userDetailsJsonObject.get(InfinityConstants.coreCustomerId).isJsonNull()) {
				ErrorCodeEnum.ERR_10050.setErrorCode(result,
						"coreCustomerId is mandatory Input according to configuration");
				return false;
			}
		
		}

		jsonObject.add(InfinityConstants.userDetails, userDetailsJsonObject);

		String signatoryGroups = map.get(InfinityConstants.signatoryGroups);
		jsonObject.addProperty(InfinityConstants.signatoryGroups, signatoryGroups);
		
		Set<String> allLegalEntities = LegalEntityUtil.getAllCompanyLegalUnits();
		try {
			userDetailsJsonObject = LegalEntityUtil.addLegalEntityToPermissionsIfReq(userDetailsJsonObject,
					isSuperAdmin, allLegalEntities, request);
		} catch (Exception e) {
			ErrorCodeEnum.ERR_29042.setErrorCode(result, "Failed to validate user details!");
			return result;
		}

		if (!userManagementResource.validateUserDetails(userDetailsJsonObject, result)) {
			return result;
		}
		
		if (userManagementResource.validateinput(jsonObject, result, map, request, isContractValidationRequired, null,
				allLegalEntities)) {
			InfinityUserManagementBusinessDelegate infinityUserManagementBusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(InfinityUserManagementBusinessDelegate.class);
			JsonArray companyList = jsonObject.has(InfinityConstants.companyList)
	                && jsonObject.get(InfinityConstants.companyList).isJsonArray()
	                        ? jsonObject.get(InfinityConstants.companyList).getAsJsonArray()
	                        : new JsonArray();
			if(companyList.isEmpty())
			{
				ErrorCodeEnum.ERR_29059.setErrorCode(result, "Invalid Input payload!");
				return result;
			}
			DBXResult dbxResult = infinityUserManagementBusinessDelegate.createInfinityUser(jsonObject,
					request.getHeaderMap());
			if (dbxResult.getResponse() != null) {
				JsonObject jsonResultObject = (JsonObject) dbxResult.getResponse();
				result = JSONToResult.convert(jsonResultObject.toString());
				if (jsonResultObject.has(InfinityConstants.id)
						&& !jsonResultObject.get(InfinityConstants.id).isJsonNull()) {
					String customerId = jsonResultObject.get(InfinityConstants.id).getAsString();
					map.put(InfinityConstants.userId, customerId);
					map.put(InfinityConstants.contractStatus, DBPUtilitiesConstants.CONTRACT_STATUS_ACTIVE);
					result.addParam(InfinityConstants.userName, map.get(InfinityConstants.userName));
					try {
						String legalEntityId = userDetailsJsonObject.has(InfinityConstants.legalEntityId) ? userDetailsJsonObject.get(InfinityConstants.legalEntityId).getAsString(): null;
						request.addRequestParam_(InfinityConstants.legalEntityId, legalEntityId);
						activationCodeResult = generateInfinityUserActivationCodeAndUsername(methodId, inputArray,
								request, response);
						String activationCode = activationCodeResult.getParamValueByName("activationcodeForSCA");
						String userName = activationCodeResult.getParamValueByName("usernameForSCA");
						result.addStringParam(InfinityConstants.userName, userName);
						result.addStringParam(InfinityConstants.activationCode, activationCode);
					} catch (ApplicationException e) {
						// TODO Auto-generated catch block
						alert.prepareError("Error occured",e).log();
						ErrorCodeEnum.ERR_10795.setErrorCode(result);
					}
					/*try {
						signatoryGroups = request.getParameter(InfinityConstants.signatoryGroups);
						jsonObject.addProperty(InfinityConstants.signatoryGroups, signatoryGroups);
					} catch (Exception e) {
					}*/
					userManagementResource.updateSignatoryGroupEntry(customerId, jsonObject, result, request);
				}

				diagnostic.prepareDebug("Json response " + ResultToJSON.convert(result).toString()).log();
			}
		}

		return result;
	}

	@Override
	public Object editInfinityUser(String methodId, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse)
			throws JsonMappingException, JsonProcessingException, JSONException, ApplicationException {
		InfinityUserManagementResource managementResource = new InfinityUserManagementResourceImpl();
		return managementResource.editInfinityUser(methodId, inputArray, dcRequest, dcResponse);
	}

	@Override
	public Object getInfinityUser(String methodId, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse)
			throws JsonMappingException, JsonProcessingException, ApplicationException {
		InfinityUserManagementResource managementResource = new InfinityUserManagementResourceImpl();
		return managementResource.getInfinityUser(methodId, inputArray, dcRequest, dcResponse);
	}

	@Override
	public Result getCoreCustomerFeatureActionLimits(String methodId, Object[] inputArray,
			DataControllerRequest dcRequest, DataControllerResponse dcResponse) throws ApplicationException {
		InfinityUserManagementResource managementResource = new InfinityUserManagementResourceImpl();
		return managementResource.getCoreCustomerFeatureActionLimits(methodId, inputArray, dcRequest, dcResponse);
	}

	@Override
	public Result getCoreCustomerInformation(String methodId, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws ApplicationException {
		InfinityUserManagementResource managementResource = new InfinityUserManagementResourceImpl();
		return managementResource.getCoreCustomerInformation(methodId, inputArray, dcRequest, dcResponse);
	}

	@Override
	public Result getInfinityUserContractCustomerDetails(String methodId, Object[] inputArray,
			DataControllerRequest dcRequest, DataControllerResponse dcResponse) throws ApplicationException {
		InfinityUserManagementResource managementResource = new InfinityUserManagementResourceImpl();
		return managementResource.getInfinityUserContractCustomerDetails(methodId, inputArray, dcRequest, dcResponse);
	}

	@Override
	public Result createCustomRole(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		InfinityUserManagementResource managementResource = new InfinityUserManagementResourceImpl();
		return managementResource.createCustomRole(methodID, inputArray, request, response);
	}

	@Override
	public Result editCustomRole(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		InfinityUserManagementResource managementResource = new InfinityUserManagementResourceImpl();
		return managementResource.editCustomRole(methodID, inputArray, request, response);
	}

	@Override
	public Result verifyCustomRole(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		InfinityUserManagementResource managementResource = new InfinityUserManagementResourceImpl();
		return managementResource.verifyCustomRole(methodID, inputArray, request, response);
	}

	@Override
	public Result getCustomRole(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		InfinityUserManagementResource managementResource = new InfinityUserManagementResourceImpl();
		return managementResource.getCustomRole(methodID, inputArray, request, response);
	}

	@Override
	public Result getInfinityUserContractCoreCustomerActions(String methodID, Object[] inputArray,
			DataControllerRequest request, DataControllerResponse response) throws ApplicationException {
		InfinityUserManagementResource managementResource = new InfinityUserManagementResourceImpl();
		return managementResource.getInfinityUserContractCoreCustomerActions(methodID, inputArray,request, response);
	}

	@Override
	public Result getCustomRoleByCompanyID(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		InfinityUserManagementResource managementResource = new InfinityUserManagementResourceImpl();
		return managementResource.getCustomRoleByCompanyID(methodID, inputArray, request,response);
	}

	@Override
	public Result getCompanyLevelCustomRoles(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		InfinityUserManagementResource managementResource = new InfinityUserManagementResourceImpl();
		return managementResource.getCompanyLevelCustomRoles(methodID, inputArray,request,response);
	}

	@Override
	public Result getAssociatedContractUsers(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws ApplicationException {
		InfinityUserManagementResource managementResource = new InfinityUserManagementResourceImpl();
		return managementResource.getAssociatedContractUsers(methodID, inputArray, request, response);
	}

	@Override
	public Result getInfinityUserBasedContractDetails(String methodID, Object[] inputArray,
			DataControllerRequest request, DataControllerResponse response) throws ApplicationException {
		InfinityUserManagementResource managementResource = new InfinityUserManagementResourceImpl();
		return managementResource.getInfinityUserBasedContractDetails(methodID, inputArray, request, response);
	}

	private String createAUserAndAssignTOGivenContract(Map<String, Object> contractPayload, String coreCustomerId,
			String legalEntityId, DataControllerRequest request, String partyId) throws ApplicationException {
		InfinityUserManagementResourceImpl impl = new InfinityUserManagementResourceImpl();
		
		Set<String> createdValidContractCoreCustomers = request.getAttribute("createdValidCustomers");
		Map<String, Set<ContractAccountsDTO>> createdCoreCustomerAccounts = request
				.getAttribute("createdCustomerAccounts");
		String createdServiceType = request.getAttribute("serviceType");
		String contractId = request.getAttribute("contractId");
		String authorizedSignatory = contractPayload.get("authorizedSignatory").toString();
		String authorizedSignatoryRoles = contractPayload.get("authorizedSignatoryRoles").toString();
		List<CustomerDTO> authorizedSignatoryList = DTOUtils.getDTOList(authorizedSignatory, CustomerDTO.class);
		String companyId = legalEntityId;

		if (authorizedSignatoryList == null || authorizedSignatoryList.isEmpty()) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10385);
		}

		Map<String, String> userToCoreCustomerRoles = impl.getUserToCoreCustomerRoles(authorizedSignatoryRoles);
		String userId = impl.createUser(authorizedSignatoryList.get(0), createdServiceType, request);

		Callable<Result> callable = new Callable<Result>() {
			public Result call() {
				try {
					impl.createCustomerPreference(userId, request.getHeaderMap());
					impl.createBackendIdentifierEntry(coreCustomerId, userId, contractId, request, companyId, partyId);
					impl.createUserRoles(userId, contractId, userToCoreCustomerRoles, request);
					impl.assignUserToContractCustomers(userId, contractId, createdValidContractCoreCustomers, request);
					impl.createUserAccounts(userId, contractId, createdCoreCustomerAccounts, legalEntityId, request);
					impl.createUserActionLimits(userId, contractId, companyId, createdValidContractCoreCustomers,
							userToCoreCustomerRoles, request);
				} catch (Exception e) {

				}
				return new Result();
			}
		};
		try {
			ThreadExecutor.getExecutor().execute(callable);
		} catch (Exception e) {
			alert.prepareError("ThreadExecutor : Exception occured while generating the activation code").log();
		}
		return userId;

	}

	@Override
	public Object createInfinityUserWithContract(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws JsonMappingException, JsonProcessingException, JSONException, ApplicationException {
		diagnostic.prepareDebug("createInfinityUserWithContract started....").log();
		Result result = new Result();

		Map<String, String> map = HelperMethods.getInputParamMap(inputArray);

		Iterator<String> iterator = request.getParameterNames();

		while (iterator.hasNext()) {
			String key = iterator.next();
			if ((!map.containsKey(key) || StringUtils.isBlank(map.get(key)))
					&& StringUtils.isNotBlank(request.getParameter(key))) {
				map.put(key, request.getParameter(key));
			}
		}
		
		String contractDetails = map.containsKey(InfinityConstants.contractDetails)
				&& map.get(InfinityConstants.contractDetails) != null ? map.get(InfinityConstants.contractDetails)
						: new JsonArray().toString();

		JsonElement element = new JsonParser().parse(contractDetails);
		JsonObject contractJsonObject;//= JSONUtil.parseAsJsonObject(contractDetails);
		
		
		if (element.isJsonObject()) {
			contractJsonObject = element.getAsJsonObject();

			if (!contractJsonObject.isJsonNull()) {
				if (contractJsonObject.has(InfinityConstants.contractName)
						&& !contractJsonObject.get(InfinityConstants.contractName).isJsonNull()) {
					for (Entry<String, JsonElement> entry : contractJsonObject.entrySet()) {
						map.put(entry.getKey(), entry.getValue().getAsString());
					}

					ContractResource resource = DBPAPIAbstractFactoryImpl.getResource(ContractResource.class);
					diagnostic.prepareDebug("Contract creation started....").log();
					try {
						result = resource.createContract(methodId, inputArray, request, response);
					} catch (ApplicationException e) {
						alert.prepareError("createContract exception", e).log();
						e.getErrorCodeEnum().setErrorCode(result);
					}

					if (!result.hasParamByName(InfinityConstants.contractId)
							|| StringUtils.isBlank(result.getParamValueByName(InfinityConstants.contractId))) {
						diagnostic.prepareDebug("Contract creation failed....").log();
						return result;
					}
					diagnostic.prepareDebug("Contract creation completed....").log();

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
					diagnostic.prepareDebug("contractId populated to companyList....").log();
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
					diagnostic.prepareDebug("contractId populated to signatoryGroups....").log();
					map.put(InfinityConstants.signatoryGroups, signatoryGroupsArray.toString());
				}
			}
		}
		
		InfinityUserManagementResource userManagementResource = DBPAPIAbstractFactoryImpl
				.getResource(InfinityUserManagementResource.class);

		result = (Result) userManagementResource.createInfinityUser(methodId, inputArray, request, response);
		diagnostic.prepareDebug("createInfinityUserWithContract completed....").log();
		return result;
	}

	@Override
	public Result getInfinityUserAccountsForAdmin(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws ApplicationException {
		InfinityUserManagementResource managementResource = new InfinityUserManagementResourceImpl();
		return managementResource.getInfinityUserAccountsForAdmin(methodID, inputArray, request, response);
	}

	@Override
	public Result getInfinityUserFeatureActions(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response)
			throws ApplicationException, JsonMappingException, JsonProcessingException {
		InfinityUserManagementResource managementResource = new InfinityUserManagementResourceImpl();
		return managementResource.getInfinityUserFeatureActions(methodID, inputArray, request, response);
	}

	@Override
	public Result getInfinityUserLimits(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response)
			throws ApplicationException, JsonMappingException, JsonProcessingException {
		InfinityUserManagementResource managementResource = new InfinityUserManagementResourceImpl();
		return managementResource.getInfinityUserLimits(methodID, inputArray, request, response);
	}

	@Override
	public Result generateInfinityUserActivationCodeAndUsername(String methodID, Object[] inputArray,
			DataControllerRequest request, DataControllerResponse response) throws ApplicationException {
		InfinityUserManagementResource managementResource = new InfinityUserManagementResourceImpl();
		return managementResource.generateInfinityUserActivationCodeAndUsername(methodID, inputArray, request, response);
	}

	@Override
	public Object createRetailContract(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws ApplicationException {
		InfinityUserManagementResource managementResource = new InfinityUserManagementResourceImpl();
		return managementResource.createRetailContract(methodID, inputArray, request, response);
	}

	@Override
	public Object editInfinityUserWithContract(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response)
			throws JsonMappingException, JsonProcessingException, JSONException, ApplicationException {
		InfinityUserManagementResource managementResource = new InfinityUserManagementResourceImpl();
		return managementResource.editInfinityUserWithContract(methodId, inputArray, request, response);
	}

	@Override
	public Object applyCustomRole(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		InfinityUserManagementResource managementResource = new InfinityUserManagementResourceImpl();
		return managementResource.applyCustomRole(methodId, inputArray, request, response);
	}

	@Override
	public Result UpdateInfinityUserStatus(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws ApplicationException {
		InfinityUserManagementResource managementResource = new InfinityUserManagementResourceImpl();
		return managementResource.UpdateInfinityUserStatus(methodID, inputArray, request, response);
	}

	@Override
	public Result processOpenedNewAccounts(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws ApplicationException {
		InfinityUserManagementResource managementResource = new InfinityUserManagementResourceImpl();
		return managementResource.processOpenedNewAccounts(methodID, inputArray, request, response);
	}

	@Override
	public Result assignInfinityUserToPrimaryRetailContract(String methodID, Object[] inputArray,
			DataControllerRequest request, DataControllerResponse response) {
		InfinityUserManagementResource managementResource = new InfinityUserManagementResourceImpl();
		return managementResource.assignInfinityUserToPrimaryRetailContract(methodID, inputArray, request, response);
	}

	@Override
	public Result getUserApprovalPermissions(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws ApplicationException {
		InfinityUserManagementResource managementResource = new InfinityUserManagementResourceImpl();
		return managementResource.getUserApprovalPermissions(methodID, inputArray, dcRequest, dcResponse);
	}

	@Override
	public Result enrollRetailUserOperation(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws ApplicationException {
		Result result = new Result();
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		//String companyLegalUnit =  EnvironmentConfigurationsHandler.getValue(DBPUtilitiesConstants.BRANCH_ID_REFERENCE);
		
		LegalEntityUtil.addCompanyIDToHeaders(dcRequest);
		String lastName = StringUtils.isBlank(inputParams.get("lastName")) ? inputParams.get("lastName")
				: dcRequest.getParameter("lastName");
		String taxId = StringUtils.isBlank(inputParams.get("taxId")) ? inputParams.get("taxId")
				: dcRequest.getParameter("taxId");
		String dateOfBirth = StringUtils.isBlank(inputParams.get("dateOfBirth")) ? inputParams.get("dateOfBirth")
				: dcRequest.getParameter("dateOfBirth");
		String legalEntityId = StringUtils.isBlank(inputParams.get("legalEntityId")) ? inputParams.get("legalEntityId")
				: dcRequest.getParameter("legalEntityId");
		dcRequest.addRequestParam_(InfinityConstants.legalEntityId, legalEntityId);
		
		if (StringUtils.isBlank(lastName) || StringUtils.isBlank(taxId) || StringUtils.isBlank(dateOfBirth)) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10801);
		}

		InfinityUserManagementBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(InfinityUserManagementBusinessDelegate.class);
		InfinityUserManagementResource infinityUserManagementResource = DBPAPIAbstractFactoryImpl
				.getResource(InfinityUserManagementResource.class);
		InfinityUserManagementResourceImpl impl =new InfinityUserManagementResourceImpl();

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
			String infinityUserId = JSONUtil.getString(coreCustomerJson, "infinityUserId");

			if ("true".equalsIgnoreCase(JSONUtil.getString(coreCustomerJson, "isUserEnrolled"))) {
				result.addStringParam("isUserEnrolled", "true");
				return result;
				// throw new ApplicationException(ErrorCodeEnum.ERR_10802);
			}

			/**
			 * Enrolling the user
			 */
			if (StringUtils.isBlank(infinityUserId)) {
				ContractResource resource = DBPAPIAbstractFactoryImpl.getResource(ContractResource.class);
				ContractBusinessDelegate contractBusinessDelegate = DBPAPIAbstractFactoryImpl
						.getBusinessDelegate(ContractBusinessDelegate.class);
				Map<String, Object> contractPayload = impl.createContractPayload(coreCustomerJson, dcRequest,legalEntityId);
				contractPayload.put("legalEntityId", legalEntityId);
				inputArray[1] = contractPayload;
				dcRequest.addRequestParam_("isDefaultActionsEnabled", "true");
				result = resource.createContract(methodID, inputArray, dcRequest, dcResponse);
				contractBusinessDelegate.updateContractStatus(result.getParamValueByName("contractId"),
						DBPUtilitiesConstants.CONTRACT_STATUS_ACTIVE, dcRequest.getHeaderMap());
				dcRequest.addRequestParam_("legalEntityId", legalEntityId);
				
				infinityUserId = createAUserAndAssignTOGivenContract(contractPayload,
						JSONUtil.getString(coreCustomerJson, "id"),
						legalEntityId, dcRequest,
						JSONUtil.getString(coreCustomerJson, "partyId"));
			}

			Map<String, String> activationMap = new HashMap<>();
			activationMap.put(InfinityConstants.userId, infinityUserId);
			activationMap.put("Phone", JSONUtil.getString(coreCustomerJson, "phone"));
			activationMap.put("Email", JSONUtil.getString(coreCustomerJson, "email"));
			inputArray[1] = activationMap;
			Callable<Result> callable = new Callable<Result>() {
				public Result call() {
					try {
						infinityUserManagementResource.generateInfinityUserActivationCodeAndUsername(methodID,
								inputArray, dcRequest, dcResponse);
					} catch (Exception e) {
						diagnostic.prepareDebug("Exception occured while sending the activation code and username").log();
					}
					return new Result();
				}
			};
			try {
				ThreadExecutor.getExecutor().execute(callable);
			} catch (Exception e) {
				alert.prepareError("ThreadExecutor : Exception occured while generating the activation code").log();
			}
			result.addStringParam("isActivationCodeSent", "true");
			result.addStringParam("firstName", JSONUtil.getString(coreCustomerJson, "firstName"));
			result.addStringParam("lastName", JSONUtil.getString(coreCustomerJson, "lastName"));
			result.addStringParam("isUserExists", "true");
			result.addStringParam("legalEntityId", legalEntityId);

		} catch (ApplicationException e) {
			alert.prepareError("InfinityUserManagementResourceImpl : Exception occured while enrolling retail user"
					+ e.getMessage()).log();
			throw new ApplicationException(e.getErrorCodeEnum());
		} catch (Exception e) {
			alert.prepareError("InfinityUserManagementResourceImpl : Exception occured while enrolling retail user"
					+ e.getMessage()).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_10812);
		}
		return result;
	}



	@Override
	public Result createBusinessContract(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws ApplicationException {
		InfinityUserManagementResource managementResource = new InfinityUserManagementResourceImpl();
		return managementResource.createBusinessContract(methodID, inputArray, request, response);
	}

	@Override
	public Result getCoreCustomerAccounts(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws ApplicationException {
		InfinityUserManagementResource managementResource = new InfinityUserManagementResourceImpl();
		return managementResource.getCoreCustomerAccounts(methodId, inputArray, request, response);
	}

	@Override
	public Result getCoreCustomerDetails(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws ApplicationException {
		InfinityUserManagementResource managementResource = new InfinityUserManagementResourceImpl();
		return managementResource.getCoreCustomerDetails(methodId, inputArray, request, response);
	}

	@Override
	public Result getInfinityUserServiceDefsRoles(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		InfinityUserManagementResource managementResource = new InfinityUserManagementResourceImpl();
		return managementResource.getInfinityUserServiceDefsRoles(methodId, inputArray, request, response);
	}

	@Override
	public Result DBXCustomerCommunicationDetails(String methodId, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws ApplicationException {
		InfinityUserManagementResource managementResource = new InfinityUserManagementResourceImpl();
		return managementResource.DBXCustomerCommunicationDetails(methodId, inputArray, dcRequest, dcResponse);
	}

	@Override
	public void createAUserAndAssignTOGivenContract(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws ApplicationException {
		final String INPUT_AUTHORIZEDSIGNATORY = "authorizedSignatory";
		final String INPUT_AUTHORIZEDSIGNATORY_ROLES = "authorizedSignatoryRoles";
		final String INPUT_SERVICEKEY = "serviceKey";
		String backendId = null;

		InfinityUserManagementResourceImpl managementResource = new InfinityUserManagementResourceImpl();
		Set<String> createdValidContractCoreCustomers = request.getAttribute("createdValidCustomers");
		Map<String, Set<ContractAccountsDTO>> createdCoreCustomerAccounts = request
				.getAttribute("createdCustomerAccounts");
		String createdServiceType = request.getAttribute("serviceType");
		String contractId = request.getAttribute("contractId");
		String contractStatus = request.getAttribute("contractStatus");
		String companyId = request.getParameter("legalEntityId");
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		alert.prepareError("createdValidContractCoreCustomers"+createdValidContractCoreCustomers).log();
		alert.prepareError("createdCoreCustomerAccounts"+createdCoreCustomerAccounts).log();
		

		String authorizedSignatory = StringUtils.isNotBlank(inputParams.get(INPUT_AUTHORIZEDSIGNATORY))
				? inputParams.get(INPUT_AUTHORIZEDSIGNATORY)
				: request.getParameter(INPUT_AUTHORIZEDSIGNATORY);
		String authorizedSignatoryRoles = StringUtils.isNotBlank(inputParams.get(INPUT_AUTHORIZEDSIGNATORY_ROLES))
				? inputParams.get(INPUT_AUTHORIZEDSIGNATORY_ROLES)
				: request.getParameter(INPUT_AUTHORIZEDSIGNATORY_ROLES);

		String serviceKey = StringUtils.isNotBlank(inputParams.get(INPUT_SERVICEKEY))
				? inputParams.get(INPUT_SERVICEKEY)
				: request.getParameter(INPUT_SERVICEKEY);

		List<CustomerDTO> authorizedSignatoryList = DTOUtils.getDTOList(authorizedSignatory, CustomerDTO.class);

		if (authorizedSignatoryList == null || authorizedSignatoryList.isEmpty()) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10385);
		}

		Map<String, String> userToCoreCustomerRoles = managementResource.getUserToCoreCustomerRoles(authorizedSignatoryRoles);
		JsonObject payloadJson = managementResource.getPayload(serviceKey, request);

		StringBuilder email = new StringBuilder();
		StringBuilder phone = new StringBuilder();
		String userId = managementResource.createUser(authorizedSignatoryList.get(0), createdServiceType, request);
		managementResource.updateCommunicationDetails(phone, email, payloadJson);
		managementResource.createCustomerCommunication(email, phone, userId, request);
		managementResource.createUserRoles(userId, contractId, userToCoreCustomerRoles, request);
		managementResource.assignUserToContractCustomers(userId, contractId, createdValidContractCoreCustomers, request);
		managementResource.createUserAccounts(userId, contractId, createdCoreCustomerAccounts, request.getParameter("legalEntityId"), request);
		managementResource.createUserActionLimits(userId, contractId, request.getParameter("legalEntityId"), createdValidContractCoreCustomers, userToCoreCustomerRoles, request);
		managementResource.createCustomerPreference(userId, request.getHeaderMap());
		if (payloadJson.has(DBPUtilitiesConstants.BACKENDID)
				&& !payloadJson.get(DBPUtilitiesConstants.BACKENDID).isJsonNull()
				&& StringUtils.isNotBlank(payloadJson.get(DBPUtilitiesConstants.BACKENDID).getAsString())) {
			backendId = payloadJson.get(DBPUtilitiesConstants.BACKENDID).getAsString();
		}
		managementResource.createBackendIdentifierEntry(backendId, userId, contractId, request, companyId, null);
		managementResource.createBackendIdentifierForParty(userId, backendId, companyId);
		managementResource.deleteMfaService(serviceKey, request);

		request.addRequestParam_("userId", userId);
		request.addRequestParam_("contractStatus", contractStatus);
		request.addRequestParam_("contractId", contractId);

	}


	@Override
	public Result updateCustomerLanguage(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response)
			throws JsonMappingException, JsonProcessingException, ApplicationException {
		InfinityUserManagementResource managementResource = new InfinityUserManagementResourceImpl();
		return managementResource.updateCustomerLanguage(methodId, inputArray, request, response);
	
	}
	

}
