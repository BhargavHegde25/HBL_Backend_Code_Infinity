package com.temenos.dbx.product.forexservices.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import com.dbp.core.api.APIImplementationTypes;
import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.servlet.IntegrationCustomServlet;
import com.temenos.dbx.product.forexservices.mapper.ForexServicesBackendDelegateMapper;
import com.temenos.dbx.product.forexservices.mapper.ForexServicesBusinessDelegateMapper;
import com.temenos.dbx.product.forexservices.mapper.ForexServicesResourceMapper;

@IntegrationCustomServlet(servletName = "QfxServicesCustomResourcesServlet", urlPatterns = {
		"QfxServicesCustomResourcesServlet" })
public class ForexServicesCustomResourcesServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6842795337180705526L;

	@Override
	public void init() throws ServletException {

		DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(ResourceFactory.class)
				.registerResourceMappings(new ForexServicesResourceMapper(), APIImplementationTypes.BASE);

		DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
				.registerBusinessDelegateMappings(new ForexServicesBusinessDelegateMapper(),
						APIImplementationTypes.BASE);

		DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BackendDelegateFactory.class)
				.registerBackendDelegateMappings(new ForexServicesBackendDelegateMapper(), APIImplementationTypes.BASE);
	}

	@Override
	public void destroy() {

	}

}
