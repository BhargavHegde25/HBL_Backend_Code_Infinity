package com.temenos.dbx.eum.product.usermanagement.backenddelegate.impl;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;

import org.apache.commons.lang3.StringUtils;

import org.json.JSONException;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.DBPDatasetConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.IntegrationTemplateURLFinder;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.LegalEntityUtil;
import com.kony.dbputilities.util.ServiceId;
import com.kony.dbputilities.util.URLConstants;
import com.kony.eum.dbputilities.util.ServiceCallHelper;
import com.kony.eum.dbputilities.util.UnsubscribeAlertsForRemovedAccounts;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.eum.product.constants.Constants;
import com.temenos.dbx.eum.product.contract.backenddelegate.api.ContractBackendDelegate;
import com.temenos.dbx.eum.product.contract.backenddelegate.api.ContractCoreCustomerBackendDelegate;
import com.temenos.dbx.eum.product.contract.backenddelegate.api.CoreCustomerBackendDelegate;
import com.temenos.dbx.eum.product.limitsandpermissions.backenddelegate.api.LimitsAndPermissionsBackendDelegate;
import com.temenos.dbx.eum.product.limitsandpermissions.dto.ActionLimitsDTO;
import com.temenos.dbx.eum.product.usermanagement.backenddelegate.api.BackendIdentifiersBackendDelegate;
import com.temenos.dbx.eum.product.usermanagement.backenddelegate.api.InfinityUserManagementBackendDelegate;
import com.temenos.dbx.product.dto.BackendIdentifierDTO;
import com.temenos.dbx.product.dto.ContractCoreCustomersDTO;
import com.temenos.dbx.product.dto.ContractCustomersDTO;
import com.temenos.dbx.product.dto.CustomerAddressDTO;
import com.temenos.dbx.product.dto.CustomerCommunicationDTO;
import com.temenos.dbx.product.dto.CustomerDTO;
import com.temenos.dbx.product.dto.CustomerLegalEntityDTO;
import com.temenos.dbx.product.dto.AssociatedContractUsersDTO;
import com.temenos.dbx.product.dto.CustomerPreferenceDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.dto.FeatureActionLimitsDTO;
import com.temenos.dbx.product.dto.MembershipDTO;
import com.temenos.dbx.product.dto.UserCustomerViewDTO;
import com.temenos.dbx.product.dto.UserCustomerViewDTOwithAccounts;
import com.temenos.dbx.product.usermanagement.backenddelegate.api.CommunicationBackendDelegate;
import com.temenos.dbx.eum.product.usermanagement.backenddelegate.impl.InfinityUserManagementBackendDelegateImpl;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.CustomerPreferenceBusinessDelegate;
import com.temenos.dbx.product.utils.DTOConstants;
import com.temenos.dbx.product.utils.DTOUtils;
import com.temenos.dbx.product.utils.InfinityConstants;
import com.temenos.dbx.product.utils.ThreadExecutor;


public class InfinityUserManagementBackendDelegateImpl implements InfinityUserManagementBackendDelegate {
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    private static final String RANDOM_GENERATOR_ALGORITHM = "SHA1PRNG";

