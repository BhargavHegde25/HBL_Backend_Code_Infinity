package com.temenos.dbx.datamigrationservices.resource.impl;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.ConvertJsonToResult;
import com.kony.dbputilities.util.DBPDatasetConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.temenos.dbx.datamigrationservices.resource.api.MigrateContractResource;
import com.temenos.dbx.eum.product.contract.backenddelegate.api.ContractBackendDelegate;
import com.temenos.dbx.eum.product.contract.backenddelegate.api.CoreCustomerBackendDelegate;
import com.temenos.dbx.eum.product.contract.businessdelegate.api.ContractBusinessDelegate;
import com.temenos.dbx.eum.product.contract.businessdelegate.api.CoreCustomerBusinessDelegate;
import com.temenos.dbx.eum.product.contract.resource.api.ContractResource;
import com.temenos.dbx.product.dto.AllAccountsViewDTO;
import com.temenos.dbx.product.dto.ContractCoreCustomersDTO;
import com.temenos.dbx.product.dto.ContractDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.dto.FeatureActionLimitsDTO;
import com.temenos.dbx.product.dto.MembershipDTO;
import com.temenos.dbx.product.utils.InfinityConstants;

@SuppressWarnings("deprecation")
public class MigrateContractResourceImpl implements MigrateContractResource {
	
	LoggerUtil logger = new LoggerUtil(MigrateContractResourceImpl.class);
	SimpleDateFormat idFormatter = new SimpleDateFormat("yyMMddHHmmssSSS");

	private static final String CONTRACT_ID = "contractId";
	private static final String SERVICE_DEFINITION_ID = "serviceDefinitionId";
	private static final String COMMUNICATION = "communication";
	private static final String PHONE_NUMBER = "phoneNumber";
	private static final String PHONE_COUNTRY_CODE = "phoneCountryCode";
	private static final String TYPEID = "typeId";
	private static final String COMM_VALUE = "value";
	private static final String EMAIL = "email";
	private static final String CONTRACTCUSTOMERS = "contractCustomers";
	private static final String PRIMARY_CIF = "primaryCif";
	private static final String COMMUNICATION_DETAILS = "communicationDetails";
	private static final String CIF_LIST = "cifList";
	public static final String IMPLICIT_ACCOUNT_ACCESS = "implicitAccountAccess";
	private static final String LEGAL_ENTITY_ID = "legalEntityId";
    
    @Override
	public Result createContract(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws ApplicationException {
		Result result = new Result();
		try {

			if (StringUtils.isBlank(dcRequest.getParameter("legalEntityId"))) {
				ErrorCodeEnum.ERR_10001.setErrorCode(result, "22232", "LegalEntityId cannot be null");
				return result;
			}
			if (StringUtils.isBlank(dcRequest.getParameter("serviceDefinitionId"))) {
				ErrorCodeEnum.ERR_10001.setErrorCode(result, "22232", "Invalid or missing service definition name or id");
				return result;
			}

			if (StringUtils.isBlank(dcRequest.getParameter("cifList"))) {
				ErrorCodeEnum.ERR_10001.setErrorCode(result, "22232", "Invalid or missing contract Customers details");
				return result;
			}

			Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);

			getCreateContractPayload(inputParams, dcRequest);

			logger.debug("******* the final input map " + inputParams);
			logger.debug("******* the final inputArray " + inputArray[1]);

			ContractResource resource = DBPAPIAbstractFactoryImpl.getResource(ContractResource.class); 
			result = resource.createContract(methodID, inputArray, dcRequest, dcResponse);

			if (result == null || !result.hasParamByName("status")
					|| !result.getParamValueByName("status").equalsIgnoreCase("Success")
					|| !result.hasParamByName("contractId") || result.getParamValueByName("contractId").equals("")) {
				ErrorCodeEnum.ERR_21012.setErrorCode(result, "Failed to create customer");
				result.addParam(new Param("status", "Failure", "string"));
				return result;
			}
			if (result.hasParamByName("dbpErrMsg")) {
				result.addParam(new Param("status", "Failure", "string"));
				return result;
			}
			result.addParam(new Param("status", "Success", "string"));

			return result;
		} catch (Exception e) {
			logger.debug("Exception occured while creating a contract " + e);
			result.addParam(new Param("FailureReason", e.getMessage(), "string"));
			return ErrorCodeEnum.ERR_10001.setErrorCode(result, "21967","Error returned while creating or editing contract");
		}
	}
    
