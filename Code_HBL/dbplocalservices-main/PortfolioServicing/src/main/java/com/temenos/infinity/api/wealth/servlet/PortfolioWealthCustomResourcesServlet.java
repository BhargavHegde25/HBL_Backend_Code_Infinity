package com.temenos.infinity.api.wealth.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.dbp.core.api.APIImplementationTypes;
import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.servlet.IntegrationCustomServlet;
import com.temenos.infinity.api.wealthservices.constants.T24CertificateConstants;
import com.temenos.infinity.api.wealth.mapper.PortfolioWealthBackendDelegateMapper;
import com.temenos.infinity.api.wealth.mapper.PortfolioWealthBusinessDelegateMapper;
import com.temenos.infinity.api.wealth.mapper.PortfolioWealthResourceMapper;
import com.temenos.infinity.api.wealthservices.util.PortfolioWealthUtils;
import com.temenos.infinity.transact.tokenmanager.config.BackendCertificateStore;
import com.temenos.infinity.transact.tokenmanager.dto.BackendCertificate;

@IntegrationCustomServlet(servletName = "PortfolioWealthCustomResourcesServlet", urlPatterns = {
"PortfolioWealthCustomResourcesServlet" })
public class PortfolioWealthCustomResourcesServlet extends HttpServlet {

	private static final long serialVersionUID = 7467307922351886973L;
	 @Override
	    public void init() throws ServletException {
	        // Register Resource Delegates
	        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(ResourceFactory.class)
	                .registerResourceMappings(new PortfolioWealthResourceMapper(), APIImplementationTypes.BASE);

	        // Register Business Delegates
	        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
	                .registerBusinessDelegateMappings(new PortfolioWealthBusinessDelegateMapper(),
	                        APIImplementationTypes.BASE);

	        // Register Backend Delegates
	        DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BackendDelegateFactory.class)
	                .registerBackendDelegateMappings(new PortfolioWealthBackendDelegateMapper(),
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
