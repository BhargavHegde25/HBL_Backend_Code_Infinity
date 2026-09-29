package com.temenos.dbx.common.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.dbp.core.api.APIImplementationTypes;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.servlet.IntegrationCustomServlet;
import com.temenos.dbx.common.mapper.BaseBusinessDelegateMapper;


@IntegrationCustomServlet(servletName = "BusinessAPIsBaseCustomResourcesServlet", urlPatterns = {
        "BusinessAPIsBaseCustomResourcesServlet" })
public class BusinessAPIsBaseCustomResourcesServlet extends HttpServlet {

    private static final long serialVersionUID = 5238602672197055192L;

    @Override
    public void init() throws ServletException {

        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
                .registerBusinessDelegateMappings(new BaseBusinessDelegateMapper(), APIImplementationTypes.BASE);
    }

    @Override
    public void destroy() {
    }

}
