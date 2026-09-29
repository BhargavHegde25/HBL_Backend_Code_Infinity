package com.kony.makerchecker.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.dbp.core.api.APIImplementationTypes;
import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.makerchecker.mapper.MakerCheckerBackendDelegateMapper;
import com.kony.makerchecker.mapper.MakerCheckerBusinessDelegateMapper;
import com.kony.makerchecker.mapper.MakerCheckerResourceMapper;
import com.konylabs.middleware.servlet.IntegrationCustomServlet;

@IntegrationCustomServlet(servletName = "MakerCheckerCustomResourceServlet", urlPatterns = {
"MakerCheckerCustomResourceServlet" })
public class MakerCheckerServlet extends HttpServlet{

	public static final long serialVersionUID = 1L;

	@Override
	public void init() throws ServletException {

	    DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(ResourceFactory.class)
	            .registerResourceMappings(new MakerCheckerResourceMapper(), APIImplementationTypes.BASE);

	    DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
	            .registerBusinessDelegateMappings(new MakerCheckerBusinessDelegateMapper(), APIImplementationTypes.BASE);

	    DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BackendDelegateFactory.class)
	            .registerBackendDelegateMappings(new MakerCheckerBackendDelegateMapper(), APIImplementationTypes.BASE);

	}

	@Override
	public void destroy() {

	}
	
}
