package com.kony.testscripts.createscripts;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.json.JSONArray;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.infinity.dbx.dbp.jwt.auth.AuthConstants;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.BCrypt;
import com.kony.dbputilities.util.DBPDatasetConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.IntegrationTemplateURLFinder;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.LegalEntityUtil;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.URLFinder;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.eum.product.contract.businessdelegate.api.ContractBusinessDelegate;
import com.temenos.dbx.eum.product.contract.businessdelegate.api.CoreCustomerBusinessDelegate;
import com.temenos.dbx.eum.product.contract.resource.api.ContractResource;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.CustomerAccountsBusinessDelegate;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.CustomerActionsBusinessDelegate;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.CustomerGroupBusinessDelegate;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.PasswordHistoryBusinessDelegate;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.UserManagementBusinessDelegate;
import com.temenos.dbx.party.utils.PartyPropertyConstants;
import com.temenos.dbx.party.utils.PartyURLFinder;
import com.temenos.dbx.party.utils.PartyUtils;
import com.temenos.dbx.product.dto.AlternateIdentity;
import com.temenos.dbx.product.dto.BackendIdentifierDTO;
import com.temenos.dbx.product.dto.ContractAccountsDTO;
import com.temenos.dbx.product.dto.ContractCustomersDTO;
import com.temenos.dbx.product.dto.CustomerDTO;
import com.temenos.dbx.product.dto.CustomerGroupDTO;
import com.temenos.dbx.product.dto.CustomerLegalEntityDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.dto.MembershipDTO;
import com.temenos.dbx.product.dto.PartyDTO;
import com.temenos.dbx.product.dto.PasswordHistoryDTO;
import com.temenos.dbx.product.dto.TaxDetails;
import com.temenos.dbx.product.integration.IntegrationMappings;
import com.temenos.dbx.product.utils.DTOConstants;
import com.temenos.dbx.product.utils.DTOUtils;
import com.temenos.dbx.product.utils.HTTPOperations;
import com.temenos.dbx.product.utils.InfinityConstants;
import com.temenos.dbx.usermanagement.resource.api.PartyUserManagementResource;
import com.temenos.dbx.product.contract.resource.api.CoreCustomerResource;
import com.konylabs.middleware.dataobject.ResultToJSON;

