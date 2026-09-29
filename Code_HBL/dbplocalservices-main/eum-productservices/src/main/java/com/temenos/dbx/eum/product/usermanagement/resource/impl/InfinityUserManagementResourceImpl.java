package com.temenos.dbx.eum.product.usermanagement.resource.impl;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.infinity.dbx.dbp.jwt.auth.AuthConstants;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.apache.http.entity.ContentType;
import org.apache.poi.util.StringUtil;
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

import com.infinity.dbx.dbp.jwt.auth.Authentication;
import com.infinity.dbx.dbp.jwt.auth.utils.TemenosUtils;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.memorymanagement.MemoryManager;
import com.kony.dbputilities.sessionmanager.SessionScope;
import com.kony.dbputilities.util.BundleConfigurationHandler;
import com.kony.dbputilities.util.ConvertJsonToResult;
import com.kony.dbputilities.util.DBPDatasetConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.FeatureConfiguration;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.IntegrationTemplateURLFinder;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.LegalEntityUtil;
import com.kony.dbputilities.util.OperationName;
import com.kony.dbputilities.util.ServiceId;
import com.kony.dbputilities.util.TokenUtils;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.kony.eum.dbputilities.util.ServiceCallHelper;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.konylabs.middleware.registry.AppRegistryException;
import com.temenos.dbx.eum.product.contract.backenddelegate.api.ContractBackendDelegate;
import com.temenos.dbx.eum.product.contract.backenddelegate.api.ContractCoreCustomerBackendDelegate;
import com.temenos.dbx.eum.product.contract.backenddelegate.api.CoreCustomerBackendDelegate;
import com.temenos.dbx.eum.product.contract.backenddelegate.api.ServiceDefinitionBackendDelegate;
import com.temenos.dbx.eum.product.contract.businessdelegate.api.ContractAccountsBusinessDelegate;
import com.temenos.dbx.eum.product.contract.businessdelegate.api.ContractBusinessDelegate;
import com.temenos.dbx.eum.product.contract.businessdelegate.api.ContractFeatureActionsBusinessDelegate;
import com.temenos.dbx.eum.product.contract.businessdelegate.api.CoreCustomerBusinessDelegate;
import com.temenos.dbx.eum.product.contract.businessdelegate.api.ServiceDefinitionBusinessDelegate;
import com.temenos.dbx.eum.product.contract.resource.api.ContractResource;
import com.temenos.dbx.eum.product.usermanagement.backenddelegate.api.BackendIdentifiersBackendDelegate;
import com.temenos.dbx.eum.product.usermanagement.backenddelegate.api.InfinityUserManagementBackendDelegate;
import com.temenos.dbx.eum.product.usermanagement.backenddelegate.impl.InfinityUserManagementBackendDelegateImpl;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.CommunicationBusinessDelegate;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.CustomerAccountsBusinessDelegate;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.CustomerActionsBusinessDelegate;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.CustomerGroupBusinessDelegate;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.CustomerPreferenceBusinessDelegate;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.InfinityUserManagementBusinessDelegate;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.PushExternalEventBusinessDelegate;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.UserManagementBusinessDelegate;
import com.temenos.dbx.eum.product.usermanagement.resource.api.InfinityUserManagementResource;
import com.temenos.dbx.mfa.businessdelegate.api.MFAServiceBusinessDelegate;
import com.temenos.dbx.mfa.dto.MFAServiceDTO;
import com.temenos.dbx.mfa.utils.MFAServiceUtil;
import com.temenos.dbx.product.approvalmatrixservices.businessdelegate.api.ApprovalMatrixBusinessDelegate;
import com.temenos.dbx.product.businessdelegate.api.ApplicationBusinessDelegate;
import com.temenos.dbx.product.businessdelegate.api.FeatureBusinessDelegate;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.dbx.product.dto.AllAccountsViewDTO;
import com.temenos.dbx.product.dto.ApplicationDTO;
import com.temenos.dbx.product.dto.BackendIdentifierDTO;
import com.temenos.dbx.product.dto.ContractAccountsDTO;
import com.temenos.dbx.product.dto.ContractCustomersDTO;
import com.temenos.dbx.product.dto.CustomerAccountsDTO;
import com.temenos.dbx.product.dto.CustomerCommunicationDTO;
import com.temenos.dbx.product.dto.CustomerDTO;
import com.temenos.dbx.product.dto.CustomerGroupDTO;
import com.temenos.dbx.product.dto.CustomerLegalEntityDTO;
import com.temenos.dbx.product.dto.CustomerPreferenceDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.dto.FeatureActionLimitsDTO;
import com.temenos.dbx.product.dto.MembershipDTO;
import com.temenos.dbx.product.dto.ServiceDefinitionDTO;
import com.temenos.dbx.product.dto.UserCustomerViewDTO;
import com.temenos.dbx.product.integration.IntegrationMappings;
import com.temenos.dbx.product.signatorygroupservices.resource.api.SignatoryGroupResource;
import com.temenos.dbx.product.usermanagement.backenddelegate.api.CommunicationBackendDelegate;
import com.temenos.dbx.product.utils.CustomerCreationMode;
import com.temenos.dbx.product.utils.CustomerSessionsUtil;
import com.temenos.dbx.product.utils.DTOConstants;
import com.temenos.dbx.product.utils.DTOUtils;
import com.temenos.dbx.product.utils.InfinityConstants;
import com.temenos.dbx.product.utils.ThreadExecutor;

public class InfinityUserManagementResourceImpl implements InfinityUserManagementResource {
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final String DEFAULT_SERVICE_ID = "DEFAULT_SERVICE_ID";

	LoggerUtil logger = new LoggerUtil(InfinityUserManagementResourceImpl.class);
	SimpleDateFormat idFormatter = new SimpleDateFormat("yyMMddHHmmssSSS");

	private Set<String> allowedLanguages;

	public InfinityUserManagementResourceImpl() {
		allowedLanguages = new HashSet<>();
		allowedLanguages.add("US-English");
		allowedLanguages.add("UK-English");
		allowedLanguages.add("Spanish");
		allowedLanguages.add("German");
		allowedLanguages.add("French");
		allowedLanguages.add("Arabic");
		allowedLanguages.add("English");
		allowedLanguages.add("Nepalese");
		
	}
	
