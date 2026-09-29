package com.temenos.infinity.api.arrangements.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;
import com.temenos.infinity.api.arrangements.resource.api.AccountClosureResource;
import com.temenos.infinity.api.arrangements.resource.api.ArrangementsResource;
import com.temenos.infinity.api.arrangements.resource.api.CustomViewResource;
import com.temenos.infinity.api.arrangements.resource.api.GetAccountsBySearchResource;
import com.temenos.infinity.api.arrangements.resource.api.MortgageFacilityDetailsResource;
import com.temenos.infinity.api.arrangements.resource.api.MortgageFacilityDrawingsResource;
import com.temenos.infinity.api.arrangements.resource.api.SubmitChangeRepaymentResource;
import com.temenos.infinity.api.arrangements.resource.api.SubmitPartialRepaymentResource;
import com.temenos.infinity.api.arrangements.resource.api.UserManagementResource;
import com.temenos.infinity.api.arrangements.resource.impl.AccountClosureResourceImpl;
import com.temenos.infinity.api.arrangements.resource.impl.ArrangementsResourceImpl;
import com.temenos.infinity.api.arrangements.resource.impl.CustomViewResourceImpl;
import com.temenos.infinity.api.arrangements.resource.impl.GetAccountsBySearchResourceImpl;
import com.temenos.infinity.api.arrangements.resource.impl.MortgageFacilityDetailsResourceImpl;
import com.temenos.infinity.api.arrangements.resource.impl.MortgageFacilityDrawingsResourceImpl;
import com.temenos.infinity.api.arrangements.resource.impl.SubmitChangeRepaymentResourceImpl;
import com.temenos.infinity.api.arrangements.resource.impl.SubmitPartialRepaymentResourceImpl;
import com.temenos.infinity.api.arrangements.resource.impl.UserManagementResourceImpl;

/**
 * 
 * @author KH2281 version 1.0 implements {@link DBPAPIMapper}
 */

public class ArrangementsResourceMapper implements DBPAPIMapper<Resource> {

    @Override
    public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {

        Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();

        // Add Mapping of Business Delegates interface and their implementation
        map.put(ArrangementsResource.class, ArrangementsResourceImpl.class);
        map.put(GetAccountsBySearchResource.class, GetAccountsBySearchResourceImpl.class);
        map.put(MortgageFacilityDetailsResource.class, MortgageFacilityDetailsResourceImpl.class);
        map.put(MortgageFacilityDrawingsResource.class, MortgageFacilityDrawingsResourceImpl.class);
        map.put(CustomViewResource.class, CustomViewResourceImpl.class);
        map.put(UserManagementResource.class, UserManagementResourceImpl.class);
        map.put(SubmitChangeRepaymentResource.class, SubmitChangeRepaymentResourceImpl.class);
        map.put(SubmitPartialRepaymentResource.class, SubmitPartialRepaymentResourceImpl.class);
        map.put(AccountClosureResource.class, AccountClosureResourceImpl.class);
        
        return map;
    }
}
