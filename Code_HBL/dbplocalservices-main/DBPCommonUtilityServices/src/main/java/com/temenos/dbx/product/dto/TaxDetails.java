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
public class TaxDetails implements DBPDTO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2719178862016071640L;
	private String taxType;
	private String Country;
	private String taxReference ;
    private String taxId;
	private String eligibilityType;
	private String eligibilityReason;
	private String taxStartDate;
	private String endDate;
	private String taxReasonType;
	private String taxReasonComment;
	
	/**
	 * @return the taxType
	 */
	public String getTaxType() {
		return taxType;
	}

	/**
	 * @param taxType the taxType to set
	 */
	public void setTaxType(String taxType) {
		this.taxType = taxType;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return Country;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		Country = country;
	}

	/**
	 * @return the taxId
	 */
	public String getTaxId() {
		return taxId;
	}
	
	/**
     * @return the taxKey
     */
    public String getTaxReference() {
        return taxReference;
    }

    /**
     * @param taxKey the taxKey to set
     */
    public void getTaxReference(String taxReference ) {
        this.taxReference  = taxReference;
    }

	/**
	 * @param taxId the taxId to set
	 */
	public void setTaxId(String taxId) {
		this.taxId = taxId;
	}

	/**
	 * @return the eligibilityType
	 */
	public String getEligibilityType() {
		return eligibilityType;
	}

	/**
	 * @param eligibilityType the eligibilityType to set
	 */
	public void setEligibilityType(String eligibilityType) {
		this.eligibilityType = eligibilityType;
	}

	/**
	 * @return the eligibilityReason
	 */
	public String getEligibilityReason() {
		return eligibilityReason;
	}

	/**
	 * @param eligibilityReason the eligibilityReason to set
	 */
	public void setEligibilityReason(String eligibilityReason) {
		this.eligibilityReason = eligibilityReason;
	}

	/**
	 * @return the taxStartDate
	 */
	public String getTaxStartDate() {
		return taxStartDate;
	}

	/**
	 * @param taxStartDate the taxStartDate to set
	 */
	public void setTaxStartDate(String taxStartDate) {
		this.taxStartDate = taxStartDate;
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
	 * @return the taxReasonType
	 */
	public String getTaxReasonType() {
		return taxReasonType;
	}

	/**
	 * @param taxReasonType the taxReasonType to set
	 */
	public void setTaxReasonType(String taxReasonType) {
		this.taxReasonType = taxReasonType;
	}

	/**
	 * @return the taxReasonComment
	 */
	public String getTaxReasonComment() {
		return taxReasonComment;
	}

	/**
	 * @param taxReasonComment the taxReasonComment to set
	 */
	public void setTaxReasonComment(String taxReasonComment) {
		this.taxReasonComment = taxReasonComment;
	}

	/**
	 * @return the taxIdLackReason
	 */
	public String getTaxIdLackReason() {
		return taxIdLackReason;
	}

	/**
	 * @param taxIdLackReason the taxIdLackReason to set
	 */
	public void setTaxIdLackReason(String taxIdLackReason) {
		this.taxIdLackReason = taxIdLackReason;
	}

	/**
	 * @return the isMainTaxResidency
	 */
	public String getIsMainTaxResidency() {
		return isMainTaxResidency;
	}

	/**
	 * @param isMainTaxResidency the isMainTaxResidency to set
	 */
	public void setIsMainTaxResidency(String isMainTaxResidency) {
		this.isMainTaxResidency = isMainTaxResidency;
	}

	private String taxIdLackReason;
	private String isMainTaxResidency;
	
	public TaxDetails() {
	}

	public void loadFromJson(JsonObject jsonObject) {
		DTOUtils.loadDTOFromJson(this, jsonObject);
    }

	public static List<TaxDetails> loadFromJsonArray(JsonArray jsonArray) {

		List<TaxDetails> list = new ArrayList<TaxDetails>();
		for(int i=0; i<jsonArray.size(); i++) {

			JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
			TaxDetails taxDetails = new TaxDetails();
			taxDetails.loadFromJson(jsonObject);
			list.add(taxDetails);
		}

		return list;
	}
	
    public JsonObject toStringJson() {
        return DTOUtils.getJsonObjectFromObject(this);
    }
	
	
}
