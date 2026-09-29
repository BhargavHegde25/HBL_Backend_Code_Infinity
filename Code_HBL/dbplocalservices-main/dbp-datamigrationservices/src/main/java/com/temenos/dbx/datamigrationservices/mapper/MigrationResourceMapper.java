package com.temenos.dbx.datamigrationservices.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;
import com.temenos.dbx.datamigrationservices.resource.api.MigrateApprovalMatrixResource;
import com.temenos.dbx.datamigrationservices.resource.api.MigrateApprovalModeResource;
import com.temenos.dbx.datamigrationservices.resource.api.MigrateContractResource;
import com.temenos.dbx.datamigrationservices.resource.api.MigrateInfinityUserResource;
import com.temenos.dbx.datamigrationservices.resource.api.MigratePayeesResource;
import com.temenos.dbx.datamigrationservices.resource.impl.MigrateApprovalMatrixResourceImpl;
import com.temenos.dbx.datamigrationservices.resource.impl.MigrateApprovalModeResourceImpl;
import com.temenos.dbx.datamigrationservices.resource.impl.MigrateContractResourceImpl;
import com.temenos.dbx.datamigrationservices.resource.impl.MigrateInfinityUserResourceImpl;
import com.temenos.dbx.datamigrationservices.resource.impl.MigratePayeesResourceImpl;

public class MigrationResourceMapper implements DBPAPIMapper<Resource> {

	@Override
	public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {
        Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();
        map.put(MigrateInfinityUserResource.class, MigrateInfinityUserResourceImpl.class);
        map.put(MigrateContractResource.class, MigrateContractResourceImpl.class);
        map.put(MigrateApprovalMatrixResource.class, MigrateApprovalMatrixResourceImpl.class);
        map.put(MigrateApprovalModeResource.class, MigrateApprovalModeResourceImpl.class);
        map.put(MigratePayeesResource.class, MigratePayeesResourceImpl.class);
        return map;
	}

}
