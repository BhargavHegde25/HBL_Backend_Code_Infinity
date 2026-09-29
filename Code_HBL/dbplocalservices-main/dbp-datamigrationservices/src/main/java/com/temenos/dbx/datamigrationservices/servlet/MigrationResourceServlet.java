/**
 * 
 */
package com.temenos.dbx.datamigrationservices.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.dbp.core.api.APIImplementationTypes;
import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.servlet.IntegrationCustomServlet;
import com.temenos.dbx.datamigrationservices.mapper.MigrationResourceMapper;
import com.temenos.dbx.datamigrationservices.mapper.MigrationBusinessDelegateMapper;
import com.temenos.dbx.datamigrationservices.mapper.MigrationBackendDelegateMapper;


/**
 * @author amitabh.kotha
 *
 */
@IntegrationCustomServlet(servletName = "MigrationResourceServlet", urlPatterns = {
"MigrationResourceServlet" })
public class MigrationResourceServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5156265780980367928L;
	
	@Override
    public void init() throws ServletException {
		// Register Resource Delegates
        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(ResourceFactory.class)
                .registerResourceMappings(new MigrationResourceMapper(), APIImplementationTypes.BASE);
        
     // Register Business Delegates
        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
                .registerBusinessDelegateMappings(new MigrationBusinessDelegateMapper(), APIImplementationTypes.BASE);

        // Register Backend Delegates
        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BackendDelegateFactory.class)
                .registerBackendDelegateMappings(new MigrationBackendDelegateMapper(), APIImplementationTypes.BASE);
	}
	
    @Override
    public void destroy() {
    	
    }

}
