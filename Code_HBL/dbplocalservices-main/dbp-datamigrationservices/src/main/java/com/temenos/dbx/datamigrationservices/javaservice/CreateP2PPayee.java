package com.temenos.dbx.datamigrationservices.javaservice;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.datamigrationservices.resource.api.MigratePayeesResource;

public class CreateP2PPayee implements JavaService2 {

	private static final Logger LOG = LogManager.getLogger(CreateP2PPayee.class);

	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {

		Result result = new Result();
		Result newResult = new Result();

		try {
			MigratePayeesResource payeeResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(MigratePayeesResource.class);
			
			newResult = payeeResource.createP2PPayee(methodID, inputArray, request, response);
			if(newResult.hasParamByName("id") && !newResult.hasParamByName("dbpErrCode")) {
				result.addParam(new Param("PayPersonId", newResult.getParamValueByName("id")));
			}else {
				result = newResult;
			}
			
		} catch (ApplicationException e) {
			LOG.error("Exception occured while fetching user contract and contract customer details" + e.getMessage());
			e.getErrorCodeEnum().setErrorCode(result);
		} catch (Exception e) {
			LOG.error("Error occured while invoking createPayee: ", e);
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}

		return result;
	}

	
}
