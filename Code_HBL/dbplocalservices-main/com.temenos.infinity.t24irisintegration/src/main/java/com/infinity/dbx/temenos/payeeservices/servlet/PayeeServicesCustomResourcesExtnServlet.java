package com.infinity.dbx.temenos.payeeservices.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.dbp.core.api.APIImplementationTypes;
import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.infinity.dbx.temenos.payeeservices.mapper.PayeeServicesBackendExtnDelegateMapper;
import com.konylabs.middleware.servlet.IntegrationCustomServlet;

@IntegrationCustomServlet(servletName = "PayeeServicesCustomResourcesExtnServlet", urlPatterns = {
"PayeeServicesCustomResourcesExtnServlet" })
public class PayeeServicesCustomResourcesExtnServlet extends HttpServlet{
	
	private static final long serialVersionUID = -3129189462161930907L;
	
	@Override
    public void init() throws ServletException {
    	               
        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BackendDelegateFactory.class)
      		.registerBackendDelegateMappings(new PayeeServicesBackendExtnDelegateMapper(),
      				APIImplementationTypes.EXTENSION);
    }

    @Override
    public void destroy() {
    	
    }
}