	@Override
	public Result updateCustomerLanguage(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response)
			throws JsonMappingException, JsonProcessingException, ApplicationException {

		Result result = null;
		InfinityUserManagementBusinessDelegate infinityUserManagementBusinessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(InfinityUserManagementBusinessDelegate.class);
		
		Map<String, String> map = HelperMethods.getInputParamMap(inputArray);

		Iterator<String> iterator = request.getParameterNames();

		while (iterator.hasNext()) {
			String key = iterator.next();
			if ((!map.containsKey(key) || StringUtils.isBlank(map.get(key)))
					&& StringUtils.isNotBlank(request.getParameter(key))) {
				map.put(key, request.getParameter(key));
			}
		}

		String defaultLanguage = map.get("defaultLanguage");
		if(StringUtils.isBlank(defaultLanguage) || !allowedLanguages.contains(defaultLanguage)) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10050);
		}
		String loggedInUserId = HelperMethods.getCustomerIdFromSession(request);
		CustomerDTO customer = new CustomerDTO();
		customer.setId(loggedInUserId);
		customer.setDefaultLanguage(defaultLanguage);
		DBXResult dbxResult = infinityUserManagementBusinessDelegate.updateCustomerLanguage(customer,
				request.getHeaderMap());
		
		if (dbxResult.getResponse() != null) {
			JsonObject jsonObject = (JsonObject) dbxResult.getResponse();

			result = JSONToResult.convert(jsonObject.toString());

			return result;

		}
		return result;
	}
	
	@Override
	public Result getInfinityUser(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws JsonMappingException, JsonProcessingException, ApplicationException {

		Result result = new Result();

		Map<String, String> map = HelperMethods.getInputParamMap(inputArray);

		Iterator<String> iterator = request.getParameterNames();

		JsonObject inputObject = new JsonObject();

		while (iterator.hasNext()) {
			String key = iterator.next();
			if ((!map.containsKey(key) || StringUtils.isBlank(map.get(key)))
					&& StringUtils.isNotBlank(request.getParameter(key))) {
				map.put(key, request.getParameter(key));
			}
		}

		String customerId = map.get(InfinityConstants.id);
		String coreCustomerId = map.get(InfinityConstants.coreCustomerId);
		String legalEntityId ="";
		
        if(StringUtils.isBlank(coreCustomerId))
        	coreCustomerId="";
        String contractId = map.get(InfinityConstants.contractId);
        if(StringUtils.isBlank(contractId))
        	contractId="";
        Boolean isSingleEntity = false;
        Result appInfo = null;
		try {
			appInfo = ServiceCallHelper.invokeServiceAndGetResult(null, null, URLConstants.APPLICATION_GET,
			        StringUtils.EMPTY);
		} catch (HttpCallException e) {
		}
		isSingleEntity = HelperMethods.getFieldValue(appInfo, "isSingleEntity")=="false"? false:true;
       
        diagnostic.prepareDebug("in class InfinityUserManagementResourceImpl.getInfinityUser" + contractId+ " "+ coreCustomerId).log();
        
		Map<String, String> loggedInUserInfo = HelperMethods.getCustomerFromAPIDBPIdentityService(request);
		if (HelperMethods.isPSD2Agent(request)) {
			inputObject.addProperty(InfinityConstants.isSuperAdmin, "true");
		} else if (HelperMethods.isAuthenticationCheckRequiredForService(loggedInUserInfo)) {
			Set<String> userPermissions = SessionScope.getAllPermissionsFromIdentityScope(request);
			if (!userPermissions.contains("USER_MANAGEMENT")) {
				ErrorCodeEnum.ERR_10051.setErrorCode(result);
				return result;
			}

			inputObject.addProperty(InfinityConstants.isSuperAdmin, "false");
		} else {
			inputObject.addProperty(InfinityConstants.isSuperAdmin, "true");
		}

		if (Boolean.parseBoolean(inputObject.get(InfinityConstants.isSuperAdmin).toString().replace("\"", "")))
			legalEntityId = LegalEntityUtil.getLegalEntityFromPayload(map, request);
		else if(request.containsKeyInRequest(InfinityConstants.legalEntityId) && isSingleEntity == false) {
			diagnostic.prepareDebug("in 11111").log();
            legalEntityId = LegalEntityUtil.getLegalEntityFromPayload(map, request);
		}
		else
			legalEntityId = LegalEntityUtil.getLegalEntityIdFromSessionOrCache(request);

		if (StringUtils.isBlank(legalEntityId)) {
			return ErrorCodeEnum.ERR_10001.setErrorCode(new Result());
		}

		List<String> allLegalEntities = LegalEntityUtil.getAllCompanyLegalUnits(request);
		if (!allLegalEntities.contains(legalEntityId)) {
			alert.prepareError("Logged in user do not have access to this legalEntity ").log();
			throw new ApplicationException(ErrorCodeEnum.ERR_12403);
		}
		
		String loggedInUserId = HelperMethods.getCustomerIdFromSession(request);

		if (StringUtils.isBlank(customerId)) {
			String userName = map.get(InfinityConstants.userName);
			if (StringUtils.isNotBlank(userName)) {
				CustomerDTO customerDTO = new CustomerDTO();
				customerDTO.setUserName(userName);
				customerDTO = (CustomerDTO) customerDTO.loadDTO();
				if (customerDTO != null) {
					customerId = customerDTO.getId();
				}
			}
		}

		if (StringUtils.isBlank(customerId)) {
			customerId = loggedInUserId;
		}

		try {
			LegalEntityUtil.addCompanyIDToHeaders(request);
			String isSuperAdmin = inputObject.get(InfinityConstants.isSuperAdmin).getAsString();
            if(isSuperAdmin.equals("false") && !StringUtils.isBlank(customerId)
                && !StringUtils.isBlank(loggedInUserId) && !StringUtils.equals(customerId, loggedInUserId)) {
				JsonObject userObject = new JsonObject();
				userObject.addProperty(InfinityConstants.id, loggedInUserId);
				userObject.addProperty(InfinityConstants.legalEntityId, legalEntityId);
				InfinityUserManagementBusinessDelegate userManagementBusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(InfinityUserManagementBusinessDelegate.class);
				DBXResult dbxRes = userManagementBusinessDelegate.getAssociatedContractUsers(userObject, request.getHeaderMap());
				if (dbxRes.getResponse() != null) {
					JsonObject jsonObject = (JsonObject) dbxRes.getResponse();
					if(jsonObject == null || !jsonObject.has(InfinityConstants.companyList)){
						return ErrorCodeEnum.ERR_10405.setErrorCode(new Result());
					}
					boolean isAssociatedUserForLoggedInUser = checkAssociatedUserForLoggedInUser(jsonObject, customerId);
					if(!isAssociatedUserForLoggedInUser){
						return ErrorCodeEnum.ERR_10404.setErrorCode(new Result());
					}
				} else {
					return ErrorCodeEnum.ERR_10405.setErrorCode(new Result());
				}
			}
		} catch(Exception e) {
			alert.prepareError("InfinityUserManagementResourceImpl : Exception occured while fetching the associated users for the loggedIn user "+ e.getMessage()).log();
			return ErrorCodeEnum.ERR_10405.setErrorCode(new Result());
		}

		inputObject.addProperty(InfinityConstants.id, customerId);
		inputObject.addProperty(InfinityConstants.coreCustomerId, coreCustomerId);
        inputObject.addProperty(InfinityConstants.contractId, contractId);
		inputObject.addProperty(InfinityConstants.loggedInUserId, loggedInUserId);
		inputObject.addProperty(InfinityConstants.legalEntityId, legalEntityId);

		if (StringUtils.isBlank(customerId)) {
			ErrorCodeEnum.ERR_10050.setErrorCode(result);
			return result;
		}

		InfinityUserManagementBusinessDelegate infinityUserManagementBusinessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(InfinityUserManagementBusinessDelegate.class);
		DBXResult dbxResult = infinityUserManagementBusinessDelegate.getInfinityUser(inputObject,
				request.getHeaderMap());

		if (dbxResult.getResponse() != null) {
			JsonObject jsonObject = (JsonObject) dbxResult.getResponse();
			if (methodId.equalsIgnoreCase("getInfinityUserFeatureActions")

					|| methodId.equalsIgnoreCase("getInfinityUserLimits")) {
				result = JSONToResult.convert(jsonObject.toString());

				return result;

			}
			//need to be updated
			addNewAccounts(jsonObject, inputObject, request);
			result = JSONToResult.convert(jsonObject.toString());
			Result result2 = DBPAPIAbstractFactoryImpl.getResource(SignatoryGroupResource.class)
					.fetchSignatoryGroupDetailsById(customerId);
			if (result2.getDatasetById(InfinityConstants.SignatoryGroups) != null) {
				result2.getDatasetById(InfinityConstants.SignatoryGroups).setId(InfinityConstants.signatoryGroups);
			}
			result.addAllDatasets(result2.getAllDatasets());
			result.addAllParams(result2.getAllParams());
			result.addAllRecords(result2.getAllRecords());
		}

		return result;
	}

	private boolean checkAssociatedUserForLoggedInUser(JsonObject infinityUserJsonObject, String userId) {
		if(infinityUserJsonObject.has(InfinityConstants.companyList)) {
			JsonArray companyList = infinityUserJsonObject.get(InfinityConstants.companyList).getAsJsonArray();
			for (int companyIndex = 0; companyIndex < companyList.size(); companyIndex++) {
				JsonObject companyListObject = companyList.get(companyIndex).getAsJsonObject();
				if (companyListObject.has(InfinityConstants.companies)) {
					JsonArray companies = companyListObject.get(InfinityConstants.companies).getAsJsonArray();
					for (int companiesIndex = 0; companiesIndex < companies.size(); companiesIndex++) {
						JsonObject companyObject = companies.get(companiesIndex).getAsJsonObject();
						if (companyObject.has(InfinityConstants.users)) {
							JsonArray usersList = companyObject.get(InfinityConstants.users).getAsJsonArray();
							for (int userIndex = 0; userIndex < usersList.size(); userIndex++) {
								JsonObject userObj = usersList.get(userIndex).getAsJsonObject();
								if (!userObj.has(InfinityConstants.infinityUserId)
										|| userObj.get(InfinityConstants.infinityUserId).isJsonNull()) {
									continue;
								}
								if (userObj.get(InfinityConstants.infinityUserId).getAsString().equals(userId)) {
									return true;
								}
							}
						}
					}
				}
			}
	    }
		return false;
	}

	private void addNewAccounts(JsonObject infinityUserJsonObject, JsonObject inputObject,
			DataControllerRequest request) throws ApplicationException, JsonMappingException, JsonProcessingException {

		Set<String> set = new HashSet<String>();
		JsonArray companyList = infinityUserJsonObject.get(InfinityConstants.companyList).getAsJsonArray();
		String legalEntityId = null;

		for (int i = 0; i < companyList.size(); i++) {
			JsonObject companyObject = companyList.get(i).getAsJsonObject();
			String cif = companyObject.get(InfinityConstants.cif).getAsString();

			boolean b = Boolean.parseBoolean(companyObject.get(InfinityConstants.autoSyncAccounts).getAsString());
			legalEntityId = companyObject.get(InfinityConstants.legalEntityId).getAsString();

			if (!set.contains(cif) && b) {
				set.add(cif);
			}
		}

		if (set.isEmpty()) {
			return;
		}

		CoreCustomerBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(CoreCustomerBusinessDelegate.class);
		ContractBackendDelegate contractBackendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(ContractBackendDelegate.class);

		Map<String, List<ContractAccountsDTO>> map = new HashMap<String, List<ContractAccountsDTO>>();
		try {
			map = businessDelegate.getAccountsWithImplicitAccountAccess(set,
					inputObject.get(InfinityConstants.id).getAsString(), legalEntityId, request.getHeaderMap());
		} catch (ApplicationException e1) {
			alert.prepareError("Exception", e1).log();
		}

		boolean isProductSpecificFeatueEnabled = FeatureConfiguration.isProductSpecificFeatueEnabled();
		boolean isAMSRoleFeatureEnabled = FeatureConfiguration.isAMSRoleFeatureEnabled();

		Map<String, Map<String, Set<String>>> productRefPermissions = null;

		Map<String, String> bundleConfigurations = null;
		Map<String, Set<String>> aaRolePermissions = null;

		if (isProductSpecificFeatueEnabled) {
			productRefPermissions = contractBackendDelegate.getProductLevelPermissions(new HashSet<>());
		}

		if (isAMSRoleFeatureEnabled) {
			bundleConfigurations = BundleConfigurationHandler
					.fetchBundleConfigurations(BundleConfigurationHandler.BUDLENAME_C360, request);
		}

		Set<String> accountSet = new HashSet<String>();
		for (int i = 0; i < companyList.size(); i++) {
			JsonObject companyObject = companyList.get(i).getAsJsonObject();
			Map<String, JsonElement> companyAccounts = new HashMap<>();
			JsonArray accounts = companyObject.get(InfinityConstants.accounts).getAsJsonArray();
			accountSet = new HashSet<String>();

			for (JsonElement element : accounts) {
				if (element.isJsonObject() && element.getAsJsonObject().has("accountId")) {
					companyAccounts.put(element.getAsJsonObject().get("accountId").getAsString(), element);
				}
				if (!accountSet.contains(element.getAsJsonObject().get(InfinityConstants.accountId).getAsString())) {
					accountSet.add(element.getAsJsonObject().get(InfinityConstants.accountId).getAsString());
					element.getAsJsonObject().addProperty(InfinityConstants.isNew, "false");
				}
			}

			String cif = companyObject.get(InfinityConstants.cif).getAsString();
			legalEntityId = companyObject.get(InfinityConstants.legalEntityId).getAsString();
			ContractBusinessDelegate contractBusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(ContractBusinessDelegate.class);

			String roleId = companyObject.get(InfinityConstants.roleId).getAsString();
			String serviceDefinition = companyObject.get(InfinityConstants.serviceDefinition).getAsString();

			if (map.containsKey(cif) && map.get(cif).size() > 0) {
				FeatureActionLimitsDTO coreCustomerFeatureActionDTO = null;
				Map<String, FeatureActionLimitsDTO> contractFeatureActionDTOMap = null;
				try {
					coreCustomerFeatureActionDTO = contractBackendDelegate.getRestrictiveFeatureActionLimits(
							serviceDefinition, null, roleId, cif, "", request.getHeaderMap(), false, "", legalEntityId);
//					contractFeatureActionDTOMap = contractBusinessDelegate.getContractActions(null, cif,
//							request.getHeaderMap());
				} catch (ApplicationException e) {
					alert.prepareError("Exception", e).log();
				}

				if (coreCustomerFeatureActionDTO == null) {
					continue;
				}

				JsonArray accountLevelFeatures = new JsonArray();
				Map<String, Set<String>> featureAction = coreCustomerFeatureActionDTO.getFeatureaction();
				diagnostic.prepareDebug("featureAction:" + featureAction).log();
				for (Entry<String, Set<String>> featureEntry : featureAction.entrySet()) {
					String featureId = featureEntry.getKey();
					JsonObject featureJson = coreCustomerFeatureActionDTO.getFeatureInfo().get(featureId);
					diagnostic.prepareDebug("featureJson:" + featureJson).log();
					JsonObject featureObjectAccount = new JsonObject();
					for (Entry<String, JsonElement> featureParams : featureJson.entrySet()) {
						featureObjectAccount.add(featureParams.getKey(), featureParams.getValue());
					}
					JsonArray accountpermissions = new JsonArray();
					for (String action : featureEntry.getValue()) {
						JsonObject actionJson = coreCustomerFeatureActionDTO.getActionsInfo().get(action);
						diagnostic.prepareDebug("actionJson:" + actionJson).log();
						boolean isAccountLevel = actionJson.has(InfinityConstants.isAccountLevel)
								&& !actionJson.get(InfinityConstants.isAccountLevel).isJsonNull()
								&& (("1".equals(actionJson.get(InfinityConstants.isAccountLevel).getAsString())) ||
										("true".equalsIgnoreCase(
												actionJson.get(InfinityConstants.isAccountLevel).getAsString())));
						if (isAccountLevel) {
							JsonObject actionObjectAccount = new JsonObject();
							for (Entry<String, JsonElement> actionParams : actionJson.entrySet()) {
								actionObjectAccount.add(actionParams.getKey(), actionParams.getValue());
							}
							actionObjectAccount.add(InfinityConstants.id, actionJson.get(InfinityConstants.actionId));
							actionObjectAccount.addProperty(InfinityConstants.isEnabled, "true");
							accountpermissions.add(actionObjectAccount);
						}
					}
					diagnostic.prepareDebug("accountpermissions:" + accountpermissions).log();
					if (accountpermissions.size() > 0) {
						featureObjectAccount.add(InfinityConstants.permissions, accountpermissions);
						accountLevelFeatures.add(featureObjectAccount);
					}
				}
				JsonArray monitoryFeatures = new JsonArray();

				Map<String, Map<String, Map<String, String>>> featureActionLimits = coreCustomerFeatureActionDTO
						.getMonetaryActionLimits();

				for (Entry<String, Map<String, Map<String, String>>> featureEntry : featureActionLimits.entrySet()) {

					String featureId = featureEntry.getKey();

					JsonObject featureJson = coreCustomerFeatureActionDTO.getFeatureInfo().get(featureId);

					for (Entry<String, Map<String, String>> actionEntry : featureEntry.getValue().entrySet()) {

						JsonObject feature = new JsonObject();

						for (Entry<String, JsonElement> featureParams : featureJson.entrySet()) {
							feature.add(featureParams.getKey(), featureParams.getValue());
						}

						JsonObject actionJson = coreCustomerFeatureActionDTO.getActionsInfo().get(actionEntry.getKey());

						for (Entry<String, JsonElement> actionParams : actionJson.entrySet()) {
							feature.add(actionParams.getKey(), actionParams.getValue());

						}

						feature.addProperty(InfinityConstants.isEnabled, "true");

						JsonArray limits = new JsonArray();

						for (Entry<String, String> limitsEntry : actionEntry.getValue().entrySet()) {
							JsonObject limit = new JsonObject();

							if (limitsEntry.getKey().equals(InfinityConstants.DAILY_LIMIT)) {
								limit.addProperty(InfinityConstants.id, InfinityConstants.PRE_APPROVED_DAILY_LIMIT);
								limit.addProperty(InfinityConstants.value, "0.0");
								limits.add(limit);
								limit = new JsonObject();
								limit.addProperty(InfinityConstants.id, InfinityConstants.AUTO_DENIED_DAILY_LIMIT);
								limit.addProperty(InfinityConstants.value, limitsEntry.getValue());
								limits.add(limit);
								limit = new JsonObject();
								limit.addProperty(InfinityConstants.id, InfinityConstants.DAILY_LIMIT);
								limit.addProperty(InfinityConstants.value, limitsEntry.getValue());
								limits.add(limit);
							} else if (limitsEntry.getKey().equals(InfinityConstants.WEEKLY_LIMIT)) {
								limit.addProperty(InfinityConstants.id, InfinityConstants.PRE_APPROVED_WEEKLY_LIMIT);
								limit.addProperty(InfinityConstants.value, "0.0");
								limits.add(limit);
								limit = new JsonObject();
								limit.addProperty(InfinityConstants.id, InfinityConstants.AUTO_DENIED_WEEKLY_LIMIT);
								limit.addProperty(InfinityConstants.value, limitsEntry.getValue());
								limits.add(limit);
								limit = new JsonObject();
								limit.addProperty(InfinityConstants.id, InfinityConstants.WEEKLY_LIMIT);
								limit.addProperty(InfinityConstants.value, limitsEntry.getValue());
								limits.add(limit);
							} else {
								limit.addProperty(InfinityConstants.id,
										InfinityConstants.PRE_APPROVED_TRANSACTION_LIMIT);
								limit.addProperty(InfinityConstants.value, "0.0");
								limits.add(limit);
								limit = new JsonObject();
								limit.addProperty(InfinityConstants.id,
										InfinityConstants.AUTO_DENIED_TRANSACTION_LIMIT);
								limit.addProperty(InfinityConstants.value, limitsEntry.getValue());
								limits.add(limit);
								limit = new JsonObject();
								limit.addProperty(InfinityConstants.id, InfinityConstants.MAX_TRANSACTION_LIMIT);
								limit.addProperty(InfinityConstants.value, limitsEntry.getValue());
								limits.add(limit);
							}

						}
						feature.add(InfinityConstants.limits, limits);
						monitoryFeatures.add(feature);
					}
				}

				JsonArray accountLevelPermissions = infinityUserJsonObject
						.get(InfinityConstants.accountLevelPermissions).getAsJsonArray();
				JsonArray accountsPermissions = new JsonArray();
				for (int j = 0; j < accountLevelPermissions.size(); j++) {
					if (accountLevelPermissions.get(j).getAsJsonObject().get(InfinityConstants.cif).getAsString()
							.equals(cif)) {
						accountsPermissions = accountLevelPermissions.get(j).getAsJsonObject()
								.get(InfinityConstants.accounts).getAsJsonArray();
					}
				}

				JsonArray accountLevelLimits = infinityUserJsonObject.get(InfinityConstants.transactionLimits)
						.getAsJsonArray();
				JsonArray accountsLimits = new JsonArray();
				for (int j = 0; j < accountLevelLimits.size(); j++) {
					if (accountLevelLimits.get(j).getAsJsonObject().get(InfinityConstants.cif).getAsString()
							.equals(cif)) {
						accountsLimits = accountLevelLimits.get(j).getAsJsonObject().get(InfinityConstants.accounts)
								.getAsJsonArray();
					}
				}
				for (ContractAccountsDTO accountsDTO : map.get(cif)) {
					if (accountsDTO != null && !accountSet.contains(accountsDTO.getAccountId())
							|| (StringUtils.isNotBlank(accountsDTO.getIsNew())
									&& accountsDTO.getIsNew().equalsIgnoreCase("1"))) {
						JsonObject accountObject = new JsonObject();
						accountObject.addProperty(InfinityConstants.accountId, accountsDTO.getAccountId());
						accountObject.addProperty(InfinityConstants.accountName, accountsDTO.getAccountName());
						accountObject.addProperty(InfinityConstants.accountType, accountsDTO.getAccountType());
						accountObject.addProperty(InfinityConstants.isEnabled, "true");
						accountObject.addProperty(InfinityConstants.isNew, "true");
						accountObject.addProperty(InfinityConstants.ownerType, accountsDTO.getOwnerType());
						if (!accountSet.contains(accountsDTO.getAccountId())) {
							accounts.add(accountObject);
						} else {
							JsonElement companyAccountNeedsModified = companyAccounts.get(accountsDTO.getAccountId());
							if (companyAccountNeedsModified != null && !companyAccountNeedsModified.isJsonNull()
									&& companyAccountNeedsModified.isJsonObject()) {
								companyAccountNeedsModified.getAsJsonObject().addProperty(InfinityConstants.isEnabled,
										"true");
								companyAccountNeedsModified.getAsJsonObject().addProperty(InfinityConstants.isNew,
										"true");
							}
						}
						JsonObject featureAccountObject = new JsonObject();
						featureAccountObject.addProperty(InfinityConstants.accountId, accountsDTO.getAccountId());
						featureAccountObject.addProperty(InfinityConstants.accountName, accountsDTO.getAccountName());
						featureAccountObject.addProperty(InfinityConstants.accountType, accountsDTO.getAccountType());
						featureAccountObject.add(InfinityConstants.featurePermissions, accountLevelFeatures);
						accountsPermissions.add(featureAccountObject);
						JsonObject featureLimitObject = new JsonObject();
						featureLimitObject.addProperty(InfinityConstants.accountId, accountsDTO.getAccountId());
						featureLimitObject.addProperty(InfinityConstants.accountName, accountsDTO.getAccountName());
						featureLimitObject.addProperty(InfinityConstants.accountType, accountsDTO.getAccountType());
						featureLimitObject.add(InfinityConstants.featurePermissions, monitoryFeatures);
						accountsLimits.add(featureLimitObject);
					}
				}
			}
		}
	}

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
		Boolean isSuperAdmin = false;
						
		Map<String, String> loggedInUserInfo = HelperMethods.getCustomerFromAPIDBPIdentityService(dcRequest);
		if (!HelperMethods.isAuthenticationCheckRequiredForService(loggedInUserInfo)) {
				isSuperAdmin =  true;
		}
		
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
		else {
			String integrationType = EnvironmentConfigurationsHandler.getServerProperty("ARRANGEMENTS_BACKEND");
			if ("MOCK".equalsIgnoreCase(integrationType)) {
				legalEntityId = LegalEntityUtil.getLegalEntityIdFromSession(dcRequest);
			} else
				legalEntityId = LegalEntityUtil.getLegalEntityIdFromSessionOrCache(dcRequest);
		}
		if (StringUtils.isBlank(legalEntityId)) {
			return ErrorCodeEnum.ERR_10001.setErrorCode(new Result());
		}

	        List<String> allLegalEntities = LegalEntityUtil.getAllCompanyLegalUnits(dcRequest);
			if (!allLegalEntities.contains(legalEntityId)) {
				alert.prepareError("Logged in user do not have access to this legalEntity ").log();
				throw new ApplicationException(ErrorCodeEnum.ERR_12403);
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
	        
	        List<String> allLegalEntities = LegalEntityUtil.getAllCompanyLegalUnits(dcRequest);
			if (!allLegalEntities.contains(legalEntityId)) {
				alert.prepareError("Logged in user do not have access to this legalEntity ").log();
				throw new ApplicationException(ErrorCodeEnum.ERR_12403);
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

		while (iterator.hasNext()) {
			String key = iterator.next();
			if ((!map.containsKey(key) || StringUtils.isBlank(map.get(key)))
					&& StringUtils.isNotBlank(request.getParameter(key))) {
				map.put(key, request.getParameter(key));
			}
		}
		LegalEntityUtil.addCompanyIDToHeaders(request);
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
		JsonElement userDetailsElement = new JsonParser().parse(userDetails);
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
			alert.prepareError("Exception", e).log();
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
		
		Set<String> allLegalEntities = LegalEntityUtil.getAllCompanyLegalUnits();
		try {
			userDetailsJsonObject = LegalEntityUtil.addLegalEntityToPermissionsIfReq(userDetailsJsonObject,
					isSuperAdmin, allLegalEntities, request);
		} catch (Exception e) {
			ErrorCodeEnum.ERR_29042.setErrorCode(result, "Failed to validate user details!");
			return result;
		}
		
		jsonObject.add(InfinityConstants.userDetails, userDetailsJsonObject);
		String signatoryGroups = map.get(InfinityConstants.signatoryGroups);
		jsonObject.addProperty(InfinityConstants.signatoryGroups, signatoryGroups);

		if (!validateUserDetails(userDetailsJsonObject, result)) {
			return result;
		}
		
		if (validateinput(jsonObject, result, map, request, isContractValidationRequired, null, allLegalEntities)) {
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
					updateSignatoryGroupEntry(customerId, jsonObject, result, request);
				}

				diagnostic.prepareDebug("Json response " + ResultToJSON.convert(result).toString()).log();
			}
		}

		return result;
	}

	public void updateSignatoryGroupEntry(String customerId, JsonObject jsonObject, Result result,
			DataControllerRequest request) {
		Map<String, String> loggedInUserInfo = HelperMethods.getCustomerFromAPIDBPIdentityService(request);
		if (HelperMethods.isAuthenticationCheckRequiredForService(loggedInUserInfo)) {
			return;
		}

		/*
		 * JsonArray signatoryGroups = jsonObject.has(InfinityConstants.signatoryGroups)
		 * && jsonObject.get(InfinityConstants.signatoryGroups).isJsonArray() ?
		 * jsonObject.get(InfinityConstants.signatoryGroups).getAsJsonArray() : new
		 * JsonArray();
		 */

		JsonArray signatoryGroups = new JsonArray();
		if (jsonObject.has(InfinityConstants.signatoryGroups)
				&& !jsonObject.get(InfinityConstants.signatoryGroups).isJsonNull()) {

			if (jsonObject.get(InfinityConstants.signatoryGroups).isJsonArray()) {
				signatoryGroups = jsonObject.get(InfinityConstants.signatoryGroups).getAsJsonArray();
			} else {
				try {
					signatoryGroups = new JsonParser()
							.parse(jsonObject.get(InfinityConstants.signatoryGroups).getAsString()).getAsJsonArray();
				} catch (Exception e) {
				}
			}
		}

		if (jsonObject.has(InfinityConstants.signatoryGroups) && signatoryGroups.size() == 0) {
			try {
				String src = "";
				if (jsonObject.has(InfinityConstants.signatoryGroups))
					src = jsonObject.get(InfinityConstants.signatoryGroups).getAsString();
				signatoryGroups = new JsonParser().parse(src).getAsJsonArray();
			} catch (Exception e) {
				signatoryGroups = new JsonArray();
			}
		}

		JSONArray signatoryGroups1 = new JSONArray();

		for (JsonElement element : signatoryGroups) {
			JsonObject signatory = element.getAsJsonObject();
			JSONObject signatory1 = new JSONObject();
			String contractId = signatory.get(InfinityConstants.contractId).getAsString();
			String cif = signatory.get(InfinityConstants.cif).getAsString();
			signatory1.put(InfinityConstants.coreCustomerId, cif);
			signatory1.put(InfinityConstants.cif, cif);
			signatory1.put(InfinityConstants.contractId, contractId);
			signatory1.put(InfinityConstants.customerId, customerId);
			JsonArray groups = signatory.get(InfinityConstants.groups).getAsJsonArray();
			String signatoryGroupId = "";
			if (groups.size() > 0) {
				for (JsonElement group : groups) {

					if (group.getAsJsonObject().has(InfinityConstants.signatoryGroupId)
							&& !group.getAsJsonObject().get(InfinityConstants.signatoryGroupId).isJsonNull()
							&& StringUtils.isNotBlank(
									group.getAsJsonObject().get(InfinityConstants.signatoryGroupId).getAsString())
							&& group.getAsJsonObject().has(InfinityConstants.isAssociated)
							&& !group.getAsJsonObject().get(InfinityConstants.isAssociated).isJsonNull()
							&& Boolean.parseBoolean(
									group.getAsJsonObject().get(InfinityConstants.isAssociated).getAsString())) {

						signatoryGroupId = group.getAsJsonObject().get(InfinityConstants.signatoryGroupId)
								.getAsString();
						signatory1.put(InfinityConstants.signatoryGroupId, signatoryGroupId);
						signatoryGroups1.put(signatory1);

					}
				}
			} else {
				signatory1.put(InfinityConstants.signatoryGroupId, signatoryGroupId);
				signatoryGroups1.put(signatory1);
			}

		}

		if (signatoryGroups1.length() > 0) {
			JSONObject signatory = new JSONObject();
			signatory.put(InfinityConstants.signatoryGroups, signatoryGroups1);
			Result result2 = DBPAPIAbstractFactoryImpl.getResource(SignatoryGroupResource.class)
					.updateSignatoryGroupForInfinityUser(signatory);
			 result.addAllDatasets(result2.getAllDatasets());
			 result.addAllParams(result2.getAllParams());
			 result.addAllRecords(result2.getAllRecords());
		}
	}

	private boolean validateRegex(String regex, String string) {
		string.trim();
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(string);
		return m.matches();
	}

	public boolean validateUserDetails(JsonObject userDetails, Result result) {
		String firstName = JSONUtil.getString(userDetails, InfinityConstants.firstName);
		String lastName = JSONUtil.getString(userDetails, InfinityConstants.lastName);
		String middleName = JSONUtil.getString(userDetails, InfinityConstants.middleName);
		String ssn = JSONUtil.getString(userDetails, InfinityConstants.ssn);
		String email = JSONUtil.getString(userDetails, InfinityConstants.email);
		String phoneCountryCode = JSONUtil.getString(userDetails, "phoneCountryCode");
		String phoneNumber = JSONUtil.getString(userDetails, "phoneNumber");
		String drivingLicenseNumber = JSONUtil.getString(userDetails, "drivingLicenseNumber");
		if (StringUtils.isNotBlank(firstName)) {
			if (!validateRegex("[A-Za-z0-9\\s]{1,20}$", firstName)) {
				ErrorCodeEnum.ERR_10050.setErrorCode(result, "Invalid first name");
				return false;
			}
		}
		if (StringUtils.isNotBlank(lastName)) {
			if (!validateRegex("[A-Za-z0-9\\s]{1,20}$", lastName)) {
				ErrorCodeEnum.ERR_10050.setErrorCode(result, "Invalid last name");
				return false;
			}
		}
		if (StringUtils.isNotBlank(middleName)) {
			if (!validateRegex("[A-Za-z0-9\\s]{1,20}$", middleName)) {
				ErrorCodeEnum.ERR_10050.setErrorCode(result, "Invalid middle name");
				return false;
			}
		}
		if (StringUtils.isNotBlank(ssn)) {
			if (!validateRegex("^(?:\\d{3}-\\d{2}-\\d{4}|\\d{9})$", ssn)) {
				ErrorCodeEnum.ERR_10050.setErrorCode(result, "Invalid ssn");
				return false;
			}
		}
		if (StringUtils.isNotBlank(email)) {
			if (!validateRegex("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]{1,20}@[a-zA-Z0-9.-]{1,15}+$", email)) {
				ErrorCodeEnum.ERR_10050.setErrorCode(result, "Invalid email");
				return false;
			}
		}
		if (StringUtils.isNotBlank(phoneCountryCode)) {
			if (!validateRegex("^\\+{1}[0-9]+$", phoneCountryCode)) {
				ErrorCodeEnum.ERR_10050.setErrorCode(result, "Invalid phone country code");
				return false;
			}
		}
		if (StringUtils.isNotBlank(phoneNumber)) {
			if (!validateRegex("^([0-9]){7,15}$", phoneNumber)) {
				ErrorCodeEnum.ERR_10050.setErrorCode(result, "Invalid phone number");
				return false;
			}
		}
		if (StringUtils.isNotBlank(drivingLicenseNumber)) {
			if (!validateRegex("^[^-_][a-zA-Z0-9\\s-]{1,20}$", drivingLicenseNumber)) {
				ErrorCodeEnum.ERR_10050.setErrorCode(result, "Invalid driving license number");
				return false;
			}
		}
		return true;
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

		diagnostic.prepareDebug("Json request " + jsonObject.toString()).log();
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
				diagnostic.prepareDebug("Json response " + ResultToJSON.convert(result).toString()).log();
			}
		}

		return result;
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
					alert.prepareError("Contract creation started....").log();
					try {
						result = resource.createContract(methodId, inputArray, request, response);
					} catch (ApplicationException e) {
						alert.prepareError("createContract exception", e).log();
						e.getErrorCodeEnum().setErrorCode(result);
					}

					if (!result.hasParamByName(InfinityConstants.contractId)
							|| StringUtils.isBlank(result.getParamValueByName(InfinityConstants.contractId))) {
						alert.prepareError("Contract creation failed....").log();
						return false;
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

	public boolean validateinput(JsonObject jsonObject, Result result, Map<String, String> map,
			DataControllerRequest dcRequest, Boolean isContractValidationRequired, String id, Set<String> allLegalEntities) throws ApplicationException, JsonMappingException, JsonProcessingException {

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
		Map<String, String> cifLegalEntityMap = new HashMap<>();
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
			
			cifLegalEntityMap.put(cif, legalEntityId);

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
				alert.prepareError("Exception", e).log();
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
		diagnostic.prepareDebug("cifLegalEntityMap : "+cifLegalEntityMap).log();
		
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
						String legalEntityId = cifLegalEntityMap.get(cif);
						if(!isSuperAdmin) {
							accountLevelPermissionsJsonObject.addProperty(InfinityConstants.legalEntityId, legalEntityId);
						} else {
							LegalEntityUtil.addLegalEntityToPermissionsIfReq(accountLevelPermissionsJsonObject, isSuperAdmin, allLegalEntities, dcRequest);
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
					Map<String, Map<String, Map<String, String>>> monitoryActions1 = new HashMap<>();
					if (contractFeatureActionDTO != null) {
						monitoryActions1 = contractFeatureActionDTO.getCoreCustomerTransactionLimits();
					}

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
							Map<String, Set<String>> accoutLevelActions1 =
									new HashMap<>();
							if (contractFeatureActionDTO != null) {
								accoutLevelActions1 = contractFeatureActionDTO.getAsscoiatedAccountActions()
										.get(accountId);
							}
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
					String legalEntityId = cifLegalEntityMap.get(cif);
					if(!isSuperAdmin) {
						globalLevelPermissionJsonObject.addProperty(InfinityConstants.legalEntityId, legalEntityId);
					} else {
						LegalEntityUtil.addLegalEntityToPermissionsIfReq(globalLevelPermissionJsonObject, isSuperAdmin, allLegalEntities, dcRequest);
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
				Map<String, Set<String>> globalLevelActions1 = new HashMap<>();
				if (contractFeatureActionDTO != null) {
					globalLevelActions1 = contractFeatureActionDTO.getCoreCustomerGlobalLevelPermissions();
				}
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
			JsonArray transactionLimitsArray = transactionLimitsElement.getAsJsonArray();
			for (int i = 0; i < transactionLimitsArray.size(); i++) {
				JsonObject transactionLimitsJsonObject = transactionLimitsArray.get(i).getAsJsonObject();
				String cif = transactionLimitsJsonObject.get(InfinityConstants.cif).getAsString();
				
				try {
					String legalEntityId = cifLegalEntityMap.get(cif);
					if(!isSuperAdmin) {
						transactionLimitsJsonObject.addProperty(InfinityConstants.legalEntityId, legalEntityId);
					} else {
						LegalEntityUtil.addLegalEntityToPermissionsIfReq(transactionLimitsJsonObject, isSuperAdmin, allLegalEntities, dcRequest);
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

	                			}

	                			if((user_maxtransactionlimit > user_dailylimit) || (user_dailylimit > user_weeklylimit)) {

	                				throw new ApplicationException(ErrorCodeEnum.ERR_10420);
	                			}
	                		}
	                	}
	                	catch (ApplicationException e) {
	                		alert.prepareError("Exception occured invalid limits" + e.getMessage()).log();                         	
	                    	ErrorCodeEnum.ERR_10420.setErrorCode(result);
	                        return false;
	                	}
	                }

				Map<String, Map<String, Map<String, String>>> transactionLimitsMap = featureActionLimitsDTO
						.getMonetaryActionLimits();
				
				Map<String, Map<String, Map<String, String>>> transactionLimitsMap1 =

						new HashMap<>();
				if (contractFeatureActionDTO != null) {
					transactionLimitsMap1 = contractFeatureActionDTO.getCoreCustomerTransactionLimits();
				}
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

						JsonArray featurePermissions = accountJsonObject.get(InfinityConstants.featurePermissions)
								.getAsJsonArray();

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
                            try {
                            	for (int z = 0; z < limits.size(); z++) {

                            		JsonObject accountlimit = limits.get(z).getAsJsonObject();

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
                            	}

                            	if((account_maxtransactionlimit > account_dailylimit) || (account_dailylimit > account_weeklylimit)) {
                            		throw new ApplicationException(ErrorCodeEnum.ERR_10420);

                            	}

                            	if((account_ADmaxtransactionlimit > account_maxtransactionlimit) || (account_ADdailylimit > account_dailylimit) || 
                            			(account_ADweeklylimit > account_weeklylimit)) {
                            		throw new ApplicationException(ErrorCodeEnum.ERR_10420);

                            	}

                            	if((account_PAmaxtransactionlimit > account_PAdailylimit) || (account_PAdailylimit > account_PAweeklylimit)) {         	
                            		throw new ApplicationException(ErrorCodeEnum.ERR_10420);
                            	}

                            	if((account_ADmaxtransactionlimit > account_ADdailylimit) || (account_ADdailylimit > account_ADweeklylimit)) {
                            		throw new ApplicationException(ErrorCodeEnum.ERR_10420);

                            	}

                            	if((account_PAmaxtransactionlimit > account_ADmaxtransactionlimit)|| (account_PAdailylimit > account_ADdailylimit) ||
                            			(account_PAweeklylimit > account_ADweeklylimit)) {  		
                            		throw new ApplicationException(ErrorCodeEnum.ERR_10420);

                            	}
                            }
                            catch (ApplicationException e) {
                            	alert.prepareError("Invalid limits" + e.getMessage()).log();                          	
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

	@Override
	public Result getCoreCustomerFeatureActionLimits(String methodId, Object[] inputArray,
			DataControllerRequest request, DataControllerResponse response) throws ApplicationException {
		DBXResult dbxresult = new DBXResult();
		Result result = new Result();
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		String coreCustomerId = StringUtils.isNotBlank(inputParams.get(InfinityConstants.coreCustomerId))
				? inputParams.get(InfinityConstants.coreCustomerId)
				: request.getParameter(InfinityConstants.coreCustomerId);
		String roleId = StringUtils.isNotBlank(inputParams.get(InfinityConstants.roleId))
				? inputParams.get(InfinityConstants.roleId)
				: request.getParameter(InfinityConstants.roleId);
				String legalEntityId ="";
		Boolean isSuperAdmin = LegalEntityUtil.checkForSuperAdmin(request);
		Boolean isSingleEntity = false;
		Result appInfo = null;
		try {
			appInfo = ServiceCallHelper.invokeServiceAndGetResult(null, null, URLConstants.APPLICATION_GET,
			        StringUtils.EMPTY);
		} catch (HttpCallException e) {
		}
		isSingleEntity = HelperMethods.getFieldValue(appInfo, "isSingleEntity")=="false"? false:true;

		if (isSuperAdmin)
			legalEntityId = LegalEntityUtil.getLegalEntityFromPayload(inputParams, request);
		else if(request.containsKeyInRequest(InfinityConstants.legalEntityId) && isSingleEntity == false) {
            legalEntityId = LegalEntityUtil.getLegalEntityFromPayload(inputParams, request);
		}
		else
			legalEntityId = LegalEntityUtil.getLegalEntityIdFromSessionOrCache(request);

		if (StringUtils.isBlank(legalEntityId)) {
			return ErrorCodeEnum.ERR_10001.setErrorCode(new Result());
		}

		List<String> allLegalEntities = LegalEntityUtil.getAllCompanyLegalUnits(request);
		if (!allLegalEntities.contains(legalEntityId)) {
			alert.prepareError("Logged in user do not have access to this legalEntity ").log();
			throw new ApplicationException(ErrorCodeEnum.ERR_12403);
		}
		if (StringUtils.isBlank(coreCustomerId) && StringUtils.isBlank(roleId)) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10768);
		}
		if (StringUtils.isBlank(legalEntityId)) {
			throw new ApplicationException(ErrorCodeEnum.ERR_29040);
		}
		try {
			InfinityUserManagementBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(InfinityUserManagementBusinessDelegate.class);
			dbxresult = businessDelegate.getCoreCustomerFeatureActionLimits(roleId, coreCustomerId, legalEntityId,
					HelperMethods.getUserIdFromSession(request), request.getHeaderMap());
			if (dbxresult != null && dbxresult.getResponse() != null) {
				result = ConvertJsonToResult.convert((JsonObject) dbxresult.getResponse());
			}
		} catch (ApplicationException e) {
			throw new ApplicationException(e.getErrorCodeEnum());
		} catch (Exception e) {
			alert.prepareError("Exception occured while fetching the restrictive feature action limits" + e.getMessage()).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_10767);
		}
		return result;
	}

	@Override
	public Result getCoreCustomerInformation(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws ApplicationException {
		Result result = new Result();
		DBXResult dbxresult = new DBXResult();
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		String coreCustomerId = StringUtils.isNotBlank(inputParams.get(InfinityConstants.coreCustomerId))
				? inputParams.get(InfinityConstants.coreCustomerId)
				: request.getParameter(InfinityConstants.coreCustomerId);
		String userId = StringUtils.isNotBlank(inputParams.get(InfinityConstants.id))
				? inputParams.get(InfinityConstants.id)
				: request.getParameter(InfinityConstants.id);
		String legalEntityId = StringUtils.isNotBlank(inputParams.get(InfinityConstants.legalEntityId))
				? inputParams.get(InfinityConstants.legalEntityId)
				: request.getParameter(InfinityConstants.legalEntityId);
		if (StringUtils.isBlank(coreCustomerId) && StringUtils.isBlank(userId)) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10772);
		}
		if(StringUtils.isBlank(legalEntityId)) {
			legalEntityId = LegalEntityUtil.getCurrentLegalEntityIdFromCache(request);
		}
		
		ContractCustomersDTO dto = new ContractCustomersDTO();
		dto.setCoreCustomerId(coreCustomerId);
		dto.setCustomerId(userId);
		dto.setCompanyLegalUnit(legalEntityId);
		try {
			InfinityUserManagementBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(InfinityUserManagementBusinessDelegate.class);
			dbxresult = businessDelegate.getCoreCustomerInformation(dto, request.getHeaderMap());

		} catch (ApplicationException e) {
			alert.prepareError("Exception occured while fetching the customer id's" + e.getMessage()).log();
			throw new ApplicationException(e.getErrorCodeEnum());
		} catch (Exception e) {
			alert.prepareError("Exception occured while fetching the customer id's" + e.getMessage()).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_10773);
		}
		if (dbxresult != null && dbxresult.getResponse() != null) {
			result = ConvertJsonToResult.convert((JsonObject) dbxresult.getResponse());
		}
		return result;
	}

	@Override
	public Result getInfinityUserContractCustomerDetails(String methodId, Object[] inputArray,
			DataControllerRequest dcRequest, DataControllerResponse dcResponse) throws ApplicationException {
		Result result = new Result();
		String customerId = HelperMethods.getCustomerIdFromSession(dcRequest);
		UserCustomerViewDTO dto = new UserCustomerViewDTO();
		dto.setCustomerId(customerId);
		DBXResult dbxresult = new DBXResult();
		try {
			InfinityUserManagementBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(InfinityUserManagementBusinessDelegate.class);
			dbxresult = businessDelegate.getInfinityUserContractCustomerDetails(dto, dcRequest.getHeaderMap());
			if (dbxresult != null && dbxresult.getResponse() != null) {
				result = ConvertJsonToResult.convert((JsonObject) dbxresult.getResponse());
			}
		} catch (ApplicationException e) {
			alert.prepareError(
					"InfinityUserManagementResourceImpl : Exception occured while fetching the user contract and contract customer details"
							+ e.getMessage()).log();
			throw new ApplicationException(e.getErrorCodeEnum());
		} catch (Exception e) {
			alert.prepareError(
					"InfinityUserManagementResourceImpl : Exception occured while fetching the user contract and contract customer details"
							+ e.getMessage()).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_10776);
		}
		return result;
	}

	@Override
	public Result createCustomRole(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {

		Result result = new Result();
		try {
			// code to check create custom role permission
			List<String> featureActionIdList = new ArrayList<>();
			featureActionIdList.add("CUSTOM_ROLES_CREATE");
			String featureActionId = CustomerSession.getPermittedActionIds(request, featureActionIdList);
			if (featureActionId == null) {
				return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
			}

			JsonObject jsonObject = new JsonObject();

			Map<String, String> map = HelperMethods.getInputParamMap(inputArray);

			Iterator<String> iterator = request.getParameterNames();

			while (iterator.hasNext()) {
				String key = iterator.next();
				if ((!map.containsKey(key) || StringUtils.isBlank(map.get(key)))
						&& StringUtils.isNotBlank(request.getParameter(key))) {
					map.put(key, request.getParameter(key));
				}
			}

			Boolean isContractValidationRequired = true;

			if (!map.containsKey(InfinityConstants.customRoleDetails)
					|| StringUtils.isBlank(map.get(InfinityConstants.customRoleDetails))) {
				ErrorCodeEnum.ERR_10056.setErrorCode(result);
				return result;
			}

			String customRoleDetails = map.get(InfinityConstants.customRoleDetails);
			JsonElement customRoleDetailsElement = new JsonParser().parse(customRoleDetails);
			if (customRoleDetailsElement.isJsonNull() || !customRoleDetailsElement.isJsonObject()) {
				ErrorCodeEnum.ERR_10056.setErrorCode(result);
				return result;
			}

			JsonObject customRoleDetailsJsonObject = customRoleDetailsElement.getAsJsonObject();

			String customRoleName = customRoleDetailsJsonObject.has(InfinityConstants.customRoleName)
					&& !customRoleDetailsJsonObject.get(InfinityConstants.customRoleName).isJsonNull()
							? customRoleDetailsJsonObject.get(InfinityConstants.customRoleName).getAsString()
							: "";
			String description = customRoleDetailsJsonObject.has(InfinityConstants.description)
					&& !customRoleDetailsJsonObject.get(InfinityConstants.description).isJsonNull()
							? customRoleDetailsJsonObject.get(InfinityConstants.description).getAsString()
							: "";

			if (customRoleName == null) {
				return ErrorCodeEnum.ERR_21104.setErrorCode(new Result());
			}
			if (!Pattern.matches("^[a-zA-Z0-9 ]+$", description)
					|| !Pattern.matches("^[a-zA-Z0-9 ]+$", customRoleName)) {
				return ErrorCodeEnum.ERR_21107.setErrorCode(new Result());
			}

			jsonObject.add(InfinityConstants.customRoleDetails, customRoleDetailsJsonObject);

			jsonObject.addProperty(InfinityConstants.name, customRoleName);
			
			String legalEntityId = LegalEntityUtil.getLegalEntityIdFromSessionOrCache(request);

	        if(StringUtils.isBlank(legalEntityId)) {
	        	return ErrorCodeEnum.ERR_10001.setErrorCode(new Result());
	        }
	        
	        jsonObject.addProperty(InfinityConstants.legalEntityId, legalEntityId);
			
			InfinityUserManagementBusinessDelegate infinityUserManagementBusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(InfinityUserManagementBusinessDelegate.class);
			DBXResult dbxResult = infinityUserManagementBusinessDelegate.verifyCustomRole(jsonObject,
					request.getHeaderMap());
			if (dbxResult.getResponse() != null) {
				ErrorCodeEnum.ERR_21107.setErrorCode(result, "Custom Role already present with the name");
				return result;
			}

			Set<String> allLegalEntities = LegalEntityUtil.getAllCompanyLegalUnits();
			if (validateinput(jsonObject, result, map, request, isContractValidationRequired, null, allLegalEntities)) {
				String name = (String) CustomerSessionsUtil.getLoggedInUserAttributesMap(request)
						.get(InfinityConstants.UserName);
				jsonObject.addProperty(InfinityConstants.createdby, name);
				dbxResult = infinityUserManagementBusinessDelegate.createCustomRole(jsonObject, request.getHeaderMap());
				if (dbxResult.getResponse() != null) {
					jsonObject = (JsonObject) dbxResult.getResponse();
					result = JSONToResult.convert(jsonObject.toString());
					String customRoleId = jsonObject.get(InfinityConstants.id).getAsString();
					String signatoryGroups = map.get(InfinityConstants.signatoryGroups);
					if (StringUtils.isNotBlank(signatoryGroups)) {
						JsonElement signatoryGroupsElement = new JsonParser().parse(signatoryGroups);
						if (!signatoryGroupsElement.isJsonNull() && signatoryGroupsElement.isJsonArray()) {
							JsonArray customRoleSignatoryGroupsObject = signatoryGroupsElement.getAsJsonArray();
							updateSignatoryGroups(customRoleId, customRoleSignatoryGroupsObject, request);
						}
					}
				}

			}

		} catch (Exception exp) {
			alert.prepareError("Exception occured while invoking resources of createCustomRole", exp).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}
		return result;
	}

	private void updateSignatoryGroups(String customRoleId, JsonArray customRoleSignatoryGroupsObject,
			DataControllerRequest request) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		String customerId = "";
		try {
			customerId = request.getServicesManager().getIdentityHandler().getUserId();
		} catch (AppRegistryException e) {
			alert.prepareError("Exception", e).log();
		}
		if (StringUtils.isBlank(customerId)) {
			return;
		}

		Map<String, Object> inputMap = new HashMap<String, Object>();

		for (JsonElement element : customRoleSignatoryGroupsObject) {
			JsonObject signatory = element.getAsJsonObject();
			String contractId = signatory.has(InfinityConstants.contractId)
					&& !signatory.get(InfinityConstants.contractId).isJsonNull()
							? signatory.get(InfinityConstants.contractId).getAsString()
							: null;

			String cif = signatory.has(InfinityConstants.cif) && !signatory.get(InfinityConstants.cif).isJsonNull()
					? signatory.get(InfinityConstants.cif).getAsString()
					: null;

			if (StringUtils.isBlank(contractId) || StringUtils.isBlank(cif) || !signatory.has(InfinityConstants.groups)
					|| !signatory.get(InfinityConstants.groups).isJsonArray())
				continue;
			inputMap.put(InfinityConstants.coreCustomerId, cif);
			inputMap.put(InfinityConstants.cif, cif);
			inputMap.put(InfinityConstants.contractId, contractId);
			String filter = InfinityConstants.coreCustomerId + DBPUtilitiesConstants.EQUAL + cif
					+ DBPUtilitiesConstants.AND + InfinityConstants.contractId + DBPUtilitiesConstants.EQUAL
					+ contractId;
			map.put(DBPUtilitiesConstants.FILTER, filter);
			JsonObject jsonObject = ServiceCallHelper.invokeServiceAndGetJson(map, request.getHeaderMap(),
					URLConstants.SIGNATORYGROUP_GET);

			Set<String> signatoryGroups = new HashSet<String>();
			// Set<String> validSignatoryGroups = new HashSet<String>();
			if (jsonObject.has(DBPDatasetConstants.SIGNATORYGROUP)
					&& jsonObject.get(DBPDatasetConstants.SIGNATORYGROUP).isJsonArray()
					&& jsonObject.get(DBPDatasetConstants.SIGNATORYGROUP).getAsJsonArray().size() > 0) {
				JsonArray groups = jsonObject.get(DBPDatasetConstants.SIGNATORYGROUP).getAsJsonArray();
				for (JsonElement group : groups) {
					if (group.isJsonObject()) {
						if (group.getAsJsonObject().has(InfinityConstants.signatoryGroupId)
								&& !group.getAsJsonObject().get(InfinityConstants.signatoryGroupId).isJsonNull()) {
							signatoryGroups
									.add(group.getAsJsonObject().get(InfinityConstants.signatoryGroupId).getAsString());
						}
					}
				}
			}

			boolean isAssociatedAtleastOne = false;

			if (signatoryGroups.size() > 0) {

				// map = new HashMap<String, Object>();
				//
				// filter = InfinityConstants.customerId + DBPUtilitiesConstants.EQUAL +
				// customerId;
				// map.put(DBPUtilitiesConstants.FILTER, filter);
				//
				// jsonObject = ServiceCallHelper.invokeServiceAndGetJson(map,
				// request.getHeaderMap(),
				// URLConstants.CUSTOMER_SIGNATORY_GROUP_GET);
				//
				// if (jsonObject.has(DBPDatasetConstants.CUSTOMER_SIGNATORY_GROUP)
				// && jsonObject.get(DBPDatasetConstants.CUSTOMER_SIGNATORY_GROUP).isJsonArray()
				// &&
				// jsonObject.get(DBPDatasetConstants.CUSTOMER_SIGNATORY_GROUP).getAsJsonArray().size()
				// > 0) {
				// JsonArray groups =
				// jsonObject.get(DBPDatasetConstants.CUSTOMER_SIGNATORY_GROUP).getAsJsonArray();
				// for (JsonElement group : groups) {
				// if (group.isJsonObject()) {
				// if (group.getAsJsonObject().has(InfinityConstants.signatoryGroupId)
				// &&
				// !group.getAsJsonObject().get(InfinityConstants.signatoryGroupId).isJsonNull())
				// {
				// if (signatoryGroups.contains(group.getAsJsonObject()
				// .get(InfinityConstants.signatoryGroupId).getAsString())) {
				// validSignatoryGroups.add(group.getAsJsonObject()
				// .get(InfinityConstants.signatoryGroupId).getAsString());
				// }
				// }
				// }
				// }
				// }

				JsonArray groups = signatory.get(InfinityConstants.groups).getAsJsonArray();
				String signatoryGroupId = "";

				if (groups.size() > 0) {
					for (JsonElement group : groups) {
						if (group.getAsJsonObject().has(InfinityConstants.signatoryGroupId)
								&& !group.getAsJsonObject().get(InfinityConstants.signatoryGroupId).isJsonNull()
								&& StringUtils.isNotBlank(group.getAsJsonObject()
										.get(InfinityConstants.signatoryGroupId).getAsString())) {
							boolean isAssociated = false;
							signatoryGroupId = group.getAsJsonObject().get(InfinityConstants.signatoryGroupId)
									.getAsString();

							if (group.getAsJsonObject().has(InfinityConstants.isAssociated)
									&& !group.getAsJsonObject().get(InfinityConstants.isAssociated).isJsonNull()
									&& Boolean.parseBoolean(group.getAsJsonObject().get(InfinityConstants.isAssociated)
											.getAsString())) {

								isAssociated = true;
								isAssociatedAtleastOne = true;
								inputMap.clear();
								inputMap.put(DBPUtilitiesConstants.FILTER,
										InfinityConstants.customRoleId + DBPUtilitiesConstants.EQUAL + customRoleId
												+ DBPUtilitiesConstants.OR
												+ InfinityConstants.customroleSignatoryGroupId
												+ DBPUtilitiesConstants.EQUAL + customRoleId);
								ServiceCallHelper.invokeServiceAndGetJson(inputMap, request.getHeaderMap(),
										URLConstants.CUSTOMROLESIGNATORYGROUP_DELETE);
							}

							if (isAssociated && signatoryGroups.contains(signatoryGroupId)) {
								inputMap.clear();
								inputMap.put(InfinityConstants.customroleSignatoryGroupId,
										customRoleId + "_" + signatoryGroupId);
								inputMap.put(InfinityConstants.customRoleId, customRoleId);
								inputMap.put(InfinityConstants.signatoryGroupId, signatoryGroupId);
								ServiceCallHelper.invokeServiceAndGetJson(inputMap, request.getHeaderMap(),
										URLConstants.CUSTOMROLESIGNATORYGROUP_CREATE);
								isAssociated = false;
							}
						}
					}
				}
			}

			if (!isAssociatedAtleastOne) {
				inputMap.clear();
				inputMap.put(DBPUtilitiesConstants.FILTER,
						InfinityConstants.customRoleId + DBPUtilitiesConstants.EQUAL + customRoleId
								+ DBPUtilitiesConstants.OR + InfinityConstants.customroleSignatoryGroupId
								+ DBPUtilitiesConstants.EQUAL + customRoleId);
				ServiceCallHelper.invokeServiceAndGetJson(inputMap, request.getHeaderMap(),
						URLConstants.CUSTOMROLESIGNATORYGROUP_DELETE);
			}
		}
	}

	@Override
	public Result editCustomRole(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		try {
			// code to check create custom role permission
			List<String> featureActionIdList = new ArrayList<>();
			featureActionIdList.add("CUSTOM_ROLES_CREATE");
			String featureActionId = CustomerSession.getPermittedActionIds(request, featureActionIdList);
			if (featureActionId == null) {
				return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
			}

			JsonObject jsonObject = new JsonObject();

			Map<String, String> map = HelperMethods.getInputParamMap(inputArray);

			Iterator<String> iterator = request.getParameterNames();

			while (iterator.hasNext()) {
				String key = iterator.next();
				if ((!map.containsKey(key) || StringUtils.isBlank(map.get(key)))
						&& StringUtils.isNotBlank(request.getParameter(key))) {
					map.put(key, request.getParameter(key));
				}
			}

			Boolean isContractValidationRequired = true;

			if (!map.containsKey(InfinityConstants.customRoleDetails)
					|| StringUtils.isBlank(map.get(InfinityConstants.customRoleDetails))) {
				ErrorCodeEnum.ERR_10056.setErrorCode(result);
				return result;
			}

			String customRoleDetails = map.get(InfinityConstants.customRoleDetails);
			JsonElement customRoleDetailsElement = new JsonParser().parse(customRoleDetails);
			if (customRoleDetailsElement.isJsonNull() || !customRoleDetailsElement.isJsonObject()) {
				ErrorCodeEnum.ERR_10056.setErrorCode(result);
				return result;
			}

			JsonObject customRoleDetailsJsonObject = customRoleDetailsElement.getAsJsonObject();

			String id = customRoleDetailsJsonObject.has(InfinityConstants.id)
					&& !customRoleDetailsJsonObject.get(InfinityConstants.id).isJsonNull()
							? customRoleDetailsJsonObject.get(InfinityConstants.id).getAsString()
							: "";

			if (StringUtils.isBlank(id)) {
				return ErrorCodeEnum.ERR_21104.setErrorCode(new Result());
			}

			jsonObject.add(InfinityConstants.customRoleDetails, customRoleDetailsJsonObject);
			
			String legalEntityId = LegalEntityUtil.getLegalEntityIdFromSessionOrCache(request);

	        if(StringUtils.isBlank(legalEntityId)) {
	        	return ErrorCodeEnum.ERR_10001.setErrorCode(new Result());
	        }
	        
	        jsonObject.addProperty(InfinityConstants.legalEntityId, legalEntityId);

			Set<String> allLegalEntities = LegalEntityUtil.getAllCompanyLegalUnits();
			if (validateinput(jsonObject, result, map, request, isContractValidationRequired, null, allLegalEntities)) {
				InfinityUserManagementBusinessDelegate infinityUserManagementBusinessDelegate = DBPAPIAbstractFactoryImpl
						.getBusinessDelegate(InfinityUserManagementBusinessDelegate.class);
				DBXResult dbxResult = infinityUserManagementBusinessDelegate.editCustomRole(jsonObject,
						request.getHeaderMap());
				if (dbxResult.getResponse() != null) {
					jsonObject = (JsonObject) dbxResult.getResponse();
					result = JSONToResult.convert(jsonObject.toString());
					String signatoryGroups = map.get(InfinityConstants.signatoryGroups);
					JsonElement signatoryGroupsElement = new JsonParser().parse(signatoryGroups);
					if (!signatoryGroupsElement.isJsonNull() && signatoryGroupsElement.isJsonArray()) {
						JsonArray customRoleSignatoryGroupsObject = signatoryGroupsElement.getAsJsonArray();
						updateSignatoryGroups(id, customRoleSignatoryGroupsObject, request);
					}
				}
			}

		} catch (Exception exp) {
			alert.prepareError("Exception occured while invoking resources of editCustomRole", exp).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}
		return result;
	}

	@Override
	public Result verifyCustomRole(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		try {
			// code to check create custom role permission
			List<String> featureActionIdList = new ArrayList<>();
			// featureActionIdList.add("CUSTOM_ROLES_VIEW");
			// String featureActionId = CustomerSession.getPermittedActionIds(request,
			// featureActionIdList);
			// if (featureActionId == null) {
			// return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
			// }

			JsonObject jsonObject = new JsonObject();

			Map<String, String> map = HelperMethods.getInputParamMap(inputArray);

			Iterator<String> iterator = request.getParameterNames();

			while (iterator.hasNext()) {
				String key = iterator.next();
				if ((!map.containsKey(key) || StringUtils.isBlank(map.get(key)))
						&& StringUtils.isNotBlank(request.getParameter(key))) {
					map.put(key, request.getParameter(key));
				}
			}

			if (!map.containsKey(InfinityConstants.name)) {
				return result;
			}

			jsonObject.addProperty(InfinityConstants.name, map.get(InfinityConstants.name));

			InfinityUserManagementBusinessDelegate infinityUserManagementBusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(InfinityUserManagementBusinessDelegate.class);
			DBXResult dbxResult = infinityUserManagementBusinessDelegate.verifyCustomRole(jsonObject,
					request.getHeaderMap());
			if (dbxResult.getResponse() != null) {
				result.addParam(InfinityConstants.isDuplicate, Boolean.TRUE.toString());
			} else
				result.addParam(InfinityConstants.isDuplicate, Boolean.FALSE.toString());

		} catch (Exception exp) {
			alert.prepareError("Exception occured while invoking resources of verifyCustomRole", exp).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}
		return result;
	}

	@Override
	public Result getCustomRole(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		try {
			// code to check create custom role permission
			List<String> featureActionIdList = new ArrayList<>();
			featureActionIdList.add("CUSTOM_ROLES_VIEW");
			// String featureActionId = CustomerSession.getPermittedActionIds(request,
			// featureActionIdList);
			// if (featureActionId == null) {
			// return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
			// }

			JsonObject jsonObject = new JsonObject();

			Map<String, String> map = HelperMethods.getInputParamMap(inputArray);

			Iterator<String> iterator = request.getParameterNames();
			
			String legalEntityId = "";
			Boolean isSingleEntity = false;
			Result appInfo = null;
			try {
				appInfo = ServiceCallHelper.invokeServiceAndGetResult(null, null, URLConstants.APPLICATION_GET,
				        StringUtils.EMPTY);
			} catch (HttpCallException e) {
			}
			isSingleEntity = HelperMethods.getFieldValue(appInfo, "isSingleEntity")=="false"? false:true;

			while (iterator.hasNext()) {
				String key = iterator.next();
				if ((!map.containsKey(key) || StringUtils.isBlank(map.get(key)))
						&& StringUtils.isNotBlank(request.getParameter(key))) {
					map.put(key, request.getParameter(key));
				}
			}

			if (!map.containsKey(InfinityConstants.id)) {
				return result;
			}
			
			if(request.containsKeyInRequest(InfinityConstants.legalEntityId) && isSingleEntity == false) {
	            legalEntityId = LegalEntityUtil.getLegalEntityFromPayload(map, request);
			}
			else
				legalEntityId = LegalEntityUtil.getLegalEntityIdFromSessionOrCache(request);

			jsonObject.addProperty(InfinityConstants.id, map.get(InfinityConstants.id));

			jsonObject.addProperty(InfinityConstants.customerId, HelperMethods.getCustomerIdFromSession(request));
			jsonObject.addProperty(InfinityConstants.legalEntityId,legalEntityId );

			InfinityUserManagementBusinessDelegate infinityUserManagementBusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(InfinityUserManagementBusinessDelegate.class);
			DBXResult dbxResult = infinityUserManagementBusinessDelegate.getCustomRole(jsonObject,
					request.getHeaderMap());
			if (dbxResult.getResponse() != null) {
				jsonObject = (JsonObject) dbxResult.getResponse();
				jsonObject = getSignatoryGroups(map.get(InfinityConstants.id), jsonObject, request);
				result = JSONToResult.convert(jsonObject.toString());

			}

		} catch (Exception exp) {
			alert.prepareError("Exception occured while invoking resources of getCustomRole", exp).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}
		return result;
	}

	private JsonObject getSignatoryGroups(String customRoleId, JsonObject jsonObject, DataControllerRequest request) {

		JsonArray signatoryGroups = new JsonArray();

		JsonArray companyList = jsonObject.get(InfinityConstants.companyList).getAsJsonArray();

		for (JsonElement companyElement : companyList) {

			JsonObject compJsonObject = companyElement.getAsJsonObject();
			JsonObject signatoryObject = new JsonObject();

			signatoryObject.add(InfinityConstants.companyName, compJsonObject.get(InfinityConstants.companyName));
			signatoryObject.add(InfinityConstants.coreCustomerName, compJsonObject.get(InfinityConstants.companyName));
			signatoryObject.add(InfinityConstants.contractName, compJsonObject.get(InfinityConstants.contractName));
			signatoryObject.add(InfinityConstants.cif, compJsonObject.get(InfinityConstants.cif));
			signatoryObject.add(InfinityConstants.coreCustomerId, compJsonObject.get(InfinityConstants.cif));
			signatoryObject.add(InfinityConstants.contractId, compJsonObject.get(InfinityConstants.contractId));
			String cif = compJsonObject.has(InfinityConstants.cif)
					&& !compJsonObject.get(InfinityConstants.cif).isJsonNull()
							? compJsonObject.get(InfinityConstants.cif).getAsString()
							: null;

			String contractId = compJsonObject.has(InfinityConstants.contractId)
					&& !compJsonObject.get(InfinityConstants.contractId).isJsonNull()
							? compJsonObject.get(InfinityConstants.contractId).getAsString()
							: null;

			Map<String, Object> map = new HashMap<String, Object>();

			String filter = InfinityConstants.coreCustomerId + DBPUtilitiesConstants.EQUAL + cif
					+ DBPUtilitiesConstants.AND + InfinityConstants.contractId + DBPUtilitiesConstants.EQUAL
					+ contractId;
			map.put(DBPUtilitiesConstants.FILTER, filter);
			JsonObject result = ServiceCallHelper.invokeServiceAndGetJson(map, request.getHeaderMap(),
					URLConstants.SIGNATORYGROUP_GET);

			Map<String, JsonObject> validSignatoryGroups = new HashMap<String, JsonObject>();
			if (result.has(DBPDatasetConstants.SIGNATORYGROUP)
					&& result.get(DBPDatasetConstants.SIGNATORYGROUP).isJsonArray()
					&& result.get(DBPDatasetConstants.SIGNATORYGROUP).getAsJsonArray().size() > 0) {
				JsonArray groups = result.get(DBPDatasetConstants.SIGNATORYGROUP).getAsJsonArray();
				for (JsonElement group : groups) {
					if (group.isJsonObject()) {
						if (group.getAsJsonObject().has(InfinityConstants.signatoryGroupId)
								&& !group.getAsJsonObject().get(InfinityConstants.signatoryGroupId).isJsonNull()) {
							group.getAsJsonObject().addProperty(InfinityConstants.isAssociated, "false");
							validSignatoryGroups.put(
									group.getAsJsonObject().get(InfinityConstants.signatoryGroupId).getAsString(),
									group.getAsJsonObject());
						}
					}
				}
			}

			if (validSignatoryGroups.size() > 0) {

				map = new HashMap<String, Object>();

				filter = InfinityConstants.customRoleId + DBPUtilitiesConstants.EQUAL + customRoleId;
				map.put(DBPUtilitiesConstants.FILTER, filter);

				result = ServiceCallHelper.invokeServiceAndGetJson(map, request.getHeaderMap(),
						URLConstants.CUSTOMROLESIGNATORYGROUP_GET);

				if (result.has(DBPDatasetConstants.CUSTOMROLE_SIGNATORYGROUP)
						&& result.get(DBPDatasetConstants.CUSTOMROLE_SIGNATORYGROUP).isJsonArray()
						&& result.get(DBPDatasetConstants.CUSTOMROLE_SIGNATORYGROUP).getAsJsonArray().size() > 0) {
					JsonArray groups = result.get(DBPDatasetConstants.CUSTOMROLE_SIGNATORYGROUP).getAsJsonArray();
					for (JsonElement group : groups) {
						if (group.isJsonObject()) {
							if (group.getAsJsonObject().has(InfinityConstants.signatoryGroupId)
									&& !group.getAsJsonObject().get(InfinityConstants.signatoryGroupId).isJsonNull()) {
								if (validSignatoryGroups.containsKey(group.getAsJsonObject()
										.get(InfinityConstants.signatoryGroupId).getAsString())) {
									validSignatoryGroups.get(group.getAsJsonObject()
											.get(InfinityConstants.signatoryGroupId).getAsString())
											.addProperty(InfinityConstants.isAssociated, "true");
								}
							}
						}
					}
				}

				JsonArray groups = new JsonArray();

				for (String key : validSignatoryGroups.keySet()) {
					groups.add(validSignatoryGroups.get(key));
				}

				signatoryObject.add(InfinityConstants.groups, groups);
				signatoryGroups.add(signatoryObject);
			}
		}

		jsonObject.add(InfinityConstants.signatoryGroups, signatoryGroups);

		return jsonObject;
	}

	@Override
	public Result getInfinityUserContractCoreCustomerActions(String methodID, Object[] inputArray,
			DataControllerRequest dcRequest, DataControllerResponse dcResponse) throws ApplicationException {

		Result result = new Result();
		String customerId = HelperMethods.getCustomerIdFromSession(dcRequest);
		String legalEntityId = LegalEntityUtil.getLegalEntityIdFromSession(dcRequest);
		ContractCustomersDTO contractCustomerDTO = new ContractCustomersDTO();
		contractCustomerDTO.setCustomerId(customerId);
		contractCustomerDTO.setCompanyLegalUnit(legalEntityId);
		DBXResult response = new DBXResult();
		try {
			InfinityUserManagementBusinessDelegate infinityUserManagementBusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(InfinityUserManagementBusinessDelegate.class);
			response = infinityUserManagementBusinessDelegate
					.getInfinityUserContractCoreCustomerActions(contractCustomerDTO, dcRequest.getHeaderMap());
			if (response != null && response.getResponse() != null) {
				JsonObject jsonObject = (JsonObject) response.getResponse();
				result = ConvertJsonToResult.convert(jsonObject);
			}
		} catch (ApplicationException e) {
			alert.prepareError("InfinityUserManagementResourceImpl : Exception occured while fetching actions ").log();
			throw new ApplicationException(e.getErrorCodeEnum());
		} catch (Exception e) {
			alert.prepareError(
					"InfinityUserManagementResourceImpl : Exception occured while fetching actions " + e.getMessage()).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_10788);
		}
		return result;
	}

	@Override
	public Result getCustomRoleByCompanyID(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		try {
			// code to check create custom role permission
			List<String> featureActionIdList = new ArrayList<>();
			featureActionIdList.add("CUSTOM_ROLES_VIEW");
			// String featureActionId = CustomerSession.getPermittedActionIds(request,
			// featureActionIdList);
			// if (featureActionId == null) {
			// return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
			// }

			JsonObject jsonObject = new JsonObject();

			Map<String, String> map = HelperMethods.getInputParamMap(inputArray);

			Iterator<String> iterator = request.getParameterNames();

			while (iterator.hasNext()) {
				String key = iterator.next();
				if ((!map.containsKey(key) || StringUtils.isBlank(map.get(key)))
						&& StringUtils.isNotBlank(request.getParameter(key))) {
					map.put(key, request.getParameter(key));
				}
			}

			if (!map.containsKey(InfinityConstants.id)) {
				return result;
			}

			jsonObject.addProperty(InfinityConstants.id, map.get(InfinityConstants.id));

			InfinityUserManagementBusinessDelegate infinityUserManagementBusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(InfinityUserManagementBusinessDelegate.class);
			DBXResult dbxResult = infinityUserManagementBusinessDelegate.getCustomRoleByCompanyID(jsonObject,
					request.getHeaderMap());
			if (dbxResult.getResponse() != null) {
				jsonObject = (JsonObject) dbxResult.getResponse();
				result = JSONToResult.convert(jsonObject.toString());
			}

		} catch (Exception exp) {
			alert.prepareError("Exception occured while invoking resources of getCustomRole", exp).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}

		return result;
	}

	@Override
	public Result getCompanyLevelCustomRoles(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		try {
			// code to check create custom role permission
			List<String> featureActionIdList = new ArrayList<>();
			featureActionIdList.add("CUSTOM_ROLES_VIEW");
			String featureActionId = CustomerSession.getPermittedActionIds(request, featureActionIdList);
			if (featureActionId == null) {
				return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
			}

			JsonObject jsonObject = new JsonObject();

			String id = HelperMethods.getCustomerIdFromSession(request);

			jsonObject.addProperty(InfinityConstants.id, id);

			String contractId = StringUtils
					.isNotBlank(HelperMethods.getInputParamMap(inputArray).get(InfinityConstants.contractId))
							? HelperMethods.getInputParamMap(inputArray).get(InfinityConstants.contractId)
							: request.getParameter(InfinityConstants.contractId);
			
			String legalEntityId ="";
			Boolean isSuperAdmin = LegalEntityUtil.checkForSuperAdmin(request);
			Boolean isSingleEntity = false;
			Result appInfo = null;
			try {
				appInfo = ServiceCallHelper.invokeServiceAndGetResult(null, null, URLConstants.APPLICATION_GET,
				        StringUtils.EMPTY);
			} catch (HttpCallException e) {
			}
			isSingleEntity = HelperMethods.getFieldValue(appInfo, "isSingleEntity")=="false"? false:true;
			
			if(isSuperAdmin)
				legalEntityId = LegalEntityUtil.getLegalEntityFromPayload(HelperMethods.getInputParamMap(inputArray), request);
			else if(request.containsKeyInRequest(InfinityConstants.legalEntityId) && isSingleEntity == false) {
	            legalEntityId = LegalEntityUtil.getLegalEntityFromPayload(HelperMethods.getInputParamMap(inputArray), request);
			}
			else
				legalEntityId = LegalEntityUtil.getLegalEntityIdFromSessionOrCache(request);

		        if(StringUtils.isBlank(legalEntityId)) {
		        	return ErrorCodeEnum.ERR_10001.setErrorCode(new Result());
		        }
		        
		        List<String> allLegalEntities = LegalEntityUtil.getAllCompanyLegalUnits(request);
				if (!allLegalEntities.contains(legalEntityId)) {
					alert.prepareError("Logged in user do not have access to this legalEntity ").log();
					throw new ApplicationException(ErrorCodeEnum.ERR_12403);
				}
		    
			jsonObject.addProperty(InfinityConstants.contractId, contractId);
			jsonObject.addProperty(InfinityConstants.legalEntityId, legalEntityId);

			InfinityUserManagementBusinessDelegate infinityUserManagementBusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(InfinityUserManagementBusinessDelegate.class);
			DBXResult dbxResult = infinityUserManagementBusinessDelegate.getCompanyLevelCustomRoles(jsonObject,
					request.getHeaderMap());
			if (dbxResult.getResponse() != null) {
				jsonObject = (JsonObject) dbxResult.getResponse();
				result = JSONToResult.convert(jsonObject.toString());
			}

		} catch (Exception exp) {
			alert.prepareError("Exception occured while invoking resources of getCustomRole", exp).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}

		return result;
	}

	@Override
	public Result getAssociatedContractUsers(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws ApplicationException {
		Result result = new Result();
		Map<String, String> loggedInUserInfo = HelperMethods.getCustomerFromAPIDBPIdentityService(request);
		Boolean isSuperAdmin = false;
		if (HelperMethods.isAuthenticationCheckRequiredForService(loggedInUserInfo)) {
			Set<String> userPermissions = SessionScope.getAllPermissionsFromIdentityScope(request);
			if (!userPermissions.contains("USER_MANAGEMENT_VIEW")) {
				ErrorCodeEnum.ERR_10051.setErrorCode(result);
				return result;
			}
		} else 
			isSuperAdmin = true;
		
		String legalEntityId = "";
		Boolean isSingleEntity = false;
		Result appInfo = null;
		try {
			appInfo = ServiceCallHelper.invokeServiceAndGetResult(null, null, URLConstants.APPLICATION_GET,
			        StringUtils.EMPTY);
		} catch (Exception e) {
		}
		isSingleEntity = HelperMethods.getFieldValue(appInfo, "isSingleEntity")=="false"? false:true;
		
		if(isSuperAdmin)
			legalEntityId = LegalEntityUtil.getLegalEntityFromPayload(HelperMethods.getInputParamMap(inputArray), request);
		else if(request.containsKeyInRequest(InfinityConstants.legalEntityId) && isSingleEntity == false) {
            legalEntityId = LegalEntityUtil.getLegalEntityFromPayload(HelperMethods.getInputParamMap(inputArray), request);
		}
		else{
			legalEntityId =	LegalEntityUtil.getLegalEntityIdFromSessionOrCache(request);
		}

	        if(StringUtils.isBlank(legalEntityId)) {
	        	return ErrorCodeEnum.ERR_10001.setErrorCode(new Result());
	        }
	        
	        List<String> allLegalEntities = LegalEntityUtil.getAllCompanyLegalUnits(request);
			if (!allLegalEntities.contains(legalEntityId)) {
				alert.prepareError("Logged in user do not have access to this legalEntity ").log();
				throw new ApplicationException(ErrorCodeEnum.ERR_12403);
			}

		try {
			JsonObject jsonObject = new JsonObject();

			String id = HelperMethods.getCustomerIdFromSession(request);

			jsonObject.addProperty(InfinityConstants.id, id);

			String contractId = StringUtils
					.isNotBlank(HelperMethods.getInputParamMap(inputArray).get(InfinityConstants.contractId))
							? HelperMethods.getInputParamMap(inputArray).get(InfinityConstants.contractId)
							: request.getParameter(InfinityConstants.contractId);
			
			jsonObject.addProperty(InfinityConstants.contractId, contractId);
			jsonObject.addProperty(InfinityConstants.legalEntityId, legalEntityId);
			InfinityUserManagementBusinessDelegate infinityUserManagementBusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(InfinityUserManagementBusinessDelegate.class);
			DBXResult dbxResult = infinityUserManagementBusinessDelegate.getAssociatedContractUsers(jsonObject,
					request.getHeaderMap());
			if (dbxResult.getResponse() != null) {
				jsonObject = (JsonObject) dbxResult.getResponse();
				result = JSONToResult.convert(jsonObject.toString());
			}
		} catch (Exception exp) {
			alert.prepareError("Exception occured while invoking resources of getCustomRole", exp).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}

		return result;
	}

	@Override
	public Result getInfinityUserBasedContractDetails(String methodID, Object[] inputArray,
			DataControllerRequest dcRequest, DataControllerResponse dcResponse) throws ApplicationException {
		Map<String, String> inputParams = new HashMap<>();
		Result result = new Result();
		String userId = StringUtils.isNotBlank(inputParams.get("userId")) ? inputParams.get("userId")
				: dcRequest.getParameter("userId");
		if (StringUtils.isBlank(userId)) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10791);
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
	        
	        List<String> allLegalEntities = LegalEntityUtil.getAllCompanyLegalUnits(dcRequest);
			if (!allLegalEntities.contains(legalEntityId)) {
				alert.prepareError("Logged in user do not have access to this legalEntity ").log();
				throw new ApplicationException(ErrorCodeEnum.ERR_12403);
			}

		Result result1 = checkValidUserForOperation(userId, legalEntityId, dcRequest);
		if(result1 != null) {
			return result1;
		}
		
		ContractCustomersDTO contractCustomerDTO = new ContractCustomersDTO();
		contractCustomerDTO.setCustomerId(userId);
		contractCustomerDTO.setCompanyLegalUnit(legalEntityId);
		DBXResult response = new DBXResult();
		try {
			InfinityUserManagementBusinessDelegate infinityUserManagementBusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(InfinityUserManagementBusinessDelegate.class);
			
			response = infinityUserManagementBusinessDelegate.getInfinityUserContractDetails(contractCustomerDTO,
					dcRequest.getHeaderMap());
			if (response != null && response.getResponse() != null) {
				JsonObject jsonObject = (JsonObject) response.getResponse();
				result = ConvertJsonToResult.convert(jsonObject);
				Result result2 = DBPAPIAbstractFactoryImpl.getResource(SignatoryGroupResource.class)
						.fetchSignatoryGroupDetailsById(userId);
				if (result2.getDatasetById(InfinityConstants.SignatoryGroups) != null) {
					result2.getDatasetById(InfinityConstants.SignatoryGroups).setId(InfinityConstants.signatoryGroups);
				}
				result.addAllDatasets(result2.getAllDatasets());
				result.addAllParams(result2.getAllParams());
				result.addAllRecords(result2.getAllRecords());
			}
		} catch (ApplicationException e) {
			alert.prepareError(
					"InfinityUserManagementResourceImpl : Exception occured while fetching infinity user contract details ").log();
			throw new ApplicationException(e.getErrorCodeEnum());
		} catch (Exception e) {
			alert.prepareError(
					"InfinityUserManagementResourceImpl : Exception occured while fetching infinity user contract details "
							, e).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_10790);
		}
		
		return result;
	}

	public void createAUserAndAssignTOGivenContract(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws ApplicationException {
		final String INPUT_AUTHORIZEDSIGNATORY = "authorizedSignatory";
		final String INPUT_AUTHORIZEDSIGNATORY_ROLES = "authorizedSignatoryRoles";
		final String INPUT_SERVICEKEY = "serviceKey";
		String backendId = null;

		Set<String> createdValidContractCoreCustomers = request.getAttribute("createdValidCustomers");
		Map<String, Set<ContractAccountsDTO>> createdCoreCustomerAccounts = request
				.getAttribute("createdCustomerAccounts");
		String createdServiceType = request.getAttribute("serviceType");
		String contractId = request.getAttribute("contractId");
		String contractStatus = request.getAttribute("contractStatus");
		String companyId = request.getAttribute("legalEntityId");
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);

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

		Map<String, String> userToCoreCustomerRoles = getUserToCoreCustomerRoles(authorizedSignatoryRoles);
		JsonObject payloadJson = getPayload(serviceKey, request);

		StringBuilder email = new StringBuilder();
		StringBuilder phone = new StringBuilder();

		String userId = createUser(authorizedSignatoryList.get(0), createdServiceType, request);
		updateCommunicationDetails(phone, email, payloadJson);
		createCustomerCommunication(email, phone, userId, request);
		createUserRoles(userId, contractId, userToCoreCustomerRoles, request);
		assignUserToContractCustomers(userId, contractId, createdValidContractCoreCustomers, request);
		createUserAccounts(userId, contractId, createdCoreCustomerAccounts, companyId, request);
		createUserActionLimits(userId, contractId, request.getParameter("legalEntityId"), createdValidContractCoreCustomers, userToCoreCustomerRoles, request);
		createCustomerPreference(userId, request.getHeaderMap());
		if (payloadJson.has(DBPUtilitiesConstants.BACKENDID)
				&& !payloadJson.get(DBPUtilitiesConstants.BACKENDID).isJsonNull()
				&& StringUtils.isNotBlank(payloadJson.get(DBPUtilitiesConstants.BACKENDID).getAsString())) {
			backendId = payloadJson.get(DBPUtilitiesConstants.BACKENDID).getAsString();
		}
		createBackendIdentifierEntry(backendId, userId, contractId, request, companyId, null);
		//createBackendIdentifierForParty(userId, backendId);
		deleteMfaService(serviceKey, request);

		request.addRequestParam_("userId", userId);
		request.addRequestParam_("contractStatus", contractStatus);
		request.addRequestParam_("contractId", contractId);

	}
	
	public void createBackendIdentifierForParty(String userId, String backendId, String legalEntityId) {
		String integrationName = IntegrationMappings.getInstance().getIntegrationName();
		try {
			if("party".equalsIgnoreCase(integrationName)) {
				InfinityUserManagementBackendDelegate infinityUserManagementBackendDelegate = DBPAPIAbstractFactoryImpl
						.getBackendDelegate(InfinityUserManagementBackendDelegate.class);
				DBXResult dbxResult = new DBXResult();
				JsonObject userDetails = new JsonObject();
				userDetails.addProperty("id", userId);
				userDetails.addProperty("coreCustomerId", backendId);
				userDetails.addProperty("legalEntityId", legalEntityId);
				dbxResult.setResponse(userDetails);
				infinityUserManagementBackendDelegate.createCustomer(dbxResult, userDetails,
						new HashMap<String, Object>());;
			}
		} catch (Exception e) {
			alert.prepareError("BackendIdentifier entry for Party failed :", e).log();
		}
	}

	public void deleteMfaService(String serviceKey, DataControllerRequest request) throws ApplicationException {
		MFAServiceBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(MFAServiceBusinessDelegate.class);

		businessDelegate.deleteMfaService(serviceKey, request.getHeaderMap());

	}

	public void createBackendIdentifierEntry(String backendId, String userId, String contractId,
			DataControllerRequest request, String companyId, String partyId) throws ApplicationException {
		if (StringUtils.isBlank(backendId)) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10397);
		}

		BackendIdentifierDTO backendIdentifierDTO = new BackendIdentifierDTO();
		backendIdentifierDTO.setId(UUID.randomUUID().toString());
		backendIdentifierDTO.setCustomer_id(userId);
		backendIdentifierDTO.setBackendId(backendId);
		backendIdentifierDTO.setBackendType(DTOConstants.CORE);
		backendIdentifierDTO.setSequenceNumber("1");
		backendIdentifierDTO.setContractId(contractId);
		backendIdentifierDTO.setIdentifier_name("customer_id");
		backendIdentifierDTO.setCompanyId(companyId);
		backendIdentifierDTO.setCompanyLegalUnit(companyId);
		/* This is for fall back*/
		if (StringUtils.isBlank(companyId)) {
			backendIdentifierDTO
					.setCompanyId(EnvironmentConfigurationsHandler.getValue(DBPUtilitiesConstants.BRANCH_ID_REFERENCE));
			backendIdentifierDTO
				.setCompanyLegalUnit(EnvironmentConfigurationsHandler.getValue(DBPUtilitiesConstants.BRANCH_ID_REFERENCE));
		}
		Map<String, Object> input = DTOUtils.getParameterMap(backendIdentifierDTO, true);
		backendIdentifierDTO.setIsNew(true);
		backendIdentifierDTO.persist(input, request.getHeaderMap());

		final String IS_Integrated = Boolean.toString(IntegrationTemplateURLFinder.isIntegrated);
		if (StringUtils.isNotBlank(IS_Integrated) && IS_Integrated.equalsIgnoreCase("true")) {
			backendIdentifierDTO = new BackendIdentifierDTO();
			backendIdentifierDTO.setIsNew(true);
			backendIdentifierDTO.setId(UUID.randomUUID().toString());
			backendIdentifierDTO
					.setBackendType(IntegrationTemplateURLFinder.getBackendURL(InfinityConstants.BackendType));
			backendIdentifierDTO.setIdentifier_name(
					IntegrationTemplateURLFinder.getBackendURL(InfinityConstants.BackendCustomerIdentifierName));
			backendIdentifierDTO.setCustomer_id(userId);
			backendIdentifierDTO.setBackendId(backendId);
			backendIdentifierDTO.setContractId(contractId);
			backendIdentifierDTO.setSequenceNumber("1");
			backendIdentifierDTO.setCompanyId(companyId);
			backendIdentifierDTO.setCompanyLegalUnit(companyId);
			/* This is for fall back */
			if (StringUtils.isBlank(companyId)) {
				backendIdentifierDTO.setCompanyId(
						EnvironmentConfigurationsHandler.getValue(DBPUtilitiesConstants.BRANCH_ID_REFERENCE));
				backendIdentifierDTO
					.setCompanyLegalUnit(EnvironmentConfigurationsHandler.getValue(DBPUtilitiesConstants.BRANCH_ID_REFERENCE));
			}
			input = DTOUtils.getParameterMap(backendIdentifierDTO, true);
			backendIdentifierDTO.persist(input, new HashMap<String, Object>());
		}

		if (null == backendIdentifierDTO || StringUtils.isBlank(backendIdentifierDTO.getId())) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10397);
		}

		if (StringUtils.isNotBlank(partyId)) {
			backendIdentifierDTO = new BackendIdentifierDTO();
			backendIdentifierDTO.setIsNew(true);
			backendIdentifierDTO.setId(UUID.randomUUID().toString());
			backendIdentifierDTO.setBackendType("PARTY");
			backendIdentifierDTO.setIdentifier_name("customer_id");
			backendIdentifierDTO.setCustomer_id(userId);
			backendIdentifierDTO.setBackendId(partyId);
			backendIdentifierDTO.setContractId(contractId);
			backendIdentifierDTO.setSequenceNumber("1");
			backendIdentifierDTO.setCompanyId(companyId);
			backendIdentifierDTO.setCompanyLegalUnit(companyId);
			/* This is for fall back */
			if (StringUtils.isBlank(companyId)) {
				backendIdentifierDTO.setCompanyId(
						EnvironmentConfigurationsHandler.getValue(DBPUtilitiesConstants.BRANCH_ID_REFERENCE));
				backendIdentifierDTO.setCompanyLegalUnit(
						EnvironmentConfigurationsHandler.getValue(DBPUtilitiesConstants.BRANCH_ID_REFERENCE));
				
			}
			input = DTOUtils.getParameterMap(backendIdentifierDTO, true);
			backendIdentifierDTO.persist(input, new HashMap<String, Object>());
		}

	}

	public void createCustomerCommunication(StringBuilder email, StringBuilder phone, String customerId,
			DataControllerRequest dcRequest) throws ApplicationException {
		CommunicationBusinessDelegate communicationBD = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(CommunicationBusinessDelegate.class);

		CustomerCommunicationDTO communicationDTO = new CustomerCommunicationDTO();
		communicationDTO.setId(idFormatter.format(new Date()));
		communicationDTO.setValue(phone.toString());
		communicationDTO.setType_id(DBPUtilitiesConstants.COMM_TYPE_PHONE);
		communicationDTO.setCustomer_id(customerId);
		communicationDTO.setIsPrimary(true);
		communicationDTO.setExtension(DBPUtilitiesConstants.CUSTOMER_COMMUNICATION_PHONE_DEFAULT_EXTENSION);

		DBXResult dbxResult = communicationBD.create(communicationDTO, dcRequest.getHeaderMap());

		if (null == dbxResult.getResponse()) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10316);
		}

		communicationDTO = new CustomerCommunicationDTO();
		communicationDTO.setId(idFormatter.format(new Date()));
		communicationDTO.setValue(email.toString());
		communicationDTO.setType_id(DBPUtilitiesConstants.COMM_TYPE_EMAIL);
		communicationDTO.setCustomer_id(customerId);
		communicationDTO.setIsPrimary(true);

		dbxResult = communicationBD.create(communicationDTO, dcRequest.getHeaderMap());

		if (null == dbxResult.getResponse()) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10316);
		}
	}

	public void updateCommunicationDetails(StringBuilder phone, StringBuilder email, JsonObject payloadJson) {
		final String COMMUNICATION_KEY = "communication";
		if (JSONUtil.isJsonNotNull(payloadJson) && JSONUtil.hasKey(payloadJson, COMMUNICATION_KEY)
				&& payloadJson.get(COMMUNICATION_KEY).isJsonObject()) {

			JsonObject communication = payloadJson.get(COMMUNICATION_KEY).getAsJsonObject();
			if (communication.has("phone")) {
				JsonArray phoneArray = communication.get("phone").getAsJsonArray();
				phone.append(phoneArray.get(0).getAsJsonObject().get("unmasked").getAsString());
			}
			if (communication.has("email")) {
				JsonArray emailArray = communication.get("email").getAsJsonArray();
				email.append(emailArray.get(0).getAsJsonObject().get("unmasked").getAsString());
			}

		}

	}

	public JsonObject getPayload(String serviceKey, DataControllerRequest dcRequest) throws ApplicationException {

		MFAServiceDTO dto = new MFAServiceDTO();
		dto.setServiceKey(serviceKey);
		MFAServiceBusinessDelegate bd = DBPAPIAbstractFactoryImpl.getBusinessDelegate(MFAServiceBusinessDelegate.class);
		List<MFAServiceDTO> list = bd.getMfaService(dto, new HashMap<>(), dcRequest.getHeaderMap());
		if (list.size() > 0) {
			dto = list.get(0);
		}
		MFAServiceUtil util = new MFAServiceUtil(dto);
		if (!util.isStateVerified()) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10324);
		}
		int otpValidTimeInMinutes = util.getServiceKeyExpiretime(dcRequest);
		if (!util.isValidServiceKey(otpValidTimeInMinutes)) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10325);
		}
		return util.getRequestPayload();
	}

	public void createUserRoles(String userId, String contractId, Map<String, String> userToCoreCustomerRoles,
			DataControllerRequest request) throws ApplicationException {
		CustomerGroupBusinessDelegate customerGroupBD = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(CustomerGroupBusinessDelegate.class);

		CustomerGroupDTO dto = new CustomerGroupDTO();
		dto.setCustomerId(userId);
		dto.setContractId(contractId);
		for (Entry<String, String> entry : userToCoreCustomerRoles.entrySet()) {
			String coreCustomerId = entry.getKey();
			String roleId = entry.getValue();
			dto.setCoreCustomerId(coreCustomerId);
			dto.setGroupId(roleId);
			dto.setCompanyLegalUnit(request.getParameter("legalEntityId"));
			customerGroupBD.createCustomerGroup(dto, request.getHeaderMap());
		}

	}

	public Map<String, String> getUserToCoreCustomerRoles(String authorizedSignatoryRoles)
			throws ApplicationException {
		if (StringUtils.isBlank(authorizedSignatoryRoles)) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10389);
		}

		Map<String, String> coreCustomerRoles = new HashMap<>();
		JsonParser parser = new JsonParser();
		JsonArray rolesArray = parser.parse(authorizedSignatoryRoles).isJsonArray()
				? parser.parse(authorizedSignatoryRoles).getAsJsonArray()
				: new JsonArray();
		if (rolesArray.size() == 0) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10389);
		}

		for (JsonElement element : rolesArray) {
			JsonObject roleObject = element.isJsonObject() ? element.getAsJsonObject() : new JsonObject();
			String coreCustomerId = roleObject.has("coreCustomerId") ? roleObject.get("coreCustomerId").getAsString()
					: "";
			String roleId = roleObject.has("authorizedSignatoryRoleId")
					? roleObject.get("authorizedSignatoryRoleId").getAsString()
					: "";

			if (StringUtils.isBlank(coreCustomerId) || StringUtils.isBlank(roleId)) {
				continue;
			}
			coreCustomerRoles.put(coreCustomerId, roleId);

		}
		return coreCustomerRoles;
	}

	public void createUserActionLimits(String userId, String contractId, String legalEntityId, Set<String> createdValidContractCoreCustomers,
			Map<String, String> userToCoreCustomerRoles, DataControllerRequest request) throws ApplicationException {
		CustomerActionsBusinessDelegate customerActionsBD = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(CustomerActionsBusinessDelegate.class);
		for (String coreCustomerId : createdValidContractCoreCustomers) {
			customerActionsBD.createCustomerActions(userId, contractId, coreCustomerId, legalEntityId, 
					userToCoreCustomerRoles.get(coreCustomerId), new HashSet<String>(), request.getHeaderMap());

			customerActionsBD.createCustomerLimitGroupLimits(userId, contractId, coreCustomerId, legalEntityId,
					request.getHeaderMap());
		}

	}

	public void createUserAccounts(String userId, String contractId,
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

	}

	public void assignUserToContractCustomers(String userId, String contractId,
			Set<String> createdValidContractCoreCustomers, DataControllerRequest request) {
		ContractCustomersDTO contractCustomerDTO;
		for (String customerId : createdValidContractCoreCustomers) {
			contractCustomerDTO = new ContractCustomersDTO();
			contractCustomerDTO.setId(idFormatter.format(new Date()));
			contractCustomerDTO.setContractId(contractId);
			contractCustomerDTO.setCoreCustomerId(customerId);
			contractCustomerDTO.setCustomerId(userId);
			contractCustomerDTO.setAutoSyncAccounts(true);
			contractCustomerDTO.setCompanyLegalUnit(request.getParameter("legalEntityId"));
			Map<String, Object> inputParams = DTOUtils.getParameterMap(contractCustomerDTO, false);
			contractCustomerDTO.persist(inputParams, request.getHeaderMap());
		}

	}

	public String createUser(CustomerDTO customerDTO, String createdServiceType, DataControllerRequest request)
			throws ApplicationException {
		UserManagementBusinessDelegate customerBD = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(UserManagementBusinessDelegate.class);

		String id = HelperMethods.generateUniqueCustomerId(request);
		String legalEntityId = request.getParameter("legalEntityId");
		customerDTO.setId(id);
		customerDTO.setUserName(HelperMethods.generateUniqueUserName(request));
		customerDTO.setIsNew(true);
		customerDTO.setStatus_id(DBPUtilitiesConstants.CUSTOMER_STATUS_NEW);
		customerDTO.setCustomerType_id(createdServiceType);
		customerDTO.setCompanyLegalUnit(legalEntityId);
		if (LegalEntityUtil.isSingleEntity())
			customerDTO.setDefaultLegalEntity(legalEntityId);
		customerDTO.setHomeLegalEntity(legalEntityId);

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

	@Override
	public Object createInfinityUserWithContract(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws JsonMappingException, JsonProcessingException, JSONException, ApplicationException {

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
		
		if (!checkIfContractCreationRequired(map, result, methodId, inputArray, request, response)) {
			return result;
		}

		InfinityUserManagementResource userManagementResource = DBPAPIAbstractFactoryImpl
				.getResource(InfinityUserManagementResource.class);

		result = (Result) userManagementResource.createInfinityUser(methodId, inputArray, request, response);
		return result;
	}

	@Override
	public Object editInfinityUserWithContract(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws JsonMappingException, JsonProcessingException, JSONException, ApplicationException {

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

		JsonObject contractJsonObject;

		JsonElement element = new JsonParser().parse(contractDetails);

		if (element.isJsonObject()) {
			contractJsonObject = element.getAsJsonObject();
			if (contractJsonObject.has(InfinityConstants.contractName)
					&& !contractJsonObject.get(InfinityConstants.contractName).isJsonNull()) {
				for (Entry<String, JsonElement> entry : contractJsonObject.entrySet()) {
					map.put(entry.getKey(), entry.getValue().getAsString());
				}

				ContractResource resource = DBPAPIAbstractFactoryImpl.getResource(ContractResource.class);
				try {
					result = resource.createContract(methodId, inputArray, request, response);
				} catch (ApplicationException e) {
					alert.prepareError("Exception", e).log();
					e.setError(result);
				}

				if (!result.hasParamByName(InfinityConstants.contractId)
						|| StringUtils.isBlank(result.getParamValueByName(InfinityConstants.contractId))) {
					return result;
				}

				String contractId = result.getParamValueByName(InfinityConstants.contractId);

				String companyList = map.get(InfinityConstants.companyList);

				JsonArray companyListArray = new JsonArray();

				element = new JsonParser().parse(companyList);

				if (element.isJsonArray()) {
					companyListArray = element.getAsJsonArray();
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
			}
		}

		InfinityUserManagementResource userManagementResource = DBPAPIAbstractFactoryImpl
				.getResource(InfinityUserManagementResource.class);

		result = (Result) userManagementResource.editInfinityUser(methodId, inputArray, request, response);
		return result;
	}

	public Result checkValidUserForOperation(String userId, String legalEntityId, DataControllerRequest request){
		Result result = null;
		JsonObject inputObject = new JsonObject();
		Map<String, String> loggedInUserInfo = HelperMethods.getCustomerFromAPIDBPIdentityService(request);
		if (HelperMethods.isPSD2Agent(request)) {
			inputObject.addProperty(InfinityConstants.isSuperAdmin, "true");
		} else if (HelperMethods.isAuthenticationCheckRequiredForService(loggedInUserInfo)) {
			inputObject.addProperty(InfinityConstants.isSuperAdmin, "false");
		} else {
			inputObject.addProperty(InfinityConstants.isSuperAdmin, "true");
		}

		String loggedInUserId = HelperMethods.getCustomerIdFromSession(request);
		try {
			String isSuperAdmin = inputObject.get(InfinityConstants.isSuperAdmin).getAsString();
			if(isSuperAdmin.equals("false") && !StringUtils.isBlank(userId) && 
			   !StringUtils.isBlank(loggedInUserId) && !StringUtils.equals(userId, loggedInUserId)) {
				JsonObject userObject = new JsonObject();
				userObject.addProperty(InfinityConstants.id, loggedInUserId);
				userObject.addProperty(InfinityConstants.legalEntityId, legalEntityId);
				InfinityUserManagementBusinessDelegate userManagementBusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(InfinityUserManagementBusinessDelegate.class);
				DBXResult dbxRes = userManagementBusinessDelegate.getAssociatedContractUsers(userObject, request.getHeaderMap());
				if (dbxRes.getResponse() != null) {
					JsonObject jsonObject = (JsonObject) dbxRes.getResponse();
					if(!jsonObject.has(InfinityConstants.companyList)){
						return ErrorCodeEnum.ERR_10405.setErrorCode(new Result());
					}
					boolean isAssociatedUserForLoggedInUser = checkAssociatedUserForLoggedInUser(jsonObject, userId);
					if(!isAssociatedUserForLoggedInUser){
						return ErrorCodeEnum.ERR_10404.setErrorCode(new Result());
					}
				} else {
					return ErrorCodeEnum.ERR_10405.setErrorCode(new Result());
				}
			}
		} catch(Exception e) {
			alert.prepareError("InfinityUserManagementResourceImpl : Exception occured while fetching the associated users for the loggedIn user "+ e.getMessage()).log();
			return ErrorCodeEnum.ERR_10405.setErrorCode(new Result());
		}
		return result;
	}

	public Result getInfinityUserAccountsForAdmin(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws ApplicationException {
		Map<String, String> inputParams = new HashMap<>();
		inputParams = HelperMethods.getInputParamMap(inputArray);
		Result result = new Result();
		String userId = StringUtils.isNotBlank(inputParams.get("userId")) ? inputParams.get("userId")
				: dcRequest.getParameter("userId");
		
		if (StringUtils.isBlank(userId)) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10792);
		}
		
		String coreCustomerId = StringUtils.isNotBlank(inputParams.get(InfinityConstants.coreCustomerId))
				? inputParams.get(InfinityConstants.coreCustomerId)
				: dcRequest.getParameter(InfinityConstants.coreCustomerId);
		;
		String contractId = StringUtils.isNotBlank(inputParams.get(InfinityConstants.contractId))
				? inputParams.get(InfinityConstants.contractId)
				: dcRequest.getParameter(InfinityConstants.contractId);
        inputParams.get(InfinityConstants.contractId);
        
        
		String legalEntityId ="";
		Boolean isSuperAdmin = LegalEntityUtil.checkForSuperAdmin(dcRequest);
		Boolean isSingleEntity = false;
		Result appInfo = null;
		try {
			appInfo = ServiceCallHelper.invokeServiceAndGetResult(null, null, URLConstants.APPLICATION_GET,
			        StringUtils.EMPTY);
		} catch (Exception e) {
		}
		isSingleEntity = HelperMethods.getFieldValue(appInfo, "isSingleEntity")=="false"? false:true;

		if (isSuperAdmin)
			legalEntityId = LegalEntityUtil.getLegalEntityFromPayload(inputParams, dcRequest);
		else if(dcRequest.containsKeyInRequest(InfinityConstants.legalEntityId) && isSingleEntity == false) {
            legalEntityId = LegalEntityUtil.getLegalEntityFromPayload(inputParams, dcRequest);
		}
		else
			legalEntityId = LegalEntityUtil.getLegalEntityIdFromSessionOrCache(dcRequest);

		if (StringUtils.isBlank(legalEntityId)) {
			return ErrorCodeEnum.ERR_10001.setErrorCode(new Result());
		}

		List<String> allLegalEntities = LegalEntityUtil.getAllCompanyLegalUnits(dcRequest);
		if (!allLegalEntities.contains(legalEntityId)) {
			alert.prepareError("Logged in user do not have access to this legalEntity ").log();
			throw new ApplicationException(ErrorCodeEnum.ERR_12403);
		}

		Result result1 = checkValidUserForOperation(userId, legalEntityId, dcRequest);
		if(result1 != null) {
			return result1;
		}

		CustomerAccountsDTO customerAccountsDTO = new CustomerAccountsDTO();
		customerAccountsDTO.setCustomerId(userId);
		customerAccountsDTO.setCompanyLegalUnit(legalEntityId);
		if (StringUtils.isNotBlank(contractId) ) {
			customerAccountsDTO.setContractId(contractId);
		}
		
		DBXResult response = new DBXResult();
		try {
			InfinityUserManagementBusinessDelegate infinityUserManagementBusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(InfinityUserManagementBusinessDelegate.class);
			response = infinityUserManagementBusinessDelegate.getInfinityUserAccountsForAdmin(isSuperAdmin, customerAccountsDTO,
					dcRequest.getHeaderMap());
			if (response != null && response.getResponse() != null) {
				JsonObject jsonObject = (JsonObject) response.getResponse();
				result = ConvertJsonToResult.convert(jsonObject);
			}
		} catch (ApplicationException e) {
			alert.prepareError(
					"InfinityUserManagementResourceImpl : Exception occured while fetching infinity user contract details ").log();
			throw new ApplicationException(e.getErrorCodeEnum());
		} catch (Exception e) {
			alert.prepareError(
					"InfinityUserManagementResourceImpl : Exception occured while fetching infinity user contract details "
							+ e.getMessage()).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_10793);
		}
		return result;
	}

	@Override
	public Result getInfinityUserFeatureActions(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws ApplicationException, JsonMappingException, JsonProcessingException {
		request.addRequestParam_(InfinityConstants.id, HelperMethods.getInputParamMap(inputArray).get("userId"));
		request.addRequestParam_(InfinityConstants.contractId, HelperMethods.getInputParamMap(inputArray).get("contractId"));
        request.addRequestParam_(InfinityConstants.coreCustomerId, HelperMethods.getInputParamMap(inputArray).get("coreCustomerId"));
		return getInfinityUser(methodID, inputArray, request, response);
	}

	@Override
	public Result getInfinityUserLimits(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws ApplicationException, JsonMappingException, JsonProcessingException {
		request.addRequestParam_(InfinityConstants.id, HelperMethods.getInputParamMap(inputArray).get("userId"));
		return getInfinityUser(methodID, inputArray, request, response);
	}

	@Override
	public Result generateInfinityUserActivationCodeAndUsername(String methodID, Object[] inputArray,
			DataControllerRequest dcRequest, DataControllerResponse dcResponse) throws ApplicationException {
		Result result = new Result();
		LegalEntityUtil.addCompanyIDToHeaders(dcRequest);
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		String contractStatus = StringUtils.isNotBlank(inputParams.get(InfinityConstants.contractStatus))
				? inputParams.get(InfinityConstants.contractStatus)
				: dcRequest.getParameter(InfinityConstants.contractStatus);
		if (StringUtils.isNotBlank(contractStatus)
				&& !DBPUtilitiesConstants.CONTRACT_STATUS_ACTIVE.equalsIgnoreCase(contractStatus))
			return result;
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
			alert.prepareError(
					"InfinityUserManagementResourceImpl : Exception occured while generating username and activation code "
							,e).log();
			throw new ApplicationException(e.getErrorCodeEnum());
		} catch (Exception e) {
			alert.prepareError(
					"InfinityUserManagementResourceImpl : Exception occured while generating username and activation code "
							,e).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_10795);
		}
		return result;
	}

	@Override
	public Object createRetailContract(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {

		Result result = new Result();
		/*try {
			CoreCustomerResource businessDelegate = DBPAPIAbstractFactoryImpl.getResource(CoreCustomerResource.class);
			businessDelegate.activateCustomer(methodID, inputArray, request, response);
		} catch (Exception e) {
			// TODO: handle exception
		}*/

		boolean isNotificationRequired = true;
		try {
			Map<String, String> map = HelperMethods.getInputParamMap(inputArray);

			Iterator<String> iterator = request.getParameterNames();

			while (iterator.hasNext()) {
				String key = iterator.next();
				if ((!map.containsKey(key) || StringUtils.isBlank(map.get(key)))
						&& StringUtils.isNotBlank(request.getParameter(key))) {
					map.put(key, request.getParameter(key));
				}
			}

			String customerId = map.get(InfinityConstants.customerId);

			if (StringUtils.isBlank(customerId)) {
				throw new ApplicationException(ErrorCodeEnum.ERR_10349, "CustomerId is Empty");
			}

			String name = "";
			String legalEntityId = "";
			String filter = InfinityConstants.id + DBPUtilitiesConstants.EQUAL + customerId;
			Map<String, Object> inutMap = new HashMap<String, Object>();
			inutMap.put(DBPUtilitiesConstants.FILTER, filter);
			JsonObject jsonObject = ServiceCallHelper.invokeServiceAndGetJson(inutMap, request.getHeaderMap(),
					URLConstants.CUSTOMER_GET);
			if (jsonObject.has(DBPDatasetConstants.DATASET_CUSTOMER)) {
				JsonElement jsonElement = jsonObject.get(DBPDatasetConstants.DATASET_CUSTOMER);
				if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
					JsonObject customerObject = jsonElement.getAsJsonArray().get(0).getAsJsonObject();
					name += customerObject.has(InfinityConstants.FirstName)
							&& !customerObject.get(InfinityConstants.FirstName).isJsonNull()
									? customerObject.get(InfinityConstants.FirstName).getAsString()
									: "";
					if (customerObject.has(InfinityConstants.LastName)
							&& !customerObject.get(InfinityConstants.LastName).isJsonNull()) {
						name += " ";
						name += customerObject.get(InfinityConstants.LastName).getAsString();
					}
					name += " ";
					name += customerId;
					
				} else
					throw new ApplicationException(ErrorCodeEnum.ERR_10349, "Invalid CustomerId");
			} else
				throw new ApplicationException(ErrorCodeEnum.ERR_10349, "Invalid CustomerId");
			
			map.put(InfinityConstants.contractName, name);
			map.put(InfinityConstants.coreCustomerName, name);

			BackendIdentifierDTO backendIdentifierDTO = new BackendIdentifierDTO();

			BackendIdentifiersBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl
					.getBackendDelegate(BackendIdentifiersBackendDelegate.class);
			backendIdentifierDTO.setCustomer_id(map.get(InfinityConstants.customerId));

			final String IS_Integrated = Boolean.toString(IntegrationTemplateURLFinder.isIntegrated);
			if (StringUtils.isNotBlank(IS_Integrated) && IS_Integrated.equalsIgnoreCase("true")) {
				backendIdentifierDTO
						.setBackendType(IntegrationTemplateURLFinder.getBackendURL(InfinityConstants.BackendType));
			} else {
				backendIdentifierDTO.setBackendType(DTOConstants.CORE);
			}

			DBXResult dbxResult = backendDelegate.get(backendIdentifierDTO, request.getHeaderMap());

			String companyId = "";
			String coreCustomerId = "";
			if (dbxResult.getResponse() != null) {
				backendIdentifierDTO = (BackendIdentifierDTO) dbxResult.getResponse();
				coreCustomerId = backendIdentifierDTO.getBackendId();
				companyId = backendIdentifierDTO.getCompanyId();
				legalEntityId = backendIdentifierDTO.getCompanyLegalUnit();
			}

			if (HelperMethods.isBlank(customerId, coreCustomerId, legalEntityId)) {
				throw new ApplicationException(ErrorCodeEnum.ERR_10349,
						"Invalid customerId or coreCustomerId or legalEntityId");
			}

			map.put(InfinityConstants.coreCustomerId, coreCustomerId);
			map.put("companyId", companyId);
			map.put(DEFAULT_SERVICE_ID, BundleConfigurationHandler.DEFAULT_RETAIL_SERVICE_ID);
			map.put(InfinityConstants.legalEntityId, legalEntityId);
			request.addRequestParam_(InfinityConstants.legalEntityId, legalEntityId);
			return createContract(methodID, inputArray, map, request, response);
		} catch (ApplicationException e) {
			result = new Result();
			e.getErrorCodeEnum().setErrorCode(result);
			alert.prepareError("Exception occured while creating a contract " + e.getStackTrace()).log();
			return result;
		} catch (Exception e) {
			result = new Result();
			alert.prepareError("Exception occured while creating a contract " + e.getStackTrace()).log();
			
			return result;
		}

	}

	private Result createContract(String methodID, Object[] inputArray, Map<String, String> map,
			DataControllerRequest request, DataControllerResponse response) {

		boolean isOnBoradingFlow = false;
		Map<String, String> loggedInUserInfo = HelperMethods.getCustomerFromAPIDBPIdentityService(request);
		if (!HelperMethods.isAuthenticationCheckRequiredForService(loggedInUserInfo)) {
			isOnBoradingFlow = true;
		}

		boolean isNotificationRequired = true;
		Result result = new Result();
		try {
			LegalEntityUtil.addCompanyIDToHeaders(request);
			String customerId = map.get(InfinityConstants.customerId);
			String coreCustomerId = map.get(InfinityConstants.coreCustomerId);
			String defaultServiceId = map.get(DEFAULT_SERVICE_ID);
			String legalEntityId = map.get(InfinityConstants.legalEntityId);

			String filter = InfinityConstants.customerId + DBPUtilitiesConstants.EQUAL + customerId
					+ DBPUtilitiesConstants.AND + InfinityConstants.coreCustomerId + DBPUtilitiesConstants.EQUAL
					+ coreCustomerId + DBPUtilitiesConstants.AND + InfinityConstants.LegalEntityId + 
					DBPUtilitiesConstants.EQUAL + legalEntityId;

			String contractId = "";

			String autoSyncAccounts = null;
			Map<String, Object> input = new HashMap<String, Object>();
			input.put(DBPUtilitiesConstants.FILTER, filter);
			JsonObject jsonResponse = ServiceCallHelper.invokeServiceAndGetJson(input, request.getHeaderMap(),
					URLConstants.CONTRACT_CUSTOMERS_GET);
			if (jsonResponse.has(DBPDatasetConstants.DATASET_CONTRACT_CUSTOMERS)) {
				JsonElement jsonElement = jsonResponse.get(DBPDatasetConstants.DATASET_CONTRACT_CUSTOMERS);
				if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
					JsonArray jsonArray = jsonElement.getAsJsonArray();
					JsonObject contractCustomer = jsonArray.get(0).getAsJsonObject();
					contractId = contractCustomer.has(InfinityConstants.contractId)
							&& !contractCustomer.get(InfinityConstants.contractId).isJsonNull()
									? contractCustomer.get(InfinityConstants.contractId).getAsString()
									: null;
					autoSyncAccounts = contractCustomer.has(InfinityConstants.autoSyncAccounts)
							&& !contractCustomer.get(InfinityConstants.autoSyncAccounts).isJsonNull()
									? contractCustomer.get(InfinityConstants.autoSyncAccounts).getAsString()
									: null;
				}
			}

			if (StringUtils.isBlank(contractId)) {
				getContractPayload(map, request, coreCustomerId, defaultServiceId);

				ContractResource resource = DBPAPIAbstractFactoryImpl.getResource(ContractResource.class);
				try {
					result = resource.createContract(methodID, inputArray, request, response);
				} catch (ApplicationException e) {
					alert.prepareError("Exception", e).log();
					e.setError(result);
				}

				if (!result.hasParamByName(InfinityConstants.contractId)
						|| StringUtils.isBlank(result.getParamValueByName(InfinityConstants.contractId))) {
					return result;
				}

				contractId = result.getParamValueByName(InfinityConstants.contractId);
				JsonObject jsonObject = createInfinityUserPayload(map, contractId, request.getHeaderMap(), 
						defaultServiceId, request, true);
				deleteGroup(customerId, map, request);
				JsonObject jsonResult = (JsonObject) new InfinityUserManagementBackendDelegateImpl()
						.editInfinityUser(jsonObject, request.getHeaderMap()).getResponse();
				result = JSONToResult.convert(jsonResult.toString());
			} else {
				isNotificationRequired = false;
				getContractPayload(map, request, coreCustomerId, defaultServiceId);
				createAccountLevelPersmissionsOfContract(inputArray,legalEntityId,request,contractId);
				addAccountsToContract(contractId, coreCustomerId, map, request.getHeaderMap());
				Set<String> newAccounts = getAccounts(customerId, contractId, coreCustomerId, request.getHeaderMap(),
						map);
				getContractInfo(map, contractId, coreCustomerId, customerId, request.getHeaderMap());
				JsonObject jsonObject = createInfinityUserPayload(map, contractId, request.getHeaderMap(),
						autoSyncAccounts, request, false);
				if (LegalEntityUtil.isSingleEntity()) {
					CustomerDTO customer = (CustomerDTO) new CustomerDTO().loadDTO(customerId);
					if (customer != null) {
						customer.setIsChanged(true);
						customer.setDefaultLegalEntity(legalEntityId);
						DTOUtils.persistObject(customer, request.getHeaderMap());
					}

				}
				new InfinityUserManagementBackendDelegateImpl().createCustomerAction(customerId, jsonObject,
						request.getHeaderMap(), coreCustomerId, legalEntityId, false, legalEntityId,false);//not in createInfinityUser
				result.addParam(InfinityConstants.id, customerId);

				ContractCoreCustomerBackendDelegate contractCoreCustomerBD = DBPAPIAbstractFactoryImpl
						.getBackendDelegate(ContractCoreCustomerBackendDelegate.class);
				Map<String, Set<String>> contractCoreCustomerDetailsMap = contractCoreCustomerBD
						.getCoreCustomerAccountsFeaturesActions(contractId, coreCustomerId, request.getHeaderMap());
				Set<String> customerAccounts = contractCoreCustomerDetailsMap.get("accounts");
				customerAccounts.retainAll(newAccounts);
				Set<String> customerActions = contractCoreCustomerDetailsMap.get("actions");
				customerActions = getActionWithApproveFeatureAction(customerActions, legalEntityId, request.getHeaderMap());
				ApprovalMatrixBusinessDelegate approvalmatrixDelegate = DBPAPIAbstractFactoryImpl
						.getBusinessDelegate(ApprovalMatrixBusinessDelegate.class);
				approvalmatrixDelegate.createDefaultApprovalMatrixEntry(contractId,
						String.join(DBPUtilitiesConstants.COMMA_SEPERATOR, customerAccounts),
						customerActions.toArray(new String[0]), coreCustomerId, legalEntityId);
			}

			if (result.hasParamByName(InfinityConstants.id)
					&& StringUtils.isNotBlank(result.getParamValueByName(InfinityConstants.id))) {
				CustomerDTO customerDTO = new CustomerDTO();
				customerDTO = (CustomerDTO) customerDTO.loadDTO(customerId);
				if (customerDTO != null && isNotificationRequired) {
					input = new HashMap<String, Object>();
					input.put(InfinityConstants.id, customerId);
					input.put(InfinityConstants.Password, InfinityConstants.defaultPassword);
					input.put(InfinityConstants.Status_id, HelperMethods.getCustomerStatus().get("ACTIVE"));
					input.put(InfinityConstants.CustomerType_id, HelperMethods.getCustomerTypes().get("Retail"));
					customerDTO.setIsChanged(true);
					customerDTO.persist(input, request.getHeaderMap());
					map.put(InfinityConstants.userName, customerDTO.getUserName());
					map.put(InfinityConstants.userId, customerId);
					map.put(InfinityConstants.contractStatus, DBPUtilitiesConstants.CONTRACT_STATUS_ACTIVE);

					if (isOnBoradingFlow) {
						map.put(InfinityConstants.isOnBoradingFlow, "true");
						/*
						 * HelperMethods.getInputParamMap(inputArray).put(InfinityConstants.
						 * EMAIL_TEMPLATE, DBPUtilitiesConstants.ONBOARDING_USERNAME_TEMPLATE);
						 */
						HelperMethods.getInputParamMap(inputArray).put(InfinityConstants.SMS_TEMPLATE,
								DBPUtilitiesConstants.ONBOARDING_ACTIVATIONCODE_TEMPLATE);
					}
					Result deliveryResult = generateInfinityUserActivationCodeAndUsername(methodID, inputArray, request,
							response);
					result.addAllParams(deliveryResult.getAllParams());
				}
			}
		} catch (ApplicationException e) {
			result = new Result();
			e.setError(result);
			alert.prepareError("Exception occured while creating a contract " + e.getStackTrace()).log();
			return result;
		} catch (Exception e) {
			result = new Result();
			alert.prepareError("Exception occured while creating a contract " + e.getStackTrace()).log();
			
			return result;
		}

		return result;
	}
    private String getServiceType(String serviceDefinitionId, DataControllerRequest dcRequest)
            throws ApplicationException {
        ServiceDefinitionDTO dto = new ServiceDefinitionDTO();
        dto.setId(serviceDefinitionId);
        ServiceDefinitionBusinessDelegate serviceBD = DBPAPIAbstractFactoryImpl
                .getBusinessDelegate(ServiceDefinitionBusinessDelegate.class);
        dto = serviceBD.getServiceDefinitionDetails(dto, dcRequest.getHeaderMap());
        if (dto == null || StringUtils.isBlank(dto.getId()) || StringUtils.isBlank(dto.getServiceType())) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10377);
        }

        return dto.getServiceType();
    }
	private void createAccountLevelPersmissionsOfContract(Object[] inputArray, String legalEntityId,
			DataControllerRequest dcRequest, String contractId) throws ApplicationException {
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		String accountLevelPermissions = inputParams.get("accountLevelPermissions1");
		JsonArray accountLevelPermissionsJsonArray = new JsonArray();
		JsonArray globalLevelPermissionsJsonArray = new JsonArray();
		JsonArray transactionLimitsJsonArray = new JsonArray();
		accountLevelPermissionsJsonArray = JSONUtil.parseAsJsonArray(accountLevelPermissions);
		
		String serviceDefinitionId = inputParams.get("serviceDefinitionId");
		String serviceDefinitionType = getServiceType(serviceDefinitionId, dcRequest);
		JsonObject contractActionObj = new JsonObject();
		contractActionObj.add("accountLevelPermissions", accountLevelPermissionsJsonArray);
		contractActionObj.add("globalLevelPermissions", globalLevelPermissionsJsonArray);
        contractActionObj.add("transactionLimits", transactionLimitsJsonArray);
		contractActionObj.addProperty("serviceDefinitionType", serviceDefinitionType);

		ContractBusinessDelegate ContractBusinessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(ContractBusinessDelegate.class);
		ContractBusinessDelegate.createContractActionLimit(contractActionObj, contractId, legalEntityId,
				dcRequest.getHeaderMap());
	}

	private void deleteGroup(String customerId, Map<String, String> map, DataControllerRequest request) {

		String filter = InfinityConstants.Customer_id + DBPUtilitiesConstants.EQUAL + customerId
				+ DBPUtilitiesConstants.AND + InfinityConstants.Group_id + DBPUtilitiesConstants.EQUAL
				+ map.get(BundleConfigurationHandler.DEFAULT_PROSPECT_GROUP) + DBPUtilitiesConstants.AND
				+ InfinityConstants.coreCustomerId + DBPUtilitiesConstants.EQUAL   + "null" + DBPUtilitiesConstants.AND
				+ InfinityConstants.contractId + DBPUtilitiesConstants.EQUAL + "null";
		Map<String, Object> input = new HashMap<String, Object>();
		input.put(DBPUtilitiesConstants.FILTER, filter);
		ServiceCallHelper.invokeServiceAndGetJson(input, request.getHeaderMap(), URLConstants.CUSTOMER_GROUP_DELETE);
	}

	private Set<String> getActionWithApproveFeatureAction(Set<String> actionsSet, String legalEntityId, Map<String, Object> headersMap)
			throws ApplicationException {
		StringBuilder actionsString = new StringBuilder();
		for (String action : actionsSet) {
			actionsString.append(action);
			actionsString.append(",");
		}
		if (actionsString.length() > 0)
			actionsString.replace(actionsString.length() - 1, actionsString.length(), "");

		ContractFeatureActionsBusinessDelegate contractFeaturesBD = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(ContractFeatureActionsBusinessDelegate.class);

		String actions = contractFeaturesBD.getActionsWithApproveFeatureAction(actionsString.toString(), legalEntityId, headersMap);

		return HelperMethods.splitString(actions, DBPUtilitiesConstants.COMMA_SEPERATOR);

	}

	private void getContractInfo(Map<String, String> map, String contractId, String coreCustomerId, String customerId,
			Map<String, Object> headerMap) {

		map.put(InfinityConstants.contractId, contractId);
		map.put(InfinityConstants.coreCustomerId, coreCustomerId);
		String legalEntityId = map.get(InfinityConstants.legalEntityId);

		String serviceDefinitionId = "";

		String filter = InfinityConstants.id + DBPUtilitiesConstants.EQUAL + contractId + DBPUtilitiesConstants.AND
				+ InfinityConstants.LegalEntityId + DBPUtilitiesConstants.EQUAL + legalEntityId;
		Map<String, Object> input = new HashMap<String, Object>();
		input.put(DBPUtilitiesConstants.FILTER, filter);
		JsonObject jsonResponse = ServiceCallHelper.invokeServiceAndGetJson(input, headerMap,
				URLConstants.CONTRACT_GET);
		if (jsonResponse.has(DBPDatasetConstants.DATASET_CONTRACT)) {
			JsonElement jsonElement = jsonResponse.get(DBPDatasetConstants.DATASET_CONTRACT);
			if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
				JsonArray jsonArray = jsonElement.getAsJsonArray();
				JsonObject contract = jsonArray.get(0).getAsJsonObject();
				serviceDefinitionId = JSONUtil.getString(contract, "servicedefinitionId");
			}
		}

		String groupId = "";

		filter = InfinityConstants.contractId + DBPUtilitiesConstants.EQUAL + contractId + DBPUtilitiesConstants.AND
				+ InfinityConstants.coreCustomerId + DBPUtilitiesConstants.EQUAL + coreCustomerId
				+ DBPUtilitiesConstants.AND + InfinityConstants.Customer_id + DBPUtilitiesConstants.EQUAL + customerId
				+ DBPUtilitiesConstants.AND + InfinityConstants.LegalEntityId + DBPUtilitiesConstants.EQUAL + legalEntityId;

		input = new HashMap<String, Object>();
		input.put(DBPUtilitiesConstants.FILTER, filter);
		jsonResponse = ServiceCallHelper.invokeServiceAndGetJson(input, headerMap, URLConstants.CUSTOMER_GROUP_GET);
		if (jsonResponse.has(DBPDatasetConstants.DATASET_CUSTOMERGROUP)) {
			JsonElement jsonElement = jsonResponse.get(DBPDatasetConstants.DATASET_CUSTOMERGROUP);
			if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
				JsonArray jsonArray = jsonElement.getAsJsonArray();
				JsonObject contract = jsonArray.get(0).getAsJsonObject();
				groupId = contract.has(InfinityConstants.Group_id)
						&& !contract.get(InfinityConstants.Group_id).isJsonNull()
								? contract.get(InfinityConstants.Group_id).getAsString()
								: null;
			}
		}

		map.put(InfinityConstants.roleId, groupId);
		map.put(InfinityConstants.serviceDefinitionId, serviceDefinitionId);
		map.put(InfinityConstants.serviceDefinition, serviceDefinitionId);
		map.put(InfinityConstants.coreCustomerId, coreCustomerId);
		map.put(InfinityConstants.customerId, customerId);

	}

	private void addAccountsToContract(String contractId, String coreCustomerId, Map<String, String> map,
			Map<String, Object> headerMap) throws ApplicationException {
		String accountsString = map.get(InfinityConstants.accounts);
		String legalEntityId = map.get(InfinityConstants.legalEntityId);
		JsonArray accounts = new JsonArray();
		try {
			accounts = new JsonParser().parse(accountsString).getAsJsonArray();
		} catch (Exception e) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10349, "Invalid accounts array");
		}

		for (JsonElement element : accounts) {
			Map<String, Object> input = new HashMap<String, Object>();
			input.put(InfinityConstants.id, HelperMethods.getNewId());
			input.put(InfinityConstants.contractId, contractId);
			input.put(InfinityConstants.coreCustomerId, coreCustomerId);
			input.put(InfinityConstants.LegalEntityId, legalEntityId);
			for (Entry<String, JsonElement> entry : element.getAsJsonObject().entrySet()) {
				input.put(entry.getKey(), entry.getValue().getAsString());
			}

			ServiceCallHelper.invokeServiceAndGetJson(input, headerMap, URLConstants.CONTRACTACCOUNT_CREATE);
		}

	}

	private Set<String> getAccounts(String customerId, String contractId, String coreCustomerId,
			Map<String, Object> headerMap, Map<String, String> map) throws ApplicationException {
		String accountsString = map.get(InfinityConstants.accounts);
		JsonArray accounts = new JsonArray();
		Set<String> accountSet = new HashSet<String>();
		try {
			accounts = new JsonParser().parse(accountsString).getAsJsonArray();
		} catch (Exception e) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10349, "Invalid accounts array");
		}

		Set<String> newAccounts = new HashSet<String>();
		JsonArray accountsArray = new JsonArray();
		HashMap<String, Object> input = new HashMap<String, Object>();
		String filter = InfinityConstants.Customer_id + DBPUtilitiesConstants.EQUAL + customerId;
		input.put(DBPUtilitiesConstants.FILTER, filter);
		JsonObject response = ServiceCallHelper.invokeServiceAndGetJson(input, headerMap,
				URLConstants.CUSTOMERACCOUNTS_GET);
		if (response.has(DBPDatasetConstants.DATASET_CUSTOMERACCOUNTS)) {
			JsonElement jsonElement = response.get(DBPDatasetConstants.DATASET_CUSTOMERACCOUNTS);
			if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
				accountsArray = jsonElement.getAsJsonArray();
				for (JsonElement element : accountsArray) {
					JsonObject account = element.getAsJsonObject();
					account.add(InfinityConstants.accountId, account.get(InfinityConstants.Account_id));
					account.add(InfinityConstants.accountName, account.get(InfinityConstants.AccountName));
					account.add(InfinityConstants.ownerType, account.get("own"));
					account.add(InfinityConstants.typeId, account.get(InfinityConstants.accountType));
					account.addProperty(InfinityConstants.isEnabled, "true");
					if (!accountSet.contains(account.get(InfinityConstants.Account_id).getAsString())) {
						accountSet.add(account.get(InfinityConstants.Account_id).getAsString());
					}
				}
			}
		}

		for (int i = 0; i < accounts.size(); i++) {
			JsonObject account = accounts.get(i).getAsJsonObject();
			if (!accountSet.contains(account.get(InfinityConstants.accountId).getAsString())) {
				accountSet.add(account.get(InfinityConstants.accountId).getAsString());
				newAccounts.add(account.get(InfinityConstants.accountId).getAsString());
			} else {
				accounts.remove(i);
				i--;
			}
		}

		map.put(InfinityConstants.accounts, accounts.toString());

		return newAccounts;
	}

	private JsonObject createInfinityUserPayload(Map<String, String> map, String contractId,
			Map<String, Object> headerMap, String autoSyncAccounts, DataControllerRequest request,
			boolean isGlobalRequired) throws ApplicationException {

		JsonObject jsonObject = new JsonObject();
		String customerId = map.get(InfinityConstants.customerId);

		JsonArray companyList = new JsonArray();
		JsonObject company = new JsonObject();

		String coreCustomerId = map.get(InfinityConstants.coreCustomerId);
		String serviceDefinition = map.get(InfinityConstants.serviceDefinition);
		String roleId = map.get(InfinityConstants.roleId);
		String accountsString = map.get(InfinityConstants.accounts);
		String legalEntityId = map.get(InfinityConstants.legalEntityId);
		JsonArray accounts = new JsonArray();
		try {
			accounts = new JsonParser().parse(accountsString).getAsJsonArray();
			for (JsonElement accountElement : accounts) {
				JsonObject account = accountElement.getAsJsonObject();
				JsonElement accelem  =  account.get(InfinityConstants.typeId);
				if (accelem !=null && !accelem.isJsonNull())
				{
					account.addProperty(InfinityConstants.accountType, HelperMethods.getAccountsNames().get(accelem.getAsString()));
				}
			}
		} catch (Exception e) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10349, "Invalid account Array");
		}

		company.addProperty(InfinityConstants.roleId, roleId);
		company.addProperty(InfinityConstants.serviceDefinition, serviceDefinition);
		company.addProperty(InfinityConstants.cif, coreCustomerId);
		company.addProperty(InfinityConstants.isPrimary, "true");
		company.addProperty(InfinityConstants.contractId, contractId);
		company.addProperty(InfinityConstants.legalEntityId, legalEntityId);
		company.add(InfinityConstants.accounts, accounts);

		Map<String, String> bundleConfigurations = BundleConfigurationHandler
				.fetchBundleConfigurations(BundleConfigurationHandler.BUDLENAME_C360, request);

		autoSyncAccounts = autoSyncAccounts.equals(BundleConfigurationHandler.DEFAULT_BUSINESS_SERVICE_ID)
				? bundleConfigurations.get(BundleConfigurationHandler.AUTO_SYNC_BUSINESS_ACCOUNTS)
				: bundleConfigurations.get(BundleConfigurationHandler.AUTO_SYNC_RETAIL_ACCOUNTS);

		if (StringUtils.isNotBlank(autoSyncAccounts)) {
			company.addProperty(InfinityConstants.autoSyncAccounts, autoSyncAccounts);
		} else {
			company.addProperty(InfinityConstants.autoSyncAccounts, false);
		}

		companyList.add(company);

		ContractBackendDelegate contractBackendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(ContractBackendDelegate.class);
		
		FeatureActionLimitsDTO coreCustomerFeatureActionDTO = contractBackendDelegate.getRestrictiveFeatureActionLimits(
				serviceDefinition, null, roleId, coreCustomerId, "", headerMap, false, "",legalEntityId);
		
		JsonArray accountLevelFeatures = new JsonArray();
		JsonArray globalLevelFeatures = new JsonArray();

		Map<String, Set<String>> featureAction = coreCustomerFeatureActionDTO.getFeatureaction();

		for (Entry<String, Set<String>> featureEntry : featureAction.entrySet()) {

			String featureId = featureEntry.getKey();

			JsonObject featureJson = coreCustomerFeatureActionDTO.getFeatureInfo().get(featureId);

			JsonObject featureObjectGlobal = new JsonObject();

			for (Entry<String, JsonElement> featureParams : featureJson.entrySet()) {
				featureObjectGlobal.add(featureParams.getKey(), featureParams.getValue());
			}

			JsonObject featureObjectAccount = new JsonObject();

			for (Entry<String, JsonElement> featureParams : featureJson.entrySet()) {
				featureObjectAccount.add(featureParams.getKey(), featureParams.getValue());
			}

			JsonArray accountpermissions = new JsonArray();
			JsonArray globalpermissions = new JsonArray();

			for (String action : featureEntry.getValue()) {

				JsonObject actionJson = coreCustomerFeatureActionDTO.getActionsInfo().get(action);

				boolean isAccountLevel = actionJson.has(InfinityConstants.isAccountLevel)
						&& !actionJson.get(InfinityConstants.isAccountLevel).isJsonNull()
						&& ("1".equals(actionJson.get(InfinityConstants.isAccountLevel).getAsString())
							||	
							"true".equals(actionJson.get(InfinityConstants.isAccountLevel).getAsString()));

				if (!isAccountLevel) {
					JsonObject actionObjectglobal = new JsonObject();
					for (Entry<String, JsonElement> actionParams : actionJson.entrySet()) {
						actionObjectglobal.add(actionParams.getKey(), actionParams.getValue());
					}

					actionObjectglobal.add(InfinityConstants.id, actionJson.get(InfinityConstants.actionId));
					actionObjectglobal.addProperty(InfinityConstants.isEnabled, "true");
					globalpermissions.add(actionObjectglobal);
				} else {
					JsonObject actionObjectAccount = new JsonObject();
					for (Entry<String, JsonElement> actionParams : actionJson.entrySet()) {
						actionObjectAccount.add(actionParams.getKey(), actionParams.getValue());
					}

					actionObjectAccount.add(InfinityConstants.id, actionJson.get(InfinityConstants.actionId));
					actionObjectAccount.addProperty(InfinityConstants.isEnabled, "true");

					accountpermissions.add(actionObjectAccount);
				}

			}

			if (globalpermissions.size() > 0) {
				featureObjectGlobal.add(InfinityConstants.permissions, globalpermissions);
				globalLevelFeatures.add(featureObjectGlobal);
			}

			if (accountpermissions.size() > 0) {
				featureObjectAccount.add(InfinityConstants.permissions, accountpermissions);
				accountLevelFeatures.add(featureObjectAccount);
			}
		}

		JsonArray globalLevelpermissions = new JsonArray();
		JsonObject globalLevelpermission = new JsonObject();

		globalLevelpermission.addProperty(InfinityConstants.cif, coreCustomerId);
		globalLevelpermission.addProperty(InfinityConstants.legalEntityId, legalEntityId);
		globalLevelpermission.add(InfinityConstants.features, globalLevelFeatures);
		globalLevelpermissions.add(globalLevelpermission);

		JsonArray accountLevelPermissions = new JsonArray();
		JsonObject accountLevelPermission = new JsonObject();
		JsonArray limitAccounts = new JsonArray();
		for (JsonElement accountElement : accounts) {

			JsonObject account = new JsonObject();

			for (Entry<String, JsonElement> accountParams : accountElement.getAsJsonObject().entrySet()) {
				account.add(accountParams.getKey(), accountParams.getValue());
			}

			account.add(InfinityConstants.featurePermissions, accountLevelFeatures);

			limitAccounts.add(account);
		}

		accountLevelPermission.addProperty(InfinityConstants.cif, coreCustomerId);
		accountLevelPermission.add(InfinityConstants.accounts, limitAccounts);
		accountLevelPermission.addProperty(InfinityConstants.legalEntityId, legalEntityId);
		accountLevelPermissions.add(accountLevelPermission);

		JsonArray features = new JsonArray();

		JsonArray limitGroups = new JsonArray();
		Map<String, Map<String, Double>> limitGroupsMap = new HashMap<String, Map<String, Double>>();

		Map<String, Map<String, Map<String, String>>> featureActionLimits = coreCustomerFeatureActionDTO
				.getMonetaryActionLimits();

		for (Entry<String, Map<String, Map<String, String>>> featureEntry : featureActionLimits.entrySet()) {

			String featureId = featureEntry.getKey();

			JsonObject featureJson = coreCustomerFeatureActionDTO.getFeatureInfo().get(featureId);

			for (Entry<String, Map<String, String>> actionEntry : featureEntry.getValue().entrySet()) {

				JsonObject feature = new JsonObject();

				for (Entry<String, JsonElement> featureParams : featureJson.entrySet()) {
					feature.add(featureParams.getKey(), featureParams.getValue());
				}

				JsonObject actionJson = coreCustomerFeatureActionDTO.getActionsInfo().get(actionEntry.getKey());

				for (Entry<String, JsonElement> actionParams : actionJson.entrySet()) {
					feature.add(actionParams.getKey(), actionParams.getValue());

				}

				String limitGroupId = actionJson.has(InfinityConstants.limitGroupId)
						&& !actionJson.get(InfinityConstants.limitGroupId).isJsonNull()
								? actionJson.get(InfinityConstants.limitGroupId).getAsString()
								: null;

				feature.addProperty(InfinityConstants.isEnabled, "true");

				JsonArray limits = new JsonArray();

				if (!limitGroupsMap.containsKey(limitGroupId)) {
					limitGroupsMap.put(limitGroupId, new HashMap<String, Double>());
				}

				for (Entry<String, String> limitsEntry : actionEntry.getValue().entrySet()) {
					JsonObject limit = new JsonObject();

					if (!limitGroupsMap.get(limitGroupId).containsKey(limitsEntry.getKey())) {
						limitGroupsMap.get(limitGroupId).put(limitsEntry.getKey(), 0.0);
					}

					Double value = Double.parseDouble(limitsEntry.getValue());

					if (limitsEntry.getKey().equals(InfinityConstants.DAILY_LIMIT)) {
						limit.addProperty(InfinityConstants.id, InfinityConstants.PRE_APPROVED_DAILY_LIMIT);
						limit.addProperty(InfinityConstants.value, "0.0");
						limits.add(limit);
						limit = new JsonObject();
						limit.addProperty(InfinityConstants.id, InfinityConstants.AUTO_DENIED_DAILY_LIMIT);
						limit.addProperty(InfinityConstants.value, limitsEntry.getValue());
						limits.add(limit);
						
						limit = new JsonObject();
						limit.addProperty(InfinityConstants.id, InfinityConstants.DAILY_LIMIT);
						limit.addProperty(InfinityConstants.value, limitsEntry.getValue());
						limits.add(limit);

						limitGroupsMap.get(limitGroupId).put(limitsEntry.getKey(),
								value + limitGroupsMap.get(limitGroupId).get(limitsEntry.getKey()));
					} else if (limitsEntry.getKey().equals(InfinityConstants.WEEKLY_LIMIT)) {
						limit.addProperty(InfinityConstants.id, InfinityConstants.PRE_APPROVED_WEEKLY_LIMIT);
						limit.addProperty(InfinityConstants.value, "0.0");
						limits.add(limit);
						limit = new JsonObject();
						limit.addProperty(InfinityConstants.id, InfinityConstants.AUTO_DENIED_WEEKLY_LIMIT);
						limit.addProperty(InfinityConstants.value, limitsEntry.getValue());
						limits.add(limit);
						
						limit = new JsonObject();
						limit.addProperty(InfinityConstants.id, InfinityConstants.WEEKLY_LIMIT);
						limit.addProperty(InfinityConstants.value, limitsEntry.getValue());
						limits.add(limit);
						
						limitGroupsMap.get(limitGroupId).put(limitsEntry.getKey(),
								value + limitGroupsMap.get(limitGroupId).get(limitsEntry.getKey()));
					} else {
						limit.addProperty(InfinityConstants.id, InfinityConstants.PRE_APPROVED_TRANSACTION_LIMIT);
						limit.addProperty(InfinityConstants.value, "0.0");
						limits.add(limit);
						limit = new JsonObject();
						limit.addProperty(InfinityConstants.id, InfinityConstants.AUTO_DENIED_TRANSACTION_LIMIT);
						limit.addProperty(InfinityConstants.value, limitsEntry.getValue());
						limits.add(limit);
						
						limit = new JsonObject();
						limit.addProperty(InfinityConstants.id, InfinityConstants.MAX_TRANSACTION_LIMIT);
						limit.addProperty(InfinityConstants.value, limitsEntry.getValue());
						limits.add(limit);
						
						limitGroupsMap.get(limitGroupId).put(limitsEntry.getKey(),
								value > limitGroupsMap.get(limitGroupId).get(limitsEntry.getKey()) ? value
										: limitGroupsMap.get(limitGroupId).get(limitsEntry.getKey()));
					}

				}
				feature.add(InfinityConstants.limits, limits);
				features.add(feature);
			}
		}

		limitAccounts = new JsonArray();
		for (JsonElement accountElement : accounts) {

			JsonObject account = new JsonObject();

			for (Entry<String, JsonElement> accountParams : accountElement.getAsJsonObject().entrySet()) {
				account.add(accountParams.getKey(), accountParams.getValue());
			}

			account.add(InfinityConstants.featurePermissions, features);

			limitAccounts.add(account);
		}

		for (Entry<String, Map<String, Double>> entry : limitGroupsMap.entrySet()) {
			JsonObject limitGroupJsonObject = new JsonObject();

			limitGroupJsonObject.addProperty(InfinityConstants.limitGroupId, entry.getKey());

			JsonArray limits = new JsonArray();
			for (Entry<String, Double> limitEntry : entry.getValue().entrySet()) {
				JsonObject limit = new JsonObject();
				limit.addProperty(InfinityConstants.id, limitEntry.getKey());
				limit.addProperty(InfinityConstants.value, limitEntry.getValue() + "");
				limits.add(limit);
			}
			limitGroupJsonObject.add(InfinityConstants.limits, limits);
			limitGroups.add(limitGroupJsonObject);
		}

		JsonObject transactionLimit = new JsonObject();
		transactionLimit.addProperty(InfinityConstants.cif, coreCustomerId);
		transactionLimit.add(InfinityConstants.accounts, limitAccounts);
		transactionLimit.add(InfinityConstants.limitGroups, limitGroups);
		transactionLimit.addProperty(InfinityConstants.legalEntityId, legalEntityId);
		JsonArray transactionLimits = new JsonArray();

		transactionLimits.add(transactionLimit);

		JsonObject userDetails = new JsonObject();

		userDetails.addProperty(InfinityConstants.id, customerId);
		userDetails.addProperty(InfinityConstants.legalEntityId, legalEntityId);

		map.put(InfinityConstants.userDetails, userDetails.toString());
		map.put(InfinityConstants.companyList, companyList.toString());
		if (isGlobalRequired) {
			map.put(InfinityConstants.globalLevelPermissions, globalLevelpermissions.toString());
		} else {
			map.put(InfinityConstants.globalLevelPermissions, new JsonArray().toString());
		}
		map.put(InfinityConstants.accountLevelPermissions, accountLevelPermissions.toString());
		map.put(InfinityConstants.transactionLimits, transactionLimits.toString());

		jsonObject.add(InfinityConstants.userDetails, userDetails);
		jsonObject.add(InfinityConstants.companyList, companyList);
		if (isGlobalRequired) {
			jsonObject.add(InfinityConstants.globalLevelPermissions, globalLevelpermissions);
		} else {
			jsonObject.add(InfinityConstants.globalLevelPermissions, new JsonArray());
		}

		jsonObject.add(InfinityConstants.accountLevelPermissions, accountLevelPermissions);
		jsonObject.add(InfinityConstants.transactionLimits, transactionLimits);

		diagnostic.prepareDebug("input Map to create InfinityUserMap " + map).log();

		return jsonObject;
	}

	public Map<String, String> processLegalEntitydefaultServiceDefsData(String serviceDefJson) throws ApplicationException {
		Map<String, String> legalEntityDefaultServDefs = new HashMap<>();
		try {
			legalEntityDefaultServDefs = new ObjectMapper().readValue(serviceDefJson, HashMap.class);
		} catch (JsonProcessingException e) {
			alert.prepareError("Error in parsing default service definitions", e).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_29053, "Improper default service defition data");
		}
		return legalEntityDefaultServDefs;
	}
	private void getContractPayload(Map<String, String> map, DataControllerRequest request, String coreCustomerId,
			String serviceDefinitionId) throws ApplicationException {
		// TODO Auto-generated method stub
		LegalEntityUtil.addCompanyIDToHeaders(request);
		String customerId = map.get(InfinityConstants.customerId);

		String accountsString = map.get(InfinityConstants.accounts);

		String contractName = map.get(InfinityConstants.contractName);
		String coreCustomerName = map.get(InfinityConstants.coreCustomerName);
		String legalEntityId = map.get(InfinityConstants.legalEntityId);

		if (StringUtils.isBlank(customerId)) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10349, "CustomerId is Empty");
		}

		JsonArray accounts = new JsonArray();
		try {
			accounts = new JsonParser().parse(accountsString).getAsJsonArray();

		} catch (Exception e) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10349, "Invalid Accounts input");
		}

		String implicit = "false";

		Map<String, String> bundleConfigurations = BundleConfigurationHandler
				.fetchBundleConfigurations(BundleConfigurationHandler.BUDLENAME_C360, request);

		
		implicit = serviceDefinitionId.equals(BundleConfigurationHandler.DEFAULT_BUSINESS_SERVICE_ID)
				? bundleConfigurations.get(BundleConfigurationHandler.AUTO_SYNC_BUSINESS_ACCOUNTS)
				: bundleConfigurations.get(BundleConfigurationHandler.AUTO_SYNC_RETAIL_ACCOUNTS);
		String serviceDefJson = bundleConfigurations.get(serviceDefinitionId);
		Map<String, String> legalEntityDefaultServDefs = processLegalEntitydefaultServiceDefsData(serviceDefJson);
		serviceDefinitionId = legalEntityDefaultServDefs.get(legalEntityId);

		String defaultRole = null;

		HashMap<String, Object> input = new HashMap<String, Object>();

		String filter = InfinityConstants.serviceDefinitionId + DBPUtilitiesConstants.EQUAL + serviceDefinitionId
				+ DBPUtilitiesConstants.AND + InfinityConstants.isDefaultGroup + DBPUtilitiesConstants.EQUAL + '1'
				+ DBPUtilitiesConstants.AND + InfinityConstants.LegalEntityId + DBPUtilitiesConstants.EQUAL + legalEntityId;

		input.put(DBPUtilitiesConstants.FILTER, filter);
		JsonObject jsonResponse = ServiceCallHelper.invokeServiceAndGetJson(input, request.getHeaderMap(),
				URLConstants.GROUP_SERVICEDEFINITION);

		if (jsonResponse.has(DBPDatasetConstants.DATASET_GROUPSERVICEDEFINITION)
				&& jsonResponse.get(DBPDatasetConstants.DATASET_GROUPSERVICEDEFINITION).isJsonArray()) {

			JsonObject groupServiceDefinition = jsonResponse.get(DBPDatasetConstants.DATASET_GROUPSERVICEDEFINITION)
					.getAsJsonArray().size() > 0
							? jsonResponse.get(DBPDatasetConstants.DATASET_GROUPSERVICEDEFINITION).getAsJsonArray()
									.get(0).getAsJsonObject()
							: new JsonObject();

			defaultRole = groupServiceDefinition.has(InfinityConstants.Group_id)
					&& !groupServiceDefinition.get(InfinityConstants.Group_id).isJsonNull()
							? groupServiceDefinition.get(InfinityConstants.Group_id).getAsString()
							: null;

		}

		if (HelperMethods.isBlank(serviceDefinitionId, defaultRole)) {

			throw new ApplicationException(ErrorCodeEnum.ERR_10349, "Invalid serviceDefinition or Role");
		}

		String serviceDefinitionName = "serviceDefinition";

		filter = InfinityConstants.id + DBPUtilitiesConstants.EQUAL + serviceDefinitionId
				+ DBPUtilitiesConstants.AND + InfinityConstants.LegalEntityId + DBPUtilitiesConstants.EQUAL + legalEntityId;

		input = new HashMap<String, Object>();
		input.put(DBPUtilitiesConstants.FILTER, filter);
		jsonResponse = ServiceCallHelper.invokeServiceAndGetJson(input, request.getHeaderMap(),
				URLConstants.SERVICEDEFINITION_GET);

		if (jsonResponse.has(DBPDatasetConstants.DATASET_SERVICEDEFINITION)
				&& jsonResponse.get(DBPDatasetConstants.DATASET_SERVICEDEFINITION).isJsonArray()) {

			JsonObject serviceDefinition = jsonResponse.get(DBPDatasetConstants.DATASET_SERVICEDEFINITION)
					.getAsJsonArray().size() > 0
							? jsonResponse.get(DBPDatasetConstants.DATASET_SERVICEDEFINITION).getAsJsonArray().get(0)
									.getAsJsonObject()
							: new JsonObject();
			serviceDefinitionName = serviceDefinition.has(InfinityConstants.name)
					&& !serviceDefinition.get(InfinityConstants.name).isJsonNull()
							? serviceDefinition.get(InfinityConstants.name).getAsString()
							: "serviceDefinition";
		}

		if (HelperMethods.isBlank(customerId, coreCustomerId)) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10349, "coreCustomerId is Empty");
		}

		map.put(InfinityConstants.roleId, defaultRole);
		map.put(InfinityConstants.serviceDefinitionName, serviceDefinitionName);
		map.put(InfinityConstants.serviceDefinitionId, serviceDefinitionId);
		map.put(InfinityConstants.serviceDefinition, serviceDefinitionId);
		map.put(InfinityConstants.coreCustomerId, coreCustomerId);
		map.put(InfinityConstants.customerId, customerId);
		String groupId = "DEFAULT_GROUP";
		if (StringUtils.isNotBlank(bundleConfigurations.get(BundleConfigurationHandler.DEFAULT_PROSPECT_GROUP))) {
			groupId = bundleConfigurations.get(BundleConfigurationHandler.DEFAULT_PROSPECT_GROUP);
		}
		map.put(BundleConfigurationHandler.DEFAULT_PROSPECT_GROUP, groupId);
		Map<String, Object> inputParams = new HashMap<String, Object>();
		filter = "id" + DBPUtilitiesConstants.EQUAL + customerId;
		inputParams.put(DBPUtilitiesConstants.FILTER, filter);

		CommunicationBackendDelegate communicationBackendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(CommunicationBackendDelegate.class);

		CustomerCommunicationDTO communicationDTO = new CustomerCommunicationDTO();
		communicationDTO.setCustomer_id(customerId);
		DBXResult dbxResult = communicationBackendDelegate.getPrimaryCommunicationDetails(communicationDTO,
				request.getHeaderMap());

		CoreCustomerBusinessDelegate coreCustomerBusinessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(CoreCustomerBusinessDelegate.class);
		MembershipDTO membershipDetails = coreCustomerBusinessDelegate.getMembershipDetails(coreCustomerId,
				legalEntityId, request.getHeaderMap());

		request.getHeaderMap().put("companyId", map.get("companyId"));

		JsonArray addressArray = new JsonArray();
		JsonObject addressRecord = new JsonObject();

		addressRecord.addProperty(InfinityConstants.country, membershipDetails.getCountry());
		addressRecord.addProperty(InfinityConstants.cityName, membershipDetails.getCityName());
		addressRecord.addProperty(InfinityConstants.state, membershipDetails.getState());
		addressRecord.addProperty(InfinityConstants.zipCode, membershipDetails.getZipCode());
		addressRecord.addProperty(InfinityConstants.addressLine1, membershipDetails.getAddressLine1());
		addressRecord.addProperty(InfinityConstants.addressLine2, membershipDetails.getAddressLine2());

		addressArray.add(addressRecord);

		JsonArray array = new JsonArray();

		if (dbxResult.getResponse() != null) {
			JsonObject communication = new JsonObject();
			JsonObject jsonObject = (JsonObject) dbxResult.getResponse();
			jsonObject = jsonObject.has(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION)
					&& !jsonObject.get(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION).isJsonNull()
							? jsonObject.get(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION).getAsJsonObject()
							: new JsonObject();
			String email = jsonObject.has(DTOConstants.EMAIL) && !jsonObject.get(DTOConstants.EMAIL).isJsonNull()
					? jsonObject.get(DTOConstants.EMAIL).getAsString()
					: null;
			String phone = jsonObject.has(DTOConstants.PHONE) && !jsonObject.get(DTOConstants.PHONE).isJsonNull()
					? jsonObject.get(DTOConstants.PHONE).getAsString()
					: null;
			String phoneCountryCode = jsonObject.has(DTOConstants.PHONECOUNTRYCODE)
					&& !jsonObject.get(DTOConstants.PHONECOUNTRYCODE).isJsonNull()
							? jsonObject.get(DTOConstants.PHONECOUNTRYCODE).getAsString()
							: null;
			communication.addProperty(InfinityConstants.phoneNumber, phone);
			communication.addProperty(InfinityConstants.phoneCountryCode, phoneCountryCode);
			communication.addProperty(InfinityConstants.email, email);
			if (HelperMethods.isBlank(phone, email)) {
				throw new ApplicationException(ErrorCodeEnum.ERR_10349, "invalid communication");
			}
			array.add(communication);
		}

		JsonObject jsonObject = new JsonObject();

		jsonObject.addProperty(InfinityConstants.isPrimary, "true");
		jsonObject.addProperty(InfinityConstants.coreCustomerId, coreCustomerId);
		jsonObject.addProperty(InfinityConstants.coreCustomerName, coreCustomerName);
		jsonObject.addProperty(InfinityConstants.coreCustomerName, coreCustomerName);
		jsonObject.addProperty("implicitAccountAccess", implicit);
		jsonObject.add(InfinityConstants.accounts, accounts);

		ContractBackendDelegate contractBackendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(ContractBackendDelegate.class);

		FeatureActionLimitsDTO coreCustomerFeatureActionDTO = contractBackendDelegate.getRestrictiveFeatureActionLimits(
				serviceDefinitionId, null, defaultRole, null, "", request.getHeaderMap(), false, "",legalEntityId);

		JsonArray accountLevelFeatures = new JsonArray();
		JsonArray globalLevelFeatures = new JsonArray();

		Map<String, Set<String>> featureAction = coreCustomerFeatureActionDTO.getFeatureaction();

		for (Entry<String, Set<String>> featureEntry : featureAction.entrySet()) {

			String featureId = featureEntry.getKey();

			JsonObject featureJson = coreCustomerFeatureActionDTO.getFeatureInfo().get(featureId);

			JsonObject featureObjectGlobal = new JsonObject();

			for (Entry<String, JsonElement> featureParams : featureJson.entrySet()) {
				featureObjectGlobal.add(featureParams.getKey(), featureParams.getValue());
			}

			JsonObject featureObjectAccount = new JsonObject();

			for (Entry<String, JsonElement> featureParams : featureJson.entrySet()) {
				featureObjectAccount.add(featureParams.getKey(), featureParams.getValue());
			}

			JsonArray accountpermissions = new JsonArray();
			JsonArray globalpermissions = new JsonArray();

			for (String action : featureEntry.getValue()) {

				JsonObject actionJson = coreCustomerFeatureActionDTO.getActionsInfo().get(action);

				boolean isAccountLevel = actionJson.has(InfinityConstants.isAccountLevel)
						&& !actionJson.get(InfinityConstants.isAccountLevel).isJsonNull()
						&& ("1".equals(actionJson.get(InfinityConstants.isAccountLevel).getAsString())
							|| "true".equals(actionJson.get(InfinityConstants.isAccountLevel).getAsString())
							);

				if (!isAccountLevel) {
					JsonObject actionObjectglobal = new JsonObject();
					for (Entry<String, JsonElement> actionParams : actionJson.entrySet()) {
						actionObjectglobal.add(actionParams.getKey(), actionParams.getValue());
					}

					actionObjectglobal.add(InfinityConstants.id, actionJson.get(InfinityConstants.actionId));
					actionObjectglobal.addProperty(InfinityConstants.isEnabled, "true");
					globalpermissions.add(actionObjectglobal);
				} else {
					JsonObject actionObjectAccount = new JsonObject();
					for (Entry<String, JsonElement> actionParams : actionJson.entrySet()) {
						actionObjectAccount.add(actionParams.getKey(), actionParams.getValue());
					}

					actionObjectAccount.add(InfinityConstants.id, actionJson.get(InfinityConstants.actionId));
					actionObjectAccount.addProperty(InfinityConstants.isEnabled, "true");

					accountpermissions.add(actionObjectAccount);
					globalpermissions.add(actionObjectAccount);
				}

			}

			if (globalpermissions.size() > 0) {
				featureObjectGlobal.add(InfinityConstants.permissions, globalpermissions);
				globalLevelFeatures.add(featureObjectGlobal);
			}

			if (accountpermissions.size() > 0) {
				featureObjectAccount.add(InfinityConstants.permissions, accountpermissions);
				accountLevelFeatures.add(featureObjectAccount);
			}
		}

		JsonArray globalLevelpermissions = new JsonArray();
		JsonObject globalLevelpermission = new JsonObject();

		globalLevelpermission.addProperty(InfinityConstants.coreCustomerId, coreCustomerId);
		globalLevelpermission.add(InfinityConstants.features, globalLevelFeatures);
		globalLevelpermissions.add(globalLevelpermission);

		JsonArray accountLevelPermissions = new JsonArray();
		JsonObject accountLevelPermission = new JsonObject();
		JsonArray limitAccounts = new JsonArray();
		for (JsonElement accountElement : accounts) {

			JsonObject account = new JsonObject();

			for (Entry<String, JsonElement> accountParams : accountElement.getAsJsonObject().entrySet()) {
				account.add(accountParams.getKey(), accountParams.getValue());
			}

			account.add(InfinityConstants.featurePermissions, accountLevelFeatures);

			limitAccounts.add(account);
		}

		accountLevelPermission.addProperty(InfinityConstants.coreCustomerId, coreCustomerId);
		accountLevelPermission.add(InfinityConstants.accounts, limitAccounts);
		accountLevelPermission.add("featurepermissions", accountLevelFeatures);
		accountLevelPermissions.add(accountLevelPermission);

		JsonArray features = new JsonArray();

		JsonArray transactionLimits = new JsonArray();
		JsonObject transactionLimit = new JsonObject();
		Map<String, Map<String, Map<String, String>>> featureActionLimits = coreCustomerFeatureActionDTO
				.getMonetaryActionLimits();

		for (Entry<String, Map<String, Map<String, String>>> featureEntry : featureActionLimits.entrySet()) {

			String featureId = featureEntry.getKey();

			JsonObject featureJson = coreCustomerFeatureActionDTO.getFeatureInfo().get(featureId);

			for (Entry<String, Map<String, String>> actionEntry : featureEntry.getValue().entrySet()) {

				JsonObject feature = new JsonObject();

				for (Entry<String, JsonElement> featureParams : featureJson.entrySet()) {
					feature.add(featureParams.getKey(), featureParams.getValue());
				}

				JsonObject actionJson = coreCustomerFeatureActionDTO.getActionsInfo().get(actionEntry.getKey());

				for (Entry<String, JsonElement> actionParams : actionJson.entrySet()) {
					feature.add(actionParams.getKey(), actionParams.getValue());

				}

				JsonArray limits = new JsonArray();
				for (Entry<String, String> limitsEntry : actionEntry.getValue().entrySet()) {
					JsonObject limit = new JsonObject();
					limit.addProperty(InfinityConstants.id, limitsEntry.getKey());
					limit.addProperty(InfinityConstants.value, limitsEntry.getValue() + "");
					limits.add(limit);
				}
				feature.add(InfinityConstants.limits, limits);
				features.add(feature);
			}
				
			
		}
		transactionLimit.addProperty(InfinityConstants.coreCustomerId, coreCustomerId);
		transactionLimit.add("featurePermissions", features);

		transactionLimits.add(transactionLimit);

		map.put(InfinityConstants.contractName, contractName);
		map.put(InfinityConstants.serviceDefinitionName, serviceDefinitionName);
		map.put(InfinityConstants.serviceDefinitionId, serviceDefinitionId);
		map.put(InfinityConstants.communication, array.toString());
		map.put(InfinityConstants.address, addressArray.toString());
		map.put(InfinityConstants.faxId, null);
		JsonArray contractCustomers = new JsonArray();
		contractCustomers.add(jsonObject);
		map.put(InfinityConstants.contractCustomers, contractCustomers.toString());
		map.put("accountLevelPermissions1", accountLevelPermissions.toString());
		map.put("globalLevelPermissions1", globalLevelpermissions.toString());
		map.put("transactionLimits1", transactionLimits.toString());


		diagnostic.prepareDebug("input Map to create Contract " + map).log();

	}

	@Override
	public Object applyCustomRole(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();

		Map<String, String> map = HelperMethods.getInputParamMap(inputArray);

		Iterator<String> iterator = request.getParameterNames();
		LegalEntityUtil.addCompanyIDToHeaders(request);
		while (iterator.hasNext()) {
			String key = iterator.next();
			if ((!map.containsKey(key) || StringUtils.isBlank(map.get(key)))
					&& StringUtils.isNotBlank(request.getParameter(key))) {
				map.put(key, request.getParameter(key));
			}
		}

		Map<String, String> loggedInUserInfo = HelperMethods.getCustomerFromAPIDBPIdentityService(request);
		if (HelperMethods.isAuthenticationCheckRequiredForService(loggedInUserInfo)) {
			Set<String> userPermissions = SessionScope.getAllPermissionsFromIdentityScope(request);
			if (!userPermissions.contains("USER_MANAGEMENT")) {
				ErrorCodeEnum.ERR_10051.setErrorCode(result);
				return result;
			}
		}

		if (!map.containsKey(InfinityConstants.userList) || StringUtils.isBlank(map.get(InfinityConstants.userList))) {
			ErrorCodeEnum.ERR_10056.setErrorCode(result);
			return result;
		}

		if (!map.containsKey(InfinityConstants.id) || StringUtils.isBlank(map.get(InfinityConstants.id))) {
			ErrorCodeEnum.ERR_10056.setErrorCode(result);
			return result;
		}

		String userList = map.get(InfinityConstants.userList);
		JsonElement userListElement = new JsonParser().parse(userList);
		if (userListElement.isJsonNull() || !userListElement.isJsonArray()) {
			ErrorCodeEnum.ERR_10056.setErrorCode(result);
			return result;
		}

		JsonObject jsonObject = new JsonObject();

		jsonObject.addProperty(InfinityConstants.id, map.get(InfinityConstants.id));

		jsonObject.addProperty(InfinityConstants.customerId, HelperMethods.getCustomerIdFromSession(request));

		InfinityUserManagementBusinessDelegate infinityUserManagementBusinessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(InfinityUserManagementBusinessDelegate.class);
		DBXResult dbxResult = infinityUserManagementBusinessDelegate.getCustomRole(jsonObject, request.getHeaderMap());
		if (dbxResult.getResponse() != null) {
			jsonObject = (JsonObject) dbxResult.getResponse();
		} else {
			ErrorCodeEnum.ERR_10056.setErrorCode(result, "Unable to get CustomRoleDetails");
			return result;
		}

		JsonArray userListJsonArray = userListElement.getAsJsonArray();

		for (JsonElement element : userListJsonArray) {

			if (!element.isJsonObject()) {
				continue;
			}

			JsonObject userListJsonObject = element.getAsJsonObject();
			if (!userListJsonObject.has(InfinityConstants.infinityUserId)
					|| userListJsonObject.get(InfinityConstants.infinityUserId).isJsonNull()) {
				continue;
			}

			JsonObject editUserJsonObject = new JsonObject();
			for (Entry<String, JsonElement> entry : jsonObject.entrySet()) {
				editUserJsonObject.add(entry.getKey(), entry.getValue());
			}

			JsonObject userDetailsJsonObject = new JsonObject();
			String infinityUserId = userListJsonObject.get(InfinityConstants.infinityUserId).getAsString();
			userDetailsJsonObject.addProperty(InfinityConstants.id, infinityUserId);
			editUserJsonObject.add(InfinityConstants.userDetails, userDetailsJsonObject);
			try {
			dbxResult = infinityUserManagementBusinessDelegate.editInfinityUser(editUserJsonObject,
					request.getHeaderMap());
			}
			catch (Exception e) {
				alert.prepareError("Error occured",e).log();
			}
			if (dbxResult.getResponse() != null) {
				jsonObject = (JsonObject) dbxResult.getResponse();
			} else {
				ErrorCodeEnum.ERR_10056.setErrorCode(result, "Unable to apply customRole");
				return result;
			}
		}

		result.addParam(InfinityConstants.status, "success");

		diagnostic.prepareDebug("Json request " + jsonObject.toString()).log();

		return result;
	}

	@Override
	public Result UpdateInfinityUserStatus(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse response) throws ApplicationException {
		String eventCode = null;
		Result result = new Result();
		final String USERID_INPUT = "userId";
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		List<CustomerDTO> customerList = new ArrayList<>();

		CustomerDTO inputDTO = new CustomerDTO();

		String userId = StringUtils.isNotBlank(inputParams.get(USERID_INPUT)) ? inputParams.get(USERID_INPUT)
				: dcRequest.getParameter(USERID_INPUT);

		if (StringUtils.isBlank(userId)) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10405);
		}

		inputDTO.setId(userId);
		try {

			UserManagementBusinessDelegate usermanagementBusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(UserManagementBusinessDelegate.class);
			customerList = usermanagementBusinessDelegate.getCustomerDetails(inputDTO, dcRequest.getHeaderMap());
		} catch (ApplicationException e) {
			throw new ApplicationException(e.getErrorCodeEnum());
		} catch (Exception e) {
			alert.prepareError("Exception occured while calling the UserManagement Business delegate :" + e.getMessage()).log();
		}
		if (customerList.isEmpty() || StringUtils.isBlank(customerList.get(0).getId())) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10405);
		}
		CustomerDTO userUnderUpdateDTO = customerList.get(0);
		String statusId = null;
		if (DBPUtilitiesConstants.CUSTOMER_STATUS_ACTIVE.equalsIgnoreCase(customerList.get(0).getStatus_id())) {
			statusId = DBPUtilitiesConstants.CUSTOMER_STATUS_SUSPENDED;
		} else if (DBPUtilitiesConstants.CUSTOMER_STATUS_SUSPENDED
				.equalsIgnoreCase(customerList.get(0).getStatus_id())) {
			statusId = DBPUtilitiesConstants.CUSTOMER_STATUS_ACTIVE;
		}

		if (StringUtils.isBlank(statusId)) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10405);
		}

		inputDTO = new CustomerDTO();
		inputDTO.setId(customerList.get(0).getId());
		inputDTO.setStatus_id(statusId);

		boolean isUpdateSuccess = false;
		try {
			UserManagementBusinessDelegate usermanagementBusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(UserManagementBusinessDelegate.class);
			isUpdateSuccess = usermanagementBusinessDelegate.updateCustomerDetails(inputDTO, dcRequest.getHeaderMap());
		} catch (ApplicationException e) {
			throw new ApplicationException(e.getErrorCodeEnum());
		} catch (Exception e) {
			alert.prepareError("Exception occured while calling the UserManagement Business delegate :" + e.getMessage()).log();
		}
		if (isUpdateSuccess) {
			result.addStringParam("success", "User Status updated.");
			result.addStringParam("Status", inputParams.get("Status"));
			if (DBPUtilitiesConstants.CUSTOMER_STATUS_ACTIVE.equalsIgnoreCase(statusId)) {
				eventCode = "SCA_USERSTATUS_ACTIVE";
			} else if (DBPUtilitiesConstants.CUSTOMER_STATUS_SUSPENDED.equalsIgnoreCase(statusId)) {
				eventCode = "SCA_USERSTATUS_SUSPENDED";
			}
			if (StringUtils.isNotBlank(eventCode)) {
				PushExternalEventBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl
						.getBusinessDelegate(PushExternalEventBusinessDelegate.class);
				JsonObject json = new JsonObject();
				json.addProperty("userId", userId);

				businessDelegate.pushExternalEvent(eventCode, json.toString(), dcRequest.getHeaderMap());
			}
		} else {
			throw new ApplicationException(ErrorCodeEnum.ERR_10406);
		}
		return result;
	}

	@Override
	public Result processOpenedNewAccounts(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws ApplicationException {
		Result result = new Result();
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		String accounts = inputParams.get("accounts") != null ? inputParams.get("accounts")
				: request.getParameter("accounts");
		String customerId = inputParams.get("customerId") != null ? inputParams.get("customerId")
				: request.getParameter("customerId");
		try {
			InfinityUserManagementBusinessDelegate usermanagementBusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(InfinityUserManagementBusinessDelegate.class);
			DBXResult dbxresult = usermanagementBusinessDelegate.processNewAccounts(accounts, customerId,
					request.getHeaderMap());
			if (dbxresult != null && dbxresult.getResponse() != null) {
				JsonObject jsonObject = (JsonObject) dbxresult.getResponse();
				result = ConvertJsonToResult.convert(jsonObject);
				request.addRequestParam_("newAccounts", result.getParamValueByName("newAccounts"));
			}
		} catch (ApplicationException e) {
			alert.prepareError("Exception occured while processing new accounts").log();
		} catch (Exception e) {
			alert.prepareError("Exception occured while processing new accounts").log();
		}
		return result;
	}

	@Override
	public Result assignInfinityUserToPrimaryRetailContract(String methodID, Object[] inputArray,
			DataControllerRequest request, DataControllerResponse response) {
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		Result result = new Result();
		JsonObject inputObject = new JsonObject();
		LegalEntityUtil.addCompanyIDToHeaders(request);
		String primaryApplicantId = inputParams.get(InfinityConstants.primaryApplicantId) != null
				? inputParams.get(InfinityConstants.primaryApplicantId)
				: request.getParameter(InfinityConstants.primaryApplicantId);
		String coApplicantId = inputParams.get(InfinityConstants.coApplicantId) != null
				? inputParams.get(InfinityConstants.coApplicantId)
				: request.getParameter(InfinityConstants.coApplicantId);

		String accountsString = inputParams.get(InfinityConstants.accounts) != null
				? inputParams.get(InfinityConstants.accounts)
				: request.getParameter(InfinityConstants.accounts);

		JsonElement element = new JsonArray();
		try {
			element = new JsonParser().parse(accountsString);
		} catch (Exception e) {

		}
		JsonArray accounts = new JsonArray();
		if (element.isJsonArray()) {
			accounts = element.getAsJsonArray();
		}
		Boolean isSuperAdmin = false;
		String legalEntityId = "";
		Map<String, String> loggedInUserInfo = HelperMethods.getCustomerFromAPIDBPIdentityService(request);
		if (HelperMethods.isAuthenticationCheckRequiredForService(loggedInUserInfo)) {
			Set<String> userPermissions = SessionScope.getAllPermissionsFromIdentityScope(request);
			if (!userPermissions.contains("USER_MANAGEMENT")) {

				ErrorCodeEnum.ERR_10051.setErrorCode(result);
				return result;
			}

			inputObject.addProperty(InfinityConstants.isSuperAdmin, "false");
		} else {
			inputObject.addProperty(InfinityConstants.isSuperAdmin, "true");
			isSuperAdmin = true;
		}
		
		if(isSuperAdmin)
        {
			legalEntityId = inputParams.containsKey("legalEntityId") ? (String) inputParams.get("legalEntityId")
					: request.getParameter("legalEntityId");
			if(StringUtils.isBlank(legalEntityId))
			{
				alert.prepareError("LegalEntity is mandatory!!").log();
				return ErrorCodeEnum.ERR_29040.setErrorCode(new Result());
			}				    
        }
		else
			legalEntityId = LegalEntityUtil.getLegalEntityIdFromSessionOrCache(request);

	        if(StringUtils.isBlank(legalEntityId)) {
	        	return ErrorCodeEnum.ERR_10001.setErrorCode(new Result());
	        }
	        
	        List<String> allLegalEntities = LegalEntityUtil.getAllCompanyLegalUnits(request);
			if (!allLegalEntities.contains(legalEntityId)) {
				alert.prepareError("Logged in user do not have access to this legalEntity ").log();
				return ErrorCodeEnum.ERR_12403.setErrorCode(new Result());
			}
	    

		String loggedInUserId = HelperMethods.getCustomerIdFromSession(request);

		inputObject.addProperty(InfinityConstants.id, primaryApplicantId);
		inputObject.addProperty(InfinityConstants.loggedInUserId, loggedInUserId);
		inputObject.addProperty(InfinityConstants.legalEntityId, legalEntityId);
		InfinityUserManagementBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(InfinityUserManagementBusinessDelegate.class);
		DBXResult dbxResult = businessDelegate.getInfinityUserPrimaryRetailContract(inputObject,
				request.getHeaderMap());
		JsonObject jsonObject = (JsonObject) dbxResult.getResponse();
		removeUnAssignedAccounts(jsonObject, accounts);
		if (dbxResult.getResponse() != null) {
			new InfinityUserManagementBackendDelegateImpl().createCustomerAction(coApplicantId, jsonObject,
					request.getHeaderMap(), null, null, true, legalEntityId,false);

			CustomerDTO customerDTO = new CustomerDTO();
			customerDTO = (CustomerDTO) customerDTO.loadDTO(coApplicantId);
			if (customerDTO != null) {
				HashMap<String, Object> input = new HashMap<String, Object>();
				input.put(InfinityConstants.id, coApplicantId);
				input.put(InfinityConstants.Password, "123");
				input.put(InfinityConstants.Status_id, HelperMethods.getCustomerStatus().get("ACTIVE"));
				input.put(InfinityConstants.CustomerType_id, HelperMethods.getCustomerTypes().get("Retail"));
				customerDTO.setIsChanged(true);
				customerDTO.persist(input, request.getHeaderMap());
				Map<String, String> map = HelperMethods.getInputParamMap(inputArray);
				map.put(InfinityConstants.userName, customerDTO.getUserName());
				map.put(InfinityConstants.userId, coApplicantId);
				map.put(InfinityConstants.contractStatus, DBPUtilitiesConstants.CONTRACT_STATUS_ACTIVE);
				map.put(InfinityConstants.isOnBoradingFlow, "true");
				/*
				 * HelperMethods.getInputParamMap(inputArray).put(InfinityConstants.
				 * EMAIL_TEMPLATE, DBPUtilitiesConstants.ONBOARDING_USERNAME_TEMPLATE);
				 */
				map.put(InfinityConstants.SMS_TEMPLATE, DBPUtilitiesConstants.ONBOARDING_ACTIVATIONCODE_TEMPLATE);
				Result deliveryResult;
				try {
					deliveryResult = generateInfinityUserActivationCodeAndUsername(methodID, inputArray, request,
							response);
					result.addAllParams(deliveryResult.getAllParams());
				} catch (ApplicationException e) {
					alert.prepareError("Exception", e).log();
				}
				result.addParam("status", "success");
			}
		} else {
			result.addParam("status", "failure");
		}

		return result;
	}

	@Override
	public Result getUserApprovalPermissions(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws ApplicationException {
		Result result = new Result();
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		String userName = StringUtils.isBlank(inputParams.get("userName")) ? inputParams.get("userName")
				: dcRequest.getParameter("userName");
		String userId = StringUtils.isBlank(inputParams.get("userId")) ? inputParams.get("userId")
				: dcRequest.getParameter("userId");
		String legalEntityId = LegalEntityUtil.getLegalEntityIdFromSessionOrCache(dcRequest);
		if (StringUtils.isBlank(userName) && StringUtils.isBlank(userId) && StringUtils.isBlank(legalEntityId)) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10799);
		}
		String coreCustomerId = StringUtils.isBlank(inputParams.get("coreCustomerId"))
				? inputParams.get("coreCustomerId")
				: dcRequest.getParameter("coreCustomerId");
		InfinityUserManagementBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(InfinityUserManagementBusinessDelegate.class);
		if(!checkIfUserHasPermissionForUserApprovalPermissions(userName, dcRequest)) {
			ErrorCodeEnum.ERR_12007.setErrorCode(result);
			return result;
		}
		try {
			DBXResult dbxresult = businessDelegate.getUserApprovalPermissions(userId, userName, legalEntityId, coreCustomerId,
					dcRequest.getHeaderMap());
			if (dbxresult != null && dbxresult.getResponse() != null) {
				JsonObject response = new JsonObject();
				JsonArray jsonObject = (JsonArray) dbxresult.getResponse();
				response.add("accounts", jsonObject);
				result = ConvertJsonToResult.convert(response);
			}
		} catch (ApplicationException e) {
			alert.prepareError(
					"InfinityUserManagementResourceImpl : Exception occured while fetching the logged in user approval permissions"
							+ e.getMessage()).log();
			throw new ApplicationException(e.getErrorCodeEnum());
		} catch (Exception e) {
			alert.prepareError(
					"InfinityUserManagementResourceImpl : Exception occured while fetching the logged in user approval permissions"
							+ e.getMessage()).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_10800);
		}
		return result;
	}

	private void removeUnAssignedAccounts(JsonObject jsonObject, JsonArray accounts) {
		JsonArray companyList = jsonObject.has(InfinityConstants.companyList)
				&& jsonObject.get(InfinityConstants.companyList).isJsonArray()
						? jsonObject.get(InfinityConstants.companyList).getAsJsonArray()
						: new JsonArray();
		Set<String> set = new HashSet<String>();
		for (JsonElement accountElement : accounts) {
			JsonObject account = accountElement.getAsJsonObject();
			if (account.has(InfinityConstants.accountId) && !account.get(InfinityConstants.accountId).isJsonNull()) {
				set.add(account.get(InfinityConstants.accountId).getAsString());
			}
		}
		if (companyList.size() > 0 && set.size() > 0) {
			for (JsonElement companyElement : companyList) {
				JsonObject company = companyElement.getAsJsonObject();
				if (company.has(InfinityConstants.accounts) && company.get(InfinityConstants.accounts).isJsonArray()) {
					accounts = company.get(InfinityConstants.accounts).getAsJsonArray();
					for (int i = 0; i < accounts.size(); i++) {
						if (accounts.get(i).isJsonObject()) {
							JsonObject account = accounts.get(i).getAsJsonObject();
							if (account.has(InfinityConstants.accountId)
									&& !account.get(InfinityConstants.accountId).isJsonNull()) {
								if (!set.contains(account.get(InfinityConstants.accountId).getAsString())) {
									accounts.remove(i);
									i--;
								}
							}
						}
					}
				}
			}
		}
	}

	@Override
	public Result enrollRetailUserOperation(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws ApplicationException {
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
				com.temenos.dbx.eum.product.contract.businessdelegate.api.ContractBusinessDelegate contractBusinessDelegate = DBPAPIAbstractFactoryImpl
						.getBusinessDelegate(
								com.temenos.dbx.eum.product.contract.businessdelegate.api.ContractBusinessDelegate.class);
				coreCustomerJson.addProperty("legalEntityId", legalEntityId);
				Map<String, Object> contractPayload = createContractPayload(coreCustomerJson, dcRequest,legalEntityId);
				contractPayload.put("legalEntityId", legalEntityId);
				inputArray[1] = contractPayload;
				dcRequest.addRequestParam_("isDefaultActionsEnabled", "true");
				result = resource.createContract(methodID, inputArray, dcRequest, dcResponse);
				contractBusinessDelegate.updateContractStatus(result.getParamValueByName("contractId"),
						DBPUtilitiesConstants.CONTRACT_STATUS_ACTIVE, legalEntityId, dcRequest.getHeaderMap());
				
				infinityUserId = createAUserAndAssignTOGivenContract(contractPayload,
						JSONUtil.getString(coreCustomerJson, "id"), dcRequest,
						JSONUtil.getString(coreCustomerJson, "partyId"));
			}

			Map<String, String> activationMap = new HashMap<>();
			activationMap.put(InfinityConstants.userId, infinityUserId);
			activationMap.put("Phone", JSONUtil.getString(coreCustomerJson, "phone"));
			activationMap.put("Email", JSONUtil.getString(coreCustomerJson, "email"));
			inputArray[1] = activationMap;
			/*Callable<Result> callable = new Callable<Result>() {
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
			}*/
			// For SCA User Enrollment modified thread call to Synchronized call..
			try {
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
				diagnostic.prepareDebug("Exception occured while sending the activation code and username").log();
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

	private String createAUserAndAssignTOGivenContract(Map<String, Object> contractPayload, String coreCustomerId,
			DataControllerRequest request, String partyId) throws ApplicationException {
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
		String userId = createUser(authorizedSignatoryList.get(0), createdServiceType, request);

		Callable<Result> callable = new Callable<Result>() {
			public Result call() {
				try {
					createCustomerPreference(userId, request.getHeaderMap());
					createBackendIdentifierEntry(coreCustomerId, userId, contractId, request, companyId, partyId);
					createUserRoles(userId, contractId, userToCoreCustomerRoles, request);
					assignUserToContractCustomers(userId, contractId, createdValidContractCoreCustomers, request);
					createUserAccounts(userId, contractId, createdCoreCustomerAccounts, companyId, request);
					createUserActionLimits(userId, contractId, companyId, createdValidContractCoreCustomers,
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

	public void createCustomerPreference(String customerId, Map<String, Object> headersMap) {
		CustomerPreferenceDTO customerPreferenceDTO = new CustomerPreferenceDTO();
		customerPreferenceDTO.setId(HelperMethods.getNewId());
		customerPreferenceDTO.setCustomer_id(customerId);
		customerPreferenceDTO.setIsNew(true);

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
Map<String, String> legalEntityWiseSerDefs = processLegalEntitydefaultServiceDefsData(dcRequest);
		
		String servDefId = legalEntityWiseSerDefs.get(legalEntityId);
		serviceDefinitionDTO.setId(servDefId);

		String contractName = JSONUtil.getString(coreCustomer, "name");
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
				alert.prepareError("Exception", e).log();
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
		authorizedSignatory.addProperty("FirstName", JSONUtil.getString(coreCustomer, "firstName"));
		authorizedSignatory.addProperty("LastName", JSONUtil.getString(coreCustomer, "lastName"));
		authorizedSignatory.addProperty("DateOfBirth", JSONUtil.getString(coreCustomer, "dateOfBirth"));
		authorizedSignatory.addProperty("Ssn", JSONUtil.getString(coreCustomer, "taxId"));
		authorizedSignatoryJsonArray.add(authorizedSignatory);
		contractPayloadMap.put("authorizedSignatory", authorizedSignatoryJsonArray.toString());
		authorizedSignatoryRole.addProperty("coreCustomerId", JSONUtil.getString(coreCustomer, "id"));
		authorizedSignatoryRole.addProperty("authorizedSignatoryRoleId",
				serviceDefinitionBusinessDelegate.fetchDefaultRoleId(serviceDefinitionDTO, dcRequest.getHeaderMap()));

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
		return features;
	}

	@Override
	public Result createBusinessContract(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws ApplicationException {
		Result result = new Result();
		try {
			Map<String, String> map = HelperMethods.getInputParamMap(inputArray);

			Iterator<String> iterator = request.getParameterNames();

			while (iterator.hasNext()) {
				String key = iterator.next();
				if ((!map.containsKey(key) || StringUtils.isBlank(map.get(key)))
						&& StringUtils.isNotBlank(request.getParameter(key))) {
					map.put(key, request.getParameter(key));
				}
			}

			String customerId = map.get(InfinityConstants.customerId);

			if (StringUtils.isBlank(customerId)) {
				throw new ApplicationException(ErrorCodeEnum.ERR_10349, "CustomerId is Empty");
			}

			String coreCustomerId = map.get(InfinityConstants.coreCustomerId);

			if (StringUtils.isBlank(coreCustomerId)) {
				throw new ApplicationException(ErrorCodeEnum.ERR_10349, "coreCustomerId is Empty");
			}

			String name = "";
			JsonObject membershipJson = new JsonObject();
			String serviceId = ServiceId.T24ISUSER_INTEGRATION_SERVICE;
			String operationName = OperationName.CORE_CUSTOMER_SEARCH;

			BackendIdentifierDTO backendIdentifierDTO = new BackendIdentifierDTO();

			backendIdentifierDTO
					.setBackendType(IntegrationTemplateURLFinder.getBackendURL(InfinityConstants.BackendType));

			backendIdentifierDTO.setCustomer_id(customerId);

			/* This is for fall back */
			String companyId = EnvironmentConfigurationsHandler
					.getServerProperty(DBPUtilitiesConstants.BRANCH_ID_REFERENCE);
			String legalEntityId = "";
			DBXResult dbxResult = DBPAPIAbstractFactoryImpl.getBackendDelegate(BackendIdentifiersBackendDelegate.class)
					.get(backendIdentifierDTO, request.getHeaderMap());
			if (dbxResult.getResponse() != null) {
				BackendIdentifierDTO identifierDTO = (BackendIdentifierDTO) dbxResult.getResponse();
				companyId = StringUtils.isNotBlank(identifierDTO.getCompanyId()) ? identifierDTO.getCompanyId()
						: companyId;
				legalEntityId = StringUtils.isNotBlank(identifierDTO.getCompanyLegalUnit()) ? identifierDTO.getCompanyLegalUnit()
						: legalEntityId;
			}

			request.getHeaderMap().put("companyId", companyId);
			HelperMethods.addJWTAuthHeader(request.getHeaderMap(), AuthConstants.PRE_LOGIN_FLOW);
			/*
			 * Map<String, Object> inputParams = new HashMap<String, Object>();
			 * inputParams.put("customerId", coreCustomerId); membershipJson =
			 * ServiceCallHelper.invokeServiceAndGetJson(serviceId, null, operationName,
			 * inputParams, request.getHeaderMap());
			 */

			Map<String, Object> headerMap = request.getHeaderMap();
			headerMap.put("companyId", companyId);
			Map<String, String> input = new HashMap<String, String>();
			input.put("flowType", AuthConstants.PRE_LOGIN_FLOW);
			headerMap.put("Authorization", TokenUtils.getT24AuthToken(input, headerMap));

			Map<String, Object> inputParams = new HashMap<String, Object>();
			inputParams.put("customerId", coreCustomerId);
			
			membershipJson = ServiceCallHelper.invokeServiceAndGetJson(serviceId, null, operationName, inputParams,
					headerMap);

			JsonArray jsonArray = new JsonArray();

			if (null != membershipJson && JSONUtil.hasKey(membershipJson, DBPDatasetConstants.DATASET_CUSTOMERS)
					&& membershipJson.get(DBPDatasetConstants.DATASET_CUSTOMERS).isJsonArray()) {
				jsonArray = membershipJson.get(DBPDatasetConstants.DATASET_CUSTOMERS).getAsJsonArray();
			}
			JsonObject customerJson = new JsonObject();
			if (jsonArray.size() > 0) {
				customerJson = jsonArray.get(0).getAsJsonObject();
				name = JSONUtil.getString(customerJson, "firstName") + " "
						+ JSONUtil.getString(customerJson, "lastName") + " " + coreCustomerId;
			} else
				throw new ApplicationException(ErrorCodeEnum.ERR_10349, "Invalid CoreCustomerId");

			if (StringUtils.isBlank(name)) {
				throw new ApplicationException(ErrorCodeEnum.ERR_10349, "Invalid CoreCustomer Name");
			}
			map.put(InfinityConstants.contractName, name);
			map.put(InfinityConstants.coreCustomerName, name);

			if (HelperMethods.isBlank(customerId, coreCustomerId)) {
				throw new ApplicationException(ErrorCodeEnum.ERR_10349, "Invalid coreCustomerId or customerId");
			}

			map.put(InfinityConstants.coreCustomerId, coreCustomerId);
			map.put("companyId", companyId);
			map.put(InfinityConstants.legalEntityId, legalEntityId);
			map.put(DEFAULT_SERVICE_ID, BundleConfigurationHandler.DEFAULT_BUSINESS_SERVICE_ID);
			return createContract(methodID, inputArray, map, request, response);

		} catch (ApplicationException e) {
			result = new Result();
			e.setError(result);
			alert.prepareError("Exception occured while creating a contract " + e.getStackTrace()).log();
			return result;
		} catch (Exception e) {
			result = new Result();
			alert.prepareError("Exception occured while creating a contract " + e.getStackTrace()).log();
			
			return result;
		}
	}

	@Override
	public Result getCoreCustomerDetails(String methodId, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws ApplicationException {
		Result result = new Result();
	    Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
	    String coreCustomerId = StringUtils.isNotBlank(inputParams.get(InfinityConstants.coreCustomerId))
	            ? inputParams.get(InfinityConstants.coreCustomerId)
	            : dcRequest.getParameter(InfinityConstants.coreCustomerId);
	    String legalentityId = StringUtils.isNotBlank(inputParams.get(InfinityConstants.legalEntityId))
	            ? inputParams.get(InfinityConstants.legalEntityId)
	            : dcRequest.getParameter(InfinityConstants.legalEntityId);
	    if (StringUtils.isBlank(coreCustomerId)) {
	        throw new ApplicationException(ErrorCodeEnum.ERR_10772);
	    }
	    ContractCustomersDTO dto = new ContractCustomersDTO();
	    dto.setCoreCustomerId(coreCustomerId);
	    dto.setCompanyLegalUnit(legalentityId);
	    try {
	    	CoreCustomerBusinessDelegate coreCustomerBusinessDelegate =
	                DBPAPIAbstractFactoryImpl.getBusinessDelegate(CoreCustomerBusinessDelegate.class);
	        MembershipDTO membershipDetails = coreCustomerBusinessDelegate.getMembershipDetails(coreCustomerId,
	        		legalentityId, dcRequest.getHeaderMap());
	        if (membershipDetails != null && membershipDetails.getId() != null) {
				String membershipDetailsStr = JSONUtils.stringify(membershipDetails);
				JSONObject membershipJsonObj = new JSONObject(membershipDetailsStr);
				JSONArray responseArray = new JSONArray();
				responseArray.put(membershipJsonObj);
				JSONObject resultObj = new JSONObject();
				resultObj.put("CoreCustomer", responseArray);
				result = JSONToResult.convert(resultObj.toString());
			} else {
				ErrorCodeEnum.ERR_10367.setErrorCode(result);
			}
	    } catch (ApplicationException e) {
	        alert.prepareError("Exception occured while fetching the core customerid details", e).log();
	        throw new ApplicationException(e.getErrorCodeEnum());
	    } catch (Exception e) {
	        alert.prepareError("Exception occured while fetching the customerid details" , e).log();
	        throw new ApplicationException(ErrorCodeEnum.ERR_10773);
	    }
	    
	    return result;
	}


	@Override
	public Result getCoreCustomerAccounts(String methodId, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws ApplicationException {
	    Map<String, String> inputParams = new HashMap<>();
	    Result result = new Result();
	    String userId = StringUtils.isNotBlank(inputParams.get("userId")) ? inputParams.get("userId")
	            : dcRequest.getParameter("userId");
	    String coreCustomerId = StringUtils.isNotBlank(inputParams.get(InfinityConstants.coreCustomerId))
	            ? inputParams.get(InfinityConstants.coreCustomerId)
	            : dcRequest.getParameter(InfinityConstants.coreCustomerId);
	    
	    String legalEntityId = StringUtils.isNotBlank(inputParams.get(InfinityConstants.legalEntityId))
	            ? inputParams.get(InfinityConstants.legalEntityId)
	            : dcRequest.getParameter(InfinityConstants.legalEntityId);
	    
	    if (StringUtils.isBlank(userId) || StringUtils.isBlank(coreCustomerId)
	    		|| StringUtils.isBlank(legalEntityId)) {
	        throw new ApplicationException(ErrorCodeEnum.ERR_10792);
	    }
	    CustomerAccountsDTO customerAccountsDTO = new CustomerAccountsDTO();
	    customerAccountsDTO.setCustomerId(userId);
	    customerAccountsDTO.setCoreCustomerId(coreCustomerId);
	    customerAccountsDTO.setCompanyLegalUnit(legalEntityId);
	    DBXResult response = new DBXResult();
	    try {
	        InfinityUserManagementBusinessDelegate infinityUserManagementBusinessDelegate =
	                DBPAPIAbstractFactoryImpl.getBusinessDelegate(InfinityUserManagementBusinessDelegate.class);
	        response =
	                infinityUserManagementBusinessDelegate.getCoreCustomerAccountsForAdmin(
	                        customerAccountsDTO,
	                        dcRequest.getHeaderMap());
	        if (response != null && response.getResponse() != null) {
	            JsonObject jsonObject = (JsonObject) response.getResponse();
	            result = ConvertJsonToResult.convert(jsonObject);
	        }
	    } catch (ApplicationException e) {
	        alert.prepareError(
	                "InfinityUserManagementResourceImpl : Exception occured while fetching infinity user contract details ").log();
	        throw new ApplicationException(e.getErrorCodeEnum());
	    } catch (Exception e) {
	        alert.prepareError(
	                "InfinityUserManagementResourceImpl : Exception occured while fetching infinity user contract details "
	                        + e.getMessage()).log();
	        throw new ApplicationException(ErrorCodeEnum.ERR_10793);
	    }
	    return result;
	}
	public Map<String, String> processLegalEntitydefaultServiceDefsData(DataControllerRequest dcRequest) throws ApplicationException {
        Map<String, String> legalEntityDefaultServDefs = new HashMap<>();
        Map<String, String> bundleConfigurations = BundleConfigurationHandler
				.fetchBundleConfigurations(BundleConfigurationHandler.BUDLENAME_C360, dcRequest);
		String defaultRetailServiceId = bundleConfigurations.get(BundleConfigurationHandler.DEFAULT_RETAIL_SERVICE_ID);
        try {
            legalEntityDefaultServDefs = new ObjectMapper().readValue(defaultRetailServiceId, HashMap.class);
        } catch (JsonProcessingException e) {
            alert.prepareError("Error in parsing default service definitions", e).log();
            //throw new ApplicationException(ErrorCodeEnum.ERR_29053, "Improper default service defition data");
        }
        return legalEntityDefaultServDefs;
    }
	
	
	@Override
    public Result getInfinityUserServiceDefsRoles(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) {
		
        Result result = new Result();
		LegalEntityUtil.addCompanyIDToHeaders(request);
        Map<String, String> map = HelperMethods.getInputParamMap(inputArray);

        Iterator<String> iterator = request.getParameterNames();

        JsonObject inputObject = new JsonObject();

        while (iterator.hasNext()) {
            String key = iterator.next();
            if ((!map.containsKey(key) || StringUtils.isBlank(map.get(key)))
                    && StringUtils.isNotBlank(request.getParameter(key))) {
                map.put(key, request.getParameter(key));
            }
        }

        String customerId = map.get(InfinityConstants.id);
        String coreCustomerId = map.get(InfinityConstants.coreCustomerId);
        String contractId = map.get(InfinityConstants.contractId);
        String legalEntityId ="";
        Map<String, String> loggedInUserInfo = HelperMethods.getCustomerFromAPIDBPIdentityService(request);
		
		try {
			if (HelperMethods.isPSD2Agent(request)) {
				inputObject.addProperty(InfinityConstants.isSuperAdmin, "true");
			} else if (HelperMethods.isAuthenticationCheckRequiredForService(loggedInUserInfo)) {
				Set<String> userPermissions = SessionScope.getAllPermissionsFromIdentityScope(request);
				if (!userPermissions.contains("USER_MANAGEMENT")) {
					ErrorCodeEnum.ERR_10051.setErrorCode(result);
					return result;
				}

				inputObject.addProperty(InfinityConstants.isSuperAdmin, "false");
			} else {
				inputObject.addProperty(InfinityConstants.isSuperAdmin, "true");
			}

			Boolean isSuperAdmin = LegalEntityUtil.checkForSuperAdmin(request);
			Boolean isSingleEntity = false;
			Result appInfo = null;
			try {
				appInfo = ServiceCallHelper.invokeServiceAndGetResult(null, null, URLConstants.APPLICATION_GET,
				        StringUtils.EMPTY);
			} catch (HttpCallException e) {
			}
			isSingleEntity = HelperMethods.getFieldValue(appInfo, "isSingleEntity")=="false"? false:true;

			if (isSuperAdmin)
				legalEntityId = LegalEntityUtil.getLegalEntityFromPayload(map, request);
			else if(request.containsKeyInRequest(InfinityConstants.legalEntityId) && isSingleEntity == false) {
	            legalEntityId = LegalEntityUtil.getLegalEntityFromPayload(map, request);
			}
			else
				legalEntityId = LegalEntityUtil.getLegalEntityIdFromSessionOrCache(request);

			if (StringUtils.isBlank(legalEntityId)) {
				return ErrorCodeEnum.ERR_10001.setErrorCode(new Result());
			}
		        
			 List<String> allLegalEntities = LegalEntityUtil.getAllCompanyLegalUnits(request);
				if (!allLegalEntities.contains(legalEntityId)) {
					alert.prepareError("Logged in user do not have access to this legalEntity ").log();
					throw new ApplicationException(ErrorCodeEnum.ERR_12403);
				}
		}

		catch (Exception e) {
			// TODO: handle exception

			alert.prepareError(e.getMessage()).log();
		}
	
        String loggedInUserId = HelperMethods.getCustomerIdFromSession(request);

        if (StringUtils.isBlank(customerId)) {
            String userName = map.get(InfinityConstants.userName);
            if (StringUtils.isNotBlank(userName)) {
                CustomerDTO customerDTO = new CustomerDTO();
                customerDTO.setUserName(userName);
                customerDTO = (CustomerDTO) customerDTO.loadDTO();
                if (customerDTO != null) {
                    customerId = customerDTO.getId();
                }
            }
        }

        if (StringUtils.isBlank(customerId)) {
            customerId = loggedInUserId;
        }

        inputObject.addProperty(InfinityConstants.id, customerId);
        inputObject.addProperty(InfinityConstants.loggedInUserId, loggedInUserId);
        
        if (StringUtils.isNotBlank(contractId)) {
			inputObject.addProperty(InfinityConstants.contractId, contractId);
		}
        
        if (StringUtils.isNotBlank(coreCustomerId)) {
			inputObject.addProperty(InfinityConstants.coreCustomerId, coreCustomerId);
		}
        
        if (StringUtils.isBlank(customerId)) {
            ErrorCodeEnum.ERR_10050.setErrorCode(result);
            return result;
        }
        inputObject.addProperty(InfinityConstants.legalEntityId, legalEntityId);
        InfinityUserManagementBusinessDelegate infinityUserManagementBusinessDelegate =
                DBPAPIAbstractFactoryImpl.getBusinessDelegate(InfinityUserManagementBusinessDelegate.class);
        DBXResult dbxResult =
                infinityUserManagementBusinessDelegate.getInfinityUserServiceDefsRoles(inputObject, request.getHeaderMap());

        if (dbxResult.getResponse() != null) {
            JsonObject jsonObject = (JsonObject) dbxResult.getResponse();
//            addNewAccounts(jsonObject, inputObject, request);
            result = JSONToResult.convert(jsonObject.toString());
        }
        return result;

	
	}
	
	@Override
	public Result DBXCustomerCommunicationDetails(String methodId, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws ApplicationException {
		
		Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        String coreCustomerId = "";
        String legalEntityId = "";
        
        
        BackendIdentifierDTO backendIdentifierDTO = new BackendIdentifierDTO();

        BackendIdentifiersBackendDelegate backendDelegate =
                DBPAPIAbstractFactoryImpl.getBackendDelegate(BackendIdentifiersBackendDelegate.class);
        backendIdentifierDTO.setCustomer_id(inputParams.get(InfinityConstants.Customer_id));

        final String IS_Integrated = Boolean.toString(IntegrationTemplateURLFinder.isIntegrated);
        String integrationName = IntegrationMappings.getInstance().getIntegrationName();
		if (StringUtils.isNotBlank(IS_Integrated) && IS_Integrated.equalsIgnoreCase("true")) {
			if (StringUtils.isNotBlank(integrationName) && "T24".equalsIgnoreCase(integrationName)) {
				backendIdentifierDTO.setBackendType(DTOConstants.T24);
			} else if (StringUtils.isNotBlank(integrationName) && "party".equalsIgnoreCase(integrationName)) {
				backendIdentifierDTO.setBackendType(DTOConstants.PARTY);
			}
		} else {
			backendIdentifierDTO.setBackendType(DTOConstants.CORE);
		}

        DBXResult dbxResult = backendDelegate.get(backendIdentifierDTO, dcRequest.getHeaderMap());
        if (dbxResult.getResponse() != null) {
            backendIdentifierDTO = (BackendIdentifierDTO) dbxResult.getResponse();
            coreCustomerId = backendIdentifierDTO.getBackendId();
            legalEntityId = backendIdentifierDTO.getCompanyLegalUnit();
        }
        
        ContractCustomersDTO dto = new ContractCustomersDTO();
        dto.setCoreCustomerId(coreCustomerId);
        try {
        	MembershipDTO membershipDetails;
			if (StringUtils.isBlank(coreCustomerId)) {
				membershipDetails = getVirtualUserMembershipDetails(inputParams.get(InfinityConstants.Customer_id),dcRequest.getHeaderMap());
			} else {
				CoreCustomerBusinessDelegate coreCustomerBusinessDelegate = DBPAPIAbstractFactoryImpl
						.getBusinessDelegate(CoreCustomerBusinessDelegate.class);
				membershipDetails = coreCustomerBusinessDelegate.getMembershipDetails(coreCustomerId, legalEntityId,
						dcRequest.getHeaderMap());
			}
            if (membershipDetails != null && membershipDetails.getId() != null) {
				String membershipDetailsStr = JSONUtils.stringify(membershipDetails);
				JSONObject membershipJsonObj = new JSONObject(membershipDetailsStr);
				JSONArray responseArray = new JSONArray();
				responseArray.put(membershipJsonObj);
				JSONObject resultObj = new JSONObject();
				resultObj.put("CoreCustomer", responseArray);
				result = JSONToResult.convert(resultObj.toString());
			} else {
				ErrorCodeEnum.ERR_10367.setErrorCode(result);
			}
        } catch (ApplicationException e) {
            alert.prepareError("Exception occured while fetching the core customerid details", e).log();
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            alert.prepareError("Exception occured while fetching the customerid details" , e).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10773);
        }
        
        return result;
		
	}

	private MembershipDTO getVirtualUserMembershipDetails(String customerId, Map<String, Object> headerMap) {
		Map<String, Object> inputParams = new HashMap<>();
		MembershipDTO membershipDTO = new MembershipDTO();
		//LegalEntityUtil.getLegalEntityIdFromSessionOrCache(null);
		membershipDTO.setId(customerId);
		String filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + customerId + DBPUtilitiesConstants.AND
				+ "isPrimary" + DBPUtilitiesConstants.EQUAL + "1";
		inputParams.put(DBPUtilitiesConstants.FILTER, filter);
		JsonObject customerCommunication = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
				URLConstants.CUSTOMER_COMMUNICATION_GET);
		String email = null, phone = null;
		JsonArray communicationArray;
		if (customerCommunication.has(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION)
				&& customerCommunication.get(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION).isJsonArray()) {
			communicationArray = customerCommunication.get(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION)
					.getAsJsonArray();
			for (JsonElement jsonelement : communicationArray) {
				JsonObject object = jsonelement.getAsJsonObject();
				if (StringUtils.isBlank(email)) {
					if ("COMM_TYPE_EMAIL".equalsIgnoreCase(JSONUtil.getString(object, "Type_id")))
						email = JSONUtil.getString(object, "Value");
				}
				if (StringUtils.isBlank(phone)) {
					if ("COMM_TYPE_PHONE".equalsIgnoreCase(JSONUtil.getString(object, "Type_id")))
						phone = JSONUtil.getString(object, "Value");
				}
			}
		} else if (customerCommunication.has(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION)
				&& customerCommunication.get(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION).isJsonObject()) {
			JsonObject commobject = customerCommunication.get(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION)
					.getAsJsonObject();
			if (StringUtils.isBlank(email)) {
				email = JSONUtil.getString(commobject, "Email");
			}
			if (StringUtils.isBlank(phone)) {
				phone = JSONUtil.getString(commobject, "Phone");
			}
		}
		membershipDTO.setPhone(phone);
		membershipDTO.setEmail(email);
		return membershipDTO;
	}

	private boolean checkIfUserHasPermissionForUserApprovalPermissions(String userName, DataControllerRequest dcRequest) {
		Map<String, Object> map = CustomerSession.getCustomerMap(dcRequest);
		String loggedInUserName = map.get("UserName").toString();
		if (!userName.equalsIgnoreCase(loggedInUserName))
			return false;
		return true;
	}
}