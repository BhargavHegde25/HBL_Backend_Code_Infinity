package com.kony.contentutilities.customerfeedback;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.constants.DBPConstants;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.metrics.DBPMetricData;
import com.kony.dbputilities.metrics.DBPMetricsProcessorHelper;
import com.kony.dbputilities.util.CommonUtils;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.MWConstants;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.exceptions.MetricsException;

public class CreateCustomerFeedback implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        String userName = null;
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        santizeInputStrings(inputParams);
        if (preProcess(inputParams, dcRequest, result)) {
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.FEEDBACK_CREATE);
        }
        if (HelperMethods.hasRecords(result)) {
            String userId = HelperMethods.getFieldValue(result, "user_id");
            String filter = "id " + DBPUtilitiesConstants.EQUAL + userId;
            Result customer = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                    URLConstants.CUSTOMERVERIFY_GET);
            if (HelperMethods.hasRecords(customer)) {
                userName = HelperMethods.getFieldValue(customer, "UserName");
                result.addParam(new Param("UserName", userName, "String"));
            }
        }
        pushCustomMetricData(dcRequest, inputParams, userName);
        return result;
    }

    private void pushCustomMetricData(DataControllerRequest dcRequest, Map<String, String> inputParams,
            String userName) {
        DBPMetricsProcessorHelper metricsHelper = new DBPMetricsProcessorHelper();
        List<DBPMetricData> custMetrics = new ArrayList<>();
        custMetrics.add(new DBPMetricData("count", "1", DBPMetricData.LONG));
        custMetrics.add(new DBPMetricData("userName", userName, DBPMetricData.STRING));
        if (StringUtils.isNotBlank(inputParams.get("rating"))) {
            custMetrics.add(new DBPMetricData("rating", inputParams.get("rating"), DBPMetricData.DOUBLE));
        }
        try {
            metricsHelper.addCustomMetrics(dcRequest, custMetrics, false);
        } catch (MetricsException e) {
            alert.prepareError(e.getMessage()).log();
        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
        }

    }

    private boolean preProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result) {
        String errorCode = null;
        String errorMessage = null;
        if (inputParams.containsKey("user_id") && StringUtils.isNotBlank(inputParams.get("user_id"))) {
            return true;
        } else if (inputParams.containsKey("feedbackID") && StringUtils.isNotBlank(inputParams.get("feedbackID"))) {
            String feedbackId = inputParams.get("feedbackID");
            String customerId = null;
            String deviceId = null;
            String reportingParams = dcRequest.getHeader("X-Kony-ReportingParams");
            if (StringUtils.isNotBlank(reportingParams)) {
                JSONObject reportingParamsJson = null;
                try {
                    reportingParamsJson = new JSONObject(
                            URLDecoder.decode(reportingParams, StandardCharsets.UTF_8.name()));
                } catch (JSONException e) {
                    alert.prepareError(e.getMessage()).log();
                } catch (UnsupportedEncodingException e) {
                    alert.prepareError(e.getMessage()).log();
                }
                if (null != reportingParamsJson) {
                    deviceId = reportingParamsJson.optString("did");
                } else {
                    errorCode = "10167";
                    errorMessage = "Service is down, please try again after sometime.";
                    setErrorCode(result, errorCode, errorMessage);
                    return false;
                }
            }

            String filterQuery = "feedbackID" + DBPUtilitiesConstants.EQUAL + feedbackId + DBPUtilitiesConstants.AND
                    + "status" + DBPUtilitiesConstants.EQUAL + "false" + DBPUtilitiesConstants.AND + "deviceID"
                    + DBPUtilitiesConstants.EQUAL + deviceId;
            Result feedbackStatus = null;
            try {
                feedbackStatus = HelperMethods.callGetApi(dcRequest, filterQuery, HelperMethods.getHeaders(dcRequest),
                        URLConstants.FEEDBACKSTATUS_GET);
            } catch (HttpCallException e1) {
                alert.prepareError(e1.getMessage()).log();
            }
            if (null == feedbackStatus || !HelperMethods.hasRecords(feedbackStatus)) {
                errorCode = "10166";
                errorMessage = "Please provide valid Details.";
                setErrorCode(result, errorCode, errorMessage);
                return false;
            }
            Map<String, String> updateParams = new HashMap<>();
            updateParams.put("id", HelperMethods.getFieldValue(feedbackStatus, "id"));
            updateParams.put("status", "true");
            try {
                HelperMethods.callApi(dcRequest, updateParams, HelperMethods.getHeaders(dcRequest),
                        URLConstants.FEEDBACKSTATUS_UPDATE);
            } catch (HttpCallException e1) {
                alert.prepareError(e1.getMessage()).log();
            }
            customerId = HelperMethods.getFieldValue(feedbackStatus, "customerID");

            if (StringUtils.isBlank(customerId)) {
                errorCode = "10167";
                errorMessage = "Service is down, please try again after sometime.";
                setErrorCode(result, errorCode, errorMessage);
            }

            inputParams.put("user_id", customerId);
            return true;
        } else {
            if (StringUtils.isBlank(HelperMethods.getUserIdFromSession(dcRequest))) {
                errorCode = "10166";
                errorMessage = "Please provide valid Details.";
                setErrorCode(result, errorCode, errorMessage);
                return false;
            }
            inputParams.put("user_id", HelperMethods.getUserIdFromSession(dcRequest));
            return true;
        }
    }

    private void setErrorCode(Result result, String errorCode, String errorMessage) {
        result.addParam(new Param(DBPConstants.DBP_ERROR_CODE_KEY, errorCode, MWConstants.INT));
        result.addParam(new Param(DBPConstants.DBP_ERROR_MESSAGE_KEY, errorMessage, MWConstants.STRING));
        result.addParam(new Param(DBPConstants.FABRIC_OPSTATUS_KEY, "0", MWConstants.INT));
        result.addParam(new Param(DBPConstants.FABRIC_HTTP_STATUS_CODE_KEY, "0", MWConstants.INT));

    }
    
    private void santizeInputStrings(Map<String, String> inputMap) {
    	if(inputMap.containsKey("description")&&inputMap.get("description")!=null) {
    		String desc = CommonUtils.getXSSSanitizedString(inputMap.get("description"));
    		inputMap.put("description", desc);
    	}
		if(inputMap.containsKey("featureRequest")&&inputMap.get("featureRequest")!=null) {
			String featureRequest = CommonUtils.getXSSSanitizedString(inputMap.get("featureRequest"));
			inputMap.put("featureRequest", featureRequest);
		}
    }
}