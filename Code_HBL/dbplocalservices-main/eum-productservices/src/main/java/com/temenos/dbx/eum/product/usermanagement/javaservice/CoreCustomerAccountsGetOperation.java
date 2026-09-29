package com.temenos.dbx.eum.product.usermanagement.javaservice;


import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;


import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;

import com.kony.dbp.exception.ApplicationException;

import com.kony.dbputilities.util.ErrorCodeEnum;

import com.konylabs.middleware.common.JavaService2;

import com.konylabs.middleware.controller.DataControllerRequest;

import com.konylabs.middleware.controller.DataControllerResponse;

import com.konylabs.middleware.dataobject.Result;

import com.temenos.dbx.eum.product.usermanagement.resource.api.InfinityUserManagementResource;


public class CoreCustomerAccountsGetOperation implements JavaService2  {


	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	@Override

	public Object invoke(String methodId, Object[] inputArray,

			DataControllerRequest request,

			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();

		Result result = new Result();

		

		try {

			InfinityUserManagementResource resource =

                    DBPAPIAbstractFactoryImpl.getResource(InfinityUserManagementResource.class);

			result = resource.getCoreCustomerAccounts(methodId, inputArray, request, response);

		} catch (ApplicationException e) {

            e.getErrorCodeEnum().setErrorCode(result);

            alert.prepareError("Exception occured while fetching the core customer accounts ", e).log();

        } catch (Exception e) {

        	alert.prepareError("Exception occured while fetching the core customer accounts " , e).log();

            ErrorCodeEnum.ERR_10762.setErrorCode(result);

        }

		return result;

	}

}
