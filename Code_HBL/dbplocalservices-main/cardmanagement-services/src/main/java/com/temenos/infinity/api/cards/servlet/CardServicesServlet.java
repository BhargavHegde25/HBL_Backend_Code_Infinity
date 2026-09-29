package com.temenos.infinity.api.cards.servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.temenos.dbx.product.integration.IntegrationMappings;
import com.temenos.infinity.api.cards.mapper.CardServicesBusinessDelegateMapper;
import com.temenos.infinity.api.cards.mapper.CardServicesDBXBusinessDelegateMapper;
import com.temenos.infinity.api.cards.mapper.CardServicesDBXResourceMapper;
import com.temenos.infinity.api.cards.mapper.CardServicesResourceMapper;
import com.dbp.core.api.APIImplementationTypes;
import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
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
		
		String CARDMANAGEMENT_BACKEND = EnvironmentConfigurationsHandler.getValue("CARDS_BACKEND");
		 if ("DBXDB".equalsIgnoreCase(CARDMANAGEMENT_BACKEND)) {
			 registerDBXImpl();
	    }else {
	    	registerSRMSImpl();
	    }
	}
	
	public static void registerSRMSImpl() {
		DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(ResourceFactory.class)
		.registerResourceMappings(new CardServicesResourceMapper(), APIImplementationTypes.BASE);
		
		DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
		.registerBusinessDelegateMappings(new CardServicesBusinessDelegateMapper(), APIImplementationTypes.BASE);
	}
	public static void registerDBXImpl() {
		DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(ResourceFactory.class)
		.registerResourceMappings(new CardServicesDBXResourceMapper(), APIImplementationTypes.BASE);

		DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
		.registerBusinessDelegateMappings(new CardServicesDBXBusinessDelegateMapper(), APIImplementationTypes.BASE);
	}

	@Override
	public void destroy() {
	}
}


