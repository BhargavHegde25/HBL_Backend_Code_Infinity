package com.infinity.dbx.temenos.accounts;

import java.util.HashMap;


import com.google.gson.Gson;
import com.infinity.dbx.temenos.TemenosBasePreProcessor;
import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbx.objects.Customer;
import com.kony.dbx.util.Constants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class AccountsPreProcessor extends TemenosBasePreProcessor implements AccountsConstants {


    @Override
    public boolean execute(HashMap params, DataControllerRequest request, DataControllerResponse response,
            Result result)
            throws Exception {

        // super.execute(params, request, response, result);
        //
        // String customerType_id = params.get(TYPE_ID) != null ? params.get(TYPE_ID).toString() : "";
        // if(!"".equalsIgnoreCase(customerType_id) && !RETAIL_TYPE.equalsIgnoreCase(customerType_id)) {
        // result.addOpstatusParam(0);
        // result.addHttpStatusCodeParam(200);
        // return Boolean.FALSE;
        // }
        // request.addRequestParam_(PARAM_LOGINUSERID, params.get(PARAM_LOGINUSERID).toString());
        //
        // TemenosUtils temenosUtils = TemenosUtils.getInstance();
        // Gson gson = new Gson();
        // String customerGson = (String) temenosUtils.retreiveFromSession(Constants.SESSION_ATTRIB_CUSTOMER, request);
        // Object customerInSession = gson.fromJson(customerGson, Customer.class);
        // Customer cust = (Customer) customerInSession;
        // if(cust != null)
        // request.addRequestParam_(Constants.PARAM_ACCOUNT_HOLDER, cust.getFirstName() + " " + cust.getLastName());
        // String accountHolder = (String) temenosUtils.retreiveFromSession(params.get(USER_ID).toString(), request);

        result.addOpstatusParam(0);
        result.addHttpStatusCodeParam(200);
        return Boolean.FALSE;
    }
}
