package com.temenos.dbx.product.dto;

import com.dbp.core.api.DBPDTO;

public class CaptchaDTO implements DBPDTO {
    /**
     * 
     */
    private static final long serialVersionUID = -3261035127914205350L;
    private String captchaValue;
    private String encodedCaptcha;

    public String getCaptchaValue() {
        return captchaValue;
    }

    public void setCaptchaValue(String captchaValue) {
        this.captchaValue = captchaValue;
    }

    public String getEncodedCaptcha() {
        return encodedCaptcha;
    }

    public void setEncodedCaptcha(String encodedCaptcha) {
        this.encodedCaptcha = encodedCaptcha;
    }

}
