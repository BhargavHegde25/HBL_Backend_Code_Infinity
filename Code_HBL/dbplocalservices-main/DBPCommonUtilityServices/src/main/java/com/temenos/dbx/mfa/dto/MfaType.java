package com.temenos.dbx.mfa.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class MfaType {

	private String mfaTypeId;
    private List<MfaConfigurations> mfaConfigurations;
    private String emailBody;
    private String mfaTypeName;
    private String emailSubject;
    private String smsText;
	/**
	 * @return the mfaTypeId
	 */
	public String getMfaTypeId() {
		return mfaTypeId;
	}
	/**
	 * @param mfaTypeId the mfaTypeId to set
	 */
	public void setMfaTypeId(String mfaTypeId) {
		this.mfaTypeId = mfaTypeId;
	}
	/**
	 * @return the mfaConfigurations
	 */
	public List<MfaConfigurations> getMfaConfigurations() {
		return mfaConfigurations;
	}
	/**
	 * @param mfaConfigurations the mfaConfigurations to set
	 */
	public void setMfaConfigurations(List<MfaConfigurations> mfaConfigurations) {
		this.mfaConfigurations = mfaConfigurations;
	}
	/**
	 * @return the emailBody
	 */
	public String getEmailBody() {
		return emailBody;
	}
	/**
	 * @param emailBody the emailBody to set
	 */
	public void setEmailBody(String emailBody) {
		this.emailBody = emailBody;
	}
	/**
	 * @return the mfaTypeName
	 */
	public String getMfaTypeName() {
		return mfaTypeName;
	}
	/**
	 * @param mfaTypeName the mfaTypeName to set
	 */
	public void setMfaTypeName(String mfaTypeName) {
		this.mfaTypeName = mfaTypeName;
	}
	/**
	 * @return the emailSubject
	 */
	public String getEmailSubject() {
		return emailSubject;
	}
	/**
	 * @param emailSubject the emailSubject to set
	 */
	public void setEmailSubject(String emailSubject) {
		this.emailSubject = emailSubject;
	}
	/**
	 * @return the smsText
	 */
	public String getSmsText() {
		return smsText;
	}
	/**
	 * @param smsText the smsText to set
	 */
	public void setSmsText(String smsText) {
		this.smsText = smsText;
	}
	public String getConfiguration(String key) {
		// TODO Auto-generated method stub
		for(MfaConfigurations configurations : mfaConfigurations) {
			if(key.equals(configurations.getMfaKey())){
				return configurations.getMfaValue();
			}
		}
		
		return null;
	}
}
