package com.temenos.infinity.api.loanspayoff.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.infinity.api.loanspayoff.businessdelegate.api.LoansPayoffBusinessDelegate;
import com.temenos.infinity.api.loanspayoff.businessdelegate.impl.LoansPayoffBusinessDelegateImpl;

/**
 * 
 * @author KH2281 version 1.0 implements {@link DBPAPIMapper}
 */
public class LoansPayoffBusinessDelegateMapper implements DBPAPIMapper<BusinessDelegate> {

    @Override
    public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {
        Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();

        // Add Mapping of Business Delegates interface and their implementation
        map.put(LoansPayoffBusinessDelegate.class, LoansPayoffBusinessDelegateImpl.class);

        return map;
    }

}
