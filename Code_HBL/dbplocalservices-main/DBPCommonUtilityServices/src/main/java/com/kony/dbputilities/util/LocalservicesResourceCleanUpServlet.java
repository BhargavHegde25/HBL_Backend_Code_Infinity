package com.kony.dbputilities.util;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.kony.dbputilities.dbutil.DBManager;
import com.konylabs.middleware.servlet.IntegrationCustomServlet;

@IntegrationCustomServlet(urlPatterns = {
        "/LocalServicesResourceCleanUpServlet" }, servletName = "LocalServicesResourceCleanUpServlet")
public class LocalservicesResourceCleanUpServlet extends HttpServlet {

    private static final long serialVersionUID = 3445805586916322021L;

    @Override
    public void init() throws ServletException {
    }

    @Override
    public void destroy() {
        DBManager.closeDataSource();

    }
}
