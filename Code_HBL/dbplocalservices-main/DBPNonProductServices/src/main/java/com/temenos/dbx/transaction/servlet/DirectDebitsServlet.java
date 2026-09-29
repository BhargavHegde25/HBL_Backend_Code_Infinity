package com.temenos.dbx.transaction.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.dbp.core.api.APIImplementationTypes;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.servlet.IntegrationCustomServlet;
import com.temenos.dbx.transaction.mapper.DirectDebitsBusinessDelegateMapper;
import com.temenos.dbx.transaction.mapper.DirectDebitsResourceMapper;

/**
 * @author sribarani.vasthan
 */

@IntegrationCustomServlet(servletName = "DirectDebitsServlet", urlPatterns = { "DirectDebitsServlet" })
public class DirectDebitsServlet extends HttpServlet {

	private static final long serialVersionUID = 5258692671184055192L;

	@Override
	public void init() throws ServletException {

		DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(ResourceFactory.class)
				.registerResourceMappings(new DirectDebitsResourceMapper(), APIImplementationTypes.BASE);

		DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
				.registerBusinessDelegateMappings(new DirectDebitsBusinessDelegateMapper(),
						APIImplementationTypes.BASE);
	}

	@Override
	public void destroy() {
	}

}
