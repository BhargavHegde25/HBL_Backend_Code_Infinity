package com.temenos.dbx.product.payeeservices.mapper;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.DBPAPIMapper;
import com.temenos.dbx.product.payeeservices.businessdelegate.api.*;
import com.temenos.dbx.product.payeeservices.businessdelegate.impl.*;

import java.util.HashMap;
import java.util.Map;

/**
* This is a mapper class which maps business delegate interface to its implementations so that
* the implementation can be changed whenever required.
*
* @author kkrath KH2544
 *
*
* **/

public class PayeeManagementDelegateMapper implements DBPAPIMapper<BusinessDelegate> {

    @Override
    public Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> getAPIMappings() {
        Map<Class<? extends BusinessDelegate>, Class<? extends BusinessDelegate>> map = new HashMap<>();

       /* mappings of business delegate interface and implementation to be added */
        map.put(BillPayPayeeBusinessDelegate.class, BillPayPayeeBusinessDelegateImpl.class);
        map.put(InterBankPayeeBusinessDelegate.class, InterBankPayeeBusinessDelegateImpl.class);
        map.put(ExternalPayeeBusinessDelegate.class, ExternalPayeeBusinessDelegateImpl.class);
        map.put(WireTransfersPayeeBusinessDelegate.class, WireTransfersPayeeBusinessDelegateImpl.class);
        map.put(IntraBankPayeeBusinessDelegate.class, IntraBankPayeeBusinessDelegateImpl.class);
        map.put(InternationalPayeeBusinessDelegate.class, InternationalPayeeBusinessDelegateImpl.class);
        map.put(P2PPayeeBusinessDelegate.class, P2PPayeeBusinessDelegateImpl.class);
        map.put(GetExternalPayeesBusinessDelegate.class,GetExternalPayeesBusinessDelegateImpl.class);
        return map;
    }
}
