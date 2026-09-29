package com.temenos.dbx.product.transactionservices.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.dbp.core.api.APIImplementationTypes;
import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.servlet.IntegrationCustomServlet;
import com.temenos.dbx.product.constants.TransactionBackendURL;
import com.temenos.dbx.product.transactionservices.mapper.TransactionServicesBackendDelegateMapper;
import com.temenos.dbx.product.transactionservices.mapper.TransactionServicesBusinessDelegateMapper;
import com.temenos.dbx.product.transactionservices.mapper.TransactionServicesResourceMapper;



/**
 * 
 * @author KH1755
 * @version 1.0
 * extends {@link HttpServlet}
 */
@IntegrationCustomServlet(servletName = "TransactionServicesCustomResourcesServlet", urlPatterns = {
"TransactionServicesCustomResourcesServlet" })
public class TransactionServicesCustomResourcesServlet extends HttpServlet{
	
	private static final long serialVersionUID = 5258692671194055192L;

    @Override
    public void init() throws ServletException {
    	
        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(ResourceFactory.class)
                .registerResourceMappings(new TransactionServicesResourceMapper(), APIImplementationTypes.BASE);

        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
                .registerBusinessDelegateMappings(new TransactionServicesBusinessDelegateMapper(), APIImplementationTypes.BASE);
        
        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BackendDelegateFactory.class)
		.registerBackendDelegateMappings(new TransactionServicesBackendDelegateMapper(),
				APIImplementationTypes.BASE);
        
        //Load the backend properties
        TransactionBackendURL backendHelper = new TransactionBackendURL();
        backendHelper.loadTransactionBackendURLs();

    }

    @Override
    public void destroy() {
    }
}
