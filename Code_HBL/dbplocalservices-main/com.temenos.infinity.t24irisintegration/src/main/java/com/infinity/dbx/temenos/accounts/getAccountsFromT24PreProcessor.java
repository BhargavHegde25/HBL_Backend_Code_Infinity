/**
 *
 */
package com.infinity.dbx.temenos.accounts;


import java.net.URLEncoder;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;

import com.hbl.infinity.accounts.perf.GetListPerfConstants;
import com.hbl.infinity.accounts.perf.GetListSnapshotCache;
import com.hbl.infinity.accounts.perf.GetListTimer;
import com.hbl.infinity.accounts.perf.T24PreSnapshot;
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

public class getAccountsFromT24PreProcessor extends TemenosBasePreProcessor {
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public boolean execute(HashMap params, DataControllerRequest request, DataControllerResponse response,
            Result result) throws Exception {
        GetListTimer timer = GetListTimer.start(GetListPerfConstants.COMPONENT_T24_PRE);
        try {
            return prepareRequest(params, request, response, result, timer);
        } finally {
            timer.finish();
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private boolean prepareRequest(HashMap params, DataControllerRequest request, DataControllerResponse response,
            Result result, GetListTimer timer) throws Exception {
    	String userId = request.getParameter("loginUserId");
    	super.execute(params, request, response, result);
    	timer.mark("basePreProcessor");
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
		Authentication.getInstance();
    	String authToken = getT24AuthToken(request);
    	timer.mark("authToken");
        customerId = HelperMethods.getCustomerIdFromSession(request);
		String companyId = com.temenos.infinity.api.arrangements.utils.CommonUtils.getCompanyId(request);
		if (StringUtils.isBlank(companyId)) {
			companyId = EnvironmentConfigurationsHandler.getServerProperty(DBPUtilitiesConstants.BRANCH_ID_REFERENCE);
		}
        if(preLoginFlow)customerId=userId;
        request.addRequestParam_("loginUserId", customerId);
        request.addRequestParam_(TemenosConstants.PARAM_AUTHORIZATION, authToken);
        String coreCustId = request.getParameter("coreCustomerIdList");
        String coreId = request.getParameter("Membership_id");
        if (StringUtils.isNotBlank(coreId)) {
            params.put("coreCustomerIdList",coreId );
            request.addRequestParam_("isRequestForActions", "true");
            request.addRequestParam_("coreCustomerIdList",coreId);
            request.addRequestParam_("explicitCoreCustomerIdList"," ");
            debug("call with Membership_id");
            return Boolean.TRUE.booleanValue();
        }
		if (StringUtils.isNotBlank(coreCustId)) {
            params.put("coreCustomerIdList",coreCustId );
            request.addRequestParam_("isRequestForActions", "true");
            request.addRequestParam_("coreCustomerIdList",coreCustId);
            debug("call with coreCustomerIdList");
            return Boolean.TRUE.booleanValue();
        }
		request.addRequestParam_("isRequestForActions", "false");

        HashMap<String, Object> inputParams = new HashMap<String, Object>();
        inputParams.put("$filter", "customerId eq " + customerId +" and companyLegalUnit eq "+companyId);
        request.addRequestParam_("$filter", "customerId eq " + customerId+" and companyLegalUnit eq "+companyId);

        // getList cache (HBL_GETLIST_CACHE_ENABLED): the contract-customer lookup depends only on the customer and
        // the company, so a stored result for the same inputs and permission version is replayed instead.
        GetListSnapshotCache.Session snapshots = GetListSnapshotCache.open(GetListPerfConstants.STAGE_T24_PRE,
                customerId, customerId, companyId);
        T24PreSnapshot cached = snapshots == null ? null : snapshots.read(T24PreSnapshot.class);
        timer.mark("snapshotLookup");
        if (cached != null) {
            params.put("coreCustomerIdList", cached.getCoreCustomerIdList());
            request.addRequestParam_("coreCustomerIdList", cached.getCoreCustomerIdList());
            request.addRequestParam_("explicitCoreCustomerIdList", cached.getExplicitCoreCustomerIdList());
            debug("core customer list replayed from the getList cache");
            return Boolean.TRUE;
        }

        Result coreCustomers = CommonUtils.callIntegrationService(request, inputParams, request.getHeaderMap(),
                SERVICE_BACKEND_CERTIFICATE, OP_CONTRACT_CUSTOMERS_GET, true);
        timer.mark("contractCustomers");

        StringBuilder coreCustomerIdList = new StringBuilder();
        StringBuilder explicitCoreCustomerIdList = new StringBuilder(" ");
        if (coreCustomers != null && coreCustomers.getAllDatasets().size() > 0
                && coreCustomers.getDatasetById("contractcustomers").getAllRecords().size() > 0) {
            for (Record record : coreCustomers.getDatasetById("contractcustomers").getAllRecords()) {
                coreCustomerIdList.append(record.getParamValueByName("coreCustomerId"));
                coreCustomerIdList.append(" ");
                // Known defect (kept on purpose, see DEPLOY_NOTES.md): '==' compares references, so this is
                // effectively never true and explicitCoreCustomerIdList stays " ". Changing it to equals()
                // would change which accounts go to new-account processing.
                if(record.getParamValueByName("autoSyncAccounts") == "false") {
                	explicitCoreCustomerIdList.append(record.getParamValueByName("coreCustomerId")+" ");
                }
            }
            String encodedCoreCustomerIdList = URLEncoder.encode(
                    coreCustomerIdList.toString().substring(0, coreCustomerIdList.length() - 1), "UTF-8");
            params.put("coreCustomerIdList", encodedCoreCustomerIdList);
            request.addRequestParam_("coreCustomerIdList", encodedCoreCustomerIdList);
            request.addRequestParam_("explicitCoreCustomerIdList",explicitCoreCustomerIdList.toString());
            if (snapshots != null) {
                snapshots.store(new T24PreSnapshot(encodedCoreCustomerIdList, explicitCoreCustomerIdList.toString()));
            }
            debug("core customer list resolved from contractcustomers");
            return Boolean.TRUE;
        }
            result.addOpstatusParam(0);
            result.addHttpStatusCodeParam(200);
            return Boolean.FALSE;
    }

    /**
     * Returns the T24 token for this request. The base preprocessor has already generated one with the same
     * inputs (FLOW_TYPE is PRE_LOGIN_FLOW at both points), so it is reused instead of signing a second JWT. A
     * new token is generated only when the base did not set one.
     */
    private static String getT24AuthToken(DataControllerRequest request) {
        String authToken = request.getParameter(TemenosConstants.PARAM_AUTHORIZATION);
        if (StringUtils.isBlank(authToken)) {
            authToken = TokenUtils.getT24AuthToken(request);
        }
        return authToken;
    }

    private static void debug(String message) {
        if (diagnostic.isDebugEnabled()) {
            diagnostic.prepareDebug("getAccountsFromT24PreProcessor: " + message).log();
        }
    }

}
