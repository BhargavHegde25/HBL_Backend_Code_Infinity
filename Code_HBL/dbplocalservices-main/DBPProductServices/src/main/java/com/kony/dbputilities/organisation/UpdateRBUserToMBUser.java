package com.kony.dbputilities.organisation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.AdminUtil;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.ErrorCodes;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class UpdateRBUserToMBUser implements JavaService2 {

    @Override
    public Object invoke(String methodId, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {

        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);

        if (preProcess(inputParams, dcRequest, result)) {
            if (!isOrgExists(dcRequest)) {
                String userId = updateCustomerType(dcRequest, result);
                if (StringUtils.isNotBlank(userId)) {
                    inputParams.put("userId", userId);
                    result = createOrganization(inputParams, dcRequest);
                }
                if (HelperMethods.hasRecords(result)) {
                    String id = HelperMethods.getFieldValue(result, "id");
                    inputParams.put("id", id);
                    result = updateOrgIdInCustomer(inputParams, userId, dcRequest);
                    if (!HelperMethods.hasError(result)
                            && StringUtils.isNotBlank(dcRequest.getParameter("Communication"))) {
                        result = CreateOrganisationCommunication.invoke(inputParams, dcRequest);
                    }
                    if (!HelperMethods.hasError(result) && StringUtils.isNotBlank(dcRequest.getParameter("Address"))) {
                        result = CreateOrganisationAddress.invoke(inputParams, dcRequest);
                    }
                    if (!HelperMethods.hasError(result)
                            && StringUtils.isNotBlank(dcRequest.getParameter("Membership"))) {
                        result = CreateOrganisationMembership.invoke(id, null, dcRequest);
                    }
                    if (!HelperMethods.hasError(result)) {
                        result = CreateOrganisationAccounts(inputParams, dcRequest);
                    }
                    if (!HelperMethods.hasError(result)) {
                        result = createOrganisationEmployee(inputParams, dcRequest);
                    }
                    if (!HelperMethods.hasError(result)) {
                        result = CreateOrganisationOwner(inputParams, dcRequest);
                    }
                } else if (HelperMethods.hasError(result)) {

                    if (HelperMethods.getError(result).contains("Name_UNIQUE")) {
                        ErrorCodeEnum.ERR_11008.setErrorCode(result);
                        result.addParam(new Param("dbpErrMessage",
                                "A company is already enrolled in this name. Please check & try again",
                                DBPUtilitiesConstants.STRING_TYPE));
                    } else {
                        ErrorCodeEnum.ERR_11009.setErrorCode(result);
                        result.addParam(new Param("dbpErrMessage", "Company Enrollment is Failed",
                                DBPUtilitiesConstants.STRING_TYPE));
                    }
                    return result;
                }
            } else {
                ErrorCodeEnum.ERR_11010.setErrorCode(result);
                result.addParam(new Param("dbpErrMessage",
                        "A company is already enrolled with MembershipId and Taxid. Please check & try again",
                        DBPUtilitiesConstants.STRING_TYPE));

                return result;
            }

            return postProcess(inputParams, result);
        }

        return result;

    }

    public String getAccessToken(DataControllerRequest dcRequest) throws HttpCallException {
        return AdminUtil.getAdminToken(dcRequest);
    }

    private Result postProcess(Map<String, String> inputParams, Result result) throws Exception {
        Result retResult = new Result();
        if (HelperMethods.hasRecords(result)) {
            HelperMethods.setSuccessMsg(DBPUtilitiesConstants.SUCCESS_MSG, retResult);
            retResult.addParam(new Param("id", inputParams.get("id"), "String"));
            retResult.addParam(new Param("FirstName", inputParams.get("FirstName"), "String"));
            retResult.addParam(new Param("LastName", inputParams.get("LastName"), "String"));
            retResult.addParam(new Param("Email", inputParams.get("Email"), "String"));

        } else {
            ErrorCodeEnum.ERR_11009.setErrorCode(retResult);
            retResult.addParam(
                    new Param("dbpErrMessage", "Company Enrollment is Failed", DBPUtilitiesConstants.STRING_TYPE));
        }
        return retResult;
    }

    private Result CreateOrganisationOwner(Map<String, String> inputParams, DataControllerRequest dcRequest)
            throws HttpCallException {
        Result result = new Result();
        String userId = inputParams.get("userId");
        String orgId = inputParams.get("id");
        String filter = "id" + DBPUtilitiesConstants.EQUAL + userId;
        Result customerResult = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.CUSTOMER_GET);

        Map<String, String> input = new HashMap<>();
        input.put("FirstName", HelperMethods.getFieldValue(customerResult, "FirstName"));
        input.put("MidleName", HelperMethods.getFieldValue(customerResult, "MiddleName"));
        input.put("LastName", HelperMethods.getFieldValue(customerResult, "LastName"));
        input.put("DateOfBirth", HelperMethods.getFieldValue(customerResult, "DateOfBirth"));
        input.put("Ssn", HelperMethods.getFieldValue(customerResult, "Ssn"));
        input.put("Organization_id", orgId);

        filter = "";
        filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + userId;
        Result customerCommResult = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.CUSTOMER_COMMUNICATION_GET);
        if (HelperMethods.hasRecords(customerCommResult)) {
            List<Record> customerCommRecords = customerCommResult.getAllDatasets().get(0).getAllRecords();
            for (Record record : customerCommRecords) {
                String type = record.getParam("Type_id").getValue();
                String value = record.getParam("Value").getValue();
                if (DBPUtilitiesConstants.COMM_TYPE_EMAIL.equalsIgnoreCase(type)) {
                    input.put("Email", value);
                }
                if (DBPUtilitiesConstants.COMM_TYPE_PHONE.equalsIgnoreCase(type)) {
                    input.put("Phone", value);
                }
            }

        }

        result = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.ORGANISATIONOWNER_CREATE);

        inputParams.put("FirstName", HelperMethods.getFieldValue(customerResult, "FirstName"));
        inputParams.put("MidleName", HelperMethods.getFieldValue(customerResult, "MiddleName"));
        inputParams.put("LastName", HelperMethods.getFieldValue(customerResult, "LastName"));
        inputParams.put("Email", input.get("Email"));
        return result;
    }

    private Result createOrganisationEmployee(Map<String, String> inputParams, DataControllerRequest dcRequest)
            throws HttpCallException {

        Map<String, String> input = new HashMap<>();
        Result result;
        input.put("id", HelperMethods.getNumericId() + "");
        input.put("Customer_id", inputParams.get("userId"));
        input.put("Organization_id", inputParams.get("id"));
        input.put("Is_Owner", "1");
        input.put("Is_Admin", "1");
        result = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.ORGANISATIONEMPLOYEE_CREATE);

        return result;
    }

    private Result CreateOrganisationAccounts(Map<String, String> inputParams, DataControllerRequest dcRequest)
            throws HttpCallException {

        String taxid = inputParams.get("Taxid");
        String membershipid = inputParams.get("Membership_id");
        String userId = inputParams.get("userId");
        String orgId = inputParams.get("id");
        String filter = "User_id" + DBPUtilitiesConstants.EQUAL + userId;
        Result result = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.ACCOUNT_GET);
        if (HelperMethods.hasRecords(result)) {
            List<Record> accounts = result.getAllDatasets().get(0).getAllRecords();
            for (Record record : accounts) {

                String accountNum = record.getParam("Account_id").getValue();
                String accountName = record.getParam("AccountName").getValue();
                Map<String, String> input = new HashMap<>();
                input = new HashMap<>();
                input.put("id", HelperMethods.getNumericId() + "");
                input.put("Membership_id", membershipid);
                input.put("Account_id", accountNum);
                input.put("accountName", accountName);
                input.put("Taxid", taxid);
                HelperMethods.removeNullValues(input);
                HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                        URLConstants.MEMBERSHIP_ACCOUNTS_CREATE);

                input = new HashMap<>();
                input.put("id", HelperMethods.getNumericId() + "");
                input.put("Account_id", accountNum);
                input.put("AccountName", accountName);
                input.put("IsOrganizationAccount", "true");
                input.put("IsOrgAccountUnLinked", "false");
                input.put("Organization_id", orgId);
                HelperMethods.removeNullValues(input);
                HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                        URLConstants.CUSTOMERACCOUNTS_CREATE);

                input = new HashMap<>();
                input.put("id", HelperMethods.getNumericId() + "");
                input.put("Customer_id", userId);
                input.put("Account_id", accountNum);
                input.put("AccountName", accountName);
                input.put("IsOrganizationAccount", "false");
                input.put("IsOrgAccountUnLinked", "false");
                HelperMethods.removeNullValues(input);
                HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                        URLConstants.CUSTOMERACCOUNTS_CREATE);

            }

        }
        return result;
    }

    private Result updateOrgIdInCustomer(Map<String, String> inputParams, String userId,
            DataControllerRequest dcRequest) throws HttpCallException {
        Map<String, String> input = new HashMap<>();
        Result result = new Result();
        input.put("id", userId);
        input.put("Organization_Id", inputParams.get("id"));
        result = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.CUSTOMER_UPDATE);
        return result;
    }

    private Result createOrganization(Map<String, String> inputParams, DataControllerRequest dcRequest)
            throws HttpCallException {

        Result result = new Result();

        Map<String, String> input = new HashMap<>();
        if (StringUtils.isNotBlank(dcRequest.getParameter("Name"))) {
            input.put("Name", dcRequest.getParameter("Name"));
            input.put("Type_Id", "TYPE_ID_MICRO_BUSINESS");
            if (StringUtils.isNotBlank(dcRequest.getParameter("Description"))) {
                input.put("Description", dcRequest.getParameter("Description"));
            }
            result = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                    URLConstants.ORGANISATION_CREATE);
        }

        return result;
    }

    private boolean isOrgExists(DataControllerRequest dcRequest) throws HttpCallException {

        String string = dcRequest.getParameter("Membership");

        if (StringUtils.isNotBlank(string)) {
            HashMap<String, String> hashMap = HelperMethods.getAllRecordsMap(string).get(0);
            String membership_id = hashMap.get("Membership_id");
            String taxid = hashMap.get("Taxid");

            String filter = "";
            if (StringUtils.isNotBlank(membership_id)) {
                filter += "Membership_id" + DBPUtilitiesConstants.EQUAL + membership_id;
            }

            if (StringUtils.isNotBlank(taxid)) {
                if (!filter.isEmpty()) {
                    filter += DBPUtilitiesConstants.AND;
                }
                filter += "Taxid" + DBPUtilitiesConstants.EQUAL + taxid;
            }

            if (!filter.isEmpty()) {
                Result result = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                        URLConstants.ORGANISATIONMEMBERSHIP_GET);

                if (HelperMethods.hasRecords(result)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean preProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result)
            throws HttpCallException {

        Map<String, String> loggedInUserInfo = HelperMethods.getCustomerFromIdentityService(dcRequest);
        if (!HelperMethods.isAuthenticationCheckRequiredForService(loggedInUserInfo)) {
            return true;
        } else {
            ErrorCodeEnum.ERR_11007.setErrorCode(result);
            return false;
        }
    }

    private String updateCustomerType(DataControllerRequest dcRequest, Result result) throws HttpCallException {
        Map<String, String> input = new HashMap<>();
        String userName = dcRequest.getParameter("UserName");
        String userId = dcRequest.getParameter("userId");
        if (StringUtils.isNotBlank(userId)) {
            input.put("id", userId);
            input.put("CustomerType_id", "TYPE_ID_MICRO_BUSINESS");
            HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.CUSTOMER_UPDATE);
            return userId;
        }
        if (StringUtils.isNotBlank(userName)) {
            Result user = new Result();
            String filter = "";
            filter = filter + "UserName" + DBPUtilitiesConstants.EQUAL + "'" + userName + "'";
            user = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                    URLConstants.CUSTOMER_GET);
            if (HelperMethods.hasRecords(user)) {
                userId = HelperMethods.getFieldValue(user, "id");
                input.put("id", userId);
                input.put("CustomerType_id", "TYPE_ID_MICRO_BUSINESS");
                HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                        URLConstants.CUSTOMER_UPDATE);
                return userId;
            }
            HelperMethods.setValidationMsgwithCode("Please provide valid username", ErrorCodes.ERROR_CREATING_RECORD,
                    result);
        } else {
            HelperMethods.setValidationMsgwithCode("Please provide username", ErrorCodes.ERROR_CREATING_RECORD, result);
        }
        return null;

    }

}
