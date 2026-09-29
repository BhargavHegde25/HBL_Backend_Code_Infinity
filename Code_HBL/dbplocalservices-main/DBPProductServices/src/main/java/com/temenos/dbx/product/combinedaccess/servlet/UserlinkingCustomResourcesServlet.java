package com.temenos.dbx.product.combinedaccess.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.dbp.core.api.APIImplementationTypes;
import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.servlet.IntegrationCustomServlet;
import com.temenos.dbx.product.combinedaccess.mapper.UserlinkingBackendDelegateMapper;
import com.temenos.dbx.product.combinedaccess.mapper.UserlinkingBusinessDelegateMapper;
import com.temenos.dbx.product.combinedaccess.mapper.UserlinkingResourceMapper;

/**
 * Custom servlet used to intialize/destroy resources required by this module
 * 
 * @author muthukumarv
 * @version 1.0
 *
 */
@IntegrationCustomServlet(servletName = "UserlinkingCustomResourcesServlet", urlPatterns = {
		"UserlinkingCustomResourcesServlet" })
public class UserlinkingCustomResourcesServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8525716451974894311L;

	@Override
	public void init() throws ServletException {
		// Register Resource Delegates
		DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(ResourceFactory.class)
				.registerResourceMappings(new UserlinkingResourceMapper(), APIImplementationTypes.BASE);

		// Register Business Delegates
		DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
				.registerBusinessDelegateMappings(new UserlinkingBusinessDelegateMapper(), APIImplementationTypes.BASE);

		// Register Backend Delegates
		DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BackendDelegateFactory.class)
				.registerBackendDelegateMappings(new UserlinkingBackendDelegateMapper(), APIImplementationTypes.BASE);
	}

	@Override
	public void destroy() {
	}

}
