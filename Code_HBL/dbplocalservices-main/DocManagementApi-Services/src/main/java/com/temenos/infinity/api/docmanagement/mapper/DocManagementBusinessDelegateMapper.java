package com.temenos.infinity.api.docmanagement.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.infinity.api.docmanagement.businessdelegate.api.DMSBusinessDelegate;
import com.temenos.infinity.api.docmanagement.businessdelegate.api.TransactionReportBusinessDelegate;
import com.temenos.infinity.api.docmanagement.businessdelegate.impl.DMSBusinessDelegateImpl;
import com.temenos.infinity.api.docmanagement.businessdelegate.impl.TransactionReportBusinessDelegateImpl;

public class DocManagementBusinessDelegateMapper implements DBPAPIMapper<BusinessDelegate> {

    @Override
    public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {
        Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();

        map.put(DMSBusinessDelegate.class, DMSBusinessDelegateImpl.class);
        map.put(TransactionReportBusinessDelegate.class, TransactionReportBusinessDelegateImpl.class);

        return map;
    }

}
