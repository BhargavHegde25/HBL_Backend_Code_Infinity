package com.kony.adminconsole.licensing.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.dbp.core.api.APIImplementationTypes;
import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.licensing.mapper.LicensingBackendDelegateMapper;
import com.kony.adminconsole.licensing.mapper.LicensingBusinessDelegateMapper;
import com.kony.adminconsole.licensing.mapper.LicensingResourceMapper;
import com.konylabs.middleware.servlet.IntegrationCustomServlet;

@IntegrationCustomServlet(servletName = "LicensingUnitCustomResourceServlet", urlPatterns = {
"LicensingUnitCustomResourceServlet" })
public class LicensingServlet extends HttpServlet {

	
	public static final long serialVersionUID = 1L;

	@Override
	public void init() throws ServletException {

	    DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(ResourceFactory.class)
	            .registerResourceMappings(new LicensingResourceMapper(), APIImplementationTypes.BASE);

	    DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
	            .registerBusinessDelegateMappings(new LicensingBusinessDelegateMapper(), APIImplementationTypes.BASE);

	    DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BackendDelegateFactory.class)
	            .registerBackendDelegateMappings(new LicensingBackendDelegateMapper(), APIImplementationTypes.BASE);

	}

	@Override
	public void destroy() {

	}

}
