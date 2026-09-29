package com.temenos.infinity.api.accountaggregation.dto;

import java.io.Serializable;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_NULL)
public class ConsentDTO implements Serializable, DBPDTO {

    /**
     * 
     */
    private static final long serialVersionUID = -420744608326440876L;

    private String code;
    private String id;
    private String identifier;
    private String secret;
    private String digitalprofileid;
    private String message;
    private String expires_at;
    private String connect_url;

    public ConsentDTO() {
        super();
    }

    public ConsentDTO(String code, String id, String identifier, String secret, String digitalprofileid, String message,
            String expires_at, String connect_url) {
        super();
        this.code = code;
        this.id = id;
        this.identifier = identifier;
        this.secret = secret;
        this.digitalprofileid = digitalprofileid;
        this.message = message;
        this.expires_at = expires_at;
        this.connect_url = connect_url;

    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getDigitalprofileid() {
        return digitalprofileid;
    }

    public void setDigitalprofileid(String digitalprofileid) {
        this.digitalprofileid = digitalprofileid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getExpires_at() {
        return expires_at;
    }

    public void setExpires_at(String expires_at) {
        this.expires_at = expires_at;
    }

    public String getConnect_url() {
        return connect_url;
    }

    public void setConnect_url(String connect_url) {
        this.connect_url = connect_url;
    }

}
