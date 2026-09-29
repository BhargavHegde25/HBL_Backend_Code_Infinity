package com.kony.eum.dbputilities.customersecurityservices;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.temenos.dbx.product.utils.CustomerSessionsUtil;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.BCrypt;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.kony.eum.dbputilities.customersecurityservices.PasswordHistoryManagement;
import com.kony.eum.dbputilities.customersecurityservices.UpdateDBXUserPassword;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class UpdateDBXUserPassword implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {

		Result result = new Result();
		String userId = HelperMethods.getCustomerIdFromSession(dcRequest);
		String oldPassword = dcRequest.getParameter("oldPassword");
		String newPassword = dcRequest.getParameter("newPassword");

		PasswordHistoryManagement pm = new PasswordHistoryManagement(dcRequest, true);
		if (preProcess(result, dcRequest, userId, newPassword, oldPassword, pm)) {
			return updateUserPassword(dcRequest, userId, newPassword, pm);
		}

		return postProcess(result, dcRequest, pm);
	}

	private boolean preProcess(Result result, DataControllerRequest dcRequest, String userId, String newPassword,
			String password, PasswordHistoryManagement pm) {

		String oldPassword = null;
		if (StringUtils.isNotBlank(pm.getDbpErrorCode())) {
			ErrorCodeEnum.ERR_10164.setErrorCode(result, pm.getDbpErrorCode(), pm.getDbpErrorMessage());
			return false;
		}
		if (StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(password) && StringUtils.isNotBlank(newPassword)
				&& !password.equals(newPassword)) {
			try {
				Result customerResult = HelperMethods.callGetApi(dcRequest, "id eq " + userId,
						HelperMethods.getHeaders(dcRequest), URLConstants.CUSTOMER_GET);
				if (HelperMethods.hasRecords(customerResult)) {
					oldPassword = HelperMethods.getFieldValue(customerResult, "Password");
					// log.error("reached here-1");
					return BCrypt.checkpw(password, oldPassword);
				}
			} catch (HttpCallException e) {

				alert.prepareError(e.getMessage()).log();
			}
		}
		return false;
	}

	private Result postProcess(Result result, DataControllerRequest dcRequest, PasswordHistoryManagement pm)
			throws HttpCallException {

		Result retResult = new Result();
		if (HelperMethods.hasRecords(result)) {
			retResult.addParam(new Param("success", "success", DBPUtilitiesConstants.STRING_TYPE));
			return retResult;
		} else {
			ErrorCodeEnum.ERR_10145.setErrorCode(retResult,
					"Password is already present in the previous " + pm.getPasswordHistoryCount() + " passwords.");

			if (StringUtils.isNotBlank(pm.getDbpErrorCode())) {
				ErrorCodeEnum.ERR_10164.setErrorCode(retResult, pm.getDbpErrorCode(),
						pm.getDbpErrorMessage());
			}
			return retResult;
		}
	}

	private Result updateUserPassword(DataControllerRequest dcRequest, String userId, String newPassword,
			PasswordHistoryManagement pm) throws HttpCallException {

		Map<String, String> input = new HashMap<>();
		input.put("id", userId);

		String filterQuery = "id" + DBPUtilitiesConstants.EQUAL + userId;
		Result result = HelperMethods.callGetApi(dcRequest, filterQuery, HelperMethods.getHeaders(dcRequest),
				URLConstants.CUSTOMER_GET);

		String username = HelperMethods.getFieldValue(result, "UserName");

		if (pm.checkForPasswordEntry(dcRequest, username, newPassword)) {
			String salt = BCrypt.gensalt(DBPUtilitiesConstants.SALT_NUMBER_OF_ROUNDS);
			String hashedPassword = BCrypt.hashpw(newPassword, salt);
			input.put("Password", hashedPassword);
			input.put("createdts", HelperMethods.getCurrentTimeStamp());
			Result checkUpdate = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
					URLConstants.CUSTOMER_UPDATE);
			if (HelperMethods.hasRecords(checkUpdate)) {
				if (pm.makePasswordEntry(dcRequest, userId, hashedPassword)) {
					result.addParam(new Param("success", "success", DBPUtilitiesConstants.STRING_TYPE));
					CustomerSessionsUtil.deleteActiveUserSessionsIfAny(userId, username, dcRequest);
				} else {
					ErrorCodeEnum.ERR_10140.setErrorCode(result);
				}
				return result;
			}
		} else {
			result = new Result();
			ErrorCodeEnum.ERR_10145.setErrorCode(result,
					"Password is already present in the previous " + pm.getPasswordHistoryCount() + " passwords.");
			return result;
		}
		return result;

	}

}