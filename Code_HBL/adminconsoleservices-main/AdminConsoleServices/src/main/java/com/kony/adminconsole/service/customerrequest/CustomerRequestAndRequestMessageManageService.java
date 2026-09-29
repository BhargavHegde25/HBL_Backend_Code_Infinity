package com.kony.adminconsole.service.customerrequest;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileUploadBase.FileSizeLimitExceededException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.google.gson.JsonObject;
import com.kony.adminconsole.commons.exception.InvalidFileNameException;
import com.kony.adminconsole.commons.handler.MultipartPayloadHandler;
import com.kony.adminconsole.commons.handler.MultipartPayloadHandler.FormItem;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.commons.utils.ODataQueryConstants;
import com.kony.adminconsole.commons.utils.ThreadExecutor;
import com.kony.adminconsole.core.security.LoggedInUserHandler;
import com.kony.adminconsole.core.config.EnvironmentConfiguration;
import com.kony.adminconsole.core.security.UserDetailsBean;
import com.kony.adminconsole.dto.CustomerRequestPayload;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.handler.AuditHandler;
import com.kony.adminconsole.handler.CustomerHandler;
import com.kony.adminconsole.handler.CustomerRequestAndMessagesHandler;
import com.kony.adminconsole.service.authmodule.APICustomIdentityService;
import com.kony.adminconsole.service.servicedefinition.businessdelegate.api.ServiceDefinitionBusinessDelegate;
import com.kony.adminconsole.service.servicedefinition.dto.ServiceDefinitionDTO;
import com.kony.adminconsole.utilities.ActivityStatusEnum;
import com.kony.adminconsole.utilities.EnvironmentParamRead;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.EventEnum;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.ModuleNameEnum;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.kony.objectserviceutils.EventsDispatcher;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

/**
 * <p>
 * Service to handle Customer Requests, Messages and the attachments
 * </p>
 * 
 * @author Aditya Mankal
 */
public class CustomerRequestAndRequestMessageManageService implements JavaService2 {

    private static final String OPERATION_SUCCESS_CODE = "0";
    private static final String OPERATION_FAILURE_CODE = "-1";

    private static final String DRAFT_MESSAGE_STATUS = "DRAFT";
    private static final String UPDATE_CUSTOMER_REQUEST_METHOD_ID = "updateCustomerRequest";

    private static final String EVENT_TYPE = "SECURE_MESSAGE";
    private static final String STATUS_ID = "SID_EVENT_SUCCESS";
    private static final String EVENT_SUB_TYPE = "SECURE_MESSAGE_ALERT";

    private static final String APP_ID = "RETAIL_AND_BUSINESS_BANKING";
    private static final String PRODUCER = "RETAIL_AND_BUSINESS_BANKING";
    private static final int CUST_READ_BATCH_SIZE = 5000;

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

