package com.temenos.infinity.api.savingspot.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.dbp.core.api.APIImplementationTypes;
import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.servlet.IntegrationCustomServlet;
import com.temenos.infinity.api.savingspot.mapper.SavingsPotBackendDelegateMapper;
import com.temenos.infinity.api.savingspot.mapper.SavingsPotBusinessDelegateMapper;
import com.temenos.infinity.api.savingspot.mapper.SavingsPotResourceMapper;

/**
 * Custom servlet used to intialize/destroy resources required by this module
 * 
 * @author muthukumarv
 *
 */
@IntegrationCustomServlet(servletName = "SavingsPotCustomResourcesServlet", urlPatterns = {
        "SavingsPotCustomResourcesServlet" })
public class SavingsPotCustomResourcesServlet extends HttpServlet {

    private static final long serialVersionUID = 7467307922351824473L;

    @Override
    public void init() throws ServletException {
        // Register Resource Delegates
        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(ResourceFactory.class)
                .registerResourceMappings(new SavingsPotResourceMapper(), APIImplementationTypes.BASE);

        // Register Business Delegates
        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
                .registerBusinessDelegateMappings(new SavingsPotBusinessDelegateMapper(),
                        APIImplementationTypes.BASE);

        // Register Backend Delegates
        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BackendDelegateFactory.class)
                .registerBackendDelegateMappings(new SavingsPotBackendDelegateMapper(),
                        APIImplementationTypes.BASE);
    }

    @Override
    public void destroy() {
    }

}
