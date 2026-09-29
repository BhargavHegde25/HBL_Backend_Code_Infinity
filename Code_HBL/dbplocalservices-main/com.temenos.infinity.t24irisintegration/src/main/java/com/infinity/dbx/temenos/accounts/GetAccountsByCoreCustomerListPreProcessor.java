package com.infinity.dbx.temenos.accounts;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.infinity.dbx.temenos.TemenosBasePreProcessor;
import com.kony.dbputilities.sessionmanager.SessionScope;
import com.kony.dbputilities.util.DBPDatasetConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbx.util.CommonUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.utils.InfinityConstants;
import com.konylabs.middleware.dataobject.Record;

public class GetAccountsByCoreCustomerListPreProcessor extends TemenosBasePreProcessor {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public boolean execute(HashMap params, DataControllerRequest request, DataControllerResponse response,
            Result result) throws Exception {
        String loginUserId = "";
        StringBuilder coreCustomerIdList = new StringBuilder();

        Map<String, String> loggedInUserInfo = HelperMethods.getCustomerFromAPIDBPIdentityService(request);
        if (HelperMethods.isAuthenticationCheckRequiredForService(loggedInUserInfo)) {
            loginUserId = HelperMethods.getCustomerIdFromSession(request);
        } else {
            loginUserId = (String) params.get(InfinityConstants.id);
            request.addRequestParam_(InfinityConstants.id, (String) params.get(InfinityConstants.id));
        }

        if (StringUtils.isBlank(loginUserId)) {
            super.execute(params, request);
            String companyId =
                    EnvironmentConfigurationsHandler.getServerProperty(DBPUtilitiesConstants.BRANCH_ID_REFERENCE);
            request.getHeaderMap().put("companyId", companyId);
            if (params.containsKey(InfinityConstants.coreCustomerId)
                    && params.get(InfinityConstants.coreCustomerId) != null) {
                coreCustomerIdList.append(params.get(InfinityConstants.coreCustomerId));
                coreCustomerIdList.append(" ");
            }
        } else {
            super.execute(params, request, response, result);
            alert.prepareError("Logged in user id  : " + loginUserId).log();
            HashMap<String, Object> inputParams = new HashMap<String, Object>();
            String filter = InfinityConstants.customerId + " eq " + loginUserId + " and "
                    + InfinityConstants.autoSyncAccounts + " eq 1";
            inputParams.put("$filter", filter);
            request.addRequestParam_("$filter", filter);
            alert.prepareError("Input params " + inputParams).log();
            Result coreCustomers = CommonUtils.callIntegrationService(request, inputParams, request.getHeaderMap(),
                    SERVICE_BACKEND_CERTIFICATE, OP_CONTRACT_CUSTOMERS_GET, true);

            if (coreCustomers != null && coreCustomers.getAllDatasets().size() > 0
                    && coreCustomers.getDatasetById(DBPDatasetConstants.DATASET_CONTRACT_CUSTOMERS).getAllRecords()
                            .size() > 0) {
                for (Record record : coreCustomers.getDatasetById(DBPDatasetConstants.DATASET_CONTRACT_CUSTOMERS)
                        .getAllRecords()) {
                    coreCustomerIdList.append(record.getParamValueByName(InfinityConstants.coreCustomerId));
                    coreCustomerIdList.append(" ");
                }
                alert.prepareError("coreCustomerIdList  : " + coreCustomerIdList.toString()).log();
            }
        }
        if (coreCustomerIdList.length() > 0) {
            params.put("coreCustomerIdList",
                    URLEncoder.encode(coreCustomerIdList.toString().substring(0, coreCustomerIdList.length() - 1),
                            "UTF-8"));
            return Boolean.TRUE;
        }

        result.addOpstatusParam(0);
        result.addHttpStatusCodeParam(200);
        return Boolean.FALSE;
    }
}
