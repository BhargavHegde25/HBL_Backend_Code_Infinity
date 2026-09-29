package com.dbp.batchprocessengine.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;


import com.konylabs.middleware.servlet.IntegrationCustomServlet;

@IntegrationCustomServlet(servletName = "BusinessAPIsBaseCustomResourcesServlet", urlPatterns = {
		"BusinessAPIsBaseCustomResourcesServlet" })
public class BusinessAPIsBaseCustomResourcesServlet extends HttpServlet {

	private static final long serialVersionUID = 5258692671194055192L;

	@Override
	public void init() throws ServletException {
		// registering base resources and business delegates
	}

	@Override
	public void destroy() {

	}

}
