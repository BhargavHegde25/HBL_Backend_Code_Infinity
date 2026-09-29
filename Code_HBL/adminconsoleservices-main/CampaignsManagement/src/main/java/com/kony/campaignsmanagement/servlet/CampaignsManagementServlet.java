package com.kony.campaignsmanagement.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.dbp.core.api.APIImplementationTypes;
import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.campaignsmanagement.mapper.CampaignsManagementBackendDelegateMapper;
import com.kony.campaignsmanagement.mapper.CampaignsManagementBusinessDelegateMapper;
import com.kony.campaignsmanagement.mapper.CampaignsManagementResourceMapper;
import com.konylabs.middleware.servlet.IntegrationCustomServlet;

@IntegrationCustomServlet(servletName = "CampaignsManagementCustomResourceServlet", urlPatterns = {
"CampaignsManagementCustomResourceServlet" })
public class CampaignsManagementServlet extends HttpServlet {

	public static final long serialVersionUID = 1L;

	@Override
	public void init() throws ServletException {

	    DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(ResourceFactory.class)
	            .registerResourceMappings(new CampaignsManagementResourceMapper(), APIImplementationTypes.BASE);

	    DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
	            .registerBusinessDelegateMappings(new CampaignsManagementBusinessDelegateMapper(), APIImplementationTypes.BASE);

	    DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BackendDelegateFactory.class)
	            .registerBackendDelegateMappings(new CampaignsManagementBackendDelegateMapper(), APIImplementationTypes.BASE);

	}

	@Override
	public void destroy() {

	}
}
