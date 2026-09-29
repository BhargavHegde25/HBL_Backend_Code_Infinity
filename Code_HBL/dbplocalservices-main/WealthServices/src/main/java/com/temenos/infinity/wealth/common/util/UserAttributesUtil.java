package com.temenos.infinity.wealth.common.util;

import java.util.HashMap;
import java.util.Map;

import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.utils.CustomerSessionsUtil;

import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;

/**
 * @author himaja.sridhar
 *
 */

public class UserAttributesUtil {	
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	
	public static Map<String, Object> getUserAttributes(DataControllerRequest request) throws Exception {
		diagnostic.prepareDebug("==========> UserAttributesUtil - Entered ").log();
		Map<String, Object> userAttributeMap = new HashMap<>();
		userAttributeMap = CustomerSessionsUtil.getLoggedInUserAttributesMap(request);
		diagnostic.prepareDebug("==========> UserAttributesUtil - Exited ").log();
		return userAttributeMap;
	}
	public static String getBackendId(DataControllerRequest request) throws Exception {
		Map<String, Object> userAttributeMap = getUserAttributes(request);
		if (userAttributeMap.get("backendIdentifiers") != null) {
			String backendId = userAttributeMap.get("backendIdentifiers") + "";

			return backendId;
		}
//        String backendId = userAttributeMap.get("backendIdentifiers").toString();
//        return backendId;
		return null;
    }
	public static String getCustomerFirstName(DataControllerRequest request) throws Exception {
		Map<String, Object> userAttributeMap = getUserAttributes(request);
        String firstName = userAttributeMap.get("FirstName").toString();
        return firstName;
    }
	public static String getCustomerLastName(DataControllerRequest request) throws Exception {
		Map<String, Object> userAttributeMap = getUserAttributes(request);
        String LastName = userAttributeMap.get("LastName").toString();
        return LastName;
    }
	public static String getCompanyId(DataControllerRequest request) throws Exception {
		Map<String, Object> userAttributeMap = getUserAttributes(request);
        String companyId = userAttributeMap.get("companyId").toString();
        return companyId;
    }
	
	public static String getCustomerId(DataControllerRequest request) throws Exception {
		Map<String, Object> userAttributeMap = getUserAttributes(request);
        String userId = userAttributeMap.get("UserName").toString();
        return userId;
    }
	
	public static String getUserName(DataControllerRequest request) throws Exception {
		Map<String, Object> userAttributeMap = getUserAttributes(request);
        String userName = userAttributeMap.get("customer_id").toString();
        return userName;
    }
}
