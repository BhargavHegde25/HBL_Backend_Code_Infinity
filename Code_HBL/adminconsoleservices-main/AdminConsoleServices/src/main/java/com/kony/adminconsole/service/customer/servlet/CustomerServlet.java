package com.kony.adminconsole.service.customer.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.dbp.core.api.APIImplementationTypes;
import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.service.customer.mapper.CustomerBackendDelegateMapper;
import com.kony.adminconsole.service.customer.mapper.CustomerBusinessDelegateMapper;
import com.kony.adminconsole.service.customer.mapper.CustomerResourceMapper;
import com.konylabs.middleware.servlet.IntegrationCustomServlet;

/**
 * 
 * @author This is a custom servlet for customer Services
 * @version 1.0
 * @extends {HttpServlet}
 *
 */

@IntegrationCustomServlet(servletName = "CustomerServletCustomResourceServlet", urlPatterns = {
        "CustomerServletCustomResourceServlet" })
public class CustomerServlet extends HttpServlet {

    public static final long serialVersionUID = 1L;

    @Override
    public void init() throws ServletException {

        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(ResourceFactory.class)
                .registerResourceMappings(new CustomerResourceMapper(), APIImplementationTypes.BASE);

        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
                .registerBusinessDelegateMappings(new CustomerBusinessDelegateMapper(), APIImplementationTypes.BASE);

        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BackendDelegateFactory.class)
                .registerBackendDelegateMappings(new CustomerBackendDelegateMapper(), APIImplementationTypes.BASE);
    }

    @Override
    public void destroy() {

    }

}
