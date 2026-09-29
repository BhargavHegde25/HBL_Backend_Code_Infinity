package com.kony.adminconsole.service.approvalworkflow.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.dbp.core.api.APIImplementationTypes;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.service.approvalworkflow.mapper.ApprovalWorkflowResourceMapper;
import com.kony.adminconsole.service.approvalworkflow.mapper.ApprovalWorkflowBusinessDelegateMapper;
import com.konylabs.middleware.servlet.IntegrationCustomServlet;

/**
 *
 * @author This is a custom servlet for customer Services
 * @version 1.0
 * @extends {HttpServlet}
 *
 */

@IntegrationCustomServlet(servletName = "ApprovalWorkflowServletCustomResourceServlet", urlPatterns = {
        "ApprovalWorkflowServletCustomResourceServlet" })
public class ApprovalWorkflowServlet extends HttpServlet {

    public static final long serialVersionUID = 1L;

    @Override
    public void init() throws ServletException {

        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(ResourceFactory.class)
                .registerResourceMappings(new ApprovalWorkflowResourceMapper(), APIImplementationTypes.BASE);

        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
                .registerBusinessDelegateMappings(new ApprovalWorkflowBusinessDelegateMapper(), APIImplementationTypes.BASE);

    }

    @Override
    public void destroy() {

    }

}