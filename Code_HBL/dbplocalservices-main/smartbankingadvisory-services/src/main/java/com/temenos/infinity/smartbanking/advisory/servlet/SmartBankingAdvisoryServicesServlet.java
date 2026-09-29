package com.temenos.infinity.smartbanking.advisory.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.dbp.core.api.APIImplementationTypes;
import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.servlet.IntegrationCustomServlet;
import com.temenos.infinity.smartbanking.advisory.mapper.SmartBankingAdvisoryBackendDelegateMapper;
import com.temenos.infinity.smartbanking.advisory.mapper.SmartBankingAdvisoryBusinessDelegateMapper;
import com.temenos.infinity.smartbanking.advisory.mapper.SmartBankingAdvisoryResourceMapper;

/**
 * @author shubham.ahuja
 *
 */
@IntegrationCustomServlet(servletName = "SmartBankingAdvisoryServicesServlet", urlPatterns = {
		"SmartBankingAdvisoryServicesServlet" })
public class SmartBankingAdvisoryServicesServlet extends HttpServlet {

	private static final long serialVersionUID = 5258692671194089192L;

	@Override
	public void init() throws ServletException {

		DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(ResourceFactory.class)
				.registerResourceMappings(new SmartBankingAdvisoryResourceMapper(), APIImplementationTypes.BASE);

		DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
				.registerBusinessDelegateMappings(new SmartBankingAdvisoryBusinessDelegateMapper(),
						APIImplementationTypes.BASE);

		DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BackendDelegateFactory.class)
				.registerBackendDelegateMappings(new SmartBankingAdvisoryBackendDelegateMapper(),
						APIImplementationTypes.BASE);

	}

	@Override
	public void destroy() {
	}

}
