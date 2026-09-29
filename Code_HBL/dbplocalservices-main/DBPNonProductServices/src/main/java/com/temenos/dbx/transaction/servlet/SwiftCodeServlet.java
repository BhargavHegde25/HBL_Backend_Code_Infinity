package com.temenos.dbx.transaction.servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;


import com.dbp.core.api.APIImplementationTypes;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.servlet.IntegrationCustomServlet;
import com.temenos.dbx.transaction.mapper.SwiftCodeBusinessDelegateMapper;
import com.temenos.dbx.transaction.mapper.SwiftCodeResourceMapper;


@IntegrationCustomServlet(servletName = "SwiftCodeServlet", urlPatterns = {
"SwiftCodeServlet" })

public class SwiftCodeServlet  extends HttpServlet {

	private static final long serialVersionUID = 5258692671194055192L;

	@Override
	public void init() throws ServletException {

		DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(ResourceFactory.class)
		.registerResourceMappings(new SwiftCodeResourceMapper(), APIImplementationTypes.BASE);

		DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
		.registerBusinessDelegateMappings(new SwiftCodeBusinessDelegateMapper(), APIImplementationTypes.BASE);
	}

	@Override
	public void destroy() {
	}
}
