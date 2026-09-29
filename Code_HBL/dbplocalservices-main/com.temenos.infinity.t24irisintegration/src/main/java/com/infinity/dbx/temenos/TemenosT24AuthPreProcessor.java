package com.infinity.dbx.temenos;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;

import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.kony.dbputilities.util.TokenUtils;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.utils.CustomerSessionsUtil;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

public class TemenosT24AuthPreProcessor implements DataPreProcessor2, TemenosConstants {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public boolean execute(HashMap params, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {

		request.addRequestParam_(TemenosConstants.FLOW_TYPE, TemenosConstants.PRE_LOGIN_FLOW);
		String serviceName = request.getParameter("current_appID").toUpperCase();
		String authToken = "";
		if (serviceName.equals("T24ISPAYMENTORDERS")) {
			request.addRequestParam_("issuer", "Fabric");
			authToken = TokenUtils.getT24AuthToken(request);
			if (StringUtils.isBlank(authToken)) {
				return false;
			}

		} else
			authToken = TokenUtils.getT24AuthToken(request);

		request.addRequestParam_(TemenosConstants.PARAM_AUTHORIZATION, authToken);

		alert.prepareError("$$T24authToken$$ : " + authToken).log();

		String companyId = null;
		companyId = request.getParameter("legalEntityId");

		request.addRequestParam_("companyId", companyId);

		if (StringUtils.isBlank(companyId)) {
			companyId = request.getHeader("companyId");
		}

		if (StringUtils.isNotBlank(companyId)) {
			request.getHeaderMap().put("companyId", companyId);
		}

		if (serviceName.equalsIgnoreCase("T24BulkPaymentAPIs")) {
			String userSignOnName = (String) CustomerSessionsUtil.getLoggedInUserAttributesMap(request).get("UserName");
			request.getHeaderMap().put("aliasInfo", userSignOnName);
		}

		return Boolean.TRUE;
	}

}
