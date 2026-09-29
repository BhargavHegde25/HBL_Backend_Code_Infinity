package com.hbl.resource.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.hbl.resource.constants.HBLConstants;
import com.infinity.dbx.dbp.jwt.auth.AuthConstants;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.ConvertJsonToResult;
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
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.kony.eum.dbputilities.util.UnsubscribeAlertsForRemovedAccounts;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.mysql.cj.protocol.a.NativeConstants.StringLengthDataType;
import com.temenos.dbx.eum.product.contract.businessdelegate.api.ContractAccountsBusinessDelegate;
import com.temenos.dbx.eum.product.contract.businessdelegate.api.ContractAddressBusinessDelegate;
import com.temenos.dbx.eum.product.contract.businessdelegate.api.ContractBusinessDelegate;
import com.temenos.dbx.eum.product.contract.businessdelegate.api.ContractCommunicationBusinessDelegate;
import com.temenos.dbx.eum.product.contract.businessdelegate.api.ContractCoreCustomerBusinessDelegate;
import com.temenos.dbx.eum.product.contract.businessdelegate.api.ContractFeatureActionsBusinessDelegate;
import com.temenos.dbx.eum.product.contract.businessdelegate.api.ExcludedContractAccountsBusinessDelegate;
import com.temenos.dbx.eum.product.contract.businessdelegate.api.ServiceDefinitionBusinessDelegate;
import com.temenos.dbx.eum.product.contract.resource.impl.ContractResourceImpl;
import com.temenos.dbx.eum.product.usermanagement.backenddelegate.api.BackendIdentifiersBackendDelegate;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.BackendIdentifierBusinessDelegate;
import com.temenos.dbx.product.accounts.businessdelegate.api.AccountsBusinessDelegate;
import com.temenos.dbx.product.approvalmatrixservices.businessdelegate.api.ApprovalMatrixBusinessDelegate;
import com.temenos.dbx.product.businessdelegate.api.AddressBusinessDelegate;
import com.temenos.dbx.product.businessdelegate.api.BusinessConfigurationBusinessDelegate;
import com.temenos.dbx.product.dto.AccountsDTO;
import com.temenos.dbx.product.dto.AddressDTO;
import com.temenos.dbx.product.dto.BackendIdentifierDTO;
import com.temenos.dbx.product.dto.ContractAccountsDTO;
import com.temenos.dbx.product.dto.ContractActionLimitsDTO;
import com.temenos.dbx.product.dto.ContractAddressDTO;
import com.temenos.dbx.product.dto.ContractCommunicationDTO;
import com.temenos.dbx.product.dto.ContractCoreCustomersDTO;
import com.temenos.dbx.product.dto.ContractDTO;
import com.temenos.dbx.product.dto.ContractDTOExtn;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.dto.ExcludedContractAccountDTO;
import com.temenos.dbx.product.dto.FeatureActionLimitsDTO;
import com.temenos.dbx.product.dto.ServiceDefinitionDTO;
import com.temenos.dbx.product.utils.DTOConstants;
import com.temenos.dbx.product.utils.DTOUtils;
import com.temenos.dbx.product.utils.InfinityConstants;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import java.util.UUID;

public class ContractResourceImplExtn extends ContractResourceImpl{
	
	SimpleDateFormat idFormatter = new SimpleDateFormat("yyMMddHHmmssSSS");

	private static final String CONTRACT_ID = "contractId";
	private static final String CONTRACT_NAME = "contractName";
	private static final String SERVICE_DEFINITION_ID = "serviceDefinitionId";
	private static final String FAX_ID = "faxId";
	private static final String COMMUNICATION = "communication";
	private static final String PHONE_NUMBER = "phoneNumber";
	private static final String PHONE_COUNTRY_CODE = "phoneCountryCode";
	private static final String EMAIL = "email";
	private static final String ADDRESS = "address";
	private static final String CONTRACTCUSTOMERS = "contractCustomers";
	private static final String DELETEDCUSTOMERS = "deletedCustomers";
	private static final String ISPRIMARY = "isPrimary";
	private static final String ISBUSINESS = "isBusiness";
	private static final String SECTORID = "sectorId";
	private static final String CORECUSTOMERID = "coreCustomerId";
	private static final String CORECUSTOMER_NAME = "coreCustomerName";
	private static final String ACCOUNTS = "accounts";
	private static final String ACCOUNTID = "accountId";
	private static final String TYPEID = "typeId";
	private static final String ACCOUNTNAME = "accountName";
	private static final String FEATURES = "features";
	private static final String FEATUREID = "featureId";
	private static final String ACTIONS = "actions";
	private static final String ACTIONID = "actionId";
	private static final String ISALLOWED = "isAllowed";
	private static final String ISNEWACTION = "isNewAction";
	private static final String LIMITS = "limits";
	private static final String LIMITID = "id";
	private static final String LIMITVALUE = "value";
	private static final String COUNTRY = "country";
	private static final String OWNERTYPE = "ownerType";
	private static final String COMM_VALUE = "value";
	private static final String STATUSID = "statusId";
	private static final String REJECTEDREASON = "rejectedReason";
	private static final String REJECTEDBY = "rejectedby";
	private static final String ISDEFAULTACTIONSENABLED = "isDefaultActionsEnabled";
	private static final String ARRANGEMENTID = "arrangementId";
	private static final String ACCOUNTHOLDERNAME = "accountHolderName";
	private static final String ACCOUNTSTATUS = "accountStatus";
	private static final String ACCOUNTTYPE = "accountType";
	public static final String IMPLICIT_ACCOUNT_ACCESS = "implicitAccountAccess";
	private static final String EXCLUDED_ACCOUNTS = "excludedAccounts";
	private static final String DAILY_LIMIT = "DAILY_LIMIT";
    private static final String MAX_TRANSACTION_LIMIT = "MAX_TRANSACTION_LIMIT";
    private static final String WEEKLY_LIMIT = "WEEKLY_LIMIT";
    
    private static final String MB_DAILY_LIMIT = "MB_DAILY_LIMIT";
    private static final String MB_MAX_TRANSACTION_LIMIT = "MB_MAX_TRANSACTION_LIMIT";
    private static final String MB_WEEKLY_LIMIT = "MB_WEEKLY_LIMIT";
    private static final String ROLE_ID = "roleId";
    private static final String PRODUCTSLIST = "productsList";
    private static final String PRODUCTID = "productId";
    private static final String featurePermissions = "featurePermissions";
    private static final String permissions = "permissions";
    private static final String LEGAL_ENTITY_ID = "legalEntityId";
    private static final String CONTRACT_ID_DB = "id";
    private static final String CONTRACT_NAME_DB = "name";
	LoggerUtil logger = new LoggerUtil(ContractResourceImplExtn.class);
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	@Override
	public Result updateContractStatus(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws ApplicationException {
		String userId = null;
		Result result = new Result();
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		HelperMethods.removeNullValues(inputParams);
		List<String> allowedStatus = new ArrayList<>();
		allowedStatus.add(DBPUtilitiesConstants.CONTRACT_STATUS_ACTIVE);
		allowedStatus.add(DBPUtilitiesConstants.CONTRACT_STATUS_REJECTED);

		String statusId = StringUtils.isNotBlank(inputParams.get(STATUSID)) ? inputParams.get(STATUSID)
				: dcRequest.getParameter(STATUSID);
		String contractId = StringUtils.isNotBlank(inputParams.get(CONTRACT_ID)) ? inputParams.get(CONTRACT_ID)
				: dcRequest.getParameter(CONTRACT_ID);
		JsonArray array = null;
		if (StringUtils.isBlank(statusId) || StringUtils.isBlank(contractId) || !allowedStatus.contains(statusId)) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10382);
		}

