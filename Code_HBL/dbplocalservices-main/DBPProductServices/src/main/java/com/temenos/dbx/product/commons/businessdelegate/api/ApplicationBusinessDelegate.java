package com.temenos.dbx.product.commons.businessdelegate.api;

import com.dbp.core.api.BusinessDelegate;
import com.temenos.dbx.product.commons.dto.ApplicationDTO;

public interface ApplicationBusinessDelegate extends BusinessDelegate {

	
	/**
	 * Fetches application Properties
	 * @return ApplicationDTO
	 */
	public ApplicationDTO properties();
	
	/**
	 * gets the server time stamp using server time zone
	 * @return
	 */
	public String getServerTimeStamp();
	
	/**
	 * Fetches application Properties and stores it in cache
	 * @return ApplicationDTO
	 */
	public ApplicationDTO getPropertiesFromCache();

	/**
	 * returns isStateManagementAvailable value from cache
	 * @return
	 */
	public boolean getIsStateManagementAvailableFromCache();
	
	/**
	 *  returns base currency from cache
	 * @return
	 */
	public String getBaseCurrencyFromCache();

	/**
	 * returns the isSelfApprovalEnabled state config from cache
	 * @return
	 */
	public boolean getIsSelfApprovalEnabledFromCache();
	
}
