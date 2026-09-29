package com.temenos.dbx.product.achservices.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;
import com.temenos.dbx.product.achservices.resource.api.ACHCommonsResource;
import com.temenos.dbx.product.achservices.resource.api.ACHFileRecordResource;
import com.temenos.dbx.product.achservices.resource.api.ACHFileResource;
import com.temenos.dbx.product.achservices.resource.api.ACHFileSubRecordResource;
import com.temenos.dbx.product.achservices.resource.api.ACHTemplateRecordResource;
import com.temenos.dbx.product.achservices.resource.api.ACHTemplateResource;
import com.temenos.dbx.product.achservices.resource.api.ACHTemplateSubRecordResource;
import com.temenos.dbx.product.achservices.resource.api.ACHTransactionRecordResource;
import com.temenos.dbx.product.achservices.resource.api.ACHTransactionResource;
import com.temenos.dbx.product.achservices.resource.api.ACHTransactionSubRecordResource;
import com.temenos.dbx.product.achservices.resource.impl.ACHCommonsResourceImpl;
import com.temenos.dbx.product.achservices.resource.impl.ACHFileRecordResourceImpl;
import com.temenos.dbx.product.achservices.resource.impl.ACHFileResourceImpl;
import com.temenos.dbx.product.achservices.resource.impl.ACHFileSubRecordResourceImpl;
import com.temenos.dbx.product.achservices.resource.impl.ACHTemplateRecordResourceImpl;
import com.temenos.dbx.product.achservices.resource.impl.ACHTemplateResourceImpl;
import com.temenos.dbx.product.achservices.resource.impl.ACHTemplateSubRecordResourceImpl;
import com.temenos.dbx.product.achservices.resource.impl.ACHTransactionRecordImpl;
import com.temenos.dbx.product.achservices.resource.impl.ACHTransactionResourceImpl;
import com.temenos.dbx.product.achservices.resource.impl.ACHTransactionSubRecordImpl;

public class ACHServicesResourceMapper implements DBPAPIMapper<Resource>{

	@Override
	public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {
		Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();
		
		//Add Mapping of Business Delegates interface and their implementation
        map.put(ACHTemplateResource.class, ACHTemplateResourceImpl.class);
		map.put(ACHFileResource.class, ACHFileResourceImpl.class);
		map.put(ACHTransactionResource.class, ACHTransactionResourceImpl.class);
        map.put(ACHCommonsResource.class, ACHCommonsResourceImpl.class);
        map.put(ACHTransactionRecordResource.class, ACHTransactionRecordImpl.class);
        map.put(ACHTransactionSubRecordResource.class,ACHTransactionSubRecordImpl.class);
		map.put(ACHTemplateRecordResource.class, ACHTemplateRecordResourceImpl.class);
        map.put(ACHTemplateSubRecordResource.class, ACHTemplateSubRecordResourceImpl.class);
        map.put(ACHFileRecordResource.class, ACHFileRecordResourceImpl.class);
        map.put(ACHFileSubRecordResource.class, ACHFileSubRecordResourceImpl.class);
        return map;
	}

}
