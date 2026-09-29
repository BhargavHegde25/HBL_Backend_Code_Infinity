package com.kony.dbputilities.customersecurityservices;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONArray;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.EncryptionUtils;
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

/**
 * Custom Identity service used by API consumers of DBP App
 * 
 * @author Chandan Gupta
 *
 */
public class SchedulingEngineIdentityService implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	private static final String API_ACCESS_TOKEN_HEADER = "X-Kony-DBP-API-Access-Token";

	public static final String API_USER_ID = "DBP_API_USER";
	public static final String API_USER_TYPE = "DBP_API_USER";
	public static final String API_ACCESS_PERMISSION = "API_ACCESS";

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Result result = new Result();
		login(request, result);
		return result;
	}

	private void login(DataControllerRequest request, Result result) {
		String accessTokenOnRequest = request.getHeader(API_ACCESS_TOKEN_HEADER);
		processSharedSecret(accessTokenOnRequest, request, result);
	}

	private void processSharedSecret(String sharedSecret, DataControllerRequest dcRequest, Result result) {
		String sharedSecretFromDB = "";
		String key = "";
		String filter = "";
		Result systemConfig = new Result();

		filter = "PropertyName" + DBPUtilitiesConstants.EQUAL + "SCH_EG_API_ACCESS_TOKEN";
		try {
			systemConfig = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
					URLConstants.SYSTEM_CONFIGURATION_GET);
		} catch (HttpCallException e) {
			alert.prepareError(e.getMessage()).log();
		}
		sharedSecretFromDB = HelperMethods.getFieldValue(systemConfig, "PropertyValue");
		alert.prepareError("db shared secret " + sharedSecret).log();
		try {
			key = URLFinder.getPathUrl(URLConstants.DBP_ACCESS_TOKEN, dcRequest);
		} catch (Exception e) {
			key = "";
			alert.prepareError(e.getMessage()).log();
		}
		if (StringUtils.isBlank(key) || StringUtils.isBlank(sharedSecretFromDB) || StringUtils.isBlank(sharedSecret)) {
			ErrorCodeEnum.ERR_10086.setErrorCode(result);
			return;
		}
		try {
			sharedSecretFromDB = EncryptionUtils.decrypt(sharedSecretFromDB, key);
		} catch (Exception e) {

			alert.prepareError(e.getMessage()).log();
		}
		if (sharedSecret.equals(sharedSecretFromDB)) {
			Record userAttributesRecord = new Record();
			userAttributesRecord.setId("user_attributes");

			userAttributesRecord.addParam(new Param("user_id", "SchedulingEngine", MWConstants.STRING));
			userAttributesRecord.addParam(new Param("customerTypeId", API_USER_TYPE, MWConstants.STRING));
			userAttributesRecord.addParam(new Param("CustomerType_id", API_USER_TYPE, MWConstants.STRING));
			userAttributesRecord.addParam(new Param("UserName", "SchedulingEngine", MWConstants.STRING));
			userAttributesRecord.addParam(new Param("isSchedulingEngine", "true", MWConstants.STRING));

			Record securityAttributesRecord = new Record();
			securityAttributesRecord.setId("security_attributes");
			securityAttributesRecord
					.addParam(new Param("session_token", UUID.randomUUID().toString(), MWConstants.STRING));
			securityAttributesRecord.addParam(new Param("session_ttl", "-1", MWConstants.INT));
			JSONArray currPermissionJSONArray = new JSONArray();
			currPermissionJSONArray.put(API_ACCESS_PERMISSION);
			securityAttributesRecord
					.addParam(new Param("permissions", currPermissionJSONArray.toString(), MWConstants.STRING));

			result.addRecord(userAttributesRecord);
			result.addRecord(securityAttributesRecord);
			result.addParam(
					new Param(MWConstants.HTTP_STATUS_CODE, String.valueOf(HttpStatus.SC_OK), MWConstants.INT));
		} else {
			ErrorCodeEnum.ERR_10086.setErrorCode(result);
		}
	}

}
