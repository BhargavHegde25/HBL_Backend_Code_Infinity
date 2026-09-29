package com.kony.dbputilities.customersecurityservices;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONObject;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.kms.KMSUtil;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.URLFinder;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class SendCustomerUnlockEmail implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
	
		Result result = new Result();
		
		String isSuperAdmin = dcRequest.getParameter("isSuperAdmin");
		
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		if(StringUtils.isBlank(isSuperAdmin)) {
			isSuperAdmin = inputParams.get("isSuperAdmin");
		}
		
		
		if(!"true".equals(isSuperAdmin)) {
			result.addParam(new Param("mailRequestSent", "false"));
			ErrorCodeEnum.ERR_10191.setErrorCode(result);
			return result;
		}
		
		String userName = dcRequest.getParameter("userName");
		Result user = getUserStatus(dcRequest, userName);
		if (!HelperMethods.hasRecords(user)) {
			result.addParam(new Param("mailRequestSent", "false"));
			ErrorCodeEnum.ERR_10192.setErrorCode(result);
			return result;
		}

		String customer_Id = HelperMethods.getFieldValue(user, "id");

		Result customerCommunication = getCustomerCommunication(dcRequest, customer_Id);

		String email = HelperMethods.getFieldValue(customerCommunication, "Value");

		if (StringUtils.isBlank(email)) {
			result.addParam(new Param("mailRequestSent", "false"));
			ErrorCodeEnum.ERR_10193.setErrorCode(result);
			return result;
		}


		String firstName = HelperMethods.getFieldValue(user, "FirstName");
		String lastName = HelperMethods.getFieldValue(user, "LastName");

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
		map.put("UserName", customer_Id);
		map.put("linktype", HelperMethods.CREDENTIAL_TYPE.UNLOCK.toString());
		map.put("createdts", HelperMethods.getCurrentTimeStamp());
		Result createCredential = HelperMethods.callApi(dcRequest, map, HelperMethods.getHeaders(dcRequest),
				URLConstants.CREDENTIAL_CHECKER_CREATE);

		if (!HelperMethods.hasError(createCredential)) {

			String link;


			link = URLFinder.getPathUrl(URLConstants.DBX_CUSTOMER_UNLOCK_LINK, dcRequest) + "?qp="
					+ encodeToBase64(activationToken);

			PasswordHistoryManagement pm = new PasswordHistoryManagement(dcRequest, true);
			Map<String, String> input = new HashMap<>();
			input.put("Subscribe", "true");
			input.put("FirstName", firstName);
			input.put("EmailType", "UNLOCK_CUSTOMER");
			input.put("LastName", lastName);
			JSONObject addContext = new JSONObject();
			addContext.put("unlockAccountLink", link);
			addContext.put("userName", userName);
			addContext.put("linkExpiry", String.valueOf(Math.floorDiv(pm.getRecoveryEmailLinkValidity(), 60)));
			input.put("AdditionalContext", KMSUtil.getOTPContent(null, null, addContext));
			input.put("Email", email);
			Map<String, String> headers = HelperMethods.getHeaders(dcRequest);
			headers.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
			HelperMethods.callApiAsync(dcRequest, input, headers, URLConstants.DBX_SEND_EMAIL_ORCH);
			result.addParam(new Param("mailRequestSent", "true"));
			
			return result;
		}
		
		result.addParam(new Param("mailRequestSent", "false"));
		return result;
	}


	private Result getCustomerCommunication(DataControllerRequest dcRequest, String customer_Id) {
		String filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + customer_Id + DBPUtilitiesConstants.AND +
				"isPrimary" + DBPUtilitiesConstants.EQUAL + "1" +DBPUtilitiesConstants.AND + "Type_id" + DBPUtilitiesConstants.EQUAL + HelperMethods.getCommunicationTypes().get("Email"); 
		try {
			return HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
					URLConstants.CUSTOMERCOMMUNICATION_GET);
		} catch (HttpCallException e) {
			alert.prepareError(e.getMessage()).log();
		}
		return null;
	}

	private Result getUserStatus(DataControllerRequest dcRequest, String userName) {
		String filter = "UserName" + DBPUtilitiesConstants.EQUAL + userName;
		try {
			return HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
					URLConstants.CUSTOMERVERIFY_GET);
		} catch (HttpCallException e) {
			alert.prepareError(e.getMessage()).log();
		}

		return null;
	}

	public static String encodeToBase64(String sourceString) {
		if (sourceString == null) {
			return null;
		}
		return new String(java.util.Base64.getEncoder().encode(sourceString.getBytes()));
	}
}