    @Override
    public Object invoke(String methodId, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {
        try {

            Result result = new Result();

            // Bean containing the Request Information
            CustomerRequestPayload customerRequest = new CustomerRequestPayload();

            /**
             * Data From client is sent as MultiPart-Form-Data or Application/JSON.<br>
             * Below Code is to process the data and to add it to the requestInstance. <br>
             * THE OPERATIONS USING THIS SERVICE CODE MUST BE OF TYPE PASS-THROUGH ONLY IN ALL CASES
             */

            boolean isValidRequestInstance =
                    validateRequestInstanceAndInitialiseBean(requestInstance, result, customerRequest);

            if (isValidRequestInstance == false) {
                alert.prepareError("Invalid Request Payload. Failed to Initialise Request Bean").log();
                return result;
            }

            if(!CommonUtilities.validateCompleteRequestInputJson(requestInstance)) {
            	ErrorCodeEnum.ERR_20541.setErrorCode(result);
                alert.prepareError("Invalid Request Payload. Failed to Initialise Request Bean").log();
                return result;
            }
            // Read Input Headers
            String loggedInCustomerUsername = requestInstance.getHeader("username");

            /*
             * Initialize the bean with the data which remains same across customers even in case of message to a group
             * and/or list of customers
             */
            String csrId = StringUtils.trim(requestInstance.getParameter("assignedto"));
            customerRequest.setCsrId(csrId);

            String requestId = StringUtils.trim(requestInstance.getParameter("requestid"));
            customerRequest.setRequestId(requestId);

            String accountId = StringUtils.trim(requestInstance.getParameter("accountid"));
            customerRequest.setAccountId(accountId);

            String messageId = StringUtils.trim(requestInstance.getParameter("messageid"));
            customerRequest.setMessageId(messageId);

            String requestPriority = StringUtils.trim(requestInstance.getParameter("priority"));
            customerRequest.setRequestPriority(requestPriority);

            String requestStatus = StringUtils.trim(requestInstance.getParameter("requeststatus"));
            customerRequest.setRequestStatus(requestStatus);

            String messageStatus = StringUtils.trim(requestInstance.getParameter("messagestatus"));
            customerRequest.setMessageStatus(messageStatus);
            
            String requestSubject = getXSSSanitizedString(StringUtils.trim(requestInstance.getParameter("requestsubject")));
            customerRequest.setRequestSubject(requestSubject);

            String markAllAsReadFlag = StringUtils.trim(requestInstance.getParameter("markallasread"));
            String isDiscardRequest = StringUtils.trim(requestInstance.getParameter("isDiscardRequest"));

            String requestCategoryId = StringUtils.trim(requestInstance.getParameter("requestcategory_id"));
            customerRequest.setRequestCategoryId(requestCategoryId);

            String messageDescription = requestInstance.getParameter("messagedescription");
            if(StringUtils.isNotBlank(messageDescription)) {
            String decodedSanitizedMessageDescription = getXSSSanitizedString(messageDescriptionValidation(messageDescription));
            messageDescription = CommonUtilities.encodeToBase64(decodedSanitizedMessageDescription);
            customerRequest.setMessageDescription(messageDescription);
            alert.prepareError(messageDescription).log();
            }
            String softDeleteFlag = StringUtils.trim(requestInstance.getParameter("softdelete"));
            String hardDeleteFlag = StringUtils.trim(requestInstance.getParameter("harddelete"));

            String isPriorityMessage = StringUtils.trim(requestInstance.getParameter("isPriorityMessage"));
            customerRequest.setIsPriorityMessage(isPriorityMessage);
            
            String customerUsername = StringUtils.trim(requestInstance.getParameter("username"));
            customerRequest.setCurrentUsername(customerUsername);

            // Passed as a JSON array
            String mediaIds = StringUtils.trim(requestInstance.getParameter("mediaIds"));
            List<String> mediaIdsList = CommonUtilities.getStringifiedArrayAsList(mediaIds);
            customerRequest.setMediaIds(mediaIdsList);

            // Passed as a JSON array
            String discardedMediaIds = StringUtils.trim(requestInstance.getParameter("discardedMediaIds"));
            List<String> discardedMediaIdsList = CommonUtilities.getStringifiedArrayAsList(discardedMediaIds);
            customerRequest.setDiscardedMediaIds(discardedMediaIdsList);

            String createdby = StringUtils.trim(requestInstance.getParameter("createdby"));
            String modifiedBy = StringUtils.trim(requestInstance.getParameter("modifiedby"));
            String customerId = StringUtils.trim(requestInstance.getParameter("customer_id"));
            String isNewRequest = StringUtils.trim(requestInstance.getParameter("isNewRequest"));
            String recipientList = StringUtils.trim(requestInstance.getParameter("recipientList"));

            // Fetched User Details from Identity Scope
            UserDetailsBean userDetailsBeanInstance = LoggedInUserHandler.getUserDetails(requestInstance);
            customerRequest.setUserDetailsBeanInstance(userDetailsBeanInstance);

            boolean isCreateRequest = StringUtils.isBlank(customerRequest.getRequestId());
            customerRequest.setCreateRequest(isCreateRequest);
            diagnostic.prepareDebug("isCreateRequest:" + customerRequest.isCreateRequest()).log();

            // Check if the Request is from Customer360 (OR) OLB/MB
            if (StringUtils.equalsIgnoreCase(userDetailsBeanInstance.getUserId(),
                    APICustomIdentityService.API_USER_ID)) {

                // Request is from Client Application - OLB/MB
                diagnostic.prepareDebug("Request is from Client Application - OLB/MB").log();

                createdby = loggedInCustomerUsername;
                // Check the availability of current logged-in user from modifiedBy parameter
                if (StringUtils.isBlank(loggedInCustomerUsername) && StringUtils.isNotBlank(modifiedBy)) {
                    loggedInCustomerUsername = modifiedBy;
                }
                customerRequest.setRequestByAdmin(false);
                requestInstance.setAttribute("isServiceBeingAccessedByOLB", true);

                /* Perform Security Checks */
                if (StringUtils.isBlank(loggedInCustomerUsername)) {
                    // Missing mandatory input value - Customer Username
                    alert.prepareError("Username of the Logged In Customer is a mandatory input, and has not been provided.").log();
                    ErrorCodeEnum.ERR_20132.setErrorCode(result);
                    return result;
                }

                // Verify if the Customer username in payload and Logged-In Customer's username
                // are the same
                if (!StringUtils.equals(customerUsername, loggedInCustomerUsername)) {
                    // Customer username has been tampered in the input payload
                    alert.prepareError("Customer username has been tampered in the input payload").log();
                    ErrorCodeEnum.ERR_21027.setErrorCode(result);
                    return result;
                }

                if (!customerRequest.isCreateRequest()) {
                    // Indicates an update/reply of/to an existing request. Verify if the Customer
                    // is the owner of the request
                    String requestOwner = CustomerRequestAndMessagesHandler
                            .getCustomerUsernameFromRequestId(customerRequest.getRequestId(), requestInstance);
                    if (!StringUtils.equals(requestOwner, loggedInCustomerUsername)) {
                        alert.prepareError("Unauthorised Request. Logged In Customer is the not the owner of the request").log();
                        ErrorCodeEnum.ERR_21027.setErrorCode(result);
                        return result;
                    }
                }

            } else {
                // Request is from Customer 360
                diagnostic.prepareDebug("Request is from Customer 360").log();
                customerRequest.setRequestByAdmin(true);
                customerRequest.setCurrentUsername(userDetailsBeanInstance.getUserName());
            }

            diagnostic.prepareDebug("isAdminRequest:" + customerRequest.isRequestByAdmin()).log();

            boolean isDraftMessage = StringUtils.equalsIgnoreCase(messageStatus, DRAFT_MESSAGE_STATUS);
            customerRequest.setDraftMessage(isDraftMessage);
            diagnostic.prepareDebug("isDraftMessage:" + customerRequest.isDraftMessage()).log();

            List<String> invalidRecipientsList = new ArrayList<>();
            List<String> processedListOfRecipients = new ArrayList<>();
            List<String> listOfRecipients = CommonUtilities.getStringifiedArrayAsList(recipientList);

            if (listOfRecipients != null && listOfRecipients.size() > 0) {
                String currEntry = StringUtils.EMPTY;
//                String currGroupId = StringUtils.EMPTY;

                /*
                 * Initial Assumption: Assume the entries to be customer usernames and attempt to resolve Customer Ids
                 */

                // customerUsernameIdMap map contains a mapping between the CustomerUsername and the CustomerId
                Map<String, String> customerUsernameIdMap =
                        CustomerHandler.getCustomersIdList(requestInstance, listOfRecipients);

                // Removing the username of the resolved customer from the list of recipients
                listOfRecipients.removeAll(customerUsernameIdMap.keySet());

                // Adding the customerId of the resolved customers to the expanded list of recipients
                processedListOfRecipients.addAll(customerUsernameIdMap.values());

                /*
                 * Intermediate Assumption: Assume the unresolved entries in the listOfRecipients to be ids of
                 * servicedefinition and serviceType Resolve the customerId of the customers who are a part of the group
                 */
                Set<String> servicedefinitionList =new HashSet<String>();
                for (int indexVar = 0; indexVar < listOfRecipients.size(); indexVar++) {

                    // currEntry is a Group Name
                    currEntry = listOfRecipients.get(indexVar);

                    
                    Set<String> servicedefinitions = getServiceDefinitionId(requestInstance, currEntry);
                    if (servicedefinitions.size() == 0) {
                        // Invalid Entry. Non Existing group and Non Existing customer
                        invalidRecipientsList.add(currEntry);
                        continue;
                    }
                    servicedefinitionList.addAll(servicedefinitions);

                }
                
                if(servicedefinitionList.size() > 0) {
                	
                	processedListOfRecipients.addAll(CustomerHandler.getInfinityCustomers(requestInstance, servicedefinitionList));
                }
            }

            /*
             * Case where the customer username is passed as 'username' - Backward Compatibility with OLB/RB
             */
            if (StringUtils.isNotBlank(customerUsername)) {
                String currCustomerId = CustomerHandler.getCustomerId(customerUsername, requestInstance);
                if (StringUtils.isNotBlank(currCustomerId) && !processedListOfRecipients.contains(currCustomerId)) {
                    processedListOfRecipients.add(currCustomerId);
                }
            }

            // Removing the duplicate entries from processedListOfRecipients to avoid duplicate requests
            CommonUtilities.removeDuplicatesInList(processedListOfRecipients);

            // Left over entries are deemed to be invalid recipients
            if (invalidRecipientsList.size() > 0) {
                result.addParam(
                        new Param("invalidRecipientsList", invalidRecipientsList.toString(), FabricConstants.STRING));
            }
            if (processedListOfRecipients.size() == 1) {
                customerRequest.setCustomerId(processedListOfRecipients.get(0));
            }

            // Validate the Request Payload
            String errorMessage = CustomerRequestAndMessagesHandler
                    .validateCustomerRequestData(processedListOfRecipients, customerRequest);
            if (StringUtils.isNotBlank(errorMessage)) {
                errorMessage = StringUtils.trim(errorMessage);
                alert.prepareError("Invalid Customer Request Payload. Detailed Error Message:" + errorMessage).log();
                result.addParam(new Param("validationError", errorMessage, FabricConstants.STRING));
                ErrorCodeEnum.ERR_20131.setErrorCode(result);
                return result;
            }

            // Request to Mark All Messages As Read
            if (StringUtils.equalsIgnoreCase(markAllAsReadFlag, String.valueOf(true))
                    || StringUtils.equals(markAllAsReadFlag, "1")) {
                customerRequest.setCreateRequest(false);
                Record markAllAsReadRecord = CustomerRequestAndMessagesHandler
                        .markAllMessagesOfCustomerRequestAsRead(requestInstance, customerRequest, modifiedBy);
                checkForOperationSuccessAndPrepareServiceResult(markAllAsReadRecord, result);
                result.addRecord(markAllAsReadRecord);
                return result;
            }

            // Request to Soft Delete a Request
            if (StringUtils.isNotBlank(softDeleteFlag)) {
                customerRequest.setCreateRequest(false);
                Record softDeleteRequestRecord = CustomerRequestAndMessagesHandler
                        .softDeleteCustomerRequest(requestInstance, customerRequest, softDeleteFlag, modifiedBy);
                checkForOperationSuccessAndPrepareServiceResult(softDeleteRequestRecord, result);
                result.addRecord(softDeleteRequestRecord);
                return result;
            }

            // Request to Hard Delete a Request
            if (StringUtils.equalsIgnoreCase(hardDeleteFlag, String.valueOf(true))
                    || StringUtils.equals(hardDeleteFlag, "1")) {
                customerRequest.setCreateRequest(false);
                Record hardDeleteRequestRecord = CustomerRequestAndMessagesHandler
                        .hardDeleteCustomerRequest(requestInstance, customerRequest, hardDeleteFlag, modifiedBy);
                checkForOperationSuccessAndPrepareServiceResult(hardDeleteRequestRecord, result);
                result.addRecord(hardDeleteRequestRecord);
                return result;
            }

            // Check if the call is to discard a Request
            if (isDiscardRequest != null && isDiscardRequest.equalsIgnoreCase(String.valueOf(true))) {
                customerRequest.setCreateRequest(false);
                boolean isDiscardRequestSuccessful = true;
                if (isNewRequest != null && isNewRequest.equalsIgnoreCase(String.valueOf(true))) {
                    /*
                     * Discard call on a Request which hasn't been sent yet. Request is currently in Draft state.
                     * Discard all data related to Request
                     */
                    isDiscardRequestSuccessful = CustomerRequestAndMessagesHandler
                            .discardRequestAndMessages(requestInstance, customerRequest, false);
                } else {
                    /*
                     * Discard call on a new message of an Existing Request. Discard only the current Message and it's
                     * Attachments(if any)
                     */
                    isDiscardRequestSuccessful = CustomerRequestAndMessagesHandler.discardMessage(requestInstance,
                            customerRequest, messageId);
                }
                if (!isDiscardRequestSuccessful) {
                    ErrorCodeEnum.ERR_20120.setErrorCode(result);
                }
                
                result.addParam(new Param("status", "success", FabricConstants.STRING));
                return result;
            }

            if (processedListOfRecipients.size() == 1 || methodId.equalsIgnoreCase(UPDATE_CUSTOMER_REQUEST_METHOD_ID)) {
                // Implies a Create/Reply to a Single Recipient OR Update Request

                if (processedListOfRecipients.size() == 1) {
                    // True in case of a Create/Reply to a Single Recipient
                    diagnostic.prepareDebug("Create/Reply to a Single Recipient").log();
                    if (customerRequest.isCreateRequest()) {
                        // Implies creation of a new Customer Request
                        diagnostic.prepareDebug("Scenario: Create New Customer Request").log();
                        requestId = "REQ" + CommonUtilities.getNumericId();
                        diagnostic.prepareDebug("Generated Request Id:" + requestId).log();
                        customerRequest.setRequestId(requestId);
                    }
                    customerId = processedListOfRecipients.get(0);
                }

                if (StringUtils.isNotBlank(customerUsername) && StringUtils.isBlank(customerId)) {
                    // Input - CustomerUsername only. Resolve Customer Id
                    customerId = CustomerHandler.getCustomerId(customerUsername, requestInstance);
                } else if (StringUtils.isNotBlank(customerId) && StringUtils.isBlank(customerUsername)) {
                    // Input - CustomerId only. Resolve Customer username
                    customerUsername = CustomerHandler.getCustomerUsername(customerId, requestInstance);
                }

                // Relying on the parameter created-by in case of the Customer not being resolved
                // in above 2 cases
                if (StringUtils.isBlank(customerUsername) && StringUtils.isNotBlank(createdby)) {
                    customerUsername = createdby;
                    customerId = CustomerHandler.getCustomerId(createdby, requestInstance);
                }
                if (methodId.equalsIgnoreCase(UPDATE_CUSTOMER_REQUEST_METHOD_ID)
                        && StringUtils.isBlank(customerUsername)
                        && StringUtils.isNotBlank(customerRequest.getRequestId())) {
                    // Resolving the Customer username from the Request in case of Customer not
                    // being resolved in above 3 cases
                    customerUsername = CustomerRequestAndMessagesHandler
                            .getCustomerUsernameFromRequestId(customerRequest.getRequestId(), requestInstance);
                }

                if (customerRequest.isRequestByAdmin() == false) {
                    customerRequest.setCurrentUsername(customerUsername);
                }

                customerRequest.setCustomerId(customerId);
                customerRequest.setCustomerUsername(customerUsername);

                if (customerRequest.isRequestByAdmin() == true) {
                    if (discardedMediaIds != null && !discardedMediaIds.isEmpty()
                            && (StringUtils.equalsIgnoreCase(isDiscardRequest, String.valueOf(true))
                                    || StringUtils.equalsIgnoreCase(isDiscardRequest, "1"))) {
                        // Request is to discard an attachment
                        Param param = processDiscardedMessageAttachments_(mediaIdsList, customerRequest.getCsrId(),
                                requestInstance);
                        result.addParam(param);
                        return result;
                    }
                }

                // Fetch Customer Details and initialize Bean
                JSONObject customerDetailsJSON =
                        CustomerHandler.getCustomerNameDetails(customerUsername, requestInstance);
                if (customerDetailsJSON != null) {
                    customerRequest.setCustomerFirstName(customerDetailsJSON.optString("FirstName"));
                    customerRequest.setCustomerMiddleName(customerDetailsJSON.optString("MiddleName"));
                    customerRequest.setCustomerLastName(customerDetailsJSON.optString("LastName"));
                    customerRequest.setCustomerSalutation(customerDetailsJSON.optString("Salutation"));
                }
                
                String lastTwoDigits = "";
                String firstTwoDigits = "";
                if (customerUsername != null && customerUsername.length() > 2) {
                    lastTwoDigits = customerUsername.substring(customerUsername.length() - 2);
                    firstTwoDigits = customerUsername.substring(0, 2);
                }

                Record currCustomerRecord = new Record();
                currCustomerRecord
                        .setId(StringUtils.isBlank(customerUsername) ? "unresolvedCustomerUsername" : firstTwoDigits+"****"+lastTwoDigits);

                Record customerRequestRecord = processCustomerRequest(requestInstance, customerRequest);
                customerRequestRecord.setId("customerRequest");
                currCustomerRecord.addRecord(customerRequestRecord);

                boolean isProcessCustomerRequestSuccessful =
                        checkForOperationSuccessAndPrepareServiceResult(customerRequestRecord, result);
                diagnostic.prepareDebug("isProcessRequestSuccessful:" + isProcessCustomerRequestSuccessful).log();

                if (isProcessCustomerRequestSuccessful == true) {
                    Record processRequestMessageRecord = processRequestMessage(requestInstance, customerRequest);
                    if (processRequestMessageRecord != null) {
                        processRequestMessageRecord.setId("requestMessage");
                        currCustomerRecord.addRecord(processRequestMessageRecord);
                        checkForOperationSuccessAndPrepareServiceResult(processRequestMessageRecord, result);
                    }
                }
                result.addRecord(currCustomerRecord);
                return result;

            } else if (processedListOfRecipients.size() > 1) {

                // Implies a Create Request to a group and/or multiple customers
                diagnostic.prepareDebug("Create Request to a group and/or multiple customers").log();
                boolean hasErrorOccured = false;

                Record record = new Record();
                record.setId("operationRecord");
                result.addRecord(record);

                customerRequest.setCreateRequest(true);
                customerRequest.setCurrentUsername(userDetailsBeanInstance.getUserName());

                // customerUsernameMap is a map between customer Id's and Customer usernames
                // Map<String, String> customerUsernameMap =
                //         CustomerHandler.getCustomersUsernameList(requestInstance, processedListOfRecipients);
                Map<String, String> customerUsernameMap = new HashMap<>();

                String batchSizeFromEnvCnonfig = EnvironmentConfiguration.AC_CUST_READ_BATCH_SIZE.getValue(requestInstance);
                int batchSizeFromEnvCnonfigInt = 0;

                if(StringUtils.isBlank(batchSizeFromEnvCnonfig)) {
                    batchSizeFromEnvCnonfigInt = CUST_READ_BATCH_SIZE;
                }else {
                    batchSizeFromEnvCnonfigInt = Integer.valueOf(batchSizeFromEnvCnonfig);
                }

                for(int i=0; i<processedListOfRecipients.size(); i+=batchSizeFromEnvCnonfigInt) {
                    Map<String, String> customerUsernameMapPartial = new HashMap<>();
                    int start= i;
                    int end = Math.min(processedListOfRecipients.size()-1,i+batchSizeFromEnvCnonfigInt);
                    customerUsernameMapPartial = CustomerHandler.getCustomersUsernameList(requestInstance, processedListOfRecipients.subList(start, end+1));
                    customerUsernameMapPartial.forEach(
                            (key, value)
                                    -> customerUsernameMap.merge(key, value,
                                    (v1, v2)
                                            -> v1.equalsIgnoreCase(v2) ? v1 : v1 + ", " + v2));
                }

                //added thread count
                int threadCnt = EnvironmentParamRead.getMsgCreateThreadCount(requestInstance);
                List<Callable<Record>> listOfCallable = new ArrayList<>();
                int count = 1;
                
                for (int index = 0; index < processedListOfRecipients.size(); index++) {
                    
                    customerId = processedListOfRecipients.get(index);
                    customerUsername = customerUsernameMap.get(customerId);
                    if (StringUtils.isBlank(customerUsername)) {
                        continue;
                    }
                    requestId = "REQ" + CommonUtilities.getNumericId();
                    String lastTwoDigits = "";
                    String firstTwoDigits = "";
                    if (customerUsername != null && customerUsername.length() > 2) {
                        lastTwoDigits = customerUsername.substring(customerUsername.length() - 2);
                        firstTwoDigits = customerUsername.substring(0, 2);
                    }
                    
                    listOfCallable.add(new CustomerRequestCreateCallable(requestInstance, customerRequest,
                    		requestId, customerId, firstTwoDigits + "****" + lastTwoDigits));
                    
                    if(count<threadCnt && index < (processedListOfRecipients.size() -1 )) {
                    	count ++;
                    	customerRequest.setRequestId(null);
                        customerRequest.setCustomerId(null);
                        customerRequest.setCustomerUsername(null);
                        customerRequest.setMessageId(null);
                    	continue;
                    }
    				
    				List<Future<Record>> futures = ThreadExecutor.execute(listOfCallable);
    				
    				for (Future<Record> f : futures) {
    					Record processCustomerRequestRecord = f.get();
    					
    					if(!hasErrorOccured) {
    						hasErrorOccured = Boolean.parseBoolean(processCustomerRequestRecord.getParamValueByName("hasErrorOccured"));
    					}
    					processCustomerRequestRecord.removeParamByName("hasErrorOccured");
                        record.addAllParams(processCustomerRequestRecord.getAllParams());

    				}
    				
    				listOfCallable = new ArrayList<>();
    				count =1;
                    
                }

                if (hasErrorOccured) {
                    ErrorCodeEnum.ERR_20131.setErrorCode(result);
                }
            } else {
                // Implies an invalid group/customer. Or group without any customers
                alert.prepareError("Invalid group/customer. (OR) Group without any customers").log();
                Record record = new Record();
                record.setId("operationRecord");
                result.addParam(new Param("customerRequest",
                        "Invalid group/customer name (OR) Group may not have any customers assigned"));
                result.addRecord(record);
                ErrorCodeEnum.ERR_22221.setErrorCode(result);
            }
            return result;
        } catch (ApplicationException e) {
            Result errorResult = new Result();
            alert.prepareError("ApplicationException in CustomerRequestAndRequestMessageManageService", e).log();
            e.getErrorCodeEnum().setErrorCode(errorResult);
            return errorResult;
        } catch (Exception e) {
            Result errorResult = new Result();
            diagnostic.prepareDebug("Runtime Exception.Exception Trace:", e).log();
            ErrorCodeEnum.ERR_20001.setErrorCode(errorResult);
            return errorResult;
        }

    }
    
    
    /**
     * @param requestInstance
     * @param customerRequestPayload
     * @return
     */
    private Record processCustomerRequest(DataControllerRequest requestInstance,
            CustomerRequestPayload customerRequestPayload) {

        Record record = new Record();
        record.addParam(new Param("requestId", customerRequestPayload.getRequestId(), FabricConstants.STRING));
        Param statusCodeParam = new Param("statusCode", OPERATION_SUCCESS_CODE, FabricConstants.INT);
        record.addParam(statusCodeParam);

        String requestId = customerRequestPayload.getRequestId();
        String requestSubject = getXSSSanitizedString(customerRequestPayload.getRequestSubject());
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

    /**
     * @param requestInstance
     * @param customerRequest
     * @return
     * @throws ApplicationException
     */
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
        if (StringUtils.isBlank(CommonUtilities.decodeFromBase64(messageDescription)) && !customerRequest.isCreateRequest()) {
            return null; // Implies a case of Update Request where there is no Message Data.
        }

        if (customerRequest.isRequestByAdmin()) {
        	repliedById = StringUtils.isNotBlank(customerRequest.getUserDetailsBeanInstance().getId()) ? customerRequest.getUserDetailsBeanInstance().getId() : customerRequest.getUserDetailsBeanInstance().getUserId();
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
                    "Request message updation successful. Request subject:" + getXSSSanitizedString(customerRequest.getRequestSubject()));

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
                    "Request message updation failed. Request subject:" + getXSSSanitizedString(customerRequest.getRequestSubject()));
        }

