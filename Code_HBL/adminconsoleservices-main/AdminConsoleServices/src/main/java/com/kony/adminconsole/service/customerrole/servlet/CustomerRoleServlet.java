package com.kony.adminconsole.service.customerrole.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import com.dbp.core.api.APIImplementationTypes;
import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.service.customerrole.mapper.CustomerRoleBackendDelegateMapper;
import com.kony.adminconsole.service.customerrole.mapper.CustomerRoleBusinessDelegateMapper;
import com.kony.adminconsole.service.customerrole.mapper.CustomerRoleResourceMapper;
import com.konylabs.middleware.servlet.IntegrationCustomServlet;

/**
 * 
 * @author KH2661
 * This is a custom servlet for Customer role Services
 * @version 1.0
 * @extends {HttpServlet}
 *
 */
@IntegrationCustomServlet(servletName = "CustomerRoleCustomServlet", urlPatterns = {
"CustomerRoleCustomServlet" })
public class CustomerRoleServlet  extends HttpServlet{

	private static final long serialVersionUID = -547826949327612294L;

	@Override
    public void init() throws ServletException {

        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(ResourceFactory.class)
                .registerResourceMappings(new CustomerRoleResourceMapper(), APIImplementationTypes.BASE);

        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
                .registerBusinessDelegateMappings(new CustomerRoleBusinessDelegateMapper(), APIImplementationTypes.BASE);
        
        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BackendDelegateFactory.class)
        .registerBackendDelegateMappings(new CustomerRoleBackendDelegateMapper(), APIImplementationTypes.BASE);
    }

    @Override
    public void destroy() {

    }
	
}
