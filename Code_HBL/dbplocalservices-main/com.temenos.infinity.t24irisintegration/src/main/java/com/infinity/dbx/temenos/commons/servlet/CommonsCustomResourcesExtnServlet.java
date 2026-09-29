
package com.infinity.dbx.temenos.commons.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.dbp.core.api.APIImplementationTypes;
import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.infinity.dbx.temenos.commons.mapper.CommonsBackendExtnDelegateMapper;
import com.konylabs.middleware.servlet.IntegrationCustomServlet;

@IntegrationCustomServlet(servletName = "CommonsCustomResourcesExtnServlet", urlPatterns = {
"CommonsCustomResourcesExtnServlet" })
public class CommonsCustomResourcesExtnServlet extends HttpServlet{
	
	private static final long serialVersionUID = -3129189462161930907L;
	
	@Override
    public void init() throws ServletException {
    	               
        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BackendDelegateFactory.class)
      		.registerBackendDelegateMappings(new CommonsBackendExtnDelegateMapper(),
      				APIImplementationTypes.EXTENSION);
    }

    @Override
    public void destroy() {
    	
    }

}

