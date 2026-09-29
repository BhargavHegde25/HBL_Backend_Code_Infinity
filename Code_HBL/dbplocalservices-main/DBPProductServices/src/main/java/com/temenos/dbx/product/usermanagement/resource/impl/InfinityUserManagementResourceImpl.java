package com.temenos.dbx.product.usermanagement.resource.impl;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.infinity.dbx.dbp.jwt.auth.AuthConstants;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.sessionmanager.SessionScope;
import com.kony.dbputilities.util.BundleConfigurationHandler;
import com.kony.dbputilities.util.ConvertJsonToResult;
import com.kony.dbputilities.util.DBPDatasetConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.IntegrationTemplateURLFinder;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.LegalEntityUtil;
import com.kony.dbputilities.util.OperationName;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.ServiceId;
import com.kony.dbputilities.util.StatusEnum;
import com.kony.dbputilities.util.URLConstants;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.konylabs.middleware.registry.AppRegistryException;
import com.temenos.dbx.mfa.businessdelegate.api.MFAServiceBusinessDelegate;
import com.temenos.dbx.mfa.dto.MFAServiceDTO;
import com.temenos.dbx.mfa.utils.MFAServiceUtil;
import com.temenos.dbx.product.approvalmatrixservices.businessdelegate.api.ApprovalMatrixBusinessDelegate;
import com.temenos.dbx.product.businessdelegate.api.ApplicationBusinessDelegate;
import com.temenos.dbx.product.businessdelegate.api.FeatureBusinessDelegate;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.dbx.product.contract.backenddelegate.api.ContractBackendDelegate;
import com.temenos.dbx.product.contract.backenddelegate.api.ContractCoreCustomerBackendDelegate;
import com.temenos.dbx.product.contract.backenddelegate.api.CoreCustomerBackendDelegate;
import com.temenos.dbx.product.contract.backenddelegate.api.ServiceDefinitionBackendDelegate;
import com.temenos.dbx.product.contract.businessdelegate.api.ContractFeatureActionsBusinessDelegate;
import com.temenos.dbx.product.contract.businessdelegate.api.CoreCustomerBusinessDelegate;
import com.temenos.dbx.product.contract.resource.api.ContractResource;
import com.temenos.dbx.product.contract.resource.impl.ContractResourceImpl;
import com.temenos.dbx.product.dto.AllAccountsViewDTO;
import com.temenos.dbx.product.dto.ApplicationDTO;
import com.temenos.dbx.product.dto.BackendIdentifierDTO;
import com.temenos.dbx.product.dto.ContractAccountsDTO;
import com.temenos.dbx.product.dto.ContractCustomersDTO;
import com.temenos.dbx.product.dto.CustomerAccountsDTO;
import com.temenos.dbx.product.dto.CustomerCommunicationDTO;
import com.temenos.dbx.product.dto.CustomerDTO;
import com.temenos.dbx.product.dto.CustomerGroupDTO;
import com.temenos.dbx.product.dto.CustomerPreferenceDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.dto.FeatureActionLimitsDTO;
import com.temenos.dbx.product.dto.MembershipDTO;
import com.temenos.dbx.product.dto.ServiceDefinitionDTO;
import com.temenos.dbx.product.dto.UserCustomerViewDTO;
import com.temenos.dbx.product.signatorygroupservices.resource.api.SignatoryGroupResource;
import com.temenos.dbx.product.usermanagement.backenddelegate.api.BackendIdentifiersBackendDelegate;
import com.temenos.dbx.product.usermanagement.backenddelegate.api.CommunicationBackendDelegate;
import com.temenos.dbx.product.usermanagement.backenddelegate.impl.InfinityUserManagementBackendDelegateImpl;
import com.temenos.dbx.product.usermanagement.businessdelegate.api.CommunicationBusinessDelegate;
import com.temenos.dbx.product.usermanagement.businessdelegate.api.CustomerAccountsBusinessDelegate;
import com.temenos.dbx.product.usermanagement.businessdelegate.api.CustomerActionsBusinessDelegate;
import com.temenos.dbx.product.usermanagement.businessdelegate.api.CustomerGroupBusinessDelegate;
import com.temenos.dbx.product.usermanagement.businessdelegate.api.CustomerPreferenceBusinessDelegate;
import com.temenos.dbx.product.usermanagement.businessdelegate.api.InfinityUserManagementBusinessDelegate;
import com.temenos.dbx.product.usermanagement.businessdelegate.api.PushExternalEventBusinessDelegate;
import com.temenos.dbx.product.usermanagement.businessdelegate.api.UserManagementBusinessDelegate;
import com.temenos.dbx.product.usermanagement.resource.api.InfinityUserManagementResource;
import com.temenos.dbx.product.usermanagement.resource.api.PushExternalEventResource;
import com.temenos.dbx.product.utils.CustomerCreationMode;
import com.temenos.dbx.product.utils.CustomerSessionsUtil;
import com.temenos.dbx.product.utils.DTOConstants;
import com.temenos.dbx.product.utils.DTOUtils;
import com.temenos.dbx.product.utils.InfinityConstants;
import com.temenos.dbx.product.utils.ThreadExecutor;

public class InfinityUserManagementResourceImpl implements InfinityUserManagementResource {

    private static final String DEFAULT_SERVICE_ID = "DEFAULT_SERVICE_ID";

    Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    SimpleDateFormat idFormatter = new SimpleDateFormat("yyMMddHHmmssSSS");

