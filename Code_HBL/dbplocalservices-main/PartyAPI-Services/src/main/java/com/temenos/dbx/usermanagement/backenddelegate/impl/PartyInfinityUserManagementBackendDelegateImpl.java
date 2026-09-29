package com.temenos.dbx.usermanagement.backenddelegate.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import org.apache.commons.lang3.StringUtils;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.infinity.dbx.dbp.jwt.auth.AuthConstants;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.DBPDatasetConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.temenos.dbx.eum.product.usermanagement.backenddelegate.api.InfinityUserManagementBackendDelegate;
import com.temenos.dbx.eum.product.usermanagement.backenddelegate.api.ProfileManagementBackendDelegate;
import com.temenos.dbx.eum.product.usermanagement.backenddelegate.impl.InfinityUserManagementBackendDelegateImpl;
import com.temenos.dbx.party.utils.PartyConstants;
import com.temenos.dbx.party.utils.PartyUtils;
import com.temenos.dbx.product.dto.AlternateIdentity;
import com.temenos.dbx.product.dto.BackendIdentifierDTO;
import com.temenos.dbx.product.dto.ContractCustomersDTO;
import com.temenos.dbx.product.dto.CustomerCommunicationDTO;
import com.temenos.dbx.product.dto.CustomerDTO;
import com.temenos.dbx.product.dto.CustomerLegalEntityDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.dto.PartyDTO;
import com.temenos.dbx.product.dto.UserCustomerViewDTO;
import com.temenos.dbx.product.dto.UserCustomerViewDTOwithAccounts;
import com.temenos.dbx.product.usermanagement.backenddelegate.api.CommunicationBackendDelegate;
import com.temenos.dbx.product.utils.DTOConstants;
import com.temenos.dbx.product.utils.DTOUtils;
import com.temenos.dbx.product.utils.InfinityConstants;
import com.temenos.dbx.usermanagement.backenddelegate.api.PartyUserManagementBackendDelegate;
import com.temenos.dbx.usermanagement.businessdelegate.api.PartyUserManagementBusinessDelegate;
import com.temenos.dbx.usermanagement.dto.PartySearchDTO;

public class PartyInfinityUserManagementBackendDelegateImpl implements InfinityUserManagementBackendDelegate {
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    LoggerUtil logger = new LoggerUtil(PartyInfinityUserManagementBackendDelegateImpl.class);

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
        alert.prepareError("inputss"+inputParams).log();
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
        InfinityUserManagementBackendDelegate backendDelegate = new InfinityUserManagementBackendDelegateImpl();
        DBXResult dbxResult = backendDelegate.createInfinityUser(jsonObject, headerMap);
        JsonObject userDetails = jsonObject.has(InfinityConstants.userDetails)
                && jsonObject.get(InfinityConstants.userDetails).isJsonObject()
                        ? jsonObject.get(InfinityConstants.userDetails).getAsJsonObject()
                        : null;
        if (userDetails != null) {
            createCustomer(dbxResult, userDetails, headerMap);
        }

