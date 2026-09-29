package com.kony.dbputilities.customersecurityservices.preprocessor;

import java.util.List;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbputilities.mfa.MFAConstants;
import com.kony.dbputilities.util.CryptoText;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.JSONUtil;
import com.konylabs.middleware.api.processor.FabricRequestChain;
import com.konylabs.middleware.api.processor.PayloadHandler;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePreProcessor;
import com.temenos.dbx.mfa.businessdelegate.api.MFAServiceBusinessDelegate;
import com.temenos.dbx.mfa.dto.MFAServiceDTO;
import com.temenos.dbx.product.dto.CaptchaDTO;
import com.temenos.dbx.product.security.businessdelegate.api.CaptchaBusinessDelegate;

public class CaptchaVerifyPreprocessor implements ObjectServicePreProcessor {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public void execute(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager,
            FabricRequestChain fabricRequestChain) throws Exception {
		Log4j2Configurator.getInstance();
        final String INPUTPARAM_CAPTCHAVALUE = "captchaValue";
        final String MFASERVICE_PAYLOAD_CAPTCHA = "captchaValue";
        PayloadHandler requestPayloadHandler = fabricRequestManager.getPayloadHandler();
        PayloadHandler responsePayloadHandler = fabricResponseManager.getPayloadHandler();

        JsonObject requestpayload = requestPayloadHandler.getPayloadAsJson().getAsJsonObject();
        JsonObject responsePayload;
        String serviceKey = requestpayload.has(MFAConstants.SERVICE_KEY)
                && !requestpayload.get(MFAConstants.SERVICE_KEY).isJsonNull()
                        ? requestpayload.get(MFAConstants.SERVICE_KEY).getAsString()
                        : null;
        String captchaValue =
                requestpayload.has(INPUTPARAM_CAPTCHAVALUE) && !requestpayload.get(INPUTPARAM_CAPTCHAVALUE).isJsonNull()
                        ? requestpayload.get(INPUTPARAM_CAPTCHAVALUE).getAsString()
                        : null;
        String encryptedPayload;
        String storedCaptchaValue;

        if (StringUtils.isBlank(serviceKey) || StringUtils.isBlank(captchaValue)) {
            responsePayload = new JsonObject();
            ErrorCodeEnum.ERR_10343.setErrorCode(responsePayload);
            responsePayloadHandler.updatePayloadAsJson(responsePayload);
            return;
        }

        MFAServiceBusinessDelegate mfaserviceBD = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class)
                .getBusinessDelegate(MFAServiceBusinessDelegate.class);
        MFAServiceDTO dto = new MFAServiceDTO();
        dto.setServiceKey(serviceKey);

        List<MFAServiceDTO> dtoList = mfaserviceBD.getMfaService(dto, null, null);
        if (null == dtoList || dtoList.isEmpty() || StringUtils.isBlank(dtoList.get(0).getServiceKey())) {
            responsePayload = new JsonObject();
            ErrorCodeEnum.ERR_10344.setErrorCode(responsePayload);
            responsePayloadHandler.updatePayloadAsJson(responsePayload);
            return;
        }

        dto = dtoList.get(0);
        encryptedPayload = dto.getPayload();

        if (StringUtils.isBlank(encryptedPayload)) {
            responsePayload = new JsonObject();
            ErrorCodeEnum.ERR_10344.setErrorCode(responsePayload);
            responsePayloadHandler.updatePayloadAsJson(responsePayload);
            return;
        }

        String payloadString = CryptoText.decrypt(encryptedPayload);
        JsonParser parser = new JsonParser();
        JsonObject json = parser.parse(payloadString).isJsonObject() ? parser.parse(payloadString).getAsJsonObject()
                : null;
        if (JSONUtil.isJsonNull(json) || !JSONUtil.hasKey(json, MFASERVICE_PAYLOAD_CAPTCHA)
                || !json.get(MFASERVICE_PAYLOAD_CAPTCHA).isJsonPrimitive()
                || StringUtils.isBlank(json.get(MFASERVICE_PAYLOAD_CAPTCHA).getAsString())) {
            responsePayload = new JsonObject();
            ErrorCodeEnum.ERR_10344.setErrorCode(responsePayload);
            responsePayloadHandler.updatePayloadAsJson(responsePayload);
            return;
        }

        storedCaptchaValue = json.get(MFASERVICE_PAYLOAD_CAPTCHA).getAsString();
        if (storedCaptchaValue.equalsIgnoreCase(captchaValue)) {
            mfaserviceBD.deleteMfaService(serviceKey, null);
            fabricRequestChain.execute();
        } else {
            CaptchaBusinessDelegate captchaBD = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(BusinessDelegateFactory.class)
                    .getBusinessDelegate(CaptchaBusinessDelegate.class);
            responsePayload = new JsonObject();

            CaptchaDTO captchaDTO = captchaBD.getEncodedImage(DBPUtilitiesConstants.DEFAULT_CAPTCHA_LENGTH);
            String encrptedPayload = captchaBD.generateEncodedCaptchaPayload(captchaDTO);
            dto = new MFAServiceDTO();
            dto.setServiceKey(serviceKey);
            dto.setPayload(encrptedPayload);
            mfaserviceBD.updateMfaService(dto, null);
            responsePayload.addProperty("encodedImage", captchaDTO.getEncodedCaptcha());
            ErrorCodeEnum.ERR_10345.setErrorCode(responsePayload);
            responsePayloadHandler.updatePayloadAsJson(responsePayload);
        }

    }
}
