package com.temenos.dbx.product.dto;

import java.lang.reflect.Field;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.gson.JsonObject;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.temenos.dbx.product.utils.DTOUtils;

@JsonInclude(Include.NON_NULL)
public class PartyNames implements DBPDTO {

    /**
     * 
     */

    private static final long serialVersionUID = 2406576684561160966L;
    private String startDate;
    private String title;
    private String firstName;
    private String middleName;
    private String lastName;
    private String suffix;
    private String alias;
    private String entityName;
    private ExtensionData extensionData;
    
    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the middleName
     */
    public String getMiddleName() {
        return middleName;
    }

    /**
     * @param middleName the middleName to set
     */
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the suffix
     */
    public String getSuffix() {
        return suffix;
    }

    /**
     * @param suffix the suffix to set
     */
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    /**
     * @return the alias
     */
    public String getAlias() {
        return alias;
    }

    /**
     * @param alias the alias to set
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }

    /**
     * @return the entityName
     */
    public String getEntityName() {
        return entityName;
    }

    /**
     * @param entityName the entityName to set
     */
    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    /**
     * @return the serialversionuid
     */
    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public PartyNames() {
    }

    public ExtensionData getExtensionData() {
        return extensionData;
    }

    public void setExtensionData(ExtensionData extensionData) {
        this.extensionData = extensionData;
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


    public JsonObject toStringJson() {
        JsonObject jsonObject = DTOUtils.getJsonObjectFromObject(this);

        if(extensionData != null) {
            jsonObject.add("extensionData", extensionData.toStringJson());
        }
        return jsonObject;
    }

    public void loadFromJson(JsonObject jsonObject) {
        
        LoggerUtil logger = new LoggerUtil(PartyDTO.class);
        Field[] fields= this.getClass().getDeclaredFields();
        
        for(int i=0; i<fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            String fieldName = field.getName();
            if(jsonObject.has(fieldName) && !jsonObject.get(fieldName).isJsonNull()) {
                try {
                    if(field.getType().equals(String.class)) {
                        field.set(this, jsonObject.get(fieldName).getAsString());
                    }
                    else if(field.getType().equals(int.class)) {
                        field.set(this, jsonObject.get(fieldName).getAsInt());
                    }
                    else if(field.getType().equals(Boolean.class)) {
                        field.set(this, jsonObject.get(fieldName).getAsBoolean());
                    }

                } catch (IllegalArgumentException | IllegalAccessException e) {
                    logger.error("Caught exception while converting json to Object: " , e);
                }
            }
            field.setAccessible(false);
        }
        
        if(jsonObject.has("startDate") && !jsonObject.get("startDate").isJsonNull()) {
            this.startDate = jsonObject.get("startDate").getAsString();
        }
        
        if(jsonObject.has("extensionData") && !jsonObject.get("extensionData").isJsonNull()) {
            JsonObject extensionData = jsonObject.get("extensionData").getAsJsonObject();
            this.extensionData = new ExtensionData();
            this.extensionData.loadFromJson(extensionData);

        }
        
    }

}
