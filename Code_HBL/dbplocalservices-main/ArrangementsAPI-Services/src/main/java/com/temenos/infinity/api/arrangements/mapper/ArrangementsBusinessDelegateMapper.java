package com.temenos.infinity.api.arrangements.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.infinity.api.arrangements.businessdelegate.api.AccountClosureBusinessDelegate;
import com.temenos.infinity.api.arrangements.businessdelegate.api.ArrangementsBusinessDelegate;
import com.temenos.infinity.api.arrangements.businessdelegate.api.CustomViewBusinessDelegate;
import com.temenos.infinity.api.arrangements.businessdelegate.api.GetAccountsBySearchBusinessDelegate;
import com.temenos.infinity.api.arrangements.businessdelegate.api.MortgageFacilityDetailsBusinessDelegate;
import com.temenos.infinity.api.arrangements.businessdelegate.api.MortgageFacilityDrawingBusinessDelegate;
import com.temenos.infinity.api.arrangements.businessdelegate.api.SubmitChangeRepaymentBusinessDelegate;
import com.temenos.infinity.api.arrangements.businessdelegate.api.SubmitPartialRepaymentBusinessDelegate;
import com.temenos.infinity.api.arrangements.businessdelegate.api.UserManagementBusinessDelegate;
import com.temenos.infinity.api.arrangements.businessdelegate.impl.AccountClosureBusinessDelegateImpl;
import com.temenos.infinity.api.arrangements.businessdelegate.impl.ArrangementsBusinessDelegateImpl;
import com.temenos.infinity.api.arrangements.businessdelegate.impl.CustomViewBusinessDelegateImpl;
import com.temenos.infinity.api.arrangements.businessdelegate.impl.GetAccountsBySearchBusinessDelegateImpl;
import com.temenos.infinity.api.arrangements.businessdelegate.impl.MortgageFacilityDetailsBusinessDelegateImpl;
import com.temenos.infinity.api.arrangements.businessdelegate.impl.MortgageFacilityDrawingBusinessDelegateImpl;
import com.temenos.infinity.api.arrangements.businessdelegate.impl.SubmitChangeRepaymentBusinessDelegateImpl;
import com.temenos.infinity.api.arrangements.businessdelegate.impl.SubmitPartialRepaymentBusinessDelegateImpl;
import com.temenos.infinity.api.arrangements.businessdelegate.impl.UserManagementBusinessDelegateImpl;
import com.temenos.infinity.api.arrangements.resource.api.GetAccountsBySearchResource;
import com.temenos.infinity.api.arrangements.resource.impl.GetAccountsBySearchResourceImpl;

/**
 * 
 * @author KH2281 version 1.0 implements {@link DBPAPIMapper}
 */
public class ArrangementsBusinessDelegateMapper implements DBPAPIMapper<BusinessDelegate> {

    @Override
    public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {
        Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();

        // Add Mapping of Business Delegates interface and their implementation
        map.put(ArrangementsBusinessDelegate.class, ArrangementsBusinessDelegateImpl.class);
        map.put(GetAccountsBySearchBusinessDelegate.class, GetAccountsBySearchBusinessDelegateImpl.class);
        map.put(MortgageFacilityDetailsBusinessDelegate.class, MortgageFacilityDetailsBusinessDelegateImpl.class);
        map.put(MortgageFacilityDrawingBusinessDelegate.class, MortgageFacilityDrawingBusinessDelegateImpl.class);
        map.put(CustomViewBusinessDelegate.class, CustomViewBusinessDelegateImpl.class);
        map.put(UserManagementBusinessDelegate.class, UserManagementBusinessDelegateImpl.class);
        map.put(SubmitChangeRepaymentBusinessDelegate.class, SubmitChangeRepaymentBusinessDelegateImpl.class);
        map.put(SubmitPartialRepaymentBusinessDelegate.class, SubmitPartialRepaymentBusinessDelegateImpl.class);
        map.put(AccountClosureBusinessDelegate.class, AccountClosureBusinessDelegateImpl.class);
        return map;
    }

}