    @Override
    public Result getInfinityUser(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) {

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
        Map<String, String> loggedInUserInfo = HelperMethods.getCustomerFromAPIDBPIdentityService(request);
		if (HelperMethods.isPSD2Agent(request)) {
			inputObject.addProperty(InfinityConstants.isSuperAdmin, "false");
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

        if (StringUtils.isBlank(customerId)) {
            ErrorCodeEnum.ERR_10050.setErrorCode(result);
            return result;
        }
        LegalEntityUtil.addCompanyIDToHeaders(request);
        InfinityUserManagementBusinessDelegate infinityUserManagementBusinessDelegate = DBPAPIAbstractFactoryImpl
                .getBusinessDelegate(InfinityUserManagementBusinessDelegate.class);
        DBXResult dbxResult = infinityUserManagementBusinessDelegate.getInfinityUser(inputObject,
                request.getHeaderMap());

        if (dbxResult.getResponse() != null) {
            JsonObject jsonObject = (JsonObject) dbxResult.getResponse();
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

    private void addNewAccounts(JsonObject infinityUserJsonObject, JsonObject inputObject,
            DataControllerRequest request) {

        Set<String> set = new HashSet<String>();
        JsonArray companyList = infinityUserJsonObject.get(InfinityConstants.companyList).getAsJsonArray();

        for (int i = 0; i < companyList.size(); i++) {
            JsonObject companyObject = companyList.get(i).getAsJsonObject();
            String cif = companyObject.get(InfinityConstants.cif).getAsString();

            boolean b = Boolean.parseBoolean(companyObject.get(InfinityConstants.autoSyncAccounts).getAsString());

            if (!set.contains(cif) && b) {
                set.add(cif);
            }
        }

        if (set.isEmpty()) {
            return;
        }

        CoreCustomerBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl
                .getBusinessDelegate(CoreCustomerBusinessDelegate.class);

        Map<String, List<ContractAccountsDTO>> map = new HashMap<String, List<ContractAccountsDTO>>();
        try {
            map = businessDelegate.getAccountsWithImplicitAccountAccess(set,
                    inputObject.get(InfinityConstants.id).getAsString(), request.getHeaderMap());
        } catch (ApplicationException e1) {
        	alert.prepareError("Exception", e1).log();
        }

        Set<String> accountSet = new HashSet<String>();
        for (int i = 0; i < companyList.size(); i++) {
            JsonObject companyObject = companyList.get(i).getAsJsonObject();

            JsonArray accounts = companyObject.get(InfinityConstants.accounts).getAsJsonArray();
            accountSet = new HashSet<String>();

            for (JsonElement element : accounts) {
                if (!accountSet.contains(element.getAsJsonObject().get(InfinityConstants.accountId).getAsString())) {
                    accountSet.add(element.getAsJsonObject().get(InfinityConstants.accountId).getAsString());
                    element.getAsJsonObject().addProperty(InfinityConstants.isNew, "false");
                }
            }

            String cif = companyObject.get(InfinityConstants.cif).getAsString();

            ContractBackendDelegate contractBackendDelegate = DBPAPIAbstractFactoryImpl
                    .getBackendDelegate(ContractBackendDelegate.class);

            String roleId = companyObject.get(InfinityConstants.roleId).getAsString();
            String serviceDefinition = companyObject.get(InfinityConstants.serviceDefinition).getAsString();

            if (map.containsKey(cif) && map.get(cif).size() > 0) {
                FeatureActionLimitsDTO coreCustomerFeatureActionDTO = null;
                try {
                    coreCustomerFeatureActionDTO = contractBackendDelegate.getRestrictiveFeatureActionLimits(
                            serviceDefinition, null, roleId, cif, "", request.getHeaderMap(), false, "");
                } catch (ApplicationException e) {
                	alert.prepareError("Exception", e).log();
                }

                if (coreCustomerFeatureActionDTO == null) {
                    continue;
                }

                JsonArray accountLevelFeatures = new JsonArray();

                Map<String, Set<String>> featureAction = coreCustomerFeatureActionDTO.getFeatureaction();

                for (Entry<String, Set<String>> featureEntry : featureAction.entrySet()) {

                    String featureId = featureEntry.getKey();

                    JsonObject featureJson = coreCustomerFeatureActionDTO.getFeatureInfo().get(featureId);

                    JsonObject featureObjectAccount = new JsonObject();

                    for (Entry<String, JsonElement> featureParams : featureJson.entrySet()) {
                        featureObjectAccount.add(featureParams.getKey(), featureParams.getValue());
                    }

                    JsonArray accountpermissions = new JsonArray();

                    for (String action : featureEntry.getValue()) {

                        JsonObject actionJson = coreCustomerFeatureActionDTO.getActionsInfo().get(action);

                        boolean isAccountLevel = actionJson.has(InfinityConstants.isAccountLevel)
                                && !actionJson.get(InfinityConstants.isAccountLevel).isJsonNull()
                                && "1".equals(actionJson.get(InfinityConstants.isAccountLevel).getAsString());

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
                            } else if (limitsEntry.getKey().equals(InfinityConstants.WEEKLY_LIMIT)) {
                                limit.addProperty(InfinityConstants.id, InfinityConstants.PRE_APPROVED_WEEKLY_LIMIT);
                                limit.addProperty(InfinityConstants.value, "0.0");
                                limits.add(limit);
                                limit = new JsonObject();
                                limit.addProperty(InfinityConstants.id, InfinityConstants.AUTO_DENIED_WEEKLY_LIMIT);
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
                    if (!accountSet.contains(accountsDTO.getAccountId())) {
                        JsonObject accountObject = new JsonObject();
                        accountObject.addProperty(InfinityConstants.accountId, accountsDTO.getAccountId());
                        accountObject.addProperty(InfinityConstants.accountName, accountsDTO.getAccountName());
                        accountObject.addProperty(InfinityConstants.accountType, accountsDTO.getAccountType());
                        accountObject.addProperty(InfinityConstants.isEnabled, "true");
                        accountObject.addProperty(InfinityConstants.isNew, "true");
                        accountObject.addProperty(InfinityConstants.ownerType, accountsDTO.getOwnerType());
                        accounts.add(accountObject);
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

        contractCustomerDTO.setCustomerId(customerId);
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
        DBXResult dbxResult = new DBXResult();
        try {
            InfinityUserManagementBusinessDelegate infinityUserManagementBusinessDelegate = DBPAPIAbstractFactoryImpl
                    .getBusinessDelegate(InfinityUserManagementBusinessDelegate.class);
            dbxResult = infinityUserManagementBusinessDelegate.getAllEligibleRelationalCustomers(coreCustomerId,
                    dcRequest.getHeaderMap());
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
            DataControllerResponse response) {
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

        Boolean isContractValidationRequired = true;

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

        jsonObject.add(InfinityConstants.userDetails, userDetailsJsonObject);

        String signatoryGroups = map.get(InfinityConstants.signatoryGroups);
        jsonObject.addProperty(InfinityConstants.signatoryGroups, signatoryGroups);

        if (!validateUserDetails(userDetailsJsonObject, result)) {
            return result;
        }

        diagnostic.prepareDebug("Json request " + jsonObject.toString()).log();
        if (validateinput(jsonObject, result, map, request, isContractValidationRequired, null)) {
            InfinityUserManagementBusinessDelegate infinityUserManagementBusinessDelegate = DBPAPIAbstractFactoryImpl
                    .getBusinessDelegate(InfinityUserManagementBusinessDelegate.class);
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
                        activationCodeResult = generateInfinityUserActivationCodeAndUsername(methodId, inputArray,
                                request, response);
                        String activationCode = activationCodeResult.getParamValueByName("activationcodeForSCA");
                        String userName = activationCodeResult.getParamValueByName("usernameForSCA");
                        result.addStringParam(InfinityConstants.userName, userName);
                        result.addStringParam(InfinityConstants.activationCode, activationCode);
                    } catch (ApplicationException e) {
                       
                    	alert.prepareError("Exception", e).log();
                        ErrorCodeEnum.ERR_10795.setErrorCode(result);
                    }
                    updateSignatoryGroupEntry(customerId, jsonObject, result, request);
                }

                diagnostic.prepareDebug("Json response " + ResultToJSON.convert(result).toString()).log();
            }
        }

        return result;
    }

    private void updateSignatoryGroupEntry(String customerId, JsonObject jsonObject, Result result,
            DataControllerRequest request) {

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
            DBPAPIAbstractFactoryImpl.getResource(SignatoryGroupResource.class)
                    .updateSignatoryGroupForInfinityUser(signatory);
            // result.addAllDatasets(result2.getAllDatasets());
            // result.addAllParams(result2.getAllParams());
            // result.addAllRecords(result2.getAllRecords());
        }
    }

    private boolean validateRegex(String regex, String string) {
        string.trim();
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(string);
        return m.matches();
    }

    private boolean validateUserDetails(JsonObject userDetails, Result result) {
        String firstName = JSONUtil.getString(userDetails, InfinityConstants.firstName);
        String lastName = JSONUtil.getString(userDetails, InfinityConstants.lastName);
        String middleName = JSONUtil.getString(userDetails, InfinityConstants.middleName);
        String ssn = JSONUtil.getString(userDetails, InfinityConstants.ssn);
        String email = JSONUtil.getString(userDetails, InfinityConstants.email);
        String phoneCountryCode = JSONUtil.getString(userDetails, "phoneCountryCode");
        String phoneNumber = JSONUtil.getString(userDetails, "phoneNumber");
        if (StringUtils.isNotBlank(firstName)) {
            if (!validateRegex("[A-Za-z0-9\\s]{1,51}$", firstName)) {
                ErrorCodeEnum.ERR_10050.setErrorCode(result, "Invalid first name");
                return false;
            }
        }
        if (StringUtils.isNotBlank(lastName)) {
            if (!validateRegex("[A-Za-z0-9\\s]{1,51}$", lastName)) {
                ErrorCodeEnum.ERR_10050.setErrorCode(result, "Invalid last name");
                return false;
            }
        }
        if (StringUtils.isNotBlank(middleName)) {
            if (!validateRegex("[A-Za-z0-9\\s]{1,51}$", middleName)) {
                ErrorCodeEnum.ERR_10050.setErrorCode(result, "Invalid middle name");
                return false;
            }
        }
        if (StringUtils.isNotBlank(ssn)) {
            if (!validateRegex("^[^-_][a-zA-Z0-9\\s-]*[a-zA-Z0-9\\s]{1,51}$", ssn)) {
                ErrorCodeEnum.ERR_10050.setErrorCode(result, "Invalid ssn");
                return false;
            }
        }
        if (StringUtils.isNotBlank(email)) {
            if (!validateRegex("^[a-zA-Z0-9_!#$%&â€™*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]{1,51}+$", email)) {
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
        return true;
    }

    @Override
    public Object editInfinityUser(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) {
        Result result = new Result();

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

        if (!map.containsKey(InfinityConstants.userDetails)
                || StringUtils.isBlank(map.get(InfinityConstants.userDetails))) {
            ErrorCodeEnum.ERR_10056.setErrorCode(result);
            return false;
        }

        String userDetails = map.get(InfinityConstants.userDetails);
        JsonElement userDetailsElement = new JsonParser().parse(userDetails);
        if (userDetailsElement.isJsonNull() || !userDetailsElement.isJsonObject()) {
            ErrorCodeEnum.ERR_10056.setErrorCode(result);
            return false;
        }

        Boolean isContractValidationRequired = true;

        Map<String, String> loggedInUserInfo = HelperMethods.getCustomerFromAPIDBPIdentityService(request);
        if (HelperMethods.isAuthenticationCheckRequiredForService(loggedInUserInfo)) {
            Set<String> userPermissions = SessionScope.getAllPermissionsFromIdentityScope(request);
            if (!userPermissions.contains("USER_MANAGEMENT")) {
                ErrorCodeEnum.ERR_10051.setErrorCode(result);
                return result;
            }
        } else {
            isContractValidationRequired = false;
        }

        JsonObject userDetailsJsonObject = userDetailsElement.getAsJsonObject();

        jsonObject.add(InfinityConstants.userDetails, userDetailsJsonObject);

        String signatoryGroups = map.get(InfinityConstants.signatoryGroups);
        jsonObject.addProperty(InfinityConstants.signatoryGroups, signatoryGroups);

        String id = userDetailsJsonObject.has(InfinityConstants.id)
                && !userDetailsJsonObject.get(InfinityConstants.id).isJsonNull()
                        ? userDetailsJsonObject.get(InfinityConstants.id).getAsString()
                        : null;

        diagnostic.prepareDebug("Json request " + jsonObject.toString()).log();
        if (validateinput(jsonObject, result, map, request, isContractValidationRequired, id)) {

            InfinityUserManagementBusinessDelegate infinityUserManagementBusinessDelegate = DBPAPIAbstractFactoryImpl
                    .getBusinessDelegate(InfinityUserManagementBusinessDelegate.class);
            DBXResult dbxResult = infinityUserManagementBusinessDelegate.editInfinityUser(jsonObject,
                    request.getHeaderMap());
            if (dbxResult.getResponse() != null) {
                JsonObject jsonResultObject = (JsonObject) dbxResult.getResponse();
                result = JSONToResult.convert(jsonResultObject.toString());
                updateSignatoryGroupEntry(id, jsonObject, result, request);
                diagnostic.prepareDebug("Json response " + ResultToJSON.convert(result).toString()).log();
            }
        }

        return result;
    }

    private boolean validateinput(JsonObject jsonObject, Result result, Map<String, String> map,
            DataControllerRequest dcRequest, Boolean isContractValidationRequired, String id) {

        Map<String, Set<String>> customerAccountsMap = new HashMap<String, Set<String>>();

        String customerId = null;

        if (isContractValidationRequired) {
            customerId = HelperMethods.getCustomerIdFromSession(dcRequest);
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
        Map<String, Map<String, Map<String, Map<String, Double>>>> loggedInUserLimits =
                new HashMap<String, Map<String, Map<String, Map<String, Double>>>>();
        if (isContractValidationRequired) {
            getLoggedInUserContracts(customerId, customerContracts, dcRequest.getHeaderMap());
            getAccountsForCustomer(customerId, customerAccounts, dcRequest.getHeaderMap());
            getLoggedInUserPermissions(customerId, loggedInUserPermisions, loggedInUserLimits, dcRequest);
        }

        Map<String, String> serviceDefinitions = new HashMap<String, String>();

        ContractBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl
                .getBackendDelegate(ContractBackendDelegate.class);

        Map<String, FeatureActionLimitsDTO> featureActionsLimitsDTOs = new HashMap<String, FeatureActionLimitsDTO>();
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
                        serviceDefinition, contractId, userRole, cif, "", dcRequest.getHeaderMap(), true, "");
                featureActionsLimitsDTOs.put(cif, coreCustomerFeatureActionDTO);
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
        // isAccountLevelAllowed = AdminUtil.isAccountlevelPermissionsAllowed(request);

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

                JsonArray accountLevelPermissionsArray = accountLevelPermissionsElement.getAsJsonArray();

                for (int i = 0; i < accountLevelPermissionsArray.size(); i++) {

                    JsonObject accountLevelPermissionsJsonObject = accountLevelPermissionsArray.get(i)
                            .getAsJsonObject();

                    String cif = accountLevelPermissionsJsonObject.get(InfinityConstants.cif).getAsString();

                    JsonElement accountsElement = accountLevelPermissionsJsonObject.get(InfinityConstants.accounts);

                    FeatureActionLimitsDTO featureActionLimitsDTO = featureActionsLimitsDTOs.get(cif);

                    if (!featureActionsLimitsDTOs.containsKey(cif)
                            || (isContractValidationRequired && !loggedInUserPermisions.containsKey(cif))) {
                        accountLevelPermissionsArray.remove(i);
                        i--;
                        continue;
                    }

                    Map<String, Set<String>> accoutLevelActions = featureActionLimitsDTO.getFeatureaction();
                    Map<String, Map<String, Map<String, String>>> monitoryActions = featureActionLimitsDTO
                            .getMonetaryActionLimits();

                    if (!accountsElement.isJsonNull() && accountsElement.isJsonArray()) {

                        JsonArray accounts = accountsElement.getAsJsonArray();

                        for (int j = 0; j < accounts.size(); j++) {

                            JsonObject accountJsonObject = accounts.get(j).getAsJsonObject();
                            String accountId = accountJsonObject.get(InfinityConstants.accountId).getAsString();

                            if (!customerAccountsMap.containsKey(cif)
                                    || !customerAccountsMap.get(cif).contains(accountId)) {
                                accounts.remove(j);
                                j--;
                                continue;
                            }

                            JsonArray featurePermissions = accountJsonObject.get(InfinityConstants.featurePermissions)
                                    .getAsJsonArray();

                            for (int k = 0; k < featurePermissions.size(); k++) {

                                JsonObject featurePermissionsJsonObject = featurePermissions.get(k).getAsJsonObject();
                                String featureId = featurePermissionsJsonObject.get(InfinityConstants.featureId)
                                        .getAsString();

                                if (!accoutLevelActions.containsKey(featureId)
                                        && !monitoryActions.containsKey(featureId)) {
                                    featurePermissions.remove(k);
                                    k--;
                                    continue;
                                }

                                JsonArray permissions = featurePermissionsJsonObject.get(InfinityConstants.permissions)
                                        .getAsJsonArray();

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

                                    Boolean isAccountLevel = "1".equals(
                                            JSONUtil.getString(featureActionLimitsDTO.getActionsInfo().get(actionId),
                                                    InfinityConstants.isAccountLevel));

                                    if (((accoutLevelActions.get(featureId) == null
                                            || !accoutLevelActions.get(featureId).contains(actionId))
                                            && (monitoryActions.get(featureId) == null
                                                    || !monitoryActions.get(featureId).containsKey(actionId))
                                            && !isAccountLevel) || !isEnabled
                                            || (isContractValidationRequired
                                                    && !loggedInUserPermisions.get(cif).contains(actionId))) {
                                        permissions.remove(l);
                                        l--;
                                        continue;
                                    }
                                }
                            }
                        }
                    }
                }
                jsonObject.add(InfinityConstants.accountLevelPermissions, accountLevelPermissionsArray);
            }
        }

        String globalLevelPermissions = map.get(InfinityConstants.globalLevelPermissions);
        JsonElement globalLevelPermissionsElement = new JsonObject();
        try {
            globalLevelPermissionsElement = new JsonParser().parse(globalLevelPermissions);
        } catch (Exception e) {
        }

        if (!globalLevelPermissionsElement.isJsonNull() && globalLevelPermissionsElement.isJsonArray()) {

            JsonArray globalLevelPermissionsArray = globalLevelPermissionsElement.getAsJsonArray();
            for (int i = 0; i < globalLevelPermissionsArray.size(); i++) {
                JsonObject globalLevelPermissionJsonObject = globalLevelPermissionsArray.get(i).getAsJsonObject();

                String cif = globalLevelPermissionJsonObject.get(InfinityConstants.cif).getAsString();

                JsonElement featuresElement = globalLevelPermissionJsonObject.get(InfinityConstants.features);

                FeatureActionLimitsDTO featureActionLimitsDTO = featureActionsLimitsDTOs.get(cif);

                if (!featureActionsLimitsDTOs.containsKey(cif)
                        || (isContractValidationRequired && !loggedInUserPermisions.containsKey(cif))) {
                    globalLevelPermissionsArray.remove(i);
                    i--;
                    continue;
                }

                Map<String, Set<String>> globalLevelActions = featureActionLimitsDTO.getFeatureaction();

                if (!featuresElement.isJsonNull() && featuresElement.isJsonArray()) {

                    JsonArray features = featuresElement.getAsJsonArray();

                    for (int j = 0; j < features.size(); j++) {

                        JsonObject featureJsonObject = features.get(j).getAsJsonObject();

                        String featureId = featureJsonObject.get(InfinityConstants.featureId).getAsString();
                        if (!globalLevelActions.containsKey(featureId)) {
                            features.remove(j);
                            j--;
                            continue;
                        }
                        JsonArray permissions = featureJsonObject.get(InfinityConstants.permissions).getAsJsonArray();
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
                            Set<String> permissionsSet = globalLevelActions.get(featureId);
                            if (!permissionsSet.contains(permissionId) || !isEnabled || (isContractValidationRequired
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
        }

        String transactionLimits = map.get(InfinityConstants.transactionLimits);
        JsonElement transactionLimitsElement = new JsonObject();
        try {
            transactionLimitsElement = new JsonParser().parse(transactionLimits);
        } catch (Exception e) {
        }

        if (!transactionLimitsElement.isJsonNull() && transactionLimitsElement.isJsonArray()) {

            JsonArray transactionLimitsArray = transactionLimitsElement.getAsJsonArray();
            for (int i = 0; i < transactionLimitsArray.size(); i++) {
                JsonObject transactionLimitsJsonObject = transactionLimitsArray.get(i).getAsJsonObject();

                String cif = transactionLimitsJsonObject.get(InfinityConstants.cif).getAsString();

                FeatureActionLimitsDTO featureActionLimitsDTO = featureActionsLimitsDTOs.get(cif);

                if (!featureActionsLimitsDTOs.containsKey(cif)
                        || (isContractValidationRequired && !loggedInUserLimits.containsKey(cif))) {
                    transactionLimitsArray.remove(i);
                    i--;
                    continue;
                }

                Map<String, Map<String, Map<String, String>>> transactionLimitsMap = featureActionLimitsDTO
                        .getMonetaryActionLimits();

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

                        JsonArray featurePermissions = accountJsonObject.get(InfinityConstants.featurePermissions)
                                .getAsJsonArray();

                        for (int k = 0; k < featurePermissions.size(); k++) {
                            JsonObject featurePermissionJsonObject = featurePermissions.get(k).getAsJsonObject();

                            String feaureId = featurePermissionJsonObject.get(InfinityConstants.featureId)
                                    .getAsString();

                            String actionId = featurePermissionJsonObject.get(InfinityConstants.actionId).getAsString();

                            if (!transactionLimitsMap.containsKey(feaureId)
                                    || !transactionLimitsMap.get(feaureId).containsKey(actionId)
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
        if (StringUtils.isBlank(coreCustomerId) && StringUtils.isBlank(roleId)) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10768);
        }
        try {
            InfinityUserManagementBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl
                    .getBusinessDelegate(InfinityUserManagementBusinessDelegate.class);
            dbxresult = businessDelegate.getCoreCustomerFeatureActionLimits(roleId, coreCustomerId,
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
        String userId = StringUtils.isNotBlank(inputParams.get(InfinityConstants.roleId))
                ? inputParams.get(InfinityConstants.id)
                : request.getParameter(InfinityConstants.id);
        if (StringUtils.isBlank(coreCustomerId) && StringUtils.isBlank(userId)) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10772);
        }
        ContractCustomersDTO dto = new ContractCustomersDTO();
        dto.setCoreCustomerId(coreCustomerId);
        dto.setCustomerId(userId);
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
            InfinityUserManagementBusinessDelegate infinityUserManagementBusinessDelegate = DBPAPIAbstractFactoryImpl
                    .getBusinessDelegate(InfinityUserManagementBusinessDelegate.class);
            DBXResult dbxResult = infinityUserManagementBusinessDelegate.verifyCustomRole(jsonObject,
                    request.getHeaderMap());
            if (dbxResult.getResponse() != null) {
                ErrorCodeEnum.ERR_21107.setErrorCode(result, "Custom Role already present with the name");
                return result;
            }

            if (validateinput(jsonObject, result, map, request, isContractValidationRequired, null)) {
                String name = (String) CustomerSessionsUtil.getLoggedInUserAttributesMap(request)
                        .get(InfinityConstants.UserName);
                jsonObject.addProperty(InfinityConstants.createdby, name);
                dbxResult = infinityUserManagementBusinessDelegate.createCustomRole(jsonObject, request.getHeaderMap());
                if (dbxResult.getResponse() != null) {
                    jsonObject = (JsonObject) dbxResult.getResponse();
                    result = JSONToResult.convert(jsonObject.toString());
                    String customRoleId = jsonObject.get(InfinityConstants.id).getAsString();
                    String signatoryGroups = map.get(InfinityConstants.signatoryGroups);
                    JsonElement signatoryGroupsElement = new JsonParser().parse(signatoryGroups);
                    if (!signatoryGroupsElement.isJsonNull() && signatoryGroupsElement.isJsonArray()) {
                        JsonArray customRoleSignatoryGroupsObject = signatoryGroupsElement.getAsJsonArray();
                        updateSignatoryGroups(customRoleId, customRoleSignatoryGroupsObject, request);
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
                // filter = InfinityConstants.customerId + DBPUtilitiesConstants.EQUAL + customerId;
                // map.put(DBPUtilitiesConstants.FILTER, filter);
                //
                // jsonObject = ServiceCallHelper.invokeServiceAndGetJson(map, request.getHeaderMap(),
                // URLConstants.CUSTOMER_SIGNATORY_GROUP_GET);
                //
                // if (jsonObject.has(DBPDatasetConstants.CUSTOMER_SIGNATORY_GROUP)
                // && jsonObject.get(DBPDatasetConstants.CUSTOMER_SIGNATORY_GROUP).isJsonArray()
                // && jsonObject.get(DBPDatasetConstants.CUSTOMER_SIGNATORY_GROUP).getAsJsonArray().size() > 0) {
                // JsonArray groups = jsonObject.get(DBPDatasetConstants.CUSTOMER_SIGNATORY_GROUP).getAsJsonArray();
                // for (JsonElement group : groups) {
                // if (group.isJsonObject()) {
                // if (group.getAsJsonObject().has(InfinityConstants.signatoryGroupId)
                // && !group.getAsJsonObject().get(InfinityConstants.signatoryGroupId).isJsonNull()) {
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

            if (validateinput(jsonObject, result, map, request, isContractValidationRequired, null)) {
                InfinityUserManagementBusinessDelegate infinityUserManagementBusinessDelegate =
                        DBPAPIAbstractFactoryImpl
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

            jsonObject.addProperty(InfinityConstants.customerId, HelperMethods.getCustomerIdFromSession(request));

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
        ContractCustomersDTO contractCustomerDTO = new ContractCustomersDTO();
        contractCustomerDTO.setCustomerId(customerId);
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
            jsonObject.addProperty(InfinityConstants.contractId, contractId);

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
            DataControllerResponse response) {
        Result result = new Result();
        Map<String, String> loggedInUserInfo = HelperMethods.getCustomerFromAPIDBPIdentityService(request);
        if (HelperMethods.isAuthenticationCheckRequiredForService(loggedInUserInfo)) {
            Set<String> userPermissions = SessionScope.getAllPermissionsFromIdentityScope(request);
            if (!userPermissions.contains("USER_MANAGEMENT")) {
                ErrorCodeEnum.ERR_10051.setErrorCode(result);
                return result;
            }
        }

        try {
            LegalEntityUtil.addCompanyIDToHeaders(request);
            JsonObject jsonObject = new JsonObject();

            String id = HelperMethods.getCustomerIdFromSession(request);

            jsonObject.addProperty(InfinityConstants.id, id);

            String contractId = StringUtils
                    .isNotBlank(HelperMethods.getInputParamMap(inputArray).get(InfinityConstants.contractId))
                            ? HelperMethods.getInputParamMap(inputArray).get(InfinityConstants.contractId)
                            : request.getParameter(InfinityConstants.contractId);
            jsonObject.addProperty(InfinityConstants.contractId, contractId);
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
        ContractCustomersDTO contractCustomerDTO = new ContractCustomersDTO();
        contractCustomerDTO.setCustomerId(userId);
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
                            + e.getMessage()).log();
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
        createUserAccounts(userId, contractId, createdCoreCustomerAccounts, request);
        createUserActionLimits(userId, contractId, createdValidContractCoreCustomers, userToCoreCustomerRoles, request);
        createCustomerPreference(userId, request.getHeaderMap());
        if (payloadJson.has(DBPUtilitiesConstants.BACKENDID)
                && !payloadJson.get(DBPUtilitiesConstants.BACKENDID).isJsonNull()
                && StringUtils.isNotBlank(payloadJson.get(DBPUtilitiesConstants.BACKENDID).getAsString())) {
            backendId = payloadJson.get(DBPUtilitiesConstants.BACKENDID).getAsString();
        }
        String companyId = "";
        createBackendIdentifierEntry(backendId, userId, contractId, request, companyId, null);
        deleteMfaService(serviceKey, request);

        request.addRequestParam_("userId", userId);
        request.addRequestParam_("contractStatus", contractStatus);
        request.addRequestParam_("contractId", contractId);

    }

    private void deleteMfaService(String serviceKey, DataControllerRequest request) throws ApplicationException {
        MFAServiceBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl
                .getBusinessDelegate(MFAServiceBusinessDelegate.class);

        businessDelegate.deleteMfaService(serviceKey, request.getHeaderMap());

    }

    private void createBackendIdentifierEntry(String backendId, String userId, String contractId,
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
        if (StringUtils.isBlank(companyId)) {
            backendIdentifierDTO
                    .setCompanyId(EnvironmentConfigurationsHandler.getValue(DBPUtilitiesConstants.BRANCH_ID_REFERENCE));
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
            if (StringUtils.isBlank(companyId)) {
                backendIdentifierDTO.setCompanyId(
                        EnvironmentConfigurationsHandler.getValue(DBPUtilitiesConstants.BRANCH_ID_REFERENCE));
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
            if (StringUtils.isBlank(companyId)) {
                backendIdentifierDTO.setCompanyId(
                        EnvironmentConfigurationsHandler.getValue(DBPUtilitiesConstants.BRANCH_ID_REFERENCE));
            }
            input = DTOUtils.getParameterMap(backendIdentifierDTO, true);
            backendIdentifierDTO.persist(input, new HashMap<String, Object>());
        }

    }

    private void createCustomerCommunication(StringBuilder email, StringBuilder phone, String customerId,
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

    private void updateCommunicationDetails(StringBuilder phone, StringBuilder email, JsonObject payloadJson) {
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

    private JsonObject getPayload(String serviceKey, DataControllerRequest dcRequest) throws ApplicationException {

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

    private void createUserRoles(String userId, String contractId, Map<String, String> userToCoreCustomerRoles,
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
            customerGroupBD.createCustomerGroup(dto, request.getHeaderMap());
        }

    }

    private Map<String, String> getUserToCoreCustomerRoles(String authorizedSignatoryRoles)
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

    private void createUserActionLimits(String userId, String contractId, Set<String> createdValidContractCoreCustomers,
            Map<String, String> userToCoreCustomerRoles, DataControllerRequest request) throws ApplicationException {
        CustomerActionsBusinessDelegate customerActionsBD = DBPAPIAbstractFactoryImpl
                .getBusinessDelegate(CustomerActionsBusinessDelegate.class);
        for (String coreCustomerId : createdValidContractCoreCustomers) {
            customerActionsBD.createCustomerActions(userId, contractId, coreCustomerId,
                    userToCoreCustomerRoles.get(coreCustomerId), new HashSet<String>(), request.getHeaderMap());

            customerActionsBD.createCustomerLimitGroupLimits(userId, contractId, coreCustomerId,
                    request.getHeaderMap());
        }

    }

    private void createUserAccounts(String userId, String contractId,
            Map<String, Set<ContractAccountsDTO>> createdCoreCustomerAccounts, DataControllerRequest request)
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
            customerAccountsBD.createCustomerAccounts(userId, contractId, coreCustomerId, accounts,
                    request.getHeaderMap());
        }

    }

    private void assignUserToContractCustomers(String userId, String contractId,
            Set<String> createdValidContractCoreCustomers, DataControllerRequest request) {
        ContractCustomersDTO contractCustomerDTO;
        for (String customerId : createdValidContractCoreCustomers) {
            contractCustomerDTO = new ContractCustomersDTO();
            contractCustomerDTO.setId(idFormatter.format(new Date()));
            contractCustomerDTO.setContractId(contractId);
            contractCustomerDTO.setCoreCustomerId(customerId);
            contractCustomerDTO.setCustomerId(userId);
            contractCustomerDTO.setAutoSyncAccounts(true);
            Map<String, Object> inputParams = DTOUtils.getParameterMap(contractCustomerDTO, false);
            contractCustomerDTO.persist(inputParams, request.getHeaderMap());
        }

    }

    private String createUser(CustomerDTO customerDTO, String createdServiceType, DataControllerRequest request)
            throws ApplicationException {
        UserManagementBusinessDelegate customerBD = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class)
                .getBusinessDelegate(UserManagementBusinessDelegate.class);

        String id = HelperMethods.generateUniqueCustomerId(request);
        customerDTO.setId(id);
        customerDTO.setUserName(HelperMethods.generateUniqueUserName(request));
        customerDTO.setIsNew(true);
        customerDTO.setStatus_id(DBPUtilitiesConstants.CUSTOMER_STATUS_NEW);
        customerDTO.setCustomerType_id(createdServiceType);

        DBXResult customerResult = customerBD.update(customerDTO, request.getHeaderMap());
        String customerId = (String) customerResult.getResponse();
        if (StringUtils.isBlank(customerId)) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10386);
        }

        return customerId;
    }

    @Override
    public Object createInfinityUserWithContract(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) {

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
                    e.getErrorCodeEnum().setErrorCode(result);
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

        result = (Result) userManagementResource.createInfinityUser(methodId, inputArray, request, response);
        return result;
    }

    @Override
    public Object editInfinityUserWithContract(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) {

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
                    e.getErrorCodeEnum().setErrorCode(result);
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

    public Result getInfinityUserAccountsForAdmin(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws ApplicationException {
        Map<String, String> inputParams = new HashMap<>();
        Result result = new Result();
        String userId = StringUtils.isNotBlank(inputParams.get("userId")) ? inputParams.get("userId")
                : dcRequest.getParameter("userId");
        if (StringUtils.isBlank(userId)) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10792);
        }
        CustomerAccountsDTO customerAccountsDTO = new CustomerAccountsDTO();
        customerAccountsDTO.setCustomerId(userId);
        DBXResult response = new DBXResult();
        try {
            InfinityUserManagementBusinessDelegate infinityUserManagementBusinessDelegate = DBPAPIAbstractFactoryImpl
                    .getBusinessDelegate(InfinityUserManagementBusinessDelegate.class);
            response = infinityUserManagementBusinessDelegate.getInfinityUserAccountsForAdmin(customerAccountsDTO,
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
            DataControllerResponse response) throws ApplicationException {
        request.addRequestParam_(InfinityConstants.id, HelperMethods.getInputParamMap(inputArray).get("userId"));
        return getInfinityUser(methodID, inputArray, request, response);
    }

    @Override
    public Result getInfinityUserLimits(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws ApplicationException {
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
             * The below parameters have been added in dcRequest, that is being used while pushing the sca event
             */

            dcRequest.addRequestParam_("userId", userIdForSCA);
            dcRequest.addRequestParam_("activationCode", activationCode);
            inputParams.put("userId", userIdForSCA);
            inputParams.put("activationCode", activationCode);

            Map<String, String> map = new HashMap<>();
            Object[] inputArray1 = new Object[3];
            map.put("userId", userIdForSCA);
            map.put("activationCode", activationCode);
            inputArray1[1] = map;

            PushExternalEventResource resource = DBPAPIAbstractFactoryImpl.getResource(PushExternalEventResource.class);
            resource.pushUserIdAndActivationCode(methodID, inputArray1, dcRequest, dcResponse);
        } catch (ApplicationException e) {
            alert.prepareError(
                    "InfinityUserManagementResourceImpl : Exception occured while generating username and activation code "
                            + e.getMessage()).log();
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            alert.prepareError(
                    "InfinityUserManagementResourceImpl : Exception occured while generating username and activation code "
                            + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10795);
        }
        return result;
    }

    @Override
    public Object createRetailContract(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) {

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

            String name = "";
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
            }

            if (HelperMethods.isBlank(customerId, coreCustomerId)) {
                throw new ApplicationException(ErrorCodeEnum.ERR_10349, "Invalid customerId or coreCustomerId");
            }

            map.put(InfinityConstants.coreCustomerId, coreCustomerId);
            map.put("companyId", companyId);
            map.put(DEFAULT_SERVICE_ID, BundleConfigurationHandler.DEFAULT_RETAIL_SERVICE_ID);
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
            String customerId = map.get(InfinityConstants.customerId);
            String coreCustomerId = map.get(InfinityConstants.coreCustomerId);
            String defaultServiceId = map.get(DEFAULT_SERVICE_ID);

            String filter = InfinityConstants.customerId + DBPUtilitiesConstants.EQUAL + customerId
                    + DBPUtilitiesConstants.AND + InfinityConstants.coreCustomerId + DBPUtilitiesConstants.EQUAL
                    + coreCustomerId;

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
                    e.getErrorCodeEnum().setErrorCode(result);
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
                addAccountsToContract(contractId, coreCustomerId, map, request.getHeaderMap());
                Set<String> newAccounts = getAccounts(customerId, contractId, coreCustomerId, request.getHeaderMap(),
                        map);
                getContractInfo(map, contractId, coreCustomerId, customerId, request.getHeaderMap());
                JsonObject jsonObject = createInfinityUserPayload(map, contractId, request.getHeaderMap(),
                        autoSyncAccounts, request, false);
                new InfinityUserManagementBackendDelegateImpl().createCustomerAction(customerId, jsonObject,
                        request.getHeaderMap(), null, null, false);
                result.addParam(InfinityConstants.id, customerId);

                ContractCoreCustomerBackendDelegate contractCoreCustomerBD = DBPAPIAbstractFactoryImpl
                        .getBackendDelegate(ContractCoreCustomerBackendDelegate.class);
                Map<String, Set<String>> contractCoreCustomerDetailsMap = contractCoreCustomerBD
                        .getCoreCustomerAccountsFeaturesActions(contractId, coreCustomerId, request.getHeaderMap());
                Set<String> customerAccounts = contractCoreCustomerDetailsMap.get("accounts");
                customerAccounts.retainAll(newAccounts);
                Set<String> customerActions = contractCoreCustomerDetailsMap.get("actions");
                customerActions = getActionWithApproveFeatureAction(customerActions, request.getHeaderMap());
                ApprovalMatrixBusinessDelegate approvalmatrixDelegate = DBPAPIAbstractFactoryImpl
                        .getBusinessDelegate(ApprovalMatrixBusinessDelegate.class);
                approvalmatrixDelegate.createDefaultApprovalMatrixEntry(contractId,
                        String.join(DBPUtilitiesConstants.COMMA_SEPERATOR, customerAccounts),
                        customerActions.toArray(new String[0]), coreCustomerId, null);
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
                         * HelperMethods.getInputParamMap(inputArray).put(InfinityConstants. EMAIL_TEMPLATE,
                         * DBPUtilitiesConstants.ONBOARDING_USERNAME_TEMPLATE);
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
            e.getErrorCodeEnum().setErrorCode(result);
            alert.prepareError("Exception occured while creating a contract " + e.getStackTrace()).log();
            return result;
        } catch (Exception e) {
            result = new Result();
            alert.prepareError("Exception occured while creating a contract " + e.getStackTrace()).log();
            
            return result;
        }

        return result;
    }

    private void deleteGroup(String customerId, Map<String, String> map, DataControllerRequest request) {

        String filter = InfinityConstants.Customer_id + DBPUtilitiesConstants.EQUAL + customerId
                + DBPUtilitiesConstants.AND + InfinityConstants.Group_id + DBPUtilitiesConstants.EQUAL
                + map.get(BundleConfigurationHandler.DEFAULT_PROSPECT_GROUP)
                + DBPUtilitiesConstants.AND + DBPUtilitiesConstants.EQUAL + InfinityConstants.coreCustomerId
                + "null" + DBPUtilitiesConstants.AND + InfinityConstants.contractId
                + DBPUtilitiesConstants.EQUAL + "null";
        Map<String, Object> input = new HashMap<String, Object>();
        input.put(DBPUtilitiesConstants.FILTER, filter);
        ServiceCallHelper.invokeServiceAndGetJson(input, request.getHeaderMap(), URLConstants.CUSTOMER_GROUP_DELETE);
    }

    private Set<String> getActionWithApproveFeatureAction(Set<String> actionsSet, Map<String, Object> headersMap)
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

        String actions = contractFeaturesBD.getActionsWithApproveFeatureAction(actionsString.toString(), headersMap);

        return HelperMethods.splitString(actions, DBPUtilitiesConstants.COMMA_SEPERATOR);

    }

    private void getContractInfo(Map<String, String> map, String contractId, String coreCustomerId, String customerId,
            Map<String, Object> headerMap) {

        map.put(InfinityConstants.contractId, contractId);
        map.put(InfinityConstants.coreCustomerId, coreCustomerId);

        String serviceDefinitionId = "";

        String filter = InfinityConstants.id + DBPUtilitiesConstants.EQUAL + contractId;
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
                + DBPUtilitiesConstants.AND + InfinityConstants.Customer_id + DBPUtilitiesConstants.EQUAL + customerId;

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
        JsonArray accounts = new JsonArray();
        try {
            accounts = new JsonParser().parse(accountsString).getAsJsonArray();
            for (JsonElement accountElement : accounts) {
                JsonObject account = accountElement.getAsJsonObject();
                account.add(InfinityConstants.accountType, account.get(InfinityConstants.typeId));
            }
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10349, "Invalid account Array");
        }

        company.addProperty(InfinityConstants.roleId, roleId);
        company.addProperty(InfinityConstants.serviceDefinition, serviceDefinition);
        company.addProperty(InfinityConstants.cif, coreCustomerId);
        company.addProperty(InfinityConstants.isPrimary, "true");
        company.addProperty(InfinityConstants.contractId, contractId);
        company.add(InfinityConstants.accounts, accounts);

        Map<String, String> bundleConfigurations = BundleConfigurationHandler
                .fetchBundleConfigurations(BundleConfigurationHandler.BUDLENAME_C360, request);

        autoSyncAccounts = autoSyncAccounts.equals(BundleConfigurationHandler.DEFAULT_BUSINESS_SERVICE_ID)
                ? bundleConfigurations.get(BundleConfigurationHandler.AUTO_SYNC_BUSINESS_ACCOUNTS)
                : bundleConfigurations.get(BundleConfigurationHandler.AUTO_SYNC_RETAIL_ACCOUNTS);
        autoSyncAccounts = bundleConfigurations.get(autoSyncAccounts);

        if (StringUtils.isNotBlank(autoSyncAccounts)) {
            company.addProperty(InfinityConstants.autoSyncAccounts, autoSyncAccounts);
        } else {
            company.addProperty(InfinityConstants.autoSyncAccounts, false);
        }

        companyList.add(company);

        ContractBackendDelegate contractBackendDelegate = DBPAPIAbstractFactoryImpl
                .getBackendDelegate(ContractBackendDelegate.class);

        FeatureActionLimitsDTO coreCustomerFeatureActionDTO = contractBackendDelegate.getRestrictiveFeatureActionLimits(
                serviceDefinition, null, roleId, coreCustomerId, "", headerMap, false, "");

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
                        && "1".equals(actionJson.get(InfinityConstants.isAccountLevel).getAsString());

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
        JsonArray transactionLimits = new JsonArray();

        transactionLimits.add(transactionLimit);

        JsonObject userDetails = new JsonObject();

        userDetails.addProperty(InfinityConstants.id, customerId);

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

    private void getContractPayload(Map<String, String> map, DataControllerRequest request, String coreCustomerId,
            String serviceDefinitionId) throws ApplicationException {
        // TODO Auto-generated method stub
        LegalEntityUtil.addCompanyIDToHeaders(request);
        String customerId = map.get(InfinityConstants.customerId);

        String accountsString = map.get(InfinityConstants.accounts);

        String contractName = map.get(InfinityConstants.contractName);
        String coreCustomerName = map.get(InfinityConstants.coreCustomerName);

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
        serviceDefinitionId = bundleConfigurations.get(serviceDefinitionId);

        String defaultRole = null;

        HashMap<String, Object> input = new HashMap<String, Object>();

        String filter = InfinityConstants.serviceDefinitionId + DBPUtilitiesConstants.EQUAL + serviceDefinitionId
                + DBPUtilitiesConstants.AND + InfinityConstants.isDefaultGroup + DBPUtilitiesConstants.EQUAL + '1';

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

        filter = InfinityConstants.id + DBPUtilitiesConstants.EQUAL + serviceDefinitionId;

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
                request.getHeaderMap());

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
        jsonObject.addProperty(ContractResourceImpl.IMPLICIT_ACCOUNT_ACCESS, implicit);
        jsonObject.add(InfinityConstants.accounts, accounts);

        ContractBackendDelegate contractBackendDelegate = DBPAPIAbstractFactoryImpl
                .getBackendDelegate(ContractBackendDelegate.class);

        FeatureActionLimitsDTO coreCustomerFeatureActionDTO = contractBackendDelegate.getRestrictiveFeatureActionLimits(
                serviceDefinitionId, null, defaultRole, null, "", request.getHeaderMap(), false, "");

        Map<String, Map<String, Map<String, String>>> monitoryLimits = coreCustomerFeatureActionDTO
                .getMonetaryActionLimits();

        Map<String, Set<String>> featureActions = coreCustomerFeatureActionDTO.getFeatureaction();

        for (Entry<String, Set<String>> featureActionEntry : featureActions.entrySet()) {
            if (!monitoryLimits.containsKey(featureActionEntry.getKey())) {
                monitoryLimits.put(featureActionEntry.getKey(), new HashMap<String, Map<String, String>>());
            }

            for (String action : featureActionEntry.getValue()) {
                if (!monitoryLimits.get(featureActionEntry.getKey()).containsKey(action)) {
                    monitoryLimits.get(featureActionEntry.getKey()).put(action, new HashMap<String, String>());
                }
            }
        }

        JsonArray features = new JsonArray();
        for (Entry<String, Map<String, Map<String, String>>> featureEntry : monitoryLimits.entrySet()) {
            JsonObject featureJsonObject = new JsonObject();
            if (coreCustomerFeatureActionDTO.getFeatureInfo().containsKey(featureEntry.getKey())) {
                for (Entry<String, JsonElement> featureParams : coreCustomerFeatureActionDTO.getFeatureInfo()
                        .get(featureEntry.getKey()).entrySet()) {
                    featureJsonObject.add(featureParams.getKey(), featureParams.getValue());
                }
                JsonArray actions = new JsonArray();
                for (Entry<String, Map<String, String>> actionEntry : featureEntry.getValue().entrySet()) {
                    JsonObject action = new JsonObject();
                    if (coreCustomerFeatureActionDTO.getActionsInfo().containsKey(actionEntry.getKey())) {
                        for (Entry<String, JsonElement> actionParams : coreCustomerFeatureActionDTO.getActionsInfo()
                                .get(actionEntry.getKey()).entrySet()) {
                            action.add(actionParams.getKey(), actionParams.getValue());
                        }
                        if (actionEntry.getValue().size() != 0) {
                            JsonArray limits = new JsonArray();
                            for (Entry<String, String> limitsEntry : actionEntry.getValue().entrySet()) {
                                JsonObject limit = new JsonObject();

                                limit.addProperty(InfinityConstants.id, limitsEntry.getKey());
                                limit.addProperty(InfinityConstants.value, limitsEntry.getValue());
                                limits.add(limit);
                            }
                            action.add(InfinityConstants.limits, limits);
                        }
                        action.addProperty(InfinityConstants.isAllowed, "true");
                    }
                    actions.add(action);
                }
                featureJsonObject.add(InfinityConstants.actions, actions);
            }
            features.add(featureJsonObject);
        }

        jsonObject.add(InfinityConstants.features, features);

        map.put(InfinityConstants.contractName, contractName);
        map.put(InfinityConstants.serviceDefinitionName, serviceDefinitionName);
        map.put(InfinityConstants.serviceDefinitionId, serviceDefinitionId);
        map.put(InfinityConstants.communication, array.toString());
        map.put(InfinityConstants.address, addressArray.toString());
        map.put(InfinityConstants.faxId, null);
        JsonArray contractCustomers = new JsonArray();
        contractCustomers.add(jsonObject);
        map.put(InfinityConstants.contractCustomers, contractCustomers.toString());

        diagnostic.prepareDebug("input Map to create Contract " + map).log();

    }

    @Override
    public Object applyCustomRole(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) {
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
            dbxResult = infinityUserManagementBusinessDelegate.editInfinityUser(editUserJsonObject,
                    request.getHeaderMap());
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
        LegalEntityUtil.addCompanyIDToHeaders(request);
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        Result result = new Result();
        JsonObject inputObject = new JsonObject();

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
        }

        String loggedInUserId = HelperMethods.getCustomerIdFromSession(request);

        inputObject.addProperty(InfinityConstants.id, primaryApplicantId);
        inputObject.addProperty(InfinityConstants.loggedInUserId, loggedInUserId);
        InfinityUserManagementBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl
                .getBusinessDelegate(InfinityUserManagementBusinessDelegate.class);
        DBXResult dbxResult = businessDelegate.getInfinityUserPrimaryRetailContract(inputObject,
                request.getHeaderMap());
        JsonObject jsonObject = (JsonObject) dbxResult.getResponse();
        removeUnAssignedAccounts(jsonObject, accounts);
        if (dbxResult.getResponse() != null) {
            new InfinityUserManagementBackendDelegateImpl().createCustomerAction(coApplicantId, jsonObject,
                    request.getHeaderMap(), null, null, true);

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
                 * HelperMethods.getInputParamMap(inputArray).put(InfinityConstants. EMAIL_TEMPLATE,
                 * DBPUtilitiesConstants.ONBOARDING_USERNAME_TEMPLATE);
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
        if (StringUtils.isBlank(userName) && StringUtils.isBlank(userId)) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10799);
        }
        InfinityUserManagementBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl
                .getBusinessDelegate(InfinityUserManagementBusinessDelegate.class);
        try {
            DBXResult dbxresult = businessDelegate.getUserApprovalPermissions(userId, userName,
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
        String lastName = StringUtils.isBlank(inputParams.get("lastName")) ? inputParams.get("lastName")
                : dcRequest.getParameter("lastName");
        String taxId = StringUtils.isBlank(inputParams.get("taxId")) ? inputParams.get("taxId")
                : dcRequest.getParameter("taxId");
        String dateOfBirth = StringUtils.isBlank(inputParams.get("dateOfBirth")) ? inputParams.get("dateOfBirth")
                : dcRequest.getParameter("dateOfBirth");

        if (StringUtils.isBlank(lastName) || StringUtils.isBlank(taxId) || StringUtils.isBlank(dateOfBirth)) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10801);
        }

        InfinityUserManagementBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl
                .getBusinessDelegate(InfinityUserManagementBusinessDelegate.class);
        InfinityUserManagementResource infinityUserManagementResource = DBPAPIAbstractFactoryImpl
                .getResource(InfinityUserManagementResource.class);

        try {
            DBXResult dbxresult = businessDelegate.validateCustomerEnrollmentDetails(lastName, taxId, dateOfBirth,
                    dcRequest.getHeaderMap());
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
                com.temenos.dbx.product.contract.businessdelegate.api.ContractBusinessDelegate contractBusinessDelegate =
                        DBPAPIAbstractFactoryImpl
                                .getBusinessDelegate(
                                        com.temenos.dbx.product.contract.businessdelegate.api.ContractBusinessDelegate.class);

                Map<String, Object> contractPayload = createContractPayload(coreCustomerJson, dcRequest);
                inputArray[1] = contractPayload;
                dcRequest.addRequestParam_("isDefaultActionsEnabled", "true");
                result = resource.createContract(methodID, inputArray, dcRequest, dcResponse);
                contractBusinessDelegate.updateContractStatus(result.getParamValueByName("contractId"),
                        DBPUtilitiesConstants.CONTRACT_STATUS_ACTIVE, dcRequest.getHeaderMap());
                infinityUserId = createAUserAndAssignTOGivenContract(contractPayload,
                        JSONUtil.getString(coreCustomerJson, "id"), dcRequest,
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
        String companyId = EnvironmentConfigurationsHandler
                .getServerProperty(DBPUtilitiesConstants.BRANCH_ID_REFERENCE);

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
                    createUserAccounts(userId, contractId, createdCoreCustomerAccounts, request);
                    createUserActionLimits(userId, contractId, createdValidContractCoreCustomers,
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

    private void createCustomerPreference(String customerId, Map<String, Object> headersMap) {
        CustomerPreferenceDTO customerPreferenceDTO = new CustomerPreferenceDTO();
        customerPreferenceDTO.setId(HelperMethods.getNewId());
        customerPreferenceDTO.setCustomer_id(customerId);
        customerPreferenceDTO.setIsNew(true);

        CustomerPreferenceBusinessDelegate customerPreferenceBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class)
                .getBusinessDelegate(CustomerPreferenceBusinessDelegate.class);

        customerPreferenceBusinessDelegate.update(customerPreferenceDTO, headersMap);

    }

    private Map<String, Object> createContractPayload(JsonObject coreCustomer, DataControllerRequest dcRequest)
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
        serviceDefinitionDTO.setId("5801fa32-a416-45b6-af01-b22e2de93777");

        String contractName = JSONUtil.getString(coreCustomer, "name");
        if (StringUtils.isBlank(contractName))
            contractName = JSONUtil.getString(coreCustomer, "firstName") + " "
                    + JSONUtil.getString(coreCustomer, "lastName");

        contractPayloadMap.put("contractName", contractName);
        contractPayloadMap.put("serviceDefinitionName", "Retail Online Banking");
        contractPayloadMap.put("serviceDefinitionId", "5801fa32-a416-45b6-af01-b22e2de93777");
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
        accountsResult = coreCustomerBackendDelegate.getCoreCustomerAccounts(membershipDTO, dcRequest.getHeaderMap());
        if (accountsResult != null && accountsResult.getResponse() != null) {
            @SuppressWarnings("unchecked")
            List<AllAccountsViewDTO> accountsList = (List<AllAccountsViewDTO>) accountsResult.getResponse();
            String accounts = "";
            try {
                accounts = JSONUtils.stringifyCollectionWithTypeInfo(accountsList, AllAccountsViewDTO.class);
            } catch (Exception e) {
            	alert.prepareError("exception",e).log();
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

            String companyId = EnvironmentConfigurationsHandler
                    .getServerProperty(DBPUtilitiesConstants.BRANCH_ID_REFERENCE);
            DBXResult dbxResult = DBPAPIAbstractFactoryImpl.getBackendDelegate(BackendIdentifiersBackendDelegate.class)
                    .get(backendIdentifierDTO, request.getHeaderMap());
            if (dbxResult.getResponse() != null) {
                BackendIdentifierDTO identifierDTO = (BackendIdentifierDTO) dbxResult.getResponse();
                companyId = StringUtils.isNotBlank(identifierDTO.getCompanyId()) ? identifierDTO.getCompanyId()
                        : companyId;
            }

            request.getHeaderMap().put("companyId", companyId);
            HelperMethods.addJWTAuthHeader(request.getHeaderMap(), AuthConstants.PRE_LOGIN_FLOW);
            Map<String, Object> inputParams = new HashMap<String, Object>();
            inputParams.put("customerId", coreCustomerId);
            membershipJson = ServiceCallHelper.invokeServiceAndGetJson(serviceId, null, operationName, inputParams,
                    request.getHeaderMap());

            JsonArray jsonArray = new JsonArray();

            if (null != membershipJson && JSONUtil.hasKey(membershipJson, DBPDatasetConstants.DATASET_CUSTOMERS)
                    && membershipJson.get(DBPDatasetConstants.DATASET_CUSTOMERS).isJsonArray()) {
                jsonArray = membershipJson.get(DBPDatasetConstants.DATASET_CUSTOMERS).getAsJsonArray();
            }
            JsonObject customerJson = new JsonObject();
            if (jsonArray.size() > 0) {
                customerJson = jsonArray.get(0).getAsJsonObject();
                name = JSONUtil.getString(customerJson, "firstName") + " "
                        + JSONUtil.getString(customerJson, "lastName") +
                        " " + coreCustomerId;
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
            map.put(DEFAULT_SERVICE_ID, BundleConfigurationHandler.DEFAULT_BUSINESS_SERVICE_ID);
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
}
