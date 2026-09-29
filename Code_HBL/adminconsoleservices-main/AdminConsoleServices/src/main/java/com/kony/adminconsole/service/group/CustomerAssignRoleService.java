package com.kony.adminconsole.service.group;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.commons.utils.ODataQueryConstants;
import com.kony.adminconsole.core.config.EnvironmentConfiguration;
import com.kony.adminconsole.core.security.LoggedInUserHandler;
import com.kony.adminconsole.core.security.UserDetailsBean;
import com.kony.adminconsole.dto.EmailHandlerBean;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.handler.EmailHandler;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

/**
 * Service to assign Customers to a Role
 * 
 * @author Sai Krishna Aitha
 *
 */
public class CustomerAssignRoleService implements JavaService2 {

    private static final int CUSTOMERS_BATCH_SIZE = 10;
    private static final String EMAIL_TEMPLATE_PATH = "emailTemplates/UploadCustomerForaRole.html";
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {
       

        try {

            Result processedResult = new Result();

            String roleId = requestInstance.getParameter("roleId");
            String userId = requestInstance.getParameter("User_id");
            // update customer assign role status
            setEntityStatus(roleId, "0", requestInstance);

            // Fetch logged-in user details
            UserDetailsBean loggedInUserDetails = LoggedInUserHandler.getUserDetails(requestInstance);

            // Validate Role Id
            Map<String, String> queryMap = new HashMap<String, String>();
            queryMap.put(ODataQueryConstants.FILTER, "id eq '" + roleId + "'");
            String serviceResponse = Executor.invokeService(ServiceURLEnum.MEMBERGROUP_READ, queryMap, null,
                    requestInstance);
            JSONObject roleReadResponse = CommonUtilities.getStringAsJSONObject(serviceResponse);
            if (roleReadResponse == null || !roleReadResponse.has(FabricConstants.OPSTATUS)
                    || roleReadResponse.getInt(FabricConstants.OPSTATUS) != 0
                    || roleReadResponse.optJSONArray("membergroup") == null) {
                alert.prepareError("Failed to read membergroup").log();
                throw new ApplicationException(ErrorCodeEnum.ERR_20404);
            }
            if (roleReadResponse.optJSONArray("membergroup").length() == 0) {
                alert.prepareError("Invalid Role Id").log();
                throw new ApplicationException(ErrorCodeEnum.ERR_20510);
            }
            JSONArray roleArray = roleReadResponse.optJSONArray("membergroup");
            JSONObject currJson = roleArray.optJSONObject(0);
            String roleName = currJson.optString("Name");
           
            // Load CSV File into a BufferedReader
            diagnostic.prepareDebug("Loading csv file into buffer reader").log();
            String line = StringUtils.EMPTY;
            String encodedString = requestInstance.getParameter("file");
			Base64.Decoder decoder = Base64.getDecoder();
	        byte[] decodedByteArray = decoder.decode(encodedString);
	        String [] decodedStrings= new String(decodedByteArray).replace("\r","").split("\n");
            @SuppressWarnings("unused") // Skip Header Line
            String headerLine = decodedStrings[0];

            // Traverse CSV File
            int index = 0, countOfCustomers = 0;
            String currentCustomerId = StringUtils.EMPTY;
            // Set<String> processedCustomerRecords = new HashSet<>();
            List<String> currentCustomerIds = new ArrayList<>();
            List<String> failedCustomerIds = new ArrayList<>();
            int i =1;
            
            while (i<decodedStrings.length&&(line = decodedStrings[i]) != null) {
                if (StringUtils.isNotBlank(line)) {

                    if (line.contains(",")) {
                        // Extract first element from the row
                        currentCustomerId = StringUtils.trim(line.substring(0, line.indexOf(",")));
                    } else {
                        // Only one element present in the row
                        currentCustomerId = StringUtils.trim(line);
                    }

                    // Check if the customer record has already been processed
                    if (StringUtils.isBlank(currentCustomerId)) {
                        continue;
                    }
                    index++;

                    // Add Customer Id to the current list
                    currentCustomerIds.add(currentCustomerId);
                    countOfCustomers++;
                    if (index == CUSTOMERS_BATCH_SIZE) {
                        // Assign customers by processing current list
                        failedCustomerIds
                                .addAll(assignCustomersToRole(currentCustomerIds, roleId, userId, requestInstance));
                        currentCustomerIds.clear();
                        index = 0;
                    }
                }
                i++;
            }

            // Assign remaining customers
            failedCustomerIds.addAll(assignCustomersToRole(currentCustomerIds, roleId, userId, requestInstance));

            // Compute Failed Count of Customers
            int failedCountOfCustomers = failedCustomerIds.size();
            int successCount = countOfCustomers - failedCountOfCustomers;
            diagnostic.prepareDebug("Failed Count of Customers:" + failedCountOfCustomers).log();

            // Send Email notification

            sendEmailNotificationWithUploadStatus(loggedInUserDetails, roleName, countOfCustomers, successCount,
                    failedCountOfCustomers, failedCustomerIds, requestInstance, processedResult);
            // Return service result
            processedResult.addParam(
                    new Param("TotalCountofCustomers", Integer.toString(countOfCustomers), FabricConstants.STRING));
            processedResult.addParam(
                    new Param("SuccessCountofCustomers", Integer.toString(successCount), FabricConstants.STRING));
            processedResult.addParam(new Param("Failed Ids", failedCustomerIds.toString(), FabricConstants.STRING));
            setEntityStatus(roleId, "1", requestInstance);
            return processedResult;
        } /*catch (FileSizeLimitExceededException fslee) {
            Result errorResult = new Result();
            alert.prepareError("FileSizeLimitExceededException found:" + fslee).log();
            ErrorCodeEnum.ERR_20563.setErrorCode(errorResult);
            return errorResult;
        } */catch (ApplicationException e) {
            Result errorResult = new Result();
            alert.prepareError("ApplicationException found:" + e).log();
            e.getErrorCodeEnum().setErrorCode(errorResult);
            return errorResult;
        } catch (Exception e) {
            Result errorResult = new Result();
            alert.prepareError("Exception:" + e).log();
            ErrorCodeEnum.ERR_20001.setErrorCode(errorResult);
            return errorResult;
        } /*finally {
            br.close();
        }*/
    }

