package com.kony.dbputilities.customersecurityservices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;
import org.json.JSONObject;

import com.kony.dbputilities.kms.KMSUtil;
import com.kony.dbputilities.sessionmanager.SessionScope;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.MWConstants;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.URLFinder;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class ResendOrgEmployeeActivationLink implements JavaService2 {


	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();

		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);

		String UserName = inputParams.get("UserName");
		if (StringUtils.isBlank(UserName)) {
			UserName = dcRequest.getParameter("UserName");
		}

		if (StringUtils.isBlank(UserName)) {
			ErrorCodeEnum.ERR_12416.setErrorCode(result);
			return result;
		}

		String requestedEmailId = inputParams.get("Email");
		if (StringUtils.isBlank(requestedEmailId)) {
			requestedEmailId = dcRequest.getParameter("Email");
		}

		Result user = HelperMethods.getUserRecordByName(UserName, dcRequest);

		if (!HelperMethods.hasRecords(user)) {
			ErrorCodeEnum.ERR_12417.setErrorCode(result);
			return result;
		}

		String status = HelperMethods.getFieldValue(user, "Status_id");
		String firstName = HelperMethods.getFieldValue(user, "FirstName");
		String lastName = HelperMethods.getFieldValue(user, "LastName");
		String Type_id = HelperMethods.getFieldValue(user, "CustomerType_id");
		String id = HelperMethods.getFieldValue(user, "id");
		String orgIdOfUserUnderUpdate = HelperMethods.getFieldValue(user, "Organization_Id");

		Map<String, String> loggedInUserInfo = HelperMethods.getCustomerFromIdentityService(dcRequest);
		String loggedInUserId = loggedInUserInfo.get("customer_id");
		String loggedInUserOrgId = HelperMethods.getOrganizationIDForUser(loggedInUserId, dcRequest);

		if (HelperMethods.isAuthenticationCheckRequiredForService(loggedInUserInfo)) {
			Set<String> userPermissions = SessionScope.getAllPermissionsFromIdentityScope(dcRequest);
			if (userPermissions.contains("USER_MANAGEMENT")) {
				if (StringUtils.isNotBlank(orgIdOfUserUnderUpdate)
						&& !loggedInUserOrgId.equals(orgIdOfUserUnderUpdate)) {
					ErrorCodeEnum.ERR_12419.setErrorCode(result);
					return result;
				}
			} else {
				ErrorCodeEnum.ERR_12418.setErrorCode(result);
				return result;
			}
		}

		Result userEmailsList = HelperMethods.getUserEmails(id, dcRequest);
		if (!HelperMethods.hasRecords(userEmailsList)) {
			ErrorCodeEnum.ERR_12420.setErrorCode(result);
			return result;
		}

		String primaryEmailid = "";
		List<String> emailIds = new ArrayList<>();

		for (Record record : userEmailsList.getAllDatasets().get(0).getAllRecords()) {
			emailIds.add(HelperMethods.getFieldValue(record, "Value"));
			if (HelperMethods.getFieldValue(record, "isPrimary").equals("true")) {
				primaryEmailid = HelperMethods.getFieldValue(record, "Value");
			}
		}

		String email = "";

		if (StringUtils.isNotBlank(requestedEmailId)) {
			if (emailIds.contains(requestedEmailId)) {
				email = requestedEmailId;
			} else {
				ErrorCodeEnum.ERR_12421.setErrorCode(result);
				return result;
			}
		} else {
			email = primaryEmailid;
		}

		if (StringUtils.isBlank(email)) {
			ErrorCodeEnum.ERR_12422.setErrorCode(result);
			return result;
		}

		if (!status.equals("SID_CUS_NEW")) {
			ErrorCodeEnum.ERR_12423.setErrorCode(result);
			return result;

		} else {

			Result existingRecords = HelperMethods.getActivationRecord(UserName, dcRequest);
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
			map.put("UserName", UserName);
			map.put("linktype", HelperMethods.CREDENTIAL_TYPE.ACTIVATION.toString());
			map.put("createdts", HelperMethods.getCurrentTimeStamp());

			Result createCredential = HelperMethods.callApi(dcRequest, map, HelperMethods.getHeaders(dcRequest),
					URLConstants.CREDENTIAL_CHECKER_CREATE);

			Result response = new Result();

			if (!HelperMethods.hasError(createCredential)) {
				String link = "";
				if (Type_id.equals("TYPE_ID_BUSINESS")) {
					link = URLFinder.getPathUrl(URLConstants.DBX_SBB_ACTIVATION_LINK, dcRequest) + "?qp="
							+ activationToken;
				} else if (Type_id.equals("TYPE_ID_MICRO_BUSINESS")) {
					link = URLFinder.getPathUrl(URLConstants.DBX_MB_ACTIVATION_LINK, dcRequest) + "?qp="
							+ activationToken;
				} else {
					link = URLFinder.getPathUrl(URLConstants.DBX_RETAIL_ACTIVATION_LINK, dcRequest) + "?qp="
							+ activationToken;
				}

				PasswordHistoryManagement pm = new PasswordHistoryManagement(dcRequest, true);
				Map<String, String> input = new HashMap<>();
				input.put("Subscribe", "true");
				input.put("FirstName", (StringUtils.isNotBlank(firstName)) ? firstName : "Banking User");
				input.put("EmailType", "activationLink");
				input.put("LastName", (StringUtils.isNotBlank(lastName)) ? lastName : "User");
				JSONObject addContext = new JSONObject();
				addContext.put("resetPasswordLink", link);
				addContext.put("userName", UserName);
				addContext.put("linkExpiry", String.valueOf(Math.floorDiv(pm.getRecoveryEmailLinkValidity(), 60)));
				input.put("AdditionalContext", KMSUtil.getOTPContent(null, null, addContext));
				input.put("Email", email);
				Map<String, String> headers = HelperMethods.getHeaders(dcRequest);
				headers.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
				response = HelperMethods.callApi(dcRequest, input, headers, URLConstants.DBX_SEND_EMAIL_ORCH);
			} else {
				Result returnResult = new Result();
				String error = HelperMethods.getError(createCredential);
				returnResult.addParam(new Param("status", error, MWConstants.STRING));
				ErrorCodeEnum.ERR_12424.setErrorCode(returnResult);
				return returnResult;
			}
			return postProcess(response);
		}

	}

	private Result postProcess(Result response) {
		Result result = new Result();
		if ("true".equals(HelperMethods.getParamValue(response.getParamByName("KMSemailStatus")))) {
			result.addParam(new Param("status", "Email sent successfully.", MWConstants.STRING));
			HelperMethods.setSuccessMsg(DBPUtilitiesConstants.SUCCESS_MSG, result);
		} else {
			String errorMsg = HelperMethods.getParamValue(response.getParamByName("KMSuserMsg"))
					+ HelperMethods.getParamValue(response.getParamByName("KMSemailMsg"));

			result.addParam(new Param("status", "Failed to send Email.", MWConstants.STRING));
			ErrorCodeEnum.ERR_12425.setErrorCode(result, errorMsg);
		}
		return result;
	}

}
