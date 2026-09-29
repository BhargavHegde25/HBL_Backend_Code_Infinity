package com.temenos.dbx.product.contract.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.dbp.core.api.APIImplementationTypes;
import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.servlet.IntegrationCustomServlet;
import com.temenos.dbx.product.contract.mapper.ContractBackendDelegateMapper;
import com.temenos.dbx.product.contract.mapper.ContractBusinessDelegateMapper;
import com.temenos.dbx.product.contract.mapper.ContractResourceMapper;

@IntegrationCustomServlet(servletName = "ContractCustomResourceServlet", urlPatterns = {
        "ContractCustomResourceServlet" })
public class ContractCustomResourceServlet extends HttpServlet {

    /**
     * 
     */
    private static final long serialVersionUID = 8525716451974894311L;

    @Override
    public void init() throws ServletException {
        // Register Resource Delegates
        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(ResourceFactory.class)
                .registerResourceMappings(new ContractResourceMapper(), APIImplementationTypes.BASE);

        // Register Business Delegates
        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
                .registerBusinessDelegateMappings(new ContractBusinessDelegateMapper(), APIImplementationTypes.BASE);

        // Register Backend Delegates
        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BackendDelegateFactory.class)
                .registerBackendDelegateMappings(new ContractBackendDelegateMapper(), APIImplementationTypes.BASE);
    }

    @Override
    public void destroy() {
    }

}
