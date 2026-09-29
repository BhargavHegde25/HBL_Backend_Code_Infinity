package com.temenos.dbx.product.payeeservices.mapper;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;
import com.temenos.dbx.product.payeeservices.resource.api.*;
import com.temenos.dbx.product.payeeservices.resource.impl.*;

import java.util.HashMap;
import java.util.Map;

/**
 * This class maps resource interface to its implementation and it can be
 * changed whenever required
 *
 * @author kkrath KH2544
 *
 *  **/
public class PayeeManagementResourceMapper implements DBPAPIMapper<Resource> {

    @Override
    public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {
        Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();
        /* all resource interface to implementation mappings are done here */
        map.put(BillPayPayeeResource.class, BillPayPayeeResourceImpl.class);
        map.put(InterBankPayeeResource.class, InterBankPayeeResourceImpl.class);
        map.put(ExternalPayeeResource.class, ExternalPayeeResourceImpl.class);
        map.put(WireTransfersPayeeResource.class, WireTransfersPayeeResourceImpl.class);
        map.put(IntraBankPayeeResource.class, IntraBankPayeeResourceImpl.class);
        map.put(InternationalPayeeResource.class, InternationalPayeeResourceImpl.class);
        map.put(P2PPayeeResource.class, P2PPayeeResourceImpl.class);
        map.put(BulkPaymentsPayeeResource.class, BulkPaymentsPayeeResourceImpl.class);
        map.put(GetExternalPayeesResource.class, GetExternalPayeesResourceImpl.class);
        return map;
    }
}
