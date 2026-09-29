package com.kony.utilities;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.konylabs.middleware.servlet.IntegrationCustomServlet;

@IntegrationCustomServlet(urlPatterns = { "dummy" })
public class StopServlet extends HttpServlet {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public void init() throws ServletException {
		/*
		 * Workaround. Loading the following referenced custom classes in this method to
		 * avoid java.lang.NoClassDefFoundError on custom classes when destroy() is
		 * called on app un-publish.
		 */
		diagnostic.prepareDebug("Invoked init from AlertEngine").log();
		loadCustomClasses();
	}

	@Override
	public void destroy() {
		diagnostic.prepareDebug("Invoked destroy from AlertEngine").log();
		HikariConfiguration.closeDatasource();
	}

	public void loadCustomClasses() {
		// Load Custom Classes
	}
}
