package com.infinity.dbx.temenos.accounts.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.dbp.core.api.APIImplementationTypes;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.infinity.dbx.temenos.accounts.mapper.InfinityAcccountsResourceMapper;
import com.konylabs.middleware.servlet.IntegrationCustomServlet;

@IntegrationCustomServlet(servletName = "AccountsServlet", urlPatterns = {
		"AccountsServlet" })
public class AccountsServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6842795337180705529L;

	@Override
	public void init() throws ServletException {
		DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(ResourceFactory.class)
		.registerResourceMappings(new  InfinityAcccountsResourceMapper(), APIImplementationTypes.BASE);
	}

	@Override
	public void destroy() {

	}

}