        return record;
    }

    /**
     * @param requestInstance
     * @param customerRequest
     * @return
     * @throws ApplicationException
     */
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

    /**
     * @param mediaIds
     * @param messageId
     * @param loggedInUserUsername
     * @param requestInstance
     * @return
     * @throws ApplicationException
     */
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

    /**
     * @param mediaIds
     * @param loggedInUserId
     * @param requestInstance
     * @return
     * @throws ApplicationException
     */
    private Param processDiscardedMessageAttachments_(List<String> mediaIds, String loggedInUserId,
            DataControllerRequest requestInstance) throws ApplicationException {

        if (mediaIds == null || mediaIds.isEmpty()) {
            return null;
        }

        // Discard attachments
        CustomerRequestAndMessagesHandler.deleteMedia(mediaIds, loggedInUserId, requestInstance);
        // Result meta
        return new Param("discardAttachments", Integer.toString(mediaIds.size()), FabricConstants.INT);
    }

    /**
     * @param requestInstance
     * @param customerRequest
     * @return
     * @throws ApplicationException
     */
    @Deprecated
    public Record processUploadedMessageAttachments(DataControllerRequest requestInstance,
            CustomerRequestPayload customerRequest) throws ApplicationException {

        // Result members
        Record record = new Record();
        record.setId("upload");
        Dataset uploadAttachmentsDataset = new Dataset();
        uploadAttachmentsDataset.setId("uploadAttachments");
        record.addDataset(uploadAttachmentsDataset);

        // Local variables
        boolean hasAttachments = false;
        String messageAttachmentId, requestMessageId, attachmentTypeId, mediaId, serviceResponse;
        requestMessageId = customerRequest.getMessageId();
        Map<String, String> inputMap = new HashMap<>();
        JSONObject serviceResponseJSON;

        // Get Media Collection
        Map<String, String> mediaCollection = customerRequest.getMediaCollection();
        if (mediaCollection == null) {
            mediaCollection = new HashMap<>();
            customerRequest.setMediaCollection(mediaCollection);
        }

        // Existing attachments of message. Map of file name and it's media ID
        Map<String, String> messageAttachments = getMessageAttachments(requestMessageId, requestInstance);

        List<FormItem> formItems = customerRequest.getFormItems();
        if (formItems != null && !formItems.isEmpty()) {
            for (FormItem currFormItem : formItems) {

                if (currFormItem.isFile()) {
                    if (messageAttachments.containsKey(currFormItem.getFileName())) {
                        // Attachment has already been uploaded during a draft request
                        continue;
                    }

                    hasAttachments = true;
                    Record currAttachmentRecord = new Record();
                    currAttachmentRecord
                            .addParam(new Param("fileName", currFormItem.getFileName(), FabricConstants.STRING));

                    // Validate Attachment File Type
                    attachmentTypeId = CustomerRequestAndMessagesHandler
                            .validateFileTypeAndGetAttachmentTypeId(currFormItem.getFileExtension());
                    if (attachmentTypeId.equalsIgnoreCase("INVALID_FILE")) {
                        currAttachmentRecord.addParam(new Param("errorMessage",
                                "Invalid File Type. Allowed file types are .txt .doc .docx .pdf .png .jpeg .jpg",
                                FabricConstants.STRING));
                        continue;
                    }

                    if (currFormItem
                            .getFileSize() > CustomerRequestAndMessagesHandler.ATTACHMENT_MAX_FILE_SIZE_IN_BYTES) {
                        currAttachmentRecord.addParam(new Param("errorMessage",
                                "Invalid File Size.File Size cannot be greater than "
                                        + CustomerRequestAndMessagesHandler.ATTACHMENT_MAX_FILE_SIZE_IN_BYTES + "Bytes",
                                FabricConstants.STRING));
                        continue;
                    }

                    if (mediaCollection.containsKey(currFormItem.getFileName())) {
                        // Existing Attachment. Reuse the entry from Media Table
                        mediaId = mediaCollection.get(currFormItem.getFileName());
                        currAttachmentRecord.addParam(
                                new Param("isExistingAttachment", String.valueOf(true), FabricConstants.STRING));
                    } else {
                        // New Attachment - Upload File
                        mediaId = CommonUtilities.getNewId().toString();
                        inputMap.put("id", mediaId);
                        inputMap.put("Name", currFormItem.getFileName());
                        inputMap.put("Type", currFormItem.getFileContentType());
                        inputMap.put("Description", requestMessageId + " MEDIA: " + currFormItem.getFileName());
                        String downloadURL = "/" + CustomerRequestAndMessagesHandler.MEDIA_DOWNLOAD_OBJECT_SERVICE_URL
                                + mediaId + "&authToken=";
                        inputMap.put("Url", downloadURL);

                        // File content will be uploaded directly via client. Just creating an empty record
                        /**
                         * try { inputMap.put("Content", CommonUtilities.encodeFile(currFormItem.getFile())); } catch
                         * (Exception e) { throw new ApplicationException(ErrorCodeEnum.ERR_20542); }
                         */

                        inputMap.put("Size", Long.toString(currFormItem.getFileSize()));
                        inputMap.put("createdby", customerRequest.getCurrentUsername());
                        inputMap.put("createdts", CommonUtilities.getISOFormattedLocalTimestamp());
                        serviceResponse =
                                Executor.invokeService(ServiceURLEnum.MEDIA_CREATE, inputMap, null, requestInstance);
                        serviceResponseJSON = CommonUtilities.getStringAsJSONObject(serviceResponse);

                        currAttachmentRecord.addParam(
                                new Param("isExistingAttachment", String.valueOf(false), FabricConstants.STRING));
                        if (serviceResponseJSON == null || !serviceResponseJSON.has(FabricConstants.OPSTATUS)
                                && serviceResponseJSON.getInt(FabricConstants.OPSTATUS) != 0) {
                            // Failed file upload
                            continue;
                        }
                        mediaCollection.put(currFormItem.getFileName(), mediaId);
                    }

                    // Link uploaded attachment to message
                    inputMap.clear();
                    messageAttachmentId = CommonUtilities.getNewId().toString();
                    inputMap.put("id", messageAttachmentId);
                    inputMap.put("RequestMessage_id", requestMessageId);
                    inputMap.put("AttachmentType_id", attachmentTypeId);
                    inputMap.put("Media_id", mediaId);
                    inputMap.put("createdby", customerRequest.getCurrentUsername());
                    inputMap.put("createdts", CommonUtilities.getISOFormattedLocalTimestamp());
                    serviceResponse = Executor.invokeService(ServiceURLEnum.MESSAGEATTACHMENT_CREATE, inputMap, null,
                            requestInstance);
                    uploadAttachmentsDataset.addRecord(currAttachmentRecord);
                    currAttachmentRecord
                            .addParam(new Param("attachmentId", messageAttachmentId, FabricConstants.STRING));
                    currAttachmentRecord.addParam(new Param("mediaId", mediaId, FabricConstants.STRING));

                }

            }
        }

        return hasAttachments ? record : null;
    }

    /**
     * <p>
     * Returns set of media ids that are linked to a message
     * </p>
     * 
     * @param messageId
     * @param requestInstance
     * @return List<String>
     */
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

    /**
     * @param messageId
     * @param requestInstance
     * @return
     */
    @Deprecated
    private Map<String, String> getMessageAttachments(String messageId, DataControllerRequest requestInstance) {

        Map<String, String> messageAttachmentNames = new HashMap<>();
        if (requestInstance == null || StringUtils.isBlank(messageId)) {
            return messageAttachmentNames;
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
            return messageAttachmentNames;
        }
        JSONObject currJSON = null;
        List<String> mediaIds = new ArrayList<>();

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
        if (mediaIds.isEmpty()) {
            return messageAttachmentNames;
        }

        // Fetch File Names
        inputMap.clear();
        StringBuffer filterQueryBuffer = new StringBuffer();
        for (String mediaId : mediaIds) {
            filterQueryBuffer.append("id eq '" + mediaId + "' or ");
        }
        String filerQuery = filterQueryBuffer.toString();
        filerQuery = CommonUtilities.replaceLastOccuranceOfString(filerQuery, " or ", StringUtils.EMPTY);
        filerQuery = filerQuery.trim();
        inputMap.put(ODataQueryConstants.FILTER, filerQuery);
        inputMap.put(ODataQueryConstants.SELECT, "id,Name");
        serviceResponse = Executor.invokeService(ServiceURLEnum.MEDIA_READ, inputMap, null, requestInstance);
        serviceResponseJSON = CommonUtilities.getStringAsJSONObject(serviceResponse);
        if (serviceResponseJSON == null || !serviceResponseJSON.has(FabricConstants.OPSTATUS)
                || serviceResponseJSON.getInt(FabricConstants.OPSTATUS) != 0 || !serviceResponseJSON.has("media")) {
            alert.prepareError("Failed CRUD Operation. Read Media. Response:" + serviceResponse).log();
            return messageAttachmentNames;
        }
        JSONArray mediaIdsArray = serviceResponseJSON.optJSONArray("media");
        if (mediaIdsArray != null && mediaIdsArray.length() > 0) {
            String mediaId, attachmentName;
            for (Object currObject : mediaIdsArray) {
                if (currObject instanceof JSONObject) {
                    currJSON = (JSONObject) currObject;
                    if (currJSON.has("id") && currJSON.has("Name")) {
                        mediaId = currJSON.optString("id");
                        attachmentName = currJSON.optString("Name");
                        messageAttachmentNames.put(attachmentName, mediaId);
                    }
                }
            }
        }

        return messageAttachmentNames;
    }

    /**
     *
     * @param requestInstance
     * @param customerRequest
     * @return
     */
    @Deprecated
    public Record processDiscardedMessageAttachments(DataControllerRequest requestInstance,
            CustomerRequestPayload customerRequest) {

        List<String> discardedMediaIds = customerRequest.getDiscardedMediaIds();
        if (discardedMediaIds != null && !discardedMediaIds.isEmpty()) {

            Record record = new Record();
            record.setId("discardAttachments");

            JSONArray messageAttachmentsJSONArray;
            String currMessageAttachmentId, response;
            JSONObject responseJSON, currMessageAttachmentJSONObject;
            Map<String, String> inputMap = new HashMap<>();

            String requestMessageId = customerRequest.getMessageId();

            for (String mediaId : discardedMediaIds) {

                inputMap.put(ODataQueryConstants.SELECT, "id, Media_id");
                inputMap.put(ODataQueryConstants.FILTER,
                        "RequestMessage_id eq '" + requestMessageId + "' and Media_id eq '" + mediaId + "'");
                inputMap.put(ODataQueryConstants.TOP, "1");
                response =
                        Executor.invokeService(ServiceURLEnum.MESSAGEATTACHMENT_READ, inputMap, null, requestInstance);
                responseJSON = CommonUtilities.getStringAsJSONObject(response);

                if (responseJSON != null && responseJSON.has(FabricConstants.OPSTATUS)
                        && responseJSON.optInt(FabricConstants.OPSTATUS) == 0
                        && responseJSON.has("messageattachment")) {

                    // Fetch Message Attachment-Id
                    messageAttachmentsJSONArray = responseJSON.getJSONArray("messageattachment");

                    if (messageAttachmentsJSONArray.length() > 0) {
                        currMessageAttachmentJSONObject = messageAttachmentsJSONArray.optJSONObject(0);
                        if (currMessageAttachmentJSONObject != null && currMessageAttachmentJSONObject.has("id")
                                && currMessageAttachmentJSONObject.has("Media_id")) {

                            // Delete Message Attachment Entry
                            currMessageAttachmentId = currMessageAttachmentJSONObject.getString("id");
                            inputMap.put("id", currMessageAttachmentId);
                            Executor.invokeService(ServiceURLEnum.MESSAGEATTACHMENT_DELETE, inputMap, null,
                                    requestInstance);

                            // Delete Media Entry
                            inputMap.clear();
                            mediaId = currMessageAttachmentJSONObject.getString("Media_id");
                            inputMap.put("id", mediaId);
                            Executor.invokeService(ServiceURLEnum.MEDIA_DELETE, inputMap, null, requestInstance);
                        }
                    }

                }

                record.addParam(new Param("mediaId", mediaId, FabricConstants.STRING));
            }
            return record;
        }
        return null;
    }

    /**
     * @param requestInstance
     * @param groupName
     * @return
     */
