package com.kony.dbp.batchprocessingenginedbutils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;

import com.konylabs.middleware.servlet.IntegrationCustomServlet;

@IntegrationCustomServlet(urlPatterns = { "dummy" })
public class StopServlet extends HttpServlet {
	private static final long serialVersionUID = 792938477529485739L;
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");

	@Override
	public void init() throws ServletException {
		/*
		 * Workaround. Loading the following referenced custom classes in this method to
		 * avoid java.lang.NoClassDefFoundError on custom classes when destroy() is
		 * called on app un-publish.
		 */
		diagnostic.prepareDebug("Invoked init from batch processing alerts").log();
		loadCustomClasses();

	}

	@Override
	public void destroy() {
		diagnostic.prepareDebug("Invoked destroy from batch processing alerts").log();

	}

	public void loadCustomClasses() {
		// Load custom classes
	}
}
