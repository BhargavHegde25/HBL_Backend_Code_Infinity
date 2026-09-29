package com.temenos.dbx.consents.servlet;

import java.io.IOException;

import com.konylabs.middleware.servlet.IntegrationCustomServlet;
import com.temenos.dbx.consents.javaservice.GetUrl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

@IntegrationCustomServlet(servletName = "ConsentsManagementCustomResourceServlet", urlPatterns = {
"ConsentsManagementCustomResourceServlet" })
public class ConsentsManagementCustomResourceServlet extends HttpServlet {

	private static final long serialVersionUID = -2023367655414920000L;
	public void init() throws ServletException {
	
		GetUrl get=new GetUrl();
		get.loadProperties();
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}
