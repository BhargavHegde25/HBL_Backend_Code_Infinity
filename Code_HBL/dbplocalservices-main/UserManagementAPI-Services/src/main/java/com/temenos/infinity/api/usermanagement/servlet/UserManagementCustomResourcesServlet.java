package com.temenos.infinity.api.usermanagement.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.dbp.core.api.APIImplementationTypes;
import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.servlet.IntegrationCustomServlet;
import com.temenos.infinity.api.usermanagement.mapper.UserManagementBackendDelegateMapper;
import com.temenos.infinity.api.usermanagement.mapper.UserManagementBusinessDelegateMapper;
import com.temenos.infinity.api.usermanagement.mapper.UserManagementResourceMapper;

/**
 * Custom servlet used to intialize/destroy resources required by this module
 * 
 * @author Aditya Mankal
 *
 */
@IntegrationCustomServlet(servletName = "UserManagementCustomResourcesServlet", urlPatterns = {
        "UserManagementCustomResourcesServlet" })
public class UserManagementCustomResourcesServlet extends HttpServlet {

    /**
     * 
     */
    private static final long serialVersionUID = -2023367655414960390L;

    @Override
    public void init() throws ServletException {

        // Register Resource Delegates
        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(ResourceFactory.class)
                .registerResourceMappings(new UserManagementResourceMapper(), APIImplementationTypes.BASE);

        // Register Business Delegates
        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
                .registerBusinessDelegateMappings(new UserManagementBusinessDelegateMapper(), APIImplementationTypes.BASE);

        // Register Backend Delegates
        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BackendDelegateFactory.class)
                .registerBackendDelegateMappings(new UserManagementBackendDelegateMapper(), APIImplementationTypes.BASE);
    }

    @Override
    public void destroy() {
    }

}