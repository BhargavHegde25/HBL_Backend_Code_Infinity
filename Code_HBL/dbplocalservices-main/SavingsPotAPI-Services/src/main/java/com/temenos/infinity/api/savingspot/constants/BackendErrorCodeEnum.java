package com.temenos.infinity.api.savingspot.constants;

public enum BackendErrorCodeEnum {

    CUSTOMER_RECORD_EXISTS("E-00110"), NORECORDSFOUND("E-00120"), BACKENDFAILURE("400"), BACKENDSUCCESS("200"),
    REFRESHNOTPOSSIBLE("E-00140");

    private String message;

    BackendErrorCodeEnum(String message) {
        this.message = message;
    }

    public String getErrorCode() {
        return message;
    }
}
