package com.temenos.dbx.product.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.dbp.core.api.APIImplementationTypes;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.servlet.IntegrationCustomServlet;
import com.temenos.dbx.product.mapper.ProductBusinessDelegateMapper;
import com.temenos.dbx.product.mapper.ProductResourceMapper;
import com.temenos.dbx.product.utils.ThreadExecutor;

@IntegrationCustomServlet(servletName = "DBXUserCustomerResourcesServlet", urlPatterns = {
        "DBXUserCustomerResourcesServlet" })
public class ProductResourcesServlet extends HttpServlet {

    /**
     * 
     */
    private static final long serialVersionUID = -7996896027215639726L;

    @Override
    public void init() throws ServletException {

        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(ResourceFactory.class)
                .registerResourceMappings(new ProductResourceMapper(), APIImplementationTypes.BASE);

        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
                .registerBusinessDelegateMappings(new ProductBusinessDelegateMapper(),
                        APIImplementationTypes.BASE);
    }

    @Override
    public void destroy() {
    	try {
            // Shutting down the customer 360 thread pool
            ThreadExecutor.getExecutor().shutdownExecutor();
        } catch (Exception e) {
            
        }
    }
}
