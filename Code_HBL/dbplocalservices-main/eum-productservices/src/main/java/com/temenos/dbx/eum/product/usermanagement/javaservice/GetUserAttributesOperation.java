package com.temenos.dbx.eum.product.usermanagement.javaservice;

import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.memorymanagement.MemoryManager;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.temenos.dbx.eum.product.usermanagement.javaservice.GetUserAttributesOperation;
import com.temenos.dbx.eum.product.usermanagement.resource.api.CustomerIdentityAttributesResource;

public class GetUserAttributesOperation implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	private static final int EXPIRY_TIME = (20 * 60);

	/*
	 * 1)look for cache if user attributes are available 2) if available it will
	 * return UA 3) if not available then call resource.getUserAttributes and save
	 * in cache and return
	 */
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		Result result = new Result();
		try {
			Map<String, Object> identityInfo = HelperMethods.getIdentityServiceInfo(request);
			
			String sessiontoken = "";
			if (identityInfo.containsKey("CustomerType_id") && identityInfo.get("CustomerType_id") != null
					&& identityInfo.get("CustomerType_id").toString().equalsIgnoreCase("DBP_API_USER"))
				return result;
			if (identityInfo.containsKey("session_token") && identityInfo.get("session_token") != null) {
				sessiontoken = identityInfo.get("session_token").toString();
			}

			String userId = null != identityInfo.get("user_id") ? identityInfo.get("user_id").toString() : "";
			String serviceRespcache = (String) MemoryManager.getFromCache(sessiontoken + userId + "_USER_ATTRIBUTES");
			alert.prepareError("userId->"+userId+"sessiontoken->"+sessiontoken+"_USER_ATTRIBUTES"+serviceRespcache).log();
			if (StringUtils.isEmpty(serviceRespcache)) {
				CustomerIdentityAttributesResource resource = DBPAPIAbstractFactoryImpl
						.getResource(CustomerIdentityAttributesResource.class);
				result = resource.getUserAttributes(methodID, inputArray, request, response);
				String userAttributes = ResultToJSON.convert(result);
				MemoryManager.saveIntoCache(sessiontoken + userId + "_USER_ATTRIBUTES", userAttributes, EXPIRY_TIME);
			} else {
				result = JSONToResult.convert(serviceRespcache);
			}
		} catch (ApplicationException e) {
			alert.prepareError("Exception occured in GetUserAttributesOperation" , e).log();
		} catch (Exception e) {
			alert.prepareError("Exception occured in GetUserAttributesOperation" , e).log();
		}
		return result;
	}
}
