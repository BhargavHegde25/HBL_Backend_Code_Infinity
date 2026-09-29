package com.hbl.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.dbp.core.api.APIImplementationTypes;
import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.hbl.resource.mapper.HBLBackendDelegateMapper;
import com.hbl.resource.mapper.HBLBusinessDelegateMapper;
import com.hbl.resource.mapper.HBLResourceMapper;
import com.konylabs.middleware.servlet.IntegrationCustomServlet;

@IntegrationCustomServlet(servletName = "ProductExternalUserManagementServletExtn", urlPatterns = { "ProductExternalUserManagementServletExtn" })
public class HBLeumProductServicesServlet extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void init() throws ServletException {

		DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(ResourceFactory.class)
				.registerResourceMappings(new HBLResourceMapper(), APIImplementationTypes.EXTENSION);
		DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
		.registerBusinessDelegateMappings(new HBLBusinessDelegateMapper(), APIImplementationTypes.EXTENSION);

		DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BackendDelegateFactory.class)
		.registerBackendDelegateMappings(new HBLBackendDelegateMapper(), APIImplementationTypes.EXTENSION);
		

}
	@Override
	public void destroy() {
		
	}
}
