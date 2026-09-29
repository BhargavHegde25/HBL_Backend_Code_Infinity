package com.temenos.dbx.product.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.temenos.dbx.product.api.DBPDTOEXT;
import com.temenos.dbx.product.utils.DTOUtils;

public class CustomerFlagStatus implements DBPDTOEXT {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5077250632458245021L;

	private String status_id;
	private String createdby;
	private String modifiedby;
	private String createdts;
	private String lastmodifiedts;
	private String synctimestamp;
	private String softdeleteflag;
	private boolean isNew;
	private boolean isChanged;

	private String customer_id;
	/**
	 * @return the status_id
	 */
	public String getStatus_id() {
		return status_id;
	}

	/**
	 * @param status_id the status_id to set
	 */
	public void setStatus_id(String status_id) {
		this.status_id = status_id;
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
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	} 



	@Override
	public boolean persist(Map<String, Object> input, Map<String, Object> headers) {
		if(!isNew && isChanged){
			return !ServiceCallHelper.invokeServiceAndGetJson(input, headers, URLConstants.CUSTOMERFLAG_DELETE).has("errmsg");
		}

		if(isNew) {
			return !ServiceCallHelper.invokeServiceAndGetJson(input, headers, URLConstants.CUSTOMERFLAG_CREATE).has("errmsg");
		}

		return true;
	}

	 @Override
	    public Object loadDTO(String id) {
	        String filter = "Customer_id"+ DBPUtilitiesConstants.EQUAL + id;
	        List<DBPDTOEXT> exts = new ArrayList<DBPDTOEXT>();

	        DTOUtils.loadDTOListfromDB(exts, filter, URLConstants.CUSTOMERFLAG_GET, true, true);

	        if(exts != null && exts.size() >0) {
	            return exts;
	        }

	        return null;
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

	@Override
	public Object loadDTO() {

		if(StringUtils.isNotBlank(customer_id)) {
			String filter = "Customer_id"+ DBPUtilitiesConstants.EQUAL + customer_id;
			List<DBPDTOEXT> exts = new ArrayList<DBPDTOEXT>();

			DTOUtils.loadDTOListfromDB(exts, filter, URLConstants.CUSTOMERFLAG_GET, true, true);

			if(exts != null && exts.size() >0) {
				return exts;
			}
		}

		return null;
	}


}
