package com.temenos.infinity.api.loanspayoff.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.dbp.core.api.APIImplementationTypes;
import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.servlet.IntegrationCustomServlet;
import com.temenos.infinity.api.loanspayoff.constants.T24CertificateConstants;
import com.temenos.infinity.api.loanspayoff.mapper.LoansPayoffBackendDelegateMapper;
import com.temenos.infinity.api.loanspayoff.mapper.LoansPayoffBusinessDelegateMapper;
import com.temenos.infinity.api.loanspayoff.mapper.LoansPayoffResourceMapper;
import com.temenos.infinity.api.loanspayoff.utils.LoansPayOffUtils;
import com.temenos.infinity.transact.tokenmanager.config.BackendCertificateStore;
import com.temenos.infinity.transact.tokenmanager.dto.BackendCertificate;

/**
 * Custom servlet used to intialize/destroy resources required by this module
 * 
 * @author Aditya Mankal
 *
 */
@IntegrationCustomServlet(servletName = "LoansPayoffCustomResourcesServlet", urlPatterns = {
        "LoansPayofCustomResourcesServlet" })
public class LoansPayoffCustomResourcesServlet extends HttpServlet {

    /**
     * 
     */
    private static final long serialVersionUID = -2023367655414960390L;

    @Override
    public void init() throws ServletException {

        // Register Resource Delegates
        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(ResourceFactory.class)
                .registerResourceMappings(new LoansPayoffResourceMapper(), APIImplementationTypes.BASE);

        // Register Business Delegates
        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
                .registerBusinessDelegateMappings(new LoansPayoffBusinessDelegateMapper(), APIImplementationTypes.BASE);

        // Register Backend Delegates
        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BackendDelegateFactory.class)
                .registerBackendDelegateMappings(new LoansPayoffBackendDelegateMapper(), APIImplementationTypes.BASE);

        // Read from the persistent store and construct the instance of BackendCertificate
        BackendCertificate backendCertificate = LoansPayOffUtils.getCertFromDB("T24");

        // Register backend-certificate
        BackendCertificateStore.registerBackendCertificate(T24CertificateConstants.BACKEND, backendCertificate);
    }

    @Override
    public void destroy() {
    }

}