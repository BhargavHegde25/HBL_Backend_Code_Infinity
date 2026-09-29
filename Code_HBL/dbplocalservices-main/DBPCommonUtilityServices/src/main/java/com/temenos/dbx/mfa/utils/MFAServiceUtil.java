package com.temenos.dbx.mfa.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import com.dbp.core.api.DBPDTO;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.mfa.MFAConstants;
import com.kony.dbputilities.util.CryptoText;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.URLFinder;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.temenos.dbx.mfa.dto.MFAServiceDTO;
import com.temenos.dbx.product.utils.DTOUtils;

public class MFAServiceUtil {

    MFAServiceDTO mfaServiceDTO = null;

    private static Alert alert;

    private static void initLOG() {
        if (alert == null) {
            alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
        }
    }

    public MFAServiceDTO getMfaServiceDTO() {
        return mfaServiceDTO;
    }

    public void setMfaServiceDTO(MFAServiceDTO mfaServiceDTO) {
        this.mfaServiceDTO = mfaServiceDTO;
    }

    public MFAServiceUtil() {
        initLOG();
    }

    public MFAServiceUtil(Result mfaServiceResult) {
        initLOG();
        Dataset dataset = mfaServiceResult.getDatasetById("mfaservice");
        Record record = dataset.getAllRecords().get(0);
        mfaServiceDTO = (MFAServiceDTO) DTOUtils.getDTOFromRecord(record, MFAServiceDTO.class, true, false);
    }

    public MFAServiceUtil(MFAServiceDTO mfaServiceDTO) {
        initLOG();
        this.mfaServiceDTO = mfaServiceDTO;
    }

    public MFAServiceUtil(JsonObject mfaServiceObject) {
        initLOG();

        if (JSONUtil.isJsonNotNull(mfaServiceObject)
                && JSONUtil.hasKey(mfaServiceObject, "mfaservice")
                && mfaServiceObject.get("mfaservice").isJsonArray()) {
            JsonArray mfaServiceArray = mfaServiceObject.get("mfaservice").getAsJsonArray();
            JsonElement element = mfaServiceArray.size() > 0 ? mfaServiceArray.get(0) : new JsonObject();
            mfaServiceDTO = (MFAServiceDTO) DTOUtils.loadJsonObjectIntoObject(element.getAsJsonObject(),
                    MFAServiceDTO.class, true);
        }
    }

    public void loadDTO(DataControllerRequest dcRequest, String filter) {

        List<DBPDTO> dbpdtos = DTOUtils.getDTOListfromDB(dcRequest, filter, URLConstants.MFA_SERVICE_GET, true, false);

        if (dbpdtos.size() > 0) {
            mfaServiceDTO = (MFAServiceDTO) (dbpdtos.get(0));
        }
    }

    public void loadDTO(FabricRequestManager requestManager, String filter) {

        List<DBPDTO> dbpdtos = DTOUtils.getDTOListfromDB(requestManager, filter, URLConstants.MFA_SERVICE_GET, true, false);

        if (dbpdtos.size() > 0) {
            mfaServiceDTO = (MFAServiceDTO) (dbpdtos.get(0));
        }
    }

    public JsonArray getSecurityQuestionsArrayfromDB() {

        String securityQuestionsString = mfaServiceDTO.getSecurityQuestions();

        JsonArray array = new JsonArray();

        if (StringUtils.isNotBlank(securityQuestionsString)) {
            try {
                securityQuestionsString = CryptoText.decrypt(securityQuestionsString);
                array = new JsonParser().parse(securityQuestionsString).getAsJsonArray();
            } catch (Exception e) {
                alert.prepareError(e.getMessage()).log();
            }
        }
        return array;

    }

    public JsonObject getSecurityQuestionsJsonfromDB() {

        String securityQuestionsString = mfaServiceDTO.getSecurityQuestions();

        JsonObject jsonObject = new JsonObject();

        if (StringUtils.isNotBlank(securityQuestionsString)) {
            try {
                securityQuestionsString = CryptoText.decrypt(securityQuestionsString);
                jsonObject = new JsonParser().parse(securityQuestionsString).getAsJsonObject();
            } catch (Exception e) {
                alert.prepareError(e.getMessage()).log();
            }
        }
        return jsonObject;

    }

    public int getSecurityQuestionsRetry() {
        return Integer.parseInt(mfaServiceDTO.getRetryCount());
    }

    public JSONObject getMFAMODE() {

        String str = mfaServiceDTO.getPayload();

        JSONObject jsonObject = new JSONObject();

        if (StringUtils.isNotBlank(str)) {
            try {
                jsonObject = new JSONObject(CryptoText.decrypt(str));
            } catch (Exception e) {
                alert.prepareError("Caught exception while Decrypting : ", e).log();
            }
        }

        return jsonObject.getJSONObject("mfaMode");
    }

