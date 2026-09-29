package com.temenos.dbx.product.accounts.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.dbp.core.api.APIImplementationTypes;
import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.servlet.IntegrationCustomServlet;
import com.temenos.dbx.product.accounts.mapper.AccountsBackendDelegateMapper;
import com.temenos.dbx.product.accounts.mapper.AccountsBusinessDelegateMapper;
import com.temenos.dbx.product.accounts.mapper.AccountsResourceMapper;

/**
 * Custom servlet used to intialize/destroy resources required by this module
 * 
 * @author sowmya.vandanapu
 * @version 1.0
 *
 */
@IntegrationCustomServlet(servletName = "AccountsCustomResourcesServlet", urlPatterns = {
		"AccountsCustomResourcesServlet" })
public class AccountsCustomResourcesServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8525716451974894311L;

	@Override
	public void init() throws ServletException {
		// Register Resource Delegates
		DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(ResourceFactory.class)
				.registerResourceMappings(new AccountsResourceMapper(), APIImplementationTypes.BASE);

		// Register Business Delegates
		DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
				.registerBusinessDelegateMappings(new AccountsBusinessDelegateMapper(), APIImplementationTypes.BASE);

		// Register Backend Delegates
		DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BackendDelegateFactory.class)
				.registerBackendDelegateMappings(new AccountsBackendDelegateMapper(), APIImplementationTypes.BASE);
	}

	@Override
	public void destroy() {
	}

}