public class CreateDemoUsers implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	SimpleDateFormat idFormatter = new SimpleDateFormat("yyMMddHHmmssSSS");

	String userName;
	MembershipDTO membershipDTO = null;
	private PartyDTO partydetailsDTO = null;

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public MembershipDTO getMembershipDTO() {
		return membershipDTO;
	}

	public void setMembershipDTO(MembershipDTO membershipDTO) {
		if (this.membershipDTO == null)
			this.membershipDTO = new MembershipDTO();
		this.membershipDTO = membershipDTO;
	}

	String companyId;

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Result result = new Result();
		JsonArray userCreated = new JsonArray();
		JsonArray userIdCreated = new JsonArray();
		try {
			// added isDefaultActionsEnabled param in request to create default actions for
			// features while creating the
			// contract
			String userId = null;
			Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
			String coreCustomersList = StringUtils.isNotBlank(inputParams.get("coreCustomersList"))
					? inputParams.get("coreCustomersList")
					: request.getParameter("coreCustomersList");
			String partyIdList = StringUtils.isNotBlank(inputParams.get("partyIdList"))
					? inputParams.get("partyIdList")
					: request.getParameter("partyIdList");
			String serviceType = StringUtils.isNotBlank(inputParams.get("serviceType"))
					? inputParams.get("serviceType")
					: request.getParameter("serviceType");
			String legalEntityId = StringUtils.isNotBlank(inputParams.get("legalEntityId"))
					? inputParams.get("legalEntityId")
					: request.getParameter("legalEntityId");
			boolean isRequestWithParty = false;
			if(StringUtils.isNotBlank(partyIdList))
				isRequestWithParty = true;
			JsonParser parser = new JsonParser();
			request.addRequestParam_("isDefaultActionsEnabled", "true");
			
			ContractBusinessDelegate contractBD = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(ContractBusinessDelegate.class);

			JsonArray coreCustomerorPartyListArray = null;
			if (isRequestWithParty) {
				coreCustomerorPartyListArray = parser.parse(partyIdList).getAsJsonArray();
			} else {
				coreCustomerorPartyListArray = parser.parse(coreCustomersList).getAsJsonArray();
			}
			for (JsonElement element : coreCustomerorPartyListArray) {
				String coreCustomerOrPartyDetails = element.getAsString();
				String[] corecustomerOrPartyDetailsArray = StringUtils.split(coreCustomerOrPartyDetails, ":");

				String coreCustomerIdorPartyId = corecustomerOrPartyDetailsArray[0];
				String userName = null;
				String password = "Kony@1234";
				String companyId = legalEntityId;
				userId = null;
				setCompanyId(companyId);
				if (corecustomerOrPartyDetailsArray.length > 1) {
					userName = corecustomerOrPartyDetailsArray[1];
				}
				if (corecustomerOrPartyDetailsArray.length > 2) {
					password = corecustomerOrPartyDetailsArray[2];
				}
				if (corecustomerOrPartyDetailsArray.length > 3) {
					companyId = corecustomerOrPartyDetailsArray[3];
					setCompanyId(companyId);
				}
				try {		
					JSONArray customers = new JSONArray();
					try {
					CoreCustomerResource resource1 = DBPAPIAbstractFactoryImpl.getResource(CoreCustomerResource.class);
			        result = resource1.getCoreRelativeCustomers(methodId, inputArray, request, response);
			         String data = null;
			         data =	ResultToJSON.convert(result);
			         JSONObject jsonObject = new JSONObject(data);
			         customers = (JSONArray) jsonObject.get("customers");
					}catch (Exception e) {
						alert.prepareError("Error in fetching corerelativecustomers",e).log();
					}
			         Map<String, String> contractPayload = createContractPayload(coreCustomerIdorPartyId, request,isRequestWithParty,serviceType,legalEntityId);
					inputArray[1] = contractPayload;		
					ContractResource resource = DBPAPIAbstractFactoryImpl.getResource(ContractResource.class);
					result = resource.createContract(methodId, inputArray, request, response);
					contractBD.updateContractStatus(result.getParamValueByName("contractId"),
							DBPUtilitiesConstants.CONTRACT_STATUS_ACTIVE, getHeadersMap(request));
					request.setAttribute("contractId", result.getParamValueByName("contractId"));
					/*if party is passed as input, we are getting populated corecustomerid for user creation*/
					String partyid = null;
					if(isRequestWithParty)
						partyid = coreCustomerIdorPartyId;
					if (isRequestWithParty && contractPayload.containsKey("coreCustomerIdFromParty")) {
						coreCustomerIdorPartyId = contractPayload.get("coreCustomerIdFromParty");
					}
					
					userId = createAUserAndAssignTOGivenContract(contractPayload, coreCustomerIdorPartyId, userName, password, request,
							methodId, response,isRequestWithParty,partyid,companyId);
					updateContractCustomerDetails(coreCustomerIdorPartyId, result.getParamValueByName("contractId"));
					userCreated.add(this.userName);
					userIdCreated.add(userId);
					
			         if (customers.length() >= 1) {
			        	 for (int i = 0; i < customers.length(); i++) {
			        		 JSONObject newData = customers.getJSONObject(i);
			        		 String relativeCustomerId = (String) newData.get("id");
			        		 coreCustomerIdorPartyId = relativeCustomerId;
			        		 userId = null;
			        		 Map<String, String> contractPayload1 = createContractPayload(coreCustomerIdorPartyId, request,isRequestWithParty,serviceType,legalEntityId);
								inputArray[1] = contractPayload1;
								ContractResource resource2 = DBPAPIAbstractFactoryImpl.getResource(ContractResource.class);
								result = resource2.createContract(methodId, inputArray, request, response);
								contractBD.updateContractStatus(result.getParamValueByName("contractId"),
										DBPUtilitiesConstants.CONTRACT_STATUS_ACTIVE, getHeadersMap(request));
								/*if party is passed as input, we are getting populated corecustomerid for user creation*/
								String partyId = null;
								if(isRequestWithParty)
									partyId = coreCustomerIdorPartyId;
								if (isRequestWithParty && contractPayload1.containsKey("coreCustomerIdFromParty")) {
									coreCustomerIdorPartyId = contractPayload1.get("coreCustomerIdFromParty");
								}
								 JSONObject jsonObject1 = new JSONObject(contractPayload1);	
						         JSONArray customer = new JSONArray(jsonObject1.optString("authorizedSignatoryRoles"));
						         if (i == 0) {
						        	 customer.getJSONObject(0).put("authorizedSignatoryRoleId", "GROUP_AUTHORIZER");
							         customer.getJSONObject(0).put("coreCustomerId", coreCustomerIdorPartyId);
							         contractPayload1.put("authorizedSignatoryRoles", customer.toString());
						         } else if (i == 1) {
						        	 customer.getJSONObject(1).put("authorizedSignatoryRoleId", "GROUP_CREATOR");
							         customer.getJSONObject(1).put("coreCustomerId", coreCustomerIdorPartyId);
							         contractPayload1.put("authorizedSignatoryRoles", customer.toString());
						         }
								userId = createAUserAndAssignTOGivenContract(contractPayload1, coreCustomerIdorPartyId, userName, password, request,
										methodId, response,isRequestWithParty,partyId,companyId);
								userCreated.add(this.userName);
								userIdCreated.add(userId);
			        	 }
			         }
				} catch (ApplicationException e) {
					e.getErrorCodeEnum().setErrorCode(result);
				} catch (Exception e) {
					alert.prepareError("Error occured",e).log();
					ErrorCodeEnum.ERR_10390.setErrorCode(result);
				}
			}
			result.addParam(new Param("status", "success"));
			result.addParam(new Param("userCreated", userCreated.toString()));
			result.addParam(new Param("customerId", userIdCreated.toString()));
		} catch (Exception e) {
			alert.prepareError("Error occured",e).log();
			ErrorCodeEnum.ERR_10390.setErrorCode(result);
		}

		return result;
	}

	private Map<String, Object> getHeadersMap(DataControllerRequest request) {
		Map<String, Object> headers = request.getHeaderMap();
		headers.put("companyId", getCompanyId());
		return headers;
	}

	private String createAUserAndAssignTOGivenContract(Map<String, String> contractPayload, String coreCustomerId,
			String userName, String password, DataControllerRequest request, String methodId,
			DataControllerResponse response,boolean isRequestedwithParty, String partyId,String companyIdPassed) throws ApplicationException {
		Set<String> createdValidContractCoreCustomers = request.getAttribute("createdValidCustomers");
		Map<String, Set<ContractAccountsDTO>> createdCoreCustomerAccounts = request
				.getAttribute("createdCustomerAccounts");
		String createdServiceType = request.getAttribute("serviceType");
		String contractId = request.getAttribute("contractId");
		String authorizedSignatory = contractPayload.get("authorizedSignatory");
		String authorizedSignatoryRoles = contractPayload.get("authorizedSignatoryRoles");
		List<CustomerDTO> authorizedSignatoryList = DTOUtils.getDTOList(authorizedSignatory, CustomerDTO.class);

		if (authorizedSignatoryList == null || authorizedSignatoryList.isEmpty()) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10385);
		}

		Map<String, String> userToCoreCustomerRoles = getUserToCoreCustomerRoles(authorizedSignatoryRoles);
		String userId = createUser(authorizedSignatoryList.get(0), createdServiceType, userName, password, request);
		createUserRoles(userId, contractId, companyIdPassed, userToCoreCustomerRoles, request);
		assignUserToContractCustomers(userId, contractId, createdValidContractCoreCustomers, request);
		createUserAccounts(userId, contractId, createdCoreCustomerAccounts, companyIdPassed,request);
		createUserActionLimits(userId, contractId, createdValidContractCoreCustomers, userToCoreCustomerRoles, request);

		//String companyId = "";
		createBackendIdentifierEntry(coreCustomerId, userId, contractId, request, companyIdPassed, methodId, response,isRequestedwithParty,partyId);
		return userId;

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

	private Map<String, String> createContractPayload(String coreCustomerIdorPartyId, DataControllerRequest dcRequest,
			boolean isRequestWithParty, String serviceType, String legalEntityId)
			throws ApplicationException, HttpCallException {
		Map<String, String> contractPayloadMap = new HashMap<>();
		JsonArray accountsArray = new JsonArray();
		JsonArray contractCustomersJsonArray = new JsonArray();
		JsonObject contractCustomer = new JsonObject();
		JsonArray authorizedSignatoryJsonArray = new JsonArray();
		JsonObject authorizedSignatory = new JsonObject();
		JsonArray authorizedSignatoryRolesJsonArray = new JsonArray();
		JsonObject authorizedSignatoryRole = new JsonObject();
		String serviceDefinitionId = "";
		String serviceDefinitionName = "";
		String coreCustomerId = "";

		LegalEntityUtil.addCompanyIDToHeaders(dcRequest);
		if (isRequestWithParty) {
			coreCustomerId = generatePayloadFromParty(coreCustomerIdorPartyId, contractPayloadMap, dcRequest, contractCustomer,
					serviceType,authorizedSignatory ,authorizedSignatoryRole,legalEntityId);
		} else {
			coreCustomerId = generatePayloadFromT24(coreCustomerIdorPartyId, contractPayloadMap, dcRequest, contractCustomer,
					serviceType, authorizedSignatory,authorizedSignatoryRole,legalEntityId);
		}

		String isWealthRetailCustomer = dcRequest.getParameter("isWealthRetailCustomer");
		if (StringUtils.isNotBlank(isWealthRetailCustomer)) {
			if (Boolean.valueOf(isWealthRetailCustomer)) {
				serviceDefinitionId = "f85d8392-9afe-4128-b23e-a370f138784f";
				serviceDefinitionName = "Retail and wealth online banking";
			} else {
				serviceDefinitionId = "90356097-7fdf-4b8c-89bd-8a1065338a97";
				serviceDefinitionName = "Wealth Online Banking";
			}
			contractPayloadMap.put("serviceDefinitionName", serviceDefinitionName);
			contractPayloadMap.put("serviceDefinitionId", serviceDefinitionId);
			authorizedSignatoryRole.addProperty("authorizedSignatoryRoleId",
					fetchDefaultRoleId(serviceDefinitionId, dcRequest));
		}
		
		String isCorporateCustomer = dcRequest.getParameter("isCorporateCustomer");
		if (StringUtils.isNotBlank(isCorporateCustomer)) {
			if (Boolean.valueOf(isCorporateCustomer)) {
				serviceDefinitionId = "83c9b8d7-3715-480e-8c7d-3d6e61c00035";
				serviceDefinitionName = "Corporate Online Banking";
			}
			contractPayloadMap.put("serviceDefinitionName", serviceDefinitionName);
			contractPayloadMap.put("serviceDefinitionId", serviceDefinitionId);
			authorizedSignatoryRole.addProperty("authorizedSignatoryRoleId",
					fetchDefaultRoleId(serviceDefinitionId, dcRequest));
		}
		

		contractPayloadMap.put("isDefaultActionsEnabled", "true");
		contractPayloadMap.put("communication", "[]");
		contractPayloadMap.put("address", "[]");

		CoreCustomerBusinessDelegate coreCustomerBD = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(CoreCustomerBusinessDelegate.class);

		JsonArray cifArray = new JsonArray();
		cifArray.add(coreCustomerId);
		DBXResult accountsResult = null;
		try {
			accountsResult = coreCustomerBD.getCoreCustomerAccounts(cifArray.toString(), legalEntityId, getHeadersMap(dcRequest));
		} catch (Exception e) {
			e.getMessage();
		}
		if (null == accountsResult) {
			accountsResult = coreCustomerBD.getCoreCustomerAccounts(cifArray.toString(), legalEntityId, getHeadersMap(dcRequest));
		}
		JsonObject resultJson = (JsonObject) accountsResult.getResponse();
		if (JSONUtil.isJsonNotNull(resultJson) && JSONUtil.hasKey(resultJson, "coreCustomerAccounts")
				&& JSONUtil.getJsonArrary(resultJson, "coreCustomerAccounts") != null
				&& JSONUtil.getJsonArrary(resultJson, "coreCustomerAccounts").size() > 0 && JSONUtil
						.getJsonArrary(resultJson, "coreCustomerAccounts").get(0).getAsJsonObject().has("accounts")) {
			accountsArray = JSONUtil.getJsonArrary(resultJson, "coreCustomerAccounts").get(0).getAsJsonObject()
					.get("accounts").getAsJsonArray();

		}

		contractCustomer.addProperty("isPrimary", "true");
		contractCustomer.addProperty("coreCustomerId", coreCustomerId);
		contractCustomer.add("accounts", accountsArray);
		contractCustomer.add("features", getFeaturesList(dcRequest));
		contractCustomersJsonArray.add(contractCustomer);
		contractPayloadMap.put("contractCustomers", contractCustomersJsonArray.toString());
		authorizedSignatoryJsonArray.add(authorizedSignatory);
		contractPayloadMap.put("authorizedSignatory", authorizedSignatoryJsonArray.toString());
		contractPayloadMap.put("legalEntityId",legalEntityId);
		authorizedSignatoryRole.addProperty("coreCustomerId", coreCustomerId);
		
		// if
		// (DBPUtilitiesConstants.BOOLEAN_STRING_TRUE.equalsIgnoreCase(corecustomerDetailsDTO.getIsBusiness()))
		// {
		// authorizedSignatoryRole.addProperty("authorizedSignatoryRoleId",
		// "GROUP_ADMINISTRATOR");
		// } else {
		// authorizedSignatoryRole.addProperty("authorizedSignatoryRoleId",
		// "DEFAULT_GROUP");
		// }

		authorizedSignatoryRolesJsonArray.add(authorizedSignatoryRole);
		contractPayloadMap.put("authorizedSignatoryRoles", authorizedSignatoryRolesJsonArray.toString());
		return contractPayloadMap;
	}

	private String generatePayloadFromT24(String coreCustomerIdorPartyId, Map<String, String> contractPayloadMap,
			DataControllerRequest dcRequest, JsonObject contractCustomer, String serviceType,
			JsonObject authorizedSignatory,JsonObject authorizedSignatoryRole, String legalEntityId) {
		CoreCustomerBusinessDelegate coreCustomerBusinessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(CoreCustomerBusinessDelegate.class);
		MembershipDTO corecustomerDetailsDTO = new MembershipDTO();
		try {
			corecustomerDetailsDTO = coreCustomerBusinessDelegate.getMembershipDetails(coreCustomerIdorPartyId,
					legalEntityId, getHeadersMap(dcRequest));
		} catch (ApplicationException e) {
			alert.prepareError("Error Occured while fetching data from T24").log();
		}
		corecustomerDetailsDTO.setCompanyLegalUnit(legalEntityId);
		setMembershipDTO(corecustomerDetailsDTO);
		contractPayloadMap.put("contractName",
				corecustomerDetailsDTO.getFirstName() + corecustomerDetailsDTO.getLastName());
		String serviceDefinitionId = "";
		String serviceDefinitionName = "";
		if (DBPUtilitiesConstants.BOOLEAN_STRING_TRUE.equalsIgnoreCase(corecustomerDetailsDTO.getIsBusiness())) {
			serviceDefinitionId = "707dfea8-d0fe-4154-89c3-e7d7ef2ee16a";
			serviceDefinitionName = "SME Online Banking";
		} else {
			serviceDefinitionId = "5801fa32-a416-45b6-af01-b22e2de93777";
			serviceDefinitionName = "Retail Online Banking";
		}
		contractCustomer.addProperty("isBusiness", corecustomerDetailsDTO.getIsBusiness());
		contractCustomer.addProperty("coreCustomerName",
				corecustomerDetailsDTO.getFirstName() + corecustomerDetailsDTO.getLastName());
		authorizedSignatory.addProperty("FirstName", corecustomerDetailsDTO.getFirstName());
		authorizedSignatory.addProperty("LastName", corecustomerDetailsDTO.getLastName());
		authorizedSignatory.addProperty("DateOfBirth", corecustomerDetailsDTO.getDateOfBirth());
		authorizedSignatory.addProperty("Ssn", corecustomerDetailsDTO.getTaxId());
		contractPayloadMap.put("serviceDefinitionName", serviceDefinitionName);
		contractPayloadMap.put("serviceDefinitionId", serviceDefinitionId);
		contractPayloadMap.put("legalEntityId",legalEntityId);
		authorizedSignatoryRole.addProperty("authorizedSignatoryRoleId",
				fetchDefaultRoleId(serviceDefinitionId, dcRequest));
		return coreCustomerIdorPartyId;
	}

	private String generatePayloadFromParty(String coreCustomerIdorPartyId, Map<String, String> contractPayloadMap,
			DataControllerRequest dcRequest, JsonObject contractCustomer, String serviceType,
			JsonObject authorizedSignatory,JsonObject authorizedSignatoryRole, String legalEntityId) {
		String coreCustomerId = "";
		LegalEntityUtil.addCompanyIDToHeaders(dcRequest);
		PartyDTO partyDTO = getCustomerDataFromParty(dcRequest.getHeaderMap(), coreCustomerIdorPartyId);
		setPartydetailsDTO(partyDTO);
		if (partyDTO.getAlternateIdentities() != null) {
			AlternateIdentity aIdentity = partyDTO.getAlternateIdentities().get(0);
			coreCustomerId = aIdentity.getIdentityNumber();
			if (coreCustomerId != null) {
				String[] custsplitarray = coreCustomerId.split("-");
				if (custsplitarray != null && custsplitarray.length == 2)
					coreCustomerId = custsplitarray[1];
			}
		}
		String serviceDefinitionId = "5801fa32-a416-45b6-af01-b22e2de93777";
		String serviceDefinitionName = "Retail Online Banking";
		boolean isBusiness = serviceType != null && serviceType.equalsIgnoreCase("business");
		if (isBusiness) {
			serviceDefinitionId = "83c9b8d7-3715-480e-8c7d-3d6e61c00035";
			serviceDefinitionName = "Corporate Online Banking";
		}
		contractPayloadMap.put("coreCustomerIdFromParty", coreCustomerId);
		String contractName = "";
		if (partyDTO.getFirstName() != null)
			contractName = contractName + partyDTO.getFirstName();
		if (partyDTO.getLastName() != null)
			contractName = contractName + partyDTO.getLastName();
		contractPayloadMap.put("contractName", contractName);
		contractCustomer.addProperty("isBusiness", isBusiness);
		contractCustomer.addProperty("coreCustomerName", partyDTO.getFirstName() + partyDTO.getLastName());
		authorizedSignatory.addProperty("FirstName", partyDTO.getFirstName());
		authorizedSignatory.addProperty("LastName", partyDTO.getLastName());
		authorizedSignatory.addProperty("DateOfBirth", partyDTO.getDateOfBirth());
		authorizedSignatoryRole.addProperty("authorizedSignatoryRoleId",
				fetchDefaultRoleId(serviceDefinitionId, dcRequest));
		contractPayloadMap.put("serviceDefinitionName", serviceDefinitionName);
		contractPayloadMap.put("serviceDefinitionId", serviceDefinitionId);
		contractPayloadMap.put("legalEntityId",legalEntityId);
		String ssn = "";
		if (partyDTO.getTaxDetails() != null) {
			TaxDetails taxDetails = partyDTO.getTaxDetails().get(0);
			if (taxDetails != null)
				ssn = taxDetails.getTaxId();
		}
		authorizedSignatory.addProperty("Ssn", ssn);
		return coreCustomerId;
	}

	private PartyDTO getCustomerDataFromParty(Map<String, Object> map, String partyId) {

		PartyDTO partyDTO = new PartyDTO();

		JsonObject jsonObject ;
		PartyUtils.addJWTAuthHeader(map, AuthConstants.PRE_LOGIN_FLOW);
		String partyURL = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL)
				+ PartyURLFinder.getServiceUrl(PartyPropertyConstants.PARTY_GET, partyId);

		DBXResult response = HTTPOperations.sendHttpRequest(HTTPOperations.operations.GET, partyURL, null, map);
		jsonObject = new JsonParser().parse((String) response.getResponse()).getAsJsonObject();
		partyDTO.loadFromJson(jsonObject);

		return partyDTO;
	}

	private String fetchDefaultRoleId(String serviceDefinitionId, DataControllerRequest request) {
		String filter = "serviceDefinitionId" + DBPUtilitiesConstants.EQUAL + serviceDefinitionId
				+ DBPUtilitiesConstants.AND + "isDefaultGroup" + DBPUtilitiesConstants.EQUAL + '1';
		Map<String, Object> input = new HashMap<>();
		input.put(DBPUtilitiesConstants.FILTER, filter);
		JsonObject jsonResponse = ServiceCallHelper.invokeServiceAndGetJson(input, request.getHeaderMap(),
				URLConstants.GROUP_SERVICEDEFINITION);
		if (jsonResponse.has(DBPDatasetConstants.DATASET_GROUPSERVICEDEFINITION)
				&& jsonResponse.get(DBPDatasetConstants.DATASET_GROUPSERVICEDEFINITION).isJsonArray()
				&& jsonResponse.get(DBPDatasetConstants.DATASET_GROUPSERVICEDEFINITION).getAsJsonArray().size() > 0) {
			JsonObject groupServiceDefinition = jsonResponse.get(DBPDatasetConstants.DATASET_GROUPSERVICEDEFINITION)
					.getAsJsonArray().get(0).getAsJsonObject();
			return JSONUtil.getString(groupServiceDefinition, InfinityConstants.Group_id);
		}
		return "";
	}

	private JsonElement getFeaturesList(DataControllerRequest dcRequest) throws HttpCallException {
		JsonArray features = new JsonArray();
		Result result = HelperMethods.callGetApi(dcRequest, null, HelperMethods.getHeaders(dcRequest),
				URLConstants.FEATURE_GET);
		for (com.konylabs.middleware.dataobject.Record r : result.getDatasetById("feature").getAllRecords()) {
			JsonObject json = new JsonObject();
			json.addProperty("featureId", r.getParamValueByName("id"));
			features.add(json);
		}
		return features;
	}

	private String createUser(CustomerDTO customerDTO, String createdServiceType, String userName, String password,
			DataControllerRequest request) throws ApplicationException {
		UserManagementBusinessDelegate customerBD = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(UserManagementBusinessDelegate.class);

		PasswordHistoryBusinessDelegate passwordHistoryBD = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(PasswordHistoryBusinessDelegate.class);

		String salt = BCrypt.gensalt(DBPUtilitiesConstants.SALT_NUMBER_OF_ROUNDS);
		String hashedPassword = BCrypt.hashpw(password, salt);

		String id = HelperMethods.generateUniqueCustomerId(request);
		if (StringUtils.isBlank(userName)) {
			this.userName = HelperMethods.generateUniqueUserName(request);
		} else {
			this.userName = userName;
		}
		customerDTO.setId(id);
		customerDTO.setUserName(this.userName);
		customerDTO.setIsNew(true);
		customerDTO.setStatus_id(DBPUtilitiesConstants.CUSTOMER_STATUS_ACTIVE);
		customerDTO.setCompanyLegalUnit(companyId);
		customerDTO.setHomeLegalEntity(companyId);
		customerDTO.setDefaultLegalEntity(companyId);
		/*For business party call not happening*/
		if ("TYPE_ID_WEALTH".equalsIgnoreCase(createdServiceType))
			createdServiceType = "TYPE_ID_RETAIL";
		customerDTO.setCustomerType_id(createdServiceType);
		customerDTO.setIsEnrolled(true);
		customerDTO.setPassword(hashedPassword);

		DBXResult customerResult = customerBD.update(customerDTO, getHeadersMap(request));
		
		CustomerLegalEntityDTO customerLegalEntityDTO = new CustomerLegalEntityDTO();
		customerLegalEntityDTO.setId(HelperMethods.getNewId());
		customerLegalEntityDTO.setCustomer_id(id);
		customerLegalEntityDTO.setNew(true);
		customerLegalEntityDTO.setStatus_id(HelperMethods.getCustomerStatus().get("ACTIVE"));
		customerLegalEntityDTO.setLegalEntityId(companyId);
		customerLegalEntityDTO.persist(DTOUtils.getParameterMap(customerLegalEntityDTO, true), request.getHeaderMap());
		
		String customerId = (String) customerResult.getResponse();
		if (StringUtils.isBlank(customerId)) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10386);
		}
		
		

		PasswordHistoryDTO passwordHistoryDTO = new PasswordHistoryDTO();
		passwordHistoryDTO.setId(idFormatter.format(new Date()));
		passwordHistoryDTO.setCustomer_id(customerId);
		passwordHistoryDTO.setPreviousPassword(hashedPassword);

		passwordHistoryBD.update(passwordHistoryDTO, getHeadersMap(request));
		return customerId;
	}

	private void createUserRoles(String userId, String contractId, String companyIdPassed, Map<String, String> userToCoreCustomerRoles,
			DataControllerRequest request) throws ApplicationException {
		CustomerGroupBusinessDelegate customerGroupBD = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(CustomerGroupBusinessDelegate.class);

		CustomerGroupDTO dto = new CustomerGroupDTO();
		dto.setCustomerId(userId);
		dto.setContractId(contractId);
		dto.setCompanyLegalUnit(companyIdPassed);
		for (Entry<String, String> entry : userToCoreCustomerRoles.entrySet()) {
			String coreCustomerId = entry.getKey();
			String roleId = entry.getValue();
			dto.setCoreCustomerId(coreCustomerId);
			dto.setGroupId(roleId);
			customerGroupBD.createCustomerGroup(dto, getHeadersMap(request));
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
			contractCustomerDTO.setCompanyLegalUnit(request.getParameter("legalEntityId"));
			Map<String, Object> inputParams = DTOUtils.getParameterMap(contractCustomerDTO, false);
			contractCustomerDTO.persist(inputParams, getHeadersMap(request));
		}

	}

	private void createUserAccounts(String userId, String contractId,
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
					getHeadersMap(request));
		}

	}

	private void createUserActionLimits(String userId, String contractId, Set<String> createdValidContractCoreCustomers,
			Map<String, String> userToCoreCustomerRoles, DataControllerRequest request) throws ApplicationException {
		CustomerActionsBusinessDelegate customerActionsBD = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(CustomerActionsBusinessDelegate.class);
		alert.prepareError("started createUserActionLimits ..").log();
		for (String coreCustomerId : createdValidContractCoreCustomers) {
			customerActionsBD.createCustomerActions(userId, contractId, coreCustomerId, getCompanyId(),
					userToCoreCustomerRoles.get(coreCustomerId), new HashSet<String>(), getHeadersMap(request));

			customerActionsBD.createCustomerLimitGroupLimits(userId, contractId, coreCustomerId, getCompanyId(),
					getHeadersMap(request));
		}
		alert.prepareError("end createUserActionLimits ..").log();
	}

	private void createBackendIdentifierEntry(String backendId, String userId, String contractId,
			DataControllerRequest request, String companyId, String methodId, DataControllerResponse response,
			boolean isRequestedwithParty, String partyIdPassed) throws ApplicationException {
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
		if (StringUtils.isBlank(companyId)) {
			backendIdentifierDTO
					.setCompanyId(EnvironmentConfigurationsHandler.getValue(DBPUtilitiesConstants.BRANCH_ID_REFERENCE));
		}
		Map<String, Object> input = DTOUtils.getParameterMap(backendIdentifierDTO, true);
		backendIdentifierDTO.setIsNew(true);
		backendIdentifierDTO.persist(input, getHeadersMap(request));

		final String IS_Integrated = Boolean.toString(IntegrationTemplateURLFinder.isIntegrated);
		if (StringUtils.isNotBlank(IS_Integrated) && IS_Integrated.equalsIgnoreCase("true") && !isRequestedwithParty) {
			backendIdentifierDTO = new BackendIdentifierDTO();
			backendIdentifierDTO.setIsNew(true);
			backendIdentifierDTO.setId(UUID.randomUUID().toString());
			backendIdentifierDTO
					.setBackendType(IntegrationTemplateURLFinder.getBackendURL(InfinityConstants.BackendType));
			backendIdentifierDTO.setIdentifier_name(
					IntegrationTemplateURLFinder.getBackendURL(InfinityConstants.BackendCustomerIdentifierName));
			backendIdentifierDTO.setCustomer_id(userId);
			backendIdentifierDTO.setContractId(contractId);
			backendIdentifierDTO.setSequenceNumber("1");
			backendIdentifierDTO.setCompanyId(companyId);
			backendIdentifierDTO.setBackendId(backendId);
			backendIdentifierDTO.setCompanyLegalUnit(companyId);
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
		String partyId = null;
		if (!"T24".equalsIgnoreCase(IntegrationMappings.getInstance().getIntegrationName())) {
			try {
				if (StringUtils.isBlank(partyIdPassed)) {

					partyId = getPartyId(backendId, request);
					if (StringUtils.isBlank(partyId)) {
						partyId = createParty(methodId, request, response);
					}
				} else
					partyId = partyIdPassed;
			} catch (Exception e) {

			} 
		}
		if (StringUtils.isNotBlank(partyId)) {
			backendIdentifierDTO = new BackendIdentifierDTO();
			backendIdentifierDTO.setIsNew(true);
			backendIdentifierDTO.setId(UUID.randomUUID().toString());
			backendIdentifierDTO.setBackendType("PARTY");
			backendIdentifierDTO.setIdentifier_name("customer_id");
			backendIdentifierDTO.setCustomer_id(userId);
			backendIdentifierDTO.setContractId(contractId);
			backendIdentifierDTO.setSequenceNumber("1");
			backendIdentifierDTO.setCompanyId(companyId);
			backendIdentifierDTO.setBackendId(partyId);
			backendIdentifierDTO.setCompanyLegalUnit(companyId);
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
	}

	private String createParty(String methodId, DataControllerRequest request, DataControllerResponse response) {
		MembershipDTO dto = this.getMembershipDTO();
		Map<String, Object> inputParams = new HashMap<>();
		inputParams.put("partyType", "Individual");
		inputParams.put("firstName", dto.getFirstName());
		inputParams.put("lastName", dto.getLastName());
		JsonArray jsonarray = new JsonArray();
		JsonObject jsonobject = new JsonObject();
		jsonobject.addProperty("phoneAddressType", "Home Number");
		String phone = dto.getPhone();
		boolean isFormat = false;
		String[] array = new String[2];
		if (phone.contains("-")) {
			isFormat = true;
			array = phone.split("-");
		}
		jsonobject.addProperty("internationalPhoneNo", isFormat ? array[1] : dto.getPhone());
		jsonobject.addProperty("iddPrefixPhone", isFormat ? array[0] : "+91");
		jsonobject.addProperty("nationalPhoneNo", isFormat ? array[1] : dto.getPhone());
		jsonarray.add(jsonobject);
		inputParams.put("phoneAddress", jsonarray.toString());
		inputParams.put("partyStatus", "Active");
		jsonarray = new JsonArray();
		jsonobject = new JsonObject();
		jsonobject.addProperty("startDate", HelperMethods.getCurrentDate());
		jsonobject.addProperty("firstName", dto.getFirstName());
		jsonobject.addProperty("lastName", dto.getLastName());
		jsonobject.addProperty("middleName", "");
		jsonobject.addProperty("suffix", "");
		jsonarray.add(jsonobject);
		inputParams.put("partyNames", jsonarray.toString());
		jsonarray = new JsonArray();
		jsonobject = new JsonObject();
		jsonobject.addProperty("physicalAddressType", "Office");
		jsonobject.addProperty("startDate", HelperMethods.getCurrentDate());
		jsonobject.addProperty("reliabilityType", "Confirmed");
		jsonobject.addProperty("contactName", dto.getFirstName() + dto.getLastName());
		jsonobject.addProperty("endDate", HelperMethods.getCurrentDate());
		jsonobject.addProperty("countryCode", "US");
		jsonobject.addProperty("town", dto.getCityName());
		jsonobject.addProperty("postalOrZipCode", dto.getZipCode());
		jsonobject.addProperty("addressCountry", dto.getCountry());
		jsonarray.add(jsonobject);
		inputParams.put("contactAddress", jsonarray.toString());

		Object[] inputArray1 = new Object[3];
		inputArray1[1] = inputParams;
		PartyUserManagementResource resource = DBPAPIAbstractFactoryImpl.getResource(PartyUserManagementResource.class);
		Result partyCreateResponse = resource.partyCreate(methodId, inputArray1, request, response);
		return null;
	}

	public static Map<String, Object> addJWTAuthHeader(DataControllerRequest request, Map<String, Object> headers,
			String flow_type) {
		String authToken = URLFinder.getServerRuntimeProperty("PARTY_AUTH_TOKEN");
		headers.put("Authorization", authToken);

		String deploymentPlatform = URLFinder.getServerRuntimeProperty("PARTYMS_DEPLOYMENT_PLATFORM");
		if (StringUtils.isNotBlank(deploymentPlatform)) {
			if (StringUtils.equals(deploymentPlatform, "aws"))
				headers.put("x-api-key", URLFinder.getServerRuntimeProperty("PARTYMS_AUTHORIZATION_KEY"));
			if (StringUtils.equals(deploymentPlatform, "azure"))
				headers.put("x-functions-key", URLFinder.getServerRuntimeProperty("PARTYMS_AUTHORIZATION_KEY"));
		}
		headers.put("companyid", request.getParameter("legalEntityId"));
		return headers;
	}

	private String getPartyId(String backendId, DataControllerRequest request) {
		String companyId = EnvironmentConfigurationsHandler
				.getServerProperty(DBPUtilitiesConstants.BRANCH_ID_REFERENCE);
		String id = getCompanyId() + "-" + backendId;
		Map<String, Object> headers = addJWTAuthHeader(request, request.getHeaderMap(), AuthConstants.PRE_LOGIN_FLOW);
		String queryParams = "";

		queryParams += "alternateIdentifierNumber=" + id;
		queryParams += "&";
		queryParams += "alternateIdentifierType=BackOfficeIdentifier";

		DBXResult dbxResult = new DBXResult();
		JsonObject partyJson = new JsonObject();
		JsonArray partyJsonArray = new JsonArray();
		if (queryParams.length() > 1) {
			try {
				String partyURL = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL)
						+ "/api/v4.0.0/party/parties?" + queryParams;

				dbxResult = HTTPOperations.sendHttpRequest(HTTPOperations.operations.GET, partyURL, null, headers);
				if (dbxResult.getResponse() != null) {
					JsonElement partyResponse = new JsonParser().parse((String) dbxResult.getResponse());
					if (partyResponse.isJsonObject()) {
						JsonObject partyResponseObject = partyResponse.getAsJsonObject();
						if (partyResponseObject.has("parties") && partyResponseObject.get("parties").isJsonArray()
								&& partyResponseObject.get("parties").getAsJsonArray().size() > 0) {
							partyJsonArray = partyResponseObject.get("parties").getAsJsonArray();
							partyJson = partyJsonArray.get(0).getAsJsonObject();
						}
					}
				}
			} catch (Exception e) {
			}
		}
		return JSONUtil.getString(partyJson, "partyId");
	}

	public PartyDTO getPartydetailsDTO() {
		return partydetailsDTO;
	}

	public void setPartydetailsDTO(PartyDTO partydetailsDTO) {
		this.partydetailsDTO = partydetailsDTO;
	}
	
	private void updateContractCustomerDetails(String coreCustomerId, String contractId) throws ApplicationException {;
		String id = null;
		
		try {
			Map<String, Object> inputParams = new HashMap<>();
			String filter = "coreCustomerId eq " + coreCustomerId;
			inputParams.put(DBPUtilitiesConstants.FILTER, filter);
			alert.prepareError("Error in fetching corerelativecustomers "+inputParams.toString()).log();
			JsonObject contractCustomersJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, new HashMap<String, Object>(),
			        URLConstants.CONTRACT_CUSTOMERS_GET);
			if (null != contractCustomersJson
			        && JSONUtil.hasKey(contractCustomersJson, "contractcustomers")
			        && contractCustomersJson.get("contractcustomers").isJsonArray()) {
			    JsonArray array = contractCustomersJson.get("contractcustomers")
			            .getAsJsonArray();
			    id = array.get(0).getAsJsonObject().get("id").getAsString();
			}

			if (StringUtils.isNotBlank(id)) {
				inputParams.clear();
				inputParams.put("id", id);
				inputParams.put("isPrimary",'1');
				alert.prepareError("Error in fetching corerelativecustomers "+inputParams.toString()).log();
				ServiceCallHelper.invokeServiceAndGetJson("dbpRbLocalServicesdb",null,"dbxdb_contractcustomers_update",
						inputParams, new HashMap<String, Object>());
			}
		} catch (Exception e) {
			alert.prepareError("Error in fetching corerelativecustomers",e).log();
		}

	}

}
