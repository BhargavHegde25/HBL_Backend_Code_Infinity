package com.kony.dbp.cardservices.servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import com.kony.dbp.cardservices.mapper.CardServicesBusinessDelegateMapper;
import com.kony.dbp.cardservices.mapper.CardServicesResourceMapper;
import com.dbp.core.api.APIImplementationTypes;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.servlet.IntegrationCustomServlet;

/**
 * 
 * @author KH2394
 * @version 1.0
 * extends {@link HttpServlet}
 */

@IntegrationCustomServlet(servletName = "CardServicesServlet", urlPatterns = {
"CardServicesServlet" })

public class CardServicesServlet  extends HttpServlet {

	private static final long serialVersionUID = 5258692671194089192L;

	@Override
	public void init() throws ServletException {

		DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(ResourceFactory.class)
		.registerResourceMappings(new CardServicesResourceMapper(), APIImplementationTypes.BASE);

		DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
		.registerBusinessDelegateMappings(new CardServicesBusinessDelegateMapper(), APIImplementationTypes.BASE);
	}

	@Override
	public void destroy() {
	}
}


