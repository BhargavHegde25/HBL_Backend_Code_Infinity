package com.kony.adminconsole.service.contract.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;
import com.kony.adminconsole.service.contract.resource.api.ApprovalMatrixManageResource;
import com.kony.adminconsole.service.contract.resource.api.ContractResource;
import com.kony.adminconsole.service.contract.resource.impl.ApprovalMatrixManageResourceImpl;
import com.kony.adminconsole.service.contract.resource.impl.ContractResourceImpl;

/**
 * 
 * @author This class maps resource interface to its implementation and it can be changed whenever required
 *
 */

public class ContractResourceMapper implements DBPAPIMapper<Resource> {

    @Override
    public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {
        Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();
        /* all resource interface to implementation mappings are done here */
        map.put(ContractResource.class, ContractResourceImpl.class);
        map.put(ApprovalMatrixManageResource.class, ApprovalMatrixManageResourceImpl.class);
        return map;
    }
}
