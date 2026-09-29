package com.temenos.dbx.product.usermanagement.servlet;

import com.dbp.core.api.APIImplementationTypes;
import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.servlet.IntegrationCustomServlet;
import com.temenos.dbx.product.integration.IntegrationMappings;
import com.temenos.dbx.product.usermanagement.mapper.UserManagementBackendDelegateMapper;
import com.temenos.dbx.product.usermanagement.mapper.UserManagementBusinessDelegateMapper;
import com.temenos.dbx.product.usermanagement.mapper.UserManagementResourceMapper;
import com.temenos.dbx.product.usermanagement.resource.api.ProfileManagementResource;
import com.temenos.dbx.product.utils.ThreadExecutor;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

@IntegrationCustomServlet(servletName = "DBXUserCustomerResourcesServlet", urlPatterns = {"DBXUserCustomerResourcesServlet"})
public class ProductUserManagementServlet extends HttpServlet {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
  
  private static final long serialVersionUID = -7996896027215639726L;
  
  public void init() throws ServletException {
    register();
  }
  
  public static void register() {
    alert.prepareError("inside register").log();
    ((ResourceFactory)DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(ResourceFactory.class))
      .registerResourceMappings((DBPAPIMapper)new UserManagementResourceMapper(), APIImplementationTypes.BASE);
    ((BusinessDelegateFactory)DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class))
      .registerBusinessDelegateMappings((DBPAPIMapper)new UserManagementBusinessDelegateMapper(), APIImplementationTypes.BASE);
    ((BackendDelegateFactory)DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BackendDelegateFactory.class))
      .registerBackendDelegateMappings((DBPAPIMapper)new UserManagementBackendDelegateMapper(), APIImplementationTypes.BASE);
  }
  
  public void destroy() {
    try {
      ThreadExecutor.getExecutor().shutdownExecutor();
    } catch (Exception exception) {}
  }
}
