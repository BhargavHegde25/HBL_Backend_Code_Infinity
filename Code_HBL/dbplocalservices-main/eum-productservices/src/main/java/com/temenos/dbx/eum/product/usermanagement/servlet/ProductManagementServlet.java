package com.temenos.dbx.eum.product.usermanagement.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.api.APIImplementationTypes;
import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.servlet.IntegrationCustomServlet;
import com.temenos.dbx.eum.product.usermanagement.mapper.ProductBackendDelegateMapper;
import com.temenos.dbx.eum.product.usermanagement.mapper.ProductResourceMapper;
import com.temenos.dbx.eum.product.usermanagement.mapper.UserManagementBusinessDelegateMapper;
import com.temenos.dbx.product.utils.ThreadExecutor;

@IntegrationCustomServlet(servletName = "ProductManagementServlet", urlPatterns = {
		"ProductManagementServlet" })
public class ProductManagementServlet extends HttpServlet {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	private static final long serialVersionUID = -7996896027215639726L;

	public void init() throws ServletException {
		registerCommonImplementations();
	}

	public void registerCommonImplementations() {
		DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(ResourceFactory.class)
				.registerResourceMappings(new ProductResourceMapper(), APIImplementationTypes.BASE);
		DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
				.registerBusinessDelegateMappings(new UserManagementBusinessDelegateMapper(),
						APIImplementationTypes.BASE);
		DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BackendDelegateFactory.class)
				.registerBackendDelegateMappings(new ProductBackendDelegateMapper(),
						APIImplementationTypes.BASE);

	}

	public void destroy() {
		try {
			ThreadExecutor.getExecutor().shutdownExecutor();
		} catch (Exception exception) {
		}
	}
}
