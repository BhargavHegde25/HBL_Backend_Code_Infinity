package com.kony.adminconsole.service.contract.resource.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.core.security.LoggedInUserHandler;
import com.kony.adminconsole.handler.AuditHandler;
import com.kony.adminconsole.service.contract.businessdelegate.api.ContractBusinessDelegate;
import com.kony.adminconsole.service.contract.resource.api.ContractResource;
import com.kony.adminconsole.utilities.ActivityStatusEnum;
import com.kony.adminconsole.utilities.DBPServices;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.EventEnum;
import com.kony.adminconsole.utilities.ModuleNameEnum;
import com.kony.adminconsole.utilities.PermissionName;
import com.konylabs.middleware.api.processor.IdentityHandler;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class ContractResourceImpl implements ContractResource {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
    private static final String INPUT_CORE_CUSTOMER_ID_LIST = "coreCustomerIdList";
    private static final String INPUT_CORE_CUSTOMER_ID = "coreCustomerId";
    private static final String INPUT_CORE_CUSTOMER_NAME = "coreCustomerName";
    private static final String INPUT_CONTRACT_NAME = "contractName";
    private static final String INPUT_CONTRACT_ID = "contractId";
    private static final String INPUT_STATUS_ID = "statusId";
    private static final String INPUT_REJECTED_REASON = "rejectedReason";
    private static final String INPUT_CONTRACT_CUSTOMERS = "contractCustomers";
    private static final String INPUT_LEGALENTITY_ID = "legalEntityId";
    private static final String DELETED_CUSTOMERS = "deletedCustomers";

    private static final String INPUT_SERVICE_DEFINITION_NAME = "serviceDefinitionName";
    private static final String INPUT_SERVICE_DEFINITION_ID = "serviceDefinitionId";
    private static final String INPUT_COMMUNICATION = "communication";
    private static final String INPUT_ADDRESS = "address";
    private static final String INPUT_FAX_ID = "faxId";

    private static final String INPUT_ID = "id";
    private static final String INPUT_NAME = "name";
    private static final String INPUT_EMAIL = "email";
    private static final String INPUT_PHONE_NUMBER = "phoneNumber";
    private static final String INPUT_PHONE_COUNTRY_CODE = "phoneCountryCode";
    private static final String INPUT_DOB = "dob";
    private static final String INPUT_CUSTOMER_STATUS = "customerStatus";
    private static final String INPUT_COUNTRY = "country";
    private static final String INPUT_TOWN = "town";
    private static final String INPUT_ZIPCODE = "zipcode";
    private static final String CONTRACT_STATUS_REJECTED = "SID_CONTRACT_REJECTED";
    private static final String CONTRACT_STATUS_ACTIVE = "SID_CONTRACT_ACTIVE";
    private static final String INPUT_LEGAL_ENTITY_ID = "legalEntityId";

    ContractBusinessDelegate contractBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
            .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(ContractBusinessDelegate.class);

    @Override
    public Result getContractDetails(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse response) {

        Result result = new Result();
        try {
        	if (StringUtils.isBlank(requestInstance.getParameter("legalEntityId"))) {
                alert.prepareError("legalEntityId cannot be empty").log();
                ErrorCodeEnum.ERR_22232.setErrorCode(result);
                return result;
            }
            String[] reqPermissions = {PermissionName.VIEW_CONTRACT};
            if(!LoggedInUserHandler.hasAccessToLegalEntity(requestInstance,reqPermissions))
            {
                result.addParam(new Param("Status", "getContractDetails failed", FabricConstants.STRING));
                ErrorCodeEnum.ERR_22231.setErrorCode(result);
                alert.prepareError("Logged in user do not have access to this legalEntity ").log();
                return result;        
            }
            if (requestInstance.getParameter(INPUT_CONTRACT_ID) == null) {
                ErrorCodeEnum.ERR_21960.setErrorCode(result);
                return result;
            } else {
                String contractId = requestInstance.getParameter(INPUT_CONTRACT_ID);
                String legalEntityId = requestInstance.getParameter(INPUT_LEGAL_ENTITY_ID);
                Map<String, Object> postParametersMap = new HashMap<>();
                postParametersMap.put("contractId", contractId);
                postParametersMap.put("legalEntityId", legalEntityId);
                String dbpServicesClaimsToken = DBPServices.getDBPServicesClaimsToken(requestInstance);
                JSONObject getContractresponse =
                        contractBusinessDelegate.getContractDetails(postParametersMap, dbpServicesClaimsToken);
                if (getContractresponse == null || !getContractresponse.has(FabricConstants.OPSTATUS)
                        || getContractresponse.getInt(FabricConstants.OPSTATUS) != 0) {
                    ErrorCodeEnum.ERR_21012.setErrorCode(result);
                    result.addParam(new Param("status", "Failure", FabricConstants.STRING));
                    AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.CONTRACTS, EventEnum.SEARCH,
                            ActivityStatusEnum.FAILED, "Contract search Failed");
                    return result;
                } else if (getContractresponse.has("dbpErrMsg")) {
                	result = CommonUtilities.constructResultFromJSONObject(getContractresponse);
                	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
                    return result;
                } else {

                    for (String currKey : getContractresponse.keySet()) {
                        if (currKey.equals("address")) {
                            JSONArray addressArray = getContractresponse.getJSONArray("address");
                            Dataset recordsDataset = CommonUtilities.constructDatasetFromJSONArray(addressArray);
                            recordsDataset.setId("address");
                            result.addDataset(recordsDataset);
                        } else if (currKey.equals("contractCustomers")) {
                            JSONArray contractCustomersArray = getContractresponse.getJSONArray("contractCustomers");
                            Dataset recordsDataset =
                                    CommonUtilities.constructDatasetFromJSONArray(contractCustomersArray);
                            recordsDataset.setId("contractCustomers");
                            result.addDataset(recordsDataset);
                        } else if (currKey.equals("contractCustomers")) {
                            JSONArray contractCustomersArray = getContractresponse.getJSONArray("contractCustomers");
                            Dataset recordsDataset =
                                    CommonUtilities.constructDatasetFromJSONArray(contractCustomersArray);
                            recordsDataset.setId("contractCustomers");
                            result.addDataset(recordsDataset);
                        } else if (currKey.equals("communication")) {
                            JSONArray communicationArray = getContractresponse.getJSONArray("communication");
                            Dataset recordsDataset = CommonUtilities.constructDatasetFromJSONArray(communicationArray);
                            recordsDataset.setId("communication");
                            result.addDataset(recordsDataset);
                        } else {
                            result.addParam(new Param(currKey, getContractresponse.get(currKey).toString(),
                                    FabricConstants.STRING));
                        }
                    }
                    result.addParam(new Param("status", "Success", FabricConstants.STRING));
                    result.addParam(new Param("opstatus", getContractresponse.get("opstatus").toString(),
                            FabricConstants.STRING));

                    AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.CONTRACTS, EventEnum.SEARCH,
                            ActivityStatusEnum.SUCCESSFUL,
                            "Successfully fetched contract details. contractId= " + contractId);

                }
            }

        } catch (Exception e) {
            alert.prepareError("Unexepected Error in get contract details", e).log();
            result.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
            ErrorCodeEnum.ERR_21973.setErrorCode(result);
            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.CONTRACTS, EventEnum.SEARCH,
                    ActivityStatusEnum.FAILED, "Contract fetch Failed");
        }
        return result;

    }

    @Override
    public Result createContract(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse response) throws Exception {
        Result result = new Result();
        try {
 
                if (StringUtils.isBlank(requestInstance.getParameter("legalEntityId"))) {
                    alert.prepareError("legalEntityId cannot be empty").log();
                    ErrorCodeEnum.ERR_22232.setErrorCode(result);
                    return result;
                }
                String[] reqPermissions = {PermissionName.CREATE_CONTRACT};
                if(!LoggedInUserHandler.hasAccessToLegalEntity(requestInstance,reqPermissions))
                {
                    result.addParam(new Param("Status", "createContract failed", FabricConstants.STRING));
                    ErrorCodeEnum.ERR_22231.setErrorCode(result);
                    alert.prepareError("Logged in user do not have access to this legalEntity ").log();
                    return result;        
                }
            if (requestInstance.getParameter(INPUT_CONTRACT_NAME) == null) {
                ErrorCodeEnum.ERR_21960.setErrorCode(result);
                return result;
            } else if (requestInstance.getParameter(INPUT_SERVICE_DEFINITION_NAME) == null) {
                ErrorCodeEnum.ERR_21961.setErrorCode(result);
                return result;
            } else if (StringUtils.isBlank(requestInstance.getParameter(INPUT_SERVICE_DEFINITION_ID))) {
                ErrorCodeEnum.ERR_21961.setErrorCode(result);
                return result;
            } else if (requestInstance.getParameter(INPUT_COMMUNICATION) == null) {
                ErrorCodeEnum.ERR_21962.setErrorCode(result);
                return result;
            } else if (requestInstance.getParameter(INPUT_ADDRESS) == null) {
                ErrorCodeEnum.ERR_21963.setErrorCode(result);
                return result;
            } else if (requestInstance.getParameter(INPUT_CONTRACT_CUSTOMERS) == null) {
                ErrorCodeEnum.ERR_21964.setErrorCode(result);
                return result;
            } else if (requestInstance.getParameter(INPUT_LEGAL_ENTITY_ID) == null) {
            	ErrorCodeEnum.ERR_22232.setErrorCode(result);
            	return result;
            } else {
                String contractName = requestInstance.getParameter(INPUT_CONTRACT_NAME);
                String serviceDefinitionName = requestInstance.getParameter(INPUT_SERVICE_DEFINITION_NAME);
                String serviceDefinitionId = requestInstance.getParameter(INPUT_SERVICE_DEFINITION_ID);
                String communication = requestInstance.getParameter(INPUT_COMMUNICATION);
                String address = requestInstance.getParameter(INPUT_ADDRESS);
                String contractCustomers = requestInstance.getParameter(INPUT_CONTRACT_CUSTOMERS);
                String faxId = "";
                String accountLevelPermissions = requestInstance.getParameter("accountLevelPermissions1");
                String globalLevelPermissions = requestInstance.getParameter("globalLevelPermissions1");
                String transactionLimits = requestInstance.getParameter("transactionLimits1");

                if (StringUtils.isNotBlank(INPUT_FAX_ID)) {
                    faxId = requestInstance.getParameter(INPUT_FAX_ID);
                }
                String legalEntityId = requestInstance.getParameter(INPUT_LEGAL_ENTITY_ID);
                address = StringEscapeUtils.escapeHtml(address);
                Map<String, Object> postParametersMap = new HashMap<>();
                postParametersMap.put("contractName", contractName);
                postParametersMap.put("serviceDefinitionName", serviceDefinitionName);
                postParametersMap.put("serviceDefinitionId", serviceDefinitionId);
                postParametersMap.put("faxId", faxId);
                postParametersMap.put("communication", stringifyForVelocityTemplate(communication));
                postParametersMap.put("address", stringifyForVelocityTemplate(address));
                postParametersMap.put("contractCustomers", stringifyForVelocityTemplate(contractCustomers.toString()));
                postParametersMap.put("accountLevelPermissions1", stringifyForVelocityTemplate(accountLevelPermissions));
                postParametersMap.put("globalLevelPermissions1", stringifyForVelocityTemplate(globalLevelPermissions));
                postParametersMap.put("transactionLimits1", stringifyForVelocityTemplate(transactionLimits));
                postParametersMap.put("legalEntityId", legalEntityId);
                
                String dbpServicesClaimsToken = DBPServices.getDBPServicesClaimsToken(requestInstance);
                JSONObject createContractresponse =
                        contractBusinessDelegate.createContract(postParametersMap, dbpServicesClaimsToken);
                if (createContractresponse == null || !createContractresponse.has(FabricConstants.OPSTATUS)
                        || createContractresponse.getInt(FabricConstants.OPSTATUS) != 0) {
                    ErrorCodeEnum.ERR_21012.setErrorCode(result);
                    result.addParam(new Param("status", "Failure", FabricConstants.STRING));
                    return result;
                } else if (createContractresponse.has("dbpErrMsg")) {
                	result = CommonUtilities.constructResultFromJSONObject(createContractresponse);
                	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
                    return result;
                } else {
                    result.addParam(new Param("status", "Success", FabricConstants.STRING));
                    result.addParam(new Param("opstatus", createContractresponse.get("opstatus").toString(),
                            FabricConstants.STRING));
                    result.addParam(new Param("contractId", createContractresponse.getString("contractId"),
                            FabricConstants.STRING));
                }
            }
        } catch (Exception e) {
            alert.prepareError("Unexepected Error in create contract ", e).log();
            result.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
            ErrorCodeEnum.ERR_21967.setErrorCode(result);
        }
        return result;

    }

    @Override
    public Result editContract(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse response) throws Exception {
        Result result = new Result();
        try {
        	
        	if (StringUtils.isBlank(requestInstance.getParameter("legalEntityId"))) {
                alert.prepareError("legalEntityId cannot be empty").log();
                ErrorCodeEnum.ERR_22232.setErrorCode(result);
                return result;
            }
            String[] reqPermissions = {PermissionName.UPDATE_CONTRACT};
            if(!LoggedInUserHandler.hasAccessToLegalEntity(requestInstance,reqPermissions))
            {
                result.addParam(new Param("Status", "Contract Manage Service Operation Failed", FabricConstants.STRING));
                ErrorCodeEnum.ERR_22231.setErrorCode(result);
                alert.prepareError("Logged in user do not have access to this legalEntity ").log();
                return result;        
            }
        	
            if (requestInstance.getParameter(INPUT_CONTRACT_ID) == null) {
                ErrorCodeEnum.ERR_21960.setErrorCode(result);
                return result;
            } else if (requestInstance.getParameter(INPUT_CONTRACT_NAME) == null) {
                ErrorCodeEnum.ERR_21960.setErrorCode(result);
                return result;
            } else if (requestInstance.getParameter(INPUT_SERVICE_DEFINITION_NAME) == null) {
                ErrorCodeEnum.ERR_21961.setErrorCode(result);
                return result;
            } else if (StringUtils.isBlank(requestInstance.getParameter(INPUT_SERVICE_DEFINITION_ID))) {
                ErrorCodeEnum.ERR_21961.setErrorCode(result);
                return result;
            } else if (requestInstance.getParameter(INPUT_COMMUNICATION) == null) {
                ErrorCodeEnum.ERR_21962.setErrorCode(result);
                return result;
            } else if (requestInstance.getParameter(INPUT_ADDRESS) == null) {
                ErrorCodeEnum.ERR_21963.setErrorCode(result);
                return result;
            } else if (requestInstance.getParameter(INPUT_CONTRACT_CUSTOMERS) == null) {
                ErrorCodeEnum.ERR_21964.setErrorCode(result);
                return result;
            } else if (requestInstance.getParameter(INPUT_LEGAL_ENTITY_ID) == null) {
            	ErrorCodeEnum.ERR_22232.setErrorCode(result);
            	return result;
            } else {
                String contractId = requestInstance.getParameter(INPUT_CONTRACT_ID);
                String contractName = requestInstance.getParameter(INPUT_CONTRACT_NAME);
                String serviceDefinitionName = requestInstance.getParameter(INPUT_SERVICE_DEFINITION_NAME);
                String serviceDefinitionId = requestInstance.getParameter(INPUT_SERVICE_DEFINITION_ID);
                String communication = requestInstance.getParameter(INPUT_COMMUNICATION);
                String address = requestInstance.getParameter(INPUT_ADDRESS);
                String contractCustomers = requestInstance.getParameter(INPUT_CONTRACT_CUSTOMERS);
                String deletedCustomers =  requestInstance.getParameter(DELETED_CUSTOMERS);
                String faxId = "";
                String accountLevelPermissions = requestInstance.getParameter("accountLevelPermissions1");
                String globalLevelPermissions = requestInstance.getParameter("globalLevelPermissions1");
                String transactionLimits = requestInstance.getParameter("transactionLimits1");
                String legalEntityId = requestInstance.getParameter(INPUT_LEGAL_ENTITY_ID);
                if (StringUtils.isNotBlank(requestInstance.getParameter(INPUT_FAX_ID))) {
                    faxId = requestInstance.getParameter(INPUT_FAX_ID);
                }
                Map<String, Object> postParametersMap = new HashMap<>();
                postParametersMap.put("contractId", contractId);
                postParametersMap.put("contractName", contractName);
                postParametersMap.put("serviceDefinitionName", serviceDefinitionName);
                postParametersMap.put("serviceDefinitionId", serviceDefinitionId);
                postParametersMap.put("faxId", faxId);
                postParametersMap.put("communication", stringifyForVelocityTemplate(communication));
                postParametersMap.put("address", stringifyForVelocityTemplate(address));
                postParametersMap.put("contractCustomers", stringifyForVelocityTemplate(contractCustomers.toString()));

                postParametersMap.put(DELETED_CUSTOMERS, stringifyForVelocityTemplate(deletedCustomers.toString()));
                postParametersMap.put("legalEntityId", legalEntityId);
                postParametersMap.put("accountLevelPermissions1", stringifyForVelocityTemplate(accountLevelPermissions));
                postParametersMap.put("globalLevelPermissions1", stringifyForVelocityTemplate(globalLevelPermissions));
                postParametersMap.put("transactionLimits1", stringifyForVelocityTemplate(transactionLimits));

                String dbpServicesClaimsToken = DBPServices.getDBPServicesClaimsToken(requestInstance);
                JSONObject editContractresponse =
                        contractBusinessDelegate.editContract(postParametersMap, dbpServicesClaimsToken);
                if (editContractresponse == null || !editContractresponse.has(FabricConstants.OPSTATUS)
                        || editContractresponse.getInt(FabricConstants.OPSTATUS) != 0) {
                    ErrorCodeEnum.ERR_21013.setErrorCode(result);
                    result.addParam(new Param("status", "Failure", FabricConstants.STRING));
                    return result;
                } else if (editContractresponse.has("dbpErrMsg")) {
                	result = CommonUtilities.constructResultFromJSONObject(editContractresponse);
                	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
                    return result;
                } else {
                    result.addParam(new Param("status", "Success", FabricConstants.STRING));
                    result.addParam(new Param("opstatus", editContractresponse.get("opstatus").toString(),
                            FabricConstants.STRING));
                }
            }
        } catch (Exception e) {
            alert.prepareError("Unexepected Error in create contract ", e).log();
            result.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
            ErrorCodeEnum.ERR_21967.setErrorCode(result);
        }
        return result;
    }

    @Override
    public Result getContractFeatureActionLimits(String methodID, Object[] inputArray,
            DataControllerRequest requestInstance,
            DataControllerResponse response) {
        
        Result result = new Result();
        try {
            if (requestInstance.getParameter(INPUT_CONTRACT_ID) == null) {
                ErrorCodeEnum.ERR_21960.setErrorCode(result);
                return result;
            } 
            if (requestInstance.getParameter(INPUT_LEGAL_ENTITY_ID) == null) {
                ErrorCodeEnum.ERR_22230.setErrorCode(result);
                return result;
            } 
            else {
                String contractId = requestInstance.getParameter(INPUT_CONTRACT_ID);
                String legalEntityId = requestInstance.getParameter(INPUT_LEGAL_ENTITY_ID);
                Map<String, Object> postParametersMap = new HashMap<>();
                postParametersMap.put("contractId", contractId);
                postParametersMap.put("legalEntityId", legalEntityId);
                String dbpServicesClaimsToken = DBPServices.getDBPServicesClaimsToken(requestInstance);
                JSONObject getContractresponse =
                        contractBusinessDelegate.getContractFeatureActionLimits(postParametersMap,
                                dbpServicesClaimsToken);
                if (getContractresponse == null || !getContractresponse.has(FabricConstants.OPSTATUS)
                        || getContractresponse.getInt(FabricConstants.OPSTATUS) != 0) {
                    ErrorCodeEnum.ERR_22002.setErrorCode(result);
                    result.addParam(new Param("status", "Failure", FabricConstants.STRING));
                    AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.CONTRACTS, EventEnum.SEARCH,
                            ActivityStatusEnum.FAILED, "Fetch Contract features and actions failed");
                    return result;
                } else if (getContractresponse.has("dbpErrMsg")) {
                	result = CommonUtilities.constructResultFromJSONObject(getContractresponse);
                	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
                    return result;
                } else {

                    for (String currKey : getContractresponse.keySet()) {
                        if (currKey.equals("features")) {
                            JSONArray featuresArray = getContractresponse.getJSONArray("features");
                            Dataset recordsDataset = CommonUtilities.constructDatasetFromJSONArray(featuresArray);
                            recordsDataset.setId("features");
                            result.addDataset(recordsDataset);
                        } else if (currKey.equals("limits")) {
                            JSONArray limitsArray = getContractresponse.getJSONArray("limits");
                            Dataset recordsDataset =
                                    CommonUtilities.constructDatasetFromJSONArray(limitsArray);
                            recordsDataset.setId("limits");
                            result.addDataset(recordsDataset);
                        } else {
                            result.addParam(new Param(currKey, getContractresponse.get(currKey).toString(),
                                    FabricConstants.STRING));
                        }
                    }
                    result.addParam(new Param("status", "Success", FabricConstants.STRING));
                    result.addParam(new Param("opstatus", getContractresponse.get("opstatus").toString(),
                            FabricConstants.STRING));

                    AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.CONTRACTS, EventEnum.SEARCH,
                            ActivityStatusEnum.SUCCESSFUL,
                            "Successfully fetched contract features and actions contractId= " + contractId);

                }
            }

        } catch (Exception e) {
            alert.prepareError("Unexepected Error in get contract features and actions", e).log();
            result.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
            ErrorCodeEnum.ERR_22003.setErrorCode(result);
            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.CONTRACTS, EventEnum.SEARCH,
                    ActivityStatusEnum.FAILED, "Contract features and actions fetch Failed");
        }
        return result;

    }
    @Override
    public Result searchContract(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse response) throws Exception {



        Result result = new Result();
        try {

            String contractId = requestInstance.getParameter(INPUT_CONTRACT_ID);
            String contractName = requestInstance.getParameter(INPUT_CONTRACT_NAME);
            String coreCustomerId = requestInstance.getParameter(INPUT_CORE_CUSTOMER_ID);
            String coreCustomerName = requestInstance.getParameter(INPUT_CORE_CUSTOMER_NAME);
            String email = requestInstance.getParameter(INPUT_EMAIL);
            String phoneNumber = requestInstance.getParameter(INPUT_PHONE_NUMBER);
            String phoneCountryCode = requestInstance.getParameter(INPUT_PHONE_COUNTRY_CODE);
            String country = requestInstance.getParameter(INPUT_COUNTRY);
            String serviceDefinitionId = requestInstance.getParameter(INPUT_SERVICE_DEFINITION_ID);
            String legalEntityId = requestInstance.getParameter(INPUT_LEGAL_ENTITY_ID);

            if(StringUtils.isBlank(legalEntityId) || legalEntityId.equalsIgnoreCase("null")){
            	ErrorCodeEnum.ERR_22230.setErrorCode(result);
            	return result;
            }
            if ((StringUtils.isBlank(contractId) || contractId.equalsIgnoreCase("null"))
                    && (StringUtils.isBlank(contractName) || contractName.equalsIgnoreCase("null"))
                    && (StringUtils.isBlank(coreCustomerId) || coreCustomerId.equalsIgnoreCase("null"))
                    && (StringUtils.isBlank(coreCustomerName) || coreCustomerName.equalsIgnoreCase("null"))
                    && (StringUtils.isBlank(email) || email.equalsIgnoreCase("null"))
                    && (StringUtils.isBlank(phoneNumber) || phoneNumber.equalsIgnoreCase("null"))
                    && (StringUtils.isBlank(phoneCountryCode) || phoneCountryCode.equalsIgnoreCase("null"))
                    && (StringUtils.isBlank(country) || country.equalsIgnoreCase("null"))
                    && (StringUtils.isBlank(serviceDefinitionId) || serviceDefinitionId.equalsIgnoreCase("null"))) {
                result.addParam(new Param("TotalResultsFound", "0", FabricConstants.INT));
                Dataset recordsDS = new Dataset();
                recordsDS.setId("records");
                result.addDataset(recordsDS);
                return result;
            }

            Map<String, Object> postParametersMap = new HashMap<>();
            postParametersMap.put("contractId", contractId);
            postParametersMap.put("contractName", contractName);
            postParametersMap.put("coreCustomerId", coreCustomerId);
            postParametersMap.put("coreCustomerName", coreCustomerName);
            postParametersMap.put("email", email);
            postParametersMap.put("phoneCountryCode", phoneCountryCode);
            postParametersMap.put("phoneNumber", phoneNumber);
            postParametersMap.put("country", country);
            postParametersMap.put("serviceDefinitionId", serviceDefinitionId);
            postParametersMap.put("legalEntityId", legalEntityId);

            String dbpServicesClaimsToken = DBPServices.getDBPServicesClaimsToken(requestInstance);
            JSONObject searchCustomersResponse =
                    contractBusinessDelegate.searchContract(postParametersMap, dbpServicesClaimsToken);
            if (searchCustomersResponse == null || !searchCustomersResponse.has(FabricConstants.OPSTATUS)
                    || searchCustomersResponse.getInt(FabricConstants.OPSTATUS) != 0) {
                ErrorCodeEnum.ERR_21972.setErrorCode(result);
                result.addParam(new Param("status", "Failure", FabricConstants.STRING));
                AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.CONTRACTS, EventEnum.SEARCH,
                        ActivityStatusEnum.FAILED, "Search contract failed");
                return result;
            } else if (searchCustomersResponse.has("dbpErrMsg")) {
            	result = CommonUtilities.constructResultFromJSONObject(searchCustomersResponse);
            	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
                return result;
            } else {
                if (searchCustomersResponse.has("contracts")) {
                    JSONArray contractsArray = searchCustomersResponse.getJSONArray("contracts");
                    Dataset recordsDataset = CommonUtilities.constructDatasetFromJSONArray(contractsArray);
                    recordsDataset.setId("contracts");
                    Param recordsStatus = new Param("Status", "Records returned: " + contractsArray.length(),
                            FabricConstants.STRING);
                    result.addDataset(recordsDataset);
                    result.addParam(recordsStatus);
                } else {
                    ErrorCodeEnum.ERR_21972.setErrorCode(result);
                    return result;

                }
            }
        } catch (Exception e) {
            alert.prepareError("Unexpected Error in search contract", e).log();
            result.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
            ErrorCodeEnum.ERR_21973.setErrorCode(result);
            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.CONTRACTS, EventEnum.SEARCH,
                    ActivityStatusEnum.FAILED, "Search contract failed");
        }
        return result;

    }
    
    @Override
	public Result updateContractStatus(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse response) throws Exception {
    	
    	Result result = new Result();
    	String contractId = StringUtils.EMPTY;
    	try {
	    	if (StringUtils.isBlank(requestInstance.getParameter(INPUT_CONTRACT_ID))) {
	            ErrorCodeEnum.ERR_21960.setErrorCode(result);
	            return result;
	        }
	    	if (StringUtils.isBlank(requestInstance.getParameter(INPUT_STATUS_ID))) {
	            ErrorCodeEnum.ERR_22021.setErrorCode(result);
	            return result;
	        }
	    	
	    	String statusId = requestInstance.getParameter(INPUT_STATUS_ID);
	    	Map<String, Object> postParametersMap = new HashMap<>();
	    	if(statusId.equalsIgnoreCase(CONTRACT_STATUS_REJECTED)) {
	    		
	    		if (StringUtils.isBlank(requestInstance.getParameter(INPUT_REJECTED_REASON))) {
	                ErrorCodeEnum.ERR_22022.setErrorCode(result);
	                return result;
	            }
	    		String rejectedReason = requestInstance.getParameter(INPUT_REJECTED_REASON);
	        	
	        	String userid = LoggedInUserHandler.getUserDetails(requestInstance).getUserName();
	        	String rejectedBy = userid;
	        	postParametersMap.put("rejectedReason", rejectedReason);
	        	postParametersMap.put("rejectedBy", rejectedBy);
	        	
	    	} else if(!statusId.equalsIgnoreCase(CONTRACT_STATUS_ACTIVE)) {
	    		
	    		ErrorCodeEnum.ERR_22021.setErrorCode(result);
	            return result;
	            
	    	} else {
	    		
	    		postParametersMap.put("rejectedReason", "");
	        	postParametersMap.put("rejectedBy", "");
	    	}
	    	String dbpServicesClaimsToken = DBPServices.getDBPServicesClaimsToken(requestInstance);
	    	
	        postParametersMap.put("statusId", statusId);
	        
	    	contractId = requestInstance.getParameter(INPUT_CONTRACT_ID);
	    	postParametersMap.put("contractId", contractId);
	    	postParametersMap.put("statusId", statusId);
	    	
	    	postParametersMap.put("contractId", contractId);
	    	postParametersMap.put("statusId", statusId);
	    	
	    	JSONObject serviceResponse =
	                contractBusinessDelegate.updateContractStatus(postParametersMap, dbpServicesClaimsToken);
	        if (serviceResponse == null || !serviceResponse.has(FabricConstants.OPSTATUS)
	                || serviceResponse.getInt(FabricConstants.OPSTATUS) != 0) {
	            ErrorCodeEnum.ERR_21972.setErrorCode(result);
	            result.addParam(new Param("status", "Failure", FabricConstants.STRING));
	            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.CONTRACTS, EventEnum.UPDATE,
	                    ActivityStatusEnum.FAILED, "Update contract status failed for contractId : "+contractId);
	            return result;
	        } else if (serviceResponse.has("dbpErrMsg")) {
	        	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
                return result;
	        } else {
	        	
	        	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
	        }
	        
    	}catch (Exception e) {
            alert.prepareError("Unexpected Error in updateContractStatus", e).log();
            result.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
            ErrorCodeEnum.ERR_21973.setErrorCode(result);
            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.CONTRACTS, EventEnum.SEARCH,
                    ActivityStatusEnum.FAILED, "Update contract status failed for contractId : "+contractId);
        }
    	
    	
		return result;
	}

	@Override
	public Result getListOfContractsByStatus(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse response) throws Exception {
		
		Result result = new Result();
		String statusId = StringUtils.EMPTY;
		String legalEntityId = StringUtils.EMPTY;
		try {
			if (StringUtils.isBlank(requestInstance.getParameter("legalEntityId"))) {
                alert.prepareError("legalEntityId cannot be empty").log();
                ErrorCodeEnum.ERR_22232.setErrorCode(result);
                return result;
            }
            String[] reqPermissions = {PermissionName.VIEW_CONTRACT};
            if(!LoggedInUserHandler.hasAccessToLegalEntity(requestInstance,reqPermissions))
            {
                result.addParam(new Param("Status", "ContractManageService failed", FabricConstants.STRING));
                ErrorCodeEnum.ERR_22231.setErrorCode(result);
                alert.prepareError("Logged in user do not have access to this legalEntity ").log();
                return result;
            }
	    	if (StringUtils.isBlank(requestInstance.getParameter(INPUT_STATUS_ID))) {
	            ErrorCodeEnum.ERR_22021.setErrorCode(result);
	            return result;
	        }
	    	
	    	if (StringUtils.isBlank(requestInstance.getParameter(INPUT_LEGALENTITY_ID))) {
	    		ErrorCodeEnum.ERR_22232.setErrorCode(result);
	    		return result;
	    	}
	    	
	    	statusId = requestInstance.getParameter(INPUT_STATUS_ID);
	    	legalEntityId = requestInstance.getParameter(INPUT_LEGALENTITY_ID);
	    	String dbpServicesClaimsToken = DBPServices.getDBPServicesClaimsToken(requestInstance);
	    	Map<String, Object> postParametersMap = new HashMap<>();
	        postParametersMap.put("statusId", statusId);
	        postParametersMap.put("legalEntityId", legalEntityId);
	        
	        JSONObject serviceResponse =
	                contractBusinessDelegate.getListOfContractsByStatus(postParametersMap, dbpServicesClaimsToken);
	        if (serviceResponse == null || !serviceResponse.has(FabricConstants.OPSTATUS)
                    || serviceResponse.getInt(FabricConstants.OPSTATUS) != 0) {
                ErrorCodeEnum.ERR_21972.setErrorCode(result);
                result.addParam(new Param("status", "Failure", FabricConstants.STRING));
                AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.CONTRACTS, EventEnum.SEARCH,
                        ActivityStatusEnum.FAILED, "Search contract by statusId failed : "+statusId);
                return result;
            } else if (serviceResponse.has("dbpErrMsg")) {
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
                return result;
            } else {
            	
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            }
		}catch (Exception e) {
            alert.prepareError("Unexpected Error in getListOfContractsByStatus", e).log();
            result.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
            ErrorCodeEnum.ERR_21972.setErrorCode(result);
            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.CONTRACTS, EventEnum.SEARCH,
                    ActivityStatusEnum.FAILED, "getListOfContractsByStatus failed: "+statusId);
        }

        return result;
	}

    @Override
    public Result getCoreCustomerAccounts(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse response) throws Exception {
        
        Result result = new Result();
        try {
        	if (StringUtils.isBlank(requestInstance.getParameter("legalEntityId"))) {
                alert.prepareError("legalEntityId cannot be empty").log();
                ErrorCodeEnum.ERR_22232.setErrorCode(result);
                return result;
            }
            String[] reqPermissions = {PermissionName.VIEW_CONTRACT};
            if(!LoggedInUserHandler.hasAccessToLegalEntity(requestInstance,reqPermissions))
            {
                result.addParam(new Param("Status", "getCoreCustomerAccounts operation failed", FabricConstants.STRING));
                ErrorCodeEnum.ERR_22231.setErrorCode(result);
                alert.prepareError("Logged in user do not have access to this legalEntity ").log();
                return result;        
            }
            String listOfCustomerIds = requestInstance.getParameter(INPUT_CORE_CUSTOMER_ID_LIST);
            String legalEntityId = requestInstance.getParameter(INPUT_LEGALENTITY_ID);
            if (StringUtils.isBlank(listOfCustomerIds) || listOfCustomerIds.equalsIgnoreCase("null")) {
                result.addParam(new Param("TotalResultsFound", "0", FabricConstants.INT));
                Dataset recordsDS = new Dataset();
                recordsDS.setId("records");
                result.addDataset(recordsDS);
                return result;
            } else {
                Map<String, Object> postParametersMap = new HashMap<>();
                postParametersMap.put("coreCustomerIdList", listOfCustomerIds);
                postParametersMap.put("legalEntityId", legalEntityId);
                String dbpServicesClaimsToken = DBPServices.getDBPServicesClaimsToken(requestInstance);
                JSONObject searchCustomersResponse =
                        contractBusinessDelegate.getCoreCustomerAccounts(postParametersMap, dbpServicesClaimsToken);
                if (searchCustomersResponse == null || !searchCustomersResponse.has(FabricConstants.OPSTATUS)
                        || searchCustomersResponse.getInt(FabricConstants.OPSTATUS) != 0) {
                    ErrorCodeEnum.ERR_21970.setErrorCode(result);
                    result.addParam(new Param("status", "Failure", FabricConstants.STRING));
                    AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.CONTRACTS, EventEnum.SEARCH,
                            ActivityStatusEnum.FAILED, "Search core customers failed");
                    return result;
                } else if (searchCustomersResponse.has("dbpErrMsg")) {
                	result = CommonUtilities.constructResultFromJSONObject(searchCustomersResponse);
                	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
                    return result;
                } else {
                    if (searchCustomersResponse.has("coreCustomerAccounts")) {
                        JSONArray customerAccountsArray = searchCustomersResponse.getJSONArray("coreCustomerAccounts");
                        Dataset recordsDataset = CommonUtilities.constructDatasetFromJSONArray(customerAccountsArray);
                        recordsDataset.setId("coreCustomerAccounts");
                        Param recordsStatus = new Param("Status", "Records returned: " + customerAccountsArray.length(),
                                FabricConstants.STRING);
                        result.addDataset(recordsDataset);
                        result.addParam(recordsStatus);
                    } else {
                        ErrorCodeEnum.ERR_21970.setErrorCode(result);
                        return result;

                    }
                }
            }
        } catch (Exception e) {
            alert.prepareError("Unexpected Error in search accounts of customer", e).log();
            result.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
            ErrorCodeEnum.ERR_21971.setErrorCode(result);
            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.CONTRACTS, EventEnum.SEARCH,
                    ActivityStatusEnum.FAILED, "Search accounts of customer failed");
        }

        return result;

    }

    @Override
    public Result getCoreRelativeCustomers(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse response) throws Exception {
        
        Result result = new Result();
        try {
        	if (StringUtils.isBlank(requestInstance.getParameter("legalEntityId"))) {
                alert.prepareError("legalEntityId cannot be empty").log();
                ErrorCodeEnum.ERR_22232.setErrorCode(result);
                return result;
            }
            String[] reqPermissions = {PermissionName.VIEW_CONTRACT};
            if(!LoggedInUserHandler.hasAccessToLegalEntity(requestInstance,reqPermissions))
            {
                result.addParam(new Param("Status", "CoreCustomerRelativeCustomersGet failed", FabricConstants.STRING));
                ErrorCodeEnum.ERR_22231.setErrorCode(result);
                alert.prepareError("Logged in user do not have access to this legalEntity ").log();
                return result;        
            }
            String id = requestInstance.getParameter(INPUT_CORE_CUSTOMER_ID);
            String legalEntityId = requestInstance.getParameter(INPUT_LEGAL_ENTITY_ID);
            if (StringUtils.isBlank(id) || id.equalsIgnoreCase("null")) {
                result.addParam(new Param("TotalResultsFound", "0", FabricConstants.INT));
                Dataset recordsDS = new Dataset();
                recordsDS.setId("records");
                result.addDataset(recordsDS);
                return result;
            }
            if("null".equalsIgnoreCase(legalEntityId)) {
            	legalEntityId = null;
            }
            Map<String, Object> postParametersMap = new HashMap<>();
            postParametersMap.put("coreCustomerId", id);
            postParametersMap.put("legalEntityId", legalEntityId);
            String dbpServicesClaimsToken = DBPServices.getDBPServicesClaimsToken(requestInstance);
            JSONObject searchCustomersResponse =
                    contractBusinessDelegate.getCoreRelativeCustomers(postParametersMap, dbpServicesClaimsToken);
            if (searchCustomersResponse == null || !searchCustomersResponse.has(FabricConstants.OPSTATUS)
                    || searchCustomersResponse.getInt(FabricConstants.OPSTATUS) != 0) {
                Dataset recordsDataset = new Dataset();
                recordsDataset.setId("customers");
                Param recordsStatus = new Param("Status", "Records returned: 0",
                        FabricConstants.STRING);
                result.addDataset(recordsDataset);
                result.addParam(recordsStatus);
                return result;
            } else if (searchCustomersResponse.has("dbpErrMsg")) {
            	result = CommonUtilities.constructResultFromJSONObject(searchCustomersResponse);
            	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
                return result;
            } else {
                if (searchCustomersResponse.has("customers")) {
                    JSONArray customerArray = searchCustomersResponse.getJSONArray("customers");
                    Dataset recordsDataset = CommonUtilities.constructDatasetFromJSONArray(customerArray);
                    recordsDataset.setId("customers");
                    Param recordsStatus = new Param("Status", "Records returned: " + customerArray.length(),
                            FabricConstants.STRING);
                    result.addDataset(recordsDataset);
                    result.addParam(recordsStatus);
                } else {
                    Dataset recordsDataset = new Dataset();
                    recordsDataset.setId("customers");
                    Param recordsStatus = new Param("Status", "Records returned: 0",
                            FabricConstants.STRING);
                    result.addDataset(recordsDataset);
                    result.addParam(recordsStatus);
                    return result;
                }
            }
        } catch (Exception e) {
            alert.prepareError("Unexpected Error in search relations of customer", e).log();
            result.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
            ErrorCodeEnum.ERR_21969.setErrorCode(result);
            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.CONTRACTS, EventEnum.SEARCH,
                    ActivityStatusEnum.FAILED, "Search relations of customer failed");
        }
        return result;
    }

    @Override
    public Result searchCoreCustomers(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse response) throws Exception {
        
        Result result = new Result();
        try {
            String id = requestInstance.getParameter(INPUT_ID);
            String name = requestInstance.getParameter(INPUT_NAME);
            String email = requestInstance.getParameter(INPUT_EMAIL);
            String phoneNumber = requestInstance.getParameter(INPUT_PHONE_NUMBER);
            String phoneCountryCode = requestInstance.getParameter(INPUT_PHONE_COUNTRY_CODE);
            String dob = requestInstance.getParameter(INPUT_DOB);
            String customerStatus = requestInstance.getParameter(INPUT_CUSTOMER_STATUS);
            String country = requestInstance.getParameter(INPUT_COUNTRY);
            String town = requestInstance.getParameter(INPUT_TOWN);
            String zipcode = requestInstance.getParameter(INPUT_ZIPCODE);
            String legalEntityId = requestInstance.getParameter(INPUT_LEGAL_ENTITY_ID);
            if (StringUtils.isBlank(legalEntityId)) {
            	ErrorCodeEnum.ERR_22232.setErrorCode(result);
            	return result;
            	
            }
            if ((StringUtils.isBlank(id) || id.equalsIgnoreCase("null"))
                    && (StringUtils.isBlank(name) || name.equalsIgnoreCase("null"))
                    && (StringUtils.isBlank(legalEntityId) || legalEntityId.equalsIgnoreCase("null"))
                    && (StringUtils.isBlank(dob) || dob.equalsIgnoreCase("null"))
                    && (StringUtils.isBlank(customerStatus) || customerStatus.equalsIgnoreCase("null"))
                    && (StringUtils.isBlank(phoneNumber) || phoneNumber.equalsIgnoreCase("null"))
                    && (StringUtils.isBlank(phoneCountryCode) || phoneCountryCode.equalsIgnoreCase("null"))
                    && (StringUtils.isBlank(email) || email.equalsIgnoreCase("null"))
                    && (StringUtils.isBlank(country) || country.equalsIgnoreCase("null"))
                    && (StringUtils.isBlank(town) || town.equalsIgnoreCase("null"))
                    && (StringUtils.isBlank(zipcode) || zipcode.equalsIgnoreCase("null"))) {
                result.addParam(new Param("TotalResultsFound", "0", FabricConstants.INT));
                Dataset recordsDS = new Dataset();
                recordsDS.setId("records");
                result.addDataset(recordsDS);
                return result;
            }

            Map<String, Object> postParametersMap = new HashMap<>();
            postParametersMap.put("coreCustomerId", id);
            postParametersMap.put("customerName", name);
            postParametersMap.put("email", email);
            postParametersMap.put("phoneCountryCode", phoneCountryCode);
            postParametersMap.put("phoneNumber", phoneNumber);
            postParametersMap.put("dateOfBirth", dob);
            postParametersMap.put("customerStatus", customerStatus);
            postParametersMap.put("country", country);
            postParametersMap.put("city", town);
            postParametersMap.put("zipCode", zipcode);
            postParametersMap.put("legalEntityId", legalEntityId);
            String dbpServicesClaimsToken = DBPServices.getDBPServicesClaimsToken(requestInstance);
            JSONObject searchCustomersResponse =
                    contractBusinessDelegate.searchCoreCustomers(postParametersMap, dbpServicesClaimsToken);
            if (searchCustomersResponse == null || !searchCustomersResponse.has(FabricConstants.OPSTATUS)
                    || searchCustomersResponse.getInt(FabricConstants.OPSTATUS) != 0) {
                ErrorCodeEnum.ERR_21965.setErrorCode(result);
                result.addParam(new Param("status", "Failure", FabricConstants.STRING));
                AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.CONTRACTS, EventEnum.SEARCH,
                        ActivityStatusEnum.FAILED, "Search core customers failed");
                return result;
            } else if (searchCustomersResponse.has("dbpErrMsg")) {
            	Dataset recordsDataset = new Dataset();
				recordsDataset.setId("records");
				result.addDataset(recordsDataset);
				if(!result.hasParamByName("TotalResultsFound")) {
					result.addParam(new Param("TotalResultsFound", "0", FabricConstants.INT));
				}
				if(!result.hasParamByName("Status")) {
					result.addParam(new Param("Status", "Records returned: 0", FabricConstants.STRING));
				}
                return result;
            } else {
                if (searchCustomersResponse.has("customers")) {
                    JSONArray customerArray = searchCustomersResponse.getJSONArray("customers");
                    Dataset recordsDataset = CommonUtilities.constructDatasetFromJSONArray(customerArray);
                    recordsDataset.setId("customers");
                    Param recordsStatus = new Param("Status", "Records returned: " + customerArray.length(),
                            FabricConstants.STRING);
                    result.addDataset(recordsDataset);
                    result.addParam(recordsStatus);
                } else {
                    ErrorCodeEnum.ERR_21965.setErrorCode(result);
                    return result;
                }
            }
        } catch (Exception e) {
            alert.prepareError("Unexpected Error in search core customer", e).log();
            result.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
            ErrorCodeEnum.ERR_21966.setErrorCode(result);
            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.CONTRACTS, EventEnum.SEARCH,
                    ActivityStatusEnum.FAILED, "Search core customer failed");
        }
        return result;

    }

    @Override
    public Result getContractInfinityUsers(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse response) throws Exception {
        
        Result result = new Result();
        try {
            String id = requestInstance.getParameter(INPUT_CONTRACT_ID);
            if (StringUtils.isBlank(id) || id.equalsIgnoreCase("null")) {
                Dataset recordsDataset = new Dataset("customers");
                Param recordsStatus = new Param("Status", "Records returned: 0",
                        FabricConstants.STRING);
                result.addDataset(recordsDataset);
                result.addParam(recordsStatus);
                return result;
            }
            Map<String, Object> postParametersMap = new HashMap<>();
            postParametersMap.put("contractId", id);
            String dbpServicesClaimsToken = DBPServices.getDBPServicesClaimsToken(requestInstance);
            JSONObject serviceResponse =
                    contractBusinessDelegate.getContractInfinityUsers(postParametersMap, dbpServicesClaimsToken);
            if (serviceResponse == null || !serviceResponse.has(FabricConstants.OPSTATUS)
                    || serviceResponse.getInt(FabricConstants.OPSTATUS) != 0) {
                Dataset recordsDataset = new Dataset("contractUsers");
                Param recordsStatus = new Param("Status", "Records returned: 0",
                        FabricConstants.STRING);

                result.addDataset(recordsDataset);
                result.addParam(recordsStatus);
                return result;
            } else if (serviceResponse.has("dbpErrMsg")) {
                Dataset recordsDataset = new Dataset();
                recordsDataset.setId("contractUsers");
                Param recordsStatus = new Param("Status", "Records returned: 0",
                        FabricConstants.STRING);
                result.addDataset(recordsDataset);
                result.addParam(recordsStatus);
                return result;
            } else {

            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            }
        } catch (Exception e) {
            Dataset recordsDataset = new Dataset("contractUsers");
            Param recordsStatus = new Param("Status", "Records returned: 0",
                    FabricConstants.STRING);
            result.addDataset(recordsDataset);
            result.addParam(recordsStatus);
            return result;
        }
        return result;
    }

	@Override
	public Result getContractAccounts(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance) throws Exception {

		Result result = new Result();
    	String contractId = StringUtils.EMPTY;
    	
    	try {
    		
	    	if (StringUtils.isBlank(requestInstance.getParameter(INPUT_CONTRACT_ID))) {
	            ErrorCodeEnum.ERR_21960.setErrorCode(result);
	            return result;
	        }
	    	
	    	
	    	Map<String, Object> postParametersMap = new HashMap<>();
	    	String dbpServicesClaimsToken = DBPServices.getDBPServicesClaimsToken(requestInstance);
	        
	    	contractId = requestInstance.getParameter(INPUT_CONTRACT_ID);
	    	postParametersMap.put("contractId", contractId);
	    	
	    	JSONObject serviceResponse =
	                contractBusinessDelegate.getContractAccounts(postParametersMap, dbpServicesClaimsToken);
	        if (serviceResponse == null || !serviceResponse.has(FabricConstants.OPSTATUS)
	                || serviceResponse.getInt(FabricConstants.OPSTATUS) != 0) {
	            ErrorCodeEnum.ERR_21973.setErrorCode(result);
	            result.addParam(new Param("status", "Failure", FabricConstants.STRING));
	            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.CONTRACTS, EventEnum.SEARCH,
	                    ActivityStatusEnum.FAILED, "Failed to fetch contract accounts for contractId : "+contractId);
	            return result;
	        } else if (serviceResponse.has("dbpErrMsg")) {
	        	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
                return result;
	        } else {
	        	
	        	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
	        }
	        
    	}catch (Exception e) {
            alert.prepareError("Unexpected Error in getContractAccounts", e).log();
            result.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
            ErrorCodeEnum.ERR_21973.setErrorCode(result);
            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.CONTRACTS, EventEnum.SEARCH,
                    ActivityStatusEnum.FAILED, "Failed to fetch contract accounts for contractId : "+contractId);
        }
    	
		return result;
		
	}
	

	@Override

	public Result getCoreCustomerDetails(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse response) throws Exception {

		Result result = new Result();

		try {

			String id = requestInstance.getParameter(INPUT_CORE_CUSTOMER_ID);
			String legalEntityId = requestInstance.getParameter(INPUT_LEGAL_ENTITY_ID);

			if (StringUtils.isBlank(id) || id.equalsIgnoreCase("null")) {
				result.addParam(new Param("TotalResultsFound", "0", FabricConstants.INT));
				Dataset recordsDS = new Dataset();
				recordsDS.setId("records");
				result.addDataset(recordsDS);
				return result;
			}
			if (StringUtils.isBlank(legalEntityId) || legalEntityId.equalsIgnoreCase("null")) {
				 ErrorCodeEnum.ERR_22230.setErrorCode(result);
				 return result;
			}

			Map<String, Object> postParametersMap = new HashMap<>();
			postParametersMap.put("coreCustomerId", id);
			postParametersMap.put("legalEntityId", legalEntityId);
			String dbpServicesClaimsToken = DBPServices.getDBPServicesClaimsToken(requestInstance);

			JSONObject searchCustomersResponse = contractBusinessDelegate.getCoreCustomerDetails(postParametersMap,
					dbpServicesClaimsToken);
			if (searchCustomersResponse == null || !searchCustomersResponse.has(FabricConstants.OPSTATUS)
					|| searchCustomersResponse.getInt(FabricConstants.OPSTATUS) != 0) {
				Dataset recordsDataset = new Dataset();
				recordsDataset.setId("CoreCustomer");
				Param recordsStatus = new Param("Status", "Records returned: 0", FabricConstants.STRING);
				result.addDataset(recordsDataset);
				result.addParam(recordsStatus);
				return result;
			} else if (searchCustomersResponse.has("dbpErrMsg")) {
				result = CommonUtilities.constructResultFromJSONObject(searchCustomersResponse);
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return result;
			} else {
				if (searchCustomersResponse.has("CoreCustomer")) {
					JSONArray customerArray = searchCustomersResponse.getJSONArray("CoreCustomer");
					Dataset recordsDataset = CommonUtilities.constructDatasetFromJSONArray(customerArray);
					recordsDataset.setId("CoreCustomer");
					Param recordsStatus = new Param("Status", "Records returned: " + customerArray.length(),
							FabricConstants.STRING);
					result.addDataset(recordsDataset);
					result.addParam(recordsStatus);
				} else {
					Dataset recordsDataset = new Dataset();
					recordsDataset.setId("CoreCustomer");
					Param recordsStatus = new Param("Status", "Records returned: 0", FabricConstants.STRING);
					result.addDataset(recordsDataset);
					result.addParam(recordsStatus);
					return result;
				}

			}

		} catch (Exception e) {
			alert.prepareError("Unexpected Error in search core customers", e).log();
			result.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
			ErrorCodeEnum.ERR_21965.setErrorCode(result);
			AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.CONTRACTS, EventEnum.SEARCH,
					ActivityStatusEnum.FAILED, "Search core customers failed");
		}

		return result;

	}
	
	private  String stringifyForVelocityTemplate(String str) {
		if (StringUtils.isBlank(str)) {

			return "\"\"";
		}else if(str.contains("\\")) {
			str= str.replace("\\","\\\\" );
		}
		return "\"" + str.replace("\"", "\\\"") + "\"";
	}

}