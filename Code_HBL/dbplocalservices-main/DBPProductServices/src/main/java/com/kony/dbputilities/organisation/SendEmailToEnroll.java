package com.kony.dbputilities.organisation;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;
import org.json.JSONObject;

import com.kony.dbputilities.kms.KMSUtil;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.MWConstants;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.URLFinder;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class SendEmailToEnroll implements JavaService2 {

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		String id = dcRequest.getParameter("id");
		if (StringUtils.isBlank(id)) {
			id = inputParams.get("id");
		}
		String userName = dcRequest.getParameter("UserName");
		if (StringUtils.isBlank(userName)) {
			userName = inputParams.get("UserName");
		}
		String firstName = dcRequest.getParameter("FirstName");
		if (StringUtils.isBlank(firstName)) {
			firstName = StringUtils.isBlank(inputParams.get("FirstName")) ? "Banking" : inputParams.get("FirstName");
		}
		String lastName = dcRequest.getParameter("LastName");
		if (StringUtils.isBlank(lastName)) {
			lastName = StringUtils.isBlank(inputParams.get("LastName")) ? "User" : inputParams.get("LastName");
		}
		String email = dcRequest.getParameter("Email");
		if (StringUtils.isBlank(email)) {
			email = inputParams.get("Email");
		}

		Result response = new Result();
		String link = URLFinder.getPathUrl(URLConstants.EMAIL_TO_ENROLL, dcRequest);

		if (StringUtils.isNotBlank(email)) {
			Map<String, String> input = new HashMap<>();
			input.put("Subscribe", "true");
			input.put("FirstName", firstName);
			input.put("EmailType", "emailtoenroll");
			input.put("LastName", lastName);
			JSONObject addContext = new JSONObject();
			addContext.put("emailtoenroll", link);
			addContext.put("userName", userName);
			addContext.put("CompanyID", id);
			addContext.put("firstName", userName);
			input.put("AdditionalContext", KMSUtil.getOTPContent(null, null, addContext));
			input.put("Email", email);
			Map<String, String> headers = HelperMethods.getHeaders(dcRequest);
			headers.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
			response = HelperMethods.callApi(dcRequest, input, headers, URLConstants.DBX_SEND_EMAIL_ORCH);
			return postProcess(response);
		}

		response.addParam(new Param("status_email", "Email sending Failed.", MWConstants.STRING));
		return response;
	}

	private Result postProcess(Result response) {
		Result result = new Result();
		if ("true".equals(HelperMethods.getParamValue(response.getParamByName("KMSemailStatus")))) {
			result.addParam(new Param("status_email", "Email sent successfully.", MWConstants.STRING));
		} else {
			result.addParam(new Param("status_email", "Failed to send Email.", MWConstants.STRING));
		}
		return result;
	}

}