		ContractBusinessDelegate contractBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(ContractBusinessDelegate.class);
		ContractDTO inputDTO = new ContractDTO();
		ContractDTO outputDTO = new ContractDTO();
		inputDTO.setId(contractId);
		boolean isUpdateSuccess = false;
		inputDTO.setStatusId(statusId);
		try {

			outputDTO = contractBusinessDelegate.getContractDetails(inputDTO, dcRequest.getHeaderMap());
		} catch (ApplicationException e) {
			logger.error("Exception occured while updating contract status");
			throw new ApplicationException(e.getErrorCodeEnum());
		} catch (Exception e) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10381);
		}

		/*if (StringUtils.isNotBlank(outputDTO.getStatusId())
				&& DBPUtilitiesConstants.CONTRACT_STATUS_ACTIVE.equalsIgnoreCase(outputDTO.getStatusId())) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10383);
		}
		*/
		if (DBPUtilitiesConstants.CONTRACT_STATUS_REJECTED.equalsIgnoreCase(statusId)) {
			inputDTO.setRejectedReason(dcRequest.getParameter(REJECTEDREASON));
			inputDTO.setRejectedby(dcRequest.getParameter(REJECTEDBY));
			inputDTO.setRejectedts(HelperMethods.getCurrentTimeStamp());
			inputDTO.setDescription("SID_CONTRACT_REJECTED");
		}
		 array= getBackendDetails(contractId, dcRequest);
		if (DBPUtilitiesConstants.CONTRACT_STATUS_ACTIVE.equalsIgnoreCase(statusId)) {
			inputDTO.setDescription("SID_CONTRACT_MANUAL_APPROVED");
			inputDTO.setStatusId(DBPUtilitiesConstants.CONTRACT_STATUS_ACTIVE);
			String loggedInUser = null; 
			  try { 
				  Map<String, String> userProfile =HelperMethods.getCustomerFromIdentityService(dcRequest); 
			  loggedInUser =userProfile.get("UserName"); 
			  } catch (Exception e) {
			  logger.error("BCT::ContractResourceImplExtn::getCustomerFromIdentityService: exception:" +e.toString()); 
			  } 
        	inputDTO.setRejectedby(loggedInUser);
			inputDTO.setRejectedts(HelperMethods.getCurrentTimeStamp());
			Map<String, Object> inputMap = HelperMethods.getInputParamObjectMap(inputArray);
			/* BackendIdentifierDTO backendIdentifierDTO = getBackendIdFromContract(contractId, dcRequest);
			 String customerId=backendIdentifierDTO.getCustomer_id();
			 String backendId= backendIdentifierDTO.getBackendId();
			 */
			 String backendId="";
			 String isUserActive="";
			 if (JSONUtil.isJsonNotNull(array) && array.size() > 0) {
				JsonObject object = array.get(0).getAsJsonObject();
				backendId = object.has("primaryCoreCustomerId") ? object.get("primaryCoreCustomerId").getAsString() : "";
				isUserActive=object.has("statusId") ? object.get("statusId").getAsString() : "";
				if(isUserActive.equalsIgnoreCase("SID_CUS_ACTIVE")) {
					/*String isMobileBankingService=inputMap.get("isMobileBankingService")!=null? inputMap.get("isMobileBankingService").toString():"";
					String isInternetBankingService=inputMap.get("isInternetBankingService")!=null? inputMap.get("isInternetBankingService").toString():"";
					*/
					inputMap.put("isInternetBankingService", "YES");
					inputMap.put("isMobileBankingService", "YES");
					inputMap.put("infinityAccess",dcRequest.getParameter("infinityAccess"));
                    inputDTO.setInfinityAccess(dcRequest.getParameter("infinityAccess"));
					dcRequest.addRequestParam_("isUserActive", isUserActive);
				 }
			 }
			inputMap.put("customerId", backendId);
			logger.debug("BCT::ContractResourceImplExtn::InputMap for updateEnrollConent:" +inputMap); 
			logger.debug("BCT::ContractResourceImplExtn::inputDTO for updateEnrollConent:" +inputDTO); 
			JsonObject consentStatus = updateEnrollConent(inputMap, dcRequest);
			isUpdateSuccess=consentStatus.get("isSuccess").getAsBoolean();
			if(!isUpdateSuccess) {
				String errMsg= consentStatus.get("errmsg")!=null?consentStatus.get("errmsg").getAsString():consentStatus.getAsString();
				result.addParam(new Param("dbpErrCode", ErrorCodeEnum.ERR_10406.getErrorCodeAsString()));
				result.addParam(new Param("dbpErrMsg", errMsg));
				result.addParam(new Param("opstatus", "0"));
				return result;
			}
		}
		try {
			isUpdateSuccess = contractBusinessDelegate.updateContractStatus(inputDTO,
					dcRequest.getHeaderMap());
		} catch (ApplicationException e) {
			logger.error("Exception occured while updating contract status");
			throw new ApplicationException(e.getErrorCodeEnum());
		} catch (Exception e) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10381);
		}
		result.addStringParam(DBPUtilitiesConstants.IS_UPDATE_SUCEES, String.valueOf(isUpdateSuccess));
		dcRequest.addRequestParam_(DBPUtilitiesConstants.IS_UPDATE_SUCEES, String.valueOf(isUpdateSuccess));

		if (isUpdateSuccess && (DBPUtilitiesConstants.CONTRACT_STATUS_ACTIVE.equalsIgnoreCase(statusId) || DBPUtilitiesConstants.CONTRACT_STATUS_REJECTED.equalsIgnoreCase(statusId))) {
			//JsonArray array = contractBusinessDelegate.getContractInfinityUsers(contractId, dcRequest.getHeaderMap());

			if (JSONUtil.isJsonNotNull(array) && array.size() > 0) {
				JsonObject object = array.get(0).getAsJsonObject();
				userId = object.has("customerId") ? object.get("customerId").getAsString() : "";

			}

			if (StringUtils.isNotBlank(userId)) {

				// These details are being used in orchestration service.
				dcRequest.addRequestParam_("userId", userId);
				dcRequest.addRequestParam_(CONTRACT_ID, contractId);

			}
		}

		dcRequest.addRequestParam_("contractStatus", statusId);
		return result;
	}
	public JsonArray getBackendDetails(String contractId, DataControllerRequest dcRequest) {
		ContractBusinessDelegate contractBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(ContractBusinessDelegate.class);
		JsonArray array =null;
		JsonObject object = null;
		try {
			 array = contractBusinessDelegate.getContractInfinityUsers(contractId, dcRequest.getHeaderMap());

		}catch (ApplicationException e) {
			logger.error("Exception Occured",e);
		}
		return array;
	}
	public Result editContract(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws ApplicationException {
		Result result = new Result();
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		String contractId;
		Set<String> addedCustomersList = new HashSet<>();
		Set<String> removedCustomersList = new HashSet<>();
		Set<String> existingCustomersList = new HashSet<>();
		String existingPrimaryCoreCustomerId = null;
		StringBuilder currentPrimaryCoreCustomerId = new StringBuilder();
		List<String> customerAccountsPayloadsList  = new ArrayList<String>();
		HashMap<String, String> customerAccountMap = new HashMap<>();
		try {
			String contractName = inputParams.get(CONTRACT_NAME);
			String communication = inputParams.get(COMMUNICATION);
			String address = inputParams.get(ADDRESS);
			String contractCustomers = inputParams.get(CONTRACTCUSTOMERS);
			String faxId = inputParams.get(FAX_ID);
			contractId = inputParams.get(CONTRACT_ID);
			String deletedCustomers = inputParams.get(DELETEDCUSTOMERS);
            String accountLevelPermissions = inputParams.get("accountLevelPermissions1");
            String globalLevelPermissions = inputParams.get("globalLevelPermissions1");
            String transactionLimits = inputParams.get("transactionLimits1");
            String legalEntityId = inputParams.get(LEGAL_ENTITY_ID);
            JsonArray accountLevelPermissionsJsonArray = new JsonArray();
            JsonArray globalLevelPermissionsJsonArray = new JsonArray();
            JsonArray transactionLimitsJsonArray = new JsonArray();



                if (StringUtils.isBlank(accountLevelPermissions) || StringUtils.isBlank(globalLevelPermissions)
                        || StringUtils.isBlank(transactionLimits))
                    throw new ApplicationException(ErrorCodeEnum.ERR_10348);
                else {
                    accountLevelPermissionsJsonArray = JSONUtil.parseAsJsonArray(accountLevelPermissions);
                    globalLevelPermissionsJsonArray = JSONUtil.parseAsJsonArray(globalLevelPermissions);
                    transactionLimitsJsonArray = JSONUtil.parseAsJsonArray(transactionLimits);
                }
            
			if (StringUtils.isBlank(contractName) || StringUtils.isBlank(contractId)
					|| StringUtils.isBlank(contractCustomers)) {
				throw new ApplicationException(ErrorCodeEnum.ERR_10364);

			}
			
			if (StringUtils.isBlank(legalEntityId)){
            	throw new ApplicationException(ErrorCodeEnum.ERR_29040);
            }
			
			String serviceDefinitionId = getServiceDefinitionID(contractId, dcRequest);
			String serviceDefinitionType = getServiceType(serviceDefinitionId, dcRequest);

			if (StringUtils.isBlank(serviceDefinitionType)) {
				throw new ApplicationException(ErrorCodeEnum.ERR_10365);
			}
            if(jobInProgress(serviceDefinitionId, dcRequest.getHeaderMap())) {
            	throw new ApplicationException(ErrorCodeEnum.ERR_10365, "Features Job is in progress, please try after some time");
            }

			boolean isNotExists = verifyContractName(contractId, contractName, dcRequest);

			if (!isNotExists) {
				throw new ApplicationException(ErrorCodeEnum.ERR_10401);
			}

			checkIfPayloadContainsValidAccountsForAllCustomers(contractCustomers, contractId, dcRequest);

			existingPrimaryCoreCustomerId = getExistingPrimaryCoreCustomerId(contractId, dcRequest);
			if (StringUtils.isBlank(existingPrimaryCoreCustomerId)) {
				throw new ApplicationException(ErrorCodeEnum.ERR_10400);
			}

			segregateGivenCustomersListAndUpdateCoreCustomerId(contractCustomers, contractId, addedCustomersList,
					removedCustomersList, existingCustomersList, currentPrimaryCoreCustomerId, dcRequest, deletedCustomers);

			if (currentPrimaryCoreCustomerId.length() > 0 && 
					!existingPrimaryCoreCustomerId.equalsIgnoreCase(currentPrimaryCoreCustomerId.toString())) {
				updateCoreCustomerDetails(existingPrimaryCoreCustomerId, contractId, false, legalEntityId, dcRequest);
			}

			updateContractDetails(contractId, contractName, faxId, legalEntityId, dcRequest);

			/* it should add permssions only for new customers but now it is creating for all*/
			addNewCustomers(contractCustomers, addedCustomersList, contractId, serviceDefinitionType,globalLevelPermissionsJsonArray, accountLevelPermissionsJsonArray, transactionLimitsJsonArray, legalEntityId, dcRequest);

			deleteRemovedCustomers(contractId, removedCustomersList, dcRequest);
			if(!isValidCustomerLimits(transactionLimitsJsonArray, existingCustomersList, contractId, serviceDefinitionType,
                    dcRequest)) {
            	throw new ApplicationException(ErrorCodeEnum.ERR_10420);
            }

			updateExistingCutomers(contractCustomers, existingCustomersList, contractId, serviceDefinitionType, globalLevelPermissionsJsonArray, accountLevelPermissionsJsonArray, transactionLimitsJsonArray, legalEntityId, 
					dcRequest, customerAccountsPayloadsList, customerAccountMap);

			if (!existingPrimaryCoreCustomerId.equalsIgnoreCase(currentPrimaryCoreCustomerId.toString())) {
				updateCoreCustomerDetails(currentPrimaryCoreCustomerId.toString(), contractId, true, legalEntityId, dcRequest);
			}

			deleteContractCommunicationAddress(contractId, dcRequest);
			List<AddressDTO> addressList = DTOUtils.getDTOList(address, AddressDTO.class);
			createContractCommunication(communication, contractId, legalEntityId, dcRequest);
			createContractAddress(addressList, contractId, legalEntityId, dcRequest);
			createExcludedContractAccounts(contractCustomers, contractId, legalEntityId, dcRequest);
			UnsubscribeAlertsForRemovedAccounts.asyncUnsubscribeAlerts(customerAccountMap);

			result.addParam(new Param(CONTRACT_ID, contractId, DBPUtilitiesConstants.STRING_TYPE));
			result.addParam(new Param("status", "success", DBPUtilitiesConstants.STRING_TYPE));

		} catch (ApplicationException e) {
			throw new ApplicationException(e.getErrorCodeEnum());
		} catch (Exception e) {
			logger.error("Error Occured",e);
			throw new ApplicationException(ErrorCodeEnum.ERR_10365);

		}
		return result;
	}
	private void createExcludedContractAccounts(String contractCustomers, String contractId,
    		String legalEntityId, DataControllerRequest dcRequest) throws ApplicationException {
        
        JsonArray contractCustomersArray = JSONUtil.parseAsJsonArray(contractCustomers);

        ExcludedContractAccountsBusinessDelegate contractAccountsBD = DBPAPIAbstractFactoryImpl
                .getBusinessDelegate(ExcludedContractAccountsBusinessDelegate.class);

        for (JsonElement customerElement : contractCustomersArray) {
            JsonObject customerJson = customerElement.isJsonObject() ? customerElement.getAsJsonObject()
                    : new JsonObject();
            if (JSONUtil.hasKey(customerJson, CORECUSTOMERID) && JSONUtil.hasKey(customerJson, EXCLUDED_ACCOUNTS)
                    && customerJson.get(EXCLUDED_ACCOUNTS).isJsonArray()) {
                String coreCustomerId = JSONUtil.getString(customerJson, CORECUSTOMERID);
                JsonArray accountsArray = customerJson.get(EXCLUDED_ACCOUNTS).getAsJsonArray();

                for (JsonElement accountElement : accountsArray) {
                    JsonObject accountObject = accountElement.isJsonObject() ? accountElement.getAsJsonObject()
                            : new JsonObject();
                    String accountId = JSONUtil.hasKey(accountObject, ACCOUNTID)
                            ? accountObject.get(ACCOUNTID).getAsString()
                            : "";
                    String accountName = JSONUtil.hasKey(accountObject, ACCOUNTNAME)
                            ? accountObject.get(ACCOUNTNAME).getAsString()
                            : "";
                    String typeId = JSONUtil.hasKey(accountObject, TYPEID) ? accountObject.get(TYPEID).getAsString()
                            : "";
                    String ownerType = JSONUtil.hasKey(accountObject, OWNERTYPE)
                            ? accountObject.get(OWNERTYPE).getAsString()
                            : "";
                    String arrangementId = JSONUtil.hasKey(accountObject, ARRANGEMENTID)
                            ? accountObject.get(ARRANGEMENTID).getAsString()
                            : "";
                    String accountStatus = JSONUtil.hasKey(accountObject, ACCOUNTSTATUS)
                            ? accountObject.get(ACCOUNTSTATUS).getAsString()
                            : DBPUtilitiesConstants.DEFAULT_ACCOUNT_STATUS;
                    String accountType = JSONUtil.hasKey(accountObject, ACCOUNTTYPE)
                            ? accountObject.get(ACCOUNTTYPE).getAsString()
                            : "";           

                    if (StringUtils.isNotBlank(accountId)) {

                        ExcludedContractAccountDTO contractAccountDTO = new ExcludedContractAccountDTO();
                        contractAccountDTO.setAccountId(accountId);
                        contractAccountDTO.setAccountName(accountName);
                        contractAccountDTO.setContractId(contractId);
                        contractAccountDTO.setCoreCustomerId(coreCustomerId);
                        contractAccountDTO.setId(idFormatter.format(new Date()));
                        contractAccountDTO.setTypeId(typeId);
                        contractAccountDTO.setOwnerType(ownerType);
                        contractAccountDTO.setArrangementId(arrangementId);
                        contractAccountDTO.setStatusDesc(accountStatus);
                        contractAccountDTO.setAccountType(accountType);
                        contractAccountDTO.setCompanyLegalUnit(legalEntityId);
                        ExcludedContractAccountDTO createdAcontractAccountDTO = contractAccountsBD
                                .getExcludedContractAccount(contractAccountDTO, dcRequest.getHeaderMap());

                        if (createdAcontractAccountDTO == null
                                || StringUtils.isBlank(createdAcontractAccountDTO.getId())) {

                            contractAccountDTO = contractAccountsBD.createExcludedContractAccount(contractAccountDTO,
                                    dcRequest.getHeaderMap());
                            if (null == contractAccountDTO || StringUtils.isBlank(contractAccountDTO.getId())) {
                            	logger.debug("ExcludedContractaccount creation failed for accountId "+accountId);
                                throw new ApplicationException(ErrorCodeEnum.ERR_10413);
                            }
                        }

                    }

                }
            }

        }

    }
	 private void createContractAddress(List<AddressDTO> addressList, String contractId, String legalEntityId, DataControllerRequest dcRequest)
	            throws ApplicationException {
	        AddressBusinessDelegate addressBD = DBPAPIAbstractFactoryImpl.getInstance()
	                .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(AddressBusinessDelegate.class);

	        ContractAddressBusinessDelegate contractAddressBD = DBPAPIAbstractFactoryImpl
	                .getBusinessDelegate(ContractAddressBusinessDelegate.class);

	        for (AddressDTO inputDTO : addressList) {
	            inputDTO.setId(UUID.randomUUID().toString());
	            inputDTO.setCompanyLegalUnit(legalEntityId);
	            AddressDTO resultDTO = addressBD.createAddress(inputDTO, dcRequest.getHeaderMap());
	            if (null == resultDTO || StringUtils.isBlank(resultDTO.getId())) {
	                throw new ApplicationException(ErrorCodeEnum.ERR_10354);
	            }

	            ContractAddressDTO contractAddressDTO = new ContractAddressDTO();
	            contractAddressDTO.setId(UUID.randomUUID().toString());
	            contractAddressDTO.setContractId(contractId);
	            contractAddressDTO.setAddressId(resultDTO.getId());
	            contractAddressDTO.setCompanyLegalUnit(legalEntityId);

	            contractAddressBD.createContractAddress(contractAddressDTO, dcRequest.getHeaderMap());
	        }

	    }
	private void createContractCommunication(String communication, String contractId, String legalEntityId, DataControllerRequest dcRequest)
            throws ApplicationException {

        if (StringUtils.isBlank(communication)) {
            return;
        }

        ContractCommunicationBusinessDelegate contractCommunicationBD = DBPAPIAbstractFactoryImpl
                .getBusinessDelegate(ContractCommunicationBusinessDelegate.class);
        ContractCommunicationDTO contractCommunicationDTO = null;
        JsonParser parser = new JsonParser();
        JsonArray communicationArray = parser.parse(communication).getAsJsonArray();
        for (JsonElement communicationElement : communicationArray) {
            if (JSONUtil.isJsonNotNull(communicationElement) && communicationElement.isJsonObject()) {
                JsonObject communicationJson = communicationElement.getAsJsonObject();
                if (JSONUtil.hasKey(communicationJson, PHONE_NUMBER)
                        && JSONUtil.hasKey(communicationJson, PHONE_COUNTRY_CODE)) {
                    contractCommunicationDTO = new ContractCommunicationDTO();
                    contractCommunicationDTO.setContractId(contractId);
                    contractCommunicationDTO
                            .setPhoneCountryCode(communicationJson.get(PHONE_COUNTRY_CODE).getAsString());
                    contractCommunicationDTO.setValue(communicationJson.get(PHONE_NUMBER).getAsString());
                    contractCommunicationDTO.setId(UUID.randomUUID().toString());
                    contractCommunicationDTO.setIsPreferredContactMethod(DBPUtilitiesConstants.BOOLEAN_STRING_TRUE);
                    contractCommunicationDTO.setTypeId(DBPUtilitiesConstants.COMM_TYPE_PHONE);
                    contractCommunicationDTO.setCompanyLegalUnit(legalEntityId);
                    contractCommunicationBD.createContractCommunication(contractCommunicationDTO,
                            dcRequest.getHeaderMap());

                }
                if (JSONUtil.hasKey(communicationJson, EMAIL)) {
                    contractCommunicationDTO = new ContractCommunicationDTO();
                    contractCommunicationDTO.setContractId(contractId);
                    contractCommunicationDTO.setValue(communicationJson.get(EMAIL).getAsString());
                    contractCommunicationDTO.setId(UUID.randomUUID().toString());
                    contractCommunicationDTO.setIsPreferredContactMethod(DBPUtilitiesConstants.BOOLEAN_STRING_TRUE);
                    contractCommunicationDTO.setTypeId(DBPUtilitiesConstants.COMM_TYPE_EMAIL);
                    contractCommunicationDTO.setCompanyLegalUnit(legalEntityId);
                    contractCommunicationBD.createContractCommunication(contractCommunicationDTO,
                            dcRequest.getHeaderMap());
                }

            }

        }
    }
	private void deleteContractCommunicationAddress(String contractId, DataControllerRequest dcRequest) {
		ContractCommunicationBusinessDelegate communicationBD = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(ContractCommunicationBusinessDelegate.class);
		communicationBD.deleteContractCommunicationAddress(contractId, dcRequest.getHeaderMap());

	}
	private void deleteRemovedCustomers(String contractId, Set<String> removedCustomersList,
			DataControllerRequest dcRequest) throws ApplicationException {
		ContractCoreCustomerBusinessDelegate customersBD = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(ContractCoreCustomerBusinessDelegate.class);
		customersBD.deleteContractCoreCustomers(removedCustomersList, contractId, dcRequest.getHeaderMap());

		ApprovalMatrixBusinessDelegate approvalmatrixDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(ApprovalMatrixBusinessDelegate.class);

		approvalmatrixDelegate.deleteApprovalMatrixForCifs(contractId, removedCustomersList, dcRequest);

	}
	private Set<String> addNewCustomers(String contractCustomers, Set<String> addedCustomersList, String contractId,
			String serviceDefinitionType,JsonArray globalLevelPermissionsJsonArray,
            JsonArray accountLevelPermissionsJsonArray, JsonArray transactionLimitsJsonArray, String legalEntityId, DataControllerRequest dcRequest) throws ApplicationException {
		Set<String> createdValidContractCustomers = new HashSet<>();
		if (!addedCustomersList.isEmpty()) {
			ContractCoreCustomerBusinessDelegate customerBD = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(ContractCoreCustomerBusinessDelegate.class);
			Set<String> validCustomers = customerBD.getValidCoreContractCustomers(addedCustomersList,
					legalEntityId, dcRequest.getHeaderMap());
			createdValidContractCustomers = createContractCustomers(contractCustomers, validCustomers, contractId,
					legalEntityId, dcRequest);

			createContractAccounts(contractCustomers, createdValidContractCustomers, null, contractId, legalEntityId, dcRequest);
			/*
			 * Map<String, Set<String>> featuresCreated =
			 * createContractFeatures(contractCustomers, createdValidContractCustomers,
			 * null, contractId, serviceDefinitionType, "false", dcRequest);
			 * createContractActionLimits(contractCustomers, featuresCreated, contractId,
			 * serviceDefinitionType, dcRequest);
			 */

			JsonArray accountLevelPermissionsforNewCustomer = new JsonArray();
			JsonArray globalLevelPermissionsforNewCustomer = new JsonArray();
			JsonArray transactionLimitsforNewCustomer = new JsonArray();
			
			for(JsonElement actionElement : accountLevelPermissionsJsonArray) {
				if(addedCustomersList.contains(
						JSONUtil.getString(actionElement.getAsJsonObject(), "coreCustomerId"))) {
					accountLevelPermissionsforNewCustomer.add(actionElement);
				}
			}
			
			for(JsonElement globalElement : globalLevelPermissionsJsonArray) {
				if(addedCustomersList.contains(
						JSONUtil.getString(globalElement.getAsJsonObject(), "coreCustomerId"))) {
					globalLevelPermissionsforNewCustomer.add(globalElement);
				}
			}
			
			for(JsonElement transactionLimitElement : transactionLimitsJsonArray) {
				if(addedCustomersList.contains(
						JSONUtil.getString(transactionLimitElement.getAsJsonObject(), "coreCustomerId"))) {
					transactionLimitsforNewCustomer.add(transactionLimitElement);
				}
			}
			
			 JsonObject contractActionObj = new JsonObject();
	         contractActionObj.add("accountLevelPermissions", accountLevelPermissionsforNewCustomer);
	         contractActionObj.add("globalLevelPermissions", globalLevelPermissionsforNewCustomer);
	         contractActionObj.add("transactionLimits", transactionLimitsforNewCustomer);
	         contractActionObj.addProperty("serviceDefinitionType", serviceDefinitionType);

	         ContractBusinessDelegate ContractBusinessDelegate = DBPAPIAbstractFactoryImpl
	                    .getBusinessDelegate(ContractBusinessDelegate.class);
	         ContractBusinessDelegate.createContractActionLimit(contractActionObj, contractId,
	        		 legalEntityId, dcRequest.getHeaderMap());
	            
			createDefaultApprovalMatrixEntry(contractCustomers, contractId, createdValidContractCustomers, dcRequest, legalEntityId);
		}
		return createdValidContractCustomers;

	}
	private void createDefaultApprovalMatrixEntry(String contractCustomers, String contractId,
            Set<String> createdValidContractCoreCustomers, DataControllerRequest dcRequest, String legalEntityId )
            throws ApplicationException {
        JsonParser parser = new JsonParser();
        JsonArray contractCustomersArray = parser.parse(contractCustomers).getAsJsonArray();
        ContractCoreCustomerBusinessDelegate contractCoreCustomerBD = DBPAPIAbstractFactoryImpl
                .getBusinessDelegate(ContractCoreCustomerBusinessDelegate.class);

        ApprovalMatrixBusinessDelegate approvalmatrixDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class)
                .getBusinessDelegate(ApprovalMatrixBusinessDelegate.class);

        for (JsonElement customerElement : contractCustomersArray) {
            JsonObject customerJson = customerElement.isJsonObject() ? customerElement.getAsJsonObject()
                    : new JsonObject();
            if (JSONUtil.hasKey(customerJson, CORECUSTOMERID) && JSONUtil.hasKey(customerJson, ACCOUNTS)
                    && customerJson.get(ACCOUNTS).isJsonArray()
                    && createdValidContractCoreCustomers.contains(JSONUtil.getString(customerJson, CORECUSTOMERID))) {

                String coreCustomerId = JSONUtil.getString(customerJson, CORECUSTOMERID);
                Map<String, Set<String>> contractCoreCustomerDetailsMap = contractCoreCustomerBD
                        .getCoreCustomerAccountsFeaturesActions(contractId, coreCustomerId, dcRequest.getHeaderMap());

                Set<String> customerAccounts = contractCoreCustomerDetailsMap.get("accounts");
                Set<String> customerActions = contractCoreCustomerDetailsMap.get("actions");
                customerActions = getActionWithApproveFeatureAction(customerActions, legalEntityId, dcRequest);

                try {
                    approvalmatrixDelegate.createDefaultApprovalMatrixEntry(contractId,
                            String.join(DBPUtilitiesConstants.COMMA_SEPERATOR, customerAccounts),
                            customerActions.toArray(new String[0]), coreCustomerId, legalEntityId);
                } catch (Exception e) {
                    logger.error("failed to create approval matrix", e);
                }

            }
        }

    }
	private Set<String> getActionWithApproveFeatureAction(Set<String> actionsSet, String legalEntityId, DataControllerRequest dcRequest)
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

		String actions = contractFeaturesBD.getActionsWithApproveFeatureAction(actionsString.toString(), legalEntityId,
				dcRequest.getHeaderMap());

        return HelperMethods.splitString(actions, DBPUtilitiesConstants.COMMA_SEPERATOR);

    }

	 private Set<String> createContractCustomers(String contractCustomers, Set<String> validContractCustomers,
	            String contractId, String legalEntityId, DataControllerRequest dcRequest) throws ApplicationException {
	        Set<String> createdValidCustomers = new HashSet<>();
	        JsonArray contractCustomersArray = JSONUtil.parseAsJsonArray(contractCustomers);
	        ContractCoreCustomerBusinessDelegate customerBD = DBPAPIAbstractFactoryImpl
	                .getBusinessDelegate(ContractCoreCustomerBusinessDelegate.class);
	        for (JsonElement customerElement : contractCustomersArray) {
	            if (JSONUtil.isJsonNotNull(customerElement) && customerElement.isJsonObject()) {
	                JsonObject customerJson = customerElement.getAsJsonObject();
	                String customerId = customerJson.has(CORECUSTOMERID) ? customerJson.get(CORECUSTOMERID).getAsString()
	                        : "";
	                String isPrimary = customerJson.has(ISPRIMARY) ? customerJson.get(ISPRIMARY).getAsString() : "";
	                String customerName = customerJson.has(CORECUSTOMER_NAME)
	                        ? customerJson.get(CORECUSTOMER_NAME).getAsString()
	                        : "";
	                String isBusinessType = customerJson.has(ISBUSINESS) ? customerJson.get(ISBUSINESS).getAsString() : "";
	                String sectorId = customerJson.has(SECTORID) ? customerJson.get(SECTORID).getAsString() : "";
	                String implicitAccountAccess = customerJson.has(IMPLICIT_ACCOUNT_ACCESS)
	                        ? customerJson.get(IMPLICIT_ACCOUNT_ACCESS).getAsString()
	                        : "";
	                if (StringUtils.isBlank(customerId) || StringUtils.isBlank(customerName)) {
	                    return createdValidCustomers;
	                }

	                if (StringUtils.isBlank(implicitAccountAccess)
	                        || !DBPUtilitiesConstants.BOOLEAN_STRING_TRUE.equalsIgnoreCase(implicitAccountAccess)) {
	                    implicitAccountAccess = DBPUtilitiesConstants.BOOLEAN_STRING_FALSE;
	                }
	                if (!validContractCustomers.contains(customerId) || !JSONUtil.hasKey(customerJson, ACCOUNTS)
	                        || !customerJson.get(ACCOUNTS).isJsonArray()
	                        || customerJson.get(ACCOUNTS).getAsJsonArray().size() == 0) {
	                    continue;
	                }
	                ContractCoreCustomersDTO coreCustomerDTO = new ContractCoreCustomersDTO();
	                coreCustomerDTO.setContractId(contractId);
	                coreCustomerDTO.setCoreCustomerId(customerId);
	                coreCustomerDTO.setId(UUID.randomUUID().toString());
	                coreCustomerDTO.setCoreCustomerName(customerName);
	                coreCustomerDTO.setImplicitAccountAccess(implicitAccountAccess);
	                if (isBusinessType.equalsIgnoreCase(DBPUtilitiesConstants.BOOLEAN_STRING_TRUE)) {
	                    coreCustomerDTO.setIsBusiness(DBPUtilitiesConstants.BOOLEAN_STRING_TRUE);
	                } else {
	                    coreCustomerDTO.setIsBusiness(DBPUtilitiesConstants.BOOLEAN_STRING_FALSE);
	                }

	                if (isPrimary.equalsIgnoreCase(DBPUtilitiesConstants.BOOLEAN_STRING_TRUE)) {
	                    coreCustomerDTO.setIsPrimary(DBPUtilitiesConstants.BOOLEAN_STRING_TRUE);
	                } else {
	                    coreCustomerDTO.setIsPrimary(DBPUtilitiesConstants.BOOLEAN_STRING_FALSE);
	                }

	                if (StringUtils.isNotBlank(sectorId)) {
	                    coreCustomerDTO.setSectorId(sectorId);
	                }
	                coreCustomerDTO.setCompanyLegalUnit(legalEntityId);

	                ContractCoreCustomersDTO resultCustomerDTO = customerBD.createContractCustomer(coreCustomerDTO,
	                        dcRequest.getHeaderMap());
	                if (resultCustomerDTO != null && StringUtils.isNotBlank(resultCustomerDTO.getCoreCustomerId())) {
	                    createdValidCustomers.add(customerId);
	                }

	            }

	        }

	        return createdValidCustomers;
	    }
	private Map<String, Set<ContractAccountsDTO>> createContractAccounts(String contractCustomers,
            Set<String> validContractCustomers, Map<String, Set<String>> coreCustomersAccountsMapToCreate,
            String contractId, String legalEntityId, DataControllerRequest dcRequest) throws ApplicationException {
        ContractAccountsDTO contractAccountDTO;

        Map<String, Set<ContractAccountsDTO>> customerAccountsMap = new HashMap<>();
        
        JsonArray contractCustomersArray = JSONUtil.parseAsJsonArray(contractCustomers);

        ContractAccountsBusinessDelegate contractAccountsBD = DBPAPIAbstractFactoryImpl
                .getBusinessDelegate(ContractAccountsBusinessDelegate.class);

        AccountsBusinessDelegate accountsBD = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(AccountsBusinessDelegate.class);

        for (JsonElement customerElement : contractCustomersArray) {
            JsonObject customerJson = customerElement.isJsonObject() ? customerElement.getAsJsonObject()
                    : new JsonObject();
            if (JSONUtil.hasKey(customerJson, CORECUSTOMERID)
                    && validContractCustomers.contains(JSONUtil.getString(customerJson, CORECUSTOMERID))
                    && (coreCustomersAccountsMapToCreate == null || coreCustomersAccountsMapToCreate
                            .containsKey(JSONUtil.getString(customerJson, CORECUSTOMERID)))
                    && JSONUtil.hasKey(customerJson, ACCOUNTS) && customerJson.get(ACCOUNTS).isJsonArray()) {
                String coreCustomerId = JSONUtil.getString(customerJson, CORECUSTOMERID);
                String coreCustomerName = JSONUtil.getString(customerJson, CORECUSTOMER_NAME);
                JsonArray accountsArray = customerJson.get(ACCOUNTS).getAsJsonArray();

                Set<ContractAccountsDTO> customerAccounts = new HashSet<>();

                for (JsonElement accountElement : accountsArray) {
                    JsonObject accountObject = accountElement.isJsonObject() ? accountElement.getAsJsonObject()
                            : new JsonObject();
                    String accountId = JSONUtil.hasKey(accountObject, ACCOUNTID)
                            ? accountObject.get(ACCOUNTID).getAsString()
                            : "";
                    String accountName = JSONUtil.hasKey(accountObject, ACCOUNTNAME)
                            ? accountObject.get(ACCOUNTNAME).getAsString()
                            : "";
                    String typeId = JSONUtil.hasKey(accountObject, TYPEID) ? accountObject.get(TYPEID).getAsString()
                            : "";
                    String ownerType = JSONUtil.hasKey(accountObject, OWNERTYPE)
                            ? accountObject.get(OWNERTYPE).getAsString()
                            : "";
                    String arrangementId = JSONUtil.hasKey(accountObject, ARRANGEMENTID)
                            ? accountObject.get(ARRANGEMENTID).getAsString()
                            : "";
                    String accountHoldername = JSONUtil.hasKey(accountObject, ACCOUNTHOLDERNAME)
                            ? accountObject.get(ACCOUNTHOLDERNAME).getAsString()
                            : "";
                    String accountStatus = StringUtils.isNotBlank(JSONUtil.getString(accountObject, ACCOUNTSTATUS))
                            ? JSONUtil.getString(accountObject, ACCOUNTSTATUS)
                            : DBPUtilitiesConstants.DEFAULT_ACCOUNT_STATUS;
                    String accountType = JSONUtil.hasKey(accountObject, ACCOUNTTYPE)
                            ? accountObject.get(ACCOUNTTYPE).getAsString()
                            : "";
                    String productId = JSONUtil.hasKey(accountObject, PRODUCTID)
                            ? accountObject.get(PRODUCTID).getAsString()
                            : "";       

                    if (StringUtils.isNotBlank(accountId) && StringUtils.isNotBlank(accountName)
                            && StringUtils.isNotBlank(typeId) && (coreCustomersAccountsMapToCreate == null
                                    || coreCustomersAccountsMapToCreate.get(coreCustomerId).contains(accountId))) {

                        contractAccountDTO = new ContractAccountsDTO();
                        contractAccountDTO.setAccountId(accountId);
                        contractAccountDTO.setAccountName(accountName);
                        contractAccountDTO.setContractId(contractId);
                        contractAccountDTO.setCoreCustomerId(coreCustomerId);
                        //contractAccountDTO.setId(idFormatter.format(new Date()));
                        contractAccountDTO.setId(UUID.randomUUID().toString());
                        contractAccountDTO.setTypeId(typeId);
                        contractAccountDTO.setOwnerType(ownerType);
                        contractAccountDTO.setArrangementId(arrangementId);
                        contractAccountDTO.setStatusDesc(accountStatus);
                        contractAccountDTO.setAccountType(accountType);
                        contractAccountDTO.setProductId(productId);
                        contractAccountDTO.setCompanyLegalUnit(legalEntityId);

                        contractAccountDTO = contractAccountsBD.createContractAccount(contractAccountDTO,
                                dcRequest.getHeaderMap());
                        if (null == contractAccountDTO || StringUtils.isBlank(contractAccountDTO.getId())) {
                        	logger.debug("contractaccount creation failed for accountId "+accountId);
                            throw new ApplicationException(ErrorCodeEnum.ERR_10357);
                        }

                        AccountsDTO accountGetDTO = accountsBD.getAccountDetailsByAccountID(accountId,
                                dcRequest.getHeaderMap());
                        if (null == accountGetDTO || StringUtils.isBlank(accountGetDTO.getAccountId())) {
                            AccountsDTO inputDTO = new AccountsDTO();
                            inputDTO.setAccountId(accountId);
                            inputDTO.setAccountName(accountName);
                            inputDTO.setAccountType(accountType);
                            inputDTO.setTypeId(typeId);
                            inputDTO.setAccountHolder(accountHoldername);
                            inputDTO.setStatusDescription(accountStatus);
                            inputDTO.setArrangementId(arrangementId);
                            inputDTO.setMembershipId(coreCustomerId);
                            inputDTO.setMembershipName(coreCustomerName);
                            inputDTO.setCompanyLegalUnit(legalEntityId);
                            AccountsDTO accountCreatedDTO = accountsBD.createAccount(inputDTO,
                                    dcRequest.getHeaderMap());
                            if (null == accountCreatedDTO || StringUtils.isBlank(accountCreatedDTO.getAccountId())) {
                            	logger.debug("account creation failed for accountId "+accountId);
                                throw new ApplicationException(ErrorCodeEnum.ERR_10297);
                            }

                        }

                        customerAccounts.add(contractAccountDTO);
                    }
                }

                customerAccountsMap.put(coreCustomerId, customerAccounts);

            }

        }

        return customerAccountsMap;
    }
	private void updateContractDetails(String contractId, String ContractName, String faxId, String legalEntityId,
			DataControllerRequest dcRequest) throws ApplicationException {
		ContractDTO dto = new ContractDTO();
		dto.setId(contractId);
		dto.setName(ContractName);
		dto.setFaxId(faxId);
		dto.setCompanyLegalUnit(legalEntityId);

		ContractBusinessDelegate contractBD = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(ContractBusinessDelegate.class);
		contractBD.updateContract(dto, dcRequest.getHeaderMap());

	}
	private void updateCoreCustomerDetails(String coreCustomerId, String contractId, boolean isPrimary, String legalEntityId, 
			DataControllerRequest dcRequest) throws ApplicationException {
		ContractCoreCustomerBusinessDelegate coreCustomerBD = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(ContractCoreCustomerBusinessDelegate.class);
		String id = null;
		ContractCoreCustomersDTO coreCustomerDTO = new ContractCoreCustomersDTO();
		coreCustomerDTO.setContractId(contractId);
		coreCustomerDTO.setCoreCustomerId(coreCustomerId);
		coreCustomerDTO.setCompanyLegalUnit(legalEntityId);
		List<ContractCoreCustomersDTO> coreCustomersDTO = coreCustomerBD.getContractCoreCustomerDetails(coreCustomerDTO,
				dcRequest.getHeaderMap());
		if (null != coreCustomersDTO && !coreCustomersDTO.isEmpty()) {
			id = coreCustomersDTO.get(0).getId();
		}

		if (StringUtils.isNotBlank(id)) {
			coreCustomerDTO = new ContractCoreCustomersDTO();
			coreCustomerDTO.setId(id);
			coreCustomerDTO.setIsPrimary(Boolean.toString(isPrimary));
			coreCustomerBD.updateContractCustomer(coreCustomerDTO, dcRequest.getHeaderMap());
		}

	}
	private void segregateGivenCustomersListAndUpdateCoreCustomerId(String contractCustomers, String contractId,
			Set<String> addedCustomersList, Set<String> removedCustomersList, Set<String> notUpdatedCustomersList,
			StringBuilder currentPrimaryCoreCustomerId, DataControllerRequest dcRequest, String deletedCustomers) throws ApplicationException {
		boolean isAtleastaCustomerisPrimary = false;
		/*if (StringUtils.isBlank(contractId) || StringUtils.isBlank(contractCustomers)) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10365);
		}*/

		Set<String> createdCustomers = new HashSet<>();
		ContractCoreCustomerBusinessDelegate customersBD = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(ContractCoreCustomerBusinessDelegate.class);
		List<ContractCoreCustomersDTO> customersList = customersBD.getContractCoreCustomers(contractId, false, false,
				dcRequest.getHeaderMap());
		for (ContractCoreCustomersDTO dto : customersList) {
			createdCustomers.add(dto.getCoreCustomerId());
		}
		
		JsonArray contractCustomersArray = JSONUtil.parseAsJsonArray(contractCustomers);
		for (JsonElement customerElement : contractCustomersArray) {
			if (JSONUtil.isJsonNotNull(customerElement) && customerElement.isJsonObject()) {
				JsonObject customerJson = customerElement.getAsJsonObject();
				String customerId = customerJson.has(CORECUSTOMERID) ? customerJson.get(CORECUSTOMERID).getAsString()
						: "";
				String customerName = customerJson.has(CORECUSTOMER_NAME)
						? customerJson.get(CORECUSTOMER_NAME).getAsString()
						: "";
				String isPrimary = customerJson.has(ISPRIMARY) ? customerJson.get(ISPRIMARY).getAsString() : "";
				if (StringUtils.isBlank(customerId) || StringUtils.isBlank(customerName)) {
					throw new ApplicationException(ErrorCodeEnum.ERR_10364);
				}
				if (createdCustomers.contains(customerId)) {
					notUpdatedCustomersList.add(customerId);
				} else {
					addedCustomersList.add(customerId);
				}
				if (!isAtleastaCustomerisPrimary && "true".equalsIgnoreCase(isPrimary)) {
					isAtleastaCustomerisPrimary = true;
					currentPrimaryCoreCustomerId.append(customerId);
				}

			}

		}
		calculateRemovedCustomers(deletedCustomers, createdCustomers, removedCustomersList);
		
		/*for (ContractCoreCustomersDTO dto : customersList) {
			if (!notUpdatedCustomersList.contains(dto.getCoreCustomerId())) {
				removedCustomersList.add(dto.getCoreCustomerId());
			}
		}
		if (!isAtleastaCustomerisPrimary) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10369);
		}*/

	}
	private void calculateRemovedCustomers(String deletedCustomers, Set<String> existingcoreCustomers,
			Set<String> removedCustomersList) {

		if (StringUtils.isBlank(deletedCustomers))
			return;
		JsonElement deletedcontractCustomerselement;
		try {
			deletedcontractCustomerselement = JSONUtil.parseAsJsonArray(deletedCustomers);
		} catch (Exception e) {
			logger.error("Error in calculating deleted customers",e);
			return;
		}
		logger.debug("existingcoreCustomers"+existingcoreCustomers);
		if (deletedcontractCustomerselement == null || deletedcontractCustomerselement.isJsonNull()
				|| !deletedcontractCustomerselement.isJsonArray())
			return;
		logger.debug("deletedcontractCustomerselement"+deletedcontractCustomerselement);
		JsonArray deletedcontractCustomersArray = deletedcontractCustomerselement.getAsJsonArray();
		for (JsonElement deletedcustomerElement : deletedcontractCustomersArray) {
			if (JSONUtil.isJsonNotNull(deletedcustomerElement) && deletedcustomerElement.isJsonObject()) {
				JsonObject customerJson = deletedcustomerElement.getAsJsonObject();
				String deletedcustomerId = customerJson.has(CORECUSTOMERID)
						? customerJson.get(CORECUSTOMERID).getAsString()
						: "";
				logger.debug("deletedcustomerId"+deletedcustomerId);
				if (existingcoreCustomers.contains(deletedcustomerId))
					removedCustomersList.add(deletedcustomerId);
			}
		}
	} 
	private String getExistingPrimaryCoreCustomerId(String contractId, DataControllerRequest dcRequest)
			throws ApplicationException {
		ContractCoreCustomerBusinessDelegate coreCustomerBD = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(ContractCoreCustomerBusinessDelegate.class);
		String coreCustomerId = "";

		ContractCoreCustomersDTO coreCustomerDTO = new ContractCoreCustomersDTO();
		coreCustomerDTO.setContractId(contractId);
		coreCustomerDTO.setIsPrimary(DBPUtilitiesConstants.BOOLEAN_STRING_TRUE);
		List<ContractCoreCustomersDTO> coreCustomersDTO = coreCustomerBD.getContractCoreCustomerDetails(coreCustomerDTO,
				dcRequest.getHeaderMap());
		if (null != coreCustomersDTO && !coreCustomersDTO.isEmpty()) {
			coreCustomerId = coreCustomersDTO.get(0).getCoreCustomerId();
		}

		return coreCustomerId;
	}
	private void checkIfPayloadContainsValidAccountsForAllCustomers(String contractCustomers, String contractId,
            DataControllerRequest dcRequest) throws ApplicationException {
        
        JsonArray contractCustomersArray = JSONUtil.parseAsJsonArray(contractCustomers);
        boolean accountsStatus = false;
        String accountNumber=dcRequest.getParameter("accountNumber"); 
        ContractAccountsBusinessDelegate contractAccountsBD = DBPAPIAbstractFactoryImpl
                .getBusinessDelegate(ContractAccountsBusinessDelegate.class);
        Set<String> givenAccountsList = new HashSet<>();
        boolean isAtleastSavingOrCurrentAccountFound = false;
        String customerAccountType="";

        for (JsonElement customerElement : contractCustomersArray) {
            JsonObject customerJson = customerElement.isJsonObject() ? customerElement.getAsJsonObject()
                    : new JsonObject();
            if (JSONUtil.hasKey(customerJson, CORECUSTOMERID)
                    && StringUtils.isNotBlank(customerJson.get(CORECUSTOMERID).getAsString())
                    && JSONUtil.hasKey(customerJson, ACCOUNTS) && customerJson.get(ACCOUNTS).isJsonArray()) {
                JsonArray accountsArray = customerJson.get(ACCOUNTS).getAsJsonArray();
                logger.debug("checkIfPayloadContainsValidAccountsForAllCustomers: accountsArray:"+accountsArray);

                if (accountsArray.size() == 0) {
                	logger.debug("no accounts in the contract payload");
                    throw new ApplicationException(ErrorCodeEnum.ERR_10408);
                }

                for (JsonElement accountElement : accountsArray) {
                    JsonObject accountObject = accountElement.isJsonObject() ? accountElement.getAsJsonObject()
                            : new JsonObject();
                    String accountId = accountObject.has(ACCOUNTID) ? accountObject.get(ACCOUNTID).getAsString() : "";
                    String accountName = accountObject.has(ACCOUNTNAME) ? accountObject.get(ACCOUNTNAME).getAsString()
                            : "";
                    String typeId = accountObject.has(TYPEID) && !accountObject.get(TYPEID).isJsonNull()? accountObject.get(TYPEID).getAsString() : "";
                    String accountType = JSONUtil.hasKey(accountObject, ACCOUNTTYPE)? accountObject.get(ACCOUNTTYPE).getAsString(): "";
                    if (StringUtils.isBlank(accountId) || StringUtils.isBlank(accountName)
                            || StringUtils.isBlank(typeId)) {
                    	logger.debug("incorrect details in the contract payload accountId,accountName,typeId,accountType: "
                    			+StringUtils.isBlank(accountId)+", "
                    			+StringUtils.isBlank(accountName)+", "+StringUtils.isBlank(typeId)+", "+StringUtils.isBlank(accountType));
                        throw new ApplicationException(ErrorCodeEnum.ERR_10409);
                    }
                    if(StringUtils.isNotBlank(accountNumber) && accountNumber.equalsIgnoreCase(accountId)) {
                    	customerAccountType=accountType;
                    }
                    givenAccountsList.add(accountId);
                }
            } else {
            	logger.debug("incorrect contract payload");
                throw new ApplicationException(ErrorCodeEnum.ERR_10410);
            }
        }
        if(StringUtils.isNotBlank(customerAccountType)) {
        	 if(customerAccountType.equalsIgnoreCase("Savings") || customerAccountType.equalsIgnoreCase("Checking")) {
             	isAtleastSavingOrCurrentAccountFound=true;
             }else {
            	 throw new ApplicationException(ErrorCodeEnum.ERR_30011 );
             }
        }
        if (StringUtils.isNotBlank(contractId)) {
            Set<String> existingContractAccounts = new HashSet<>();
            Set<String> newlyAddedAccounts = new HashSet<>();
            List<ContractAccountsDTO> contractAccountsList = contractAccountsBD.getContractCustomerAccounts(contractId,
                    null, dcRequest.getHeaderMap());
            for (ContractAccountsDTO dto : contractAccountsList) {
                String accountId = dto.getAccountId();
                if (StringUtils.isNotBlank(accountId)) {
                    existingContractAccounts.add(accountId);
                }
            }

            for (String givenAccountId : givenAccountsList) {
                if (!existingContractAccounts.contains(givenAccountId)) {
                    newlyAddedAccounts.add(givenAccountId);
                }
            }

            accountsStatus = contractAccountsBD.checkIfGivenAccountsAreValid(newlyAddedAccounts,
                    dcRequest.getHeaderMap());
        } else {
            accountsStatus = contractAccountsBD.checkIfGivenAccountsAreValid(givenAccountsList,
                    dcRequest.getHeaderMap());
        }

        if (!accountsStatus) {
        	logger.debug("incorrect accountsStatus in contract payload");
            throw new ApplicationException(ErrorCodeEnum.ERR_10412);
        }
    }
	private boolean verifyContractName(String contarctId, String contractName, DataControllerRequest dcRequest)
            throws ApplicationException {
        ContractBusinessDelegate contractBD = DBPAPIAbstractFactoryImpl
                .getBusinessDelegate(ContractBusinessDelegate.class);
        ContractDTO dto = new ContractDTO();
        ContractDTO resultContractDTO;
        if (StringUtils.isBlank(contarctId) && StringUtils.isNotBlank(contractName)) {
            dto.setName(contractName);
            resultContractDTO = contractBD.getContractDetails(dto, dcRequest.getHeaderMap());
            if (null == resultContractDTO || StringUtils.isBlank(resultContractDTO.getId())) {
                return true;
            }
        } else if (StringUtils.isNotBlank(contarctId) && StringUtils.isNotBlank(contractName)) {
            dto.setName(contractName);
            resultContractDTO = contractBD.getContractDetails(dto, dcRequest.getHeaderMap());
            if (null == resultContractDTO || StringUtils.isBlank(resultContractDTO.getId())
                    || (StringUtils.isNotBlank(resultContractDTO.getId())
                            && contarctId.equalsIgnoreCase(resultContractDTO.getId()))) {
                return true;

            }

        }
        return false;

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
	private String getServiceDefinitionID(String contractId, DataControllerRequest dcRequest)
			throws ApplicationException {
		ContractDTO contractDTO = new ContractDTO();
		contractDTO.setId(contractId);
		ContractBusinessDelegate contractBD = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(ContractBusinessDelegate.class);
		contractDTO = contractBD.getContractDetails(contractDTO, dcRequest.getHeaderMap());
		if (contractDTO == null || StringUtils.isBlank(contractDTO.getId())
				|| StringUtils.isBlank(contractDTO.getServicedefinitionId())) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10378);
		}
		return contractDTO.getServicedefinitionId();
	}

  private boolean isValidCustomerLimits(JsonArray transactionLimitsJsonArray, Set<String> existingCustomersList, String contractId,
    		String serviceDefinitionType, DataControllerRequest dcRequest) throws ApplicationException {

    	//JsonParser parser = new JsonParser();
    	//JsonArray contractCustomersArray = parser.parse(contractCustomers).getAsJsonArray();
	  boolean isVaild=false;
    	for (JsonElement customerElement : transactionLimitsJsonArray) {
    		JsonObject customerJson =
    				customerElement.isJsonObject() ? customerElement.getAsJsonObject() : new JsonObject();
    		if (JSONUtil.hasKey(customerJson, CORECUSTOMERID)
    				&& existingCustomersList.contains(JSONUtil.getString(customerJson, CORECUSTOMERID))) {

    			JsonArray featuresArray = customerJson.get(featurePermissions).getAsJsonArray();

    			for (JsonElement featureElement : featuresArray) {
    				JsonObject featureJson =
    						featureElement.isJsonObject() ? featureElement.getAsJsonObject() : new JsonObject();

   					JsonArray limitsArray =
    							featureJson.has(LIMITS) && featureJson.get(LIMITS).isJsonArray()
    							? featureJson.get(LIMITS).getAsJsonArray()
    									: new JsonArray();

    				double max_txn_limit_value=0, daily_limit_value = 0, weekly_limit_value = 0;
    				logger.debug("HBL::ContractResourceImplExtn::isValidCustomerLimits:limitsArray size"+limitsArray.size());
    					if(limitsArray.size()==4) {
    					for (JsonElement array_position : limitsArray) {

    						JsonObject limitobject = array_position.isJsonObject() ? array_position.getAsJsonObject():new JsonObject();

    						if(limitobject.has(LIMITID) && limitobject.get(LIMITID).getAsString().equals(MAX_TRANSACTION_LIMIT)) {

    							max_txn_limit_value = Double.parseDouble(limitobject.get(LIMITVALUE).getAsString());

    						}
    						else if(limitobject.has(LIMITID) && limitobject.get(LIMITID).getAsString().equals(DAILY_LIMIT)) {

    							daily_limit_value = Double.parseDouble(limitobject.get(LIMITVALUE).getAsString());
    						}
    						else if(limitobject.has(LIMITID) && limitobject.get(LIMITID).getAsString().equals(WEEKLY_LIMIT)) {

    							weekly_limit_value = Double.parseDouble(limitobject.get(LIMITVALUE).getAsString());
    						}

    					}
    					if((max_txn_limit_value > daily_limit_value) || (daily_limit_value > weekly_limit_value) ) {
    						return false;
    					}
    					}
    					if(limitsArray.size()==8) {
    					isVaild=validateHBLLimits(limitsArray);
    					}
    			}
    			
    		}
    	}
    	 logger.debug("HBL::ContractResourceImplExtn::isValidCustomerLimits:limits are valid:"+isVaild);
    	return isVaild;
    }
  public boolean validateHBLLimits(JsonArray limitsArray) {
	  logger.debug("HBL::ContractResourceImplExtn::validateHBLLimits:limitsArray size"+limitsArray.size());
	  double max_txn_limit_value=0, daily_limit_value = 0, weekly_limit_value = 0;
	  double mb_max_txn_limit_value=0, mb_daily_limit_value = 0, mb_weekly_limit_value = 0;
	  boolean isVaild=true;
	  for (JsonElement array_position : limitsArray) {

			JsonObject limitobject = array_position.isJsonObject() ? array_position.getAsJsonObject():new JsonObject();

			if(limitobject.has(LIMITID) && limitobject.get(LIMITID).getAsString().equals(MAX_TRANSACTION_LIMIT)) {

				max_txn_limit_value = Double.parseDouble(limitobject.get(LIMITVALUE).getAsString());

			}
			else if(limitobject.has(LIMITID) && limitobject.get(LIMITID).getAsString().equals(DAILY_LIMIT)) {

				daily_limit_value = Double.parseDouble(limitobject.get(LIMITVALUE).getAsString());
			}
			else if(limitobject.has(LIMITID) && limitobject.get(LIMITID).getAsString().equals(WEEKLY_LIMIT)) {

				weekly_limit_value = Double.parseDouble(limitobject.get(LIMITVALUE).getAsString());
			}
			
			else if(limitobject.has(LIMITID) && limitobject.get(LIMITID).getAsString().equals(MB_MAX_TRANSACTION_LIMIT)) {

				mb_max_txn_limit_value = Double.parseDouble(limitobject.get(LIMITVALUE).getAsString());

			}
			else if(limitobject.has(LIMITID) && limitobject.get(LIMITID).getAsString().equals(MB_DAILY_LIMIT)) {

				mb_daily_limit_value = Double.parseDouble(limitobject.get(LIMITVALUE).getAsString());
			}
			else if(limitobject.has(LIMITID) && limitobject.get(LIMITID).getAsString().equals(MB_WEEKLY_LIMIT)) {

				mb_weekly_limit_value = Double.parseDouble(limitobject.get(LIMITVALUE).getAsString());
			}

		}
	  logger.debug("HBL::ContractResourceImplExtn::validateHBLLimits:limits:max_txn_limit_value:"+max_txn_limit_value);
	  logger.debug("HBL::ContractResourceImplExtn::validateHBLLimits:limits:daily_limit_value:"+daily_limit_value);
	  logger.debug("HBL::ContractResourceImplExtn::validateHBLLimits:limits:weekly_limit_value:"+weekly_limit_value);
	  logger.debug("HBL::ContractResourceImplExtn::validateHBLLimits:limits:mb_max_txn_limit_value:"+mb_max_txn_limit_value);
	  logger.debug("HBL::ContractResourceImplExtn::validateHBLLimits:limits:mb_daily_limit_value:"+mb_daily_limit_value);
	  logger.debug("HBL::ContractResourceImplExtn::validateHBLLimits:limits:mb_weekly_limit_value:"+mb_weekly_limit_value);
	  
		if((max_txn_limit_value > daily_limit_value) || (daily_limit_value > weekly_limit_value) ) {
			isVaild= false;
		}
		if(isVaild) {
			logger.debug("HBL::ContractResourceImplExtn::validateHBLLimits:OLB limits: true");
			if((mb_max_txn_limit_value > mb_daily_limit_value) || (mb_daily_limit_value > mb_weekly_limit_value) ) {
				isVaild= false;
			}
			
		}
	  
	return isVaild;
	  
  }
  private Map<String, Map<String, Map<String, String>>> getExistingTransactionLimits(String contractId,
			DataControllerRequest dcRequest) throws ApplicationException {

		Map<String, Map<String, Map<String, String>>> existingActionsMap = new HashMap<>();
		ContractBusinessDelegate actionBD = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(ContractBusinessDelegate.class);
		ContractActionLimitsDTO dto = new ContractActionLimitsDTO();
		dto.setContractId(contractId);
		List<ContractActionLimitsDTO> contractActionsList = actionBD.getContractActions(dto, dcRequest.getHeaderMap());
		if (null != contractActionsList && !contractActionsList.isEmpty()) {
			for (ContractActionLimitsDTO contractActionDTO : contractActionsList) {
				if (contractId.equalsIgnoreCase(contractActionDTO.getContractId())) {
					String coreCustomerId = contractActionDTO.getCoreCustomerId();
					String actionId = contractActionDTO.getActionId();
					String limitTYpeId = contractActionDTO.getLimitTypeId();
					String limitValue = contractActionDTO.getValue();
					if (StringUtils.isNotBlank(coreCustomerId) && StringUtils.isNotBlank(actionId)
							&& StringUtils.isNotBlank(limitTYpeId) && StringUtils.isNotBlank(limitValue)) {

						if (!existingActionsMap.containsKey(coreCustomerId)) {
							Map<String, Map<String, String>> actionsMap = new HashMap<>();
							Map<String, String> limitMap = new HashMap<>();
							limitMap.put(limitTYpeId, limitValue);
							actionsMap.put(actionId, limitMap);
							existingActionsMap.put(coreCustomerId, actionsMap);
						} else if (existingActionsMap.containsKey(coreCustomerId)
								&& !existingActionsMap.get(coreCustomerId).containsKey(actionId)) {
							Map<String, Map<String, String>> actionsMap = existingActionsMap.get(coreCustomerId);
							Map<String, String> limitMap = new HashMap<>();
							limitMap.put(limitTYpeId, limitValue);
							actionsMap.put(actionId, limitMap);
						} else if (existingActionsMap.containsKey(coreCustomerId)
								&& existingActionsMap.get(coreCustomerId).containsKey(actionId)
								&& !existingActionsMap.get(coreCustomerId).get(actionId).containsKey(limitTYpeId)) {
							Map<String, String> limitMap = existingActionsMap.get(coreCustomerId).get(actionId);
							limitMap.put(limitTYpeId, limitValue);

						}

					}
				}

			}

		}
		return existingActionsMap;
	}
  private String getQueryString(String contractId, String coreCustomerId, String accountId,String featureId, String actionId,
			String isNewAction, String limitId, String limitValue, String legalEntityId) {
		StringBuilder query = new StringBuilder("");

		query.append("\"" + contractId + "\",");
		query.append("\"" + coreCustomerId + "\",");
		query.append(accountId == null ? "\"\"," : "\"" + accountId + "\",");
		query.append("\"" + featureId + "\",");
		query.append("\"" + actionId + "\",");
		query.append("\"" + isNewAction + "\",");
		query.append("\"" + legalEntityId + "\",");
		query.append("\"" + limitId + "\",");
		limitValue = StringUtils.isNotBlank(limitValue)
				? "@".equals(limitValue) ? "@" : new BigDecimal(limitValue).toPlainString()
				: null;
		query.append("\"" + limitValue + "\"");
		query.append("|");

		return query.toString();

	}
  private void updateApprovalMatrixEntriesForNewActionsAdded(Set<String> addedActionsList,
			Set<String> existingAccountsList, Set<String> addedAccountsList, String coreCustomerId, String contractId,
			DataControllerRequest dcRequest, String legalEntityId) throws ApplicationException {
		ApprovalMatrixBusinessDelegate approvalmatrixDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(ApprovalMatrixBusinessDelegate.class);

		Set<String> customerActions = getActionWithApproveFeatureAction(addedActionsList, legalEntityId, dcRequest);
		existingAccountsList.addAll(addedAccountsList);

		try {
			approvalmatrixDelegate.createDefaultApprovalMatrixEntry(contractId,
					String.join(DBPUtilitiesConstants.COMMA_SEPERATOR, existingAccountsList),
					customerActions.toArray(new String[0]), coreCustomerId, null);
		} catch (Exception e) {
			logger.error(e.getMessage());
			logger.error("failed to create approval matrix");
		}

	}
  private void updateApprovalMatrixEntriesForNewAccountsCreated(Set<String> addedAccountsList,
			Set<String> existingActionsList, String coreCustomerId, String contractId, DataControllerRequest dcRequest, String legalEntityId)
			throws ApplicationException {
		ApprovalMatrixBusinessDelegate approvalmatrixDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(ApprovalMatrixBusinessDelegate.class);

		Set<String> customerActions = getActionWithApproveFeatureAction(existingActionsList, legalEntityId, dcRequest);

		try {
			if (null != addedAccountsList && !addedAccountsList.isEmpty()) {
				approvalmatrixDelegate.createDefaultApprovalMatrixEntry(contractId,
						String.join(DBPUtilitiesConstants.COMMA_SEPERATOR, addedAccountsList),
						customerActions.toArray(new String[0]), coreCustomerId, null);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			logger.error("failed to create approval matrix");
		}

	}
  private void updateLimitQueries(StringBuilder changedActionLimitsQuery, StringBuilder decreasedLimitsQuery,
			Map<String, Map<String, Map<String, String>>> existingActionsMap, String contractId, String coreCustomerId,String accountId,
			String featureId, String actionId, String limitId, String limitValue,String isNewAction, String legalEntityId) {
		if (existingActionsMap.containsKey(coreCustomerId)
				&& existingActionsMap.get(coreCustomerId).containsKey(actionId)
				&& existingActionsMap.get(coreCustomerId).get(actionId).containsKey(limitId)) {
			String existingLimitValue = existingActionsMap.get(coreCustomerId).get(actionId).get(limitId);
			if (StringUtils.isNotBlank(existingLimitValue) && StringUtils.isNotBlank(limitValue)) {
				if (Double.parseDouble(existingLimitValue) > Double.parseDouble(limitValue)) {
					decreasedLimitsQuery.append(getQueryString(contractId, coreCustomerId, accountId,featureId, actionId,
							isNewAction, limitId, limitValue, legalEntityId));
				}
				if (Double.parseDouble(existingLimitValue) != Double.parseDouble(limitValue)) {
					changedActionLimitsQuery.append(getQueryString(contractId, coreCustomerId,accountId, featureId, actionId,
							isNewAction, limitId, limitValue , legalEntityId));
				}

			}
		} else if (existingActionsMap.containsKey(coreCustomerId)
				&& !existingActionsMap.get(coreCustomerId).containsKey(actionId) && StringUtils.isNotBlank(limitValue)
				&& StringUtils.isNotBlank(limitId)) {

			changedActionLimitsQuery.append(
					getQueryString(contractId, coreCustomerId, accountId,featureId, actionId, isNewAction, limitId, limitValue, legalEntityId));

		}
	}
	private void updateExistingCutomers(String contractCustomers, Set<String> existingCustomersList, String contractId,
			String serviceDefinitionType, JsonArray globalLevelPermissionsJsonArray,
            JsonArray accountLevelPermissionsJsonArray, JsonArray transactionLimitsJsonArray, String legalEntityId, DataControllerRequest dcRequest,
            List<String> customerAccountsPayloadsList, Map<String, String> customerAccountMap) throws ApplicationException {
		logger.debug("HBL::ContractResourceImplExtn::updateExistingCutomers:limits are valid:"+transactionLimitsJsonArray.toString());
		Map<String, Set<String>> removedCustomerAccountsMap = new HashMap<>();
		Map<String, Set<String>> removedCustomerFeaturesMap = new HashMap<>();
		Map<String, Set<String>> removedCustomerActionsMap = new HashMap<>();
		Map<String, Set<String>> removedCustomerFeaturesMap1 = new HashMap<>();
        Map<String, Set<String>> removedCustomerActionsMap1 = new HashMap<>();

		Map<String, Set<String>> addedCustomerAccountsMap = new HashMap<>();
		Map<String, Set<String>> addedCustomerFeaturesMap = new HashMap<>();
		Map<String, Set<String>> addedCustomerActionsMap = new HashMap<>();
		Map<String, Set<String>> addedCustomerFeaturesMap1 = new HashMap<>();
        Map<String, Set<String>> addedCustomerActionsMap1 = new HashMap<>();

		Map<String, Set<String>> existingCustomerAccountsMap = new HashMap<>();
		Map<String, Set<String>> existingCustomerFeaturesMap = new HashMap<>();
		Map<String, Set<String>> existingCustomerActionsMap = new HashMap<>();
		Map<String, Set<String>> existingCustomerFeaturesMap1 = new HashMap<>();
        Map<String, Set<String>> existingCustomerActionsMap1 = new HashMap<>();

		StringBuilder changedActionLimitsQuery = new StringBuilder();
		StringBuilder decreasedLimitsQuery = new StringBuilder();

		Map<String, Map<String, Map<String, String>>> existingTransactionLimitsMap = getExistingTransactionLimits(contractId,
				dcRequest);
		
		String account = "";
		
		JsonArray contractCustomersArray = JSONUtil.parseAsJsonArray(contractCustomers);
		ContractCoreCustomerBusinessDelegate contractCoreCustomerBD = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(ContractCoreCustomerBusinessDelegate.class);

		for (JsonElement customer : contractCustomersArray) {
			JsonObject customerJson = customer.isJsonObject() ? customer.getAsJsonObject()
					: new JsonObject();
			if (JSONUtil.hasKey(customerJson, CORECUSTOMERID)
					&& existingCustomersList.contains(JSONUtil.getString(customerJson, CORECUSTOMERID))
					&& JSONUtil.hasKey(customerJson, ACCOUNTS) && customerJson.get(ACCOUNTS).isJsonArray()) {
				Set<String> existingAccountsList = new HashSet<>();
				Set<String> addedAccountsList = new HashSet<>();
				Set<String> removedAccountsList = new HashSet<>();

				Set<String> existingFeaturesList = new HashSet<>();
				Set<String> addedFeaturesList = new HashSet<>();
				Set<String> removedFeaturesList = new HashSet<>();

				Set<String> existingActionsList = new HashSet<>();
				Set<String> addedActionsList = new HashSet<>();
				Set<String> removedActionsList = new HashSet<>();
				
				Set<String> existingFeaturesList1 = new HashSet<>();
		        Set<String> addedFeaturesList1 = new HashSet<>();
		        Set<String> removedFeaturesList1 = new HashSet<>();

		        Set<String> existingActionsList1 = new HashSet<>();
		        Set<String> addedActionsList1 = new HashSet<>();
		        Set<String> removedActionsList1 = new HashSet<>();

				String coreCustomerId = JSONUtil.getString(customerJson, CORECUSTOMERID);
				/* not sure of its usage
				if (!existingTransactionLimitsMap.containsKey(coreCustomerId)) {
					Map<String, Map<String, String>> actionsMap = new HashMap<>();
					existingTransactionLimitsMap.put(coreCustomerId, actionsMap);
				}
				*/
				
				Map<String, Set<String>> contractCoreCustomerDetailsMap = contractCoreCustomerBD
						.getCoreCustomerAccountsFeaturesActions(contractId, coreCustomerId, dcRequest.getHeaderMap());
				JsonArray accountsArray = customerJson.get(ACCOUNTS).getAsJsonArray();

				Set<String> customerAccounts = contractCoreCustomerDetailsMap.get("accounts");
				Set<String> customerFeatures = contractCoreCustomerDetailsMap.get("features");

				for (JsonElement accountElement : accountsArray) {
					JsonObject accountObject = accountElement.isJsonObject() ? accountElement.getAsJsonObject()
							: new JsonObject();
					String accountId = accountObject.has(ACCOUNTID) ? accountObject.get(ACCOUNTID).getAsString() : "";
					String accountName = accountObject.has(ACCOUNTNAME) ? accountObject.get(ACCOUNTNAME).getAsString()
							: "";
					String typeId = accountObject.has(TYPEID) ? accountObject.get(TYPEID).getAsString() : "";					
					if (StringUtils.isNotBlank(accountId) && StringUtils.isNotBlank(accountName)
							&& StringUtils.isNotBlank(typeId)) {
						if (customerAccounts.contains(accountId)) {
							existingAccountsList.add(accountId);
						} else {
							addedAccountsList.add(accountId);
						}
					} else {
	                    removedAccountsList.add(accountId);
	                 }
			                 
			          contractCoreCustomerBD.deleteCoreCustomerAccounts(removedAccountsList, contractId,coreCustomerId,
			                      dcRequest.getHeaderMap());
			          removedCustomerAccountsMap.put(coreCustomerId, removedAccountsList);
			          addedCustomerAccountsMap.put(coreCustomerId, addedAccountsList);
			          existingCustomerAccountsMap.put(coreCustomerId, existingAccountsList);
			              
			          }
				for (String accountId : customerAccounts) {
                    if (!existingAccountsList.contains(accountId) && !addedAccountsList.contains(accountId)) {
                        removedAccountsList.add(accountId);
                        String payloadForFetchDistinctAccountsProc = contractId+","+coreCustomerId+","+accountId;
                        customerAccountsPayloadsList.add(payloadForFetchDistinctAccountsProc);
                        
                    }
                }
				
				getDistinctCustomerAccounts(customerAccountsPayloadsList, legalEntityId, dcRequest, customerAccountMap);

				contractCoreCustomerBD.deleteCoreCustomerAccounts(removedAccountsList, contractId,coreCustomerId,
	                      dcRequest.getHeaderMap());
	          removedCustomerAccountsMap.put(coreCustomerId, removedAccountsList);
	          addedCustomerAccountsMap.put(coreCustomerId, addedAccountsList);
	          existingCustomerAccountsMap.put(coreCustomerId, existingAccountsList);
			                    
			   // Getting existing contract feature actions
				
				    ContractBusinessDelegate ContractBusinessDelegate = DBPAPIAbstractFactoryImpl
	                        .getBusinessDelegate(ContractBusinessDelegate.class);
	                
			               
					Map<String, FeatureActionLimitsDTO> coreCustomerFeatureActionDTO = ContractBusinessDelegate.getContractActions(
				            contractId, coreCustomerId, dcRequest.getHeaderMap());

				    Set<String> coreCustomerGlobalActions = coreCustomerFeatureActionDTO
				    		.get(coreCustomerId).getGlobalLevelActions();
				    Map<String, Map<String, Set<String>>> coreCustomerAccountActions = coreCustomerFeatureActionDTO
				    		.get(coreCustomerId).getAsscoiatedAccountActions();
				                   
				                    		             
			  // global level

			       for (JsonElement customerElement1 : globalLevelPermissionsJsonArray) {
			          JsonObject customerJson2 =
			                    customerElement1.isJsonObject() ? customerElement1.getAsJsonObject() : new JsonObject();
			          JsonArray featuresArray2 = customerJson2.get(FEATURES).getAsJsonArray();
			             for (JsonElement featureElement2 : featuresArray2) {
			               JsonObject featureJson2 =
			                          featureElement2.isJsonObject() ? featureElement2.getAsJsonObject()
			                           : new JsonObject();
			               String featureId2 =
			                           featureJson2.has(FEATUREID) ? featureJson2.get(FEATUREID).getAsString() : "";
			               JsonArray actionsArray2 =
			                         featureJson2.has(permissions) && featureJson2.get(permissions).isJsonArray()
			                         ? featureJson2.get(permissions).getAsJsonArray()
			                         : new JsonArray();
			                       
		                    boolean areActionsAllowed = false;
		                    for (JsonElement action2 : actionsArray2) {
	                            JsonObject actionJson2 =
	                                  action2.isJsonObject() ? action2.getAsJsonObject() : new JsonObject();
	                            String actionId1 =
	                                  actionJson2.has(ACTIONID) ? actionJson2.get(ACTIONID).getAsString() : "";
	                            String isEnabled =
	                                  actionJson2.has("isEnabled") ? actionJson2.get("isEnabled").getAsString()
	                                                   : "";
	                            String isNewAction =
	                                  actionJson2.has(ISNEWACTION) ? actionJson2.get(ISNEWACTION).getAsString()
	                                  : "";

	                            if (StringUtils.isNotBlank(isNewAction) && "true".equalsIgnoreCase(isNewAction)) {
	                                   isNewAction = "1";
	                            } else {
	                                   isNewAction = "0";
	                              }
	                            
	                            if (StringUtils.isNotBlank(actionId1) && "true".equalsIgnoreCase(isEnabled)) {
	                                areActionsAllowed = true;
	                                        
	                            if (coreCustomerGlobalActions.contains(actionId1)) {
	                                existingActionsList.add(actionId1);
	                            } else {
	                                addedActionsList.add(actionId1);
	                              }
	                            } else {
	                                removedActionsList.add(actionId1);
	                               }
		                          
	                            if (StringUtils.isNotBlank(actionId1) && addedActionsList.contains(actionId1)) {
	                                /*updateLimitQueries(changedActionLimitsQuery, decreasedLimitsQuery,
	                                existingTransactionLimitsMap,contractId, coreCustomerId, null, featureId2, actionId1, "@", "@",
	                                                isNewAction, legalEntityId);*/
	                                changedActionLimitsQuery.append(
	                    					getQueryString(contractId, coreCustomerId, null,featureId2, actionId1, isNewAction, "@", "@", legalEntityId));
	                            }
	                          }
	                          if (areActionsAllowed) {
	                            if (StringUtils.isNotBlank(featureId2) && actionsArray2.size() > 0) {
	                              if (customerFeatures.contains(featureId2)) {
	                                 existingFeaturesList.add(featureId2);
	                              } else {
	                                 addedFeaturesList.add(featureId2);
	                                 }
	                            }
	                          }
	                          else {
				                   removedFeaturesList.add(featureId2);
				             }
	                        }		                            
			                                
			        contractCoreCustomerBD.deleteCoreCustomerFeatures(removedFeaturesList, contractId, coreCustomerId,
				                  dcRequest.getHeaderMap());
				    contractCoreCustomerBD.deleteCoreCustomerActions(removedActionsList, contractId, coreCustomerId,
				                  account,dcRequest.getHeaderMap());
				    updateApprovalMatrixEntriesForNewActionsAdded(addedActionsList, existingAccountsList,
				                  addedAccountsList,coreCustomerId, contractId, dcRequest, legalEntityId);		         
			       }

			   // account level
			       
			       for (JsonElement accountElement : accountLevelPermissionsJsonArray) {
		                JsonObject accountJson = accountElement.isJsonObject() ? accountElement.getAsJsonObject()
		                  : new JsonObject();
		                JsonArray accountsArray1 = accountJson.get(ACCOUNTS).getAsJsonArray();
		                  for (JsonElement accountsElement1 : accountsArray1) {
		                	  JsonObject accountObject =
			                             accountsElement1.isJsonObject() ? accountsElement1.getAsJsonObject()
			                                                : new JsonObject();
			                  String accountId1 =
			                          accountObject.has(ACCOUNTID) ? accountObject.get(ACCOUNTID).getAsString()
			                                                : "";
			               JsonArray featuresArray1 = accountObject.get(featurePermissions).getAsJsonArray();
			               for (JsonElement featureElement1 : featuresArray1) {
	                           JsonObject featureJson1 =
	                                featureElement1.isJsonObject() ? featureElement1.getAsJsonObject()
	                                : new JsonObject();
	                             String featureId1 =
	                                featureJson1.has(FEATUREID) ? featureJson1.get(FEATUREID).getAsString()
	                                 : "";
	                            JsonArray actionsArray1 =
	                               featureJson1.has(permissions) && featureJson1.get(permissions).isJsonArray()
	                               ? featureJson1.get(permissions).getAsJsonArray()
	                               : new JsonArray();
		                                
	                             boolean areActionsAllowed = false;
			                     for (JsonElement action1 : actionsArray1) {
			                       JsonObject actionJson1 =
			                             action1.isJsonObject() ? action1.getAsJsonObject() : new JsonObject();
			                       String actionId =
			                              actionJson1.has(ACTIONID) ? actionJson1.get(ACTIONID).getAsString()
			                              : "";
			                       String isEnabled = actionJson1.has("isEnabled")
			                              ? actionJson1.get("isEnabled").getAsString() : "";
			                       String isNewAction = actionJson1.has(ISNEWACTION)
			                              ? actionJson1.get(ISNEWACTION).getAsString()
			                              : "";
			                       if (StringUtils.isNotBlank(isNewAction)&& "true".equalsIgnoreCase(isNewAction)) {
			                             isNewAction = "1";
			                       } else {
			                             isNewAction = "0";
			                         }
			                                   		                            
			                       if(coreCustomerAccountActions.containsKey(accountId1)
			                    		   && coreCustomerAccountActions.get(accountId1).containsKey(featureId1)){
				                        if (StringUtils.isNotBlank(actionId) && "true".equalsIgnoreCase(isEnabled)) {
				                               areActionsAllowed = true;
				                         if (coreCustomerAccountActions.get(accountId1).get(featureId1).contains(actionId)) {
				                                existingActionsList1.add(actionId);
				                         } else {
				                                addedActionsList1.add(actionId);
				                           }
				                        } else {
				                                removedActionsList1.add(actionId);
				                          }
				                    } else
				                            
				                       if (StringUtils.isNotBlank(actionId) && "true".equalsIgnoreCase(isEnabled)) {
				                           areActionsAllowed = true;
				                        
				                            addedActionsList1.add(actionId);
				                       
				                       } else {
				                            removedActionsList1.add(actionId);
				                         }

			                         if (StringUtils.isNotBlank(actionId) && addedActionsList1.contains(actionId)){
				                            /*updateLimitQueries(changedActionLimitsQuery, decreasedLimitsQuery,
				                            existingTransactionLimitsMap,contractId, coreCustomerId, accountId1, featureId1, actionId, "@",
				                            "@", isNewAction, legalEntityId);*/
				                            changedActionLimitsQuery.append(
			                    					getQueryString(contractId, coreCustomerId, accountId1,featureId1, actionId, isNewAction, "@", "@", legalEntityId));
				                       }
				                     }
				                     
				                     if (areActionsAllowed) {
				                        if (StringUtils.isNotBlank(featureId1) && actionsArray1.size() > 0 
				                            && StringUtils.isNotBlank(accountId1)) {
				                          if (customerFeatures.contains(featureId1)){
				                               existingFeaturesList1.add(featureId1);
				                          } else {
				                               addedFeaturesList1.add(featureId1);
				                            }
				                         }
				                      }else {
			                                removedFeaturesList1.add(featureId1);
			                           }
				                 }		                            
		                        

			        contractCoreCustomerBD.deleteCoreCustomerFeatures(removedFeaturesList1, contractId,coreCustomerId,
			                 dcRequest.getHeaderMap());
			             
			        removedFeaturesList1.clear(); 
			             
			        contractCoreCustomerBD.deleteCoreCustomerActions(removedActionsList1, contractId,coreCustomerId, accountId1,
			             dcRequest.getHeaderMap());
			        removedActionsList1.clear(); 

			        updateApprovalMatrixEntriesForNewAccountsCreated(addedAccountsList, existingActionsList1,coreCustomerId,
			                 contractId, dcRequest, legalEntityId);
			        updateApprovalMatrixEntriesForNewActionsAdded(addedActionsList1, existingAccountsList,addedAccountsList,
			                 coreCustomerId, contractId, dcRequest, legalEntityId);
			         }
			       }

			   // transaction limits
			       Set<String> existingActionslimits = new HashSet<>();
			       Set<String> addedActionslimits = new HashSet<>();
			       for (JsonElement customerElement : transactionLimitsJsonArray) {
			              JsonObject customerJson1 = customerElement.isJsonObject() ? customerElement.getAsJsonObject()
			              : new JsonObject();
			                JsonArray featuresArray = customerJson1.get(featurePermissions).getAsJsonArray();
			                
			            for (JsonElement featureElement : featuresArray) {
			               JsonObject featureJson =
			                 featureElement.isJsonObject() ? featureElement.getAsJsonObject() : new JsonObject();
			                String featureId = featureJson.has(FEATUREID) ? featureJson.get(FEATUREID).getAsString() : "";

			                String actionId = featureJson.has(ACTIONID) ? featureJson.get(ACTIONID).getAsString() : "";
			                               
			                if (coreCustomerGlobalActions.contains(actionId)) {
			                	existingActionslimits.add(actionId);
                            } else {
                            	addedActionslimits.add(actionId);
                              }
                            
			                String isNewAction = featureJson.has(ISNEWACTION) ? featureJson.get(ISNEWACTION).getAsString() : "";
			                 if (StringUtils.isNotBlank(isNewAction) && "true".equalsIgnoreCase(isNewAction)) {
			                     isNewAction = "1";
			                 } else {
			                     isNewAction = "0";
			                   }
			         
			              JsonArray limitsArray = featureJson.has(LIMITS) && featureJson.get(LIMITS).isJsonArray()
				                    ? featureJson.get(LIMITS).getAsJsonArray()
				                    : new JsonArray();
				                  
				             if (limitsArray.size() > 0) {
				            	 for (JsonElement limitRecord : limitsArray) {
			                          JsonObject limitJson =
			                            limitRecord.isJsonObject() ? limitRecord.getAsJsonObject()
			                            : new JsonObject();
			                             String limitId =
			                               limitJson.has(LIMITID) ? limitJson.get(LIMITID).getAsString() : "";
			                             String limitValue =
			                               limitJson.has(LIMITVALUE) ? limitJson.get(LIMITVALUE).getAsString()
			                               : "";
				                                
											if (existingActionslimits.contains(actionId))
												updateLimitQueries(changedActionLimitsQuery, decreasedLimitsQuery,
														existingTransactionLimitsMap, contractId, coreCustomerId, null,
														featureId, actionId, limitId, limitValue, isNewAction,
														legalEntityId);
											else
												changedActionLimitsQuery.append(getQueryString(contractId,
														coreCustomerId, null, featureId, actionId, isNewAction, limitId,
														limitValue, legalEntityId));

				                  }
				              }
				         }
				   }		                 
			          
			       removedCustomerFeaturesMap.put(coreCustomerId, removedFeaturesList);
	               addedCustomerFeaturesMap.put(coreCustomerId, addedFeaturesList);
	               existingCustomerFeaturesMap.put(coreCustomerId, existingFeaturesList);

	               removedCustomerActionsMap.put(coreCustomerId, removedActionsList);
	               addedCustomerActionsMap.put(coreCustomerId, addedActionsList);
	               existingCustomerActionsMap.put(coreCustomerId, existingActionsList);

	               removedCustomerFeaturesMap1.put(coreCustomerId, removedFeaturesList1);
	               addedCustomerFeaturesMap1.put(coreCustomerId, addedFeaturesList1);
	               existingCustomerFeaturesMap1.put(coreCustomerId, existingFeaturesList1);

	               removedCustomerActionsMap1.put(coreCustomerId, removedActionsList1);
	               addedCustomerActionsMap1.put(coreCustomerId, addedActionsList1);
	               existingCustomerActionsMap1.put(coreCustomerId, existingActionsList1);  

			}
		}
		createContractAccounts(contractCustomers, existingCustomersList, addedCustomerAccountsMap, contractId,
				legalEntityId, dcRequest);
		createContractFeatures(globalLevelPermissionsJsonArray, accountLevelPermissionsJsonArray, existingCustomersList, addedCustomerFeaturesMap, addedCustomerFeaturesMap1,contractId,
				serviceDefinitionType, DBPUtilitiesConstants.BOOLEAN_STRING_FALSE, legalEntityId, dcRequest);

		updateContractActions(changedActionLimitsQuery, decreasedLimitsQuery, dcRequest);
}
	private void updateContractActions(StringBuilder changedActionLimitsQuery, StringBuilder decreasedLimitsQuery,
			DataControllerRequest dcRequest) throws ApplicationException {
		logger.debug("HBL::ContractResourceImplExtn::updateContractActions:changedActionLimitsQuery:"+changedActionLimitsQuery+"decreasedLimitsQuery:"+decreasedLimitsQuery);
		ContractFeatureActionsBusinessDelegate contractFeatureActionBD = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(ContractFeatureActionsBusinessDelegate.class);
		if (StringUtils.isNotBlank(changedActionLimitsQuery) && changedActionLimitsQuery.length() > 0) {
			changedActionLimitsQuery.replace(changedActionLimitsQuery.length() - 1, changedActionLimitsQuery.length(),
					"");
			logger.debug("HBL::ContractResourceImplExtn::updateContractActions:createContractActionLimits:"+changedActionLimitsQuery);
			contractFeatureActionBD.createContractActionLimits(changedActionLimitsQuery, dcRequest.getHeaderMap());
		}

		if (StringUtils.isNotBlank(decreasedLimitsQuery) && decreasedLimitsQuery.length() > 0) {
			decreasedLimitsQuery.replace(decreasedLimitsQuery.length() - 1, decreasedLimitsQuery.length(), "");
			logger.debug("HBL::ContractResourceImplExtn::updateContractActions:decreaseUserLimits:"+decreasedLimitsQuery);
			contractFeatureActionBD.decreaseUserLimits(decreasedLimitsQuery, dcRequest.getHeaderMap());
		}

	}
	private Map<String, Set<String>> createContractFeatures(JsonArray globalLevelPermissionsJsonArray,JsonArray accountLevelPermissionsJsonArray,
            Set<String> validContractCustomers, Map<String, Set<String>> coreCustomersFeaturesMapToCreate,
            Map<String, Set<String>> coreCustomeraccountFeaturesMapToCreate,
            String contractId, String serviceDefinitionType, String isDefaultActionsEnabled, String legalEntityId,
            DataControllerRequest dcRequest) throws ApplicationException {

        
        //JsonArray contractCustomersArray = parser.parse(contractCustomers).getAsJsonArray();

        Map<String, Set<String>> customerFeaturesCreatedMap = new HashMap<>();
        ContractFeatureActionsBusinessDelegate contractFeatureActionBD = DBPAPIAbstractFactoryImpl
                .getBusinessDelegate(ContractFeatureActionsBusinessDelegate.class);
        Set<String> customerFeatures = new HashSet<>();
        String coreCustomerId= null;
        for (JsonElement customerElement : globalLevelPermissionsJsonArray) {
            JsonObject customerJson = customerElement.isJsonObject() ? customerElement.getAsJsonObject()
                    : new JsonObject();
            if (JSONUtil.hasKey(customerJson, CORECUSTOMERID)
                    && validContractCustomers.contains(JSONUtil.getString(customerJson, CORECUSTOMERID))
                    && (coreCustomersFeaturesMapToCreate == null || coreCustomersFeaturesMapToCreate
                            .containsKey(JSONUtil.getString(customerJson, CORECUSTOMERID)))) {
                coreCustomerId = JSONUtil.getString(customerJson, CORECUSTOMERID);
                JsonArray featuresArray = customerJson.get(FEATURES).getAsJsonArray();
                for (JsonElement featureElement : featuresArray) {
                    JsonObject featureJson = featureElement.isJsonObject() ? featureElement.getAsJsonObject()
                            : new JsonObject();
                    String featureId = featureJson.has(FEATUREID) ? featureJson.get(FEATUREID).getAsString() : "";
                    JsonArray actionsArray = featureJson.has(permissions) && featureJson.get(permissions).isJsonArray()
                            ? featureJson.get(permissions).getAsJsonArray()
                            : new JsonArray();
                    if (StringUtils.isNotBlank(featureId)
                            && (actionsArray.size() > 0 || DBPUtilitiesConstants.BOOLEAN_STRING_TRUE
                                    .equalsIgnoreCase(isDefaultActionsEnabled))
                            && (coreCustomersFeaturesMapToCreate == null
                                    || coreCustomersFeaturesMapToCreate.get(coreCustomerId).contains(featureId))) {
                        customerFeatures.add(featureId);
                    }

                }

            }

        }

        for (JsonElement customerElement : accountLevelPermissionsJsonArray) {
            JsonObject customerJson = customerElement.isJsonObject() ? customerElement.getAsJsonObject()
                    : new JsonObject();
            if (JSONUtil.hasKey(customerJson, CORECUSTOMERID)
                    && validContractCustomers.contains(JSONUtil.getString(customerJson, CORECUSTOMERID))
                    && (coreCustomersFeaturesMapToCreate == null || coreCustomersFeaturesMapToCreate
                            .containsKey(JSONUtil.getString(customerJson, CORECUSTOMERID)))
                    && JSONUtil.hasKey(customerJson, ACCOUNTS) && customerJson.get(ACCOUNTS).isJsonArray()) {
                coreCustomerId = JSONUtil.getString(customerJson, CORECUSTOMERID);
                JsonArray accountsArray1 = customerJson.get(ACCOUNTS).getAsJsonArray();
                for (JsonElement accountsElement1 : accountsArray1) {
                    JsonObject accountObject = accountsElement1.isJsonObject() ? accountsElement1.getAsJsonObject()
                            : new JsonObject();
                    JsonArray featuresArray = accountObject.get(featurePermissions).getAsJsonArray();
                  
                    for (JsonElement featureElement : featuresArray) {
                        JsonObject featureJson = featureElement.isJsonObject() ? featureElement.getAsJsonObject()
                                : new JsonObject();
                        String featureId = featureJson.has(FEATUREID) ? featureJson.get(FEATUREID).getAsString() : "";
                        JsonArray actionsArray =
                                featureJson.has(permissions) && featureJson.get(permissions).isJsonArray()
                                        ? featureJson.get(permissions).getAsJsonArray()
                                        : new JsonArray();
                        if (StringUtils.isNotBlank(featureId)
                                && (actionsArray.size() > 0 || DBPUtilitiesConstants.BOOLEAN_STRING_FALSE
                                        .equalsIgnoreCase(isDefaultActionsEnabled))
                                && (coreCustomeraccountFeaturesMapToCreate == null
                                        || coreCustomeraccountFeaturesMapToCreate.get(coreCustomerId)
                                                .contains(featureId))) {
                            customerFeatures.add(featureId);
                        }

                    }

                }

            }
        }

                Set<String> createdCustomerFeatures = contractFeatureActionBD.createContractFeatures(customerFeatures,
                        contractId, coreCustomerId, serviceDefinitionType, isDefaultActionsEnabled,
                        legalEntityId, dcRequest.getHeaderMap());
                if (createdCustomerFeatures != null && !createdCustomerFeatures.isEmpty()) {
                    customerFeaturesCreatedMap.put(coreCustomerId, createdCustomerFeatures);
                }

        return customerFeaturesCreatedMap;
    }
	
	 @Override
	    public Result createContract(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
	            DataControllerResponse dcResponse) throws ApplicationException {
	        Result result = new Result();
	        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
	        String contractId;
	        Set<String> validContractCustomers;
	        try {
	            String contractName = inputParams.get(CONTRACT_NAME);
	            String serviceDefinitionId = inputParams.get(SERVICE_DEFINITION_ID);
	            String faxId = inputParams.get(FAX_ID);
	            String communication = inputParams.get(COMMUNICATION);
	            String address = inputParams.get(ADDRESS);
	            String contractCustomers = inputParams.get(CONTRACTCUSTOMERS);
	            String isDefaultActionsEnabled = dcRequest.getParameter(ISDEFAULTACTIONSENABLED);
	            String accountLevelPermissions = inputParams.get("accountLevelPermissions1");
	            String globalLevelPermissions = inputParams.get("globalLevelPermissions1");
	            String transactionLimits = inputParams.get("transactionLimits1");
	            String legalEntityId = StringUtils.isNotBlank(inputParams.get(LEGAL_ENTITY_ID)) ? inputParams.get(LEGAL_ENTITY_ID)
	    				: dcRequest.getParameter(LEGAL_ENTITY_ID); 
	            JsonArray accountLevelPermissionsJsonArray = new JsonArray();
	            JsonArray globalLevelPermissionsJsonArray = new JsonArray();
	            JsonArray transactionLimitsJsonArray = new JsonArray();

	            if (StringUtils.isBlank(isDefaultActionsEnabled)) {
	                isDefaultActionsEnabled = DBPUtilitiesConstants.BOOLEAN_STRING_FALSE;
	            }
	            
	            diagnostic.prepareDebug("isDefaultActionsEnabled flag is set to...." + isDefaultActionsEnabled).log();
	            diagnostic.prepareDebug("legalEntityId is set to...." + legalEntityId).log();


	            if (StringUtils.isBlank(contractName) || StringUtils.isBlank(serviceDefinitionId)
	                    || StringUtils.isBlank(contractCustomers)) {
	                throw new ApplicationException(ErrorCodeEnum.ERR_10348);

	            }
	            
	            if (DBPUtilitiesConstants.BOOLEAN_STRING_FALSE.equals(isDefaultActionsEnabled)) {
	                if (StringUtils.isBlank(accountLevelPermissions) || StringUtils.isBlank(globalLevelPermissions)
	                        || StringUtils.isBlank(transactionLimits))
	                    throw new ApplicationException(ErrorCodeEnum.ERR_10348);
	                else {
	                    accountLevelPermissionsJsonArray = JSONUtil.parseAsJsonArray(accountLevelPermissions);
	                    globalLevelPermissionsJsonArray = JSONUtil.parseAsJsonArray(globalLevelPermissions);
	                    transactionLimitsJsonArray = JSONUtil.parseAsJsonArray(transactionLimits);
	                }
	            }
	            
	            if (StringUtils.isBlank(legalEntityId)){
	            	throw new ApplicationException(ErrorCodeEnum.ERR_29040);
	            }

	            String serviceDefinitionType = getServiceType(serviceDefinitionId, dcRequest);

	            if (StringUtils.isBlank(serviceDefinitionType)) {
	                throw new ApplicationException(ErrorCodeEnum.ERR_10349);
	            }

	            boolean isNotExists = verifyContractName(null, contractName, dcRequest);

	            if (!isNotExists) {
	                throw new ApplicationException(ErrorCodeEnum.ERR_10401);
	            }

	            validContractCustomers = getValidContractCustomers(contractCustomers, true, legalEntityId, dcRequest);
	            if (validContractCustomers == null || validContractCustomers.isEmpty()) {
	                throw new ApplicationException(ErrorCodeEnum.ERR_10368);
	            }

	            checkIfPayloadContainsValidAccountsForAllCustomers(contractCustomers, null, dcRequest);

	            List<AddressDTO> addressList = DTOUtils.getDTOList(address, AddressDTO.class);

	            String contractStatus = getAutoApprovalStatus(isDefaultActionsEnabled, dcRequest);

	            ContractDTO contractDTO = createContract(contractName, serviceDefinitionId, serviceDefinitionType, faxId,
	                    contractStatus, legalEntityId, dcRequest);
	            diagnostic.prepareDebug("contract table is populated").log();
	            contractId = contractDTO.getId();
	            createContractCommunication(communication, contractId, legalEntityId, dcRequest);
	            diagnostic.prepareDebug("ContractCommunication table is populated").log();
	            createContractAddress(addressList, contractId, legalEntityId, dcRequest);
	            diagnostic.prepareDebug("ContractAddress table is populated").log();
	            Set<String> createdValidContractCustomers = createContractCustomers(contractCustomers,
	                    validContractCustomers, contractId, legalEntityId, dcRequest);
	            diagnostic.prepareDebug("ContractCustomers table is populated. Valid ContractCustomers count : "
	                    +createdValidContractCustomers.size()).log();

	            Map<String, Set<ContractAccountsDTO>> createdCustomerAccounts = createContractAccounts(contractCustomers,
	                    createdValidContractCustomers, null, contractId, legalEntityId, dcRequest);
	            diagnostic.prepareDebug("Contractaccount table is populated").log();
	            createExcludedContractAccounts(contractCustomers, contractId, legalEntityId, dcRequest);
	            diagnostic.prepareDebug("excludedContractaccount table is populated").log();

				/*
				 * Map<String, Set<String>> featuresCreated =
				 * createContractFeatures(contractCustomers, createdValidContractCustomers,
				 * null, contractId, serviceDefinitionType, isDefaultActionsEnabled, dcRequest);
				 */
	            if (!isValidContractActionLimits(transactionLimitsJsonArray, validContractCustomers, contractId, serviceDefinitionType,
	                    dcRequest)) {
	            	diagnostic.prepareDebug("ContractActionLimits validation failed").log();
	                throw new ApplicationException(ErrorCodeEnum.ERR_10420);
	            }
				/*
				 * if (DBPUtilitiesConstants.BOOLEAN_STRING_FALSE.equalsIgnoreCase(
				 * isDefaultActionsEnabled)) { createContractActionLimits(contractCustomers,
				 * featuresCreated, contractId, serviceDefinitionType, dcRequest); }
				 */
	            if (DBPUtilitiesConstants.BOOLEAN_STRING_TRUE.equals(isDefaultActionsEnabled)) {
	                createContractDefaultFeatureActionLimits(contractCustomers, createdValidContractCustomers,
	                        createdCustomerAccounts, null, contractId, serviceDefinitionType, "true", serviceDefinitionId,
	                        legalEntityId, dcRequest);
	                ContractFeatureActionsBusinessDelegate contractFeatureActionBusinessDelegate = DBPAPIAbstractFactoryImpl
	                        .getBusinessDelegate(ContractFeatureActionsBusinessDelegate.class);
	                contractFeatureActionBusinessDelegate.createDefaultContractActionLimits(contractId,
	                		legalEntityId, dcRequest.getHeaderMap());
	            } else {
	                JsonObject contractActionObj = new JsonObject();
	                contractActionObj.add("accountLevelPermissions", accountLevelPermissionsJsonArray);
	                contractActionObj.add("globalLevelPermissions", globalLevelPermissionsJsonArray);
	                contractActionObj.add("transactionLimits", transactionLimitsJsonArray);
	                contractActionObj.addProperty("serviceDefinitionType", serviceDefinitionType);

	                ContractBusinessDelegate ContractBusinessDelegate = DBPAPIAbstractFactoryImpl
	                        .getBusinessDelegate(ContractBusinessDelegate.class);
	                ContractBusinessDelegate.createContractActionLimit(contractActionObj, contractId,
	                		legalEntityId, dcRequest.getHeaderMap());

	            }
	            diagnostic.prepareDebug("contract actions created").log();
	            createDefaultApprovalMatrixEntry(contractCustomers, contractId, createdValidContractCustomers, dcRequest, legalEntityId);
	            diagnostic.prepareDebug("Default Approval Matrix created").log();
	            result.addParam(new Param(CONTRACT_ID, contractId, DBPUtilitiesConstants.STRING_TYPE));
	            result.addParam(new Param("status", "success", DBPUtilitiesConstants.STRING_TYPE));

	            // Below parameters are added in request to use while enrolling the contract
	            dcRequest.setAttribute("serviceType", serviceDefinitionType);
	            dcRequest.setAttribute("createdValidCustomers", createdValidContractCustomers);
	            dcRequest.setAttribute("createdCustomerAccounts", createdCustomerAccounts);
	            dcRequest.setAttribute("createdCustomerAccounts", createdCustomerAccounts);
	            dcRequest.setAttribute("contractStatus", contractDTO.getStatusId());
	            dcRequest.setAttribute("contractId", contractId);
	            
	        } catch (ApplicationException e) {
	        	alert.prepareError("Error occured", e).log();
	            throw new ApplicationException(e.getErrorCodeEnum());
	        } catch (Exception e) {
	            alert.prepareError("Error occured", e).log();
	            throw new ApplicationException(ErrorCodeEnum.ERR_10351);

	        }
	        return result;
	    }
	 private Set<String> getValidContractCustomers(String contractCustomers,
	            boolean checkIfAtleastOnePrimaryCustomerInGivenList, String legalEntityId, DataControllerRequest dcRequest)
	            throws ApplicationException {
	        boolean isAtleastaCustomerisPrimary = false;
	        
	      
	        Set<String> givenCustomers = new HashSet<>();
	        
	        JsonArray contractCustomersArray = JSONUtil.parseAsJsonArray(contractCustomers);
	        ContractCoreCustomerBusinessDelegate customerBD = DBPAPIAbstractFactoryImpl
	                .getBusinessDelegate(ContractCoreCustomerBusinessDelegate.class);
	        for (JsonElement customerElement : contractCustomersArray) {
	            if (JSONUtil.isJsonNotNull(customerElement) && customerElement.isJsonObject()) {
	                JsonObject customerJson = customerElement.getAsJsonObject();
	                String customerId = customerJson.has(CORECUSTOMERID) ? customerJson.get(CORECUSTOMERID).getAsString()
	                        : "";
	                String isPrimary = customerJson.has(ISPRIMARY) ? customerJson.get(ISPRIMARY).getAsString() : "";
	                if (StringUtils.isNotBlank(customerId)) {
	                    givenCustomers.add(customerId);
	                }
	                if (StringUtils.isNotBlank(customerId) && !isAtleastaCustomerisPrimary
	                        && "true".equalsIgnoreCase(isPrimary)) {
	                    isAtleastaCustomerisPrimary = true;
	                }

	            }
	        }
	        if (checkIfAtleastOnePrimaryCustomerInGivenList && !isAtleastaCustomerisPrimary) {
	        	diagnostic.prepareDebug("ContractCustomers validation failed "
	        			+ "[checkIfAtleastOnePrimaryCustomerInGivenList, isAtleastaCustomerisPrimary] : "
	        			+checkIfAtleastOnePrimaryCustomerInGivenList+", "+isAtleastaCustomerisPrimary).log();
	            throw new ApplicationException(ErrorCodeEnum.ERR_10369);
	        }
	        return customerBD.getValidCoreContractCustomers(givenCustomers, legalEntityId, dcRequest.getHeaderMap());
	    }
	 private String getAutoApprovalStatus(String isDefaultActionsEnabled, DataControllerRequest dcRequest)
				throws ApplicationException {
			if (DBPUtilitiesConstants.BOOLEAN_STRING_TRUE.equalsIgnoreCase(isDefaultActionsEnabled)) {
				BusinessConfigurationBusinessDelegate configurationBD = DBPAPIAbstractFactoryImpl.getInstance()
						.getFactoryInstance(BusinessDelegateFactory.class)
						.getBusinessDelegate(BusinessConfigurationBusinessDelegate.class);

				return configurationBD.getAutoApprovalStatus(dcRequest.getHeaderMap());
			}
			return DBPUtilitiesConstants.CONTRACT_STATUS_ACTIVE;
		}
	 private ContractDTO createContract(String contractName, String serviceDefinitionId, String serviceDefinitionType,
	            String faxId, String contractStatus, String legalEntityId, DataControllerRequest dcRequest) throws ApplicationException {
	        ContractDTO contractDTO = new ContractDTO();
	        contractDTO.setId(HelperMethods.generateUniqueContractId(dcRequest));
	        contractDTO.setName(contractName);
	        contractDTO.setServicedefinitionId(serviceDefinitionId);
	        contractDTO.setServiceType(serviceDefinitionType);
	        contractDTO.setFaxId(faxId);
	        contractDTO.setStatusId(contractStatus);
	        contractDTO.setCompanyLegalUnit(legalEntityId);

	        ContractBusinessDelegate contractBD = DBPAPIAbstractFactoryImpl
	                .getBusinessDelegate(ContractBusinessDelegate.class);
	        return contractBD.createContract(contractDTO, dcRequest.getHeaderMap());

	    }
	 private boolean isValidContractActionLimits(JsonArray transactionLimitsJsonArray, Set<String> validContractCustomers,
	    		String contractId, String serviceDefinitionType, DataControllerRequest dcRequest)
	    				throws ApplicationException {
	    	//JsonParser parser = new JsonParser();
	    	//JsonArray contractCustomersArray = parser.parse(contractCustomers).getAsJsonArray();

	    	for (JsonElement customerElement : transactionLimitsJsonArray) {
	    		JsonObject customerJson =
	    				customerElement.isJsonObject() ? customerElement.getAsJsonObject() : new JsonObject();
	    		if (JSONUtil.hasKey(customerJson, CORECUSTOMERID)
	    				&& validContractCustomers.contains(JSONUtil.getString(customerJson, CORECUSTOMERID))) {
	    			JsonArray featuresArray = customerJson.get(featurePermissions).getAsJsonArray();
	    			for (JsonElement featureElement : featuresArray) {
	    				JsonObject featureJson =
	    						featureElement.isJsonObject() ? featureElement.getAsJsonObject() : new JsonObject();
						
	    				String actionId = featureJson.has(ACTIONID) ? featureJson.get(ACTIONID).getAsString() : "";
	    			    JsonArray limitsArray = featureJson.has(LIMITS) && featureJson.get(LIMITS).isJsonArray()
	    							? featureJson.get(LIMITS).getAsJsonArray()
	    							: new JsonArray();
	    				 
	    				 if (StringUtils.isNotBlank(actionId)) {

	    						double max_txn_limit_value=0, daily_limit_value = 0, weekly_limit_value=0;

	    						for (JsonElement array_position : limitsArray) {

	    							JsonObject limitobject = array_position.isJsonObject() ? array_position.getAsJsonObject():new JsonObject();

	    							if(limitobject.has(LIMITID) && limitobject.get(LIMITID).getAsString().equals(MAX_TRANSACTION_LIMIT)) {

	    								max_txn_limit_value = Double.parseDouble(limitobject.get(LIMITVALUE).getAsString());

	    							}
	    							else if(limitobject.has(LIMITID) && limitobject.get(LIMITID).getAsString().equals(DAILY_LIMIT)) {

	    								daily_limit_value = Double.parseDouble(limitobject.get(LIMITVALUE).getAsString());
	    							}
	    							else if(limitobject.has(LIMITID) && limitobject.get(LIMITID).getAsString().equals(WEEKLY_LIMIT)) {

	    								weekly_limit_value = Double.parseDouble(limitobject.get(LIMITVALUE).getAsString());
	    							}

	    						}
	    						if((max_txn_limit_value > daily_limit_value) || (daily_limit_value>weekly_limit_value)) {
	    							diagnostic.prepareDebug("limit validation failed {max_txn_limit_value, daily_limit_value, weekly_limit_value} "
	    									+"{"+max_txn_limit_value + ", "+ daily_limit_value+", "+weekly_limit_value+"}").log();
	    							return false;
	    						}

	    					}

	    				}

	    			}

	    		}


	    	return true;
	    } 
	 private Map<String, Set<String>> createContractDefaultFeatureActionLimits(String contractCustomers,
	            Set<String> validContractCustomers, Map<String, Set<ContractAccountsDTO>> createdCustomerAccounts,
	            Map<String, Set<String>> coreCustomersFeaturesMapToCreate, String contractId, String serviceDefinitionType,
	            String isDefaultActionsEnabled, String serviceDefinitionId, String legalEntityId, DataControllerRequest dcRequest)
	            throws ApplicationException {

	        JsonArray contractCustomersArray = JSONUtil.parseAsJsonArray(contractCustomers);

	        Map<String, Set<String>> customerFeaturesCreatedMap = new HashMap<>();
	        ContractFeatureActionsBusinessDelegate contractFeatureActionBD = DBPAPIAbstractFactoryImpl
	                .getBusinessDelegate(ContractFeatureActionsBusinessDelegate.class);

	        for (JsonElement customerElement : contractCustomersArray) {
	            JsonObject customerJson = customerElement.isJsonObject() ? customerElement.getAsJsonObject()
	                    : new JsonObject();
	            if (JSONUtil.hasKey(customerJson, CORECUSTOMERID)
	                    && validContractCustomers.contains(JSONUtil.getString(customerJson, CORECUSTOMERID))
	                    && (coreCustomersFeaturesMapToCreate == null || coreCustomersFeaturesMapToCreate
	                            .containsKey(JSONUtil.getString(customerJson, CORECUSTOMERID)))
	                    && JSONUtil.hasKey(customerJson, FEATURES) && customerJson.get(FEATURES).isJsonArray()) {
	                String coreCustomerId = JSONUtil.getString(customerJson, CORECUSTOMERID);
	                JsonArray featuresArray = customerJson.get(FEATURES).getAsJsonArray();
	                Set<String> customerFeatures = new HashSet<>();
	                for (JsonElement featureElement : featuresArray) {
	                    JsonObject featureJson = featureElement.isJsonObject() ? featureElement.getAsJsonObject()
	                            : new JsonObject();
	                    String featureId = featureJson.has(FEATUREID) ? featureJson.get(FEATUREID).getAsString() : "";
	                    JsonArray actionsArray = featureJson.has(ACTIONS) && featureJson.get(ACTIONS).isJsonArray()
	                            ? featureJson.get(ACTIONS).getAsJsonArray()
	                            : new JsonArray();
	                    if (StringUtils.isNotBlank(featureId)
	                            && (actionsArray.size() > 0 || DBPUtilitiesConstants.BOOLEAN_STRING_TRUE
	                                    .equalsIgnoreCase(isDefaultActionsEnabled))
	                            && (coreCustomersFeaturesMapToCreate == null
	                                    || coreCustomersFeaturesMapToCreate.get(coreCustomerId).contains(featureId))) {
	                        customerFeatures.add(featureId);
	                    }
	                }
	                Set<String> createdCustomerFeatures = contractFeatureActionBD.createContractFeatures(customerFeatures,
	                        contractId, coreCustomerId, serviceDefinitionType, isDefaultActionsEnabled,
	                        legalEntityId, dcRequest.getHeaderMap());
	                if (createdCustomerFeatures != null && !createdCustomerFeatures.isEmpty()) {
	                    customerFeaturesCreatedMap.put(coreCustomerId, createdCustomerFeatures);
	                }
	            }
	        }
	        return customerFeaturesCreatedMap;
	    }	
	 private void getDistinctCustomerAccounts(List<String> customerAccountsPayloadsList, String legalEntityId,
				DataControllerRequest dcRequest, Map<String, String> customerAccountMap){
			try {
		    	Map<String, Object> inputParams = new HashMap<String, Object>();
		    	StringBuilder fetchDistinctCustAccountsPayloadString = new StringBuilder();
		    	
				for (String payload : customerAccountsPayloadsList) {
					fetchDistinctCustAccountsPayloadString.append("|").append(payload);
				}
				inputParams.put("_queryInput", fetchDistinctCustAccountsPayloadString.toString().substring(1));
				inputParams.put("_companyLegalunit", legalEntityId);
				
				
				JsonObject infinityJobJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, dcRequest.getHeaderMap(),
						URLConstants.FETCH_DISTINCT_CUSTOMERACCOUNTS);
				
				JsonArray jsonArray = JSONUtil.getJsonArrary(infinityJobJson, "records");
				for (int j = 0; j < jsonArray.size(); j++) {
					JsonObject jsonObject = jsonArray.get(j).getAsJsonObject();
					String customerId = JSONUtil.getString(jsonObject, InfinityConstants.customer_id);
					String accountId = JSONUtil.getString(jsonObject, InfinityConstants.account_id);
					customerAccountMap.put(customerId, accountId);
				}

			} catch (Exception e) {
				alert.prepareError("Error Occured while fetching customer accounts",e.getMessage()).log();
			}
		}
		
	 @Override
		public Result getListOfContractsByStatus(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
				DataControllerResponse dcResponse) throws ApplicationException {
			Result result = new Result();
			try {
				Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
				String statusId = StringUtils.isNotBlank(inputParams.get(STATUSID)) ? inputParams.get(STATUSID)
						: dcRequest.getParameter(STATUSID);

				String legalEntityId = StringUtils.isNotBlank(inputParams.get(LEGAL_ENTITY_ID)) ? inputParams.get(LEGAL_ENTITY_ID)
						: dcRequest.getParameter(LEGAL_ENTITY_ID);
				
				if(StringUtils.isBlank(legalEntityId)) {
					result = ErrorCodeEnum.ERR_29040.setErrorCode(result);
					return result;
				}
				
				ContractBusinessDelegate contractBD = DBPAPIAbstractFactoryImpl.getInstance()
						.getFactoryInstance(BusinessDelegateFactory.class)
						.getBusinessDelegate(ContractBusinessDelegate.class);
				ContractBackendDelegateImplExtn extn = new ContractBackendDelegateImplExtn();
				//List<ContractDTO> dtoList = contractBD.getListOfContractsByStatus(statusId, legalEntityId, dcRequest.getHeaderMap());
				List<ContractDTOExtn> dtoList = extn.getListOfContractsByStatusNew(statusId, legalEntityId, dcRequest.getHeaderMap());
				if (null != dtoList) {
					//String contractString = JSONUtils.stringifyCollectionWithTypeInfo(dtoList, ContractDTO.class);
					String contractString = JSONUtils.stringifyCollectionWithTypeInfo(dtoList, ContractDTOExtn.class);
					contractString = HelperMethods.replaceCompanyLegalUnitWithLegalEntityId(contractString);
					JSONArray contractArray = new JSONArray(contractString);
					//contractArray=processChannelAccess(contractArray);
					JSONObject contractJson = new JSONObject();
					contractJson.put(DBPDatasetConstants.DATASET_CONTRACT, contractArray);
					result = ConvertJsonToResult.convert(contractJson.toString());
				}
			} catch (ApplicationException e) {
				throw new ApplicationException(e.getErrorCodeEnum());

			} catch (Exception e) {
				throw new ApplicationException(ErrorCodeEnum.ERR_10379);

			}
			return result;
		}

		public JSONArray processChannelAccess(JSONArray contractArray) {
			JSONArray newContractArray = new JSONArray();
			if (contractArray != null && contractArray.length() > 0) {
				for (int i = 0; i < contractArray.length(); i++) {
					JSONObject obj = contractArray.getJSONObject(i);
					if(StringUtils.isNotBlank(obj.optString("channelAccess"))) {
					obj.put("requestedChannelAccess", obj.getString("channelAccess")); 
					obj.put("submittedFrom", obj.getString("faxId"));
					}
					newContractArray.put(obj);
				}
				return newContractArray;
			} else {
				return contractArray;
			}
		}
		@Override
		public Result getContractDetails(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
				DataControllerResponse dcResponse) throws ApplicationException {
			Result result = new Result();
			Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
			String contractId = inputParams.get(CONTRACT_ID);
			String legalEntityId ="";

			if (StringUtils.isBlank(contractId)) {
				contractId = getPrimaryContractId(dcRequest, HelperMethods.getCustomerIdFromSession(dcRequest));
			}

			if (StringUtils.isBlank(contractId)) {
				throw new ApplicationException(ErrorCodeEnum.ERR_10360);
			}
			
			Boolean isSuperAdmin = false;
							
			Map<String, String> loggedInUserInfo = HelperMethods.getCustomerFromAPIDBPIdentityService(dcRequest);
			if (!HelperMethods.isAuthenticationCheckRequiredForService(loggedInUserInfo)) {
					isSuperAdmin =  true;
			}
			
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
			try {
				ContractBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl
						.getBusinessDelegate(ContractBusinessDelegate.class);
				ContractDTO contractDTO = businessDelegate.getCompleteContractDetails(contractId, legalEntityId, dcRequest.getHeaderMap());

				if (null != contractDTO) {
					String contractString = JSONUtils.stringify(contractDTO);
					JsonParser parser = new JsonParser();
					JsonObject resultJson = formatContractDetailsRespnse(parser.parse(contractString).getAsJsonObject());
	                updateJobStatus(resultJson, dcRequest.getHeaderMap());
					result = JSONToResult.convert(HelperMethods.replaceCompanyLegalUnitWithLegalEntityId(resultJson.toString()));
					contractDTO = new ContractDTO();
					contractDTO.setId(contractId);
					ContractDTOExtn contractExtn = getContractDetails(contractDTO, dcRequest.getHeaderMap());
					if(contractExtn.getChannelAccess()!=null) {
						result.addParam(new Param("requestedChannelAccess", contractExtn.getChannelAccess()));
						result.addParam(new Param("submittedFrom", contractExtn.getFaxId()));
					}
				}

			} catch (ApplicationException e) {
				throw new ApplicationException(e.getErrorCodeEnum());
			} catch (Exception e) {
				throw new ApplicationException(ErrorCodeEnum.ERR_10363);
			}

			return result;
		}
		 public ContractDTOExtn getContractDetails(ContractDTO contractDTO, Map<String, Object> headersMap)
		            throws ApplicationException {

		        Map<String, Object> inputParams = new HashMap<>();
		        ContractDTOExtn resultContractDTO = null;
		        StringBuilder filter = new StringBuilder();
		        if (StringUtils.isNotBlank(contractDTO.getId())) {
		            filter = filter.append(CONTRACT_ID_DB + DBPUtilitiesConstants.EQUAL + contractDTO.getId());
		        }
		        if (StringUtils.isNotBlank(contractDTO.getName()) && StringUtils.isNotBlank(filter)) {
		            filter.append(DBPUtilitiesConstants.AND);
		        }
		        if (StringUtils.isNotBlank(contractDTO.getName())) {
		            filter.append(CONTRACT_NAME_DB + DBPUtilitiesConstants.EQUAL + "'" + contractDTO.getName() + "'");
		        }
		        inputParams.put(DBPUtilitiesConstants.FILTER, filter);
		        JsonObject contractJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
		                URLConstants.CONTRACT_GET);

		        if (null != contractJson && JSONUtil.hasKey(contractJson, DBPDatasetConstants.DATASET_CONTRACT)
		                && contractJson.get(DBPDatasetConstants.DATASET_CONTRACT).isJsonArray()) {
		            JsonArray array = contractJson.get(DBPDatasetConstants.DATASET_CONTRACT).getAsJsonArray();
		            JsonElement element = array.size() > 0 ? array.get(0) : new JsonObject();
		            resultContractDTO = (ContractDTOExtn) DTOUtils.loadJsonObjectIntoObject(element.getAsJsonObject(),
		            		ContractDTOExtn.class, false);
		        }
		        return resultContractDTO;
		    }
		private JsonObject formatContractDetailsRespnse(JsonObject contractJson) {
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
		 private void updateJobStatus(JsonObject resultJson, Map<String, Object> headerMap) {
				
		    	String servicedefinitionId = resultJson.has(InfinityConstants.servicedefinitionId) && 
		    			!resultJson.get(InfinityConstants.servicedefinitionId).isJsonNull()
		    			?resultJson.get(InfinityConstants.servicedefinitionId).getAsString() : "";
				if(StringUtils.isNoneBlank(servicedefinitionId)) {
					if(isServiceDefintionEditInProgress(servicedefinitionId, headerMap)) {
						resultJson.addProperty(InfinityConstants.updateJobStatus, InfinityConstants.jobStatus.SID_JOB_INPROGRESS.toString());
					}
					else{
						resultJson.addProperty(InfinityConstants.updateJobStatus, InfinityConstants.jobStatus.SID_JOB_COMPLETED.toString());
					}
				}
			}
		 private boolean isServiceDefintionEditInProgress(String serviceDefinitonId, Map<String, Object> headerMap) {
				Map<String, Object> inputParams = new HashMap<String, Object>();
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty(InfinityConstants.serviceDefinitionId, serviceDefinitonId);
				String filter = InfinityConstants.data + DBPUtilitiesConstants.EQUAL+ "'"+jsonObject.toString() +"'"+
						DBPUtilitiesConstants.AND+
						InfinityConstants.status + DBPUtilitiesConstants.EQUAL + InfinityConstants.jobStatus.SID_JOB_INPROGRESS.toString();
				inputParams.put(DBPUtilitiesConstants.FILTER, filter);
				JsonObject infinityJobJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
						URLConstants.INIFNITY_JOB_GET);
				if (null != infinityJobJson
						&& JSONUtil.hasKey(infinityJobJson, DBPDatasetConstants.DATASET_INFINITY_JOB)
						&& infinityJobJson.get(DBPDatasetConstants.DATASET_INFINITY_JOB).isJsonArray()) {
					JsonArray array = infinityJobJson.get(DBPDatasetConstants.DATASET_INFINITY_JOB).getAsJsonArray();
					if(array.size() >0) {
						return true;
					}
				}

				return false;
			}
		 private String getPrimaryContractId(DataControllerRequest dcRequest, String customerId) {

				BackendIdentifierDTO backendIdentifierDTO = new BackendIdentifierDTO();

				BackendIdentifiersBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl
						.getBackendDelegate(BackendIdentifiersBackendDelegate.class);
				backendIdentifierDTO.setCustomer_id(customerId);

				final String IS_Integrated = Boolean.toString(IntegrationTemplateURLFinder.isIntegrated);
				if (StringUtils.isNotBlank(IS_Integrated) && IS_Integrated.equalsIgnoreCase("true")) {
					backendIdentifierDTO
							.setBackendType(IntegrationTemplateURLFinder.getBackendURL(InfinityConstants.BackendType));
				} else {
					backendIdentifierDTO.setBackendType(DTOConstants.CORE);
				}

				DBXResult dbxResult = new DBXResult();
				try {
					dbxResult = backendDelegate.get(backendIdentifierDTO, dcRequest.getHeaderMap());
				} catch (ApplicationException e) {
					alert.prepareError("Exception", e).log();
				}

				String coreCustomerId = "";
				if (dbxResult.getResponse() != null) {
					backendIdentifierDTO = (BackendIdentifierDTO) dbxResult.getResponse();
					coreCustomerId = backendIdentifierDTO.getBackendId();
				}
				if (StringUtils.isBlank(coreCustomerId)) {
					return null;
				}

				String filter = InfinityConstants.customerId + DBPUtilitiesConstants.EQUAL + customerId
						+ DBPUtilitiesConstants.AND + InfinityConstants.coreCustomerId + DBPUtilitiesConstants.EQUAL
						+ coreCustomerId;
				Map<String, Object> input = new HashMap<String, Object>();
				input.put(DBPUtilitiesConstants.FILTER, filter);
				JsonObject jsonResponse = ServiceCallHelper.invokeServiceAndGetJson(input, dcRequest.getHeaderMap(),
						URLConstants.CONTRACT_CUSTOMERS_GET);
				if (jsonResponse.has(DBPDatasetConstants.DATASET_CONTRACT_CUSTOMERS)) {
					JsonElement jsonElement = jsonResponse.get(DBPDatasetConstants.DATASET_CONTRACT_CUSTOMERS);
					if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
						JsonArray jsonArray = jsonElement.getAsJsonArray();
						JsonObject contractCustomer = jsonArray.get(0).getAsJsonObject();
						return contractCustomer.has(InfinityConstants.contractId)
								&& !contractCustomer.get(InfinityConstants.contractId).isJsonNull()
										? contractCustomer.get(InfinityConstants.contractId).getAsString()
										: null;
					}
				}

				return null;
			}
		public JsonObject updateEnrollConent(Map<String, Object> inputParams, DataControllerRequest dcRequest) throws ApplicationException{
			boolean isSuccess=false;
			JsonObject resultObj = new JsonObject();
			try { 
			addT24Headers(dcRequest.getHeaderMap(), (String) inputParams.get("customerId"), (String) inputParams.get("legalEntityId"));
			resultObj = ServiceCallHelper.invokeServiceAndGetJson("HBL_T24ISUser", null, "updateEnrollConsent",inputParams, dcRequest.getHeaderMap());
			alert.prepareError("updateEnrollConent Response:"+resultObj).log();
			String errMsg= resultObj.get("errmsg")!=null?resultObj.get("errmsg").getAsString():"";
			if (!HelperMethods.hasError(resultObj))
			isSuccess=true; 
			else if(StringUtils.isNotBlank(errMsg)&& errMsg.equalsIgnoreCase("Record Not Changed")) {
				isSuccess=true; 
			}
			else
			isSuccess=false; 
			}catch (Exception e) {
				isSuccess=false; 
				 alert.prepareError("Exception Occured while updating EnrollConent status:",e).log();
				 resultObj.addProperty("errmsg", e.getMessage());
			}
			 resultObj.addProperty("isSuccess", isSuccess);
			return resultObj;
		}
		 public void addT24Headers(Map<String, Object> headersMap, String id, String legalEntityId) {
		        HelperMethods.addJWTAuthHeader(headersMap, AuthConstants.PRE_LOGIN_FLOW);
		        
		        if(StringUtils.isNotBlank(legalEntityId)) {
		        	headersMap.put("companyId", legalEntityId);
		        } else {
		        BackendIdentifierDTO backendIdentifierDTO = new BackendIdentifierDTO();
		        backendIdentifierDTO.setBackendId(id);
		        backendIdentifierDTO.setBackendType(IntegrationTemplateURLFinder.getBackendURL(InfinityConstants.BackendType));
		        backendIdentifierDTO = (BackendIdentifierDTO) backendIdentifierDTO.loadDTO();
		            if (backendIdentifierDTO != null && StringUtils.isNotBlank(backendIdentifierDTO.getCompanyLegalUnit())) {
		                headersMap.put("companyId", backendIdentifierDTO.getCompanyLegalUnit());
		                
		            } else {
		            	/*This is for fall back..*/
		                headersMap.put("companyId",
		                        EnvironmentConfigurationsHandler.getValue(DBPUtilitiesConstants.BRANCH_ID_REFERENCE));
		            }
		        }
		    }
		 private BackendIdentifierDTO getBackendIdFromContract(String contractId, DataControllerRequest request ) throws ApplicationException {
				String backendId="";
				BackendIdentifierDTO backendIdentifierDTO = new BackendIdentifierDTO();
				String legalEntityId=EnvironmentConfigurationsHandler.getValue(DBPUtilitiesConstants.BRANCH_ID_REFERENCE);
			    backendIdentifierDTO.setCompanyLegalUnit(legalEntityId);
			    backendIdentifierDTO.setBackendType("T24");
			    backendIdentifierDTO.setContractId(contractId);
				if(StringUtils.isNotBlank(backendIdentifierDTO.getContractId())) {
				BackendIdentifierBusinessDelegate backendIdentifierBusinessDelegate = DBPAPIAbstractFactoryImpl
						.getBusinessDelegate(BackendIdentifierBusinessDelegate.class);
				DBXResult backendIdentifiers = backendIdentifierBusinessDelegate.get(backendIdentifierDTO, request.getHeaderMap());
				if (backendIdentifiers != null && backendIdentifiers.getResponse() != null) {
					backendIdentifierDTO = (BackendIdentifierDTO) backendIdentifiers.getResponse();
					backendId= backendIdentifierDTO.getBackendId();
					
				}
				}
				return backendIdentifierDTO;
		 }

}
