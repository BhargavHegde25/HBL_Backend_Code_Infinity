package com.temenos.dbx.product.approvalsframework.approvalmatrix.servlet;

import com.dbp.core.api.APIImplementationTypes;
import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.servlet.IntegrationCustomServlet;
import com.temenos.dbx.product.approvalsframework.approvalmatrix.mapper.ApprovalMatrixBackendDelegateMapper;
import com.temenos.dbx.product.approvalsframework.approvalmatrix.mapper.ApprovalMatrixBusinessDelegateMapper;
import com.temenos.dbx.product.approvalsframework.approvalmatrix.mapper.ApprovalMatrixResourceMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

@IntegrationCustomServlet(servletName = "ApprovalMatrixCustomServlet", urlPatterns = {
        "ApprovalMatrixCustomServlet"})
public class ApprovalMatrixCustomServlet extends HttpServlet {
    public static final long serialVersionUID = 2L;

    @Override
    public void init() throws ServletException {
        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(ResourceFactory.class)
                .registerResourceMappings(new ApprovalMatrixResourceMapper(), APIImplementationTypes.BASE);
        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
                .registerBusinessDelegateMappings(new ApprovalMatrixBusinessDelegateMapper(), APIImplementationTypes.BASE);
        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BackendDelegateFactory.class)
                .registerBackendDelegateMappings(new ApprovalMatrixBackendDelegateMapper(), APIImplementationTypes.BASE);
    }

    @Override
    public void destroy() {

    }
}
