/**
 * 
 */
package com.temenos.infinity.api.wealth.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.infinity.api.wealth.businessdelegate.api.ReportAndDownloadTypesBusinessDelegate;
import com.temenos.infinity.api.wealth.businessdelegate.impl.ReportAndDownloadTypesBusinessDelegateImpl;


/**
 * @author himaja.sridhar
 *
 */
public class ReportAndDownloadTypesBusinessDelegateMapper implements DBPAPIMapper<BusinessDelegate> {
	
	 @Override
	    public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {
	        Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();

	        // Add Mapping of Business Delegates interface and their implementation
	        map.put(ReportAndDownloadTypesBusinessDelegate.class, ReportAndDownloadTypesBusinessDelegateImpl.class);

	        return map;
	    }
}
