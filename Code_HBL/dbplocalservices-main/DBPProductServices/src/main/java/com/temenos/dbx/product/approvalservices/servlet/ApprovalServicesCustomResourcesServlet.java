package com.temenos.dbx.product.approvalservices.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.dbp.core.api.APIImplementationTypes;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.temenos.dbx.product.approvalservices.mapper.ApprovalServicesBusinessDelegateMapper;
import com.temenos.dbx.product.approvalservices.mapper.ApprovalServicesResourceMapper;
import com.konylabs.middleware.servlet.IntegrationCustomServlet;


/**
 * 
 * @author KH2174
 * @version 1.0
 * extends {@link HttpServlet}
 */

@IntegrationCustomServlet(servletName = "ApprovalServiceCustomResourcesServlet", urlPatterns = {
"ApprovalServiceCustomResourcesServlet" })
public class ApprovalServicesCustomResourcesServlet extends HttpServlet{
	private static final long serialVersionUID = 5258692671194055192L;

    @Override
    public void init() throws ServletException {
    	
        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(ResourceFactory.class)
                .registerResourceMappings(new ApprovalServicesResourceMapper(), APIImplementationTypes.BASE);

        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
                .registerBusinessDelegateMappings(new ApprovalServicesBusinessDelegateMapper(), APIImplementationTypes.BASE);
    }

    @Override
    public void destroy() {
    }
}
