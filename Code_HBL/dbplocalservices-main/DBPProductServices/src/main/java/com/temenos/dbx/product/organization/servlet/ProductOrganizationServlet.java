package com.temenos.dbx.product.organization.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.dbp.core.api.APIImplementationTypes;
import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.servlet.IntegrationCustomServlet;
import com.temenos.dbx.product.organization.mapper.OrganizationBackendDelegateMapper;
import com.temenos.dbx.product.organization.mapper.OrganizationBusinessDelegateMapper;
import com.temenos.dbx.product.organization.mapper.OrganizationResourceMapper;
import com.temenos.dbx.product.utils.ThreadExecutor;

/**
 * 
 * @author Infinity DBX
 *
 */
@IntegrationCustomServlet(servletName = "OrganizationResourcesServlet", urlPatterns = {
        "OrganizationResourcesServlet" })
public class ProductOrganizationServlet extends HttpServlet {

    private static final long serialVersionUID = 1547559709023301174L;

    public void init() throws ServletException {

        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(ResourceFactory.class)
                .registerResourceMappings(new OrganizationResourceMapper(), APIImplementationTypes.BASE);

        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
                .registerBusinessDelegateMappings(new OrganizationBusinessDelegateMapper(),
                        APIImplementationTypes.BASE);

        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BackendDelegateFactory.class)
                .registerBackendDelegateMappings(new OrganizationBackendDelegateMapper(),
                        APIImplementationTypes.BASE);
    }

    @Override
    public void destroy() {
        try {
            // Shutting down the customer 360 thread pool
            ThreadExecutor.getExecutor().shutdownExecutor();
        } catch (Exception e) {

        }
    }

}
