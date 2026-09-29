package com.temenos.dbx.product.resource.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.BCrypt;
import com.kony.dbputilities.util.BundleConfigurationHandler;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.IntegrationTemplateURLFinder;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.URLFinder;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.objectserviceutils.EventsDispatcher;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.registry.AppRegistryException;
import com.temenos.dbx.product.dto.BackendIdentifierDTO;
import com.temenos.dbx.product.dto.CustomerCommunicationDTO;
import com.temenos.dbx.product.dto.CustomerDTO;
import com.temenos.dbx.product.dto.CustomerPreferenceDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.dto.PasswordHistoryDTO;
import com.temenos.dbx.product.resource.api.CustomerResource;
import com.temenos.dbx.product.usermanagement.businessdelegate.api.CustomerPreferenceBusinessDelegate;
import com.temenos.dbx.product.usermanagement.businessdelegate.api.PasswordHistoryBusinessDelegate;
import com.temenos.dbx.product.usermanagement.businessdelegate.api.UserManagementBusinessDelegate;
import com.temenos.dbx.product.usermanagement.resource.api.InfinityUserManagementResource;
import com.temenos.dbx.product.utils.CustomerUtils;
import com.temenos.dbx.product.utils.DTOConstants;
import com.temenos.dbx.product.utils.DTOUtils;
import com.temenos.dbx.product.utils.InfinityConstants;

public class CustomerResourceImpl implements CustomerResource {

    private static Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    private static Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Result get(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) {
        // TODO Auto-generated method stub
        return new Result();
    }

    private void makeAuditEntry(Object[] inputArray, DataControllerRequest dcRequest, Result result,
            DataControllerResponse dcResponse, boolean isSuccess, String eventType) {
        try {

            String enableEvents = URLFinder.getPathUrl("ENABLE_EVENTS", dcRequest);

            alert.prepareError("ENABLE_EVENTS=  " + enableEvents).log();

            String partyEventData = HelperMethods.getInputParamMap(inputArray).get(DTOConstants.PARTYEVENTDATA);
            if (StringUtils.isBlank(partyEventData)) {
                partyEventData = dcRequest.getParameter(DTOConstants.PARTYEVENTDATA);
            }

            JsonObject customParams = getJsonFromInput(HelperMethods.getInputParamMap(inputArray), dcRequest);

            String operation = null;
            try {
                operation = dcRequest.getServicesManager().getOperationData().getOperationId();
            } catch (AppRegistryException e) {
                alert.prepareError("Error while getting Operation from service Manager", e).log();
            }

            String eventSubType = "";

            if (enableEvents != null && enableEvents.equals("true")) {

                if (operation.toLowerCase().contains("create")) {
                    eventSubType = eventType + "_CREATE";
                } else {
                    eventSubType = eventType + "_UPDATE";
                }

                if (StringUtils.isNotBlank(result.getParamValueByName(DTOConstants.ID))) {
                    customParams.addProperty("customerId", result.getParamValueByName(DTOConstants.ID));
                }

                if (StringUtils.isNotBlank(result.getParamValueByName(DTOConstants.PARTYID))) {
                    customParams.addProperty(DTOConstants.PARTYID, result.getParamValueByName(DTOConstants.PARTYID));
                }

                if (StringUtils.isNotBlank(result.getParamValueByName(DTOConstants.CORECUSTOMERID))) {
                    customParams.addProperty(DTOConstants.CORECUSTOMERID,
                            result.getParamValueByName(DTOConstants.CORECUSTOMERID));
                }

                alert.prepareError("eventsArray:" + customParams.toString()).log();

                String StatusId;
                if (isSuccess) {
                    StatusId = "SID_EVENT_SUCCESS";
                } else {
                    StatusId = "SID_EVENT_FAILURE";
                }

                EventsDispatcher.dispatch(dcRequest, dcResponse, eventType, eventSubType,
                        CustomerResourceImpl.class.getName(), StatusId, null, null, customParams);
            }

        } catch (Exception ex) {
            alert.prepareError("exception occured while sending alert for transfer=", ex).log();
        }
    }

    private JsonObject getJsonFromInput(Map<String, String> inputParamMap, DataControllerRequest dcRequest) {

        JsonObject jsonObject = new JsonObject();
        for (String key : inputParamMap.keySet()) {
            if (StringUtils.isNotBlank(inputParamMap.get(key))) {
                jsonObject.addProperty(key, inputParamMap.get(key));
            }
        }

        Iterator<String> iterator = dcRequest.getParameterNames();

        while (iterator.hasNext()) {
            String key = iterator.next();
            if (StringUtils.isNotBlank(dcRequest.getParameter(key))) {
                jsonObject.addProperty(key, dcRequest.getParameter(key));
            }
        }

        return jsonObject;
    }

    public static String getStringFromJsonObject(JsonObject object, String key, boolean required) {
        JsonElement element = getElementFromJsonObject(object, key, required);
        return element == null ? null : element.getAsString();
    }

    public static JsonElement getElementFromJsonObject(JsonObject object, String key, boolean required) {
        JsonElement element = object.get(key);
        if ((element == null) && (required)) {
            throw new IllegalArgumentException("Required attribute '" + key + "' was not present");
        }
        return element;
    }