    private void getCreateContractPayload(Map<String, String> inputParams, DataControllerRequest dcRequest) throws Exception {
    	
    	String serviceDefinitionId = inputParams.get(SERVICE_DEFINITION_ID);
        String cifListString = inputParams.get(CIF_LIST);            
        String legalEntityId = inputParams.get(LEGAL_ENTITY_ID); 
        String primaryCif = inputParams.get(PRIMARY_CIF);
        
        logger.debug("******** mandatory params serviceDefinitionId :"+serviceDefinitionId);
        logger.debug("******** mandatory params cifListString :"+cifListString);
        logger.debug("******** mandatory params legalEntityId :"+legalEntityId);
        logger.debug("******** mandatory params primaryCif :"+primaryCif);
        
        // Checking for mandatory input
 		if (HelperMethods.isBlank(serviceDefinitionId, legalEntityId, cifListString)) {
 			logger.debug("******** mandatory params failed");
 			throw new ApplicationException(ErrorCodeEnum.ERR_10348);
 		}
        
		// Adding core customer details along with accounts and communication details
		populateFeaturesPermissionsLimitsAndCoreCustomerDetails(inputParams, dcRequest);
		
		// Adding ServiceDefinitionName
		String serviceDefinitionName = "";
		String filter = InfinityConstants.id + DBPUtilitiesConstants.EQUAL + serviceDefinitionId
				+ DBPUtilitiesConstants.AND + InfinityConstants.LegalEntityId + DBPUtilitiesConstants.EQUAL
				+ legalEntityId;

		Map<String, Object> input = new HashMap<String, Object>();
		input.put(DBPUtilitiesConstants.FILTER, filter);
		JsonObject jsonResponse = ServiceCallHelper.invokeServiceAndGetJson(input, dcRequest.getHeaderMap(), URLConstants.SERVICEDEFINITION_GET);
		
		if (jsonResponse.has(DBPDatasetConstants.DATASET_SERVICEDEFINITION)
				&& jsonResponse.get(DBPDatasetConstants.DATASET_SERVICEDEFINITION).isJsonArray()) {

			JsonObject serviceDefinition = jsonResponse.get(DBPDatasetConstants.DATASET_SERVICEDEFINITION).getAsJsonArray().size() > 0
							? jsonResponse.get(DBPDatasetConstants.DATASET_SERVICEDEFINITION).getAsJsonArray().get(0).getAsJsonObject()
							: new JsonObject();
			serviceDefinitionName = serviceDefinition.has(InfinityConstants.name) && !serviceDefinition.get(InfinityConstants.name).isJsonNull()
							? serviceDefinition.get(InfinityConstants.name).getAsString()
							: "serviceDefinition";
		} else {
			logger.debug("******** no serviceDefinition found");
			throw new ApplicationException(ErrorCodeEnum.ERR_10349);
		}
		logger.debug("******** added serviceDefinitionName to input");
		inputParams.put(InfinityConstants.serviceDefinitionName, serviceDefinitionName);
    	
    }
    
    
    
