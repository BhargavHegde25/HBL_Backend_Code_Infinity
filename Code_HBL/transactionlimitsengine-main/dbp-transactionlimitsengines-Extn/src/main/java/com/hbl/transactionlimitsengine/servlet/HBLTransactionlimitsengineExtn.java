package com.hbl.transactionlimitsengine.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.dbp.core.api.APIImplementationTypes;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.hbl.transactionlimitsengine.mapper.HBLBusinessDeligateMapper;
import com.hbl.transactionlimitsengine.mapper.HBLResourceMapper;
import com.konylabs.middleware.servlet.IntegrationCustomServlet;
@IntegrationCustomServlet(servletName = "TransactionlimitsengineExtn", urlPatterns = { "TransactionlimitsengineExtn" })
public class HBLTransactionlimitsengineExtn extends HttpServlet{
	
		private static final long serialVersionUID = 1L;

		public void init() throws ServletException {

			DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(ResourceFactory.class)
					.registerResourceMappings(new HBLResourceMapper(), APIImplementationTypes.EXTENSION);
			DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
			.registerBusinessDelegateMappings(new HBLBusinessDeligateMapper(), APIImplementationTypes.EXTENSION);

			/*DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BackendDelegateFactory.class)
			.registerBackendDelegateMappings(new HBLBackendDelegateMapper(), APIImplementationTypes.EXTENSION);
			*/

	}
		@Override
		public void destroy() {
			
		}
	}

