package com.kony.notificationservice;

import java.util.Map;
import org.json.JSONObject;

import com.dbp.core.constants.DBPConstants;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.LegalEntityUtil;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

public class DeleteNotification implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings("rawtypes")
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		Map inputParams = HelperMethods.getInputParamMap(inputArray);
		try {
			if (preProcess(inputParams, dcRequest, result)) {
				result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
						URLConstants.USER_NOTIFICATION_DELETE);
			} else {
				if (inputParams.containsKey("isLEPresent") 
						&& ("false").equals(inputParams.get("isLEPresent"))) {
					result.addErrMsgParam(ErrorCodeEnum.ERR_29040.getMessage());
					throw new Exception(ErrorCodeEnum.ERR_29040.getMessage());
				}
				if (inputParams.containsKey("isSecurityException")
						&& ("true").equals(inputParams.get("isSecurityException"))) {
					result.addErrMsgParam(ErrorCodeEnum.ERR_10149.getMessage());
					throw new Exception(ErrorCodeEnum.ERR_10149.getMessage());
				} 
			}
			if (!HelperMethods.hasError(result)) {
				result = postProcess(result);
			}
		} catch(Exception e) {
			alert.prepareError(e.getMessage()).log();
			result.addOpstatusParam(-1);
			result.addHttpStatusCodeParam(400);
		}

		return result;
	}

	private Result postProcess(Result result) {
		Result retVal = new Result();
		if (!HelperMethods.hasError(retVal)) {
			Param p = new Param(DBPUtilitiesConstants.RESULT_MSG, "Notification is deleted",
					DBPConstants.FABRIC_STRING_CONSTANT_KEY);
			retVal.addParam(p);
		}
		return retVal;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result)
			throws HttpCallException {
		boolean status = false;
		String notificationId = (String) inputParams.get("notificationId");
		if (canBeDeleted(dcRequest, inputParams, result)) {
			inputParams.put(DBPUtilitiesConstants.UN_ID, notificationId);
			status = true;
		}
		return status;
	}

	private boolean canBeDeleted(DataControllerRequest dcRequest, Map inputParams, Result result)
			throws HttpCallException {
		boolean status = false;
		String userId = HelperMethods.getUserIdFromSession(dcRequest);
		String legalEntityId = LegalEntityUtil.getLegalEntityIdFromSessionOrCache(dcRequest);

		if (StringUtils.isBlank(legalEntityId)) {
			HelperMethods.setValidationMsg("Legal Entity Id cannot be empty!", dcRequest, result);
			inputParams.put("isLEPresent", "false");
			return false;
		}
		String filter = "id" + DBPUtilitiesConstants.EQUAL + inputParams.get("notificationId")
				+ DBPUtilitiesConstants.AND + "companyLegalUnit" + DBPUtilitiesConstants.EQUAL + legalEntityId;
		Result rs = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
				URLConstants.USER_NOTIFICATION_GET);

		if (HelperMethods.hasRecords(rs)) {
			String resultString = ResultToJSON.convert(rs);
            JSONObject resultJSON = new JSONObject(resultString);
			if (resultJSON.has("usernotification") && resultJSON.getJSONArray("usernotification") != null
                    && resultJSON.getJSONArray("usernotification").length() > 0) {
				JSONObject userNotificationObject = resultJSON.getJSONArray("usernotification").optJSONObject(0);
				if (userNotificationObject.has("user_id")) {
                    if (userNotificationObject.optString("user_id").equals(userId)) {
						if ("0".equalsIgnoreCase(HelperMethods.getFieldValue(rs, "isRead"))) {
							HelperMethods.setValidationMsg("Unread notifications can't be deleted", dcRequest, result);
						}
						status = true;
                    } else {
                        HelperMethods.setValidationMsg("Notification can't be deleted", dcRequest, result);
						inputParams.put("isSecurityException", "true");
                    }
                }
			}
		} else {
			HelperMethods.setValidationMsg("Notification doesn't exist", dcRequest, result);
			inputParams.put("isSecurityException", "true");
		}
		return status;
	}
}