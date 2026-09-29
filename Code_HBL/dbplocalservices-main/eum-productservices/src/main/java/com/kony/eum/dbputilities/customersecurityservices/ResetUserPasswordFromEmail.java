package com.kony.eum.dbputilities.customersecurityservices;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.dbp.core.constants.DBPConstants;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.mfa.MFAConstants;
import com.kony.dbputilities.util.AdminUtil;
import com.kony.dbputilities.util.BCrypt;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.kony.eum.dbputilities.customersecurityservices.PasswordHistoryManagement;
import com.kony.eum.dbputilities.customersecurityservices.ResetUserPasswordFromEmail;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class ResetUserPasswordFromEmail implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {

		Result result = new Result();
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		PasswordHistoryManagement pm = new PasswordHistoryManagement(dcRequest, true);

		boolean inputValidation = validateInput(inputParams);

		if (!inputValidation) {
			ErrorCodeEnum.ERR_10168.setErrorCode(result, "Invalid Request Payload");
			return result;
		}

		boolean identifierValidation = validateIdentifier(dcRequest, inputParams, pm);

		if (!identifierValidation) {
			ErrorCodeEnum.ERR_10169.setErrorCode(result, "Invalid identifier");
			return result;
		}

		if (preProcess(inputParams, dcRequest, result, pm)) {
			result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
					URLConstants.CUSTOMER_UPDATE);
			if (StringUtils.isNotBlank(getUserId(result))
					&& pm.makePasswordEntry(dcRequest, getUserId(result), inputParams.get("Password"))) {
				String securityKey = inputParams.get("link");
				deleteResetLinkPostPasswordUpdate(securityKey, dcRequest);
				result = postProcess(result, dcRequest);
			} else {
				ErrorCodeEnum.ERR_10170.setErrorCode(result);
			}
		}
		return result;
	}

	private boolean validateIdentifier(DataControllerRequest dcRequest, Map<String, String> inputParams,
			PasswordHistoryManagement pm) {

		String link = inputParams.get("identifier");
		String UserName = inputParams.get("UserName");

		boolean status = true;
		try {
			link = new String(java.util.Base64.getDecoder().decode(link));

			String[] strings = link.split("_");

			link = strings[0];

		} catch (Exception e) {
			alert.prepareError(e.getMessage()).log();
			status = false;
		}

		if (!status) {
			return status;
		}

		if (StringUtils.isBlank(link)) {
			status = false;
		}

		inputParams.put("link", link);
		Result existingRecord = new Result();
		try {
			existingRecord = HelperMethods.getActivationRecordByActivationId(link, dcRequest);
		} catch (HttpCallException e) {
			alert.prepareError(e.getMessage()).log();
			status = false;
		}
		String createdDate = new String();
		String createdUser = new String();
		String credentialType = new String();

		if (status && HelperMethods.hasRecords(existingRecord)) {

			createdDate = HelperMethods.getFieldValue(existingRecord, "createdts");
			createdUser = HelperMethods.getFieldValue(existingRecord, "UserName");
			credentialType = HelperMethods.getFieldValue(existingRecord, "linktype");

			if (createdDate.trim().isEmpty() || !createdUser.equalsIgnoreCase(UserName)
					|| !HelperMethods.isDateInRange(createdDate, pm.getRecoveryEmailLinkValidity())
					|| !credentialType.equals(HelperMethods.CREDENTIAL_TYPE.RESETPASSWORD.toString())) {
				status = false;
			}
		} else {
			status = false;
		}

		return status;
	}

	private boolean validateInput(Map<String, String> inputParams) {
		String UserName = inputParams.get("UserName");
		String Password = inputParams.get("Password");
		String identifier = inputParams.get("identifier");

		if (StringUtils.isBlank(UserName) || StringUtils.isBlank(Password) || StringUtils.isBlank(identifier)) {
			return false;
		}

		return true;
	}

	private String getUserId(Result result) {
		String id = "";
		Dataset ds = result.getAllDatasets().get(0);
		if (null != ds && !ds.getAllRecords().isEmpty()) {
			id = ds.getRecord(0).getParam("id").getValue();
		}
		return id;
	}

	private Result postProcess(Result result, DataControllerRequest dcRequest) throws HttpCallException {
		Result retVal = new Result();
		boolean eSignAgreementRequired = isUserEsignAgreementReq(result.getAllDatasets().get(0).getRecord(0),
				dcRequest);
		boolean isEagreementSigned = esignStatus(result.getAllDatasets().get(0).getRecord(0));
		Param p = new Param(DBPUtilitiesConstants.RESULT_MSG, "updated", DBPConstants.FABRIC_STRING_CONSTANT_KEY);
		retVal.addParam(new Param("isEAgreementRequired", "" + eSignAgreementRequired, "String"));
		retVal.addParam(new Param("isEagreementSigned", "" + isEagreementSigned, "String"));
		HelperMethods.setSuccessMsg("updated", retVal);
		retVal.addParam(p);
		return retVal;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result, 
			PasswordHistoryManagement pm) throws Exception {

		if (StringUtils.isNotBlank(pm.getDbpErrorCode())) {
			ErrorCodeEnum.ERR_10171.setErrorCode(result, pm.getDbpErrorCode(), pm.getDbpErrorMessage());
			return false;
		}
		boolean status = true;
		Result credentialchecker = new Result();
		String securityKey = "";
		String userName = (String) inputParams.get("UserName");
		String password = (String) inputParams.get("Password");
		inputParams.put("inputPassword", password);

		if (StringUtils.isBlank(userName) || StringUtils.isBlank(password)) {
			ErrorCodeEnum.ERR_10172.setErrorCode(result);
			return false;
		}

		securityKey = (String) inputParams.get(MFAConstants.SECURITY_KEY);
		if (StringUtils.isNotBlank(securityKey)) {
			String filter = "id" + DBPUtilitiesConstants.EQUAL + securityKey;
			credentialchecker = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
					URLConstants.CREDENTIAL_CHECKER_GET);
			if (HelperMethods.hasRecords(credentialchecker)) {
				String userName2 = HelperMethods.getFieldValue(credentialchecker, "UserName");
				String type = HelperMethods.getFieldValue(credentialchecker, "linktype");
				String createdDate = HelperMethods.getFieldValue(credentialchecker, "createdts");
				if (!(StringUtils.isNotBlank(userName2) && userName.equals(userName2)
						&& type.equals(HelperMethods.CREDENTIAL_TYPE.RESETPASSWORD.toString())
						&& !pm.isEmailRecoveryLinkExpired(dcRequest, createdDate))) {
					ErrorCodeEnum.ERR_10173.setErrorCode(result);
					return false;
				}
			} else {
				ErrorCodeEnum.ERR_10173.setErrorCode(result);
				return false;
			}
		}

		StringBuilder sb = new StringBuilder();
		sb.append("UserName").append(DBPUtilitiesConstants.EQUAL).append(userName);

		Result rs = HelperMethods.callGetApi(dcRequest, sb.toString(), HelperMethods.getHeaders(dcRequest),
				URLConstants.CUSTOMERVERIFY_GET);

		if (!HelperMethods.hasRecords(rs)) {
			ErrorCodeEnum.ERR_10174.setErrorCode(result);
			return false;
		}
		String customertypeId = HelperMethods.getFieldValue(rs, "CustomerType_id");
		if (StringUtils.isNotBlank(customertypeId) && accessGranted(customertypeId, dcRequest)) {
			if (HelperMethods.hasRecords(rs)) {
				inputParams.put("id", HelperMethods.getFieldValue(rs, "id"));
			} else {
				ErrorCodeEnum.ERR_10174.setErrorCode(result);
				status = false;
			}

			if (HelperMethods.getFieldValue(rs, "Status_id").equals("SID_CUS_ACTIVE")
					&& StringUtils.isNotBlank(HelperMethods.getFieldValue(rs, "Password"))) {
				if (pm.checkForPasswordEntry(dcRequest, userName, password)) {
					String salt = BCrypt.gensalt(DBPUtilitiesConstants.SALT_NUMBER_OF_ROUNDS);
					String hashedPassword = BCrypt.hashpw(password, salt);
					inputParams.put("Password", hashedPassword);
				} else {
					ErrorCodeEnum.ERR_10175.setErrorCode(result,
							"Password is already present in the previous " + pm.getPasswordHistoryCount()
									+ " passwords.");
					return false;
				}
			} else {
				ErrorCodeEnum.ERR_10176.setErrorCode(result, "Password cannot be reset");
				return false;
			}

			return status;
		}
		ErrorCodeEnum.ERR_10177.setErrorCode(result);
		return false;
	}

	private boolean accessGranted(String customer_type_id, DataControllerRequest dcRequest)
			throws UnsupportedEncodingException, HttpCallException {
		Map<String, String> inputParams = new HashMap<>();
		Result result = new Result();
		StringBuilder sb = new StringBuilder();
		String reportingParams = dcRequest.getHeader("X-Kony-ReportingParams");
		if (StringUtils.isNotBlank(reportingParams)) {
			JSONObject reportingParamsJson = new JSONObject(
					URLDecoder.decode(reportingParams, StandardCharsets.UTF_8.name()));
			String appId = reportingParamsJson.optString("aid");
			if (StringUtils.isNotBlank(appId)) {
				sb.append("CustomerType_id").append(DBPUtilitiesConstants.EQUAL).append(customer_type_id);
				sb.append(DBPUtilitiesConstants.AND);
				sb.append("Appid").append(DBPUtilitiesConstants.EQUAL).append(appId);
				inputParams.put(DBPUtilitiesConstants.FILTER, sb.toString());
				result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
						URLConstants.CUSTOMER_TYPE_CONFIG);
				if ("1".equals(HelperMethods.getFieldValue(result, "AccessPermitted"))
						|| "true".equalsIgnoreCase(HelperMethods.getFieldValue(result, "AccessPermitted"))) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean isUserEsignAgreementReq(Record user, DataControllerRequest dcRequest) throws HttpCallException {
		Map<String, String> map = new HashMap<>();
		map.put("Username", HelperMethods.getFieldValue(user, "UserName"));
		Result result = AdminUtil.invokeAPI(map, URLConstants.E_AGREEMENT_AVAILABLE, dcRequest);
		return "true".equalsIgnoreCase(HelperMethods.getParamValue(result.getParamByName("isEAgreementAvailable")));
	}

	private boolean esignStatus(Record user) {
		return "true".equalsIgnoreCase(HelperMethods.getFieldValue(user, "isEagreementSigned"));
	}

	private void deleteResetLinkPostPasswordUpdate(String securityKey, DataControllerRequest dcRequest) {
		Map<String, String> deleteRecord = new HashMap<>();
		deleteRecord.put("id", securityKey);
		try {
			HelperMethods.callApi(dcRequest, deleteRecord, HelperMethods.getHeaders(dcRequest),
					URLConstants.CREDENTIAL_CHECKER_DELETE);
		} catch (Exception e) {
			alert.prepareError(e.getMessage()).log();
		}
	}
}