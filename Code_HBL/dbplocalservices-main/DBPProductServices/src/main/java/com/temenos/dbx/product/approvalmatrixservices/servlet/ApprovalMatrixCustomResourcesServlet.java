package com.temenos.dbx.product.approvalmatrixservices.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.dbp.core.api.factory.BackendDelegateFactory;
import com.temenos.dbx.product.approvalmatrixservices.mapper.ApprovalMatrixBackendDelegateMapper;
import com.temenos.dbx.product.approvalmatrixservices.mapper.ApprovalMatrixBusinessDelegateMapper;
import com.temenos.dbx.product.approvalmatrixservices.mapper.ApprovalMatrixResourcesMapper;
import com.dbp.core.api.APIImplementationTypes;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.servlet.IntegrationCustomServlet;

/**
 * 
 * @author KH2387
 * @version 1.0
 * extends {@link HttpServlet}
 */

@IntegrationCustomServlet(servletName = "ApprovalMatrixCustomResourcesServlet", urlPatterns = {
        "ApprovalMatrixCustomResourcesServlet" })
public class ApprovalMatrixCustomResourcesServlet extends HttpServlet {

    private static final long serialVersionUID = 5258692671194055192L;

    @Override
    public void init() throws ServletException {
        // registering base resources and business delegates
        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(ResourceFactory.class)
                .registerResourceMappings(new ApprovalMatrixResourcesMapper(), APIImplementationTypes.BASE);

        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
                .registerBusinessDelegateMappings(new ApprovalMatrixBusinessDelegateMapper(), APIImplementationTypes.BASE);

        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BackendDelegateFactory.class)
                .registerBackendDelegateMappings(new ApprovalMatrixBackendDelegateMapper(), APIImplementationTypes.BASE);
    }

    @Override
    public void destroy() {
    }

}
