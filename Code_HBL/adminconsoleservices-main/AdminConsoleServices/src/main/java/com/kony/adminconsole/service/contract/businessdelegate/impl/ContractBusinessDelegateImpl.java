package com.kony.adminconsole.service.contract.businessdelegate.impl;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.service.contract.businessdelegate.api.ContractBusinessDelegate;
import com.kony.adminconsole.utilities.OperationName;
import com.kony.adminconsole.utilities.ServiceId;

public class ContractBusinessDelegateImpl implements ContractBusinessDelegate {

    @Override
    public JSONObject createContract(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
            throws DBPApplicationException {
        
        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", dbpServicesClaimsToken);
        String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.DBPSERVICE)
                        .withOperationId(OperationName.OP_CREATECONTRACT).withRequestHeaders(headerMap)
                        .withRequestParameters(postParametersMap).withPassThroughOutput(true).build()
                        .getResponse();

        return CommonUtilities.getStringAsJSONObject(serviceResponse);
    }

    @Override
    public JSONObject editContract(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
            throws DBPApplicationException {
        
        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", dbpServicesClaimsToken);
        String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.DBPSERVICE)
                        .withOperationId(OperationName.OP_EDITCONTRACT).withRequestHeaders(headerMap)
                        .withRequestParameters(postParametersMap).withPassThroughOutput(true).build().getResponse();

        return CommonUtilities.getStringAsJSONObject(serviceResponse);
    }

    @Override
    public JSONObject searchContract(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
            throws DBPApplicationException {
        
        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", dbpServicesClaimsToken);
        String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.DBPSERVICE)
                        .withOperationId(OperationName.OP_SEARCHCONTRACT).withRequestHeaders(headerMap)
                        .withRequestParameters(postParametersMap).withPassThroughOutput(true).build().getResponse();

        return CommonUtilities.getStringAsJSONObject(serviceResponse);
    }
    
    @Override
	public JSONObject updateContractStatus(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
			throws DBPApplicationException {

    	Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", dbpServicesClaimsToken);
        String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.DBPSERVICE)
                        .withOperationId(OperationName.OP_UPDATECONTRACTSTATUS).withRequestHeaders(headerMap)
                        .withRequestParameters(postParametersMap).withPassThroughOutput(true).build().getResponse();

        return CommonUtilities.getStringAsJSONObject(serviceResponse);
	}

	@Override
	public JSONObject getListOfContractsByStatus(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
			throws DBPApplicationException {
		
		Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", dbpServicesClaimsToken);
        String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.DBPSERVICE)
                        .withOperationId(OperationName.OP_GETLISTOFCONTRACTSBYSTATUS).withRequestHeaders(headerMap)
                        .withRequestParameters(postParametersMap).withPassThroughOutput(true).build().getResponse();

        return CommonUtilities.getStringAsJSONObject(serviceResponse);
	}

    @Override
    public JSONObject searchCoreCustomers(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
            throws DBPApplicationException {
        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", dbpServicesClaimsToken);
        String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.DBPSERVICE)
                        .withOperationId(OperationName.OP_SEARCHCORECUSTOMERS).withRequestHeaders(headerMap)
                        .withRequestParameters(postParametersMap).withPassThroughOutput(true).build().getResponse();

        return CommonUtilities.getStringAsJSONObject(serviceResponse);

    }

    @Override
    public JSONObject getCoreRelativeCustomers(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
            throws DBPApplicationException {
        
        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", dbpServicesClaimsToken);
        String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.DBPSERVICE)
                        .withOperationId(OperationName.OP_GETCORERELATIVECUSTOMERS).withRequestHeaders(headerMap)
                        .withRequestParameters(postParametersMap).withPassThroughOutput(true).build().getResponse();

        return CommonUtilities.getStringAsJSONObject(serviceResponse);
    }

    @Override
    public JSONObject getCoreCustomerAccounts(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
            throws DBPApplicationException {
        
        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", dbpServicesClaimsToken);
        String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.DBPSERVICE)
                        .withOperationId(OperationName.OP_GETCORECUSTOMERACCOUNTS).withRequestHeaders(headerMap)
                        .withRequestParameters(postParametersMap).withPassThroughOutput(true).build().getResponse();
        return CommonUtilities.getStringAsJSONObject(serviceResponse);
    }

    @Override
    public JSONObject getContractDetails(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
            throws DBPApplicationException {
        
        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", dbpServicesClaimsToken);
        String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.DBPSERVICE)
                        .withOperationId(OperationName.OP_GETCONTRACTDETAILS).withRequestHeaders(headerMap)
                        .withRequestParameters(postParametersMap).withPassThroughOutput(true).build().getResponse();
        return CommonUtilities.getStringAsJSONObject(serviceResponse);
    }

    @Override
    public JSONObject getContractFeatureActionLimits(Map<String, Object> postParametersMap,
            String dbpServicesClaimsToken)
            throws DBPApplicationException {
        
        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", dbpServicesClaimsToken);
        String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.DBPSERVICE)
                        .withOperationId(OperationName.OP_GETCONTRACTFEATUREACTIONLIMITS).withRequestHeaders(headerMap)
                        .withRequestParameters(postParametersMap).withPassThroughOutput(true).build().getResponse();
        return CommonUtilities.getStringAsJSONObject(serviceResponse);
    }

    @Override
    public JSONObject getContractInfinityUsers(Map<String, Object> postParametersMap,
            String dbpServicesClaimsToken)
            throws DBPApplicationException {
        
        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", dbpServicesClaimsToken);
        String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.DBPSERVICE)
                        .withOperationId(OperationName.OP_GETCONTRACTINFINITYUSERS).withRequestHeaders(headerMap)
                        .withRequestParameters(postParametersMap).withPassThroughOutput(true).build().getResponse();
        return CommonUtilities.getStringAsJSONObject(serviceResponse);
    }

	@Override
	public JSONObject getContractAccounts(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
			throws DBPApplicationException {
		
		Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", dbpServicesClaimsToken);
        String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.DBPSERVICE)
                        .withOperationId(OperationName.OP_GETCONTRACTACCOUNTS).withRequestHeaders(headerMap)
                        .withRequestParameters(postParametersMap).withPassThroughOutput(true).build().getResponse();
        return CommonUtilities.getStringAsJSONObject(serviceResponse);
	}
	
	@Override

    public JSONObject getCoreCustomerDetails(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
    		throws DBPApplicationException {

        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", dbpServicesClaimsToken);
        String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.DBPSERVICE)
                        .withOperationId(OperationName.OP_GETCORECUSTOMERDETAILS).withRequestHeaders(headerMap)
                        .withRequestParameters(postParametersMap).withPassThroughOutput(true).build().getResponse();
        return CommonUtilities.getStringAsJSONObject(serviceResponse);
    }

	
}
