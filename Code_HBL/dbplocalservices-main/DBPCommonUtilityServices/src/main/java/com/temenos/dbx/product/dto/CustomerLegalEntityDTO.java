package com.temenos.dbx.product.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.temenos.dbx.product.api.DBPDTOEXT;
import com.temenos.dbx.product.utils.DTOConstants;
import com.temenos.dbx.product.utils.DTOUtils;
import org.apache.commons.lang3.StringUtils;

public class CustomerLegalEntityDTO implements DBPDTOEXT {

	private static final long serialVersionUID = 2492515660704055991L;

	private String id;
	private String Customer_id;
	private String Status_id;
	private boolean isNew;
	private boolean isChanged;
	private String legalEntityId;
	private String createdts;
	private String modifiedts;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCustomer_id() {
		return Customer_id;
	}

	public void setCustomer_id(String customer_id) {
		Customer_id = customer_id;
	}

	public String getStatus_id() {
		return Status_id;
	}

	public void setStatus_id(String status_id) {
		Status_id = status_id;
	}

	public boolean isNew() {
		return isNew;
	}

	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}

	public boolean isChanged() {
		return isChanged;
	}

	public void setChanged(boolean isChanged) {
		this.isChanged = isChanged;
	}

	public String getLegalEntityId() {
		return legalEntityId;
	}

	public void setLegalEntityId(String legalEntityId) {
		this.legalEntityId = legalEntityId;
	}

	public String getModifiedts() {
		return modifiedts;
	}

	public void setModifiedts(String modifiedts) {
		this.modifiedts = modifiedts;
	}

	public String getCreatedts() {
		return createdts;
	}

	public void setCreatedts(String createdts) {
		this.createdts = createdts;
	}

	public CustomerLegalEntityDTO() {

	}

	@Override
	public Object loadDTO(String id) {

		String filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + id;
		List<DBPDTOEXT> exts = new ArrayList<DBPDTOEXT>();

		DTOUtils.loadDTOListfromDB(exts, filter, URLConstants.CUSTOMERLEGALENTITY_GET, true, true);

		return exts;
	}

	@Override
	public boolean persist(Map<String, Object> input, Map<String, Object> headers) {
		if (!isNew && isChanged) {
			return !ServiceCallHelper.invokeServiceAndGetJson(input, headers, URLConstants.CUSTOMERLEGALENTITY_UPDATE)
					.has("errmsg");
		}

		if (isNew) {
			return !ServiceCallHelper.invokeServiceAndGetJson(input, headers, URLConstants.CUSTOMERLEGALENTITY_CREATE)
					.has("errmsg");
		}

		return true;

	}

	@Override
	public Object loadDTO() {
		String filter = "";
		if(StringUtils.isNotBlank(Customer_id)) {
			filter = "Customer_id"+ DBPUtilitiesConstants.EQUAL + Customer_id;
		}
		if(StringUtils.isNotBlank(legalEntityId)) {
		    if(StringUtils.isNotBlank(filter)) {
		        filter += DBPUtilitiesConstants.AND;
		    }
            filter += "legalEntityId"+ DBPUtilitiesConstants.EQUAL + legalEntityId;
        }
		List<DBPDTOEXT> exts = new ArrayList<DBPDTOEXT>();

		if(StringUtils.isNotBlank(filter)) {
			DTOUtils.loadDTOListfromDB(exts, filter, URLConstants.CUSTOMERLEGALENTITY_GET, true, true);
		}

		return exts;
	}

}
