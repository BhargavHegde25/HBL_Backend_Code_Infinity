package com.kony.adminconsole.service.customerdatamanagement.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.dbp.core.api.APIImplementationTypes;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.service.customerdatamanagement.mapper.CustomerDataManagementBusinessDelegateMapper;
import com.kony.adminconsole.service.customerdatamanagement.mapper.CustomerDataManagementResourceMapper;
import com.konylabs.middleware.servlet.IntegrationCustomServlet;

@IntegrationCustomServlet(servletName = "CustomerDataManagementServlet", urlPatterns = {
"CustomerDataManagementServlet" })
public class CustomerDataManagementServlet extends HttpServlet{

	public static final long serialVersionUID = 1L;

    @Override
    public void init() throws ServletException {

        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(ResourceFactory.class)
                .registerResourceMappings(new CustomerDataManagementResourceMapper(), APIImplementationTypes.BASE);

        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
                .registerBusinessDelegateMappings(new CustomerDataManagementBusinessDelegateMapper(), APIImplementationTypes.BASE);

    }

    @Override
    public void destroy() {

    }
}
