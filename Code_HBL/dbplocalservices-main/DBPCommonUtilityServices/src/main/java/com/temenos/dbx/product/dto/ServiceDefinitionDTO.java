package com.temenos.dbx.product.dto;

import com.dbp.core.api.DBPDTO;

public class ServiceDefinitionDTO implements DBPDTO {

    /**
     * 
     */
    private static final long serialVersionUID = -514709964401513902L;
    private String id;
    private String name;
    private String description;
    private String serviceType;
    private String status;
    private String createdby;
    private String modifiedby;
    private String createdts;
    private String lastmodifiedts;
    private String synctimestamp;
    private String softdeleteflag;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedby() {
        return createdby;
    }

    public void setCreatedby(String createdby) {
        this.createdby = createdby;
    }

    public String getModifiedby() {
        return modifiedby;
    }

    public void setModifiedby(String modifiedby) {
        this.modifiedby = modifiedby;
    }

    public String getCreatedts() {
        return createdts;
    }

    public void setCreatedts(String createdts) {
        this.createdts = createdts;
    }

    public String getLastmodifiedts() {
        return lastmodifiedts;
    }

    public void setLastmodifiedts(String lastmodifiedts) {
        this.lastmodifiedts = lastmodifiedts;
    }

    public String getSynctimestamp() {
        return synctimestamp;
    }

    public void setSynctimestamp(String synctimestamp) {
        this.synctimestamp = synctimestamp;
    }

    public String getSoftdeleteflag() {
        return softdeleteflag;
    }

    public void setSoftdeleteflag(String softdeleteflag) {
        this.softdeleteflag = softdeleteflag;
    }

}
