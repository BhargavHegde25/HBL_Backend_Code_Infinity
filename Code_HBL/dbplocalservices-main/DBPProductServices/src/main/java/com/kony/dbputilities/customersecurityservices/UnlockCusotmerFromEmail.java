package com.kony.dbputilities.customersecurityservices;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONObject;

import com.dbp.core.constants.DBPConstants;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.AdminUtil;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class UnlockCusotmerFromEmail implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
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
		String credentialType = new String();

		if (status && HelperMethods.hasRecords(existingRecord)) {

			createdDate = HelperMethods.getFieldValue(existingRecord, "createdts");
			credentialType = HelperMethods.getFieldValue(existingRecord, "linktype");

			if (createdDate.trim().isEmpty() || !HelperMethods.isDateInRange(createdDate, pm.getRecoveryEmailLinkValidity())
					|| !credentialType.equals(HelperMethods.CREDENTIAL_TYPE.UNLOCK.toString())) {
				status = false;
			}
			
			inputParams.put("id", HelperMethods.getFieldValue(existingRecord, "UserName"));
			
		} else {
			status = false;
		}

		return status;
	}

	private boolean validateInput(Map<String, String> inputParams) {
		String identifier = inputParams.get("identifier");

		if (StringUtils.isBlank(identifier)) {
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
		Param p = new Param(DBPUtilitiesConstants.RESULT_MSG, "updated", DBPConstants.FABRIC_STRING_CONSTANT_KEY);
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
		inputParams.put("lockCount", "0");
		inputParams.put("isUserAccountLocked", "0");

		String id = (String) inputParams.get("id");

		StringBuilder sb = new StringBuilder();
		sb.append("id").append(DBPUtilitiesConstants.EQUAL).append(id);

		Result rs = HelperMethods.callGetApi(dcRequest, sb.toString(), HelperMethods.getHeaders(dcRequest),
				URLConstants.CUSTOMERVERIFY_GET);

		if (!HelperMethods.hasRecords(rs)) {
			ErrorCodeEnum.ERR_10174.setErrorCode(result);
			return false;
		}
		String customertypeId = HelperMethods.getFieldValue(rs, "CustomerType_id");
		if (StringUtils.isNotBlank(customertypeId) && accessGranted(customertypeId, dcRequest)) {
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