package com.infinity.dbx.temenos.forexservices.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import com.dbp.core.api.APIImplementationTypes;
import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.servlet.IntegrationCustomServlet;
import com.infinity.dbx.temenos.forexservices.mapper.ForexServicesBackendDelegateMapperExtn;

@IntegrationCustomServlet(servletName = "QfxServicesCustomResourcesServlet", urlPatterns = {
		"QfxServicesCustomResourcesServlet" })
public class ForexServicesCustomResourcesExtnServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6842795337180705529L;

	@Override
	public void init() throws ServletException {

		DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BackendDelegateFactory.class)
				.registerBackendDelegateMappings(new ForexServicesBackendDelegateMapperExtn(), APIImplementationTypes.EXTENSION);
	}

	@Override
	public void destroy() {

	}

}
