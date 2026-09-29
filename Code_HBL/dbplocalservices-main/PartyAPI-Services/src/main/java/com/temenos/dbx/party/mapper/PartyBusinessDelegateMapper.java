package com.temenos.dbx.party.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.dbx.party.businessdelegate.api.AuditActivityPartyCustomerInformationUpdateBusinessDelegate;
import com.temenos.dbx.party.businessdelegate.api.PartyCustomerBusinessDelegate;
import com.temenos.dbx.party.businessdelegate.api.ProspectBusinessDelegate;
import com.temenos.dbx.party.businessdelegate.api.UpdateCustomerApplicationBusinessDelegate;
import com.temenos.dbx.party.businessdelegate.api.DueDiligenceBusinessDelegate;
import com.temenos.dbx.party.businessdelegate.impl.AuditActivityPartyCustomerInformationUpdateBusinessDelegateImpl;
import com.temenos.dbx.party.businessdelegate.impl.PartyCustomerBusinessDelegateImpl;
import com.temenos.dbx.party.businessdelegate.impl.ProspectBusinessDelegateImpl;
import com.temenos.dbx.party.businessdelegate.impl.UpdateCustomerApplicationBusinessDelegateImpl;
import com.temenos.dbx.party.businessdelegate.impl.DueDiligenceBusinessDelegateImpl;

public class PartyBusinessDelegateMapper implements DBPAPIMapper<BusinessDelegate> {

    @Override
    public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {
        Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();
        map.put(PartyCustomerBusinessDelegate.class, PartyCustomerBusinessDelegateImpl.class);

        map.put(ProspectBusinessDelegate.class, ProspectBusinessDelegateImpl.class);

        map.put(UpdateCustomerApplicationBusinessDelegate.class, UpdateCustomerApplicationBusinessDelegateImpl.class);

        map.put(AuditActivityPartyCustomerInformationUpdateBusinessDelegate.class,
                AuditActivityPartyCustomerInformationUpdateBusinessDelegateImpl.class);
        map.put(DueDiligenceBusinessDelegate.class, DueDiligenceBusinessDelegateImpl.class);
        return map;
    }

}
