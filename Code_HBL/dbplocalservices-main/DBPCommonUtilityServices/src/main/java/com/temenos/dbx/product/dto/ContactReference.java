package com.temenos.dbx.product.dto;

import java.util.ArrayList;
import java.util.List;

import com.dbp.core.api.DBPDTO;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.temenos.dbx.product.utils.DTOUtils;

public class ContactReference implements DBPDTO {

    /**
     * 
     */
    private static final long serialVersionUID = -4714656794162952301L;
    private String partyId;
    private String contactReferences;
    private String reliabilityType;
    private String contactAddressId;
    private String contactName;
    private String startDate;
    private String endDate;
   
    
    
    /**
     * @return the partyId
     */
    public String getPartyId() {
        return partyId;
    }

    /**
     * @param partyId the partyId to set
     */
    public void setPartyId(String partyId) {
        this.partyId = partyId;
    }

    /**
     * @return the contactReferences
     */
    public String getContactReferences() {
        return contactReferences;
    }

    /**
     * @param contactReferences the contactReferences to set
     */
    public void setContactReferences(String contactReferences) {
        this.contactReferences = contactReferences;
    }

    /**
     * @return the reliabilityType
     */
    public String getReliabilityType() {
        return reliabilityType;
    }

    /**
     * @param reliabilityType the reliabilityType to set
     */
    public void setReliabilityType(String reliabilityType) {
        this.reliabilityType = reliabilityType;
    }

    /**
     * @return the contactAddressId
     */
    public String getContactAddressId() {
        return contactAddressId;
    }

    /**
     * @param contactAddressId the contactAddressId to set
     */
    public void setContactAddressId(String contactAddressId) {
        this.contactAddressId = contactAddressId;
    }

    /**
     * @return the contactName
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * @param contactName the contactName to set
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /**
     * @return the startDate
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the endDate
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /**
     * @return the serialversionuid
     */
    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public void loadFromJson(JsonObject jsonObject) {
        DTOUtils.loadDTOFromJson(this, jsonObject);
    }

    public static List<ContactReference> loadFromJsonArray(JsonArray jsonArray) {

        List<ContactReference> list = new ArrayList<ContactReference>();
        for(int i=0; i<jsonArray.size(); i++) {

            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
            ContactReference contactReference = new ContactReference();
            contactReference.loadFromJson(jsonObject);
            list.add(contactReference);
        }

        return list;
    }

    public JsonObject toStringJson() {
        return DTOUtils.getJsonObjectFromObject(this);
    }
}

