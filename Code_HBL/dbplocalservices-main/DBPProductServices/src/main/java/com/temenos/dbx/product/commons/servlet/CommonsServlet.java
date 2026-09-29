package com.temenos.dbx.product.commons.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.dbp.core.api.APIImplementationTypes;
import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.temenos.dbx.product.commons.mapper.CommonsBackendDelegateMapper;
import com.temenos.dbx.product.commons.mapper.CommonsBusinessDelegateMapper;
import com.temenos.dbx.product.commons.mapper.CommonsResourceMapper;
import com.konylabs.middleware.servlet.IntegrationCustomServlet;


@IntegrationCustomServlet(servletName = "CancellationReasonsServlet", urlPatterns = {
"CancellationReasonsServlet" })


public class CommonsServlet extends HttpServlet {
	private static final long serialVersionUID = 5258692671194089192L;

	@Override
	public void init() throws ServletException {

		DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(ResourceFactory.class)
		.registerResourceMappings(new CommonsResourceMapper(), APIImplementationTypes.BASE);
		
		DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
		.registerBusinessDelegateMappings(new CommonsBusinessDelegateMapper(), APIImplementationTypes.BASE);

		DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BackendDelegateFactory.class)
		.registerBackendDelegateMappings(new CommonsBackendDelegateMapper(), APIImplementationTypes.BASE);
	}

	@Override
	public void destroy() {
	}
}
