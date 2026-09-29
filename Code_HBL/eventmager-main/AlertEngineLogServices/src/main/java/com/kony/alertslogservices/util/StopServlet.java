package com.kony.alertslogservices.util;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;


import com.kony.alertslogservices.dbutils.AlertsLogDataSourceHandler;
import com.konylabs.middleware.servlet.IntegrationCustomServlet;

@IntegrationCustomServlet(urlPatterns = { "dummy" })
public class StopServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");

	@Override
	public void init() throws ServletException {
		/*
		 * Workaround. Loading the following referenced custom classes in this method to
		 * avoid java.lang.NoClassDefFoundError on custom classes when destroy() is
		 * called on app un-publish.
		 */
	}

	@Override
	public void destroy() {
		diagnostic.prepareDebug("Invoked destroy from AlertEnginelogservices").log();
		AlertsLogDataSourceHandler.closeLogDataSource();
	}

}
