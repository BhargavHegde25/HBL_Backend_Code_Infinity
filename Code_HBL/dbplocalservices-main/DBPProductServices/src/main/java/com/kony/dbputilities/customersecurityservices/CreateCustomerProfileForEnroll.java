package com.kony.dbputilities.customersecurityservices;

import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.Callable;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.dbp.core.constants.DBPConstants;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.BCrypt;
import com.kony.dbputilities.util.DBPInputConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.URLFinder;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.utils.ThreadExecutor;

public class CreateCustomerProfileForEnroll implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);

        for (Entry<String, String[]> entry : dcRequest.getSource().entrySet()) {
            String[] value = entry.getValue();
            inputParams.put(entry.getKey(), value[0]);
        }

        String memberId = inputParams.get("MemberId");
        if (StringUtils.isBlank(memberId)) {
            memberId = inputParams.get("membershipID");
        }
        String dbpErrCode = StringUtils.isNotBlank(dcRequest.getParameter(DBPConstants.DBP_ERROR_CODE_KEY))
                ? dcRequest.getParameter(DBPConstants.DBP_ERROR_CODE_KEY)
                : inputParams.get(DBPConstants.DBP_ERROR_CODE_KEY);

        String dbpErrMsg = StringUtils.isNotBlank(dcRequest.getParameter(DBPConstants.DBP_ERROR_MESSAGE_KEY))
                ? dcRequest.getParameter(DBPConstants.DBP_ERROR_MESSAGE_KEY)
                : inputParams.get(DBPConstants.DBP_ERROR_MESSAGE_KEY);

        String id = StringUtils.isNotBlank(dcRequest.getParameter("id"))
                ? dcRequest.getParameter("id")
                : inputParams.get("id");

        if (StringUtils.isNotBlank(dbpErrCode) || StringUtils.isNotBlank(dbpErrMsg)) {
            result.addParam(new Param(DBPConstants.DBP_ERROR_CODE_KEY, dbpErrCode, "String"));
            result.addParam(new Param(DBPConstants.DBP_ERROR_MESSAGE_KEY, dbpErrMsg, "String"));
            return result;
        }
        if (StringUtils.isNotBlank(memberId) && StringUtils.isNotBlank(id)) {
            return result;
        }

        if (StringUtils.isBlank(memberId) && StringUtils.isBlank(id)) {
            if (!checkIfMandatoryDetailsPresent(inputParams)) {
                ErrorCodeEnum.ERR_10044.setErrorCode(result);
                return result;

            }
            PasswordHistoryManagement pm = new PasswordHistoryManagement(dcRequest, false);
            String existingCustomerId = getCustomerId(inputParams, dcRequest);
            if (StringUtils.isNotBlank(existingCustomerId)) {

                String password = inputParams.get("Password");
                if (StringUtils.isNotBlank(password)) {
                    inputParams.put("Status_id", "SID_CUS_ACTIVE");
                    String salt = BCrypt.gensalt(DBPUtilitiesConstants.SALT_NUMBER_OF_ROUNDS);
                    String hashedPassword = BCrypt.hashpw(password, salt);
                    inputParams.put("Password", hashedPassword);
                } else {
                    inputParams.put("Status_id", "SID_CUS_NEW");
                }
                inputParams.put("isEnrolled", "true");
                inputParams.put("id", existingCustomerId);

                HelperMethods.removeNullValues(inputParams);
                result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                        URLConstants.CUSTOMER_UPDATE);

            } else if (preProcess(inputParams, dcRequest, result)) {
                HelperMethods.removeNullValues(inputParams);
                result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                        URLConstants.CUSTOMER_CREATE);
            }
            if (StringUtils.isNotBlank(getUserId(result))
                    && pm.makePasswordEntry(dcRequest, getUserId(result), inputParams.get("Password"))) {
                result = postProcess(inputParams, result, dcRequest, memberId, existingCustomerId);
            } else if (StringUtils.isNotBlank(getUserId(result))) {
                Map<String, String> input = new HashMap<>();
                input.put("id", getUserId(result));
                HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                        URLConstants.CUSTOMER_DELETE);
                result = addErrorTOResult();
            } else {
                result = addErrorTOResult();
            }

        }
        return result;

    }

    private String getUserId(Result result) {
        String id = "";
        if (HelperMethods.hasRecords(result)) {
            Dataset ds = result.getAllDatasets().get(0);
            if (null != ds && !ds.getAllRecords().isEmpty()) {
                id = ds.getRecord(0).getParam("id").getValue();
            }
        }
        return id;
    }

    private boolean checkIfMandatoryDetailsPresent(Map<String, String> inputParams) {
        return (StringUtils.isNotBlank(DBPUtilitiesConstants.C_SSN)
                && StringUtils.isNotBlank(DBPUtilitiesConstants.CUSTOMER_LAST_NAME)
                && StringUtils.isNotBlank(DBPUtilitiesConstants.C_DOB)
                && StringUtils.isNotBlank("UserName"));
    }

    private String getCustomerId(Map<String, String> inputParams, DataControllerRequest dcRequest) {
        Result customerresult = null;
        String customerId = null;
        String customerlastname = inputParams.get(DBPInputConstants.CUSTOMER_LAST_NAME);
        String ssn = inputParams.get(DBPUtilitiesConstants.C_SSN);
        String dob = inputParams.get(DBPUtilitiesConstants.C_DOB);

        StringBuilder sb = new StringBuilder();
        try {

            dob = HelperMethods.convertDateFormat(dob, null);
        } catch (ParseException e) {
            dob = "";
            alert.prepareError(e.getMessage()).log();
        }
        if (StringUtils.isNotBlank(dob)) {
            sb.append(DBPUtilitiesConstants.CUSTOMER_LAST_NAME).append(DBPUtilitiesConstants.EQUAL)
                    .append(customerlastname);
            sb.append(DBPUtilitiesConstants.AND);
            sb.append(DBPUtilitiesConstants.C_SSN).append(DBPUtilitiesConstants.EQUAL).append(ssn);
            sb.append(DBPUtilitiesConstants.AND);
            sb.append(DBPUtilitiesConstants.C_DOB).append(DBPUtilitiesConstants.EQUAL).append(dob);

            Map<String, String> filterMap = new HashMap<>();
            filterMap.put(DBPUtilitiesConstants.FILTER, sb.toString());
            try {
                customerresult = HelperMethods.callApi(dcRequest, filterMap, HelperMethods.getHeaders(dcRequest),
                        URLConstants.CUSTOMERVERIFY_GET);
            } catch (HttpCallException e) {
                alert.prepareError(e.getMessage()).log();
            }
        }

        if (HelperMethods.hasRecords(customerresult)) {

            List<Record> records = customerresult.getAllDatasets().get(0).getAllRecords();

            for (Record record : records) {
                if (!HelperMethods.getBusinessUserTypes()
                        .contains(HelperMethods.getFieldValue(customerresult, "CustomerType_id"))) {
                    return record.getParamValueByName("id");
                }
            }
        }

        return customerId;
    }

    private boolean preProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result) {
        boolean status = true;

        String id = HelperMethods.generateUniqueCustomerId(dcRequest);
        inputParams.put("id", id);

        String username = inputParams.get("UserName");
        String password = inputParams.get("Password");
        String personalInfoString = inputParams.get("personalInformation");
        inputParams.put("inputPassword", password);
        if (StringUtils.isNotBlank(personalInfoString)) {
            HashMap<String, String> hashMap = HelperMethods.getRecordMap(personalInfoString);
            inputParams.put("FirstName", hashMap.get("firstName"));
            inputParams.put("MiddleName", hashMap.get("middleName"));
        } else {
            inputParams.put("FirstName", inputParams.get("LastName"));
        }

        if (StringUtils.isBlank(username)) {
            ErrorCodeEnum.ERR_10042.setErrorCode(result);
            return false;
        }
        if (StringUtils.isNotBlank(password)) {
            inputParams.put("Status_id", "SID_CUS_ACTIVE");
            String salt = BCrypt.gensalt(DBPUtilitiesConstants.SALT_NUMBER_OF_ROUNDS);
            String hashedPassword = BCrypt.hashpw(password, salt);
            inputParams.put("Password", hashedPassword);
        } else {
            inputParams.put("Status_id", "SID_CUS_NEW");
        }

        inputParams.put("CustomerType_id", "TYPE_ID_RETAIL");

        inputParams.put("id", HelperMethods.generateUniqueCustomerId(dcRequest));

        String role = inputParams.get(DBPUtilitiesConstants.ROLE);
        if (!StringUtils.isNotBlank(role)) {
            inputParams.put("Role", "BASIC");
        }
        inputParams.put("Bank_id", "1");
        inputParams.put("isEnrolled", "true");
        HelperMethods.removeNullValues(inputParams);
        return status;
    }

    private Result postProcess(Map<String, String> inputParams, Result result, DataControllerRequest dcRequest,
            String memberId, String existingCustomerId) {

        Result retResult = new Result();
        if (HelperMethods.hasRecords(result)) {
            String id = getUserId(result);
            HelperMethods.setSuccessMsg(DBPUtilitiesConstants.SUCCESS_MSG, retResult);
            retResult.addParam(new Param("id", id, "String"));
            inputParams.put("id", id);

            retResult.addParam(
                    new Param("redirectLink", URLFinder.getPathUrl(URLConstants.REDIRECTLINK, dcRequest), "String"));

            List<Callable<Result>> listOfCallable = Arrays.asList(new Callable<Result>() {
                @Override
                public Result call() {
                    Map<String, String> postParamMapGroup = new HashMap<>();
                    postParamMapGroup.put("Customer_id", id);
                    postParamMapGroup.put("Group_id", "DEFAULT_GROUP");
                    try {
                        HelperMethods.callApi(dcRequest, postParamMapGroup, HelperMethods.getHeaders(dcRequest),
                                URLConstants.CUSTOMER_GROUP_CREATE);
                    } catch (HttpCallException e) {
                        alert.prepareError(e.getMessage()).log();
                    }

                    return new Result();
                }
            }, new Callable<Result>() {
                @Override
                public Result call() {
                    if (StringUtils.isBlank(existingCustomerId)) {
                        CreateCustomerCommunication.invoke(inputParams, dcRequest);
                    }
                    return new Result();
                }
            }, new Callable<Result>() {
                @Override
                public Result call() {
                    createCustomerAddress(inputParams, result, dcRequest);
                    return new Result();
                }
            }, new Callable<Result>() {
                @Override
                public Result call() {
                    createBackendIdentifier(inputParams, result, dcRequest);
                    return new Result();
                }
            }, new Callable<Result>() {
                @Override
                public Result call() {
                    try {
                        return CreateCustomerTermsandConditionsPreLogin.invoke(id, DBPUtilitiesConstants.TNC_ENROLL,
                                null, dcRequest);
                    } catch (Exception e) {
                        alert.prepareError(e.getMessage()).log();
                    }
                    return new Result();
                }
            });

            try {
                ThreadExecutor.getExecutor(dcRequest).execute(listOfCallable);
            } catch (InterruptedException e) {
                alert.prepareError(e.getMessage()).log();
                Thread.currentThread().interrupt();

            }

        } else if (HelperMethods.hasError(result)) {
            ErrorCodeEnum.ERR_10044.setErrorCode(retResult);
        } else {
            ErrorCodeEnum.ERR_10045.setErrorCode(retResult);
        }

        return retResult;
    }

    private void createBackendIdentifier(Map<String, String> inputParams, Result result,
            DataControllerRequest dcRequest) {
        String backendIdentifierInfo = inputParams.get("backendIdentifierInfo");
        String customerId = inputParams.get("id");
        List<HashMap<String, String>> allRecords = HelperMethods.getAllRecordsMap(backendIdentifierInfo);

        for (HashMap<String, String> map : allRecords) {
            map.put("id", UUID.randomUUID().toString());
            map.put("Customer_id", customerId);
            try {
                HelperMethods.callApi(dcRequest, map, HelperMethods.getHeaders(dcRequest),
                        URLConstants.BACKENDIDENTIFIER_CREATE);
            } catch (HttpCallException e) {
                
                alert.prepareError(e.toString()).log();
            }
        }
    }

    private void createCustomerAddress(Map<String, String> inputParams, Result result,
            DataControllerRequest dcRequest) {

        String addressInformation = inputParams.get("addressInformation");
        if (StringUtils.isNotBlank(addressInformation)) {
            HashMap<String, String> hashMap = HelperMethods.getRecordMap(addressInformation);
            hashMap.put("id", inputParams.get("id"));
            CreateCustomerAddress.invoke(hashMap, dcRequest);
        }
    }

    private Result addErrorTOResult() {
        Result retResult = new Result();
        ErrorCodeEnum.ERR_10044.setErrorCode(retResult);
        return retResult;
    }
}
