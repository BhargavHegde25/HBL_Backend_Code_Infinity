package com.infinity.dbx.temenos.accounts;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.infinity.dbx.temenos.TemenosBasePreProcessor;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class GetAccountsForAdminRetailPreProcessor extends TemenosBasePreProcessor implements AccountsConstants {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public boolean execute(HashMap params, DataControllerRequest request, DataControllerResponse response,
            Result result)
            throws Exception {
        result.addOpstatusParam(0);
        result.addHttpStatusCodeParam(200);
        // result.addErrMsgParam("Misssing input param username");
        return Boolean.FALSE;

        // super.execute(params, request);
        // String username = params.get(AccountsConstants.PARAM_USERNAME) != null ?
        // params.get(AccountsConstants.PARAM_USERNAME).toString() : "";
        // if(username.equals("")) {
        // result.addOpstatusParam(0);
        // result.addHttpStatusCodeParam(200);
        // result.addErrMsgParam("Misssing input param username");
        // return Boolean.FALSE;
        // }
        //
        //
        // Map<String, String> typeIdMap = getCustomerTypeAndId( request, username);
        // String customerId = typeIdMap.get(Constants.KONY_DBX_USER_LOWERCASE_ID);
        // if(null == customerId || customerId.equals("")) {
        // result.addOpstatusParam(0);
        // result.addHttpStatusCodeParam(200);
        // result.addErrMsgParam("Customer Doesn't exist for the provided username");
        // return Boolean.FALSE;
        // }
        //
        // String customerType_id = typeIdMap.get(AccountsConstants.CUSTOMER_TYPE_ID);
        // if(!"".equalsIgnoreCase(customerType_id) && !RETAIL_TYPE.equalsIgnoreCase(customerType_id)) {
        // result.addOpstatusParam(0);
        // result.addHttpStatusCodeParam(200);
        // return Boolean.FALSE;
        // }
        //
        // String backendId = getBackendId(request, customerId, CONSTANT_TEMPLATE_NAME, Constants.PARAM_CUSTOMER_ID,
        // "1");
        // if(null == backendId || backendId.equals("")) {
        // result.addOpstatusParam(0);
        // result.addHttpStatusCodeParam(200);
        // result.addErrMsgParam("Customer Doesn't exist for the provided username.");
        // return Boolean.FALSE;
        // }
        //
        //
        // params.put(USER_ID, backendId);
        // request.addRequestParam_(USER_ID, backendId);
        // request.addRequestParam_(PARAM_LOGINUSERID, customerId);
        //
        // return Boolean.TRUE;
        // }
        //
        // public Map<String, String> getCustomerTypeAndId(DataControllerRequest request, String username) {
        //
        // String customerId = "";
        // String customerType_id = "";
        // Map<String, String> typeIdMap = new HashMap<String, String>();
        //
        // try {
        //
        // String filter = CommonUtils.buildOdataCondition(TemenosConstants.PARAM_USERNAME, Constants.EQUAL, username);
        // HashMap<String, Object> svcHeaders = new HashMap<String, Object>();
        // HashMap<String, Object> svcParams = new HashMap<String, Object>();
        //
        // svcParams.put(Constants.PARAM_DOLLAR_FILTER, filter);
        // Result result = CommonUtils.callIntegrationService(request, svcParams, svcHeaders,
        // Constants.DBX_DB_SERVICE_NAME, TemenosConstants.OP_CUSTOMER_GET, false);
        // Dataset customerDataset = result.getDatasetById(TemenosConstants.DS_CUSTOMER);
        // if ( null !=customerDataset ) {
        //
        // customerId = customerDataset.getRecord(0).getParamValueByName(Constants.KONY_DBX_USER_LOWERCASE_ID);
        // customerType_id = customerDataset.getRecord(0).getParamValueByName(AccountsConstants.CUSTOMER_TYPE_ID);
        //
        // }
        //
        // } catch(Exception e) {
        //
        // alert.prepareError("Error while retrieving backendId for "+ username ).log();
        // }
        //
        // typeIdMap.put(Constants.KONY_DBX_USER_LOWERCASE_ID, customerId );
        // typeIdMap.put(AccountsConstants.CUSTOMER_TYPE_ID, customerType_id );
        // return typeIdMap;
        // }
        //
        // public String getBackendId(DataControllerRequest request, String customerId, String backendType,
        // String identifier_name, String sequenceNumber) {
        //
        // String backendId = "";
        // String companyId = "";
        // try {
        //
        // HashMap<String, Object> svcHeaders = new HashMap<String, Object>();
        // HashMap<String, Object> svcParams = new HashMap<String, Object>();
        // String filter = CommonUtils.buildOdataCondition(Constants.CUSTOMER_ID, Constants.EQUAL, customerId);
        // filter = StringUtils.isNotBlank(sequenceNumber)
        // ? CommonUtils
        // .buildSearchGroupQuery(filter, Constants.AND,
        // CommonUtils.buildOdataCondition(Constants.PARAM_SEQUENCENUMBER,
        // Constants.EQUAL, sequenceNumber),
        // false)
        // : filter;
        // filter = StringUtils.isNotBlank(backendType)
        // ? CommonUtils.buildSearchGroupQuery(filter, Constants.AND,
        // CommonUtils.buildOdataCondition(Constants.BACKEND_TYPE, Constants.EQUAL, backendType), false)
        // : filter;
        // svcParams.put(Constants.PARAM_DOLLAR_FILTER, filter);
        // Result result = CommonUtils.callIntegrationService(request, svcParams, svcHeaders,
        // Constants.DBX_DB_SERVICE_NAME, Constants.DBX_DB_BACKEND_IDENTIFIER_GET, false);
        // Dataset backendIdentifiersDataset = result.getDatasetById(Constants.DS_BACKEND_IDENTIFIER);
        // if ( null !=backendIdentifiersDataset ) {
        // for (Record rec : backendIdentifiersDataset.getAllRecords()) {
        // if (identifier_name.equals(rec.getParamValueByName(Constants.PARAM_IDENTIFIER_NAME))) {
        // backendId = rec.getParamValueByName(Constants.BACKEND_ID);
        // companyId = rec.getParamValueByName(Constants.COMPANY_ID);
        // if(StringUtils.isNotEmpty(companyId))
        // request.addRequestParam_("companyId", companyId);
        // }
        //
        // }
        // }
        //
        //
        // } catch(Exception e) {
        // alert.prepareError("Error while retrieving backendId for "+ customerId ).log();
        // }
        //
        // return backendId;
    }
}