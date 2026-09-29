package com.temenos.dbx.mfa.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MFA {

	private String isMFARequired;
	private String app_id;
	private String action_id;
	private String frequencyTypeId;
	private String frequencyValue;
	private String primaryMFATypeId;
	private String secondaryMFATypeId;
	private List<MfaType> mfaTypes;
	private MfaType primaryMFAType;
	
	/**
	 * @return the isMFARequired
	 */
	public String getIsMFARequired() {
		return isMFARequired;
	}
	/**
	 * @param isMFARequired the isMFARequired to set
	 */
	public void setIsMFARequired(String isMFARequired) {
		this.isMFARequired = isMFARequired;
	}
	/**
	 * @return the app_id
	 */
	public String getApp_id() {
		return app_id;
	}
	/**
	 * @param app_id the app_id to set
	 */
	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}
	/**
	 * @return the action_id
	 */
	public String getAction_id() {
		return action_id;
	}
	/**
	 * @param action_id the action_id to set
	 */
	public void setAction_id(String action_id) {
		this.action_id = action_id;
	}
	/**
	 * @return the frequencyTypeId
	 */
	public String getFrequencyTypeId() {
		return frequencyTypeId;
	}
	/**
	 * @param frequencyTypeId the frequencyTypeId to set
	 */
	public void setFrequencyTypeId(String frequencyTypeId) {
		this.frequencyTypeId = frequencyTypeId;
	}
	/**
	 * @return the frequencyValue
	 */
	public String getFrequencyValue() {
		return frequencyValue;
	}
	/**
	 * @param frequencyValue the frequencyValue to set
	 */
	public void setFrequencyValue(String frequencyValue) {
		this.frequencyValue = frequencyValue;
	}
	/**
	 * @return the primaryMFATypeId
	 */
	public String getPrimaryMFATypeId() {
		return primaryMFATypeId;
	}
	/**
	 * @param primaryMFATypeId the primaryMFATypeId to set
	 */
	public void setPrimaryMFATypeId(String primaryMFATypeId) {
		this.primaryMFATypeId = primaryMFATypeId;
	}
	/**
	 * @return the secondaryMFATypeId
	 */
	public String getSecondaryMFATypeId() {
		return secondaryMFATypeId;
	}
	/**
	 * @param secondaryMFATypeId the secondaryMFATypeId to set
	 */
	public void setSecondaryMFATypeId(String secondaryMFATypeId) {
		this.secondaryMFATypeId = secondaryMFATypeId;
	}
	public void setBackupMFAType(boolean areSecurityQuestionsPresent) {
		if(!areSecurityQuestionsPresent) {
			
			for(MfaType mfaType : mfaTypes) {
				if(mfaType.getMfaTypeId().equals(secondaryMFATypeId)) {
					primaryMFAType = mfaType;
				}
			}
			
			primaryMFATypeId = secondaryMFATypeId;
		}
	}
	
	public List<MfaType> getMfaTypes() {
		return mfaTypes;
	}
	public void setMfaTypes(List<MfaType> mfaTypes) {
		this.mfaTypes = mfaTypes;
	}
	public void setPrimaryMFAType() {
		for(MfaType mfaType : mfaTypes) {
			if(mfaType.getMfaTypeId().equals(primaryMFATypeId)) {
				primaryMFAType = mfaType;
			}
		}
	}
	
	public String getConfiguration(String key) {
		return primaryMFAType.getConfiguration(key);
	}
	public MfaType getPrimaryMFAType() {
		return primaryMFAType;
	}
		
	
	
}