        return dbxResult;
    }
    private void createBackendIdentifierEntry(String id, String coreCustomerId, String legalEntityId, Map<String, Object> headerMap) {
        if (StringUtils.isBlank(coreCustomerId)) {
            return;
        }
        BackendIdentifierDTO backendIdentifierDTO = new BackendIdentifierDTO();
        backendIdentifierDTO.setId(UUID.randomUUID().toString());
        backendIdentifierDTO.setCustomer_id(id);
        backendIdentifierDTO.setBackendId(coreCustomerId);
        backendIdentifierDTO.setBackendType(PartyConstants.PARTY);
        backendIdentifierDTO.setSequenceNumber("1");
        backendIdentifierDTO.setContractId(null);
        backendIdentifierDTO.setIdentifier_name(InfinityConstants.customerId);
        backendIdentifierDTO.setContractTypeId(null);
        backendIdentifierDTO
                .setCompanyId(legalEntityId);
        backendIdentifierDTO.setCompanyLegalUnit(legalEntityId);
        Map<String, Object> input = DTOUtils.getParameterMap(backendIdentifierDTO, true);
        backendIdentifierDTO.setIsNew(true);
        backendIdentifierDTO.persist(input, headerMap);
    }

    private PartySearchDTO buildSearchPartyDTO(Map<String, String> inputMap) {
        PartySearchDTO partySearchDTO = new PartySearchDTO();
        DTOUtils.loadInputIntoDTO(partySearchDTO, inputMap, false);
        return partySearchDTO;
    }

    @Override
    public void createCustomer(DBXResult dbxResult, JsonObject userDetails, Map<String, Object> headerMap) {
        JsonObject jsonObject =
                dbxResult.getResponse() != null ? (JsonObject) dbxResult.getResponse() : new JsonObject();
        String id = jsonObject.has(InfinityConstants.id) && !jsonObject.get(InfinityConstants.id).isJsonNull()
                ? jsonObject.get(InfinityConstants.id).getAsString()
                : null;
        diagnostic.prepareDebug("createInfinityUser : id "+ id).log();
        String coreCustomerId = userDetails.has(InfinityConstants.coreCustomerId)
                && !userDetails.get(InfinityConstants.coreCustomerId).isJsonNull()
                        ? userDetails.get(InfinityConstants.coreCustomerId).getAsString()
                        : null;
        String legalEntityId = userDetails.has(InfinityConstants.legalEntityId)
                && !userDetails.get(InfinityConstants.legalEntityId).isJsonNull()
                        ? userDetails.get(InfinityConstants.legalEntityId).getAsString()
                        : null;
        diagnostic.prepareDebug("createInfinityUser : coreCustomerId "+ coreCustomerId).log();
        diagnostic.prepareDebug("createInfinityUser : legalEntityId "+ legalEntityId).log();
        if (StringUtils.isBlank(id)) {
            return;
        }
        JsonArray partyJsonArray = new JsonArray();
        try {
            if (StringUtils.isNotBlank(coreCustomerId) && StringUtils.isNotBlank(legalEntityId)) {
                PartyUserManagementBusinessDelegate managementBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                        .getFactoryInstance(BusinessDelegateFactory.class)
                        .getBusinessDelegate(PartyUserManagementBusinessDelegate.class);
                DBXResult response = new DBXResult();
                 partyJsonArray = new JsonArray();
                Map<String, String> inputMap = new HashMap<String, String>();
                String companyId = "";
                if(StringUtils.isNotBlank(legalEntityId)) {
              	  companyId= legalEntityId;
                }
                inputMap.put("alternateIdentifierNumber", companyId + "-" + coreCustomerId);
                inputMap.put("alternateIdentifierType", PartyConstants.BackOfficeIdentifier);
                PartySearchDTO searchDTO = buildSearchPartyDTO(inputMap);
                diagnostic.prepareDebug("createInfinityUser : alternateIdentifierNumber "+ inputMap.get("alternateIdentifierNumber")).log();
                headerMap = PartyUtils.addJWTAuthHeader(null, headerMap, AuthConstants.PRE_LOGIN_FLOW);
                headerMap.put(DBPUtilitiesConstants.COMPANY_ID, legalEntityId);
                response = managementBusinessDelegate.searchParty(searchDTO, headerMap);
                if (response.getResponse() != null) {
                    JsonObject party = (JsonObject) response.getResponse();
                    partyJsonArray =
                            party.has(PartyConstants.parties) && party.get(PartyConstants.parties).isJsonArray()
                                    ? party.get(PartyConstants.parties).getAsJsonArray()
                                    : new JsonArray();
                }

                if (partyJsonArray.size() > 0) {
                    JsonObject party = partyJsonArray.get(0).isJsonObject() ? partyJsonArray.get(0).getAsJsonObject()
                            : new JsonObject();
                    if (party.has(PartyConstants.partyId) && !party.get(PartyConstants.partyId).isJsonNull()) {
                        createBackendIdentifierEntry(id, party.get(PartyConstants.partyId).getAsString(), legalEntityId, headerMap);
                    }
                    return;
                }
            }
        } catch (Exception e) {

        }
        CustomerDTO customerDTO = (CustomerDTO) new CustomerDTO().loadDTO(id);

        if (customerDTO == null)
            return;

        CustomerCommunicationDTO phoneCommunicationDTO = new CustomerCommunicationDTO();
        CustomerCommunicationDTO emailCommunicationDTO = new CustomerCommunicationDTO();
        phoneCommunicationDTO.setId(HelperMethods.getNewId());
        String phoneCountryCode = JSONUtil.getString(userDetails, InfinityConstants.phoneCountryCode);
        String phoneNumber = JSONUtil.getString(userDetails, InfinityConstants.phoneNumber);
        if (StringUtils.isNotBlank(phoneCountryCode))
            phoneCountryCode = phoneCountryCode.trim();
        if (!phoneCountryCode.contains("+"))
            phoneCountryCode = "+" + phoneCountryCode;
        phoneCommunicationDTO.setPhoneCountryCode(phoneCountryCode);
        phoneCommunicationDTO.setValue(phoneNumber);
        phoneCommunicationDTO.setIsPrimary(true);
        phoneCommunicationDTO.setType_id(HelperMethods.getCommunicationTypes().get(DTOConstants.PHONE));
        emailCommunicationDTO.setIsPrimary(true);
        emailCommunicationDTO.setValue(
                userDetails.has(InfinityConstants.email) && !userDetails.get(InfinityConstants.email).isJsonNull()
                        ? userDetails.get(InfinityConstants.email).getAsString()
                        : null);
        emailCommunicationDTO.setType_id(HelperMethods.getCommunicationTypes().get(DTOConstants.EMAIL));
        if (StringUtils.isNotBlank(coreCustomerId)) {
            Map<String, Object> input = new HashMap<String, Object>();
            HelperMethods.addMSJWTAuthHeader(headerMap, AuthConstants.PRE_LOGIN_FLOW);
            input.put("customerId", coreCustomerId);
            headerMap.put(DBPUtilitiesConstants.COMPANY_ID, legalEntityId);
            /*String serviceId = ServiceId.T24ISUSER_INTEGRATION_SERVICE;
            String operationName = OperationName.CORE_CUSTOMER_SEARCH;

            JsonObject membershipJson =
                    ServiceCallHelper.invokeServiceAndGetJson(serviceId, null, operationName,
                            input, headerMap);*/
            diagnostic.prepareDebug("createInfinityUser : t24 search "+ coreCustomerId).log();
            /*if (null != membershipJson && JSONUtil.hasKey(membershipJson, DBPDatasetConstants.DATASET_CUSTOMERS)
                    && membershipJson.get(DBPDatasetConstants.DATASET_CUSTOMERS).isJsonArray()) {
                JsonArray partyJsonArray =
                        membershipJson.get(DBPDatasetConstants.DATASET_CUSTOMERS).getAsJsonArray();
                if (partyJsonArray.size() > 0) {
                    membershipJson = partyJsonArray.get(0).getAsJsonObject();
                    customerDTO.setSsn(membershipJson.get(InfinityConstants.ssn).getAsString());
                }
            }*/
            JsonObject party = partyJsonArray.get(0).isJsonObject() ? partyJsonArray.get(0).getAsJsonObject()
                    : new JsonObject();
            JsonArray customerDetails = party.get(DTOConstants.PARTYIDENTIFIERS).getAsJsonArray();
            for (JsonElement taxids : customerDetails) {
             	 String type = JSONUtil.getString(taxids.getAsJsonObject(), "type");
                if(type.equalsIgnoreCase("SOCIAL.SECURITY.NO")) {
                  String taxId = JSONUtil.getString(taxids.getAsJsonObject(), "identifierNumber");
                  customerDTO.setSsn(taxId);
                }
            }
        }

        customerDTO.setCustomerCommuncation(phoneCommunicationDTO);
        customerDTO.setCustomerCommuncation(emailCommunicationDTO);

        PartyUserManagementBackendDelegate backendDelegate =
                DBPAPIAbstractFactoryImpl.getBackendDelegate(PartyUserManagementBackendDelegate.class);

        PartyDTO partyDTO = new PartyDTO();
        PartyUtils.buildPartyDTO(customerDTO, partyDTO);
        
        if (StringUtils.isNotBlank(coreCustomerId)) {
        	AlternateIdentity alternateIdentity = new AlternateIdentity();
            alternateIdentity.setIdentityType(PartyConstants.BackOfficeIdentifier);
            alternateIdentity.setIdentitySource(PartyConstants.TransactT24);
            alternateIdentity.setIdentityNumber(
            		legalEntityId + "-" + coreCustomerId);
            partyDTO.setAlternateIdentities(alternateIdentity);
        }

        headerMap.put(DBPUtilitiesConstants.COMPANY_ID, legalEntityId);
        DBXResult partyResult = backendDelegate.create(partyDTO, headerMap);
        if (partyResult.getResponse() != null && StringUtils.isNotBlank((String) partyResult.getResponse())) {
            createBackendIdentifierEntry(id, (String) partyResult.getResponse(), legalEntityId, headerMap);
        }

        return;
    }

    private boolean getIsExistingLegalEntity(String legalEntityId,String id,String coreCustomerId)
    {
		boolean isExistingLegalEntity = false;
		if (StringUtils.isNoneBlank(id) && StringUtils.isNotBlank(legalEntityId) && coreCustomerId!=null) {
			CustomerLegalEntityDTO customerLegalEntityDTO = null;
			customerLegalEntityDTO = new CustomerLegalEntityDTO();
			customerLegalEntityDTO.setCustomer_id(id);
			customerLegalEntityDTO.setLegalEntityId(legalEntityId);

			List<CustomerLegalEntityDTO> customerLegalEntityIds = (List<CustomerLegalEntityDTO>) customerLegalEntityDTO
					.loadDTO();
			if (customerLegalEntityIds != null && !customerLegalEntityIds.isEmpty()) {
				for (CustomerLegalEntityDTO customerLegalEntityDTOs : customerLegalEntityIds) {
					if (legalEntityId.equalsIgnoreCase(customerLegalEntityDTOs.getLegalEntityId()))
						isExistingLegalEntity = true;
				}
			}
		}
		return isExistingLegalEntity;
    }
    @Override
    public DBXResult editInfinityUser(JsonObject jsonObject, Map<String, Object> headerMap) throws ApplicationException {
		JsonObject userDetails = jsonObject.has(InfinityConstants.userDetails)
				&& jsonObject.get(InfinityConstants.userDetails).isJsonObject()
						? jsonObject.get(InfinityConstants.userDetails).getAsJsonObject()
						: null;
		String id = userDetails.has(InfinityConstants.id) && !userDetails.get(InfinityConstants.id).isJsonNull()
				? userDetails.get(InfinityConstants.id).getAsString()
				: null;
		String legalEntityId = JSONUtil.hasKey(userDetails, "legalEntityId")
				? JSONUtil.getString(userDetails, "legalEntityId")
				: null;
		String coreCustomerId = userDetails.has(InfinityConstants.coreCustomerId)
                && !userDetails.get(InfinityConstants.coreCustomerId).isJsonNull()
                        ? userDetails.get(InfinityConstants.coreCustomerId).getAsString()
                        : null;
		boolean isExistingLegalEntity = getIsExistingLegalEntity(legalEntityId, id,coreCustomerId);
		InfinityUserManagementBackendDelegate backendDelegate = new InfinityUserManagementBackendDelegateImpl();
    	DBXResult dbxResult = backendDelegate.editInfinityUser(jsonObject, headerMap);

    	
		if (StringUtils.isNotBlank(id) && coreCustomerId!=null && !isExistingLegalEntity) {
			createCustomer(dbxResult, userDetails, headerMap);
		}

		if (StringUtils.isNotBlank(id) && userDetails != null)
			updateCustomer(userDetails, headerMap);
		return dbxResult;
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

        JsonObject jsonObject =
                ServiceCallHelper.invokeServiceAndGetJson(map, headerMap, URLConstants.CUSTOMERCOMMUNICATION_GET);
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
    public DBXResult getInfinityUser(JsonObject inputJson, Map<String, Object> headerMap) {
        InfinityUserManagementBackendDelegate backendDelegate = new InfinityUserManagementBackendDelegateImpl();
        DBXResult dbxResult = backendDelegate.getInfinityUser(inputJson, headerMap);
        JsonObject jsonObject =
                dbxResult.getResponse() != null ? (JsonObject) dbxResult.getResponse() : new JsonObject();

        JsonObject userDetails = jsonObject.has(InfinityConstants.userDetails)
                && jsonObject.get(InfinityConstants.userDetails).isJsonObject()
                        ? jsonObject.get(InfinityConstants.userDetails).getAsJsonObject()
                        : null;

        String customerId = inputJson.has(InfinityConstants.id) && !inputJson.get(InfinityConstants.id).isJsonNull()
                ? inputJson.get(InfinityConstants.id).getAsString()
                : null;
        if (userDetails != null && StringUtils.isNotBlank(customerId))
            getCustomerJson(customerId, userDetails, headerMap);
        return dbxResult;
    }

    private void getCustomerJson(String customerId, JsonObject userDetails, Map<String, Object> headerMap) {

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(customerId);
        ProfileManagementBackendDelegate profileManagementBackendDelegate =
                DBPAPIAbstractFactoryImpl.getBackendDelegate(ProfileManagementBackendDelegate.class);
        DBXResult dbxResult = profileManagementBackendDelegate.getCustomerForUserResponse(customerDTO, headerMap);

        Map<String, String> map = (Map<String, String>) dbxResult.getResponse();
        userDetails.addProperty(InfinityConstants.firstName, map.get("FirstName"));
        userDetails.addProperty(InfinityConstants.lastName, map.get("LastName"));
        userDetails.addProperty(InfinityConstants.middleName, map.get("MiddleName"));
        userDetails.addProperty(InfinityConstants.dob, map.get("DateOfBirth"));
        userDetails.addProperty(InfinityConstants.ssn, map.get("Ssn"));
        userDetails.addProperty(InfinityConstants.drivingLicenseNumber, map.get("DrivingLicenseNumber"));

        CustomerCommunicationDTO customerCommunicationDTO = new CustomerCommunicationDTO();
        customerCommunicationDTO.setCustomer_id(customerId);
        CommunicationBackendDelegate backendDelegate =
                DBPAPIAbstractFactoryImpl.getBackendDelegate(CommunicationBackendDelegate.class);
        dbxResult =
                backendDelegate.getPrimaryMFACommunicationDetails(customerCommunicationDTO, headerMap);
        if (dbxResult.getResponse() != null) {
            JsonObject jsonObject = (JsonObject) dbxResult.getResponse();
            if (jsonObject.has(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION)) {
                JsonElement jsonElement = jsonObject.get(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION);
                if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
                    JsonArray jsonArray = jsonElement.getAsJsonArray();
                    for (int i = 0; i < jsonArray.size(); i++) {
                        jsonObject = jsonArray.get(i).getAsJsonObject();
                        if (jsonObject.get("Type_id").getAsString()
                                .equals(HelperMethods.getCommunicationTypes().get("Phone"))) {
                            userDetails.addProperty(InfinityConstants.phoneNumber,
                                    jsonObject.has("Value") && !jsonObject.get("Value").isJsonNull()
                                            ? jsonObject.get("Value").getAsString()
                                            : null);

                            userDetails.addProperty(InfinityConstants.phoneCountryCode,
                                    jsonObject.has("phoneCountryCode")
                                            && !jsonObject.get("phoneCountryCode").isJsonNull()
                                                    ? jsonObject.get("phoneCountryCode").getAsString()
                                                    : null);
                        } else if (jsonObject.get("Type_id").getAsString()
                                .equals(HelperMethods.getCommunicationTypes().get("Email"))) {
                            userDetails.addProperty(InfinityConstants.email,
                                    jsonObject.has("Value") && !jsonObject.get("Value").isJsonNull()
                                            ? jsonObject.get("Value").getAsString()
                                            : null);
                        }
                    }
                }
            }
        }

        return;
    }

    @Override
    public DBXResult createCustomRole(JsonObject jsonObject, Map<String, Object> headerMap) {

        InfinityUserManagementBackendDelegate backendDelegate = new InfinityUserManagementBackendDelegateImpl();
        return backendDelegate.createCustomRole(jsonObject, headerMap);

    }

    @Override
    public DBXResult editCustomRole(JsonObject jsonObject, Map<String, Object> headerMap) {

        InfinityUserManagementBackendDelegate backendDelegate = new InfinityUserManagementBackendDelegateImpl();
        return backendDelegate.editCustomRole(jsonObject, headerMap);
    }

    @Override
    public DBXResult verifyCustomRole(JsonObject jsonObject, Map<String, Object> headerMap) {
        InfinityUserManagementBackendDelegate backendDelegate = new InfinityUserManagementBackendDelegateImpl();
        return backendDelegate.verifyCustomRole(jsonObject, headerMap);
    }

    @Override
    public DBXResult getCustomRole(JsonObject jsonObject, Map<String, Object> headerMap) {
        InfinityUserManagementBackendDelegate backendDelegate = new InfinityUserManagementBackendDelegateImpl();
        return backendDelegate.getCustomRole(jsonObject, headerMap);
    }

    @Override
    public DBXResult getCustomRoleByCompanyID(JsonObject jsonObject, Map<String, Object> headerMap) {

        InfinityUserManagementBackendDelegate backendDelegate = new InfinityUserManagementBackendDelegateImpl();
        return backendDelegate.getCustomRoleByCompanyID(jsonObject, headerMap);
    }

    @Override
    public DBXResult getCompanyLevelCustomRoles(JsonObject jsonObject, Map<String, Object> headerMap) {

        InfinityUserManagementBackendDelegate backendDelegate = new InfinityUserManagementBackendDelegateImpl();
        return backendDelegate.getCompanyLevelCustomRoles(jsonObject, headerMap);

    }

    @Override
    public DBXResult getAssociatedContractUsers(JsonObject jsonObject, Map<String, Object> headerMap) {
        InfinityUserManagementBackendDelegate backendDelegate = new InfinityUserManagementBackendDelegateImpl();
        return backendDelegate.getAssociatedContractUsers(jsonObject, headerMap);
    }

    @Override
    public List<ContractCustomersDTO> getInfinityContractCustomers(ContractCustomersDTO dto, String filter,
            Map<String, Object> headersMap) throws ApplicationException {
        InfinityUserManagementBackendDelegate backendDelegate = new InfinityUserManagementBackendDelegateImpl();
        return backendDelegate.getInfinityContractCustomers(dto, filter, headersMap);
    }

    @Override
    public DBXResult getInfinityUserContractDetailsGetOperation(String infinityUserId,String infinityLegalEntityId,Map<String, Object> headersMap)
            throws ApplicationException {
        InfinityUserManagementBackendDelegate backendDelegate = new InfinityUserManagementBackendDelegateImpl();
        return backendDelegate.getInfinityUserContractDetailsGetOperation(infinityUserId,infinityLegalEntityId, headersMap);
    }
    
   

    @Override
    public DBXResult generateInfinityUserName(String userNameLength) {
        InfinityUserManagementBackendDelegate backendDelegate = new InfinityUserManagementBackendDelegateImpl();
        return backendDelegate.generateInfinityUserName(userNameLength);
    }

    @Override
    public DBXResult generateActivationCode(String activationCodeLength) {
        InfinityUserManagementBackendDelegate backendDelegate = new InfinityUserManagementBackendDelegateImpl();
        return backendDelegate.generateActivationCode(activationCodeLength);
    }

    @Override
    public DBXResult processNewAccounts(String arg0, Map<String, Object> arg1) throws ApplicationException {
        InfinityUserManagementBackendDelegate backendDelegate = new InfinityUserManagementBackendDelegateImpl();
        return backendDelegate.processNewAccounts(arg0, arg1);
    }

    @Override
    public DBXResult getInfinityUserPrimaryRetailContract(JsonObject arg0, Map<String, Object> arg1) {
        InfinityUserManagementBackendDelegate backendDelegate = new InfinityUserManagementBackendDelegateImpl();
        return backendDelegate.getInfinityUserPrimaryRetailContract(arg0, arg1);
    }

	@Override
	public List<UserCustomerViewDTOwithAccounts> getAssociatedCustomerswithaccounts(
			ContractCustomersDTO contractCustomerDTO, Map<String, Object> headerMap) throws ApplicationException {
		InfinityUserManagementBackendDelegate backendDelegate = new InfinityUserManagementBackendDelegateImpl();
		return backendDelegate.getAssociatedCustomerswithaccounts(contractCustomerDTO, headerMap);
	}

	@Override
	public DBXResult getInfinityUserServiceDefsRoles(JsonObject inputJson, Map<String, Object> headerMap) {
		InfinityUserManagementBackendDelegate backendDelegate = new InfinityUserManagementBackendDelegateImpl();
		return backendDelegate.getInfinityUserServiceDefsRoles(inputJson, headerMap);
	}


	@Override
	public Map<String, String> getUserLegalEntites(String customerId, Map<String, Object> headersMap) {
		InfinityUserManagementBackendDelegate backendDelegate = new InfinityUserManagementBackendDelegateImpl();
        return backendDelegate.getUserLegalEntites(customerId, headersMap);
	}


	@Override
	public DBXResult updateCustomerLanguage(CustomerDTO customer, Map<String, Object> headerMap) throws ApplicationException{
		InfinityUserManagementBackendDelegate backendDelegate = new InfinityUserManagementBackendDelegateImpl();
		return backendDelegate.updateCustomerLanguage(customer, headerMap);

	}

}
