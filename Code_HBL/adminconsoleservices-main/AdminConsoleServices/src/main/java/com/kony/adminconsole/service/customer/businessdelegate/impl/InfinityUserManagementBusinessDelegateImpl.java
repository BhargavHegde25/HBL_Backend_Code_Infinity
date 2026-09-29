package com.kony.adminconsole.service.customer.businessdelegate.impl;

import java.util.HashMap;
import java.util.Map;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;
import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.service.customer.businessdelegate.api.InfinityUserManagementBusinessDelegate;
import com.kony.adminconsole.utilities.OperationName;
import com.kony.adminconsole.utilities.ServiceId;

public class InfinityUserManagementBusinessDelegateImpl implements InfinityUserManagementBusinessDelegate {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

    @Override
    public JSONObject getAssociatedCustomers(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
            throws DBPApplicationException {
        
        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", dbpServicesClaimsToken);
        String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.DBPSERVICE)
                        .withOperationId(OperationName.OP_GETASSOCIATEDCUSTOMERS).withRequestHeaders(headerMap)
                        .withRequestParameters(postParametersMap).withPassThroughOutput(true).build().getResponse();
        return CommonUtilities.getStringAsJSONObject(serviceResponse);

    }

    @Override
    public JSONObject getAllEligibleRelationalCustomers(Map<String, Object> postParametersMap,
            String dbpServicesClaimsToken)
            throws DBPApplicationException {
        
        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", dbpServicesClaimsToken);
        String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.DBPSERVICE)
                        .withOperationId(OperationName.OP_GETALLELIGIBLERELATIONALCUSTOMERS)
                        .withRequestHeaders(headerMap)
                        .withRequestParameters(postParametersMap).withPassThroughOutput(true).build().getResponse();
        return CommonUtilities.getStringAsJSONObject(serviceResponse);

    }

    @Override
    public JSONObject createInfinityUser(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
            throws DBPApplicationException {
        
        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", dbpServicesClaimsToken);
        String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.DBPSERVICE)
                        .withOperationId(OperationName.OP_CREATEINFINITYUSER).withRequestHeaders(headerMap)
                        .withRequestParameters(postParametersMap).withPassThroughOutput(true).build().getResponse();
        return CommonUtilities.getStringAsJSONObject(serviceResponse);

    }

    @Override
    public JSONObject editInfinityUser(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
            throws DBPApplicationException {
        
        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", dbpServicesClaimsToken);
        String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.DBPSERVICE)
                        .withOperationId(OperationName.OP_EDITINFINITYUSER).withRequestHeaders(headerMap)
                        .withRequestParameters(postParametersMap).withPassThroughOutput(true).build().getResponse();
        return CommonUtilities.getStringAsJSONObject(serviceResponse);

    }

    @Override
    public JSONObject getInfinityUser(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
            throws DBPApplicationException {
        
        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", dbpServicesClaimsToken);
        String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.DBPSERVICE)
                        .withOperationId(OperationName.OP_GETINFINITYUSER).withRequestHeaders(headerMap)
                        .withRequestParameters(postParametersMap).withPassThroughOutput(true).build().getResponse();
        return CommonUtilities.getStringAsJSONObject(serviceResponse);

    }
    
    @Override
    public JSONObject getInfinityUserServicedefsRoles(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
            throws DBPApplicationException {        

    	try {
        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", dbpServicesClaimsToken);
        String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.DBPSERVICE)
                        .withOperationId(OperationName.OP_GETINFINITYUSER_SERVICEDEFS_ROLES).withRequestHeaders(headerMap)
                        .withRequestParameters(postParametersMap).withPassThroughOutput(true).build().getResponse();
        return CommonUtilities.getStringAsJSONObject(serviceResponse);
    	}
    	catch (Exception e) {
		alert.prepareError("Error occured calling service getInfinityUserServiceDefsRoles",e).log();	
		}
    	return new JSONObject();

    }
    
    @Override
    public JSONObject getCoreCustomerRoleFeatureActionLimits(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
            throws DBPApplicationException {
        
        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", dbpServicesClaimsToken);
        String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.DBPSERVICE)
                        .withOperationId(OperationName.OP_GETCORECUSTOMERROLEFEATUREACTIONLIMITS).withRequestHeaders(headerMap)
                        .withRequestParameters(postParametersMap).withPassThroughOutput(true).build().getResponse();
        return CommonUtilities.getStringAsJSONObject(serviceResponse);

    }
    
    @Override
    public JSONObject getCoreCustomerProductRolesFeatureActionLimits(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
            throws DBPApplicationException {
        
        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", dbpServicesClaimsToken);
        String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.DBPSERVICE)
                        .withOperationId(OperationName.OP_GETCORECUSTOMERPRODUCTROLESFEATUREACTIONLIMITS).withRequestHeaders(headerMap)
                        .withRequestParameters(postParametersMap).withPassThroughOutput(true).build().getResponse();
        return CommonUtilities.getStringAsJSONObject(serviceResponse);

    }

	@Override
	public JSONObject getRelativeCoreCustomerContractDetails(Map<String, Object> postParametersMap,
			String dbpServicesClaimsToken) throws DBPApplicationException {

		Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", dbpServicesClaimsToken);
        String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.DBPSERVICE)
                        .withOperationId(OperationName.OP_GETRELATIVECORECUSTOMERCONTRACTDETAILS).withRequestHeaders(headerMap)
                        .withRequestParameters(postParametersMap).withPassThroughOutput(true).build().getResponse();
        return CommonUtilities.getStringAsJSONObject(serviceResponse);
	}

	@Override
	public JSONObject getCoreCustomerContractDetails(Map<String, Object> postParametersMap,
			String dbpServicesClaimsToken) throws DBPApplicationException {

		Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", dbpServicesClaimsToken);
        String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.DBPSERVICE)
                        .withOperationId(OperationName.OP_GETCORECUSTOMERCONTRACTDETAILS).withRequestHeaders(headerMap)
                        .withRequestParameters(postParametersMap).withPassThroughOutput(true).build().getResponse();
        return CommonUtilities.getStringAsJSONObject(serviceResponse);
	}

	@Override
	public JSONObject getInfinityUserContractDetails(Map<String, Object> postParametersMap,
			String dbpServicesClaimsToken) throws DBPApplicationException {
		
		Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", dbpServicesClaimsToken);
        String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.DBPSERVICE)
                        .withOperationId(OperationName.OP_GETINFINITYUSERCONTRACTDETAILS).withRequestHeaders(headerMap)
                        .withRequestParameters(postParametersMap).withPassThroughOutput(true).build().getResponse();
        return CommonUtilities.getStringAsJSONObject(serviceResponse);
	}

	@Override
	public JSONObject getInfinityUserAccounts(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
			throws DBPApplicationException {
		
		Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", dbpServicesClaimsToken);
        String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.DBPSERVICE)
                        .withOperationId(OperationName.OP_GETINFINITYUSERACCOUNTS).withRequestHeaders(headerMap)
                        .withRequestParameters(postParametersMap).withPassThroughOutput(true).build().getResponse();
        return CommonUtilities.getStringAsJSONObject(serviceResponse);
	}

	@Override
	public JSONObject getInfinityUserFeatureActions(Map<String, Object> postParametersMap,
			String dbpServicesClaimsToken) throws DBPApplicationException {
		
		Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", dbpServicesClaimsToken);
        String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.DBPSERVICE)
                        .withOperationId(OperationName.OP_GETINFINITYUSERFEATUREACTIONS).withRequestHeaders(headerMap)
                        .withRequestParameters(postParametersMap).withPassThroughOutput(true).build().getResponse();
        return CommonUtilities.getStringAsJSONObject(serviceResponse);
	}

	@Override
	public JSONObject getInfinityUserLimits(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
			throws DBPApplicationException {
		
		Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", dbpServicesClaimsToken);
        String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.DBPSERVICE)
                        .withOperationId(OperationName.OP_GETINFINITYUSERLIMITS).withRequestHeaders(headerMap)
                        .withRequestParameters(postParametersMap).withPassThroughOutput(true).build().getResponse();
        return CommonUtilities.getStringAsJSONObject(serviceResponse);
	}
	
	@Override

	public JSONObject getInfinityUserAccountsForCorecustomer(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
			throws DBPApplicationException {		

		Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", dbpServicesClaimsToken);
        String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.DBPSERVICE)
                        .withOperationId(OperationName.OP_GETINFINITYUSERACCOUNTSFORCORECUSTOMER).withRequestHeaders(headerMap)
                        .withRequestParameters(postParametersMap).withPassThroughOutput(true).build().getResponse();
        return CommonUtilities.getStringAsJSONObject(serviceResponse);
	}

}