    private void setEntityStatus(String roleId, String status, DataControllerRequest requestInstance)
            throws ApplicationException {

        // Read Customer Assign Role Status
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put(ODataQueryConstants.FILTER, "Entity_id eq '" + roleId + "'");
        requestMap.put(ODataQueryConstants.SELECT, "Entity_id");
        String serviceResponse = Executor.invokeService(ServiceURLEnum.ENTITYSTATUS_READ, requestMap, null,
                requestInstance);
        JSONObject serviceResponseJSON = CommonUtilities.getStringAsJSONObject(serviceResponse);
        if (serviceResponseJSON == null || !serviceResponseJSON.has(FabricConstants.OPSTATUS)
                || serviceResponseJSON.getInt(FabricConstants.OPSTATUS) != 0
                || serviceResponseJSON.optJSONArray("entitystatus") == null) {
            alert.prepareError("Failed to read customerassignrolestatus").log();
            throw new ApplicationException(ErrorCodeEnum.ERR_20431);
        }

        requestMap.clear();
        requestMap.put("Entity_id", roleId);
        requestMap.put("status", status);
        ServiceURLEnum serviceURL;
        ErrorCodeEnum errorCode;
        if (serviceResponseJSON.optJSONArray("entitystatus").length() == 0) {
            // Status info of current role does not exist. create a new record in table
            errorCode = ErrorCodeEnum.ERR_20432;
            serviceURL = ServiceURLEnum.ENTITYSTATUS_CREATE;
        } else {
            // Status info of current role does exist. update existing record in table
            errorCode = ErrorCodeEnum.ERR_20433;
            serviceURL = ServiceURLEnum.ENTITYSTATUS_UPDATE;
        }

        // Check service status
        serviceResponse = Executor.invokeService(serviceURL, requestMap, null, requestInstance);
        serviceResponseJSON = CommonUtilities.getStringAsJSONObject(serviceResponse);
        diagnostic.prepareDebug(serviceResponseJSON.toString()).log();
        if (serviceResponseJSON == null || !serviceResponseJSON.has(FabricConstants.OPSTATUS)
                || serviceResponseJSON.getInt(FabricConstants.OPSTATUS) != 0) {
            alert.prepareError("Failed CRUD Operation:" + serviceURL.name()).log();
            throw new ApplicationException(errorCode);
        }
    }

