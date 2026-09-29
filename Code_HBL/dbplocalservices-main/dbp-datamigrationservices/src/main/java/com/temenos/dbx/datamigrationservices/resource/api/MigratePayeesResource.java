package com.temenos.dbx.datamigrationservices.resource.api;

import com.dbp.core.api.Resource;
import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface MigratePayeesResource extends Resource {

	Result createP2PPayee(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws ApplicationException;

	Result createBillPayPayee(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws ApplicationException;

}
