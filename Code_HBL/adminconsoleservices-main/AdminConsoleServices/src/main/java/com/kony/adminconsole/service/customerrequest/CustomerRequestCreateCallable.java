package com.kony.adminconsole.service.customerrequest;


import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.JsonObject;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.commons.utils.ODataQueryConstants;
import com.kony.adminconsole.commons.utils.ThreadExecutor;
import com.kony.adminconsole.dto.CustomerRequestPayload;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.handler.AuditHandler;
import com.kony.adminconsole.handler.CustomerRequestAndMessagesHandler;
import com.kony.adminconsole.utilities.ActivityStatusEnum;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.EventEnum;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.ModuleNameEnum;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.kony.objectserviceutils.EventsDispatcher;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;

public class CustomerRequestCreateCallable implements Callable<Record>{

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
	
	private static final String OPERATION_SUCCESS_CODE = "0";
	
	private static final String OPERATION_FAILURE_CODE = "-1";
	
	private static final String DRAFT_MESSAGE_STATUS = "DRAFT";
	
	private static final String EVENT_TYPE = "SECURE_MESSAGE";
	
    private static final String STATUS_ID = "SID_EVENT_SUCCESS";
    
    private static final String EVENT_SUB_TYPE = "SECURE_MESSAGE_ALERT";

    private static final String APP_ID = "RETAIL_AND_BUSINESS_BANKING";
    
    private static final String PRODUCER = "RETAIL_AND_BUSINESS_BANKING";
	
	DataControllerRequest requestInstance;
	
	CustomerRequestPayload customerRequestPayload;
	
	String requestId;
	
	String customerId;
	
	String customerUsername;
	
	CustomerRequestCreateCallable(DataControllerRequest request, CustomerRequestPayload payload, String reqId,
			String custId, String custUsername){
		
		requestInstance = request;
		customerRequestPayload = payload;
		requestId = reqId;
		customerId = custId;
		customerUsername = custUsername;
	}
	
	@Override
	public Record call() throws Exception {
		
		Record record = new Record();
		boolean currentMessageSuccessState = true;
		boolean hasErrorOccured = false;
		CustomerRequestPayload customerRequestPayloadCloned=(CustomerRequestPayload)customerRequestPayload.clone();
		customerRequestPayloadCloned.setRequestId(requestId);
		customerRequestPayloadCloned.setCustomerId(customerId);
		customerRequestPayloadCloned.setCustomerUsername(customerUsername);
		
		Record processCustomerRequestRecord = processCustomerRequest(requestInstance, customerRequestPayloadCloned);
        boolean isProcessRequestSuccessful = checkForOperationSuccessState(processCustomerRequestRecord);
        
        if (isProcessRequestSuccessful == true) {
            diagnostic.prepareDebug("Processed Request for Customer with Id:" + customerRequestPayloadCloned.getCustomerId()).log();
            Record processRequestMessageRecord = processRequestMessage(requestInstance, customerRequestPayloadCloned);
            boolean isProcessRequestMessageSuccesful =
                    checkForOperationSuccessState(processRequestMessageRecord);
            if (isProcessRequestMessageSuccesful == true) {
                diagnostic.prepareDebug("Processed Request Message for Customer with Id:"
                        + customerRequestPayloadCloned.getCustomerId()).log();
            } else {
                alert.prepareError("Error in Processing Request Message for Customer with Id:"
                        + customerRequestPayloadCloned.getCustomerId()).log();
                hasErrorOccured = true;
                currentMessageSuccessState = false;
            }
        } else {
            alert.prepareError("Error in Processing Request. Customer with Id:" + customerRequestPayloadCloned.getCustomerId()).log();
            hasErrorOccured = true;
            currentMessageSuccessState = false;
        }
        record.addParam(new Param(customerRequestPayloadCloned.getCustomerUsername(), Boolean.toString(currentMessageSuccessState),
                FabricConstants.STRING));
        record.addParam(new Param("hasErrorOccured", Boolean.toString(hasErrorOccured),
                FabricConstants.STRING));
        
        return record;
	}
	
