/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2024. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradelending.servlet;

import javax.servlet.http.HttpServlet;

import com.dbp.core.api.APIImplementationTypes;
import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.servlet.IntegrationCustomServlet;
import com.temenos.infinity.tradelending.mapper.TradeLendingBackendDelegateMapper;
import com.temenos.infinity.tradelending.mapper.TradeLendingBusinessDelegateMapper;
import com.temenos.infinity.tradelending.mapper.TradeLendingResourceMapper;
import com.temenos.infinity.tradelending.utils.TradeLendingProperties;


/**
 * @author mrunalini.adepu
 *
 */
@IntegrationCustomServlet(servletName = "TradeLendingCustomResourceServlet", urlPatterns = {"TradeLendingCustomResourceServlet"})
public class TradeLendingCustomResourceServlet extends HttpServlet {

	@Override
    public void init() {
        // Register Resource Delegates
        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(ResourceFactory.class)
                .registerResourceMappings(new TradeLendingResourceMapper(), APIImplementationTypes.BASE);

        // Register Business Delegates
        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
                .registerBusinessDelegateMappings(new TradeLendingBusinessDelegateMapper(), APIImplementationTypes.BASE);

        // Register Backend Delegates
        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BackendDelegateFactory.class)
                .registerBackendDelegateMappings(new TradeLendingBackendDelegateMapper(), APIImplementationTypes.BASE);

        new TradeLendingProperties().loadTypeAndSubType();
    }

    @Override
    public void destroy() {
    }
}
