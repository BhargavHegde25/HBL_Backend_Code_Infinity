package com.temenos.dbx.product.commons.businessdelegate.api;

import com.dbp.core.api.BusinessDelegate;
import com.temenos.dbx.product.commons.dto.LimitsDTO;

public interface OrganisationBusinessDelegate extends BusinessDelegate {

	/**
	 * returns a comma separated string with list of active feature actions for the given companyId
	 * @param companyId
	 * @return String
	 */
	public String fetchActiveFeatureActions(String companyId);
	
	/**
	 * fetched the company level limits for a given companyID and featureActionID
	 * @param companyId
	 * @param featureActionID
	 * @return {@link LimitsDTO}
	 */
	public LimitsDTO fetchLimits(String companyId, String featureActionID);
	
	/**
	 * fetched the exhausted company level limits for a given companyID and featureActionID
	 * @param companyId
	 * @param featureActionID
	 * @param date
	 * @return {@link LimitsDTO}
	 */
	public LimitsDTO fetchExhaustedLimits(String companyId, String featureActionID, String date);
	
}
