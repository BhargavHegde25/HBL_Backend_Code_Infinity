package com.temenos.auth.security.businessdelegate.impl;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.google.gson.JsonObject;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.CryptoText;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.auth.security.backenddelegate.api.CaptchaBackendDelegate;
import com.temenos.auth.security.businessdelegate.api.CaptchaBusinessDelegate;
import com.temenos.dbx.product.dto.CaptchaDTO;

public class CaptchaBusinessDelegateImpl implements CaptchaBusinessDelegate {

    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public CaptchaDTO getEncodedImage(int captchaLength) throws ApplicationException {
        CaptchaBackendDelegate captchaBD = DBPAPIAbstractFactoryImpl
                .getBackendDelegate(CaptchaBackendDelegate.class);

        return captchaBD.getEncodedImage(captchaLength);
    }

    @Override
    public String generateEncodedCaptchaPayload(CaptchaDTO dto) throws ApplicationException {
        String encryptedPayload;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("captchaValue", dto.getCaptchaValue());
        try {
            encryptedPayload = CryptoText.encrypt(jsonObject.toString());
        } catch (Exception e) {
            alert.prepareError("Error occured while encrypting the captcha value").log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10342);
        }

        return encryptedPayload;
    }

    @Override
    public boolean verifyCaptchaValue(String inputCaptchaValue, String generatedCaptchaValue, boolean isCaseSensitive)
            throws ApplicationException {
        CaptchaBackendDelegate captchaBD = DBPAPIAbstractFactoryImpl
                .getBackendDelegate(CaptchaBackendDelegate.class);

        return captchaBD.verifyCaptchaValue(inputCaptchaValue, generatedCaptchaValue, isCaseSensitive);
    }

    @Override
    public String generateEncodedCaptchaPayloadForRetailUserEnrollment(CaptchaDTO dto) throws ApplicationException {
        String encryptedPayload;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("captchaValue", dto.getCaptchaValue());
        jsonObject.addProperty(DBPUtilitiesConstants.IS_RETAILUSER_ENROLLEMENT, dto.getCaptchaValue());
        try {
            encryptedPayload = CryptoText.encrypt(jsonObject.toString());
        } catch (Exception e) {
            alert.prepareError("Error occured while encrypting the captcha value").log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10807);
        }

        return encryptedPayload;
    }

}
