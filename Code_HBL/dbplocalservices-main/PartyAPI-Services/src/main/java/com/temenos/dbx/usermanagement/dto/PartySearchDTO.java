package com.temenos.dbx.usermanagement.dto;

import com.google.gson.JsonObject;
import com.temenos.dbx.product.utils.DTOUtils;

public class PartySearchDTO {
    public String identifierNumber;
    public String identifierType;
    public String alternateIdentifierNumber;
    public String alternateIdentifierType;
    public String dateOfBirth;
    public String contactNumber;
    public String entityName;
    public String emailId;
    public String recordCount;

    public JsonObject toStringJson() {
        return DTOUtils.getJsonObjectFromObject(this);
    }
}
