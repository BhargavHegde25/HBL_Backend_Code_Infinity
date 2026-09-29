package com.temenos.auth.security.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.dbp.core.api.APIImplementationTypes;
import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.servlet.IntegrationCustomServlet;
import com.temenos.auth.security.mapper.SecurityBackendDelegateMapper;
import com.temenos.auth.security.mapper.SecurityBusinessDelegateMapper;
import com.temenos.auth.security.mapper.SecurityResourceMapper;
import com.temenos.auth.usermanagement.mapper.UsermanagementBackendDelegateMapper;
import com.temenos.auth.usermanagement.mapper.UsermanagementBusinessDelegateMapper;
import com.temenos.auth.usermanagement.mapper.UsermanagementResourceMapper;
import com.temenos.dbx.product.utils.ThreadExecutor;

@IntegrationCustomServlet(servletName = "AuthSecurityResourcesServlet", urlPatterns = {
        "AuthSecurityResourcesServlet" })
public class SecurityServlet extends HttpServlet {

    public void init() throws ServletException {

        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(ResourceFactory.class)
                .registerResourceMappings(new SecurityResourceMapper(), APIImplementationTypes.BASE);

        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
                .registerBusinessDelegateMappings(new SecurityBusinessDelegateMapper(),
                        APIImplementationTypes.BASE);

        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BackendDelegateFactory.class)
                .registerBackendDelegateMappings(new SecurityBackendDelegateMapper(),
                        APIImplementationTypes.BASE);
        
        
        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(ResourceFactory.class)
        	.registerResourceMappings(new UsermanagementResourceMapper(), APIImplementationTypes.BASE);

        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
        	.registerBusinessDelegateMappings(new UsermanagementBusinessDelegateMapper(),
                APIImplementationTypes.BASE);

        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BackendDelegateFactory.class)
        	.registerBackendDelegateMappings(new UsermanagementBackendDelegateMapper(),
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