	private void populateFeaturesPermissionsLimitsAndCoreCustomerDetails(Map<String, String> inputParams, DataControllerRequest dcRequest) throws Exception {
		
		String legalEntityId = inputParams.get(LEGAL_ENTITY_ID);
		String communicationDetailsString = inputParams.get(COMMUNICATION_DETAILS);
		String cifListString = inputParams.get(CIF_LIST);
		String primaryCif = inputParams.get(PRIMARY_CIF);
		JsonArray communication = new JsonArray();
		JsonArray address = new JsonArray();
		String faxId = "";
		
		logger.debug("******** legalEntityId :"+legalEntityId);
		logger.debug("******** communicationDetailsString :"+communicationDetailsString);
		logger.debug("******** cifListString :"+cifListString);
		logger.debug("******** primaryCif :"+primaryCif);
		
		JsonArray cifListArray = new JsonArray();
		try {
			cifListArray = new JsonParser().parse(cifListString).getAsJsonArray();
		} catch (Exception e) {
			logger.debug("******** cifList error");
			throw new ApplicationException(ErrorCodeEnum.ERR_10349, "Invalid CIF input");
		}
		logger.debug("******** cifList Array :"+cifListArray);
		logger.debug("******** cifList size :"+cifListArray.size());
		if (cifListArray.size() > 1 && HelperMethods.isBlank(primaryCif)) {
			logger.debug("******** primaryCif not given");
			throw new ApplicationException(ErrorCodeEnum.ERR_10348);
		}
		
		if(!HelperMethods.isBlank(primaryCif)) {
			inputParams.put(PRIMARY_CIF,primaryCif);
		}else {
			inputParams.put(PRIMARY_CIF, cifListArray.get(0).getAsJsonObject().get("cif").getAsString());
		}
		
		String primaryCoreCustomerId = inputParams.get(PRIMARY_CIF);
		logger.debug("******** primaryCoreCustomerId :"+primaryCoreCustomerId);
		
		if(!StringUtils.isBlank(communicationDetailsString)) {
			JsonObject communicationDetails = new JsonParser().parse(communicationDetailsString).getAsJsonObject(); 
			communication = communicationDetails.get("communication").getAsJsonArray();
			address = communicationDetails.get("address").getAsJsonArray();
			faxId = communicationDetails.get("faxId").getAsString();
		}

		CoreCustomerBusinessDelegate coreCustomerBusinessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(CoreCustomerBusinessDelegate.class);
		MembershipDTO membershipDetails = coreCustomerBusinessDelegate.getMembershipDetails(primaryCoreCustomerId,
				legalEntityId, dcRequest.getHeaderMap());

		if (membershipDetails != null) {
			String name = membershipDetails.getFirstName() + " " + membershipDetails.getLastName();
			logger.debug("******** contractName :"+name);
			inputParams.put(InfinityConstants.contractName, name + " " + primaryCoreCustomerId);
			inputParams.put(InfinityConstants.coreCustomerName, name);

			if (communication.isJsonNull() || communication.isEmpty() || communication.size() == 0) {

				JsonArray array = new JsonArray();
				JsonObject jsonObject = new JsonObject();
				String phone[] = membershipDetails.getPhone().split("-");
				jsonObject.addProperty(InfinityConstants.phoneNumber, phone[1]);
				jsonObject.addProperty(InfinityConstants.phoneCountryCode, phone[0]);
				jsonObject.addProperty(InfinityConstants.email, membershipDetails.getEmail());
				array.add(jsonObject);
				logger.debug("******** adding communication to input :"+array);
				inputParams.put(InfinityConstants.communication, array.toString());
			}else {
				inputParams.put(InfinityConstants.communication, communication.toString());
			}

			if (address.isJsonNull() || address.isEmpty() || address.size() == 0) {

				JsonArray array = new JsonArray();
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty(InfinityConstants.addressLine1, membershipDetails.getAddressLine1());
				jsonObject.addProperty(InfinityConstants.addressLine2, membershipDetails.getAddressLine2());
				jsonObject.addProperty(InfinityConstants.cityName, membershipDetails.getCityName());
				jsonObject.addProperty(InfinityConstants.state, membershipDetails.getState());
				jsonObject.addProperty(InfinityConstants.zipCode, membershipDetails.getZipCode());
				jsonObject.addProperty(InfinityConstants.country, membershipDetails.getCountry());
				array.add(jsonObject);
				logger.debug("******** adding address to input :"+array);
				inputParams.put(InfinityConstants.address, array.toString());
			}else {
				inputParams.put(InfinityConstants.address, address.toString());
			}
			
			if (StringUtils.isBlank(faxId)) {
				inputParams.put(InfinityConstants.faxId, "");
			}else {
				inputParams.put(InfinityConstants.faxId, faxId);
			}
			
			JsonArray contractCustomers = new JsonArray();

			for (int i = 0; i < cifListArray.size(); i++) {

				JsonObject customerObject = new JsonObject();
				customerObject = cifListArray.get(i).getAsJsonObject();
				logger.debug("******** each cif list :"+customerObject);
				JsonObject customer = new JsonObject();

				String coreCustomerId = customerObject.get("cif").getAsString();
				logger.debug("******** each cif coreCustomerId :"+coreCustomerId);

				if (coreCustomerId.equals(primaryCoreCustomerId)) {

					customer.addProperty("isPrimary", "true");
					customer.addProperty("coreCustomerId", coreCustomerId);
					customer.addProperty("coreCustomerName", name);
					customer.addProperty("isBusiness", membershipDetails.isBusinessType());
					customer.addProperty("sectorId", membershipDetails.getSectorId());
				} else {
					MembershipDTO eachMembershipDetails = coreCustomerBusinessDelegate
							.getMembershipDetails(coreCustomerId, legalEntityId, dcRequest.getHeaderMap());

					if (eachMembershipDetails != null) {

						String memberName = eachMembershipDetails.getFirstName() + " "
								+ eachMembershipDetails.getLastName();
						logger.debug("******** each cif member name :"+memberName);
						customer.addProperty("isPrimary", "false");
						customer.addProperty("coreCustomerId", coreCustomerId);
						customer.addProperty("coreCustomerName", memberName);
						customer.addProperty("isBusiness", eachMembershipDetails.isBusinessType());
						customer.addProperty("sectorId", eachMembershipDetails.getSectorId());
					}
				}

				JsonArray accounts = new JsonArray();
				accounts = getCoreCustomerAccounts(customerObject, inputParams, dcRequest);
				logger.debug("******** each cif accounts :"+accounts);
				customer.add("accounts", accounts);
				contractCustomers.add(customer);
				
			}
			logger.debug("******** adding contractCustomers :"+contractCustomers);
			inputParams.put(CONTRACTCUSTOMERS, contractCustomers.toString());
			
			//Adding accountLevelPermissions1
			addFeatureAndPermissionsToContractPayload(inputParams, dcRequest);

		} else {
			logger.debug("******** No memeber details found ************");
			throw new ApplicationException(ErrorCodeEnum.ERR_10269);
		}
	}
	
	@SuppressWarnings({ "unchecked" })
	private JsonArray getCoreCustomerAccounts(JsonObject customerObject, Map<String, String> map,
			DataControllerRequest dcRequest) throws ApplicationException {

		JsonArray inputAccountsArray = customerObject.get("accounts").getAsJsonArray();
		logger.debug("******** inputAccountsArray :"+inputAccountsArray);
		JsonArray accounts = new JsonArray();
		JsonArray accountsArray = new JsonArray();

		CoreCustomerBackendDelegate coreCustomerBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(CoreCustomerBackendDelegate.class);
		DBXResult accountsResult = new DBXResult();
		MembershipDTO membershipDTO = new MembershipDTO();
		membershipDTO.setId(customerObject.get("cif").getAsString());
		membershipDTO.setCompanyLegalUnit(map.get(LEGAL_ENTITY_ID));
		accountsResult = coreCustomerBackendDelegate.getCoreCustomerAccounts(membershipDTO, dcRequest.getHeaderMap());
		if (accountsResult != null && accountsResult.getResponse() != null) {

			List<AllAccountsViewDTO> accountsList = (List<AllAccountsViewDTO>) accountsResult.getResponse();
			String accountsString = "";
			try {
				accountsString = JSONUtils.stringifyCollectionWithTypeInfo(accountsList, AllAccountsViewDTO.class);
				logger.debug("******** account for corecustomer :"+accountsString);
			} catch (Exception e) {
				logger.error("Exception", e);
			}
			accountsArray = new JsonParser().parse(accountsString).getAsJsonArray();
		}

		// Adding only the accounts sent in the input
		for (int i = 0; i < accountsArray.size(); i++) {
			for (int j = 0; j < inputAccountsArray.size(); j++) {
				if (accountsArray.get(i).getAsJsonObject().get("accountId").getAsString()
						.equalsIgnoreCase(inputAccountsArray.get(j).getAsJsonObject().get("accountId").getAsString())) {
					accountsArray.get(i).getAsJsonObject().add("ownerType", accountsArray.get(i).getAsJsonObject().get("ownership"));
					accounts.add(accountsArray.get(i).getAsJsonObject());
					break;
				}
			}
		}
		logger.debug("******** account for corecustomer after filter :"+accounts);
		return accounts;
	}
	
