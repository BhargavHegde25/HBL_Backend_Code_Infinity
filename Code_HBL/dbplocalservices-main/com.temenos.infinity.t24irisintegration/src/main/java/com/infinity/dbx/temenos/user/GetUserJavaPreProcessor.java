package com.infinity.dbx.temenos.user;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.infinity.dbx.temenos.TemenosBasePreProcessor;
import com.konylabs.middleware.api.processor.IdentityHandler;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.utils.CustomerSessionsUtil;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

public class GetUserJavaPreProcessor extends TemenosBasePreProcessor {
	 
	@Override
	public boolean execute(HashMap params, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
 
		if (StringUtils.isNotEmpty(request.getParameter("partyID"))) {
			result.addOpstatusParam(0);
			result.addHttpStatusCodeParam(200);
			return Boolean.FALSE;
		}
		if (StringUtils.isNotEmpty(request.getParameter("loginUserId"))) {
			return Boolean.TRUE;
		}
		Map<String, Object> userAttributes = request.getServicesManager().getIdentityHandler().getUserAttributes();
		// CustomerSessionsUtil.getLoggedInUserAttributesMap(request);
		if (userAttributes != null && userAttributes.size() > 0) {
			String coreIdentifier = null != userAttributes.get("backendIdentifiers")
					? (String) userAttributes.get("backendIdentifiers")
					: "";
			request.addRequestParam_("coreIdentifier", coreIdentifier);
			params.put("coreIdentifier", coreIdentifier);
			String loginUserId = null != userAttributes.get("user_id") ? (String) userAttributes.get("user_id") : "";
			request.addRequestParam_("loginUserId", loginUserId);
			params.put("loginUserId", loginUserId);
			String userName = null != userAttributes.get("UserName") ? (String) userAttributes.get("UserName") : "";
			request.addRequestParam_("userName", userName);
			params.put("userName", userName);
			String type_id = null != userAttributes.get("CustomerType_id") ? (String) userAttributes.get("CustomerType_id") : "";
			request.addRequestParam_("type_id", type_id);
			params.put("type_id", type_id);
			request.addRequestParam_("isSuperAdmin",
					null != userAttributes.get("isSuperAdmin") ? (String) userAttributes.get("isSuperAdmin") : "false");
		}
		return Boolean.TRUE;
 
	}
}
