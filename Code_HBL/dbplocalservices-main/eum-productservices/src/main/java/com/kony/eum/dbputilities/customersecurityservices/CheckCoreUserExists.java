package com.kony.eum.dbputilities.customersecurityservices;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kony.dbputilities.util.DBPInputConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.IntegrationTemplateURLFinder;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.OperationName;
import com.kony.dbputilities.util.ServiceId;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.URLFinder;
import com.kony.eum.dbputilities.customersecurityservices.CheckCoreUserExists;
import com.kony.eum.dbputilities.util.ServiceCallHelper;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class CheckCoreUserExists implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override

    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        String customerlastname = inputParams.get(DBPInputConstants.CUSTOMER_LAST_NAME);
        String ssn = inputParams.get(DBPUtilitiesConstants.C_SSN);
        String dob = inputParams.get(DBPUtilitiesConstants.C_DOB);

        final String IS_Integrated = Boolean.toString(IntegrationTemplateURLFinder.isIntegrated);
        if (StringUtils.isNotBlank(IS_Integrated) && IS_Integrated.equalsIgnoreCase("true")) {

            return checkCoreUserInTransact(customerlastname, ssn, dob, HelperMethods.getAuthToken(dcRequest));

        }

        Result retResult = new Result();
        JsonObject coreCommunication = new JsonObject();
        JsonArray phone = new JsonArray();
        JsonArray email = new JsonArray();
        JsonObject contact = new JsonObject();
        if (preProcess(inputParams, dcRequest, result)) {
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.COREUSER_GET);
            if (HelperMethods.hasRecords(result)) {
                Dataset ds = result.getAllDatasets().get(0);
                for (Record communication : ds.getAllRecords()) {
                    contact = new JsonObject();
                    contact.addProperty("unmasked", HelperMethods.getFieldValue(communication, "phone"));
                    contact.addProperty("isPrimary", "true");
                    phone.add(contact);
                    contact = new JsonObject();
                    contact.addProperty("unmasked", HelperMethods.getFieldValue(communication, "email"));
                    contact.addProperty("isPrimary", "true");
                    email.add(contact);
                }

            } else {
                contact = new JsonObject();
                contact.addProperty("unmasked", URLFinder.getPathUrl(URLConstants.ENROLL_PHONE, dcRequest));
                contact.addProperty("isPrimary", "true");
                phone.add(contact);
                contact = new JsonObject();
                contact.addProperty("unmasked", URLFinder.getPathUrl(URLConstants.ENROLL_EMAIL, dcRequest));
                contact.addProperty("isPrimary", "true");
                email.add(contact);
            }
            coreCommunication.add("phone", phone);
            coreCommunication.add("email", email);
            String coreCommunicationString = coreCommunication.toString();

            retResult.addParam(new Param("coreCommunication", coreCommunicationString, "String"));
            retResult.addParam(new Param("isUserExists", "true", "String"));
        } else {
            ErrorCodeEnum.ERR_10183.setErrorCode(result);
            Param p = new Param(DBPUtilitiesConstants.VALIDATION_ERROR, "Please provide valid Details.",
                    DBPUtilitiesConstants.STRING_TYPE);
            result.addParam(p);
        }

        return retResult;
    }

    private Result checkCoreUserInTransact(String cLastName, String ssn, String dateOfBirth, String konyAuthToken) {

        String serviceId = ServiceId.T24ISEXTRA_INTEGRATION_SERVICE;
        String operationName = OperationName.GET_CORE_USER_EXISTS;

        Map<String, Object> inputParams = new HashMap<String, Object>();

        inputParams.put("LastName", cLastName);
        inputParams.put("Ssn", ssn);
        inputParams.put("DateOfBirth", dateOfBirth);

        Map<String, Object> headerMap = new HashMap<>();

        JsonObject jsonobject = ServiceCallHelper.invokeServiceAndGetJson(serviceId, null, operationName,
                inputParams, headerMap, konyAuthToken);
        if (JSONUtil.isJsonNull(jsonobject)) {

            Result result = new Result();
            ErrorCodeEnum.ERR_10183.setErrorCode(result);
            Param p = new Param(DBPUtilitiesConstants.VALIDATION_ERROR, "Please check if the Core User Exists.",
                    DBPUtilitiesConstants.STRING_TYPE);
            result.addParam(p);
            return result;
        }
        return JSONToResult.convert(jsonobject.toString());
    }

    private boolean preProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result) {
        boolean status = true;
        String customerlastname = inputParams.get(DBPInputConstants.CUSTOMER_LAST_NAME);
        String ssn = inputParams.get(DBPUtilitiesConstants.C_SSN);
        String dob = inputParams.get(DBPUtilitiesConstants.C_DOB);

        if (StringUtils.isBlank(customerlastname) || StringUtils.isBlank(ssn) || StringUtils.isBlank(dob)) {
            return false;
        }

        boolean isAdded = false;
        StringBuilder sb = new StringBuilder();
        try {

            dob = HelperMethods.convertDateFormat(dob, null);
        } catch (ParseException e) {
            // log.error("Date format conversion exception");
            dob = "";
            status = false;
            alert.prepareError(e.getMessage()).log();
        }
        if (StringUtils.isNotBlank(dob)) {
            sb.append("userLastName").append(DBPUtilitiesConstants.EQUAL).append(customerlastname);
            sb.append(DBPUtilitiesConstants.AND);
            sb.append("ssn").append(DBPUtilitiesConstants.EQUAL).append(ssn);
            sb.append(DBPUtilitiesConstants.AND);
            sb.append("dateOfBirth").append(DBPUtilitiesConstants.EQUAL).append(dob);
            isAdded = true;
        }
        if (isAdded) {
            inputParams.put(DBPUtilitiesConstants.FILTER, sb.toString());
        }
        return status;
    }

}
