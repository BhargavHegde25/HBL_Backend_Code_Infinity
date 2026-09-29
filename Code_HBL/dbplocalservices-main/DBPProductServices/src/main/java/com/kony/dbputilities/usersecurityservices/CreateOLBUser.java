package com.kony.dbputilities.usersecurityservices;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
//import org.omg.CORBA.portable.ApplicationException;

import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.customersecurityservices.CreateCustomerCommunication;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.utils.ThreadExecutor;

public class CreateOLBUser implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);

        if (preProcess(inputParams, dcRequest, result)) {
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.USER_CREATE);
        }
        if (!HelperMethods.hasError(result)) {
            result = postProcess(dcRequest, result, inputParams);
        }

        return result;
    }

    private Result postProcess(DataControllerRequest dcRequest, Result result, Map<String, String> inputParams)
            throws Exception {
        String userId = getUserId(result);
        createAccount(dcRequest, userId, getAccountId(), "1");
        createAccount(dcRequest, userId, getAccountId(), "2");
        createCustomerCommunication(userId, dcRequest);
        inputParams.put("FirstName", inputParams.get("LastName"));
        inputParams.put("email", inputParams.get("LastName") + "@kony.com");
        inputParams.put("phone", "7889672345");
        ThreadExecutor.getExecutor(dcRequest).execute(new Callable<Result>() {
            @Override
            public Result call() throws ApplicationException {
                Map<String, String> postParamMapGroup = new HashMap<>();
                postParamMapGroup.put("Customer_id", inputParams.get("id"));
                postParamMapGroup.put("Group_id", "DEFAULT_GROUP");
                try {
                    return HelperMethods.callApi(dcRequest, postParamMapGroup, HelperMethods.getHeaders(dcRequest),
                            URLConstants.CUSTOMER_GROUP_CREATE);
                } catch (HttpCallException e) {
                    alert.prepareError(e.getMessage()).log();
                }

                return new Result();
            }

        });

        return result;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void createAccount(DataControllerRequest dcRequest, String userId, Object accountId, String accountType)
            throws HttpCallException {
        Map accountInput = new HashMap();
        accountInput.put(DBPUtilitiesConstants.PI_USR_ID, userId);
        accountInput.put("Type_id", accountType);
        accountInput.put("Account_id", accountId);
        HelperMethods.callApi(dcRequest, accountInput, HelperMethods.getHeaders(dcRequest),
                URLConstants.ACCOUNTS_CREATE);
    }

    private String getAccountId() {
        SimpleDateFormat idformatter = new SimpleDateFormat("yyMMddHHmmssSSS");
        return idformatter.format(new Date());
    }

    private String getUserId(Result result) {
        String id = "";
        Dataset ds = result.getAllDatasets().get(0);
        if (null != ds && !ds.getAllRecords().isEmpty()) {
            id = ds.getRecord(0).getParam(DBPUtilitiesConstants.U_ID).getValue();
        }
        return id;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result)
            throws ParseException, HttpCallException {
        String role = (String) inputParams.get(DBPUtilitiesConstants.ROLE);
        String password = (String) inputParams.get("password");
        if (!StringUtils.isNotBlank(role)) {
            inputParams.put(DBPUtilitiesConstants.ROLE, "BASIC");
        }
        String dob = (String) inputParams.get(DBPUtilitiesConstants.DOB);
        if (StringUtils.isBlank(dob)) {
            inputParams.remove(dob);
        } else {
            dob = HelperMethods.convertDateFormat(dob, null);
            inputParams.put(DBPUtilitiesConstants.DOB, dob);
        }
        String currentTime = HelperMethods.getCurrentTimeStamp();
        Map<String, String> customerTypes = HelperMethods.getCustomerTypes();
        inputParams.put("CustomerType_id", customerTypes.get("Customer"));
        inputParams.put("LastName", inputParams.get("userlastname"));
        inputParams.put("UserName", inputParams.get("userName"));
        inputParams.put("Password", password);
        inputParams.put("CurrentLoginTime", currentTime);
        inputParams.put("Lastlogintime", currentTime);
        inputParams.put(DBPUtilitiesConstants.IS_ENROLLED, "false");
        inputParams.put("isBillPayActivated", "false");
        inputParams.put("isP2PActivated", "false");
        inputParams.put("isBillPaySupported", "false");
        inputParams.put("isP2PSupported", "false");
        inputParams.put("isWireTransferEligible", "false");
        inputParams.put("isWireTransferActivated", "false");
        inputParams.remove("userName");
        inputParams.remove("userlastname");
        return true;
    }

    private void createCustomerCommunication(String userId, DataControllerRequest dcRequest) throws Exception {
        Map<String, String> input = new HashMap<>();
        input.put("Customer_id", userId);
        input.put("isPrimary", "1");
        input.put("Extension", DBPUtilitiesConstants.CUSTOMER_COMMUNICATION_PHONE_DEFAULT_EXTENSION);
        input.put("softdeleteflag", "0");
        input.put("createdby", "Infinity User");
        input.put("modifiedby", "Infinity User");
        input.put("id", HelperMethods.getNewId());
        input.put("Type_id", DBPUtilitiesConstants.COMM_TYPE_PHONE);
        input.put("Value", "7889672345");
        CreateCustomerCommunication.invoke(input, dcRequest);
        input.put("id", HelperMethods.getNewId());
        input.put("Type_id", DBPUtilitiesConstants.COMM_TYPE_EMAIL);
        input.put("Value", dcRequest.getParameter("userlastname") + "@kony.com");
        CreateCustomerCommunication.invoke(input, dcRequest);
    }
}