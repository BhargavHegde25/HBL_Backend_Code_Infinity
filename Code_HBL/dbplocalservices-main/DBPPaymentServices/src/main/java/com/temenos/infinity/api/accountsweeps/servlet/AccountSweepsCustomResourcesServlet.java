package com.temenos.infinity.api.accountsweeps.servlet;

import com.dbp.core.api.APIImplementationTypes;
import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.servlet.IntegrationCustomServlet;
import com.temenos.infinity.api.accountsweeps.mapper.AccountSweepsBackendDelegateMapper;
import com.temenos.infinity.api.accountsweeps.mapper.AccountSweepsBusinessDelegateMapper;
import com.temenos.infinity.api.accountsweeps.mapper.AccountSweepsResourceMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * @author naveen.yerra
 */
@IntegrationCustomServlet(servletName = "AccountSweepsCustomResourcesServlet", urlPatterns = {"AccountSweepsCustomResourcesServlet" })
public class AccountSweepsCustomResourcesServlet extends HttpServlet {

    public void init() throws ServletException {
        // Register Resource Delegates
        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(ResourceFactory.class)
                .registerResourceMappings(new AccountSweepsResourceMapper(), APIImplementationTypes.BASE);

        // Register Business Delegates
        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
                .registerBusinessDelegateMappings(new AccountSweepsBusinessDelegateMapper(), APIImplementationTypes.BASE);

        // Register Backend Delegates
        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BackendDelegateFactory.class)
                .registerBackendDelegateMappings(new AccountSweepsBackendDelegateMapper(), APIImplementationTypes.BASE);
    }

    @Override
    public void destroy() {
    }
}
