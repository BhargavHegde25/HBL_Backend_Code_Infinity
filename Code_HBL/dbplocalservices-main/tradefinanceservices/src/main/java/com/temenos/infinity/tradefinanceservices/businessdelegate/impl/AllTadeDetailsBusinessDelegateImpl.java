package com.temenos.infinity.tradefinanceservices.businessdelegate.impl;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.DashboardBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.utils.ExcelBusinessDelegate;

import java.util.HashMap;
import java.util.List;

/**
 * @author naveen.yerra
 */
public class AllTadeDetailsBusinessDelegateImpl implements ExcelBusinessDelegate {
    DashboardBusinessDelegate dashboardBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(DashboardBusinessDelegate.class);
    @Override
    public <T> List<T> getList(DataControllerRequest request) throws ApplicationException {
        return (List<T>) dashboardBusinessDelegate.fetchAllDetails(new HashMap<>(), request);
    }
}