	private boolean checkForOperationSuccessState(Record operationRecord) {
        if (operationRecord.getParamByName("statusCode") != null) {
            if (!operationRecord.getParamByName("statusCode").getValue().equalsIgnoreCase("0")) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }
	
	private Record processCustomerRequest(DataControllerRequest requestInstance,
            CustomerRequestPayload customerRequestPayload) {

        Record record = new Record();
        record.addParam(new Param("requestId", customerRequestPayload.getRequestId(), FabricConstants.STRING));
        Param statusCodeParam = new Param("statusCode", OPERATION_SUCCESS_CODE, FabricConstants.INT);
        record.addParam(statusCodeParam);

        String requestId = customerRequestPayload.getRequestId();
        String requestSubject = customerRequestPayload.getRequestSubject();
        String requestCategoryId = customerRequestPayload.getRequestCategoryId();
        String customerId = customerRequestPayload.getCustomerId();
        String requestPriority = customerRequestPayload.getRequestPriority();
        String assignedCSRId = customerRequestPayload.getCsrId();
        String accountId = customerRequestPayload.getAccountId();
        String requestStatus = customerRequestPayload.getRequestStatus();
        String userId = customerRequestPayload.getUserDetailsBeanInstance().getId();

        StringBuffer errorMessageBuffer = new StringBuffer();
        errorMessageBuffer.append("ERROR:");

        Map<String, String> inputMap = new HashMap<String, String>();
        inputMap.put("id", requestId);
        diagnostic.prepareDebug("isCreateRequest:" + customerRequestPayload.isCreateRequest()).log();

        if (customerRequestPayload.isCreateRequest()) {
            if (StringUtils.isBlank(assignedCSRId)) {
                // Default behavior - Assigned to Current Logged in CSR
                customerRequestPayload.setCsrId(userId);
                assignedCSRId = userId;
            }
            inputMap.put("RequestCategory_id", requestCategoryId);
            inputMap.put("Customer_id", customerId);
            inputMap.put("Status_id", requestStatus);
            inputMap.put("Priority", requestPriority);
            inputMap.put("RequestSubject", requestSubject);
            if (customerRequestPayload.isRequestByAdmin()) {
                // Set AssignedTo only when the call is from an Admin
                inputMap.put("AssignedTo", assignedCSRId);
            }

            inputMap.put("Accountid", accountId);
            inputMap.put("createdts", CommonUtilities.getISOFormattedLocalTimestamp());
            inputMap.put("createdby", customerRequestPayload.getCurrentUsername());
        } else {
            // Update an Existing Request

            if (StringUtils.isNotBlank(requestSubject)) {
                inputMap.put("RequestSubject", requestSubject);
            }
            if (StringUtils.isNotBlank(customerId)) {
                inputMap.put("Customer_id", customerId);
            }
            if (StringUtils.isNotBlank(requestCategoryId)) {
                inputMap.put("RequestCategory_id", requestCategoryId);
            }
            if (StringUtils.isNotBlank(requestPriority)) {
                inputMap.put("Priority", requestPriority);
            }
            if (StringUtils.isNotBlank(assignedCSRId) && customerRequestPayload.isRequestByAdmin()) {
                inputMap.put("AssignedTo", assignedCSRId);
            }
            if (StringUtils.isNotBlank(requestStatus)) {
                inputMap.put("Status_id", requestStatus);
            }
            if (StringUtils.isNotBlank(accountId)) {
                inputMap.put("Accountid", accountId);
            }
            inputMap.put("modifiedby", customerRequestPayload.getCurrentUsername());
            if (StringUtils
                    .isNotBlank(CommonUtilities.decodeFromBase64(customerRequestPayload.getMessageDescription()))) {
                inputMap.put("lastmodifiedts", CommonUtilities.getISOFormattedLocalTimestamp());
                // Do not change the lastmodifiedts of a Request in case of an update call without a message description
            }
        }
        if (customerRequestPayload.isRequestByAdmin()) {
            inputMap.put("lastupdatedbycustomer", "0");
        } else {
            inputMap.put("lastupdatedbycustomer", "1");
        }

        String serviceResponse;
        EventEnum eventEnum = null;
        ActivityStatusEnum activityStatusEnum = null;
        if (customerRequestPayload.isCreateRequest()) {
            eventEnum = EventEnum.CREATE;
            serviceResponse =
                    Executor.invokeService(ServiceURLEnum.CUSTOMERREQUEST_CREATE, inputMap, null, requestInstance);
        } else {
            eventEnum = EventEnum.UPDATE;
            serviceResponse =
                    Executor.invokeService(ServiceURLEnum.CUSTOMERREQUEST_UPDATE, inputMap, null, requestInstance);
        }

        JSONObject serviceResponseJSON = CommonUtilities.getStringAsJSONObject(serviceResponse);
        if (serviceResponseJSON != null && serviceResponseJSON.has(FabricConstants.OPSTATUS)
                && serviceResponseJSON.optInt(FabricConstants.OPSTATUS) == 0) {
            diagnostic.prepareDebug("Process Customer Request Successful").log();
            activityStatusEnum = ActivityStatusEnum.SUCCESSFUL;

        } else {
            alert.prepareError("Manage Customer Request Failed").log();
            activityStatusEnum = ActivityStatusEnum.FAILED;
            record.addParam(new Param("errorMessage", serviceResponse, FabricConstants.STRING));
            statusCodeParam.setValue(OPERATION_FAILURE_CODE);
            if (customerRequestPayload.isCreateRequest()) {
                record.removeParamByName("requestId");
            }
        }

        AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.MESSAGES, eventEnum, activityStatusEnum,
                "Request subject: " + requestSubject);
        return record;
    }
	
