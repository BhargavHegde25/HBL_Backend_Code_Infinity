package com.kony.dbputilities.organisation;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;
import org.json.JSONObject;

import com.kony.dbputilities.kms.KMSUtil;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.URLFinder;
import com.konylabs.middleware.controller.DataControllerRequest;

public class SendEmailToEnrollOrganization {

	public void invoke(Map<String, String> inputParams, DataControllerRequest dcRequest) {
		String id = dcRequest.getParameter("id");
		if (StringUtils.isBlank(id)) {
			id = inputParams.get("id");
		}

		String email = dcRequest.getParameter("Email");
		if (StringUtils.isBlank(email)) {
			email = inputParams.get("Email");
		}

		String firstName = inputParams.get("FirstName");
		String lastName = inputParams.get("LastName");

		String link = URLFinder.getPathUrl(URLConstants.EMAIL_TO_ORGANIZATION_ENROLL, dcRequest);

		if (StringUtils.isNotBlank(email)) {
			Map<String, String> input = new HashMap<>();
			input.put("Subscribe", "true");
			input.put("EmailType", "emailtoenroll");
			input.put("FirstName", firstName);
			input.put("LastName", lastName);
			JSONObject addContext = new JSONObject();
			addContext.put("emailtoenroll", link);
			addContext.put("CompanyID", id);
			input.put("AdditionalContext", KMSUtil.getOTPContent(null, null, addContext));
			input.put("Email", email);
			Map<String, String> headers = HelperMethods.getHeaders(dcRequest);
			headers.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
			HelperMethods.callApiAsync(dcRequest, input, headers, URLConstants.DBX_SEND_EMAIL_ORCH);
		}
	}

}
