package com.dbp.reminderengine.resource.impl;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.reminderengine.businessdelegate.api.CustomerDetailsBusinessDelegate;
import com.dbp.reminderengine.resource.api.CustomerDetailsResource;
import com.dbp.reminderengine.utils.HelperMethods;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;

public class CustomerDetailsResourceImpl implements CustomerDetailsResource {
	private static String schemaname = null;

	@Override
	public Result getCustomers(DataControllerRequest request) {

		if (schemaname == null)
			try {
				schemaname = HelperMethods.getConfigProperty("DBX_SCHEMA_NAME");
			} catch (Exception e1) {
				schemaname = "dbxdb";
			}

		CustomerDetailsBusinessDelegate subscriberBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(CustomerDetailsBusinessDelegate.class);
		return JSONToResult.convert(subscriberBusinessDelegate.getCustomerDetails().toString());

	}

	public static String getSchemaName() {
		return schemaname;
	}
}
