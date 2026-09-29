package com.temenos.dbx.product.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.util.BCrypt;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.temenos.dbx.product.api.DBPDTOEXT;
import com.temenos.dbx.product.utils.DTOUtils;

public class PasswordHistoryDTO implements DBPDTOEXT {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8034633732574011187L;
	private String id;
	private String customer_id;
	private String previousPassword;
	private String createdby;
	private String modifiedby;
	private String createdts;
	private String lastmodifiedts;
	private String synctimestamp;
	private Boolean softdeleteflag;
	private boolean isNew;

	/**
	 * @param isNew the isNew to set
	 */
	public void setIsNew(boolean isNew) {
		this.isNew = isNew;
	}


	/**
	 * @return the isNew
	 */
	public Boolean getIsNew() {
		return isNew;
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
	 * @return the previousPassword
	 */
	public String getPreviousPassword() {
		return previousPassword;
	}


	/**
	 * @param previousPassword the previousPassword to set
	 */
	public void setPreviousPassword(String previousPassword) {
		this.previousPassword = previousPassword;
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
		createdts = HelperMethods.getFormattedTimeStamp(HelperMethods.getFormattedTimeStamp(createdts), "yyyy-MM-dd");
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
		return !ServiceCallHelper.invokeServiceAndGetJson(input, headers, URLConstants.PASSWORDHISTORY_CREATE).has("errmsg");
	}

	private Object loadDTOList(int count) {
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put(DBPUtilitiesConstants.FILTER, "Customer_id"+ DBPUtilitiesConstants.EQUAL + customer_id);
		hashMap.put(DBPUtilitiesConstants.ORDERBY, "createdts desc");
		hashMap.put(DBPUtilitiesConstants.TOP, Integer.toString(count));
		hashMap.put(DBPUtilitiesConstants.SKIP, "0");

		List<DBPDTOEXT> exts = new ArrayList<DBPDTOEXT>();

		DTOUtils.loadDTOListfromDB(exts, hashMap, URLConstants.PASSWORDHISTORY_GET, true, false);

		return exts;
	}

	@Override
	public Object loadDTO(String id) {
		String filter = "Customer_id"+ DBPUtilitiesConstants.EQUAL + id;
		List<DBPDTOEXT> exts = new ArrayList<DBPDTOEXT>();

		DTOUtils.loadDTOListfromDB(exts, filter, URLConstants.PASSWORDHISTORY_GET, true, false);

		if(exts != null && exts.size() >0 ) {
			return exts.get(exts.size()-1);
		}

		return null;
	}


	@Override
	public Object loadDTO() {

		if(StringUtils.isNotBlank(customer_id)) {
			String filter = "Customer_id"+ DBPUtilitiesConstants.EQUAL + customer_id;
			List<DBPDTOEXT> exts = new ArrayList<DBPDTOEXT>();

			DTOUtils.loadDTOListfromDB(exts, filter, URLConstants.PASSWORDHISTORY_GET, true, false);

			if(exts != null && exts.size() >0 ) {
				return exts.get(exts.size()-1);
			}
		}
		return null;
	}


	public boolean checkForPasswordEntry(int count, String password) {

		@SuppressWarnings("unchecked")
		List<DBPDTOEXT> dtos = (List<DBPDTOEXT>) loadDTOList(count);

		if(dtos == null || dtos.size() <= 0) {
			return true;
		}

		for (DBPDTOEXT dto : dtos) {
			PasswordHistoryDTO historyDTO = (PasswordHistoryDTO) dto;

			if (StringUtils.isNotBlank(historyDTO.getPreviousPassword())&& BCrypt.checkpw(password, historyDTO.getPreviousPassword())) {
				return false;
			}
		}

		return true;

	}




}
