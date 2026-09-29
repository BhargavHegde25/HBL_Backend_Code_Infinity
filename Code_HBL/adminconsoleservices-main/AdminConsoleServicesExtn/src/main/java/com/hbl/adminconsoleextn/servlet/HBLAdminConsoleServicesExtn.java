package com.hbl.adminconsoleextn.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.dbp.core.api.APIImplementationTypes;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.hbl.adminconsoleextn.mapper.HBLBusinessdeligateMapper;
import com.hbl.adminconsoleextn.mapper.HBLResourceMapper;
import com.konylabs.middleware.servlet.IntegrationCustomServlet;


@IntegrationCustomServlet(servletName = "FeaturesAndActionsResourceImplExtn", urlPatterns = { "FeaturesAndActionsResourceImplExtn" })
public class HBLAdminConsoleServicesExtn  extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4702021150490173676L;
	public void init() throws ServletException {

		DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(ResourceFactory.class)
				.registerResourceMappings(new HBLResourceMapper(), APIImplementationTypes.EXTENSION);
		DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
		.registerBusinessDelegateMappings(new HBLBusinessdeligateMapper(), APIImplementationTypes.EXTENSION);

		/*DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BackendDelegateFactory.class)
		.registerBackendDelegateMappings(new HBLBackendDelegateMapper(), APIImplementationTypes.EXTENSION);
		*/

}
	@Override
	public void destroy() {
		
	}
}

