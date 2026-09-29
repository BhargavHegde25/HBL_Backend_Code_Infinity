package com.kony.adminconsole.dto;

import com.konylabs.middleware.dataobject.Record;
import org.json.JSONObject;

public class RequestBean {
    private String requestId;
    private String recordId;
    private String module;
    private String feature;
    private String status;
    private String expAPIOperationName;
    private String expAPINickName;
    private String permissionId;
    private String permissionName;
    private String createdBy;
    private String createdTS;
    private String checkedBy;
    private String checkedTS;
    private String reason;

    public RequestBean(String requestId, String recordId, String module, String feature, String status, String expAPIOperationName, String expAPINickName, String permissionId, String permissionName, String createdBy, String createdTS, String checkedBy, String checkedTS, String reason){
        this.requestId = requestId;
        this.recordId = recordId;
        this.module = module;
        this.feature = feature;
        this.status = status;
        this.expAPIOperationName = expAPIOperationName;
        this.expAPINickName = expAPINickName;
        this.permissionId = permissionId;
        this.permissionName = permissionName;
        this.createdBy = createdBy;
        this.createdTS = createdTS;
        this.checkedBy = checkedBy;
        this.checkedTS = checkedTS;
        this.reason = reason;
    }

    public RequestBean(JSONObject requestObj){
        this.requestId = requestObj.has("requestId") ? (String) requestObj.get("requestId") : null;
        this.recordId = requestObj.has("recordId") ? (String) requestObj.get("recordId") : null;
        this.module = requestObj.has("module") ? (String) requestObj.get("module") : null;
        this.feature = requestObj.has("feature") ? (String) requestObj.get("feature") : null;
        this.status = requestObj.has("status") ? (String) requestObj.get("status") : null;
        this.expAPIOperationName = requestObj.has("expAPIOperationName") ? (String) requestObj.get("expAPIOperationName") : null;
        this.expAPINickName = requestObj.has("expAPINickName") ? (String) requestObj.get("expAPINickName") : null;
        this.permissionId = requestObj.has("permissionId") ? (String) requestObj.get("permissionId") : null;
        this.permissionName = requestObj.has("permissionName") ? (String) requestObj.get("permissionName") : null;
        this.createdBy = requestObj.has("createdby") ? (String) requestObj.get("createdby") : null;
        this.createdTS = requestObj.has("createdts") ? (String) requestObj.get("createdts") : null;
        this.checkedBy = requestObj.has("checkedBy") ? (String) requestObj.get("checkedBy") : null;
        this.checkedTS = requestObj.has("checkedts") ? (String) requestObj.get("checkedts") : null;
        this.reason = requestObj.has("reason") ? (String) requestObj.get("reason") : null;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getStatus(){
        return status;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public String getExpAPIOperationName() {
        return expAPIOperationName;
    }

    public void setExpAPIOperationName(String expAPIOperationName) {
        this.expAPIOperationName = expAPIOperationName;
    }

    public String getExpAPINickName() {
        return expAPINickName;
    }

    public void setExpAPINickName(String expAPINickName) {
        this.expAPINickName = expAPINickName;
    }

    public String getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(String permissionId) {
        this.permissionId = permissionId;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedTS() {
        return createdTS;
    }

    public void setCreatedTS(String createdTS) {
        this.createdTS = createdTS;
    }

    public String getCheckedBy() {
        return checkedBy;
    }

    public void setCheckedBy(String checkedBy) {
        this.checkedBy = checkedBy;
    }

    public String getCheckedTS() {
        return checkedTS;
    }

    public void setCheckedTS(String checkedTS) {
        this.checkedTS = checkedTS;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Record getAsRecord(){
        Record record = new Record();
        record.addParam("requestId", getRequestId());
        record.addParam("recordId", getRecordId());
        record.addParam("module", getModule());
        record.addParam("feature", getFeature());
        record.addParam("status", getStatus());
        record.addParam("expAPIOperationName", getExpAPIOperationName());
        record.addParam("expAPINickName", getExpAPINickName());
        record.addParam("permissionId", getPermissionId());
        record.addParam("permissionName", getPermissionName());
        record.addParam("createdBy", getCreatedBy());
        record.addParam("createdTS", getCreatedTS());
        record.addParam("checkedBy", getCheckedBy());
        record.addParam("checkedTS", getCheckedTS());
        record.addParam("reason", getReason());
        return record;
    }
}
