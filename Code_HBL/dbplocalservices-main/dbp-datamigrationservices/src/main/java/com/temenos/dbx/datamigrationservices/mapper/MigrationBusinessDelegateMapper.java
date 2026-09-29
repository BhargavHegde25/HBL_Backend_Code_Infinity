/**
 * 
 */
package com.temenos.dbx.datamigrationservices.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.dbx.datamigrationservices.business.api.MigrateInfinityUserBusinessDelegate;
import com.temenos.dbx.datamigrationservices.business.impl.MigrateInfinityUserBusinessDelegateImpl;

/**
 * @author amitabh.kotha
 *
 */
public class MigrationBusinessDelegateMapper implements DBPAPIMapper<BusinessDelegate> {

	@Override
	public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {
		
		Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();
		map.put(MigrateInfinityUserBusinessDelegate.class , MigrateInfinityUserBusinessDelegateImpl.class);
		return map;
	}

}