	private void addFeatureAndPermissionsToContractPayload(Map<String, String> inputParams,
			DataControllerRequest request) throws ApplicationException, Exception {

		String legalEntityId = inputParams.get(LEGAL_ENTITY_ID);
		String serviceDefinitionId = inputParams.get(SERVICE_DEFINITION_ID);
		String cifListString = inputParams.get(CONTRACTCUSTOMERS);

		JsonArray contractCustomers = new JsonArray();
		try {
			contractCustomers = new JsonParser().parse(cifListString).getAsJsonArray();
		} catch (Exception e) {
			logger.debug("******** contractCustomers error");
			throw new ApplicationException(ErrorCodeEnum.ERR_10349, "Invalid contractCustomers input");
		}

		ContractBackendDelegate contractBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(ContractBackendDelegate.class);

		FeatureActionLimitsDTO coreCustomerFeatureActionDTO = contractBackendDelegate.getRestrictiveFeatureActionLimits(
				serviceDefinitionId, null, "", null, "", request.getHeaderMap(), false, "", legalEntityId);

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
								|| "true".equals(actionJson.get(InfinityConstants.isAccountLevel).getAsString()));

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
		JsonArray accountLevelPermissions = new JsonArray();
		JsonArray transactionLimits = new JsonArray();

		for (JsonElement customerElement : contractCustomers) {

			JsonObject customerObject = customerElement.getAsJsonObject();
			logger.debug("******** add Features and Permissions customerObject :"+customerObject);
			JsonArray accounts = customerObject.get("accounts").getAsJsonArray();
			String coreCustomerId = customerObject.get("coreCustomerId").getAsString();
			String coreCustomerName = customerObject.get("coreCustomerName").getAsString();

			JsonObject globalLevelpermission = new JsonObject();

			globalLevelpermission.addProperty(InfinityConstants.coreCustomerId, coreCustomerId);
			globalLevelpermission.addProperty(InfinityConstants.coreCustomerName, coreCustomerName);
			globalLevelpermission.add(InfinityConstants.features, globalLevelFeatures);
			globalLevelpermissions.add(globalLevelpermission);
			logger.debug("******** add Features and Permissions each customer globalLevelpermissions :"+globalLevelpermissions);

			JsonObject accountLevelPermission = new JsonObject();

			JsonArray limitAccounts = new JsonArray();

			for (JsonElement accountElement : accounts) {

				JsonObject account = new JsonObject();
				for (Entry<String, JsonElement> accountParams : accountElement.getAsJsonObject().entrySet()) {
					String key = accountParams.getKey();
					JsonElement value = accountParams.getValue();
					if (key.equalsIgnoreCase("accountId") || key.equalsIgnoreCase("productId")
							|| key.equalsIgnoreCase("accountName") || key.equalsIgnoreCase("accountType")
							|| key.equalsIgnoreCase("ownerType")) {

						if (key.equalsIgnoreCase("ownerType")) {
							account.add("ownershipType", value);
						} else {
							account.add(key, value);
						}
					}
				}
				account.addProperty(InfinityConstants.isEnabled, "true");
				account.add(InfinityConstants.featurePermissions, accountLevelFeatures);
				limitAccounts.add(account);
				logger.debug("******** add Features and Permissions each account  :"+account);
				logger.debug("******** add Features and Permissions each account limitAccounts :"+limitAccounts);
			}
			accountLevelPermission.addProperty(InfinityConstants.coreCustomerId, coreCustomerId);
			accountLevelPermission.addProperty(InfinityConstants.coreCustomerName, coreCustomerName);
			accountLevelPermission.add(InfinityConstants.accounts, limitAccounts);
			accountLevelPermission.add("featurepermissions", accountLevelFeatures);
			accountLevelPermissions.add(accountLevelPermission);
			logger.debug("******** add Features and Permissions each customer accountLevelPermissions :"+accountLevelPermissions);

			JsonArray features = new JsonArray();

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
			transactionLimit.addProperty(InfinityConstants.coreCustomerName, coreCustomerName);
			transactionLimit.add("featurePermissions", features);
			transactionLimits.add(transactionLimit);
			
			logger.debug("******** add Features and Permissions each customer transactionLimit :"+transactionLimit);

		}
		logger.debug("******** add Features and Permissions final accountLevelPermissions :"+accountLevelPermissions);
		logger.debug("******** add Features and Permissions final globalLevelpermissions :"+globalLevelpermissions);
		logger.debug("******** add Features and Permissions final transactionLimits :"+transactionLimits);
		
