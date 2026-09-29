package com.hbl.adminconsoleextn.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kony.adminconsole.service.featuresandactions.dto.ActionsDTO;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ActionsDTOExtn extends ActionsDTO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3518684141353317514L;
	private String id;
	@JsonAlias({"Action_id"})
	private String actionId;
	@JsonAlias({"LimitType_id"})
	private String limitTypeId;
	private String localeId;
	@JsonAlias({"status"})
    private String statusId;
	private String value;
	private String displayName;
	private String displayDescription;
	private double minTxLimit;
	private double maxTxLimit;
	private double dailyLimit;
	private double weeklyLimit;
	
	private double MB_minTxLimit;
	private double MB_maxTxLimit;
	private double MB_dailyLimit;
	private double MB_weeklyLimit;
	private String modifiedby;
	private String lastmodifiedts;
	private String companyLegalUnit;
	public ActionsDTOExtn(String id, String actionId, String limitTypeId, String localeId, String statusId,
			String value, String displayName, String displayDescription, double minTxLimit, double maxTxLimit,
			double dailyLimit, double weeklyLimit, double mB_minTxLimit, double mB_maxTxLimit, double mB_dailyLimit,
			double mB_weeklyLimit, String modifiedby, String lastmodifiedts, String companyLegalUnit) {
		super();
		this.id = id;
		this.actionId = actionId;
		this.limitTypeId = limitTypeId;
		this.localeId = localeId;
		this.statusId = statusId;
		this.value = value;
		this.displayName = displayName;
		this.displayDescription = displayDescription;
		this.minTxLimit = minTxLimit;
		this.maxTxLimit = maxTxLimit;
		this.dailyLimit = dailyLimit;
		this.weeklyLimit = weeklyLimit;
		MB_minTxLimit = mB_minTxLimit;
		MB_maxTxLimit = mB_maxTxLimit;
		MB_dailyLimit = mB_dailyLimit;
		MB_weeklyLimit = mB_weeklyLimit;
		this.modifiedby = modifiedby;
		this.lastmodifiedts = lastmodifiedts;
		this.companyLegalUnit = companyLegalUnit;
	}
	public ActionsDTOExtn() {
		super();
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getActionId() {
		return actionId;
	}
	public void setActionId(String actionId) {
		this.actionId = actionId;
	}
	public String getLimitTypeId() {
		return limitTypeId;
	}
	public void setLimitTypeId(String limitTypeId) {
		this.limitTypeId = limitTypeId;
	}
	public String getLocaleId() {
		return localeId;
	}
	public void setLocaleId(String localeId) {
		this.localeId = localeId;
	}
	public String getStatusId() {
		return statusId;
	}
	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getDisplayDescription() {
		return displayDescription;
	}
	public void setDisplayDescription(String displayDescription) {
		this.displayDescription = displayDescription;
	}
	public double getMinTxLimit() {
		return minTxLimit;
	}
	public void setMinTxLimit(double minTxLimit) {
		this.minTxLimit = minTxLimit;
	}
	public double getMaxTxLimit() {
		return maxTxLimit;
	}
	public void setMaxTxLimit(double maxTxLimit) {
		this.maxTxLimit = maxTxLimit;
	}
	public double getDailyLimit() {
		return dailyLimit;
	}
	public void setDailyLimit(double dailyLimit) {
		this.dailyLimit = dailyLimit;
	}
	public double getWeeklyLimit() {
		return weeklyLimit;
	}
	public void setWeeklyLimit(double weeklyLimit) {
		this.weeklyLimit = weeklyLimit;
	}
	public double getMB_minTxLimit() {
		return MB_minTxLimit;
	}
	public void setMB_minTxLimit(double mB_minTxLimit) {
		MB_minTxLimit = mB_minTxLimit;
	}
	public double getMB_maxTxLimit() {
		return MB_maxTxLimit;
	}
	public void setMB_maxTxLimit(double mB_maxTxLimit) {
		MB_maxTxLimit = mB_maxTxLimit;
	}
	public double getMB_dailyLimit() {
		return MB_dailyLimit;
	}
	public void setMB_dailyLimit(double mB_dailyLimit) {
		MB_dailyLimit = mB_dailyLimit;
	}
	public double getMB_weeklyLimit() {
		return MB_weeklyLimit;
	}
	public void setMB_weeklyLimit(double mB_weeklyLimit) {
		MB_weeklyLimit = mB_weeklyLimit;
	}
	public String getModifiedby() {
		return modifiedby;
	}
	public void setModifiedby(String modifiedby) {
		this.modifiedby = modifiedby;
	}
	public String getLastmodifiedts() {
		return lastmodifiedts;
	}
	public void setLastmodifiedts(String lastmodifiedts) {
		this.lastmodifiedts = lastmodifiedts;
	}
	public String getCompanyLegalUnit() {
		return companyLegalUnit;
	}
	public void setCompanyLegalUnit(String companyLegalUnit) {
		this.companyLegalUnit = companyLegalUnit;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(MB_dailyLimit, MB_maxTxLimit, MB_minTxLimit, MB_weeklyLimit, actionId,
				companyLegalUnit, dailyLimit, displayDescription, displayName, id, lastmodifiedts, limitTypeId,
				localeId, maxTxLimit, minTxLimit, modifiedby, statusId, value, weeklyLimit);
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ActionsDTOExtn other = (ActionsDTOExtn) obj;
		return Double.doubleToLongBits(MB_dailyLimit) == Double.doubleToLongBits(other.MB_dailyLimit)
				&& Double.doubleToLongBits(MB_maxTxLimit) == Double.doubleToLongBits(other.MB_maxTxLimit)
				&& Double.doubleToLongBits(MB_minTxLimit) == Double.doubleToLongBits(other.MB_minTxLimit)
				&& Double.doubleToLongBits(MB_weeklyLimit) == Double.doubleToLongBits(other.MB_weeklyLimit)
				&& Objects.equals(actionId, other.actionId) && Objects.equals(companyLegalUnit, other.companyLegalUnit)
				&& Double.doubleToLongBits(dailyLimit) == Double.doubleToLongBits(other.dailyLimit)
				&& Objects.equals(displayDescription, other.displayDescription)
				&& Objects.equals(displayName, other.displayName) && Objects.equals(id, other.id)
				&& Objects.equals(lastmodifiedts, other.lastmodifiedts)
				&& Objects.equals(limitTypeId, other.limitTypeId) && Objects.equals(localeId, other.localeId)
				&& Double.doubleToLongBits(maxTxLimit) == Double.doubleToLongBits(other.maxTxLimit)
				&& Double.doubleToLongBits(minTxLimit) == Double.doubleToLongBits(other.minTxLimit)
				&& Objects.equals(modifiedby, other.modifiedby) && Objects.equals(statusId, other.statusId)
				&& Objects.equals(value, other.value)
				&& Double.doubleToLongBits(weeklyLimit) == Double.doubleToLongBits(other.weeklyLimit);
	}
	
	

}
