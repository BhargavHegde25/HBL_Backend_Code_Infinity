package com.temenos.dbx.transaction.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;
import com.temenos.dbx.transaction.resource.api.BlockedFundsResource;
import com.temenos.dbx.transaction.resource.impl.BlockedFundsResourceImpl;

/**
 * TODO: Document me!
 *
 * @author smugesh
 *
 */
public class BlockedFundsResourceMapper implements DBPAPIMapper<Resource>{
    
    @Override
    public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {
        Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();

        //Add Mapping of Business Delegates interface and their implementation
        map.put(BlockedFundsResource.class, BlockedFundsResourceImpl.class);

        return map;
    }

}
