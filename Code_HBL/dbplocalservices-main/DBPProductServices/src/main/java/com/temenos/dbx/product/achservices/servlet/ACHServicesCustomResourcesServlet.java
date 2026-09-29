package com.temenos.dbx.product.achservices.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.dbp.core.api.APIImplementationTypes;
import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.temenos.dbx.product.achservices.mapper.ACHServicesBackendDelegateMapper;
import com.temenos.dbx.product.achservices.mapper.ACHServicesBusinessDelegateMapper;
import com.temenos.dbx.product.achservices.mapper.ACHServicesResourceMapper;
import com.konylabs.middleware.servlet.IntegrationCustomServlet;

/**
 * @author KH2626
 * @version 1.0
 * @extends {HttpServlet}
 * **/
@IntegrationCustomServlet(servletName = "ACHServicesCustomResourcesServlet", urlPatterns = {
"ACHServicesCustomResourcesServlet" })
public class ACHServicesCustomResourcesServlet extends HttpServlet{

	private static final long serialVersionUID = -686776799320776268L;

	@Override
    public void init() throws ServletException {
    	
        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(ResourceFactory.class)
                .registerResourceMappings(new ACHServicesResourceMapper(), APIImplementationTypes.BASE);

        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
                .registerBusinessDelegateMappings(new ACHServicesBusinessDelegateMapper(), APIImplementationTypes.BASE);
        
        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BackendDelegateFactory.class)
      		.registerBackendDelegateMappings(new ACHServicesBackendDelegateMapper(),
      				APIImplementationTypes.BASE);
    }

    @Override
    public void destroy() {
    	
    }
}
