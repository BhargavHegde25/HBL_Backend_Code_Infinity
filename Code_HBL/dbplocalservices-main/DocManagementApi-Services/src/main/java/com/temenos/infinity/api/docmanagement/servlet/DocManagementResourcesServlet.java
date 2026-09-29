package com.temenos.infinity.api.docmanagement.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.dbp.core.api.APIImplementationTypes;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.servlet.IntegrationCustomServlet;
import com.temenos.infinity.api.docmanagement.mapper.DocManagementBusinessDelegateMapper;
import com.temenos.infinity.api.docmanagement.mapper.DocManagementResourceMapper;
import com.temenos.dbx.product.utils.ThreadExecutor;

@IntegrationCustomServlet(servletName = "DocManagementResourcesServlet", urlPatterns = {
        "DocManagementResourcesServlet" })
public class DocManagementResourcesServlet extends HttpServlet {

    /**
     * 
     */
    private static final long serialVersionUID = -7996896027215639726L;

    @Override
    public void init() throws ServletException {

        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(ResourceFactory.class)
                .registerResourceMappings(new DocManagementResourceMapper(), APIImplementationTypes.BASE);

        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
                .registerBusinessDelegateMappings(new DocManagementBusinessDelegateMapper(),
                        APIImplementationTypes.BASE);
    }

    @Override
    public void destroy() {
    	try {
            // Shutting down the customer 360 thread pool
            ThreadExecutor.getExecutor().shutdownExecutor();
        } catch (Exception e) {
            
        }
    }
}
