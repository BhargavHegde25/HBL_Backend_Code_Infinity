package com.temenos.infinity.api.accountsweeps.utils;

import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.exceptions.MiddlewareException;
import com.temenos.dbx.product.commonsutils.CustomerSession;

import java.util.Map;

/**
 * @author naveen.yerra
 */
public class AccountSweepCommonUtils {

    public static String fetchCustomerIdFromSession(DataControllerRequest request) {
        Map<String, Object> customer = CustomerSession.getCustomerMap(request);
        return CustomerSession.getCustomerId(customer);
    }

    public static String fetchCustomerIdFromSession(FabricRequestManager request) {
        try {
            return request.getServicesManager().getIdentityHandler().getUserAttributes().get("customer_id").toString();
        } catch (MiddlewareException e) {
            return null;
        }
    }
}
