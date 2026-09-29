package com.kony.adminconsole.service.approvalmatrix.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.dbp.core.api.APIImplementationTypes;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.service.approvalmatrix.mapper.ApprovalMatrixBusinessDelegateMapper;
import com.kony.adminconsole.service.approvalmatrix.mapper.ApprovalMatrixResourceMapper;
import com.konylabs.middleware.servlet.IntegrationCustomServlet;

/**
 * 
 * @author This is a custom servlet for SignatoryGroup Services
 * @version 1.0
 * @extends {HttpServlet}
 *
 */
@IntegrationCustomServlet(servletName = "ApprovalMatrixServletCustomResourceServlet", urlPatterns = {
		"ApprovalMatrixServletCustomResourceServlet" })
public class ApprovalMatrixServlet extends HttpServlet {

    public static final long serialVersionUID = 2L;

    @Override
    public void init() throws ServletException {

        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(ResourceFactory.class)
                .registerResourceMappings(new ApprovalMatrixResourceMapper(), APIImplementationTypes.BASE);

        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
                .registerBusinessDelegateMappings(new ApprovalMatrixBusinessDelegateMapper(), APIImplementationTypes.BASE);

     }

    @Override
    public void destroy() {

    }
}
