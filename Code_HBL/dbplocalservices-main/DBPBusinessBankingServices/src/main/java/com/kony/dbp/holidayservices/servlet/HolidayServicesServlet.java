package com.kony.dbp.holidayservices.servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;


import com.kony.dbp.holidayservices.mapper.HolidayServicesBusinessDelegateMapper;
import com.kony.dbp.holidayservices.mapper.HolidayServicesResourceMapper;
import com.dbp.core.api.APIImplementationTypes;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.servlet.IntegrationCustomServlet;

/**
 * 
 * @author KH2394
 * @version 1.0
 * extends {@link HttpServlet}
 */

@IntegrationCustomServlet(servletName = "HolidaysServlet", urlPatterns = {
"HolidaysServlet" })

public class HolidayServicesServlet  extends HttpServlet {

	private static final long serialVersionUID = 5258692671194055192L;

	@Override
	public void init() throws ServletException {

		DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(ResourceFactory.class)
		.registerResourceMappings(new HolidayServicesResourceMapper(), APIImplementationTypes.BASE);

		DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
		.registerBusinessDelegateMappings(new HolidayServicesBusinessDelegateMapper(), APIImplementationTypes.BASE);
	}

	@Override
	public void destroy() {
	}
}
