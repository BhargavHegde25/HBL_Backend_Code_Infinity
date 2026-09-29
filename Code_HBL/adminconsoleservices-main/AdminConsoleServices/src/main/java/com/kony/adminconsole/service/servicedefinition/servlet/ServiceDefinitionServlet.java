package com.kony.adminconsole.service.servicedefinition.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.dbp.core.api.APIImplementationTypes;
import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.service.servicedefinition.mapper.ServiceDefinitionBackendDelegateMapper;
import com.kony.adminconsole.service.servicedefinition.mapper.ServiceDefinitionBusinessDelegateMapper;
import com.kony.adminconsole.service.servicedefinition.mapper.ServiceDefinitionResourceMapper;
import com.konylabs.middleware.servlet.IntegrationCustomServlet;

/**
 * 
 * @author KH2660
 * This is a custom servlet for service definition Services
 * @version 1.0
 * @extends {HttpServlet}
 *
 */

@IntegrationCustomServlet(servletName = "ServiceDefinitionCustomResourceServlet", urlPatterns = {
"ServiceDefinitionCustomResourceServlet" })
public class ServiceDefinitionServlet  extends HttpServlet{
	
	public static final long serialVersionUID = 1L;

    @Override
    public void init() throws ServletException {

        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(ResourceFactory.class)
                .registerResourceMappings(new ServiceDefinitionResourceMapper(), APIImplementationTypes.BASE);

        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
                .registerBusinessDelegateMappings(new ServiceDefinitionBusinessDelegateMapper(), APIImplementationTypes.BASE);
        
        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BackendDelegateFactory.class)
        .registerBackendDelegateMappings(new ServiceDefinitionBackendDelegateMapper(), APIImplementationTypes.BASE);
    }

    @Override
    public void destroy() {

    }


}