		inputParams.put("accountLevelPermissions1", accountLevelPermissions.toString());
		inputParams.put("globalLevelPermissions1", globalLevelpermissions.toString());
		inputParams.put("transactionLimits1", transactionLimits.toString());
		logger.debug("Contract map : " + inputParams);
	}

    

	@Override
	public Result editContract(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws ApplicationException {
		logger.debug("************ editContract ********");
		Result result = new Result();
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		try {
			String contractId = inputParams.get(CONTRACT_ID);
			String legalEntityId = inputParams.get(LEGAL_ENTITY_ID);
			
			
			if (StringUtils.isBlank(contractId)) {
				throw new ApplicationException(ErrorCodeEnum.ERR_10364);
			}
			if (StringUtils.isBlank(legalEntityId)){
            	throw new ApplicationException(ErrorCodeEnum.ERR_29040);
            }
			if (StringUtils.isBlank(inputParams.get("transactionLimits"))){
            	throw new ApplicationException(ErrorCodeEnum.ERR_29059);
            }
			
			JsonElement  limits = JsonParser.parseString(inputParams.get("transactionLimits"));
			if(!limits.isJsonArray() || !(limits.getAsJsonArray().size() > 0)) {
				throw new ApplicationException(ErrorCodeEnum.ERR_29059);
			}
			
			
            /**
             * Getting Contract communication, address, contract customers
             */
			
            ContractBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ContractBusinessDelegate.class);
			ContractDTO contractDTO = businessDelegate.getCompleteContractDetails(contractId, legalEntityId, dcRequest.getHeaderMap());
			Result contractDetails = new Result();
			if (null != contractDTO) {
				String contractString = JSONUtils.stringify(contractDTO);
				JsonParser parser = new JsonParser();
				JsonObject resultJson = formatContactDetailsRespnse(parser.parse(contractString).getAsJsonObject());
				contractDetails = JSONToResult.convert(HelperMethods.replaceCompanyLegalUnitWithLegalEntityId(resultJson.toString()));
				//logger.debug("************before contractDetails :"+ResultToJSON.convert(contractDetails));
				for (Record contractCustomer : contractDetails.getDatasetById("contractCustomers").getAllRecords()) {
					contractCustomer.removeDatasetById("customerAccounts");
					for (Record account : contractCustomer.getDatasetById("accounts").getAllRecords()) {
						account.addParam(new Param("accountStatus", account.getParamValueByName("statusDesc")));
					}
				}
			}
			
			//logger.debug("************after contractDetails :"+ResultToJSON.convert(contractDetails));
			
			/**
			 * Getting globalLevelPermissions, accountLevelPermissions, transactionLimits
			 */
			
			ContractCoreCustomersDTO dto = new ContractCoreCustomersDTO();
			dto.setContractId(contractId);
			dto.setCompanyLegalUnit(legalEntityId);
			DBXResult  response = businessDelegate.getContractFeatureActionLimitsAtProductLevel(dto, dcRequest.getHeaderMap());
			Result contractData = new Result();
			if (response != null && response.getResponse() != null) {
				contractData = ConvertJsonToResult.convert((JsonObject) response.getResponse());
				
			}
			
			//logger.debug("************ contractData :"+ResultToJSON.convert(contractData));
			
			String serviceDefinitionId = contractDetails.hasParamByName("servicedefinitionId")
					? contractDetails.getParamValueByName("servicedefinitionId")
					: "";
			String serviceDefinitionName = contractDetails.hasParamByName("servicedefinitionName")
					? contractDetails.getParamValueByName("servicedefinitionName")
					: "";
			String contractName = contractDetails.hasParamByName("name") ? contractDetails.getParamValueByName("name")
					: "";
			JSONArray communication = contractDetails.getDatasetById("communication") != null
					? ResultToJSON.convertDataset(contractDetails.getDatasetById("communication"))
					: new JSONArray();
			JSONArray address = contractDetails.getDatasetById("address") != null
					? ResultToJSON.convertDataset(contractDetails.getDatasetById("address"))
					: new JSONArray();
			JSONArray contractCustomers = contractDetails.getDatasetById("contractCustomers") != null
					? ResultToJSON.convertDataset(contractDetails.getDatasetById("contractCustomers"))
					: new JSONArray();
			JSONArray accountLevelPermissions = contractData.getDatasetById("accountLevelPermissions") != null
					? ResultToJSON.convertDataset(contractData.getDatasetById("accountLevelPermissions"))
					: new JSONArray();
			JSONArray globalLevelPermissions = contractData.getDatasetById("globalLevelPermissions") != null
					? ResultToJSON.convertDataset(contractData.getDatasetById("globalLevelPermissions"))
					: new JSONArray();
			
            if(jobInProgress(serviceDefinitionId, dcRequest.getHeaderMap())) {
            	throw new ApplicationException(ErrorCodeEnum.ERR_10365, "Features Job is in progress, please try after some time");
            }
            if (StringUtils.isBlank(contractName)) {
				throw new ApplicationException(ErrorCodeEnum.ERR_10364);
			}
            
            Set<String> changedFeatureLimits = new HashSet<String>();
            Map<String, Map<String, Map<String, String>>> transactionLimitsMap = new HashMap<String, Map<String,Map<String,String>>>();
            
            ContractBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(ContractBackendDelegate.class);
            try {
    			FeatureActionLimitsDTO coreCustomerFeatureActionDTO = backendDelegate.getRestrictiveFeatureActionLimits(
    					serviceDefinitionId, "", "", "", "", dcRequest.getHeaderMap(), true, "", legalEntityId);
    			transactionLimitsMap = coreCustomerFeatureActionDTO.getMonetaryActionLimits();
    		} catch (ApplicationException e) {
    			logger.error("Exception", e);
    		}
          
			JSONArray transactionLimits = getDefaultTransactionLimitsChanged(
					ResultToJSON.convertDataset(contractData.getDatasetById("transactionLimits")),
					inputParams.get("transactionLimits"), changedFeatureLimits, transactionLimitsMap);
			
			//logger.debug("************MigrateContractResourceImpl changedFeatureLimits :"+changedFeatureLimits);
			
			// check if nothing to update
			if(changedFeatureLimits == null || changedFeatureLimits.isEmpty() || !(changedFeatureLimits.size() > 0)) {
				//throw new ApplicationException(ErrorCodeEnum.ERR_12415,"No changes to update the limits");
				ErrorCodeEnum.ERR_12415.setErrorCode(result, "The actionId or feature limits are not applicable / changed for the customer");
				return result;
			}
			
			//logger.debug("******* MigrateContractResourceImpl final transactionLimits :"+transactionLimits);
			
			inputParams.put("contractName", contractName);
			inputParams.put("servicedefinitionId", serviceDefinitionId);
			inputParams.put("serviceDefinitionName", serviceDefinitionName);
			inputParams.put("faxId", "");
			inputParams.put("communication", communication.toString());
			inputParams.put("address", address.toString());
			inputParams.put("contractCustomers", contractCustomers.toString());
			inputParams.put("accountLevelPermissions1", accountLevelPermissions.toString());
			inputParams.put("globalLevelPermissions1", globalLevelPermissions.toString());
			inputParams.put("transactionLimits1", transactionLimits.toString());
			inputParams.put("deletedCustomers", "[]");
			
			inputParams.remove("transactionLimits");
			
			//logger.debug("******* MigrateContractResourceImpl inputParams :"+inputParams);
			ContractResource resource = DBPAPIAbstractFactoryImpl.getResource(ContractResource.class);
            result = resource.editContract(methodID, inputArray, dcRequest, dcResponse);
             
           
		} catch (ApplicationException e) {
			throw new ApplicationException(e.getErrorCodeEnum());
		} catch (Exception e) {
			logger.error("Error Occured",e);
			throw new ApplicationException(ErrorCodeEnum.ERR_10365);

		}
		return result;
	}
	

	private JSONArray getDefaultTransactionLimitsChanged(JSONArray defaultTransactionLimitsArray,
			String changeTransactionLimitsString, Set<String> changedFeatureLimits,
			Map<String, Map<String, Map<String, String>>> transactionLimitsMap) throws ApplicationException {

		//logger.debug("*********** defaultTransactionLimitsArray :"+defaultTransactionLimitsArray);
		//logger.debug("*********** changeTransactionLimitsString :"+changeTransactionLimitsString);
		
		JSONArray changeTransactionLimitsArray = new JSONArray(changeTransactionLimitsString);
		
		// For each core customer from input
		for(Object changeTransactionLimitsObj : changeTransactionLimitsArray) {
			JSONObject changeTransactionLimitJSON = (JSONObject) changeTransactionLimitsObj;
			String changeCoreCustomerId = changeTransactionLimitJSON.optString("cif","");
			
			//Check for coreCustomerId present in the input
			if(null != changeCoreCustomerId && !"".equals(changeCoreCustomerId)) {
				
				if(changeTransactionLimitJSON.has("featurePermissions")) {
				
					JSONArray changeFeaturePermissionsArray = changeTransactionLimitJSON.getJSONArray("featurePermissions");
					
					// For each customer
					for(Object defaultTransactionLimitObj : defaultTransactionLimitsArray) { 
						JSONObject defaultTransactionLimitJSON = (JSONObject) defaultTransactionLimitObj;
						String defaultCoreCustomerId =  defaultTransactionLimitJSON.optString("coreCustomerId","");
						//logger.debug("****** defaultCoreCustomerId :"+defaultCoreCustomerId);
						//logger.debug("****** changeCoreCustomerId :"+changeCoreCustomerId);
						// Match the customer and get the featurePermissions
						if(defaultCoreCustomerId.equals(changeCoreCustomerId)) { 
							//logger.debug("******** core customer id matched - defaultTransactionLimitJSON :"+defaultTransactionLimitJSON);
							JSONArray defaultFeaturePermissionsArray = defaultTransactionLimitJSON.getJSONArray("featurePermissions");
							//logger.debug("******** core customer id matched - defaultFeaturePermissionsArray :"+defaultFeaturePermissionsArray);
							defaultFeaturePermissionsArray = getDefaultLimitsUpdated(defaultFeaturePermissionsArray,
									changeFeaturePermissionsArray, changedFeatureLimits, transactionLimitsMap);
							defaultTransactionLimitJSON.remove("featurePermissions");
							defaultTransactionLimitJSON.put("featurePermissions", defaultFeaturePermissionsArray);
							break;
						}
					}
				}else {
					logger.error("************ featurePermissions not present in the request");
					throw new ApplicationException(ErrorCodeEnum.ERR_21108);
				}
			}else {
				logger.error("************ coreCustomerId not present in the request");
				throw new ApplicationException(ErrorCodeEnum.ERR_21025);
			}
		}
		
		//logger.debug("************MigrateContractResourceImpl defaultTransactionLimitsArray :"+defaultTransactionLimitsArray);
		return defaultTransactionLimitsArray;
	}

	private JSONArray getDefaultLimitsUpdated(JSONArray defaultFeaturePermissionsArray,
			JSONArray changeFeaturePermissionsArray, Set<String> changedFeatureLimits,
			Map<String, Map<String, Map<String, String>>> transactionLimitsMap) throws ApplicationException {

		// For each default featurePermissions object, get featureId and actionId
		for(Object defaultFeaturePermissionObj : defaultFeaturePermissionsArray) { 
			JSONObject defaultFeaturePermissionJSON = (JSONObject) defaultFeaturePermissionObj;
			String defaultFeatureId = defaultFeaturePermissionJSON.optString("featureId","");
			String defaultActionId = defaultFeaturePermissionJSON.optString("actionId","");
			// For each featurePermissions object from input, get featureId and actionId
			for(Object changeFeaturePermissionObj : changeFeaturePermissionsArray) { 
				JSONObject changeFeaturePermissionJSON = (JSONObject) changeFeaturePermissionObj;
				String changeFeatureId = changeFeaturePermissionJSON.optString("featureId","");
				String changeActionId = changeFeaturePermissionJSON.optString("actionId","");
				if(changeFeatureId != null && !"".equals(changeFeatureId) && changeActionId != null && !"".equals(changeActionId)) { 
					// Match the featureId and actionId
					if(defaultFeatureId.equalsIgnoreCase(changeFeatureId) && defaultActionId.equalsIgnoreCase(changeActionId)) {
						Map<String, String> limitMap = transactionLimitsMap.get(changeFeatureId).get(changeActionId);
						JSONArray changeLimits = changeFeaturePermissionJSON.getJSONArray("limits");
						JSONArray defaultLimits = defaultFeaturePermissionJSON.getJSONArray("limits");
						// Update the limit value for the respective featureId and actionId
						//logger.debug("******* Before the change - defaultLimits :" + defaultLimits);
						for (Object defaultLimit : defaultLimits) {
							JSONObject defaultLimitJSON = (JSONObject) defaultLimit;
							String defaultId = defaultLimitJSON.optString("id", "");
							String defaultValue = defaultLimitJSON.optString("value","0");
							for (Object changeLimit : changeLimits) {
								JSONObject changeLimitJSON = (JSONObject) changeLimit;
								String changeId = changeLimitJSON.optString("id", "");
								String changedValue = changeLimitJSON.optString("value", "0");
								if (defaultId.equalsIgnoreCase(changeId) && Double.parseDouble(defaultValue) != Double.parseDouble(changedValue)) {
									if (isValidLimits(changeId, changedValue, limitMap)) {
										defaultLimitJSON.remove("value");
										defaultLimitJSON.put("value", changedValue);
										changedFeatureLimits.add(changedValue);
									}else {
										logger.error("******** limits greater than serviceDef");
										throw new ApplicationException(ErrorCodeEnum.ERR_21108);
									}
									
									break;
								}
							}
						}
						//logger.debug("******* After the change - defaultLimits :" + defaultLimits);
						if(isValidFeatureLimits(defaultLimits)) {
							defaultFeaturePermissionJSON.remove("limits");
							defaultFeaturePermissionJSON.put("limits", defaultLimits);
						}else {
							logger.error("******** not valid limits");
							throw new ApplicationException(ErrorCodeEnum.ERR_21108);
						}
						break;
					}
				}else {
					logger.error("******** featureId or actionId is not present");
					throw new ApplicationException(ErrorCodeEnum.ERR_29028);
				}
			}
		}
		
		
		return defaultFeaturePermissionsArray;
	}
	
	private boolean isValidLimits(String limitId, String limitValue, Map<String, String> limitMap)
			throws ApplicationException {
		logger.debug("**** limitMap :"+limitMap);
		boolean isValidLimits = true;
		
		Double limit1 = new Double(0);
		Double limit2 = new Double(0);
		limit1 = Double.parseDouble(limitValue);

		if (limitId.equals(InfinityConstants.DAILY_LIMIT)) {
			limit2 = Double.parseDouble((limitMap.containsKey(InfinityConstants.DAILY_LIMIT)
					&& StringUtils.isNotBlank(limitMap.get(InfinityConstants.DAILY_LIMIT)))
							? limitMap.get(InfinityConstants.DAILY_LIMIT)
							: "0.0");
		} else if (limitId.equals(InfinityConstants.WEEKLY_LIMIT)) {
			limit2 = Double.parseDouble((limitMap.containsKey(InfinityConstants.WEEKLY_LIMIT)
					&& StringUtils.isNotBlank(limitMap.get(InfinityConstants.WEEKLY_LIMIT)))
							? limitMap.get(InfinityConstants.WEEKLY_LIMIT)
							: "0.0");
		} else if (limitId.equals(InfinityConstants.MAX_TRANSACTION_LIMIT)) {
			limit2 = Double.parseDouble((limitMap.containsKey(InfinityConstants.MAX_TRANSACTION_LIMIT)
					&& StringUtils.isNotBlank(limitMap.get(InfinityConstants.MAX_TRANSACTION_LIMIT)))
							? limitMap.get(InfinityConstants.MAX_TRANSACTION_LIMIT)
							: "0.0");
		}

		if (limit1 > limit2) {
			isValidLimits = false;
		}
		
		logger.debug("********** isValidLimits :"+isValidLimits);
		return isValidLimits;
	}
	
	private boolean isValidFeatureLimits(JSONArray featureLimits) {
		
		boolean isValidLimits = true;

		Double weeklyLimit = 0.0;
		Double maxLimit = 0.0;
		Double dailyLimit = 0.0;
		Double minLimit = 0.0;
		
		for (Object featureLimit : featureLimits) {
			JSONObject featureLimitJSON = (JSONObject) featureLimit;
			String limitId = featureLimitJSON.optString("id", "");
			String limitValue = featureLimitJSON.optString("value","0");
			
			if ("WEEKLY_LIMIT".equalsIgnoreCase(limitId))
				weeklyLimit = Double.valueOf(limitValue);

			if ("MAX_TRANSACTION_LIMIT".equalsIgnoreCase(limitId))
				maxLimit = Double.valueOf(limitValue);

			if ("DAILY_LIMIT".equalsIgnoreCase(limitId))
				dailyLimit = Double.valueOf(limitValue);

			if ("MIN_TRANSACTION_LIMIT".equalsIgnoreCase(limitId))
				minLimit = Double.valueOf(limitValue);
		}
		
		/**
		 * MIN_TRANSACTION_LIMIT <= MAX_TRANSACTION_LIMIT <= DAILY_LIMIT <= WEEKLY_LIMIT
		 **/
		if (minLimit > maxLimit || maxLimit > dailyLimit || dailyLimit > weeklyLimit) {
			isValidLimits = false;
		}
		return isValidLimits;
	}

	private boolean jobInProgress(String serviceDefinitionId, Map<String, Object> headerMap) {
    	Map<String, Object> inputParams = new HashMap<String, Object>();
		String filter = InfinityConstants.serviceDefinitionId + DBPUtilitiesConstants.EQUAL + serviceDefinitionId+
				DBPUtilitiesConstants.AND + InfinityConstants.status + DBPUtilitiesConstants.EQUAL + InfinityConstants.jobStatus.SID_JOB_INPROGRESS;
		inputParams.put(DBPUtilitiesConstants.FILTER, filter);
		JsonObject infinityJobJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
				URLConstants.INIFNITY_JOB_GET);
		if (null != infinityJobJson
				&& JSONUtil.hasKey(infinityJobJson, DBPDatasetConstants.DATASET_INFINITY_JOB)
				&& infinityJobJson.get(DBPDatasetConstants.DATASET_INFINITY_JOB).isJsonArray()) {
			return infinityJobJson.get(DBPDatasetConstants.DATASET_INFINITY_JOB).getAsJsonArray().size() > 0;
		}
		
		return false;
	}
    
    
    private JsonObject formatContactDetailsRespnse(JsonObject contractJson) {
		JsonArray resultCommunicationArray = new JsonArray();
		if (contractJson.has(COMMUNICATION) && contractJson.get(COMMUNICATION).isJsonArray()) {
			JsonArray commArray = contractJson.get(COMMUNICATION).getAsJsonArray();
			JsonObject resultCommJson = new JsonObject();
			for (JsonElement element : commArray) {
				JsonObject communicationJson = element.isJsonObject() ? element.getAsJsonObject() : new JsonObject();
				if (JSONUtil.isJsonNotNull(communicationJson) && JSONUtil.hasKey(communicationJson, TYPEID) && JSONUtil
						.getString(communicationJson, TYPEID).equals(DBPUtilitiesConstants.COMM_TYPE_PHONE)) {
					resultCommJson.addProperty(PHONE_COUNTRY_CODE,
							JSONUtil.getString(communicationJson, PHONE_COUNTRY_CODE));
					resultCommJson.addProperty(PHONE_NUMBER, JSONUtil.getString(communicationJson, COMM_VALUE));
				} else if (JSONUtil.isJsonNotNull(communicationJson) && JSONUtil.hasKey(communicationJson, TYPEID)
						&& JSONUtil.getString(communicationJson, TYPEID)
								.equals(DBPUtilitiesConstants.COMM_TYPE_EMAIL)) {
					resultCommJson.addProperty(EMAIL, JSONUtil.getString(communicationJson, COMM_VALUE));
				}

			}
			contractJson.remove(COMMUNICATION);
			resultCommunicationArray.add(resultCommJson);
			contractJson.add(COMMUNICATION, resultCommunicationArray);
		}
		
		return contractJson;
	}
	
}
