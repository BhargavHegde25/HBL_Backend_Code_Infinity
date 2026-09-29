package com.temenos.dbx.product.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.temenos.dbx.product.api.DBPDTOEXT;
import com.temenos.dbx.product.utils.DTOUtils;

public class OtherSourceOfIncome implements DBPDTOEXT {


	/**
	 * 
	 */
	private static final long serialVersionUID = -2036304502551416784L;
	private String id;
	private String incomeInfo_id;
	private String sourceType;
	private String payPeriod;
	private String grossIncome;
	private String weekWorkingHours;
	private String customer_id;
	private String sourceOfIncomeName;
	private String sourceofIncomeDescription;
	private String createdby;
	private String modifiedby;
	private String createdts;
	private String lastmodifiedts;
	private String synctimestamp;
	private String softdeleteflag;
	private boolean isNew;
	private boolean isChanged;



	/**
	 * @return the isChanged
	 */
	public Boolean getIsChanged() {
		return isChanged;
	}


	/**
	 * @param isChanged the isChanged to set
	 */
	public void setIsChanged(Boolean isChanged) {
		this.isChanged = isChanged;
	}


	/**
	 * @return the isNew
	 */
	public Boolean getIsNew() {
		return isNew;
	}


	/**
	 * @param isNew the isNew to set
	 */
	public void setIsNew(Boolean isNew) {
		this.isNew = isNew;
	}

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
	 * @return the incomeInfo_id
	 */
	public String getIncomeInfo_id() {
		return incomeInfo_id;
	}
	/**
	 * @param incomeInfo_id the incomeInfo_id to set
	 */
	public void setIncomeInfo_id(String incomeInfo_id) {
		this.incomeInfo_id = incomeInfo_id;
	}
	/**
	 * @return the sourceType
	 */
	public String getSourceType() {
		return sourceType;
	}
	/**
	 * @param sourceType the sourceType to set
	 */
	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}
	/**
	 * @return the payPeriod
	 */
	public String getPayPeriod() {
		return payPeriod;
	}
	/**
	 * @param payPeriod the payPeriod to set
	 */
	public void setPayPeriod(String payPeriod) {
		this.payPeriod = payPeriod;
	}
	/**
	 * @return the grossIncome
	 */
	public String getGrossIncome() {
		return grossIncome;
	}
	/**
	 * @param grossIncome the grossIncome to set
	 */
	public void setGrossIncome(String grossIncome) {
		this.grossIncome = grossIncome;
	}
	/**
	 * @return the weekWorkingHours
	 */
	public String getWeekWorkingHours() {
		return weekWorkingHours;
	}
	/**
	 * @param weekWorkingHours the weekWorkingHours to set
	 */
	public void setWeekWorkingHours(String weekWorkingHours) {
		this.weekWorkingHours = weekWorkingHours;
	}
	/**
	 * @return the customer_id
	 */
	public String getCustomer_id() {
		return customer_id;
	}
	/**
	 * @param customer_id the customer_id to set
	 */
	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}
	/**
	 * @return the sourceOfIncomeName
	 */
	public String getSourceOfIncomeName() {
		return sourceOfIncomeName;
	}
	/**
	 * @param sourceOfIncomeName the sourceOfIncomeName to set
	 */
	public void setSourceOfIncomeName(String sourceOfIncomeName) {
		this.sourceOfIncomeName = sourceOfIncomeName;
	}
	/**
	 * @return the sourceofIncomeDescription
	 */
	public String getSourceofIncomeDescription() {
		return sourceofIncomeDescription;
	}
	/**
	 * @param sourceofIncomeDescription the sourceofIncomeDescription to set
	 */
	public void setSourceofIncomeDescription(String sourceofIncomeDescription) {
		this.sourceofIncomeDescription = sourceofIncomeDescription;
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
		if(!isNew && isChanged){
			return !ServiceCallHelper.invokeServiceAndGetJson(input, headers, URLConstants.OTHER_SOURCSE_OF_INCOME_UPDATE).has("errmsg");
		}

		if(isNew){
			return !ServiceCallHelper.invokeServiceAndGetJson(input, headers, URLConstants.OTHER_SOURCSE_OF_INCOME_CREATE).has("errmsg");
		}

		return true;
	}

	 @Override
	    public Object loadDTO(String id) {
	        String filter = "Customer_id"+ DBPUtilitiesConstants.EQUAL + id;
	        List<DBPDTOEXT> exts = new ArrayList<DBPDTOEXT>();

	        DTOUtils.loadDTOListfromDB(exts, filter, URLConstants.OTHER_SOURCSE_OF_INCOME_GET, true, true);

	        return exts;
	    }
	 
	@Override
	public Object loadDTO() {

		if(StringUtils.isNotBlank(customer_id)) {
			String filter = "Customer_id"+ DBPUtilitiesConstants.EQUAL + id;
			List<DBPDTOEXT> exts = new ArrayList<DBPDTOEXT>();

			DTOUtils.loadDTOListfromDB(exts, filter, URLConstants.OTHER_SOURCSE_OF_INCOME_GET, true, true);

			return exts;
		}
		
		return null;
	}

}
