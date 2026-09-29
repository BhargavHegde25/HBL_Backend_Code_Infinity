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
public class PartyIdentifier implements DBPDTO {
	/**
	 * 
	 */
	private static final long serialVersionUID = -337416418903721909L;
	private String type;
	private String status;
	private String issuingAuthority;
	private String identifierNumber;
	private String issuedDate;
	private String expiryDate;
	private String issuingCountry;
	private String countrySubdivision;
	private String holderName;
	private Boolean isLegalIdentifier;
	private String partyType;
	private Boolean isExternal;
	
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the issuingAuthority
	 */
	public String getIssuingAuthority() {
		return issuingAuthority;
	}

	/**
	 * @param issuingAuthority the issuingAuthority to set
	 */
	public void setIssuingAuthority(String issuingAuthority) {
		this.issuingAuthority = issuingAuthority;
	}

	/**
	 * @return the identifierNumber
	 */
	public String getIdentifierNumber() {
		return identifierNumber;
	}

	/**
	 * @param identifierNumber the identifierNumber to set
	 */
	public void setIdentifierNumber(String identifierNumber) {
		this.identifierNumber = identifierNumber;
	}

	/**
	 * @return the issuedDate
	 */
	public String getIssuedDate() {
		return issuedDate;
	}

	/**
	 * @param issuedDate the issuedDate to set
	 */
	public void setIssuedDate(String issuedDate) {
		this.issuedDate = issuedDate;
	}

	/**
	 * @return the expiryDate
	 */
	public String getExpiryDate() {
		return expiryDate;
	}

	/**
	 * @param expiryDate the expiryDate to set
	 */
	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	/**
	 * @return the issuingCountry
	 */
	public String getIssuingCountry() {
		return issuingCountry;
	}

	/**
	 * @param issuingCountry the issuingCountry to set
	 */
	public void setIssuingCountry(String issuingCountry) {
		this.issuingCountry = issuingCountry;
	}

	/**
	 * @return the countrySubdivision
	 */
	public String getCountrySubdivision() {
		return countrySubdivision;
	}

	/**
	 * @param countrySubdivision the countrySubdivision to set
	 */
	public void setCountrySubdivision(String countrySubdivision) {
		this.countrySubdivision = countrySubdivision;
	}

	/**
	 * @return the holderName
	 */
	public String getHolderName() {
		return holderName;
	}

	/**
	 * @param holderName the holderName to set
	 */
	public void setHolderName(String holderName) {
		this.holderName = holderName;
	}

	/**
	 * @return the isLegalIdentifier
	 */
	public boolean isLegalIdentifier() {
		return isLegalIdentifier;
	}

	/**
	 * @param isLegalIdentifier the isLegalIdentifier to set
	 */
	public void setLegalIdentifier(boolean isLegalIdentifier) {
		this.isLegalIdentifier = isLegalIdentifier;
	}

	/**
	 * @return the partyType
	 */
	public String getPartyType() {
		return partyType;
	}

	/**
	 * @param partyType the partyType to set
	 */
	public void setPartyType(String partyType) {
		this.partyType = partyType;
	}

	/**
	 * @return the isExternal
	 */
	public Boolean isExternal() {
		return isExternal;
	}

	/**
	 * @param isExternal the isExternal to set
	 */
	public void setExternal(Boolean isExternal) {
		this.isExternal = isExternal;
	}

	public PartyIdentifier() {
	}

	public void loadFromJson(JsonObject jsonObject) {
		DTOUtils.loadDTOFromJson(this, jsonObject);
    }

	public static List<PartyIdentifier> loadFromJsonArray(JsonArray jsonArray) {

		List<PartyIdentifier> list = new ArrayList<PartyIdentifier>();
		for(int i=0; i<jsonArray.size(); i++) {

			JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
			PartyIdentifier partyIdentifier = new PartyIdentifier();
			partyIdentifier.loadFromJson(jsonObject);
			list.add(partyIdentifier);
		}

		return list;
	}
	
	public JsonObject toStringJson() {
        return DTOUtils.getJsonObjectFromObject(this);
    }
	
	
	
}
