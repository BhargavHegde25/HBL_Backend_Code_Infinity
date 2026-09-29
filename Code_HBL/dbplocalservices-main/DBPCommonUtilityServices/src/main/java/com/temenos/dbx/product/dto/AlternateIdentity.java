package com.temenos.dbx.product.dto;

import java.util.ArrayList;
import java.util.List;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.temenos.dbx.product.utils.DTOUtils;

@JsonInclude(Include.NON_NULL)
public class AlternateIdentity implements DBPDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3288130798189868826L;
	private String  identityType;
	private String  identityNumber;
	private String identitySource;
	
	public AlternateIdentity() {
//		DTOUtils.addOnetoIntFields(this);
	}
	/**
	 * @return the identityType
	 */
	public String getIdentityType() {
		return identityType;
	}
	/**
	 * @param identityType the identityType to set
	 */
	public void setIdentityType(String identityType) {
		this.identityType = identityType;
	}
	/**
	 * @return the identityNumber
	 */
	public String getIdentityNumber() {
		return identityNumber;
	}
	/**
	 * @param identityNumber the identityNumber to set
	 */
	public void setIdentityNumber(String identityNumber) {
		this.identityNumber = identityNumber;
	}
	/**
	 * @return the identitySource
	 */
	public String getIdentitySource() {
		return identitySource;
	}
	/**
	 * @param identitySource the identitySource to set
	 */
	public void setIdentitySource(String identitySource) {
		this.identitySource = identitySource;
	}
    public JsonObject toStringJson() {
        return DTOUtils.getJsonObjectFromObject(this);
    }
    public static List<AlternateIdentity> loadFromJsonArray(JsonArray alternateIdentities) {
        List<AlternateIdentity> list = new ArrayList<AlternateIdentity>();
        for(int i=0; i<alternateIdentities.size(); i++) {

            JsonObject jsonObject = alternateIdentities.get(i).getAsJsonObject();
            AlternateIdentity alternateIdentity = new AlternateIdentity();
            alternateIdentity.loadFromJson(jsonObject);
            list.add(alternateIdentity);
        }

        return list;
    }
    
    public void loadFromJson(JsonObject jsonObject) {
        DTOUtils.loadDTOFromJson(this, jsonObject);
    }
	
}
