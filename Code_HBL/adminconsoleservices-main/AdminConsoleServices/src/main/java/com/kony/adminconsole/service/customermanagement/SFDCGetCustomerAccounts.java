package com.kony.adminconsole.service.customermanagement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.handler.AuditHandler;
import com.kony.adminconsole.service.contract.businessdelegate.api.ContractBusinessDelegate;
import com.kony.adminconsole.service.customer.businessdelegate.api.InfinityUserManagementBusinessDelegate;
import com.kony.adminconsole.service.customer.resource.api.PartyUserManagementResource;
import com.kony.adminconsole.utilities.ActivityStatusEnum;
import com.kony.adminconsole.utilities.DBPServices;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.EventEnum;
import com.kony.adminconsole.utilities.ModuleNameEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;


public class SFDCGetCustomerAccounts implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance) throws Exception {
		Result result = new Result();
		return searchPartyUser( methodId,  inputArray,  requestInstance,responseInstance);
		 
	}
	private Object searchPartyUser(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) {
        
        Result result = null;
        try {
            PartyUserManagementResource customerResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(PartyUserManagementResource.class);
            result = customerResource.searchPartyUser(methodID, inputArray, requestInstance, responseInstance);
        
        String dbpServicesClaimsToken = DBPServices.getDBPServicesClaimsToken(requestInstance);
        Dataset Parties = result.getDatasetById("parties");
        
        List<Record> partyRecords = Parties.getAllRecords();
        
        Record partyRecord = partyRecords.get(0);
        
        String coreCustomerId = partyRecord.getParamValueByName("coreCustomerId");
        if(StringUtils.isNotBlank(coreCustomerId)) {
        String []corecusID= coreCustomerId.split("-");
        coreCustomerId =corecusID[1];
        }
        String id = partyRecord.getParamValueByName("id");
        String partyId = partyRecord.getParamValueByName("partyId");
        String isEnrolled = partyRecord.getParamValueByName("isEnrolled");
        result = new Result();
        if(StringUtils.isNotBlank(id) ) {
        	InfinityUserManagementBusinessDelegate infinityUserManagementBusinessDelegate = DBPAPIAbstractFactoryImpl
        			.getBusinessDelegate(InfinityUserManagementBusinessDelegate.class);
        	Map<String, Object> postParametersMap = new HashMap<>();
            postParametersMap.put("userId", id);
            JSONObject serviceResponse =
                    infinityUserManagementBusinessDelegate.getInfinityUserAccounts(postParametersMap,
                            dbpServicesClaimsToken);
            if (serviceResponse == null || !serviceResponse.has(FabricConstants.OPSTATUS)
	                || serviceResponse.getInt(FabricConstants.OPSTATUS) != 0) {
	            ErrorCodeEnum.ERR_22078.setErrorCode(result);
	            result.addParam(new Param("status", "Failure", FabricConstants.STRING));
	            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.CUSTOMERS, EventEnum.SEARCH,
	                    ActivityStatusEnum.FAILED, "Failed to fetch infinity user Accounts for userId : "+id);
	            return result;
	        } else if (serviceResponse.has("dbpErrMsg")) {
	        	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return result;
	        } else {
	        	
	        	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
	        	AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.CUSTOMERS, EventEnum.SEARCH,
                        ActivityStatusEnum.SUCCESSFUL,
                        "Succefully fetched Infinity User Accounts for userId :" + id);
	        }
        }else {
        	 Map<String, Object> postParametersMap = new HashMap<>();
        	 if(StringUtils.isNotBlank(coreCustomerId)) {
        	 List<String> coreCustomerIdList = new ArrayList<String>();
        	 coreCustomerIdList.add(0, coreCustomerId);
        	 //result.addParam("coreCustomerIdList", coreCustomerIdList.toString());
             postParametersMap.put("coreCustomerIdList", coreCustomerIdList);
             ContractBusinessDelegate contractBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                     .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(ContractBusinessDelegate.class);
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
        }
        return result;
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of searchPartyUser: ", e).log();
            return ErrorCodeEnum.ERR_20557.setErrorCode(new Result());
        }
    }
	}


