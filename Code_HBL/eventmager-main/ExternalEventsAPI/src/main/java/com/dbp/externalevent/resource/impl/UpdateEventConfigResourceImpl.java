package com.dbp.externalevent.resource.impl;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.externalevent.businessdelegate.api.UpdateEventConfigBusinessDelegate;
import com.dbp.externalevent.resource.api.UpdateEventConfigResource;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class UpdateEventConfigResourceImpl implements UpdateEventConfigResource {
	@Override
	public Result updateEventConfigData() {
		Result res = new Result();
		UpdateEventConfigBusinessDelegate updateeventconfigBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(UpdateEventConfigBusinessDelegate.class);
		boolean flag = updateeventconfigBusinessDelegate.updateEventConfigData();
		if (flag)
			res.addParam(new Param("success", "true", "String"));
		else {
			res.addParam(new Param("success", "false", "String"));
			res.addParam(new Param("dbpErrMsg", "Error in updaing event configuration", "String"));
		}
		return res;
	}

}
