/**
 * 
 */
package com.temenos.dbx.product.combinedaccess.backenddelegate.impl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.temenos.dbx.product.constants.Constants;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.customersecurityservices.PasswordHistoryManagement;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.kms.KMSUtil;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.URLFinder;
import com.kony.objectserviceutils.EventsDispatcher;
import com.konylabs.middleware.api.OperationData;
import com.konylabs.middleware.api.ServiceRequest;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.combinedaccess.backenddelegate.api.UserLinkingBackendDelegate;
import com.temenos.dbx.product.dto.CredentialCheckerDTO;
import com.temenos.dbx.product.dto.CustomerDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.usermanagement.backenddelegate.api.ProfileManagementBackendDelegate;
import com.temenos.dbx.product.usermanagement.businessdelegate.api.CredentialCheckerBusinessDelegate;
import com.temenos.dbx.product.utils.ThreadExecutor;

/**
 * @author muthukumarv
 *
 */
public class UserLinkingBackendDelegateImpl implements UserLinkingBackendDelegate {
    // LoggerUtil logger = new LoggerUtil(UserLinkingBackendDelegateImpl.class);
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    static private List<String> delinkingAccIdList = new ArrayList<String>();
    static private String newUserId, firstName, lastName, email;
    static private Map<String, String> customParams = new HashMap<>();

    public Result userLinkingOperation(Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse)
            throws ApplicationException, JSONException, UnsupportedEncodingException {

        Result result = new Result();
        customParams.clear();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        String otherUser = inputParams.get("otherUser");
        String combinedUser = inputParams.get("combinedUser");
        String combinedProfile = null;
        if (StringUtils.isBlank(combinedUser) || StringUtils.isBlank(otherUser)) {
            alert.prepareError("Error:Invalid input").log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10197);
        }
        // checking whether user is already combinedUser
        if (!isCombinedUser(combinedUser, dcRequest)) {
            if (validateInput(combinedUser, otherUser, dcRequest)) {
                if (!isBusinessUser(combinedUser, dcRequest) && isBusinessUser(otherUser, dcRequest)) {
                    combinedProfile = "personal";
                    if (!isSignatoryUser(otherUser, dcRequest)) {
                        alert.prepareError("Error: The business user is not a signatory").log();
                        throw new ApplicationException(ErrorCodeEnum.ERR_21124);
                    }
                    result = businessToRetailLinking(combinedUser, otherUser, dcRequest);
                } else if (isBusinessUser(combinedUser, dcRequest) && !isBusinessUser(otherUser, dcRequest)) {
                    combinedProfile = "business";
                    if (!isSignatoryUser(combinedUser, dcRequest)) {
                        alert.prepareError("Error: The business user is not a signatory").log();
                        throw new ApplicationException(ErrorCodeEnum.ERR_21124);
                    }
                    result = retailToBusinessLinking(otherUser, combinedUser, dcRequest);
                } else {
                    alert.prepareError("Error:Customer id provided is invalid").log();
                    throw new ApplicationException(ErrorCodeEnum.ERR_10197);
                }
            } else {
                alert.prepareError("Error: the basic details of the users are not matching").log();
                throw new ApplicationException(ErrorCodeEnum.ERR_21123);
            }
        } else {
            alert.prepareError("Error: User is already a combined user").log();
            throw new ApplicationException(ErrorCodeEnum.ERR_21115);
        }

        Map<String, Object> input = new HashMap<>();
        input.put("newCustomerId", combinedUser);
        input.put("deactivatedCustomerId", otherUser);
        OperationData operationData;
        try {
            operationData = dcRequest.getServicesManager().getOperationDataBuilder()
                    .withServiceId("CombinedAccessAlertServices").withVersion("1.0")
                    .withOperationId("activateCombinedAccess").build();
            ServiceRequest serviceRequest = dcRequest.getServicesManager().getRequestBuilder(operationData)
                    .withInputs(input).withHeaders(dcRequest.getHeaderMap()).build();
            Result result1 = serviceRequest.invokeServiceAndGetResult();
        } catch (Exception e) {
            alert.prepareError("Exception occured while triggering alerts for combined access from backend delegate :"
                    + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_21125);
        }

