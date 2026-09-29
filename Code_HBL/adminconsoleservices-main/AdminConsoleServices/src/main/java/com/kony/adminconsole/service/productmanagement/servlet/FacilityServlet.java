package com.kony.adminconsole.service.productmanagement.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.dbp.core.api.APIImplementationTypes;
import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.service.productmanagement.mapper.FacilityBackendDelegateMapper;
import com.kony.adminconsole.service.productmanagement.mapper.FacilityBusinessDelegateMapper;
import com.kony.adminconsole.service.productmanagement.mapper.FacilityResourceMapper;
import com.konylabs.middleware.servlet.IntegrationCustomServlet;

/**
 * 
 * @author This is a custom servlet for Facility Related Services
 * @version 1.0
 * @extends {HttpServlet}
 *
 */
@IntegrationCustomServlet(servletName = "FacilityServletCustomResourceServlet", urlPatterns = {
		"FacilityServletCustomResourceServlet" })
public class FacilityServlet extends HttpServlet {

    public static final long serialVersionUID = 42L;

    @Override
    public void init() throws ServletException {

        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(ResourceFactory.class)
                .registerResourceMappings(new FacilityResourceMapper(), APIImplementationTypes.BASE);

        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
                .registerBusinessDelegateMappings(new FacilityBusinessDelegateMapper(), APIImplementationTypes.BASE);
        
        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BackendDelegateFactory.class)
        .registerBackendDelegateMappings(new FacilityBackendDelegateMapper(), APIImplementationTypes.BASE);
     }

    @Override
    public void destroy() {

    }
}
