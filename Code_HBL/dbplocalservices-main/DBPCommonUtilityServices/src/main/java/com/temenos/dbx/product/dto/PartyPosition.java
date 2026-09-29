package com.temenos.dbx.product.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.temenos.dbx.product.utils.DTOUtils;

@JsonInclude(Include.NON_NULL)
public class PartyPosition implements DBPDTO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1146749687315372799L;
	private String partyPosition;
	private String countryOfPosition;
	private String effectiveFromDate;
	private String effectiveToDate;
	private String expiryDate;
	private String comments;
	private String lastUpdated;
	private String informationSource;
	private String partyPositionType;
	
	public PartyPosition() {
	}
	
	/**
	 * @return the partyPosition
	 */
	public String getPartyPosition() {
		return partyPosition;
	}
	/**
	 * @param partyPosition the partyPosition to set
	 */
	public void setPartyPosition(String partyPosition) {
		this.partyPosition = partyPosition;
	}
	/**
	 * @return the countryOfPosition
	 */
	public String getCountryOfPosition() {
		return countryOfPosition;
	}
	/**
	 * @param countryOfPosition the countryOfPosition to set
	 */
	public void setCountryOfPosition(String countryOfPosition) {
		this.countryOfPosition = countryOfPosition;
	}
	/**
	 * @return the effectiveFromDate
	 */
	public String getEffectiveFromDate() {
		return effectiveFromDate;
	}
	/**
	 * @param effectiveFromDate the effectiveFromDate to set
	 */
	public void setEffectiveFromDate(String effectiveFromDate) {
		this.effectiveFromDate = effectiveFromDate;
	}
	/**
	 * @return the effectiveToDate
	 */
	public String getEffectiveToDate() {
		return effectiveToDate;
	}
	/**
	 * @param effectiveToDate the effectiveToDate to set
	 */
	public void setEffectiveToDate(String effectiveToDate) {
		this.effectiveToDate = effectiveToDate;
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
	 * @return the comments
	 */
	public String getComments() {
		return comments;
	}
	/**
	 * @param comments the comments to set
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}
	/**
	 * @return the lastUpdated
	 */
	public String getLastUpdated() {
		return lastUpdated;
	}
	/**
	 * @param lastUpdated the lastUpdated to set
	 */
	public void setLastUpdated(String lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	/**
	 * @return the informationSource
	 */
	public String getInformationSource() {
		return informationSource;
	}
	/**
	 * @param informationSource the informationSource to set
	 */
	public void setInformationSource(String informationSource) {
		this.informationSource = informationSource;
	}
	/**
	 * @return the partyPositionType
	 */
	public String getPartyPositionType() {
		return partyPositionType;
	}
	/**
	 * @param partyPositionType the partyPositionType to set
	 */
	public void setPartyPositionType(String partyPositionType) {
		this.partyPositionType = partyPositionType;
	}
	
}
