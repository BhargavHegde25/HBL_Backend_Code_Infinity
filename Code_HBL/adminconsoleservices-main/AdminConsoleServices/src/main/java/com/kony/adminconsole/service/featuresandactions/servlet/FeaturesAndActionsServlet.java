package com.kony.adminconsole.service.featuresandactions.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.dbp.core.api.APIImplementationTypes;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.service.featuresandactions.mapper.FeaturesAndActionsBusinessDelegateMapper;
import com.kony.adminconsole.service.featuresandactions.mapper.FeaturesAndActionsResourceMapper;
import com.konylabs.middleware.servlet.IntegrationCustomServlet;

/**
 * @author KH2660
 * This is a custom servlet for Features and actions Services
 * @version 1.0
 * @extends {HttpServlet}
 *
 */

@IntegrationCustomServlet(servletName = "FeaturesAndActionsCustomResourceServlet", urlPatterns = {
"FeaturesAndActionsCustomResourceServlet" })
public class FeaturesAndActionsServlet extends HttpServlet{

	public static final long serialVersionUID = 2L;
	
	 @Override
	    public void init() throws ServletException {

	        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(ResourceFactory.class)
	                .registerResourceMappings(new FeaturesAndActionsResourceMapper(), APIImplementationTypes.BASE);

	        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
	                .registerBusinessDelegateMappings(new FeaturesAndActionsBusinessDelegateMapper(), APIImplementationTypes.BASE);
	        
	    }

	    @Override
	    public void destroy() {

	    }

}
