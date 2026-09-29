package com.kony.dbputilities.customersecurityservices;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonObject;
import com.kony.dbputilities.util.AdminUtil;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class PasswordSettings implements JavaService2 {
	private int passwordValidity;
	private boolean passwordExpiryWarningRequired;
	private int passwordExpiryWarningThreshold;
	private int passwordHistoryCount;
	private int accountLockoutThreshold;
	private int accountLockoutTime;
	private int recoveryEmailLinkValidity;

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		Result user = new Result();
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		String userName = inputParams.get("UserName");
		if (StringUtils.isBlank("userName")) {
			userName = HelperMethods.getUserFromIdentityService(dcRequest).get("userName");
		}
		JsonObject object = new JsonObject();
		Map<String, String> input = new HashMap<>();
		object = AdminUtil.invokeAPIAndGetJson(input, URLConstants.PASSWORD_LOCKOUT_SETTINGS, dcRequest);
		if (null != object) {
			Map<String, String> passwordlockoutsettings = HelperMethods
					.getRecordMap(object.get("passwordlockoutsettings").toString());
			this.passwordValidity = Integer.parseInt(passwordlockoutsettings.get("passwordValidity"));
			this.passwordExpiryWarningRequired = Boolean
					.parseBoolean(passwordlockoutsettings.get("passwordExpiryWarningRequired"));
			this.passwordExpiryWarningThreshold = Integer
					.parseInt(passwordlockoutsettings.get("passwordExpiryWarningThreshold"));
			this.passwordHistoryCount = Integer.parseInt(passwordlockoutsettings.get("passwordHistoryCount"));
			this.accountLockoutThreshold = Integer.parseInt(passwordlockoutsettings.get("accountLockoutThreshold"));
			this.accountLockoutTime = Integer.parseInt(passwordlockoutsettings.get("accountLockoutTime"));
			this.recoveryEmailLinkValidity = Integer.parseInt(passwordlockoutsettings.get("recoveryEmailLinkValidity"));
		} else {
			return result;
		}
		if (passwordExpiryWarningRequired) {
			String filter = "UserName" + DBPUtilitiesConstants.EQUAL + "'" + userName +"'";
			String createDate = "";
			Date createdts = null;
			Date expirydts = null;
			Date now = new Date();
			Calendar cal = Calendar.getInstance();
			user = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
					URLConstants.CUSTOMER_GET);
			if (HelperMethods.hasRecords(user)) {
				createDate = HelperMethods.getFieldValue(user, "createdts");
				createdts = HelperMethods.getFormattedTimeStamp(createDate);

				cal.setTime(createdts);
				cal.add(Calendar.DATE, this.passwordValidity);
				expirydts = cal.getTime();
				cal.add(Calendar.DATE, -this.passwordExpiryWarningThreshold);
				createdts = cal.getTime();
				if (now.after(createdts)) {
					expirydts.getTime();
					now.getTime();

				}

			}

		}

		return dcResponse;

	}

}
