package com.temenos.dbx.product.commons.businessdelegate.api;

import java.util.List;

import com.dbp.core.api.BusinessDelegate;
import com.temenos.dbx.product.commons.dto.FeatureDTO;

public interface FeatureBusinessDelegate extends BusinessDelegate {

	/**
	 * fetches all the features
	 * @return List<FeatureDTO>
	 */
	public List<FeatureDTO> fetchFeatures();
	
	/**
	 * fetches feature by Id
	 * @param featureId
	 * @return FeatureDTO
	 */
	public FeatureDTO fetchFeatureById(String featureId);
	
}
