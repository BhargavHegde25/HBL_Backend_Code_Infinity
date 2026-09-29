package com.temenos.dbx.eum.product.usermanagement.businessdelegate.api;

import java.util.List;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.google.gson.JsonObject;
import com.temenos.dbx.eum.product.dto.CustomerImageDTO;

/**
 * 
 * @author KH2627
 * @version 1.0 Interface for CustomerImageBusinessDelegate extends
 *          {@link BusinessDelegate}
 *
 */

public interface CustomerImageBusinessDelegate extends BusinessDelegate {

	/**
	 * 
	 * @param customerImageDTO
	 * @param headersMap
	 * @param url
	 * @return Customer Image
	 */
	JsonObject getCustomerImage(CustomerImageDTO customerImageDTO, Map<String, String> headersMap);

	/**
	 * 
	 * @param customerImageDTO
	 * @param headersMap
	 * @param url
	 * @return boolean flag
	 */
	boolean deleteCustomerImage(CustomerImageDTO customerImageDTO, Map<String, String> headersMap);

	/**
	 * 
	 * @param customerImageDTO
	 * @param headersMap
	 * @param url
	 * @return boolean flag
	 */
	boolean createCustomerImage(CustomerImageDTO customerImageDTO, Map<String, String> headersMap);

	/**
	 * 
	 * @param customerImageDTO
	 * @param headersMap
	 * @param url
	 * @return boolean flag
	 */
	boolean updateCustomerImage(CustomerImageDTO customerImageDTO, Map<String, String> headersMap);
}
