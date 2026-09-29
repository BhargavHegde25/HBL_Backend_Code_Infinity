package com.temenos.dbx.transaction.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.dbp.core.api.APIImplementationTypes;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.servlet.IntegrationCustomServlet;
import com.temenos.dbx.transaction.mapper.BlockedFundsBusinessDelegateMapper;
import com.temenos.dbx.transaction.mapper.BlockedFundsResourceMapper;

/**
 * TODO: Document me!
 *
 * @author smugesh
 *
 */

@IntegrationCustomServlet(servletName = "BlockedFundsServlet", urlPatterns = {
"BlockedFundsServlet" })

public class BlockedFundsServlet  extends HttpServlet {

    private static final long serialVersionUID = 5258692671184055192L;

    @Override
    public void init() throws ServletException {

        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(ResourceFactory.class)
        .registerResourceMappings(new BlockedFundsResourceMapper(), APIImplementationTypes.BASE);

        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
        .registerBusinessDelegateMappings(new BlockedFundsBusinessDelegateMapper(), APIImplementationTypes.BASE);
    }
 
    @Override
    public void destroy() {
    }
}
