package com.temenos.infinity.api.chequemanagement.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.dbp.core.api.APIImplementationTypes;
import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.servlet.IntegrationCustomServlet;
import com.temenos.infinity.api.chequemanagement.constants.GetCommandUrl;
import com.temenos.infinity.api.chequemanagement.mapper.ChequeManagementBackendDelegateMapper;
import com.temenos.infinity.api.chequemanagement.mapper.ChequeManagementBusinessDelegateMapper;
import com.temenos.infinity.api.chequemanagement.mapper.ChequeManagementResourceMapper;

/**
 * Custom servlet used to intialize/destroy resources required by this module
 * 
 * @author Aditya Mankal
 *
 */
@IntegrationCustomServlet(servletName = "ChequeManagementCustomResourcesServlet", urlPatterns = {
        "ChequeManagementCustomResourcesServlet" })
public class ChequeManagementCustomResourcesServlet extends HttpServlet {

    /**
     * 
     */
    private static final long serialVersionUID = -2023367655414960390L; 

    @Override
    public void init() throws ServletException {

        // Register Resource Delegates
        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(ResourceFactory.class)
                .registerResourceMappings(new ChequeManagementResourceMapper(), APIImplementationTypes.BASE);

        // Register Business Delegates
        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
                .registerBusinessDelegateMappings(new ChequeManagementBusinessDelegateMapper(), APIImplementationTypes.BASE);

        // Register Backend Delegates
        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BackendDelegateFactory.class)
                .registerBackendDelegateMappings(new ChequeManagementBackendDelegateMapper(), APIImplementationTypes.BASE);
     
        //Load the url properties
        GetCommandUrl backendHelper = new GetCommandUrl();
        backendHelper.loadURL();
    }

    @Override
    public void destroy() {
    }

}