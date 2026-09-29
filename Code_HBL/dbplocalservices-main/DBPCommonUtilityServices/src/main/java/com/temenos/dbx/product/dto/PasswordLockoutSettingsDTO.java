package com.temenos.dbx.product.dto;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.api.DBPDTOEXT;
import com.temenos.dbx.product.utils.DTOUtils;

public class PasswordLockoutSettingsDTO implements DBPDTOEXT {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8225300949203537943L;
	/**
	 * 
	 */
	private String id;
	private int passwordValidity;
	private boolean passwordExpiryWarningRequired;
	private int passwordExpiryWarningThreshold;
	private int passwordHistoryCount;
	private int accountLockoutThreshold;
	private int accountLockoutTime;
	private int recoveryEmailLinkValidity;
	private String createdby;
	private String modifiedby;
	private String createdts;
	private String lastmodifiedts;
	private String synctimestamp;
	private String softdeleteflag;


	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the passwordValidity
	 */
	public int getPasswordValidity() {
		return passwordValidity;
	}

	/**
	 * @param passwordValidity the passwordValidity to set
	 */
	public void setPasswordValidity(int passwordValidity) {
		this.passwordValidity = passwordValidity;
	}

	/**
	 * @return the passwordExpiryWarningRequired
	 */
	public boolean isPasswordExpiryWarningRequired() {
		return passwordExpiryWarningRequired;
	}

	/**
	 * @param passwordExpiryWarningRequired the passwordExpiryWarningRequired to set
	 */
	public void setPasswordExpiryWarningRequired(boolean passwordExpiryWarningRequired) {
		this.passwordExpiryWarningRequired = passwordExpiryWarningRequired;
	}

	/**
	 * @return the passwordExpiryWarningThreshold
	 */
	public int getPasswordExpiryWarningThreshold() {
		return passwordExpiryWarningThreshold;
	}

	/**
	 * @param passwordExpiryWarningThreshold the passwordExpiryWarningThreshold to set
	 */
	public void setPasswordExpiryWarningThreshold(int passwordExpiryWarningThreshold) {
		this.passwordExpiryWarningThreshold = passwordExpiryWarningThreshold;
	}

	/**
	 * @return the passwordHistoryCount
	 */
	public int getPasswordHistoryCount() {
		return passwordHistoryCount;
	}

	/**
	 * @param passwordHistoryCount the passwordHistoryCount to set
	 */
	public void setPasswordHistoryCount(int passwordHistoryCount) {
		this.passwordHistoryCount = passwordHistoryCount;
	}

	/**
	 * @return the accountLockoutThreshold
	 */
	public int getAccountLockoutThreshold() {
		return accountLockoutThreshold;
	}

	/**
	 * @param accountLockoutThreshold the accountLockoutThreshold to set
	 */
	public void setAccountLockoutThreshold(int accountLockoutThreshold) {
		this.accountLockoutThreshold = accountLockoutThreshold;
	}

	/**
	 * @return the accountLockoutTime
	 */
	public int getAccountLockoutTime() {
		return accountLockoutTime;
	}

	/**
	 * @param accountLockoutTime the accountLockoutTime to set
	 */
	public void setAccountLockoutTime(int accountLockoutTime) {
		this.accountLockoutTime = accountLockoutTime;
	}

	/**
	 * @return the recoveryEmailLinkValidity
	 */
	public int getRecoveryEmailLinkValidity() {
		return recoveryEmailLinkValidity;
	}

	/**
	 * @param recoveryEmailLinkValidity the recoveryEmailLinkValidity to set
	 */
	public void setRecoveryEmailLinkValidity(int recoveryEmailLinkValidity) {
		this.recoveryEmailLinkValidity = recoveryEmailLinkValidity;
	}

	/**
	 * @return the createdby
	 */
	public String getCreatedby() {
		return createdby;
	}

	/**
	 * @param createdby the createdby to set
	 */
	public void setCreatedby(String createdby) {
		this.createdby = createdby;
	}

	/**
	 * @return the modifiedby
	 */
	public String getModifiedby() {
		return modifiedby;
	}

	/**
	 * @param modifiedby the modifiedby to set
	 */
	public void setModifiedby(String modifiedby) {
		this.modifiedby = modifiedby;
	}

	/**
	 * @return the createdts
	 */
	public String getCreatedts() {
		return createdts;
	}

	/**
	 * @param createdts the createdts to set
	 */
	public void setCreatedts(String createdts) {
		this.createdts = createdts;
	}

	/**
	 * @return the lastmodifiedts
	 */
	public String getLastmodifiedts() {
		return lastmodifiedts;
	}

	/**
	 * @param lastmodifiedts the lastmodifiedts to set
	 */
	public void setLastmodifiedts(String lastmodifiedts) {
		this.lastmodifiedts = lastmodifiedts;
	}

	/**
	 * @return the synctimestamp
	 */
	public String getSynctimestamp() {
		return synctimestamp;
	}

	/**
	 * @param synctimestamp the synctimestamp to set
	 */
	public void setSynctimestamp(String synctimestamp) {
		this.synctimestamp = synctimestamp;
	}

	/**
	 * @return the softdeleteflag
	 */
	public String getSoftdeleteflag() {
		return softdeleteflag;
	}

	/**
	 * @param softdeleteflag the softdeleteflag to set
	 */
	public void setSoftdeleteflag(String softdeleteflag) {
		this.softdeleteflag = softdeleteflag;
	}

	@Override
	public boolean persist(Map<String, Object> input, Map<String, Object> headers) {
		return true;
	}

	@Override
	public Object loadDTO() {
		List<DBPDTOEXT> exts = new ArrayList<DBPDTOEXT>();

		DTOUtils.loadDTOListfromDB(exts, "", URLConstants.PASSWORDLOCKOUTSETTINGS_GET, false, true);

		if(exts != null && exts.size() >0 ) {
			return exts.get(exts.size()-1);
		}
		return null;
	}
	public boolean isEmailRecoveryLinkExpired(DataControllerRequest dcRequest, String createdDate) {
		boolean isEmailRecoveryLinkExpired = false;
		try {
			Date createdts = HelperMethods.getFormattedTimeStamp(createdDate);
			Date now = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(createdts);
			cal.add(Calendar.MINUTE, getRecoveryEmailLinkValidity());
			createdts = cal.getTime();
			if (now.after(createdts)) {
				isEmailRecoveryLinkExpired = true;
			}
			else {
				isEmailRecoveryLinkExpired = false;
			}

		}catch (Exception e) {
		}

		return isEmailRecoveryLinkExpired;
	}
	@Override
	public Object loadDTO(String id) {
		List<DBPDTOEXT> exts = new ArrayList<DBPDTOEXT>();

		DTOUtils.loadDTOListfromDB(exts, "", URLConstants.PASSWORDLOCKOUTSETTINGS_GET, false, true);

		if(exts != null && exts.size() >0 ) {
			return exts.get(exts.size()-1);
		}
		return null;
	}


}
