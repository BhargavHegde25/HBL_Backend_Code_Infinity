package com.temenos.dbx.mfa.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.dbp.core.api.APIImplementationTypes;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.servlet.IntegrationCustomServlet;
import com.temenos.dbx.mfa.mapper.MFABusinessDelegateMapper;
import com.temenos.dbx.mfa.mapper.MFAResourceMapper;
import com.temenos.dbx.product.utils.ThreadExecutor;

@IntegrationCustomServlet(servletName = "MFACustomerResourcesServlet", urlPatterns = { "MFACustomerResourcesServlet" })
public class MFACustomerResourcesServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7996896027215639726L;

	@Override
	public void init() throws ServletException {

		DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(ResourceFactory.class)
				.registerResourceMappings(new MFAResourceMapper(), APIImplementationTypes.BASE);

		DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
				.registerBusinessDelegateMappings(new MFABusinessDelegateMapper(), APIImplementationTypes.BASE);
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
