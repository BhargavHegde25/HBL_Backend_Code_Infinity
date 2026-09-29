package com.temenos.auth.security.businessdelegate.api;

import com.dbp.core.api.BusinessDelegate;
import com.kony.dbp.exception.ApplicationException;
import com.temenos.dbx.product.dto.CaptchaDTO;

public interface CaptchaBusinessDelegate extends BusinessDelegate {

    /**
     * 
     * @param captchaLength
     * @return
     * @throws ApplicationException
     */
    public CaptchaDTO getEncodedImage(int captchaLength) throws ApplicationException;

    /**
     * 
     * @param dto
     * @return
     * @throws ApplicationException
     */
    public String generateEncodedCaptchaPayload(CaptchaDTO dto) throws ApplicationException;

    /**
     * 
     * @param inputCaptchaValue
     * @param generatedCaptchaValue
     * @param isCaseSensitive
     * @return
     * @throws ApplicationException
     */
    public boolean verifyCaptchaValue(String inputCaptchaValue, String generatedCaptchaValue, boolean isCaseSensitive)
            throws ApplicationException;

    /**
     * 
     * @param dto
     * @return
     * @throws ApplicationException
     */
    public String generateEncodedCaptchaPayloadForRetailUserEnrollment(CaptchaDTO dto) throws ApplicationException;
}
