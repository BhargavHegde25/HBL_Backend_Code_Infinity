/**
 * 
 */
package com.temenos.infinity.api.wealth.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.infinity.api.wealth.backenddelegate.api.ReportAndDownloadTypesBackendDelegate;
import com.temenos.infinity.api.wealth.backenddelegate.impl.ReportAndDownloadTypesBackendDelegateImpl;


/**
 * @author himaja.sridhar
 *
 */
public class ReportAndDownloadTypesBackendDelegateMapper implements DBPAPIMapper<BackendDelegate> {
	
	@Override
	public Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> getAPIMappings() {
		 Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> map = new HashMap<>();

	        // Register your BackendDelegate Delegate Implementation classes here
	        map.put(ReportAndDownloadTypesBackendDelegate.class, ReportAndDownloadTypesBackendDelegateImpl.class);
		return map;
	}
}
