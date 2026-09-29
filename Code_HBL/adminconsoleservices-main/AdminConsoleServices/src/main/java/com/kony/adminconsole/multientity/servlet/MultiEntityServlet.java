package com.kony.adminconsole.multientity.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.dbp.core.api.APIImplementationTypes;
import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.multientity.mapper.MultiEntityBackendDelegateMapper;
import com.kony.adminconsole.multientity.mapper.MultiEntityBusinessDelegateMapper;
import com.kony.adminconsole.multientity.mapper.MultiEntityResourceMapper;
import com.konylabs.middleware.servlet.IntegrationCustomServlet;

@IntegrationCustomServlet(servletName = "MultiEntityServletCustomResourceServlet", urlPatterns = {
"MultiEntityServletCustomResourceServlet" })
public class MultiEntityServlet  extends HttpServlet {
	
public static final long serialVersionUID = 1L;

@Override
public void init() throws ServletException {

    DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(ResourceFactory.class)
            .registerResourceMappings(new MultiEntityResourceMapper(), APIImplementationTypes.BASE);

    DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
            .registerBusinessDelegateMappings(new MultiEntityBusinessDelegateMapper(), APIImplementationTypes.BASE);

    DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BackendDelegateFactory.class)
            .registerBackendDelegateMappings(new MultiEntityBackendDelegateMapper(), APIImplementationTypes.BASE);

}

@Override
public void destroy() {

}

}
