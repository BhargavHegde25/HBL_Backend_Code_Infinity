package com.kony.dbputilities.extaccountservices;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class GetAllExternalAccountsWithPagination implements JavaService2 {
    @SuppressWarnings("rawtypes")
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map<String,String> inputParams = HelperMethods.getInputParamMap(inputArray);

        if (preProcess(inputParams, dcRequest, result)) {
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.EXT_ACCOUNTS_GET);
        }

        return result;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private boolean preProcess(Map<String,String> inputParams, DataControllerRequest dcRequest, Result result)
            throws HttpCallException {
        HelperMethods.removeNullValues(inputParams);
        String userFilter = "";
        /* fetch from input params */
        String userId = inputParams.get(DBPUtilitiesConstants.CUSTOMERID);
        String companyId = inputParams.get(DBPUtilitiesConstants.COMPANYID);
        /* if input params does not have this fetch from session */
        if(StringUtils.isBlank(userId))
            userId = HelperMethods.getUserIdFromSession(dcRequest);
        if(StringUtils.isBlank(companyId))
            companyId = HelperMethods.getOrganizationIDForUser(userId,dcRequest);

        if(StringUtils.isNotBlank(userId)) {
        	userFilter = DBPUtilitiesConstants.USER_ID + DBPUtilitiesConstants.EQUAL + userId;
        }
        if(StringUtils.isNotBlank(companyId)) {
        	if(StringUtils.isNotBlank(userFilter)) {
        		userFilter = userFilter + DBPUtilitiesConstants.OR;
        	}
    		userFilter = userFilter + DBPUtilitiesConstants.P_ORGANIZATION_ID + DBPUtilitiesConstants.EQUAL + companyId;
        }

    	userFilter = "(" + userFilter + ")";
        
        String sortBy = (String) inputParams.get(DBPUtilitiesConstants.SORTBY);
        String order = (String) inputParams.get(DBPUtilitiesConstants.ORDER);
        String skip = (String) inputParams.get("offset");
        String top = (String) inputParams.get("limit");
        if (!StringUtils.isNotBlank(sortBy)) {
            sortBy = "Id";
        }
        if (!StringUtils.isNotBlank(order)) {
            order = "asc";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(userFilter);
        sb.append(DBPUtilitiesConstants.AND);
        sb.append(DBPUtilitiesConstants.SOFT_DEL).append(DBPUtilitiesConstants.EQUAL).append("0");
        inputParams.put(DBPUtilitiesConstants.FILTER, sb.toString());
        inputParams.put(DBPUtilitiesConstants.TOP, top);
        inputParams.put(DBPUtilitiesConstants.SKIP, skip);
        inputParams.put(DBPUtilitiesConstants.ORDERBY, sortBy + " " + order + " ");
        return true;
    }
}