    public static String getServiceKey(JsonObject requestpayload, JsonElement securityQuestions, String user_id,
            JsonElement jsonElement, FabricRequestManager requestManager, DataControllerRequest dcRequest,
            Dataset securityQuestionsDataset) {
        Map<String, String> map = getServiceMap(requestpayload, user_id, jsonElement);

        if (securityQuestions != null) {
            try {
                map.put(MFAConstants.SECURITY_QUESTIONS_DB, CryptoText.encrypt(securityQuestions.toString()));
            } catch (Exception e) {
                alert.prepareError(e.getMessage()).log();
            }
        } else if (securityQuestionsDataset != null) {

            try {
                map.put(MFAConstants.SECURITY_QUESTIONS_DB,
                        CryptoText.encrypt(ResultToJSON.convertDataset(securityQuestionsDataset).toString()));
            } catch (Exception e) {
                alert.prepareError(e.getMessage()).log();
            }
        }

        Result result = null;
        if (requestManager != null) {
            result = HelperMethods.callApi(requestManager, map, HelperMethods.getHeaders(requestManager),
                    URLConstants.MFA_SERVICE_CREATE);
        } else {
            try {
                result = HelperMethods.callApi(dcRequest, map, HelperMethods.getHeaders(dcRequest),
                        URLConstants.MFA_SERVICE_CREATE);
            } catch (HttpCallException e) {
                alert.prepareError("Caught exception while creating MFAServiceKey : ", e).log();
            }
        }

        if (HelperMethods.hasRecords(result)) {
            return HelperMethods.getFieldValue(result, MFAConstants.SERVICE_KEY);
        }

        return null;
    }

    private static Map<String, String> getServiceMap(JsonObject requestpayload, String user_id,
            JsonElement jsonElement) {

        Map<String, String> mfaservice = new HashMap<>();
        mfaservice.put(MFAConstants.SERVICE_KEY, HelperMethods.getNewId());
        if (requestpayload.has(MFAConstants.MFA_ATTRIBUTES)
                && requestpayload.get(MFAConstants.MFA_ATTRIBUTES).getAsJsonObject().has(MFAConstants.SERVICE_NAME)) {
            mfaservice.put(MFAConstants.SERVICE_NAME, requestpayload.get(MFAConstants.MFA_ATTRIBUTES).getAsJsonObject()
                    .get(MFAConstants.SERVICE_NAME).getAsString());
        }
        mfaservice.put("User_id", user_id);
        mfaservice.put("Createddts", HelperMethods.getCurrentTimeStamp());
        mfaservice.put(MFAConstants.RETRY_COUNT, "0");
        requestpayload.add("mfaMode", jsonElement);
        try {
            mfaservice.put("payload", CryptoText.encrypt(requestpayload.toString()));
        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
        }
        return mfaservice;
    }

    public JsonObject getRequestPayload() {
        String payload = mfaServiceDTO.getPayload();

        if (StringUtils.isNotBlank(payload)) {
            try {
                return new JsonParser().parse(CryptoText.decrypt(payload)).getAsJsonObject();
            } catch (Exception e) {
                alert.prepareError(e.getMessage()).log();
            }
        }

        return new JsonObject();
    }

    public boolean isValidServiceKey(int minutes) {
        return mfaServiceDTO != null && !isServiceKeyExpired(minutes);
    }

    private boolean isServiceKeyExpired(int minutes) {

        String string = mfaServiceDTO.getCreateddts();

        if (StringUtils.isNotBlank(string)) {
            Date createdts = HelperMethods.getFormattedTimeStamp(string);
            Calendar generatedCal = Calendar.getInstance();
            generatedCal.setTime(createdts);

            Date verifyDate = new Date();
            Calendar verifyingCal = Calendar.getInstance();
            verifyingCal.setTime(verifyDate);

            generatedCal.add(Calendar.MINUTE, minutes);

            long GeneratedMilliSeconds = generatedCal.getTimeInMillis();
            long verifyingMilliSeconds = verifyingCal.getTimeInMillis();

            if (GeneratedMilliSeconds > verifyingMilliSeconds) {
                return false;
            }
        }

        return true;
    }

    public boolean isStateVerified() {
        return Boolean.parseBoolean(mfaServiceDTO.getIsVerified());
    }

    public int getServiceKeyExpiretime(FabricRequestManager requestManager) {
        try {
            return Integer.parseInt(URLFinder.getPathUrl(URLConstants.SERVICEKEY_EXPIRE_TIME, requestManager));
        } catch (Exception e) {
        }
        return 10;
    }

    public int getServiceKeyExpiretime(DataControllerRequest dcRequest) {
        try {
            return Integer.parseInt(URLFinder.getPathUrl(URLConstants.SERVICEKEY_EXPIRE_TIME, dcRequest));
        } catch (Exception e) {
        }
        return 10;
    }

}