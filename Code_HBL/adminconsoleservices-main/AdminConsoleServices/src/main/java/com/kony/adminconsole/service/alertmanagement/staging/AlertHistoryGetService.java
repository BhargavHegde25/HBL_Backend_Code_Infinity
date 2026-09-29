package com.kony.adminconsole.service.alertmanagement.staging;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.handler.CustomerHandler;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

/**
 * Service to Fetch the Customer Alert History
 * 
 * <p>
 * This Service fetches a certain count of the Alert History Events. Parameter: ALERT_HISTORY_EVENTS_TO_BE_FETCHED
 * Updating this value requires the limit in the 'alert_history_proc' to be updated too. The 'limit' value in the stored
 * procedure should be (Number of Communication Channels) * ALERT_HISTORY_EVENTS_TO_BE_FETCHED
 * </p>
 * 
 * @author Aditya Mankal
 *
 */
public class AlertHistoryGetService implements JavaService2 {

    private static final String CUSTOMER_ID_PARAM = "CustomerId";
    private static final int ALERT_HISTORY_EVENTS_TO_BE_FETCHED = 200;

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) {

        try {

            Result processedResult = new Result();
            String customerId = requestInstance.getParameter(CUSTOMER_ID_PARAM);
            diagnostic.prepareDebug("Is Customer ID Null?" + StringUtils.isBlank(customerId)).log();

            // Validate Inputs
            if (StringUtils.isBlank(customerId)) {
                // Missing Alert Customer ID
                alert.prepareError("Missing Customer ID").log();
                ErrorCodeEnum.ERR_20688.setErrorCode(processedResult);
                return processedResult;
            }
            // Validate Customer Id by resolving username
            String customerUsername = CustomerHandler.getCustomerUsername(customerId, requestInstance);
            if (StringUtils.isBlank(customerUsername)) {
                // Unrecognized Customer. Customer Type Could not be resolved
                alert.prepareError("Unrecognized Customer").log();
                ErrorCodeEnum.ERR_20539.setErrorCode(processedResult);
                return processedResult;
            }

            Dataset customerAlertHistoryDataset = getCustomerAlertHistory(customerId, requestInstance);
            processedResult.addDataset(customerAlertHistoryDataset);
            return processedResult;

        } catch (ApplicationException e) {
            Result errorResult = new Result();
            alert.prepareError("Application Exception. Checked Involved Operations. Exception Trace:", e).log();
            e.getErrorCodeEnum().setErrorCode(errorResult);
            return errorResult;
        } catch (Exception e) {
            Result errorResult = new Result();
            diagnostic.prepareDebug("Runtime Exception.Exception Trace:", e).log();
            ErrorCodeEnum.ERR_20921.setErrorCode(errorResult);
            return errorResult;
        }

    }

    /**
     * Method to get the Alert History of a Customer
     * 
     * @param customerId
     * @param requestInstance
     * @return Result Dataset
     * @throws ApplicationException
     */
    private Dataset getCustomerAlertHistory(String customerId, DataControllerRequest requestInstance)
            throws ApplicationException {
        Dataset customerAlertHistoryDataset = new Dataset();
        customerAlertHistoryDataset.setId("alertHistory");

        if (requestInstance == null) {
            alert.prepareError("Data Controller Request Instance is NULL. Throwing Application Exception.").log();
            throw new ApplicationException(ErrorCodeEnum.ERR_20909);
        }
        if (StringUtils.isBlank(customerId)) {
            alert.prepareError("Missing Mandatory Input : Customer Id. Returning Empty Record").log();
            return customerAlertHistoryDataset;
        }

        // Construct Input Map
        Map<String, String> inputMap = new HashMap<>();
        inputMap.put("_customerId", customerId);

        // Fetch Alert History Records
        String operationResponse =
                Executor.invokeService(ServiceURLEnum.ALERT_HISTORY_PROC_SERVICE, inputMap, null, requestInstance);
        JSONObject operationResponseJSON = CommonUtilities.getStringAsJSONObject(operationResponse);
        if (operationResponseJSON == null || !operationResponseJSON.has(FabricConstants.OPSTATUS)
                || operationResponseJSON.getInt(FabricConstants.OPSTATUS) != 0
                || !operationResponseJSON.has("records")) {
            alert.prepareError("Failed CRUD Operation").log();
            throw new ApplicationException(ErrorCodeEnum.ERR_20909);
        }
        diagnostic.prepareDebug("Successful CRUD Operation").log();
        JSONArray alertHistoryArray = operationResponseJSON.optJSONArray("records");

        JSONObject currJSON = null;
        String currEventId = StringUtils.EMPTY;
        String currChannelId = StringUtils.EMPTY;

        // Construct Result Dataset
        if (alertHistoryArray != null && alertHistoryArray.length() > 0) {

            Map<String, Record> alertsMap = new LinkedHashMap<>();

            for (Object currObject : alertHistoryArray) {
                if (currObject instanceof JSONObject) {
                    currJSON = (JSONObject) currObject;

                    // Record per Reference Number. This record contains inner records. Each for a particular channel
                    currEventId = currJSON.optString("alerthistory_EventId");
                    if (!alertsMap.containsKey(currEventId)) {
                        Record currEventRecord = new Record();
                        alertsMap.put(currEventId, currEventRecord);
                    }
                    Record currEventRecord = alertsMap.get(currEventId);

                    currChannelId = currJSON.optString("alerthistory_ChannelId");
                    Record currAlertMessageRecord = new Record();
                    currAlertMessageRecord.setId(currChannelId);
                    for (String key : currJSON.keySet()) {
                        currAlertMessageRecord
                                .addParam(new Param(key, currJSON.optString(key), FabricConstants.STRING));
                    }

                    // Add current Alert Message Record to Alert Reference Record
                    currEventRecord.addRecord(currAlertMessageRecord);

                    // Condition TRUE when the data of required number of events has been added to the dataset
                    if (alertsMap.size() == ALERT_HISTORY_EVENTS_TO_BE_FETCHED) {
                        break;
                    }
                }
            }
            customerAlertHistoryDataset.addAllRecords(alertsMap.values());
        }

        diagnostic.prepareDebug("Returning Success Response").log();
        return customerAlertHistoryDataset;
    }

}
