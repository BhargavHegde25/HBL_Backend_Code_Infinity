package com.temenos.mfa.businessdelegate.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import org.apache.commons.lang3.StringUtils;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.CryptoText;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.temenos.dbx.mfa.businessdelegate.api.MFAServiceBusinessDelegate;
import com.temenos.dbx.mfa.dto.MFAServiceDTO;
import com.temenos.dbx.mfa.utils.MFAServiceUtil;
import com.temenos.dbx.product.utils.DTOUtils;

public class MFAServiceBusinessDelegateImpl implements MFAServiceBusinessDelegate {

    LoggerUtil logger = new LoggerUtil(MFAServiceBusinessDelegateImpl.class);
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    
    @Override
    public MFAServiceDTO createMfaService(String serviceName, String phone, String email, String requestJson,
            Map<String, Object> headerMap) throws ApplicationException {
        MFAServiceDTO dto = null;
        try {
            if (StringUtils.isBlank(serviceName) || StringUtils.isBlank(phone) || StringUtils.isBlank(email)) {
                return null;
            }
            Map<String, Object> inputParams = new HashMap<>();
            String createddts = HelperMethods.getCurrentTimeStamp();
            inputParams.put("serviceKey", UUID.randomUUID().toString());
            inputParams.put("serviceName", serviceName);
            inputParams.put("payload", generatePayload(phone, email, requestJson));
            inputParams.put("Createddts", createddts);

            JsonObject mfaJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
                    URLConstants.MFA_SERVICE_CREATE);
            MFAServiceUtil util = new MFAServiceUtil(mfaJson);
            dto = util.getMfaServiceDTO();

        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10235);
        }

        return dto;
    }

    private String generatePayload(String phone, String email, String requestJson) {
        JsonObject payloadJson = new JsonObject();
        JsonObject communicationJson = new JsonObject();
        JsonArray phoneArray = new JsonArray();
        JsonArray emailArray = new JsonArray();
        JsonObject phoneRecord = new JsonObject();
        JsonObject emailRecord = new JsonObject();
        emailRecord.addProperty("unmasked", email);
        emailRecord.addProperty("isPrimary", "true");
        emailArray.add(emailRecord);
        String processedPhone = process(phone);
        phoneRecord.addProperty("unmasked", processedPhone);
        phoneRecord.addProperty("isPrimary", "true");
        phoneArray.add(phoneRecord);
        communicationJson.add("phone", phoneArray);
        communicationJson.add("email", emailArray);
        payloadJson.add("communication", communicationJson);
        JsonElement element = getJsonObject(requestJson);
        payloadJson.add("requestPayload", element);
        if (JSONUtil.isJsonNotNull(element) && element.isJsonObject()
                && JSONUtil.hasKey(element.getAsJsonObject(), DBPUtilitiesConstants.BACKENDID)) {
            payloadJson.addProperty(DBPUtilitiesConstants.BACKENDID,
                    element.getAsJsonObject().get(DBPUtilitiesConstants.BACKENDID).getAsString());
        }
        String payload = payloadJson.toString();
        try {
            payload = CryptoText.encrypt(payload);
        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
        }

        return payload;
    }

    private JsonElement getJsonObject(String requestJson) {
        JsonParser parser = new JsonParser();
        return StringUtils.isNotBlank(requestJson) && parser.parse(requestJson).isJsonObject()
                ? parser.parse(requestJson).getAsJsonObject()
                : new JsonObject();

    }

    private String process(String phone) {
        if (!StringUtils.contains(phone, "-")) {
            phone = "+91-" + phone;

        }
        return phone;
    }

    @Override
    public List<MFAServiceDTO> getMfaService(MFAServiceDTO inputDTO, Map<String, Object> inputParams,
            Map<String, Object> headersMap) throws ApplicationException {

        List<MFAServiceDTO> mfaserviceDTO = new ArrayList<>();
        boolean isAppendAnd = Boolean.FALSE;
        StringBuilder sb = new StringBuilder();
        if (inputParams != null && inputParams.containsKey("masterServiceKey")) {
            sb.append("serviceKey" + DBPUtilitiesConstants.EQUAL + inputParams.get("masterServiceKey").toString()
                    + DBPUtilitiesConstants.OR);
            inputParams.clear();
        }
        if (StringUtils.isNotBlank(inputDTO.getServiceKey())) {
            sb.append("serviceKey" + DBPUtilitiesConstants.EQUAL + inputDTO.getServiceKey());
            isAppendAnd = Boolean.TRUE;
        }
        if (StringUtils.isNotBlank(inputDTO.getServiceName())) {
            if (isAppendAnd)
                sb.append(DBPUtilitiesConstants.AND);
            sb.append("serviceName" + DBPUtilitiesConstants.EQUAL + inputDTO.getServiceName());
            isAppendAnd = Boolean.TRUE;
        }
        if (StringUtils.isNotBlank(inputDTO.getIsVerified())) {
            if (isAppendAnd)
                sb.append(DBPUtilitiesConstants.AND);
            sb.append("isVerified" + DBPUtilitiesConstants.EQUAL + inputDTO.getIsVerified());
            isAppendAnd = Boolean.TRUE;
        }

        inputParams = new HashMap<String, Object>();
        inputParams.put(DBPUtilitiesConstants.FILTER, sb.toString());

        JsonObject response = new JsonObject();

        try {
            response = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap, URLConstants.MFA_SERVICE_GET);
        } catch (Exception e) {
            alert.prepareError("Exception occured while fetching mfa service details" + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10242);
        }

        if (!response.has("opstatus") || response.get("opstatus").getAsInt() != 0 || !response.has("mfaservice")) {
            alert.prepareError("Exception occured while fetching mfa service details").log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10242);
        }

        if (response.get("mfaservice").getAsJsonArray().size() == 0) {
            alert.prepareError("No data with the provided service key").log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10243);
        }

        try {
            for (JsonElement mfaserviceJson : response.get("mfaservice").getAsJsonArray()) {
                if (mfaserviceJson != null) {
                    mfaserviceDTO.add((MFAServiceDTO) DTOUtils
                            .loadJsonObjectIntoObject(mfaserviceJson.getAsJsonObject(), MFAServiceDTO.class, true));
                }
            }
        } catch (Exception e) {
            alert.prepareError("Exception occured while parsing the mfa response").log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10242);
        }
        return mfaserviceDTO;
    }

    @Override
    public boolean updateMfaService(MFAServiceDTO mfaserviceDTO, Map<String, Object> headersMap)
            throws ApplicationException {
        boolean isUpdateSuccess = false;

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("serviceKey", mfaserviceDTO.getServiceKey());
        if (StringUtils.isNotBlank(mfaserviceDTO.getPayload())) {
            inputParams.put("payload", mfaserviceDTO.getPayload());
        }
        if (StringUtils.isNotBlank(mfaserviceDTO.getIsVerified())) {
            inputParams.put("isVerified", mfaserviceDTO.getIsVerified());
        }

        JsonObject response = new JsonObject();

        try {
            response = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                    URLConstants.MFA_SERVICE_UPDATE);
        } catch (Exception e) {
            alert.prepareError("Exception occured while updating mfa service" + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10244);
        }

        if (!response.has("opstatus") || response.get("opstatus").getAsInt() != 0 || !response.has("updatedRecords")) {
            alert.prepareError("Exception occured while updating mfa service").log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10244);
        }

        if (response.get("updatedRecords").getAsInt() == 1) {
            isUpdateSuccess = true;
        }
        return isUpdateSuccess;
    }

    @Override
    public boolean deleteMfaService(String serviceKey, Map<String, Object> headersMap) throws ApplicationException {
        if (StringUtils.isBlank(serviceKey)) {
            return false;
        }
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("serviceKey", serviceKey);
        JsonObject response =
                ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap, URLConstants.MFA_SERVICE_DELETE);
        return (JSONUtil.isJsonNotNull(response) && JSONUtil.hasKey(response, "deletedRecords")
                && !JSONUtil.hasKey(response, "errmsg"));
    }

    @Override
    public boolean validateMasterServiceKeyAndServiceKey(JsonObject masterServiceKeyPayloadJson,
            JsonObject serviceKeyPayloadJson) {
        final String REQUEST_PAYLOAD_PARAM_SSN = "Ssn";
        final String REQUEST_PAYLOAD_PARAM_DOB = "DateOfBirth";
        final String REQUEST_PAYLOAD_PARAM_LASTNAME = "LastName";
        String masterSsn =
                masterServiceKeyPayloadJson.has(REQUEST_PAYLOAD_PARAM_SSN)
                        ? masterServiceKeyPayloadJson.get(REQUEST_PAYLOAD_PARAM_SSN).getAsString()
                        : "";
        String masterDOB = masterServiceKeyPayloadJson.has(REQUEST_PAYLOAD_PARAM_DOB)
                ? masterServiceKeyPayloadJson.get(REQUEST_PAYLOAD_PARAM_DOB).getAsString()
                : "";
        String masterLastName =
                masterServiceKeyPayloadJson.has(REQUEST_PAYLOAD_PARAM_LASTNAME)
                        ? masterServiceKeyPayloadJson.get(REQUEST_PAYLOAD_PARAM_LASTNAME).getAsString()
                        : "";
        String ssn = serviceKeyPayloadJson.has(REQUEST_PAYLOAD_PARAM_SSN)
                ? serviceKeyPayloadJson.get(REQUEST_PAYLOAD_PARAM_SSN).getAsString()
                : "";
        String dateOfBirth =
                serviceKeyPayloadJson.has(REQUEST_PAYLOAD_PARAM_DOB)
                        ? serviceKeyPayloadJson.get(REQUEST_PAYLOAD_PARAM_DOB).getAsString()
                        : "";
        String lastName = serviceKeyPayloadJson.has(REQUEST_PAYLOAD_PARAM_LASTNAME)
                ? serviceKeyPayloadJson.get(REQUEST_PAYLOAD_PARAM_LASTNAME).getAsString()
                : "";

        return StringUtils.isNotBlank(ssn) && StringUtils.isNotBlank(dateOfBirth) && StringUtils.isNotBlank(lastName)
                && ssn.equalsIgnoreCase(masterSsn) && dateOfBirth.equalsIgnoreCase(masterDOB)
                && lastName.equalsIgnoreCase(masterLastName);
    }

    @Override
    public boolean updateGivenServiceKeyRecord(JsonObject payloadJson, String serviceKey,
            Map<String, Object> headersMap) throws ApplicationException {
        MFAServiceDTO dto = new MFAServiceDTO();
        String encryptedData;
        try {
            encryptedData = CryptoText.encrypt(payloadJson.toString());
            dto.setPayload(encryptedData);
            dto.setServiceKey(serviceKey);
            MFAServiceBusinessDelegate mfaServiceBD = DBPAPIAbstractFactoryImpl
                    .getBusinessDelegate(MFAServiceBusinessDelegate.class);
            return mfaServiceBD.updateMfaService(dto, headersMap);
        } catch (ApplicationException e) {
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            diagnostic.prepareDebug("Error occured while encrypting the payload").log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10336);
        }
    }

    @Override
    public MFAServiceDTO create(MFAServiceDTO mfaDTO, Map<String, Object> headersMap) throws ApplicationException {
        Map<String, Object> inputParams = new HashMap<>();
        MFAServiceDTO responseDTO = new MFAServiceDTO();
        try {
            String isVerified = "true";
            if (StringUtils.isNotBlank(mfaDTO.getIsVerified())) {
                isVerified = mfaDTO.getIsVerified();
            }
            inputParams.put("serviceKey", UUID.randomUUID().toString());
            inputParams.put("payload", mfaDTO.getPayload());
            inputParams.put("User_id", mfaDTO.getUser_());
            inputParams.put("serviceName", mfaDTO.getServiceName());
            inputParams.put("isVerified", isVerified);
            inputParams.put("Createddts", HelperMethods.getCurrentTimeStamp());
            ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                    URLConstants.MFA_SERVICE_CREATE);
            responseDTO.setServiceKey(inputParams.get("serviceKey").toString());
        } catch (Exception e) {
            alert.prepareError("Exception occured while creating entry in MFA Service table" + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10235);
        }
        return responseDTO;
    }
}
