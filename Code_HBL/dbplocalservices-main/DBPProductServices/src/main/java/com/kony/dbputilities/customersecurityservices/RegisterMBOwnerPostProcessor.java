package com.kony.dbputilities.customersecurityservices;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.AdminUtil;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class RegisterMBOwnerPostProcessor implements DataPostProcessor2 {

	@Override
	public Object execute(Result result, DataControllerRequest dcRequest, DataControllerResponse dcResponse)
			throws Exception {

		String userName = dcRequest.getParameter("UserName");
		String createdUserId = HelperMethods.getParamValue(result.getParamByName("id"));
		String responseStatusCode = HelperMethods
				.getParamValue(result.getParamByName(DBPUtilitiesConstants.ERROR_CODE));
		boolean isAgreementRequired = false;

		if (StringUtils.isNotBlank(userName)) {
			try {
				isAgreementRequired = isUserEsignAgreementReq(userName, dcRequest);
			} catch (Exception e) {
				// log.error("Exception Caught: "+e);
			}
		}

		if (StringUtils.isNotBlank(userName) && StringUtils.isNotBlank(createdUserId)
				&& responseStatusCode.equals("3400")) {
			result.addParam(new Param("isEagreementSigned", "false", "String"));

			if (isAgreementRequired) {
				result.addParam(new Param("isEAgreementRequired", "true", "String"));
			} else {
				result.addParam(new Param("isEAgreementRequired", "false", "String"));
			}

		} else {
			if (StringUtils.isNotBlank(createdUserId)) {
				result.addParam(new Param("isEAgreementRequired", "false", "String"));
				result.addParam(new Param("isEagreementSigned", "false", "String"));
			}
		}

		return result;
	}

	private boolean isUserEsignAgreementReq(String userName, DataControllerRequest dcRequest) throws HttpCallException {
		Map<String, String> map = new HashMap<>();
		map.put("Username", userName);
		Result result = AdminUtil.invokeAPI(map, URLConstants.E_AGREEMENT_AVAILABLE, dcRequest);
		return "true".equalsIgnoreCase(result.getParamByName("isEAgreementAvailable").getValue());
	}

}
