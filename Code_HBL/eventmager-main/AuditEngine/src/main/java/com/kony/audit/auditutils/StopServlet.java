package com.kony.audit.auditutils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.konylabs.middleware.servlet.IntegrationCustomServlet;

@IntegrationCustomServlet(urlPatterns = { "dummy" })
public class StopServlet extends HttpServlet {

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
		//to do
	}
}
