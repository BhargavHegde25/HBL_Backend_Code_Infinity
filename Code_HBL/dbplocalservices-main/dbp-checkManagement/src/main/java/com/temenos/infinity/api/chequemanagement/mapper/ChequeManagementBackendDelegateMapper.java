package com.temenos.infinity.api.chequemanagement.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.infinity.api.chequemanagement.backenddelegate.api.ApproveCheckBookRequestBackendDelegate;
import com.temenos.infinity.api.chequemanagement.backenddelegate.api.ChequeManagementBackendDelegate;
import com.temenos.infinity.api.chequemanagement.backenddelegate.api.CreateChequeBookBackendDelegate;
import com.temenos.infinity.api.chequemanagement.backenddelegate.api.CreateStopPaymentBackendDelegate;
import com.temenos.infinity.api.chequemanagement.backenddelegate.api.GetChequeBookBackendDelegate;
import com.temenos.infinity.api.chequemanagement.backenddelegate.api.GetCommandNameBackendDelegate;
import com.temenos.infinity.api.chequemanagement.backenddelegate.api.GetStopPaymentBackendDelegate;
import com.temenos.infinity.api.chequemanagement.backenddelegate.impl.ApproveCheckBookRequestBackendDelegateImpl;
import com.temenos.infinity.api.chequemanagement.backenddelegate.api.RevokeStopPaymentBackendDelegate;
import com.temenos.infinity.api.chequemanagement.backenddelegate.impl.ChequeManagementBackendDelegateImpl;
import com.temenos.infinity.api.chequemanagement.backenddelegate.impl.CreateChequeBookBackendDelegateImpl;
import com.temenos.infinity.api.chequemanagement.backenddelegate.impl.CreateStopPaymentBackendDelegateImpl;
import com.temenos.infinity.api.chequemanagement.backenddelegate.impl.GetChequeBookBackendDelegateImpl;
import com.temenos.infinity.api.chequemanagement.backenddelegate.impl.GetCommandNameBackendDelegateImpl;
import com.temenos.infinity.api.chequemanagement.backenddelegate.impl.GetStopPaymentBackendDelegateImpl;
import com.temenos.infinity.api.chequemanagement.backenddelegate.impl.RevokeStopPaymentBackendDelegateImpl;

/**
 * Mapper between Backend Delegate Interfaces & corresponding Backend Delegate Implementation classes
 * 
 * @author Aditya Mankal
 *
 */
public class ChequeManagementBackendDelegateMapper implements DBPAPIMapper<BackendDelegate> {

    @Override
    public Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> getAPIMappings() {
        Map<Class<? extends BackendDelegate>, Class<? extends BackendDelegate>> map = new HashMap<>();

        // Register your BackendDelegate Delegate Implementation classes here
        map.put(CreateChequeBookBackendDelegate.class, CreateChequeBookBackendDelegateImpl.class);
        map.put(GetChequeBookBackendDelegate.class, GetChequeBookBackendDelegateImpl.class);
        map.put(CreateStopPaymentBackendDelegate.class, CreateStopPaymentBackendDelegateImpl.class); 
        map.put(GetStopPaymentBackendDelegate.class, GetStopPaymentBackendDelegateImpl.class);
        map.put(RevokeStopPaymentBackendDelegate.class, RevokeStopPaymentBackendDelegateImpl.class);
        map.put(ApproveCheckBookRequestBackendDelegate.class, ApproveCheckBookRequestBackendDelegateImpl.class);
        map.put(ChequeManagementBackendDelegate.class, ChequeManagementBackendDelegateImpl.class);
        map.put(GetCommandNameBackendDelegate.class, GetCommandNameBackendDelegateImpl.class);
        return map;
    }

} 
