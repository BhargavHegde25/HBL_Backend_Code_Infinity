package com.kony.adminconsole.service.contract.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.kony.adminconsole.service.contract.businessdelegate.api.ApprovalMatrixManageBusinessDelegate;
import com.kony.adminconsole.service.contract.businessdelegate.api.ContractBusinessDelegate;
import com.kony.adminconsole.service.contract.businessdelegate.impl.ApprovalMatrixManageBusinessDelegateImpl;
import com.kony.adminconsole.service.contract.businessdelegate.impl.ContractBusinessDelegateImpl;

public class ContractBusinessDelegateMapper implements DBPAPIMapper<BusinessDelegate> {

    @Override
    public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {
        Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();
        map.put(ContractBusinessDelegate.class, ContractBusinessDelegateImpl.class);
        map.put(ApprovalMatrixManageBusinessDelegate.class, ApprovalMatrixManageBusinessDelegateImpl.class);
        return map;
    }
}
