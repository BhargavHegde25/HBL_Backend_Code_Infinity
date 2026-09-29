package com.temenos.dbx.product.utils;

public enum CustomerCreationMode {

    WITH_RECORD("WITH-RECORD"),
    WITHOUT_RECORD("WITHOUT-RECORD"),
    HYBRID("HYBRID");
    
    String mode;
    private CustomerCreationMode(String mode) {
        this.mode = mode;
    }
    
    public String getValue() {
        return this.mode;
    }
}
