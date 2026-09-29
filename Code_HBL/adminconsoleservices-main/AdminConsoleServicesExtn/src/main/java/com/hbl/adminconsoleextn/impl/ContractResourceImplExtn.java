package com.hbl.adminconsoleextn.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.core.security.LoggedInUserHandler;
import com.kony.adminconsole.handler.AuditHandler;
import com.kony.adminconsole.service.contract.businessdelegate.api.ContractBusinessDelegate;
import com.kony.adminconsole.service.contract.resource.impl.ContractResourceImpl;
import com.kony.adminconsole.utilities.ActivityStatusEnum;
import com.kony.adminconsole.utilities.DBPServices;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.EventEnum;
import com.kony.adminconsole.utilities.ModuleNameEnum;
import com.kony.adminconsole.utilities.PermissionName;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

public class ContractResourceImplExtn extends ContractResourceImpl{
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
	public Result updateContractStatus(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse response) throws Exception {
    	
    	Result result = new Result();
    	String contractId = StringUtils.EMPTY;
    	String statusId = requestInstance.getParameter(INPUT_STATUS_ID);
    	EventEnum eventType=null;
    	if(statusId.equalsIgnoreCase(CONTRACT_STATUS_ACTIVE)) {
    		eventType=EventEnum.APPROVEENROLL;
    	}else if(statusId.equalsIgnoreCase(CONTRACT_STATUS_REJECTED)) {
    		eventType=EventEnum.REJECTENROLL;
    	}
    	try {
	    	if (StringUtils.isBlank(requestInstance.getParameter(INPUT_CONTRACT_ID))) {
	            ErrorCodeEnum.ERR_21960.setErrorCode(result);
	            return result;
	        }
	    	if (StringUtils.isBlank(requestInstance.getParameter(INPUT_STATUS_ID))) {
	            ErrorCodeEnum.ERR_22021.setErrorCode(result);
	            return result;
	        }
	    	
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
	        	
	        	
	        	postParametersMap.put("isMobileBankingService", requestInstance.getParameter("isMobileBankingService"));
		    	postParametersMap.put("isInternetBankingService", requestInstance.getParameter("isInternetBankingService"));
		    	postParametersMap.put("isEnrollConsentProvided", requestInstance.getParameter("isEnrollConsentProvided"));
				postParametersMap.put("infinityAccess", requestInstance.getParameter("infinityAccess"));
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
	    	alert.prepareError("ContractResourceImplExtn:updateContractStatus:serviceResponse:"+serviceResponse).log();
	        if (serviceResponse == null || !serviceResponse.has(FabricConstants.OPSTATUS)
	                || serviceResponse.getInt(FabricConstants.OPSTATUS) != 0) {
	            ErrorCodeEnum.ERR_21972.setErrorCode(result);
	            result.addParam(new Param("status", "Failure", FabricConstants.STRING));
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
        }
    	
    	
		return result;
	}
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
            String[] reqPermissions = {PermissionName.VIEW_CONTRACT,PermissionName.VIEW_ENROLL_REQUESTS };
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
            String[] reqPermissions = {PermissionName.VIEW_CONTRACT,PermissionName.VIEW_ENROLL_REQUESTS};
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

}
