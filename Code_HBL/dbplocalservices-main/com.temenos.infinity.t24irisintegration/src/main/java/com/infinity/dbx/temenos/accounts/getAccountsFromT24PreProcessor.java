/**
 * 
 */
package com.infinity.dbx.temenos.accounts;


import java.net.URLEncoder;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.infinity.dbx.dbp.jwt.auth.Authentication;
import com.infinity.dbx.temenos.TemenosBasePreProcessor;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.TokenUtils;
import com.kony.dbx.util.CommonUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;

public class getAccountsFromT24PreProcessor extends TemenosBasePreProcessor {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public boolean execute(HashMap params, DataControllerRequest request, DataControllerResponse response,
            Result result) throws Exception {
    	alert.prepareError("****************************************** GetAccountsByCoreCustomerIdListConcPreProcessor start ************************").log();
    	String userId = request.getParameter("loginUserId");
    	super.execute(params, request, response, result);
    	String customerId;
    	Boolean preLoginFlow = false;
    	if (StringUtils.isNotBlank(userId)) {
    		if(userId.contains("PreLogin-")) {
    			userId = userId.replace("PreLogin-", "");
    			preLoginFlow = true;
    			request.addRequestParam_(TemenosConstants.FLOW_TYPE, TemenosConstants.PRE_LOGIN_FLOW);
    			request.addRequestParam_("loginUserId", userId);
    		}
    	}
		Authentication authentication = Authentication.getInstance();
    	String authToken = TokenUtils.getT24AuthToken(request);
        diagnostic.prepareDebug("*************************** GetAccountsByCoreCustomerIdListConcPreProcessor authToken :"+authToken).log();
        diagnostic.prepareDebug("*************************** GetAccountsByCoreCustomerIdListConcPreProcessor params :"+params).log();
        diagnostic.prepareDebug("*************************** GetAccountsByCoreCustomerIdListConcPreProcessor getHeaderMap :"+request.getHeaderMap()).log();
        customerId = HelperMethods.getCustomerIdFromSession(request);
		String companyId = com.temenos.infinity.api.arrangements.utils.CommonUtils.getCompanyId(request);
		if (StringUtils.isBlank(companyId)) {
			companyId = EnvironmentConfigurationsHandler.getServerProperty(DBPUtilitiesConstants.BRANCH_ID_REFERENCE);
		}
        if(preLoginFlow)customerId=userId;
	    diagnostic.prepareDebug("*********** customerId  from request **********" + customerId).log();
        request.addRequestParam_("loginUserId", customerId);
        alert.prepareError("Logged in user id  : " + customerId).log();
        request.addRequestParam_(TemenosConstants.PARAM_AUTHORIZATION, authToken);
        String coreCustId = request.getParameter("coreCustomerIdList");
        String coreId = request.getParameter("Membership_id");
        if (StringUtils.isNotBlank(coreId)) {
            params.put("coreCustomerIdList",coreId );
            request.addRequestParam_("isRequestForActions", "true");
            request.addRequestParam_("coreCustomerIdList",coreId);      
            request.addRequestParam_("explicitCoreCustomerIdList"," ");
            alert.prepareError("****************************************** call with corecustomeridist ************************").log();
            return Boolean.TRUE.booleanValue();
        }
		if (StringUtils.isNotBlank(coreCustId)) {
            params.put("coreCustomerIdList",coreCustId );
            request.addRequestParam_("isRequestForActions", "true");
            request.addRequestParam_("coreCustomerIdList",coreCustId);            
            alert.prepareError("****************************************** call with corecustomeridist ************************").log();
            return Boolean.TRUE.booleanValue();
        }
		request.addRequestParam_("isRequestForActions", "false");
        
        HashMap<String, Object> inputParams = new HashMap<String, Object>();
        inputParams.put("$filter", "customerId eq " + customerId +" and companyLegalUnit eq "+companyId);
        request.addRequestParam_("$filter", "customerId eq " + customerId+" and companyLegalUnit eq "+companyId);
        
        alert.prepareError("Input params " + inputParams).log();
        Result coreCustomers = CommonUtils.callIntegrationService(request, inputParams, request.getHeaderMap(),
                SERVICE_BACKEND_CERTIFICATE, OP_CONTRACT_CUSTOMERS_GET, true);
        
        alert.prepareError("****************************************** coreCustomers : "+ResultToJSON.convert(coreCustomers)).log();
        StringBuilder coreCustomerIdList = new StringBuilder();
        StringBuilder explicitCoreCustomerIdList = new StringBuilder(" ");
        if (coreCustomers != null && coreCustomers.getAllDatasets().size() > 0
                && coreCustomers.getDatasetById("contractcustomers").getAllRecords().size() > 0) {
        	alert.prepareError("Records in contractcustomers ::::").log();
            for (Record record : coreCustomers.getDatasetById("contractcustomers").getAllRecords()) {
                coreCustomerIdList.append(record.getParamValueByName("coreCustomerId"));
                coreCustomerIdList.append(" ");
                if(record.getParamValueByName("autoSyncAccounts") == "false") {
                	explicitCoreCustomerIdList.append(record.getParamValueByName("coreCustomerId")+" ");
                }
            }
            params.put("coreCustomerIdList",URLEncoder.encode(coreCustomerIdList.toString().substring(0, coreCustomerIdList.length() - 1),
                    "UTF-8"));
            request.addRequestParam_("coreCustomerIdList",URLEncoder.encode(coreCustomerIdList.toString().substring(0, coreCustomerIdList.length() - 1),
                    "UTF-8"));
            request.addRequestParam_("explicitCoreCustomerIdList",explicitCoreCustomerIdList.toString());
            alert.prepareError("params  : " + params.toString()).log();
            alert.prepareError("****************************************** GetAccountsByCoreCustomerIdListConcPreProcessor true end ************************").log();
            return Boolean.TRUE;
        }
            result.addOpstatusParam(0);
            result.addHttpStatusCodeParam(200);
            return Boolean.FALSE;
    }
  
    
}
