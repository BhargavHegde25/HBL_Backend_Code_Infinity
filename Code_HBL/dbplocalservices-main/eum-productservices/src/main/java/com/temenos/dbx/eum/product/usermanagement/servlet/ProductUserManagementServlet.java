package com.temenos.dbx.eum.product.usermanagement.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.api.APIImplementationTypes;
import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.servlet.IntegrationCustomServlet;
import com.temenos.dbx.eum.product.usermanagement.mapper.UserManagementBackendDelegateMapper;
import com.temenos.dbx.eum.product.usermanagement.mapper.UserManagementResourceMapper;
import com.temenos.dbx.product.integration.IntegrationMappings;
import com.temenos.dbx.product.utils.ThreadExecutor;

@IntegrationCustomServlet(servletName = "ProductExternalUserManagementServlet", urlPatterns = {
		"ProductExternalUserManagementServlet" })
public class ProductUserManagementServlet extends HttpServlet {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	private static final long serialVersionUID = -7996896027215639726L;

	public void init() throws ServletException {
		String integrationName = IntegrationMappings.getInstance().getIntegrationName();
			register();
	}

	public void register() {
		DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(ResourceFactory.class)
				.registerResourceMappings(new UserManagementResourceMapper(), APIImplementationTypes.BASE);
		DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BackendDelegateFactory.class)
				.registerBackendDelegateMappings(new UserManagementBackendDelegateMapper(),
						APIImplementationTypes.BASE);
	}

	public void destroy() {
		try {
			ThreadExecutor.getExecutor().shutdownExecutor();
		} catch (Exception exception) {
		}
	}
}
