package com.temenos.dbx.eum.product.security.backenddelegate.api;

import com.dbp.core.api.BackendDelegate;
import com.kony.dbp.exception.ApplicationException;
import com.temenos.dbx.product.dto.CaptchaDTO;

public interface CaptchaBackendDelegate extends BackendDelegate {
    public CaptchaDTO getEncodedImage(int captchaLength) throws ApplicationException;

    public boolean verifyCaptchaValue(String inputCaptchaValue, String generatedCaptchaValue, boolean isCaseSensitive)
            throws ApplicationException;

}
