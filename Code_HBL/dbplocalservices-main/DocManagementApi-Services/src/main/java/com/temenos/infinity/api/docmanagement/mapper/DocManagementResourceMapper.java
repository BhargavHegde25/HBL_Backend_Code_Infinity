package com.temenos.infinity.api.docmanagement.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;
import com.temenos.infinity.api.docmanagement.resource.api.DMSResource;
import com.temenos.infinity.api.docmanagement.resource.impl.DMSResourceImpl;
import com.temenos.infinity.api.docmanagement.resource.api.TransactionReportDownloadResource;
import com.temenos.infinity.api.docmanagement.resource.impl.TransactionReportDownloadResourceImpl;

public class DocManagementResourceMapper implements DBPAPIMapper<Resource> {

    @Override
    public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {
        Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();

        map.put(DMSResource.class, DMSResourceImpl.class);
        map.put(TransactionReportDownloadResource.class, TransactionReportDownloadResourceImpl.class);

        return map;
    }
}