	private Record processRequestMessage(DataControllerRequest requestInstance, CustomerRequestPayload customerRequest)
            throws ApplicationException {

        Record record = new Record();
        Map<String, String> inputMap = new HashMap<>();
        Param statusCodeParam = new Param("statusCode", OPERATION_SUCCESS_CODE, FabricConstants.INT);
        record.addParam(statusCodeParam);

        boolean isExistingMessage = false;
        String repliedBy, repliedByName, repliedById;
        String messageId = customerRequest.getMessageId();
        String customerRequestId = customerRequest.getRequestId();
        String messageDescription = customerRequest.getMessageDescription();
        String isPriorityMessage = customerRequest.getIsPriorityMessage();

        if (StringUtils.isBlank(messageDescription) && !customerRequest.isCreateRequest()) {
            return null; // Implies a case of Update Request where there is no Message Data.
        }

        if (customerRequest.isRequestByAdmin()) {
            repliedById = customerRequest.getUserDetailsBeanInstance().getId();
            repliedByName = customerRequest.getUserDetailsBeanInstance().getFirstName() + " "
                    + customerRequest.getUserDetailsBeanInstance().getMiddleName() + " "
                    + customerRequest.getUserDetailsBeanInstance().getLastName();
            repliedBy = "ADMIN|CSR";
        } else {
            repliedById = customerRequest.getCustomerId();
            repliedByName = customerRequest.getCustomerSalutation() + customerRequest.getCustomerFirstName() + " "
                    + customerRequest.getCustomerMiddleName() + " " + customerRequest.getCustomerLastName();
            repliedBy = "CUSTOMER";
        }
        diagnostic.prepareDebug("Replied By:" + repliedBy).log();

        if (StringUtils.isNotBlank(messageId)) {
            isExistingMessage = true;
            // Indicates an existing message in Draft State. It is to be updated with latest content and status
        } else {
            messageId = CommonUtilities.getNewId().toString(); // Create a new message
            customerRequest.setMessageId(messageId);
        }
        diagnostic.prepareDebug("isExistingMessage:" + isExistingMessage).log();
        record.addParam(new Param("messageId", messageId, FabricConstants.STRING));

        int replySequence = 0;
        // Default Message Reply Sequence.
        if (!customerRequest.isCreateRequest()) {
            // Fetch the Message Reply sequence of the latest message in case of an existing request
            inputMap.put(ODataQueryConstants.SELECT, "ReplySequence");
            inputMap.put(ODataQueryConstants.FILTER, "CustomerRequest_id eq '" + customerRequest.getRequestId() + "'");
            inputMap.put(ODataQueryConstants.ORDER_BY, "ReplySequence desc");
            inputMap.put(ODataQueryConstants.TOP, "1");
            String readRequestMessageResponse =
                    Executor.invokeService(ServiceURLEnum.REQUESTMESSAGE_READ, inputMap, null, requestInstance);
            JSONObject readRequestMessageResponseJSON =
                    CommonUtilities.getStringAsJSONObject(readRequestMessageResponse);
            if (readRequestMessageResponseJSON != null && readRequestMessageResponseJSON.has(FabricConstants.OPSTATUS)
                    && readRequestMessageResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
                    && readRequestMessageResponseJSON.has("requestmessage")) {
                JSONArray requestMessageRecords = readRequestMessageResponseJSON.getJSONArray("requestmessage");
                if (requestMessageRecords.length() >= 1) {
                    if (requestMessageRecords.getJSONObject(0).has("ReplySequence")) {
                        replySequence = requestMessageRecords.getJSONObject(0).optInt("ReplySequence");
                    }
                }
            }
        }
        replySequence++;

        inputMap.clear();
        inputMap.put("id", messageId);
        inputMap.put("CustomerRequest_id", customerRequestId);
        inputMap.put("MessageDescription", messageDescription);
        inputMap.put("rtx", "MessageDescription");// Used in Preprocessor to decode the encoded message description
        inputMap.put("RepliedBy", repliedBy);
        inputMap.put("RepliedBy_Name", repliedByName);
        inputMap.put("RepliedBy_id", repliedById);
        inputMap.put("ReplySequence", Integer.toString(replySequence));
        if (StringUtils.equalsIgnoreCase(customerRequest.getMessageStatus(), DRAFT_MESSAGE_STATUS)) {
            inputMap.put("IsRead", DRAFT_MESSAGE_STATUS);
        } else {
            inputMap.put("IsRead", customerRequest.isRequestByAdmin() ? "FALSE" : "TRUE");
        }
        if(StringUtils.isNotBlank(isPriorityMessage)) {
        	inputMap.put("isPriorityMessage", isPriorityMessage);
        }

        String operationResponse;
        if (isExistingMessage == false) {
            inputMap.put("createdby", customerRequest.getCurrentUsername());
            inputMap.put("createdts", CommonUtilities.getISOFormattedLocalTimestamp());
            inputMap.put("frominternaluser",customerRequest.isRequestByAdmin()?"1":"0");
            operationResponse =
                    Executor.invokeService(ServiceURLEnum.REQUESTMESSAGE_CREATE, inputMap, null, requestInstance);
        } else {
            inputMap.put("modifiedby", customerRequest.getCurrentUsername());
            inputMap.put("lastmodifiedts", CommonUtilities.getISOFormattedLocalTimestamp());
            operationResponse =
                    Executor.invokeService(ServiceURLEnum.REQUESTMESSAGE_UPDATE, inputMap, null, requestInstance);
        }
        JSONObject operationResponseJSON = CommonUtilities.getStringAsJSONObject(operationResponse);
        if (operationResponseJSON != null && operationResponseJSON.has(FabricConstants.OPSTATUS)
                && operationResponseJSON.optInt(FabricConstants.OPSTATUS) == 0) {

            try {
                if (customerRequest.isRequestByAdmin()
                        && !StringUtils.equalsIgnoreCase(customerRequest.getMessageStatus(), DRAFT_MESSAGE_STATUS)) {
                    ThreadExecutor.execute(
                            () -> EventsDispatcher.dispatch(requestInstance, null, EVENT_TYPE, EVENT_SUB_TYPE, PRODUCER,
                                    STATUS_ID, "", customerRequest.getCustomerId(), APP_ID, new JsonObject()));
                }
            } catch (Exception e) {
                alert.prepareError("Exception occurred while calling alerts engine", e).log();
            }

            diagnostic.prepareDebug("Manage Request Message Successful").log();
            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.MESSAGES, EventEnum.UPDATE,
                    ActivityStatusEnum.SUCCESSFUL,
                    "Request message updation successful. Request subject:" + customerRequest.getRequestSubject());

            inputMap.clear();
            inputMap.put("id", customerRequestId);
            inputMap.put("lastupdatedbycustomer", customerRequest.isRequestByAdmin() ? "0" : "1");
            inputMap.put("modifiedby", customerRequest.getCurrentUsername());
            inputMap.put("lastmodifiedts", CommonUtilities.getISOFormattedLocalTimestamp());
            Executor.invokeService(ServiceURLEnum.CUSTOMERREQUEST_UPDATE, inputMap, null, requestInstance);

            // Process Message Attachments on Successful Creation of Request Message
            if (operationResponseJSON != null && operationResponseJSON.has("requestmessage")) {
                record.addRecord(processMessageAttachments(repliedById, customerRequest, requestInstance));
            }

        } else {
            alert.prepareError("Manage Request Message Failure").log();
            record.removeParamByName("messageId");
            record.addParam(new Param("errorMessage", operationResponse, FabricConstants.STRING));
            statusCodeParam.setValue(OPERATION_FAILURE_CODE);
            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.MESSAGES, EventEnum.UPDATE,
                    ActivityStatusEnum.FAILED,
                    "Request message updation failed. Request subject:" + customerRequest.getRequestSubject());
        }

        return record;
    }
	
	private Record processMessageAttachments(String loggedInUserId, CustomerRequestPayload customerRequest,
            DataControllerRequest requestInstance) throws ApplicationException {

        // Result meta
        Record record = new Record();
        record.setId("attachments");

        // Uploaded attachments
        List<String> mediaIds = customerRequest.getMediaIds();
        if (mediaIds != null && !mediaIds.isEmpty()) {
            // Verify access
            /*boolean hasAccessToMedia =
                    CustomerRequestAndMessagesHandler.hasAccessToMedia(mediaIds, loggedInUserId, requestInstance);
            if (hasAccessToMedia == false) {
                diagnostic.prepareDebug("Unathorized access to attach/discard media").log();
                throw new ApplicationException(ErrorCodeEnum.ERR_21027);
            }*/
            // Attach media
            Dataset uploadAttachments = processUploadedMessageAttachments_(mediaIds, customerRequest.getMessageId(),
                    customerRequest.getCurrentUsername(), requestInstance);
            if (uploadAttachments != null) {
                record.addDataset(uploadAttachments);
            }
        }

        // Discarded attachments
        List<String> discardedMediaIds = customerRequest.getDiscardedMediaIds();
        if (discardedMediaIds != null && !discardedMediaIds.isEmpty()) {
            // Verify access
            boolean hasAccessToMedia = CustomerRequestAndMessagesHandler.hasAccessToMedia(discardedMediaIds,
                    loggedInUserId, requestInstance);
            if (hasAccessToMedia == false) {
                diagnostic.prepareDebug("Unathorized access to attach/discard media").log();
                throw new ApplicationException(ErrorCodeEnum.ERR_21027);
            }
            // Delete media
            /*Param discardAttachmentsParam =
                    processDiscardedMessageAttachments_(discardedMediaIds, loggedInUserId, requestInstance);
            if (discardAttachmentsParam != null) {
                record.addParam(discardAttachmentsParam);
            }*/
        }

        return record;
    }
	
	private Dataset processUploadedMessageAttachments_(List<String> mediaIds, String messageId,
            String loggedInUserUsername, DataControllerRequest requestInstance) throws ApplicationException {

        // Result members
        Dataset uploadAttachmentsDataset = new Dataset();
        uploadAttachmentsDataset.setId("uploadAttachments");

        String serviceResponse;
        JSONObject serviceResponseJSON;
        Map<String, String> inputMap = new HashMap<>();

        // Get already linked attachments
        Set<String> linkedMediaIds = getMessageAttachments_(messageId, requestInstance);

        // Link attachments to message
        for (String mediaId : mediaIds) {
            if (linkedMediaIds.contains(mediaId)) {
                diagnostic.prepareDebug("Skipping media with Id:" + mediaId + ". Already linked").log();
                continue;
            }

            Record currAttachmentRecord = new Record();
            String messageAttachmentId = CommonUtilities.getNewId().toString();
            inputMap.put("id", messageAttachmentId);
            inputMap.put("RequestMessage_id", messageId);
            // Attachment Type-Id is not referred. Hard-coding it to comply with the foreign key checks
            // Fetching the actual value from the 'media' table would add a performance bottlenecks
            inputMap.put("AttachmentType_id", "ATTACH_TYPE_FILE");
            inputMap.put("Media_id", mediaId);
            inputMap.put("createdby", loggedInUserUsername);
            inputMap.put("createdts", CommonUtilities.getISOFormattedLocalTimestamp());
            serviceResponse =
                    Executor.invokeService(ServiceURLEnum.MESSAGEATTACHMENT_CREATE, inputMap, null, requestInstance);
            serviceResponseJSON = CommonUtilities.getStringAsJSONObject(serviceResponse);
            if (serviceResponseJSON == null || !serviceResponseJSON.has(FabricConstants.OPSTATUS)
                    || serviceResponseJSON.getInt(FabricConstants.OPSTATUS) != 0) {
                alert.prepareError("Failed to link attachment to message. Service response:" + serviceResponse).log();
                throw new ApplicationException(ErrorCodeEnum.ERR_20119);
            }
            uploadAttachmentsDataset.addRecord(currAttachmentRecord);
            currAttachmentRecord.addParam(new Param("attachmentId", messageAttachmentId, FabricConstants.STRING));
            currAttachmentRecord.addParam(new Param("mediaId", mediaId, FabricConstants.STRING));
        }

        return uploadAttachmentsDataset;
    }
	
	private Set<String> getMessageAttachments_(String messageId, DataControllerRequest requestInstance) {

        Set<String> mediaIds = new HashSet<>();
        if (requestInstance == null || StringUtils.isBlank(messageId)) {
            return mediaIds;
        }

        // Fetch Media Ids
        Map<String, String> inputMap = new HashMap<>();
        inputMap.put(ODataQueryConstants.FILTER, "RequestMessage_id eq '" + messageId + "'");
        inputMap.put(ODataQueryConstants.SELECT, "Media_id");
        String serviceResponse =
                Executor.invokeService(ServiceURLEnum.MESSAGEATTACHMENT_READ, inputMap, null, requestInstance);
        JSONObject serviceResponseJSON = CommonUtilities.getStringAsJSONObject(serviceResponse);
        if (serviceResponseJSON == null || !serviceResponseJSON.has(FabricConstants.OPSTATUS)
                || serviceResponseJSON.getInt(FabricConstants.OPSTATUS) != 0
                || !serviceResponseJSON.has("messageattachment")) {
            alert.prepareError("Failed CRUD Operation. Read Message Attachment. Response:" + serviceResponse).log();
            return mediaIds;
        }

        JSONObject currJSON = null;
        JSONArray messageAttachmentIdsArray = serviceResponseJSON.optJSONArray("messageattachment");
        if (messageAttachmentIdsArray != null && messageAttachmentIdsArray.length() > 0) {
            for (Object currObject : messageAttachmentIdsArray) {
                if (currObject instanceof JSONObject) {
                    currJSON = (JSONObject) currObject;
                    if (currJSON.has("Media_id")) {
                        mediaIds.add(currJSON.optString("Media_id"));
                    }
                }
            }
        }

        return mediaIds;
    }

}
