package com.temenos.dbx.party.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.dbp.core.api.APIImplementationTypes;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.servlet.IntegrationCustomServlet;
import com.temenos.dbx.party.mapper.PartyBusinessDelegateMapper;
import com.temenos.dbx.party.mapper.PartyResourceMapper;
import com.temenos.dbx.product.integration.IntegrationMappings;

@IntegrationCustomServlet(servletName = "PartyServlet", urlPatterns = {
        "PartyServlet" })
public class PartyServlet extends HttpServlet {

    /**
     * 
     */
    private static final long serialVersionUID = -7996896027215639726L;

    @Override
    public void init() throws ServletException {
    	
    	    String integrationName = IntegrationMappings.getInstance().getIntegrationName();
    	    if ("party".equalsIgnoreCase(integrationName)) {
    	      register();
    	    }
    }

    
    public static void register() {
    	 DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(ResourceFactory.class)
         .registerResourceMappings(new PartyResourceMapper(), APIImplementationTypes.BASE);

    	 DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
         .registerBusinessDelegateMappings(new PartyBusinessDelegateMapper(),
                 APIImplementationTypes.BASE);
    }
    
    
    @Override
    public void destroy() {

    }
}
