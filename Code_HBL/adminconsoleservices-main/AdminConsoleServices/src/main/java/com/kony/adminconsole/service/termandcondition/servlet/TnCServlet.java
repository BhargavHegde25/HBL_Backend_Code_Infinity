package com.kony.adminconsole.service.termandcondition.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.dbp.core.api.APIImplementationTypes;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.service.termandcondition.mapper.TnCBusinessDelegateMapper;
import com.kony.adminconsole.service.termandcondition.mapper.TnCResourceMapper;
import com.konylabs.middleware.servlet.IntegrationCustomServlet;

/**
 * 
 * @author This is a custom servlet for SignatoryGroup Services
 * @version 1.0
 * @extends {HttpServlet}
 *
 */
@IntegrationCustomServlet(servletName = "TnCServletCustomResourceServlet", urlPatterns = {
		"TnCServletCustomResourceServlet" })
public class TnCServlet extends HttpServlet {

    public static final long serialVersionUID = 1L;

    @Override
    public void init() throws ServletException {

        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(ResourceFactory.class)
                .registerResourceMappings(new TnCResourceMapper(), APIImplementationTypes.BASE);

        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
                .registerBusinessDelegateMappings(new TnCBusinessDelegateMapper(), APIImplementationTypes.BASE);

     }

    @Override
    public void destroy() {

    }
}