    @SuppressWarnings("unused")
    private void sendEmailNotificationWithUploadStatus(UserDetailsBean loggedInUserDetails, String rolename,
            int countOfCustomers, int successCountOfCustomers, int failedCountOfCustomers,
            List<String> failedCustomerIds, DataControllerRequest requestInstance, Result processedResult) {

        String loggedInUserId = loggedInUserDetails.getId();
        String firstName = loggedInUserDetails.getFirstName();
        String lastName = loggedInUserDetails.getLastName();
        String emailId = loggedInUserDetails.getEmailId();

        String emailContent = StringUtils.EMPTY;
        String emailSubject = "File import Complete:";
        InputStream templateStream = null;
        try {
            templateStream = this.getClass().getClassLoader().getResourceAsStream(EMAIL_TEMPLATE_PATH);
            emailContent = IOUtils.toString(templateStream, StandardCharsets.UTF_8);

        } catch (IOException e) {
            alert.prepareError("Exception while reading UploadCustomerForaRole.html file", e).log();
            ErrorCodeEnum.ERR_20434.setErrorCode(processedResult);
            return;
        }finally {
        	if( templateStream != null) {
        		try {
        			templateStream.close();
        		}catch(Exception e1) {
        			alert.prepareError(e1.toString()).log();
        		}
        	}
        }

        emailContent = emailContent.replaceAll("%firstname%", firstName);
        //emailContent = emailContent.replaceAll("%filename%", fileName);
        emailContent = emailContent.replaceAll("%Rolename%", rolename);
        emailContent = emailContent.replaceAll("%TotalCustomers%", String.valueOf(countOfCustomers));
        emailContent = emailContent.replaceAll("%SuccessCount%", String.valueOf(successCountOfCustomers));
        emailContent = emailContent.replaceAll("%FailedCount%", String.valueOf(failedCountOfCustomers));
        emailContent = emailContent.replaceAll("%FailedCustomerIDs%", failedCustomerIds.toString());

        String emailTemplateLogo = EnvironmentConfiguration.AC_EMAIL_TEMPLATE_LOGO_URL.getValue(requestInstance);
        emailContent = emailContent.replaceAll("emailTemplateLogo", emailTemplateLogo);

        diagnostic.prepareDebug(emailContent).log();
        EmailHandlerBean emailHandlerBean = new EmailHandlerBean();
        emailHandlerBean.setSubject(emailSubject);
        emailHandlerBean.setBody(emailContent);
        emailHandlerBean.setRecipientEmailId(emailId);
        emailHandlerBean.setFirstName(firstName);
        emailHandlerBean.setLastName(lastName);

//        JSONObject enrollKMSResponse = EmailHandler.enrolKMSUser(emailHandlerBean, requestInstance);
//        diagnostic.prepareDebug(enrollKMSResponse).log();
        JSONObject emailResponse = EmailHandler.sendEmailToSingleRecipient(emailHandlerBean, requestInstance);
        diagnostic.prepareDebug(emailResponse.toString()).log();
    }

    /**
     * Method to assign the role to the list of Customers
     * 
     * @param customerIds
     * @param roleId
     * @param requestInstance
     * @return Ids of Customers for which the role assignment could not be done
     * @throws ApplicationException
     */
    private List<String> assignCustomersToRole(List<String> customerIds, String roleId, String userId,
            DataControllerRequest requestInstance) throws ApplicationException {

        List<String> failedRecords = new ArrayList<>();

        if (customerIds != null && !customerIds.isEmpty()) {
            StringBuilder customerIdsBuffer = new StringBuilder();

            // Append CustomerIds to Buffer
            for (String customerId : customerIds) {
                customerIdsBuffer.append(customerId).append(",");
            }

            // Prepare Input Map
            Map<String, String> inputMap = new HashMap<>();
            String customerIdsStr = customerIdsBuffer.toString();
            customerIdsStr = CommonUtilities.replaceLastOccuranceOfString(customerIdsStr, ",", StringUtils.EMPTY);
            inputMap.put("_customerIds", customerIdsStr);
            inputMap.put("_groupId", roleId);
            inputMap.put("_userId", userId);
            inputMap.put("_numberOfRows", Integer.toString(customerIds.size()));

            // Execute Service
            String operationResponse = Executor.invokeService(ServiceURLEnum.CUSTOMER_GROUP_PROC_SERVICE, inputMap,
                    null, requestInstance);

            // Check Service Success state
            JSONObject operationResponseJSON = CommonUtilities.getStringAsJSONObject(operationResponse);
            if (operationResponseJSON == null || !operationResponseJSON.has(FabricConstants.OPSTATUS)
                    || operationResponseJSON.getInt(FabricConstants.OPSTATUS) != 0
                    || !operationResponseJSON.has("records")) {
                alert.prepareError("Failed CRUD Operation").log();
                throw new ApplicationException(ErrorCodeEnum.ERR_20509);
            }
            diagnostic.prepareDebug("Succesful CRUD Operation").log();

            // Traverse failed records
            JSONArray records = operationResponseJSON.optJSONArray("records");
            if (records != null && records.length() > 0) {

                JSONObject record = records.optJSONObject(0);
                if (record != null && record.has("FAILED_RECORDS")) {

                    // Comma separated list of Customer Ids for which the insert/update failed
                    String failedRecordsVal = record.optString("FAILED_RECORDS");

                    if (StringUtils.isNotBlank(failedRecordsVal)) {
                        if (StringUtils.contains(failedRecordsVal, ",")) {
                            // Multiple Failed Records
                            failedRecords = Arrays.asList(failedRecordsVal.split(","));
                        } else {
                            // Single Failed Record
                            failedRecords.add(failedRecordsVal);
                        }
                    }
                }
            }
        }
        return failedRecords;
    }


    class ImportCustomersRequest {
        private String roleId;
        private File importedFile;
        private String userId;

        public String getRoleId() {
            return roleId;
        }

        public void setRoleId(String roleId) {
            this.roleId = roleId;
        }

        public File getImportedFile() {
            return importedFile;
        }

        public void setImportedFile(File importedFile) {
            this.importedFile = importedFile;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

    }
    
}
