package com.temenos.dbx.usermanagement.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.dbp.core.api.APIImplementationTypes;
import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.servlet.IntegrationCustomServlet;
import com.temenos.dbx.product.integration.IntegrationMappings;
import com.temenos.dbx.product.utils.ThreadExecutor;
import com.temenos.dbx.usermanagement.mapper.PartyUserManagementBackendDelegateMapper;
import com.temenos.dbx.usermanagement.mapper.PartyUserManagementBusinessDelegateMapper;
import com.temenos.dbx.usermanagement.mapper.PartyUserManagementResourceMapper;

@IntegrationCustomServlet(servletName = "PartyUserManagementServlet", urlPatterns = {
"PartyUserManagementServlet" })
public class PartyUserManagementServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7996896027215639726L;

	@Override
	public void init() throws ServletException {
		 String integrationName = IntegrationMappings.getInstance().getIntegrationName();
		 if ("party".equalsIgnoreCase(integrationName)) {
 	      register();
 	    }
	}

	public static void register() {
		DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(ResourceFactory.class)
		.registerResourceMappings(new PartyUserManagementResourceMapper(), APIImplementationTypes.BASE);

		DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
		.registerBusinessDelegateMappings(new PartyUserManagementBusinessDelegateMapper(),
				APIImplementationTypes.BASE);

		DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BackendDelegateFactory.class)
		.registerBackendDelegateMappings(new PartyUserManagementBackendDelegateMapper(),
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
