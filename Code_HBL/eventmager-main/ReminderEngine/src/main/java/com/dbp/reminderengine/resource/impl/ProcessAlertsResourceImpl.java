package com.dbp.reminderengine.resource.impl;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.reminderengine.businessdelegate.api.ProcessAlertsBusinessDelegate;
import com.dbp.reminderengine.resource.api.ProcessAlertsResource;
import com.dbp.reminderengine.utils.Constants;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.objectserviceutils.EventsDispatcher;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class ProcessAlertsResourceImpl implements ProcessAlertsResource {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");

	@Override
	public Result getCustomers(DataControllerRequest request) {
		Result result = new Result();
		try {
			ProcessAlertsBusinessDelegate alertsBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(BusinessDelegateFactory.class)
					.getBusinessDelegate(ProcessAlertsBusinessDelegate.class);
			JsonArray customerDetailsObj = alertsBusinessDelegate
					.getCustomerDetails(request.getParameter("CustomerDetails"));
			diagnostic.prepareDebug("customerDetailsObj " + customerDetailsObj).log();
			for (JsonElement customerObj : customerDetailsObj) {
				if (customerObj.isJsonObject()) {
					JsonObject customer = customerObj.getAsJsonObject();

					EventsDispatcher.dispatch(request, null, customer.get("alertTypeId").getAsString(),
							customer.get("alertSubTypeId").getAsString(), "", "SID_EVENT_SUCCESS",
							customer.get("accountId").getAsString(), customer.get("customerId").getAsString(),
							Constants.APPID, customer.get("customparams").getAsJsonObject());

				}

			}
		} catch (Exception e) {
			alert.prepareError(e.toString()).log();
			result.addParam(new Param("success", "false", "String"));
			return result;

		}
		result.addParam(new Param("success", "true", "String"));
		return result;

	}

}
