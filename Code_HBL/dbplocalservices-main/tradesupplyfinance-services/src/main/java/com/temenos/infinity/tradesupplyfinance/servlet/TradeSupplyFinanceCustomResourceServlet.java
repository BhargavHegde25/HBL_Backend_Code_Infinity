/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2023. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradesupplyfinance.servlet;

import com.dbp.core.api.APIImplementationTypes;
import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.servlet.IntegrationCustomServlet;
import com.temenos.infinity.tradesupplyfinance.mapper.TradeSupplyFinanceBackendDelegateMapper;
import com.temenos.infinity.tradesupplyfinance.mapper.TradeSupplyFinanceBusinessDelegateMapper;
import com.temenos.infinity.tradesupplyfinance.mapper.TradeSupplyFinanceResourceMapper;
import com.temenos.infinity.tradesupplyfinance.utils.TradeSupplyFinanceProperties;

import javax.servlet.http.HttpServlet;

/**
 * @author k.meiyazhagan
 */
@IntegrationCustomServlet(servletName = "TradeSupplyFinanceCustomResourceServlet", urlPatterns = {"TradeSupplyFinanceCustomResourceServlet"})
public class TradeSupplyFinanceCustomResourceServlet extends HttpServlet {

    @Override
    public void init() {
        // Register Resource Delegates
        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(ResourceFactory.class)
                .registerResourceMappings(new TradeSupplyFinanceResourceMapper(), APIImplementationTypes.BASE);

        // Register Business Delegates
        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
                .registerBusinessDelegateMappings(new TradeSupplyFinanceBusinessDelegateMapper(), APIImplementationTypes.BASE);

        // Register Backend Delegates
        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BackendDelegateFactory.class)
                .registerBackendDelegateMappings(new TradeSupplyFinanceBackendDelegateMapper(), APIImplementationTypes.BASE);

        new TradeSupplyFinanceProperties().loadTypeAndSubType();
    }

    @Override
    public void destroy() {
    }
}
