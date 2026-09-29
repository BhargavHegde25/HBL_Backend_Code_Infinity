package com.temenos.infinity.api.chequemanagement.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.infinity.api.chequemanagement.businessdelegate.api.ChequeManagementBusinessDelegate;
import com.temenos.infinity.api.chequemanagement.businessdelegate.api.CreateChequeBookBusinessDelegate;
import com.temenos.infinity.api.chequemanagement.businessdelegate.api.CreateStopPaymentBusinessDelegate;
import com.temenos.infinity.api.chequemanagement.businessdelegate.api.GetChequeBookBusinessDelegate;
import com.temenos.infinity.api.chequemanagement.businessdelegate.api.GetStopPaymentBusinessDelegate;
import com.temenos.infinity.api.chequemanagement.businessdelegate.api.RevokeStopPaymentBusinessDelegate;
import com.temenos.infinity.api.chequemanagement.businessdelegate.impl.ChequeManagementBusinessDelegateImpl;
import com.temenos.infinity.api.chequemanagement.businessdelegate.impl.CreateChequeBookBusinessDelegateImpl;
import com.temenos.infinity.api.chequemanagement.businessdelegate.impl.CreateStopPaymentBusinessDelegateImpl;
import com.temenos.infinity.api.chequemanagement.businessdelegate.impl.GetChequeBookBusinessDelegateImpl;
import com.temenos.infinity.api.chequemanagement.businessdelegate.impl.GetStopPaymentBusinessDelegateImpl;
import com.temenos.infinity.api.chequemanagement.businessdelegate.impl.RevokeStopPaymentBusinessDelegateImpl;

/**
 * 
 * @author KH2281 version 1.0 implements {@link DBPAPIMapper}
 */
public class ChequeManagementBusinessDelegateMapper implements DBPAPIMapper<BusinessDelegate> {

    @Override
    public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {
        Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();

        // Add Mapping of Business Delegates interface and their implementation
        map.put(CreateChequeBookBusinessDelegate.class, CreateChequeBookBusinessDelegateImpl.class);
        map.put(GetChequeBookBusinessDelegate.class, GetChequeBookBusinessDelegateImpl.class);
        map.put(CreateStopPaymentBusinessDelegate.class, CreateStopPaymentBusinessDelegateImpl.class);
        map.put(GetStopPaymentBusinessDelegate.class, GetStopPaymentBusinessDelegateImpl.class);
        map.put(RevokeStopPaymentBusinessDelegate.class, RevokeStopPaymentBusinessDelegateImpl.class);
        map.put(ChequeManagementBusinessDelegate.class, ChequeManagementBusinessDelegateImpl.class);
        return map; 
    }

}
