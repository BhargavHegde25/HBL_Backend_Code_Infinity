package com.kony.adminconsole.licensing.resource.impl;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.error.DBPApplicationException;
import com.kony.adminconsole.licensing.businessdelegate.api.LicensingBusinessDelegate;
import com.kony.adminconsole.licensing.resource.api.LicensingResource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class LicensingResourceImpl implements LicensingResource {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");

	/*
	 * Method to get the count of users - External Users, Internal Users and
	 * Prospect Users
	 */

	@Override
	public Result getCountOfUsers(String method, Object[] inputArray, DataControllerRequest dataControllerRequest,
			DataControllerResponse dataControllerResponse) throws DBPApplicationException {

		Result resultExternalUsers = new Result();
		Result resultInternalUsers = new Result();

		Result result = new Result();
		try {
			LicensingBusinessDelegate licensingBD = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(LicensingBusinessDelegate.class);

			resultInternalUsers = licensingBD.getInternalUsersCount();
			resultExternalUsers = licensingBD.getExternalUsersCount();

			result.appendResult(resultInternalUsers);
			result.appendResult(resultExternalUsers);

			return result;
		} catch (Exception e) {
			alert.prepareError("Exception occured while invoking getCountOfUsers : ", e).log();

			return result;

		}
	}

}