    @Override
    public List<UserCustomerViewDTO> getAssociatedCustomers(ContractCustomersDTO contractCustomerDTO,
            Map<String, Object> headersMap)
            throws ApplicationException {
        List<UserCustomerViewDTO> customers = new ArrayList<>();
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("_customerId",
                StringUtils.isNotBlank(contractCustomerDTO.getCustomerId()) ? contractCustomerDTO.getCustomerId() : "");
        inputParams.put("_coreCustomerId",
                StringUtils.isNotBlank(contractCustomerDTO.getCoreCustomerId())
                        ? contractCustomerDTO.getCoreCustomerId()
                        : "");
        inputParams.put("_legalEntityId", contractCustomerDTO.getCompanyLegalUnit());
        
        try {
            JsonObject response = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                    URLConstants.USER_CUSTOMERS_PROC);
            if (JSONUtil.hasKey(response, DBPDatasetConstants.DATASET_RECORDS)
                    && response.get(DBPDatasetConstants.DATASET_RECORDS).getAsJsonArray().size() > 0) {
                customers = JSONUtils.parseAsList(
                        response.get(DBPDatasetConstants.DATASET_RECORDS).getAsJsonArray().toString(),
                        UserCustomerViewDTO.class);
            }
        } catch (Exception e) {
            alert.prepareError(
                    "InfinityUserManagementBackendDelegateImpl : Exception occured while fetching the associated customers info "
                            + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10763);
        }
        return customers;
    }

    @Override
    public DBXResult createInfinityUser(JsonObject jsonObject, Map<String, Object> headerMap) {

        DBXResult dbxResult = new DBXResult();
        JsonObject result = new JsonObject();
        dbxResult.setResponse(result);
        JsonObject userDetails = jsonObject.get(InfinityConstants.userDetails).getAsJsonObject();

        String coreCustomerId = userDetails.has(InfinityConstants.coreCustomerId)
                && !userDetails.get(InfinityConstants.coreCustomerId).isJsonNull()
                        ? userDetails.get(InfinityConstants.coreCustomerId).getAsString()
                        : null;

        String legalEntityId = JSONUtil.hasKey(userDetails, "legalEntityId") ?
        		JSONUtil.getString(userDetails, "legalEntityId") : null;
        
        if (legalEntityId == null) {
        	dbxResult.setDbpErrCode(ErrorCodeEnum.ERR_29040.getErrorCodeAsString());
        	dbxResult.setDbpErrMsg(ErrorCodeEnum.ERR_29040.getMessage());
        	return dbxResult;
        }
		if (StringUtils.isNotBlank(coreCustomerId)
				&& (isProfileAlreadyExistsForCustomer(coreCustomerId, legalEntityId,headerMap))) {
			dbxResult.setDbpErrCode(ErrorCodeEnum.ERR_29057.getErrorCodeAsString());
			dbxResult.setDbpErrMsg(ErrorCodeEnum.ERR_29057.getMessage());
			return dbxResult;

		}   
        
        if (!createOrAddCustomerToUser(userDetails, headerMap, result, coreCustomerId, legalEntityId,false)) {
            return dbxResult;
        }

        String customerId = result.get(InfinityConstants.id).getAsString();

        String companyId = userDetails.has(InfinityConstants.companyId)
                && !userDetails.get(InfinityConstants.companyId).isJsonNull()
                        ? userDetails.get(InfinityConstants.companyId).getAsString()
                        : "";

        createCustomerPreference(customerId, headerMap, legalEntityId);
        createCustomerAction(customerId, jsonObject, headerMap, coreCustomerId, companyId, true, legalEntityId,false);

        return dbxResult;
    }
    
	private boolean isProfileAlreadyExistsForCustomer(String coreCustomerId, String legalEnityId,
			Map<String, Object> headerMap) {
		BackendIdentifierDTO backendidentifier = new BackendIdentifierDTO();
		backendidentifier.setBackendId(coreCustomerId);
		backendidentifier.setCompanyLegalUnit(legalEnityId);

		try {
			DBXResult dbxResult = DBPAPIAbstractFactoryImpl.getBackendDelegate(BackendIdentifiersBackendDelegate.class)
					.get(backendidentifier, headerMap);
			backendidentifier = (BackendIdentifierDTO) dbxResult.getResponse();
			if (backendidentifier != null)
				return true;
		} catch (ApplicationException e1) {
			alert.prepareError("Exception occured while fetching  backendidentifier ID", e1).log();
			return false;
		}

		return false;
	}

    private void createCustomerPreference(String customerId, Map<String, Object> headersMap, String legalEntityId) {
        CustomerPreferenceDTO customerPreferenceDTO = new CustomerPreferenceDTO();
        customerPreferenceDTO.setId(HelperMethods.getNewId());
        customerPreferenceDTO.setCustomer_id(customerId);
        customerPreferenceDTO.setIsNew(true);
        customerPreferenceDTO.setCompanyLegalUnit(legalEntityId);

        CustomerPreferenceBusinessDelegate customerPreferenceBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class)
                .getBusinessDelegate(CustomerPreferenceBusinessDelegate.class);

        customerPreferenceBusinessDelegate.update(customerPreferenceDTO, headersMap);

    }

    public void createCustomerAction(String customerId, JsonObject jsonObject, Map<String, Object> headerMap,
            String coreCustomerId, String companyId, boolean isDeleteRequired, String legalEntityId,boolean isExistingLegalEntity) {

        List<String> customerActionLimits = new ArrayList<String>();
        List<String> excludedCustomerActionLimits = new ArrayList<String>();
        Map<String, Map<String, Set<String>>> map = new HashMap<String, Map<String, Set<String>>>();
        Map<String, String> roleIds = new HashMap<String, String>();
        Map<String, String> cifPrimaryFlag = new HashMap<>();

        Map<String, Boolean> autoSyncAccountsMap = new HashMap<String, Boolean>();
        JsonArray companyList = jsonObject.has(InfinityConstants.companyList)
                && jsonObject.get(InfinityConstants.companyList).isJsonArray()
                        ? jsonObject.get(InfinityConstants.companyList).getAsJsonArray()
                        : new JsonArray();

        createCustomerAccounts(customerId, companyList, headerMap, map, roleIds, coreCustomerId, companyId,
                isDeleteRequired, autoSyncAccountsMap,cifPrimaryFlag, legalEntityId,isExistingLegalEntity);
        createCustomerContractsEntry(map, customerId, headerMap, autoSyncAccountsMap,cifPrimaryFlag, legalEntityId,isDeleteRequired);
        JsonArray accountLevelPermissions = jsonObject.has(InfinityConstants.accountLevelPermissions)
                && jsonObject.get(InfinityConstants.accountLevelPermissions).isJsonArray()
                        ? jsonObject.get(InfinityConstants.accountLevelPermissions).getAsJsonArray()
                        : new JsonArray();
        createAccountLevelPermissons(customerId, accountLevelPermissions, headerMap, map, roleIds, customerActionLimits,
                false, false);

        JsonArray excludedAccountLevelPermissions = jsonObject.has(InfinityConstants.excludedAccountLevelPermissions)
                && jsonObject.get(InfinityConstants.excludedAccountLevelPermissions).isJsonArray()
                        ? jsonObject.get(InfinityConstants.excludedAccountLevelPermissions).getAsJsonArray()
                        : new JsonArray();
        createAccountLevelPermissons(customerId, excludedAccountLevelPermissions, headerMap, map, roleIds,
        		excludedCustomerActionLimits, false,true);
        JsonArray globalLevelPermissions = jsonObject.has(InfinityConstants.globalLevelPermissions)
                && jsonObject.get(InfinityConstants.globalLevelPermissions).isJsonArray()
                        ? jsonObject.get(InfinityConstants.globalLevelPermissions).getAsJsonArray()
                        : new JsonArray();

        createGlobalLevelPermissons(customerId, globalLevelPermissions, headerMap, map, roleIds, customerActionLimits,
                false,false);
        JsonArray excludedGlobalLevelPermissions = jsonObject.has(InfinityConstants.excludedGlobalLevelPermissions)
                && jsonObject.get(InfinityConstants.excludedGlobalLevelPermissions).isJsonArray()
                        ? jsonObject.get(InfinityConstants.excludedGlobalLevelPermissions).getAsJsonArray()
                        : new JsonArray();

        createGlobalLevelPermissons(customerId, excludedGlobalLevelPermissions, headerMap, map, roleIds,
                excludedCustomerActionLimits, false, true);
        JsonArray transactionLimits = jsonObject.has(InfinityConstants.transactionLimits)
                && jsonObject.get(InfinityConstants.transactionLimits).isJsonArray()
                        ? jsonObject.get(InfinityConstants.transactionLimits).getAsJsonArray()
                        : new JsonArray();

        createTransactionLimits(customerId, transactionLimits, headerMap, map, roleIds, customerActionLimits, false, false);
        StringBuilder input = new StringBuilder("");
        int queries = customerActionLimits.size();
        if (queries > 0) {
            for (int query = 0; query < queries; query++) {
                String temp = customerActionLimits.get(query);
                if((input.length()+temp.length())>2000) {
                	
					if (input.charAt(input.length() - 1) == '|') {
						input.deleteCharAt(input.length()-1);
					}
                	Map<String, Object> inputParams = new HashMap<String, Object>();

                    inputParams.put(InfinityConstants._queryInput, input.toString());

                    HelperMethods.callApiAsync(inputParams, headerMap,
                            URLConstants.CUSTOMERACTIONLIMIT_SAVE_PROC);
                    
                    input = new StringBuilder("");
                }
                if (query < queries - 1)
                    input.append(temp + "|");
                else
                    input.append(temp);
            }
			if (input.length() > 0) {
				Map<String, Object> inputParams = new HashMap<String, Object>();

				inputParams.put(InfinityConstants._queryInput, input.toString());

				HelperMethods.callApiAsync(inputParams, headerMap, URLConstants.CUSTOMERACTIONLIMIT_SAVE_PROC);
			}
        }
        input = new StringBuilder("");
        queries = excludedCustomerActionLimits.size();
        if (queries > 0) {
            for (int query = 0; query < queries; query++) {
                String temp = excludedCustomerActionLimits.get(query);
                if((input.length()+temp.length())>2000) {
                	
					if (input.charAt(input.length() - 1) == '|') {
						input.deleteCharAt(input.length()-1);
					}
                	Map<String, Object> inputParams = new HashMap<String, Object>();

                    inputParams.put(InfinityConstants._queryInput, input.toString());

                    HelperMethods.callApiAsync(inputParams, headerMap,
                            URLConstants.CUSTOMERACTIONLIMIT_SAVE_PROC);
                    
                    input = new StringBuilder("");
                }
                if (query < queries - 1)
                    input.append(temp + "|");
                else
                    input.append(temp);
            }
			if (input.length() > 0) {
				Map<String, Object> inputParams = new HashMap<String, Object>();

				inputParams.put(InfinityConstants._queryInput, input.toString());

				HelperMethods.callApiAsync(inputParams, headerMap, URLConstants.EXCLUDEDCUSTOMERACTIONLIMIT_SAVE_PROC);
			}
        }
        JsonArray removedCompanies = jsonObject.has(InfinityConstants.removedCompanies)
                && jsonObject.get(InfinityConstants.removedCompanies).isJsonArray()
                        ? jsonObject.get(InfinityConstants.removedCompanies).getAsJsonArray()
                        : new JsonArray();
        removeCompanies(customerId, removedCompanies, headerMap, false, legalEntityId);
        checkAndSuspendUser(customerId, headerMap, legalEntityId);
    }

    private void checkAndSuspendUser(String customerId, Map<String, Object> headerMap, String legalEntityId) {
        JsonArray jsonArray = getUserContracts(customerId, null, null, headerMap, false, legalEntityId);
        if (jsonArray.size() <= 0) {
        	CustomerLegalEntityDTO customerLegalEntityDTO = new CustomerLegalEntityDTO();
            customerLegalEntityDTO.setCustomer_id(customerId);
            customerLegalEntityDTO.setLegalEntityId(legalEntityId);
            List<CustomerLegalEntityDTO> customerLegalEntityId = (List<CustomerLegalEntityDTO>) customerLegalEntityDTO.loadDTO();
            if (customerLegalEntityId != null  && customerLegalEntityId.size() > 0) {
            	customerLegalEntityDTO = customerLegalEntityId.get(0);
                customerLegalEntityDTO.setChanged(true);
                customerLegalEntityDTO.setStatus_id(InfinityConstants.SID_CUS_SUSPENDED);
                DTOUtils.persistObject(customerLegalEntityDTO, headerMap);
            }
            unSetDefaultLegalEntityIfRequired(customerId, legalEntityId, headerMap);
            checkIfCustomerSuspendRequired(customerId,headerMap);
        }
    }
    
    private void unSetDefaultLegalEntityIfRequired(String customerId, String legalEntityId,Map<String, Object> headerMap)
    {
    	CustomerDTO customerDTO = (CustomerDTO) new CustomerDTO().loadDTO(customerId);
		if (customerDTO != null && customerDTO.getDefaultLegalEntity() != null && customerDTO.getDefaultLegalEntity().equals(legalEntityId)) {
			customerDTO.setDefaultLegalEntity(null);
			customerDTO.setIsChanged(true);
			DTOUtils.persistObject(customerDTO, headerMap);
		}
    }
    
	private void checkIfCustomerSuspendRequired(String customerId, Map<String, Object> headerMap) {
		CustomerLegalEntityDTO customerLegalEntityDTO = new CustomerLegalEntityDTO();
		customerLegalEntityDTO.setCustomer_id(customerId);
		List<CustomerLegalEntityDTO> customerLegalEntityId = (List<CustomerLegalEntityDTO>) customerLegalEntityDTO
				.loadDTO();
		if (customerLegalEntityId != null && customerLegalEntityId.size() > 0) {
			Boolean isSuspended = false;
			for (CustomerLegalEntityDTO customerLegalEntity : customerLegalEntityId) {
				if (customerLegalEntity.getStatus_id().equals(InfinityConstants.SID_CUS_SUSPENDED)) {
					isSuspended = true;
				} else {
					isSuspended = false;
					break;
				}
			}
			if (isSuspended) {
				CustomerDTO customerDTO = (CustomerDTO) new CustomerDTO().loadDTO(customerId);
				if (customerDTO != null) {
					customerDTO.setStatus_id(InfinityConstants.SID_CUS_SUSPENDED);
					customerDTO.setIsChanged(true);
					DTOUtils.persistObject(customerDTO, headerMap);
				}
			}
		}
	}
     
    
    
    
     
    private void removeCompanies(String customerId, JsonArray removedCompanies, Map<String, Object> headerMap,
            boolean isCustomRoleFlow, String legalEntityId) {
        for (int i = 0; i < removedCompanies.size(); i++) {
            JsonObject cifJsonObject = removedCompanies.get(i).getAsJsonObject();
            String contractId = cifJsonObject.get(InfinityConstants.contractId).getAsString();
            String cif = cifJsonObject.get(InfinityConstants.cif).getAsString();
            removeDBEntries(customerId, cif, contractId, headerMap, isCustomRoleFlow, legalEntityId);
            createSuspendEntry(customerId, cif, contractId, headerMap, legalEntityId);
        }
    }

    private void createSuspendEntry(String customerId, String cif, String contractId, Map<String, Object> headerMap, String legalEntityId) {
        Map<String, Object> inputParams = new HashMap<String, Object>();
        inputParams.put(InfinityConstants.customerId, customerId);
        inputParams.put(InfinityConstants.contractId, contractId);
        inputParams.put(InfinityConstants.coreCustomerId, cif);
        inputParams.put(InfinityConstants.id, HelperMethods.getNewId());
        inputParams.put(InfinityConstants.LegalEntityId, legalEntityId);
        ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap, URLConstants.SUSPENDED_CUSTOMERS_CREATE);
    }

    private void removeDBEntries(String customerId, String cif, String contractId, Map<String, Object> headerMap,
            boolean isCustomRoleFlow, String legalEntityId) {
        Map<String, Object> inputParams = new HashMap<String, Object>();
        inputParams.put(InfinityConstants.customerId, customerId);
        inputParams.put(InfinityConstants.coreCustomerId, cif);
        inputParams.put(InfinityConstants.contractId, contractId);
        inputParams.put(InfinityConstants.legalEntityId, legalEntityId);

        if (!isCustomRoleFlow) {
            ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
                    URLConstants.CONTRACT_CUSTOMER_DELETE_PROC);
        } else {
            inputParams.put(InfinityConstants.customRoleId, customerId);
            inputParams.remove(InfinityConstants.customerId);
            ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
                    URLConstants.CONTRACT_CUSTOMROLE_DELETE_PROC);
        }
    }

    private void createTransactionLimits(String customerId, JsonArray transactionLimits, Map<String, Object> headerMap,
            Map<String, Map<String, Set<String>>> map, Map<String, String> roleIds, List<String> customerActionLimits,
            boolean isCustomRole, boolean isExcluded) {

        for (int i = 0; i < transactionLimits.size(); i++) {

            JsonObject transactionLimitsJsonObject = transactionLimits.get(i).getAsJsonObject();

            String cif = transactionLimitsJsonObject.get(InfinityConstants.cif).getAsString();
            String legalEntityId = transactionLimitsJsonObject.get(InfinityConstants.legalEntityId).getAsString();

            String contractId = getContractId(map, cif);
            String roleId = roleIds.get(cif);

            JsonArray accountsJsonArray = transactionLimitsJsonObject.get(InfinityConstants.accounts).getAsJsonArray();

            for (int j = 0; j < accountsJsonArray.size(); j++) {

                JsonObject accountJsonObject = accountsJsonArray.get(j).getAsJsonObject();
                String accountId = accountJsonObject.get(InfinityConstants.accountId).getAsString();

                JsonArray featurePermissions =
                        accountJsonObject.get(InfinityConstants.featurePermissions).getAsJsonArray();

                for (int k = 0; k < featurePermissions.size(); k++) {

                    JsonObject featureJsonObject = featurePermissions.get(k).getAsJsonObject();
                    String featureId = featureJsonObject.get(InfinityConstants.featureId).getAsString();
                    boolean isEnabled = true;
					if (featureJsonObject.has(InfinityConstants.isEnabled)
							&& !featureJsonObject.get(InfinityConstants.isEnabled).isJsonNull()) {
						isEnabled = Boolean
								.parseBoolean(featureJsonObject.get(InfinityConstants.isEnabled).getAsString());
					}
					if (!isEnabled) {
						accountsJsonArray.remove(j);
						j--;
						break;
					}
                    String limitGroupId = featureJsonObject.has(InfinityConstants.limitGroupId)
                            && !featureJsonObject.get(InfinityConstants.limitGroupId).isJsonNull()
                                    ? featureJsonObject.get(InfinityConstants.limitGroupId).getAsString()
                                    : null;

                    String actionId = featureJsonObject.get(InfinityConstants.actionId).getAsString();

                    JsonArray limits = featureJsonObject.has(InfinityConstants.limits)
                            && !featureJsonObject.get(InfinityConstants.limits).isJsonNull()
                                    ? featureJsonObject.get(InfinityConstants.limits).getAsJsonArray()
                                    : new JsonArray();
                    String isAllowed = "1";
                    for (int l = 0; l < limits.size(); l++) {
                        String id = limits.get(l).getAsJsonObject().get(InfinityConstants.id).getAsString();
                        String value = limits.get(l).getAsJsonObject().get(InfinityConstants.value).getAsString();
                        createCustomerAction(customerId, contractId, cif, featureId, actionId, accountId, isAllowed,
                                roleId, limitGroupId, id, value, customerActionLimits, isCustomRole, isExcluded, legalEntityId);
                    }
                    if (limits.size() == 0) {
                        createCustomerAction(customerId, contractId, cif, featureId, actionId, accountId, isAllowed,
                                roleId, null, null, null, customerActionLimits, isCustomRole, isExcluded, legalEntityId);
                    }
                }
            }

            JsonArray limitGroupsJsonArray = transactionLimitsJsonObject.has(InfinityConstants.limitGroups)
                    && !transactionLimitsJsonObject.get(InfinityConstants.limitGroups).isJsonNull()
                            ? transactionLimitsJsonObject.get(InfinityConstants.limitGroups).getAsJsonArray()
                            : new JsonArray();
            createCustomerLimitGroups(contractId, cif, customerId, limitGroupsJsonArray, headerMap, legalEntityId);
        }

    }

    private void createCustomerLimitGroups(String contractId, String cif, String customerId,
            JsonArray limitGroupsJsonArray, Map<String, Object> headerMap, String legalEntityId) {
        for (int j = 0; j < limitGroupsJsonArray.size(); j++) {
            JsonObject limitGroupsJsonObject = limitGroupsJsonArray.get(j).getAsJsonObject();
            String limitGroupId = limitGroupsJsonObject.has(InfinityConstants.limitGroupId)
                    && !limitGroupsJsonObject.get(InfinityConstants.limitGroupId).isJsonNull()
                            ? limitGroupsJsonObject.get(InfinityConstants.limitGroupId).getAsString()
                            : "";
            JsonArray limits = limitGroupsJsonObject.has(InfinityConstants.limits)
                    && !limitGroupsJsonObject.get(InfinityConstants.limits).isJsonNull()
                            ? limitGroupsJsonObject.get(InfinityConstants.limits).getAsJsonArray()
                            : new JsonArray();
            for (int l = 0; l < limits.size(); l++) {
                String id = limits.get(l).getAsJsonObject().has(InfinityConstants.id)
                        && !limits.get(l).getAsJsonObject().get(InfinityConstants.id).isJsonNull()
                                ? limits.get(l).getAsJsonObject().get(InfinityConstants.id).getAsString()
                                : "";
                String value = limits.get(l).getAsJsonObject().has(InfinityConstants.value)
                        && !limits.get(l).getAsJsonObject().get(InfinityConstants.value).isJsonNull()
                                ? limits.get(l).getAsJsonObject().get(InfinityConstants.value).getAsString()
                                : "0.0";
                if (StringUtils.isNotBlank(limitGroupId) && StringUtils.isNotBlank(id)) {
                    Map<String, Object> input = new HashMap<String, Object>();
                    input.put(InfinityConstants.id, HelperMethods.getNewId());
                    input.put(InfinityConstants.Customer_id, customerId);
                    input.put(InfinityConstants.contractId, contractId);
                    input.put(InfinityConstants.coreCustomerId, cif);
                    input.put(InfinityConstants.limitGroupId, limitGroupId);
                    input.put(InfinityConstants.LimitType_id, id);
                    input.put(InfinityConstants.value, value);
                    input.put(InfinityConstants.LegalEntityId, legalEntityId);
                    ServiceCallHelper.invokeServiceAndGetJson(input, headerMap,
                            URLConstants.CUSTOMER_LIMIT_GROUP_LIMITS_CREATE);
                }
            }
        }
    }

    private void createGlobalLevelPermissons(String customerId, JsonArray globalLevelPermissions,
            Map<String, Object> headerMap, Map<String, Map<String, Set<String>>> map, Map<String, String> roleIds,
            List<String> customerActionLimits, boolean isCustomRole, boolean isExcluded) {
    	alert.prepareError("createInfinityUser : createGlobalLevelPermissons called ").log();
        for (int i = 0; i < globalLevelPermissions.size(); i++) {

            JsonObject companyJsonObject = globalLevelPermissions.get(i).getAsJsonObject();

            String cif = companyJsonObject.get(InfinityConstants.cif).getAsString();
            String legalEntityId = companyJsonObject.get(InfinityConstants.legalEntityId).getAsString();

            String contractId = getContractId(map, cif);
            String roleId = roleIds.get(cif);

            JsonArray featuresJsonArray = companyJsonObject.get(InfinityConstants.features).getAsJsonArray();

            for (int j = 0; j < featuresJsonArray.size(); j++) {

                JsonObject featureJsonObject = featuresJsonArray.get(j).getAsJsonObject();

                String featureId = featureJsonObject.get(InfinityConstants.featureId).getAsString();

                JsonArray permissions = featureJsonObject.get(InfinityConstants.permissions).getAsJsonArray();
                
                Set<String> addedPermissions = new HashSet<String>();
                for (int k = 0; k < permissions.size(); k++) {

                    JsonObject permissionJsonObject = permissions.get(k).getAsJsonObject();
                    String actionId = "";

                    if (StringUtils.isBlank(actionId) && permissionJsonObject.has(InfinityConstants.actionId)
                    		&& !permissionJsonObject.get(InfinityConstants.actionId).isJsonNull()) {
                        actionId = permissionJsonObject.get(InfinityConstants.actionId).getAsString();
                    }

                    if (StringUtils.isBlank(actionId) && permissionJsonObject.has(InfinityConstants.id)
							&& !permissionJsonObject.get(InfinityConstants.id).isJsonNull()) {
                        actionId = permissionJsonObject.get(InfinityConstants.id).getAsString();
                    }

                    if (StringUtils.isBlank(actionId) && permissionJsonObject.has(InfinityConstants.permissionType)
							&& !permissionJsonObject.get(InfinityConstants.permissionType).isJsonNull()) {
                        actionId = permissionJsonObject.get(InfinityConstants.permissionType).getAsString();
                    }

					if (isExcluded && addedPermissions.contains(actionId))
                        continue;
                    addedPermissions.add(actionId);
                    String isAllowed = "1";
                    createCustomerAction(customerId, contractId, cif, featureId, actionId, null, isAllowed, roleId,
                            null, null, null, customerActionLimits, isCustomRole, isExcluded, legalEntityId);
                }
            }
        }

    }

    private void createAccountLevelPermissons(String customerId, JsonArray accountLevelPermissions,
            Map<String, Object> headerMap, Map<String, Map<String, Set<String>>> map, Map<String, String> roleIds,
            List<String> customerActionLimits, boolean isCustomRole, boolean isExcluded) {
    	alert.prepareError("createInfinityUser : createAccountLevelPermissons called ").log();
        for (int i = 0; i < accountLevelPermissions.size(); i++) {

            JsonObject cifJsonObject = accountLevelPermissions.get(i).getAsJsonObject();

            String cif = cifJsonObject.get(InfinityConstants.cif).getAsString();
            String legalEntityId = cifJsonObject.get(InfinityConstants.legalEntityId).getAsString();

            String contractId = getContractId(map, cif);
            String roleId = roleIds.get(cif);

            JsonArray accountsJsonArray = cifJsonObject.get(InfinityConstants.accounts).getAsJsonArray();

            for (int j = 0; j < accountsJsonArray.size(); j++) {

                JsonObject accountJsonObject = accountsJsonArray.get(j).getAsJsonObject();
                String accountId = accountJsonObject.get(InfinityConstants.accountId).getAsString();

                JsonArray featurePermissions =
                        accountJsonObject.get(InfinityConstants.featurePermissions).getAsJsonArray();

                for (int k = 0; k < featurePermissions.size(); k++) {

                    JsonObject featureJsonObject = featurePermissions.get(k).getAsJsonObject();
                    String featureId = featureJsonObject.get(InfinityConstants.featureId).getAsString();

                    JsonArray permissions = featureJsonObject.get(InfinityConstants.permissions).getAsJsonArray();
					Set<String> addedPermissions = new HashSet<String>();
                    for (int l = 0; l < permissions.size(); l++) {

                        JsonObject permissionJsonObject = permissions.get(l).getAsJsonObject();

                        String actionId = "";

                        if (permissionJsonObject.has(InfinityConstants.id)
									&& !permissionJsonObject.get(InfinityConstants.id).isJsonNull()) {
                            actionId = permissionJsonObject.get(InfinityConstants.id).getAsString();
                        }

                        if (StringUtils.isBlank(actionId) && permissionJsonObject.has(InfinityConstants.actionId)
								&& !permissionJsonObject.get(InfinityConstants.actionId).isJsonNull()) {
                            actionId = permissionJsonObject.get(InfinityConstants.actionId).getAsString();
                        }


						if (isExcluded && addedPermissions.contains(actionId))
                            continue;

                        addedPermissions.add(actionId);
                        String isAllowed = "1";
                        createCustomerAction(customerId, contractId, cif, featureId, actionId, accountId, isAllowed,
                                roleId, null, null, null, customerActionLimits, isCustomRole, isExcluded, legalEntityId);
                    }
                }
            }
        }
    }

    private void createCustomerAction(String customerId, String contractId, String cif, String featureId,
            String actionId, String accountId,
            String isAllowed, String roleId, String limitGroupId, String limitTypeId, String value,
            List<String> strings,
            boolean isCustomRole, boolean isExcluded, String legalEntityId) {
        StringBuilder query = new StringBuilder("");

        if (!isCustomRole) {
            query.append("\"" + roleId + "\",");
        }
        query.append("\"" + customerId + "\",");
        query.append("\"" + cif + "\",");
        query.append("\"" + contractId + "\",");
        query.append(featureId == null ? "null," : "\"" + featureId + "\",");
        query.append(actionId == null ? "null," : "\"" + actionId + "\",");
        query.append(accountId == null ? "null," : "\"" + accountId + "\",");
		if (!isExcluded) {
			query.append("\"" + isAllowed + "\",");
        	query.append(limitGroupId == null ? "null," : "\"" + limitGroupId + "\",");
        	query.append(limitTypeId == null ? "null," : "\"" + limitTypeId + "\",");
			value = StringUtils.isNotBlank(value) ? new BigDecimal(value).toPlainString() : null;
        	query.append(value == null ? "null," : "\"" + value + "\",");
		}
		query.append("\"" + legalEntityId + "\"");

        strings.add(query.toString());

    }

    private String getContractId(Map<String, Map<String, Set<String>>> map, String cif) {
        Set<Entry<String, Map<String, Set<String>>>> entries = map.entrySet();

        for (Entry<String, Map<String, Set<String>>> entry : entries) {
            if (entry.getValue().containsKey(cif)) {
                return entry.getKey();
            }
        }

        return null;

    }

    private void createCustomerContractsEntry(Map<String, Map<String, Set<String>>> map, String id,
            Map<String, Object> headerMap, Map<String, Boolean> autoSyncAccountsMap,
			Map<String, String> cifPrimaryFlag, String legalEntityId, boolean isDeleteRequired) {

    	/*Customer contract data got deleted in prevoius steps, so we have to create data*/
    	 if(!isDeleteRequired)
    		 return;
        Set<Entry<String, Map<String, Set<String>>>> entries = map.entrySet();
        for (Entry<String, Map<String, Set<String>>> entry : entries) {

            Set<Entry<String, Set<String>>> cifEntries = entry.getValue().entrySet();
            for (Entry<String, Set<String>> cifEntry : cifEntries) {

                ContractCustomersDTO contractcustomersDTO = new ContractCustomersDTO();
                contractcustomersDTO.setContractId(entry.getKey());
                contractcustomersDTO.setCoreCustomerId(cifEntry.getKey());
                contractcustomersDTO.setAutoSyncAccounts(autoSyncAccountsMap.get(cifEntry.getKey()));
				//contractcustomersDTO.setPrimary(Boolean.valueOf(cifPrimaryFlag.get(cifEntry.getKey())));
                List<ContractCustomersDTO> contractcustomersDTOs =
                        (List<ContractCustomersDTO>) contractcustomersDTO.loadDTO();

                if (contractcustomersDTOs.size() == 0) {
                    contractcustomersDTO.setPrimary(true);
                }

                contractcustomersDTO.setId(HelperMethods.getNewId());

                contractcustomersDTO.setCustomerId(id);
                contractcustomersDTO.setCompanyLegalUnit(legalEntityId);
               
                DTOUtils.persistObject(contractcustomersDTO, headerMap);
                
            }
        }
    }

    private void createBackendIdentifierEntry(String id, String coreCustomerId, String contractId,
            Map<String, Object> headerMap, String companyId, String legalEntityId) {
        if (StringUtils.isBlank(coreCustomerId)) {
            return;
        }

        String filter = InfinityConstants.id + DBPUtilitiesConstants.EQUAL + contractId;
        Map<String, Object> input = new HashMap<String, Object>();
        input.put(DBPUtilitiesConstants.FILTER, filter);
        JsonObject jsonObject = ServiceCallHelper.invokeServiceAndGetJson(input, headerMap, URLConstants.CONTRACT_GET);
        input.clear();
        String contractTypeId = "";
        if (jsonObject.has(DBPDatasetConstants.DATASET_CONTRACT)) {
            JsonElement jsonElement = jsonObject.get(DBPDatasetConstants.DATASET_CONTRACT);
            if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
                JsonArray jsonArray = jsonElement.getAsJsonArray();
                jsonObject = jsonArray.get(0).getAsJsonObject();
                contractTypeId = jsonObject.get(InfinityConstants.serviceType).getAsString();
            }
        }

        BackendIdentifierDTO backendIdentifierDTO = new BackendIdentifierDTO();
        backendIdentifierDTO.setId(UUID.randomUUID().toString());
        backendIdentifierDTO.setCustomer_id(id);
        backendIdentifierDTO.setBackendId(coreCustomerId);
        backendIdentifierDTO.setBackendType(DTOConstants.CORE);
        backendIdentifierDTO.setSequenceNumber("1");
        backendIdentifierDTO.setContractId(contractId);
        backendIdentifierDTO.setIdentifier_name("customer_id");
        backendIdentifierDTO.setContractTypeId(contractTypeId);
        backendIdentifierDTO.setCompanyId(legalEntityId);
        backendIdentifierDTO.setCompanyLegalUnit(legalEntityId);
        backendIdentifierDTO.setCompanyId(legalEntityId);
        input = DTOUtils.getParameterMap(backendIdentifierDTO, true);
        backendIdentifierDTO.setIsNew(true);
        input.put("CompanyId", legalEntityId);
        backendIdentifierDTO.persist(input, headerMap);
        final String IS_Integrated = Boolean.toString(IntegrationTemplateURLFinder.isIntegrated);
        backendIdentifierDTO = new BackendIdentifierDTO();
        if (StringUtils.isNotBlank(IS_Integrated) && IS_Integrated.equalsIgnoreCase("true")) {
            backendIdentifierDTO.setIsNew(true);
            backendIdentifierDTO.setId(UUID.randomUUID().toString());
            backendIdentifierDTO
                    .setBackendType(IntegrationTemplateURLFinder.getBackendURL(InfinityConstants.BackendType));
            backendIdentifierDTO.setIdentifier_name(
                    IntegrationTemplateURLFinder.getBackendURL(InfinityConstants.BackendCustomerIdentifierName));
            backendIdentifierDTO.setCustomer_id(id);
            backendIdentifierDTO.setBackendId(coreCustomerId);
            backendIdentifierDTO.setContractTypeId(contractTypeId);
            backendIdentifierDTO.setContractId(contractId);
            backendIdentifierDTO.setSequenceNumber("1");
            backendIdentifierDTO.setCompanyId(legalEntityId);
            backendIdentifierDTO.setCompanyLegalUnit(legalEntityId);
            backendIdentifierDTO.setCompanyId(legalEntityId);
            input = DTOUtils.getParameterMap(backendIdentifierDTO, true);
            input.put("CompanyId", legalEntityId);
            backendIdentifierDTO.persist(input, headerMap);
        }
    }

    private void createCustomerAccounts(String id, JsonArray companyList, Map<String, Object> headerMap,
            Map<String, Map<String, Set<String>>> map, Map<String, String> roleIds, String coreCustomerId,
            String companyId, boolean isDeleteRequired, Map<String, Boolean> autoSyncAccountsMap,
			Map<String, String> cifPrimaryFlag, String legalEntityID,boolean isExistingLegalEntity) {

        boolean backendIdentifierEntryMade = false;
        
        for (int i = 0; i < companyList.size(); i++) {

            JsonObject cifJsonObject = companyList.get(i).getAsJsonObject();
            String contractId = cifJsonObject.get(InfinityConstants.contractId).getAsString();
            String cif = cifJsonObject.get(InfinityConstants.cif).getAsString();
            String userRole = cifJsonObject.get(InfinityConstants.roleId).getAsString();
      		String isPrimary = cifJsonObject.get(InfinityConstants.isPrimary).getAsString();
            cifPrimaryFlag.put(cif, isPrimary);
            boolean autoSyncAccounts =
                    Boolean.parseBoolean(cifJsonObject.get(InfinityConstants.autoSyncAccounts).getAsString());
            roleIds.put(cif, userRole);
            autoSyncAccountsMap.put(cif, autoSyncAccounts);
            JsonArray accounts = cifJsonObject.get(InfinityConstants.accounts).getAsJsonArray();
            JsonArray excludedAccountsList = cifJsonObject.has(InfinityConstants.excludedAccounts)
                    && !cifJsonObject.get(InfinityConstants.excludedAccounts).isJsonNull()
                            ? cifJsonObject.get(InfinityConstants.excludedAccounts).getAsJsonArray()
                            : new JsonArray();

            String legalEntityId = cifJsonObject.get(InfinityConstants.legalEntityId).getAsString();
            Set<String> set = new HashSet<String>();
            Map<String, Object> input;
            if (StringUtils.isNotBlank(coreCustomerId) && coreCustomerId.equals(cif) && !isExistingLegalEntity) {
                createBackendIdentifierEntry(id, coreCustomerId, contractId, headerMap,
                        companyId, legalEntityID);
                backendIdentifierEntryMade = true;
            }

            if (isDeleteRequired) {
                removeDBEntries(id, cif, contractId, headerMap, false, legalEntityId);
            }
            processAccounts(accounts,id,cif,contractId,headerMap,legalEntityId,set,isDeleteRequired);
            processExcludedAccounts(excludedAccountsList,id,cif,contractId,headerMap,legalEntityId,set,isDeleteRequired);
            if (isDeleteRequired) {
                createCustomerGroup(id, contractId, cif, userRole, headerMap, legalEntityId);
            }

            if (!map.containsKey(contractId)) {
                map.put(contractId, new HashMap<String, Set<String>>());
            }
            if (!map.get(contractId).containsKey(cif)) {
                map.get(contractId).put(cif, set);
            }

        }

        if (StringUtils.isNotBlank(coreCustomerId) && !backendIdentifierEntryMade && !isExistingLegalEntity) {
            createBackendIdentifierEntry(id, coreCustomerId, null, headerMap,
                    companyId, legalEntityID);
        }
    }
    
	private static void processAccounts(JsonArray accounts, String id, String cif, String contractId,
			Map<String, Object> headerMap, String legalEntityId, Set<String> accountSet, boolean isDeleteRequired) {

		if (accounts == null || accounts.size() == 0)
			return;
		Set<String> existingAccountsSet = new HashSet<String>();
		/* Reading existing accounts from database */
		String filter = InfinityConstants.Customer_id + DBPUtilitiesConstants.EQUAL + id + DBPUtilitiesConstants.AND
				+ InfinityConstants.contractId + DBPUtilitiesConstants.EQUAL + contractId + DBPUtilitiesConstants.AND
				+ InfinityConstants.coreCustomerId + DBPUtilitiesConstants.EQUAL + cif + DBPUtilitiesConstants.AND
				+ InfinityConstants.LegalEntityId + DBPUtilitiesConstants.EQUAL + legalEntityId;
		Map<String, Object> input = new HashMap<>();
		input.put(DBPUtilitiesConstants.FILTER, filter);
		JsonObject existingAccounts = ServiceCallHelper.invokeServiceAndGetJson(input, headerMap,
				URLConstants.CUSTOMERACCOUNTS_GET);
		if (existingAccounts.has(DBPDatasetConstants.DATASET_CUSTOMERACCOUNTS)) {
			JsonElement jsonElement = existingAccounts.get(DBPDatasetConstants.DATASET_CUSTOMERACCOUNTS);
			if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
				JsonArray jsonArray = jsonElement.getAsJsonArray();
				for (int i = 0; i < jsonArray.size(); i++) {
					JsonObject existingAccountRecord = jsonArray.get(i).getAsJsonObject();
					if (existingAccountRecord.has(InfinityConstants.Account_id)) {
						existingAccountsSet.add(existingAccountRecord.get(InfinityConstants.Account_id).getAsString());
					}
				}
			}
		}
		/*
		 * Calculating newly added accounts and deleted accounts using Payload accounts
		 * data and existing database accounts records
		 */
		Set<String> addedAccounts = new HashSet<>();
		for (int j = 0; j < accounts.size(); j++) {
			JsonObject jsonObject = accounts.get(j).getAsJsonObject();
			String accountId = JSONUtil.getString(jsonObject, InfinityConstants.accountId);
			if (existingAccountsSet.contains(accountId)) {
				existingAccountsSet.remove(accountId);
			} else {
				addedAccounts.add(accountId);
			}
		}
		/* creating customeraccounts entry for newly added accounts */
		createCustomerAccounts(addedAccounts, accounts, id, contractId, cif, headerMap, legalEntityId, accountSet);
		/* removing customeraccounts table entries for deletedaccounts */
		if (isDeleteRequired) {
			deleteRemovedCustomerAccounts(existingAccountsSet, accounts, id, contractId, cif, legalEntityId, headerMap);
		}
	}

	private static void deleteRemovedCustomerAccounts(Set<String> deletedAccountsSet, JsonArray accounts, String id,
			String contractId, String cif, String legalEntityId, Map<String, Object> headerMap) {
		
		if (deletedAccountsSet == null || deletedAccountsSet.size() == 0)
			return;
		Map<String, Object> input = new HashMap<>();
		input.put("_customerId", id);
		input.put("_coreCustomerId", cif);
		input.put("_contractId", contractId);
		input.put("_legalEntityId", legalEntityId);
		input.put("_accountIdList", String.join(",", deletedAccountsSet));

		ServiceCallHelper.invokeServiceAndGetJson(input, headerMap, URLConstants.CUSTOMERACCOUNTS_DELETE_PROC);
		
		

	}

	private static void createCustomerAccounts(Set<String> addedAccounts, JsonArray accounts, String id,
			String contractId, String cif, Map<String, Object> headerMap, String legalEntityId,
			Set<String> accountSet) {
		if (accounts == null || accounts.size() == 0)
			return;
		for (int j = 0; j < accounts.size(); j++) {
			JsonObject jsonObject = accounts.get(j).getAsJsonObject();
			Map<String, Object> input = new HashMap<>();
			String accountName = JSONUtil.getString(jsonObject, InfinityConstants.accountName);
			String accountId = JSONUtil.getString(jsonObject, InfinityConstants.accountId);
			if(!addedAccounts.contains(accountId))
				continue;
			String accountType = JSONUtil.getString(jsonObject, InfinityConstants.accountType);
			accountSet.add(accountId);
			input.put(InfinityConstants.id, HelperMethods.getNewId());
			input.put(InfinityConstants.contractId, contractId);
			input.put(InfinityConstants.Customer_id, id);
			input.put(InfinityConstants.coreCustomerId, cif);
			input.put(InfinityConstants.Account_id, accountId);
			input.put(InfinityConstants.AccountName, accountName);
			input.put(InfinityConstants.accountType, accountType);
			input.put(InfinityConstants.LegalEntityId, legalEntityId);

			ServiceCallHelper.invokeServiceAndGetJson(input, headerMap, URLConstants.CUSTOMERACCOUNTS_CREATE);
		}
	}

	private static void processExcludedAccounts(JsonArray excludedAccounts, String id, String cif, String contractId,
			Map<String, Object> headerMap, String legalEntityId, Set<String> accountSet, boolean isDeleteRequired) {
		/*if (excludedAccounts == null || excludedAccounts.size() == 0)
			return;*/
		/* Reading existing accounts from database */
		HashMap<String, String> customerAccountMap = new HashMap<>();
		Set<String> existingAccountsSet = new HashSet<>();
		String filter = InfinityConstants.Customer_id + DBPUtilitiesConstants.EQUAL + id + DBPUtilitiesConstants.AND
				+ InfinityConstants.contractId + DBPUtilitiesConstants.EQUAL + contractId + DBPUtilitiesConstants.AND
				+ InfinityConstants.coreCustomerId + DBPUtilitiesConstants.EQUAL + cif + DBPUtilitiesConstants.AND
				+ InfinityConstants.LegalEntityId + DBPUtilitiesConstants.EQUAL + legalEntityId;
		Map<String, Object> input = new HashMap<>();
		input.put(DBPUtilitiesConstants.FILTER, filter);
		JsonObject existingAccounts = ServiceCallHelper.invokeServiceAndGetJson(input, headerMap,
				URLConstants.EXCLUDEDCUSTOMERACCOUNTS_GET);
		if (existingAccounts.has(DBPDatasetConstants.DATASET_EXCLUDED_CUSTOMERACCOUNTS)) {
			JsonElement jsonElement = existingAccounts.get(DBPDatasetConstants.DATASET_EXCLUDED_CUSTOMERACCOUNTS);
			if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
				JsonArray jsonArray = jsonElement.getAsJsonArray();
				for (int i = 0; i < jsonArray.size(); i++) {
					JsonObject existingAccountRecord = jsonArray.get(i).getAsJsonObject();
					if (existingAccountRecord.has(InfinityConstants.Account_id)) {
						existingAccountsSet.add(existingAccountRecord.get(InfinityConstants.Account_id).getAsString());
					}
				}
			}
		}
		/*
		 * Calculating newly added accounts and deleted accounts using Payload accounts
		 * data and existing database accounts records
		 */
		Set<String> addedAccounts = new HashSet<>();
		for (int j = 0; j < excludedAccounts.size(); j++) {
			JsonObject jsonObject = excludedAccounts.get(j).getAsJsonObject();
			String accountId = JSONUtil.getString(jsonObject, InfinityConstants.accountId);
			if (existingAccountsSet.contains(accountId)) {
				existingAccountsSet.remove(accountId);
			} else {
				addedAccounts.add(accountId);
			}
		}
		/* creating excludedcustomeraccounts entry for newly added accounts */
		
		createExcludedCustomerAccounts(addedAccounts, excludedAccounts, id, contractId, cif, headerMap, legalEntityId,
				accountSet, customerAccountMap);
		
		UnsubscribeAlertsForRemovedAccounts.asyncUnsubscribeAlerts(customerAccountMap);
		/* removing excludedcustomeraccounts table entries for deletedaccounts */
		if (isDeleteRequired) {
			deleteRemovedExcludedCustomerAccounts(existingAccountsSet, excludedAccounts, id, contractId, cif,
					legalEntityId, headerMap);
		}
	}

	private static void deleteRemovedExcludedCustomerAccounts(Set<String> deletingAccountsSet,
			JsonArray excludedAccounts, String id, String contractId, String cif, String legalEntityId,
			Map<String, Object> headerMap) {
		if (deletingAccountsSet == null || deletingAccountsSet.isEmpty())
			return;
		Map<String, Object> input = new HashMap<>();
		input.put("_customerId", id);
		input.put("_coreCustomerId", cif);
		input.put("_contractId", contractId);
		input.put("_legalEntityId", legalEntityId);
		input.put("_accountIdList", String.join(",", deletingAccountsSet));
		ServiceCallHelper.invokeServiceAndGetJson(input, headerMap, URLConstants.EXCLUDEDCUSTOMERACCOUNTS_DELETE_PROC);
	}

	private static void createExcludedCustomerAccounts(Set<String> addedAccounts, JsonArray excludedAccounts, String id,
			String contractId, String cif, Map<String, Object> headerMap, String legalEntityId,
			Set<String> accountSet, HashMap<String, String> customerAccountMap) {
		if (excludedAccounts == null || excludedAccounts.size() == 0)
			return;

		for (int j = 0; j < excludedAccounts.size(); j++) {
			JsonObject jsonObject = excludedAccounts.get(j).getAsJsonObject();
			Map<String, Object> input = new HashMap<>();
			String accountName = JSONUtil.getString(jsonObject, InfinityConstants.accountName);
			String accountId = JSONUtil.getString(jsonObject, InfinityConstants.accountId);
			String accountType = JSONUtil.getString(jsonObject, InfinityConstants.accountType);
			if (!addedAccounts.contains(accountId))
				continue;
			accountSet.add(accountId);
			customerAccountMap.put(id, accountId);
			input.put(InfinityConstants.id, HelperMethods.getNewId());
			input.put(InfinityConstants.contractId, contractId);
			input.put(InfinityConstants.Customer_id, id);
			input.put(InfinityConstants.coreCustomerId, cif);
			input.put(InfinityConstants.Account_id, accountId);
			input.put(InfinityConstants.AccountName, accountName);
			input.put(InfinityConstants.accountType, accountType);
			input.put(InfinityConstants.LegalEntityId, legalEntityId);
			ServiceCallHelper.invokeServiceAndGetJson(input, headerMap, URLConstants.EXCLUDEDCUSTOMERACCOUNTS_CREATE);
		}

	}

    private void createCustomerGroup(String id, String contractId, String cif, String userRole,
            Map<String, Object> headerMap, String legalEntityId) {
        Map<String, Object> input = new HashMap<String, Object>();
        input.put(InfinityConstants.Customer_id, id);
        input.put(InfinityConstants.contractId, contractId);
        input.put(InfinityConstants.coreCustomerId, cif);
        input.put(InfinityConstants.Group_id, userRole);
        input.put(InfinityConstants.LegalEntityId, legalEntityId);
        ServiceCallHelper.invokeServiceAndGetJson(input, headerMap, URLConstants.CUSTOMER_GROUP_CREATE);
    }

    private boolean validateRegex(String regex, String string) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(string);
        return m.matches();
    }

	private boolean createOrAddCustomerToUser(JsonObject userDetails, Map<String, Object> headerMap, JsonObject result,
			String coreCustomerId, String legalEntityId, boolean isExistingLegalEntity) {

		CustomerDTO coreCustomerDTO = null;
		if (!IntegrationTemplateURLFinder.isIntegrated && StringUtils.isNotBlank(coreCustomerId)) {
			coreCustomerDTO = new CustomerDTO();
			coreCustomerDTO.setId(coreCustomerId);
			coreCustomerDTO = (CustomerDTO) coreCustomerDTO.loadDTO();
		}

		String id = userDetails.has(InfinityConstants.id) && !userDetails.get(InfinityConstants.id).isJsonNull()
				? userDetails.get(InfinityConstants.id).getAsString()
				: null;

		CustomerLegalEntityDTO customerLegalEntityDTO = null;
		/*editinfinityuser in adduserid flow with existing user*/
		if (StringUtils.isNotBlank(id) && isExistingLegalEntity) {
			result.addProperty(InfinityConstants.id, id);
			return true;
		}
		/*Simple editinfinityuser*/
		if (StringUtils.isNotBlank(id) && coreCustomerId == null) {
			result.addProperty(InfinityConstants.id, id);
			return true;
		}
		
		if (StringUtils.isNotBlank(id) && coreCustomerId !=null &&isExistingLegalEntity) {
			result.addProperty(InfinityConstants.id, id);
			return true;
		}
		

		String email = JSONUtil.getString(userDetails, InfinityConstants.email);
		String phoneCountryCode = JSONUtil.getString(userDetails, "phoneCountryCode");
		String phoneNumber = JSONUtil.getString(userDetails, "phoneNumber");
		
		if (StringUtils.isNotBlank(id) && coreCustomerId!=null && !isExistingLegalEntity) {
			/*This is editinfinityuser flow, where customers from different legal entity added to user*/
			String status = getUserExitingStatus(id);
			customerLegalEntityDTO = new CustomerLegalEntityDTO();
			customerLegalEntityDTO.setId(HelperMethods.getNewId());
			customerLegalEntityDTO.setCustomer_id(id);
			customerLegalEntityDTO.setNew(true);
			customerLegalEntityDTO.setStatus_id(status);
			customerLegalEntityDTO.setLegalEntityId(legalEntityId);
			customerLegalEntityDTO.persist(DTOUtils.getParameterMap(customerLegalEntityDTO, true), headerMap);
			if (!IntegrationTemplateURLFinder.isIntegrated || StringUtils.isBlank(coreCustomerId)) {
				createCommunicationData(legalEntityId, id, phoneCountryCode, phoneNumber, email, headerMap,coreCustomerId);
			}
			result.addProperty(InfinityConstants.id, id);
			return true;
		}

		else {
			/*This is createinfinityuser flow*/
			CustomerDTO customerDTO = new CustomerDTO();
			customerDTO.setId(HelperMethods.getUniqueNumericString(8));
			customerDTO.setUserName(customerDTO.getId());
			String firstName = JSONUtil.getString(userDetails, InfinityConstants.firstName);
			String lastName = JSONUtil.getString(userDetails, InfinityConstants.lastName);
			String middleName = JSONUtil.getString(userDetails, InfinityConstants.middleName);
			String ssn = JSONUtil.getString(userDetails, InfinityConstants.ssn);
			String dob = JSONUtil.getString(userDetails, InfinityConstants.dob);
			String drivingLicense = JSONUtil.getString(userDetails, InfinityConstants.drivingLicenseNumber);

			customerDTO.setFirstName(StringUtils.isNotBlank(firstName) ? firstName : null);
			customerDTO.setLastName(StringUtils.isNotBlank(lastName) ? lastName : null);
			customerDTO.setMiddleName(StringUtils.isNotBlank(middleName) ? middleName : null);
			customerDTO.setDateOfBirth(StringUtils.isNotBlank(dob) ? dob : null);
			customerDTO.setCompanyLegalUnit(legalEntityId);
			customerDTO.setHomeLegalEntity(legalEntityId);
			if (LegalEntityUtil.isSingleEntity()) {
				customerDTO.setDefaultLegalEntity(legalEntityId);
			}
			if (StringUtils.isNotBlank(phoneCountryCode))
				phoneCountryCode = phoneCountryCode.trim();
			if (!phoneCountryCode.contains("+"))
				phoneCountryCode = "+" + phoneCountryCode;
			if (coreCustomerDTO == null) {
				customerDTO.setSsn(StringUtils.isNotBlank(ssn) ? ssn : null);
			} else {
				customerDTO.setSsn(coreCustomerDTO != null ? coreCustomerDTO.getSsn() : null);
			}
			customerDTO.setDrivingLicenseNumber(StringUtils.isNotBlank(drivingLicense) ? drivingLicense : null);
			customerDTO.setStatus_id(HelperMethods.getCustomerStatus().get("NEW"));
			customerDTO.setIsNew(true);
			customerDTO.setIsEnrolledFromSpotlight("1");
			if (DTOUtils.persistObject(customerDTO, headerMap)) {
				if (!IntegrationTemplateURLFinder.isIntegrated || StringUtils.isBlank(coreCustomerId)) {
					createCommunicationData(legalEntityId, customerDTO.getId(), phoneCountryCode, phoneNumber, email, headerMap,coreCustomerId);
				}
				if (!IntegrationTemplateURLFinder.isIntegrated && StringUtils.isNotBlank(coreCustomerId)) {
					String customerId = customerDTO.getId();
					CustomerAddressDTO customerAddressDTO = new CustomerAddressDTO();
					customerAddressDTO.setCustomer_id(coreCustomerId);
					List<CustomerAddressDTO> corecustomerAddresses = (List<CustomerAddressDTO>) customerAddressDTO
							.loadDTO();
					for (CustomerAddressDTO corecustomerAddressDTO : corecustomerAddresses) {
						corecustomerAddressDTO.setIsdeleted(true);
						corecustomerAddressDTO.setIsChanged(false);
						corecustomerAddressDTO.setIsNew(false);
						DTOUtils.persistObject(corecustomerAddressDTO, headerMap);
						customerAddressDTO = new CustomerAddressDTO();
						customerAddressDTO.setIsNew(true);
						customerAddressDTO.setCustomer_id(customerId);
						customerAddressDTO.setAddress_id(corecustomerAddressDTO.getAddress_id());
						customerAddressDTO.setType_id(corecustomerAddressDTO.getType_id());
						customerAddressDTO.setIsPrimary(corecustomerAddressDTO.getIsPrimary());
						customerAddressDTO.setCompanyLegalUnit(legalEntityId);
						DTOUtils.persistObject(customerAddressDTO, headerMap);
					}

					CustomerCommunicationDTO customerCommunicationDTO = new CustomerCommunicationDTO();
					customerCommunicationDTO.setCustomer_id(coreCustomerId);
					List<CustomerCommunicationDTO> customerCommunicationDTOs = (List<CustomerCommunicationDTO>) customerCommunicationDTO
							.loadDTO();
					for (CustomerCommunicationDTO customerCommunication : customerCommunicationDTOs) {
						customerCommunication.setIsdeleted(true);
						customerCommunication.setIsChanged(false);
						customerCommunication.setIsNew(false);
						DTOUtils.persistObject(customerCommunication, headerMap);
					}
					if (coreCustomerDTO != null) {
						coreCustomerDTO.setUserName(coreCustomerDTO.getUserName() + 1);
						coreCustomerDTO.setLastName(coreCustomerDTO.getFirstName());
						coreCustomerDTO.setSsn(coreCustomerDTO.getSsn() + 1);
						coreCustomerDTO.setDateOfBirth(HelperMethods.getCurrentDate());
						coreCustomerDTO.setIsChanged(true);
						coreCustomerDTO.setCompanyLegalUnit(legalEntityId);
						coreCustomerDTO.persist(DTOUtils.getParameterMap(coreCustomerDTO, true), headerMap);
					}
				}
				
				customerLegalEntityDTO = new CustomerLegalEntityDTO();
				customerLegalEntityDTO.setId(HelperMethods.getNewId());
				customerLegalEntityDTO.setCustomer_id(customerDTO.getId());
				customerLegalEntityDTO.setNew(true);
				customerLegalEntityDTO.setStatus_id(HelperMethods.getCustomerStatus().get("NEW"));
				customerLegalEntityDTO.setLegalEntityId(legalEntityId);
				customerLegalEntityDTO.persist(DTOUtils.getParameterMap(customerLegalEntityDTO, true), headerMap);

				result.addProperty(InfinityConstants.id, customerDTO.getId());
			} else {
				ErrorCodeEnum.ERR_10050.setErrorCode(result);
				return false;
			}

			return true;
		}
	}

	private String getUserExitingStatus(String userid) {
		try {
			HelperMethods.getCustomerStatus().get("NEW");
			CustomerLegalEntityDTO custLegDTO = new CustomerLegalEntityDTO();
			custLegDTO.setCustomer_id(userid);
			List<CustomerLegalEntityDTO> legalEntities = (List<CustomerLegalEntityDTO>) custLegDTO.loadDTO();
			if (legalEntities != null) {
				for (CustomerLegalEntityDTO custLe : legalEntities) {
					if (custLe.getStatus_id().equals(HelperMethods.getCustomerStatus().get("NEW"))) {
						return HelperMethods.getCustomerStatus().get("NEW");
					}
				}
			}
		} catch (Exception e) {
			alert.prepareError("Error in getting Legal entity", e).log();
		}
		return HelperMethods.getCustomerStatus().get("ACTIVE");

	}
	private void createCommunicationData(String legalEntityId, String customerid,
			String phoneCountryCode, String phoneNumber, String email, Map<String, Object> headerMap,String coreCustomerId) {
		CustomerCommunicationDTO phoneCommunicationDTO =new CustomerCommunicationDTO();
		CustomerCommunicationDTO emailCommunicationDTO = new CustomerCommunicationDTO();
		if (StringUtils.isNotBlank(phoneCountryCode))
			phoneCountryCode = phoneCountryCode.trim();
		if (!phoneCountryCode.contains("+"))
			phoneCountryCode = "+" + phoneCountryCode;
		phoneCommunicationDTO.setId(HelperMethods.getNewId());

		phoneCommunicationDTO.setPhoneCountryCode(phoneCountryCode);
		phoneCommunicationDTO.setValue(phoneCountryCode + "-" + phoneNumber);
		phoneCommunicationDTO.setCompanyLegalUnit(legalEntityId);
		emailCommunicationDTO.setId(HelperMethods.getNewId());
		emailCommunicationDTO.setValue(email);
		emailCommunicationDTO.setCompanyLegalUnit(legalEntityId);

		phoneCommunicationDTO.setCustomer_id(customerid);
		emailCommunicationDTO.setCustomer_id(customerid);
		phoneCommunicationDTO.setExtension(InfinityConstants.Mobile);
		emailCommunicationDTO.setExtension(InfinityConstants.Personal);
		phoneCommunicationDTO.setType_id(HelperMethods.getCommunicationTypes().get("Phone"));
		emailCommunicationDTO.setType_id(HelperMethods.getCommunicationTypes().get("Email"));
		phoneCommunicationDTO.setIsPrimary(false);
		emailCommunicationDTO.setIsPrimary(false);
		if (StringUtils.isBlank(coreCustomerId))
		{
			phoneCommunicationDTO.setIsPrimary(true);
			emailCommunicationDTO.setIsPrimary(true);
		}
		phoneCommunicationDTO.setIsNew(true);
		emailCommunicationDTO.setIsNew(true);
		DTOUtils.persistObject(phoneCommunicationDTO, headerMap);
		DTOUtils.persistObject(emailCommunicationDTO, headerMap);

	}
	@Override
	public DBXResult editInfinityUser(JsonObject jsonObject, Map<String, Object> headerMap) throws ApplicationException {
		
		DBXResult dbxResult = new DBXResult();
		JsonObject result = new JsonObject();
		dbxResult.setResponse(result);
		JsonObject userDetails = jsonObject.has(InfinityConstants.userDetails)
				&& jsonObject.get(InfinityConstants.userDetails).isJsonObject()
						? jsonObject.get(InfinityConstants.userDetails).getAsJsonObject()
						: new JsonObject();
		String coreCustomerId = userDetails.has(InfinityConstants.coreCustomerId)
				&& !userDetails.get(InfinityConstants.coreCustomerId).isJsonNull()
						? userDetails.get(InfinityConstants.coreCustomerId).getAsString()
						: null;

		String legalEntityId = JSONUtil.hasKey(userDetails, "legalEntityId")
				? JSONUtil.getString(userDetails, "legalEntityId")
				: null;
		String id = userDetails.has(InfinityConstants.id) && !userDetails.get(InfinityConstants.id).isJsonNull()
				? userDetails.get(InfinityConstants.id).getAsString()
				: null;
		boolean isExistingLegalEntity = checkIsExistingLegalEntity(id, legalEntityId);
		if (isExistingLegalEntity && coreCustomerId!=null) {
			throw new ApplicationException(ErrorCodeEnum.ERR_12466);
		}
		
		createOrAddCustomerToUser(userDetails, headerMap, result, coreCustomerId, legalEntityId,isExistingLegalEntity);

		if (StringUtils.isNotBlank(id) && userDetails != null)
			updateCustomer(userDetails, headerMap);

        String customerId = userDetails.get(InfinityConstants.id).getAsString();
        createCustomerAction(customerId, jsonObject, headerMap, coreCustomerId, null, true, legalEntityId,isExistingLegalEntity);
        result.addProperty(InfinityConstants.status, "success");
        return dbxResult;
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
	private void updateCustomer(JsonObject userDetails, Map<String, Object> headerMap) {
		CustomerDTO customerDTO = new CustomerDTO();

		customerDTO.setId(userDetails.get(InfinityConstants.id).getAsString());
		customerDTO = (CustomerDTO) customerDTO.loadDTO();

		if (customerDTO == null) {
			return;
		}

		String filter = InfinityConstants.Customer_id + DBPUtilitiesConstants.EQUAL + customerDTO.getId()
				+ DBPUtilitiesConstants.AND + InfinityConstants.isPrimary + DBPUtilitiesConstants.EQUAL + "1";

		Map<String, Object> map = new HashMap<String, Object>();
		map.put(DBPUtilitiesConstants.FILTER, filter);

		JsonObject jsonObject = ServiceCallHelper.invokeServiceAndGetJson(map, headerMap,
				URLConstants.CUSTOMERCOMMUNICATION_GET);
		map = new HashMap<String, Object>();
		if (jsonObject.has(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION)) {
			JsonElement jsonElement = jsonObject.get(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION);
			if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
				JsonArray jsonArray = jsonElement.getAsJsonArray();
				for (int i = 0; i < jsonArray.size(); i++) {
					JsonObject jsonObject2 = jsonArray.get(i).getAsJsonObject();
					map.put(InfinityConstants.id, jsonObject2.get(InfinityConstants.id).getAsString());
					ServiceCallHelper.invokeServiceAndGetJson(map, headerMap,
							URLConstants.CUSTOMERCOMMUNICATION_DELETE);
				}
			}
		}

		return;
	} 
    
    @Override
   	public DBXResult getInfinityUserServiceDefsRoles(JsonObject inputJson, Map<String, Object> headerMap) {
   		DBXResult dbxResult = new DBXResult();
   		String customerId = inputJson.get(InfinityConstants.id).getAsString();
		String contractId = (inputJson.get(InfinityConstants.contractId) != null
				&& !inputJson.get(InfinityConstants.contractId).isJsonNull())
						? inputJson.get(InfinityConstants.contractId).getAsString()
						: "";

		String isSuperadmin = inputJson.get(InfinityConstants.isSuperAdmin).getAsString();
		String legalEntityId = inputJson.get(InfinityConstants.legalEntityId).getAsString();
		JsonArray cifs = new JsonArray();
		
		/*OLB customer role creation manual flow*/
		if (isSuperadmin.equalsIgnoreCase("false") && StringUtils.isNotBlank(contractId)) {
			cifs  = getUserCustomersByContract(customerId, contractId, legalEntityId, headerMap);
		} else {
			String coreCustomerId = "";
			if (inputJson.get(InfinityConstants.coreCustomerId) != null
					&& !inputJson.get(InfinityConstants.coreCustomerId).isJsonNull()) {
				coreCustomerId = inputJson.get(InfinityConstants.coreCustomerId).getAsString();
			}
			
			Map<String, Object> inputParams = new HashMap<>();
			inputParams.put("_customerId", customerId);
			inputParams.put("_coreCustomerId", coreCustomerId);
			inputParams.put("_legalEntityId", legalEntityId);
			try {
				JsonObject response = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
						URLConstants.USER_CUSTOMERS_PROC);
				if (JSONUtil.hasKey(response, DBPDatasetConstants.DATASET_RECORDS)
						&& response.get(DBPDatasetConstants.DATASET_RECORDS).getAsJsonArray().size() > 0) {
					cifs = response.get(DBPDatasetConstants.DATASET_RECORDS).getAsJsonArray();
				}
			} catch (Exception e) {
				alert.prepareError(
						"InfinityUserManagementBackendDelegateImpl : Exception occured while fetching the associated customers info "
								+ e.getMessage()).log();
			}
		}
   		Map<String, String> groupsInfo = getGroupsInfo(headerMap);

   		Set<String> servicedfs = new HashSet<>();
   		for (JsonElement corecustomer : cifs) {
   			servicedfs.add(JSONUtil.getString(corecustomer.getAsJsonObject(), "serviceDefinitionId"));
   			String legalEntityIdfromDB = JSONUtil.getString(corecustomer.getAsJsonObject(), "companyLegalUnit");
   			corecustomer.getAsJsonObject().addProperty("legalEntityId", legalEntityIdfromDB);
   		}

   		Map<String, JsonArray> servdefsvalidroles = getValidRolesforServDefs(servicedfs, headerMap, groupsInfo);
   		for (JsonElement corecustomer : cifs) {
   			corecustomer.getAsJsonObject().add("validRoles",
   					servdefsvalidroles.get(JSONUtil.getString(corecustomer.getAsJsonObject(), "serviceDefinitionId")));
   		}
   		JsonObject dbxuserObj = new JsonObject();
   		dbxuserObj.add(InfinityConstants.companyList, cifs);
   		JsonObject userDetails = getCustomerJson(customerId, headerMap, legalEntityId);
   		dbxuserObj.add(InfinityConstants.userDetails, userDetails);

   		dbxResult.setResponse(dbxuserObj);
   		return dbxResult;
   	}
    
	private JsonArray getUserCustomersByContract(String customerId, String contractId, String legalEntityId,
			Map<String, Object> headerMap) {
		JsonObject response;
		JsonArray cifs = new JsonArray();
		Map<String, Object> inputParams = new HashMap<>();
		try {
			inputParams.put("_customerId", customerId);
			inputParams.put("_contractId", contractId);
			inputParams.put("_legalEntityId", legalEntityId);
			response = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
					URLConstants.USER_CONTRACTS_PROC);
			if (JSONUtil.hasKey(response, DBPDatasetConstants.DATASET_RECORDS)
					&& response.get(DBPDatasetConstants.DATASET_RECORDS).getAsJsonArray().size() > 0) {
				cifs = response.get(DBPDatasetConstants.DATASET_RECORDS).getAsJsonArray();
			}
		} catch (Exception e) {
			alert.prepareError(
					"InfinityUserManagementBackendDelegateImpl : Exception occured while fetching the associated customers info "
							+ e.getMessage()).log();
		}
		return cifs;
	}

   	private Map<String, String> getGroupsInfo(Map<String, Object> headerMap) {
   		Map<String, Object> input = new HashMap<>();
   		JsonObject membershipJsonObject = ServiceCallHelper.invokeServiceAndGetJson(input, headerMap,
   				URLConstants.MEMBERGROUP_GET);
   		Map<String, String> groupsInfo = new HashMap<>();
   		if (membershipJsonObject.has(DBPDatasetConstants.DATASET_MEMBERGROUP)) {
   			for (JsonElement jsonelement : membershipJsonObject.get(DBPDatasetConstants.DATASET_MEMBERGROUP)
   					.getAsJsonArray()) {
   				if (InfinityConstants.SID_ACTIVE
   						.equals(JSONUtil.getString(jsonelement.getAsJsonObject(), InfinityConstants.Status_id))) {
   					groupsInfo.put(JSONUtil.getString(jsonelement.getAsJsonObject(), "id"),
   							JSONUtil.getString(jsonelement.getAsJsonObject(), "Name"));
   				}
   			}
   		}
   		return groupsInfo;
   	}
       
   	private Map<String, JsonArray> getValidRolesforServDefs(Set<String> serviceDefinitionIds,
   			Map<String, Object> headerMap, Map<String, String> groupsInfo) {

   		Map<String, JsonArray> servdefvalidroles = new HashMap<>();
   		JsonObject jsonObject;
   		Map<String, Object> input = new HashMap<>();

   		input.put("_servicedefList", StringUtils.join(serviceDefinitionIds, ','));
   		jsonObject = ServiceCallHelper.invokeServiceAndGetJson(input, headerMap,
   				URLConstants.FETCH_ROLES_FOR_SERVICEDEFS_PROC);
   		JsonElement jsonElement;
   		JsonArray jsonArray;
   		if (!(jsonObject.has(DBPDatasetConstants.DATASET_RECORDS)))
   			return servdefvalidroles;
   		jsonElement = jsonObject.get(DBPDatasetConstants.DATASET_RECORDS);
   		if (!jsonElement.isJsonArray() || (jsonElement.getAsJsonArray().size() <= 0))
   			return servdefvalidroles;
   		jsonArray = jsonElement.getAsJsonArray();
   		for (JsonElement jsonelement : jsonArray) {
   			JsonObject json = new JsonObject();
   			if (groupsInfo.containsKey(JSONUtil.getString(jsonelement.getAsJsonObject(), "Group_id"))) {
   				String servicedefid = JSONUtil.getString(jsonelement.getAsJsonObject(), "serviceDefinitionId");
   				json.addProperty(InfinityConstants.roleId,
   						JSONUtil.getString(jsonelement.getAsJsonObject(), "Group_id"));
   				json.addProperty(InfinityConstants.userRole,
   						groupsInfo.containsKey(JSONUtil.getString(jsonelement.getAsJsonObject(), "Group_id"))
   								? groupsInfo.get(JSONUtil.getString(jsonelement.getAsJsonObject(), "Group_id"))
   								: null);
   				if (servdefvalidroles.containsKey(servicedefid)) {
   					servdefvalidroles.get(servicedefid).add(json);
   				} else {
   					JsonArray servicedefarr = new JsonArray();
   					servicedefarr.add(json);
   					servdefvalidroles.put(servicedefid,servicedefarr);
   				}

   			}
   		}
   		return servdefvalidroles;
   	}

   	
	@Override
	public DBXResult updateCustomerLanguage(CustomerDTO customer, Map<String, Object> headerMap) throws ApplicationException{

		DBXResult dbxResult = new DBXResult();
		try {
			JsonObject status = new JsonObject();
			customer.persist(headerMap, headerMap);
			String defaultLanguage = customer.getDefaultLanguage();
			customer = (CustomerDTO) customer.loadDTO();
			customer.setDefaultLanguage(defaultLanguage);
			status.addProperty("success", "false");
			dbxResult.setResponse(status);
			customer.setIsChanged(true);
			if(customer.persist(DTOUtils.getParameterMap(customer, true), headerMap))
				status.addProperty("success", "true");
			return dbxResult;
		} catch (Exception e) {
			alert.prepareError(
                    "InfinityUserManagementBackendDelegateImpl : Exception occured while updating default language details "
                            ,e).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10821);
		}
	}
   	
    @Override
    public DBXResult getInfinityUser(JsonObject inputJson, Map<String, Object> headerMap) {
        JsonObject jsonObject = new JsonObject();
        DBXResult dbxResult = new DBXResult();
        dbxResult.setResponse(jsonObject);

        String customerId = inputJson.get(InfinityConstants.id).getAsString();
        String legalEntityId = inputJson.get(InfinityConstants.legalEntityId).getAsString();

        String contractIdInput = "";
        if(!inputJson.get(InfinityConstants.contractId).isJsonNull())
        	contractIdInput = inputJson.get(InfinityConstants.contractId).getAsString();
        String coreCustomerIdInput = "";
        if(!inputJson.get(InfinityConstants.coreCustomerId).isJsonNull())		
        	coreCustomerIdInput = inputJson.get(InfinityConstants.coreCustomerId).getAsString();


        String loggedInUserId = inputJson.get(InfinityConstants.loggedInUserId).getAsString();

        boolean isSuperAdmin = Boolean.parseBoolean(inputJson.get(InfinityConstants.isSuperAdmin).getAsString());

        JsonObject customerjsonObject = getCustomerJson(customerId, headerMap, legalEntityId);

        jsonObject.add(InfinityConstants.userDetails, customerjsonObject);

        getInfinityUser(jsonObject, headerMap, customerId, loggedInUserId, contractIdInput, coreCustomerIdInput,isSuperAdmin, legalEntityId);
        dbxResult.setResponse(jsonObject);
        return dbxResult;
    }

    private void getInfinityUser(JsonObject jsonObject, Map<String, Object> headerMap, String customerId,
            String loggedInUserId,String contractIdInput,String coreCustomerIdInput, boolean isSuperAdmin, String legalEntityId) {
        JsonArray jsonArray = new JsonArray();

        Map<String, Map<String, Map<String, Map<String, String>>>> map =
                new HashMap<String, Map<String, Map<String, Map<String, String>>>>();

        Map<String, List<String>> companyMap = new HashMap<>();

        Map<String, Map<String, Map<String, String>>> accountMap =
                new HashMap<String, Map<String, Map<String, String>>>();

        Map<String, FeatureActionLimitsDTO> featureActionDTOs = new HashMap<String, FeatureActionLimitsDTO>();

        Map<String, JsonObject> contracts = new HashMap<String, JsonObject>();
        Map<String, JsonObject> contractCoreCustomers = new HashMap<String, JsonObject>();
        Map<String, JsonObject> coreCustomerGroupInfo = new HashMap<>();

        JsonArray actions = new JsonArray();

        Map<String, Object> input = new HashMap<String, Object>();

        String filter = InfinityConstants.Customer_id + DBPUtilitiesConstants.EQUAL + customerId
        				+ DBPUtilitiesConstants.AND + InfinityConstants.LegalEntityId + DBPUtilitiesConstants.EQUAL + legalEntityId;
        if(StringUtils.isNotBlank(contractIdInput) && StringUtils.isNotBlank(coreCustomerIdInput)) {
        	filter += DBPUtilitiesConstants.AND + InfinityConstants.contractId + DBPUtilitiesConstants.EQUAL + contractIdInput 
        	+ DBPUtilitiesConstants.AND + InfinityConstants.coreCustomerId + DBPUtilitiesConstants.EQUAL + coreCustomerIdInput;
        }
        diagnostic.prepareDebug("filter for customeraction: " + filter).log();
        
        input.put(DBPUtilitiesConstants.FILTER, filter);
        JsonObject response =
                ServiceCallHelper.invokeServiceAndGetJson(input, headerMap, URLConstants.CUSTOMER_ACTION_LIMITS_GET);
        if (response.has(DBPDatasetConstants.DATASET_CUSTOMERACTION)) {
            JsonElement jsonElement = response.get(DBPDatasetConstants.DATASET_CUSTOMERACTION);
            if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
                actions = jsonElement.getAsJsonArray();
            }
        }

        JsonArray excludedActions = new JsonArray();

        filter = InfinityConstants.Customer_id + DBPUtilitiesConstants.EQUAL + customerId
        		+ DBPUtilitiesConstants.AND + InfinityConstants.LegalEntityId + DBPUtilitiesConstants.EQUAL + legalEntityId;
        input.put(DBPUtilitiesConstants.FILTER, filter);
        response = ServiceCallHelper.invokeServiceAndGetJson(input, headerMap,
                URLConstants.EXCLUDED_CUSTOMER_ACTION_LIMITS_GET);
        if (response.has(DBPDatasetConstants.DATASET_EXCLUDED_CUSTOMERACTION)) {
            JsonElement jsonElement = response.get(DBPDatasetConstants.DATASET_EXCLUDED_CUSTOMERACTION);
            if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
                excludedActions = jsonElement.getAsJsonArray();
            }
        }
        JsonArray accounts = new JsonArray();
        input = new HashMap<String, Object>();
        filter = InfinityConstants.Customer_id + DBPUtilitiesConstants.EQUAL + customerId
        		+ DBPUtilitiesConstants.AND + InfinityConstants.LegalEntityId + DBPUtilitiesConstants.EQUAL + legalEntityId;

        if(StringUtils.isNotBlank(contractIdInput) && StringUtils.isNotBlank(coreCustomerIdInput)) {
        	filter += DBPUtilitiesConstants.AND + InfinityConstants.contractId + DBPUtilitiesConstants.EQUAL + contractIdInput 
        			+ DBPUtilitiesConstants.AND + InfinityConstants.coreCustomerId + DBPUtilitiesConstants.EQUAL + coreCustomerIdInput;
        }
        input.put(DBPUtilitiesConstants.FILTER, filter);
        diagnostic.prepareDebug("filter for customeraccounts: " + filter).log();

        response =
                ServiceCallHelper.invokeServiceAndGetJson(input, headerMap, URLConstants.CUSTOMERACCOUNTS_GET);
        if (response.has(DBPDatasetConstants.DATASET_CUSTOMERACCOUNTS)) {
            JsonElement jsonElement = response.get(DBPDatasetConstants.DATASET_CUSTOMERACCOUNTS);
            if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
                accounts = jsonElement.getAsJsonArray();
            }
        }

        jsonArray = getCompanyList(coreCustomerGroupInfo, contractIdInput, coreCustomerIdInput, contracts, contractCoreCustomers, customerId, headerMap, map,
                companyMap, accountMap, featureActionDTOs, loggedInUserId, accounts, isSuperAdmin, false, legalEntityId);
		if (!isSuperAdmin) {
			jsonArray = filteredContractIdsArray(jsonArray, jsonObject, headerMap);
		}
		jsonObject.add(InfinityConstants.companyList, jsonArray);

        addPermissions(coreCustomerGroupInfo, contracts, contractCoreCustomers, customerId, headerMap, map, jsonObject,
                accountMap, companyMap, featureActionDTOs, isSuperAdmin, actions,excludedActions,false);

    }
    

    private void addPermissions(Map<String, JsonObject> coreCustomerGroupInfo, Map<String, JsonObject> contracts,
            Map<String, JsonObject> contractCoreCustomers,
            String customerId, Map<String, Object> headerMap,
            Map<String, Map<String, Map<String, Map<String, String>>>> map, JsonObject customerJsonObject,
            Map<String, Map<String, Map<String, String>>> accountMap, Map<String, List<String>> companyMap,
            Map<String, FeatureActionLimitsDTO> featureActionDTOs, Boolean isSuperAdmin, JsonArray actions,JsonArray excludedActions,
			boolean isCustomRoleFlow) {

        Map<String, Map<String, Map<String, Boolean>>> globalActions =
                new HashMap<String, Map<String, Map<String, Boolean>>>();
        Map<String, Map<String, Map<String, Map<String, Boolean>>>> accountActions =
                new HashMap<String, Map<String, Map<String, Map<String, Boolean>>>>();
        Map<String, Map<String, Map<String, Map<String, Map<String, String>>>>> transactionLimits =
                new HashMap<String, Map<String, Map<String, Map<String, Map<String, String>>>>>();

        Map<String, Map<String, Map<String, Map<String, Double>>>> limitGroups =
                new HashMap<String, Map<String, Map<String, Map<String, Double>>>>();
        for (int i = 0; i < actions.size(); i++) {
            JsonObject jsonObject2 = actions.get(i).getAsJsonObject();
            String coreCustomerId = jsonObject2.has(InfinityConstants.coreCustomerId)
                    && !jsonObject2.get(InfinityConstants.coreCustomerId).isJsonNull()
                            ? jsonObject2.get(InfinityConstants.coreCustomerId).getAsString()
                            : null;
            String value = jsonObject2.has(InfinityConstants.value)
                    && !jsonObject2.get(InfinityConstants.value).isJsonNull()
                            ? jsonObject2.get(InfinityConstants.value).getAsString()
                            : null;
            String limitTypeId = jsonObject2.has(InfinityConstants.LimitType_id)
                    && !jsonObject2.get(InfinityConstants.LimitType_id).isJsonNull()
                            ? jsonObject2.get(InfinityConstants.LimitType_id).getAsString()
                            : null;
            if (StringUtils.isBlank(limitTypeId)) {
                limitTypeId = jsonObject2.has(InfinityConstants.limitType_id)
                        && !jsonObject2.get(InfinityConstants.limitType_id).isJsonNull()
                                ? jsonObject2.get(InfinityConstants.limitType_id).getAsString()
                                : null;
            }
            String featureId = jsonObject2.has(InfinityConstants.featureId)
                    && !jsonObject2.get(InfinityConstants.featureId).isJsonNull()
                            ? jsonObject2.get(InfinityConstants.featureId).getAsString().trim()
                            : null;
            String actionId = jsonObject2.has(InfinityConstants.Action_id)
                    && !jsonObject2.get(InfinityConstants.Action_id).isJsonNull()
                            ? jsonObject2.get(InfinityConstants.Action_id).getAsString().trim()
                            : null;
            if (StringUtils.isBlank(actionId)) {
                actionId = jsonObject2.has(InfinityConstants.action_id)
                        && !jsonObject2.get(InfinityConstants.action_id).isJsonNull()
                                ? jsonObject2.get(InfinityConstants.action_id).getAsString().trim()
                                : null;
            }
            String accountId = jsonObject2.has(InfinityConstants.Account_id)
                    && !jsonObject2.get(InfinityConstants.Account_id).isJsonNull()
                            ? jsonObject2.get(InfinityConstants.Account_id).getAsString()
                            : null;
            if (StringUtils.isBlank(accountId)) {
                accountId = jsonObject2.has(InfinityConstants.account_id)
                        && !jsonObject2.get(InfinityConstants.account_id).isJsonNull()
                                ? jsonObject2.get(InfinityConstants.account_id).getAsString()
                                : null;
            }

            String limitGroupId = jsonObject2.has(InfinityConstants.limitGroupId)
                    && !jsonObject2.get(InfinityConstants.limitGroupId).isJsonNull()
                            ? jsonObject2.get(InfinityConstants.limitGroupId).getAsString()
                            : null;

            FeatureActionLimitsDTO featureActionDTO = featureActionDTOs.get(coreCustomerId);
            if (featureActionDTO == null) {
                continue;
            }

            limitGroupId = featureActionDTO.getActionsInfo().containsKey(actionId)
                    ? featureActionDTO.getActionsInfo().get(actionId).get(InfinityConstants.limitGroupId).getAsString()
                    : limitGroupId;

            if (StringUtils.isBlank(accountId) && StringUtils.isBlank(limitGroupId)) {
                if (!globalActions.containsKey(coreCustomerId)) {
                    globalActions.put(coreCustomerId, new HashMap<String, Map<String, Boolean>>());
                }
                if (!featureActionDTO.getFeatureaction().containsKey(featureId)) {
                    continue;
                }
                if (!globalActions.get(coreCustomerId).containsKey(featureId)) {
                    globalActions.get(coreCustomerId).put(featureId, new HashMap<String, Boolean>());
                }
                if (!featureActionDTO.getFeatureaction().get(featureId).contains(actionId)) {
                    continue;
                }
                globalActions.get(coreCustomerId).get(featureId).put(actionId, true);
            } else {
                if (StringUtils.isNotBlank(accountId)) {
                    if (!accountActions.containsKey(coreCustomerId)) {
                        accountActions.put(coreCustomerId, new HashMap<String, Map<String, Map<String, Boolean>>>());
                    }
                    if (!featureActionDTO.getFeatureaction().containsKey(featureId)
                            && !featureActionDTO.getMonetaryActionLimits().containsKey(featureId)) {
                        continue;
                    }
                    if (!accountActions.get(coreCustomerId).containsKey(accountId)) {
                        accountActions.get(coreCustomerId).put(accountId, new HashMap<String, Map<String, Boolean>>());
                    }
                    if ((featureActionDTO.getFeatureaction().get(featureId) == null
                            || !featureActionDTO.getFeatureaction().get(featureId).contains(actionId))
                            && (featureActionDTO.getMonetaryActionLimits().get(featureId) == null
                                    || !featureActionDTO.getMonetaryActionLimits().get(featureId)
                                            .containsKey(actionId))) {
                        continue;
                    }
                    if (!accountActions.get(coreCustomerId).get(accountId).containsKey(featureId)) {
                        accountActions.get(coreCustomerId).get(accountId).put(featureId,
                                new HashMap<String, Boolean>());
                    }
                    accountActions.get(coreCustomerId).get(accountId).get(featureId).put(actionId, true);
                }

                if (StringUtils.isNotBlank(accountId) && StringUtils.isNotBlank(limitTypeId)) {
                    if (!transactionLimits.containsKey(coreCustomerId)) {
                        transactionLimits.put(coreCustomerId,
                                new HashMap<String, Map<String, Map<String, Map<String, String>>>>());
                        limitGroups.put(coreCustomerId, new HashMap<String, Map<String, Map<String, Double>>>());
                    }

                    if (!transactionLimits.get(coreCustomerId).containsKey(accountId)) {
                        transactionLimits.get(coreCustomerId).put(accountId,
                                new HashMap<String, Map<String, Map<String, String>>>());
                        limitGroups.get(coreCustomerId).put(accountId, new HashMap<String, Map<String, Double>>());
                    }
                    if (!featureActionDTO.getMonetaryActionLimits().containsKey(featureId)) {
                        continue;
                    }
                    if (!transactionLimits.get(coreCustomerId).get(accountId).containsKey(featureId)) {
                        transactionLimits.get(coreCustomerId).get(accountId).put(featureId,
                                new HashMap<String, Map<String, String>>());
                    }
                    if (!featureActionDTO.getMonetaryActionLimits().get(featureId).containsKey(actionId)) {
                        continue;
                    }
                    if (!transactionLimits.get(coreCustomerId).get(accountId).get(featureId)
                            .containsKey(actionId)) {
                        transactionLimits.get(coreCustomerId).get(accountId).get(featureId).put(actionId,
                                new HashMap<String, String>());
                    }

                    Map<String, String> limitMap =
                            featureActionDTO.getMonetaryActionLimits().get(featureId).get(actionId);

                    Double limit2 = new Double("0.0");
                    if (limitTypeId.equals(InfinityConstants.PRE_APPROVED_DAILY_LIMIT)
                            || limitTypeId.equals(InfinityConstants.AUTO_DENIED_DAILY_LIMIT)
                            || limitTypeId.equals(InfinityConstants.DAILY_LIMIT)) {
                        limit2 = Double.parseDouble((limitMap.containsKey(InfinityConstants.DAILY_LIMIT) &&
                                StringUtils.isNotBlank(limitMap.get(InfinityConstants.DAILY_LIMIT)))
                                        ? limitMap.get(InfinityConstants.DAILY_LIMIT)
                                        : "0.0");
                        value = Math.min(limit2, Double.parseDouble(value)) + "";

                        if (limitTypeId.equals(InfinityConstants.DAILY_LIMIT)) {
                            //value = limit2 + "";
                            if (!limitGroups.get(coreCustomerId).get(accountId).containsKey(limitGroupId)) {
                                limitGroups.get(coreCustomerId).get(accountId).put(limitGroupId,
                                        new HashMap<String, Double>());

                            }
                            if (!limitGroups.get(coreCustomerId).get(accountId).get(limitGroupId)
                                    .containsKey(InfinityConstants.DAILY_LIMIT)) {
                                limitGroups.get(coreCustomerId).get(accountId).get(limitGroupId)
                                        .put(InfinityConstants.DAILY_LIMIT, limit2);
                            } else {
                                Double limitValue = limitGroups.get(coreCustomerId).get(accountId).get(limitGroupId)
                                        .get(InfinityConstants.DAILY_LIMIT);

                                limitValue = limitValue + limit2;

                                limitGroups.get(coreCustomerId).get(accountId).get(limitGroupId)
                                        .put(InfinityConstants.DAILY_LIMIT, limitValue);
                            }
                        }
                    } else if (limitTypeId.equals(InfinityConstants.PRE_APPROVED_WEEKLY_LIMIT)
                            || limitTypeId.equals(InfinityConstants.AUTO_DENIED_WEEKLY_LIMIT)
                            || limitTypeId.equals(InfinityConstants.WEEKLY_LIMIT)) {
                        limit2 = Double.parseDouble((limitMap.containsKey(InfinityConstants.WEEKLY_LIMIT) &&
                                StringUtils.isNotBlank(limitMap.get(InfinityConstants.WEEKLY_LIMIT)))
                                        ? limitMap.get(InfinityConstants.WEEKLY_LIMIT)
                                        : "0.0");
                        value = Math.min(limit2, Double.parseDouble(value)) + "";

                        if (limitTypeId.equals(InfinityConstants.WEEKLY_LIMIT)) {

                            value = limit2 + "";
                            if (!limitGroups.get(coreCustomerId).get(accountId).containsKey(limitGroupId)) {
                                limitGroups.get(coreCustomerId).get(accountId).put(limitGroupId,
                                        new HashMap<String, Double>());
                            }

                            if (!limitGroups.get(coreCustomerId).get(accountId).get(limitGroupId)
                                    .containsKey(InfinityConstants.WEEKLY_LIMIT)) {
                                limitGroups.get(coreCustomerId).get(accountId).get(limitGroupId)
                                        .put(InfinityConstants.WEEKLY_LIMIT, limit2);
                            } else {
                                Double limitValue = limitGroups.get(coreCustomerId).get(accountId).get(limitGroupId)
                                        .get(InfinityConstants.WEEKLY_LIMIT);

                                limitValue = limitValue + limit2;

                                limitGroups.get(coreCustomerId).get(accountId).get(limitGroupId)
                                        .put(InfinityConstants.WEEKLY_LIMIT, limitValue);
                            }
                        }
                    } else if (limitTypeId.equals(InfinityConstants.PRE_APPROVED_TRANSACTION_LIMIT)
                            || limitTypeId.equals(InfinityConstants.AUTO_DENIED_TRANSACTION_LIMIT)
                            || limitTypeId.equals(InfinityConstants.MAX_TRANSACTION_LIMIT)) {
                        limit2 = Double
                                .parseDouble((limitMap.containsKey(InfinityConstants.MAX_TRANSACTION_LIMIT) &&
                                        StringUtils.isNotBlank(
                                                limitMap.get(InfinityConstants.MAX_TRANSACTION_LIMIT)))
                                                        ? limitMap.get(InfinityConstants.MAX_TRANSACTION_LIMIT)
                                                        : "0.0");
                        value = Math.min(limit2, Double.parseDouble(value)) + "";
                        if (limitTypeId.equals(InfinityConstants.MAX_TRANSACTION_LIMIT)) {

                            if (!limitGroups.get(coreCustomerId).get(accountId).containsKey(limitGroupId)) {
                                limitGroups.get(coreCustomerId).get(accountId).put(limitGroupId,
                                        new HashMap<String, Double>());
                            }

                            //value = limit2 + "";
                            // if (!limitGroups.get(coreCustomerId).get(accountId).containsKey(limitGroupId)) {
                            // limitGroups.get(coreCustomerId).get(accountId).get(limitGroupId)
                            // .put(InfinityConstants.MAX_TRANSACTION_LIMIT, Double.parseDouble(value));
                            // }

                            if (!limitGroups.get(coreCustomerId).get(accountId).get(limitGroupId)
                                    .containsKey(InfinityConstants.MAX_TRANSACTION_LIMIT)) {
                                limitGroups.get(coreCustomerId).get(accountId).get(limitGroupId)
                                        .put(InfinityConstants.MAX_TRANSACTION_LIMIT, limit2);
                            } else {
                                Double limitValue = limitGroups.get(coreCustomerId).get(accountId).get(limitGroupId)
                                        .get(InfinityConstants.MAX_TRANSACTION_LIMIT);

                                limitValue = Math.max(limitValue, limit2);

                                limitGroups.get(coreCustomerId).get(accountId).get(limitGroupId)
                                        .put(InfinityConstants.MAX_TRANSACTION_LIMIT, limitValue);
                            }
                        }
                    }
                    /*Returning contract level limits for getinfinityUser*/

					if (limitTypeId.equals(InfinityConstants.MAX_TRANSACTION_LIMIT)
							|| limitTypeId.equals(InfinityConstants.DAILY_LIMIT)
							|| limitTypeId.equals(InfinityConstants.WEEKLY_LIMIT)) {
						transactionLimits.get(coreCustomerId).get(accountId).get(featureId).get(actionId)
								.put(limitTypeId, limit2 + "");
					} else {
						transactionLimits.get(coreCustomerId).get(accountId).get(featureId).get(actionId)
								.put(limitTypeId, value);
					}
                    transactionLimits.get(coreCustomerId).get(accountId).get(featureId).get(actionId)
                            .put(InfinityConstants.limitGroupId, limitGroupId);
                    transactionLimits.get(coreCustomerId).get(accountId).get(featureId).get(actionId)
                            .put(InfinityConstants.isEnabled, "true");

                }
            }

}
        Map<String, Map<String, Map<String, Boolean>>> excludedGlobalActions =
                new HashMap<String, Map<String, Map<String, Boolean>>>();
        Map<String, Map<String, Map<String, Map<String, Boolean>>>> excludedAccountActions =
                new HashMap<String, Map<String, Map<String, Map<String, Boolean>>>>();

        for (int i = 0; i < excludedActions.size(); i++) {
            JsonObject jsonObject = excludedActions.get(i).getAsJsonObject();

            String coreCustomer = jsonObject.has(InfinityConstants.coreCustomerId)
                    && !jsonObject.get(InfinityConstants.coreCustomerId).isJsonNull()
                            ? jsonObject.get(InfinityConstants.coreCustomerId).getAsString()
                            : null;
            String featureId = jsonObject.has(InfinityConstants.featureId)
                    && !jsonObject.get(InfinityConstants.featureId).isJsonNull()
                            ? jsonObject.get(InfinityConstants.featureId).getAsString().trim()
                            : null;
            String actionId = jsonObject.has(InfinityConstants.Action_id)
                    && !jsonObject.get(InfinityConstants.Action_id).isJsonNull()
                            ? jsonObject.get(InfinityConstants.Action_id).getAsString().trim()
                            : null;
            if (StringUtils.isBlank(actionId)) {
                actionId = jsonObject.has(InfinityConstants.action_id)
                        && !jsonObject.get(InfinityConstants.action_id).isJsonNull()
                                ? jsonObject.get(InfinityConstants.action_id).getAsString().trim()
                                : null;
            }
            String accountId = jsonObject.has(InfinityConstants.Account_id)
                    && !jsonObject.get(InfinityConstants.Account_id).isJsonNull()
                            ? jsonObject.get(InfinityConstants.Account_id).getAsString()
                            : null;
            if (StringUtils.isBlank(accountId)) {
                accountId = jsonObject.has(InfinityConstants.account_id)
                        && !jsonObject.get(InfinityConstants.account_id).isJsonNull()
                                ? jsonObject.get(InfinityConstants.account_id).getAsString()
                                : null;
            }

            FeatureActionLimitsDTO actionLimitsDTO = featureActionDTOs.get(coreCustomer);
            if (actionLimitsDTO == null) {
                continue;
            }

            if (actionLimitsDTO.getGlobalLevelActions().contains(actionId)) {
                if (!excludedGlobalActions.containsKey(coreCustomer)) {
                    excludedGlobalActions.put(coreCustomer, new HashMap<String, Map<String, Boolean>>());
                }
                if (!actionLimitsDTO.getFeatureaction().containsKey(featureId)) {
                    continue;
                }
                if (!excludedGlobalActions.get(coreCustomer).containsKey(featureId)) {
                    excludedGlobalActions.get(coreCustomer).put(featureId, new HashMap<String, Boolean>());
                }
                if (!actionLimitsDTO.getFeatureaction().get(featureId).contains(actionId)) {
                    continue;
                }
                excludedGlobalActions.get(coreCustomer).get(featureId).put(actionId, true);
            } else {
                if (actionLimitsDTO.getAccountLevelActions().contains(actionId)) {
                    if (!excludedAccountActions.containsKey(coreCustomer)) {
                        excludedAccountActions.put(coreCustomer,
                                new HashMap<String, Map<String, Map<String, Boolean>>>());
                    }
                    if (!actionLimitsDTO.getFeatureaction().containsKey(featureId)
                            && !actionLimitsDTO.getMonetaryActionLimits().containsKey(featureId)) {
                        continue;
                    }
                    if (!excludedAccountActions.get(coreCustomer).containsKey(accountId)) {
                        excludedAccountActions.get(coreCustomer).put(accountId,
                                new HashMap<String, Map<String, Boolean>>());
                    }
                    if ((actionLimitsDTO.getFeatureaction().get(featureId) == null
                            || !actionLimitsDTO.getFeatureaction().get(featureId).contains(actionId))
                            && (actionLimitsDTO.getMonetaryActionLimits().get(featureId) == null || !actionLimitsDTO
                                    .getMonetaryActionLimits().get(featureId).containsKey(actionId))) {
                        continue;
                    }
                    if (!excludedAccountActions.get(coreCustomer).get(accountId).containsKey(featureId)) {
                        excludedAccountActions.get(coreCustomer).get(accountId).put(featureId,
                                new HashMap<String, Boolean>());
                    }
                    excludedAccountActions.get(coreCustomer).get(accountId).get(featureId).put(actionId, true);
                }
            }
        }

        Map<String, Map<String, Map<String, Map<String, Double>>>> limitsMap =
                new HashMap<String, Map<String, Map<String, Map<String, Double>>>>();
        Map<String, Map<String, Map<String, String>>> limitsIds =
                new HashMap<String, Map<String, Map<String, String>>>();

        for (String cif : limitGroups.keySet()) {
            if (!limitsMap.containsKey(cif)) {
                limitsMap.put(cif, new HashMap<String, Map<String, Map<String, Double>>>());
            }
            if (!limitsIds.containsKey(cif)) {
                limitsIds.put(cif, new HashMap<String, Map<String, String>>());
            }
            Map<String, Object> input = new HashMap<String, Object>();
            String filter = InfinityConstants.coreCustomerId + DBPUtilitiesConstants.EQUAL + cif
                    + DBPUtilitiesConstants.AND + InfinityConstants.Customer_id + DBPUtilitiesConstants.EQUAL
                    + customerId;
            input.put(DBPUtilitiesConstants.FILTER, filter);
            JsonObject response = ServiceCallHelper.invokeServiceAndGetJson(input, headerMap,
                    URLConstants.CUSTOMER_LIMIT_GROUP_LIMITS_GET);
            if (response.has(DBPDatasetConstants.DATASET_CUSTOMRLIMITGROUPLIMITS)) {
                JsonElement jsonElement = response.get(DBPDatasetConstants.DATASET_CUSTOMRLIMITGROUPLIMITS);
                if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
                    for (JsonElement element : jsonElement.getAsJsonArray()) {
                        String limitTypeId = element.getAsJsonObject().get(InfinityConstants.LimitType_id)
                                .getAsString();
                        String limitGroupId = element.getAsJsonObject().get(InfinityConstants.limitGroupId)
                                .getAsString();
                        String value = element.getAsJsonObject().get(InfinityConstants.value).getAsString();

                        String id = element.getAsJsonObject().get(InfinityConstants.id).getAsString();

                        if (!limitsMap.get(cif).containsKey(limitGroupId)) {
                            limitsMap.get(cif).put(limitGroupId, new HashMap<String, Map<String, Double>>());
                        }

                        if (!limitsIds.get(cif).containsKey(limitGroupId)) {
                            limitsIds.get(cif).put(limitGroupId, new HashMap<String, String>());
                        }

                        if (!limitsMap.get(cif).get(limitGroupId).containsKey(limitTypeId)) {
                            limitsMap.get(cif).get(limitGroupId).put(limitTypeId, new HashMap<String, Double>());
                        }
                        if (!limitsIds.get(cif).get(limitGroupId).containsKey(limitTypeId)) {
                            limitsIds.get(cif).get(limitGroupId).put(limitTypeId, id);
                        }

                        limitsMap.get(cif).get(limitGroupId).get(limitTypeId).put(InfinityConstants.value,
                                Double.parseDouble(value));
                    }

                }
            }
        }

        Map<String, String> contractsMap = new HashMap<String, String>();
        getcoreCustomerContract(map, contractsMap);
        for (String coreCustomer : featureActionDTOs.keySet()) {
            FeatureActionLimitsDTO actionLimitsDTO = featureActionDTOs.get(coreCustomer);

            if (actionLimitsDTO == null)
                continue;
            if (!limitsMap.containsKey(coreCustomer)) {
                limitsMap.put(coreCustomer, new HashMap<String, Map<String, Map<String, Double>>>());
            }

            if (actionLimitsDTO.getNewFeatureAction() != null) {
                Map<String, Set<String>> featureActions = actionLimitsDTO.getNewFeatureAction();

                ActionLimitsDTO dto = new ActionLimitsDTO();
                for (String feature : featureActions.keySet()) {
                    for (String action : featureActions.get(feature)) {
                        dto = new ActionLimitsDTO();
                        dto.setContractId(contractsMap.get(coreCustomer));
                        if (isCustomRoleFlow) {
                            dto.setCustomRoleId(customerId);
                        } else {
                            dto.setCustomerId(customerId);
                        }
                        dto.setCoreCustomerId(coreCustomer);
                        dto.setFeatureId(feature);
                        dto.setActionId(action);
                       
                        dto.setRoleId(
                                coreCustomerGroupInfo.get(coreCustomer).get(InfinityConstants.roleId).getAsString());
                        dto.setAccountLevel(actionLimitsDTO.getAccountLevelActions().contains(action));
                        dto.setMonetory(actionLimitsDTO.getMonetaryActions().contains(action));

                        if (actionLimitsDTO.getGlobalLevelActions().contains(action)) {
                            if (!globalActions.containsKey(coreCustomer)) {
                                globalActions.put(coreCustomer, new HashMap<String, Map<String, Boolean>>());
                            }
                            if (!globalActions.get(coreCustomer).containsKey(feature)) {
                                globalActions.get(coreCustomer).put(feature, new HashMap<String, Boolean>());
                            }
                            if (globalActions.get(coreCustomer).get(feature).containsKey(action)) {
                                continue;
                            }
                            if (excludedGlobalActions.containsKey(coreCustomer)
                                    && excludedGlobalActions.get(coreCustomer).containsKey(feature)
                                    && excludedGlobalActions.get(coreCustomer).get(feature).containsKey(action)) {
                                continue;
                            }
                            dto.setCompanyLegalUnit(companyMap.get(coreCustomer).get(1));
                            
                            globalActions.get(coreCustomer).get(feature).put(action, true);
                            addActions(dto, headerMap, isCustomRoleFlow);
                        } else if (actionLimitsDTO.getMonetaryActions().contains(action)
                                && actionLimitsDTO.getNewMonetaryActionLimits().containsKey(feature)) {
                            for (String accountId : accountMap.get(coreCustomer).keySet()) {
                                dto.setAccountId(accountId);
                                if (!transactionLimits.containsKey(coreCustomer)) {
                                    transactionLimits.put(coreCustomer,
                                            new HashMap<String, Map<String, Map<String, Map<String, String>>>>());
                                    limitGroups.put(coreCustomer,
                                            new HashMap<String, Map<String, Map<String, Double>>>());
                                }

                                if (!transactionLimits.get(coreCustomer).containsKey(accountId)) {
                                    transactionLimits.get(coreCustomer).put(accountId,
                                            new HashMap<String, Map<String, Map<String, String>>>());
                                    limitGroups.get(coreCustomer).put(accountId,
                                            new HashMap<String, Map<String, Double>>());
                                }
                                if (!transactionLimits.get(coreCustomer).get(accountId).containsKey(feature)) {
                                    transactionLimits.get(coreCustomer).get(accountId).put(feature,
                                            new HashMap<String, Map<String, String>>());
                                }
                                if (transactionLimits.get(coreCustomer).get(accountId).get(feature)
                                        .containsKey(action)) {
                                    continue;
                                }

                                if (excludedAccountActions.containsKey(coreCustomer)
                                        && excludedAccountActions.get(coreCustomer).containsKey(accountId)
                                        && excludedAccountActions.get(coreCustomer).get(accountId).containsKey(feature)
                                        && excludedAccountActions.get(coreCustomer).get(accountId).get(feature)
                                                .containsKey(action)) {
                                    continue;
                                }

                                transactionLimits.get(coreCustomer).get(accountId).get(feature).put(action,
                                        new HashMap<String, String>());

                                Map<String, String> limitMap = actionLimitsDTO.getNewMonetaryActionLimits().get(feature)
                                        .get(action);

                                String limitGroupId = actionLimitsDTO.getActionsInfo().get(action)
                                        .get(InfinityConstants.limitGroupId).getAsString();

                                dto.setLimitGroupId(limitGroupId);

                                if (!limitsMap.get(coreCustomer).containsKey(limitGroupId)) {
                                    limitsMap.get(coreCustomer).put(limitGroupId,
                                            new HashMap<String, Map<String, Double>>());
                                }

                                for (String limitTypeId : limitMap.keySet()) {

                                    double value = Double.parseDouble((limitMap.containsKey(limitTypeId)
                                            && StringUtils.isNotBlank(limitMap.get(limitTypeId)))
                                                    ? limitMap.get(limitTypeId)
                                                    : "0.0");

                                    if (!limitsMap.get(coreCustomer).get(limitGroupId).containsKey(limitTypeId)) {
                                        limitsMap.get(coreCustomer).get(limitGroupId).put(limitTypeId,
                                                new HashMap<String, Double>());
                                        limitsMap.get(coreCustomer).get(limitGroupId).get(limitTypeId)
                                                .put(InfinityConstants.value, Double.valueOf(0.0));
                                    }

                                    if (limitTypeId.equals(InfinityConstants.DAILY_LIMIT)) {

                                        if (!limitGroups.get(coreCustomer).get(accountId).containsKey(limitGroupId)) {
                                            limitGroups.get(coreCustomer).get(accountId).put(limitGroupId,
                                                    new HashMap<String, Double>());

                                        }
                                        /*if (!limitGroups.get(coreCustomer).get(accountId).get(limitGroupId)
                                                .containsKey(InfinityConstants.DAILY_LIMIT)) {
                                            limitGroups.get(coreCustomer).get(accountId).get(limitGroupId)
                                                    .put(InfinityConstants.DAILY_LIMIT, value);
                                        } else {
                                            Double limitValue = limitGroups.get(coreCustomer).get(accountId)
                                                    .get(limitGroupId).get(InfinityConstants.DAILY_LIMIT);

                                            limitValue = limitValue + value;

                                            limitGroups.get(coreCustomer).get(accountId).get(limitGroupId)
                                                    .put(InfinityConstants.DAILY_LIMIT, limitValue);
                                        }*/

                                        limitsMap.get(coreCustomer).get(limitGroupId).get(limitTypeId).put(
                                                InfinityConstants.value, limitsMap.get(coreCustomer).get(limitGroupId)
                                                        .get(limitTypeId).get(InfinityConstants.value) + value);

                                        transactionLimits.get(coreCustomer).get(accountId).get(feature).get(action)
                                                .put(InfinityConstants.PRE_APPROVED_DAILY_LIMIT, 0.0 + "");
                                        transactionLimits.get(coreCustomer).get(accountId).get(feature).get(action)
                                                .put(InfinityConstants.AUTO_DENIED_DAILY_LIMIT, value + "");
                                        transactionLimits.get(coreCustomer).get(accountId).get(feature).get(action)
                                                .put(InfinityConstants.DAILY_LIMIT, value + "");

                                        dto.setDailyLimitValue(value);

                                    } else if (limitTypeId.equals(InfinityConstants.WEEKLY_LIMIT)) {

                                        if (!limitGroups.get(coreCustomer).get(accountId).containsKey(limitGroupId)) {
                                            limitGroups.get(coreCustomer).get(accountId).put(limitGroupId,
                                                    new HashMap<String, Double>());
                                        }

                                        /*if (!limitGroups.get(coreCustomer).get(accountId).get(limitGroupId)
                                                .containsKey(InfinityConstants.WEEKLY_LIMIT)) {
                                            limitGroups.get(coreCustomer).get(accountId).get(limitGroupId)
                                                    .put(InfinityConstants.WEEKLY_LIMIT, value);
                                        } else {
                                            Double limitValue = limitGroups.get(coreCustomer).get(accountId)
                                                    .get(limitGroupId).get(InfinityConstants.WEEKLY_LIMIT);

                                            limitValue = limitValue + value;

                                            limitGroups.get(coreCustomer).get(accountId).get(limitGroupId)
                                                    .put(InfinityConstants.WEEKLY_LIMIT, limitValue);
                                        }*/

                                        limitsMap.get(coreCustomer).get(limitGroupId).get(limitTypeId).put(
                                                InfinityConstants.value, limitsMap.get(coreCustomer).get(limitGroupId)
                                                        .get(limitTypeId).get(InfinityConstants.value) + value);

                                        transactionLimits.get(coreCustomer).get(accountId).get(feature).get(action)
                                                .put(InfinityConstants.PRE_APPROVED_WEEKLY_LIMIT, 0.0 + "");
                                        transactionLimits.get(coreCustomer).get(accountId).get(feature).get(action)
                                                .put(InfinityConstants.AUTO_DENIED_WEEKLY_LIMIT, value + "");
                                        transactionLimits.get(coreCustomer).get(accountId).get(feature).get(action)
                                                .put(InfinityConstants.WEEKLY_LIMIT, value + "");
                                        dto.setWeeklyLimitValue(value);
                                    } else if (limitTypeId.equals(InfinityConstants.MAX_TRANSACTION_LIMIT)) {

                                        if (!limitGroups.get(coreCustomer).get(accountId).containsKey(limitGroupId)) {
                                            limitGroups.get(coreCustomer).get(accountId).put(limitGroupId,
                                                    new HashMap<String, Double>());
                                        }

                                        /*if (!limitGroups.get(coreCustomer).get(accountId).get(limitGroupId)
                                                .containsKey(InfinityConstants.MAX_TRANSACTION_LIMIT)) {
                                            limitGroups.get(coreCustomer).get(accountId).get(limitGroupId)
                                                    .put(InfinityConstants.MAX_TRANSACTION_LIMIT, value);
                                        } else {
                                            Double limitValue = limitGroups.get(coreCustomer).get(accountId)
                                                    .get(limitGroupId).get(InfinityConstants.MAX_TRANSACTION_LIMIT);

                                            limitValue = Math.max(limitValue, value);

                                            limitGroups.get(coreCustomer).get(accountId).get(limitGroupId)
                                                    .put(InfinityConstants.MAX_TRANSACTION_LIMIT, limitValue);
                                        }*/

                                        limitsMap.get(coreCustomer).get(limitGroupId).get(limitTypeId)
                                                .put(InfinityConstants.value,
                                                        Math.max(
                                                                limitsMap.get(coreCustomer).get(limitGroupId)
                                                                        .get(limitTypeId).get(InfinityConstants.value),
                                                                value));

                                        transactionLimits.get(coreCustomer).get(accountId).get(feature).get(action)
                                                .put(InfinityConstants.PRE_APPROVED_TRANSACTION_LIMIT, 0.0 + "");
                                        transactionLimits.get(coreCustomer).get(accountId).get(feature).get(action)
                                                .put(InfinityConstants.AUTO_DENIED_TRANSACTION_LIMIT, value + "");
                                        transactionLimits.get(coreCustomer).get(accountId).get(feature).get(action)
                                                .put(InfinityConstants.MAX_TRANSACTION_LIMIT, value + "");
                                        dto.setMaxTransactionLimitValue(value);
                                    }

                                }
                                dto.setCompanyLegalUnit(companyMap.get(coreCustomer).get(1));
                                addActions(dto, headerMap, isCustomRoleFlow);
                            }
                        } else if (actionLimitsDTO.getAccountLevelActions().contains(action)) {
                            for (String accountId : accountMap.get(coreCustomer).keySet()) {
                                dto.setAccountId(accountId);
                                if (!accountActions.containsKey(coreCustomer)) {
                                    accountActions.put(coreCustomer,
                                            new HashMap<String, Map<String, Map<String, Boolean>>>());
                                }
                                if (!accountActions.get(coreCustomer).containsKey(accountId)) {
                                    accountActions.get(coreCustomer).put(accountId,
                                            new HashMap<String, Map<String, Boolean>>());
                                }
                                if (!accountActions.get(coreCustomer).get(accountId).containsKey(feature)) {
                                    accountActions.get(coreCustomer).get(accountId).put(feature,
                                            new HashMap<String, Boolean>());
                                }
                                if (accountActions.get(coreCustomer).get(accountId).get(feature).containsKey(action)) {
                                    continue;
                                }
                                if (excludedAccountActions.containsKey(coreCustomer)
                                        && excludedAccountActions.get(coreCustomer).containsKey(accountId)
                                        && excludedAccountActions.get(coreCustomer).get(accountId).containsKey(feature)
                                        && excludedAccountActions.get(coreCustomer).get(accountId).get(feature)
                                                .containsKey(action)) {
                                    continue;
                                }
                                accountActions.get(coreCustomer).get(accountId).get(feature).put(action, true);
                                dto.setCompanyLegalUnit(companyMap.get(coreCustomer).get(1));
                                addActions(dto, headerMap, isCustomRoleFlow);
                            }
                        }
                    }
                }

                Map<String, Object> input = new HashMap<String, Object>();

                for (String limitGroupId : limitsMap.get(coreCustomer).keySet()) {
                    for (String limitTypeId : limitsMap.get(coreCustomer).get(limitGroupId).keySet()) {
                        input.put(InfinityConstants.Customer_id, customerId);
                        input.put(InfinityConstants.contractId, contractsMap.get(coreCustomer));
                        input.put(InfinityConstants.coreCustomerId, coreCustomer);
                        input.put(InfinityConstants.limitGroupId, limitGroupId);
                        input.put(InfinityConstants.LimitType_id, limitTypeId);
                        input.put(InfinityConstants.value, limitsMap.get(coreCustomer).get(limitGroupId)
                                .get(limitTypeId).get(InfinityConstants.value) + "");
                        if (!limitsIds.containsKey(coreCustomer)
								|| !limitsIds.get(coreCustomer).containsKey(limitGroupId)
								|| !limitsIds.get(coreCustomer).get(limitGroupId).containsKey(limitTypeId)) {
							input.put(InfinityConstants.id, HelperMethods.getNewId());
							ServiceCallHelper.invokeServiceAndGetJson(input, headerMap,
									URLConstants.CUSTOMER_LIMIT_GROUP_LIMITS_CREATE);
						} else {

							input.put(InfinityConstants.id,
									limitsIds.get(coreCustomer).get(limitGroupId).get(limitTypeId));
							ServiceCallHelper.invokeServiceAndGetJson(input, headerMap,
									URLConstants.CUSTOMER_LIMIT_GROUP_LIMITS_UPDATE);
						}
                    }
                }
            }
        
        }
		

        for (String coreCustomerId : featureActionDTOs.keySet()) {
            FeatureActionLimitsDTO featureActionDTO = featureActionDTOs.get(coreCustomerId);
            if (!globalActions.containsKey(coreCustomerId)) {
                globalActions.put(coreCustomerId, new HashMap<String, Map<String, Boolean>>());
            }

            for (Entry<String, Set<String>> featureEntry : featureActionDTO.getFeatureaction().entrySet()) {
                for (String actionId : featureEntry.getValue()) {
                    if ((!globalActions.get(coreCustomerId).containsKey(featureEntry.getKey())
                            || !globalActions.get(coreCustomerId).get(featureEntry.getKey()).containsKey(actionId))
                            && !isAccountLevel(actionId, featureActionDTO.getActionsInfo().get(actionId))) {
                        if (!globalActions.get(coreCustomerId).containsKey(featureEntry.getKey())) {
                            globalActions.get(coreCustomerId).put(featureEntry.getKey(),
                                    new HashMap<String, Boolean>());
                        }
                        globalActions.get(coreCustomerId).get(featureEntry.getKey()).put(actionId, false);
                    }
                }
            }

            if (!accountActions.containsKey(coreCustomerId)) {
                accountActions.put(coreCustomerId, new HashMap<String, Map<String, Map<String, Boolean>>>());
                transactionLimits.put(coreCustomerId,
                        new HashMap<String, Map<String, Map<String, Map<String, String>>>>());
            }

            if (!limitGroups.containsKey(coreCustomerId)) {
                limitGroups.put(coreCustomerId, new HashMap<String, Map<String, Map<String, Double>>>());
            }

            if (!transactionLimits.containsKey(coreCustomerId)) {
                transactionLimits.put(coreCustomerId,
                        new HashMap<String, Map<String, Map<String, Map<String, String>>>>());
            }

            for (String accountId : accountMap.get(coreCustomerId).keySet()) {
                if (!accountActions.get(coreCustomerId).containsKey(accountId)) {
                    accountActions.get(coreCustomerId).put(accountId, new HashMap<String, Map<String, Boolean>>());
                }

                if (!transactionLimits.get(coreCustomerId).containsKey(accountId)) {
                    transactionLimits.get(coreCustomerId).put(accountId,
                            new HashMap<String, Map<String, Map<String, String>>>());
                }

                for (Entry<String, Set<String>> featureEntry : featureActionDTO.getFeatureaction().entrySet()) {
                    for (String actionId : featureEntry.getValue()) {
                        if ((!accountActions.get(coreCustomerId).get(accountId).containsKey(featureEntry.getKey())
                                || !accountActions.get(coreCustomerId).get(accountId).get(featureEntry.getKey())
                                        .containsKey(actionId))
                                && isAccountLevel(actionId, featureActionDTO.getActionsInfo().get(actionId))) {
                            if (!accountActions.get(coreCustomerId).get(accountId)
                                    .containsKey(featureEntry.getKey())) {
                                accountActions.get(coreCustomerId).get(accountId).put(featureEntry.getKey(),
                                        new HashMap<String, Boolean>());
                            }
                            accountActions.get(coreCustomerId).get(accountId).get(featureEntry.getKey()).put(
                                    actionId,
                                    false);
                        }
                    }
                }
            }

            for (String accountId : accountMap.get(coreCustomerId).keySet()) {
                for (String featureId : featureActionDTO.getMonetaryActionLimits().keySet()) {

                    if (!accountActions.get(coreCustomerId).get(accountId).containsKey(featureId)) {
                        accountActions.get(coreCustomerId).get(accountId).put(featureId,
                                new HashMap<String, Boolean>());
                    }

                    for (String actionId : featureActionDTO.getMonetaryActionLimits().get(featureId).keySet()) {
                        if (!accountActions.get(coreCustomerId).get(accountId).get(featureId)
                                .containsKey(actionId)) {
                            accountActions.get(coreCustomerId).get(accountId).get(featureId).put(actionId, false);
                        }
                    }
                }
            }

        }

        JsonArray globalActionLimits = new JsonArray();
        Set<Entry<String, Map<String, Map<String, Boolean>>>> entries = globalActions.entrySet();
        for (Entry<String, Map<String, Map<String, Boolean>>> entry : entries) {
            JsonObject jsonObject = new JsonObject();

            for (Entry<String, JsonElement> entrySet : contractCoreCustomers.get(entry.getKey()).entrySet()) {
                jsonObject.add(entrySet.getKey(), entrySet.getValue());
                if (InfinityConstants.contractId.equalsIgnoreCase(entrySet.getKey())) {
                    for (Entry<String, JsonElement> contractEntrySet : contracts
                            .get(entrySet.getValue().getAsString())
                            .entrySet()) {
                        if ("name".equalsIgnoreCase(contractEntrySet.getKey()))
                            jsonObject.add("contractName", contractEntrySet.getValue());
                        else
                            jsonObject.add(contractEntrySet.getKey(), contractEntrySet.getValue());
                    }
                }
            }

            jsonObject.addProperty(InfinityConstants.companyName, companyMap.get(entry.getKey()).get(0));
            jsonObject.addProperty(InfinityConstants.cif, entry.getKey());
            if (coreCustomerGroupInfo.containsKey(entry.getKey())
                    && coreCustomerGroupInfo.get(entry.getKey()) != null) {
                for (Entry<String, JsonElement> entrySet : coreCustomerGroupInfo.get(entry.getKey()).entrySet()) {
                    jsonObject.add(entrySet.getKey(), entrySet.getValue());
                }
            }
            JsonArray features = new JsonArray();
            JsonArray sortedFeatures = new JsonArray();

            FeatureActionLimitsDTO actionLimitsDTO = featureActionDTOs.get(entry.getKey());

            Set<Entry<String, Map<String, Boolean>>> featureEntries = entry.getValue().entrySet();
            for (Entry<String, Map<String, Boolean>> featureEntry : featureEntries) {
                JsonObject featureJsonObject = actionLimitsDTO.getFeatureInfo().get(featureEntry.getKey());
                JsonObject feature = new JsonObject();
                for (Entry<String, JsonElement> featureJsonEntry : featureJsonObject.entrySet()) {
                    feature.add(featureJsonEntry.getKey(), featureJsonEntry.getValue());
                }

                JsonArray permissions = new JsonArray();
                for (Entry<String, Boolean> actionentry : featureEntry.getValue().entrySet()) {
                    JsonObject permission = new JsonObject();
                    permission.addProperty(InfinityConstants.permissionType, actionentry.getKey().trim());
                    JsonObject action = actionLimitsDTO.getActionsInfo().get(actionentry.getKey());
                    for (Entry<String, JsonElement> actionJsonEntry : action.entrySet()) {
                        permission.add(actionJsonEntry.getKey(), actionJsonEntry.getValue());
                    }
                    permission.addProperty(InfinityConstants.isEnabled, actionentry.getValue().toString());
                    permissions.add(permission);
                }
                feature.add(InfinityConstants.permissions, permissions);
                features.add(feature);
                sortedFeatures = sortJsonArray(features, "featureName");

            }
            jsonObject.add(InfinityConstants.features, sortedFeatures);
            globalActionLimits.add(jsonObject);
        }
        customerJsonObject.add(InfinityConstants.globalLevelPermissions, globalActionLimits);

        JsonArray accountLevelPermissions = new JsonArray();
        for (Entry<String, Map<String, Map<String, Map<String, Boolean>>>> entry : accountActions.entrySet()) {
            JsonObject jsonObject = new JsonObject();

            for (Entry<String, JsonElement> entrySet : contractCoreCustomers.get(entry.getKey()).entrySet()) {
                jsonObject.add(entrySet.getKey(), entrySet.getValue());
                if (InfinityConstants.contractId.equalsIgnoreCase(entrySet.getKey())) {
                    for (Entry<String, JsonElement> contractEntrySet : contracts
                            .get(entrySet.getValue().getAsString())
                            .entrySet()) {
                        if ("name".equalsIgnoreCase(contractEntrySet.getKey()))
                            jsonObject.add("contractName", contractEntrySet.getValue());
                        else
                            jsonObject.add(contractEntrySet.getKey(), contractEntrySet.getValue());
                    }
                }
            }
            jsonObject.addProperty(InfinityConstants.companyName, companyMap.get(entry.getKey()).get(0));
            jsonObject.addProperty(InfinityConstants.cif, entry.getKey());
            if (coreCustomerGroupInfo.containsKey(entry.getKey())
                    && coreCustomerGroupInfo.get(entry.getKey()) != null) {
                for (Entry<String, JsonElement> entrySet : coreCustomerGroupInfo.get(entry.getKey()).entrySet()) {
                    jsonObject.add(entrySet.getKey(), entrySet.getValue());
                }
            }
            JsonArray accounts = new JsonArray();

            FeatureActionLimitsDTO actionLimitsDTO = featureActionDTOs.get(entry.getKey().trim());

            Set<Entry<String, Map<String, Map<String, Boolean>>>> accountEntries = entry.getValue().entrySet();
            for (Entry<String, Map<String, Map<String, Boolean>>> accountEntry : accountEntries) {
                JsonObject account = new JsonObject();
                if (!accountMap.containsKey(entry.getKey())
                        || !accountMap.get(entry.getKey()).containsKey(accountEntry.getKey())) {
                    continue;
                }
                String accountName = accountMap.get(entry.getKey()).get(accountEntry.getKey())
                        .get(InfinityConstants.accountName);
                String accountType = accountMap.get(entry.getKey()).get(accountEntry.getKey())
                        .get(InfinityConstants.accountType);

                String accountStatus = accountMap.get(entry.getKey()).get(accountEntry.getKey())
                        .get(InfinityConstants.accountStatus);
                if(!isSuperAdmin && StringUtils.equals(accountStatus, InfinityConstants.ACCOUNTSTATUS_CLOSED)) {
                	continue;
                }
                account.addProperty(InfinityConstants.accountName, accountName);
                account.addProperty(InfinityConstants.accountType, accountType);
                account.addProperty(InfinityConstants.accountId, accountEntry.getKey());
                account.addProperty("ownerType", accountMap.get(entry.getKey()).get(accountEntry.getKey())
                        .get("ownerType"));
                JsonArray features = new JsonArray();
                Set<Entry<String, Map<String, Boolean>>> featureEntries = accountEntry.getValue().entrySet();
                for (Entry<String, Map<String, Boolean>> featureEntry : featureEntries) {
                    JsonObject featureJsonObject = actionLimitsDTO.getFeatureInfo().get(featureEntry.getKey());
                    JsonObject feature = new JsonObject();
                    for (Entry<String, JsonElement> featureJsonEntry : featureJsonObject.entrySet()) {
                        feature.add(featureJsonEntry.getKey(), featureJsonEntry.getValue());
                    }

                    boolean isEnabled = false;
                    JsonArray permissions = new JsonArray();
                    for (Entry<String, Boolean> actionEntry : featureEntry.getValue().entrySet()) {
                        JsonObject permission = new JsonObject();

                        JsonObject action = actionLimitsDTO.getActionsInfo().get(actionEntry.getKey());
                        for (Entry<String, JsonElement> actionJsonEntry : action.entrySet()) {
                            permission.add(actionJsonEntry.getKey(), actionJsonEntry.getValue());
                        }
                        permission.addProperty(InfinityConstants.id, actionEntry.getKey());
                        if (actionEntry.getValue()) {
                            isEnabled = true;
                        }
                        permission.addProperty(InfinityConstants.isEnabled, actionEntry.getValue().toString());
                        permissions.add(permission);
                    }

                    feature.addProperty(InfinityConstants.isEnabled, "" + isEnabled);
                    feature.add(InfinityConstants.permissions, permissions);
                    features.add(feature);
                }
                account.add(InfinityConstants.featurePermissions, features);
                accounts.add(account);
            }
            jsonObject.add(InfinityConstants.accounts, accounts);
            accountLevelPermissions.add(jsonObject);
        }
        customerJsonObject.add(InfinityConstants.accountLevelPermissions, accountLevelPermissions);

        JsonArray transactionLimitsJsonArray = new JsonArray();
        Set<Entry<String, Map<String, Map<String, Map<String, Map<String, String>>>>>> transactionEntries =
                transactionLimits.entrySet();
        for (Entry<String, Map<String, Map<String, Map<String, Map<String, String>>>>> entry : transactionEntries) {
            JsonObject jsonObject = new JsonObject();

            for (Entry<String, JsonElement> entrySet : contractCoreCustomers.get(entry.getKey()).entrySet()) {
                jsonObject.add(entrySet.getKey(), entrySet.getValue());
                if (InfinityConstants.contractId.equalsIgnoreCase(entrySet.getKey())) {
                    for (Entry<String, JsonElement> contractEntrySet : contracts
                            .get(entrySet.getValue().getAsString())
                            .entrySet()) {
                        if ("name".equalsIgnoreCase(contractEntrySet.getKey()))
                            jsonObject.add("contractName", contractEntrySet.getValue());
                        else
                            jsonObject.add(contractEntrySet.getKey(), contractEntrySet.getValue());
                    }
                }
            }

            jsonObject.addProperty(InfinityConstants.companyName, companyMap.get(entry.getKey()).get(0));
            jsonObject.addProperty(InfinityConstants.cif, entry.getKey());
            JsonArray accounts = new JsonArray();
            Set<Entry<String, Map<String, Map<String, Map<String, String>>>>> accountEntries =
                    entry.getValue().entrySet();
            FeatureActionLimitsDTO actionLimitsDTO = featureActionDTOs.get(entry.getKey().trim());

            for (Entry<String, Map<String, Map<String, Map<String, String>>>> accountEntry : accountEntries) {
                JsonObject account = new JsonObject();
                if (!accountMap.containsKey(entry.getKey())
                        || !accountMap.get(entry.getKey()).containsKey(accountEntry.getKey())) {
                    continue;
                }
                String accountName = accountMap.get(entry.getKey()).get(accountEntry.getKey())
                        .get(InfinityConstants.accountName);
                String accountType = accountMap.get(entry.getKey()).get(accountEntry.getKey())
                        .get(InfinityConstants.accountType);
                String accountStatus = accountMap.get(entry.getKey()).get(accountEntry.getKey())
                        .get(InfinityConstants.accountStatus);
                if(!isSuperAdmin && StringUtils.equals(accountStatus, InfinityConstants.ACCOUNTSTATUS_CLOSED)) {
                	continue;
                }
                account.addProperty(InfinityConstants.accountName, accountName);
                account.addProperty(InfinityConstants.accountType, accountType);
                account.addProperty(InfinityConstants.accountId, accountEntry.getKey());
                account.addProperty("ownerType", accountMap.get(entry.getKey()).get(accountEntry.getKey())
                        .get("ownerType"));
                JsonArray featurePermissions = new JsonArray();
                Set<Entry<String, Map<String, Map<String, String>>>> featureEntries =
                        accountEntry.getValue().entrySet();
                for (Entry<String, Map<String, Map<String, String>>> featureEntry : featureEntries) {
                    Set<Entry<String, Map<String, String>>> actionEntries = featureEntry.getValue().entrySet();
                    for (Entry<String, Map<String, String>> actionEntry : actionEntries) {
                        JsonObject featureJsonObject =
                                actionLimitsDTO.getFeatureInfo().get(featureEntry.getKey());
                        JsonObject feature = new JsonObject();
                        for (Entry<String, JsonElement> featureJsonEntry : featureJsonObject.entrySet()) {
                            feature.add(featureJsonEntry.getKey(), featureJsonEntry.getValue());
                        }
                        String actionId = actionEntry.getKey();
                        feature.addProperty(InfinityConstants.actionId, actionId);
                        JsonObject action = actionLimitsDTO.getActionsInfo().get(actionId);
                        for (Entry<String, JsonElement> actionJsonEntry : action.entrySet()) {
                            feature.add(actionJsonEntry.getKey(), actionJsonEntry.getValue());
                        }

                        feature.addProperty(InfinityConstants.limitGroupId,
                                actionEntry.getValue().get(InfinityConstants.limitGroupId));
                        actionEntry.getValue().remove(InfinityConstants.limitGroupId);

                        feature.addProperty(InfinityConstants.isEnabled,
                                actionEntry.getValue().get(InfinityConstants.isEnabled));
                        actionEntry.getValue().remove(InfinityConstants.isEnabled);

                        JsonArray limits = new JsonArray();
                        Set<Entry<String, String>> limitEntries = actionEntry.getValue().entrySet();
                        for (Entry<String, String> limitEntry : limitEntries) {
                            JsonObject limitJson = new JsonObject();
                            limitJson.addProperty(InfinityConstants.id, limitEntry.getKey());
                            limitJson.addProperty(InfinityConstants.value, limitEntry.getValue());
                            limits.add(limitJson);
                        }

                        feature.add(InfinityConstants.limits, limits);
                        featurePermissions.add(feature);
                    }
                }
                account.add(InfinityConstants.featurePermissions, featurePermissions);
                accounts.add(account);
            }

            jsonObject.add(InfinityConstants.accounts, accounts);
            transactionLimitsJsonArray.add(jsonObject);
        }


        for (Entry<String, Map<String, Map<String, Map<String, Double>>>> cifEntry : limitGroups.entrySet()) {
          if (!limitsMap.containsKey(cifEntry.getKey())) {
            limitsMap.put(cifEntry.getKey(), new HashMap<String, Map<String, Map<String, Double>>>());
		}
            for (Entry<String, Map<String, Map<String, Double>>> accountEntry : cifEntry.getValue().entrySet()) {
                for (Entry<String, Map<String, Double>> limitGroupEntry : accountEntry.getValue().entrySet()) {
                    if (!limitsMap.get(cifEntry.getKey()).containsKey(limitGroupEntry.getKey())) {
                        limitsMap.get(cifEntry.getKey()).put(limitGroupEntry.getKey(),
                                new HashMap<String, Map<String, Double>>());
                    }
                    for (Entry<String, Double> limitTypeEntry : limitGroupEntry.getValue().entrySet()) {

                        if (!limitsMap.get(cifEntry.getKey()).get(limitGroupEntry.getKey())
                                .containsKey(limitTypeEntry.getKey())) {
                            limitsMap.get(cifEntry.getKey()).get(limitGroupEntry.getKey()).put(limitTypeEntry.getKey(),
                                    new HashMap<String, Double>());
						}
                        if (!limitsMap.get(cifEntry.getKey()).get(limitGroupEntry.getKey()).get(limitTypeEntry.getKey())
                                .containsKey(InfinityConstants.maxValue)) {
                            limitsMap.get(cifEntry.getKey()).get(limitGroupEntry.getKey()).get(limitTypeEntry.getKey())
                                    .put(InfinityConstants.maxValue, limitTypeEntry.getValue());
                        } else if (limitTypeEntry.getKey().equals(InfinityConstants.MAX_TRANSACTION_LIMIT)) {
                            Double value = limitsMap.get(cifEntry.getKey()).get(limitGroupEntry.getKey())
                                    .get(limitTypeEntry.getKey()).get(InfinityConstants.maxValue);
                            limitsMap.get(cifEntry.getKey()).get(limitGroupEntry.getKey()).get(limitTypeEntry.getKey())
                                    .put(InfinityConstants.maxValue, Math.max(value, limitTypeEntry.getValue()));
                        } else {
                            Double value = limitsMap.get(cifEntry.getKey()).get(limitGroupEntry.getKey())
                                    .get(limitTypeEntry.getKey()).get(InfinityConstants.maxValue);
                            limitsMap.get(cifEntry.getKey()).get(limitGroupEntry.getKey()).get(limitTypeEntry.getKey())
                                    .put(InfinityConstants.maxValue, value + limitTypeEntry.getValue());
                        }
                    }
                }
            }
        }

        for (String cif : limitGroups.keySet()) {

            Map<String, Object> input = new HashMap<String, Object>();
            String filter =
                    InfinityConstants.coreCustomerId + DBPUtilitiesConstants.EQUAL + cif + DBPUtilitiesConstants.AND
                            + InfinityConstants.Customer_id + DBPUtilitiesConstants.EQUAL + customerId;
            input.put(DBPUtilitiesConstants.FILTER, filter);
            JsonObject response = ServiceCallHelper.invokeServiceAndGetJson(input, headerMap,
                    URLConstants.CUSTOMER_LIMIT_GROUP_LIMITS_GET);
            if (response.has(DBPDatasetConstants.DATASET_CUSTOMRLIMITGROUPLIMITS)) {
                JsonElement jsonElement = response.get(DBPDatasetConstants.DATASET_CUSTOMRLIMITGROUPLIMITS);
                if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
                    for (JsonElement element : jsonElement.getAsJsonArray()) {
                        String limitTypeId =
                                element.getAsJsonObject().get(InfinityConstants.LimitType_id).getAsString();
                        String limitGroupId =
                                element.getAsJsonObject().get(InfinityConstants.limitGroupId).getAsString();
                        String value = element.getAsJsonObject().get(InfinityConstants.value).getAsString();
                        if (!limitsMap.get(cif).containsKey(limitGroupId)) {
                            limitsMap.get(cif).put(limitGroupId, new HashMap<String, Map<String, Double>>());
                        }

                        if (!limitsMap.get(cif).get(limitGroupId).containsKey(limitTypeId)) {
                            limitsMap.get(cif).get(limitGroupId).put(limitTypeId, new HashMap<String, Double>());
                        }
                        limitsMap.get(cif).get(limitGroupId).get(limitTypeId).put(InfinityConstants.value,
                                Double.parseDouble(value));
                    }

                }
            }
        }

        String locale = "en-US";
        if (headerMap.containsKey("locale") && headerMap.get("locale") != null
                && StringUtils.isNotBlank(headerMap.get("locale").toString()))
            locale = headerMap.get("locale").toString();

        String filter = InfinityConstants.localeId + DBPUtilitiesConstants.EQUAL + locale;
        Map<String, JsonObject> limitGroupMap = new HashMap<String, JsonObject>();
        Map<String, Object> input = new HashMap<String, Object>();
        input.put(DBPUtilitiesConstants.FILTER, filter);
        JsonObject response = ServiceCallHelper.invokeServiceAndGetJson(input, headerMap,
                URLConstants.LIMIT_GROUP_DESCRIPTION_GET);
        if (response.has(DBPDatasetConstants.DATASET_LIMIT_GROUP_DESCRIPTION)) {
            JsonElement jsonElement = response.get(DBPDatasetConstants.DATASET_LIMIT_GROUP_DESCRIPTION);
            if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
                for (JsonElement element : jsonElement.getAsJsonArray()) {
                    JsonObject limitGroupJsonObject = element.getAsJsonObject();
                    limitGroupMap.put(limitGroupJsonObject.get(InfinityConstants.limitGroupId).getAsString(),
                            limitGroupJsonObject);
                }
            }
        }

        for (Entry<String, Map<String, Map<String, Map<String, Double>>>> cifEntry : limitsMap.entrySet()) {

            JsonObject limitJsonObject = new JsonObject();
            boolean matchFound = false;

            for (JsonElement element : transactionLimitsJsonArray) {
                JsonObject cifJsonObject = element.getAsJsonObject();
                if (cifJsonObject.has(InfinityConstants.cif)
                        && !cifJsonObject.get(InfinityConstants.cif).isJsonNull()
                        && cifJsonObject.get(InfinityConstants.cif).getAsString().equals(cifEntry.getKey())) {
                    limitJsonObject = cifJsonObject;
                    matchFound = true;
                }
            }
            if (!matchFound) {
                limitJsonObject.addProperty(InfinityConstants.companyName, companyMap.get(cifEntry.getKey()).get(0));
                limitJsonObject.addProperty(InfinityConstants.cif, cifEntry.getKey());
            }
            JsonArray limitGroupids = new JsonArray();
            for (Entry<String, Map<String, Map<String, Double>>> limitGroupEntry : cifEntry.getValue().entrySet()) {

                JsonObject limitGroup = new JsonObject();
                JsonArray limits = new JsonArray();
                limitGroup.addProperty(InfinityConstants.limitGroupId, limitGroupEntry.getKey());
                if (limitGroupMap.containsKey(limitGroupEntry.getKey())) {
                    limitGroup.add(InfinityConstants.limitGroupName,
                            limitGroupMap.get(limitGroupEntry.getKey()).get(InfinityConstants.displayName));
                    limitGroup.add(InfinityConstants.limitGroupDescription,
                            limitGroupMap.get(limitGroupEntry.getKey()).get(InfinityConstants.displayDescription));
                }
                for (Entry<String, Map<String, Double>> limitsEntry : limitGroupEntry.getValue().entrySet()) {
                    JsonObject limit = new JsonObject();
                    limit.addProperty(InfinityConstants.id, limitsEntry.getKey());
                    limit.addProperty(InfinityConstants.maxValue,
                            limitsEntry.getValue().containsKey(InfinityConstants.maxValue)
                                    ? limitsEntry.getValue().get(InfinityConstants.maxValue).toString()
                                    : "0.0");
                    limit.addProperty(InfinityConstants.value,
                            limitsEntry.getValue().containsKey(InfinityConstants.value)
                                    ? limitsEntry.getValue().get(InfinityConstants.value).toString()
                                    : "0.0");
                    limit.addProperty(InfinityConstants.minValue, "0.0");
                    limits.add(limit);
                }
                limitGroup.add(InfinityConstants.limits, limits);

                limitGroupids.add(limitGroup);
            }

            limitJsonObject.add(InfinityConstants.limitGroups, limitGroupids);
            if (!matchFound) {
                transactionLimitsJsonArray.add(limitJsonObject);
            }
        }
        customerJsonObject.add(InfinityConstants.transactionLimits, transactionLimitsJsonArray);
    }

    private void addActions(ActionLimitsDTO dto, Map<String, Object> headerMap, boolean isCustomRoleFlow) {
        Callable<Result> callable = new Callable<Result>() {
            public Result call() {
                try {
                    addActionsAsync(dto, headerMap, isCustomRoleFlow);
                } catch (Exception e) {
                }
                return new Result();
            }

            private void addActionsAsync(ActionLimitsDTO dto, Map<String, Object> headerMap, boolean isCustomRoleFlow) {
                LimitsAndPermissionsBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl
                        .getBackendDelegate(LimitsAndPermissionsBackendDelegate.class);
                if (!isCustomRoleFlow) {
                    backendDelegate.addActionsToCustomer(dto, headerMap);
                } else {
                    backendDelegate.addActionsToCustomRole(dto, headerMap);
                }
            }
        };
        try {
            ThreadExecutor.getExecutor().execute(callable);
        } catch (Exception e) {
            alert.prepareError("ThreadExecutor : Exception occured while adding new action to  "
                    + (isCustomRoleFlow ? "customRole" + dto.getCustomRoleId() : "infinity User" + dto.getCustomerId()),
                    e).log();
        }
    }

    private void getcoreCustomerContract(Map<String, Map<String, Map<String, Map<String, String>>>> map,
            Map<String, String> contractsMap) {
        for (String contract : map.keySet()) {
            for (String coreCustomerId : map.get(contract).keySet()) {
                contractsMap.put(coreCustomerId, contract);
            }
        }

    }
    
    private JsonArray sortJsonArray(JsonArray features, String keyname) {
    	JsonArray sortedFeatures = new JsonArray();
    	List<JsonObject> jsonValues = new ArrayList<JsonObject>();
    	for (int i = 0; i < features.size(); i++) {
    		jsonValues.add((JsonObject) features.get(i));
    	}
    	Collections.sort(jsonValues, new Comparator<JsonObject>() {
    	@Override
	    	public int compare(JsonObject a, JsonObject b) {
		    	String valA = new String();
		    	String valB = new String();
		    	try {
		    		valA = (String) a.get(keyname).getAsString();
		    		valB = (String) b.get(keyname).getAsString();
		    	} 
		    	catch (JSONException e) {
		    		//do something
		    	}
		    	return valA.compareTo(valB);
	    	}
    	});
    	for (int i = 0; i < features.size(); i++) {
    		sortedFeatures.add(jsonValues.get(i));
    	}
    	return sortedFeatures;
    }
 
    private boolean isAccountLevel(String actionId, JsonObject jsonObject) {
        if (jsonObject == null) {
            return true;
        }
        return jsonObject.has(InfinityConstants.isAccountLevel)
                && !jsonObject.get(InfinityConstants.isAccountLevel).isJsonNull() &&
                ("1".equals(jsonObject.get(InfinityConstants.isAccountLevel).getAsString())
                        || Boolean.parseBoolean(jsonObject.get(InfinityConstants.isAccountLevel).getAsString()));
    }

    private void getUserContracts(String customerId, String contractIdInput, String coreCustomerIdInput, Map<String, Set<String>> customerContracts,
            Map<String, Object> headerMap, Map<String, Boolean> autoSyncAccountsMap, boolean isCustomRoleFlow, String legalEntityId) {
        JsonArray jsonArray = getUserContracts(customerId, contractIdInput, coreCustomerIdInput, headerMap, isCustomRoleFlow, legalEntityId);

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
            autoSyncAccountsMap.put(coreCustomerId,
                    Boolean.parseBoolean(customerContract.has(InfinityConstants.autoSyncAccounts)
                            && !customerContract.get(InfinityConstants.autoSyncAccounts).isJsonNull()
                                    ? customerContract.get(InfinityConstants.autoSyncAccounts).getAsString()
                                    : null));
            if (!customerContracts.containsKey(contractId)) {
                customerContracts.put(contractId, new HashSet<String>());
            }

            if (customerContracts.containsKey(contractId)
                    && !customerContracts.get(contractId).contains(coreCustomerId)) {
                customerContracts.get(contractId).add(coreCustomerId);
            }
        }
    }

    private JsonArray getUserContracts(String customerId, String contractIdInput, String coreCustomerIdInput, Map<String, Object> headerMap, boolean isCustomRoleFlow, String legalEntityId) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(customerId)) {
            if (!isCustomRoleFlow) {
                String filter = InfinityConstants.customerId + DBPUtilitiesConstants.EQUAL + customerId
                		+ DBPUtilitiesConstants.AND + InfinityConstants.LegalEntityId + DBPUtilitiesConstants.EQUAL + legalEntityId;
                if(StringUtils.isNotBlank(contractIdInput) && StringUtils.isNotBlank(coreCustomerIdInput)) {
                	filter += DBPUtilitiesConstants.AND + InfinityConstants.contractId + DBPUtilitiesConstants.EQUAL + contractIdInput 
                			+ DBPUtilitiesConstants.AND + InfinityConstants.coreCustomerId + DBPUtilitiesConstants.EQUAL + coreCustomerIdInput;
                }
                diagnostic.prepareDebug("filter for contractcustomers: ").log();
                map.put(DBPUtilitiesConstants.FILTER, filter);
                JsonObject jsonObject =
                        ServiceCallHelper.invokeServiceAndGetJson(map, headerMap, URLConstants.CONTRACT_CUSTOMERS_GET);

                if (jsonObject.has(DBPDatasetConstants.DATASET_CONTRACT_CUSTOMERS)) {
                    JsonElement jsonElement = jsonObject.get(DBPDatasetConstants.DATASET_CONTRACT_CUSTOMERS);
                    if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
                        JsonArray jsonArray = jsonElement.getAsJsonArray();
                        return jsonArray;
                    }
                }
            } else {
                String filter = InfinityConstants.customRoleId + DBPUtilitiesConstants.EQUAL + customerId;

                Map<String, Object> input = new HashMap<String, Object>();
                input.put(DBPUtilitiesConstants.FILTER, filter);
                if(StringUtils.isNotBlank(contractIdInput) && StringUtils.isNotBlank(coreCustomerIdInput)) {
                	filter += DBPUtilitiesConstants.AND + InfinityConstants.contractId + DBPUtilitiesConstants.EQUAL + contractIdInput 
                			+ DBPUtilitiesConstants.AND + InfinityConstants.coreCustomerId + DBPUtilitiesConstants.EQUAL + coreCustomerIdInput;
                }
                diagnostic.prepareDebug("filter for contractscustomrole: " + filter).log();

                JsonObject jsonObject = ServiceCallHelper.invokeServiceAndGetJson(input, headerMap,
                        URLConstants.CONTRACT_CUSTOM_ROLE_GET);
                if (jsonObject.has(DBPDatasetConstants.DATASET_CONTRACTCUSTOMROLE)) {
                    JsonElement jsonElement = jsonObject.get(DBPDatasetConstants.DATASET_CONTRACTCUSTOMROLE);
                    if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
                        return jsonElement.getAsJsonArray();
                    }
                }
            }
        }

        return new JsonArray();
    }

    private void getContractCIFs(Map<String, Set<String>> contractCIFs,
    		String contractIdInput,
    		String coreCustomerIdInput,
            Map<String, Object> headerMap) {
        Map<String, Object> map = new HashMap<String, Object>();
        
//      String filter= "";
//        
//        if(StringUtils.isNotBlank(contractIdInput) && StringUtils.isNotBlank(coreCustomerIdInput)) {
//			filter += InfinityConstants.contractId + DBPUtilitiesConstants.EQUAL + contractIdInput 
//			+ DBPUtilitiesConstants.AND + InfinityConstants.coreCustomerId + DBPUtilitiesConstants.EQUAL + coreCustomerIdInput;
//        }
//        else if(StringUtils.isNotBlank(contractIdInput) && contractIdInput.contains(",")) {
//         String contractids[] = contractIdInput.split(",");
//         ArrayList<String> contractIdsList = (ArrayList<String>) Arrays.asList(contractids);
//         contractIdInput = contractIdsList.stream().collect(Collectors.joining("','", "'", "'"));
//         
//        }
//        
//        diagnostic.prepareDebug("filter for contractcorecustomer..... ").log();
//        diagnostic.prepareDebug("contractIdInput: "+contractIdInput + "coreCustomerIdInput:"+ coreCustomerIdInput + "filter: "+ filter).log();
//        
//        map.put("$filter", filter);
        
        diagnostic.prepareDebug("coreCustomerIdInput: "+coreCustomerIdInput).log();
        diagnostic.prepareDebug("contractId: "+contractIdInput).log();
        diagnostic.prepareDebug("coreCustomerIdInput: "+coreCustomerIdInput).log();
        
        if(StringUtils.isNotBlank(contractIdInput) && StringUtils.isNotBlank(coreCustomerIdInput)) {
        	map.put("_contractIdList","");
        	map.put("_contractId", contractIdInput);
        	map.put("_coreCustomerId", coreCustomerIdInput);
        }
        else if(StringUtils.isNotBlank(contractIdInput) && StringUtils.isBlank(coreCustomerIdInput)) {
        	map.put("_contractIdList",contractIdInput);
        	map.put("_contractId", "");
        	map.put("_coreCustomerId", "");
        }
        
        diagnostic.prepareDebug("input for fetch_contractaccounts_for_user_proc: "+map.toString()).log();
        
        JsonObject jsonObject =
        		ServiceCallHelper.invokeServiceAndGetJson(map, headerMap, URLConstants.FETCH_CONTRACTCORECUSTOMERS_FOR_USER_PROC);
        diagnostic.prepareDebug("response from fetch_contratcorecustomers_for_user_proc: "+jsonObject.toString()).log();
        if (jsonObject.has(DBPDatasetConstants.DATASET_RECORDS)) {
        	JsonElement jsonElement = jsonObject.get(DBPDatasetConstants.DATASET_RECORDS);
            if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
                JsonArray jsonArray = jsonElement.getAsJsonArray();
                diagnostic.prepareDebug(" contractcorecustomer response array size : "+jsonArray.size()).log();
                for (int i = 0; i < jsonArray.size(); i++) {
                    JsonObject contractCoreCustomer = jsonArray.get(i).getAsJsonObject();
                    String contractId = contractCoreCustomer.has(InfinityConstants.contractId)
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

    private void getAccountsForContract(Map<String, String> accountOwnership,
    		String contractIdInput,
    		String coreCustomerIdInput,
            Map<String, Map<String, Map<String, JsonObject>>> contractAccounts,
            Map<String, Object> headerMap) {

        Map<String, Object> map = new HashMap<String, Object>();
        String filter = "";
//      if(StringUtils.isNotBlank(contractIdInput) && StringUtils.isNotBlank(coreCustomerIdInput)) {
//			filter +=  InfinityConstants.contractId + DBPUtilitiesConstants.EQUAL + contractIdInput 
//			+ DBPUtilitiesConstants.AND + InfinityConstants.coreCustomerId + DBPUtilitiesConstants.EQUAL + coreCustomerIdInput;
//      }
//      diagnostic.prepareDebug("contractIdInput: "+contractIdInput + "coreCustomerIdInput:"+ coreCustomerIdInput + "filter: "+ filter).log();
//      map.put("$filter", filter);
      
      diagnostic.prepareDebug("coreCustomerIdInput: "+coreCustomerIdInput).log();
      diagnostic.prepareDebug("contractId: "+contractIdInput).log();
      diagnostic.prepareDebug("coreCustomerIdInput: "+coreCustomerIdInput).log();
      
     if(StringUtils.isNotBlank(contractIdInput) && StringUtils.isNotBlank(coreCustomerIdInput)) {
      	map.put("_contractIdList","");
      	map.put("_contractId", contractIdInput);
      	map.put("_coreCustomerId", coreCustomerIdInput);
      }
      else if(StringUtils.isNotBlank(contractIdInput) && StringUtils.isBlank(coreCustomerIdInput)) {
      	map.put("_contractIdList",contractIdInput);
      	map.put("_contractId", "");
      	map.put("_coreCustomerId", "");
      }
      
      diagnostic.prepareDebug("input for fetch_contractaccounts_for_user_proc: "+map.toString()).log();
      
        JsonObject jsonObject =
        		ServiceCallHelper.invokeServiceAndGetJson(map, headerMap, URLConstants.FETCH_CONTRACTACCOUNTS_FOR_USER_PROC);
        diagnostic.prepareDebug("response from fetch_contractaccounts_for_user_proc: " +jsonObject.toString()).log();
        if (jsonObject.has(DBPDatasetConstants.DATASET_RECORDS)) {
        	JsonElement jsonElement = jsonObject.get(DBPDatasetConstants.DATASET_RECORDS);
            if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
                JsonArray jsonArray = jsonElement.getAsJsonArray();
                diagnostic.prepareDebug("response of contractaccount array seize:" + jsonArray.size()).log();
                for (int i = 0; i < jsonArray.size(); i++) {
                    JsonObject account = jsonArray.get(i).getAsJsonObject();
                    accountOwnership.put(JSONUtil.getString(account, "accountId"),
                            JSONUtil.getString(account, "ownerType"));
                    String contractId = account.has(InfinityConstants.contractId)
                            && !account.get(InfinityConstants.contractId).isJsonNull()
                                    ? account.get(InfinityConstants.contractId).getAsString()
                                    : null;

                    String coreCustomerId = account.has(InfinityConstants.coreCustomerId)
                            && !account.get(InfinityConstants.coreCustomerId).isJsonNull()
                                    ? account.get(InfinityConstants.coreCustomerId).getAsString()
                                    : null;

                    if (!contractAccounts.containsKey(contractId)) {
                        contractAccounts.put(contractId, new HashMap<String, Map<String, JsonObject>>());
                    }

                    if (!contractAccounts.get(contractId).containsKey(coreCustomerId)) {
                        contractAccounts.get(contractId).put(coreCustomerId, new HashMap<String, JsonObject>());
                    }
                    contractAccounts.get(contractId).get(coreCustomerId)
                            .put(account.get(InfinityConstants.accountId).getAsString(), account);
                }
            }
        }
    }

    private String getAccountsForCustomer(String customerId, 
    		String contractIdInput, 
    		String coreCustomerIdInput, 
    		Map<String, Set<String>> customerAccounts,
    		Map<String, Object> headerMap, String legalEntityId) {

        Map<String, Object> map = new HashMap<String, Object>();
        StringBuilder sb= new StringBuilder();
        if (StringUtils.isNotBlank(customerId)) {
            String filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + customerId 
            		+ DBPUtilitiesConstants.AND + InfinityConstants.LegalEntityId + DBPUtilitiesConstants.EQUAL + legalEntityId;
            if(StringUtils.isNotBlank(contractIdInput) && StringUtils.isNotBlank(coreCustomerIdInput)) {
            	filter += DBPUtilitiesConstants.AND + InfinityConstants.contractId + DBPUtilitiesConstants.EQUAL + contractIdInput 
            			+ DBPUtilitiesConstants.AND + InfinityConstants.coreCustomerId + DBPUtilitiesConstants.EQUAL + coreCustomerIdInput;
            }  
            diagnostic.prepareDebug("filter for customeraccounts: "+ filter).log();
            map.put(DBPUtilitiesConstants.FILTER, filter);

            JsonObject jsonObject =
                    ServiceCallHelper.invokeServiceAndGetJson(map, headerMap, URLConstants.CUSTOMERACCOUNTS_GET);

            if (jsonObject.has(DBPDatasetConstants.CUSTOMER_ACCOUNTS_DATASET)) {
                JsonElement jsonElement = jsonObject.get(DBPDatasetConstants.CUSTOMER_ACCOUNTS_DATASET);
                if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
                    JsonArray jsonArray = jsonElement.getAsJsonArray();
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JsonObject account = jsonArray.get(i).getAsJsonObject();
                        String coreCustomerId = account.has(InfinityConstants.coreCustomerId)
                                && !account.get(InfinityConstants.coreCustomerId).isJsonNull()
                                        ? account.get(InfinityConstants.coreCustomerId).getAsString()
                                        : null;

                        if (!customerAccounts.containsKey(coreCustomerId)) {
                            customerAccounts.put(coreCustomerId, new HashSet<String>());
                        }
                        customerAccounts.get(coreCustomerId)
                                .add(account.get("Account_id").getAsString());
                    
                        if (StringUtils.isNotBlank(sb.toString()))
                            sb.append(",");
                        sb.append(account.get("Account_id").getAsString());
                        }
                }
            }
        }
        return sb.toString();


    }

    private JsonArray getCompanyList(Map<String, JsonObject> coreCustomerGroupInfo,
    		String contractIdInput,
    		String coreCustomerIdInput,
            Map<String, JsonObject> contracts,
            Map<String, JsonObject> contractCoreCustomers, String id, Map<String, Object> headerMap,
            Map<String, Map<String, Map<String, Map<String, String>>>> map, Map<String, List<String>> companyMap,
            Map<String, Map<String, Map<String, String>>> accountMap,
            Map<String, FeatureActionLimitsDTO> featureActionDTOs, String loggedInUserId, JsonArray jsonArray,
            boolean isSuperAdmin, boolean isCustomRoleFlow, String legalEntityId) {
        JsonArray companyList = new JsonArray();

        JsonObject companyJsonObject = new JsonObject();

        Map<String, String> accountTypes = new HashMap<>();

        HashMap<String, Object> input = new HashMap<String, Object>();
        JsonObject accoutTypeJson = ServiceCallHelper.invokeServiceAndGetJson(input, headerMap,
                URLConstants.ACCOUNT_TYPE_GET);

        if (accoutTypeJson.has(DBPDatasetConstants.DATASET_ACCOUNTTYPE)) {
            for (JsonElement jsonelement : accoutTypeJson
                    .get(DBPDatasetConstants.DATASET_ACCOUNTTYPE).getAsJsonArray()) {
                accountTypes.put(JSONUtil.getString(jsonelement.getAsJsonObject(), InfinityConstants.TypeID),
                        JSONUtil.getString(jsonelement.getAsJsonObject(), InfinityConstants.displayName));
            }
        }

        Map<String, Set<String>> customerContracts = new HashMap<String, Set<String>>();
        Map<String, Set<String>> customerAccounts = new HashMap<String, Set<String>>();
        Map<String, Boolean> autoSyncAccountsMap = new HashMap<String, Boolean>();
        String loggedinuseraccounts = "";


        if (isSuperAdmin) {
        	getUserContracts(id, contractIdInput, coreCustomerIdInput, customerContracts, headerMap, autoSyncAccountsMap, false, legalEntityId);
        	getAccountsForCustomer(id, contractIdInput, coreCustomerIdInput, customerAccounts, headerMap, legalEntityId);
        } else {
        	getUserContracts(loggedInUserId, contractIdInput, coreCustomerIdInput, customerContracts, headerMap, new HashMap<String, Boolean>(), false, legalEntityId);
        	getUserContracts(id, contractIdInput, coreCustomerIdInput, new HashMap<String, Set<String>>(), headerMap, autoSyncAccountsMap, isCustomRoleFlow, legalEntityId);
        	loggedinuseraccounts = getAccountsForCustomer(loggedInUserId, contractIdInput, coreCustomerIdInput, customerAccounts, headerMap, legalEntityId);
        }

        Map<String, Set<String>> contractCIFs = new HashMap<String, Set<String>>();

        if(StringUtils.isBlank(contractIdInput))
        	contractIdInput = String.join(",",customerContracts.keySet());
        diagnostic.prepareDebug("keyset -- > contractIdInput: "+ contractIdInput).log();
        getContractCIFs(contractCIFs, contractIdInput, coreCustomerIdInput, headerMap);
        Map<String, Map<String, Map<String, JsonObject>>> contractAccounts =
                new HashMap<String, Map<String, Map<String, JsonObject>>>();

        Map<String, String> accountOwnership = new HashMap<>();
        getAccountsForContract(accountOwnership, contractIdInput, coreCustomerIdInput, contractAccounts, headerMap);

        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject2 = jsonArray.get(i).getAsJsonObject();
            String contractId = JSONUtil.getString(jsonObject2, InfinityConstants.contractId);
            String coreCustomerId = JSONUtil.getString(jsonObject2, InfinityConstants.coreCustomerId);
            String accountId = JSONUtil.getString(jsonObject2, InfinityConstants.Account_id);
            String accountName = JSONUtil.getString(jsonObject2, InfinityConstants.AccountName);
            String accountType = JSONUtil.getString(jsonObject2, InfinityConstants.accountType);
            String accountStatus = JSONUtil.getString(jsonObject2, InfinityConstants.accountStatus);

            Map<String, String> account = new HashMap<String, String>();

            account.put(InfinityConstants.accountName, accountName);
            account.put(InfinityConstants.accountType, accountType);
            account.put(InfinityConstants.accountStatus, accountStatus);
            if (StringUtils.isNotBlank(accountOwnership.get("accountId")))
                account.put("ownerType", accountOwnership.get(accountId));
            else
                account.put("ownerType", "Owner");
            if (!contractCIFs.containsKey(contractId) || !customerContracts.containsKey(contractId)
                    || !contractAccounts.containsKey(contractId)) {
                continue;
            }

            if (!map.containsKey(contractId)) {
                map.put(contractId, new HashMap<String, Map<String, Map<String, String>>>());
            }

            if (!contractCIFs.get(contractId).contains(coreCustomerId)
                    || !customerContracts.get(contractId).contains(coreCustomerId)
                    || !contractAccounts.get(contractId).containsKey(coreCustomerId)
                    || !customerAccounts.containsKey(coreCustomerId)) {
                continue;
            }

            if (!map.get(contractId).containsKey(coreCustomerId)) {
                map.get(contractId).put(coreCustomerId, new HashMap<String, Map<String, String>>());
            }

            if (!contractAccounts.get(contractId).get(coreCustomerId).containsKey(accountId)
                    || !customerAccounts.get(coreCustomerId).contains(accountId)) {
                continue;
            }

            if (!map.get(contractId).get(coreCustomerId).containsKey(accountId)) {
                map.get(contractId).get(coreCustomerId).put(accountId, account);
            }

        }

        input = new HashMap<String, Object>();
        JsonObject membershipJsonObject = ServiceCallHelper.invokeServiceAndGetJson(input, headerMap,
                URLConstants.MEMBERGROUP_GET);
        Map<String, String> groupsInfo = new HashMap<>();
        if (membershipJsonObject.has(DBPDatasetConstants.DATASET_MEMBERGROUP)) {
            for (JsonElement jsonelement : membershipJsonObject
                    .get(DBPDatasetConstants.DATASET_MEMBERGROUP).getAsJsonArray()) {
                if (InfinityConstants.SID_ACTIVE
                        .equals(JSONUtil.getString(jsonelement.getAsJsonObject(), InfinityConstants.Status_id))) {
                    groupsInfo.put(JSONUtil.getString(jsonelement.getAsJsonObject(), "id"),
                            JSONUtil.getString(jsonelement.getAsJsonObject(), "Name"));
                }
            }
        }

        Map<String, JsonObject> customerGroups = new HashMap<String, JsonObject>();
        Map<String, JsonArray> groupServiceDefinitions = new HashMap<String, JsonArray>();

        ContractBackendDelegate backendDelegate =
                DBPAPIAbstractFactoryImpl.getBackendDelegate(ContractBackendDelegate.class);

        Map<String, JsonObject> serviceDefinitions = new HashMap<String, JsonObject>();

        JsonObject jsonObject =
                ServiceCallHelper.invokeServiceAndGetJson(new HashMap<String, Object>(), headerMap,
                        URLConstants.SERVICEDEFINITION_GET);
        input.clear();

        if (jsonObject.has(DBPDatasetConstants.DATASET_SERVICEDEFINITION)) {
            JsonElement jsonElement = jsonObject.get(DBPDatasetConstants.DATASET_SERVICEDEFINITION);
            if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
                jsonArray = jsonElement.getAsJsonArray();
                for (JsonElement entrySet : jsonArray) {
                    serviceDefinitions.put(entrySet.getAsJsonObject().get(InfinityConstants.id).getAsString(),
                            entrySet.getAsJsonObject());
                }
            }
        }

        for (String contractEntry : map.keySet()) {

            JsonElement jsonElement = null;
            jsonArray = new JsonArray();
            String contractName = null;
            String serviceDefinitionId = null;
            String contractType = null;
            String companyLegalUnit = null;

            JsonObject contractJsonObject = new JsonObject();

            if (contracts.containsKey(contractEntry)) {
                contractJsonObject = contracts.get(contractEntry);
            } else {
                String filter = InfinityConstants.id + DBPUtilitiesConstants.EQUAL + contractEntry
                		+ DBPUtilitiesConstants.AND + InfinityConstants.LegalEntityId + DBPUtilitiesConstants.EQUAL + legalEntityId;
                input.put(DBPUtilitiesConstants.FILTER, filter);
                jsonObject =
                        ServiceCallHelper.invokeServiceAndGetJson(input, headerMap, URLConstants.CONTRACT_GET);
                input.clear();

                if (jsonObject.has(DBPDatasetConstants.DATASET_CONTRACT)) {
                    jsonElement = jsonObject.get(DBPDatasetConstants.DATASET_CONTRACT);
                    if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
                        jsonArray = jsonElement.getAsJsonArray();
                        for (Entry<String, JsonElement> entrySet : jsonArray.get(0).getAsJsonObject().entrySet()) {
                            contractJsonObject.add(entrySet.getKey(), entrySet.getValue());
                        }
                        contracts.put(contractEntry, contractJsonObject);
                    } else
                        continue;
                } else
                    continue;
            }

            ContractCoreCustomerBackendDelegate contractCoreCustomerBackendDelegate =
                    DBPAPIAbstractFactoryImpl.getBackendDelegate(ContractCoreCustomerBackendDelegate.class);
            List<ContractCoreCustomersDTO> list = new ArrayList<ContractCoreCustomersDTO>();
            try {
                list = contractCoreCustomerBackendDelegate.getContractCoreCustomers(contractEntry, false, true,
                        headerMap);
            } catch (ApplicationException e1) {
            	alert.prepareError("Exception", e1).log();
            }

            serviceDefinitionId = JSONUtil.getString(contractJsonObject, "servicedefinitionId");
            contractName = JSONUtil.getString(contractJsonObject, InfinityConstants.name);
            contractType = JSONUtil.getString(contractJsonObject, InfinityConstants.serviceType);
            companyLegalUnit = JSONUtil.getString(contractJsonObject, InfinityConstants.LegalEntityId);

            Map<String, Map<String, Map<String, String>>> cifs = map.get(contractEntry);

            for (String cifEntry : cifs.keySet()) {

                companyJsonObject = new JsonObject();

                if (!isViewPermissionEnbled(companyJsonObject, cifEntry, loggedInUserId, isSuperAdmin, headerMap)) {
                    continue;
                }

                JsonObject cifJsonObject = new JsonObject();
                String companyName = cifEntry;
                String isPrimary = "false";
                if (contractCoreCustomers.containsKey(cifEntry)) {
                    cifJsonObject = contractCoreCustomers.get(cifEntry);
                } else {
                    String filter = InfinityConstants.coreCustomerId + DBPUtilitiesConstants.EQUAL + cifEntry
                            + DBPUtilitiesConstants.AND + InfinityConstants.contractId
                            + DBPUtilitiesConstants.EQUAL
                            + contractEntry + DBPUtilitiesConstants.AND + InfinityConstants.LegalEntityId 
                            + DBPUtilitiesConstants.EQUAL + legalEntityId;

                    input.put(DBPUtilitiesConstants.FILTER, filter);
                    diagnostic.prepareDebug("response of contractcorecustomer... ").log();


                    jsonObject = ServiceCallHelper.invokeServiceAndGetJson(input, headerMap,
                            URLConstants.CONTRACTCORECUSTOMER_GET);

                    if (jsonObject.has(DBPDatasetConstants.DATASET_CONTRACTCORECUSTOMERS)) {
                        jsonElement = jsonObject.get(DBPDatasetConstants.DATASET_CONTRACTCORECUSTOMERS);
                        if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
                            jsonArray = jsonElement.getAsJsonArray();
                            cifJsonObject = jsonArray.get(0).getAsJsonObject();
                            MembershipDTO membershipDTO = new MembershipDTO();
                            membershipDTO.setId(cifEntry);
                            membershipDTO.setCompanyLegalUnit(legalEntityId);
                            CoreCustomerBackendDelegate coreCustomerBackendDelegate =
                                    DBPAPIAbstractFactoryImpl.getBackendDelegate(CoreCustomerBackendDelegate.class);
                            try {
                                DBXResult membershipresponse =
                                        coreCustomerBackendDelegate.searchCoreCustomers(membershipDTO, headerMap);
                                if (membershipresponse != null && membershipresponse.getResponse() != null) {
                                    JsonObject membershipResponse = ((JsonArray) membershipresponse
                                            .getResponse()).get(0).getAsJsonObject();
                                    for (Entry<String, JsonElement> entrySet : membershipResponse.entrySet()) {
                                        cifJsonObject.add(entrySet.getKey(), entrySet.getValue());
                                    }
                                }
                            } catch (ApplicationException e) {
                            }
                            contractCoreCustomers.put(cifEntry, cifJsonObject);
                        } else
                            continue;
                    } else
                        continue;
                }

                for (ContractCoreCustomersDTO contractCoreCustomer : list) {
                    if (contractCoreCustomer.getCoreCustomerId().equals(cifEntry)) {
                        companyJsonObject.addProperty(InfinityConstants.coreCustomerId,
                                contractCoreCustomer.getCoreCustomerId());
                        companyName = contractCoreCustomer.getCoreCustomerName();
                        companyJsonObject.addProperty(InfinityConstants.coreCustomerName,
                                companyName);
                        companyJsonObject.addProperty(InfinityConstants.isBusiness,
                                contractCoreCustomer.getIsBusiness());
                        companyJsonObject.addProperty(InfinityConstants.addressLine1,
                                contractCoreCustomer.getAddressLine1());
                        companyJsonObject.addProperty(InfinityConstants.addressLine2,
                                contractCoreCustomer.getAddressLine2());
                        companyJsonObject.addProperty(InfinityConstants.taxId, contractCoreCustomer.getTaxId());
                        companyJsonObject.addProperty(InfinityConstants.city, contractCoreCustomer.getCityName());
                        companyJsonObject.addProperty(InfinityConstants.state, contractCoreCustomer.getState());
                        companyJsonObject.addProperty(InfinityConstants.country, contractCoreCustomer.getCityName());
                        companyJsonObject.addProperty(InfinityConstants.zipCode, contractCoreCustomer.getCityName());
                        companyJsonObject.addProperty(InfinityConstants.phone, contractCoreCustomer.getCityName());
                        companyJsonObject.addProperty(InfinityConstants.email, contractCoreCustomer.getCityName());
                        companyJsonObject.addProperty(InfinityConstants.industry, contractCoreCustomer.getCityName());
                    }
                }
                BackendIdentifierDTO backendIdentifierDTO = new BackendIdentifierDTO();
                backendIdentifierDTO.setCustomer_id(id);
                backendIdentifierDTO.setBackendType(DTOConstants.CORE);
                if (IntegrationTemplateURLFinder.isIntegrated) {
                    backendIdentifierDTO
                            .setBackendType(IntegrationTemplateURLFinder.getBackendURL(InfinityConstants.BackendType));
                }
                backendIdentifierDTO = (BackendIdentifierDTO) backendIdentifierDTO.loadDTO();
                if (backendIdentifierDTO != null && cifEntry.equals(backendIdentifierDTO.getBackendId())) {
                    isPrimary = "true";
                }
                List<String> companyInfo = new ArrayList<String>();
                companyInfo.add(companyName);
                companyInfo.add(companyLegalUnit);
                companyMap.put(cifEntry, companyInfo);
                accountMap.put(cifEntry, map.get(contractEntry).get(cifEntry));
                companyJsonObject.addProperty(InfinityConstants.companyName, companyName);
                companyJsonObject.addProperty(InfinityConstants.contractId, contractEntry);
                companyJsonObject.addProperty(InfinityConstants.contractName, contractName);
                companyJsonObject.addProperty(InfinityConstants.contractType, contractType);
                companyJsonObject.addProperty(InfinityConstants.serviceDefinition, serviceDefinitionId);
                companyJsonObject.addProperty(InfinityConstants.serviceDefinitionName,
                        serviceDefinitions.get(serviceDefinitionId) != null
                                ? serviceDefinitions.get(serviceDefinitionId).get(InfinityConstants.name).getAsString()
                                : "");
                companyJsonObject.addProperty(InfinityConstants.autoSyncAccounts, autoSyncAccountsMap.get(cifEntry));
                companyJsonObject.addProperty(InfinityConstants.cif, cifEntry);
                companyJsonObject.addProperty(InfinityConstants.isPrimary, isPrimary);
                companyJsonObject.addProperty(InfinityConstants.legalEntityId, companyLegalUnit);

                JsonObject groupJsonObject = new JsonObject();

                if (customerGroups.containsKey(id.concat(contractEntry.concat(cifEntry)))) {
                    cifJsonObject =
                            customerGroups.get(id.concat(contractEntry.concat(cifEntry)));
                } else {

                    jsonArray = getGroupForCompany(id, contractEntry, cifEntry, headerMap,
                            isCustomRoleFlow);
                    if (jsonArray.size() > 0) {
                        groupJsonObject = jsonArray.get(0).getAsJsonObject();
                        contractCoreCustomers.put(
                                id.concat(contractEntry.concat(cifEntry)),
                                groupJsonObject);
                    } else
                        continue;
                }

                String groupId = "";

                if (isCustomRoleFlow) {
                    groupId = JSONUtil.getString(groupJsonObject, InfinityConstants.roleId);
                } else {
                    groupId = JSONUtil.getString(groupJsonObject, InfinityConstants.Group_id);

                }
                companyJsonObject.addProperty(InfinityConstants.roleId, groupId);
                companyJsonObject.addProperty(InfinityConstants.userRole, groupsInfo.get(groupId));
                JsonObject coreCustomerGroupJsonObject = new JsonObject();
                coreCustomerGroupJsonObject.addProperty(InfinityConstants.roleId, groupId);
                coreCustomerGroupJsonObject.addProperty(InfinityConstants.userRole, groupsInfo.get(groupId));
                coreCustomerGroupInfo.put(cifEntry, coreCustomerGroupJsonObject);
                try {
                    //FeatureActionLimitsDTO coreCustomerFeatureActionDTO =
                            //backendDelegate.getRestrictiveFeatureActionLimits(serviceDefinitionId,
                                    //contractEntry, groupId, cifEntry, "", headerMap, false, "");
                	//above comment needs to be added again
                	// need to be updated for product speific
                	FeatureActionLimitsDTO coreCustomerFeatureActionDTO =
                              backendDelegate.getRestrictiveFeatureActionLimits(
                                    serviceDefinitionId, contractEntry, groupId, cifEntry, "", headerMap, false, "",companyLegalUnit);
                	
                    featureActionDTOs.put(cifEntry, coreCustomerFeatureActionDTO);
                } catch (ApplicationException e) {
                	alert.prepareError("Exception", e).log();
                }

                JsonArray validRoles = new JsonArray();
                if (groupServiceDefinitions.containsKey(serviceDefinitionId)) {
                    validRoles = groupServiceDefinitions.get(serviceDefinitionId);
                } else {
                    String filter = "serviceDefinitionId" + DBPUtilitiesConstants.EQUAL + serviceDefinitionId;
                    input.put(DBPUtilitiesConstants.FILTER, filter);
                    jsonObject = ServiceCallHelper.invokeServiceAndGetJson(input, headerMap,
                            URLConstants.GROUP_SERVICEDEFINITION);
                    if (jsonObject.has(DBPDatasetConstants.DATASET_GROUPSERVICEDEFINITION)) {
                        jsonElement = jsonObject.get(DBPDatasetConstants.DATASET_GROUPSERVICEDEFINITION);
                        if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
                            jsonArray = jsonElement.getAsJsonArray();
                            for (JsonElement jsonelement : jsonArray) {
                                JsonObject json = new JsonObject();
                                if (groupsInfo
                                        .containsKey(JSONUtil.getString(jsonelement.getAsJsonObject(), "Group_id"))) {
                                    json.addProperty(InfinityConstants.roleId,
                                            JSONUtil.getString(jsonelement.getAsJsonObject(), "Group_id"));
                                    json.addProperty(InfinityConstants.userRole,
                                            groupsInfo.containsKey(
                                                    JSONUtil.getString(jsonelement.getAsJsonObject(), "Group_id"))
                                                            ? groupsInfo
                                                                    .get(JSONUtil.getString(
                                                                            jsonelement.getAsJsonObject(),
                                                                            "Group_id"))
                                                            : null);
                                    validRoles.add(json);
                                }
                            }
                            groupServiceDefinitions.put(serviceDefinitionId, validRoles);
                        } else
                            continue;
                    } else
                        continue;
                }

                companyJsonObject.add("validRoles", validRoles);

                JsonObject accountObject = new JsonObject();
                JsonArray accounts = new JsonArray();
                JsonArray excludedAccounts = new JsonArray();

                for (Entry<String, Map<String, String>> accountEntry : cifs.get(cifEntry).entrySet()) {
                    accountObject = new JsonObject();
                    if(!isSuperAdmin && accountEntry.getValue().get(InfinityConstants.accountStatus).equals(InfinityConstants.ACCOUNTSTATUS_CLOSED))
                    	continue;
                    accountObject.addProperty(InfinityConstants.accountId, accountEntry.getKey());
                    accountObject.addProperty(InfinityConstants.accountName,
                            accountEntry.getValue().get(InfinityConstants.accountName));
                    accountObject.addProperty(InfinityConstants.accountType,
                            accountEntry.getValue().get(InfinityConstants.accountType));
                    accountObject.addProperty(InfinityConstants.isEnabled, "true");
                    accountObject.addProperty(InfinityConstants.ownerType,
                            accountEntry.getValue().get(InfinityConstants.ownerType));
                    accounts.add(accountObject);
                }

                for (Entry<String, JsonObject> cifAccount : contractAccounts.get(contractEntry).get(cifEntry)
                        .entrySet()) {
                    if (!map.get(contractEntry).get(cifEntry).containsKey(cifAccount.getKey())) {
                        accountObject = new JsonObject();
                        if(!isSuperAdmin && cifAccount.getValue().get("statusDesc").equals(InfinityConstants.ACCOUNTSTATUS_CLOSED))
                        	continue;
                        accountObject.addProperty(InfinityConstants.accountId, cifAccount.getKey());
                        accountObject.add(InfinityConstants.accountName,
                                cifAccount.getValue().get(InfinityConstants.accountName));
                        accountObject.addProperty(InfinityConstants.accountType,
                                accountTypes.get(cifAccount.getValue().get(InfinityConstants.typeId).getAsString()));
                        accountObject.addProperty(InfinityConstants.isEnabled, "false");
                        accountObject.add(InfinityConstants.ownerType,
                                cifAccount.getValue().get(InfinityConstants.ownerType));
                        excludedAccounts.add(accountObject);
                        accounts.add(accountObject);
                    }
                }

                companyJsonObject.add(InfinityConstants.accounts, accounts);
                companyJsonObject.add(InfinityConstants.excludedAccounts, excludedAccounts);
                companyList.add(companyJsonObject);
            }
        }

        return companyList;
    }

    private boolean isViewPermissionEnbled(JsonObject companyJsonObject, String cifEntry, String loggedInUserId,
            boolean isSuperAdmin, Map<String, Object> headerMap) {

        boolean isCreatePermissionEnabled = false;
        boolean isEditPermissionEnabled = false;
        boolean isViewPermissionEnabled = false;

        if (isSuperAdmin) {
            companyJsonObject.addProperty("isCreatePermissionEnabled", isCreatePermissionEnabled);
            companyJsonObject.addProperty("isEditPermissionEnabled", isEditPermissionEnabled);
            companyJsonObject.addProperty("isViewPermissionEnabled", isViewPermissionEnabled);
            return true;
        }

        String filter =
                InfinityConstants.Customer_id + DBPUtilitiesConstants.EQUAL + loggedInUserId + DBPUtilitiesConstants.AND
                        + InfinityConstants.coreCustomerId + DBPUtilitiesConstants.EQUAL + cifEntry
                        + DBPUtilitiesConstants.AND +
                        InfinityConstants.featureId + DBPUtilitiesConstants.EQUAL + "USER_MANAGEMENT";

        Map<String, Object> input = new HashMap<String, Object>();

        input.put(DBPUtilitiesConstants.FILTER, filter);
        JsonObject response =
                ServiceCallHelper.invokeServiceAndGetJson(input, headerMap, URLConstants.CUSTOMER_ACTION_LIMITS_GET);
        JsonArray actions = new JsonArray();
        if (response.has(DBPDatasetConstants.DATASET_CUSTOMERACTION)) {
            JsonElement jsonElement = response.get(DBPDatasetConstants.DATASET_CUSTOMERACTION);
            if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
                actions = jsonElement.getAsJsonArray();
                for (int i = 0; i < actions.size(); i++) {
                    JsonObject jsonObject2 = actions.get(i).getAsJsonObject();
                    String coreCustomerId = jsonObject2.has(InfinityConstants.coreCustomerId)
                            && !jsonObject2.get(InfinityConstants.coreCustomerId).isJsonNull()
                                    ? jsonObject2.get(InfinityConstants.coreCustomerId).getAsString()
                                    : null;
                    String featureId = jsonObject2.has(InfinityConstants.featureId)
                            && !jsonObject2.get(InfinityConstants.featureId).isJsonNull()
                                    ? jsonObject2.get(InfinityConstants.featureId).getAsString().trim()
                                    : null;
                    String actionId = jsonObject2.has(InfinityConstants.Action_id)
                            && !jsonObject2.get(InfinityConstants.Action_id).isJsonNull()
                                    ? jsonObject2.get(InfinityConstants.Action_id).getAsString().trim()
                                    : null;
                    if (StringUtils.isBlank(actionId)) {
                        actionId = jsonObject2.has(InfinityConstants.action_id)
                                && !jsonObject2.get(InfinityConstants.action_id).isJsonNull()
                                        ? jsonObject2.get(InfinityConstants.action_id).getAsString().trim()
                                        : null;
                    }

                    if (cifEntry.equals(coreCustomerId) && "USER_MANAGEMENT".equals(featureId)) {
                        if ("USER_MANAGEMENT_CREATE".equals(actionId)) {
                            isCreatePermissionEnabled = true;
                        }

                        if ("USER_MANAGEMENT_EDIT".equals(actionId)) {
                            isEditPermissionEnabled = true;
                        }

                        if ("USER_MANAGEMENT_VIEW".equals(actionId)) {
                            isViewPermissionEnabled = true;
                        }
                    }
                }
            }
        }

        companyJsonObject.addProperty("isCreatePermissionEnabled", isCreatePermissionEnabled);
        companyJsonObject.addProperty("isEditPermissionEnabled", isEditPermissionEnabled);
        companyJsonObject.addProperty("isViewPermissionEnabled", isViewPermissionEnabled);

        return isViewPermissionEnabled;
    }

    private JsonArray getGroupForCompany(String id, String contractId, String coreCustomerID,
            Map<String, Object> headerMap,
            boolean isCustomRoleFlow) {
        String filter = InfinityConstants.coreCustomerId
                + DBPUtilitiesConstants.EQUAL + coreCustomerID
                + DBPUtilitiesConstants.AND
                + InfinityConstants.contractId + DBPUtilitiesConstants.EQUAL
                + contractId;
        if (!isCustomRoleFlow) {
            filter += DBPUtilitiesConstants.AND + InfinityConstants.Customer_id + DBPUtilitiesConstants.EQUAL + id;

            Map<String, Object> input = new HashMap<String, Object>();
            input.put(DBPUtilitiesConstants.FILTER, filter);

            JsonObject jsonObject = ServiceCallHelper.invokeServiceAndGetJson(input, headerMap,
                    URLConstants.CUSTOMER_GROUP_GET);

            if (jsonObject.has(DBPDatasetConstants.DATASET_CUSTOMERGROUP)) {
                JsonElement jsonElement = jsonObject.get(DBPDatasetConstants.DATASET_CUSTOMERGROUP);
                if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
                    return jsonElement.getAsJsonArray();
                }
            }
        } else {
            filter += DBPUtilitiesConstants.AND + InfinityConstants.customRoleId + DBPUtilitiesConstants.EQUAL + id;

            Map<String, Object> input = new HashMap<String, Object>();
            input.put(DBPUtilitiesConstants.FILTER, filter);

            JsonObject jsonObject = ServiceCallHelper.invokeServiceAndGetJson(input, headerMap,
                    URLConstants.CONTRACT_CUSTOM_ROLE_GET);
            if (jsonObject.has(DBPDatasetConstants.DATASET_CONTRACTCUSTOMROLE)) {
                JsonElement jsonElement = jsonObject.get(DBPDatasetConstants.DATASET_CONTRACTCUSTOMROLE);
                if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
                    return jsonElement.getAsJsonArray();
                }
            }
        }

        return new JsonArray();
    }

    private JsonArray getAccountsForCompanyList(String id, Map<String, Object> headerMap, String loggedInUserId) {
        // TODO Auto-generated method stub
        if (id.equals(loggedInUserId)) {
            Map<String, Object> input = new HashMap<String, Object>();
            String filter = InfinityConstants.Customer_id + DBPUtilitiesConstants.EQUAL + id;
            input.put(DBPUtilitiesConstants.FILTER, filter);
            JsonObject jsonObject =
                    ServiceCallHelper.invokeServiceAndGetJson(input, headerMap, URLConstants.CUSTOMERACCOUNTS_GET);
            if (jsonObject.has(DBPDatasetConstants.DATASET_CUSTOMERACCOUNTS)) {
                JsonElement jsonElement = jsonObject.get(DBPDatasetConstants.DATASET_CUSTOMERACCOUNTS);
                if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
                    return jsonElement.getAsJsonArray();
                }
            }
        } else {
            Map<String, Object> input = new HashMap<String, Object>();
            String filter = InfinityConstants.customRoleId + DBPUtilitiesConstants.EQUAL + id;
            input.put(DBPUtilitiesConstants.FILTER, filter);
            JsonObject jsonObject =
                    ServiceCallHelper.invokeServiceAndGetJson(input, headerMap, URLConstants.CUSTOM_ROLE_ACCOUNTS_GET);
            if (jsonObject.has(DBPDatasetConstants.DATASET_CUSTOMROLEACCOUNTS)) {
                JsonElement jsonElement = jsonObject.get(DBPDatasetConstants.DATASET_CUSTOMROLEACCOUNTS);
                if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
                    return jsonElement.getAsJsonArray();
                }
            }
        }
        return new JsonArray();
    }

    private JsonObject getCustomerJson(String customerId, Map<String, Object> headerMap, String legalEntityId) {

        JsonObject customerJsonObject = new JsonObject();

        Map<String, Object> map = new HashMap<String, Object>();

        String filter = InfinityConstants.id + DBPUtilitiesConstants.EQUAL + customerId + DBPUtilitiesConstants.AND 
        				+ InfinityConstants.LegalEntityId + DBPUtilitiesConstants.EQUAL + legalEntityId ;

        map.put(DBPUtilitiesConstants.FILTER, filter);

        String companyId = "";
        JsonObject jsonObject = ServiceCallHelper.invokeServiceAndGetJson(map, headerMap, URLConstants.CUSTOMER_GET);

        if (jsonObject.has(DBPDatasetConstants.DATASET_CUSTOMER)) {
            JsonElement jsonElement = jsonObject.get(DBPDatasetConstants.DATASET_CUSTOMER);
            if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
                JsonArray jsonArray = jsonElement.getAsJsonArray();
                JsonObject jsonObject2 = jsonArray.get(0).getAsJsonObject();
                customerJsonObject.addProperty(InfinityConstants.firstName,
                        jsonObject2.has("FirstName") && !jsonObject2.get("FirstName").isJsonNull()
                                ? jsonObject2.get("FirstName").getAsString()
                                : null);
                customerJsonObject.addProperty(InfinityConstants.lastName,
                        jsonObject2.has("LastName") && !jsonObject2.get("LastName").isJsonNull()
                                ? jsonObject2.get("LastName").getAsString()
                                : null);
                customerJsonObject.addProperty(InfinityConstants.middleName,
                        jsonObject2.has("MiddleName") && !jsonObject2.get("MiddleName").isJsonNull()
                                ? jsonObject2.get("MiddleName").getAsString()
                                : null);
                customerJsonObject.addProperty(InfinityConstants.ssn,
                        jsonObject2.has("Ssn") && !jsonObject2.get("Ssn").isJsonNull()
                                ? jsonObject2.get("Ssn").getAsString()
                                : null);
                customerJsonObject.addProperty(InfinityConstants.dob,
                        jsonObject2.has("DateOfBirth") && !jsonObject2.get("DateOfBirth").isJsonNull()
                                ? jsonObject2.get("DateOfBirth").getAsString()
                                : null);
                customerJsonObject.addProperty(InfinityConstants.drivingLicenseNumber,
                        jsonObject2.has("DrivingLicenseNumber") && !jsonObject2.get("DrivingLicenseNumber").isJsonNull()
                                ? jsonObject2.get("DrivingLicenseNumber").getAsString()
                                : null);
                customerJsonObject.addProperty(InfinityConstants.isEnrolled,
                        (Boolean.parseBoolean(jsonObject2.get("isEnrolled").getAsString())
                                || jsonObject2.get("isEnrolled").getAsString().equals("1")) ? "true" : "false");
                customerJsonObject.addProperty(InfinityConstants.legalEntityId,
                        jsonObject2.has("companyLegalUnit") && !jsonObject2.get("companyLegalUnit").isJsonNull()
                                ? jsonObject2.get("companyLegalUnit").getAsString()
                                : null);

                filter = InfinityConstants.Customer_id + DBPUtilitiesConstants.EQUAL + customerId
                        + DBPUtilitiesConstants.AND 
        				+ InfinityConstants.LegalEntityId + DBPUtilitiesConstants.EQUAL + legalEntityId 
        				+ DBPUtilitiesConstants.AND ;

                if (IntegrationTemplateURLFinder.isIntegrated) {
                    filter += InfinityConstants.BackendType + DBPUtilitiesConstants.EQUAL
                            + ServiceId.BACKEND_TYPE;
                } else {
                    filter += InfinityConstants.BackendType + DBPUtilitiesConstants.EQUAL
                            + DTOConstants.CORE;
                }

                map.put(DBPUtilitiesConstants.FILTER, filter);

                jsonObject =
                        ServiceCallHelper.invokeServiceAndGetJson(map, headerMap, URLConstants.BACKENDIDENTIFIER_GET);

                if (jsonObject.has(DBPDatasetConstants.DATASET_BACKENDIDENTIFIER)) {
                    jsonElement = jsonObject.get(DBPDatasetConstants.DATASET_BACKENDIDENTIFIER);
                    if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
                        jsonArray = jsonElement.getAsJsonArray();
                        jsonObject2 = jsonArray.get(0).getAsJsonObject();
                        customerJsonObject.addProperty(InfinityConstants.coreCustomerId,
                                jsonObject2.has("BackendId") && !jsonObject2.get("BackendId").isJsonNull()
                                        ? jsonObject2.get("BackendId").getAsString()
                                        : null);
                        companyId = jsonObject2.has("CompanyId") && !jsonObject2.get("CompanyId").isJsonNull()
                                ? jsonObject2.get("CompanyId").getAsString()
                                : null;
                    }
                }

                CustomerCommunicationDTO customerCommunicationDTO = new CustomerCommunicationDTO();
                customerCommunicationDTO.setCustomer_id(customerId);
                CommunicationBackendDelegate backendDelegate =
                        DBPAPIAbstractFactoryImpl.getBackendDelegate(CommunicationBackendDelegate.class);
                DBXResult dbxResult =
                        backendDelegate.getPrimaryMFACommunicationDetails(customerCommunicationDTO, headerMap);
                if (dbxResult.getResponse() != null) {
                    jsonObject = (JsonObject) dbxResult.getResponse();
                    if (jsonObject.has(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION)) {
                        jsonElement = jsonObject.get(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION);
                        if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
                            jsonArray = jsonElement.getAsJsonArray();
                            for (int i = 0; i < jsonArray.size(); i++) {
                                jsonObject2 = jsonArray.get(i).getAsJsonObject();
                                if (jsonObject2.get("Type_id").getAsString()
                                        .equals(HelperMethods.getCommunicationTypes().get("Phone"))) {
                                    customerJsonObject.addProperty(InfinityConstants.phoneNumber,
                                            jsonObject2.has("Value") && !jsonObject2.get("Value").isJsonNull()
                                                    ? jsonObject2.get("Value").getAsString()
                                                    : null);

                                    customerJsonObject.addProperty(InfinityConstants.phoneCountryCode,
                                            jsonObject2.has("phoneCountryCode")
                                                    && !jsonObject2.get("phoneCountryCode").isJsonNull()
                                                            ? jsonObject2.get("phoneCountryCode").getAsString()
                                                            : null);
                                } else if (jsonObject2.get("Type_id").getAsString()
                                        .equals(HelperMethods.getCommunicationTypes().get("Email"))) {
                                    customerJsonObject.addProperty(InfinityConstants.email,
                                            jsonObject2.has("Value") && !jsonObject2.get("Value").isJsonNull()
                                                    ? jsonObject2.get("Value").getAsString()
                                                    : null);
                                }
                                if (jsonObject2.get("Type_id").getAsString().equals("ssn")) {
                                    customerJsonObject.addProperty(InfinityConstants.ssn,
                                            jsonObject2.has("Value") //&& !jsonObject2.get("Value").isJsonNull()
                                                    ? jsonObject2.get("Value").getAsString()
                                                    : "");
                                } 
                                if (jsonObject2.get("Type_id").getAsString().equals("dob")) {
                                    customerJsonObject.addProperty(InfinityConstants.dob,
                                            jsonObject2.has("Value") //&& !!jsonObject2.get("Value").isJsonNull()
                                                    ? jsonObject2.get("Value").getAsString()
                                                    : "");
                                } 
                            }
                        }
                    }
                }

                /*if (IntegrationTemplateURLFinder.isIntegrated
                        && customerJsonObject.has(InfinityConstants.coreCustomerId)
                        && !customerJsonObject.get(InfinityConstants.coreCustomerId).isJsonNull() && StringUtils
                                .isNotBlank(customerJsonObject.get(InfinityConstants.coreCustomerId).getAsString())) {
                    Map<String, Object> input = new HashMap<String, Object>();
                    HelperMethods.addJWTAuthHeader(headerMap, AuthConstants.PRE_LOGIN_FLOW);
                    input.put("customerId", customerJsonObject.get(InfinityConstants.coreCustomerId).getAsString());
                    if (StringUtils.isBlank(companyId)) {
                        headerMap.put("companyId",
                                EnvironmentConfigurationsHandler.getValue(DBPUtilitiesConstants.BRANCH_ID_REFERENCE));
                    } else {
                        headerMap.put("companyId", companyId);
                    }
                    String serviceId = ServiceId.T24ISUSER_INTEGRATION_SERVICE;
                    String operationName = OperationName.CORE_CUSTOMER_SEARCH;

                    JsonObject membershipJson =
                            ServiceCallHelper.invokeServiceAndGetJson(serviceId, null, operationName,
                                    input, headerMap);

                    if (null != membershipJson && JSONUtil.hasKey(membershipJson, DBPDatasetConstants.DATASET_CUSTOMERS)
                            && membershipJson.get(DBPDatasetConstants.DATASET_CUSTOMERS).isJsonArray()) {
                        JsonArray partyJsonArray =
                                membershipJson.get(DBPDatasetConstants.DATASET_CUSTOMERS).getAsJsonArray();
                        if (partyJsonArray.size() > 0) {
                            membershipJson = partyJsonArray.get(0).getAsJsonObject();
                            customerJsonObject.add(InfinityConstants.ssn, membershipJson.get(InfinityConstants.ssn));
                        }
                    }
                                }*/

            }
        }
        return customerJsonObject;
    }

    @Override
    public DBXResult createCustomRole(JsonObject jsonObject, Map<String, Object> headerMap) {

        DBXResult dbxResult = new DBXResult();
        JsonObject result = new JsonObject();
        dbxResult.setResponse(result);
        JsonObject customRoleDetails = jsonObject.get(InfinityConstants.customRoleDetails).getAsJsonObject();

        if (!createCustomRole(customRoleDetails, headerMap, result,
                jsonObject.get(InfinityConstants.createdby).getAsString())) {
            return dbxResult;
        }

        String customRoleId = result.get(InfinityConstants.id).getAsString();

        createCustomRoleAction(customRoleId, jsonObject, headerMap);

        return dbxResult;
    }

    @Override
    public DBXResult editCustomRole(JsonObject jsonObject, Map<String, Object> headerMap) {

        DBXResult dbxResult = new DBXResult();
        JsonObject result = new JsonObject();
        dbxResult.setResponse(result);

        JsonObject customRoleDetails = jsonObject.get(InfinityConstants.customRoleDetails).getAsJsonObject();

        String customerId = customRoleDetails.get(InfinityConstants.id).getAsString();

        createCustomRoleAction(customerId, jsonObject, headerMap);

        result.addProperty(InfinityConstants.id, customerId);

        return dbxResult;
    }

    private void createCustomRoleAccounts(String id, JsonArray companyList, Map<String, Object> headerMap,
            Map<String, Map<String, Set<String>>> map, Map<String, String> roleIds, String legalEntityId) {

        for (int i = 0; i < companyList.size(); i++) {

            JsonObject cifJsonObject = companyList.get(i).getAsJsonObject();
            String contractId = cifJsonObject.get(InfinityConstants.contractId).getAsString();
            String cif = cifJsonObject.get(InfinityConstants.cif).getAsString();
            String roleId = cifJsonObject.get(InfinityConstants.roleId).getAsString();
            roleIds.put(cif, roleId);
            JsonArray accounts = cifJsonObject.get(InfinityConstants.accounts).getAsJsonArray();
            Set<String> set = new HashSet<String>();
            Map<String, Object> input = new HashMap<String, Object>();

            removeDBEntries(id, cif, contractId, headerMap, true, legalEntityId);
            boolean autoSyncAccounts =
                    Boolean.parseBoolean(cifJsonObject.get(InfinityConstants.autoSyncAccounts).getAsString());
            input.put(InfinityConstants.contractId, contractId);
            input.put(InfinityConstants.coreCustomerId, cif);
            input.put(InfinityConstants.customRoleId, id);
            input.put(InfinityConstants.roleId, roleId);
            input.put(InfinityConstants.autoSyncAccounts, autoSyncAccounts);
            ServiceCallHelper.invokeServiceAndGetJson(input, headerMap, URLConstants.CONTRACT_CUSTOM_ROLE_CREATE);

            for (int j = 0; j < accounts.size(); j++) {
                JsonObject jsonObject = accounts.get(j).getAsJsonObject();
                input = new HashMap<String, Object>();
                String accountName = JSONUtil.getString(jsonObject, InfinityConstants.accountName);
                String accountId = JSONUtil.getString(jsonObject, InfinityConstants.accountId);
                String accountType = JSONUtil.getString(jsonObject, InfinityConstants.accountType);
                set.add(accountId);
                input.put(InfinityConstants.id, HelperMethods.getNewId());
                input.put(InfinityConstants.contractId, contractId);
                input.put(InfinityConstants.customRoleId, id);
                input.put(InfinityConstants.coreCustomerId, cif);
                input.put(InfinityConstants.Account_id, accountId);
                input.put(InfinityConstants.AccountName, accountName);
                input.put(InfinityConstants.accountType, accountType);
                ServiceCallHelper.invokeServiceAndGetJson(input, headerMap, URLConstants.CUSTOM_ROLE_ACCOUNTS_CREATE);
            }

            JsonArray excludedAccountsList = cifJsonObject.has(InfinityConstants.excludedAccounts)
                    && !cifJsonObject.get(InfinityConstants.excludedAccounts).isJsonNull()
                            ? cifJsonObject.get(InfinityConstants.excludedAccounts).getAsJsonArray()
                            : new JsonArray();

            if (excludedAccountsList.size() > 0) {
                for (int j = 0; j < excludedAccountsList.size(); j++) {
                    JsonObject jsonObject = excludedAccountsList.get(j).getAsJsonObject();
                    input = new HashMap<String, Object>();
                    String accountName = JSONUtil.getString(jsonObject, InfinityConstants.accountName);
                    String accountId = JSONUtil.getString(jsonObject, InfinityConstants.accountId);
                    String accountType = JSONUtil.getString(jsonObject, InfinityConstants.accountType);
                    set.add(accountId);
                    input.put(InfinityConstants.id, HelperMethods.getNewId());
                    input.put(InfinityConstants.contractId, contractId);
                    input.put(InfinityConstants.Customer_id, id);
                    input.put(InfinityConstants.coreCustomerId, cif);
                    input.put(InfinityConstants.Account_id, accountId);
                    input.put(InfinityConstants.AccountName, accountName);
                    input.put(InfinityConstants.accountType, accountType);
                    ServiceCallHelper.invokeServiceAndGetJson(input, headerMap,
                            URLConstants.EXCLUDEDCUSTOMROLEACCOUNTS_CREATE);
                }
            }

            if (!map.containsKey(contractId)) {
                map.put(contractId, new HashMap<String, Set<String>>());
            }

            if (!map.get(contractId).containsKey(cif)) {
                map.get(contractId).put(cif, set);
            }
        }
    }

    private void createCustomRoleAction(String customerId, JsonObject jsonObject, Map<String, Object> headerMap) {

        List<String> customerActionLimits = new ArrayList<String>();
        List<String> excludedCustomerActionLimits = new ArrayList<String>();

        Map<String, Map<String, Set<String>>> map = new HashMap<String, Map<String, Set<String>>>();
        Map<String, String> roleIds = new HashMap<String, String>();

        JsonArray companyList = jsonObject.get(InfinityConstants.companyList).getAsJsonArray();
        
        String legalEntityId = jsonObject.get(InfinityConstants.legalEntityId).getAsString();

        createCustomRoleAccounts(customerId, companyList, headerMap, map, roleIds, legalEntityId);

        JsonArray accountLevelPermissions = jsonObject.has(InfinityConstants.accountLevelPermissions)
                && jsonObject.get(InfinityConstants.accountLevelPermissions).isJsonArray()
                        ? jsonObject.get(InfinityConstants.accountLevelPermissions).getAsJsonArray()
                        : new JsonArray();

        createAccountLevelPermissons(customerId, accountLevelPermissions, headerMap, map, roleIds,
                customerActionLimits, true,false);
        JsonArray excludedAccountLevelPermissions = jsonObject.has(InfinityConstants.excludedAccountLevelPermissions)
                && jsonObject.get(InfinityConstants.excludedAccountLevelPermissions).isJsonArray()
                        ? jsonObject.get(InfinityConstants.excludedAccountLevelPermissions).getAsJsonArray()
                        : new JsonArray();

        createAccountLevelPermissons(customerId, excludedAccountLevelPermissions, headerMap, map, roleIds,
                excludedCustomerActionLimits, true, true);
        JsonArray globalLevelPermissions = jsonObject.has(InfinityConstants.globalLevelPermissions)
                && jsonObject.get(InfinityConstants.globalLevelPermissions).isJsonArray()
                        ? jsonObject.get(InfinityConstants.globalLevelPermissions).getAsJsonArray()
                        : new JsonArray();

        createGlobalLevelPermissons(customerId, globalLevelPermissions, headerMap, map, roleIds, customerActionLimits,
                true,false);

        JsonArray excludedGlobalLevelPermissions = jsonObject.has(InfinityConstants.excludedGlobalLevelPermissions)
                && jsonObject.get(InfinityConstants.excludedGlobalLevelPermissions).isJsonArray()
                        ? jsonObject.get(InfinityConstants.excludedGlobalLevelPermissions).getAsJsonArray()
                        : new JsonArray();

        createGlobalLevelPermissons(customerId, excludedGlobalLevelPermissions, headerMap, map, roleIds,
                excludedCustomerActionLimits, true, true);
        JsonArray transactionLimits = jsonObject.has(InfinityConstants.transactionLimits)
                && jsonObject.get(InfinityConstants.transactionLimits).isJsonArray()
                        ? jsonObject.get(InfinityConstants.transactionLimits).getAsJsonArray()
                        : new JsonArray();

        createTransactionLimits(customerId, transactionLimits, headerMap, map, roleIds, customerActionLimits, true,false);

        StringBuilder input = new StringBuilder("");
        int queries = customerActionLimits.size();
        if (queries > 0) {
            for (int query = 0; query < queries; query++) {
                String temp = customerActionLimits.get(query);
                if (query < queries - 1)
                    input.append(temp + "|");
                else
                    input.append(temp);
            }

            Map<String, Object> inputParams = new HashMap<String, Object>();

            inputParams.put(InfinityConstants._queryInput, input.toString());
            inputParams.put(InfinityConstants._customRoleId, customerId);

            HelperMethods.callApiAsync(inputParams, headerMap,
                    URLConstants.CREATE_ORG_CUSTOM_ROLES_PROC);
        }

        input = new StringBuilder("");
        queries = excludedCustomerActionLimits.size();
        if (queries > 0) {
            for (int query = 0; query < queries; query++) {
                String temp = excludedCustomerActionLimits.get(query);
                if (query < queries - 1)
                    input.append(temp + "|");
                else
                    input.append(temp);
            }

            Map<String, Object> inputParams = new HashMap<String, Object>();

            inputParams.put(InfinityConstants._queryInput, input.toString());
            inputParams.put(InfinityConstants._customRoleId, customerId);

            HelperMethods.callApiAsync(inputParams, headerMap, URLConstants.EXCLUDEDCUSTOMROLEACTIONLIMIT_SAVE_PROC);
        }

        JsonArray removedCompanies = jsonObject.has(InfinityConstants.removedCompanies)
                && jsonObject.get(InfinityConstants.removedCompanies).isJsonArray()
                        ? jsonObject.get(InfinityConstants.removedCompanies).getAsJsonArray()
                        : new JsonArray();

        removeCompanies(customerId, removedCompanies, headerMap, true, legalEntityId);
    }

    private boolean createCustomRole(JsonObject customRoleDetails, Map<String, Object> headerMap, JsonObject result,
            String createdBy) {

        Map<String, Object> input = new HashMap<String, Object>();
        String customRoleName = customRoleDetails.get(InfinityConstants.customRoleName).getAsString();
        input.put(InfinityConstants.name, customRoleName);
        input.put(InfinityConstants.description,
                customRoleDetails.has(InfinityConstants.description)
                        && !customRoleDetails.get(InfinityConstants.description).isJsonNull()
                                ? customRoleDetails.get(InfinityConstants.description).getAsString()
                                : null);
        input.put(InfinityConstants.status_id, DBPUtilitiesConstants.SID_ACTIVE);
        input.put(InfinityConstants.createdby, createdBy);
        DBXResult existingCustomRoleResult = getCustomRole(customRoleName, null, headerMap);

        if (existingCustomRoleResult.getResponse() != null) {
            return false;
        }

        JsonObject response = ServiceCallHelper.invokeServiceAndGetJson(input, headerMap,
                URLConstants.CUSTOM_ROLE_CREATE);
        if (JSONUtil.isJsonNotNull(response) && JSONUtil.hasKey(response, DBPDatasetConstants.DATASET_CUSTOMROLE)
                && response.get(DBPDatasetConstants.DATASET_CUSTOMROLE).isJsonArray()) {
            input.clear();
            String id = response.get(DBPDatasetConstants.DATASET_CUSTOMROLE).getAsJsonArray().get(0).getAsJsonObject()
                    .get(InfinityConstants.id).getAsString();
            String filter = InfinityConstants.id + DBPUtilitiesConstants.EQUAL + id;
            input.put(DBPUtilitiesConstants.FILTER, filter);
            response = ServiceCallHelper.invokeServiceAndGetJson(input, headerMap,
                    URLConstants.CUSTOM_ROLE_GET);
            if (JSONUtil.isJsonNotNull(response) && JSONUtil.hasKey(response, DBPDatasetConstants.DATASET_CUSTOMROLE)
                    && response.get(DBPDatasetConstants.DATASET_CUSTOMROLE).isJsonArray()) {
                JsonElement element = response.get(DBPDatasetConstants.DATASET_CUSTOMROLE).getAsJsonArray().get(0);
                for (Entry<String, JsonElement> entry : element.getAsJsonObject().entrySet()) {
                    result.add(entry.getKey(), entry.getValue());
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public DBXResult verifyCustomRole(JsonObject jsonObject, Map<String, Object> headerMap) {
        String name = jsonObject.get(InfinityConstants.name).getAsString();
        return getCustomRole(name, null, headerMap);
    }

    private DBXResult getCustomRole(String customRoleName, String id, Map<String, Object> headerMap) {
        DBXResult customRoleDTO = new DBXResult();
        customRoleDTO.setResponse(null);
        Map<String, Object> inputParams = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNoneBlank(customRoleName)) {
            sb.append(InfinityConstants.name).append(DBPUtilitiesConstants.EQUAL).append("\'").append(customRoleName)
                    .append("\'");
        }

        if (StringUtils.isNoneBlank(id)) {
            sb.append(InfinityConstants.id).append(DBPUtilitiesConstants.EQUAL).append("\'").append(id).append("\'");
        }

        if (StringUtils.isNoneBlank(sb.toString())) {
            inputParams.put(DBPUtilitiesConstants.FILTER, sb.toString());
        }
        JsonObject response = new JsonObject();
        try {
            response = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap, URLConstants.CUSTOM_ROLE_GET);

            if (JSONUtil.isJsonNotNull(response) && JSONUtil.hasKey(response, DBPDatasetConstants.DATASET_CUSTOMROLE)
                    && response.get(DBPDatasetConstants.DATASET_CUSTOMROLE).isJsonArray()) {
                JsonArray responseArray = response.get(DBPDatasetConstants.DATASET_CUSTOMROLE).getAsJsonArray();
                if (responseArray.size() > 0) {
                    JsonObject jsonObject = responseArray.get(0).getAsJsonObject();
                    jsonObject.add(InfinityConstants.customRoleName, jsonObject.get(InfinityConstants.name));
                    customRoleDTO.setResponse(jsonObject);
                }
            }

        } catch (Exception e) {
            alert.prepareError("Exception occured while fetching data from custom roles", e).log();
        }

        return customRoleDTO;
    }

    @Override
    public DBXResult getCustomRole(JsonObject jsonObject, Map<String, Object> headerMap) {
        DBXResult dbxResult = new DBXResult();
        dbxResult.setResponse(jsonObject);

        String id = jsonObject.get(InfinityConstants.id).getAsString();

        String customerId = jsonObject.get(InfinityConstants.customerId).getAsString();
        
        String legalEntityId = jsonObject.get(InfinityConstants.legalEntityId).getAsString();

        DBXResult customerResponseObject = getCustomRole(null, id, headerMap);

        if (customerResponseObject.getResponse() == null) {
            return dbxResult;
        }

        JsonArray jsonArray = new JsonArray();
        jsonObject.add(InfinityConstants.customRoleDetails, (JsonObject) customerResponseObject.getResponse());

        Map<String, Map<String, Map<String, Map<String, String>>>> map =
                new HashMap<String, Map<String, Map<String, Map<String, String>>>>();

        Map<String, FeatureActionLimitsDTO> featureActionDTOs = new HashMap<String, FeatureActionLimitsDTO>();

        Map<String, List<String>> companyMap = new HashMap<>();

        Map<String, Map<String, Map<String, String>>> accountMap =
                new HashMap<String, Map<String, Map<String, String>>>();

        Map<String, JsonObject> contracts = new HashMap<String, JsonObject>();
        Map<String, JsonObject> contractCoreCustomers = new HashMap<String, JsonObject>();
        Map<String, JsonObject> coreCustomerGroupInfo = new HashMap<String, JsonObject>();

        JsonArray actions = new JsonArray();

        Map<String, Object> input = new HashMap<String, Object>();
        String filter = InfinityConstants.customRole_id + DBPUtilitiesConstants.EQUAL + id;
        input.put(DBPUtilitiesConstants.FILTER, filter);
        JsonObject response =
                ServiceCallHelper.invokeServiceAndGetJson(input, headerMap, URLConstants.CUSTOMROLEACTIONLIMITS_GET);

        if (response.has(DBPDatasetConstants.DATASET_CUSTOMROLEACTIONLIMITS)) {
            JsonElement jsonElement = response.get(DBPDatasetConstants.DATASET_CUSTOMROLEACTIONLIMITS);
            if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
                actions = jsonElement.getAsJsonArray();
            }
        }

        JsonArray excludedActions = new JsonArray();

        input = new HashMap<String, Object>();
        filter = InfinityConstants.customRole_id + DBPUtilitiesConstants.EQUAL + id;
        input.put(DBPUtilitiesConstants.FILTER, filter);
        response = ServiceCallHelper.invokeServiceAndGetJson(input, headerMap,
                URLConstants.EXCLUDED_CUSTOM_ROLE_ACTION_LIMITS_GET);

        if (response.has(DBPDatasetConstants.DATASET_EXCLUDED_CUSTOMROLEACTIONLIMITS)) {
            JsonElement jsonElement = response.get(DBPDatasetConstants.DATASET_EXCLUDED_CUSTOMROLEACTIONLIMITS);
            if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
                excludedActions = jsonElement.getAsJsonArray();
            }
        }
        JsonArray accounts = new JsonArray();
        input = new HashMap<String, Object>();
        filter = InfinityConstants.customRoleId + DBPUtilitiesConstants.EQUAL + id;
        input.put(DBPUtilitiesConstants.FILTER, filter);
        response =
                ServiceCallHelper.invokeServiceAndGetJson(input, headerMap, URLConstants.CUSTOM_ROLE_ACCOUNTS_GET);
        if (response.has(DBPDatasetConstants.DATASET_CUSTOMROLEACCOUNTS)) {
            JsonElement jsonElement = response.get(DBPDatasetConstants.DATASET_CUSTOMROLEACCOUNTS);
            if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
                accounts = jsonElement.getAsJsonArray();
            }
        }
        
        jsonArray = getCompanyList(coreCustomerGroupInfo, null, null, contracts, contractCoreCustomers, id, headerMap, map,
                companyMap, accountMap, featureActionDTOs, customerId, accounts, false, true, legalEntityId);

        jsonObject.add(InfinityConstants.companyList, jsonArray);

        addPermissions(coreCustomerGroupInfo, contracts, contractCoreCustomers, id, headerMap, map, jsonObject,
                accountMap, companyMap, featureActionDTOs, false, actions, excludedActions, true);

        dbxResult.setResponse(jsonObject);
        return dbxResult;
    }

    @Override
    public DBXResult getCustomRoleByCompanyID(JsonObject jsonObject, Map<String, Object> headerMap) {

        DBXResult dbxResult = new DBXResult();
        String companyID = jsonObject.get(InfinityConstants.id).getAsString();
        String filter = InfinityConstants.coreCustomerId + DBPUtilitiesConstants.EQUAL + companyID;

        Set<String> customRoles = new HashSet<String>();

        Map<String, Object> input = new HashMap<String, Object>();
        input.put(DBPUtilitiesConstants.FILTER, filter);
        JsonObject response =
                ServiceCallHelper.invokeServiceAndGetJson(input, headerMap, URLConstants.CUSTOMROLEACTIONLIMITS_GET);
        if (response.has(DBPDatasetConstants.DATASET_CUSTOMROLEACTIONLIMITS)) {
            JsonElement jsonElement = response.get(DBPDatasetConstants.DATASET_CUSTOMROLEACTIONLIMITS);
            if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
                JsonArray jsonArray = jsonElement.getAsJsonArray();

                for (int i = 0; i < jsonArray.size(); i++) {
                    JsonObject roleLimit = jsonArray.get(i).getAsJsonObject();
                    customRoles.add(roleLimit.has(InfinityConstants.coreCustomerId)
                            && !roleLimit.get(InfinityConstants.coreCustomerId).isJsonNull()
                                    ? roleLimit.get(InfinityConstants.coreCustomerId).getAsString()
                                    : null);
                }
            }
        }

        JsonObject result = new JsonObject();
        JsonArray resultArray = new JsonArray();
        for (String customRole : customRoles) {
            if (StringUtils.isNotBlank(customRole)) {
                filter = InfinityConstants.id + DBPUtilitiesConstants.EQUAL + customRole;
                try {
                    response =
                            ServiceCallHelper.invokeServiceAndGetJson(input, headerMap, URLConstants.CUSTOM_ROLE_GET);
                    if (JSONUtil.isJsonNotNull(response)
                            && JSONUtil.hasKey(response, DBPDatasetConstants.DATASET_CUSTOMROLE)
                            && response.get(DBPDatasetConstants.DATASET_CUSTOMROLE).isJsonArray()) {
                        JsonArray responseArray = response.get(DBPDatasetConstants.DATASET_CUSTOMROLE).getAsJsonArray();
                        if (responseArray.size() > 0) {
                            JsonObject customRoleJsonObject = responseArray.get(0).getAsJsonObject();
                            customRoleJsonObject.add(InfinityConstants.customRoleName,
                                    customRoleJsonObject.get(InfinityConstants.name));
                            responseArray.add(customRoleJsonObject);
                        }
                    }
                } catch (Exception e) {
                    alert.prepareError("Exception occured while fetching data from custom roles", e).log();
                }
            }
        }
        result.add(InfinityConstants.customRoles, resultArray);
        dbxResult.setResponse(result);
        return dbxResult;
    }

    @Override
    public DBXResult getCompanyLevelCustomRoles(JsonObject jsonObject, Map<String, Object> headerMap) {

        DBXResult dbxResult = new DBXResult();

        Map<String, Map<String, Set<String>>> map = new HashMap<String, Map<String, Set<String>>>();
        String contractIds = "";
        String queryInput = "";
        String customerId = jsonObject.get(InfinityConstants.id).getAsString();
        String legalEntityId = jsonObject.get(InfinityConstants.legalEntityId).getAsString();
    	String contract = null;
        Map<String, Object> inputParams = new HashMap<String, Object>();
        
        
        if (jsonObject.has(InfinityConstants.contractId)
                && !jsonObject.get(InfinityConstants.contractId).isJsonNull()) {
			contract = jsonObject.get(InfinityConstants.contractId).getAsString();
        }

        String filter = InfinityConstants.Customer_id + DBPUtilitiesConstants.EQUAL + customerId
                + DBPUtilitiesConstants.AND + InfinityConstants.Action_id + DBPUtilitiesConstants.EQUAL
                + "CUSTOM_ROLES_VIEW" + DBPUtilitiesConstants.AND + InfinityConstants.LegalEntityId + DBPUtilitiesConstants.EQUAL
                + legalEntityId;
        Map<String, Object> input = new HashMap<String, Object>();
        input.put(DBPUtilitiesConstants.FILTER, filter);
        JsonObject response =
                ServiceCallHelper.invokeServiceAndGetJson(input, headerMap, URLConstants.CUSTOMER_ACTION_LIMITS_GET);
        if (response.has(DBPDatasetConstants.DATASET_CUSTOMERACTION)) {
            JsonElement jsonElement = response.get(DBPDatasetConstants.DATASET_CUSTOMERACTION);
            if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
                JsonArray jsonArray = jsonElement.getAsJsonArray();
                for (int i = 0; i < jsonArray.size(); i++) {
                    JsonObject contractCustomer = jsonArray.get(i).getAsJsonObject();
                    String contractId = contractCustomer.has(InfinityConstants.contractId)
                            && !contractCustomer.get(InfinityConstants.contractId).isJsonNull()
                                    ? contractCustomer.get(InfinityConstants.contractId).getAsString()
                                    : null;
                    String coreCustomerId = contractCustomer.has(InfinityConstants.coreCustomerId)
                            && !contractCustomer.get(InfinityConstants.coreCustomerId).isJsonNull()
                                    ? contractCustomer.get(InfinityConstants.coreCustomerId).getAsString()
                                    : null;
					if (StringUtils.isNotBlank(coreCustomerId) && StringUtils.isNotBlank(contractId)) {
						if (!map.containsKey(contractId)) {
							map.put(contractId, new HashMap<String, Set<String>>());
							queryInput += contractId + DBPUtilitiesConstants.COMMA_SEPERATOR;
						}
						map.get(contractId).put(coreCustomerId, new HashSet<String>());
					}
				}
			}
			inputParams.put("_contractIdList", queryInput);
			contractIds = callClosedaccountContractId(inputParams, headerMap);

		}
		List<String> accountActiveContracts = Arrays.asList(contractIds.split(","));
		List<String> allContracts = Arrays.asList(queryInput.split(","));
		List<String> resultSet = new ArrayList<>(allContracts);
		resultSet.removeAll(accountActiveContracts);
		if (resultSet.size() > 0) {
			for (String i : resultSet) {
				if (map.containsKey(i)) {
					map.remove(i);
				}
			}
		}
        for (Entry<String, Map<String, Set<String>>> contractEntry : map.entrySet()) {
            for (Entry<String, Set<String>> coreCustomerEntry : contractEntry.getValue().entrySet()) {
                filter = InfinityConstants.coreCustomerId + DBPUtilitiesConstants.EQUAL + coreCustomerEntry.getKey() +
                        DBPUtilitiesConstants.AND + InfinityConstants.contractId + DBPUtilitiesConstants.EQUAL
                        + contractEntry.getKey();

                Set<String> customRoles = new HashSet<String>();

                input = new HashMap<String, Object>();
                input.put(DBPUtilitiesConstants.FILTER, filter);
                response =
                        ServiceCallHelper.invokeServiceAndGetJson(input, headerMap,
                                URLConstants.CONTRACT_CUSTOM_ROLE_GET);
                if (response.has(DBPDatasetConstants.DATASET_CONTRACTCUSTOMROLE)) {
                    JsonElement jsonElement = response.get(DBPDatasetConstants.DATASET_CONTRACTCUSTOMROLE);
                    if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
                        JsonArray jsonArray = jsonElement.getAsJsonArray();

                        for (int i = 0; i < jsonArray.size(); i++) {
                            JsonObject roleLimit = jsonArray.get(i).getAsJsonObject();
                            customRoles.add(roleLimit.has(InfinityConstants.customRoleId)
                                    && !roleLimit.get(InfinityConstants.customRoleId).isJsonNull()
                                            ? roleLimit.get(InfinityConstants.customRoleId).getAsString()
                                            : null);
                            map.get(contractEntry.getKey()).put(coreCustomerEntry.getKey(), customRoles);
                        }
                    }
                }
            }
        }

        JsonObject result = new JsonObject();
        JsonArray coreCustomerArray = new JsonArray();
        JsonArray customRoleArray = new JsonArray();
        for (Entry<String, Map<String, Set<String>>> contractEntry : map.entrySet()) {
            String contractName = "";
            filter = InfinityConstants.id + DBPUtilitiesConstants.EQUAL + contractEntry.getKey() 
            + DBPUtilitiesConstants.AND + InfinityConstants.LegalEntityId + DBPUtilitiesConstants.EQUAL + legalEntityId;

            input.put(DBPUtilitiesConstants.FILTER, filter);
            response = ServiceCallHelper.invokeServiceAndGetJson(input, headerMap, URLConstants.CONTRACT_GET);
            if (response.has(DBPDatasetConstants.DATASET_CONTRACT)) {
                JsonElement jsonElement = response.get(DBPDatasetConstants.DATASET_CONTRACT);
                if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
                    JsonArray jsonArray = jsonElement.getAsJsonArray();
                    JsonObject contractCustomer = jsonArray.get(0).getAsJsonObject();
                    contractName = contractCustomer.has(InfinityConstants.name)
                            && !contractCustomer.get(InfinityConstants.name).isJsonNull()
                                    ? contractCustomer.get(InfinityConstants.name).getAsString()
                                    : null;
                }
            }

            for (Entry<String, Set<String>> coreCustomerId : contractEntry.getValue().entrySet()) {

                String coreCustomerName = "";
                String companyLegalId = "";
                filter = InfinityConstants.coreCustomerId + DBPUtilitiesConstants.EQUAL + coreCustomerId.getKey()
                + DBPUtilitiesConstants.AND + InfinityConstants.LegalEntityId + DBPUtilitiesConstants.EQUAL + legalEntityId;
                input.put(DBPUtilitiesConstants.FILTER, filter);
                response = ServiceCallHelper.invokeServiceAndGetJson(input, headerMap,
                        URLConstants.CONTRACTCORECUSTOMER_GET);
                if (response.has(DBPDatasetConstants.DATASET_CONTRACTCORECUSTOMERS)) {
                    JsonElement jsonElement = response.get(DBPDatasetConstants.DATASET_CONTRACTCORECUSTOMERS);
                    if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
                        JsonArray jsonArray = jsonElement.getAsJsonArray();
                        JsonObject contractCustomer = jsonArray.get(0).getAsJsonObject();
                        coreCustomerName = contractCustomer.has(InfinityConstants.coreCustomerName)
                                && !contractCustomer.get(InfinityConstants.coreCustomerName).isJsonNull()
                                        ? contractCustomer.get(InfinityConstants.coreCustomerName).getAsString()
                                        : null;
                        companyLegalId = JSONUtil.hasKey(contractCustomer, InfinityConstants.LegalEntityId)
                        		? JSONUtil.getString(contractCustomer, InfinityConstants.LegalEntityId) : null;                        
                    }
                }

                JsonObject coreCustomerJsonObject = new JsonObject();

                coreCustomerJsonObject.addProperty(InfinityConstants.coreCustomerId, coreCustomerId.getKey());
                coreCustomerJsonObject.addProperty(InfinityConstants.contractId, contractEntry.getKey());
                coreCustomerJsonObject.addProperty(InfinityConstants.contractName, contractName);
                coreCustomerJsonObject.addProperty(InfinityConstants.coreCustomerName, coreCustomerName);
                coreCustomerJsonObject.addProperty(InfinityConstants.legalEntityId, companyLegalId);

                customRoleArray = new JsonArray();

                for (String customRoleId : coreCustomerId.getValue()) {
                    filter = InfinityConstants.id + DBPUtilitiesConstants.EQUAL + customRoleId;
                    input.put(DBPUtilitiesConstants.FILTER, filter);
                    try {
                        response = ServiceCallHelper.invokeServiceAndGetJson(input, headerMap,
                                URLConstants.CUSTOM_ROLE_GET);
                        if (JSONUtil.isJsonNotNull(response)
                                && JSONUtil.hasKey(response, DBPDatasetConstants.DATASET_CUSTOMROLE)
                                && response.get(DBPDatasetConstants.DATASET_CUSTOMROLE).isJsonArray()) {
                            JsonArray responseArray =
                                    response.get(DBPDatasetConstants.DATASET_CUSTOMROLE).getAsJsonArray();
                            if (responseArray.size() > 0) {
                                JsonObject customRoleJsonObject = responseArray.get(0).getAsJsonObject();
                                customRoleJsonObject.add(InfinityConstants.customRoleName,
                                        customRoleJsonObject.get(InfinityConstants.name));
                                customRoleArray.add(customRoleJsonObject);
                            }
                        }
                    } catch (Exception e) {
                        alert.prepareError("Exception occured while fetching data from custom roles", e).log();
                    }
                }
                coreCustomerJsonObject.add(InfinityConstants.customRoles, customRoleArray);

                coreCustomerArray.add(coreCustomerJsonObject);
            }
        }

        result.add(InfinityConstants.companyList, coreCustomerArray);

        dbxResult.setResponse(result);

        return dbxResult;

    }
    private void fillDataFromDTO(AssociatedContractUsersDTO userdto, JsonObject userobj) {

		userobj.addProperty("infinityUserId", userdto.getCustomerId());

		userobj.addProperty("id", userdto.getCustomerId());

		userobj.addProperty("name", userdto.getFirstName() + " " + userdto.getLastName());

		userobj.addProperty("userName", userdto.getUserName());

		userobj.addProperty("roleId", userdto.getGroupId());

		userobj.addProperty("lastSignedIn", userdto.getLastlogintime());

		userobj.addProperty("status", HelperMethods.getStatusMap().get(userdto.getStatusId()));

		userobj.addProperty("statusId", userdto.getStatusId());

		userobj.addProperty("isProfileExist", "");

		if (userdto.getCustomerId() != null && userdto.getUserName() != null

				&& userdto.getCustomerId().equals(userdto.getUserName()))

			userobj.addProperty("isProfileExist", "false");

		else if (userdto.getCustomerId() != null && userdto.getUserName() != null) {

			userobj.addProperty("isProfileExist", "true");

		userobj.addProperty("legalEntityId", userdto.getCompanyLegalUnit());
		JsonArray userLegalEntities = new JsonArray();
        if (StringUtils.isNoneBlank(userdto.getCustomerId())&& StringUtils.isNotBlank(userdto.getCompanyLegalUnit())) {
            CustomerLegalEntityDTO customerLegalEntityDTO = null;
            customerLegalEntityDTO = new CustomerLegalEntityDTO();
            customerLegalEntityDTO.setCustomer_id(userdto.getCustomerId());
            List<CustomerLegalEntityDTO> customerLegalEntityIds = (List<CustomerLegalEntityDTO>) customerLegalEntityDTO
                    .loadDTO();
            if (customerLegalEntityIds != null && !customerLegalEntityIds.isEmpty()) {
                for (CustomerLegalEntityDTO customerLegalEntityDTOs : customerLegalEntityIds) {
                	if(!customerLegalEntityDTOs.getStatus_id().equals(InfinityConstants.SID_CUS_SUSPENDED))
                    userLegalEntities.add(customerLegalEntityDTOs.getLegalEntityId().toString());
                }
                userobj.addProperty("userLegalEntities", userLegalEntities.toString());
            }
        }
    }
}

    private DBXResult generateResultfromMap(Map<String, Map<String, JsonArray>> contractCorecustomers,

			Map<String, String> contractnamedata, Map<String, String> customernamedata) {

		JsonArray contractList = new JsonArray();

     


        JsonObject result = new JsonObject();
        DBXResult dbxResult = new DBXResult();

		for (String contract : contractCorecustomers.keySet()) {

			Map<String, JsonArray> companies = contractCorecustomers.get(contract);

			JsonObject contractjson = new JsonObject();


			JsonArray companiesjsonobj = new JsonArray();

			for (String company : companies.keySet()) {

				JsonObject companyjsonobj = new JsonObject();

				companyjsonobj.addProperty("coreCustomerId", company);

				companyjsonobj.addProperty("coreCustomerName", customernamedata.get(company));

				companyjsonobj.add("users", companies.get(company));

				companiesjsonobj.add(companyjsonobj);
                }
            
        

			contractjson.addProperty("contractId", contract);

			contractjson.addProperty("contractName", contractnamedata.get(contract));

			contractjson.add("companies", companiesjsonobj);

			contractList.add(contractjson);
		}           
			result.add(InfinityConstants.companyList, contractList);
		
        
		dbxResult.setResponse(result);

		return dbxResult;

	}

		private DBXResult processDBResponse(List<AssociatedContractUsersDTO> customers) {
			Map<String, Map<String, JsonArray>> contractCorecustomers = new HashMap<>();

			Map<String, String> contractnamedata = new HashMap<>();

			Map<String, String> customenamedata = new HashMap<>();

			for (AssociatedContractUsersDTO customerrecord : customers) {

				String contractid = customerrecord.getContractId();

				Map<String, JsonArray> companies = new HashMap<>();

				if (contractCorecustomers.containsKey(contractid)) {

					companies = contractCorecustomers.get(contractid);

				} else {

					contractCorecustomers.put(contractid, companies);

					contractnamedata.put(customerrecord.getContractId(), customerrecord.getContractName());

				
            }
				String corecustomerid = customerrecord.getCoreCustomerId();

				JsonArray users = new JsonArray();

				if (companies.containsKey(corecustomerid)) {

					users = companies.get(corecustomerid);

				} else {

					companies.put(corecustomerid, users);

					customenamedata.put(customerrecord.getCoreCustomerId(), customerrecord.getCoreCustomerName());
            }

				JsonObject user = new JsonObject();

				fillDataFromDTO(customerrecord, user);

				users.add(user);
        }
			return generateResultfromMap(contractCorecustomers, contractnamedata, customenamedata);

		}

       
		@Override

		public DBXResult getAssociatedContractUsers(JsonObject jsonObject, Map<String, Object> headerMap) {

			

			DBXResult dbxResult = new DBXResult();

			Map<String, Object> input = new HashMap<String, Object>();

			String customerId = jsonObject.get(InfinityConstants.id).getAsString();
			String legalEntityId = jsonObject.get(InfinityConstants.legalEntityId).getAsString();

			try {

				input.put("_id", customerId);
				input.put("_legalEntityId", legalEntityId);

				String backendType = "T24";

				input.put("_backendType", backendType);

				List<AssociatedContractUsersDTO> customers;

				JsonObject response = ServiceCallHelper.invokeServiceAndGetJson(input, headerMap,

						URLConstants.GET_ASSOCIATED_CONTRACTUSERS_PROC);

				if (JSONUtil.hasKey(response, DBPDatasetConstants.DATASET_RECORDS)

						&& response.get(DBPDatasetConstants.DATASET_RECORDS).getAsJsonArray().size() > 0) {

					customers = JSONUtils.parseAsList(

							response.get(DBPDatasetConstants.DATASET_RECORDS).getAsJsonArray().toString(),

							AssociatedContractUsersDTO.class);

					return processDBResponse(customers);

				}


			} catch (Exception e) {

				alert.prepareError("Error occured", e).log();

			}

			return dbxResult;

			

		}

    @Override
    public List<ContractCustomersDTO> getInfinityContractCustomers(ContractCustomersDTO dto, String filter,
            Map<String, Object> headersMap) throws ApplicationException {
        List<ContractCustomersDTO> responseDTOList = new ArrayList<>();
        Map<String, Object> inputParams = new HashMap<>();
        
        if (StringUtils.isNotBlank(filter))
        	filter = "("+filter+")";
        else if (StringUtils.isNotBlank(dto.getCustomerId())) {
        	filter = "customerId" + DBPUtilitiesConstants.EQUAL + dto.getCustomerId();
        }
        else if (StringUtils.isNotBlank(dto.getCoreCustomerId())) {
        	filter = "coreCustomerId" + DBPUtilitiesConstants.EQUAL + dto.getCoreCustomerId();
        }
		
        if (StringUtils.isNotBlank(filter) && dto != null && StringUtils.isNotBlank(dto.getCompanyLegalUnit())) {
			filter = filter + DBPUtilitiesConstants.AND + "companyLegalUnit" + DBPUtilitiesConstants.EQUAL
					+ dto.getCompanyLegalUnit();
		}
        
        if(StringUtils.isBlank(filter)) {
        	alert.prepareError("input payload is empty. Hence returning empty list.").log();
        	return responseDTOList;
        }
        
        inputParams.put(DBPUtilitiesConstants.FILTER, filter);
        
        try {
            JsonObject response = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                    URLConstants.CONTRACT_CUSTOMERS_GET);
            if (JSONUtil.hasKey(response, DBPDatasetConstants.DATASET_CONTRACT_CUSTOMERS)
                    && response.get(DBPDatasetConstants.DATASET_CONTRACT_CUSTOMERS).isJsonArray()
                    && response.get(DBPDatasetConstants.DATASET_CONTRACT_CUSTOMERS).getAsJsonArray().size() > 0) {
                for (JsonElement jsonelement : response.get(DBPDatasetConstants.DATASET_CONTRACT_CUSTOMERS)
                        .getAsJsonArray()) {
                    ContractCustomersDTO childdto = new ContractCustomersDTO();
                    childdto.setCustomerId(JSONUtil.getString(jsonelement.getAsJsonObject(), "customerId"));
                    childdto.setContractId(JSONUtil.getString(jsonelement.getAsJsonObject(), "contractId"));
                    childdto.setCoreCustomerId(JSONUtil.getString(jsonelement.getAsJsonObject(), "coreCustomerId"));
                    childdto.setAutoSyncAccounts(
                            Boolean.valueOf(JSONUtil.getString(jsonelement.getAsJsonObject(), "autoSyncAccounts")));
                    responseDTOList.add(childdto);
                }
            }
        } catch (Exception e) {
            alert.prepareError(
                    "InfinityUserManagementBackendDelegateImpl : Exception occured while fetching infinity user contract details "
                            + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10763);
        }
        return responseDTOList;
    }

    @Override
    public DBXResult getInfinityUserContractDetailsGetOperation(String infinityUserId, String infinityLegalEntityId,Map<String, Object> headersMap)
            throws ApplicationException {
        DBXResult dbxresult = new DBXResult();
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("_id", infinityUserId);
        inputParams.put("_legalEntityId",infinityLegalEntityId);
        try {
            JsonObject response = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                    URLConstants.INFINITYUSER_CONTRACTDETAILS_GET);
           
            if (JSONUtil.hasKey(response, DBPDatasetConstants.DATASET_RECORDS)
                    && response.get(DBPDatasetConstants.DATASET_RECORDS).isJsonArray()
                    && response.get(DBPDatasetConstants.DATASET_RECORDS).getAsJsonArray().size() > 0) {
                dbxresult.setResponse(response.get(DBPDatasetConstants.DATASET_RECORDS).getAsJsonArray());
            }
        } catch (Exception e) {
            alert.prepareError(
                    "InfinityUserManagementBackendDelegateImpl : Exception occured while fetching infinity user contract details "
                            + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10789);
        }
        
        return dbxresult;
    }

    @Override
    public DBXResult generateInfinityUserName(String userNameLength) {
        DBXResult result = new DBXResult();
        result.setResponse(HelperMethods.getUniqueNumericString(Integer.valueOf(userNameLength)));
        return result;
    }

    @Override
    public DBXResult generateActivationCode(String activationCodeLength) {
        DBXResult result = new DBXResult();
        StringBuilder sb = new StringBuilder(Integer.valueOf(activationCodeLength));
        String alphaNumbericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";
        try {
            SecureRandom random = SecureRandom.getInstance(RANDOM_GENERATOR_ALGORITHM);
            double randomNumber;
            for (int i = 0; i < Integer.valueOf(activationCodeLength); i++) {
                randomNumber = random.nextDouble();
                int index = (int) (alphaNumbericString.length()
                        * randomNumber);
                sb.append(alphaNumbericString
                        .charAt(index));
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        result.setResponse(sb.toString());
        return result;
    }

    @Override
    public DBXResult processNewAccounts(String customerId, Map<String, Object> headersMap) throws ApplicationException {
        DBXResult dbxresult = new DBXResult();
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("customerId", customerId);
        try {
            JsonObject response =
                    ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                            URLConstants.USER_ASSOCIATED_CORECUSTOMERACCOUNTS_INFO);
            alert.prepareError(response.toString()).log();
            Map<String, Set<String>> information = new HashMap<>();
            information.put("implicitCoreCustomers", null);
            information.put("nonCIFAccounts", null);
            information.put("contractaccounts", null);
            information.put("excludedcontractaccounts", null);
            information.put("customeraccounts", null);
            information.put("excludedcustomeraccounts", null);
            boolean implicitCIFsPresent = false;
            if (response != null) {
                if (JSONUtil.hasKey(response, DBPDatasetConstants.DATASET_RECORDS)
                        && response.get(DBPDatasetConstants.DATASET_RECORDS) != null &&
                        response.get(DBPDatasetConstants.DATASET_RECORDS).isJsonArray()
                        && response.get(DBPDatasetConstants.DATASET_RECORDS).getAsJsonArray().size() > 0) {
                    implicitCIFsPresent = true;
                    Set<String> coreCustomerId = new HashSet<>();
                    for (JsonElement jsonelement : response.get(DBPDatasetConstants.DATASET_RECORDS)
                            .getAsJsonArray()) {
                        if (jsonelement.getAsJsonObject().get("coreCustomerId") != null)
                            coreCustomerId.add(jsonelement.getAsJsonObject().get("coreCustomerId").getAsString() + ":"
                                    + jsonelement.getAsJsonObject().get("contractId").getAsString());
                    }
                    information.put("implicitCoreCustomers", coreCustomerId);
                }
                if (JSONUtil.hasKey(response, DBPDatasetConstants.DATASET_RECORDS + "1")
                        && response.get(DBPDatasetConstants.DATASET_RECORDS + "1") != null &&
                        response.get(DBPDatasetConstants.DATASET_RECORDS + "1").isJsonArray()
                        && response.get(DBPDatasetConstants.DATASET_RECORDS + "1").getAsJsonArray().size() > 0) {
                    Set<String> nonCIFAccounts = new HashSet<>();
                    for (JsonElement jsonelement : response.get(DBPDatasetConstants.DATASET_RECORDS + "1")
                            .getAsJsonArray()) {
                        if (jsonelement.getAsJsonObject().get("nonCIFAccounts") != null)
                            nonCIFAccounts.add(jsonelement.getAsJsonObject().get("nonCIFAccounts").getAsString());
                    }
                    information.put("nonCIFAccounts", nonCIFAccounts);
                }
                if (implicitCIFsPresent) {
                    if (JSONUtil.hasKey(response, DBPDatasetConstants.DATASET_RECORDS + "2")
                            && response.get(DBPDatasetConstants.DATASET_RECORDS + "2") != null &&
                            response.get(DBPDatasetConstants.DATASET_RECORDS + "2").isJsonArray()
                            && response.get(DBPDatasetConstants.DATASET_RECORDS + "2").getAsJsonArray().size() > 0) {
                        Set<String> contractaccounts = new HashSet<>();
                        for (JsonElement jsonelement : response.get(DBPDatasetConstants.DATASET_RECORDS + "2")
                                .getAsJsonArray()) {
							if (jsonelement.getAsJsonObject().get("contractaccounts") != null
									&& jsonelement.getAsJsonObject().get("contractcustomer") != null)
								contractaccounts.add(jsonelement.getAsJsonObject().get("contractcustomer") + ":"
										+ jsonelement.getAsJsonObject().get("contractaccounts").getAsString());
                        }
                        information.put("contractaccounts", contractaccounts);
                    }
                    if (JSONUtil.hasKey(response, DBPDatasetConstants.DATASET_RECORDS + "3")
                            && response.get(DBPDatasetConstants.DATASET_RECORDS + "3") != null &&
                            response.get(DBPDatasetConstants.DATASET_RECORDS + "3").isJsonArray()
                            && response.get(DBPDatasetConstants.DATASET_RECORDS + "3").getAsJsonArray().size() > 0) {
                        Set<String> excludedcontractaccounts = new HashSet<>();
                        for (JsonElement jsonelement : response.get(DBPDatasetConstants.DATASET_RECORDS + "3")
                                .getAsJsonArray()) {
							if (jsonelement.getAsJsonObject().get("excludedcontractaccounts") != null
									&& jsonelement.getAsJsonObject().get("contractcustomer") != null)
								excludedcontractaccounts.add(jsonelement.getAsJsonObject().get("contractcustomer")
										.getAsString() + ":"
										+ jsonelement.getAsJsonObject().get("excludedcontractaccounts").getAsString());
                        }
                        information.put("excludedcontractaccounts", excludedcontractaccounts);
                    }
                    if (JSONUtil.hasKey(response, DBPDatasetConstants.DATASET_RECORDS + "4")
                            && response.get(DBPDatasetConstants.DATASET_RECORDS + "4") != null &&
                            response.get(DBPDatasetConstants.DATASET_RECORDS + "4").isJsonArray()
                            && response.get(DBPDatasetConstants.DATASET_RECORDS + "4").getAsJsonArray().size() > 0) {
                        Set<String> customeraccounts = new HashSet<>();
                        for (JsonElement jsonelement : response.get(DBPDatasetConstants.DATASET_RECORDS + "4")
                                .getAsJsonArray()) {
                            if (jsonelement.getAsJsonObject().get("customeraccounts") != null)
                                customeraccounts
                                        .add(jsonelement.getAsJsonObject().get("customeraccounts").getAsString());
                        }
                        information.put("customeraccounts", customeraccounts);
                    }
                    if (JSONUtil.hasKey(response, DBPDatasetConstants.DATASET_RECORDS + "5")
                            && response.get(DBPDatasetConstants.DATASET_RECORDS + "5") != null &&
                            response.get(DBPDatasetConstants.DATASET_RECORDS + "5").isJsonArray()
                            && response.get(DBPDatasetConstants.DATASET_RECORDS + "5").getAsJsonArray().size() > 0) {
                        Set<String> excludedcustomeraccounts = new HashSet<>();
                        for (JsonElement jsonelement : response.get(DBPDatasetConstants.DATASET_RECORDS + "5")
                                .getAsJsonArray()) {
                            if (jsonelement.getAsJsonObject().get("excludedcustomeraccounts") != null)
                                excludedcustomeraccounts
                                        .add(jsonelement.getAsJsonObject().get("excludedcustomeraccounts")
                                                .getAsString());
                        }
                        information.put("excludedcustomeraccounts", excludedcustomeraccounts);
                    }
                }
            }
            dbxresult.setResponse(information);
        } catch (Exception e) {
            alert.prepareError(
                    "Exception occured while fetching user associated customer accounts information" + e.getMessage()).log();
        }
        return dbxresult;
    }

    @Override
    public DBXResult getInfinityUserPrimaryRetailContract(JsonObject inputJson, Map<String, Object> headerMap) {

        DBXResult dbxResult = new DBXResult();

        JsonObject jsonObject = new JsonObject();
        dbxResult.setResponse(jsonObject);

        String customerId = inputJson.get(InfinityConstants.id).getAsString();

        String loggedInUserId = inputJson.get(InfinityConstants.loggedInUserId).getAsString();

        boolean isSuperAdmin = Boolean.parseBoolean(inputJson.get(InfinityConstants.isSuperAdmin).getAsString());
        
        String legalEntityId = inputJson.get(InfinityConstants.legalEntityId).getAsString();

        JsonObject customerjsonObject = getCustomerJson(customerId, headerMap, legalEntityId); 

        String coreCustomerId = customerjsonObject.has(InfinityConstants.coreCustomerId)
                && !customerjsonObject.get(InfinityConstants.coreCustomerId).isJsonNull()
                        ? customerjsonObject.get(InfinityConstants.coreCustomerId).getAsString()
                        : "";

        if (StringUtils.isBlank(coreCustomerId)) {
            return dbxResult;
        }

        JsonArray jsonArray = new JsonArray();
        jsonObject.add(InfinityConstants.userDetails, customerjsonObject);

        Map<String, Map<String, Map<String, Map<String, String>>>> map =
                new HashMap<String, Map<String, Map<String, Map<String, String>>>>();

        Map<String, List<String>> companyMap = new HashMap<>();

        Map<String, Map<String, Map<String, String>>> accountMap =
                new HashMap<String, Map<String, Map<String, String>>>();

        Map<String, FeatureActionLimitsDTO> featureActionDTOs = new HashMap<String, FeatureActionLimitsDTO>();

        Map<String, JsonObject> contracts = new HashMap<String, JsonObject>();
        Map<String, JsonObject> contractCoreCustomers = new HashMap<String, JsonObject>();
        Map<String, JsonObject> coreCustomerGroupInfo = new HashMap<>();

        JsonArray actions = new JsonArray();

        Map<String, Object> input = new HashMap<String, Object>();

        String filter =
                InfinityConstants.Customer_id + DBPUtilitiesConstants.EQUAL + customerId + DBPUtilitiesConstants.AND
                        + InfinityConstants.coreCustomerId + DBPUtilitiesConstants.EQUAL + coreCustomerId;
        input.put(DBPUtilitiesConstants.FILTER, filter);
        JsonObject response =
                ServiceCallHelper.invokeServiceAndGetJson(input, headerMap, URLConstants.CUSTOMER_ACTION_LIMITS_GET);
        if (response.has(DBPDatasetConstants.DATASET_CUSTOMERACTION)) {
            JsonElement jsonElement = response.get(DBPDatasetConstants.DATASET_CUSTOMERACTION);
            if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
                actions = jsonElement.getAsJsonArray();
            }
        }
        JsonArray excludedActions = new JsonArray();

        filter = InfinityConstants.Customer_id + DBPUtilitiesConstants.EQUAL + customerId;
        input.put(DBPUtilitiesConstants.FILTER, filter);
        response = ServiceCallHelper.invokeServiceAndGetJson(input, headerMap,
                URLConstants.EXCLUDED_CUSTOMER_ACTION_LIMITS_GET);
        if (response.has(DBPDatasetConstants.DATASET_EXCLUDED_CUSTOMERACTION)) {
            JsonElement jsonElement = response.get(DBPDatasetConstants.DATASET_EXCLUDED_CUSTOMERACTION);
            if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
                excludedActions = jsonElement.getAsJsonArray();
            }
        }

        JsonArray accounts = new JsonArray();
        input = new HashMap<String, Object>();
        filter = InfinityConstants.Customer_id + DBPUtilitiesConstants.EQUAL + customerId + DBPUtilitiesConstants.AND
                + InfinityConstants.coreCustomerId + DBPUtilitiesConstants.EQUAL + coreCustomerId;
        input.put(DBPUtilitiesConstants.FILTER, filter);
        response =
                ServiceCallHelper.invokeServiceAndGetJson(input, headerMap, URLConstants.CUSTOMERACCOUNTS_GET);
        if (response.has(DBPDatasetConstants.DATASET_CUSTOMERACCOUNTS)) {
            JsonElement jsonElement = response.get(DBPDatasetConstants.DATASET_CUSTOMERACCOUNTS);
            if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
                accounts = jsonElement.getAsJsonArray();
            }
        }

        jsonArray = getCompanyList(coreCustomerGroupInfo, null, null, contracts, contractCoreCustomers, customerId, headerMap, map,
                companyMap, accountMap, featureActionDTOs, loggedInUserId, accounts, isSuperAdmin, false, legalEntityId);

        jsonObject.add(InfinityConstants.companyList, jsonArray);

        if (!isPrimaryContractRetail(jsonArray, headerMap)) {
            return dbxResult;
        }
        addPermissions(coreCustomerGroupInfo, contracts, contractCoreCustomers, customerId, headerMap, map, jsonObject,
                accountMap, companyMap, featureActionDTOs, isSuperAdmin, actions,excludedActions,false);
        dbxResult.setResponse(jsonObject);

        return dbxResult;

    }

    private boolean isPrimaryContractRetail(JsonArray jsonArray, Map<String, Object> headerMap) {
        if (jsonArray.size() != 1) {
            return false;
        }
        JsonObject jsonObject = jsonArray.get(0).isJsonObject() ? jsonArray.get(0).getAsJsonObject() : new JsonObject();
        String contractId = jsonObject.has(InfinityConstants.contractId)
                && !jsonObject.get(InfinityConstants.contractId).isJsonNull()
                        ? jsonObject.get(InfinityConstants.contractId).getAsString()
                        : "";
        if (StringUtils.isBlank(contractId)) {
            return false;
        }

        String filter = InfinityConstants.id + DBPUtilitiesConstants.EQUAL + contractId;
        Map<String, Object> input = new HashMap<String, Object>();
        input.put(DBPUtilitiesConstants.FILTER, filter);
        jsonObject = ServiceCallHelper.invokeServiceAndGetJson(input, headerMap, URLConstants.CONTRACT_GET);
        input.clear();
        String contractTypeId = "";
        if (jsonObject.has(DBPDatasetConstants.DATASET_CONTRACT)) {
            JsonElement jsonElement = jsonObject.get(DBPDatasetConstants.DATASET_CONTRACT);
            if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
                jsonObject = jsonElement.getAsJsonArray().get(0).getAsJsonObject();
                contractTypeId = jsonObject.get(InfinityConstants.serviceType).getAsString();
            }
        }

        if (Constants.TYPE_ID_RETAIL.equals(contractTypeId)) {
            return true;
        }

        return false;
    }

    @Override
	public List<UserCustomerViewDTOwithAccounts> getAssociatedCustomerswithaccounts(
			ContractCustomersDTO contractCustomerDTO, Map<String, Object> headerMap) throws ApplicationException {
    	diagnostic.prepareDebug("****************************** InfinityUserManagementBackendDelegateImpl getAssociatedCustomers start ***************************************************************").log();
        List<UserCustomerViewDTOwithAccounts> customers = new ArrayList<>();
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("_customerId", contractCustomerDTO.getCustomerId());
        inputParams.put("_legalEntityId", contractCustomerDTO.getCompanyLegalUnit());
		if (StringUtils.isNotBlank(contractCustomerDTO.getContractId())) {
	        inputParams.put("_contractId", contractCustomerDTO.getContractId());
			return getUserCustomersByContractId(inputParams, headerMap);
		} else {
			inputParams.put("_coreCustomerId", "");
			try {
				JsonObject response = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
						URLConstants.USER_CUSTOMERS_WITH_ACCOUNTS_PROC);

				diagnostic.prepareDebug(
						"****************************** InfinityUserManagementBackendDelegateImpl getAssociatedCustomers response :"
								+ response).log();

				if (JSONUtil.hasKey(response, DBPDatasetConstants.DATASET_RECORDS)
						&& response.get(DBPDatasetConstants.DATASET_RECORDS).getAsJsonArray().size() > 0) {
					customers = JSONUtils.parseAsList(
							response.get(DBPDatasetConstants.DATASET_RECORDS).getAsJsonArray().toString(),
							UserCustomerViewDTOwithAccounts.class);
				}
			} catch (Exception e) {
				alert.prepareError(
						"InfinityUserManagementBackendDelegateImpl : Exception occured while fetching the associated customers info "
								+ e.getMessage()).log();
				throw new ApplicationException(ErrorCodeEnum.ERR_10763);
			}
		}

		diagnostic.prepareDebug(
				"****************************** InfinityUserManagementBackendDelegateImpl getAssociatedCustomers end ***************************************************************").log();
		return customers;
	}
    
    private List<UserCustomerViewDTOwithAccounts> getUserCustomersByContractId(Map<String,Object> inputParams,Map<String,Object> headerMap ) throws ApplicationException
    {
    	List<UserCustomerViewDTOwithAccounts> customers = new ArrayList<>();
    	try {
			JsonObject response = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
					URLConstants.USER_CONTRACTS_WITH_ACCOUNTS_PROC);

			diagnostic.prepareDebug(
					"****************************** InfinityUserManagementBackendDelegateImpl getAssociatedCustomers response :"
							+ response).log();

			if (JSONUtil.hasKey(response, DBPDatasetConstants.DATASET_RECORDS)
					&& response.get(DBPDatasetConstants.DATASET_RECORDS).getAsJsonArray().size() > 0) {
				customers = JSONUtils.parseAsList(
						response.get(DBPDatasetConstants.DATASET_RECORDS).getAsJsonArray().toString(),
						UserCustomerViewDTOwithAccounts.class);
			}
		} catch (Exception e) {
			alert.prepareError(
					"InfinityUserManagementBackendDelegateImpl : Exception occured while fetching the associated customers info "
							+ e.getMessage()).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_10763);
		}
    	return customers;
        
    }
    public String callClosedaccountContractId(Map<String, Object> inputParams, Map<String, Object> headerMap) {
    	JsonObject checkAccountStatus = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
				URLConstants.CONTRACTS_WITH_ACTIVEACCOUNTS);
    	String contractIdsResult = "";
		if (JSONUtil.hasKey(checkAccountStatus, DBPDatasetConstants.DATASET_RECORDS)) {
			JsonElement jsonElement1 = checkAccountStatus.get(DBPDatasetConstants.DATASET_RECORDS);
			if (jsonElement1.isJsonArray() && jsonElement1.getAsJsonArray().size() > 0) {
				JsonArray jsonArray1 = jsonElement1.getAsJsonArray();
				for (int j = 0; j < jsonArray1.size(); j++) {
					JsonObject filteredContratId = jsonArray1.get(j).getAsJsonObject();
					contractIdsResult = filteredContratId.get(InfinityConstants.contractIds).getAsString();

				}
			}
		}
		return contractIdsResult;
    	
    }
    public JsonArray filteredContractIdsArray(JsonArray jsonArray, JsonObject jsonObject, Map<String,Object> headerMap) {
    	jsonObject.add(InfinityConstants.companyList, jsonArray);
        String contractIds = "";
		String contractIdsResult = "";
		JsonElement jsonElement = jsonObject.get("companyList");
    	if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
			JsonArray jsonArray1 = jsonElement.getAsJsonArray();
			for (int i = 0; i < jsonArray1.size(); i++) {
				JsonObject contractCustomer = jsonArray1.get(i).getAsJsonObject();
				String contractId = contractCustomer.get(InfinityConstants.contractId).getAsString();
				contractIds += contractId + DBPUtilitiesConstants.COMMA_SEPERATOR;

			}
		}
		Map<String, Object> inputParams = new HashMap<String, Object>();
		inputParams.put("_contractIdList", contractIds);
		contractIdsResult = callClosedaccountContractId(inputParams, headerMap);
		List<String> accountActiveContracts = Arrays.asList(contractIdsResult.split(","));
		if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0
				&& !accountActiveContracts.get(0).isEmpty()) {
			JsonArray jsonArray1 = jsonElement.getAsJsonArray();
			for (int i = 0; i < jsonArray1.size(); i++) {
				JsonObject contractCustomer = jsonArray1.get(i).getAsJsonObject();
				String contractId = contractCustomer.get(InfinityConstants.contractId).getAsString();
				if (!accountActiveContracts.contains(contractId)) {
					jsonArray.remove(i);
				}

			}
		} else {
			jsonArray = new JsonArray();
		}
    	
		return jsonArray;
    	
    }

	@Override
	public Map<String, String> getUserLegalEntites(String customerId, Map<String, Object> headersMap) {
		JsonObject customerLegalEntities;
		Map<String, String> userLegalEntites = new HashMap<>();
		String filter = InfinityConstants.customerId + DBPUtilitiesConstants.EQUAL + customerId;
		Map<String, Object> input = new HashMap<String, Object>();
		input.put(DBPUtilitiesConstants.FILTER, filter);
		customerLegalEntities = ServiceCallHelper.invokeServiceAndGetJson(input, headersMap,
				URLConstants.CONTRACT_CUSTOMERS_GET);
		if (customerLegalEntities != null
				&& customerLegalEntities.has(DBPDatasetConstants.DATASET_CONTRACT_CUSTOMERS)) {
			JsonElement customersElem = customerLegalEntities.get(DBPDatasetConstants.DATASET_CONTRACT_CUSTOMERS);
			if (customersElem != null && customersElem.isJsonArray() && customersElem.getAsJsonArray().size() > 0) {
				for (int i = 0; i < customersElem.getAsJsonArray().size(); i++) {
					JsonObject customer = customersElem.getAsJsonArray().get(i).getAsJsonObject();
					if (customer.has("coreCustomerId") && !customer.get("coreCustomerId").isJsonNull()
							&& customer.has("companyLegalUnit") && !customer.get("companyLegalUnit").isJsonNull()) {
						userLegalEntites.put(customer.get("coreCustomerId").getAsString(),
								customer.get("companyLegalUnit").getAsString());
					}
				}

			}
		}

		return userLegalEntites;
	}
}