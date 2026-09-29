package com.kony.adminconsole.core.config;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.dbp.core.api.APIImplementationTypes;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.campaign.mapper.BaseBusinessDelegateMapper;
import com.kony.adminconsole.campaign.mapper.BaseResourcesMapper;
import com.kony.adminconsole.commons.utils.ThreadExecutor;
import com.konylabs.middleware.servlet.IntegrationCustomServlet;

/**
 * Custom servlet used to register & release resources required by Customer 360
 * 
 * @author Alahari Prudhvi Akhil, Aditya Mankal
 *
 */
@IntegrationCustomServlet(servletName = "AdminConsoleCustomResourcesServlet", urlPatterns = {
        "AdminConsoleCustomResourcesServlet" })
public class AdminConsoleCustomResourcesServlet extends HttpServlet {

    private static final long serialVersionUID = -3727063610799396467L;

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");

    @Override
    public void init() throws ServletException {
        try {
        	DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
            .registerBusinessDelegateMappings(new ServicePermissionMapper(), APIImplementationTypes.BASE);            
        	//DBPAPIAbstractFactory.registerFactory(ServicePermissionMapRegister.class);
            // Registering the class which populates the Service Permission Mapping
            //DBPServicesManager.registerDBPServiceImpl(ServicePermissionMapRegister.class);
        } catch (Exception e) {
            alert.prepareError("Exception in initializing resources", e).log();
        }

		initializeDBPFramework();
	}

	private void initializeDBPFramework() {
		try {
			DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(ResourceFactory.class)
			.registerResourceMappings(new BaseResourcesMapper(), APIImplementationTypes.BASE);

			DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
			.registerBusinessDelegateMappings(new BaseBusinessDelegateMapper(), APIImplementationTypes.BASE);
		} catch (Exception e) {
			alert.prepareError("Exception in initializing DBPFramework base resources", e).log();
		}
    }

    @Override
    public void destroy() {
        try {
            // Shutting down the customer 360 thread pool
            ThreadExecutor.shutdownExecutor();
        } catch (Exception e) {
            alert.prepareError("Exception in destroying resources", e).log();
        }
    }

}
