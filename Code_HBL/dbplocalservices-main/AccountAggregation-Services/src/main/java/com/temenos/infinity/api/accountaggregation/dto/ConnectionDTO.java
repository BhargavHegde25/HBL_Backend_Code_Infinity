package com.temenos.infinity.api.accountaggregation.dto;

import java.io.Serializable;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_NULL)
public class ConnectionDTO implements Serializable, DBPDTO {

    /**
     * 
     */
    private static final long serialVersionUID = -5151828838204415L;

    private String status;
    private String responseMessage;
    private String responseCode;
    private String opstatus;
    private String httpStatusCode;
    private String code;
    private String daily_refresh;
    private String message;

    public ConnectionDTO() {
        super();
    }

    public ConnectionDTO(String status, String responseMessage, String responseCode, String opstatus,
            String httpStatusCode, String code, String daily_refresh, String message) {
        super();

        this.status = status;
        this.responseMessage = responseMessage;
        this.responseCode = responseCode;
        this.opstatus = opstatus;
        this.httpStatusCode = httpStatusCode;
        this.code = code;
        this.daily_refresh = daily_refresh;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(String httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public String getOpstatus() {
        return opstatus;
    }

    public void setOpstatus(String opstatus) {
        this.opstatus = opstatus;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDaily_refresh() {
        return daily_refresh;
    }

    public void setDaily_refresh(String daily_refresh) {
        this.daily_refresh = daily_refresh;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
