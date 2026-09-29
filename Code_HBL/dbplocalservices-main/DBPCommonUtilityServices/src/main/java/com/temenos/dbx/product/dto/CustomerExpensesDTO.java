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

public class CustomerExpensesDTO implements DBPDTOEXT {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6155170374823047367L;
	private String amount;
	private String id;
	private String type;
	private String customer_id;
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
	 * @return the amount
	 */
	public String getAmount() {
		return amount;
	}
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(String amount) {
		this.amount = amount;
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
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
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
	public boolean persist(Map<String, Object> input, Map<String, Object> headers) {
		if(!isNew && isChanged){
			return !ServiceCallHelper.invokeServiceAndGetJson(input, headers, URLConstants.CUSTOMER_EXPENSES_UPDATE).has("errmsg");
		}

		if(isNew){
			return !ServiceCallHelper.invokeServiceAndGetJson(input, headers, URLConstants.CUSTOMER_EXPENSES_CREATE).has("errmsg");
		}

		return true;
	}

	@Override
    public Object loadDTO(String id) {
        String filter = "Customer_id"+ DBPUtilitiesConstants.EQUAL + id;
        List<DBPDTOEXT> exts = new ArrayList<DBPDTOEXT>();

        DTOUtils.loadDTOListfromDB(exts, filter, URLConstants.CUSTOMER_EXPENSES_GET, true, true);

        return exts;
    }
	
	@Override
	public Object loadDTO() {
		List<DBPDTOEXT> exts = new ArrayList<DBPDTOEXT>();

		if(StringUtils.isNotBlank(customer_id)) {
			String filter = "Customer_id"+ DBPUtilitiesConstants.EQUAL + customer_id;
			DTOUtils.loadDTOListfromDB(exts, filter, URLConstants.CUSTOMER_EXPENSES_GET, true, true);
		}
		return exts;
	}

}
