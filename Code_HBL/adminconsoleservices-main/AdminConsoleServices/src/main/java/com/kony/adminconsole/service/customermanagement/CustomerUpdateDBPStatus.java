package com.kony.adminconsole.service.customermanagement;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.core.config.EnvironmentConfiguration;
import com.kony.adminconsole.exception.DBPAuthenticationException;
import com.kony.adminconsole.sca.SCAServices;
import com.kony.adminconsole.utilities.DBPServices;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

/**
 * CustomerUpdateDBPStatus service will update DBP user status
 * 
 * @author Alahari Prudhvi Akhil (KH2346)
 * 
 */
public class CustomerUpdateDBPStatus implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {
        Result processedResult = new Result();
        String customerUsername = requestInstance.getParameter("customerUsername");
        String status = requestInstance.getParameter("status");
        String remarks = requestInstance.getParameter("remarksForSuspend");
        String legalEntityList = requestInstance.getParameter("legalEntityList");
        try {

            JSONObject endpointResponse = DBPServices.updateDBPUserStatus(customerUsername, status, legalEntityList, requestInstance);
            
            if (endpointResponse == null || !endpointResponse.has(FabricConstants.OPSTATUS)
                    || endpointResponse.getInt(FabricConstants.OPSTATUS) != 0) {
            	
            	ErrorCodeEnum.ERR_20531.setErrorCode(processedResult);
            	processedResult.addParam(new Param("status", "Failure", FabricConstants.STRING));
            	return processedResult;
            	
            }else if (endpointResponse.has("dbpErrMsg") && 
            		StringUtils.isNotBlank(endpointResponse.getString("dbpErrMsg"))) {
            	processedResult.addParam(new Param("dbpErrMsg", endpointResponse.getString("dbpErrMsg")));
            	processedResult.addParam(new Param("dbpErrCode", endpointResponse.getString("dbpErrCode")));
            	processedResult.addParam(new Param("status", "Failure", FabricConstants.STRING));
                return processedResult;
                
            } else {
            	boolean isSCAEnabled = Boolean.parseBoolean(EnvironmentConfiguration.IS_SCA_ENABLED.getValue(requestInstance));
            	if(isSCAEnabled) {
            		HashMap<String, String> payload = new HashMap<String, String>();
            		payload.put("userId", customerUsername);
            		payload.put("status", status);
            		payload.put("legalEntityList",legalEntityList);
            		JSONObject response = SCAServices.updateUserStatus(payload);
            		diagnostic.prepareDebug("SCA User Status Update Response : "+response).log();
            	}
            	processedResult = CommonUtilities.constructResultFromJSONObject(endpointResponse);
            	//update customer table with remarks //remarksForSuspend
            	updateRemarks(remarks, customerUsername, requestInstance);
            	
            }
//            if (endpointResponse != null && endpointResponse.has(FabricConstants.OPSTATUS)
//                    && endpointResponse.getInt(FabricConstants.OPSTATUS) == 0) {
//                processedResult.addParam(new Param("status", endpointResponse.getString("Status")));
//            } else {
//                ErrorCodeEnum.ERR_20531.setErrorCode(processedResult);
//                return processedResult;
//            }
        } catch (DBPAuthenticationException dbpException) {
            alert.prepareError("DBP login failed. " + dbpException.getMessage()).log();
            ErrorCodeEnum.ERR_20933.setErrorCode(processedResult);
            processedResult.addParam(new Param("FailureReason", dbpException.getMessage(), FabricConstants.STRING));
        } catch (Exception e) {
            alert.prepareError("Unexpected error has occurred. " + e.getMessage()).log();
            ErrorCodeEnum.ERR_20691.setErrorCode(processedResult);
            processedResult.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
        }
        return processedResult;
    }
    
    private JSONObject updateRemarks(String remarks, String customerID, DataControllerRequest requestInstance) {
        Map<String, String> postParametersMap = new HashMap<String, String>();
        String customerId = getCustomerIDFromUsername(requestInstance, customerID);
        diagnostic.prepareDebug("Customer id from username## : "+customerId).log();
        postParametersMap.put("id", customerId);
        postParametersMap.put("remarksForSuspend", remarks);
        postParametersMap.put("lastmodifiedts", CommonUtilities.getISOFormattedLocalTimestamp());

        String updateEndpointResponse =
                Executor.invokeService(ServiceURLEnum.CUSTOMER_UPDATE, postParametersMap, null, requestInstance);
        return CommonUtilities.getStringAsJSONObject(updateEndpointResponse);
    }
    
    private String getCustomerIDFromUsername(DataControllerRequest request, String UserName) {

    	String customerresponse = null;
    	String customerId = null;
		JSONObject responseObj = new JSONObject();
		HashMap<String, String> map = new HashMap<String,String>();
		HashMap<String, Object> inputMap = new HashMap<String,Object>();
		inputMap.put("$filter", "UserName eq '"+ UserName + "'");
		
		try {
			customerresponse = DBPServiceExecutorBuilder.builder()
					.withServiceId("CRUDLayer")
					.withObjectId(null)
					.withDataControllerRequest(request)
					.withOperationId("dbxdb_customer_get")
					.withRequestParameters(inputMap)
					.build()
					.getResponse();
			responseObj  = new JSONObject(customerresponse);
			diagnostic.prepareDebug("Customer response responseObj ## : "+responseObj.toString()).log();
	        JSONArray customerArray = responseObj.getJSONArray("customer");
	        JSONObject customer = customerArray.getJSONObject(0);
	        customerId = customer.getString("id");
			diagnostic.prepareDebug("Customer id from username : "+customerId).log();
		}catch (Exception e) {
			diagnostic.prepareDebug("Exception in getCustomerIDFromUsername: "+e).log();
		}
		return customerId;
	}
}
