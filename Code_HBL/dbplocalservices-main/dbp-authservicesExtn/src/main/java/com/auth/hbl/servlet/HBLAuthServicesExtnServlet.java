package com.auth.hbl.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.auth.hbl.resource.mapper.HBLAuthServicesBackendDelegateMapper;
import com.auth.hbl.resource.mapper.HBLAuthServicesBusinessDelegateMapper;
import com.auth.hbl.resource.mapper.HBLAuthServicesResourceMapper;
import com.dbp.core.api.APIImplementationTypes;
import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.servlet.IntegrationCustomServlet;

@IntegrationCustomServlet(servletName = "DBPAuthServicesExtn", urlPatterns = { "DBPAuthServicesExtn" })
public class HBLAuthServicesExtnServlet extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void init() throws ServletException {

		DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(ResourceFactory.class)
				.registerResourceMappings(new HBLAuthServicesResourceMapper(), APIImplementationTypes.EXTENSION);
		DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
		.registerBusinessDelegateMappings(new HBLAuthServicesBusinessDelegateMapper(), APIImplementationTypes.EXTENSION);

		DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BackendDelegateFactory.class)
		.registerBackendDelegateMappings(new HBLAuthServicesBackendDelegateMapper(), APIImplementationTypes.EXTENSION);
		

}
	@Override
	public void destroy() {
		
	}
}
