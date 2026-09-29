package com.temenos.dbx.product.payeeservices.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.dbp.core.api.APIImplementationTypes;
import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.servlet.IntegrationCustomServlet;
import com.temenos.dbx.product.constants.PayeeVerificationBackendURLFinder;
import com.temenos.dbx.product.payeeservices.mapper.PayeeManagementBackendDelegateMapper;
import com.temenos.dbx.product.payeeservices.mapper.PayeeManagementDelegateMapper;
import com.temenos.dbx.product.payeeservices.mapper.PayeeManagementResourceMapper;




/**
 *
 * This is a custom servlet for Payee Management Services
 *
 * @author KH2544
 * @version 1.0
 * @extends {HttpServlet}
 *
 * **/

@IntegrationCustomServlet(servletName = "PayeeManagementCustomResourceServlet", urlPatterns = {
        "PayeeManagementCustomResourceServlet" })
public class PayeeManagementServlet extends HttpServlet {

    public static final long serialVersionUID = 1L;


    @Override
    public void init() throws ServletException {

        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(ResourceFactory.class)
                .registerResourceMappings(new PayeeManagementResourceMapper(), APIImplementationTypes.BASE);

        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
                .registerBusinessDelegateMappings(new PayeeManagementDelegateMapper(), APIImplementationTypes.BASE);
        
        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BackendDelegateFactory.class)
        .registerBackendDelegateMappings(new PayeeManagementBackendDelegateMapper(), APIImplementationTypes.BASE);
        
		// Load the backend properties
		PayeeVerificationBackendURLFinder backendHelper = new PayeeVerificationBackendURLFinder();
		backendHelper.loadTransactionBackendURLs();
    }

    @Override
    public void destroy() {

    }


}
