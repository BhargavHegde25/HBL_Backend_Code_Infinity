package com.temenos.dbx.product.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.dbp.core.api.DBPDTO;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.temenos.dbx.product.api.DBPDTOEXT;
import com.temenos.dbx.product.utils.DTOUtils;

public class CustomerEmploymentDetailsDTO implements DBPDTOEXT {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2984932860497181044L;
	private String id;
	private String customer_id;
	private String employmentType;
	private String currentEmployer;
	private String designation;
	private String payPeriod;
	private String grossIncome;
	private String weekWorkingHours;
	private String employmentStartDate;
	private String previousEmployer;
	private String previousDesignation;
	private String otherEmployementType;
	private String otherEmployementDescription;
	private String createdby;
	private String modifiedby;
	private String createdts;
	private String lastmodifiedts;
	private String synctimestamp;
	private Boolean softdeleteflag;
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
	 * @return the employmentType
	 */
	public String getEmploymentType() {
		return employmentType;
	}
	/**
	 * @param employmentType the employmentType to set
	 */
	public void setEmploymentType(String employmentType) {
		this.employmentType = employmentType;
	}
	/**
	 * @return the currentEmployer
	 */
	public String getCurrentEmployer() {
		return currentEmployer;
	}
	/**
	 * @param currentEmployer the currentEmployer to set
	 */
	public void setCurrentEmployer(String currentEmployer) {
		this.currentEmployer = currentEmployer;
	}
	/**
	 * @return the designation
	 */
	public String getDesignation() {
		return designation;
	}
	/**
	 * @param designation the designation to set
	 */
	public void setDesignation(String designation) {
		this.designation = designation;
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
	 * @return the employmentStartDate
	 */
	public String getEmploymentStartDate() {
		return employmentStartDate;
	}
	/**
	 * @param employmentStartDate the employmentStartDate to set
	 */
	public void setEmploymentStartDate(String employmentStartDate) {
		this.employmentStartDate = employmentStartDate;
	}
	/**
	 * @return the previousEmployer
	 */
	public String getPreviousEmployer() {
		return previousEmployer;
	}
	/**
	 * @param previousEmployer the previousEmployer to set
	 */
	public void setPreviousEmployer(String previousEmployer) {
		this.previousEmployer = previousEmployer;
	}
	/**
	 * @return the previousDesignation
	 */
	public String getPreviousDesignation() {
		return previousDesignation;
	}
	/**
	 * @param previousDesignation the previousDesignation to set
	 */
	public void setPreviousDesignation(String previousDesignation) {
		this.previousDesignation = previousDesignation;
	}
	/**
	 * @return the otherEmployementType
	 */
	public String getOtherEmployementType() {
		return otherEmployementType;
	}
	/**
	 * @param otherEmployementType the otherEmployementType to set
	 */
	public void setOtherEmployementType(String otherEmployementType) {
		this.otherEmployementType = otherEmployementType;
	}
	/**
	 * @return the otherEmployementDescription
	 */
	public String getOtherEmployementDescription() {
		return otherEmployementDescription;
	}
	/**
	 * @param otherEmployementDescription the otherEmployementDescription to set
	 */
	public void setOtherEmployementDescription(String otherEmployementDescription) {
		this.otherEmployementDescription = otherEmployementDescription;
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
	public Boolean getSoftdeleteflag() {
		return softdeleteflag;
	}
	/**
	 * @param softdeleteflag the softdeleteflag to set
	 */
	public void setSoftdeleteflag(Boolean softdeleteflag) {
		this.softdeleteflag = softdeleteflag;
	}

	@Override
	public boolean persist(Map<String, Object> input, Map<String, Object> headers) {
		if(!isNew && isChanged){    
			return !ServiceCallHelper.invokeServiceAndGetJson(input, headers, URLConstants.CUSTOMER_EMP_DETAILS_UPDATE).has("errmsg");
		}

		if(isNew) {
			return !ServiceCallHelper.invokeServiceAndGetJson(input, headers, URLConstants.CUSTOMER_EMP_DETAILS_CREATE).has("errmsg");
		}


		return true;
	}

	@Override
	public Object loadDTO(String id) {
		String filter = "Customer_id"+ DBPUtilitiesConstants.EQUAL + id;
		List<DBPDTOEXT> exts = new ArrayList<DBPDTOEXT>();

		DTOUtils.loadDTOListfromDB(exts, filter, URLConstants.CUSTOMER_EMP_DETAILS_GET, true, true);

		return exts;
	}

	@Override
	public Object loadDTO() {

		List<DBPDTOEXT> exts = new ArrayList<DBPDTOEXT>();

		if(StringUtils.isNotBlank(customer_id)) {
			String filter = "Customer_id"+ DBPUtilitiesConstants.EQUAL + customer_id;
			DTOUtils.loadDTOListfromDB(exts, filter, URLConstants.CUSTOMER_EMP_DETAILS_GET, true, true);
		}
		return exts;
	}

}
