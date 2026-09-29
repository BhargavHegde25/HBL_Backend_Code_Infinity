package com.temenos.dbx.product.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.temenos.dbx.product.api.DBPDTOEXT;
import com.temenos.dbx.product.utils.DTOUtils;

@JsonInclude(Include.NON_NULL)
public class FeedBackStatusDTO implements DBPDTOEXT {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1759512860002461429L;

	private String id;
	private String UserName;
	private String feedbackID;
	private String createdts;
	private String status;
	private String deviceID;
	private String customerID;
	private boolean isNew;
	private boolean isChanged;


	/**
	 * @return the isNew
	 */
	public boolean isNew() {
		return isNew;
	}



	/**
	 * @param isNew the isNew to set
	 */
	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}



	/**
	 * @return the isChanged
	 */
	public boolean isChanged() {
		return isChanged;
	}



	/**
	 * @param isChanged the isChanged to set
	 */
	public void setChanged(boolean isChanged) {
		this.isChanged = isChanged;
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
	 * @return the userName
	 */
	public String getUserName() {
		return UserName;
	}



	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		UserName = userName;
	}



	/**
	 * @return the feedbackID
	 */
	public String getFeedbackID() {
		return feedbackID;
	}



	/**
	 * @param feedbackID the feedbackID to set
	 */
	public void setFeedbackID(String feedbackID) {
		this.feedbackID = feedbackID;
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
	 * @return the deviceID
	 */
	public String getDeviceID() {
		return deviceID;
	}



	/**
	 * @param deviceID the deviceID to set
	 */
	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}



	/**
	 * @return the customerID
	 */
	public String getCustomerID() {
		return customerID;
	}



	/**
	 * @param customerID the customerID to set
	 */
	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}



	@Override
	public boolean persist(Map<String, Object> input, Map<String, Object> headers) {
		 if (!isNew && isChanged) {
	            return !ServiceCallHelper.invokeServiceAndGetJson(input, headers, URLConstants.FEEDBACKSTATUS_UPDATE)
	                    .has("errmsg");
	        }

	        if (isNew) {
	            return !ServiceCallHelper.invokeServiceAndGetJson(input, headers, URLConstants.FEEDBACKSTATUS_CREATE)
	                    .has("errmsg");
	        }

	        return true;
	}



	@Override
	public Object loadDTO(String id) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public Object loadDTO() {
		String filter = "customerID" + DBPUtilitiesConstants.EQUAL + customerID;
		if (StringUtils.isNotBlank(deviceID)) {
			filter = filter + DBPUtilitiesConstants.AND + "deviceID" + DBPUtilitiesConstants.EQUAL + deviceID;
		}
		
		List<DBPDTOEXT> exts = new ArrayList<DBPDTOEXT>();

		DTOUtils.loadDTOListfromDB(exts, filter, URLConstants.FEEDBACKSTATUS_GET, true, true);

		if (exts != null && exts.size() > 0) {
			return exts.get(0);
		}

		return null;
	}

}
