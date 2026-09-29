package com.kony.dbputilities.customersecurityservices;

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
//import org.omg.CORBA.portable.ApplicationException;

import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodes;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.utils.ThreadExecutor;

public class CreateApplicant implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);

        if (dcRequest.getSource() != null) {
            for (Entry<String, String[]> entry : dcRequest.getSource().entrySet()) {
                String[] value = entry.getValue();
                inputParams.put(entry.getKey(), value[0]);
            }
        }

        if (preProcess(inputParams, dcRequest, result)) {
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.CUSTOMER_CREATE);

            result = postProcess(inputParams, result, dcRequest);
        }
        return result;
    }

    private Result postProcess(Map<String, String> inputParams, Result result, DataControllerRequest dcRequest) {

        Result retResult = new Result();
        if (HelperMethods.hasRecords(result)) {
            retResult.addParam(new Param("applicantID", HelperMethods.getFieldValue(result, "id"), "String"));
            inputParams.put("Customer_id", HelperMethods.getFieldValue(result, "id"));
            HelperMethods.setSuccessMsgwithCode("Applicant Created successfully", ErrorCodes.RECORD_CREATED, retResult);

            List<Callable<Result>> listOfCallable = Arrays.asList(new Callable<Result>() {
                @Override
                public Result call() throws ApplicationException {
                    createCustomerAddress(inputParams, result, dcRequest);
                    return new Result();
                }
            }, new Callable<Result>() {
                @Override
                public Result call() throws ApplicationException {
                    createCustomerCommunication(inputParams, result, dcRequest);
                    return new Result();
                }

            }, new Callable<Result>() {
                @Override
                public Result call() {
                    try {
                        return CreateCustomerTermsandConditionsPreLogin.invoke(inputParams.get("Customer_id"),
                                DBPUtilitiesConstants.TNC_ONBOARDING, null, dcRequest);
                    } catch (Exception e) {
                        alert.prepareError(e.getMessage()).log();
                    }
                    return new Result();
                }

            }, new Callable<Result>() {

                @Override
                public Result call() throws ApplicationException {
                    Map<String, String> postParamMapGroup = new HashMap<>();
                    postParamMapGroup.put("Customer_id", inputParams.get("Customer_id"));
                    postParamMapGroup.put("Group_id", "DEFAULT_GROUP");
                    try {
                        return HelperMethods.callApi(dcRequest, postParamMapGroup, HelperMethods.getHeaders(dcRequest),
                                URLConstants.CUSTOMER_GROUP_CREATE);
                    } catch (HttpCallException e) {
                        alert.prepareError(e.getMessage()).log();
                    }

                    return new Result();
                }

            }, new Callable<Result>() {
                @Override
                public Result call() throws ApplicationException {
                    createBackendIdentifier(inputParams, result, dcRequest);
                    return new Result();
                }

                private void createBackendIdentifier(Map<String, String> inputParams, Result result,
                        DataControllerRequest dcRequest) {
                    Map<String, String> input = new HashMap<String, String>();
                    String customerId = inputParams.get("Customer_id");
                    String coreCustomerId = inputParams.get("coreCustomerId");

                    if (StringUtils.isBlank(coreCustomerId)) {
                        return;
                    }

                    input.put("id", UUID.randomUUID().toString());
                    input.put("Customer_id", customerId);
                    input.put("BackendId", coreCustomerId);
                    input.put("BackendType", "CUSTOMER_ID");

                    try {
                        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                                URLConstants.BACKENDIDENTIFIER_CREATE);
                    } catch (HttpCallException e) {
                        alert.prepareError(e.getMessage()).log();
                    }

                }

            });

            try {
                ThreadExecutor.getExecutor(dcRequest).execute(listOfCallable);
            } catch (InterruptedException e) {
                alert.prepareError(e.getMessage()).log();
                Thread.currentThread().interrupt();
            }

        } else {
            HelperMethods.setValidationMsgwithCode("Applicant Creation Failed :" + HelperMethods.getError(result),
                    ErrorCodes.RECORD_NOT_CREATED, retResult);
            return retResult;
        }

        return retResult;
    }

    private void createCustomerCommunication(Map<String, String> inputParams, Result result,
            DataControllerRequest dcRequest) {

        String contactInformation = dcRequest.getParameter("contactInformation");
        if (StringUtils.isNotBlank(contactInformation)) {
            HashMap<String, String> hashMap = HelperMethods.getRecordMap(contactInformation);
            hashMap = getCommunicationInformation(hashMap);
            hashMap.put("id", inputParams.get("id"));
            CreateCustomerCommunication.invoke(hashMap, dcRequest);
        }
    }

    private void createCustomerAddress(Map<String, String> inputParams, Result result,
            DataControllerRequest dcRequest) {

        String addressInformation = dcRequest.getParameter("addressInformation");
        if (StringUtils.isNotBlank(addressInformation)) {
            HashMap<String, String> hashMap = HelperMethods.getRecordMap(addressInformation);
            hashMap.put("id", inputParams.get("id"));
            CreateCustomerAddress.invoke(hashMap, dcRequest);
        }
    }

    private static HashMap<String, String> getCommunicationInformation(Map<String, String> inputParams) {

        HashMap<String, String> map = new HashMap<>();
        map.put("Phone", inputParams.get("phoneNumber"));
        map.put("Email", inputParams.get("emailAddress"));
        return map;
    }

    private boolean preProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result) {
        boolean status = false;
        String personalInformation = dcRequest.getParameter("personalInformation");
        if (StringUtils.isNotBlank(personalInformation)) {
            HashMap<String, String> hashMap = HelperMethods.getRecordMap(personalInformation);
            inputParams.put("id", HelperMethods.generateUniqueCustomerId(dcRequest));
            inputParams.put("FirstName", hashMap.get("firstName"));
            inputParams.put("MiddleName", hashMap.get("middleName"));
            inputParams.put("LastName", hashMap.get("lastName"));
            if (hashMap.containsKey("userName")) {
                inputParams.put("UserName", hashMap.get("userName"));
            } else {
                inputParams.put("UserName", inputParams.get("id"));
            }

            inputParams.put("Status_id", "SID_CUS_NEW");
            String dob = parse(hashMap.get("dateofBirth"));
            if (StringUtils.isNotBlank(dob)) {
                inputParams.put("DateOfBirth", dob);
            }
            inputParams.put("Ssn", hashMap.get("SSN"));
            String identityInformation = dcRequest.getParameter("identityInformation");
            if (StringUtils.isNotBlank(identityInformation)) {
                hashMap = HelperMethods.getRecordMap(identityInformation);
                inputParams.put("IDType_id", hashMap.get("idType"));
                inputParams.put("IDValue", hashMap.get("idValue"));
            }

            HelperMethods.removeNullValues(inputParams);
            status = true;
        } else {
            HelperMethods.setValidationMsgwithCode("Applicant Creation Failed", ErrorCodes.RECORD_NOT_CREATED, result);
        }
        return status;
    }

    private String parse(String dateOfBirth) {

        if (StringUtils.isNotBlank(dateOfBirth)) {
            String[] arr = dateOfBirth.split("-");
            return arr[2] + "-" + arr[1] + "-" + arr[0];
        }
        return "";
    }
}