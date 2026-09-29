package com.hbl.productservicesExtn.api;

import com.temenos.dbx.product.commons.businessdelegate.api.CustomerBusinessDelegate;
import com.temenos.dbx.product.commons.dto.LimitsDTO;
import com.temenos.dbx.product.commons.dto.UserLimitsDTO;

public interface CustomerBusinessDelegateExtn extends CustomerBusinessDelegate{
	
	public UserLimitsDTO fetchCustomerLimits(String customerId, String featureActionID, String accountId);

	public LimitsDTO fetchExhaustedLimits(String customerId, String featureActionID, String date, String accountId);
}
