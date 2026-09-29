package com.kony.adminconsole.service.contract.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.dbp.core.api.APIImplementationTypes;
import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.service.contract.mapper.ContractBackendDelegateMapper;
import com.kony.adminconsole.service.contract.mapper.ContractBusinessDelegateMapper;
import com.kony.adminconsole.service.contract.mapper.ContractResourceMapper;
import com.konylabs.middleware.servlet.IntegrationCustomServlet;

/**
 * 
 * @author This is a custom servlet for contract Services
 * @version 1.0
 * @extends {HttpServlet}
 *
 */

@IntegrationCustomServlet(servletName = "ContractServletCustomResourceServlet", urlPatterns = {
        "ContractServletCustomResourceServlet" })
public class ContractServlet extends HttpServlet {

    public static final long serialVersionUID = 1L;

    @Override
    public void init() throws ServletException {

        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(ResourceFactory.class)
                .registerResourceMappings(new ContractResourceMapper(), APIImplementationTypes.BASE);

        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
                .registerBusinessDelegateMappings(new ContractBusinessDelegateMapper(), APIImplementationTypes.BASE);

        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BackendDelegateFactory.class)
                .registerBackendDelegateMappings(new ContractBackendDelegateMapper(), APIImplementationTypes.BASE);
    }

    @Override
    public void destroy() {

    }

}
