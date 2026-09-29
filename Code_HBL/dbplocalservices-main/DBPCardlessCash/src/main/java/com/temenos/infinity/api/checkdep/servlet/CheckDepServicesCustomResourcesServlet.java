package com.temenos.infinity.api.checkdep.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.dbp.core.api.APIImplementationTypes;
//import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.servlet.IntegrationCustomServlet;
//import com.temenos.infinity.constants.CheckDepBackendURL;
import com.temenos.infinity.api.checkdep.mapper.CheckDepServicesBusinessDelegateMapper;
import com.temenos.infinity.api.checkdep.mapper.CheckDepServicesResourceMapper;



/**
 * 
 * @author KH1755
 * @version 1.0
 * extends {@link HttpServlet}
 */
@IntegrationCustomServlet(servletName = "CheckDepServicesCustomResourcesServlet", urlPatterns = {
"CheckDepServicesCustomResourcesServlet" })
public class CheckDepServicesCustomResourcesServlet extends HttpServlet{
	
	private static final long serialVersionUID = 5258692671194055192L;

    @Override
    public void init() throws ServletException {
    	
        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(ResourceFactory.class)
                .registerResourceMappings(new CheckDepServicesResourceMapper(), APIImplementationTypes.BASE);

        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
                .registerBusinessDelegateMappings(new CheckDepServicesBusinessDelegateMapper(), APIImplementationTypes.BASE);
        
        
        //Load the backend properties
//        CheckDepBackendURL backendHelper = new CheckDepBackendURL();
//        backendHelper.loadTransactionBackendURLs();

    }

    @Override
    public void destroy() {
    }
}
