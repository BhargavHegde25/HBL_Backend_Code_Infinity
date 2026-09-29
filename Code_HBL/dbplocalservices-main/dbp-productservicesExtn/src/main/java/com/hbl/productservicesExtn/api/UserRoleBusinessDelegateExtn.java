package com.hbl.productservicesExtn.api;

import com.hbl.productservicesExtn.dto.LimitsDTOExtn;
import com.temenos.dbx.product.commons.businessdelegate.api.UserRoleBusinessDelegate;
import com.temenos.dbx.product.commons.dto.LimitsDTO;

public interface UserRoleBusinessDelegateExtn extends UserRoleBusinessDelegate{
	public LimitsDTOExtn fetchLimits(String userRoleId, String featureActionID);
	public LimitsDTOExtn fetchExhaustedLimits(String contractId, String coreCustomerId, String userRoleId, String featureActionID, String date,  String customerId);
}
