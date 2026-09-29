package com.temenos.dbx.product.commons.businessdelegate.api;

import java.util.List;

import com.dbp.core.api.BusinessDelegate;
import com.temenos.dbx.product.commons.dto.LimitGroupDTO;
import com.temenos.dbx.product.commons.dto.LimitsDTO;

public interface LimitGroupBusinessDelegate extends BusinessDelegate {

	/**
	 * fetch all the limitGroups
	 * @return List<LimitGroupDTO>
	 */
	public List<LimitGroupDTO> fetchLimitGroups();
	
	/**
	 * To fetch the LimitGroup level limits for a given customerId , contractId, coreCustomerId, limitGroupId
	 * @param customerId
	 * @param contractId
	 * @param coreCustomerId
	 * @param limitGroupId
	 * @return {@link LimitsDTO}
	 */
	public LimitsDTO fetchLimits(String customerId, String contractId, String coreCustomerId, String limitGroupId);

	/**
	 * To fetch the exhausted limits for a given companyID, customerId, featureActionID, date 
	 * @param companyId
	 * @param customerId
	 * @param featureActionID
	 * @param date
	 * @return {@link LimitsDTO}
	 */
	public LimitsDTO fetchExhaustedLimits(String contractId, String coreCustomerId, String customerId, String featureActionID, String date);
	
	/**
	 * fetch all the limitGroups
	 * @param langId
	 * @return List<LimitGroupDTO>
	 */
	public List<LimitGroupDTO> fetchLimitGroupsWithLanguageId(String localeId);
	
}
