package com.temenos.dbx.product.bulkpaymentservices.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.dbp.core.api.APIImplementationTypes;
import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.servlet.IntegrationCustomServlet;
import com.temenos.dbx.product.bulkpaymentservices.mapper.BulkPaymentServicesBackendDelegateMapper;
import com.temenos.dbx.product.bulkpaymentservices.mapper.BulkPaymentServicesBusinessDelegateMapper;
import com.temenos.dbx.product.bulkpaymentservices.mapper.BulkPaymentServicesResourceMapper;

@IntegrationCustomServlet(servletName = "BulkPaymentServicesCustomResourcesServlet", urlPatterns = {
"BulkPaymentServicesCustomResourcesServlet" })
public class BulkPaymentServicesCustomResourcesServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3129189462161930907L;
	
	@Override
    public void init() throws ServletException {
    	
        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(ResourceFactory.class)
                .registerResourceMappings(new BulkPaymentServicesResourceMapper(), APIImplementationTypes.BASE);

        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
                .registerBusinessDelegateMappings(new BulkPaymentServicesBusinessDelegateMapper(), APIImplementationTypes.BASE);
        
        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BackendDelegateFactory.class)
      		.registerBackendDelegateMappings(new BulkPaymentServicesBackendDelegateMapper(),
      				APIImplementationTypes.BASE);
    }

    @Override
    public void destroy() {
    	
    }

}
