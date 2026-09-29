package com.temenos.dbx.product.approvalsframework.approvalrequest.servlet;

import com.dbp.core.api.APIImplementationTypes;
import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.servlet.IntegrationCustomServlet;
import com.temenos.dbx.product.approvalsframework.approvalrequest.mapper.ApprovalRequestBackendDelegateMapper;
import com.temenos.dbx.product.approvalsframework.approvalrequest.mapper.ApprovalRequestBusinessDelegateMapper;
import com.temenos.dbx.product.approvalsframework.approvalrequest.mapper.ApprovalRequestResourceMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

@IntegrationCustomServlet(servletName = "ApprovalRequestCustomServlet", urlPatterns = {
        "ApprovalRequestCustomServlet"})
public class ApprovalRequestCustomServlet extends HttpServlet {
    public static final long serialVersionUID = 2L;

    @Override
    public void init() throws ServletException {
        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(ResourceFactory.class)
                .registerResourceMappings(new ApprovalRequestResourceMapper(), APIImplementationTypes.BASE);
        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
                .registerBusinessDelegateMappings(new ApprovalRequestBusinessDelegateMapper(), APIImplementationTypes.BASE);
        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BackendDelegateFactory.class)
                .registerBackendDelegateMappings(new ApprovalRequestBackendDelegateMapper(), APIImplementationTypes.BASE);
    }

    @Override
    public void destroy() {

    }
}