    public static JsonObject getJsonObjectFromJsonObject(JsonObject object, String key, boolean required) {
        JsonElement element = getElementFromJsonObject(object, key, required);
        if (element == null) {
            return null;
        }
        if (!element.isJsonObject()) {
            throw new IllegalArgumentException("Value for attribute '" + key + "' is not a JSON object");
        }
        return element.getAsJsonObject();
    }

    @Override
    public Result saveFromDBX(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) {

        Result result = new Result();
        alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
        String coreCustomerId = HelperMethods.getInputParamMap(inputArray).get(InfinityConstants.coreCustomerId);

        boolean isNotificationRequired = Boolean
                .parseBoolean(HelperMethods.getInputParamMap(inputArray).get(InfinityConstants.isNotificationRequired));

        String customerID = HelperMethods.getNumericId() + "";

        CustomerDTO customerDTO = new  CustomerDTO();
                
        try {
        customerDTO = CustomerUtils.buildCustomerDTO(customerID, HelperMethods.getInputParamMap(inputArray), dcRequest);
        }
        catch(Exception e) {
            ErrorCodeEnum.ERR_10216.setErrorCode(result, "Propect creation is failed");
            return result;
        }
        
        customerDTO.setCustomerType_id(HelperMethods.getCustomerTypes().get("Prospect"));
        customerDTO.setStatus_id(HelperMethods.getCustomerStatus().get("NEW"));
        customerDTO.setIsEnrolledFromSpotlight("0");
        UserManagementBusinessDelegate customerDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class)
                .getBusinessDelegate(UserManagementBusinessDelegate.class);

        String password = customerDTO.getPassword();

        String salt = BCrypt.gensalt(DBPUtilitiesConstants.SALT_NUMBER_OF_ROUNDS);
        String hashedPassword = BCrypt.hashpw(customerDTO.getPassword(), salt);
        customerDTO.setPassword(hashedPassword);
        DBXResult response = customerDelegate.update(customerDTO, dcRequest.getHeaderMap());

