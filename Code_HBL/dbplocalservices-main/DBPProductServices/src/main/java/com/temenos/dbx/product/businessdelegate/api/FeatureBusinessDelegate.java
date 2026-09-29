package com.temenos.dbx.product.businessdelegate.api;

import java.util.Map;
import com.dbp.core.api.BusinessDelegate;
import com.kony.dbp.exception.ApplicationException;
import com.temenos.dbx.product.dto.DBXResult;

/**
 * 
 * @author sowmya.vandanapu
 * @version 1.0
 * Interface for FeatureAction Business Delegate
 */
public interface FeatureBusinessDelegate extends BusinessDelegate{
	
	/**
	 * 
	 * @param featureDTO
	 * @param headersMap
	 * @return fetches all the features from feature table
	 * @throws ApplicationException 
	 */
	public DBXResult getFeatures(Map<String,Object> headersMap) throws ApplicationException;

}
