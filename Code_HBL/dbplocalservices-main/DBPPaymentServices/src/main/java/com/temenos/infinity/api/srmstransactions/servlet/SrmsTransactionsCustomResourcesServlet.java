package com.temenos.infinity.api.srmstransactions.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;


import com.dbp.core.api.APIImplementationTypes;
import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.servlet.IntegrationCustomServlet;
import com.temenos.infinity.api.srmstransactions.mapper.SrmsTransactionsBackendDelegateMapper;
import com.temenos.infinity.api.srmstransactions.mapper.SrmsTransactionsBusinessDelegateMapper;
import com.temenos.infinity.api.srmstransactions.mapper.SrmsTransactionsResourceMapper;

/**
 * Custom servlet used to intialize/destroy resources required by this module
 * 
 * @author Aditya Mankal
 *
 */
@IntegrationCustomServlet(servletName = "SrmsTransactionsCustomResourcesServlet", urlPatterns = {
        "SrmsTransactionsCustomResourcesServlet" })
public class SrmsTransactionsCustomResourcesServlet extends HttpServlet {
    

    /**
     * 
     */
    private static final long serialVersionUID = -2023367655414960364L;

    @Override
    public void init() throws ServletException {

        // Register Resource Delegates
        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(ResourceFactory.class)
                .registerResourceMappings(new SrmsTransactionsResourceMapper(), APIImplementationTypes.BASE);

        // Register Business Delegates
        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
                .registerBusinessDelegateMappings(new SrmsTransactionsBusinessDelegateMapper(),
                        APIImplementationTypes.BASE);

        // Register Backend Delegates
        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BackendDelegateFactory.class)
                .registerBackendDelegateMappings(new SrmsTransactionsBackendDelegateMapper(),
                        APIImplementationTypes.BASE);
    }

    @Override
    public void destroy() {
    }

}