package com.temenos.infinity.api.wealthOrder.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.dbp.core.api.APIImplementationTypes;
import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.servlet.IntegrationCustomServlet;
import com.temenos.infinity.api.wealthservices.constants.T24CertificateConstants;
import com.temenos.infinity.api.wealthOrder.mapper.WealthBackendDelegateMapper;
import com.temenos.infinity.api.wealthOrder.mapper.WealthBusinessDelegateMapper;
import com.temenos.infinity.api.wealthOrder.mapper.WealthResourceMapper;
import com.temenos.infinity.api.wealthservices.util.PortfolioWealthUtils;
import com.temenos.infinity.transact.tokenmanager.config.BackendCertificateStore;
import com.temenos.infinity.transact.tokenmanager.dto.BackendCertificate;

@IntegrationCustomServlet(servletName = "WealthCustomResourcesServlet", urlPatterns = {
"WealthCustomResourcesServlet" })
public class WealthCustomResourcesServlet extends HttpServlet {

	private static final long serialVersionUID = 7467307922351824473L;
	 @Override
	    public void init() throws ServletException {
	        // Register Resource Delegates
	        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(ResourceFactory.class)
	                .registerResourceMappings(new WealthResourceMapper(), APIImplementationTypes.BASE);

	        // Register Business Delegates
	        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
	                .registerBusinessDelegateMappings(new WealthBusinessDelegateMapper(),
	                        APIImplementationTypes.BASE);

	        // Register Backend Delegates
	        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BackendDelegateFactory.class)
	                .registerBackendDelegateMappings(new WealthBackendDelegateMapper(),
	                        APIImplementationTypes.BASE);
	        
	     // Read from the persistent store and construct the instance of BackendCertificate
	        BackendCertificate backendCertificate = PortfolioWealthUtils.getCertFromDB("T24");

	        // Register backend-certificate
	        BackendCertificateStore.registerBackendCertificate(T24CertificateConstants.BACKEND, backendCertificate);
	    }

	    @Override
	    public void destroy() {
	    }
}
