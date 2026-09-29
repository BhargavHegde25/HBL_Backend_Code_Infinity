package com.temenos.infinity.api.arrangements.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.dbp.core.api.APIImplementationTypes;
import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.servlet.IntegrationCustomServlet;

@IntegrationCustomServlet(servletName = "DocumentStorageServices", urlPatterns = {"DocumentStorageServices" })
public class DocumentStorageServices extends HttpServlet {

	private static final long serialVersionUID = -6806976126964257659L;

	@Override
	public void init() throws ServletException {
		DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(ResourceFactory.class)
        .registerResourceMappings(new com.temenos.infinity.api.arrangements.mapper.BaseResourcesMapper(), APIImplementationTypes.BASE);

		DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
        .registerBusinessDelegateMappings(new com.temenos.infinity.api.arrangements.mapper.BaseBusinessDelegatesMapper(), APIImplementationTypes.BASE);
		
		DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BackendDelegateFactory.class)
        .registerBackendDelegateMappings(new com.temenos.infinity.api.arrangements.mapper.BaseBackendDelegatesMapper(), APIImplementationTypes.BASE);
	}

}
