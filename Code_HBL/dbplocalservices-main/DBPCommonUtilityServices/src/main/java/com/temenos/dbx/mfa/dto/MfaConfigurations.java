package com.temenos.dbx.mfa.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class MfaConfigurations {
	private String mfaValue;
	private String mfaKey;
	/**
	 * @return the mfaValue
	 */
	public String getMfaValue() {
		return mfaValue;
	}
	/**
	 * @param mfaValue the mfaValue to set
	 */
	public void setMfaValue(String mfaValue) {
		this.mfaValue = mfaValue;
	}
	/**
	 * @return the mfaKey
	 */
	public String getMfaKey() {
		return mfaKey;
	}
	/**
	 * @param mfaKey the mfaKey to set
	 */
	public void setMfaKey(String mfaKey) {
		this.mfaKey = mfaKey;
	}
	
	
}
