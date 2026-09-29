package com.bct.custom.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.bct.custom.mapper.HBLCustomBackendDelegateMapper;
import com.bct.custom.mapper.HBLCustomBusinessDelegateMapper;
import com.bct.custom.mapper.HBLResourceMapper;
import com.dbp.core.api.APIImplementationTypes;
import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.servlet.IntegrationCustomServlet;

@IntegrationCustomServlet(servletName = "HBLCustomServicesServlet", urlPatterns = { "HBLCustomServicesServlet" })
public class HBLCustomServicesServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public void init() throws ServletException {

		DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(ResourceFactory.class)
				.registerResourceMappings(new HBLResourceMapper(), APIImplementationTypes.BASE);
		DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
		.registerBusinessDelegateMappings(new HBLCustomBusinessDelegateMapper(), APIImplementationTypes.BASE);

		DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BackendDelegateFactory.class)
		.registerBackendDelegateMappings(new HBLCustomBackendDelegateMapper(), APIImplementationTypes.BASE);
		

}
	@Override
	public void destroy() {
		
	}
}
