package com.kony.dbputilities.customersecurityservices;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.dbp.core.constants.DBPConstants;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.sessionmanager.SessionScope;
import com.kony.dbputilities.util.DBPDatasetConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.MWConstants;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class CreateOrgEmployeeTransactionLimits implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {

        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);

        Result result = new Result();
        Record record = new Record();

        String id = inputParams.get("id");

        if(StringUtils.isBlank(id)) {
            id = requestInstance.getParameter("id");
        }

        if(StringUtils.isBlank(id)) {
            id = requestInstance.getParameter("Customer_Id");
        }

        String userName = requestInstance.getParameter("UserName");

        if(StringUtils.isBlank(userName)) {
            userName = inputParams.get("UserName");
        }


        if (StringUtils.isBlank(id) && StringUtils.isNotBlank(userName)) {
            Result userRec = HelperMethods.getUserRecordByName(requestInstance.getParameter("UserName"), requestInstance);
            id = HelperMethods.getFieldValue(userRec, "id");
        }

        String loggedInUserId =  HelperMethods.getCustomerIdFromSession(requestInstance);
        Map<String, String> loggedInUserInfo = HelperMethods.getCustomerFromAPIDBPIdentityService(requestInstance);
        if(StringUtils.isBlank(loggedInUserId)) {
            loggedInUserId = loggedInUserInfo.get("customer_id");
        }


        if (StringUtils.isBlank(id)  || loggedInUserId.equals(id)) {
            ErrorCodeEnum.ERR_12405.setErrorCode(record);
            result.addRecord(record);
            return result;
        }

        String orgIdOfUserUnderUpdate = inputParams.get("Organization_Id"); 

        if(StringUtils.isBlank(orgIdOfUserUnderUpdate)) {
            orgIdOfUserUnderUpdate = HelperMethods.getOrganizationIDForUser(id, requestInstance);
        }

        if (StringUtils.isBlank(orgIdOfUserUnderUpdate)) {
            ErrorCodeEnum.ERR_12406.setErrorCode(record);
            result.addRecord(record);
            return result;
        }

        String loggedInUserOrgId = HelperMethods.getOrganizationIDForUser(loggedInUserId, requestInstance);

        if (HelperMethods.isAuthenticationCheckRequiredForService(loggedInUserInfo)) {
            Set<String> userPermissions = SessionScope.getAllPermissionsFromIdentityScope(requestInstance);
            if (userPermissions.contains("USER_MANAGEMENT")) {
                if (!loggedInUserOrgId.equals(orgIdOfUserUnderUpdate)) {
                    ErrorCodeEnum.ERR_12408.setErrorCode(record);
                    result.addRecord(record);
                    return result;
                }
            } else {
                ErrorCodeEnum.ERR_12407.setErrorCode(record);
                result.addRecord(record);
                return result;
            }
        }

        String group_id = requestInstance.getParameter("Role_id");

        if(StringUtils.isBlank(group_id) || !isValidGroupID(group_id, orgIdOfUserUnderUpdate, requestInstance) || !AddCustomerGroup(requestInstance, inputParams)) {
            ErrorCodeEnum.ERR_13514.setErrorCode(result);
            return result;
        }

        ;

        Result groupResult = (Result) new AssociateBusinessUserActionLimitsAndGroup().invoke(methodID, inputArray, requestInstance, responseInstance);

        if(groupResult.getNameOfAllParams().contains("Status")) {
            record.addParam("GroupCreationStatus", groupResult.getParamValueByName("Status"), MWConstants.STRING);
        }
        else{
            if(groupResult.getNameOfAllParams().contains("FailureReason")) {
                record.addParam("GroupCreationFailureReason",groupResult.getParamValueByName("FailureReason") , MWConstants.STRING);
            }
            else {
                record.addParam("GroupCreationStatus", "failure", MWConstants.STRING);
            }
            record.addParam(DBPConstants.DBP_ERROR_CODE_KEY, groupResult.getParamValueByName(DBPConstants.DBP_ERROR_CODE_KEY));
            record.addParam(DBPConstants.DBP_ERROR_MESSAGE_KEY, groupResult.getParamValueByName(DBPConstants.DBP_ERROR_MESSAGE_KEY));
        }

        record.setId("Limits_attr");
        result.addRecord(record);
        return result;
    }


    private boolean AddCustomerGroup(DataControllerRequest dcRequest, Map<String, String> inputParams) {
        // TODO Auto-generated method stub

        String customerId = dcRequest.getParameter("Customer_id")!=null ? dcRequest.getParameter("Customer_id") : dcRequest.getParameter("id");

        if(customerId!= null) {

            String filter = "Type_id"+DBPUtilitiesConstants.EQUAL+ "TYPE_ID_BUSINESS";

            Map<String, Object> input = new HashMap<String, Object>();
            input.put(DBPUtilitiesConstants.FILTER, filter);

            JsonObject jsonObject = ServiceCallHelper.invokeServiceAndGetJson(input, dcRequest.getHeaderMap(), URLConstants.MEMBERGROUP_GET);

            JsonArray membergroups = jsonObject.has(DBPDatasetConstants.DATASET_MEMBERGROUP)&& !jsonObject.get(DBPDatasetConstants.DATASET_MEMBERGROUP).isJsonNull()&& jsonObject.get(DBPDatasetConstants.DATASET_MEMBERGROUP).isJsonArray() && jsonObject.get(DBPDatasetConstants.DATASET_MEMBERGROUP).getAsJsonArray().size()>0 ? jsonObject.get(DBPDatasetConstants.DATASET_MEMBERGROUP).getAsJsonArray() : new JsonArray(); 

            Set<String> businessGroups = new HashSet<String>();
            for(int i=0; i<membergroups.size(); i++) {
                businessGroups.add(membergroups.get(i).getAsJsonObject().get("id").getAsString());
            }

            String group_id = dcRequest.getParameter("Role_id");

            if(!businessGroups.contains(group_id)) {
                return false;
            }            


            filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + customerId;

            Result result = new Result();

            try {
                result = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest), URLConstants.CUSTOMER_GROUP_GET);
            } catch (HttpCallException e1) {

            	alert.prepareError(e1.toString()).log();
            }
            
            Map<String, String> postParamMapGroup = new HashMap<>();
            if(result.getDatasetById(DBPDatasetConstants.DATASET_CUSTOMERGROUP) != null) {
                Dataset dataset =  result.getDatasetById(DBPDatasetConstants.DATASET_CUSTOMERGROUP);
                for(Record record : dataset.getAllRecords()) {
                    if(record.getParamValueByName("Group_id") != null && businessGroups.contains(record.getParamValueByName("Group_id"))) {
                        postParamMapGroup.put("Customer_id", customerId);
                        postParamMapGroup.put("Group_id", record.getParamValueByName("Group_id"));
                        try {
                            HelperMethods.callApi(dcRequest, postParamMapGroup, HelperMethods.getHeaders(dcRequest),
                                    URLConstants.CUSTOMER_GROUP_DELETE);
                        } catch (HttpCallException e) {
                        	alert.prepareError(e.toString()).log();
                        }
                    }
                }
            }
            
            Map<String, String> loggedInUserInfo = HelperMethods.getCustomerFromAPIDBPIdentityService(dcRequest);
            String userName = loggedInUserInfo.get("UserName");
            if(StringUtils.isBlank(userName)) {
                loggedInUserInfo = HelperMethods.getCustomerFromIdentityService(dcRequest);
                userName = loggedInUserInfo.get("UserName");
            }
            
            postParamMapGroup.put("Customer_id", customerId);
            postParamMapGroup.put("Group_id", group_id);
            postParamMapGroup.put("createdby", userName);
            postParamMapGroup.put("modifiedby", userName);

            try {
                HelperMethods.callApi(dcRequest, postParamMapGroup, HelperMethods.getHeaders(dcRequest),
                        URLConstants.CUSTOMER_GROUP_CREATE);
                return true;
            } catch (HttpCallException e) {
            	alert.prepareError(e.toString()).log();
            }
        }

        return false;
    }

    private boolean isValidGroupID(String group_id, String organizationId, DataControllerRequest dcRequest) {

        String filter = "id"+DBPUtilitiesConstants.EQUAL+ organizationId;
        Map<String, Object> inputParams = new HashMap<String, Object>();
        inputParams.put(DBPUtilitiesConstants.FILTER, filter);

        JsonObject jsonObject = ServiceCallHelper.invokeServiceAndGetJson(inputParams, dcRequest.getHeaderMap(), URLConstants.ORGANISATION_GET);

        jsonObject = jsonObject.has(DBPDatasetConstants.DATASET_ORGANISATION)&& !jsonObject.get(DBPDatasetConstants.DATASET_ORGANISATION).isJsonNull()&& jsonObject.get(DBPDatasetConstants.DATASET_ORGANISATION).isJsonArray() && jsonObject.get(DBPDatasetConstants.DATASET_ORGANISATION).getAsJsonArray().size()>0 ? jsonObject.get(DBPDatasetConstants.DATASET_ORGANISATION).getAsJsonArray().get(0).getAsJsonObject() : new JsonObject(); 

        String businessType_id = jsonObject.has("BusinessType_id") && !jsonObject.get("BusinessType_id").isJsonNull() ? jsonObject.get("BusinessType_id").getAsString() : "";

        if(StringUtils.isBlank(businessType_id)) {
            return false;
        }

        filter = "BusinessType_id"+DBPUtilitiesConstants.EQUAL+ businessType_id + DBPUtilitiesConstants.AND + "Group_id"+ DBPUtilitiesConstants.EQUAL + group_id;

        inputParams = new HashMap<String, Object>();
        inputParams.put(DBPUtilitiesConstants.FILTER, filter);

        jsonObject = ServiceCallHelper.invokeServiceAndGetJson(inputParams, dcRequest.getHeaderMap(), URLConstants.GROUPBUSINESSTYPE_GET);

        boolean hasAccess = jsonObject.has(DBPDatasetConstants.DATASET_GROUPBUSINESSTYPE)&& !jsonObject.get(DBPDatasetConstants.DATASET_GROUPBUSINESSTYPE).isJsonNull()&& jsonObject.get(DBPDatasetConstants.DATASET_GROUPBUSINESSTYPE).isJsonArray() && jsonObject.get(DBPDatasetConstants.DATASET_GROUPBUSINESSTYPE).getAsJsonArray().size()>0 ? true : false ; 

        return hasAccess;
        
    }
}
