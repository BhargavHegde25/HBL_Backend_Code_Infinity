package com.temenos.infinity.tradefinanceservices.servlet;

import com.dbp.core.api.APIImplementationTypes;
import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.servlet.IntegrationCustomServlet;
import com.temenos.infinity.tradefinanceservices.config.TradeFinanceUtils;
import com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants;
import com.temenos.infinity.tradefinanceservices.mapper.TradeFinanceBackendDelegateMapper;
import com.temenos.infinity.tradefinanceservices.mapper.TradeFinanceBusinessDelegateMapper;
import com.temenos.infinity.tradefinanceservices.mapper.TradeFinanceResourceMapper;
import com.temenos.infinity.tradefinanceservices.utils.TradeFinanceHelper;
import com.temenos.infinity.transact.tokenmanager.config.BackendCertificateStore;
import com.temenos.infinity.transact.tokenmanager.dto.BackendCertificate;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;


/**
 *
 */
@IntegrationCustomServlet(servletName = "TradeFinanceCustomResourcesServlet", urlPatterns = {
        "TradeFinanceCustomResourcesServlet" })
public class TradeFinanceCustomResourcesServlet extends HttpServlet {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    /**
     * 
     */
    private static final long serialVersionUID = -2023367655414960390L; 

    @Override
    public void init() throws ServletException {

        // Register Resource Delegates
        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(ResourceFactory.class)
                .registerResourceMappings(new TradeFinanceResourceMapper(), APIImplementationTypes.BASE);

        // Register Business Delegates
        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
                .registerBusinessDelegateMappings(new TradeFinanceBusinessDelegateMapper(), APIImplementationTypes.BASE);

        // Register Backend Delegates
        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BackendDelegateFactory.class)
                .registerBackendDelegateMappings(new TradeFinanceBackendDelegateMapper(), APIImplementationTypes.BASE);
        
        try {
            // Read from the persistent store and construct the instance of BackendCertificate
            BackendCertificate backendCertificate = TradeFinanceUtils.getCertFromDB(TradeFinanceConstants.BACKENDCERTNAME);

            // Register backend-certificate
            BackendCertificateStore.registerBackendCertificate(TradeFinanceConstants.DMS_BACKEND, backendCertificate);
        } catch (Exception e) {
            alert.prepareError("Unable to fetch and register Backend Certificate from DB").log();
        }
        TradeFinanceHelper helper = new TradeFinanceHelper();
        helper.loadTypeAndSubType();
    }

    @Override
    public void destroy() {
    }

}