        JsonObject customParamsObj = new JsonObject();
        customParamsObj.addProperty("combinedProfile", combinedProfile);
        try {
            ThreadExecutor.getExecutor(dcRequest)
                    .execute(() -> EventsDispatcher.dispatch(dcRequest, dcResponse, "COMBINED_ACCESS", "USER_LINKED",
                            null, "SID_EVENT_SUCCESS", null, combinedUser, "RETAIL_AND_BUSINESS_BANKING",
                            customParamsObj));
        } catch (InterruptedException e) {
            alert.prepareError("Exception while triggering USER_LINKED alert" + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_21126);
        }
        deActivatingOtherUser(combinedUser, otherUser, dcRequest);
        customParamsObj.remove(combinedProfile);
        customParamsObj.addProperty("externalphone", customParams.get("externalphone"));
        customParamsObj.addProperty("externalemail", customParams.get("externalemail"));
        try {
            ThreadExecutor.getExecutor(dcRequest)
                    .execute(() -> EventsDispatcher.dispatch(dcRequest, dcResponse, "COMBINED_ACCESS",
                            "USER_DEACTIVATED", null, "SID_EVENT_SUCCESS", null, otherUser,
                            "RETAIL_AND_BUSINESS_BANKING", customParamsObj));
        } catch (InterruptedException e) {
            alert.prepareError("Exception while triggering USER_DEACTIVATED alert" + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_21126);
        }
        return result;
    }

    private boolean isSignatoryUser(String customerId, DataControllerRequest dcRequest) throws ApplicationException {
        Result result = new Result();
        try {
            String filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + customerId;
            result = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                    URLConstants.ORGANISATIONEMPLOYEE_GET);
            if (HelperMethods.hasRecords(result)) {
                Dataset customer = result.getAllDatasets().get(0);
                for (Record record : customer.getAllRecords()) {
                    boolean isSignatory = Boolean.parseBoolean(HelperMethods.getFieldValue(record, "isAuthSignatory"));
                    if (isSignatory) {
                        return true;
                    }
                }
            }

        } catch (Exception e) {
            alert.prepareError("Exception occured while fetching the organisation employees from backend delegate :"
                    + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_21127);
        }
        return false;
    }

    private boolean validateInput(String combinedUser, String otherUser, DataControllerRequest dcRequest)
            throws ApplicationException {
        Result combinedResult = new Result();
        Result otherResult = new Result();
        String lastName = null;
        String dob = null;
        String ssn = null;
        String otherLastName = null;
        String otherDob = null;
        String otherSsn = null;
        try {
            String filter = "id" + DBPUtilitiesConstants.EQUAL + combinedUser;
            combinedResult = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                    URLConstants.CUSTOMERVERIFY_GET);
            String filter1 = "id" + DBPUtilitiesConstants.EQUAL + otherUser;
            otherResult = HelperMethods.callGetApi(dcRequest, filter1, HelperMethods.getHeaders(dcRequest),
                    URLConstants.CUSTOMERVERIFY_GET);
            if (HelperMethods.hasRecords(combinedResult) && HelperMethods.hasRecords(otherResult)) {
                Dataset customers = combinedResult.getAllDatasets().get(0);
                for (Record record : customers.getAllRecords()) {
                    lastName = HelperMethods.getFieldValue(record, "LastName");
                    dob = HelperMethods.getFieldValue(record, "DateOfBirth");
                    ssn = HelperMethods.getFieldValue(record, "Ssn");
                }
                Dataset otherCustomers = otherResult.getAllDatasets().get(0);
                for (Record record : otherCustomers.getAllRecords()) {
                    otherLastName = HelperMethods.getFieldValue(record, "LastName");
                    otherDob = HelperMethods.getFieldValue(record, "DateOfBirth");
                    otherSsn = HelperMethods.getFieldValue(record, "Ssn");
                }

                // validating the customer details
                if (StringUtils.isNotBlank(lastName) && StringUtils.isNotBlank(otherLastName)
                        && StringUtils.isNotBlank(dob) && StringUtils.isNotBlank(otherDob)
                        && StringUtils.isNotBlank(ssn) && StringUtils.isNotBlank(otherSsn)) {
                    if (((lastName.equalsIgnoreCase(otherLastName)) && (dob.equals(otherDob))
                            && (ssn.equals(otherSsn)))) {
                        return true;
                    }

                }
            }

        } catch (Exception e) {
            alert.prepareError("Exception occured while validating user details from backend delegate :" + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10020);
        }
        return false;
    }

    private void deActivatingOtherUser(String combinedUser, String otherUser, DataControllerRequest dcRequest)
            throws ApplicationException {
        Map<String, String> input = new HashMap<>();
        try {
            input.put("id", otherUser);
            input.put("combinedUserId", combinedUser);
            input.put("Status_id", "SID_CUS_INACTIVE");
            HelperMethods.removeNullValues(input);
            HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.CUSTOMER_UPDATE);

        } catch (Exception e) {
            alert.prepareError("Exception occured while inactivating other user from backend delegate :" + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10717);
        }

    }

    private Result businessToRetailLinking(String retailUserId, String businessUserId, DataControllerRequest dcRequest)
            throws ApplicationException {
        Result result = new Result();
        DBXResult partyDBXResult = new DBXResult();
        JsonObject retailObject = new JsonObject();
        JsonArray addressArray = new JsonArray();
        JsonArray commArray = new JsonArray();
        JsonArray emailArray = new JsonArray();
        // fetching retail customer details from MS-Party
        partyDBXResult = getUserDetailFromParty(retailUserId, dcRequest);
        if (partyDBXResult.getResponse() != null) {
            retailObject = (JsonObject) partyDBXResult.getResponse();
            if ((!retailObject.isJsonNull()) && (retailObject.get("customer").getAsJsonArray().size() > 0)) {
                commArray = (retailObject.get("customer").getAsJsonArray().get(0).getAsJsonObject()
                        .has("ContactNumbers"))
                                ? retailObject.get("customer").getAsJsonArray().get(0).getAsJsonObject()
                                        .get("ContactNumbers").getAsJsonArray()
                                : commArray;
                addressArray = (retailObject.get("customer").getAsJsonArray().get(0).getAsJsonObject().has("Addresses"))
                        ? retailObject.get("customer").getAsJsonArray().get(0).getAsJsonObject().get("Addresses")
                                .getAsJsonArray()
                        : addressArray;
                emailArray = (retailObject.get("customer").getAsJsonArray().get(0).getAsJsonObject().has("EmailIds"))
                        ? retailObject.get("customer").getAsJsonArray().get(0).getAsJsonObject().get("EmailIds")
                                .getAsJsonArray()
                        : emailArray;
                commArray.addAll(emailArray);
            }
        }

        updateCustomerBasicInfo(retailUserId, businessUserId, dcRequest, retailObject);
        updateCustomerAddress(retailUserId, businessUserId, dcRequest, addressArray);
        updateCustomerCommunication(retailUserId, businessUserId, dcRequest, commArray);
        updateCustomerAccounts(retailUserId, businessUserId, dcRequest);
        updateCustomerCards(retailUserId, businessUserId, dcRequest);
        updateCustomerGroups(businessUserId, retailUserId, dcRequest);
        // update other customer related tables via stored procedure
        updateOtherRecords(retailUserId, businessUserId, dcRequest, "false");
        updateBackendIdentifier(retailUserId, businessUserId, dcRequest);
        updateCustomerBusinessType(retailUserId, businessUserId, dcRequest);
        updateOrgEmployees(retailUserId, businessUserId, dcRequest);
        updateCustomerApproval(retailUserId, businessUserId, dcRequest);
        result.addParam("success", "true");

        return result;
    }

    private void updateCustomerApproval(String retailUserId, String businessUserId, DataControllerRequest dcRequest)
            throws ApplicationException {
        Result result = new Result();
        try {
            // getting communication details for business User
            result = getCustomerApprovalForCustId(businessUserId, dcRequest);

            if (HelperMethods.hasRecords(result)) {
                // Switching businessUser records to RetailUser(combined) records
                List<Record> existingRecords = result.getAllDatasets().get(0).getAllRecords();
                for (Record record : existingRecords) {
                    String customerComm_id = HelperMethods.getFieldValue(record, DBPUtilitiesConstants.C_ID);
                    Map<String, String> input = new HashMap<>();
                    input.put("customerId", retailUserId);
                    input.put("id", customerComm_id);
                    HelperMethods.removeNullValues(input);
                    HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                            URLConstants.CUSTOMERAPPROVALMATRIX_UPDATE);
                }
            }
        } catch (Exception e) {
            alert.prepareError("Exception occured while updating the Customer Approval matrix details from backend delegate :"
                    + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_21001);
        }

    }

    private Result getCustomerApprovalForCustId(String businessUserId, DataControllerRequest dcRequest)
            throws ApplicationException {
        Map<String, String> inputParams = new HashMap<>();
        String filter = "customerId" + DBPUtilitiesConstants.EQUAL + businessUserId;
        inputParams.put(DBPUtilitiesConstants.FILTER, filter);
        try {
            return HelperMethods.callGetApi(dcRequest, inputParams.get(DBPUtilitiesConstants.FILTER),
                    HelperMethods.getHeaders(dcRequest), URLConstants.CUSTOMERAPPROVALMATRIX_GET);
        } catch (Exception e) {
            alert.prepareError(
                    "Exception occured while fetching the Customer Approval matrix from backend delegate :"
                            + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_21001);
        }
    }

    private void updateOrgEmployees(String retailUserId, String businessUserId, DataControllerRequest dcRequest)
            throws ApplicationException {
        Result result = new Result();
        try {
            // getting communication details for business User
            result = getOrgEmployeesForCustId(businessUserId, dcRequest);

            if (HelperMethods.hasRecords(result)) {
                // Switching businessUser records to RetailUser(combined) records
                List<Record> existingRecords = result.getAllDatasets().get(0).getAllRecords();
                for (Record record : existingRecords) {
                    String customerComm_id = HelperMethods.getFieldValue(record, DBPUtilitiesConstants.C_ID);
                    Map<String, String> input = new HashMap<>();
                    input.put("Customer_id", retailUserId);
                    input.put("id", customerComm_id);
                    HelperMethods.removeNullValues(input);
                    HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                            URLConstants.ORGANISATIONEMPLOYEE_UPDATE);
                }
            }
        } catch (Exception e) {
            alert.prepareError("Exception occured while updating the organisation employee details from backend delegate :"
                    + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_21128);
        }

    }

    private Result getOrgEmployeesForCustId(String businessUserId, DataControllerRequest dcRequest)
            throws ApplicationException {
        Map<String, String> inputParams = new HashMap<>();
        String filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + businessUserId;
        inputParams.put(DBPUtilitiesConstants.FILTER, filter);
        try {
            return HelperMethods.callGetApi(dcRequest, inputParams.get(DBPUtilitiesConstants.FILTER),
                    HelperMethods.getHeaders(dcRequest), URLConstants.ORGANISATIONEMPLOYEE_GET);
        } catch (Exception e) {
            alert.prepareError(
                    "Exception occured while fetching the organisation employees from backend delegate :"
                            + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_21127);
        }
    }

    private void updateCustomerBusinessType(String combinedUserid, String businessUserId,
            DataControllerRequest dcRequest) throws ApplicationException {
        Map<String, String> inputParams = new HashMap<>();
        Result result = new Result();
        String businessTypeId = null;
        String signatoryTypeId = null;
        String filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + businessUserId;
        inputParams.put(DBPUtilitiesConstants.FILTER, filter);
        try {
            result = HelperMethods.callGetApi(dcRequest, inputParams.get(DBPUtilitiesConstants.FILTER),
                    HelperMethods.getHeaders(dcRequest), URLConstants.CUSTOMERBUSINESSTYPE_GET);

            Map<String, String> inputparams = new HashMap<>();
            if (HelperMethods.hasRecords(result)) {
                inputparams.put("Customer_id", businessUserId);
                inputparams.put("BusinessType_id", HelperMethods.getFieldValue(result, "BusinessType_id"));
                inputparams.put("SignatoryType_id", HelperMethods.getFieldValue(result, "SignatoryType_id"));
                businessTypeId = HelperMethods.getFieldValue(result, "BusinessType_id");
                signatoryTypeId = HelperMethods.getFieldValue(result, "SignatoryType_id");
                HelperMethods.callApi(dcRequest, inputparams, HelperMethods.getHeaders(dcRequest),
                        URLConstants.CUSTOMERBUSINESSTYPE_DELETE);

                Map<String, String> input = new HashMap<>();
                input.put("Customer_id", combinedUserid);
                input.put("BusinessType_id", businessTypeId);
                input.put("SignatoryType_id", signatoryTypeId);
                HelperMethods.removeNullValues(input);
                HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                        URLConstants.CUSTOMERBUSINESSTYPE_CREATE);
            }

        } catch (Exception e) {
            alert.prepareError("Exception occured while deleting the customerBusinessType information from backend delegate :"
                    + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10228);
        }
    }

    private void updateBackendIdentifier(String retailUserId, String businessUserId, DataControllerRequest dcRequest)
            throws ApplicationException {
        Result result = new Result();
        try {
            // Getting the backendidentifier details of BusinessUser
            result = getBackendIdentifierForCustId(businessUserId, dcRequest);
            if (HelperMethods.hasRecords(result)) {

                // Switching businessUser records to RetailUser(combined) records
                List<Record> existingRecords = result.getAllDatasets().get(0).getAllRecords();
                for (Record record : existingRecords) {
                    String backendId = HelperMethods.getFieldValue(record, "id");
                    Map<String, String> input = new HashMap<>();
                    input.put("Customer_id", retailUserId);
                    input.put("isTypeBusiness", "1");
                    input.put("id", backendId);
                    HelperMethods.removeNullValues(input);
                    HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                            URLConstants.BACKENDIDENTIFIER_UPDATE);
                }
            }
        } catch (HttpCallException e) {
            alert.prepareError(
                    "Exception occured while updating the customer ID in backend identifier from backend delegate :"
                            + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_21130);
        }

    }

    private Result getBackendIdentifierForCustId(String customerId, DataControllerRequest dcRequest)
            throws ApplicationException {
        Map<String, String> inputParams = new HashMap<>();
        String filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + customerId;
        inputParams.put(DBPUtilitiesConstants.FILTER, filter);
        try {
            return HelperMethods.callGetApi(dcRequest, inputParams.get(DBPUtilitiesConstants.FILTER),
                    HelperMethods.getHeaders(dcRequest), URLConstants.BACKENDIDENTIFIER_GET);
        } catch (Exception e) {
            alert.prepareError(
                    "Exception occured while fetching the backend identifier from backend delegate :" + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_21129);
        }
    }

    private DBXResult getUserDetailFromParty(String userId, DataControllerRequest dcRequest)
            throws ApplicationException {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(userId);
        try {
            ProfileManagementBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl
                    .getBackendDelegate(ProfileManagementBackendDelegate.class);
            return backendDelegate.getUserResponse(customerDTO, dcRequest.getHeaderMap());
        } catch (Exception e) {
            alert.prepareError("Exception occured while fetching party details from backend delegate :" + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_21114);
        }

    }

    private void updateCustomerCards(String retailUserId, String businessUserId, DataControllerRequest dcRequest)
            throws ApplicationException {
        Result result = new Result();
        try {
            // getting communication details for business User
            result = getCardsForCustId(businessUserId, dcRequest);

            if (HelperMethods.hasRecords(result)) {
                // Switching businessUser records to RetailUser(combined) records
                List<Record> existingRecords = result.getAllDatasets().get(0).getAllRecords();
                for (Record record : existingRecords) {
                    String customerComm_id = HelperMethods.getFieldValue(record, DBPUtilitiesConstants.U_ID);
                    Map<String, String> input = new HashMap<>();
                    input.put("User_id", retailUserId);
                    input.put("isTypeBusiness", "1");
                    input.put("Id", customerComm_id);
                    HelperMethods.removeNullValues(input);
                    HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                            URLConstants.CARD_UPDATE);
                }
            }
        } catch (Exception e) {
            alert.prepareError("Exception occured while updating the card details from backend delegate :" + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_21118);
        }

    }

    private void updateCustomerCardsRB(String retailUserId, String businessUserId, DataControllerRequest dcRequest)
            throws ApplicationException {
        Result businessUserResult = new Result();
        Result retailUserResult = new Result();

        try {
            // Getting the card details of BusinessUser
            businessUserResult = getCardsForCustId(businessUserId, dcRequest);

            // update the isTypeBusiness field for all fetched businessUserResult
            updateIsTypeBusinessInCardRecords(businessUserResult, dcRequest);

            // Getting the card details of RetailUser
            retailUserResult = getCardsForCustId(retailUserId, dcRequest);

            if (HelperMethods.hasRecords(retailUserResult)) {

                // Switching RetailUser records to BusinessUser(combined) records
                List<Record> existingRecords = retailUserResult.getAllDatasets().get(0).getAllRecords();
                for (Record record : existingRecords) {
                    String customerCard_id = HelperMethods.getFieldValue(record, DBPUtilitiesConstants.U_ID);
                    Map<String, String> input = new HashMap<>();
                    input.put("User_id", businessUserId);
                    input.put("Id", customerCard_id);
                    HelperMethods.removeNullValues(input);
                    HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                            URLConstants.CARD_UPDATE);
                }
            }

        } catch (Exception e) {
            alert.prepareError("Exception occured while updating the card details from backend delegate :" + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_21118);
        }

    }

    private void updateIsTypeBusinessInCardRecords(Result businessUserResult, DataControllerRequest dcRequest)
            throws ApplicationException {
        try {
            if (HelperMethods.hasRecords(businessUserResult)) {
                // Updating field isTypeBusiness for BusinessUser records
                List<Record> existingRecords = businessUserResult.getAllDatasets().get(0).getAllRecords();
                for (Record record : existingRecords) {
                    String customerCard_id = HelperMethods.getFieldValue(record, DBPUtilitiesConstants.U_ID);
                    Map<String, String> input = new HashMap<>();
                    input.put("Id", customerCard_id);
                    input.put("isTypeBusiness", "1");
                    HelperMethods.removeNullValues(input);
                    HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                            URLConstants.CARD_UPDATE);
                }
            }
        } catch (Exception e) {
            alert.prepareError(
                    "Exception occured while updating the isTypeBusiness in communication table from backend delegate :"
                            + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_21118);
        }

    }

    private void updateCustomerAccounts(String retailUserId, String businessUserId, DataControllerRequest dcRequest)
            throws ApplicationException {
        Result businessResult = new Result();
        Result retailResult = new Result();
        // get customer accounts for custId from customerAccounts table
        businessResult = getCustomerAccounts(businessUserId, dcRequest);
        // get accounts for custId from accounts table
        retailResult = getAccounts(retailUserId, dcRequest);

        if (HelperMethods.hasRecords(businessResult)) {

            // change business to retail custID in customeracc table and update the isOrgAcc
            List<Record> existingRecords = businessResult.getAllDatasets().get(0).getAllRecords();
            for (Record record : existingRecords) {
                String id = HelperMethods.getFieldValue(record, DBPUtilitiesConstants.UN_ID);
                Map<String, String> input = new HashMap<>();
                input.put("Customer_id", retailUserId);
                input.put("id", id);
                input.put("IsOrganizationAccount", "1");
                HelperMethods.removeNullValues(input);
                try {
                    HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                            URLConstants.CUSTOMERACCOUNTS_UPDATE);
                } catch (HttpCallException e) {
                    alert.prepareError(
                            "Exception occured while updating the customer Id in customerAccounts from backend delegate :"
                                    + e.getMessage()).log();
                    throw new ApplicationException(ErrorCodeEnum.ERR_10295);
                }
            }
        }

        if (HelperMethods.hasRecords(retailResult)) {

            // change business to retail custID in customeracc table and update the isOrgAcc
            List<Record> existingRecords = retailResult.getAllDatasets().get(0).getAllRecords();
            for (Record record : existingRecords) {
                Map<String, String> input = new HashMap<>();
                input.put("id", HelperMethods.getNumericId() + "");
                input.put("Customer_id", retailUserId);
                input.put("Account_id", HelperMethods.getFieldValue(record, "Account_id"));
                input.put("AccountName", HelperMethods.getFieldValue(record, "AccountName"));
                input.put("IsOrganizationAccount", HelperMethods.getFieldValue(record, "isBusinessAccount"));
                input.put("Membership_id", HelperMethods.getFieldValue(record, "Membership_id"));
                input.put("FavouriteStatus", HelperMethods.getFieldValue(record, "FavouriteStatus"));
                HelperMethods.removeNullValues(input);
                try {
                    HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                            URLConstants.CUSTOMERACCOUNTS_CREATE);
                } catch (HttpCallException e) {
                    alert.prepareError(
                            "Exception occured while creating the customer Id in customerAccounts from backend delegate :"
                                    + e.getMessage()).log();
                    throw new ApplicationException(ErrorCodeEnum.ERR_10294);
                }
            }
        }

    }

    private void updateCustomerBasicInfo(String retailUserId, String businessUserId, DataControllerRequest dcRequest,
            JsonObject retailJson) throws ApplicationException {
        Map<String, String> input = new HashMap<>();
        JsonObject businessJson = getBasicInfoForCustId(businessUserId, dcRequest);

        if (retailJson.get("customer").getAsJsonArray().size() == 0) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10236);
        }
        try {
            if (!retailJson.isJsonNull() && retailJson.has("customer")
                    && retailJson.get("customer").getAsJsonArray().size() > 0 && !businessJson.isJsonNull()
                    && businessJson.has("customer") && businessJson.get("customer").getAsJsonArray().size() > 0) {
                JsonArray retailCustArray = retailJson.get("customer").getAsJsonArray();
                JsonArray businessCustArray = businessJson.get("customer").getAsJsonArray();
                String orgId = retailCustArray.get(0).getAsJsonObject().get("Organization_Id") == null ? null
                        : retailCustArray.get(0).getAsJsonObject().get("Organization_Id").getAsString();
                String existingOrgId = businessCustArray.get(0).getAsJsonObject().get("Organization_Id") == null ? null
                        : businessCustArray.get(0).getAsJsonObject().get("Organization_Id").getAsString();
                if ((StringUtils.isBlank(orgId) || orgId == null)
                        && (StringUtils.isNotBlank(existingOrgId) && existingOrgId != null)) {
                    input.put("Organization_Id", existingOrgId);
                }
                String orgType = retailCustArray.get(0).getAsJsonObject().get("organizationType") == null ? null
                        : retailCustArray.get(0).getAsJsonObject().get("organizationType").getAsString();
                String existingOrgType = businessCustArray.get(0).getAsJsonObject().get("organizationType") == null
                        ? null
                        : businessCustArray.get(0).getAsJsonObject().get("organizationType").getAsString();
                if ((StringUtils.isBlank(orgType) || orgType == null)
                        && (StringUtils.isNotBlank(existingOrgType) && existingOrgType != null)) {
                    input.put("organizationType", existingOrgType);
                }
                String license = retailCustArray.get(0).getAsJsonObject().get("DrivingLicenseNumber") == null ? null
                        : retailCustArray.get(0).getAsJsonObject().get("DrivingLicenseNumber").getAsString();
                String existingLicense = businessCustArray.get(0).getAsJsonObject().get("DrivingLicenseNumber") == null
                        ? null
                        : businessCustArray.get(0).getAsJsonObject().get("DrivingLicenseNumber").getAsString();
                if ((StringUtils.isBlank(license) || license == null)
                        && (StringUtils.isNotBlank(existingLicense) && existingLicense != null)) {
                    input.put("DrivingLicenseNumber", existingLicense);
                }
                String company = retailCustArray.get(0).getAsJsonObject().get("UserCompany") == null ? null
                        : retailCustArray.get(0).getAsJsonObject().get("UserCompany").getAsString();
                String existingCompany = businessCustArray.get(0).getAsJsonObject().get("UserCompany") == null ? null
                        : businessCustArray.get(0).getAsJsonObject().get("UserCompany").getAsString();
                if ((StringUtils.isBlank(company) || company == null)
                        && (StringUtils.isNotBlank(existingCompany) && existingCompany != null)) {
                    input.put("UserCompany", existingCompany);
                }
                String eagreement = retailCustArray.get(0).getAsJsonObject().get("isEagreementSigned") == null ? null
                        : retailCustArray.get(0).getAsJsonObject().get("isEagreementSigned").getAsString();
                String existingEagreement = businessCustArray.get(0).getAsJsonObject().get("isEagreementSigned") == null
                        ? null
                        : businessCustArray.get(0).getAsJsonObject().get("isEagreementSigned").getAsString();
                if ((StringUtils.isBlank(eagreement) || eagreement == null)
                        && (StringUtils.isNotBlank(existingEagreement) && existingEagreement != null)) {
                    input.put("isEagreementSigned", existingEagreement);
                }
                input.put("isCombinedUser", "1");
                input.put("id", retailUserId);
                input.put("CustomerType_id", "TYPE_ID_BUSINESS");
                HelperMethods.removeNullValues(input);
                HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                        URLConstants.CUSTOMER_UPDATE);
            }
        } catch (Exception e) {
            alert.prepareError("Exception occured while updating basicInfo for combinedUser from backend delegate :"
                    + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10717);
        }
    }

    private void updateCustomerBasicInfoRB(String retailUserId, String businessUserId, DataControllerRequest dcRequest,
            JsonObject retailJson) throws ApplicationException {
        Map<String, String> input = new HashMap<>();
        JsonObject businessJson = getBasicInfoForCustId(businessUserId, dcRequest);
        try {
            if (!retailJson.isJsonNull() && retailJson.has("customer")
                    && retailJson.get("customer").getAsJsonArray().size() > 0 && !businessJson.isJsonNull()
                    && businessJson.has("customer") && businessJson.get("customer").getAsJsonArray().size() > 0) {
                JsonArray retailCustArray = retailJson.get("customer").getAsJsonArray();
                JsonArray businessCustArray = businessJson.get("customer").getAsJsonArray();
                String contactMethod = businessCustArray.get(0).getAsJsonObject().get("PreferredContactMethod") == null
                        ? null
                        : businessCustArray.get(0).getAsJsonObject().get("PreferredContactMethod").getAsString();
                String existingcontactMethod = retailCustArray.get(0).getAsJsonObject()
                        .get("PreferredContactMethod") == null ? null
                                : retailCustArray.get(0).getAsJsonObject().get("PreferredContactMethod").getAsString();
                if ((StringUtils.isBlank(contactMethod) || contactMethod == null)
                        && (StringUtils.isNotBlank(existingcontactMethod) && existingcontactMethod != null)) {
                    input.put("PreferredContactMethod", existingcontactMethod);
                }
                String contactTime = businessCustArray.get(0).getAsJsonObject().get("PreferredContactTime") == null
                        ? null
                        : businessCustArray.get(0).getAsJsonObject().get("PreferredContactTime").getAsString();
                String existingContactTime = retailCustArray.get(0).getAsJsonObject()
                        .get("PreferredContactTime") == null ? null
                                : retailCustArray.get(0).getAsJsonObject().get("PreferredContactTime").getAsString();
                if ((StringUtils.isBlank(contactTime) || contactTime == null)
                        && (StringUtils.isNotBlank(existingContactTime) && existingContactTime != null)) {
                    input.put("PreferredContactTime", existingContactTime);
                }
                String maritalStatus = businessCustArray.get(0).getAsJsonObject().get("MaritalStatus") == null ? null
                        : businessCustArray.get(0).getAsJsonObject().get("MaritalStatus").getAsString();
                String existingMaritalStatus = retailCustArray.get(0).getAsJsonObject().get("MaritalStatus_id") == null
                        ? null
                        : retailCustArray.get(0).getAsJsonObject().get("MaritalStatus_id").getAsString();
                if ((StringUtils.isBlank(maritalStatus) || maritalStatus == null)
                        && (StringUtils.isNotBlank(existingMaritalStatus) && existingMaritalStatus != null)) {
                    input.put("MaritalStatus", existingMaritalStatus);
                }
                String spouse = businessCustArray.get(0).getAsJsonObject().get("SpouseName") == null ? null
                        : businessCustArray.get(0).getAsJsonObject().get("SpouseName").getAsString();
                String existingSpouse = retailCustArray.get(0).getAsJsonObject().get("SpouseName") == null ? null
                        : retailCustArray.get(0).getAsJsonObject().get("SpouseName").getAsString();
                if ((StringUtils.isBlank(spouse) || spouse == null)
                        && (StringUtils.isNotBlank(existingSpouse) && existingSpouse != null)) {
                    input.put("SpouseName", existingSpouse);
                }
                String dependants = businessCustArray.get(0).getAsJsonObject().get("NoOfDependents") == null ? null
                        : businessCustArray.get(0).getAsJsonObject().get("NoOfDependents").getAsString();
                String existingDependants = retailCustArray.get(0).getAsJsonObject().get("NoOfDependents") == null
                        ? null
                        : retailCustArray.get(0).getAsJsonObject().get("NoOfDependents").getAsString();
                if ((StringUtils.isBlank(dependants) || dependants == null)
                        && (StringUtils.isNotBlank(existingDependants) && existingDependants != null)) {
                    input.put("NoOfDependents", existingDependants);
                }
                String gender = businessCustArray.get(0).getAsJsonObject().get("Gender") == null ? null
                        : businessCustArray.get(0).getAsJsonObject().get("Gender").getAsString();
                String existingGender = retailCustArray.get(0).getAsJsonObject().get("Gender") == null ? null
                        : retailCustArray.get(0).getAsJsonObject().get("Gender").getAsString();
                if ((StringUtils.isBlank(gender) || gender == null)
                        && (StringUtils.isNotBlank(existingGender) && existingGender != null)) {
                    input.put("Gender", existingGender);
                }
                String employmentId = businessCustArray.get(0).getAsJsonObject().get("EmployementStatus_id") == null
                        ? null
                        : businessCustArray.get(0).getAsJsonObject().get("EmployementStatus_id").getAsString();
                String existingEmploymentId = retailCustArray.get(0).getAsJsonObject()
                        .get("EmployementStatus_id") == null ? null
                                : retailCustArray.get(0).getAsJsonObject().get("EmployementStatus_id").getAsString();
                if ((StringUtils.isBlank(employmentId) || employmentId == null)
                        && (StringUtils.isNotBlank(existingEmploymentId) && existingEmploymentId != null)) {
                    input.put("EmployementStatus_id", existingEmploymentId);
                }
                String employmentInfo = businessCustArray.get(0).getAsJsonObject().get("EmploymentInfo") == null ? null
                        : businessCustArray.get(0).getAsJsonObject().get("EmploymentInfo").getAsString();
                String existingEmploymentInfo = retailCustArray.get(0).getAsJsonObject().get("EmploymentInfo") == null
                        ? null
                        : retailCustArray.get(0).getAsJsonObject().get("EmploymentInfo").getAsString();
                if ((StringUtils.isBlank(employmentInfo) || employmentInfo == null)
                        && (StringUtils.isNotBlank(existingEmploymentInfo) && existingEmploymentInfo != null)) {
                    input.put("EmploymentInfo", existingEmploymentInfo);
                }
                input.put("isCombinedUser", "1");
                input.put("id", businessUserId);
                HelperMethods.removeNullValues(input);
                HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                        URLConstants.CUSTOMER_UPDATE);
            }
        } catch (Exception e) {
            alert.prepareError("Exception occured while updating basicInfo for combinedUser from backend delegate :"
                    + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10717);
        }
    }

    private Result retailToBusinessLinking(String retailUserId, String businessUserId, DataControllerRequest dcRequest)
            throws ApplicationException {
        Result result = new Result();
        DBXResult partyDBXResult = new DBXResult();
        JsonObject retailObject = new JsonObject();
        JsonArray addressArray = new JsonArray();
        JsonArray commArray = new JsonArray();
        JsonArray emailArray = new JsonArray();
        partyDBXResult = getUserDetailFromParty(retailUserId, dcRequest);
        if (partyDBXResult.getResponse() != null) {
            retailObject = (JsonObject) partyDBXResult.getResponse();
            if ((!retailObject.isJsonNull()) && (retailObject.get("customer").getAsJsonArray().size() > 0)) {
                commArray = (retailObject.get("customer").getAsJsonArray().get(0).getAsJsonObject()
                        .has("ContactNumbers"))
                                ? retailObject.get("customer").getAsJsonArray().get(0).getAsJsonObject()
                                        .get("ContactNumbers").getAsJsonArray()
                                : commArray;
                addressArray = (retailObject.get("customer").getAsJsonArray().get(0).getAsJsonObject().has("Addresses"))
                        ? retailObject.get("customer").getAsJsonArray().get(0).getAsJsonObject().get("Addresses")
                                .getAsJsonArray()
                        : addressArray;
                emailArray = (retailObject.get("customer").getAsJsonArray().get(0).getAsJsonObject().has("EmailIds"))
                        ? retailObject.get("customer").getAsJsonArray().get(0).getAsJsonObject().get("EmailIds")
                                .getAsJsonArray()
                        : emailArray;
                commArray.addAll(emailArray);
            }
        }
        updateCustomerBasicInfoRB(retailUserId, businessUserId, dcRequest, retailObject);
        updateCustomerAddressRB(retailUserId, businessUserId, dcRequest, addressArray);

        updateCustomerCommunicationRB(retailUserId, businessUserId, dcRequest, commArray);

        updateCustomerAccountsRB(retailUserId, businessUserId, dcRequest);

        updateCustomerCardsRB(retailUserId, businessUserId, dcRequest);

        updateCustomerGroups(retailUserId, businessUserId, dcRequest);
        // update other customer related tables via stored procedure
        updateOtherRecords(businessUserId, retailUserId, dcRequest, "true");

        updateBackendIdentifierRB(retailUserId, businessUserId, dcRequest);
        result.addParam("success", "true");

        return result;
    }

    private void updateCustomerGroups(String existingId, String newUserid, DataControllerRequest dcRequest)
            throws ApplicationException {
        Map<String, String> inputParams = new HashMap<>();
        Result result = new Result();
        String groupId = null;
        String filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + existingId;
        inputParams.put(DBPUtilitiesConstants.FILTER, filter);
        try {
            result = HelperMethods.callGetApi(dcRequest, inputParams.get(DBPUtilitiesConstants.FILTER),
                    HelperMethods.getHeaders(dcRequest), URLConstants.CUSTOMER_GROUP_GET);

            Map<String, String> inputparams = new HashMap<>();
            if (HelperMethods.hasRecords(result)) {
                inputparams.put("Customer_id", existingId);
                inputparams.put("Group_id", HelperMethods.getFieldValue(result, "Group_id"));
                groupId = HelperMethods.getFieldValue(result, "Group_id");
                HelperMethods.callApiAsync(dcRequest, inputparams, HelperMethods.getHeaders(dcRequest),
                        URLConstants.CUSTOMER_GROUP_DELETE);

                Map<String, String> input = new HashMap<>();
                input.put("Customer_id", newUserid);
                input.put("Group_id", groupId);
                HelperMethods.removeNullValues(input);
                HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                        URLConstants.CUSTOMER_GROUP_CREATE);
            }

        } catch (Exception e) {
            alert.prepareError("Exception occured while deleting the customergroups information from backend delegate :"
                    + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_13528);
        }

    }

    private void updateBackendIdentifierRB(String retailUserId, String businessUserId, DataControllerRequest dcRequest)
            throws ApplicationException {
        Result businessUserResult = new Result();
        Result retailUserResult = new Result();
        try {
            // Getting the backendidentifier details of BusinessUser
            businessUserResult = getBackendIdentifierForCustId(businessUserId, dcRequest);
            // updating the isTypeBusiness field for all fetched businessUserResult
            updateIsTypeBusinessInBackendIdRecords(businessUserResult, dcRequest);
            // Getting the backendidentifier details of RetailUser
            retailUserResult = getBackendIdentifierForCustId(retailUserId, dcRequest);

            if (HelperMethods.hasRecords(retailUserResult)) {
                // Switching RetailUser records to BusinessUser records
                List<Record> existingRecords = retailUserResult.getAllDatasets().get(0).getAllRecords();
                for (Record record : existingRecords) {
                    String backendId = HelperMethods.getFieldValue(record, "id");
                    Map<String, String> input = new HashMap<>();
                    input.put("Customer_id", businessUserId);
                    input.put("id", backendId);
                    HelperMethods.removeNullValues(input);
                    HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                            URLConstants.BACKENDIDENTIFIER_UPDATE);
                }
            }

        } catch (Exception e) {
            alert.prepareError("Exception occured while updating the customer Id in backendIdentifier from backend delegate :"
                    + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_21130);
        }

    }

    private void updateIsTypeBusinessInBackendIdRecords(Result businessUserResult, DataControllerRequest dcRequest)
            throws ApplicationException {
        try {
            if (HelperMethods.hasRecords(businessUserResult)) {
                // Updating field isTypeBusiness for BusinessUser records
                List<Record> existingRecords = businessUserResult.getAllDatasets().get(0).getAllRecords();
                for (Record record : existingRecords) {
                    String backendId = HelperMethods.getFieldValue(record, "id");
                    Map<String, String> input = new HashMap<>();
                    input.put("is", backendId);
                    input.put("isTypeBusiness", "1");
                    HelperMethods.removeNullValues(input);
                    HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                            URLConstants.BACKENDIDENTIFIER_UPDATE);
                }
            }
        } catch (Exception e) {
            alert.prepareError(
                    "Exception occured while updating the isTypeBusiness in backendIdentifier table from backend delegate :"
                            + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_21130);
        }

    }

    private void updateCustomerAccountsRB(String retailUserId, String businessUserId, DataControllerRequest dcRequest)
            throws ApplicationException {
        Result businessResult = new Result();
        Result retailResult = new Result();
        // get customer accounts for custId from customerAccounts table
        businessResult = getCustomerAccounts(businessUserId, dcRequest);
        // get accounts for custId from accounts table
        retailResult = getAccounts(retailUserId, dcRequest);

        if (HelperMethods.hasRecords(businessResult)) {

            // change business to retail custID in customeracc table and update the isOrgAcc
            List<Record> existingRecords = businessResult.getAllDatasets().get(0).getAllRecords();
            for (Record record : existingRecords) {
                String id = HelperMethods.getFieldValue(record, DBPUtilitiesConstants.UN_ID);
                Map<String, String> input = new HashMap<>();
                input.put("id", id);
                input.put("IsOrganizationAccount", "1");
                HelperMethods.removeNullValues(input);
                try {
                    HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                            URLConstants.CUSTOMERACCOUNTS_UPDATE);
                } catch (HttpCallException e) {
                    alert.prepareError(
                            "Exception occured while updating the customer Id in customer Accounts from backend delegate :"
                                    + e.getMessage()).log();
                    throw new ApplicationException(ErrorCodeEnum.ERR_10295);
                }
            }
        }

        if (HelperMethods.hasRecords(retailResult)) {

            // change business to retail custID in customeracc table and update the isOrgAcc
            List<Record> existingRecords = retailResult.getAllDatasets().get(0).getAllRecords();
            for (Record record : existingRecords) {
                Map<String, String> input = new HashMap<>();
                input.put("id", HelperMethods.getNumericId() + "");
                input.put("Customer_id", businessUserId);
                input.put("Account_id", HelperMethods.getFieldValue(record, "Account_id"));
                input.put("AccountName", HelperMethods.getFieldValue(record, "AccountName"));
                input.put("IsOrganizationAccount", HelperMethods.getFieldValue(record, "isBusinessAccount"));
                input.put("Membership_id", HelperMethods.getFieldValue(record, "Membership_id"));
                input.put("FavouriteStatus", HelperMethods.getFieldValue(record, "FavouriteStatus"));
                HelperMethods.removeNullValues(input);
                try {
                    HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                            URLConstants.CUSTOMERACCOUNTS_CREATE);
                } catch (HttpCallException e) {
                    alert.prepareError(
                            "Exception occured while updating the customer Id in customer Accounts from backend delegate :"
                                    + e.getMessage()).log();
                    throw new ApplicationException(ErrorCodeEnum.ERR_10294);
                }
            }
        }

    }

    private boolean isBusinessUser(String userId, DataControllerRequest dcRequest) throws ApplicationException {
        Result result = new Result();
        try {
            String filter = "id" + DBPUtilitiesConstants.EQUAL + userId;
            result = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                    URLConstants.CUSTOMERVERIFY_GET);
            if (HelperMethods.hasRecords(result)) {
                Dataset customers = result.getAllDatasets().get(0);
                for (Record record : customers.getAllRecords()) {
                    String customerType = HelperMethods.getFieldValue(record, "CustomerType_id");
                    if (Constants.TYPE_ID_BUSINESS.equals(customerType)) {
                        return true;
                    }
                }
            }

        } catch (Exception e) {
            alert.prepareError("Exception occured while fetching the customer type information from backend delegate :"
                    + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10020);
        }
        return false;
    }

    private void updateOtherRecords(String combinedUser, String otherUser, DataControllerRequest dcRequest,
            String isCombinedUserBusiness) throws ApplicationException {
        Map<String, String> inputParams = new HashMap<>();
        inputParams.put("_combinedUser", combinedUser);
        inputParams.put("_otherUser", otherUser);
        inputParams.put("_isCombinedUserBusiness", isCombinedUserBusiness);
        try {
            HelperMethods.callApiAsync(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.USERLINKING_PROC);
        } catch (Exception e) {
            alert.prepareError("Exception occured while updating the customer Id via stored procedure from backend delegate :"
                    + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_21120);
        }
    }

    private void updateCustomerCommunicationRB(String retailUserId, String businessUserId,
            DataControllerRequest dcRequest, JsonArray communicationArray) throws ApplicationException {
        Result businessUserResult = new Result();
        Result retailUserResult = new Result();

        try {
            // Getting the communication details of BusinessUser
            businessUserResult = getCommunicationForCustId(businessUserId, dcRequest);

            // update the isTypeBusiness field for all fetched businessUserResult
            updateIsTypeBusinessInCommRecords(businessUserResult, dcRequest);

            // Getting the communication details of RetailUser
            retailUserResult = getCommunicationForCustId(retailUserId, dcRequest);

            if (HelperMethods.hasRecords(retailUserResult)) {
                // Switching RetailUser records to BusinessUser(combined) records
                List<Record> existingRecords = retailUserResult.getAllDatasets().get(0).getAllRecords();
                for (Record record : existingRecords) {
                    String customerComm_id = HelperMethods.getFieldValue(record, DBPUtilitiesConstants.UN_ID);
                    String isPrimary = HelperMethods.getFieldValue(record, "isPrimary");
                    Map<String, String> input = new HashMap<>();
                    // storing the otherUser primary details for Alert processing
                    if (isPrimary.equals("true")) {
                        if (HelperMethods.getFieldValue(record, "Type_id").equals("COMM_TYPE_PHONE")) {
                            customParams.put("externalphone", HelperMethods.getFieldValue(record, "Value"));
                        } else if (HelperMethods.getFieldValue(record, "Type_id").equals("COMM_TYPE_EMAIL")) {
                            customParams.put("externalemail", HelperMethods.getFieldValue(record, "Value"));
                        }
                    }
                    input.put("Customer_id", businessUserId);
                    input.put("id", customerComm_id);
                    input.put("isPrimary", "0");
                    HelperMethods.removeNullValues(input);
                    HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                            URLConstants.CUSTOMER_COMMUNICATION_UPDATE);
                }

            } else {
                if (communicationArray.size() > 0) {
                    for (JsonElement communication : communicationArray) {
                        Map<String, String> input = new HashMap<>();
                        JsonObject commObj = communication.getAsJsonObject();
                        for (Entry<String, JsonElement> e : commObj.entrySet()) {
                            input.put(e.getKey(), e.getValue().getAsString());
                        }
                        String isPrimary = input.get("isPrimary");
                        // storing the otherUser primary details for Alert processing
                        if (isPrimary.equals("true")) {
                            if (input.get("Type_id").equals("COMM_TYPE_PHONE")) {
                                customParams.put("externalphone", input.get("Value"));
                            } else if (input.get("Type_id").equals("COMM_TYPE_EMAIL")) {
                                customParams.put("externalemail", input.get("Value"));
                            }
                        }
                        input.put("Customer_id", businessUserId);
                        input.put("id", HelperMethods.getNumericId() + "");
                        input.put("isPrimary", "0");
                        HelperMethods.removeNullValues(input);
                        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                                URLConstants.CUSTOMERCOMMUNICATION_CREATE);
                    }
                }
            }

        } catch (Exception e) {
            alert.prepareError("Exception occured while updating the customer Id in communication from backend delegate :"
                    + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_21122);
        }

    }

    private void updateCustomerCommunication(String retailUserId, String businessUserId,
            DataControllerRequest dcRequest, JsonArray communicationArray) throws ApplicationException {
        Result result = new Result();
        Result retailResult = new Result();
        try {
            // getting communication details for business User
            result = getCommunicationForCustId(businessUserId, dcRequest);

            if (HelperMethods.hasRecords(result)) {
                // Switching businessUser records to RetailUser(combined) records
                List<Record> existingRecords = result.getAllDatasets().get(0).getAllRecords();
                for (Record record : existingRecords) {
                    String commId = HelperMethods.getFieldValue(record, "id");
                    String isPrimary = HelperMethods.getFieldValue(record, "isPrimary");
                    // storing the otherUser primary details for Alert processing
                    if (isPrimary.equals("true")) {
                        if (HelperMethods.getFieldValue(record, "Type_id").equals("COMM_TYPE_PHONE")) {
                            customParams.put("externalphone", HelperMethods.getFieldValue(record, "Value"));
                        } else if (HelperMethods.getFieldValue(record, "Type_id").equals("COMM_TYPE_EMAIL")) {
                            customParams.put("externalemail", HelperMethods.getFieldValue(record, "Value"));
                        }
                    }
                    Map<String, String> input = new HashMap<>();
                    input.put("Customer_id", retailUserId);
                    input.put("isTypeBusiness", "1");
                    input.put("id", commId);
                    input.put("isPrimary", "0");
                    HelperMethods.removeNullValues(input);
                    HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                            URLConstants.CUSTOMERCOMMUNICATION_UPDATE);
                }
            }

            retailResult = getCommunicationForCustId(retailUserId, dcRequest);
            if (!HelperMethods.hasRecords(retailResult) && communicationArray.size() > 0) {
                for (JsonElement communication : communicationArray) {
                    Map<String, String> input = new HashMap<>();
                    JsonObject commObj = communication.getAsJsonObject();
                    for (Entry<String, JsonElement> e : commObj.entrySet()) {
                        input.put(e.getKey(), e.getValue().getAsString());
                    }
                    input.put("Customer_id", retailUserId);
                    input.put("id", HelperMethods.getNumericId() + "");
                    HelperMethods.removeNullValues(input);
                    HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                            URLConstants.CUSTOMERCOMMUNICATION_CREATE);
                }
            }
        } catch (Exception e) {
            alert.prepareError("Exception occured while updating the customer Id in communication from backend delegate :"
                    + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_21122);
        }
    }

    private void updateCustomerAddressRB(String retailUserId, String businessUserId, DataControllerRequest dcRequest,
            JsonArray addressArray) throws ApplicationException {
        Result businessUserResult = new Result();
        Result retailUserResult = new Result();
        try {
            // Getting the address details of BusinessUser
            businessUserResult = getAddressForCustId(businessUserId, dcRequest);
            // updating the isTypeBusiness field for all fetched businessUserResult
            updateIsTypeBusinessInAddressRecords(businessUserResult, dcRequest);
            // Getting the address details of RetailUser
            retailUserResult = getAddressForCustId(retailUserId, dcRequest);

            if (HelperMethods.hasRecords(retailUserResult)) {
                // Switching RetailUser records to BusinessUser records
                List<Record> existingRecords = retailUserResult.getAllDatasets().get(0).getAllRecords();
                for (Record record : existingRecords) {
                    String customerAddr_id = HelperMethods.getFieldValue(record, "Address_id");
                    Map<String, String> input = new HashMap<>();
                    input.put("Customer_id", businessUserId);
                    input.put("Address_id", customerAddr_id);
                    input.put("isPrimary", "0");
                    HelperMethods.removeNullValues(input);
                    HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                            URLConstants.CUSTOMER_ADDRESS_UPDATE);
                }
            } else {
                if (addressArray.size() > 0) {
                    for (JsonElement address : addressArray) {
                        JsonObject addressObj = address.getAsJsonObject();
                        Map<String, String> input = new HashMap<>();
                        for (Entry<String, JsonElement> e : addressObj.entrySet()) {
                            input.put(e.getKey(), e.getValue().getAsString());
                        }
                        input.put("Address_id", HelperMethods.getNumericId() + "");
                        input.put("Customer_id", businessUserId);
                        input.put("isPrimary", "0");
                        HelperMethods.removeNullValues(input);
                        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                                URLConstants.CUSTOMER_ADDRESS_CREATE);
                    }
                }
            }

        } catch (Exception e) {
            alert.prepareError("Exception occured while updating the customer Id in address from backend delegate :"
                    + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10299);
        }
    }

    private void updateIsTypeBusinessInCommRecords(Result businessUserResult, DataControllerRequest dcRequest)
            throws ApplicationException {
        try {
            if (HelperMethods.hasRecords(businessUserResult)) {
                // Updating field isTypeBusiness for BusinessUser records
                List<Record> existingRecords = businessUserResult.getAllDatasets().get(0).getAllRecords();
                for (Record record : existingRecords) {
                    String customerComm_id = HelperMethods.getFieldValue(record, DBPUtilitiesConstants.UN_ID);
                    Map<String, String> input = new HashMap<>();
                    input.put("id", customerComm_id);
                    input.put("isTypeBusiness", "1");
                    HelperMethods.removeNullValues(input);
                    HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                            URLConstants.CUSTOMER_COMMUNICATION_UPDATE);
                }
            }
        } catch (Exception e) {
            alert.prepareError(
                    "Exception occured while updating the isTypeBusiness in communication table from backend delegate :"
                            + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_21122);
        }
    }

    private void updateIsTypeBusinessInAddressRecords(Result businessUserResult, DataControllerRequest dcRequest)
            throws ApplicationException {
        try {
            if (HelperMethods.hasRecords(businessUserResult)) {
                // Updating field isTypeBusiness for BusinessUser records
                List<Record> existingRecords = businessUserResult.getAllDatasets().get(0).getAllRecords();
                for (Record record : existingRecords) {
                    String customerAddr_id = HelperMethods.getFieldValue(record, "Address_id");
                    Map<String, String> input = new HashMap<>();
                    input.put("Address_id", customerAddr_id);
                    input.put("isTypeBusiness", "1");
                    HelperMethods.removeNullValues(input);
                    HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                            URLConstants.CUSTOMER_ADDRESS_UPDATE);
                }
            }
        } catch (Exception e) {
            alert.prepareError("Exception occured while updating the isTypeBusiness in address table from backend delegate :"
                    + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10224);
        }
    }

    private void updateCustomerAddress(String retailUserId, String businessUserId, DataControllerRequest dcRequest,
            JsonArray addressArray) throws ApplicationException {
        Result result = new Result();
        Result retailResult = new Result();
        try {
            // Getting the address details of BusinessUser
            result = getAddressForCustId(businessUserId, dcRequest);
            if (HelperMethods.hasRecords(result)) {
                // Switching businessUser records to RetailUser(combined) records
                List<Record> existingRecords = result.getAllDatasets().get(0).getAllRecords();
                for (Record record : existingRecords) {
                    String customerAddr_id = HelperMethods.getFieldValue(record, "Address_id");
                    Map<String, String> input = new HashMap<>();
                    input.put("Customer_id", retailUserId);
                    input.put("isTypeBusiness", "1");
                    input.put("Address_id", customerAddr_id);
                    input.put("isPrimary", "0");
                    HelperMethods.removeNullValues(input);
                    HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                            URLConstants.CUSTOMER_ADDRESS_UPDATE);
                }
            }
            // get the party response and check if its not there and if not create a new
            // record else do nothing
            retailResult = getAddressForCustId(retailUserId, dcRequest);
            if (!HelperMethods.hasRecords(retailResult) && addressArray.size() > 0) {
                for (JsonElement address : addressArray) {
                    JsonObject addressObj = address.getAsJsonObject();
                    Map<String, String> input = new HashMap<>();
                    for (Entry<String, JsonElement> e : addressObj.entrySet()) {
                        input.put(e.getKey(), e.getValue().getAsString());
                    }
                    input.put("Address_id", HelperMethods.getNumericId() + "");
                    input.put("Customer_id", retailUserId);
                    HelperMethods.removeNullValues(input);
                    HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                            URLConstants.CUSTOMER_ADDRESS_CREATE);
                }
            }

        } catch (HttpCallException e) {
            alert.prepareError(
                    "Exception occured while updating the custID, isTypeBusiness in address table from backend delegate :"
                            + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10224);
        }
    }

    private Result getAddressForCustId(String customerId, DataControllerRequest dcRequest) throws ApplicationException {
        Map<String, String> inputParams = new HashMap<>();
        String filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + customerId;
        inputParams.put(DBPUtilitiesConstants.FILTER, filter);
        try {
            return HelperMethods.callGetApi(dcRequest, inputParams.get(DBPUtilitiesConstants.FILTER),
                    HelperMethods.getHeaders(dcRequest), URLConstants.CUSTOMER_ADDRESS_GET);
        } catch (Exception e) {
            alert.prepareError(
                    "Exception occured while fetching the address details from backend delegate :" + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_20881);
        }
    }

    private Result getCommunicationForCustId(String customerId, DataControllerRequest dcRequest)
            throws ApplicationException {
        Map<String, String> inputParams = new HashMap<>();
        String filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + customerId;
        inputParams.put(DBPUtilitiesConstants.FILTER, filter);
        try {
            return HelperMethods.callGetApi(dcRequest, inputParams.get(DBPUtilitiesConstants.FILTER),
                    HelperMethods.getHeaders(dcRequest), URLConstants.CUSTOMER_COMMUNICATION_GET);
        } catch (Exception e) {
            alert.prepareError("Exception occured while fetching the communication information from backend delegate :"
                    + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_21121);
        }
    }

    private JsonObject getBasicInfoForCustId(String customerId, DataControllerRequest dcRequest)
            throws ApplicationException {
        Map<String, String> inputParams = new HashMap<>();
        String filter = "id" + DBPUtilitiesConstants.EQUAL + customerId;
        inputParams.put(DBPUtilitiesConstants.FILTER, filter);
        try {
            return HelperMethods.callApiJson(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.CUSTOMER_GET);

        } catch (Exception e) {
            alert.prepareError(
                    "Exception occured while fetching the basic information from backend delegate :" + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10020);
        }

    }

    private Result getCustomerAccounts(String customerId, DataControllerRequest dcRequest) throws ApplicationException {
        Map<String, String> inputParams = new HashMap<>();
        String filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + customerId;
        inputParams.put(DBPUtilitiesConstants.FILTER, filter);
        try {
            return HelperMethods.callGetApi(dcRequest, inputParams.get(DBPUtilitiesConstants.FILTER),
                    HelperMethods.getHeaders(dcRequest), URLConstants.CUSTOMERACCOUNTS_GET);
        } catch (Exception e) {
            alert.prepareError("Exception occured while fetching the customer account details from backend delegate :"
                    + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10293);
        }
    }

    private Result getAccounts(String customerId, DataControllerRequest dcRequest) throws ApplicationException {
        Map<String, String> inputParams = new HashMap<>();
        String filter = "User_id" + DBPUtilitiesConstants.EQUAL + customerId;
        inputParams.put(DBPUtilitiesConstants.FILTER, filter);
        try {
            return HelperMethods.callGetApi(dcRequest, inputParams.get(DBPUtilitiesConstants.FILTER),
                    HelperMethods.getHeaders(dcRequest), URLConstants.ACCOUNT_GET);
        } catch (Exception e) {
            alert.prepareError(
                    "Exception occured while fetching the account details from backend delegate :" + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10293);
        }
    }

    private Result getCardsForCustId(String customerId, DataControllerRequest dcRequest) throws ApplicationException {
        Map<String, String> inputParams = new HashMap<>();
        String filter = "User_id" + DBPUtilitiesConstants.EQUAL + customerId;
        inputParams.put(DBPUtilitiesConstants.FILTER, filter);
        try {
            return HelperMethods.callGetApi(dcRequest, inputParams.get(DBPUtilitiesConstants.FILTER),
                    HelperMethods.getHeaders(dcRequest), URLConstants.CARD_GET);
        } catch (Exception e) {
            alert.prepareError("Exception occured while fetching the card details from backend delegate :" + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_21117);
        }
    }

    @Override
    public Result userDeLinkingOperation(Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws ApplicationException {
        Result result = new Result();
        customParams.clear();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        String newUser = inputParams.get("newUser");
        String combinedUser = inputParams.get("combinedUser");

        if (StringUtils.isBlank(combinedUser) || StringUtils.isBlank(newUser)) {
            alert.prepareError("Error: Invalid input").log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10197);
        }
        // checking whether user is not already combinedUser
        if (!isCombinedUser(combinedUser, dcRequest)) {
            alert.prepareError("Error: User is not a combined user").log();
            throw new ApplicationException(ErrorCodeEnum.ERR_21116);
        }
        delinkingCustomerBasicInfo(combinedUser, newUser, dcRequest);
        delinkingCustomerAddress(combinedUser, dcRequest);

        delinkingCommunication(combinedUser, dcRequest);

        delinkingCustomerAccounts(combinedUser, dcRequest);

        delinkingCustomerCards(combinedUser, dcRequest);

        delinkingOtherRecords(combinedUser, dcRequest);

        delinkingBackendIdentifier(combinedUser, dcRequest);

        sendActivationLinkEmail(newUser, dcRequest);

        String accountIds = String.join(",", delinkingAccIdList);
        Map<String, Object> input = new HashMap<>();
        input.put("newCustomerId", combinedUser);
        input.put("combinedCustomerId", newUserId);
        input.put("accountIds", accountIds);
        OperationData operationData;
        try {
            operationData = dcRequest.getServicesManager().getOperationDataBuilder()
                    .withServiceId("CombinedAccessAlertServices").withVersion("1.0")
                    .withOperationId("deactivateCombinedAccess").build();
            ServiceRequest serviceRequest = dcRequest.getServicesManager().getRequestBuilder(operationData)
                    .withInputs(input).withHeaders(dcRequest.getHeaderMap()).build();
            Result result1 = serviceRequest.invokeServiceAndGetResult();
        } catch (Exception e) {
            alert.prepareError("Exception occured while triggering alerts for combined access from backend delegate :"
                    + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_21125);
        }
        JsonObject customParamsObj = new JsonObject();
        if (customParams.get("externalphone") != null && customParams.get("externalphone") != "") {
            customParamsObj.addProperty("maskedphone", getMaskedPhoneNumber(customParams.get("externalphone")));
        }
        if (customParams.get("externalemail") != null && customParams.get("externalemail") != "") {
            customParamsObj.addProperty("maskedemail", getMaskedEmail(customParams.get("externalemail")));
        }

        try {
            ThreadExecutor.getExecutor(dcRequest)
                    .execute(() -> EventsDispatcher.dispatch(dcRequest, dcResponse, "COMBINED_ACCESS", "USER_DELINKED",
                            null, "SID_EVENT_SUCCESS", null, combinedUser, "RETAIL_AND_BUSINESS_BANKING",
                            customParamsObj));
        } catch (InterruptedException e) {
            alert.prepareError("Exception while triggering USER_DELINKED alert" + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_21126);
        }
        result.addParam("success", "true");

        return result;
    }

    private void delinkingBackendIdentifier(String combinedUser, DataControllerRequest dcRequest)
            throws ApplicationException {
        Result result = new Result();
        try {
            // Getting the backendidentifier details of combinedUser
            result = getBackendIdentifierForCustId(combinedUser, dcRequest);
            if (HelperMethods.hasRecords(result)) {
                // Updating newUserid for isTypeBusiness false records
                List<Record> existingRecords = result.getAllDatasets().get(0).getAllRecords();
                for (Record record : existingRecords) {
                    String backendId = HelperMethods.getFieldValue(record, "id");
                    String isTypeBusiness = HelperMethods.getFieldValue(record, "isTypeBusiness");
                    if (isTypeBusiness.equals("0")) {
                        Map<String, String> input = new HashMap<>();
                        input.put("Customer_id", newUserId);
                        input.put("id", backendId);
                        HelperMethods.removeNullValues(input);
                        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                                URLConstants.BACKENDIDENTIFIER_UPDATE);
                    }
                }
            }
        } catch (HttpCallException e) {
            alert.prepareError(
                    "Exception occured while updating the backend identifier from backend delegate :" + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_21130);
        }

    }

    private void delinkingOtherRecords(String combinedUser, DataControllerRequest dcRequest)
            throws ApplicationException {
        Map<String, String> inputParams = new HashMap<>();
        inputParams.put("_combinedUser", combinedUser);
        inputParams.put("_newUser", newUserId);
        try {
            HelperMethods.callApiAsync(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.USERDELINKING_PROC);
        } catch (Exception e) {
            alert.prepareError("Exception occured while updating the customer Id via stored procedure from backend delegate :"
                    + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_21119);
        }
    }

    private void delinkingCustomerAccounts(String combinedUser, DataControllerRequest dcRequest)
            throws ApplicationException {
        Result result = new Result();
        try {
            // Getting the retail accounts details of CombinedUser
            result = getRetailAccountsFromCombinedUser(combinedUser, dcRequest);

            if (HelperMethods.hasRecords(result)) {
                // Updating retail accounts records with newUser
                List<Record> existingRecords = result.getAllDatasets().get(0).getAllRecords();
                for (Record record : existingRecords) {
                    String accountId = HelperMethods.getFieldValue(record, DBPUtilitiesConstants.ACCOUNT_ID);
                    String custAccId = HelperMethods.getFieldValue(record, DBPUtilitiesConstants.C_ID);
                    delinkingAccIdList.add(accountId);
                    Map<String, String> input = new HashMap<>();
                    input.put("User_id", newUserId);
                    input.put("Account_id", accountId);
                    HelperMethods.removeNullValues(input);
                    HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                            URLConstants.ACCOUNTS_UPDATE);

                    Map<String, String> input1 = new HashMap<>();
                    input1.put("id", custAccId);
                    HelperMethods.removeNullValues(input1);
                    HelperMethods.callApi(dcRequest, input1, HelperMethods.getHeaders(dcRequest),
                            URLConstants.CUSTOMERACCOUNTS_DELETE);
                }
            }
        } catch (Exception e) {
            alert.prepareError("Exception occured while updating account details from backend delegate :" + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10295);
        }

    }

    private Result getRetailAccountsFromCombinedUser(String combinedUser, DataControllerRequest dcRequest)
            throws ApplicationException {
        Map<String, String> inputParams = new HashMap<>();
        String filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + combinedUser + DBPUtilitiesConstants.AND
                + "IsOrganizationAccount" + DBPUtilitiesConstants.NOT_EQ + "1";
        inputParams.put(DBPUtilitiesConstants.FILTER, filter);
        try {
            return HelperMethods.callGetApi(dcRequest, inputParams.get(DBPUtilitiesConstants.FILTER),
                    HelperMethods.getHeaders(dcRequest), URLConstants.CUSTOMERACCOUNTS_GET);
        } catch (Exception e) {
            alert.prepareError("Exception occured while fetching account details from backend delegate :" + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10293);
        }
    }

    private void delinkingCustomerCards(String combinedUser, DataControllerRequest dcRequest)
            throws ApplicationException {
        Result result = new Result();
        try {
            // getting communication details for business User
            result = getRetailsCardsFromCombinedUser(combinedUser, dcRequest);

            if (HelperMethods.hasRecords(result)) {
                // Switching businessUser records to RetailUser(combined) records
                List<Record> existingRecords = result.getAllDatasets().get(0).getAllRecords();
                for (Record record : existingRecords) {
                    String customerComm_id = HelperMethods.getFieldValue(record, DBPUtilitiesConstants.U_ID);
                    Map<String, String> input = new HashMap<>();
                    input.put("User_id", newUserId);
                    input.put("Id", customerComm_id);
                    HelperMethods.removeNullValues(input);
                    HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                            URLConstants.CARD_UPDATE);
                }
            }
        } catch (Exception e) {
            alert.prepareError("Exception occured while updating the card details from backend delegate :" + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_21118);
        }

    }

    private Result getRetailsCardsFromCombinedUser(String combinedUser, DataControllerRequest dcRequest)
            throws ApplicationException {
        Map<String, String> inputParams = new HashMap<>();
        String filter = "User_id" + DBPUtilitiesConstants.EQUAL + combinedUser + DBPUtilitiesConstants.AND
                + "isTypeBusiness" + DBPUtilitiesConstants.EQUAL + "null";
        inputParams.put(DBPUtilitiesConstants.FILTER, filter);
        try {
            return HelperMethods.callGetApi(dcRequest, inputParams.get(DBPUtilitiesConstants.FILTER),
                    HelperMethods.getHeaders(dcRequest), URLConstants.CARD_GET);
        } catch (Exception e) {
            alert.prepareError(
                    "Exception occured while fetching the card information from backend delegate :" + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_21117);
        }
    }

    private void delinkingCommunication(String combinedUser, DataControllerRequest dcRequest)
            throws ApplicationException {
        Result result = new Result();
        Result combinedResult = new Result();
        Record firstRecord = new Record();
        boolean hasPrimary = false;
        Map<String,String> phoneCommunicationDetails = new HashMap<>();
        Map<String,String> emailCommunicationDetails = new HashMap<>();
        try {
            // Getting the retail communication details of CombinedUser
            result = getRetailCommunicationFromCombinedUser(combinedUser, dcRequest);
            if (HelperMethods.hasRecords(result)) {
                // Updating retail address records with newUser
                List<Record> existingRecords = result.getAllDatasets().get(0).getAllRecords();
                firstRecord = existingRecords.get(0);
                for (Record record : existingRecords) {
                    String customerComm_id = HelperMethods.getFieldValue(record, DBPUtilitiesConstants.UN_ID);
                    String isPrimary = HelperMethods.getFieldValue(record, "isPrimary");
                    // storing the otherUser primary details for Alert processing
                    if (isPrimary.equals("true")) {
                        hasPrimary = true;
                        if (HelperMethods.getFieldValue(record, "Type_id").equals("COMM_TYPE_PHONE")) {
                            customParams.put("externalphone", HelperMethods.getFieldValue(record, "Value"));
                        } else if (HelperMethods.getFieldValue(record, "Type_id").equals("COMM_TYPE_EMAIL")) {
                            email = HelperMethods.getFieldValue(record, "Value");
                            customParams.put("externalemail", HelperMethods.getFieldValue(record, "Value"));
                        }
                    }
                    if (HelperMethods.getFieldValue(record, "Type_id").equals("COMM_TYPE_PHONE")) {
                        phoneCommunicationDetails.put("phone", HelperMethods.getFieldValue(record, "Value"));
                    }
                    if (HelperMethods.getFieldValue(record, "Type_id").equals("COMM_TYPE_EMAIL")) {
                        emailCommunicationDetails.put("email", HelperMethods.getFieldValue(record, "Value"));
                    }
                    Map<String, String> input = new HashMap<>();
                    input.put("Customer_id", newUserId);
                    input.put("id", customerComm_id);
                    HelperMethods.removeNullValues(input);
                    HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                            URLConstants.CUSTOMER_COMMUNICATION_UPDATE);
                }
            }
            if (StringUtils.isBlank(email)) {
                email = emailCommunicationDetails.get("email");
            }
            if (StringUtils.isBlank(customParams.get("externalemail"))) {
                customParams.put("externalemail", emailCommunicationDetails.get("email"));
            }
            if (StringUtils.isBlank(customParams.get("externalphone"))) {
                customParams.put("externalphone", phoneCommunicationDetails.get("phone"));
            }
            if (!hasPrimary && firstRecord != null) {
                String customerComm_id = HelperMethods.getFieldValue(firstRecord, DBPUtilitiesConstants.UN_ID);
                Map<String, String> input = new HashMap<>();
                input.put("id", customerComm_id);
                input.put("isPrimary", "1");
                if (HelperMethods.getFieldValue(firstRecord, "Type_id").equals("COMM_TYPE_PHONE")) {
                    customParams.put("externalphone", HelperMethods.getFieldValue(firstRecord, "Value"));
                } else if (HelperMethods.getFieldValue(firstRecord, "Type_id").equals("COMM_TYPE_EMAIL")) {
                    customParams.put("externalemail", HelperMethods.getFieldValue(firstRecord, "Value"));
                }
                HelperMethods.removeNullValues(input);
                HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                        URLConstants.CUSTOMER_COMMUNICATION_UPDATE);
            }

            combinedResult = getCommunicationForCustId(combinedUser, dcRequest);
            if (HelperMethods.hasRecords(combinedResult)) {
                List<Record> existingRecords = combinedResult.getAllDatasets().get(0).getAllRecords();
                for (Record record : existingRecords) {
                    String customerComm_id = HelperMethods.getFieldValue(record, DBPUtilitiesConstants.UN_ID);
                    String isPrimary = HelperMethods.getFieldValue(record, "isPrimary");
                    if (isPrimary.equals("false")) {
                        Map<String, String> input = new HashMap<>();
                        input.put("id", customerComm_id);
                        input.put("isPrimary", "1");
                        HelperMethods.removeNullValues(input);
                        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                                URLConstants.CUSTOMER_COMMUNICATION_UPDATE);
                    }

                }
            }

        } catch (Exception e) {
            alert.prepareError("Exception occured while updating the custId in communication from backend delegate :"
                    + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_21122);
        }

    }

    private Result getRetailCommunicationFromCombinedUser(String combinedUser, DataControllerRequest dcRequest)
            throws ApplicationException {
        Map<String, String> inputParams = new HashMap<>();
        String filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + combinedUser + DBPUtilitiesConstants.AND
                + "isTypeBusiness" + DBPUtilitiesConstants.EQUAL + "null";
        inputParams.put(DBPUtilitiesConstants.FILTER, filter);
        try {
            return HelperMethods.callGetApi(dcRequest, inputParams.get(DBPUtilitiesConstants.FILTER),
                    HelperMethods.getHeaders(dcRequest), URLConstants.CUSTOMER_COMMUNICATION_GET);
        } catch (Exception e) {
            alert.prepareError("Exception occured while fetching the communication information from backend delegate :"
                    + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_21121);
        }
    }

    private void delinkingCustomerAddress(String combinedUser, DataControllerRequest dcRequest)
            throws ApplicationException {
        Result result = new Result();
        Result combinedResult = new Result();
        Record firstRecord = new Record();
        boolean hasPrimary = false;
        try {
            // Getting the retail address details of CombinedUser
            result = getRetailAddressFromCombinedUser(combinedUser, dcRequest);
            if (HelperMethods.hasRecords(result)) {

                // Updating retail address records with newUser
                List<Record> existingRecords = result.getAllDatasets().get(0).getAllRecords();
                firstRecord = existingRecords.get(0);
                for (Record record : existingRecords) {
                    String customerAddr_id = HelperMethods.getFieldValue(record, "Address_id");
                    String isPrimary = HelperMethods.getFieldValue(record, "isPrimary");
                    // storing the otherUser primary details for Alert processing
                    if (isPrimary.equals("true")) {
                        hasPrimary = true;
                        if (HelperMethods.getFieldValue(record, "Type_id").equals("COMM_TYPE_PHONE")) {
                            customParams.put("externalphone", HelperMethods.getFieldValue(record, "Value"));
                        } else if (HelperMethods.getFieldValue(record, "Type_id").equals("COMM_TYPE_EMAIL")) {
                            customParams.put("externalemail", HelperMethods.getFieldValue(record, "Value"));
                        }
                    }
                    Map<String, String> input = new HashMap<>();
                    input.put("Customer_id", newUserId);
                    input.put("Address_id", customerAddr_id);
                    HelperMethods.removeNullValues(input);
                    HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                            URLConstants.CUSTOMER_ADDRESS_UPDATE);
                }
            }
            if (!hasPrimary && firstRecord != null) {
                String customerAddr_id = HelperMethods.getFieldValue(firstRecord, "Address_id");
                Map<String, String> input = new HashMap<>();
                input.put("id", customerAddr_id);
                input.put("isPrimary", "1");
                if (HelperMethods.getFieldValue(firstRecord, "Type_id").equals("COMM_TYPE_PHONE")) {
                    customParams.put("externalphone", HelperMethods.getFieldValue(firstRecord, "Value"));
                } else if (HelperMethods.getFieldValue(firstRecord, "Type_id").equals("COMM_TYPE_EMAIL")) {
                    customParams.put("externalemail", HelperMethods.getFieldValue(firstRecord, "Value"));
                }
                HelperMethods.removeNullValues(input);
                HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                        URLConstants.CUSTOMER_ADDRESS_UPDATE);
            }

            // after delinking , fetch combined user records if no primary details is
            // present, set is primary as 1
            combinedResult = getAddressForCustId(combinedUser, dcRequest);
            if (HelperMethods.hasRecords(combinedResult)) {
                List<Record> existingRecords = combinedResult.getAllDatasets().get(0).getAllRecords();
                for (Record record : existingRecords) {
                    String customerAdd_id = HelperMethods.getFieldValue(record, "Address_id");
                    String isPrimary = HelperMethods.getFieldValue(record, "isPrimary");
                    if (isPrimary.equals("false")) {
                        Map<String, String> input = new HashMap<>();
                        input.put("id", customerAdd_id);
                        input.put("isPrimary", "1");
                        HelperMethods.removeNullValues(input);
                        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                                URLConstants.CUSTOMER_ADDRESS_UPDATE);
                    }

                }
            }
        } catch (HttpCallException e) {
            alert.prepareError(
                    "Exception occured while updating the custID, isTypeBusiness in address table from backend delegate :"
                            + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10299);
        }

    }

    private Result getRetailAddressFromCombinedUser(String combinedUser, DataControllerRequest dcRequest)
            throws ApplicationException {
        Map<String, String> inputParams = new HashMap<>();
        String filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + combinedUser + DBPUtilitiesConstants.AND
                + "isTypeBusiness" + DBPUtilitiesConstants.EQUAL + "null";
        inputParams.put(DBPUtilitiesConstants.FILTER, filter);
        try {
            return HelperMethods.callGetApi(dcRequest, inputParams.get(DBPUtilitiesConstants.FILTER),
                    HelperMethods.getHeaders(dcRequest), URLConstants.CUSTOMER_ADDRESS_GET);
        } catch (Exception e) {
            alert.prepareError("Exception occured while fetching the retail address information from backend delegate :"
                    + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_20881);
        }
    }

    private void delinkingCustomerBasicInfo(String combinedUser, String newUserName, DataControllerRequest dcRequest)
            throws ApplicationException {
        JsonObject combinedJson = new JsonObject();
        combinedJson = getBasicInfoForCustId(combinedUser, dcRequest);
        Map<String, String> input = new HashMap<>();

        if (!combinedJson.has("opstatus") || combinedJson.get("opstatus").getAsInt() != 0
                || !combinedJson.has("customer")) {
            alert.prepareError("Exception occured while fetching the customer information").log();
            throw new ApplicationException(ErrorCodeEnum.ERR_12606);
        }

        if (combinedJson.get("customer").getAsJsonArray().size() == 0) {
            alert.prepareError("Exception occured while fetching the customer information").log();
            throw new ApplicationException(ErrorCodeEnum.ERR_12606);
        }
        try {
            if (!combinedJson.isJsonNull() && combinedJson.has("customer")
                    && combinedJson.get("customer").getAsJsonArray().size() > 0) {
                combinedJson = combinedJson.get("customer").getAsJsonArray().get(0).getAsJsonObject();
                for (Entry<String, JsonElement> e : combinedJson.entrySet()) {
                    input.put(e.getKey(), e.getValue().getAsString());
                }
                input.remove("CurrentLoginTime");
                input.remove("Lastlogintime");
                input.remove("createdts");
                input.remove("lastmodifiedts");
                input.remove("synctimestamp");
                input.remove("ValidDate");
                input.remove("Password");
                input.put("Organization_Id", null);
                input.put("organizationType", null);
                input.put("DrivingLicenseNumber", null);
                input.put("UserComapany", null);
                input.put("isEagreementSigned", null);
                input.put("UserName", newUserName);
                input.put("Status_id", "SID_CUS_NEW");
                input.put("isCombinedUser", "0");
                input.put("CustomerType_id", "TYPE_ID_RETAIL");
                input.put("id", HelperMethods.generateUniqueCustomerId(dcRequest));
                newUserId = input.get("id");
                firstName = input.get("FirstName");
                lastName = input.get("LastName");
                HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                        URLConstants.CUSTOMER_CREATE);

                // updating isCombineduser as 0
                Map<String, String> inputParams = new HashMap<>();
                inputParams.put("id", combinedUser);
                inputParams.put("isCombinedUser", "0");
                HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                        URLConstants.CUSTOMER_UPDATE);

            }

        } catch (Exception e) {
            alert.prepareError("Exception occured while updating basicInfo for combinedUser from backend delegate :"
                    + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10717);
        }

    }

    private boolean isCombinedUser(String combinedUser, DataControllerRequest dcRequest) throws ApplicationException {
        Result result = new Result();
        try {
            String filter = "id" + DBPUtilitiesConstants.EQUAL + combinedUser;
            result = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                    URLConstants.CUSTOMERVERIFY_GET);
            if (HelperMethods.hasRecords(result)) {
                Dataset customers = result.getAllDatasets().get(0);
                for (Record record : customers.getAllRecords()) {
                    String isCombinedUser = HelperMethods.getFieldValue(record, "isCombinedUser");
                    if (isCombinedUser.equals("true")) {
                        return true;
                    }
                }
            }

        } catch (Exception e) {
            alert.prepareError("Exception occured while validating combined user from backend delegate :" + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10020);
        }
        return false;
    }

    public static String getMaskedPhoneNumber(String phoneNumber) {
        StringBuilder sb = new StringBuilder(phoneNumber);
        for (int i = 3; i < sb.length() - 2; ++i) {
            sb.setCharAt(i, 'X');
        }
        return sb.toString();
    }

    public static String getMaskedEmail(String email) {
        StringBuilder sb = new StringBuilder(email);
        for (int i = 3; i < sb.length() && sb.charAt(i) != '@'; ++i) {
            sb.setCharAt(i, 'X');
        }
        return sb.toString();
    }

    private void sendActivationLinkEmail(String newUser, DataControllerRequest dcRequest) throws ApplicationException {

        CredentialCheckerBusinessDelegate credentialCheckerDB = DBPAPIAbstractFactoryImpl
                .getBusinessDelegate(CredentialCheckerBusinessDelegate.class);
        CredentialCheckerDTO credentialCheckerDTO = new CredentialCheckerDTO();

        credentialCheckerDTO.setUserName(newUser);
        DBXResult dbxResultResponseDTO = credentialCheckerDB.get(credentialCheckerDTO, dcRequest.getHeaderMap());
        credentialCheckerDTO = ((CredentialCheckerDTO) dbxResultResponseDTO.getResponse());
        if (credentialCheckerDTO != null) {
            credentialCheckerDB.delete(credentialCheckerDTO, dcRequest.getHeaderMap());
        }
        credentialCheckerDTO = new CredentialCheckerDTO();
        String activationToken = UUID.randomUUID().toString();
        credentialCheckerDTO.setId(activationToken);
        credentialCheckerDTO.setUserName(newUser);
        credentialCheckerDTO.setLinktype(HelperMethods.CREDENTIAL_TYPE.ACTIVATION.toString());
        credentialCheckerDTO.setCreatedts(HelperMethods.getCurrentTimeStamp());

        credentialCheckerDB.create(credentialCheckerDTO, dcRequest.getHeaderMap());

        String link = URLFinder.getPathUrl(URLConstants.DBX_SBB_ACTIVATION_LINK, dcRequest) + "?qp="
                + encodeToBase64(activationToken);

        PasswordHistoryManagement pm = new PasswordHistoryManagement(dcRequest, true);
        Map<String, String> input = new HashMap<>();
        input.put("Subscribe", "true");
        input.put("FirstName", firstName);
        input.put("EmailType", "activationLink");
        input.put("LastName", lastName);
        JSONObject addContext = new JSONObject();
        addContext.put("resetPasswordLink", link);
        addContext.put("userName", newUser);
        addContext.put("linkExpiry", String.valueOf(Math.floorDiv(pm.getRecoveryEmailLinkValidity(), 60)));
        input.put("AdditionalContext", KMSUtil.getOTPContent(null, null, addContext));
        input.put("Email", email);
        Map<String, String> headers = HelperMethods.getHeaders(dcRequest);
        headers.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
        Result response = new Result();
        try {
            response = HelperMethods.callApi(dcRequest, input, headers, URLConstants.DBX_SEND_EMAIL_ORCH);
        } catch (HttpCallException e) {
            alert.prepareError("Exception occured while validating combined user from backend delegate :" + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10020);
        }
    }

    public static String encodeToBase64(String sourceString) {
        if (sourceString == null) {
            return null;
        }
        return new String(java.util.Base64.getEncoder().encode(sourceString.getBytes()));
    }
}
