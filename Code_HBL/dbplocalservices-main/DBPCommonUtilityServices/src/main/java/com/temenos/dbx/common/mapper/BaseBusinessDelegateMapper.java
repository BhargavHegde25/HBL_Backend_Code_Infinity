package com.temenos.dbx.common.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.delegate.ServicePermissionsBusinessDelegate;
import com.temenos.dbx.actions.businessdelegate.api.AccountActionBusinessDelegate;
import com.temenos.dbx.actions.businessdelegate.impl.AccountActionBusinessDelegateImpl;
import com.temenos.dbx.filesgenerator.businessdelegate.api.TransactionReportPDFGeneratorBD;
import com.temenos.dbx.filesgenerator.businessdelegate.impl.TransactionReportPDFGeneratorBDImpl;
import com.temenos.dbx.regulator.businessdelegate.impl.ServicePermissionsBusinessDelegateImpl;

public class BaseBusinessDelegateMapper implements DBPAPIMapper<BusinessDelegate> {

    @Override
    public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {
        Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();
        map.put(ServicePermissionsBusinessDelegate.class, ServicePermissionsBusinessDelegateImpl.class);
        map.put(AccountActionBusinessDelegate.class, AccountActionBusinessDelegateImpl.class);
        map.put(TransactionReportPDFGeneratorBD.class, TransactionReportPDFGeneratorBDImpl.class);
        return map;
    }

}
