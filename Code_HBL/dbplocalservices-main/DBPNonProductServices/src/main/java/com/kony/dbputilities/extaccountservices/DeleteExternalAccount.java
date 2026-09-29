package com.kony.dbputilities.extaccountservices;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPInputConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class DeleteExternalAccount implements JavaService2 {

    @SuppressWarnings("rawtypes")
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);

        if (preProcess(inputParams, dcRequest, result)) {
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.EXT_ACCOUNTS_DELETE);
        }

        return result;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private boolean preProcess(Map<String,String> inputParams, DataControllerRequest dcRequest, Result result)
            throws HttpCallException {
        HelperMethods.removeNullValues(inputParams);
        boolean status = true;
        String userId = inputParams.get(DBPInputConstants.USER_ID);
        String companyId = inputParams.get(DBPUtilitiesConstants.COMPANYID);
        String payeeId = inputParams.get(DBPInputConstants.ID);

        if(StringUtils.isBlank(userId))
            userId = HelperMethods.getUserIdFromSession(dcRequest);
        if(StringUtils.isBlank(companyId))
            companyId = HelperMethods.getOrganizationIDForUser(userId,dcRequest);

        String acctNum = inputParams.get(DBPInputConstants.ACCT_NUM);
        String iBAN = inputParams.get(DBPInputConstants.IBAN);
        if(StringUtils.isBlank(iBAN))
        	iBAN = inputParams.get("iban");
        String id = getId(dcRequest, acctNum, userId, iBAN, payeeId, companyId);

        if (!StringUtils.isNotBlank(id)) {
            HelperMethods.setValidationMsg("No records to display", dcRequest, result);
            status = false;
        }

        if (status) {
            inputParams.remove(DBPInputConstants.ACCT_NUM);
            inputParams.remove(DBPInputConstants.IBAN);
            inputParams.put(DBPUtilitiesConstants.EXT_ID, id);
            inputParams.put(DBPUtilitiesConstants.SOFT_DEL, "true");
        }

        return status;
    }

    private String getId(DataControllerRequest dcRequest, String acctNum, String userId, String iBAN, String payeeId, String companyId)
            throws HttpCallException {
    	String id = null;
        String query = "";
        if (StringUtils.isNotBlank(acctNum)) {
            query = query + DBPUtilitiesConstants.ACCNT_NUM + DBPUtilitiesConstants.EQUAL + acctNum;
        } else {
            query = query + DBPInputConstants.IBAN + DBPUtilitiesConstants.EQUAL + iBAN;
        }

        String userFilter = null;
        if (StringUtils.isNotBlank(userId)) {
        	userFilter = DBPUtilitiesConstants.USER_ID + DBPUtilitiesConstants.EQUAL + userId;
        }
        if (StringUtils.isNotBlank(companyId)) {
            if(StringUtils.isNotBlank(userFilter))
            	userFilter = userFilter + DBPUtilitiesConstants.OR;
            userFilter = userFilter + DBPUtilitiesConstants.P_ORGANIZATION_ID + DBPUtilitiesConstants.EQUAL + companyId;
        }
        
        if(userFilter != null) {
        	query = query + DBPUtilitiesConstants.AND + "(" + userFilter + ") ";
        }
        
        if (StringUtils.isNotBlank(payeeId)) {
        	query = query + DBPUtilitiesConstants.AND + DBPUtilitiesConstants.EXT_ID + DBPUtilitiesConstants.EQUAL + payeeId;
        }
        
        Result result = HelperMethods.callGetApi(dcRequest, query, HelperMethods.getHeaders(dcRequest),
                URLConstants.EXT_ACCOUNTS_GET);
        Dataset ds = result.getAllDatasets().get(0);
        if (null != ds && 0 != ds.getAllRecords().size()) {
            Record temp = ds.getRecord(0);
            id = temp.getParam(DBPUtilitiesConstants.EXT_ID).getValue();
        }
        return id;
    }
}
