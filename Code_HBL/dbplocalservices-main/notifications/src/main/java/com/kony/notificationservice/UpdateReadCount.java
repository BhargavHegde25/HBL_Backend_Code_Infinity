package com.kony.notificationservice;

import java.util.Map;

import org.json.JSONObject;

import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.LegalEntityUtil;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

public class UpdateReadCount implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @SuppressWarnings("rawtypes")
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
                         DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        try {
            Map inputParams = HelperMethods.getInputParamMap(inputArray);
            if (preProcess(inputParams, dcRequest, result)) {
                result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                        URLConstants.USER_NOTIFICATION_UPDATE);
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
        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
            result.addOpstatusParam(-1);
            result.addHttpStatusCodeParam(400);
        }
        return result;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result)
            throws HttpCallException, Exception {
        String id = (String) inputParams.get("userNotificationId");
        String userid = HelperMethods.getCustomerIdFromSession(dcRequest);
		String legalEntityId = LegalEntityUtil.getLegalEntityIdFromSessionOrCache(dcRequest);

		if (StringUtils.isBlank(legalEntityId)) {
			HelperMethods.setValidationMsg("Legal Entity Id cannot be empty!", dcRequest, result);
			inputParams.put("isLEPresent", "false");
			return false;
		}
		String filterQuery = "id" + DBPUtilitiesConstants.EQUAL + id + DBPUtilitiesConstants.AND + "companyLegalUnit"
				+ DBPUtilitiesConstants.EQUAL + legalEntityId;
        Result result1 = HelperMethods.callGetApi(dcRequest, filterQuery, HelperMethods.getHeaders(dcRequest),
                URLConstants.USER_NOTIFICATION_GET);

        if (!HelperMethods.hasRecords(result1)) {
            return false;
        } else {
            String resultString = ResultToJSON.convert(result1);
            JSONObject resultJSON = new JSONObject(resultString);
            if (resultJSON.has("usernotification") && resultJSON.getJSONArray("usernotification") != null
                    && resultJSON.getJSONArray("usernotification").length() > 0) {
                JSONObject userNotificationObject = resultJSON.getJSONArray("usernotification").optJSONObject(0);
                if (userNotificationObject.has("user_id")) {
                    if (userNotificationObject.optString("user_id").equals(userid)) {
                        inputParams.put(DBPUtilitiesConstants.FILTER, filterQuery);
                        inputParams.put("isRead", "1");
                        inputParams.put("id", id);
                        return true;
                    } else {
                        inputParams.put("isSecurityException", "true");
                    }
                }
            }
            return false;
        }
    }
}
