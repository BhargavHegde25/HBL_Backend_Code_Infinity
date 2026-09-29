package com.temenos.dbx.product.commons.businessdelegate.api;

import com.dbp.core.api.BusinessDelegate;
import com.temenos.dbx.product.commons.dto.LimitsDTO;

public interface UserRoleBusinessDelegate extends BusinessDelegate {
	
	/**
	 * Fetches User Role limits for a given role and actionId
	 * @param userRoleId
	 * @param featureActionID
	 * @return {@link LimitsDTO}
	 */
	public LimitsDTO fetchLimits(String userRoleId, String featureActionID);
	
	/**
	 * fetched the exhausted Role level limits for a given RoleId and featureActionID
	 * @param companyId
	 * @param userRoleId
	 * @param featureActionID
	 * @param date
	 * @return {@link LimitsDTO}
	 */
	public LimitsDTO fetchExhaustedLimits(String contractId, String coreCustomerId, String userRoleId, String featureActionID, String date,  String customerId);

}
