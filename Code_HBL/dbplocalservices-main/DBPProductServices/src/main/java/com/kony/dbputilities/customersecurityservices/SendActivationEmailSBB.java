package com.kony.dbputilities.customersecurityservices;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;
import org.json.JSONObject;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.kms.KMSUtil;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodes;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.MWConstants;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.URLFinder;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class SendActivationEmailSBB implements JavaService2 {

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		String id = dcRequest.getParameter("id");
		if (StringUtils.isBlank(id)) {
			id = inputParams.get("id");
		}
		Result user = getUserStatus(dcRequest, id);
		String status = HelperMethods.getFieldValue(user, "Status_id");

		if (!status.equals("SID_CUS_NEW")) {
			return new Result();
		} else {
			String userName = dcRequest.getParameter("UserName");
			String firstName = (StringUtils.isNotBlank(inputParams.get("FirstName"))) ? inputParams.get("FirstName")
					: "Banking";
			String lastName = (StringUtils.isNotBlank(inputParams.get("LastName"))) ? inputParams.get("LastName")
					: "User";
			String email = dcRequest.getParameter("Email");

			Result existingRecords = HelperMethods.getActivationRecord(userName, dcRequest);
			if (HelperMethods.hasRecords(existingRecords)) {
				String existingToken = HelperMethods.getFieldValue(existingRecords, "id");
				Map<String, String> map = new HashMap<>();
				map.put("id", existingToken);
				HelperMethods.callApi(dcRequest, map, HelperMethods.getHeaders(dcRequest),
						URLConstants.CREDENTIAL_CHECKER_DELETE);
			}

			String activationToken = UUID.randomUUID().toString();
			Map<String, String> map = new HashMap<>();
			map.put("id", activationToken);
			map.put("UserName", userName);
			map.put("linktype", HelperMethods.CREDENTIAL_TYPE.ACTIVATION.toString());
			map.put("createdts", HelperMethods.getCurrentTimeStamp());
			Result createCredential = HelperMethods.callApi(dcRequest, map, HelperMethods.getHeaders(dcRequest),
					URLConstants.CREDENTIAL_CHECKER_CREATE);

			Result response = new Result();

			if (!HelperMethods.hasError(createCredential)) {
				String link = URLFinder.getPathUrl(URLConstants.DBX_SBB_ACTIVATION_LINK, dcRequest) + "?qp="
						+ encodeToBase64(activationToken);

				PasswordHistoryManagement pm = new PasswordHistoryManagement(dcRequest, true);
				Map<String, String> input = new HashMap<>();
				input.put("Subscribe", "true");
				input.put("FirstName", firstName);
				input.put("EmailType", "activationLink");
				input.put("LastName", lastName);
				JSONObject addContext = new JSONObject();
				addContext.put("resetPasswordLink", link);
				addContext.put("userName", userName);
				addContext.put("linkExpiry", String.valueOf(Math.floorDiv(pm.getRecoveryEmailLinkValidity(), 60)));
				input.put("AdditionalContext", KMSUtil.getOTPContent(null, null, addContext));
				input.put("Email", email);
				Map<String, String> headers = HelperMethods.getHeaders(dcRequest);
				headers.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
				response = HelperMethods.callApi(dcRequest, input, headers, URLConstants.DBX_SEND_EMAIL_ORCH);
			}
			return postProcess(response);
		}

	}

	private Result postProcess(Result response) {
		Result result = new Result();
		if ("true".equals(HelperMethods.getParamValue(response.getParamByName("KMSemailStatus")))) {
			result.addParam(new Param("status", "Email sent successfully.", MWConstants.STRING));
			HelperMethods.setSuccessMsgwithCode("Email sent successfully.", ErrorCodes.RECORD_FOUND, result);
		} else {
			String errorMsg = HelperMethods.getParamValue(response.getParamByName("KMSuserMsg"))
					+ HelperMethods.getParamValue(response.getParamByName("KMSemailMsg"));
			result.addParam(new Param("status", "Failed to send Email.", MWConstants.STRING));
			HelperMethods.setSuccessMsgwithCode(errorMsg, "5008", result);
		}
		return result;
	}

	private Result getUserStatus(DataControllerRequest dcRequest, String id) throws HttpCallException {
		String filter = "id" + DBPUtilitiesConstants.EQUAL + id;
		return HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
				URLConstants.CUSTOMERVERIFY_GET);
	}

	public static String encodeToBase64(String sourceString) {
		if (sourceString == null) {
			return null;
		}
		return new String(java.util.Base64.getEncoder().encode(sourceString.getBytes()));
	}
}
