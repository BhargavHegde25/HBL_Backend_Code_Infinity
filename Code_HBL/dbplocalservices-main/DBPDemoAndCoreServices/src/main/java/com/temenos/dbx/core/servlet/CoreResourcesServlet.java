package com.temenos.dbx.core.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.dbp.core.api.APIImplementationTypes;
import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.servlet.IntegrationCustomServlet;
import com.temenos.dbx.core.mapper.CoreBackendDelegateMapper;
import com.temenos.dbx.core.mapper.CoreBusinessDelegateMapper;
import com.temenos.dbx.core.mapper.CoreResourceMapper;

@IntegrationCustomServlet(servletName = "CoreCustomerResourcesServlet", urlPatterns = {
        "CoreCustomerResourcesServlet" })
public class CoreResourcesServlet extends HttpServlet {

    /**
     * 
     */
    private static final long serialVersionUID = 8350373557619582192L;

    @Override
    public void init() throws ServletException {

        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(ResourceFactory.class)
                .registerResourceMappings(new CoreResourceMapper(), 
                        APIImplementationTypes.BASE);

        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
                .registerBusinessDelegateMappings(new CoreBusinessDelegateMapper(),
                        APIImplementationTypes.BASE);
        
        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BackendDelegateFactory.class)
                .registerBackendDelegateMappings(new CoreBackendDelegateMapper(), 
                        APIImplementationTypes.BASE);
    }

    @Override
    public void destroy() {
    }
}
