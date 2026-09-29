package com.temenos.dbx.usermanagement.businessdelegate.impl;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.DBPDatasetConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.temenos.dbx.eum.product.contract.backenddelegate.api.ContractCoreCustomerBackendDelegate;
import com.temenos.dbx.eum.product.contract.backenddelegate.api.CoreCustomerBackendDelegate;
import com.temenos.dbx.eum.product.contract.resource.impl.CoreCustomerResourceImpl;
import com.temenos.dbx.eum.product.usermanagement.backenddelegate.api.InfinityUserManagementBackendDelegate;
import com.temenos.dbx.eum.product.usermanagement.backenddelegate.api.ProfileManagementBackendDelegate;
import com.temenos.dbx.eum.product.usermanagement.backenddelegate.api.UserManagementBackendDelegate;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.BackendIdentifierBusinessDelegate;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.InfinityUserManagementBusinessDelegate;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.impl.InfinityUserManagementBusinessDelegateImpl;
import com.temenos.dbx.product.dto.BackendIdentifierDTO;
import com.temenos.dbx.product.dto.ContractCoreCustomersDTO;
import com.temenos.dbx.product.dto.ContractCustomersDTO;
import com.temenos.dbx.product.dto.CustomerAccountsDTO;
import com.temenos.dbx.product.dto.CustomerDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.dto.MembershipDTO;
import com.temenos.dbx.product.dto.UserCustomerViewDTO;

