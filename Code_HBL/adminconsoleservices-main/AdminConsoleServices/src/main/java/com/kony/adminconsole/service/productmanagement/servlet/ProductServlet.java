package com.kony.adminconsole.service.productmanagement.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.dbp.core.api.APIImplementationTypes;
import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.service.productmanagement.mapper.ProductBackendDelegateMapper;
import com.kony.adminconsole.service.productmanagement.mapper.ProductBusinessDelegateMapper;
import com.kony.adminconsole.service.productmanagement.mapper.ProductResourceMapper;
import com.konylabs.middleware.servlet.IntegrationCustomServlet;

@IntegrationCustomServlet(servletName = "ProductServletCustomResourceServlet", urlPatterns = {
							"ProductServletCustomResourceServlet" })
public class ProductServlet extends HttpServlet {

    public static final long serialVersionUID = 46L;

    @Override
    public void init() throws ServletException {

        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(ResourceFactory.class)
                .registerResourceMappings(new ProductResourceMapper(), APIImplementationTypes.BASE);

        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
                .registerBusinessDelegateMappings(new ProductBusinessDelegateMapper(), APIImplementationTypes.BASE);
        
        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BackendDelegateFactory.class)
        .registerBackendDelegateMappings(new ProductBackendDelegateMapper(), APIImplementationTypes.BASE);
     }

    @Override
    public void destroy() {

    }
}