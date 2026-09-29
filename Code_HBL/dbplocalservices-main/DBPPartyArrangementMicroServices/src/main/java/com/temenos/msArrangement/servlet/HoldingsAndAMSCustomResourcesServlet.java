package com.temenos.msArrangement.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.dbp.core.api.APIImplementationTypes;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.servlet.IntegrationCustomServlet;
import com.temenos.msArrangement.mapper.HoldingsAndAMSBusinessDelegateMapper;
import com.temenos.msArrangement.mapper.HoldingsAndAMSResourceMapper;

/**
 * Custom servlet used to intialize/destroy resources required by Log Services
 * 
 * @author KH2281
 * @version 1.0
 * extends {@link HttpServlet}
 *
 */

@IntegrationCustomServlet(servletName = "HoldingsAndAMSCustomResourcesServlet", urlPatterns = {
        "HoldingsAndAMSCustomResourcesServlet" })
public class HoldingsAndAMSCustomResourcesServlet extends HttpServlet {

    private static final long serialVersionUID = -2198682326030156595L;

    @Override
    public void init() throws ServletException {
        // registering base resources and business delegates
        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(ResourceFactory.class)
                .registerResourceMappings(new HoldingsAndAMSResourceMapper(), APIImplementationTypes.BASE);

        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
                .registerBusinessDelegateMappings(new HoldingsAndAMSBusinessDelegateMapper(), APIImplementationTypes.BASE);
    }

    @Override
    public void destroy() {
    }
 
}
