package com.kony.adminconsole.service.customerrequest;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.handler.CustomerRequestAndMessagesHandler;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

/**
 * Service to retrieve the Customer messages for a request
 *
 * @author Alahari Prudhvi Akhil, Aditya Mankal
 */

public class RequestMessageGetService implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

    private static final String INPUT_REQUEST_ID = "requestid";
    private static final String INPUT_USERNAME = "username";
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

    @Override
    public Object invoke(String methodId, Object[] arg1, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {

        Result processedResult = new Result();
        try {

            // Read Inputs
            String requestId = requestInstance.getParameter(INPUT_REQUEST_ID);
            String username = requestInstance.getParameter(INPUT_USERNAME);

            // Validate Inputs
            if (StringUtils.isBlank(username)) {
                // Missing mandatory input
                throw new ApplicationException(ErrorCodeEnum.ERR_20132);
            }
            if (StringUtils.isBlank(requestId)) {
                // Missing mandatory input
                throw new ApplicationException(ErrorCodeEnum.ERR_20133);
            }

            // Verify user access
            boolean hasAccess = CustomerRequestAndMessagesHandler.verifyIfUserHasAccessToRequest(requestId, username,
                    requestInstance);
            if (hasAccess == false) {
                throw new ApplicationException(ErrorCodeEnum.ERR_21027);
            }

            // Fetch Messages
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put("_customerRequestID", requestId);
            paramMap.put("_fetchDraftMessages", "0");
            String operationResponse = Executor.invokeService(ServiceURLEnum.REQUESTMESSAGES_PROC, paramMap, null,
                    requestInstance);
            JSONObject operationResponseJSON = CommonUtilities.getStringAsJSONObject(operationResponse);
            if (operationResponseJSON == null || !operationResponseJSON.has(FabricConstants.OPSTATUS)
                    && operationResponseJSON.getInt(FabricConstants.OPSTATUS) != 0
                    && !operationResponseJSON.has("records")) {
                // Failed CRUD Operation
                throw new ApplicationException(ErrorCodeEnum.ERR_20125);
            }

            // Construct Messages Map
            Dataset messagesDataset = new Dataset();
            messagesDataset.setId("messages");
            processedResult.addDataset(messagesDataset);
            LinkedHashMap<String, JSONObject> messagesMap = new LinkedHashMap<>();
            JSONArray readResponseJSONArray = operationResponseJSON.getJSONArray("records");
            for (Object requestMessageObject : readResponseJSONArray) {

                JSONObject attachment = null;
                JSONObject requestMessageJSONObject = (JSONObject) requestMessageObject;

                if (requestMessageJSONObject.has("Media_id")) {
                    attachment = new JSONObject();
                    attachment.put("media_Id", requestMessageJSONObject.getString("Media_id"));
                    String media_name=requestMessageJSONObject.has("Media_Name")?requestMessageJSONObject.getString("Media_Name"):"";
                    attachment.put("Name", media_name);
                    String media_type=requestMessageJSONObject.has("Media_Type")?requestMessageJSONObject.getString("Media_Type"):"";
                    attachment.put("type", media_type);
                    String media_size=requestMessageJSONObject.has("Media_Size")?requestMessageJSONObject.getString("Media_Size"):"";
                    attachment.put("Size",media_size);
                }
                if (!messagesMap.containsKey(requestMessageJSONObject.getString("Message_id"))) {
                    // Message does not exist in the map
                    requestMessageJSONObject.put("id", requestMessageJSONObject.getString("Message_id"));

                    // Parse the database time stamp to ISO format
                    requestMessageJSONObject.put("createdts", CommonUtilities.convertTimetoISO8601Format(CommonUtilities
                            .parseDateStringToDate(requestMessageJSONObject.getString("createdts"), DATE_FORMAT)));
                    requestMessageJSONObject.put("synctimestamp",
                            CommonUtilities.convertTimetoISO8601Format(CommonUtilities.parseDateStringToDate(
                                    requestMessageJSONObject.getString("synctimestamp"), DATE_FORMAT)));
                    requestMessageJSONObject.put("lastmodifiedts",
                            CommonUtilities.convertTimetoISO8601Format(CommonUtilities.parseDateStringToDate(
                                    requestMessageJSONObject.getString("lastmodifiedts"), DATE_FORMAT)));
                    // Add attachments array if exists
                    JSONArray attachments = new JSONArray();
                    if (attachment != null) {
                        attachments.put(attachment);
                        if(requestMessageJSONObject.has("Media_Type")){
                            requestMessageJSONObject.remove("Media_Type");
                        }
                        if(requestMessageJSONObject.has("Media_id")){
                            requestMessageJSONObject.remove("Media_id");
                        }
                        if(requestMessageJSONObject.has("Media_Name")){
                            requestMessageJSONObject.remove("Media_Name");
                        }
                        if(requestMessageJSONObject.has("Media_Size")){
                            requestMessageJSONObject.remove("Media_Size");
                        }
                        if(requestMessageJSONObject.has("Messageattachment_id")){
                            requestMessageJSONObject.remove("Messageattachment_id");
                        }
                        requestMessageJSONObject.put("attachments", attachments);
                    }
                    messagesMap.put(requestMessageJSONObject.getString("Message_id"), requestMessageJSONObject);
                } else {
                    // Record already exists in map
                    JSONObject message = (JSONObject) messagesMap.get(requestMessageJSONObject.getString("Message_id"));
                    if (attachment != null)
                        message.getJSONArray("attachments").put(attachment);
                }
            }

            // Construct Result Dataset
            messagesMap.forEach((mapKey, messageJSONObject) -> {
                messagesDataset.addRecord(CommonUtilities.constructRecordFromJSONObject(messageJSONObject));
            });

            // Return Success Response
            processedResult.addParam(new Param("Status", "Success", FabricConstants.STRING));

        } catch (ApplicationException e) {
            // Application Exception
            Result errorResult = new Result();
            e.getErrorCodeEnum().setErrorCode(errorResult);
            return errorResult;
        } catch (Exception excp) {
            // Exception
            Result errorResult = new Result();
            ErrorCodeEnum.ERR_20125.setErrorCode(errorResult);
            alert.prepareError("Error occurred: ", excp).log();
            errorResult.addParam(new Param("exceptionInfo", excp.getMessage(), FabricConstants.STRING));
            return errorResult;
        }
        return processedResult;
    }

}