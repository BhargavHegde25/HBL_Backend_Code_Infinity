package com.temenos.dbx.eum.product.usermanagement.businessdelegate.impl;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Callable;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.common.collect.Sets;
import com.google.gson.JsonParser;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.BundleConfigurationHandler;
import com.kony.dbputilities.util.DBPDatasetConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.kony.eum.dbputilities.kms.KMSUtil;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.eum.product.contract.backenddelegate.api.ContractBackendDelegate;
import com.temenos.dbx.eum.product.contract.backenddelegate.api.ContractCoreCustomerBackendDelegate;
import com.temenos.dbx.eum.product.contract.backenddelegate.api.CoreCustomerBackendDelegate;
import com.temenos.dbx.eum.product.contract.businessdelegate.api.ContractAccountsBusinessDelegate;
import com.temenos.dbx.eum.product.contract.businessdelegate.api.ContractBusinessDelegate;
import com.temenos.dbx.eum.product.contract.businessdelegate.api.ContractCoreCustomerBusinessDelegate;
import com.temenos.dbx.eum.product.contract.businessdelegate.api.ContractFeatureActionsBusinessDelegate;
import com.temenos.dbx.eum.product.contract.businessdelegate.api.CoreCustomerBusinessDelegate;
import com.temenos.dbx.eum.product.usermanagement.backenddelegate.api.InfinityUserManagementBackendDelegate;
import com.temenos.dbx.eum.product.usermanagement.backenddelegate.api.ProfileManagementBackendDelegate;
import com.temenos.dbx.eum.product.usermanagement.backenddelegate.api.UserManagementBackendDelegate;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.BackendIdentifierBusinessDelegate;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.CustomerAccountsBusinessDelegate;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.CustomerActionsBusinessDelegate;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.InfinityUserManagementBusinessDelegate;
import com.temenos.dbx.product.approvalmatrixservices.businessdelegate.api.ApprovalMatrixBusinessDelegate;
import com.temenos.dbx.product.businessdelegate.api.KMSBusinessDelegate;
import com.temenos.dbx.product.dto.BackendIdentifierDTO;
import com.temenos.dbx.product.dto.ContractAccountsDTO;
import com.temenos.dbx.product.dto.ContractCoreCustomersDTO;
import com.temenos.dbx.product.dto.ContractCustomersDTO;
import com.temenos.dbx.product.dto.ContractDTO;
import com.temenos.dbx.product.dto.CustomerAccountsDTO;
import com.temenos.dbx.product.dto.CustomerActionDTO;
import com.temenos.dbx.product.dto.CustomerCommunicationDTO;
import com.temenos.dbx.product.dto.CustomerDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.dto.FeatureActionLimitsDTO;
import com.temenos.dbx.product.dto.MembershipDTO;
import com.temenos.dbx.product.dto.UserCustomerViewDTO;
import com.temenos.dbx.product.dto.UserCustomerViewDTOwithAccounts;
import com.temenos.dbx.product.usermanagement.backenddelegate.api.CommunicationBackendDelegate;
import com.temenos.dbx.product.utils.DTOConstants;
import com.temenos.dbx.product.utils.InfinityConstants;
import com.temenos.dbx.product.utils.ThreadExecutor;

