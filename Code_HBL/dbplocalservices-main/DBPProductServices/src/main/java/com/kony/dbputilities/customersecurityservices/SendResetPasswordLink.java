package com.kony.dbputilities.customersecurityservices;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.kms.KMSUtil;
import com.kony.dbputilities.util.DBPDatasetConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.LegalEntityUtil;
import com.kony.dbputilities.util.MWConstants;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.URLFinder;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.dto.CustomerCommunicationDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.usermanagement.backenddelegate.api.CommunicationBackendDelegate;

public class SendResetPasswordLink implements JavaService2 {

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        LegalEntityUtil.addCompanyIDToHeaders(dcRequest);
        String UserName = dcRequest.getParameter("UserName");
        Result user = getUser(dcRequest, UserName);
        String firstName = HelperMethods.getFieldValue(user, "FirstName");
        String lastName = HelperMethods.getFieldValue(user, "LastName");
        String id = HelperMethods.getFieldValue(user, "id");

        String activationToken = UUID.randomUUID().toString();
        Map<String, String> map = new HashMap<>();
        map.put("id", activationToken);
        map.put("UserName", UserName);
        map.put("linktype", HelperMethods.CREDENTIAL_TYPE.RESETPASSWORD.toString());
        map.put("createdts", HelperMethods.getCurrentTimeStamp());
        HelperMethods.callApi(dcRequest, map, HelperMethods.getHeaders(dcRequest),
                URLConstants.CREDENTIAL_CHECKER_CREATE);

        String link = URLFinder.getPathUrl(URLConstants.DBX_RESET_PWD_LINK, dcRequest) + "?qp="
                + encodeToBase64(activationToken + "_" + id + "_" + UserName);

        CommunicationBackendDelegate backendDelegate =
                DBPAPIAbstractFactoryImpl.getBackendDelegate(CommunicationBackendDelegate.class);
        CustomerCommunicationDTO customerCommunicationDTO = new CustomerCommunicationDTO();
        customerCommunicationDTO.setCustomer_id(id);
        DBXResult dbxresult =
                backendDelegate.getPrimaryMFACommunicationDetails(customerCommunicationDTO, dcRequest.getHeaderMap());
        JsonArray communicationArray = new JsonArray();
        communicationArray = ((JsonObject) dbxresult.getResponse()).getAsJsonObject()
                .get(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION).getAsJsonArray();
        String email = "";
        for (JsonElement jsonelement : communicationArray) {
            JsonObject object = jsonelement.getAsJsonObject();
            if ("COMM_TYPE_EMAIL".equalsIgnoreCase(JSONUtil.getString(object, "Type_id")))
                email = JSONUtil.getString(object, "Value");
        }
        PasswordHistoryManagement pm = new PasswordHistoryManagement(dcRequest, true);
        Map<String, String> input = new HashMap<>();
        JSONObject addtionalContext = new JSONObject();
        addtionalContext.put("resetPasswordLink", link);
        addtionalContext.put("linkExpiry", String.valueOf(Math.floorDiv(pm.getRecoveryEmailLinkValidity(), 60)));
        addtionalContext.put("firstName", firstName);
        input.put("Subscribe", "true");
        input.put("FirstName", firstName);
        input.put("EmailType", "resetPassword");
        input.put("LastName", lastName);
        input.put("AdditionalContext", KMSUtil.getOTPContent(null, null, addtionalContext));
        input.put("Email", email);
        Map<String, String> headers = HelperMethods.getHeaders(dcRequest);
        headers.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
        Result response = HelperMethods.callApi(dcRequest, input, headers, URLConstants.DBX_SEND_EMAIL_ORCH);
        return postProcess(response);
    }

    private Result postProcess(Result response) {
        Result result = new Result();
        if ("true".equals(HelperMethods.getParamValue(response.getParamByName("KMSemailStatus")))) {
            result.addParam(new Param("status", "Email sent successfully.", MWConstants.STRING));
        } else {
            String errorMsg = HelperMethods.getParamValue(response.getParamByName("KMSuserMsg"))
                    + HelperMethods.getParamValue(response.getParamByName("KMSemailMsg"));
            result.addParam(new Param("status", "Failed to send Email.", MWConstants.STRING));
            HelperMethods.setSuccessMsgwithCode(errorMsg, "10057", result);
        }
        return result;
    }

    private Result getUser(DataControllerRequest dcRequest, String userName) throws HttpCallException {
        String filter = "UserName" + DBPUtilitiesConstants.EQUAL + userName;
        return HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.CUSTOMERVERIFY_GET);
    }

    public static String encodeToBase64(String sourceString) {
        if (sourceString == null) {
            return null;
        }
        return new String(java.util.Base64.getEncoder().encode(sourceString.getBytes()));
    }

}
