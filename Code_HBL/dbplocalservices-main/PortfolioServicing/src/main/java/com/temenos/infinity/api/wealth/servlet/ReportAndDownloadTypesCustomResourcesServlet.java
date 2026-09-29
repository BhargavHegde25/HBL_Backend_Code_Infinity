package com.temenos.infinity.api.wealth.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.dbp.core.api.APIImplementationTypes;
import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.servlet.IntegrationCustomServlet;
import com.temenos.infinity.api.wealth.mapper.ReportAndDownloadTypesBackendDelegateMapper;
import com.temenos.infinity.api.wealth.mapper.ReportAndDownloadTypesBusinessDelegateMapper;
import com.temenos.infinity.api.wealth.mapper.ReportAndDownloadTypesResourceMapper;


@IntegrationCustomServlet(servletName = "ReportAndDownloadTypesCustomResourcesServlet", urlPatterns = {
"ReportAndDownloadTypesCustomResourcesServlet" })

public class ReportAndDownloadTypesCustomResourcesServlet extends HttpServlet {
	
	private static final long serialVersionUID = 7467307922351824473L;
	 @Override
	    public void init() throws ServletException {
	        // Register Resource Delegates
	        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(ResourceFactory.class)
	                .registerResourceMappings(new ReportAndDownloadTypesResourceMapper(), APIImplementationTypes.BASE);

	        // Register Business Delegates
	        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
	                .registerBusinessDelegateMappings(new ReportAndDownloadTypesBusinessDelegateMapper(),
	                        APIImplementationTypes.BASE);

	        // Register Backend Delegates
	        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BackendDelegateFactory.class)
	                .registerBackendDelegateMappings(new ReportAndDownloadTypesBackendDelegateMapper(),
	                        APIImplementationTypes.BASE);
	    }

	    @Override
	    public void destroy() {
	    }

}