public class InfinityUserManagementBusinessDelegateImpl implements InfinityUserManagementBusinessDelegate {
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	@Override
	public DBXResult getAssociatedCustomers(ContractCustomersDTO contractCustomerDTO, Map<String, Object> headerMap)
			throws ApplicationException {
		DBXResult result = new DBXResult();
		List<UserCustomerViewDTO> response = new ArrayList<>();
		String customers = "";
		try {
			InfinityUserManagementBackendDelegate infinityUserManagementBackendDelegate = DBPAPIAbstractFactoryImpl
					.getBackendDelegate(InfinityUserManagementBackendDelegate.class);
			response = infinityUserManagementBackendDelegate.getAssociatedCustomers(contractCustomerDTO, headerMap);
			customers = JSONUtils.stringifyCollectionWithTypeInfo(response, UserCustomerViewDTO.class);
			JsonObject jsonobject = new JsonObject();
			JsonArray jsonarray = new JsonParser().parse(customers).getAsJsonArray();
			//JsonArray responseArray = isViewPermissionEnbled(jsonarray, contractCustomerDTO.getCustomerId(), headerMap);
			jsonobject.add(DBPDatasetConstants.DATASET_CUSTOMERS,  jsonarray);
			result.setResponse(jsonobject);
		} catch (ApplicationException e) {
			alert.prepareError(
					"InfinityUserManagementBusinessDelegateImpl : Exception occured while fetching the associated customers info "
							+ e.getMessage()).log();
			throw new ApplicationException(e.getErrorCodeEnum());
		} catch (Exception e) {
			alert.prepareError(
					"InfinityUserManagementBusinessDelegateImpl : Exception occured while fetching the associated customers info "
							+ e.getMessage()).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_10763);
		}
		return result;
	}

	private JsonArray isViewPermissionEnbled(JsonArray customers, String loggedInUserId,
			Map<String, Object> headerMap) {

		JsonArray responseArray = new JsonArray();
		for (JsonElement json : customers) {
			String filter = InfinityConstants.Customer_id + DBPUtilitiesConstants.EQUAL + loggedInUserId
					+ DBPUtilitiesConstants.AND + InfinityConstants.coreCustomerId + DBPUtilitiesConstants.EQUAL
					+ JSONUtil.getString(json.getAsJsonObject(), "coreCustomerId") + DBPUtilitiesConstants.AND
					+ InfinityConstants.featureId + DBPUtilitiesConstants.EQUAL + "USER_MANAGEMENT"
					+ DBPUtilitiesConstants.AND + InfinityConstants.Action_id + DBPUtilitiesConstants.EQUAL
					+ "USER_MANAGEMENT_VIEW";
			Map<String, Object> input = new HashMap<String, Object>();
			input.put(DBPUtilitiesConstants.FILTER, filter);
			JsonObject response = ServiceCallHelper.invokeServiceAndGetJson(input, headerMap,
					URLConstants.CUSTOMER_ACTION_LIMITS_GET);
			if (response.has(DBPDatasetConstants.DATASET_CUSTOMERACTION)) {
				JsonElement jsonElement = response.get(DBPDatasetConstants.DATASET_CUSTOMERACTION);
				if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
					responseArray.add(json);
				}
			}
		}
		return responseArray;
	}

	@Override
	public DBXResult getAllEligibleRelationalCustomers(String coreCustomerId, Map<String, Object> headersMap, String legalEntityId)
			throws ApplicationException {
		DBXResult result = new DBXResult();
		try {
			CoreCustomerBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl
					.getBackendDelegate(CoreCustomerBackendDelegate.class);
			MembershipDTO membershipDTO = new MembershipDTO();
			membershipDTO.setId(coreCustomerId);
			membershipDTO.setCompanyLegalUnit(legalEntityId);
			DBXResult customers = backendDelegate.getCoreRelativeCustomers(membershipDTO, headersMap);
			JsonArray jsonarray = new JsonArray();
			if (customers != null && customers.getResponse() != null) {
				jsonarray = (JsonArray) customers.getResponse();
			}
			if (jsonarray.size() > 0) {
				ContractCoreCustomerBackendDelegate contractCoreCustomerBackendDelegate = DBPAPIAbstractFactoryImpl
						.getBackendDelegate(ContractCoreCustomerBackendDelegate.class);
				ContractCoreCustomersDTO contractCoreCustomersDTO = new ContractCoreCustomersDTO();
				contractCoreCustomersDTO.setCoreCustomerId(coreCustomerId);
				contractCoreCustomersDTO.setCompanyLegalUnit(legalEntityId);
				contractCoreCustomersDTO = contractCoreCustomerBackendDelegate
						.getContractCoreCustomers(contractCoreCustomersDTO, headersMap);
				String contractId = contractCoreCustomersDTO.getContractId();
				Map<String, String> backendIdCustomerId = new HashMap<>();
				List<BackendIdentifierDTO> dtoList = new ArrayList<>();
				for (JsonElement jsonelement : jsonarray) {
					BackendIdentifierDTO dto = new BackendIdentifierDTO();
					dto.setBackendId(JSONUtil.getString(jsonelement.getAsJsonObject(), "id"));
					dto.setCompanyLegalUnit(legalEntityId);
					dtoList.add(dto);
				}
				BackendIdentifierBusinessDelegate backendIdentifierBusinessDelegate = DBPAPIAbstractFactoryImpl
						.getBusinessDelegate(BackendIdentifierBusinessDelegate.class);
				dtoList = backendIdentifierBusinessDelegate.getBackendIdentifierList(dtoList, headersMap);
				HashSet<String> customersAssociatedToContract = new HashSet<>();
				StringBuilder customerIdFilter = new StringBuilder();
				for (BackendIdentifierDTO dto : dtoList) {
					backendIdCustomerId.put(dto.getBackendId(), dto.getCustomer_id());
					if (StringUtils.isBlank(customerIdFilter.toString()))
						customerIdFilter.append("(");
					if (StringUtils.isNotBlank(customerIdFilter.toString())
							&& !"(".equalsIgnoreCase(customerIdFilter.toString()))
						customerIdFilter.append(DBPUtilitiesConstants.OR);
					customerIdFilter.append("customerId").append(DBPUtilitiesConstants.EQUAL)
							.append(dto.getCustomer_id());
				}
				if (StringUtils.isNotBlank(customerIdFilter.toString())) {
					customerIdFilter.append(")").append(DBPUtilitiesConstants.AND).append("contractId")
							.append(DBPUtilitiesConstants.EQUAL).append(contractId);
					InfinityUserManagementBackendDelegate infinityUserBackendDelegate = DBPAPIAbstractFactoryImpl
							.getBackendDelegate(InfinityUserManagementBackendDelegate.class);
					List<ContractCustomersDTO> contractCoreCustomers = infinityUserBackendDelegate
							.getInfinityContractCustomers(null, customerIdFilter.toString(), headersMap);
					if (contractCoreCustomers != null && !contractCoreCustomers.isEmpty()) {
						for (ContractCustomersDTO dto : contractCoreCustomers) {
							customersAssociatedToContract.add(dto.getCustomerId());
						}
					}
				}
				for (JsonElement jsonelement : jsonarray) {
					if (backendIdCustomerId.containsKey(JSONUtil.getString(jsonelement.getAsJsonObject(), "id"))) {
						jsonelement.getAsJsonObject().addProperty("isProfileExists",
								DBPUtilitiesConstants.BOOLEAN_STRING_TRUE);
						jsonelement.getAsJsonObject().addProperty("userId",
								backendIdCustomerId.get(JSONUtil.getString(jsonelement.getAsJsonObject(), "id")));
					} else
						jsonelement.getAsJsonObject().addProperty("isProfileExists",
								DBPUtilitiesConstants.BOOLEAN_STRING_FALSE);
					if (backendIdCustomerId.containsKey(JSONUtil.getString(jsonelement.getAsJsonObject(), "id"))
							&& customersAssociatedToContract.contains(
									backendIdCustomerId.get(JSONUtil.getString(jsonelement.getAsJsonObject(), "id"))))
						jsonelement.getAsJsonObject().addProperty("isAssociated",
								DBPUtilitiesConstants.BOOLEAN_STRING_TRUE);
					else
						jsonelement.getAsJsonObject().addProperty("isAssociated",
								DBPUtilitiesConstants.BOOLEAN_STRING_FALSE);

				}
			}
			JsonObject json = new JsonObject();
			json.add(DBPDatasetConstants.DATASET_CUSTOMERS, jsonarray);
			result.setResponse(json);
		} catch (ApplicationException e) {
			alert.prepareError(
					"InfinityUserManagementBusinessDelegateImpl : Exception occured while fetching the customer fetails"
							+ e.getMessage()).log();
			throw new ApplicationException(e.getErrorCodeEnum());
		} catch (Exception e) {
			alert.prepareError(
					"InfinityUserManagementBusinessDelegateImpl : Exception occured while fetching the customer fetails"
							+ e.getMessage()).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_10770);
		}
		return result;
	}

	@Override
	public DBXResult createInfinityUser(JsonObject jsonObject, Map<String, Object> headerMap) {
		InfinityUserManagementBackendDelegate infinityUserManagementBackendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(InfinityUserManagementBackendDelegate.class);
		return infinityUserManagementBackendDelegate.createInfinityUser(jsonObject, headerMap);
	}

	@Override
	public DBXResult editInfinityUser(JsonObject jsonObject, Map<String, Object> headerMap) throws ApplicationException {
		InfinityUserManagementBackendDelegate infinityUserManagementBackendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(InfinityUserManagementBackendDelegate.class);
		return infinityUserManagementBackendDelegate.editInfinityUser(jsonObject, headerMap);
	}

	@Override
	public DBXResult getInfinityUser(JsonObject inputJson, Map<String, Object> headerMap) {
		InfinityUserManagementBackendDelegate infinityUserManagementBackendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(InfinityUserManagementBackendDelegate.class);
		return infinityUserManagementBackendDelegate.getInfinityUser(inputJson, headerMap);
	}
	@Override
	public DBXResult updateCustomerLanguage(CustomerDTO customer, Map<String, Object> headerMap) throws ApplicationException{
		InfinityUserManagementBackendDelegate infinityUserManagementBackendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(InfinityUserManagementBackendDelegate.class);
		return infinityUserManagementBackendDelegate.updateCustomerLanguage(customer, headerMap);
	}
	
	 @Override
	 public DBXResult getInfinityUserServiceDefsRoles(JsonObject inputJson, Map<String, Object> headerMap) {
	        InfinityUserManagementBackendDelegate infinityUserManagementBackendDelegate = DBPAPIAbstractFactoryImpl
	                .getBackendDelegate(InfinityUserManagementBackendDelegate.class);
	        return infinityUserManagementBackendDelegate.getInfinityUserServiceDefsRoles(inputJson, headerMap);
	    }

	
	public DBXResult getCoreCustomerFeatureActionLimits(String roleId, String coreCustomerId, String legalEntityId, String userId,
			Map<String, Object> headersMap) throws ApplicationException {
		FeatureActionLimitsDTO responseDTO = new FeatureActionLimitsDTO();
		DBXResult response = new DBXResult();
		String serviceDefinitionId = "";
		String contractId = "";
		try {
			ContractCoreCustomerBusinessDelegate contractCoreCustomerBusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(ContractCoreCustomerBusinessDelegate.class);
			ContractBusinessDelegate contractBusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(ContractBusinessDelegate.class);
			ContractCoreCustomersDTO dto = new ContractCoreCustomersDTO();
			dto.setCoreCustomerId(coreCustomerId);
			dto.setCompanyLegalUnit(legalEntityId);
			dto = contractCoreCustomerBusinessDelegate.getContractCoreCustomers(dto, headersMap);
			ContractDTO contractDTO = contractBusinessDelegate.getContractDetails(dto.getContractId(), dto.getCompanyLegalUnit(), headersMap);
			serviceDefinitionId = contractDTO.getServicedefinitionId();
			ContractBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl
					.getBackendDelegate(ContractBackendDelegate.class);
			responseDTO = backendDelegate.getRestrictiveFeatureActionLimits(serviceDefinitionId, contractId, roleId,
					coreCustomerId, userId, headersMap, false, "", legalEntityId);
			response.setResponse(formatCoreCustomerFeatureActionLimits(responseDTO));
		} catch (ApplicationException e) {
			throw new ApplicationException(e.getErrorCodeEnum());
		} catch (Exception e) {
			alert.prepareError("Exception occured while fetching the core customer action limits " + e.getMessage()).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_10767);
		}
		return response;
	}

	private JsonObject formatCoreCustomerFeatureActionLimits(FeatureActionLimitsDTO dto) {
		Map<String, Set<String>> featureaction = dto.getFeatureaction();
		Map<String, JsonObject> featureInfo = dto.getFeatureInfo();
		Map<String, JsonObject> actionsInfo = dto.getActionsInfo();
		Map<String, Map<String, Map<String, String>>> monetaryActionLimits = dto.getMonetaryActionLimits();
		JsonObject response = new JsonObject();
		JsonArray featuresActionsJsonArray = new JsonArray();
		for (Entry<String, Set<String>> featureActionEntry : featureaction.entrySet()) {
			JsonObject featureJsonObject = featureInfo.get(featureActionEntry.getKey());
			JsonArray actionsJsonArray = new JsonArray();
			for (String actionId : featureActionEntry.getValue()) {
				JsonObject actionJsonObject = actionsInfo.get(actionId);
				String typeId = JSONUtil.getString(actionJsonObject, "typeId");
				if (HelperMethods.FEATUREACTION_TYPE.MONETARY.toString().equalsIgnoreCase(typeId)) {
					JsonArray limits = new JsonArray();
					for (Entry<String, String> limitsEntry : (monetaryActionLimits.get(featureActionEntry.getKey()))
							.get(actionId).entrySet()) {
						JsonObject limit = new JsonObject();
						limit.addProperty("id", limitsEntry.getKey());
						limit.addProperty("value", limitsEntry.getValue());
						limits.add(limit);
					}
					actionJsonObject.add("limits", limits);
				}
				actionsJsonArray.add(actionJsonObject);
			}
			featureJsonObject.add("actions", actionsJsonArray);
			featuresActionsJsonArray.add(featureJsonObject);
		}
		response.add("features", featuresActionsJsonArray);
		return response;
	}

	@Override
	public DBXResult getCoreCustomerInformation(ContractCustomersDTO contractCustomerDTO,
			Map<String, Object> headersMap) throws ApplicationException {
		DBXResult response = new DBXResult();
		List<ContractCustomersDTO> dtoList = new ArrayList<ContractCustomersDTO>();
		StringBuilder filter = new StringBuilder();
		try {
			InfinityUserManagementBackendDelegate infinityUserManagementBackendDelegate = DBPAPIAbstractFactoryImpl
					.getBackendDelegate(InfinityUserManagementBackendDelegate.class);
			if (StringUtils.isNotBlank(contractCustomerDTO.getCustomerId())) {
				dtoList = infinityUserManagementBackendDelegate.getInfinityContractCustomers(contractCustomerDTO, null,
						headersMap);
				if (dtoList == null || dtoList.isEmpty())
					throw new ApplicationException(ErrorCodeEnum.ERR_10773);
				for (ContractCustomersDTO dto : dtoList) {
					if (StringUtils.isNotBlank(filter))
						filter.append(DBPUtilitiesConstants.OR);
					filter.append("coreCustomerId").append(DBPUtilitiesConstants.EQUAL).append(dto.getCoreCustomerId());
				}
			}
			dtoList = infinityUserManagementBackendDelegate.getInfinityContractCustomers(contractCustomerDTO,
					filter.toString(), headersMap);
			Set<String> customerIdList = new HashSet<>();
			for (ContractCustomersDTO childDTO : dtoList) {
				customerIdList.add(childDTO.getCustomerId());
			}
			JsonObject jsonresponse = new JsonObject();
			JsonArray jsonArrayCustomers = new JsonArray();
			for (String customerId : customerIdList) {
				JsonObject customerIdJsonObject = new JsonObject();
				customerIdJsonObject.addProperty("customerId", customerId);
				jsonArrayCustomers.add(customerIdJsonObject);
			}
			jsonresponse.add(DBPDatasetConstants.DATASET_CUSTOMERS, jsonArrayCustomers);
			response.setResponse(jsonresponse);
		} catch (ApplicationException e) {
			alert.prepareError("InfinityUserManagementBusinessDelegateImpl : Exception occured while fetching customers"
					+ e.getMessage()).log();
			throw new ApplicationException(e.getErrorCodeEnum());
		} catch (Exception e) {
			alert.prepareError("InfinityUserManagementBusinessDelegateImpl : Exception occured while fetching customers"
					+ e.getMessage()).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_10773);
		}
		return response;
	}

	@Override
	public DBXResult getInfinityUserContractCustomerDetails(UserCustomerViewDTO dto, Map<String, Object> headersMap)
			throws ApplicationException {
		DBXResult dbxresult = new DBXResult();
		List<UserCustomerViewDTO> dtolist = new ArrayList<>();
		try {
			InfinityUserManagementBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl
					.getBackendDelegate(InfinityUserManagementBackendDelegate.class);
			ContractBackendDelegate contractBackendDelegate = DBPAPIAbstractFactoryImpl
					.getBackendDelegate(ContractBackendDelegate.class);
			ContractCustomersDTO contractCustomerDTO = new ContractCustomersDTO();
			contractCustomerDTO.setCustomerId(dto.getCustomerId());
			dtolist = backendDelegate.getAssociatedCustomers(contractCustomerDTO, headersMap);
			Map<String, Set<String>> contractCustomers = new HashMap<>();
			Map<String, JsonObject> contractInfo = new HashMap<>();
			Map<String, JsonObject> contractCustomerInfo = new HashMap<>();
			if (dtolist != null && !dtolist.isEmpty()) {
				for (UserCustomerViewDTO childDTO : dtolist) {
					if (!contractInfo.containsKey(childDTO.getContractId())) {
						JsonObject json = new JsonObject();
						json.addProperty("contractId", childDTO.getContractId());
						json.addProperty("contractName", childDTO.getContractName());
						contractInfo.put(childDTO.getContractId(), json);
					}
					if (!contractCustomerInfo.containsKey(childDTO.getCoreCustomerId())) {
						JsonObject json = new JsonObject();
						json.addProperty("coreCustomerId", childDTO.getCoreCustomerId());
						json.addProperty("coreCustomerName", childDTO.getCoreCustomerName());
						json.addProperty("userRole", childDTO.getUserRole());
						json.addProperty("isPrimary", childDTO.getIsPrimary());
						json.addProperty("isBusiness", childDTO.getIsBusiness());
						Set<String> actions = new HashSet<>();
						actions = contractBackendDelegate.fetchUserCoreCustomerActions(childDTO.getCustomerId(),
								childDTO.getCoreCustomerId(), headersMap);
						alert.prepareError(actions.toString()).log();
						json.addProperty("actions", actions.toString());
						contractCustomerInfo.put(childDTO.getCoreCustomerId(), json);
					}
					Set<String> customers = new HashSet<>();
					if (contractCustomers.containsKey(childDTO.getContractId())) {
						customers = contractCustomers.get(childDTO.getContractId());
					}
					customers.add(childDTO.getCoreCustomerId());
					contractCustomers.put(childDTO.getContractId(), customers);
				}
			}
			JsonArray contractCustomersJsonArray = new JsonArray();
			for (Entry<String, Set<String>> contractCustomerEntry : contractCustomers.entrySet()) {
				JsonObject json = new JsonObject();
				for (Map.Entry<String, JsonElement> entry : contractInfo.get(contractCustomerEntry.getKey())
						.entrySet()) {
					json.add(entry.getKey(), entry.getValue());
				}
				JsonArray coreCustomers = new JsonArray();
				for (String coreCustomer : contractCustomerEntry.getValue()) {
					JsonObject coreCustomerJson = new JsonObject();
					for (Map.Entry<String, JsonElement> entry : contractCustomerInfo.get(coreCustomer).entrySet()) {
						coreCustomerJson.add(entry.getKey(), entry.getValue());
					}
					coreCustomers.add(coreCustomerJson);
				}
				json.add("contractCustomers", coreCustomers);
				contractCustomersJsonArray.add(json);
			}
			JsonObject response = new JsonObject();
			response.add("contracts", contractCustomersJsonArray);
			dbxresult.setResponse(response);
		} catch (ApplicationException e) {
			alert.prepareError(
					"Exception occured while fetching user contract and contract customer details" + e.getMessage()).log();
			throw new ApplicationException(e.getErrorCodeEnum());
		} catch (Exception e) {
			alert.prepareError(
					"Exception occured while fetching user contract and contract customer details" + e.getMessage()).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_10776);
		}
		return dbxresult;
	}

	@Override
	public DBXResult createCustomRole(JsonObject jsonObject, Map<String, Object> headerMap) {
		InfinityUserManagementBackendDelegate infinityUserManagementBackendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(InfinityUserManagementBackendDelegate.class);
		return infinityUserManagementBackendDelegate.createCustomRole(jsonObject, headerMap);
	}

	@Override
	public DBXResult editCustomRole(JsonObject jsonObject, Map<String, Object> headerMap) {
		InfinityUserManagementBackendDelegate infinityUserManagementBackendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(InfinityUserManagementBackendDelegate.class);
		return infinityUserManagementBackendDelegate.editCustomRole(jsonObject, headerMap);
	}

	@Override
	public DBXResult verifyCustomRole(JsonObject jsonObject, Map<String, Object> headerMap) {
		InfinityUserManagementBackendDelegate infinityUserManagementBackendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(InfinityUserManagementBackendDelegate.class);
		return infinityUserManagementBackendDelegate.verifyCustomRole(jsonObject, headerMap);
	}

	@Override
	public DBXResult getCustomRole(JsonObject jsonObject, Map<String, Object> headerMap) {
		InfinityUserManagementBackendDelegate infinityUserManagementBackendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(InfinityUserManagementBackendDelegate.class);
		return infinityUserManagementBackendDelegate.getCustomRole(jsonObject, headerMap);
	}

	@Override
	public DBXResult getInfinityUserContractCoreCustomerActions(ContractCustomersDTO contractCustomerDTO,
			Map<String, Object> headersMap) throws ApplicationException {
		DBXResult result = new DBXResult();
		List<UserCustomerViewDTO> response = new ArrayList<>();
		Map<String, Set<String>> coreCustomerActions = new HashMap<>();
		Map<String, Set<String>> contractCoreCustomers = new HashMap<>();
		try {
			InfinityUserManagementBackendDelegate infinityUserManagementBackendDelegate = DBPAPIAbstractFactoryImpl
					.getBackendDelegate(InfinityUserManagementBackendDelegate.class);
			ContractBackendDelegate contractBackendDelegate = DBPAPIAbstractFactoryImpl
					.getBackendDelegate(ContractBackendDelegate.class);
			response = infinityUserManagementBackendDelegate.getAssociatedCustomers(contractCustomerDTO, headersMap);
			for (UserCustomerViewDTO dto : response) {
				Set<String> coreCustomers = new HashSet<>();
				if (contractCoreCustomers.containsKey(dto.getContractId())) {
					coreCustomers = contractCoreCustomers.get(dto.getContractId());
				}
				coreCustomers.add(dto.getCoreCustomerId());
				contractCoreCustomers.put(dto.getContractId(), coreCustomers);
				FeatureActionLimitsDTO featureActionLimitsDTO = contractBackendDelegate
						.getRestrictiveFeatureActionLimits(dto.getServiceDefinitionId(), dto.getContractId(),
								dto.getRoleId(), dto.getCoreCustomerId(), dto.getCustomerId(), headersMap, false, "",dto.getCompanyLegalUnit());
				Map<String, Set<String>> featureactions = featureActionLimitsDTO.getFeatureaction();
				Set<String> actions = new HashSet<>();
				for (Entry<String, Set<String>> featureactionsEntryset : featureactions.entrySet()) {
					if (featureactionsEntryset.getValue() == null)
						actions.addAll(new HashSet<>());
					else
						actions.addAll(featureactionsEntryset.getValue());
				}
				coreCustomerActions.put(dto.getCoreCustomerId(), actions);
			}
			JsonArray contracts = new JsonArray();
			for (Entry<String, Set<String>> contractCoreCustomersEntryset : contractCoreCustomers.entrySet()) {
				JsonObject contractJsonobject = new JsonObject();
				contractJsonobject.addProperty("contractId", contractCoreCustomersEntryset.getKey());
				JsonArray coreCustomerJsonArray = new JsonArray();
				for (String coreCustomerId : contractCoreCustomersEntryset.getValue()) {
					JsonObject coreCustomerJsonobject = new JsonObject();
					coreCustomerJsonobject.addProperty("coreCustomerId", coreCustomerId);
					coreCustomerJsonobject.addProperty("actions", coreCustomerActions.get(coreCustomerId).toString());
					coreCustomerJsonArray.add(coreCustomerJsonobject);
				}
				contractJsonobject.add("contractCustomers", coreCustomerJsonArray);
				contracts.add(contractJsonobject);
			}
			JsonObject json = new JsonObject();
			json.add("contracts", contracts);
			result.setResponse(json);
		} catch (ApplicationException e) {
			alert.prepareError(
					"InfinityUserManagementBusinessDelegateImpl : Exception occured while fetching the user actions "
							+ e.getMessage()).log();
			throw new ApplicationException(e.getErrorCodeEnum());
		} catch (Exception e) {
			alert.prepareError(
					"InfinityUserManagementBusinessDelegateImpl : Exception occured while fetching the user actions "
							+ e.getMessage()).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_10788);
		}
		return result;
	}

	@Override
	public DBXResult getCustomRoleByCompanyID(JsonObject jsonObject, Map<String, Object> headerMap) {
		InfinityUserManagementBackendDelegate infinityUserManagementBackendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(InfinityUserManagementBackendDelegate.class);
		return infinityUserManagementBackendDelegate.getCustomRoleByCompanyID(jsonObject, headerMap);
	}

	@Override
	public DBXResult getCompanyLevelCustomRoles(JsonObject jsonObject, Map<String, Object> headerMap) {
		InfinityUserManagementBackendDelegate infinityUserManagementBackendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(InfinityUserManagementBackendDelegate.class);
		return infinityUserManagementBackendDelegate.getCompanyLevelCustomRoles(jsonObject, headerMap);
	}

	@Override
	public DBXResult getAssociatedContractUsers(JsonObject jsonObject, Map<String, Object> headerMap) {
		InfinityUserManagementBackendDelegate infinityUserManagementBackendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(InfinityUserManagementBackendDelegate.class);
		return infinityUserManagementBackendDelegate.getAssociatedContractUsers(jsonObject, headerMap);
	}

	@Override
	public DBXResult getInfinityUserContractDetails(ContractCustomersDTO contractCustomerDTO,
			Map<String, Object> headersMap) throws ApplicationException {
		DBXResult response = new DBXResult();
		try {
			Map<String, Set<String>> contractCoreCustomers = new HashMap<>();
			Set<String> associatedCoreCustomers = new HashSet<>();
			Map<String, JsonObject> coreCustomerInfo = new HashMap<>();
			Map<String, JsonObject> contractInfo = new HashMap<>();
			InfinityUserManagementBackendDelegate infinityUserBackendDelegate = DBPAPIAbstractFactoryImpl
					.getBackendDelegate(InfinityUserManagementBackendDelegate.class);
			
			DBXResult dbxresult = infinityUserBackendDelegate
					.getInfinityUserContractDetailsGetOperation(contractCustomerDTO.getCustomerId(),contractCustomerDTO.getCompanyLegalUnit(),headersMap);
			CoreCustomerBackendDelegate coreCustomerBackendDelegate = DBPAPIAbstractFactoryImpl
					.getBackendDelegate(CoreCustomerBackendDelegate.class);
			
			if (dbxresult != null && dbxresult.getResponse() != null) {
				for (JsonElement jsonElement : (JsonArray) dbxresult.getResponse()) {
					JsonObject json = jsonElement.getAsJsonObject();
					if (!contractInfo.containsKey(JSONUtil.getString(json, "contractId"))) {
						JsonObject contractjson = new JsonObject();
						contractjson.addProperty("contractId", JSONUtil.getString(json, "contractId"));
						contractjson.addProperty("contractName", JSONUtil.getString(json, "contractName"));
						contractjson.addProperty("legalEntityId", JSONUtil.getString(json, "legalEntityId"));
						contractjson.addProperty("serviceDefinitionId",
								JSONUtil.getString(json, "servicedefinitionId"));
						contractjson.addProperty("serviceDefinitionName",
								JSONUtil.getString(json, "serviceDefinitionName"));
						contractjson.addProperty("serviceDefinitionType",
								JSONUtil.getString(json, "serviceDefinitionType"));
						contractInfo.put(JSONUtil.getString(json, "contractId"), contractjson);
					}
					if ("true".equalsIgnoreCase(JSONUtil.getString(json, "isAssociated"))) {
                        associatedCoreCustomers.add(JSONUtil.getString(json, "coreCustomerId"));
                        JsonObject coreCustomerInfoJson = new JsonObject();
                        coreCustomerInfoJson.addProperty("id", JSONUtil.getString(json, "coreCustomerId"));
                        coreCustomerInfoJson.addProperty("name", JSONUtil.getString(json, "coreCustomerName"));
                        coreCustomerInfoJson.addProperty("userRoleName", JSONUtil.getString(json, "userRoleName"));
                        coreCustomerInfoJson.addProperty("userRole", JSONUtil.getString(json, "userRole"));
                        coreCustomerInfoJson.addProperty("userRoleDescription",
                                JSONUtil.getString(json, "userRoleDescription"));
                        coreCustomerInfoJson.addProperty("isPrimary", JSONUtil.getString(json, "isPrimary"));
                        coreCustomerInfo.put(JSONUtil.getString(json, "coreCustomerId"), coreCustomerInfoJson);
                        
                    } else {
                        if (!coreCustomerInfo.containsKey(JSONUtil.getString(json, "coreCustomerId"))) {
                            JsonObject coreCustomerInfoJson = new JsonObject();
                            coreCustomerInfoJson.addProperty("id", JSONUtil.getString(json, "coreCustomerId"));
                            coreCustomerInfoJson.addProperty("name", JSONUtil.getString(json, "coreCustomerName"));
                            coreCustomerInfoJson.addProperty("isPrimary", JSONUtil.getString(json, "isPrimary"));
                            coreCustomerInfo.put(JSONUtil.getString(json, "coreCustomerId"), coreCustomerInfoJson);
                        }
					}
					Set<String> contractCoreCustomersSet = new HashSet<>();
					if (contractCoreCustomers.containsKey(JSONUtil.getString(json, "contractId"))) {
						contractCoreCustomersSet = contractCoreCustomers.get(JSONUtil.getString(json, "contractId"));
					}
					contractCoreCustomersSet.add(JSONUtil.getString(json, "coreCustomerId"));
					contractCoreCustomers.put(JSONUtil.getString(json, "contractId"), contractCoreCustomersSet);
				}
			}
			JsonArray contracts = new JsonArray();
			for (Entry<String, Set<String>> contractCoreCustomersEntrySet : contractCoreCustomers.entrySet()) {
				JsonObject contractsinformation = new JsonObject();
				for (Map.Entry<String, JsonElement> entry : contractInfo.get(contractCoreCustomersEntrySet.getKey())
						.entrySet()) {
					contractsinformation.addProperty(entry.getKey(), entry.getValue().getAsString());
				}
				JsonArray coreCustomersJsonArray = new JsonArray();
				for (String coreCustomer : contractCoreCustomersEntrySet.getValue()) {
					JsonObject coreCustomerJsonObject = new JsonObject();
					for (Map.Entry<String, JsonElement> entry : coreCustomerInfo.get(coreCustomer).entrySet()) {
						coreCustomerJsonObject.addProperty(entry.getKey(), entry.getValue().getAsString());
					}
					if (associatedCoreCustomers.contains(coreCustomer))
						coreCustomerJsonObject.addProperty("isAssociated", "true");
					else
						coreCustomerJsonObject.addProperty("isAssociated", "false");
					coreCustomersJsonArray.add(coreCustomerJsonObject);
				}
				contractsinformation.add("contractCustomers", coreCustomersJsonArray);
				contracts.add(contractsinformation);
			}
			JsonObject jsonobject = new JsonObject();
			jsonobject.add("contracts", contracts);
			response.setResponse(jsonobject);
		} catch (ApplicationException e) {
			alert.prepareError(
					"InfinityUserManagementBusinessDelegateImpl : Exception occured while fetching infinity user contract details "
							+ e.getMessage()).log();
			throw new ApplicationException(e.getErrorCodeEnum());
		} catch (Exception e) {
			alert.prepareError(
					"InfinityUserManagementBusinessDelegateImpl : Exception occured while fetching infinity user contract details "
							+ e.getMessage()).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_10790);
		}
		return response;
	}
	
	@Override
	public DBXResult getCoreCustomerAccountsForAdmin(CustomerAccountsDTO dto, Map<String, Object> headersMap)
            throws ApplicationException {
        DBXResult dbxresult = new DBXResult();
        try {
            InfinityUserManagementBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl
                    .getBackendDelegate(InfinityUserManagementBackendDelegate.class);
            ContractCustomersDTO contractCustomerDTO = new ContractCustomersDTO();
            contractCustomerDTO.setCustomerId(dto.getCustomerId());
            contractCustomerDTO.setCoreCustomerId(dto.getCoreCustomerId());
            contractCustomerDTO.setCompanyLegalUnit(dto.getCompanyLegalUnit());
            List<UserCustomerViewDTO> coreCustomersInfoList = backendDelegate
                    .getAssociatedCustomers(contractCustomerDTO, headersMap);
            Map<String, JsonObject> coreCustomerInfo = new HashMap<>();
            Set<String> implicitAccountAccessCustomers = new HashSet<>();
            CustomerAccountsBusinessDelegate customerAccountsBusinessDelegate = DBPAPIAbstractFactoryImpl
                    .getBusinessDelegate(CustomerAccountsBusinessDelegate.class);
            ContractAccountsBusinessDelegate contractAccountsBusinessDelegate = DBPAPIAbstractFactoryImpl
                    .getBusinessDelegate(ContractAccountsBusinessDelegate.class);
            CoreCustomerBusinessDelegate coreCustomerBusinessDelegate = DBPAPIAbstractFactoryImpl
                    .getBusinessDelegate(CoreCustomerBusinessDelegate.class);

            for (UserCustomerViewDTO childDTO : coreCustomersInfoList) {
                if (DBPUtilitiesConstants.BOOLEAN_STRING_TRUE.equalsIgnoreCase(childDTO.getAutoSyncAccounts()))
                    implicitAccountAccessCustomers.add(childDTO.getCoreCustomerId());
            }
            
            
           Map<String, List<ContractAccountsDTO>> corecustomersAccountsMap =
                    coreCustomerBusinessDelegate.getAccountsWithImplicitAccountAccess(implicitAccountAccessCustomers,
                            dto.getCustomerId(), dto.getCompanyLegalUnit(), headersMap);
            JsonArray accountsJsonArray = new JsonArray();
            JsonArray resultArray = new JsonArray();
            for (UserCustomerViewDTO childDTO : coreCustomersInfoList) {
                if (!coreCustomerInfo.containsKey(childDTO.getCoreCustomerId())) {
                    List<ContractAccountsDTO> contractAccounts = null;

                   if (DBPUtilitiesConstants.BOOLEAN_STRING_TRUE.equalsIgnoreCase(childDTO.getAutoSyncAccounts())) {
                        contractAccounts = corecustomersAccountsMap.get(childDTO.getCoreCustomerId());
                    } else {
                        contractAccounts = contractAccountsBusinessDelegate
                                .getContractCustomerAccounts(childDTO.getContractId(), childDTO.getCoreCustomerId(),
                                        headersMap);
                    }
                    if (contractAccounts != null && contractAccounts.size() > 0) {
                        String accountsString = JSONUtils.stringifyCollectionWithTypeInfo(contractAccounts,
                                ContractAccountsDTO.class);
                        accountsJsonArray = new JsonParser().parse(accountsString).getAsJsonArray();
                        Set<String> customerAccounts = new HashSet<>();
                        for (JsonElement jsonelement : accountsJsonArray) {
                            customerAccounts.add(JSONUtil.getString(jsonelement.getAsJsonObject(), "accountId"));
                        }
						customerAccounts = customerAccountsBusinessDelegate.getValidCustomerAccounts(customerAccounts,
								childDTO.getCustomerId(), childDTO.getCoreCustomerId(), headersMap);
						for (JsonElement jsonelement : accountsJsonArray) {
							if (customerAccounts
									.contains(jsonelement.getAsJsonObject().get("accountId").getAsString())) {
								jsonelement.getAsJsonObject().addProperty("isAssociated", "false");
								if (JSONUtil.hasKey(jsonelement.getAsJsonObject(), "isNew") && ("1"
										.equalsIgnoreCase(JSONUtil.getString(jsonelement.getAsJsonObject(), "isNew"))
										|| "true".equalsIgnoreCase(
												JSONUtil.getString(jsonelement.getAsJsonObject(), "isNew")))) {
									jsonelement.getAsJsonObject().addProperty("isAssociated", "true");
									jsonelement.getAsJsonObject().addProperty(InfinityConstants.isNew, "true");

								} else {
									// jsonelement.getAsJsonObject().addProperty(InfinityConstants.isNew, "false");
									// accountsJsonArray.remove(jsonelement);
									continue;
								}
							} else {
								jsonelement.getAsJsonObject().addProperty("isAssociated", "true");
								jsonelement.getAsJsonObject().addProperty(InfinityConstants.isNew, "false");
							}

							resultArray.add(jsonelement);
						}
					}
				}
			}
            JsonObject response = new JsonObject();
            response.add("coreCustomerAccounts", resultArray);
            dbxresult.setResponse(response);
        } catch (

        ApplicationException e) {
            alert.prepareError(
                    "InfinityUserManagementBusinessDelegateImpl : Exception occured while fetching infinity user core customer acounts "
                            + e.getMessage()).log();
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            alert.prepareError(
                    "InfinityUserManagementBusinessDelegateImpl : Exception occured while fetching infinity user core customer acounts "
                            + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10793);
        }
        return dbxresult;
    }

	@Override
	public DBXResult getInfinityUserAccountsForAdmin(Boolean isSuperAdmin, CustomerAccountsDTO dto, Map<String, Object> headersMap)
			throws ApplicationException {
		DBXResult dbxresult = new DBXResult();
		try {
			InfinityUserManagementBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl
					.getBackendDelegate(InfinityUserManagementBackendDelegate.class);
			ContractCustomersDTO contractCustomerDTO = new ContractCustomersDTO();
			contractCustomerDTO.setCustomerId(dto.getCustomerId());
			contractCustomerDTO.setCompanyLegalUnit(dto.getCompanyLegalUnit());
			if (StringUtils.isNotBlank(dto.getContractId())) {
				contractCustomerDTO.setContractId(dto.getContractId());
			}
			List<UserCustomerViewDTOwithAccounts> coreCustomersInfoList = backendDelegate
                    .getAssociatedCustomerswithaccounts(contractCustomerDTO, headersMap);
            Map<String, Set<String>> contractCoreCustomers = new HashMap<>();
            Map<String, JsonObject> contractInfo = new HashMap<>();
            Map<String, JsonObject> coreCustomerInfo = new HashMap<>();
            CustomerAccountsBusinessDelegate customerAccountsBusinessDelegate = DBPAPIAbstractFactoryImpl
                    .getBusinessDelegate(CustomerAccountsBusinessDelegate.class);
            /*Getting external user(dbxuser) accounts to check with contract accounts, based on this isAssociated flag will be generated 
             * in next steps */
            
			Map<String, Set<String>> externaluserAccounts = new HashMap<>();
			externaluserAccounts = customerAccountsBusinessDelegate.getValidCustomerAccounts(dto.getCustomerId(),
					headersMap);
			
			Map<String,List<ContractAccountsDTO>> contractcorecustAccounts = new HashMap<>();
			/*Looping thought each account record. While looping collecting customer info and contract info.
			 * Also Calculating contract accounts data per core customer*/
            for (UserCustomerViewDTOwithAccounts childDTO : coreCustomersInfoList) {
            	
            	/*Calculating contract accounts data per core customer*/
            	generateCorecustomerAccountsData(isSuperAdmin, childDTO,contractcorecustAccounts);
            	
            	if (!contractInfo.containsKey(childDTO.getContractId())) {
                    JsonObject contractjson = new JsonObject();
                    contractjson.addProperty("contractId", childDTO.getContractId());
                    contractjson.addProperty("contractName", childDTO.getContractName());
                    contractjson.addProperty("legalEntityId", childDTO.getCompanyLegalUnit());
					contractInfo.put(childDTO.getContractId(), contractjson);
				}
            	if (!coreCustomerInfo.containsKey(childDTO.getCoreCustomerId())) {
                    MembershipDTO membershipDTO = new MembershipDTO();
                    membershipDTO.setId(childDTO.getCoreCustomerId());
                    JsonObject coreCustomerInfoJson = new JsonObject();
                    coreCustomerInfoJson.addProperty("id", childDTO.getCoreCustomerId());
                    coreCustomerInfoJson.addProperty("name", childDTO.getCoreCustomerName());
                    coreCustomerInfoJson.addProperty("isPrimary", childDTO.getIsPrimary());
                    coreCustomerInfoJson.addProperty("autoSyncAccounts", childDTO.getAutoSyncAccounts());
                    coreCustomerInfo.put(childDTO.getCoreCustomerId(), coreCustomerInfoJson);
                }
                /*Calculating core customers per contract*/
                Set<String> coreCustomers = new HashSet<>();
                if (contractCoreCustomers.containsKey(childDTO.getContractId())) {
                    coreCustomers = contractCoreCustomers.get(childDTO.getContractId());
                }
                coreCustomers.add(childDTO.getCoreCustomerId());
                contractCoreCustomers.put(childDTO.getContractId(), coreCustomers);
            }
            JsonArray contracts = new JsonArray();
            for (Entry<String, Set<String>> contractCoreCustomersEntrySet : contractCoreCustomers.entrySet()) {
                JsonObject contractsinformation = new JsonObject();
                for (Map.Entry<String, JsonElement> entry : contractInfo.get(contractCoreCustomersEntrySet.getKey())
                        .entrySet()) {
                    contractsinformation.addProperty(entry.getKey(), entry.getValue().getAsString());
                }
                JsonArray coreCustomersJsonArray = new JsonArray();
                for (String coreCustomer : contractCoreCustomersEntrySet.getValue()) {
                	
                	/*Assign Accounts data for each core customer by checking pre calculated contract accounts data and external user accounts data */
                	List<ContractAccountsDTO> contractAccountsList = contractcorecustAccounts.get(coreCustomer);
                	if (contractAccountsList != null && !contractAccountsList.isEmpty()) {
	                	addAccountsDataToContractCoreCustomer(coreCustomer,coreCustomerInfo.get(coreCustomer),contractcorecustAccounts,externaluserAccounts);
	                    JsonObject coreCustomerJsonObject = new JsonObject();
	                    for (Map.Entry<String, JsonElement> entry : coreCustomerInfo.get(coreCustomer).entrySet()) {
	                        coreCustomerJsonObject.add(entry.getKey(), entry.getValue());
	                    }
	                    coreCustomersJsonArray.add(coreCustomerJsonObject);
                	}
                }
                if(coreCustomersJsonArray.size() > 0) {
                	contractsinformation.add("contractCustomers", coreCustomersJsonArray);
                	contracts.add(contractsinformation);
                }
            }
            JsonObject response = new JsonObject();
            response.add("contracts", contracts);
            dbxresult.setResponse(response);
        } catch (
		ApplicationException e) {
			alert.prepareError(
					"InfinityUserManagementBusinessDelegateImpl : Exception occured while fetching infinity user core customer acounts ",
					e).log();
			throw new ApplicationException(e.getErrorCodeEnum());
		} catch (Exception e) {
			alert.prepareError(
					"InfinityUserManagementBusinessDelegateImpl : Exception occured while fetching infinity user core customer acounts ",
					e).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_10793);
		}
        return dbxresult;
    }
	
	private void generateCorecustomerAccountsData(Boolean isSuperAdmin, UserCustomerViewDTOwithAccounts childDTO,
			Map<String, List<ContractAccountsDTO>> contractcorecustAccounts) {

		ContractAccountsDTO contractaccount = new ContractAccountsDTO();
		generateAccountData(isSuperAdmin, contractaccount, childDTO);
		if (contractcorecustAccounts.containsKey(childDTO.getCoreCustomerId())) {
			if(StringUtils.isNotBlank(contractaccount.getAccountId())) {
				contractcorecustAccounts.get(childDTO.getCoreCustomerId()).add(contractaccount);
			}
		} else {
			List<ContractAccountsDTO> contractcorecustaccounts = new ArrayList<>();
			if(StringUtils.isNotBlank(contractaccount.getAccountId())) {
				contractcorecustaccounts.add(contractaccount);
				contractcorecustAccounts.put(childDTO.getCoreCustomerId(), contractcorecustaccounts);
			}
		}

	}

	private void addAccountsDataToContractCoreCustomer(String coreCustomer, JsonObject corecustomerInfo,
			Map<String, List<ContractAccountsDTO>> contractcorecustAccounts,
			Map<String, Set<String>> externaluserAccounts) {
		try {
			List<ContractAccountsDTO> contractAccounts = contractcorecustAccounts.get(coreCustomer);
			if (contractAccounts != null && !contractAccounts.isEmpty()) {
				String accountsString = JSONUtils.stringifyCollectionWithTypeInfo(contractAccounts,
						ContractAccountsDTO.class);
				JsonArray accountsJsonArray = new JsonParser().parse(accountsString).getAsJsonArray();
				Set<String> customerAccounts = new HashSet<>();
				customerAccounts = externaluserAccounts.get(coreCustomer);
				for (JsonElement jsonelement : accountsJsonArray) {
					if(jsonelement.getAsJsonObject().get("accountId").isJsonNull()) {
						continue;
					}
					if (customerAccounts.contains(jsonelement.getAsJsonObject().get("accountId").getAsString()))
						jsonelement.getAsJsonObject().addProperty("isAssociated", "true");
					else
						jsonelement.getAsJsonObject().addProperty("isAssociated", "false");

					if (JSONUtil.hasKey(jsonelement.getAsJsonObject(), "isNew")
							&& "1".equalsIgnoreCase(JSONUtil.getString(jsonelement.getAsJsonObject(), "isNew"))) {
						jsonelement.getAsJsonObject().addProperty("isAssociated", "true");
						jsonelement.getAsJsonObject().addProperty(InfinityConstants.isNew, "true");
					} else {
						jsonelement.getAsJsonObject().addProperty(InfinityConstants.isNew, "false");
					}
				}
				corecustomerInfo.add("coreCustomerAccounts", accountsJsonArray);
			}
		} catch (Exception e) {
			alert.prepareError("Error in processing accounts data", e).log();
		}

	}

	private void generateAccountData(Boolean isSuperAdmin, ContractAccountsDTO contractaccount, UserCustomerViewDTOwithAccounts childDTO) {
		if (childDTO == null || contractaccount == null)
			return;
		if(!isSuperAdmin && childDTO.getStatusDesc().equals(InfinityConstants.ACCOUNTSTATUS_CLOSED)) 
			return;
		contractaccount.setAccountId(childDTO.getAccountId());
		contractaccount.setAccountName(childDTO.getAccountName());
		contractaccount.setStatusDesc(childDTO.getStatusDesc());
		contractaccount.setTypeId(childDTO.getTypeId());
		contractaccount.setAccountType(HelperMethods.getAccountsNames().get(childDTO.getTypeId()));
	}

	@Override
	public DBXResult generateInfinityUserActivationCodeAndUsername(Map<String, String> bundleConfigurations,
			Map<String, String> inputParams, Map<String, Object> headersMap) throws ApplicationException {
		boolean status = false;
		DBXResult result = new DBXResult();
		JsonObject resultObject = new JsonObject();
		try {

			InfinityUserManagementBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl
					.getBackendDelegate(InfinityUserManagementBackendDelegate.class);
			UserManagementBackendDelegate usermanagementBackendDelegate = DBPAPIAbstractFactoryImpl
					.getBackendDelegate(UserManagementBackendDelegate.class);


			String userId = inputParams.get(InfinityConstants.userId);
			String userName = inputParams.get(InfinityConstants.userName);
			boolean isOnBoradingFlow = Boolean.parseBoolean(inputParams.get(InfinityConstants.isOnBoradingFlow));
			boolean isProspectFlow = Boolean.parseBoolean(inputParams.get(InfinityConstants.isProspectFlow));

			DBXResult usernameresponse = backendDelegate
					.generateInfinityUserName(bundleConfigurations.get(BundleConfigurationHandler.USERNAME_LENGTH));
			DBXResult activatiocoderesponse = backendDelegate
					.generateActivationCode(bundleConfigurations.get(BundleConfigurationHandler.ACTIVATIONCODE_LENGTH));
			String activationCode = inputParams.get(InfinityConstants.password);
			String applicationid = inputParams.get(InfinityConstants.applicationid);
			if (StringUtils.isBlank(userName) && usernameresponse != null && usernameresponse.getResponse() != null) {
				userName = (String) usernameresponse.getResponse();
			}
			if (StringUtils.isBlank(activationCode) && activatiocoderesponse != null
					&& activatiocoderesponse.getResponse() != null) {
				activationCode = (String) activatiocoderesponse.getResponse();
			}
			if (StringUtils.isBlank(userName) || StringUtils.isBlank(activationCode)) {
				throw new ApplicationException(ErrorCodeEnum.ERR_10795);
			}

			inputParams.put(InfinityConstants.userName, userName);

			String phone = inputParams.get(DTOConstants.PHONE);
			String email = inputParams.get(DTOConstants.EMAIL);
			JsonObject customerCommunication = new JsonObject();
			JsonArray communicationArray = new JsonArray();
			if (StringUtils.isBlank(phone) || StringUtils.isBlank(email)) {
				CommunicationBackendDelegate communicationBackendDelegate = DBPAPIAbstractFactoryImpl
						.getBackendDelegate(CommunicationBackendDelegate.class);
				CustomerCommunicationDTO customerCommunicationDTO = new CustomerCommunicationDTO();
				customerCommunicationDTO.setCustomer_id(userId);
				DBXResult communicationResponse = communicationBackendDelegate
						.getPrimaryMFACommunicationDetails(customerCommunicationDTO, headersMap);
				customerCommunication = ((JsonObject) communicationResponse.getResponse());

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
				}
				else if(customerCommunication.has(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION)  && customerCommunication.get(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION).isJsonObject()) {
					JsonObject commobject = customerCommunication.get(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION).getAsJsonObject();
					if (StringUtils.isBlank(email)) {
						email = JSONUtil.getString(commobject, "Email");
					}
					if (StringUtils.isBlank(phone)) {
						phone = JSONUtil.getString(commobject, "Phone");
					}
				}
			}
			CustomerDTO customerInfo = new CustomerDTO();
			customerInfo.setId(userId);
			DBXResult customerResponse = usermanagementBackendDelegate.get(customerInfo, headersMap);
			if (customerResponse != null && customerResponse.getResponse() != null)
				customerInfo = (CustomerDTO) customerResponse.getResponse();

			if (!isOnBoradingFlow && !isProspectFlow && customerInfo.getUserName().equalsIgnoreCase(userId))
				customerInfo.setUserName(userName);

			String emailTemplate = inputParams.get(InfinityConstants.EMAIL_TEMPLATE);
			String smsTemplate = inputParams.get(InfinityConstants.SMS_TEMPLATE);

			if (!isProspectFlow) {
				UserManagementBackendDelegate userManagementBackendDelegate = DBPAPIAbstractFactoryImpl
						.getBackendDelegate(UserManagementBackendDelegate.class);
				userManagementBackendDelegate.createEntryForCredentailCheckerTable(bundleConfigurations, customerInfo,
						activationCode, headersMap);
			}

			if (!isOnBoradingFlow) {
				usermanagementBackendDelegate.updateCustomerDetails(customerInfo, headersMap);
				emailTemplate = DBPUtilitiesConstants.ENROLLMENT_USERNAME_TEMPLATE;
				smsTemplate = DBPUtilitiesConstants.ENROLLMENT_ACTIVATIONCODE_TEMPLATE;
			}
			customerInfo = (CustomerDTO) usermanagementBackendDelegate.get(customerInfo, headersMap).getResponse();
			customerInfo.setApplicationID(applicationid);
			boolean isSCAEnabled = false;
			boolean isSCACommunicationEnabled = false;
			isSCAEnabled = Boolean.parseBoolean(EnvironmentConfigurationsHandler.getServerProperty("IS_SCA_ENABLED"));
			isSCACommunicationEnabled = Boolean.parseBoolean(EnvironmentConfigurationsHandler.getServerProperty("SCA_COMMUNICATION"));
			if(!isSCAEnabled || isSCACommunicationEnabled) {
				if (StringUtils.isNotBlank(email) && StringUtils.isNotBlank(emailTemplate)) {
					sendGeneratedUsernameToEmail(bundleConfigurations, customerInfo, email, headersMap, emailTemplate);
				}
				if (StringUtils.isNotBlank(phone) && StringUtils.isNotBlank(smsTemplate)) {
					sendGeneratedActivationcodeToMobile(phone, activationCode, headersMap, smsTemplate);
				}
			}
			status = true;

			resultObject.addProperty(InfinityConstants.activationCode, activationCode);
			resultObject.addProperty(InfinityConstants.userId, customerInfo.getUserName());

		} catch (ApplicationException e) {
			alert.prepareError(
					"InfinityUserManagementBusinessDelegateImpl : Exception occured while generating username and activation code "
							,e).log();
			throw new ApplicationException(e.getErrorCodeEnum());
		} catch (Exception e) {
			alert.prepareError(
					"InfinityUserManagementBusinessDelegateImpl : Exception occured while generating username and activation code "
							,e).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_10795);
		}
		resultObject.addProperty(InfinityConstants.status, status);
		result.setResponse(resultObject);
		return result;
	}

	private void sendGeneratedUsernameToEmail(Map<String, String> configurations, CustomerDTO customerInfo,
			String email, Map<String, Object> headersMap, String templateName) throws ApplicationException {
		try {
			KMSBusinessDelegate kmsBusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(KMSBusinessDelegate.class);
			Map<String, Object> input = new HashMap<>();
			input.put("FirstName", customerInfo.getFirstName());
			input.put("EmailType", templateName);
			input.put("LastName", customerInfo.getLastName());
			JSONObject addContext = new JSONObject();
			addContext.put("resetPasswordLink", EnvironmentConfigurationsHandler.getValue("DBP_OLB_BASE_URL"));
			addContext.put("userName", customerInfo.getUserName());
			if (customerInfo.getApplicationID() != null || StringUtils.isNotBlank(customerInfo.getApplicationID())) {
				addContext.put("applicationID", customerInfo.getApplicationID());
			}
			addContext.put("activationCodeExpiry",
					String.valueOf(
							(Integer.parseInt(configurations.get(BundleConfigurationHandler.ACTIVATIONCODE_EXPIRYTIME))
									/ 1440)));
			diagnostic.prepareDebug("Context" + addContext).log();
			input.put("AdditionalContext", KMSUtil.getOTPContent(null, null, addContext));
			input.put("Email", email);
			kmsBusinessDelegate.sendKMSEmail(input, headersMap);
		} catch (Exception e) {
			alert.prepareError("Exception occured while sending email " + e.getMessage()).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_10796);
		}
	}

	private void sendGeneratedActivationcodeToMobile(String phone, String activationCode,
			Map<String, Object> headersMap, String templateName) throws ApplicationException {
		try {
			KMSBusinessDelegate kmsBusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(KMSBusinessDelegate.class);
			Map<String, Object> inputParams = new HashMap<>();
			inputParams.put("Phone", phone);
			inputParams.put("otp", activationCode);
			inputParams.put("MessageType", templateName);
			kmsBusinessDelegate.sendKMSSMS(inputParams, headersMap);
		} catch (Exception e) {
			alert.prepareError("Exception occured while sending SMS" + e.getMessage()).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_10797);
		}

	}

    @Override
    public DBXResult processNewAccounts(String accounts, String customerId, Map<String, Object> headersMap)
            throws ApplicationException {
    	diagnostic.prepareDebug("****************************************** InfinityUserManagementBusinessDelegate processNewAccounts start ************************").log();
        DBXResult result = new DBXResult();
        try {
            JsonObject jsonobject = new JsonObject();
            HashSet<String> cifList = new HashSet<>();
            HashSet<String> coreCustomerAccounts = new HashSet<>();
            Map<String, Set<String>> accountIdCustomerIdFromPayload = new HashMap<>();
            Map<String, JsonObject> accountInfo = new HashMap<>();
            diagnostic.prepareDebug("****************************************** InfinityUserManagementBusinessDelegate processNewAccounts accounts: "+accounts).log();
            if (StringUtils.isNotBlank(accounts)) {
                JsonArray jsonarray = new JsonParser().parse(accounts).getAsJsonArray();
                for (JsonElement jsonelement : jsonarray) {
                	String accountId = JSONUtil.getString(jsonelement.getAsJsonObject(), "accountId");
                	String coreCustomerId = JSONUtil.getString(jsonelement.getAsJsonObject(), "customerId");
					if (accountIdCustomerIdFromPayload.containsKey(accountId)) {
						accountIdCustomerIdFromPayload.get(accountId).add(coreCustomerId);
					} else {
						Set<String> customers = new HashSet<>();
						customers.add(coreCustomerId);
						accountIdCustomerIdFromPayload.put(accountId, customers);
					}
					coreCustomerAccounts.add(JSONUtil.getString(jsonelement.getAsJsonObject(), "accountId"));
					JsonObject json = new JsonObject();
					for (Entry<String, JsonElement> entrySet : jsonelement.getAsJsonObject().entrySet()) {
						json.add(entrySet.getKey(), entrySet.getValue());
					}
					accountInfo.put(accountId + coreCustomerId, json);
					cifList.add(coreCustomerId);
				}
			}
            InfinityUserManagementBackendDelegate userManagementBackendDelegate = DBPAPIAbstractFactoryImpl
                    .getBackendDelegate(InfinityUserManagementBackendDelegate.class);
            result =
                    userManagementBackendDelegate.processNewAccounts(customerId, headersMap);
            
            diagnostic.prepareDebug("****************************************** InfinityUserManagementBusinessDelegate processNewAccounts result11111 : "+result.getResponse()).log();
            
            Map<String, Set<String>> information = (Map<String, Set<String>>) result.getResponse();
            Set<String> finalAccountList = new HashSet<>();
            alert.prepareError("****************************************** InfinityUserManagementBusinessDelegate processNewAccounts information :"+information.toString()).log();
            if (information.containsKey("nonCIFAccounts") && information.get("nonCIFAccounts") != null
                    && !information.get("nonCIFAccounts").isEmpty())
                finalAccountList.addAll(information.get("nonCIFAccounts"));
            Map<String, String> cifContractId = new HashMap<>();
            if (information.containsKey("implicitCoreCustomers") && information.get("implicitCoreCustomers") != null) {
                Set<String> implicitCoreCustomersInfo = information.get("implicitCoreCustomers");
                for (String s : implicitCoreCustomersInfo) {
                    String[] arr = s.split(":");
                    cifContractId.put(arr[0], arr[1]);
                }
                /*if (information.get("excludedcontractaccounts") != null)
                    coreCustomerAccounts.removeAll(information.get("excludedcontractaccounts"));*/
                diagnostic.prepareDebug("information.get(\"excludedcontractaccounts\")"+information.get("excludedcontractaccounts")).log();
                diagnostic.prepareDebug("information.get(\"excludedcontractaccounts\")"+information.get("excludedcontractaccounts")).log();
                removeExcludedContractAccounts(coreCustomerAccounts,information.get("excludedcontractaccounts"),accountIdCustomerIdFromPayload);
                diagnostic.prepareDebug("coreCustomerAccounts").log();
                if (information.get("excludedcustomeraccounts") != null)
                    coreCustomerAccounts.removeAll(information.get("excludedcustomeraccounts"));
                finalAccountList.addAll(coreCustomerAccounts);
                
                HashSet<String> cifacnts = (HashSet) coreCustomerAccounts.clone();
                if (information.get("customeraccounts") != null)
                    cifacnts.removeAll(information.get("customeraccounts"));
                cifacnts = removeJointAccounts(cifacnts, headersMap, accountIdCustomerIdFromPayload,finalAccountList);
                diagnostic.prepareDebug("final accountIdCustomerIdFromPayload"+accountIdCustomerIdFromPayload).log();
                StringBuilder queryInput = new StringBuilder();
                StringBuilder queryInputAccoutProcessing = new StringBuilder();
                Map<String,String> userLegalEntites = userManagementBackendDelegate.getUserLegalEntites(customerId, headersMap);
                int i = 0;
                for (String coreCustomerAcnt : cifacnts) {
					String coreCustomerId = "";
					if (accountIdCustomerIdFromPayload.containsKey(coreCustomerAcnt)
							&& accountIdCustomerIdFromPayload.get(coreCustomerAcnt) != null
							&& !accountIdCustomerIdFromPayload.get(coreCustomerAcnt).isEmpty()) {
						Set<String> coreCustomerIds = accountIdCustomerIdFromPayload.get(coreCustomerAcnt);
						for(String custId: coreCustomerIds)
						{
							coreCustomerId = custId;
							if(cifContractId.containsKey(coreCustomerId));
							{
								break;
							}
						}
					} else {
						continue;
					}
					if(StringUtils.isBlank(coreCustomerId))
						continue;
					if(!userLegalEntites.containsKey(coreCustomerId))
						continue;
					JsonObject json = accountInfo.get(coreCustomerAcnt+coreCustomerId);
					if(json == null)
						continue;
					if(StringUtils.isBlank(coreCustomerId) || !cifContractId.containsKey(coreCustomerId))
					{
						finalAccountList.remove(coreCustomerAcnt);
						continue;
					}
	                queryInput.append(coreCustomerId);
                    queryInput.append(":");
                    queryInput.append(coreCustomerAcnt);
                    queryInput.append(":");
                    queryInput.append(JSONUtil.getString(json, "arrangementId"));
                    queryInput.append(":");
                    queryInput.append(JSONUtil.getString(json, "accountName"));
                    queryInput.append(":");
                    queryInput.append(JSONUtil.getString(json, "accountType"));
                    queryInput.append(":");
                    queryInput.append(JSONUtil.getString(json, "roleDisplayName"));
                    queryInput.append(":");
                    queryInput.append(userLegalEntites.get(coreCustomerId));
                    if (i < cifacnts.size() - 1)
                        queryInput.append("|");
                    i++;
                }
                String isRequestForActions = (String) headersMap.get("isRequestForActions");
                alert.prepareError("****************************************** InfinityUserManagementBusinessDelegate processNewAccounts isRequestForActions :"+isRequestForActions).log();
                if (StringUtils.isNotBlank(queryInput.toString()) && 
                		(StringUtils.isBlank(isRequestForActions) || "false".equalsIgnoreCase(isRequestForActions)) ) {
                    Callable<Result> callable = new Callable<Result>() {
                        public Result call() {
                            try {
                                Thread.currentThread().setName("autoSynAccounts:GetAccountsPostLogin");
                                ContractCoreCustomerBusinessDelegate contractCoreCustomerBD =
                                        DBPAPIAbstractFactoryImpl
                                                .getBusinessDelegate(ContractCoreCustomerBusinessDelegate.class);
                                ApprovalMatrixBusinessDelegate approvalmatrixDelegate =
                                        DBPAPIAbstractFactoryImpl.getInstance()
                                                .getFactoryInstance(BusinessDelegateFactory.class)
                                                .getBusinessDelegate(ApprovalMatrixBusinessDelegate.class);
                                CustomerActionsBusinessDelegate businessDelegate =
                                        DBPAPIAbstractFactoryImpl
                                                .getBusinessDelegate(CustomerActionsBusinessDelegate.class);
                                businessDelegate.createAccountDefaultCustomerActions(queryInput.toString(), customerId,
                                        headersMap);
                                for (String coreCustomerId : cifList) {
                                    Map<String, Set<String>> contractCoreCustomerDetailsMap =
                                            contractCoreCustomerBD.getCoreCustomerAccountsFeaturesActions(
                                                    cifContractId.get(coreCustomerId),
                                                    coreCustomerId,
                                                    headersMap);
                                    Set<String> customerAccounts = contractCoreCustomerDetailsMap.get("accounts");
                                    customerAccounts.retainAll(coreCustomerAccounts);
                                    Set<String> customerActions = contractCoreCustomerDetailsMap.get("actions");
                                    customerActions = getActionWithApproveFeatureAction(customerActions, headersMap);
                                    approvalmatrixDelegate.createDefaultApprovalMatrixEntry(
                                            cifContractId.get(coreCustomerId),
                                            String.join(DBPUtilitiesConstants.COMMA_SEPERATOR, customerAccounts),
                                            customerActions.toArray(new String[0]), coreCustomerId,null);
                                }
                            } catch (Exception e) {

                            }
                            return new Result();
                        }
                    };
                    try {
                        ThreadExecutor.getExecutor().execute(callable);
                    } catch (Exception e) {
                        alert.prepareError("Exception occured while autoSynAccounts:GetAccountsPostLogin thread execution",e).log();
                    }
                }
                jsonobject.addProperty("newAccounts", queryInput.toString());
            }
            StringBuilder accountList = new StringBuilder();
            for (String string : finalAccountList) {
                accountList.append(string);
                accountList.append(" ");
            }
            jsonobject.addProperty("accounts",
                    StringUtils.isNotBlank(accountList.toString()) ? accountList.toString().trim()
                            : accountList.toString());
            result.setResponse(jsonobject);
        } catch (ApplicationException e) {
            alert.prepareError("Exception occured while calling usermanagement backend delegate" ,e).log();
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            alert.prepareError("Exception occured while calling usermanagement backend delegate" ,e).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10754);
        }
        diagnostic.prepareDebug("****************************************** InfinityUserManagementBusinessDelegate processNewAccounts result : "+result.getResponse()).log();
        diagnostic.prepareDebug("****************************************** InfinityUserManagementBusinessDelegate processNewAccounts end ************************").log();
        return result;
    }
    
    /*
	 * Using excludedcontractaccounts data are removing invalid accounts from new
	 * account processing
	 */
	private void removeExcludedContractAccounts(HashSet<String> coreCustomerAccounts,
			Set<String> excludedContractAccounts, Map<String, Set<String>> accountIdCustomerIdFromPayload) {
		HashSet<String> newCoreCustomerAccounts = new HashSet<>();
		if (excludedContractAccounts == null || accountIdCustomerIdFromPayload == null || coreCustomerAccounts == null )
			return ;
		Map<String, Set<String>> excludedContractAccountsFromDB = new HashMap<>();
		for (String excludedAccount : excludedContractAccounts) {
			String[] excludedContractAccountsArr = excludedAccount.split(":");
			if(excludedContractAccountsArr.length <2)
				continue;
			String accountId = excludedContractAccountsArr[1];
			String customerId = excludedContractAccountsArr[0];
			diagnostic.prepareDebug("customerId"+customerId).log();
			diagnostic.prepareDebug("accountId"+accountId).log();
			
			if (excludedContractAccountsFromDB.containsKey(accountId)) {
				excludedContractAccountsFromDB.get(accountId).add(customerId);
			} else {
				Set<String> customerset = new HashSet<String>();
				customerset.add(customerId);
				excludedContractAccountsFromDB.put(accountId, customerset);
			}
		}
		diagnostic.prepareDebug("excludedContractAccountsFromDB"+excludedContractAccountsFromDB).log();
		newCoreCustomerAccounts.addAll(coreCustomerAccounts);
		coreCustomerAccounts.clear();
		for (String accountId : newCoreCustomerAccounts) {
			Set<String> excludedCustomerFromDB = excludedContractAccountsFromDB.get(accountId);
			if (excludedCustomerFromDB != null) {
				Set<String> customerForAccountFrompayload = accountIdCustomerIdFromPayload.get(accountId);
				Set<String> customersNeedsToBeRemoved = new HashSet<>();
				for (String customerFromPayload : customerForAccountFrompayload) {
					if (excludedCustomerFromDB.contains(customerFromPayload)) {
						customersNeedsToBeRemoved.add(customerFromPayload);
					}
				}
				customerForAccountFrompayload.removeAll(customersNeedsToBeRemoved);
			}
			diagnostic.prepareDebug("accountIdCustomerIdFromPayload"+accountIdCustomerIdFromPayload).log();
			if (accountIdCustomerIdFromPayload.get(accountId) != null && !accountIdCustomerIdFromPayload.get(accountId).isEmpty()) {
				coreCustomerAccounts.add(accountId);
			}
		}
			diagnostic.prepareDebug("accountIdCustomerIdFromPayload"+accountIdCustomerIdFromPayload).log();
			diagnostic.prepareDebug("newCoreCustomerAccounts"+newCoreCustomerAccounts).log();
			diagnostic.prepareDebug("coreCustomerAccounts"+coreCustomerAccounts).log();
	}
    /*Using contractaccounts data and input payload and updating accounts data for new account processing
     * If any account does not have entry in contractaccounts already, we need to add that account to new account processing
     *  If any account have entry in contractaccounts already
     *  	Case1) corecustomerid same as input passed customerid, this case we need to treat it as new account 
     *  	Case2) corecustomerid not same as input passed customerid, this case we need not to treat as new account since that  account already added to another contract
     * */
    
    private HashSet<String> removeJointAccounts(HashSet<String> cifacnts, Map<String, Object> headersMap,
			Map<String, Set<String>> accountIdCustomerIdFromPayload, Set<String> finalAccountList) {
		if (cifacnts.isEmpty())
			return cifacnts;
		HashSet<String> newcifacnts = new HashSet<String>();
		Map<String, Object> inputParams = new HashMap<>();
		String accountCSV = String.join(",", cifacnts);
		Map<String, String> contractAccountsDB = new HashMap<>();
		inputParams.put("_accountsCSV", accountCSV);
		JsonObject response = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
				URLConstants.CONTRACTACCOUNTS_EXISTS_PROC);
		if (JSONUtil.hasKey(response, DBPDatasetConstants.DATASET_RECORDS)
				&& response.get(DBPDatasetConstants.DATASET_RECORDS).isJsonArray()) {
			JsonArray accountsArray = response.get(DBPDatasetConstants.DATASET_RECORDS).getAsJsonArray();
			if (accountsArray.size() == 0)
				return cifacnts;
			for (JsonElement contractaccountRecord : accountsArray) {
				contractAccountsDB.put(contractaccountRecord.getAsJsonObject().get("accountId").getAsString(),
						contractaccountRecord.getAsJsonObject().get("coreCustomerId").getAsString());
			}
		}
		for (String account : cifacnts) {
			diagnostic.prepareDebug("contractAccountsDB"+contractAccountsDB).log();
			diagnostic.prepareDebug("accountIdCustomerIdFromPayload"+accountIdCustomerIdFromPayload).log();
			if (contractAccountsDB.containsKey(account)) {
				if (accountIdCustomerIdFromPayload.get(account) != null && contractAccountsDB.get(account) != null
						&& accountIdCustomerIdFromPayload.get(account).contains(contractAccountsDB.get(account))) {
					Set<String> correctCustomer = new HashSet<>();
					correctCustomer.add(contractAccountsDB.get(account));
					accountIdCustomerIdFromPayload.put(account, correctCustomer);
					newcifacnts.add(account);
				}
				else
				{
					finalAccountList.remove(account);
				}
			} else {
				newcifacnts.add(account);
			}
		}
		return newcifacnts;
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

		String actions = contractFeaturesBD.getActionsWithApproveFeatureAction(actionsString.toString(), "", headersMap);

		return HelperMethods.splitString(actions, DBPUtilitiesConstants.COMMA_SEPERATOR);

	}

	@Override
	public DBXResult getInfinityUserPrimaryRetailContract(JsonObject inputJson, Map<String, Object> headerMap) {

		InfinityUserManagementBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(InfinityUserManagementBackendDelegate.class);
		return backendDelegate.getInfinityUserPrimaryRetailContract(inputJson, headerMap);
	}

	@Override
	public DBXResult getUserApprovalPermissions(String userId, String userName, String legalEntityId, String coreCustomerId,  Map<String, Object> headersMap)
			throws ApplicationException {

		DBXResult response = new DBXResult();

		CustomerAccountsDTO customerAccountInputDTO = new CustomerAccountsDTO();
		List<CustomerAccountsDTO> customerAccounts = new ArrayList<>();
		Set<String> coreCustomerIdList = new HashSet<>();
		CustomerActionDTO customerActionInputDTO = new CustomerActionDTO();
		List<CustomerActionDTO> customerActions = new ArrayList<>();
		Map<String, FeatureActionLimitsDTO> coreCustomerPermissions = new HashMap<>();
		Map<String, Map<String, Set<String>>> accountActions = new HashMap<>();
		Map<String, Map<String, JsonObject>> featureInfo = new HashMap<>();
		Map<String, Map<String, JsonObject>> actionsInfo = new HashMap<>();

		/**
		 * Fetching the userId based on userName
		 */
		if (StringUtils.isBlank(userId)) {
			UserManagementBackendDelegate userManagementBackendDelegate = DBPAPIAbstractFactoryImpl
					.getBackendDelegate(UserManagementBackendDelegate.class);
			CustomerDTO customerDTO = new CustomerDTO();
			customerDTO.setUserName(userName);
			customerDTO.setCompanyLegalUnit(legalEntityId);
			List<CustomerDTO> responseDTOList = userManagementBackendDelegate.getCustomerDetails(customerDTO,
					headersMap);
			userId = responseDTOList.get(0).getId();
		}
		/**
		 * Fetching the customer accounts
		 */
		CustomerAccountsBusinessDelegate customerAccountsBusinessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(CustomerAccountsBusinessDelegate.class);
		customerAccountInputDTO.setCustomerId(userId);
		customerAccountInputDTO.setCoreCustomerId(coreCustomerId);
		customerAccountInputDTO.setCompanyLegalUnit(legalEntityId);
		customerAccounts = customerAccountsBusinessDelegate.getCustomerAccountsOnCustomerId(customerAccountInputDTO,
				headersMap);

		/**
		 * Fetching customer account actions
		 */

		customerActionInputDTO.setCustomerId(userId);
		customerActionInputDTO.setCompanyLegalUnit(legalEntityId);
		CustomerActionsBusinessDelegate customerActionBusinessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(CustomerActionsBusinessDelegate.class);
		customerActions = customerActionBusinessDelegate.getCustomerActions(customerActionInputDTO, headersMap);

		for (CustomerActionDTO dto : customerActions) {
			if (StringUtils.isNotBlank(dto.getAccountId())) {
				Map<String, Set<String>> tempAccountActions = new HashMap<>();
				if (accountActions.containsKey(dto.getCoreCustomerId())) {
					tempAccountActions = accountActions.get(dto.getCoreCustomerId());
				}
				Set<String> actions = new HashSet<>();
				if (tempAccountActions.containsKey(dto.getAccountId())) {
					actions = tempAccountActions.get(dto.getAccountId());
				}
				actions.add(dto.getActionId());
				tempAccountActions.put(dto.getAccountId(), actions);
				accountActions.put(dto.getCoreCustomerId(), tempAccountActions);
			}
		}

		/**
		 * Fetching parent permissions
		 */

		ContractCustomersDTO contractCustomerDTO = new ContractCustomersDTO();
		contractCustomerDTO.setCustomerId(userId);
		contractCustomerDTO.setCompanyLegalUnit(legalEntityId);
		InfinityUserManagementBackendDelegate infinityUsermanagementBackendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(InfinityUserManagementBackendDelegate.class);
		List<UserCustomerViewDTO> userDetails = infinityUsermanagementBackendDelegate
				.getAssociatedCustomers(contractCustomerDTO, headersMap);
		ContractBackendDelegate contractBackendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(ContractBackendDelegate.class);
		for (UserCustomerViewDTO dto : userDetails) {
			coreCustomerIdList.add(dto.getCoreCustomerId());
		}
		for (UserCustomerViewDTO dto : userDetails) {
			if (!coreCustomerPermissions.containsKey(dto.getCoreCustomerId())) {
				coreCustomerPermissions.put(dto.getCoreCustomerId(),
						contractBackendDelegate.getRestrictiveFeatureActionLimits(dto.getServiceDefinitionId(),
								dto.getContractId(), dto.getRoleId(), dto.getCoreCustomerId(), "", headersMap, false,
								"APPROVE,BULK_APPROVE",dto.getCompanyLegalUnit()));
			}
			
		}
		Map<String, Map<String, Set<String>>> finalaccountactions = new HashMap<>();
		for (Entry<String, FeatureActionLimitsDTO> entrySet : coreCustomerPermissions.entrySet()) {
			featureInfo.put(entrySet.getKey(), entrySet.getValue().getFeatureInfo());
			actionsInfo.put(entrySet.getKey(), entrySet.getValue().getActionsInfo());
			Map<String, Set<String>> ciffeatureactions = entrySet.getValue().getFeatureaction();
			Map<String, Set<String>> accountactions = accountActions.get(entrySet.getKey());
			for (Entry<String, Set<String>> accountactionsentryset : accountactions.entrySet()) {
				Map<String, Set<String>> accountfeatureactionstemp = new HashMap<>();
				for (Entry<String, Set<String>> ciffeatureactionsentryset : ciffeatureactions.entrySet()) {
					accountfeatureactionstemp.put(ciffeatureactionsentryset.getKey(),
							Sets.intersection(accountactionsentryset.getValue(), ciffeatureactionsentryset.getValue()));
				}
				finalaccountactions.put(accountactionsentryset.getKey(), accountfeatureactionstemp);
			}
		}

		JsonArray accounts = new JsonArray();
		for (CustomerAccountsDTO dto : customerAccounts) {
			JsonObject accountjsonobject = new JsonObject();
			accountjsonobject.addProperty("accountId", dto.getAccountId());
			accountjsonobject.addProperty("accountName", dto.getAccountName());
			accountjsonobject.addProperty("accountType", dto.getAccountType());

			Map<String, Set<String>> actions = finalaccountactions.get(dto.getAccountId());
			Map<String, JsonObject> ciffeatureinfo = featureInfo.get(dto.getCoreCustomerId());
			Map<String, JsonObject> cifactioninfo = actionsInfo.get(dto.getCoreCustomerId());
			JsonArray featurejsonarray = new JsonArray();
			for (Entry<String, Set<String>> actionsEntrySet : actions.entrySet()) {
				if (actionsEntrySet.getValue() != null && !actionsEntrySet.getValue().isEmpty()) {
					JsonObject featurejsonobject = new JsonObject();
					for (Entry<String, JsonElement> entrySet : ciffeatureinfo.get(actionsEntrySet.getKey())
							.entrySet()) {
						featurejsonobject.add(entrySet.getKey(), entrySet.getValue());
						
					}
					JsonArray actionsjsonarray = new JsonArray();
					for (String actionId : actionsEntrySet.getValue()) {
						JsonObject actionjsonobject = new JsonObject();
						for (Entry<String, JsonElement> entrySet : cifactioninfo.get(actionId).entrySet()) {
							actionjsonobject.add(entrySet.getKey(), entrySet.getValue());
						}
						actionsjsonarray.add(actionjsonobject);
					}
					featurejsonobject.add("actions", actionsjsonarray);
					featurejsonarray.add(featurejsonobject);
				}
			}
			accountjsonobject.add("features", featurejsonarray);
			accounts.add(accountjsonobject);
		}
		response.setResponse(accounts);
		return response;
	}

	@Override
	public DBXResult validateCustomerEnrollmentDetails(String lastName, String taxId, String dateOfBirth,
			Map<String, Object> headersMap,String companyLegalUnit) throws ApplicationException {
		DBXResult response = new DBXResult();
		ProfileManagementBackendDelegate profileManagementBackendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(ProfileManagementBackendDelegate.class);

		CustomerDTO customerDTO = new CustomerDTO();
		customerDTO.setLastName(lastName);
		customerDTO.setDateOfBirth(dateOfBirth);
		customerDTO.setTaxID(taxId);
		customerDTO.setCompanyLegalUnit(companyLegalUnit);
		DBXResult coreCustomer = new DBXResult();

		try {
			coreCustomer = profileManagementBackendDelegate.fetchRetailCustomerDetails(customerDTO, headersMap);

			if (coreCustomer == null)
				return response;

			JsonObject coreCustomerJson = (JsonObject) coreCustomer.getResponse();
			BackendIdentifierDTO backendIdentifierDTO = new BackendIdentifierDTO();
			backendIdentifierDTO.setBackendId(JSONUtil.getString(coreCustomerJson, "id"));
			BackendIdentifierBusinessDelegate backendIdentifierBusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(BackendIdentifierBusinessDelegate.class);
			DBXResult backendIdentifiers = backendIdentifierBusinessDelegate.get(backendIdentifierDTO, headersMap);

			if (backendIdentifiers != null && backendIdentifiers.getResponse() != null) {
				backendIdentifierDTO = (BackendIdentifierDTO) backendIdentifiers.getResponse();
				coreCustomerJson.addProperty("infinityUserId", backendIdentifierDTO.getCustomer_id());
			}

			if (StringUtils.isNotBlank(JSONUtil.getString(coreCustomerJson, "infinityUserId"))) {
				UserManagementBackendDelegate userManagementBackendDelegate = DBPAPIAbstractFactoryImpl
						.getBackendDelegate(UserManagementBackendDelegate.class);
				CustomerDTO inputDTO = new CustomerDTO();
				inputDTO.setId(JSONUtil.getString(coreCustomerJson, "infinityUserId"));
				inputDTO = userManagementBackendDelegate.getCustomerDetails(inputDTO, headersMap).get(0);

				if (inputDTO.getIsEnrolled()
						&& DBPUtilitiesConstants.CUSTOMER_STATUS_ACTIVE.equalsIgnoreCase(inputDTO.getStatus_id())) {
					coreCustomerJson.addProperty("isUserEnrolled", "true");
				} else {
					coreCustomerJson.addProperty("isUserEnrolled", "false");
				}
			}
			response.setResponse(coreCustomerJson);
		} catch (ApplicationException e) {
			throw new ApplicationException(e.getErrorCodeEnum());
		} catch (Exception e) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10806);
		}
		return response;
	}

}