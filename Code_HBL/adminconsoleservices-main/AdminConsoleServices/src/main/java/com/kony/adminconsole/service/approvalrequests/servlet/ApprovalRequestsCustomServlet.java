package com.kony.adminconsole.service.approvalrequests.servlet;

import com.dbp.core.api.APIImplementationTypes;
import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.service.approvalrequests.mapper.ApprovalRequestsBackendDelegateMapper;
import com.kony.adminconsole.service.approvalrequests.mapper.ApprovalRequestsBusinessDelegateMapper;
import com.kony.adminconsole.service.approvalrequests.mapper.ApprovalRequestsResourceMapper;
import com.konylabs.middleware.servlet.IntegrationCustomServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * @extends {HttpServlet}
 */
@IntegrationCustomServlet(servletName = "ApprovalRequestsCustomServlet", urlPatterns = {
        "ApprovalRequestsCustomServlet" })
public class ApprovalRequestsCustomServlet extends HttpServlet {

    public static final long serialVersionUID = 2L;

    @Override
    public void init() throws ServletException {

        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(ResourceFactory.class)
                .registerResourceMappings(new ApprovalRequestsResourceMapper(), APIImplementationTypes.BASE);

        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
                .registerBusinessDelegateMappings(new ApprovalRequestsBusinessDelegateMapper(), APIImplementationTypes.BASE);

        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BackendDelegateFactory.class)
                .registerBackendDelegateMappings(new ApprovalRequestsBackendDelegateMapper(), APIImplementationTypes.BASE);

    }

    @Override
    public void destroy() {

    }
}