//    private String getMemberGroupId(DataControllerRequest requestInstance, String groupName) {
//
//        String groupId = StringUtils.EMPTY;
//        if (StringUtils.isBlank(groupName)) {
//            return groupName;
//        }
//
//        Map<String, String> queryMap = new HashMap<>();
//        queryMap.put(ODataQueryConstants.SELECT, "id");
//        queryMap.put(ODataQueryConstants.FILTER, "Name eq '" + groupName + "'");
//        queryMap.put(ODataQueryConstants.TOP, "1");
//
//        String readMemberGroupResponse =
//                Executor.invokeService(ServiceURLEnum.MEMBERGROUP_READ, queryMap, null, requestInstance);
//        JSONObject readMemberGroupResponseJSON = CommonUtilities.getStringAsJSONObject(readMemberGroupResponse);
//
//        if (readMemberGroupResponseJSON != null && readMemberGroupResponseJSON.has(FabricConstants.OPSTATUS)
//                && readMemberGroupResponseJSON.optInt(FabricConstants.OPSTATUS) == 0
//                && readMemberGroupResponseJSON.has("membergroup")) {
//
//            JSONArray groupJSONArray = readMemberGroupResponseJSON.getJSONArray("membergroup");
//            if (groupJSONArray != null && groupJSONArray.length() > 0) {
//                JSONObject currGroupObject = groupJSONArray.optJSONObject(0);
//                if (currGroupObject != null && currGroupObject.has("id")) {
//                    groupId = currGroupObject.optString("id");
//                }
//            }
//        }
//        return groupId;
//    }

    /**
     * @param operationRecord
     * @param resultObject
     * @return
     */
    private boolean checkForOperationSuccessAndPrepareServiceResult(Record operationRecord, Result resultObject) {
        if (operationRecord == null) {
            return false;
        }
        if (operationRecord.getParamByName("statusCode") != null) {
            if (!operationRecord.getParamByName("statusCode").getValue().equalsIgnoreCase("0")) {
                ErrorCodeEnum.ERR_20121.setErrorCode(resultObject);
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * @param operationRecord
     * @return
     */
//    private boolean checkForOperationSuccessState(Record operationRecord) {
//        if (operationRecord.getParamByName("statusCode") != null) {
//            if (!operationRecord.getParamByName("statusCode").getValue().equalsIgnoreCase("0")) {
//                return false;
//            } else {
//                return true;
//            }
//        }
//        return false;
//    }

    /**
     * @param requestInstance
     * @param processedResult
     * @param customerRequestBeanInstance
     * @return
     * @throws IOException
     */
    @SuppressWarnings({ "deprecation" })
    private boolean validateRequestInstanceAndInitialiseBean(DataControllerRequest requestInstance,
            Result processedResult, CustomerRequestPayload customerRequestBeanInstance) throws IOException {

        if (requestInstance == null) {
            ErrorCodeEnum.ERR_20561.setErrorCode(processedResult);
            alert.prepareError("Data Controller Request Instance is null. Cannot Process Request").log();
            return false;

        }
        /*
         * Data From client is sent as MultiPart-Form-Data or Application/JSON. Below Code is to process the data and to
         * add it to the requestInstance. THE OPERATIONS USING THIS SERVICE CODE MUST BE OF TYPE PASS-THROUGH ONLY IN
         * ALL CASES
         */
        List<FormItem> formItems = null;
        try {
            formItems = MultipartPayloadHandler.handleMultipart(requestInstance);
        } catch (FileSizeLimitExceededException fslee) {
            ErrorCodeEnum.ERR_20563.setErrorCode(processedResult);
            return false;
        } catch (InvalidFileNameException ifne) {
            ErrorCodeEnum.ERR_20542.setErrorCode(processedResult);
            return false;
        }

        if (MultipartPayloadHandler.isMultipartRequest(requestInstance)) {
            diagnostic.prepareDebug("Content-Type:MultiPart Request").log();
            if (formItems != null) {
                for (FormItem currFormItem : formItems) {
                    requestInstance.addRequestParam_(currFormItem.getParamName(), currFormItem.getParamValue());
                    diagnostic.prepareDebug("KEY:" + currFormItem.getParamName() + "-->VALUE:" + currFormItem.getParamValue()).log();
                }
            }
        } else { // Content-Type -> application/JSON
            diagnostic.prepareDebug("Content-Type:application/JSON").log();
            HttpServletRequest httpServletRequest = null;
            String requestJSON = null;
            if ((httpServletRequest = (HttpServletRequest) requestInstance.getOriginalRequest()) != null) {
                requestJSON = IOUtils.toString(httpServletRequest.getInputStream(), StandardCharsets.UTF_8);
            }
            if (StringUtils.isBlank(requestJSON)) {
                ErrorCodeEnum.ERR_20982.setErrorCode(processedResult);
                alert.prepareError("ERROR: Invalid Request. Request Payload has been found to be Empty.").log();
                return false;
            } else {
            	if(requestJSON.indexOf("jsondata=") == 0) {
            		requestJSON = requestJSON.replaceFirst("jsondata=","");
            		requestJSON = URLDecoder.decode(requestJSON, "UTF-8");
            	}
                JSONObject requestJSONObject = new JSONObject(requestJSON);
                for (String currKey : requestJSONObject.keySet()) {
                    Object currValObj = requestJSONObject.opt(currKey);
                    if (currValObj != null) {
                        diagnostic.prepareDebug("KEY:" + currKey + "-->VALUE:" + String.valueOf(currValObj)).log();
                        requestInstance.addRequestParam_(currKey, String.valueOf(currValObj));
                    }
                }
            }
        }
        customerRequestBeanInstance.setFormItems(formItems);
        return true;
    }
    
    private Set<String> getServiceDefinitionId(DataControllerRequest requestInstance, String id) {

        Set<String> serviceDefinitionId = new HashSet<String>();
        String groupTypeId = StringUtils.EMPTY;
        if (StringUtils.isBlank(id)) {
            return serviceDefinitionId;
        }

        ServiceDefinitionBusinessDelegate serviceDefinitionBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class)
                .getBusinessDelegate(ServiceDefinitionBusinessDelegate.class);
        
        ServiceDefinitionDTO serviceDefinitionDTO = serviceDefinitionBusinessDelegate.getServiceDefinitionById(id);
        if(null != serviceDefinitionDTO && StringUtils.isNotBlank(serviceDefinitionDTO.getId())) {
        	
        	serviceDefinitionId.add(serviceDefinitionDTO.getId());
        	return serviceDefinitionId;
        }
        
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put(ODataQueryConstants.SELECT, "id");
        queryMap.put(ODataQueryConstants.FILTER, "id eq '" + id + "'");
        //queryMap.put(ODataQueryConstants.TOP, "1");

        String readMemberGroupResponse =
                Executor.invokeService(ServiceURLEnum.MEMBERGROUPTYPE_READ, queryMap, null, requestInstance);
        JSONObject readMemberGroupResponseJSON = CommonUtilities.getStringAsJSONObject(readMemberGroupResponse);

        if (readMemberGroupResponseJSON != null && readMemberGroupResponseJSON.has(FabricConstants.OPSTATUS)
                && readMemberGroupResponseJSON.optInt(FabricConstants.OPSTATUS) == 0
                && readMemberGroupResponseJSON.has("membergrouptype")) {

            JSONArray groupJSONArray = readMemberGroupResponseJSON.getJSONArray("membergrouptype");
            if (groupJSONArray != null && groupJSONArray.length() > 0) {
                JSONObject currGroupObject = groupJSONArray.optJSONObject(0);
                if (currGroupObject != null && currGroupObject.has("id")) {
                	groupTypeId = currGroupObject.optString("id");
                }
            }
        }
        
        if(StringUtils.isNotBlank(groupTypeId)) {
        	queryMap = new HashMap<>();
            queryMap.put(ODataQueryConstants.SELECT, "id");
            queryMap.put(ODataQueryConstants.FILTER, "serviceType eq '" + groupTypeId + "'");
            
            String serviceDefinitionResponse =
                    Executor.invokeService(ServiceURLEnum.SERVICEDEFINITION_READ, queryMap, null, requestInstance);
            JSONObject serviceDefinitionResponseJSON = CommonUtilities.getStringAsJSONObject(serviceDefinitionResponse);
            if (serviceDefinitionResponseJSON != null && serviceDefinitionResponseJSON.has(FabricConstants.OPSTATUS)
                    && serviceDefinitionResponseJSON.optInt(FabricConstants.OPSTATUS) == 0
                    && serviceDefinitionResponseJSON.has("servicedefinition")) {

                JSONArray sdJSONArray = serviceDefinitionResponseJSON.getJSONArray("servicedefinition");
                if (sdJSONArray != null && sdJSONArray.length() > 0) {
                	for(int i=0 ; i< sdJSONArray.length(); i++) {
                		JSONObject currSDObject = sdJSONArray.optJSONObject(i);
                        if (currSDObject != null && currSDObject.has("id")) {
                        	serviceDefinitionId.add(currSDObject.optString("id"));
                        }
                	}
                }
            }
        	
        }
        
        return serviceDefinitionId;
    }
    
	private String getXSSSanitizedString(String inputString) {
		
			if (!StringUtils.isBlank(inputString)) {
				// Avoid null characters
				inputString = inputString.replaceAll("\0", "");
				// Eliminates the "<", ">", "\'", "\/", "\"", "=" from the string
				inputString = inputString.replaceAll(">", "&gt;");
				inputString = inputString.replaceAll("<", "&lt;");
				inputString = inputString.replaceAll("\"", "&quot;");
				inputString = inputString.replaceAll("\'", "&#x27;");
				inputString = inputString.replaceAll("/", "&#x2F;");
				inputString = inputString.replaceAll("=", "&eq;");
				inputString = inputString.replaceAll("\\^", "&#94;");
				inputString = inputString.replaceAll("\\*", "&#42;");
			}
			return inputString;
	
	}
	
	private String messageDescriptionValidation(String messageDescription) {
		
		String p = "(&amp;lt;)\\/?(?!a|i|u|b|br|div|span|ul|li|table|tbody|tr|th|td|font|blockquote|h1|h2|h3|h4|p|img)[^(&amp;gt;)]*(&amp;gt;)";

		messageDescription = StringUtils.trim(CommonUtilities.decodeFromBase64(messageDescription));
		messageDescription = messageDescription.replaceAll("alert\\(", "(").replaceAll("iframe", "")
				.replaceAll("prompt\\(", "(").replaceAll("onload=", "").replaceAll("onerror=", "").replaceAll("confirm\\(", "(").replaceAll(p, "<");

		return messageDescription;
	}
    
}