public class PartyInfinityUserManagementBusinessDelegateImpl implements InfinityUserManagementBusinessDelegate{
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	 LoggerUtil logger = new LoggerUtil(PartyInfinityUserManagementBusinessDelegateImpl.class);
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
		alert.prepareError("inside").log();
		InfinityUserManagementBackendDelegate infinityUserManagementBackendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(InfinityUserManagementBackendDelegate.class);
		return infinityUserManagementBackendDelegate.createInfinityUser(jsonObject, headerMap);
	}

	@Override
	public DBXResult editInfinityUser(JsonObject jsonObject, Map<String, Object> headerMap) throws ApplicationException {
		InfinityUserManagementBusinessDelegateImpl infinityUserManagementBusinessDelegate = new InfinityUserManagementBusinessDelegateImpl();
		return infinityUserManagementBusinessDelegate.editInfinityUser(jsonObject,headerMap);
	}

	@Override
	public DBXResult getInfinityUser(JsonObject inputObject, Map<String, Object> headerMap) {
		InfinityUserManagementBusinessDelegateImpl infinityUserManagementBusinessDelegate = new InfinityUserManagementBusinessDelegateImpl();
		return infinityUserManagementBusinessDelegate.getInfinityUser(inputObject,headerMap);
	}

	@Override
	public DBXResult getCoreCustomerFeatureActionLimits(String roleId, String coreCustomerId, String legalEntityId, String userId,
			Map<String, Object> headerMap) throws ApplicationException {
		InfinityUserManagementBusinessDelegateImpl infinityUserManagementBusinessDelegate = new InfinityUserManagementBusinessDelegateImpl();
		return infinityUserManagementBusinessDelegate.getCoreCustomerFeatureActionLimits(roleId, coreCustomerId, legalEntityId, userId, headerMap);
	}

	@Override
	public DBXResult getCoreCustomerInformation(ContractCustomersDTO contractCustomerDTO,
			Map<String, Object> headersMap) throws ApplicationException {
		InfinityUserManagementBusinessDelegateImpl infinityUserManagementBusinessDelegate = new InfinityUserManagementBusinessDelegateImpl();
		return infinityUserManagementBusinessDelegate.getCoreCustomerInformation(contractCustomerDTO, headersMap);
	}

	@Override
	public DBXResult getInfinityUserContractCustomerDetails(UserCustomerViewDTO dto, Map<String, Object> headersMap)
			throws ApplicationException {
		InfinityUserManagementBusinessDelegateImpl infinityUserManagementBusinessDelegate = new InfinityUserManagementBusinessDelegateImpl();
		return infinityUserManagementBusinessDelegate.getInfinityUserContractCustomerDetails(dto, headersMap);
	}

	@Override
	public DBXResult createCustomRole(JsonObject jsonObject, Map<String, Object> headerMap) {
		InfinityUserManagementBusinessDelegateImpl infinityUserManagementBusinessDelegate = new InfinityUserManagementBusinessDelegateImpl();
		return infinityUserManagementBusinessDelegate.createCustomRole(jsonObject, headerMap);
	}

	@Override
	public DBXResult editCustomRole(JsonObject jsonObject, Map<String, Object> headerMap) {
		InfinityUserManagementBusinessDelegateImpl infinityUserManagementBusinessDelegate = new InfinityUserManagementBusinessDelegateImpl();
		return infinityUserManagementBusinessDelegate.editCustomRole(jsonObject, headerMap);
	}

	@Override
	public DBXResult verifyCustomRole(JsonObject jsonObject, Map<String, Object> headerMap) {
		InfinityUserManagementBusinessDelegateImpl infinityUserManagementBusinessDelegate = new InfinityUserManagementBusinessDelegateImpl();
		return infinityUserManagementBusinessDelegate.verifyCustomRole(jsonObject, headerMap);
	}

	@Override
	public DBXResult getCustomRole(JsonObject jsonObject, Map<String, Object> headerMap) {
		InfinityUserManagementBusinessDelegateImpl infinityUserManagementBusinessDelegate = new InfinityUserManagementBusinessDelegateImpl();
		return infinityUserManagementBusinessDelegate.getCustomRole(jsonObject, headerMap);
	}

	@Override
	public DBXResult getInfinityUserContractCoreCustomerActions(ContractCustomersDTO contractCustomerDTO,
			Map<String, Object> headerMap) throws ApplicationException {
		InfinityUserManagementBusinessDelegateImpl infinityUserManagementBusinessDelegate = new InfinityUserManagementBusinessDelegateImpl();
		return infinityUserManagementBusinessDelegate.getInfinityUserContractCoreCustomerActions(contractCustomerDTO, headerMap);
	}

	@Override
	public DBXResult getCustomRoleByCompanyID(JsonObject jsonObject, Map<String, Object> headerMap) {
		InfinityUserManagementBusinessDelegateImpl infinityUserManagementBusinessDelegate = new InfinityUserManagementBusinessDelegateImpl();
		return infinityUserManagementBusinessDelegate.getCustomRoleByCompanyID(jsonObject, headerMap);
	}

	@Override
	public DBXResult getCompanyLevelCustomRoles(JsonObject jsonObject, Map<String, Object> headerMap) {
		InfinityUserManagementBusinessDelegateImpl infinityUserManagementBusinessDelegate = new InfinityUserManagementBusinessDelegateImpl();
		return infinityUserManagementBusinessDelegate.getCompanyLevelCustomRoles(jsonObject, headerMap);
	}

	@Override
	public DBXResult getAssociatedContractUsers(JsonObject jsonObject, Map<String, Object> headerMap) {
		InfinityUserManagementBusinessDelegateImpl infinityUserManagementBusinessDelegate = new InfinityUserManagementBusinessDelegateImpl();
		return infinityUserManagementBusinessDelegate.getAssociatedContractUsers(jsonObject, headerMap);
	}

	@Override
	public DBXResult getInfinityUserContractDetails(ContractCustomersDTO contractCustomerDTO,
			Map<String, Object> headerMap) throws ApplicationException {
		InfinityUserManagementBusinessDelegateImpl infinityUserManagementBusinessDelegate = new InfinityUserManagementBusinessDelegateImpl();
		return infinityUserManagementBusinessDelegate.getInfinityUserContractDetails(contractCustomerDTO, headerMap);
	}

	@Override
	public DBXResult getInfinityUserAccountsForAdmin(Boolean isSuperAdmin, CustomerAccountsDTO dto, Map<String, Object> headersMap)
			throws ApplicationException {
		InfinityUserManagementBusinessDelegateImpl infinityUserManagementBusinessDelegate = new InfinityUserManagementBusinessDelegateImpl();
		return infinityUserManagementBusinessDelegate.getInfinityUserAccountsForAdmin(isSuperAdmin, dto, headersMap);
	}

	@Override
	public DBXResult generateInfinityUserActivationCodeAndUsername(Map<String, String> C360BundleConfigurations,
			Map<String, String> inputParams, Map<String, Object> headersMap) throws ApplicationException {
		InfinityUserManagementBusinessDelegateImpl infinityUserManagementBusinessDelegate = new InfinityUserManagementBusinessDelegateImpl();
		return infinityUserManagementBusinessDelegate.generateInfinityUserActivationCodeAndUsername(C360BundleConfigurations,inputParams,headersMap);
	}

	@Override
	public DBXResult processNewAccounts(String accounts, String customerId, Map<String, Object> headersMap)
			throws ApplicationException {
		InfinityUserManagementBusinessDelegateImpl infinityUserManagementBusinessDelegate = new InfinityUserManagementBusinessDelegateImpl();
		return infinityUserManagementBusinessDelegate.processNewAccounts(accounts,customerId,headersMap);
	}

	@Override
	public DBXResult getInfinityUserPrimaryRetailContract(JsonObject inputObject, Map<String, Object> headerMap) {
		InfinityUserManagementBusinessDelegateImpl infinityUserManagementBusinessDelegate = new InfinityUserManagementBusinessDelegateImpl();
		return infinityUserManagementBusinessDelegate.getInfinityUserPrimaryRetailContract(inputObject,headerMap);
	}



	@Override
	public DBXResult getCoreCustomerAccountsForAdmin(CustomerAccountsDTO dto, Map<String, Object> headersMap)
			throws ApplicationException {
		InfinityUserManagementBusinessDelegateImpl infinityUserManagementBusinessDelegate = new InfinityUserManagementBusinessDelegateImpl();
		return infinityUserManagementBusinessDelegate.getCoreCustomerAccountsForAdmin(dto,headersMap);
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



	@Override
	public DBXResult getInfinityUserServiceDefsRoles(JsonObject inputJson, Map<String, Object> headerMap) {
		InfinityUserManagementBusinessDelegateImpl infinityUserManagementBusinessDelegate = new InfinityUserManagementBusinessDelegateImpl();
		return infinityUserManagementBusinessDelegate.getInfinityUserServiceDefsRoles(inputJson, headerMap);
	}


	public DBXResult getUserApprovalPermissions(String userId, String userName, String legalEntityId, String coreCustomerId,
			Map<String, Object> headersMap) throws ApplicationException {
		InfinityUserManagementBusinessDelegateImpl infinityUserManagementBusinessDelegate = new InfinityUserManagementBusinessDelegateImpl();
		return infinityUserManagementBusinessDelegate.getUserApprovalPermissions(userId,userName,legalEntityId,coreCustomerId,headersMap);
	}

	@Override
	public DBXResult updateCustomerLanguage(CustomerDTO customer, Map<String, Object> headerMap) throws ApplicationException {
		InfinityUserManagementBusinessDelegateImpl infinityUserManagementBusinessDelegate = new InfinityUserManagementBusinessDelegateImpl();
		return infinityUserManagementBusinessDelegate.updateCustomerLanguage(customer, headerMap);
	}

	

}