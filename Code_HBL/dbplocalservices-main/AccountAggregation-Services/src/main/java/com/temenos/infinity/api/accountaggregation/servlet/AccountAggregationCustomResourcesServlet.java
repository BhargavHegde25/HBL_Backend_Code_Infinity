package com.temenos.infinity.api.accountaggregation.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.dbp.core.api.APIImplementationTypes;
import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.servlet.IntegrationCustomServlet;
import com.temenos.infinity.api.accountaggregation.constant.MSCertificateConstants;
import com.temenos.infinity.api.accountaggregation.mapper.AccountAggregationBackendDelegateMapper;
import com.temenos.infinity.api.accountaggregation.mapper.AccountAggregationBusinessDelegateMapper;
import com.temenos.infinity.api.accountaggregation.mapper.AccountAggregationResourcesMapper;
import com.temenos.infinity.api.accountaggregation.utils.AccountAggregationUtils;
import com.temenos.infinity.transact.tokenmanager.config.BackendCertificateStore;
import com.temenos.infinity.transact.tokenmanager.dto.BackendCertificate;

/**
 * Custom servlet used to intialize/destroy resources required by this module
 * 
 * @author Aditya Mankal
 *
 */
@IntegrationCustomServlet(servletName = "AccountAggregationCustomResourcesServlet", urlPatterns = {
        "AccountAggregationCustomResourcesServlet" })
public class AccountAggregationCustomResourcesServlet extends HttpServlet {

    private static final long serialVersionUID = 7467307922351824473L;
    
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public void init() throws ServletException {
        // Register Resource Delegates
        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(ResourceFactory.class)
                .registerResourceMappings(new AccountAggregationResourcesMapper(), APIImplementationTypes.BASE);

        // Register Business Delegates
        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
                .registerBusinessDelegateMappings(new AccountAggregationBusinessDelegateMapper(),
                        APIImplementationTypes.BASE);

        // Register Backend Delegates
        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BackendDelegateFactory.class)
                .registerBackendDelegateMappings(new AccountAggregationBackendDelegateMapper(),
                        APIImplementationTypes.BASE);
        
        try {
            // Read from the persistent store and construct the instance of BackendCertificate
            BackendCertificate backendCertificate = AccountAggregationUtils.getCertFromDB(MSCertificateConstants.BACKENDCERTNAME);

            // Register backend-certificate
            BackendCertificateStore.registerBackendCertificate(MSCertificateConstants.BACKEND, backendCertificate);
            }catch (Exception e) {
    				alert.prepareError("Unable to fetch and register Backend Certificate from DB").log();
    		}
    }

    @Override
    public void destroy() {
    }

}
