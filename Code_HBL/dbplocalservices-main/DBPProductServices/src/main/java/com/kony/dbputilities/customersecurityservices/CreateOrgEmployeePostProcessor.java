package com.kony.dbputilities.customersecurityservices;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.AdminUtil;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class CreateOrgEmployeePostProcessor implements DataPostProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object execute(Result result, DataControllerRequest dcRequest, DataControllerResponse dcResponse)
			throws Exception {
		Result retValue = new Result();

		Record dbxUsrAttr = result.getRecordById(DBPUtilitiesConstants.USR_ATTR);
		Record coreUsrAttr = result.getRecordById(DBPUtilitiesConstants.CORE_ATTR);
		Record accountsAttr = result.getRecordById(DBPUtilitiesConstants.CUSTOMER_ACCOUNTS_ATTR);
		Record limitsAttr = result.getRecordById("Limits_attr");

		String dbxErrorCode = HelperMethods.getFieldValue(dbxUsrAttr, ErrorCodeEnum.ERROR_CODE_KEY);
		String coreErrorCode = HelperMethods.getFieldValue(coreUsrAttr, ErrorCodeEnum.ERROR_CODE_KEY);
		String accountsErrorCode = HelperMethods.getFieldValue(accountsAttr, ErrorCodeEnum.ERROR_CODE_KEY);
		String limitsErrorCode = HelperMethods.getFieldValue(limitsAttr, ErrorCodeEnum.ERROR_CODE_KEY);

		if (dbxUsrAttr != null && dbxUsrAttr.getAllParams() != null) {
			for (Param param : dbxUsrAttr.getAllParams()) {
				retValue.addParam(param);
			}
		}

		if (StringUtils.isNotBlank(dbxErrorCode)) {
			Param p = new Param(DBPUtilitiesConstants.SUCCESS, null, DBPUtilitiesConstants.STRING_TYPE);
			retValue.addParam(p);
			return retValue;
		}

		try {
			if (coreUsrAttr != null && coreUsrAttr.getAllParams() != null) {
				for (Param param : coreUsrAttr.getAllParams()) {
					retValue.addParam(param);
				}
				if (StringUtils.isNotBlank(coreErrorCode)) {
					Param p = new Param(DBPUtilitiesConstants.SUCCESS, null, DBPUtilitiesConstants.STRING_TYPE);
					retValue.addParam(p);
					return retValue;
				}
			}

		} catch (Exception e) {
		}

		if (accountsAttr != null && accountsAttr.getAllParams() != null) {
			for (Param param : accountsAttr.getAllParams()) {
				retValue.addParam(param);
			}
		}
		if (StringUtils.isNotBlank(accountsErrorCode)) {
			Param p = new Param(DBPUtilitiesConstants.SUCCESS, null, DBPUtilitiesConstants.STRING_TYPE);
			retValue.addParam(p);
			return retValue;
		}

		if (limitsAttr != null && limitsAttr.getAllParams() != null) {
			for (Param param : limitsAttr.getAllParams()) {
				retValue.addParam(param);
			}
		}
		if (StringUtils.isNotBlank(limitsErrorCode)) {
			Param p = new Param(DBPUtilitiesConstants.SUCCESS, null, DBPUtilitiesConstants.STRING_TYPE);
			retValue.addParam(p);
			return retValue;
		}

		try {
			String userName = dcRequest.getParameter("UserName");
			if (StringUtils.isNotBlank(userName)) {
				if (isUserEsignAgreementReq(userName, dcRequest)) {
					retValue.addParam(new Param("isEAgreementRequired", "true", "String"));
					retValue.addParam(new Param("isEagreementSigned", "false", "String"));
					return retValue;
				}
			}
		} catch (Exception e) {
			alert.prepareError(e.getMessage()).log();
		}

		retValue.addParam(new Param("isEAgreementRequired", "false", "String"));
		retValue.addParam(new Param("isEagreementSigned", "false", "String"));
		return retValue;
	}

	private boolean isUserEsignAgreementReq(String userName, DataControllerRequest dcRequest) {
		Map<String, String> map = new HashMap<>();
		map.put("Username", userName);
		Result result;
		try {
			result = AdminUtil.invokeAPI(map, URLConstants.E_AGREEMENT_AVAILABLE, dcRequest);
		} catch (HttpCallException e) {

			alert.prepareError(e.getMessage()).log();
			return false;
		}
		return "true".equalsIgnoreCase(HelperMethods.getParamValue(result.getParamByName("isEAgreementAvailable")));
	}

}
