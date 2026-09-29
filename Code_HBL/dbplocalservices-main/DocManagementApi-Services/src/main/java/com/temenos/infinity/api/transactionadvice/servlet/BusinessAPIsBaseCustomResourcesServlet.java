package com.temenos.infinity.api.transactionadvice.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.dbp.core.api.APIImplementationTypes;
import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.servlet.IntegrationCustomServlet;
import com.temenos.infinity.api.transactionadvice.backenddelegate.mapper.TransactionAdviceBackendDelegateMapper;

@IntegrationCustomServlet(servletName = "BusinessAPIsBaseCustomResourcesServlet", urlPatterns = {
		"BusinessAPIsBaseCustomResourcesServlet" })
public class BusinessAPIsBaseCustomResourcesServlet extends HttpServlet {

	private static final long serialVersionUID = 5258692681194155142L;

	@Override
	public void init() throws ServletException {
		// registering base resources and business delegates

		// Register Backend Delegates
		DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BackendDelegateFactory.class)
				.registerBackendDelegateMappings(new TransactionAdviceBackendDelegateMapper(), APIImplementationTypes.BASE);
	}

	@Override
	public void destroy() {
	}

}
