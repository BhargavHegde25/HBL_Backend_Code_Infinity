package com.temenos.dbx.product.accounts.businessdelegate.api;

import java.util.List;

import com.dbp.core.api.BusinessDelegate;
import com.kony.dbp.dto.CustomViewDTO;

public interface CustomViewBusinessDelegate extends BusinessDelegate {
	
	/**
     * Gets all the custom view details of given customerId
     * @param CustomViewDTO customViewDTO
     * @return List of {@link CustomViewDTO}
     */
	public List<CustomViewDTO> getCustomView(CustomViewDTO customViewDTO);
	
	/**
     * Creates a custom view 
     * @param CustomViewDTO customViewDTO 
     * @return {@link CustomViewDTO}
     */
	public CustomViewDTO createCustomView(CustomViewDTO customViewDTO);
	
	/**
     * Deletes a custom view based on the id
     * @param String id
     * @return boolean - true if success else false
     */
	public boolean deleteCustomView(String id);
	
	/**
	 * Edits a custom view based on the id
	 * @param CustomViewDTO customViewDTO
	 * @return
	 */
	public CustomViewDTO editCustomView(CustomViewDTO customViewDTO);

}
