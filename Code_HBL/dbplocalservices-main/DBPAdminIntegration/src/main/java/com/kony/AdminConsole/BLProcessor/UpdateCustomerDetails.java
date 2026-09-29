package com.kony.AdminConsole.BLProcessor;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.constants.DBPConstants;
import com.kony.AdminConsole.Utilities.AdminConsoleOperations;
import com.kony.AdminConsole.Utilities.CommonUtilities;
import com.kony.AdminConsole.Utilities.ServiceConfig;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.kony.dbputilities.utils.HelperMethods;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class UpdateCustomerDetails implements JavaService2 {

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {
        Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
        try {
            String ModifiedByName = requestInstance.getParameter("modifiedByName");

            Map<String, Object> inputParams = CommonUtilities.getInputMapFromInputArray(inputArray);
            String userName = (String) inputParams.get("userName");

            String addresses = requestInstance.getParameter("addresses");
            JSONArray Addresses = null;
            if (addresses != null) {
                Addresses = new JSONArray(addresses);
            }
            String emailIds = requestInstance.getParameter("emailIds");
            JSONArray EmailIds = null;
            if (emailIds != null) {
                EmailIds = new JSONArray(emailIds);
            }

            String phoneNumbers = requestInstance.getParameter("phoneNumbers");
            JSONArray PhoneNumbers = null;
            if (phoneNumbers != null) {
                PhoneNumbers = new JSONArray(phoneNumbers);
            }

            String deleteAddressID = requestInstance.getParameter("deleteAddressID");
            String deleteCommunicationID = requestInstance.getParameter("deleteCommunicationID");
            String PreferredContactMethod = requestInstance.getParameter("preferredContactMethod");
            String PreferredContactTime = requestInstance.getParameter("preferredContactTime");

            JSONObject getResponse = updateCustomerDetails(ModifiedByName, userName, Addresses, EmailIds, PhoneNumbers,
                    PreferredContactMethod, PreferredContactTime, deleteAddressID, deleteCommunicationID,
                    requestInstance);

            if (getResponse.getInt(DBPConstants.FABRIC_OPSTATUS_KEY) != 0) {
                String authToken = AdminConsoleOperations.login(requestInstance);
                ServiceConfig.setValue("Auth_Token", authToken);
                getResponse = updateCustomerDetails(ModifiedByName, userName, Addresses, EmailIds, PhoneNumbers,
                        PreferredContactMethod, PreferredContactTime, deleteAddressID, deleteCommunicationID,
                        requestInstance);
            }
            Result processedResult = HelperMethods.constructResultFromJSONObject(getResponse);
            return processedResult;

        } catch (Exception e) {
            Result res = new Result();
            res.addParam(new Param("errmsgg", "Exception occured when parsing data", "string"));
			alert.prepareError("trace for updateCustomerDetails :", e).log();
            return res;
        }

    }

    public JSONObject updateCustomerDetails(String modifiedByName, String userName, JSONArray addresses,
            JSONArray emailIds, JSONArray phoneNumbers, String preferredContactMethod, String preferredContactTime,
            String deleteAddressID, String deleteCommunicationID, DataControllerRequest dcRequest) {

        Map<String, Object> postParametersMap = new HashMap<>();
        postParametersMap.put("ModifiedByName", modifiedByName);
        postParametersMap.put("userName", userName);

        if (dcRequest.getParameter("addresses") != null) {
            postParametersMap.put("Addresses", addresses.toString());
        }
        if (dcRequest.getParameter("emailIds") != null) {
            postParametersMap.put("EmailIds", emailIds.toString());
        }
        if (dcRequest.getParameter("phoneNumbers") != null) {
            postParametersMap.put("PhoneNumbers", phoneNumbers.toString());
        }
        if (preferredContactMethod != null) {
            postParametersMap.put("PreferredContactMethod", preferredContactMethod);
        }
        if (preferredContactMethod != null) {
            postParametersMap.put("PreferredContactTime", preferredContactTime);
        }
        if (deleteAddressID != null) {
            postParametersMap.put("deleteAddressID", deleteAddressID);
        }
        if (deleteCommunicationID != null) {
            postParametersMap.put("deleteCommunicationID", deleteCommunicationID);
        }

        String getResponseString = HelperMethods.invokeC360ServiceAndGetString(dcRequest, postParametersMap,
                new HashMap<>(), "updateCustomerDetails");
        return CommonUtilities.getStringAsJSONObject(getResponseString);
    }

}