        if (response.getResponse().equals(customerDTO.getId())) {
            result.addParam(DTOConstants.ID, customerDTO.getId());
            makeAuditEntry(inputArray, dcRequest, result, dcResponse, true, DTOConstants.PROSPECT);
            createCustomerPreference(customerDTO.getId(), dcRequest.getHeaderMap());
            createCustomerGroup(customerDTO.getId(), dcRequest);
            PasswordHistoryDTO dto = new PasswordHistoryDTO();

            dto.setId(HelperMethods.getNewId());
            dto.setCustomer_id(customerDTO.getId());
            dto.setPreviousPassword(customerDTO.getPassword());
            dto.setIsNew(true);

            PasswordHistoryBusinessDelegate passwordHistoryBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(BusinessDelegateFactory.class)
                    .getBusinessDelegate(PasswordHistoryBusinessDelegate.class);

            passwordHistoryBusinessDelegate.update(dto, dcRequest.getHeaderMap());
            
            result.addParam(InfinityConstants.userName, customerDTO.getUserName());

            if (isNotificationRequired) {
                HelperMethods.getInputParamMap(inputArray).put(InfinityConstants.password, password);
                HelperMethods.getInputParamMap(inputArray).put(InfinityConstants.userId, customerDTO.getId());
                HelperMethods.getInputParamMap(inputArray).put(InfinityConstants.userName, customerDTO.getUserName());
                HelperMethods.getInputParamMap(inputArray).put(InfinityConstants.contractStatus,
                        DBPUtilitiesConstants.CONTRACT_STATUS_ACTIVE);
                HelperMethods.getInputParamMap(inputArray).put(InfinityConstants.isOnBoradingFlow, "true");
                HelperMethods.getInputParamMap(inputArray).put(InfinityConstants.isProspectFlow, "true");
                HelperMethods.getInputParamMap(inputArray).put(InfinityConstants.applicationid, customerDTO.getApplicationID());
                if(StringUtils.isNotBlank(HelperMethods.getInputParamMap(inputArray).get(InfinityConstants.phoneNumber))) {
                    String phone = HelperMethods.getInputParamMap(inputArray).get(InfinityConstants.phoneNumber);
                    if(!phone.contains("-")) {
                        if(StringUtils.isNotBlank(HelperMethods.getInputParamMap(inputArray).get(InfinityConstants.phoneCountryCode))) {
                            phone = HelperMethods.getInputParamMap(inputArray).get(InfinityConstants.phoneCountryCode) + "-" + phone;
                        }
                    }
                    HelperMethods.getInputParamMap(inputArray).put(DTOConstants.PHONE, phone);
                }
                
                if(StringUtils.isNotBlank(HelperMethods.getInputParamMap(inputArray).get(InfinityConstants.email))) {
                    HelperMethods.getInputParamMap(inputArray).put(DTOConstants.EMAIL, HelperMethods.getInputParamMap(inputArray).get(InfinityConstants.email));
                }
                if(HelperMethods.getInputParamMap(inputArray).get(InfinityConstants.applicationid)!=null || StringUtils.isNotBlank(HelperMethods.getInputParamMap(inputArray).get(InfinityConstants.applicationid))) {
                	HelperMethods.getInputParamMap(inputArray).put(InfinityConstants.EMAIL_TEMPLATE, DBPUtilitiesConstants.ONBOARDING_PROSPECT_USERNAME_APPICATIONID_TEMPLATE);
                }
                else {
                HelperMethods.getInputParamMap(inputArray).put(InfinityConstants.EMAIL_TEMPLATE, DBPUtilitiesConstants.ONBOARDING_PROSPECT_USERNAME_TEMPLATE);
                }
                HelperMethods.getInputParamMap(inputArray).put(InfinityConstants.SMS_TEMPLATE, DBPUtilitiesConstants.ONBOARDING_PROSPECT_ACTIVATIONCODE_TEMPLATE);

                InfinityUserManagementResource resource =
                        DBPAPIAbstractFactoryImpl.getResource(InfinityUserManagementResource.class);

                try {
                    resource.generateInfinityUserActivationCodeAndUsername(methodID, inputArray, dcRequest, dcResponse);
                } catch (ApplicationException e) {
                    
                	alert.prepareError("Exception", e).log();
                }
            }

        } else {
            result.addParam(ErrorCodeEnum.ERROR_MESSAGE_KEY, "Prospect CreateFailed in DBX");
            makeAuditEntry(inputArray, dcRequest, result, dcResponse, false, DTOConstants.PROSPECT);
        }
        return result;
    }

    private void createCustomerGroup(String id, DataControllerRequest dcRequest) {
        Map<String, Object> input = new HashMap<String, Object>();
        input.put(InfinityConstants.Customer_id, id);
        Map<String, String> bundleConfigurations = BundleConfigurationHandler
                .fetchBundleConfigurations(BundleConfigurationHandler.BUDLENAME_C360, dcRequest);
        String groupId = StringUtils.isNotBlank( bundleConfigurations.get(BundleConfigurationHandler.DEFAULT_PROSPECT_GROUP))? bundleConfigurations.get(BundleConfigurationHandler.DEFAULT_PROSPECT_GROUP) :"DEFAULT_GROUP";
        input.put(InfinityConstants.Group_id, groupId);
        ServiceCallHelper.invokeServiceAndGetJson(input, dcRequest.getHeaderMap(), URLConstants.CUSTOMER_GROUP_CREATE);
    }

    private void createCustomerPreference(String customerId, Map<String, Object> headersMap) {
        CustomerPreferenceDTO customerPreferenceDTO = new CustomerPreferenceDTO();
        customerPreferenceDTO.setId(HelperMethods.getNewId());
        customerPreferenceDTO.setCustomer_id(customerId);
        customerPreferenceDTO.setIsNew(true);

        CustomerPreferenceBusinessDelegate customerPreferenceBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class)
                .getBusinessDelegate(CustomerPreferenceBusinessDelegate.class);

        customerPreferenceBusinessDelegate.update(customerPreferenceDTO, headersMap);

    }
    
    @Override
    public Result updateFromDBX(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) {
        Result result = new Result();
        alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
        diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        String coreCustomerID = HelperMethods.getInputParamMap(inputArray).get(DTOConstants.CORECUSTOMERID);
        String partyID = HelperMethods.getInputParamMap(inputArray).get(DTOConstants.PARTYID);
        String customerID = HelperMethods.getInputParamMap(inputArray).get(DTOConstants.ID);
        
        if (StringUtils.isBlank(coreCustomerID)) {
            coreCustomerID = dcRequest.getParameter(DTOConstants.CORECUSTOMERID);
        }

        if (StringUtils.isBlank(partyID)) {
            partyID = dcRequest.getParameter(DTOConstants.PARTYID);
        }        

        if (StringUtils.isBlank(customerID)) {
            customerID = dcRequest.getParameter(DTOConstants.ID);
        }
        
        if (StringUtils.isAllBlank(coreCustomerID, partyID, customerID)) {
            diagnostic.prepareDebug("Customer update is failed").log();
            makeAuditEntry(inputArray, dcRequest, result, dcResponse, false, DTOConstants.PROSPECT);
            ErrorCodeEnum.ERR_10217.setErrorCode(result);
            return result;
        }

        CustomerDTO customerDTO = CustomerUtils.buildCustomerDTO(customerID, inputParams, dcRequest);

        UserManagementBusinessDelegate customerDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class)
                .getBusinessDelegate(UserManagementBusinessDelegate.class);

        DBXResult response = customerDelegate.update(customerDTO, dcRequest.getHeaderMap());

        String id = (String) response.getResponse();
        if (StringUtils.isNotBlank(id)) {
            result.addParam(DTOConstants.ID, id);
            makeAuditEntry(inputArray, dcRequest, result, dcResponse, true, DTOConstants.PROSPECT);
        } else {
            result.addParam(ErrorCodeEnum.ERROR_MESSAGE_KEY, "Prospect CreateFailed in DBX");
            makeAuditEntry(inputArray, dcRequest, result, dcResponse, false, DTOConstants.PROSPECT);
        }
        return result;

    }

}
